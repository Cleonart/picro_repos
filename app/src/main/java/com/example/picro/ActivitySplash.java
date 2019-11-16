package com.example.picro;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;


public class ActivitySplash extends AppCompatActivity implements View.OnClickListener {

    Button btn_login;
    Intent intentControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        // BUTTON LOGIN
        btn_login = findViewById(R.id.buttonGroup);
        btn_login.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        int selector = view.getId();

        // BUTTON DATA
        if(R.id.buttonGroup == selector){
            intentControl = new Intent(ActivitySplash.this, ActivityLogin.class);
        }

        startActivity(intentControl);
    }
}
