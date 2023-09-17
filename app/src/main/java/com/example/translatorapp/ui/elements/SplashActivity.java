package com.example.translatorapp.ui.elements;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import android.window.SplashScreen;

import com.example.translatorapp.R;
import com.example.translatorapp.ui.elements.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.ktx.Firebase;

public class SplashActivity extends AppCompatActivity {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

    }

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser currentUser = auth.getCurrentUser();

                if (currentUser != null) {
                    collectionReference = db.collection("Users");
                    collectionReference.document(currentUser.getUid())
                            .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                @Override
                                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                    if (error != null) {
                                        // Handle Firestore error
                                        Toast.makeText(SplashActivity.this, "Firestore Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                                    } else {
                                        if (value.exists()) {
                                            // Document exists, retrieve data
                                            String name = value.getString("name");
                                            Toast.makeText(SplashActivity.this, "Welcome back, " + name, Toast.LENGTH_LONG).show();
                                        } else {
                                            // Document doesn't exist, handle it as needed
                                            Toast.makeText(SplashActivity.this, "User data not found.", Toast.LENGTH_SHORT).show();
                                        }

                                        // Start the main activity after handling Firestore data
                                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                                    }
                                }
                            });
                } else {
                    // User is not authenticated, start the login activity
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
                finish();
            }
        }, 2000);
    }

}