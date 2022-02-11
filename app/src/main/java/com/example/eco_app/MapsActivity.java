package com.example.eco_app;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.eco_app.databinding.ActivityMapsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        @NonNull ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setContentView(R.layout.activity_maps);

        //private ActivityMapsBinding binding;
        Toolbar mtoolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        FirebaseDatabase.getInstance().getReference().child("Data").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Double> latitudes = new ArrayList<>(Arrays.asList());
                List<Double> longitudes = new ArrayList<>(Arrays.asList());

                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    double latt = (double) snapshot.child("latitude").getValue();
                    double longg = (double) snapshot.child("longitude").getValue();
                    //store this data in list
                    latitudes.add(latt);
                    longitudes.add(longg);
                }
                for(int i=0;i<longitudes.size();i++){
                    LatLng newLocation = new LatLng(latitudes.get(i), longitudes.get(i));
                    mMap.addMarker(new MarkerOptions()
                            .position(newLocation));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                return;
            }
        });


    }


    public void lauchAddActivity(View view) {
        Intent intent = new Intent(MapsActivity.this,AddNew.class);
        startActivity(intent);
    }

    public void launchRecordActivity(MenuItem item) {
        Intent intent = new Intent(MapsActivity.this,viewRecord.class);
        startActivity(intent);
    }

    public void goToLogin() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    public void logout(MenuItem item) {
        FirebaseAuth.getInstance().signOut();
        goToLogin();
    }



}