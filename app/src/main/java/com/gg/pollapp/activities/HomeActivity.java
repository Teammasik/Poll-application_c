package com.gg.pollapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


//import com.bumptech.glide.Glide;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import com.gg.pollapp.R;

public class HomeActivity extends AppCompatActivity {

    public static final String PREFERENCES = "prefKey";
    SharedPreferences sharedPreferences;
    public static final String IsLogIn = "islogin";
    // add these lines
    private CircleImageView circleImg;
    private TextView nameTxt, nationalIdTxt;
    private String uid;
    private FirebaseFirestore firebaseFirestore;
    private Button createBtn, voteBtn , startBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseFirestore = FirebaseFirestore.getInstance();
        uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        //circleImg = findViewById(R.id.circle_image);
        nameTxt = findViewById(R.id.name);
        //nationalIdTxt = findViewById(R.id.national_id);
        createBtn = findViewById(R.id.admin_btn);
        voteBtn = findViewById(R.id.give_vote);
        startBtn = findViewById(R.id.candidate_create_voting);

        // add these lines
        sharedPreferences = getApplicationContext().getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor pref = sharedPreferences.edit();
        pref.putBoolean(IsLogIn, true);
        pref.commit();


        firebaseFirestore.collection("Users")
                .document(uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.isSuccessful()) {

                            String name = task.getResult().getString("name");
                            //String nationalId = task.getResult().getString("nationalId");
                            //String image = task.getResult().getString("image");
                            assert name != null;

                            if (name.equals("admin")) {
                                createBtn.setVisibility(View.VISIBLE);
                                startBtn.setVisibility(View.VISIBLE);
                                voteBtn.setVisibility(View.GONE);
                            } else {
                                createBtn.setVisibility(View.GONE);
                                startBtn.setVisibility(View.GONE);
                                voteBtn.setVisibility(View.VISIBLE);
                            }

                            nameTxt.setText(name);
                            //nationalIdTxt.setText(nationalId);
                            //Glide.with(HomeActivity.this).load(image).into(circleImg);

                        } else {
                            Toast.makeText(HomeActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(HomeActivity.this, Create_Candidate_Activity.class));
            }
        });
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,AllCandidateActivity.class));
            }
        });

        voteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,AllCandidateActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        SharedPreferences.Editor pref = sharedPreferences.edit();

        if (id == R.id.show_result) {
            startActivity(new Intent(HomeActivity.this, ResultActivity.class));
            return true;
        } else if (id == R.id.log_out) {
            FirebaseAuth.getInstance().signOut();
            pref.putBoolean(IsLogIn, false);
            pref.commit();
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}