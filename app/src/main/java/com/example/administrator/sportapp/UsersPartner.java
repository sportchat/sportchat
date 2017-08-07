package com.example.administrator.sportapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.example.administrator.sportapp.RegisterActivity.user;

public class UsersPartner extends AppCompatActivity {


        ListView usersList;
        TextView noUsersText;
        ArrayList<String> al = new ArrayList<>();
        int totalUsers = 0;
        ProgressDialog pd;
    public double lat = 31.35, lon = 35.64, lona, lata;
    List<String> lst = new ArrayList<String>();

    private String myHobbies;
    private String userHobbies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_partner);

            usersList = (ListView)findViewById(R.id.usersList);
            noUsersText = (TextView)findViewById(R.id.noUsersText);

            pd = new ProgressDialog(com.example.administrator.sportapp.UsersPartner.this);
            pd.setMessage("Loading...");
            pd.show();

            String url = "https://sportapp-74b9c.firebaseio.com/Users.json";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                doOnSuccess(s);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });



            RequestQueue rQueue = Volley.newRequestQueue(com.example.administrator.sportapp.UsersPartner.this);
            rQueue.add(request);

            usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    UserDetails.chatWith = al.get(position);
                    startActivity(new Intent(com.example.administrator.sportapp.UsersPartner.this, Chat.class));
                }
            });
        }

        public void doOnSuccess(String s){


            Firebase refUserInfo = new Firebase("https://sportapp-74b9c.firebaseio.com/UserInfo/");
            refUserInfo.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    myHobbies = (dataSnapshot.child("" + user).child("Hobbies").getValue(String.class));

                    lat = SelectMenuActivity.myLatitude;
                    lon = SelectMenuActivity.myLongitude;

                    for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                        lst.add(String.valueOf(dsp.getKey())); //add result into array list
                    }

                    for (String data : lst) {
                        userHobbies = (dataSnapshot.child("" + data).child("Hobbies").getValue(String.class));
                        lata = Double.parseDouble(dataSnapshot.child("" + data).child("Location").child("latitude").getValue(String.class).toString());
                        lona = Double.parseDouble(dataSnapshot.child("" + data).child("Location").child("longitude").getValue(String.class).toString());
                        LatLng userLocation2 = new LatLng(lata, lona);

                        if ((userHobbies.equals(myHobbies))
                                && (userLocation2.latitude < lat + 0.15 && userLocation2.latitude > lat - 0.15)  //check if the user's location betwin +\- 0.15 pixel
                                && (userLocation2.longitude < lon + 0.15 && userLocation2.longitude > lon - 0.15)
                                && (userLocation2.latitude != 0.0 || userLocation2.latitude != 0.0)) {
                            if (!data.equals(UserDetails.username)) {
                                al.add(data);
                                totalUsers++;
                            }

                        }

                    }
                }


            @Override
                public void onCancelled(FirebaseError firebaseError) {
                }


            });




            if(totalUsers <=1){
                noUsersText.setVisibility(View.VISIBLE);
                usersList.setVisibility(View.GONE);
            }
            else{
                noUsersText.setVisibility(View.GONE);
                usersList.setVisibility(View.VISIBLE);
                usersList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al));
            }

            pd.dismiss();
        }

    }
