package com.example.bhagat.finalyear;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    public static Context context;
    TextView signup, skip;
    EditText username, password;
    Button login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = getApplicationContext();        
        
        //finding widgets from layout
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        signup = (TextView) findViewById(R.id.signup);
        skip = (TextView) findViewById(R.id.skip);
        login = (Button) findViewById(R.id.login);
        signup.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
        skip.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));

        //signup.setVisibility(View.GONE);

        //login.setOnClickListener(this);
        //skip.setOnClickListener(this);
        //signup.setOnClickListener(this);

    }
    
    protected void rkj13(){
        Log.e("Error check ","yo man");
    }


}
