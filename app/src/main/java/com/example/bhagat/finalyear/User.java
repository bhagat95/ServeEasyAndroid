package com.example.bhagat.finalyear;

/**
 * Created by Shashank on 12-03-2017.
 */

class User {
    String name, mobileNo, address, password,confirmPassword, pinNo,userType;
    User(String name, String mobileNo, String password, String confirmPassword, String address, String userType) {
        this.name = name.trim();
        this.mobileNo = mobileNo.trim();
        this.password = password.trim();
        this.confirmPassword = confirmPassword.trim();
        this.address = address.trim();
        this.userType = userType.trim();
    }
}