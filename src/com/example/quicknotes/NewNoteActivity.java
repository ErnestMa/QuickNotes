package com.example.quicknotes;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class NewNoteActivity extends Activity{
	    private QuickNotesDataSource datasource;
	    private Note noteObject;
	
	   @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	        datasource = new QuickNotesDataSource(this);
	        datasource.open();
	        
		    Intent intent = getIntent();
		    Note attachment = (Note)intent.getSerializableExtra(MainActivity.EXTRA_PACKAGE);
		    this.setNoteObject(attachment);
	        
	        setContentView(R.layout.new_notes_activity);
	    }

	    protected void setNoteObject(Note note){
		   if (note != null){
			   this.noteObject = new Note (note);
		   }else{
			   // TO DO
		   }
	    }
	   
	    protected Note getNoteObject(){
		   return this.noteObject;
	    }
	   
	    @Override
	    protected void onStart() {
	        super.onStart();
	    }
	    
	    protected void onResume(){
	    	datasource.open();
	    	super.onResume();
	    	
		    if (noteObject != null){
		    	EditText titleTextView = (EditText) findViewById(R.id.editText1);
	        	titleTextView.setText(noteObject.getTitle());
		    
	        	EditText bodyTextView = (EditText) findViewById(R.id.editText2);
	        	bodyTextView.setText(noteObject.getContent());
	        	
	        	this.setTitle("Edit Note");
		    }else{
		    	this.setTitle("New Note");
		    }
	    }

	    protected void onPause(){
	    	datasource.close();
	    	super.onPause();
	    }

	    public void startDialog(View v) {
//	        Intent intent = new Intent(ActivityA.this, DialogActivity.class);
//	        startActivity(intent);
	    }

		/**
		 * Set up the {@link android.app.ActionBar}, if the API is available.
		 */
		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		private void setupActionBar() {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}

		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			
		    MenuInflater inflater = getMenuInflater();
		    inflater.inflate(R.menu.new_note_menu, menu);
		    
		    return super.onCreateOptionsMenu(menu);
		}

		
		public boolean onOptionsItemSelected(MenuItem item) {
		    // Handle presses on the action bar items
		    switch (item.getItemId()) {
		        case R.id.action_new_note_save:
		        	if (this.noteObject == null){
		        		saveNotes();
		        	}else{
		        		updateNotes();
		        	}
		            return true;
		        default:
		            return super.onOptionsItemSelected(item);
		    }
		}
		
		public void updateNotes(){
			
	    	EditText editText = (EditText) findViewById(R.id.editText1);
	    	EditText editText2 = (EditText) findViewById(R.id.editText2);

			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

			if (editText.isFocused()){
				imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
			}else if (editText2.isFocused()){
				imm.hideSoftInputFromWindow(editText2.getWindowToken(), 0);
			}

	    	
	    	String title = editText.getText().toString();
	    	String Content = editText2.getText().toString();
	    	
	    	this.noteObject.setTitle(title);
	    	this.noteObject.setContent(Content);
	    	
	    	if (this.noteObject.isEmpty() == false){
		    	Note saveNote = datasource.updateNote(this.noteObject);

		    	if (saveNote != null){
		            Toast.makeText(this, title + " is saved.", Toast.LENGTH_SHORT).show();
		    	}else{
		            Toast.makeText(this, title + " is fail to save.", Toast.LENGTH_SHORT).show();
		    	}
	    	}else{
	            Toast.makeText(this, "There is an error occurred in saving.", Toast.LENGTH_SHORT).show();
	    	}
	        this.onBackPressed();
		}
		
	    public void saveNotes() {
	    	Note newNote = new Note();
	    	
	    	EditText editText = (EditText) findViewById(R.id.editText1);
	    	EditText editText2 = (EditText) findViewById(R.id.editText2);
	    	
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

			if (editText.isFocused()){
				imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
			}else if (editText2.isFocused()){
				imm.hideSoftInputFromWindow(editText2.getWindowToken(), 0);
			}

	    	String title = editText.getText().toString();
	    	String Content = editText2.getText().toString();

	    	SimpleDateFormat dateFormat = 
	    			new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    	Calendar cal = Calendar.getInstance();
	    	
	    	newNote.setTitle(title);
	    	newNote.setContent(Content);
	    	newNote.setFolderId("1");
	    	newNote.setDateTime(dateFormat.format(cal.getTime()));
	    	
	    	Note saveNote = datasource.createNote(newNote);

	    	if (saveNote != null){
	    		Intent intent = new Intent(this, DialogActivity.class);
	    		startActivity(intent);
	    	}
	    }
	    	    
}
