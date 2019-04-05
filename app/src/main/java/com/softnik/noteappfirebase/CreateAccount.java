package com.softnik.noteappfirebase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.softnik.noteappfirebase.model.UserModel;

public class CreateAccount extends AppCompatActivity {

    EditText edtUsername, edtPassword, edtEmail;
    Button btnRegister;
    FirebaseAuth mAuth;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtEmail = findViewById(R.id.edtEmail);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        dialog = new ProgressDialog(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    public void createAccount(View view) {
        final String usernameStr = edtUsername.getText().toString().trim();
        final String passwordStr = edtPassword.getText().toString().trim();
        final String emailStr = edtEmail.getText().toString().trim();

        dialog.setMessage("Creating Account");
        dialog.setCancelable(false);
        dialog.show();

        if (TextUtils.isEmpty(passwordStr) || TextUtils.isEmpty(usernameStr) || TextUtils.isEmpty(emailStr)) {
            Toast.makeText(this, "Please Enter All fields", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        } else {
            if (TextUtils.isEmpty(passwordStr) || TextUtils.isEmpty(emailStr)) {
                Toast.makeText(this, "Please Enter All fields", Toast.LENGTH_SHORT).show();
            } else {
                mAuth.createUserWithEmailAndPassword(emailStr, passwordStr).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("users");
                        String UID = mAuth.getCurrentUser().getUid();
                        UserModel userModel = new UserModel(UID, usernameStr, emailStr, passwordStr);
                        dialog.dismiss();
                        rootRef.child(UID).setValue(userModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Account Created Successfully..!", Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "error :\n "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Error :\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }
    }

    public void loginActivity(View view) {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

}

