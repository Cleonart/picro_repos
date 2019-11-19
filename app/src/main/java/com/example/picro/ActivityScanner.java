package com.example.picro;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.picro.data_controller.FirebaseController;
import com.example.picro.feature_modul.PaymentModul;
import com.google.firebase.database.DataSnapshot;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;


import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class ActivityScanner extends AppCompatActivity implements View.OnClickListener, ZXingScannerView.ResultHandler, FirebaseController.ResultHandler{

    private FirebaseController firebaseController = new FirebaseController();
    private ZXingScannerView scannerView;
    RelativeLayout buttonBack;
    LinearLayout progress;
    String uid;
    String mode;
    Intent intentSettings;
    TextView qr_label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        qr_label = findViewById(R.id.qr_text);

        firebaseController.setResultHandler(this);

        Bundle extras = getIntent().getExtras();
        mode = extras.getString("SCANNER_MODE");

        // scanner mode for login and payment
        if(mode.equals("LOGIN")){
            qr_label.setText("Scan kartu QR anda");
        }

        else if(mode.equals("PAYMENT")){
            qr_label.setText("Scan QR untuk membayar");
        }

        else if(mode.equals("TRANSFER")){
            qr_label.setText("Scan kode QR penerima");
        }

        // view scanner init
        scannerView = (ZXingScannerView)findViewById(R.id.rxscan);
        getSupportActionBar().hide();

        // back button init
        buttonBack = findViewById(R.id.backButton);
        buttonBack.setOnClickListener(this);

        // hide the progress bar
        progress = findViewById(R.id.progressBar);
        progress.setVisibility(View.INVISIBLE);

        // permission
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        scannerView.setResultHandler(ActivityScanner.this);
                        scannerView.startCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(getApplicationContext(), "You must grant access",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                })
                .check();
    }

    @Override
    protected void onStop() {
        scannerView.stopCamera();
        super.onStop();
    }

    @Override
    public void handleResult(Result result) {
        scannerView.startCamera();                              // restart the camera for next scanning
        progress.setVisibility(View.VISIBLE);                   // set progress bar to show

        if(mode.equals("LOGIN")){
            handleLogin(result);
        }

        else if(mode.equals("PAYMENT")){

            PaymentModul pay = new PaymentModul();
            pay.setUidFrom(getShareData("SERIAL"));
            pay.setUidTo(String.valueOf(result));
            pay.setAmount(4000);
            pay.payModul();
        }
    }

    public void handleLogin(Result result){
        uid = String.valueOf(result);                           // convert result to string
        String temp_path = "picro_cards_manifest/" + uid;       // search for the card in cards_manifest
        firebaseController.getChildValueOneTime(temp_path);     // trigger the firebase module by using the previous path
    }


    @Override
    public void onClick(View view) {

        int selector = view.getId();

        // back button action
        if(selector == R.id.backButton){

            if(mode.equals("LOGIN")){
                scannerView.stopCamera();
                intentSettings = new Intent(ActivityScanner.this, ActivitySplash.class);
                startActivity(intentSettings);
                finish();
            }

            else if(mode.equals("PAYMENT")){
                finish();
            }

        }

    }

    @Override
    public void setValueStatus(String path, int status) {

    }

    @Override
    public void valueListener(String path, DataSnapshot data) {

        if(mode.equals("LOGIN")) {

            int card_stats = Integer.parseInt(String.valueOf(data.child("pica_stats").getValue()));
            String card_uid = String.valueOf(data.child("pica_uid").getValue());

            // go to register pagehy
            if (card_stats == 0) {
                Toast.makeText(getApplicationContext(), "Kartu masih tersedia, silahkan mendaftar", Toast.LENGTH_LONG).show();
                intentSettings = new Intent(ActivityScanner.this, ActivityRegister.class);
                Bundle extras = new Bundle();
                extras.putString("SERIAL", uid);
                extras.putString("UID", card_uid);
                intentSettings.putExtras(extras);
                startActivity(intentSettings);
                finish();
            }

            // 6 digit code input
            else if (card_stats == 1) {
                intentSettings = new Intent(ActivityScanner.this, ActivityAuth.class);
                Bundle extras = new Bundle();
                extras.putString("SERIAL", uid);
                extras.putString("UID", card_uid);
                intentSettings.putExtras(extras);
                startActivity(intentSettings);
                finish();
            }

        }

        else if(mode.equals("PAYMENT")){

        }
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
