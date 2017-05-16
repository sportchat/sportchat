package com.example.administrator.sportapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;


public class SelectMenuActivity extends AppCompatActivity implements View.OnClickListener {


//    //firebase auth object
    private FirebaseAuth firebaseAuth;

    private Button buttonRunning;
    private Button buttonSoccerBall;
    private Button buttonYoga;
    private Button buttonKaraoke;
    private Button buttonLogOut;
    public static String hobbies;
    public double latitude;
    public double longitude;

//    //defining a database reference
//    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_menu);

        buttonRunning=   (Button) findViewById(R.id.buttonRunning);
        buttonSoccerBall=   (Button) findViewById(R.id.buttonSoccerBall);
        buttonYoga=   (Button) findViewById(R.id.buttonYoga);
        buttonKaraoke=   (Button) findViewById(R.id.buttonKaraoke);
        buttonLogOut=   (Button) findViewById(R.id.buttonLogOut);


//        //initializing firebase authentication object
//        firebaseAuth = FirebaseAuth.getInstance();
//
//
//        //if the user is not logged in
//        //that means current user will return null
//        if (firebaseAuth.getCurrentUser() == null) {
//            //closing this activity
//            finish();
//            //starting login activity
//            startActivity(new Intent(this, LoginActivity.class));
//        }
//
//
//        //getting the database reference
//        databaseReference = FirebaseDatabase.getInstance().getReference();
//

        //adding listener to button
        buttonRunning.setOnClickListener(this);
        buttonSoccerBall.setOnClickListener(this);
        buttonYoga.setOnClickListener(this);
        buttonKaraoke.setOnClickListener(this);
        buttonLogOut.setOnClickListener(this);
    }



    private void saveLocation() {
            //save Gps location

            GPSTracker gps;

            gps = new GPSTracker(SelectMenuActivity.this);

            if (gps.canGetLocation()) {
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
            Firebase reference = new Firebase("https://sportapp-74b9c.firebaseio.com/Location");

            Location location = new Location(latitude, longitude);
            reference.child(RegisterActivity.user).setValue(location);

        }


    private void setHbbies(String s) {
        hobbies = s;
//        //getting the current logged in user
//        FirebaseUser user = firebaseAuth.getCurrentUser();
//
//
//        databaseReference.child(user.getUid()).child("Hobbies").setValue(hobbies);

        finish();

        Firebase reference = new Firebase("https://sportapp-74b9c.firebaseio.com/Hobbies/"+hobbies);

        reference.child(RegisterActivity.user).setValue(hobbies);
        //displaying a success toast
        Toast.makeText(this, "Information Saved...", Toast.LENGTH_LONG).show();



saveLocation();

        startActivity(new Intent(getApplicationContext(), MapsActivity.class));
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.buttonRunning:
                setHbbies(buttonRunning.getText().toString());

                break;
            case R.id.buttonSoccerBall:
                setHbbies(buttonSoccerBall.getText().toString());

                break;
            case R.id.buttonYoga:
                setHbbies(buttonYoga.getText().toString());

                break;

            case R.id.buttonKaraoke:
                setHbbies(buttonKaraoke.getText().toString());

                break;

            case R.id.buttonLogOut:

//    //getting firebase auth object
       firebaseAuth = FirebaseAuth.getInstance();
                //logging out the user
                firebaseAuth.signOut();
                //closing activity
                finish();
                //starting login activity
                startActivity(new Intent(this, LoginActivity.class));

                break;

        }
    }

}