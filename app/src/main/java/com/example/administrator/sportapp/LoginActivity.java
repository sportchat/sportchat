package com.example.administrator.sportapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    //defining views
    private Button buttonSignIn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignup;
//        public double latitude;
//        public double longitude;
    //firebase auth object

    private FirebaseAuth firebaseAuth;

    //progress dialog
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //getting firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        //if the objects getcurrentuser method is not null
        //means user is already logged in
        if (firebaseAuth.getCurrentUser() != null) {
            //close this activity
//            finish();
            //opening profile activity

//            startActivity(new Intent(getApplicationContext(), SelectMenuActivity.class));
        }

        //initializing views
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonSignIn = (Button) findViewById(R.id.buttonSignin);
        textViewSignup = (TextView) findViewById(R.id.textViewSignUp);

        progressDialog = new ProgressDialog(this);

        //attaching click listener
        buttonSignIn.setOnClickListener(this);
        textViewSignup.setOnClickListener(this);
    }

    //method for user login
    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();


        //checking if email and passwords are empty
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show();
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();

        //logging in the user
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        //if the task is successfull
                        if (task.isSuccessful()) {
                            //   saveLocation();
                            //start the profile activity

                            FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();//get user id from firebase
                            Toast.makeText(LoginActivity.this, "" + currentFirebaseUser.getUid(), Toast.LENGTH_SHORT).show();

                            Firebase nameReference = new Firebase("https://sportapp-74b9c.firebaseio.com/usersName/" + currentFirebaseUser.getUid());


                            nameReference.addValueEventListener(new ValueEventListener() {// get currenr user name  from firebase
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {


                                    RegisterActivity.user = (dataSnapshot.child("name").getValue(String.class));

                                    Context context = getApplicationContext();
                                    CharSequence text = RegisterActivity.user;
                                    int duration = Toast.LENGTH_SHORT;

                                    Toast toast = Toast.makeText(context, text, duration);
                                    toast.show();
                                    // do your stuff here with value

                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {

                                }
                            });
//                            finish();

                            startActivity(new Intent(getApplicationContext(), SelectMenuActivity.class));
                        }
                    }
                });

    }

    @Override
    public void onClick(View view) {
        if (view == buttonSignIn) {
            userLogin();
        }

        if (view == textViewSignup) {
            finish();

            startActivity(new Intent(this, RegisterActivity.class));
        }
    }


//        private void saveLocation() {
//            //save Gps location
//
//            GPSTracker gps;
//
//            gps = new GPSTracker(LoginActivity.this);
//
//            if (gps.canGetLocation()) {
//                latitude = gps.getLatitude();
//                longitude = gps.getLongitude();
//
//
////
////            Toast.makeText(
////                    getApplicationContext(),
////                    "Your Location is -\nLat: " + latitude + "\nLong: "
////                            + longitude, Toast.LENGTH_LONG).show();
//            } else {
//                gps.showSettingsAlert();
//            }
//            Firebase reference = new Firebase("https://sportapp-74b9c.firebaseio.com/Location");
//
//            Location location = new Location(latitude, longitude);
//            reference.child(RegisterActivity.user).setValue(location);
//
//        }
}