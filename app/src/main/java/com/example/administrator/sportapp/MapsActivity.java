package com.example.administrator.sportapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public double longitude;
    public double latitude;
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


//        //initializing firebase authentication object
//        firebaseAuth = FirebaseAuth.getInstance();
//
//
//        //getting the database reference
//        databaseReference = FirebaseDatabase.getInstance().getReference();
//        FirebaseUser user = firebaseAuth.getCurrentUser();
//

//        databaseReference.child(user.getUid()).addValueEventListener(
//                new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        Map<String , String> map = dataSnapshot.getValue(Map.class);
//                        String latitude = map.get("latitude");
//                        String longitude = map.get("longitude");
//
//                        Log.v("E_VALUE", "latitude: "+ latitude);
//                        Log.v("E_VALUE", "longitude: "+ longitude);
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });





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

    public void buttonchat(View view) {
        startActivity(new Intent(this, ChatActivity.class));
        // מעבר לעמוד הצ'ט  אפשר כבר פה לטעון את המשתמשים שמוצגים במפה ולשלוח אותם לעמוד הצ'ט
    }


}
