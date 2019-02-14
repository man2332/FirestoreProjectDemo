package com.example.firestoreprojectdemo;

import com.google.firebase.firestore.Exclude;

public class Note {
    private String title;
    private String description;
    private String documentId;

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

    public Note(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
