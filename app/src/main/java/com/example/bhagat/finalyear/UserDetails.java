package com.example.bhagat.finalyear;

/**
 * Created by bhagat on 10/25/16.
 */
public class UserDetails {
    private static UserDetails ourInstance = new UserDetails();

    public String url = "http://192.168.109.41/";
    public String consumerId = "123";
    public String providerId = "500";
    public boolean isProvider = true;

    public static UserDetails getInstance() {
        return ourInstance;
    }

    private UserDetails() {
    }
}
