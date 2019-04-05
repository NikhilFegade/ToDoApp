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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText edtEmail, edtPassword;
    Button btnLogin;
    FirebaseAuth mAuth;
    ProgressDialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtEmail=findViewById(R.id.edtEmail);
        edtPassword=findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        dialog=new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mAuth.getCurrentUser()!=null){
            startActivity(new Intent(this,LoginActivity.class));
            finish();
        }
    }

    public void accessAccount(View view) {

        String passwordStr = edtPassword.getText().toString().trim();
        String emailStr = edtEmail.getText().toString().trim();

        dialog.setMessage("Accessing Account");
        dialog.setCancelable(false);
        dialog.show();

        if(TextUtils.isEmpty(passwordStr) || TextUtils.isEmpty(emailStr)){
            Toast.makeText(this,"Please Enter All fields",Toast.LENGTH_SHORT).show();
            dialog.dismiss();

        }else{
            mAuth.createUserWithEmailAndPassword(emailStr,passwordStr).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(getApplicationContext(),"Login Successfull..!",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),"Error :\n"+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }

    }



    public void openCreateAccount(View view) {
        startActivity(new Intent(this,CreateAccount.class));
        finish();
    }
}
