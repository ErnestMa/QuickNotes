//Copyright (c) 2013 ICRL
//
//See the file license.txt for copying permission.

package com.example.quicknotes;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class NoteViewActivity extends Activity {
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
        
        setContentView(R.layout.note_view_edit);	    
    }
   
    protected void setNoteObject(Note note){
    	if (note != null){
    		this.noteObject = note;
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
	    	TextView titleTextView = (TextView) findViewById(R.id.note_view_title);
        	titleTextView.setText(noteObject.getTitle());
	    
        	TextView bodyTextView = (TextView) findViewById(R.id.note_view_body);
        	bodyTextView.setText(noteObject.getContent());
	    }
    }

    protected void onPause(){
    	datasource.close();
    	super.onPause();
    }

    public void startDialog(View v) {
//        Intent intent = new Intent(ActivityA.this, DialogActivity.class);
//        startActivity(intent);
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
	    inflater.inflate(R.menu.note_view_edit_menu, menu);
	    return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_new_note_save:
	            saveNotes();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
    public void saveNotes() {
    	Note newNote = new Note();
    	
    	EditText editText = (EditText) findViewById(R.id.editText1);
    	EditText editText2 = (EditText) findViewById(R.id.editText2);

    	String title = editText.getText().toString();
    	String Content = editText2.getText().toString();

    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
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
