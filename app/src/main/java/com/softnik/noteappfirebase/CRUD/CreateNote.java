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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.softnik.noteappfirebase.R;
import com.softnik.noteappfirebase.model.NoteModel;

public class CreateNote extends AppCompatActivity {
    EditText edtNote;
    DatabaseReference rootRef;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        edtNote=findViewById(R.id.edtNote);
        rootRef=FirebaseDatabase.getInstance().getReference().child("notes");
        mAuth=FirebaseAuth.getInstance();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_notes,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.saveNotes){
            String noteStr = edtNote.getText().toString();
            if(TextUtils.isEmpty(noteStr)){
                Toast.makeText(getApplicationContext(),"Please Enter Something",Toast.LENGTH_SHORT).show();
            }else{
                String key = rootRef.push().getKey();
                NoteModel noteModel = new NoteModel(noteStr,key);
                rootRef.child(mAuth.getCurrentUser().getUid()).child(key).setValue(noteModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Note Added Successfully",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"error\n"+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }
        return super.onOptionsItemSelected(item);
    }


}

