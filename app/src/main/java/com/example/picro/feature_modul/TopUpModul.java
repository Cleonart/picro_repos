package com.example.picro.feature_modul;

import com.example.picro.data_controller.IdFormatter;
import com.example.picro.data_model.PaymentRecord;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TopUpModul {

    private TopUpHandler topUpHandler;

    // firebase database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference topup;

    // variable data
    private String topUpFrom;
    private String topUpTo;
    private int amount;

    public void setTopUpFrom(String topUpFrom) {
        this.topUpFrom = topUpFrom;
    }

    public void setTopUpTo(String topUpTo) {
        this.topUpTo = topUpTo;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    // interface setter
    public void setTopUpHandler(TopUpHandler topUpHandler){
        this.topUpHandler = topUpHandler;
    }

    public TopUpModul(){

    }

    public void topUp(){

        // making model
        PaymentRecord recordModel = new PaymentRecord();
        recordModel.setFrom(topUpFrom);
        recordModel.setTo(topUpTo);
        recordModel.setAmount(amount);
        recordModel.setType("TOPUP_TEMPORARY");
        recordModel.setTimestamp(IdFormatter.timestamp());
        recordModel.setDaterecord(IdFormatter.getDate());
        recordModel.setId("TOPUP_TEMPOR" + String.valueOf(amount) + String.valueOf(IdFormatter.timestamp()));

        // making top up payment
        topup = database.getReference("tempor_payment/" + topUpTo);
        topup.setValue(recordModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                topUpHandler.topUpStats("SUCCESS");
            }
        });
    }


    public interface TopUpHandler{
        public void topUpStats(String status);
    }
}
