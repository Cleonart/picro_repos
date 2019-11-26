package com.example.picro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.picro.activity_fragment.TopUpStatus;
import com.example.picro.activity_modul.ActivityScanner;
import com.example.picro.data_controller.DecimalFormater;
import com.example.picro.data_controller.IdFormatter;
import com.example.picro.feature_modul.TopUpModul;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

import me.abhinay.input.CurrencyEditText;

public class ActivityTopUp extends AppCompatActivity implements View.OnClickListener{

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    Intent intentSettings;
    CurrencyEditText etInput;
    LinearLayout rp_9000,rp_19000,rp_29000;
    ImageView backButton;
    Button proceed;
    TextView help;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topup);
        getSupportActionBar().hide();
        elementInit();
        topupcontroller();
    }

    // on click override
    @Override
    public void onClick(View view) {

        switch(view.getId()){

            case R.id.back:
                finish();
                break;

            case R.id.rp_9000:
                etInput.setText(R.string.rp_9000);
                break;

            case R.id.rp_19000:
                etInput.setText(R.string.rp_19000);
                break;

            case R.id.rp_29000:
                etInput.setText(R.string.rp_29000);
                break;

            case R.id.help_topup:
                intentSettings = new Intent(ActivityTopUp.this, ActivityHelp.class);
                startActivity(intentSettings);
                break;

            case R.id.proceed:
                processTopUp();
                break;
        }

    }

    // button listener
    public void elementInit(){

        backButton = findViewById(R.id.back);
        backButton.setOnClickListener(this);

        rp_9000 = findViewById(R.id.rp_9000);
        rp_9000.setOnClickListener(this);

        rp_19000 = findViewById(R.id.rp_19000);
        rp_19000.setOnClickListener(this);

        rp_29000 = findViewById(R.id.rp_29000);
        rp_29000.setOnClickListener(this);

        help = findViewById(R.id.help_topup);
        help.setOnClickListener(this);

        proceed = findViewById(R.id.proceed);
        proceed.setOnClickListener(this);

        etInput = findViewById(R.id.etInput);
        etInput.setCurrency("Rp ");
        etInput.setDelimiter(false);
        etInput.setSpacing(false);
        etInput.setDecimals(false);
        //Make sure that Decimals is set as false if a custom Separator is used
        etInput.setSeparator(",");
    }

    // process top up
    public void processTopUp(){

        SharedPreferences shared = getSharedPreferences("rootUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();

        // set the amount of top up
        int amountTopUp = etInput.getCleanIntValue();

        // if amount of top up is more than or eqal 10.000
        if(amountTopUp >= 10000){

            // made top up
            editor.putString("on_top_up", "VALID");

            // go to scanner to scan picro qr code
            intentSettings = new Intent(ActivityTopUp.this, ActivityScanner.class);
            Bundle extras = new Bundle();
            extras.putString("SCANNER_MODE", "TOPUP");
            extras.putString("TOPUP_AMOUNT",String.valueOf(amountTopUp));
            intentSettings.putExtras(extras);
            startActivity(intentSettings);
            finish();

        }

        // if amount of top up is less than 10.000
        else if(amountTopUp < 10000){
            Toast.makeText(getApplicationContext(), "Oops, minimal nominal top up harus\nlebih dari Rp. 10.000", Toast.LENGTH_LONG).show();
            editor.putString("on_top_up", "INVALID");
        }

        editor.commit();
    }

    // top up controller
    public void topupcontroller(){

        String uid  = String.valueOf(getShareData("UID"));                                                              // get the uid of user for top up
        DatabaseReference topUpStats = database.getReference("tempor_payment/" + uid);                                    // get data from firebase with given uid
        topUpStats.addListenerForSingleValueEvent(new ValueEventListener() {                                                    // firebase single value event listener
                                                                                                                                // make it real time can crash the app
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if(String.valueOf(dataSnapshot.child("timestamp").getValue()).equals("null")){                                  // --------------------------------------------------
                    //Toast.makeText(getApplicationContext(), "Silahkan melakukan top up", Toast.LENGTH_LONG).show();           // trigger this function if there's no top up request
                }                                                                                                               // --------------------------------------------------

                else{

                    String datenow  = IdFormatter.getDate();                                                                     // get the date from --app--
                    String fromdate = String.valueOf(dataSnapshot.child("daterecord").getValue());                               // get the record from **server**

                    int now      = IdFormatter.timestamp();                                                                      // get the stamp from --app--
                    int fromuid  = Integer.parseInt(String.valueOf(dataSnapshot.child("timestamp").getValue()));                 // get the time stamp from **server**
                    String amount = DecimalFormater.goToDecimal(Integer.parseInt(String.valueOf(dataSnapshot.child("amount").getValue()))); // get the user top up amount

                    int latency = IdFormatter.timestampDifference(now, fromuid);                                                // calculate the time difference in minutes
                    int raw     = IdFormatter.timestampraw(now, fromuid);                                                       // calculate the time difference in seconds

                    Float rawLatency = (float) (now - fromuid) / 60;                                                            //

                    //Toast.makeText(getApplicationContext(),String.format("%.2f", rawLatency), Toast.LENGTH_LONG).show();      // for debugging purpose

                    // datenow
                    if(datenow.equals(fromdate) && latency <= 30){
                        //Toast.makeText(getApplicationContext(), "Waktu top up masih tersedia", Toast.LENGTH_LONG).show();
                        intentSettings = new Intent(ActivityTopUp.this, TopUpStatus.class);
                        Bundle extras = new Bundle();
                        extras.putString("AMOUNT", String.valueOf(amount));
                        extras.putString("TIME_RAW", String.format("%.2f", rawLatency));
                        intentSettings.putExtras(extras);
                        startActivity(intentSettings);
                        finish();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // set share data
    public void setShareData(String data){
        SharedPreferences shared = getSharedPreferences("rootUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putString("auth_code", data);
        editor.commit();
    }

    // get share data
    public String getShareData(String selector){
        SharedPreferences shared = getSharedPreferences("rootUser", Context.MODE_PRIVATE);
        return shared.getString(selector, null);
    }
}
