package com.example.translatorapp.ui.elements;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.translatorapp.R;
import com.example.translatorapp.databinding.ActivitySignUpBinding;
import com.example.translatorapp.ui.elements.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    AlertDialog dialog;
    CollectionReference collectionReference = db.collection("Users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dialog = new AlertDialog.Builder(this).create();
        binding.signupbtn.setOnClickListener(view -> {
        
            String email = binding.signupemail.getText().toString().trim();
            String pass = binding.signuppassword.getText().toString().trim();
            String cfPass = binding.signupconfirmpassword.getText().toString().trim();
            String name = binding.signupname.getText().toString().trim();
            String username = binding.signupusername.getText().toString().trim();
            
            if(!email.isEmpty() && !pass.isEmpty() && !cfPass.isEmpty() &&
            !name.isEmpty() && !username.isEmpty())
            {
                if(pass.equals(cfPass)) {
                    Signup(email, pass, cfPass, username, name);
                }
                else {
                    Toast.makeText(this, "Password doesn't match.", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(this, "Fill all credentials.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Signup(String email, String pass, String cfPass, String username, String name) {
        dialog.setContentView(R.layout.progress_dialog);
        dialog.show();

        auth.createUserWithEmailAndPassword(email,pass)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        User user = new User(username,name,auth.getUid(),email);
                        collectionReference.document(auth.getCurrentUser().getUid())
                                .set(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        dialog.dismiss();
                                        startActivity(new Intent(SignUpActivity.this,MainActivity.class));
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dialog.dismiss();
                                        Toast.makeText(SignUpActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                                        
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(SignUpActivity.this, "Check Internet Connection.", Toast.LENGTH_SHORT).show();
                    }
                });
        
        
    }
}