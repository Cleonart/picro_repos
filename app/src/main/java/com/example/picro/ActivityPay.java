package com.example.picro;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ActivityPay extends AppCompatActivity implements View.OnClickListener, FirebaseController.ResultHandler{

    Button payment;
    RelativeLayout backButton;
    TextView legend;
    FirebaseDatabase database;
    DatabaseReference refs;
    DatabaseReference r_data;
    FirebaseController firebaseController = new FirebaseController();

    private int button_state = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        disableActionBar();
        ElementInit();

        database = FirebaseDatabase.getInstance();
        firebaseController.setResultHandler(this);

        // ON CREATE
        if(UniversalPicroData.getPica_state()){
            payment.setText(R.string.ap_button_end);
            legend.setText(R.string.ap_legend_start);
            payment.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }

        else{
            payment.setText(R.string.ap_button_start);
            legend.setText(R.string.ap_legend_end);
            payment.setBackgroundColor(getResources().getColor(R.color.danger));
        }

        firebaseController.getChildValueRealTime("picro_device/0/device_id");

    }

    private void disableActionBar(){
        getSupportActionBar().hide();
    }

    private void ElementInit(){
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(this);

        payment = findViewById(R.id.pay_button);
        payment.setOnClickListener(this);

        legend = findViewById(R.id.ap_legend);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.backButton:
                finish();
                break;

            case R.id.pay_button:

                if(this.button_state == 0){
                    payment.setText(R.string.ap_button_end);
                    legend.setText(R.string.ap_legend_start);
                    payment.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    this.button_state = 1;
                }

                else{
                    payment.setText(R.string.ap_button_start);
                    legend.setText(R.string.ap_legend_end);
                    payment.setBackgroundColor(getResources().getColor(R.color.danger));
                    this.button_state = 0;
                }

                // Write a message to the database
                firebaseController.setValue("picro_device/0/device_id",String.valueOf(this.button_state));
                break;
        }

    }

    // SET VALUE STATUS LISTENER
    @Override
    public void setValueStatus(String path, int status) {

        if(path == "picro_device/0/device_id"){

        }

    }

    @Override
    public void valueListener(String path, DataSnapshot data) {
        if(path == "picro_device/0/device_id"){
            Toast.makeText(getApplicationContext(), String.valueOf(data),Toast.LENGTH_LONG).show();
        }
    }

}
