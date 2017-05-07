package com.example.administrator.sportapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import static com.example.administrator.sportapp.LoginActivity.user;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public double longitude;
    public double latitude;
    public double lat,lon;

    private Button chat;
    //firebase auth object
    private FirebaseAuth firebaseAuth;

    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        GPSTracker gps;

        gps = new GPSTracker(MapsActivity.this);

        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();


        }

//
//            Toast.makeText(
//                    getApplicationContext(),
//                    "Your Location is -\nLat: " + latitude + "\nLong: "
//                            + longitude, Toast.LENGTH_LONG).show();

        else

        {
            gps.showSettingsAlert();
        }


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


        Firebase ref = new Firebase("https://sportapp-74b9c.firebaseio.com/Location");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //          Map<String, Object> tt = (HashMap<String,Object>)dataSnapshot.getValue();
                lat=dataSnapshot.child(""+user).child("latitude").getValue(Double.class);
                lon=dataSnapshot.child(""+user).child("longitude").getValue(Double.class);
                Toast.makeText(MapsActivity.this, ""+lon, Toast.LENGTH_LONG).show();
                Toast.makeText(MapsActivity.this, ""+lat, Toast.LENGTH_LONG).show();


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



        //  latitude= databaseReference.child(user.getUid()).child("latitude").;
        // Add a marker in Sydney and move the camera
        LatLng MyLocation = new LatLng(latitude, longitude);
        LatLng userLocation = new LatLng(lat,lon);
        mMap.addMarker(new MarkerOptions().position(userLocation).title("user Location"));

//        Toast.makeText(this,""+latitude,Toast.LENGTH_LONG).show();

 //       mMap.addMarker(new MarkerOptions().position(MyLocation).title("Your Location"));
   //     mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MyLocation, 19.0f));
    }

    public void buttonchat(View view) {
        startActivity(new Intent(this, Users.class));
        // מעבר לעמוד הצ'ט  אפשר כבר פה לטעון את המשתמשים שמוצגים במפה ולשלוח אותם לעמוד הצ'ט
    }


}
