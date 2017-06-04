package com.example.administrator.sportapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

import static com.example.administrator.sportapp.RegisterActivity.user;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public double longitude;
    public double latitude;
    public double lat=31.35,lon=35.64,lona,lata;

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
                Map<String, Object> tt = (HashMap<String,Object>)dataSnapshot.getValue();


                lat=Double.parseDouble(dataSnapshot.child(""+user).child("latitude").getValue(String.class).toString());
                lon=Double.parseDouble(dataSnapshot.child(""+user).child("longitude").getValue(String.class).toString());
                lona=Double.parseDouble(dataSnapshot.child("191919").child("longitude").getValue(String.class).toString());
                lata=Double.parseDouble(dataSnapshot.child("191919").child("latitude").getValue(String.class).toString());

                LatLng userLocation = new LatLng(lat,lon);
                LatLng userLocation2 = new LatLng(lata,lona);
                mMap.addMarker(new MarkerOptions().position(userLocation).title("user"));
                mMap.addMarker(new MarkerOptions().position(userLocation2).title("191919"));

                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener()
                {

                    @Override
                    public void onInfoWindowClick(Marker arg0) {
                        if(arg0 != null && arg0.getTitle().equals("191919")){
                            Intent intent1 = new Intent(MapsActivity.this, Chat.class);
                            startActivity(intent1);}      }
                });
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) { }


        });



        //  latitude= databaseReference.child(user.getUid()).child("latitude").;
        // Add a marker in Sydney and move the camera

        LatLng MyLocation = new LatLng(latitude, longitude);


        //   mMap.addMarker(new MarkerOptions().position(MyLocation).title("Location"));


//        Toast.makeText(this,""+latitude,Toast.LENGTH_LONG).show();

        //       mMap.addMarker(new MarkerOptions().position(MyLocation).title("Your Location"));
        //     mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MyLocation, 19.0f));
    }

    public void buttonchat(View view) {
        startActivity(new Intent(this, Users.class));
        // מעבר לעמוד הצ'ט  אפשר כבר פה לטעון את המשתמשים שמוצגים במפה ולשלוח אותם לעמוד הצ'ט
    }


}