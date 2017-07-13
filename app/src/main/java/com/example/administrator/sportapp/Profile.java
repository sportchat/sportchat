package com.example.administrator.sportapp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Profile extends AppCompatActivity {

    private TextView mTextName;
    private ImageButton myImage;
    private final int SELECT_PICTURE = 1234;
    public boolean flag=false;
    private String imageString;
    private String strImadeDefoult;
    private Bitmap bitmap;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mTextName = (TextView)findViewById(R.id.user_profile_name);
        mTextName.setText(RegisterActivity.user);
        mTextName.setVisibility(View.VISIBLE);
        myImage = (ImageButton)findViewById(R.id.user_profile_photo);
        progressBar=(ProgressBar)findViewById(R.id.progressBar2);

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

                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    //TODO - upload bitmap as bytearray to firebase

                    bitmap = Bitmap.createScaledBitmap(bitmap,350,350,true);
                    myImage.setImageBitmap(bitmap);

                    imageString =converttostring(bitmap);


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
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        flag=true;
        return temp;
    }

    public void saveImage(View view) {

        if(flag==false) {
            progressBar.setProgress(0);
            progressBar.setMax(15);
            progressBar.setVisibility(View.VISIBLE);
            Drawable myDrawable = getResources().getDrawable(R.drawable.add);

            Bitmap bitmapD = ((BitmapDrawable) myDrawable).getBitmap();
            //TODO - upload bitmap as bytearray to firebase

            strImadeDefoult = converttostring(bitmapD);
            addtofirebace(strImadeDefoult);

        }
        else {
            addtofirebace(imageString);
            Toast.makeText(this, "התמונה נשמרה ", Toast.LENGTH_LONG).show();
        }
        startActivity(new Intent(getApplicationContext(), SelectMenuActivity.class));

    }
}
