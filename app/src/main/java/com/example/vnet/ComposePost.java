package com.example.vnet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.chinalwb.are.AREditor;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ComposePost extends AppCompatActivity {
DatabaseReference dbr;
    AREditor arEditor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_post);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Compose Message");

        dbr=FirebaseDatabase.getInstance().getReference(getIntent().getStringExtra("PostalCode"));
       arEditor =findViewById(R.id.areditor);
        arEditor.setExpandMode(AREditor.ExpandMode.FULL);
        arEditor.setHideToolbar(false);
        arEditor.setToolbarAlignment(AREditor.ToolbarAlignment.BOTTOM);


    }
    @Override
    public boolean onSupportNavigateUp() {

        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.composepost,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.post){
            anonymouslistitem it=new anonymouslistitem(arEditor.getHtml(),"0","0",dbr.push().getKey(),getIntent().getStringExtra("PostalCode"));

            dbr.child(dbr.push().getKey()).setValue(it);
            onBackPressed();


        }
        return super.onOptionsItemSelected(item);
    }


}
