package com.example.picro.interface_driver;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.picro.R;
import com.example.picro.data_controller.DecimalFormater;
import com.example.picro.data_controller.IdFormatter;
import com.example.picro.data_model.PaymentRecord;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class TopUpVerify extends AppCompatActivity {

    private FirebaseDatabase firebase = FirebaseDatabase.getInstance();
    String payment_id, username_intent, amount_intent, serial, receiver_id,receiver_raw;
    TextView username, amount;
    String uidTo, uidFrom;
    int updatedDriver, updatedPassenger;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_topup);

        getSupportActionBar().hide();

        Bundle extras   = getIntent().getExtras();
        payment_id      = extras.getString("PAYMENT_ID");
        username_intent = extras.getString("USERNAME");
        amount_intent   = extras.getString("AMOUNT");
        receiver_id     = extras.getString("ID_USER");
        receiver_raw    = extras.getString("ID_USER");
        serial          = extras.getString("SERIAL");

        DatabaseReference validate_ = firebase.getReference().child("picro_cards_manifest").child(serial).child("amount");
        validate_.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int driver_balance = Integer.parseInt(String.valueOf(dataSnapshot.getValue()));
                int top_up_amount  = Integer.parseInt(String.valueOf(amount_intent));

                if(driver_balance < top_up_amount){
                    DatabaseReference remove_topup = firebase.getReference().child("tempor_payment").child(payment_id);
                    remove_topup.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "Saldo anda tidak cukup untuk melakukan top up", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // username
        username = findViewById(R.id.username);
        username.setText(username_intent);

        // amount
        amount   = findViewById(R.id.biaya_top_up);
        amount.setText("Rp. " + DecimalFormater.goToDecimal(Integer.parseInt(amount_intent)));

        Button confirm = findViewById(R.id.confirm_button);
        confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                topup();
            }
        });

        TextView cancel = findViewById(R.id.cancel_button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void topup(){
        DatabaseReference get_data = firebase.getReference("tempor_payment/" + receiver_id);
        get_data.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // uid setter
                uidTo   = String.valueOf(dataSnapshot.child("to").getValue());
                uidFrom = String.valueOf(dataSnapshot.child("from").getValue());
                set_sender();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    // for driver
    private void set_sender(){

        DatabaseReference get_data = firebase.getReference("picro_cards_manifest/" + uidFrom + "/amount");
        get_data.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                updatedDriver = Integer.parseInt(String.valueOf(dataSnapshot.getValue())) - Integer.parseInt(amount_intent);
                DatabaseReference set_data = firebase.getReference("picro_cards_manifest/" + uidFrom + "/amount");

                set_data.setValue(updatedDriver).addOnSuccessListener(new OnSuccessListener<Void>() {

                    @Override
                    public void onSuccess(Void aVoid) {

                        DatabaseReference get_cash = firebase.getReference("picro_cards_manifest/" + uidFrom + "/cash");
                        get_cash.addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                int cash_fix = Integer.parseInt(String.valueOf(dataSnapshot.getValue())) + Integer.parseInt(amount_intent);
                                DatabaseReference set_cash = firebase.getReference("picro_cards_manifest/" + uidFrom + "/cash");
                                set_cash.setValue(cash_fix).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        set_receiver();
                                    }
                                });

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // for passenger
    private void set_receiver(){

        DatabaseReference get_data = firebase.getReference("picro_passengers/" + receiver_id + "/card_serial_number");
        get_data.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                receiver_id = String.valueOf(dataSnapshot.getValue());

                DatabaseReference get_data_amount = firebase.getReference("picro_cards_manifest/" + receiver_id + "/pica_amount");
                get_data_amount.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snap) {
                        updatedPassenger = Integer.parseInt(String.valueOf(snap.getValue())) + Integer.parseInt(amount_intent);
                        DatabaseReference set_data = firebase.getReference("picro_cards_manifest/" + receiver_id + "/pica_amount");
                        set_data.setValue(updatedPassenger).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                report_data();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // from is driver and to is passenger
    private void report_data(){

        // make new object for payment recording
        final PaymentRecord recordModel = new PaymentRecord();
        recordModel.setFrom(uidFrom);
        recordModel.setTo(receiver_id);
        recordModel.setAmount(Integer.parseInt(amount_intent));
        recordModel.setType("TOPUP");
        recordModel.setTimestamp(IdFormatter.timestamp());
        recordModel.setDaterecord(IdFormatter.getDate());
        recordModel.setId(IdFormatter.topUp());

        DatabaseReference record_data = firebase.getReference("tempor_payment").child(String.valueOf(receiver_raw));
        record_data.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // record the payment
                DatabaseReference record = firebase.getReference("picro_payment/" + receiver_id + "/" + String.valueOf(IdFormatter.topUp()));
                record.setValue(recordModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Top up berhasil :)", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
            }
        });


    }
}
