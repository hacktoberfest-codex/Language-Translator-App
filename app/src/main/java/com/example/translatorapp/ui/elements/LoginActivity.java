package com.example.translatorapp.ui.elements;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.translatorapp.R;
import com.example.translatorapp.databinding.ActivityLoginBinding;
import com.example.translatorapp.ui.elements.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference reference = db.collection("Users");

    private AlertDialog dialog;;
    private ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        dialog = new AlertDialog.Builder(this).create();
        dialog.setContentView(R.layout.progress_dialog);
        binding.loginbtn.setOnClickListener(view -> {

            String email = binding.signinemail.getText().toString().trim();
            String pass = binding.signinpassword.getText().toString().trim();

            if(!email.isEmpty() && !pass.isEmpty())
            {
                Login(email,pass);
            }
            else{
                Toast.makeText(this, "Fill required Credentials", Toast.LENGTH_SHORT).show();
            }
        });


        binding.loginredirecttosignup.setOnClickListener(view -> {
            startActivity(new Intent(this,SignUpActivity.class));
            finish();
        });
    }

    private void Login(String email, String pass) {

        dialog.show();

        auth.signInWithEmailAndPassword(email,pass)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                      assert auth.getCurrentUser()!=null;


                      reference.whereEqualTo("Uid",auth.getUid())
                              .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                  @Override
                                  public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                      for(QueryDocumentSnapshot snapshot :value)
                                      {
                                          User user = snapshot.toObject(User.class);
                                          Toast.makeText(LoginActivity.this, "Welcome "+snapshot.getString("name"), Toast.LENGTH_SHORT).show();
                                          dialog.dismiss();
                                          startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                          finish();
                                      }
                                  }
                              });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(LoginActivity.this, "This Email has not registered", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}