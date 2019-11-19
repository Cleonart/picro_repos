package com.example.picro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityStart extends AppCompatActivity {

    Intent intentSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        getSupportActionBar().hide();

        // if auth code is not authenticated
        if(getShareData("auth_code" ) == null || getShareData("auth_code") == "" ){
            //Toast.makeText(getApplicationContext(), "Logging you in",Toast.LENGTH_LONG).show();
            intentSettings = new Intent(ActivityStart.this, ActivitySplash.class);
        }

        // if code is authenticated
        else{
            //Toast.makeText(getApplicationContext(), "Selamat datang kembali",Toast.LENGTH_LONG).show();
            //Toast.makeText(getApplicationContext(), getShareData("auth_code"),Toast.LENGTH_LONG).show();
            intentSettings = new Intent(ActivityStart.this, MainActivity.class);
        }

        startActivity(intentSettings);
        finish();
    }

    // SET SHARE DATA
    public void setShareData(String data){
        SharedPreferences shared = getSharedPreferences("rootUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putString("auth_code", data);
        editor.commit();
    }

    // GET SHARE DATA
    public String getShareData(String selector){
        SharedPreferences shared = getSharedPreferences("rootUser", Context.MODE_PRIVATE);
        return shared.getString(selector, null);
    }
}
