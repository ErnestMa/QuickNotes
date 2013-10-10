//Copyright (c) 2013 ICRL
//
//See the file license.txt for copying permission.

package com.example.quicknotes;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.quicknotes.NotesOrganizerContract.NotesEntry;

//http://www.vogella.com/articles/AndroidSQLite/article.html

public class QuickNotesDataSource {
	  private SQLiteDatabase database;
	  private NotesReaderDbHelper dbHelper;
	  
	  private String[] allColumns = { 
			  NotesEntry._ID,
			  NotesEntry.COLUMN_NAME_FOLDER_ID, 
			  NotesEntry.COLUMN_NAME_NOTE_TITLE,
			  NotesEntry.COLUMN_NAME_NOTE_CONTENT,
			  NotesEntry.COLUMN_NAME_NOTE_CREATE_DATE };
	  
	  public QuickNotesDataSource(Context context) {
		   dbHelper = new NotesReaderDbHelper(context);
	  }
	  
	  public void open() throws SQLException {
		  database = dbHelper.getWritableDatabase();
	  }
	  
	  public void close() {
		  dbHelper.close();
	  }
	  
	  public Note createNote(Note note){
		// Create a new map of values, where column names are the keys
		  ContentValues values = new ContentValues();
		  values.put(NotesEntry.COLUMN_NAME_FOLDER_ID, note.getFolderId());
		  values.put(NotesEntry.COLUMN_NAME_NOTE_TITLE, note.getTitle());
		  values.put(NotesEntry.COLUMN_NAME_NOTE_CONTENT, note.getContent());
		  values.put(NotesEntry.COLUMN_NAME_NOTE_CREATE_DATE, note.getDateTime());

		  // Insert the new row, returning the primary key value of the new row
          long insertId = database.insert(
				  NotesEntry.TABLE_NAME,
				  NotesEntry.COLUMN_NAME_NULLABLE,
		           values);
          
          Cursor cursor = database.query(NotesEntry.TABLE_NAME,
        	        allColumns, NotesEntry._ID + " = " + insertId, null,
        	        null, null, null);
          cursor.moveToFirst();
          Note newNote = cursorToNote(cursor);
          cursor.close();
		  return newNote;
		  
	  }

	  public Note updateNote(Note note){
		  if (note.isEmpty() == false){
			  Note pendingNote = new Note(note);
		  
		  ContentValues values = new ContentValues();
		  values.put(NotesEntry.COLUMN_NAME_FOLDER_ID, pendingNote.getFolderId());
		  values.put(NotesEntry.COLUMN_NAME_NOTE_TITLE, pendingNote.getTitle());
		  values.put(NotesEntry.COLUMN_NAME_NOTE_CONTENT, pendingNote.getContent());
		  values.put(NotesEntry.COLUMN_NAME_NOTE_CREATE_DATE, pendingNote.getDateTime());
		  
          String selection = NotesEntry._ID + " = " + String.valueOf(pendingNote.getId());
          
          long updateId = database.update(NotesEntry.TABLE_NAME, values, selection, null);
          
          Cursor cursor = database.query(NotesEntry.TABLE_NAME,
        	        allColumns, NotesEntry._ID + " = " + updateId, null,
        	        null, null, null);
          cursor.moveToFirst();
          Note newNote = cursorToNote(cursor);
          cursor.close();
		  return newNote;
		  }else{
			  return null;
		  }
	  }
	  
	  
	  public List<Note> getAllNotes() {
		    List<Note> noteList = new ArrayList<Note>();

		    Cursor cursor = database.query(NotesEntry.TABLE_NAME,
		        allColumns, null, null, null, null, null);

		    cursor.moveToFirst();
		    while (!cursor.isAfterLast()) {
		      Note newNote = cursorToNote(cursor);
		      noteList.add(newNote);
		      cursor.moveToNext();
		    }
		    // Make sure to close the cursor
		    cursor.close();
		    return noteList;
     }
	  
	  public List<String> getAllNotesTitle() {
		    List<String> noteList = new ArrayList<String>();

		    Cursor cursor = database.query(NotesEntry.TABLE_NAME,
		        allColumns, null, null, null, null, null);

		    cursor.moveToFirst();
		    while (!cursor.isAfterLast()) {
		      noteList.add(cursor.getString(2));
		      cursor.moveToNext();
		    }
		    // Make sure to close the cursor
		    cursor.close();
		    return noteList;
   }
	  
	  public void deleteNote(Note note) {
		    long id = note.getId();
		    System.out.println("Comment deleted with id: " + id);
		    database.delete(NotesEntry.TABLE_NAME, NotesEntry._ID
		        + " = " + id, null);
		  }
	  
	  private Note cursorToNote(Cursor cursor) {
		    Note newNote = new Note();
		    newNote.setId(cursor.getLong(0));
		    newNote.setFolderId(cursor.getString(1));
		    newNote.setTitle(cursor.getString(2));
		    newNote.setContent(cursor.getString(3));
		    newNote.setDateTime(cursor.getString(4));
		    return newNote;
	 }
}
