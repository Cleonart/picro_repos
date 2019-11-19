package com.example.picro.feature_modul;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.picro.R;
import com.example.picro.data_controller.IdFormatter;
import com.example.picro.data_model.PaymentRecord;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PaymentModul extends AppCompatActivity {

    // firebase database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference payment;

    // variable data
    private String uidFrom;
    private String uidTo;
    private int amount;

    public void setUidFrom(String uidFrom) {
        this.uidFrom = uidFrom;
    }

    public void setUidTo(String uidTo) {
        this.uidTo = uidTo;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public PaymentModul(){

    }

    public PaymentModul(String uidFrom, String uidTo, int amount){
        this.uidFrom = uidFrom;
        this.uidTo = uidTo;
        this.amount = amount;
    }

    public void payModul(){
        userFundsCheck();
    }

    // check the user funds
    private void userFundsCheck(){

        DatabaseReference fundsChecker = database.getReference("picro_cards_manifest/" + uidFrom + "/pica_amount");

        fundsChecker.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                // get the user funds
                int userFunds = Integer.parseInt(String.valueOf(dataSnapshot.getValue()));

                // user funds enough
                if(userFunds > 0){
                    userFunds = userFunds - amount;
                    makePayment(userFunds);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    // payment modul
    private void makePayment(int userFunds){

       // update the user funds
       payment = database.getReference("picro_cards_manifest/" + uidFrom + "/pica_amount" );
       payment.setValue(userFunds).addOnSuccessListener(new OnSuccessListener<Void>() {
           @Override
           public void onSuccess(Void aVoid) {
               // transfer the amount of funds to receiver
               transferToReceiver();
           }
       });

    }

    // transfer to receiver
    private void transferToReceiver(){

        DatabaseReference receive = database.getReference("picro_cards_manifest/" + uidTo + "/amount");
        receive.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // transfer to user
                int rev_amount = Integer.parseInt(String.valueOf(dataSnapshot.getValue())) + amount;

                // receive the payment
                receiverReceive(rev_amount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // receiver receive payment
    private void receiverReceive(int newAmount){

        DatabaseReference newAmountUser = database.getReference("picro_cards_manifest/" + uidTo + "/amount");
        newAmountUser.setValue(newAmount).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                recordTransaction();
            }
        });

    }

    // record transaction
    private void recordTransaction(){

        // make new object for payment recording
        PaymentRecord recordModel = new PaymentRecord();
        recordModel.setFrom(uidFrom);
        recordModel.setTo(uidTo);
        recordModel.setAmount(amount);
        recordModel.setType("PAYMENT");
        recordModel.setTimestamp(IdFormatter.timestamp());
        recordModel.setDaterecord(IdFormatter.getDate());

        // record the payment
        DatabaseReference record = database.getReference("picro_payment/" + uidFrom + "/" + IdFormatter.paymentId());
        record.setValue(recordModel);

    }


}
