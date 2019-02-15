package com.example.firestoreprojectdemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;

import java.util.List;

public class Main2Activity extends AppCompatActivity {
    public final String TAG = "ttag";
    
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    CollectionReference collectionReference = firebaseFirestore.collection("Notes");

    DocumentSnapshot lastResultDocumentSnapshot;

    EditText titleET;
    EditText descriptionET;
    TextView viewDataTV;
    EditText priorityET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        titleET = findViewById(R.id.edit_text_title);
        descriptionET = findViewById(R.id.edit_text_description);
        viewDataTV = findViewById(R.id.text_view_data);
        priorityET = findViewById(R.id.edit_text_priority);


        //executeBatchedWrite();
        executeTransaction();
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        collectionReference.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//                if(e != null){
//                    return;
//                }
//                String text = "";
//                for(QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){
//                    Note note = queryDocumentSnapshot.toObject(Note.class);
//                    note.setDocumentId(queryDocumentSnapshot.getId());
//
//                    text += "ID: "+note.getDocumentId()+"\nTitle: "+note.getTitle()+"\nDescription: "+note.getDescription()
//                            +"\nPriority: "+note.getPriority()+"\n\n";
//                }
//                viewDataTV.setText(text);
//            }
//        });
//    }

    //updates all changes in the firestore db
//    @Override
//    protected void onStart() {
//        super.onStart();
//        collectionReference.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//                if (e != null) {
//                    return;
//                }
//                for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
//                    DocumentSnapshot documentSnapshot = documentChange.getDocument();
//                    String id = documentSnapshot.getId();
//                    int oldIndex = documentChange.getOldIndex();
//                    int newIndex = documentChange.getNewIndex();
//
//
//                    switch (documentChange.getType()) {
//                        case ADDED:
//                            viewDataTV.append("\nAdded: " + id +
//                                    "\nOld Index: " + oldIndex + "New Index: " + newIndex);
//                            break;
//                        case MODIFIED:
//                            viewDataTV.append("\nModified: " + id +
//                                    "\nOld Index: " + oldIndex + "New Index: " + newIndex);
//                            break;
//                        case REMOVED:
//                            viewDataTV.append("\nRemoved: " + id +
//                                    "\nOld Index: " + oldIndex + "New Index: " + newIndex);
//                            break;
//                    }
//                }
//            }
//        });
//    }

    public void addNote(View view) {
        String title = titleET.getText().toString();
        String description = descriptionET.getText().toString();
        int priority = Integer.parseInt(priorityET.getText().toString());

        Note note = new Note(title, description, priority);

        collectionReference.add(note);

    }

    //load notes
    public void loadNotes(View view) {
        //load notes where greater than or equal to
        collectionReference.whereGreaterThanOrEqualTo("priority", 3)
//                .whereEqualTo("title","aa")
                .orderBy("priority", Query.Direction.DESCENDING)
                .orderBy("title", Query.Direction.DESCENDING)
//                .limit(7)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                String text = "";
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                    Note note = queryDocumentSnapshot.toObject(Note.class);
                    note.setDocumentId(queryDocumentSnapshot.getId());

                    text += "ID: " + note.getDocumentId() + "\nTitle: " + note.getTitle() + "\nDescription: " + note.getDescription()
                            + "\nPriority: " + note.getPriority() + "\n\n";
                }
                viewDataTV.setText(text);
            }
        });
    }

    //load notes with OR/NOT EQUAL TO condition
    public void loadNotesTwo(View view) {
        Task task1 = collectionReference.whereGreaterThan("priority", 3).get();
        Task task2 = collectionReference.whereLessThan("priority", 2).get();

        Tasks.whenAllSuccess(task1, task2).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
            @Override
            public void onSuccess(List<Object> objects) {
                String text = "";
                List<QuerySnapshot> querySnapshots = (List<QuerySnapshot>) (List<?>) objects;
                for (QuerySnapshot querySnapshot : querySnapshots) {
                    for (QueryDocumentSnapshot queryDocumentSnapshot : querySnapshot) {
                        Note note = queryDocumentSnapshot.toObject(Note.class);
                        note.setDocumentId(queryDocumentSnapshot.getId());

                        text += "ID: " + note.getDocumentId() + "\nTitle: " + note.getTitle() + "\nDescription: " + note.getDescription()
                                + "\nPriority: " + note.getPriority() + "\n\n";
                    }
                }

                viewDataTV.setText(text);
            }
        });
    }

    //load notes with OR/NOT EQUAL TO condition
    public void loadNotesThree(View view) {
        Task task1 = collectionReference.whereGreaterThan("priority", 3).get();
        Task task2 = collectionReference.whereLessThan("priority", 2).get();
        //return all where greater than OR less than
        Task<List<QuerySnapshot>> tasks = Tasks.whenAllSuccess(task1, task2);

        tasks.addOnSuccessListener(new OnSuccessListener<List<QuerySnapshot>>() {
            @Override
            public void onSuccess(List<QuerySnapshot> querySnapshots) {
                String text = "";
                for (QuerySnapshot querySnapshot : querySnapshots) {
                    for (QueryDocumentSnapshot queryDocumentSnapshot : querySnapshot) {
                        Note note = queryDocumentSnapshot.toObject(Note.class);
                        note.setDocumentId(queryDocumentSnapshot.getId());

                        text += "ID: " + note.getDocumentId() + "\nTitle: " + note.getTitle() + "\nDescription: " + note.getDescription()
                                + "\nPriority: " + note.getPriority() + "\n\n";

                    }
                }
                viewDataTV.setText(text);
            }
        });
    }


    //PAGINATION BABY - load some, remember where we left off, then load some more when desired
    public void loadNotesFour(View view) {
        Query query;

        if (lastResultDocumentSnapshot == null) {
            query = collectionReference.orderBy("priority")
                    .limit(3);
        } else {
            query = collectionReference.orderBy("priority")
                    .startAfter(lastResultDocumentSnapshot)
                    .limit(3);
        }
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                String text = "";
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                    Note note = queryDocumentSnapshot.toObject(Note.class);
                    note.setDocumentId(queryDocumentSnapshot.getId());

                    text += "ID: " + note.getDocumentId() + "\nTitle: " + note.getTitle() + "\nDescription: " + note.getDescription()
                            + "\nPriority: " + note.getPriority() + "\n\n";
                }
                if (queryDocumentSnapshots.size() > 0) {
                    text += "_________________\n";

                    viewDataTV.append(text);

                    lastResultDocumentSnapshot = queryDocumentSnapshots.getDocuments()
                            .get(queryDocumentSnapshots.size() - 1);
                }
            }
        });

//        collectionReference.orderBy("priority")
//                .orderBy("title")
//                .startAt(5,"aac")
//                .get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        String text = "";
//                        for(QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){
//                            Note note = queryDocumentSnapshot.toObject(Note.class);
//                            note.setDocumentId(queryDocumentSnapshot.getId());
//
//                            text += "ID: "+note.getDocumentId()+"\nTitle: "+note.getTitle()+"\nDescription: "+note.getDescription()
//                                    +"\nPriority: "+note.getPriority()+"\n\n";
//                        }
//                        viewDataTV.setText(text);
//                    }
//                });
    }

    //BATCH WRITES
    public void executeBatchedWrite(){
        WriteBatch writeBatch = firebaseFirestore.batch();
        //write
        DocumentReference doc1 = collectionReference.document("New Note");
        writeBatch.set(doc1, new Note("new title1", "new desc1",1));
        //update
        DocumentReference doc2 = collectionReference.document("5ecWTNigB70zNdZV8KoW");
        writeBatch.update(doc2, "title", "description updated");

        //delete
        DocumentReference doc3 = collectionReference.document("5ecWTNigB70zNdZV8KoW");
        writeBatch.delete(doc3);

        DocumentReference doc4 = collectionReference.document();
        writeBatch.set(doc4, new Note("Added Note", "Added Note", 1));

        writeBatch.commit().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                viewDataTV.setText(e.toString());
            }
        });
    }
    //TRANSACTIONS - updates only from most updated fields
    public void executeTransaction(){
        Log.d(TAG, "executeTransaction: "+collectionReference.document("as").get());
        //check if document exist by doing .get() and checking if the fields exist in the document
        collectionReference.document("Dislikes").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d(TAG, "onSuccess: "+documentSnapshot.getString("title"));
                if(documentSnapshot.getString("title") != null) {
                    Log.d(TAG, "EXIST");
                    Task runTask = firebaseFirestore.runTransaction(new Transaction.Function<Long>() {
                        @Override
                        public Long apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                            DocumentReference documentReference = collectionReference.document("Dislikes");
                            //reading
                            DocumentSnapshot documentSnapshot = transaction.get(documentReference);
                            long newPriority = documentSnapshot.getLong("priority") + 1;

                            //writing
                            transaction.update(documentReference, "priority", newPriority);
                            return newPriority;
                        }
                    });

                    runTask.addOnSuccessListener(new OnSuccessListener<Long>() {
                        @Override
                        public void onSuccess(Long newPriority) {
                            Toast.makeText(Main2Activity.this, "NEW PRIORITY: " + newPriority, Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Log.d(TAG, "NOT EXIST");
                    collectionReference.document("Dislikes").set(new Note("dislikes", "How many Dislikes", 1));

                }
            }
        });
    }

}
