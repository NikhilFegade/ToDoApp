package com.softnik.noteappfirebase.CRUD;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.softnik.noteappfirebase.R;

public class UpdateNote extends AppCompatActivity {
    EditText updateNote;
    Bundle unpacker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        updateNote = findViewById(R.id.updateNote);

        unpacker = getIntent().getExtras();
        updateNote.setText(unpacker.getString("note"));
        updateNote.setSelection(unpacker.getString("note").length());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_notes, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.saveNotes) {

            String noteStr = updateNote.getText().toString().trim();

            if (TextUtils.isEmpty(noteStr)) {
                Toast.makeText(getApplicationContext(), "Please Enter Something", Toast.LENGTH_SHORT).show();
            } else {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseDatabase.getInstance().getReference().child("notes").child(mAuth.getCurrentUser()
                        .getUid()).child(unpacker.getString("nodeAddress")).child("nodeText").setValue(noteStr).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "note updated", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "error :\n"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }
        return super.onOptionsItemSelected(item);
    }
}
