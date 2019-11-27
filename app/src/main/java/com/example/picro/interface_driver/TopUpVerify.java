package com.example.picro.interface_driver;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.picro.R;
import com.example.picro.data_controller.DecimalFormater;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TopUpVerify extends AppCompatActivity {

    private FirebaseDatabase firebase = FirebaseDatabase.getInstance();
    String payment_id, username_intent, amount_intent;
    TextView username, amount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_topup);

        getSupportActionBar().hide();

        Bundle extras   = getIntent().getExtras();
        payment_id      = extras.getString("PAYMENT_ID");
        username_intent = extras.getString("USERNAME");
        amount_intent   = extras.getString("AMOUNT");

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
}
