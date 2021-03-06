package com.example.picro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.picro.data_controller.FirebaseController;
import com.example.picro.data_model.MemberData;
import com.example.picro.interface_driver.MainActivityDriver;
import com.google.firebase.database.DataSnapshot;

public class ActivityRegister extends AppCompatActivity implements FirebaseController.ResultHandler{

    EditText username, auth_code, auth_code_again;
    Button register;
    LinearLayout progress;
    FirebaseController controller = new FirebaseController();
    Intent intentSettings;
    String pathGlobal;
    private String uid, serial, user_type;
    String text_username, text_auth, text_auth_again;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        controller.setResultHandler(this);

        Bundle extras = getIntent().getExtras();
        uid       = extras.getString("UID");
        user_type = extras.getString("USER_TYPE");
        serial    = extras.getString("UID");

        if(user_type.equals("PASSENGER")){
            serial = extras.getString("SERIAL");
        }

        progress = findViewById(R.id.progressbar);
        progress.setVisibility(View.INVISIBLE);

        // edit text declaration
        username = findViewById(R.id.register_user_id);
        auth_code = findViewById(R.id.register_auth_code);
        auth_code_again = findViewById(R.id.register_auth_code_again);

        // checkbox
        CheckBox showAuth = findViewById(R.id.show_auth);
        showAuth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b == true){
                    auth_code.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    auth_code_again.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }

                else{
                    auth_code.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    auth_code_again.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        // register button
        register = findViewById(R.id.register_btn);
        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                text_username    = username.getText().toString();
                text_auth        = auth_code.getText().toString();
                text_auth_again  = auth_code_again.getText().toString();

                if(text_username.isEmpty()){
                    username.setError("Kotak username harus diisi");
                    username.requestFocus();
                }

                if(text_auth.isEmpty()){
                    auth_code.setError("Kode otentikasi harus diisi");
                }

                if(text_auth_again.isEmpty()){
                    auth_code_again.setError("Kode otentikasi harus diisi");
                }

                if(!(text_username.isEmpty() && text_auth.isEmpty() && text_auth_again.isEmpty())){

                    int auth = Integer.parseInt(text_auth);
                    int auth_again = Integer.parseInt(text_auth_again);

                    // validate the length of authentication code
                    if(auth < 100000){
                        auth_code.setError("Kode otentikasi harus 6 digit");
                    }

                    if(auth_again < 100000){
                        auth_code_again.setError("Kode otentikasi harus 6 digit");
                    }

                    // validated success
                    if(auth >= 100000 && auth_again >= 100000){

                        // validated the auth code
                        if(auth != auth_again){
                            auth_code.setError("Kode otentikasi salah");
                            auth_code_again.setError("Kode otentikasi salah");
                            auth_code.setText("");
                            auth_code_again.setText("");
                        }

                        // auth code valid, registering user
                        else{

                            // registering the user
                            progress.setVisibility(View.VISIBLE);

                            // make the member object
                            MemberData member = new MemberData();
                            member.setUsername(text_username);
                            member.setAuth_code(auth);
                            member.setCard_serial_number(serial);
                            member.setPica_uid(uid);

                            // path inside database
                            if(user_type.equals("PASSENGER")){
                                pathGlobal = "picro_passengers/" + uid;
                            }

                            else if(user_type.equals("DRIVER")){
                                pathGlobal = "picro_driver/" + uid;
                            }

                            // send request data to server
                            controller.insertValueObject(pathGlobal, member);
                        }
                    }

                }
            }
        });

    }

    private int registerNew(){
        return 0;
    }

    @Override
    public void setValueStatus(String path, int status) {

        if(path.equals(pathGlobal)){

            if (status == 1) {

                // shared preferences
                SharedPreferences shared = getSharedPreferences("rootUser", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = shared.edit();
                editor.putString("UNAME", text_username);
                editor.putString("UID", uid);
                editor.putString("SERIAL", serial);
                editor.putString("auth_code", String.valueOf(uid) + String.valueOf(serial));
                editor.putString("USER_TYPE", user_type);
                editor.commit();

                status = 0;

                // set the value
                controller.setValue("picro_cards_manifest/" + serial + "/pica_stats", "1");

                if(user_type.equals("PASSENGER")){
                    intentSettings = new Intent(ActivityRegister.this, MainActivity.class);
                }

                else if(user_type.equals("DRIVER")){
                    intentSettings = new Intent(ActivityRegister.this, MainActivityDriver.class);
                }

                startActivity(intentSettings);
                finish();
            }
        }

        else{

        }

    }

    @Override
    public void valueListener(String path, DataSnapshot data) {

    }

}
