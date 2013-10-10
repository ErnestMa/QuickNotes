package com.example.quicknotes;

import java.io.Serializable;

public class Note implements Serializable{
   private static final long serialVersionUID = 1L;

   private long id;
   private String title;
   private String content;
   private String date_time;
   private String folder_id;
   
   public Note (){
   }
   
   public Note (Note note){
	   this.id = note.getId();
	   this.title = note.getTitle();
	   this.content = note.getContent();
	   this.date_time = note.getDateTime();
	   this.folder_id = note.getFolderId();
   }
   
   // Title Getter and Setter
   public long getId(){
	   return id;
   }
   
   public void setId(long id){
	   this.id = id;
   }

   // Title Getter and Setter
   public String getTitle(){
	   return title;
   }
   
   public void setTitle(String title){
	   this.title = title;
   }
      
   // Content Getter and Setter
   public String getContent(){
	   return content;
   }
   
   public void setContent(String content){
	   this.content = content;
   }
   
   // Date time Getter and Setter
   public String getDateTime(){
	   return date_time;
   }
   
   public void setDateTime(String dateTime){
	   this.date_time = dateTime;
   }
   
   // Folder Id Getter and Setter
   public String getFolderId(){
	   return folder_id;
   }
   
   public void setFolderId(String folder_id){
	   this.folder_id = folder_id;
   }
   
   public boolean isEmpty(){
	   if (this.title == null || this.folder_id == null || this.date_time == null || this.content == null){
		   return true;
	   }else{
		   return false;
	   }
   }
   
}
