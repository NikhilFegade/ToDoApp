package com.softnik.noteappfirebase.CRUD;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.softnik.noteappfirebase.R;
import com.softnik.noteappfirebase.Store;

public class ViewNote extends AppCompatActivity {
    TextView noteText, noteDate;
    Bundle unpacker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);

        noteText = findViewById(R.id.noteText);
        noteText = findViewById(R.id.noteDate);

        unpacker=getIntent().getExtras();
        noteText.setText(unpacker.getString("note"));
        noteDate.setText(Store.getReadableDate(unpacker.getLong("date")));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedID = item.getItemId();
        if(selectedID ==R.id.delete){

            AlertDialog.Builder box = new AlertDialog.Builder(getApplicationContext());
            box.setMessage("Delete this note");

            box.setPositiveButton("delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    FirebaseDatabase.getInstance().getReference().child("notes").child(mAuth.getCurrentUser().getUid())
                            .child(unpacker.getString("nodeAddress")).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(),"note delete",Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"error:\n"+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });
            box.setNegativeButton("cancle", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = box.create();
            dialog.show();



        }else if(selectedID ==R.id.update){
            Bundle pack = new Bundle();
            pack.putString("note",unpacker.getString("key"));
            pack.putString("noteAddress",unpacker.getString("nodeAddress"));
            Intent intent = new Intent(getApplicationContext(), UpdateNote.class);
            intent.putExtras(pack);
            startActivity(intent);
            finish();

        }
        return super.onOptionsItemSelected(item);
    }
}
