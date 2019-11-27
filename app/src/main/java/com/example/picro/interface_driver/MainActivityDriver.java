package com.example.picro.interface_driver;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.picro.ActivityRegister;
import com.example.picro.ActivitySplash;
import com.example.picro.R;
import com.example.picro.activity_modul.ActivityScanner;
import com.example.picro.data_controller.DecimalFormater;
import com.example.picro.data_controller.FirebaseViewHolder;
import com.example.picro.data_controller.IdFormatter;
import com.example.picro.data_model.PaymentRecord;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivityDriver extends AppCompatActivity {

    FirebaseDatabase firebase = FirebaseDatabase.getInstance();
    DatabaseReference getSaldo;
    DatabaseReference getTopup;
    String uid, serial, username;
    TextView wallet_amount, cash_amount;
    Intent intentSettings;

    SpinKitView spin_1, spin_2;

    private RecyclerView rvRecord;
    private ArrayList<PaymentRecord> list = new ArrayList<>();
    FirebaseRecyclerOptions<PaymentRecord> options;
    FirebaseRecyclerAdapter<PaymentRecord, FirebaseViewHolder> adapters;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_driver);
        getSupportActionBar().hide();

        /** start get shared prefenences data **/
        // dont touch this part!!
        uid      = getShareData("UID");
        serial   = getShareData("SERIAL");
        username = getShareData("UNAME");
        /** end get shared prefenences data **/

        spin_1 = findViewById(R.id.spinKit);
        spin_2 = findViewById(R.id.spinKit_2);

        wallet_amount = findViewById(R.id.wallet_amount);
        cash_amount   = findViewById(R.id.cash_amount);

        recyclerViewUpdate();

        // get saldo data
        getSaldo = firebase.getReference("picro_cards_manifest/" + uid);
        getSaldo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int amount_fix = Integer.parseInt(String.valueOf(dataSnapshot.child("amount").getValue()));          // get the digital wallet amount
                int cash       = Integer.parseInt(String.valueOf(dataSnapshot.child("cash").getValue()));            // get the cash amount
                amount_fix     = amount_fix - cash;                                                       // to get the exact amount of digital wallet
                                                                                                          // we need to substract amount_fix with cash
                if(amount_fix <= 0){
                    amount_fix = 0;                                                                       // if the substraction result return negative we need to set it to 0
                }

                String wallet_fix = "Rp. " + DecimalFormater.goToDecimal(amount_fix);
                String cash_fix   = "Rp. " + DecimalFormater.goToDecimal(cash);

                spin_1.setVisibility(View.INVISIBLE);
                spin_2.setVisibility(View.INVISIBLE);

                wallet_amount.setText(wallet_fix);
                cash_amount.setText(cash_fix);

                wallet_amount.setVisibility(View.VISIBLE);
                cash_amount.setVisibility(View.VISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // button logout
        CircleImageView logout = findViewById(R.id.power_off_button);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentSettings = new Intent(MainActivityDriver.this, ActivitySplash.class);
                SharedPreferences shared = getSharedPreferences("rootUser", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = shared.edit();
                editor.clear();
                editor.commit();
                startActivity(intentSettings);
                finish();
            }
        });
    }

    private void recyclerViewUpdate(){

        /** start recycler view identifier **/
        rvRecord = findViewById(R.id.rv_record);
        rvRecord.setNestedScrollingEnabled(false);
        rvRecord.setLayoutManager(new LinearLayoutManager(this));

        final DatabaseReference ref = firebase.getReference("tempor_payment");

        Query query = ref.orderByChild("from").equalTo(serial);

        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Query io = ref.orderByChild("daterecord").equalTo(IdFormatter.getDate());

                options = new FirebaseRecyclerOptions.Builder<PaymentRecord>().setQuery(io, PaymentRecord.class).build();
                adapters = new FirebaseRecyclerAdapter<PaymentRecord, FirebaseViewHolder>(options) {

                    @Override
                    protected void onBindViewHolder(final FirebaseViewHolder holder, int i, final PaymentRecord model) {

                        DatabaseReference user = firebase.getReference("picro_passengers/" + model.getTo() + "/username");
                        user.addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                                holder.to.setText(model.getTo());
                                holder.from.setText(model.getFrom());
                                holder.timestamp.setText(String.valueOf(model.getTimestamp()));

                                if(model.getType().equals("TOPUP_TEMPORARY")){
                                    holder.id.setText("Request Top Up Saldo");
                                    holder.amount.setText("* Rp. " + String.valueOf(DecimalFormater.goToDecimal(model.getAmount())));
                                    holder.amount.setTextColor(getResources().getColor(R.color.colorPrimary));
                                }

                                holder.date_record.setText(String.valueOf(dataSnapshot.getValue()) + " | " + model.getDaterecord());
                                holder.type.setText(model.getType());

                                holder.layout_cons.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        intentSettings = new Intent(MainActivityDriver.this, TopUpVerify.class);
                                        Bundle extras = new Bundle();
                                        extras.putString("PAYMENT_ID", model.getTo());
                                        extras.putString("USERNAME", String.valueOf(dataSnapshot.getValue()));
                                        extras.putString("AMOUNT", String.valueOf(model.getAmount()));
                                        intentSettings.putExtras(extras);
                                        startActivity(intentSettings);
                                    }
                                });

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }

                    @NonNull
                    @Override
                    public FirebaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_item_row,parent,false);
                        return new FirebaseViewHolder(view);
                    }
                };

                adapters.startListening();
                rvRecord.setAdapter(adapters);
                /** end recycler view identifier **/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public String getShareData(String selector){
        SharedPreferences shared = getSharedPreferences("rootUser", Context.MODE_PRIVATE);
        return shared.getString(selector, null);
    }

}
