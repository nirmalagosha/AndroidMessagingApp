package com.example.vnet;

import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Toast;


import java.util.HashMap;

public class Profile extends AppCompatActivity {
    public FirebaseAuth mAuth;
    public DatabaseReference dbr;

    TextInputEditText name,status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        mAuth=FirebaseAuth.getInstance();
        dbr=FirebaseDatabase.getInstance().getReference("userprofile");

      name=(TextInputEditText)findViewById(R.id.Name);
      status=(TextInputEditText)findViewById(R.id.Status);
       dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                if(dataSnapshot.child(mAuth.getCurrentUser().getUid()).hasChildren()) {
                    String bns = dataSnapshot.child(mAuth.getCurrentUser().getUid()).getValue().toString();

                    String[] ans = bns.split(",");
                    String Status = ans[0].split("=")[1];
                    String Name = ans[1].split("=")[1];
                    Name = Name.substring(0, Name.length() - 1);
                    Profile.this.name.setText(Name);
                    Profile.this.status.setText(Status);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Error:","loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });


    }
    @Override
    public boolean onSupportNavigateUp(){
        HashMap<String,String> hm=new HashMap<String, String>();
        if(name.getEditableText().toString().length()==0||status.getEditableText().toString().length()==0){
            Toast.makeText(getApplicationContext(),"Fill Out both the fields",Toast.LENGTH_SHORT).show();
            return false;
        }
        hm.put("Name",name.getEditableText().toString());
        hm.put("Status",status.getEditableText().toString());

        dbr.child(mAuth.getCurrentUser().getUid()).setValue(hm);
        finish();
        return true;
    }

}
