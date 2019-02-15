package com.example.firestoreprojectdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final String KEY_TITLE = "titleEditText";
    private static final String KEY_DESCRIPTION = "descriptionEditText";

    FirebaseFirestore firebaseFirestore;
    DocumentReference documentReference;


    EditText titleEditText;
    EditText descriptionEditText;

    TextView textViewData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titleEditText = findViewById(R.id.edit_text_title);
        descriptionEditText = findViewById(R.id.edit_text_description);

        textViewData = findViewById(R.id.text_view_data);

        firebaseFirestore = FirebaseFirestore.getInstance();
        documentReference = firebaseFirestore.document("Notes/First Note");
//        documentReference = firebaseFirestore.collection("Notes").document("First Note");


    }

    @Override
    protected void onStart() {
        super.onStart();
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    Log.d(TAG, "onEvent: ");
                    return;
                }
                if(documentSnapshot.exists()){
                    Map<String, Object> map = documentSnapshot.getData();

                    textViewData.setText(String.format("Title: %s Description: %s", map.get(KEY_TITLE), map.get(KEY_DESCRIPTION)));
                }else{
                    textViewData.setText("NO DOCUMENT FOUND!!!");
                }
            }
        });
    }

    public void saveNote(View view) {
        String titleValue = titleEditText.getText().toString();
        String descriptionValue = descriptionEditText.getText().toString();

        Map<String, Object> map = new HashMap<>();
        map.put(KEY_TITLE, titleValue);
        map.put(KEY_DESCRIPTION, descriptionValue);


        documentReference.set(map)
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MainActivity.this, "Added note!!!", Toast.LENGTH_SHORT).show();
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Failed to add note", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void loadNote(View view) {
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    String title = documentSnapshot.getString(KEY_TITLE);
                    String description = documentSnapshot.getString(KEY_DESCRIPTION);

                    textViewData.setText(String.format("Title: %s Description: %s", title, description));
                }else{
                    textViewData.setText("No document found!");
                }
            }
        });

    }

    public void updateDescription(View view) {
        String description = descriptionEditText.getText().toString();

        //Map<String, Object> map = new HashMap<>();
        //map.put(KEY_DESCRIPTION, description);
        //documentReference.set(map, SetOptions.merge());


        documentReference.update(KEY_DESCRIPTION, description);
    }

    public void deleteDescription(View view) {
        documentReference.update(KEY_DESCRIPTION, FieldValue.delete());

    }

    public void deleteNote(View view) {
        documentReference.delete();
    }
}



