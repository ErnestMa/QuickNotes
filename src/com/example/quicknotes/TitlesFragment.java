package com.example.quicknotes;

import java.util.List;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TitlesFragment extends ListFragment {
    OnHeadlineSelectedListener mCallback;
    
    private List<String> noteList;
    private ArrayAdapter<String> adapter;
    
    // The container Activity must implement this interface so the frag can deliver messages
    public interface OnHeadlineSelectedListener {
        /** Called by HeadlinesFragment when a list item is selected */
        public void onArticleSelected(int position);
        public void onArticleDelete(int position);
        public void onArticleViewSelected(int position);
        public void onArticleEditSelected(int position);
    }

    public List<String> getNoteList(){
    	return noteList;
    }
    
    public void setNoteList(List<String> noteList){
    	this.noteList = noteList;
    }

    public void refreshListFragment(){
    	this.adapter.clear();
    	this.adapter.addAll(noteList);
		this.adapter.notifyDataSetChanged();
    }
    
 //  http://www.java2s.com/Code/Android/UI/Demonstrationofdisplayingacontextmenufromafragment.htm
 //  http://saigeethamn.blogspot.com/2011/05/options-menu-android-developer-tutorial.html
    
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(Menu.NONE, R.id.edit, Menu.NONE, "Edit");
        menu.add(Menu.NONE, R.id.view, Menu.NONE, "View");
        menu.add(Menu.NONE, R.id.delete, Menu.NONE, "Delete");
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//        int id = info.targetView.getId();
        int position = info.position;
        
        switch (item.getItemId()) {
            case R.id.edit:
            	mCallback.onArticleEditSelected(position);
                return true;
            case R.id.view:
                mCallback.onArticleViewSelected(position);
                return true;
            case R.id.delete:
            	mCallback.onArticleDelete(position);
                return true;
        }
        return super.onContextItemSelected(item);
    }
    
        
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        
        // We need to use a different list item layout for devices older than Honeycomb
        int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;

        this.adapter = new ArrayAdapter<String>(getActivity(),
        		layout, this.getNoteList());
        
        setListAdapter(this.adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        // When in two-pane layout, set the listview to highlight the selected list item
        // (We do this during onStart because at the point the listview is available.)
        if (getFragmentManager().findFragmentById(R.id.notes_fragment) != null) {
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
        registerForContextMenu(getListView());
    }
    
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (OnHeadlineSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Notify the parent activity of selected item
        mCallback.onArticleSelected(position);
        
        // Set the item as checked to be highlighted when in two-pane layout
        getListView().setItemChecked(position, true);
    }
}
