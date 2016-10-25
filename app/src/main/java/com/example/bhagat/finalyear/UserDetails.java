package com.example.bhagat.finalyear;

/**
 * Created by bhagat on 10/25/16.
 */
public class UserDetails {
    private static UserDetails ourInstance = new UserDetails();

    String url = "http://192.168.109.41/";
    String consumerId = "123";

    public static UserDetails getInstance() {
        return ourInstance;
    }

    private UserDetails() {
    }
}
