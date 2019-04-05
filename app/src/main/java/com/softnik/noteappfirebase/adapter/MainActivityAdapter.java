package com.softnik.noteappfirebase.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.softnik.noteappfirebase.CRUD.ViewNote;
import com.softnik.noteappfirebase.R;
import com.softnik.noteappfirebase.Store;
import com.softnik.noteappfirebase.model.NoteModel;

import java.util.ArrayList;

public class MainActivityAdapter extends RecyclerView.Adapter <MainActivityAdapter.MyViewHolder>{
    ArrayList<NoteModel> noteModels;
    Context context;

    public MainActivityAdapter(ArrayList<NoteModel> noteModels) {
        this.noteModels = noteModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final  int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.note_single_row,null);

        view.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int i) {
        holder.noteText.setText(noteModels.get(i).getNoteText());
        holder.noteDate.setText(Store.getReadableDate(noteModels.get(i).getTimeStamp()));


        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Bundle pack = new Bundle();
                pack.putString("note",noteModels.get(i).getNoteText());
                pack.putLong("date",noteModels.get(i).getTimeStamp());
                pack.putString("noteAddress",noteModels.get(i).getNodeAddress());
                Intent intent = new Intent(context, ViewNote.class);
                intent.putExtras(pack);
                context.startActivity(intent);

            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder box = new AlertDialog.Builder(context);
                box.setMessage("Delete this note");

                box.setPositiveButton("delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        FirebaseDatabase.getInstance().getReference().child("notes").child(mAuth.getCurrentUser().getUid())
                                .child(noteModels.get(i).getNodeAddress()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(context,"note delete",Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context,"error:\n"+e.getMessage(),Toast.LENGTH_SHORT).show();
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
                return false;
            }
        });
    }



    @Override
    public int getItemCount() {
        return noteModels.size();
    }

    public class MyViewHolder extends ViewHolder{
        TextView noteText , noteDate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            noteText=itemView.findViewById(R.id.noteText);
            noteDate=itemView.findViewById(R.id.noteDate);

        }
    }
}
