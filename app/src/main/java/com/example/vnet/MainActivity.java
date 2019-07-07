package com.example.vnet;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    DatabaseReference dbr;

    EditText email,password;
    ProgressBar progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        progress=(ProgressBar)findViewById(R.id.progressBar);
        progress.setVisibility(View.GONE);
      dbr=FirebaseDatabase.getInstance().getReference("users");


        if(mAuth.getCurrentUser()!=null){

            Intent i=new Intent(getApplicationContext(),Dashboard.class);
            startActivity(i);
            finish();
        }


    }
    public void login(View v){
      progress.setVisibility(View.VISIBLE);
    email=(EditText)findViewById(R.id.Lemail);
    password=(EditText) findViewById(R.id.Lpassword);
    if(email.getText().toString().length()<6||password.getText().toString().length()<6){
        Toast.makeText(getApplicationContext(),"Email or Password is less than 6 characters",Toast.LENGTH_SHORT).show();
      return;
    }
       mAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
           @Override
           public void onComplete(@NonNull Task<AuthResult> task) {
               if(task.isSuccessful()) {
                   Intent i = new Intent(getApplicationContext(), Dashboard.class);
                   startActivity(i);
                   progress.setVisibility(View.GONE);
                   finish();
               }
               else{
                   Toast.makeText(getApplicationContext(),"Email and Password is not registered",Toast.LENGTH_SHORT).show();
                   progress.setVisibility(View.GONE);
               }
           }
       });


    }
    public void signup(View d){
      progress.setVisibility(View.VISIBLE);
      email=(EditText)findViewById(R.id.Lemail);
      password=(EditText) findViewById(R.id.Lpassword);
      if(email.getText().toString().length()<6||password.getText().toString().length()<6){
          Toast.makeText(getApplicationContext(),"Email or Password is less than 6 characters",Toast.LENGTH_SHORT).show();
          return;
      }

       mAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
           @Override
           public void onComplete(@NonNull Task<AuthResult> task) {
               if(task.isSuccessful()){
                   progress.setVisibility(View.GONE);
                   Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
                   HashMap<String,String> hm=new HashMap<String,String>();
                   hm.put("email",email.getText().toString());
                   hm.put("password",password.getText().toString());
                   dbr.child(mAuth.getCurrentUser().getUid()).setValue(hm);


               }
               else {
                   progress.setVisibility(View.GONE);
                   Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
               }
           }
       });


}
    }
