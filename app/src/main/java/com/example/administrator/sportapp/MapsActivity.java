package com.example.administrator.sportapp;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public double longitude;
    public double latitude;
    //firebase auth object
    private FirebaseAuth firebaseAuth;

    private DatabaseReference databaseReference;

    /*
        private void getUsersFromServer(){
            //getting the current logged in user
            FirebaseUser user = firebaseAuth.getCurrentUser();
            String s= databaseReference.child(user.getUid()).


        }
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //getting the database reference
        databaseReference = FirebaseDatabase.getInstance().getReference();
        //getting the current logged in user
        FirebaseUser user = firebaseAuth.getCurrentUser();

        GPSTracker gps;

        gps = new GPSTracker(MapsActivity.this);

        if(gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();


//
//            Toast.makeText(
//                    getApplicationContext(),
//                    "Your Location is -\nLat: " + latitude + "\nLong: "
//                            + longitude, Toast.LENGTH_LONG).show();
        } else {
            gps.showSettingsAlert();
        }

//test
//        //initializing firebase authentication object
//        firebaseAuth = FirebaseAuth.getInstance();
//
//
//        //getting the database reference
//        databaseReference = FirebaseDatabase.getInstance().getReference();
//        FirebaseUser user = firebaseAuth.getCurrentUser();
//
//
//        databaseReference.child(user.getUid()).addValueEventListener(
//                new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        Map<String , String> map = dataSnapshot.getValue(Map.class);
//                        latitude = Double.parseDouble(map.get("latitude"));
//
//
//
//                        longitude = Double.parseDouble(map.get("longitude"));
//
//
////                        Log.v("E_VALUE", "latitude: "+ latitude);
////                        Log.v("E_VALUE", "longitude: "+ longitude);
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//        Toast.makeText(this, "The longitude="+longitude, Toast.LENGTH_LONG).show();

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

        //  latitude= databaseReference.child(user.getUid()).child("latitude").;
        // Add a marker in Sydney and move the camera
        LatLng MyLocation = new LatLng(latitude, longitude);
//        Toast.makeText(this,""+latitude,Toast.LENGTH_LONG).show();

        mMap.addMarker(new MarkerOptions().position(MyLocation).title("Your Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MyLocation, 19.0f));
    }
}
