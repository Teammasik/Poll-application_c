package com.gg.pollapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.gg.pollapp.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class Create_Candidate_Activity extends AppCompatActivity {

    //private CircleImageView candidateImg;
    private EditText candidateName,candidateParty;
    private Spinner candidateSpinner;
    private String [] candPost = {"President","Vice-President"};
    private Button submitBtn;
    private Uri mainUri = null;
    StorageReference reference ;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_candidate);

        reference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        //candidateImg = findViewById(R.id.candidate_image);
        candidateName = findViewById(R.id.candidate_name);
        candidateParty = findViewById(R.id.candidate_party_name);
        candidateSpinner = findViewById(R.id.candidate_spinner);
        submitBtn = findViewById(R.id.candidate_submit_btn);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line,candPost);
        candidateSpinner.setAdapter(adapter);



        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = candidateName.getText().toString().trim();
                String party = candidateParty.getText().toString().trim();
                String post = candidateSpinner.getSelectedItem().toString();

                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(party) && !TextUtils.isEmpty(post)) {
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    Map<String, Object> map = new HashMap<>();
                    map.put("name", name);
                    map.put("party", party);
                    map.put("post", post);
                    map.put("timestamp", FieldValue.serverTimestamp());

                    firebaseFirestore.collection("Candidate")
                            .add(map)
                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if (task.isSuccessful()) {
                                        startActivity(new Intent(Create_Candidate_Activity.this, HomeActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(Create_Candidate_Activity.this, "Data not stored", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(Create_Candidate_Activity.this, "Enter details", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

}