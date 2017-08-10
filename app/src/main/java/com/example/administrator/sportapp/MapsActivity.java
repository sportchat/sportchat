package com.example.administrator.sportapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import static com.example.administrator.sportapp.RegisterActivity.user;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public double longitude;
    public double latitude;
    public double lat = 31.35, lon = 35.64, lona, lata;
    List<String> lst = new ArrayList<String>();

    private String myHobbies;
    private String userHobbies;

    private Button chat;
    //firebase auth object
    private FirebaseAuth firebaseAuth;

    private DatabaseReference databaseReference;

    private ImageButton friendImage;
    public Bitmap bitmapImage;
    private ImageView ImageV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        friendImage = (ImageButton) findViewById(R.id.friendImage);
        friendImage.setVisibility(View.INVISIBLE);

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

        else

        {
            gps.showSettingsAlert();
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Firebase refUserInfo = new Firebase("https://sportapp-74b9c.firebaseio.com/UserInfo/");
        refUserInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                friendImage.setVisibility(View.INVISIBLE);

                myHobbies = (dataSnapshot.child("" + user).child("Hobbies").getValue(String.class));

                lat = SelectMenuActivity.myLatitude;
                lon = SelectMenuActivity.myLongitude;

                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    lst.add(String.valueOf(dsp.getKey())); //add result into array list
                }

                for (String data : lst) {
                    userHobbies = (dataSnapshot.child("" + data).child("Hobbies").getValue(String.class));
                    lata =  Double.parseDouble(dataSnapshot.child("" + data).child("Location").child("latitude").getValue(String.class).toString());
                    lona = Double.parseDouble(dataSnapshot.child("" + data).child("Location").child("longitude").getValue(String.class).toString());
                    LatLng userLocation2 = new LatLng(lata, lona);

                    if ((userHobbies.equals(myHobbies))
                            && (userLocation2.latitude <lat +0.15 && userLocation2.latitude >lat -0.15 )  //check if the user's location betwin +\- 0.15 pixel
                            && (userLocation2.longitude <lon +0.15 && userLocation2.longitude >lon -0.15 )
                            && (userLocation2.latitude != 0.0 || userLocation2.latitude != 0.0)) {


                        mMap.addMarker(new MarkerOptions().position(userLocation2).title("" + data));

                    }

                }

                lat = Double.parseDouble(dataSnapshot.child("" + user).child("Location").child("latitude").getValue(String.class).toString());
                lon = Double.parseDouble(dataSnapshot.child("" + user).child("Location").child("longitude").getValue(String.class).toString());


                LatLng userLocation = new LatLng(lat, lon);

                mMap.addMarker(new MarkerOptions().position(userLocation).title("user"));
                //  mMap.addMarker(new MarkerOptions().position(userLocation2).title(""))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15.0f));
                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker arg0) {

                        for (String data : lst) {

                            if (arg0 != null && arg0.getTitle().equals("" + data)) {
                                setImageButton(data); //TODO - use the pressed contact's image
                                Toast.makeText(MapsActivity.this, ""+data, Toast.LENGTH_LONG).show();
//                                UserDetails.chatWith = data;
////                               startActivity(new Intent(MapsActivity.this, Chat.class));
                            }
                        }
                    }

                });
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }


        });

        LatLng MyLocation = new LatLng(latitude, longitude);


        //   mMap.addMarker(new MarkerOptions().position(MyLocation).title("Location"));


//        Toast.makeText(this,""+latitude,Toast.LENGTH_LONG).show();

        //       mMap.addMarker(new MarkerOptions().position(MyLocation).title("Your Location"));
        //     mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MyLocation, 19.0f));
    }


    public void buttonChat(View view) {
        startActivity(new Intent(this, Users.class));

    }

    public void buttonCBack(View view) {
        startActivity(new Intent(this, SelectMenuActivity.class));
    }

    private void setImageButton(final String titleUserName) {

        Firebase refI = new Firebase("https://sportapp-74b9c.firebaseio.com/image/");
        refI.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String strImage= (dataSnapshot.child(""+titleUserName).getValue(String.class));

                Bitmap bitmap =StringToBitMap(strImage);

                bitmapImage = bitmap;
//                ImageV.setImageBitmap(bitmap);
//                ImageV.setVisibility(View.VISIBLE);

                friendImage.setVisibility(View.VISIBLE);
                friendImage.setImageBitmap(bitmap);

                friendImage.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        UserDetails.chatWith = titleUserName;
                        startActivity(new Intent(MapsActivity.this, Chat.class));

                    }
                });

            }



            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }

        });

    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }


    public  void onBackPressed() {


    }
}