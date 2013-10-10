package com.example.quicknotes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.quicknotes.NotesOrganizerContract.NotesEntry;

public final class NotesReaderDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "NotesReader.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES = 
       		"CREATE TABLE " + NotesEntry.TABLE_NAME + " (" +
               NotesEntry._ID + " INTEGER PRIMARY KEY," +
       	    NotesEntry.COLUMN_NAME_FOLDER_ID + TEXT_TYPE + COMMA_SEP +
       	    NotesEntry.COLUMN_NAME_NOTE_TITLE + TEXT_TYPE + COMMA_SEP +
       	    NotesEntry.COLUMN_NAME_NOTE_CONTENT + TEXT_TYPE + COMMA_SEP +
       	    NotesEntry.COLUMN_NAME_NOTE_CREATE_DATE + TEXT_TYPE +
       	    " )";
       
    private static final String SQL_DELETE_ENTRIES = 
    		   "DROP TABLE IF EXISTS" + NotesEntry.TABLE_NAME;

    public NotesReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
    
}
