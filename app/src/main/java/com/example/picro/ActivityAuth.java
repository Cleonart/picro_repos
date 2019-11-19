package com.example.picro;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.content.Context;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import com.example.picro.data_controller.FirebaseController;
import com.google.firebase.database.DataSnapshot;

public class ActivityAuth extends AppCompatActivity implements FirebaseController.ResultHandler{

    FirebaseController controller = new FirebaseController();
    String uid, serial;
    int auth_code;
    EditText auth_input;
    String mode = "LOGIN";
    Intent intentSettings;
    LinearLayout progress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_input);

        // support action bar
        getSupportActionBar().hide();

        // progress bar
        progress = findViewById(R.id.progress_show);
        progress.setVisibility(View.INVISIBLE);

        Bundle extras = getIntent().getExtras();
        uid    = extras.getString("UID");
        serial = extras.getString("SERIAL");

        // element init
        auth_input = findViewById(R.id.auth_input);

        // controller set
        controller.setResultHandler(this);

        // activity reusable status
        Button auth_login = findViewById(R.id.auth_button);
        auth_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // get the string format of auth code and then convert that string into integer
                auth_code = Integer.parseInt(auth_input.getText().toString());

                // validate number of digit - less than 100000 means that the value only 5 digits (99999)
                if(auth_code < 100000){
                    auth_input.setError("Kode harus 6 digit");
                    auth_input.requestFocus();
                }

                // validated success
                else{
                    progress.setVisibility(View.VISIBLE);
                    controller.getChildValueOneTime("picro_passengers/" + uid);
                }

            }
        });

    }

    @Override
    public void setValueStatus(String path, int status) { }

    @Override
    public void valueListener(String path, DataSnapshot data) {

        // auth login - step 1
        if(mode.equals("LOGIN") && path.equals("picro_passengers/" + uid)){

            int auth_from_database = Integer.parseInt(String.valueOf(data.child("auth_code").getValue()));

            // login credential authorized
            if(auth_from_database == auth_code){
                intentSettings = new Intent(ActivityAuth.this, MainActivity.class);
                controller.setValue("picro_cards_manifest/" + serial + "/pica_stats", "1");
                controller.getChildValueOneTime("picro_passengers/" + uid + "/username");
                mode = "SET_USERNAME";
            }

            // login credential unauthorized
            else{
                progress.setVisibility(View.INVISIBLE);
                auth_input.setError("Kode salah");
                auth_input.requestFocus();
            }

        }

        // set the username preferences - step 2
        else if(mode.equals("SET_USERNAME") && path.equals("picro_passengers/" + uid + "/username")){
            SharedPreferences shared = getSharedPreferences("rootUser", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = shared.edit();
            editor.putString("UNAME", String.valueOf(data.getValue()));
            editor.putString("UID", uid);
            editor.putString("SERIAL", serial);
            editor.putString("auth_code", uid + serial);
            editor.commit();
            startActivity(intentSettings);
            finish();
        }

    }
}
