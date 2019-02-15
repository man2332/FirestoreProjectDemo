package com.example.firestoreprojectdemo;

import com.google.firebase.firestore.Exclude;

public class Note {
    private String title;
    private String description;
    private String documentId;
    private int priority;
    

    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public Note() {
        //public no-arg constructor needed
    }

    public Note(String title, String description, int priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
