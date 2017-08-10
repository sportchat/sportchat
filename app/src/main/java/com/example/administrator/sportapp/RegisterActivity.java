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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    EditText username, password;
    public static String user;
    String pass;

    public static double myLatitude;
    public static double myLongitude;
    //defining view objects
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSignup;
    private TextView textViewSignin;
    private ProgressDialog progressDialog;
 //defining firebaseauth object
    private FirebaseAuth firebaseAuth;
    //defining a database reference
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.editTextPassword);
        Firebase.setAndroidContext(this);

        //initializing firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        //if getCurrentUser does not returns null
        if(firebaseAuth.getCurrentUser() != null){

            FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;

            Firebase nameReference = new Firebase("https://sportapp-74b9c.firebaseio.com/usersName/"+currentFirebaseUser.getUid());


            nameReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    user= (dataSnapshot.child("name").getValue(String.class));

            Context context = getApplicationContext();
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });


            //that means user is already logged in
            //so close this activit
            finish();

            saveLocation();
            //and open Menu activity
            startActivity(new Intent(getApplicationContext(), SelectMenuActivity.class));


        }
        //getting the database reference
        databaseReference = FirebaseDatabase.getInstance().getReference();

        //initializing views
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textViewSignin = (TextView) findViewById(R.id.textViewSignin);

        buttonSignup = (Button) findViewById(R.id.buttonSignup);

        progressDialog = new ProgressDialog(this);

        //attaching listener to button
        buttonSignup.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);
    }


    private void registerUser(){

        user = username.getText().toString();
        pass = password.getText().toString();

        //getting email and password from edit texts
        String email = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();

        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }
        if(user.equals("")){
            username.setError("can't be blank");

            return;
        }
        else if(!user.matches("[A-Za-z0-9א-ת]+")){
            username.setError("חייב להכיל אותיות בעברית או לועזית או מספרים");
            return;

        }
        else if(user.length()<5){
            username.setError("קצר מידיי - חייב להכיל לפחות 5 תווים");
            return;
        }
        else {
            final ProgressDialog pd = new ProgressDialog(RegisterActivity.this);
            pd.setMessage("Loading...");
            pd.show();

            String url = "https://sportapp-74b9c.firebaseio.com/Users.json";

            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                @Override
                public void onResponse(String s) {
                    Firebase reference = new Firebase("https://sportapp-74b9c.firebaseio.com/Users");

                    if(s.equals("null")) {
                        reference.child(user).child("password").setValue(pass);
                        Toast.makeText(RegisterActivity.this, "registration successful", Toast.LENGTH_LONG).show();
                    }
                    else {
                        try {
                            JSONObject obj = new JSONObject(s);

                            if (!obj.has(user)) {
                                reference.child(user).child("password").setValue(pass);
                                Toast.makeText(RegisterActivity.this, "registration successful", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(RegisterActivity.this, "username already exists", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    pd.dismiss();
                }

            },new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    System.out.println("" + volleyError );
                    pd.dismiss();
                }
            });

            RequestQueue rQueue = Volley.newRequestQueue(RegisterActivity.this);
            rQueue.add(request);
        }

        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();

        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            finish();


                            //creating a userinformation object
                            UserInformation userInformation = new UserInformation(user);

                            //getting the current logged in user
                            FirebaseUser user = firebaseAuth.getCurrentUser();


                            databaseReference.child("usersName").child(user.getUid()).setValue(userInformation);

                            saveLocation();
                            startActivity(new Intent(getApplicationContext(), Profile.class));
                        }else{
                            //display some message here
                            Toast.makeText(RegisterActivity.this,"Registration Error",Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });

    }

    @Override
    public void onClick(View view) {

        if(view == buttonSignup){
            registerUser();
        }

        if(view == textViewSignin){
            //open login activity when user taps on the already registered textview
            startActivity(new Intent(this, LoginActivity.class));
        }

    }

    private void saveLocation() {
        //save Gps location

        GPSTracker gps;

        gps = new GPSTracker(RegisterActivity.this);

        if (gps.canGetLocation()) {
            myLatitude = gps.getLatitude();
            myLongitude = gps.getLongitude();


//
//            Toast.makeText(
//                    getApplicationContext(),
//                    "Your Location is -\nLat: " + latitude + "\nLong: "
//                            + longitude, Toast.LENGTH_LONG).show();
        } else {
            gps.showSettingsAlert();
        }
        Firebase reference = new Firebase("https://sportapp-74b9c.firebaseio.com/UserInfo/" + RegisterActivity.user + "/Location");

        Location location = new Location(myLatitude, myLongitude);
        reference.setValue(location);

    }
}