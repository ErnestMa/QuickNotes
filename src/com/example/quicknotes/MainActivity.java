package com.example.quicknotes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity 
	implements TitlesFragment.OnHeadlineSelectedListener {
	
    public final static String EXTRA_PACKAGE = "com.example.quicknotes.package";

	private QuickNotesDataSource datasource;
	private TitlesFragment firstFragment;
	private List<Note> noteLists;

	  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notes_list);

		// Check whether the activity is using the layout version with
		// the fragment_container FrameLayout. If so, we must add the first fragment
		if (findViewById(R.id.fragment_container) != null) {

		    // However, if we're being restored from a previous state,
		    // then we don't need to do anything and should return or else
		    // we could end up with overlapping fragments.
		    if (savedInstanceState != null) {
		        return;
		    }
		    
		    datasource = new QuickNotesDataSource(this);
		    datasource.open();

		    // Create an instance of ExampleFragment
		    this.firstFragment = new TitlesFragment();

		    List<String> values = this.getTitlesFromAllNotes();
		    this.firstFragment.setNoteList(values);
		    
		    // In case this activity was started with special instructions from an Intent,
		    // pass the Intent's extras to the fragment as arguments
		    this.firstFragment.setArguments(getIntent().getExtras());

		    // Add the fragment to the 'fragment_container' FrameLayout
		    getSupportFragmentManager().beginTransaction()
		            .add(R.id.fragment_container, this.firstFragment).commit();
		}
	}
	
	protected void setNoteLists(List<Note> notesList){
		this.noteLists = notesList;
	}
	
	protected Note getNoteContent(int position){
		return this.noteLists.get(position);
	}
	
	
	@Override
	protected void onResume() {
      super.onResume();
	  datasource.open();
      List<String> values = this.getTitlesFromAllNotes();
	  this.firstFragment.setNoteList(values);
	  this.firstFragment.refreshListFragment();
	}
	
	protected List<String> getTitlesFromAllNotes(){
	    List<Note> values = datasource.getAllNotes();
	    this.setNoteLists(values);

	    List<String> titles = new ArrayList<String>();
		for (int i = 0; i < values.size(); i++) {
			Note tmp = values.get(i);
			titles.add(tmp.getTitle());
		}	    

		return titles;
	}
	

	@Override
	protected void onPause() {
	    datasource.close();
	    super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_new:
	            openNewActivity();
	            return true;
	        case R.id.action_collection:
	            openCollections();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	
	public void onArticleSelected(int position) {
		// The user selected the headline of an article from the HeadlinesFragment

		// Capture the article fragment from the activity layout
		NoteFragment articleFrag = (NoteFragment)
		        getSupportFragmentManager().findFragmentById(R.id.notes_fragment);

		if (articleFrag != null) {
		    // If article frag is available, we're in two-pane layout...

		    // Call a method in the ArticleFragment to update its content
		    articleFrag.updateArticleView(position);

		} else {
		    // If the frag is not available, we're in the one-pane layout and must swap frags...

		    // Create fragment and give it an argument for the selected article
			NoteFragment newFragment = new NoteFragment();
			newFragment.setCurrentNote(this.getNoteContent(position));
		    Bundle args = new Bundle();
		    args.putInt(NoteFragment.ARG_POSITION, position);
		    newFragment.setArguments(args);
		    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

		    // Replace whatever is in the fragment_container view with this fragment,
		    // and add the transaction to the back stack so the user can navigate back
		    transaction.replace(R.id.fragment_container, newFragment);
		    transaction.addToBackStack(null);

		    // Commit the transaction
		    transaction.commit();
		}
	}
	
	
	public void onArticleDelete(int position){
		Note currentNote = this.getNoteContent(position);
    	if (currentNote != null){
    		datasource.deleteNote(currentNote);
    	    List<String> values = this.getTitlesFromAllNotes();
    		this.firstFragment.setNoteList(values);
    		this.firstFragment.refreshListFragment();
    	}
    	return;
	}
	
	public void onArticleViewSelected(int position){
		Note selectedNote = this.getNoteContent(position);		
        Intent intent = new Intent(this, NoteViewActivity.class);
        intent.putExtra(EXTRA_PACKAGE, (Serializable)selectedNote);
        startActivity(intent);
	}
	
	public void onArticleEditSelected(int position){
		Note selectedNote = this.getNoteContent(position);		
        Intent intent = new Intent(this, NewNoteActivity.class);
        intent.putExtra(EXTRA_PACKAGE, (Serializable)selectedNote);
        startActivity(intent);
	}
	
	
    public void openNewActivity() {
        Intent intent = new Intent(this, NewNoteActivity.class);
        startActivity(intent);
    }

    public void openCollections() {
//        Intent intent = new Intent(MainActivity.this, ActivityC.class);
//        startActivity(intent);
    }

}
