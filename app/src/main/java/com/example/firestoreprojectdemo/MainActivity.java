package com.example.firestoreprojectdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";

    FirebaseFirestore firebaseFirestore;
    DocumentReference documentReference;


    EditText title;
    EditText description;

    TextView textViewData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = findViewById(R.id.edit_text_title);
        description = findViewById(R.id.edit_text_description);

        textViewData = findViewById(R.id.text_view_data);

        firebaseFirestore = FirebaseFirestore.getInstance();
        documentReference = firebaseFirestore.document("Notes/First Note");
    }

    public void saveNote(View view) {
        String titleValue = title.getText().toString();
        String descriptionValue = description.getText().toString();

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
                }
            }
        });

    }
}



