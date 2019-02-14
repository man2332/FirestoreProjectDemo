package com.example.firestoreprojectdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;

public class Main2Activity extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    CollectionReference collectionReference = firebaseFirestore.collection("Notes");


    EditText titleET;
    EditText descriptionET;
    TextView viewDataTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        titleET = findViewById(R.id.edit_text_title);
        descriptionET = findViewById(R.id.edit_text_description);
        viewDataTV = findViewById(R.id.text_view_data);
    }

    @Override
    protected void onStart() {
        super.onStart();

        collectionReference.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    return;
                }
                String text = "";
                for(QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){
                    Note note = queryDocumentSnapshot.toObject(Note.class);
                    note.setDocumentId(queryDocumentSnapshot.getId());

                    text += "ID: "+note.getDocumentId()+"\nTITLE: "+note.getTitle()
                            +"\nDescription: "+note.getDescription()+"\n\n";
                }
                viewDataTV.setText(text);
            }
        });
    }

    public void addNote(View view) {
        String title = titleET.getText().toString();
        String description = descriptionET.getText().toString();

        Note note = new Note(title,description);

        collectionReference.add(note);
    }

    public void loadNotes(View view) {
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                String text = "";
                for(QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){
                    Note note = queryDocumentSnapshot.toObject(Note.class);
                    note.setDocumentId(queryDocumentSnapshot.getId());

                    text += "ID: "+note.getDocumentId()+"\nTitle: "+note.getTitle()+"\nDescription: "+note.getDescription()+"\n\n";
                }
                viewDataTV.setText(text);
            }
        });
    }


}
