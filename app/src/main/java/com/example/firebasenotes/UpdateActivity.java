package com.example.firebasenotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.firebasenotes.databinding.ActivityUpdateBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

public class UpdateActivity extends AppCompatActivity {
   private String id,title,description;
   ActivityUpdateBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent=getIntent();
        id=intent.getStringExtra("id");
        title=intent.getStringExtra("title");
        description=intent.getStringExtra("description");

        binding.title.setText(title);
        binding.description.setText(description);
        binding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog progressDialog=new ProgressDialog(view.getContext());
                progressDialog.setTitle("Deleting");
                FirebaseFirestore.getInstance()
                        .collection("notes")
                        .document(id)
                        .delete();
                finish();
            }
        });



        binding.updateNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title=binding.title.getText().toString();
                description=binding.description.getText().toString();
                updatenote();


            }
        });





    }


    private void updatenote(){
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("updating");
        progressDialog.setMessage("your note");
        progressDialog.show();


        NotesModel notesModel=new NotesModel(id,title,description,firebaseAuth.getUid());
        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseFirestore.collection("notes")
                .document(id)
                .set(notesModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        finish();
                        Toast.makeText(UpdateActivity.this,"Note Saved",Toast.LENGTH_SHORT).show();
                        progressDialog.cancel();
                        finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        progressDialog.cancel();

                    }
                });


    }
}