package com.example.picro.activity_modul;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.picro.ActivityRegister;
import com.example.picro.ActivitySplash;
import com.example.picro.ActivityTopUp;
import com.example.picro.MainActivity;
import com.example.picro.R;
import com.example.picro.activity_fragment.PaymentApproved;
import com.example.picro.activity_fragment.PaymentQuantity;
import com.example.picro.activity_fragment.TopUpStatus;
import com.example.picro.data_controller.FirebaseController;
import com.example.picro.data_model.PaymentQuantitySelector;
import com.example.picro.feature_modul.PaymentModul;
import com.example.picro.feature_modul.TopUpModul;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class ActivityScanner extends AppCompatActivity implements View.OnClickListener, ZXingScannerView.ResultHandler, FirebaseController.ResultHandler{

    private ArrayList<PaymentQuantitySelector> quantity;
    private PaymentQuantity quantityAdapter;
    private FirebaseController firebaseController = new FirebaseController();
    FirebaseDatabase tes = FirebaseDatabase.getInstance();
    private PaymentModul paymentModul = new PaymentModul();
    private ZXingScannerView scannerView;
    RelativeLayout buttonBack;
    LinearLayout progress;
    String uid;
    String mode;
    Intent intentSettings;
    int amount = 4000, index;
    TextView qr_label;
    ConstraintLayout scannerBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        // handler
        firebaseController.setResultHandler(this);

        scannerBody = findViewById(R.id.scanner_body);

        // bundling
        Bundle extras = getIntent().getExtras();
        mode = extras.getString("SCANNER_MODE");

        // scanner mode for login
        if(mode.equals("LOGIN")){
        }

        // scanner mode for payment
        else if(mode.equals("PAYMENT")){

            Spinner qty_select = findViewById(R.id.quantity_select);
            qty_select.setVisibility(View.VISIBLE);

            quantity = new ArrayList<>();
            quantity.add(new PaymentQuantitySelector("Bayar Sendiri", R.drawable.pay_1,1));
            quantity.add(new PaymentQuantitySelector("Bayar Berdua", R.drawable.pay_2,2));
            quantity.add(new PaymentQuantitySelector("Bayar Bertiga", R.drawable.pay_3,3));
            quantity.add(new PaymentQuantitySelector("Bayar Berempat", R.drawable.pay_4,4));

            quantityAdapter = new PaymentQuantity(this, quantity);
            qty_select.setAdapter(quantityAdapter) ;

            qty_select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    PaymentQuantitySelector clidkedItem =  (PaymentQuantitySelector) adapterView.getItemAtPosition(i);
                    index = clidkedItem.getIndex();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        }

        // scanner mode for transfer
        else if(mode.equals("TRANSFER")){
        }

        // scanner mode for topup
        else if(mode.equals("TOPUP")){
            amount = Integer.parseInt(String.valueOf(extras.getString("TOPUP_AMOUNT")));
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
                        Toast.makeText(getApplicationContext(), "Akses kamera kamu harus diaktifkan",Toast.LENGTH_LONG).show();
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
    public void handleResult(final Result result) {

        scannerView.startCamera();                // restart the camera for next scanning
        progress.setVisibility(View.VISIBLE);     // set progress bar to show

        Query query = tes.getReference("picro_cards_manifest/" + String.valueOf(result) + "/pica_type");
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String dataSnaps = String.valueOf(dataSnapshot.getValue());

                // qr code not registered on system
                if(dataSnaps.equals("null")){
                    Toast.makeText(getApplicationContext(), "Kode QR salah atau tidak terdaftar", Toast.LENGTH_LONG).show();
                    back();
                }

                // qr code registered
                else{

                    // login
                    if(mode.equals("LOGIN")){
                        handleLogin(result);
                    }

                    // payment
                    else if(mode.equals("PAYMENT")){

                        if(dataSnaps.equals("PASSENGER")){
                            Toast.makeText(getApplicationContext(), "Maaf, kode qr yang kamu scan salah :(", Toast.LENGTH_LONG).show();
                            back();
                        }

                        else if(dataSnaps.equals("DRIVER")){
                            showSnack("Sedang memproses pembayaran kamu, sabar yah :)", 3000);
                            paymentModul.setUidFrom(getShareData("SERIAL"));
                            paymentModul.setUidTo(String.valueOf(result));
                            paymentModul.setQty(index);
                            paymentModul.payModul();
                            paymentModul.setPaymentHandler(new PaymentModul.PaymentHandler() {
                                @Override
                                public void paymentStats(String status) {

                                    // payment succeess
                                    if(status.equals("SUCCESS")){
                                        intentSettings = new Intent(ActivityScanner.this, PaymentApproved.class);
                                        startActivity(intentSettings);
                                        finish();
                                    }

                                    // payment failed - reason : not enough funds
                                    else if(status.equals("NOT_ENOUGH_FUNDS")){
                                        Toast.makeText(getApplicationContext(), "Saldo kamu enggak cukup, silahkan top up dulu ;)", Toast.LENGTH_LONG).show();
                                        finish();
                                    }

                                }
                            });
                        }

                    }

                    else if(mode.equals("TOPUP")){

                        if(dataSnaps.equals("PASSENGER")){
                            Toast.makeText(getApplicationContext(), "Maaf, kode qr yang kamu scan salah :(", Toast.LENGTH_LONG).show();
                            back();
                        }

                        else if(dataSnaps.equals("DRIVER")){
                            showSnack("Sedang memproses top up kamu... :)", 3000);
                            TopUpModul topuphandler = new TopUpModul();
                            topuphandler.setTopUpFrom(String.valueOf(result));
                            topuphandler.setTopUpTo(getShareData("UID"));
                            topuphandler.setAmount(amount);
                            topuphandler.topUp();
                            topuphandler.setTopUpHandler(new TopUpModul.TopUpHandler() {
                                @Override
                                public void topUpStats(String status) {
                                    intentSettings = new Intent(ActivityScanner.this, TopUpStatus.class);
                                    startActivity(intentSettings);
                                    finish();
                                }
                            });
                        }

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View view) {

        int selector = view.getId();

        // back button action
        if(selector == R.id.backButton){
            back();
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

    }

    // set share data
    public void setShareData(String data){
        SharedPreferences shared = getSharedPreferences("rootUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putString("auth_code", data);
        editor.commit();
    }

    public void showSnack(String msg, int duration){
        Snackbar snackbar = Snackbar.make(scannerBody, msg, Snackbar.LENGTH_LONG).setDuration(duration);
        snackbar.show();
    }

    public void handleLogin(Result result){
        uid = String.valueOf(result);                           // convert result to string
        String temp_path = "picro_cards_manifest/" + uid;       // search for the card in cards_manifest
        firebaseController.getChildValueOneTime(temp_path);     // trigger the firebase module by using the previous path
    }

    // get share data
    public String getShareData(String selector){
        SharedPreferences shared = getSharedPreferences("rootUser", Context.MODE_PRIVATE);
        return shared.getString(selector, null);
    }

    public void back(){

        if(mode.equals("LOGIN")){
            scannerView.stopCamera();
            intentSettings = new Intent(ActivityScanner.this, ActivitySplash.class);
            startActivity(intentSettings);
            finish();
        }

        else if(mode.equals("PAYMENT")){
            finish();
        }

        else if(mode.equals("TOPUP")){
            intentSettings = new Intent(ActivityScanner.this, ActivityTopUp.class);
            startActivity(intentSettings);
            finish();
        };

    }

}
