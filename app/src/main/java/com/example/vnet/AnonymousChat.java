package com.example.vnet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AnonymousChat extends AppCompatActivity  {
    public DatabaseReference dbr;
    List<anonymouslistitem> listItems;
    public RecyclerView recyclerView;
    public RecyclerView.Adapter adapter;
public  Location location;
    public RecyclerView.LayoutManager layoutManager;
    private LocationManager locationManager;
    private String provider;
    private MyLocationListener mylistener;
    private Criteria criteria;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anonymous_chat);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("AnonyChat");
        listItems=new ArrayList<anonymouslistitem>();
        recyclerView=(RecyclerView)findViewById(R.id.recyclerviewposts);

        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the location provider
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);   //default

        // user defines the criteria

        criteria.setCostAllowed(false);
        // get the best provider depending on the criteria
        provider = locationManager.getBestProvider(criteria, false);


        mylistener = new MyLocationListener();

        if (location != null) {
            mylistener.onLocationChanged(location);
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                Toast.makeText(getApplicationContext(),"You need to provide permission",Toast.LENGTH_SHORT).show();
            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);


            }
        } else {

            location = locationManager.getLastKnownLocation(provider);
            locationManager.requestLocationUpdates(provider, 200, 1, mylistener);
           // String a=""+location.getLatitude();
            //Log.w("Location Coordinates", a);
        }








    }

    @Override
    public boolean onSupportNavigateUp() {

        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       MenuInflater menuInflater= getMenuInflater();
       menuInflater.inflate(R.menu.anonymouschat,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.compose){

mylistener.send();
        }


        return super.onOptionsItemSelected(item);
    }
    private class MyLocationListener implements LocationListener {
        private String postalCode;
        @Override
        public void onLocationChanged(Location location) {

            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

              postalCode = addresses.get(0).getPostalCode();

                dbr=FirebaseDatabase.getInstance().getReference(postalCode);
                dbr.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        listItems.clear();
                        for(DataSnapshot ds: dataSnapshot.getChildren()){
                            anonymouslistitem item= ds.getValue(anonymouslistitem.class);
                            item.setId(ds.getKey());

                            listItems.add(item);

                        }
                        adapter=new anonymouslistadapter(listItems,AnonymousChat.this);
                        recyclerView.setAdapter(adapter);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                        Log.w("Error:","loadPost:onCancelled", databaseError.toException());

                    }
                });




            } catch (IOException e) {
                e.printStackTrace();
            }



        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Toast.makeText(AnonymousChat.this, provider + "'s status changed to "+status +"!",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(AnonymousChat.this, "Provider " + provider + " enabled!",
                    Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(AnonymousChat.this, "Provider " + provider + " disabled!",
                    Toast.LENGTH_SHORT).show();
        }

        public void send() {


           Intent i=new Intent(getApplicationContext(),ComposePost.class);
           i.putExtra("PostalCode",postalCode);
           startActivity(i);
        }
    }}
