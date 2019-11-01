package com.example.picro;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityTopUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topup);
        disableActionBar();
    }

    private void disableActionBar(){
        getSupportActionBar().hide();
    }

}
