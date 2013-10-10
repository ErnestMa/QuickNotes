//Copyright (c) 2013 Ernest Ma
//
//See the file license.txt for copying permission.

package com.example.quicknotes;

import android.provider.BaseColumns;

public final class NotesOrganizerContract {
    public NotesOrganizerContract(){
    	
    }
    
    public static abstract class NotesEntry implements BaseColumns {
        public static final String TABLE_NAME = "quickNotes";
        public static final String COLUMN_NAME_FOLDER_ID = "folder_id";
        public static final String COLUMN_NAME_NOTE_TITLE = "note_title";
        public static final String COLUMN_NAME_NOTE_CONTENT = "note_content";
        public static final String COLUMN_NAME_NOTE_CREATE_DATE = "create_datetime";
        public static final String COLUMN_NAME_NULLABLE = null;
   }
       
}
