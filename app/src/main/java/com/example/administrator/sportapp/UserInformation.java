package com.example.administrator.sportapp;

/**
 * Created by Administrator on 12/04/17.
 */

public class UserInformation {

    public String name;
    public String phone_number;
    public String latitude;
    public String longitude;
 //   public String Hobbies;




    public UserInformation(String name, String phoneNumber, double latitude, double longitude) {
        this.name = name;
        this.phone_number = phoneNumber;
        this.latitude = ""+latitude;
        this.longitude = ""+longitude;
//        this.Hobbies = Hobbies;
    }
}
