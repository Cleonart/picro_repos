package com.example.picro;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseController{

    private ResultHandler resultHandler;
    FirebaseDatabase firebase = FirebaseDatabase.getInstance();
    DatabaseReference reference_set;
    DatabaseReference reference_get;

    // CONSTRUCTOR
    public FirebaseController() {

    }

    // INTERFACE SETTER
    public void setResultHandler(ResultHandler resultHandler) {
        this.resultHandler = resultHandler;
    }

    // firebase set value
    public void setValue(final String path, String data) {
        reference_set = firebase.getReference(path);
        reference_set.setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                resultHandler.setValueStatus(path, 1);
            }
        });
    }

    // firebase set object value
    public void insertValueObject(final String path, Object data) {
        reference_set = firebase.getReference(path);
        reference_set.setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                resultHandler.setValueStatus(path, 1);
            }
        });
    }

    // get child value one time
    public void getChildValueOneTime(final String path) {
        reference_get = firebase.getReference();
        DatabaseReference r_data = reference_get.child(path);
        r_data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                resultHandler.valueListener(path, dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //resultHandler.valueListener(path, databaseError.toString());
            }
        });

    }

    // get value real time
    public void getChildValueRealTime(final String path){
        reference_get = firebase.getReference();
        DatabaseReference r_data = reference_get.child(path);
        r_data.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                resultHandler.valueListener(path, dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //resultHandler.valueListener("", daaa);
            }
        });

    }

    // FIREBASE CONTROLLER
    public interface ResultHandler{
        void setValueStatus(String path, int status);
        void valueListener(String path, DataSnapshot data);
    }


}
