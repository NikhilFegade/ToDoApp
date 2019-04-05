package com.softnik.noteappfirebase;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.softnik.noteappfirebase.CRUD.CreateNote;
import com.softnik.noteappfirebase.adapter.MainActivityAdapter;
import com.softnik.noteappfirebase.model.NoteModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference rootRef;
    FirebaseAuth mAuth;
    ArrayList<NoteModel> noteModels;
    MainActivityAdapter mainActivityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAuth = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference().child("notes");

        noteModels = new ArrayList<>();
        mainActivityAdapter = new MainActivityAdapter(noteModels);
        recyclerView.setAdapter(mainActivityAdapter);
        notesReader();
    }

    public void notesReader() {
        Log.i("123232", "called" + mAuth.getCurrentUser().getUid() + " ");
        rootRef.child(mAuth.getCurrentUser().getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                NoteModel noteModel = dataSnapshot.getValue(NoteModel.class);
                noteModels.add(noteModel);
                mainActivityAdapter.notifyDataSetChanged();

                Log.i("123232 - child added", dataSnapshot + " ");
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                NoteModel updatedNoteModel =dataSnapshot.getValue(NoteModel.class);

                for (int i = 0; i < noteModels.size(); i++) {
                    if (noteModels.get(i).getNodeAddress().trim().equals(dataSnapshot.getKey())) {
                        noteModels.remove(noteModels.get(i));
                        noteModels.add(i,updatedNoteModel);
                        mainActivityAdapter.notifyDataSetChanged();
                        break;
                    }
                }


            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                for (int i = 0; i < noteModels.size(); i++) {
                    if (noteModels.get(i).getNodeAddress().trim().equals(dataSnapshot.getKey())) {
                        noteModels.remove(noteModels.get(i));
                        mainActivityAdapter.notifyDataSetChanged();
                        break;
                    }
                }
                Log.i("123232 - child removed", dataSnapshot + " ");
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int selectedID = item.getItemId();
        if (selectedID == R.id.newNote) {
            //start new note activity
            startActivity(new Intent(getApplicationContext(), CreateNote.class));

        }else if(selectedID==R.id.logOut){
            mAuth.signOut();
            startActivity(new Intent(getApplicationContext(),CreateAccount.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
