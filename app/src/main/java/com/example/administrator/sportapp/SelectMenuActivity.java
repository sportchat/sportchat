package com.example.administrator.sportapp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class SelectMenuActivity extends AppCompatActivity implements View.OnClickListener {

    private final int SELECT_PICTURE = 1234;
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
    private ImageButton myImage;
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
        myImage = (ImageButton) findViewById(R.id.myButton);
//        setButtonsLock(false);
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


    public void goToGallery(View view) {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, SELECT_PICTURE);

//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                String selectedImagePath = getPath(selectedImageUri);
                System.out.println("Image Path : " + selectedImagePath);
                try {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    //TODO - upload bitmap as bytearray to firebase

                    bitmap = Bitmap.createScaledBitmap(bitmap,350,350,true);

                    Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
                    String arr =converttostring(bitmap);
                    addtofirebace(arr);
                    myImage.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    public void  addtofirebace(String smyimage) {
        Firebase ref = new Firebase("https://sportapp-74b9c.firebaseio.com/image/");
        ref.child(RegisterActivity.user).setValue(smyimage);
    }

    public String converttostring(Bitmap bitmap) {
            ByteArrayOutputStream baos=new  ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
            byte [] b=baos.toByteArray();
            String temp=Base64.encodeToString(b, Base64.DEFAULT);
    return temp;
    }
}