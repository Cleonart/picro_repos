package com.example.picro.data_controller;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class QrValidator {

    private FirebaseDatabase firebaseController = FirebaseDatabase.getInstance();
    private Boolean card_validated = false;

    public QrValidator(){
    }

    public Boolean validateQrPassenger(String qr_data){

        Query query = firebaseController.getReference("picro_cards_manifest").equalTo(qr_data);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return card_validated;
    }

    public Boolean validateQrDriver(){
        return card_validated;
    }



}
