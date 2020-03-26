package com.example.picro;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.picro.activity_modul.ActivityScanner;
import com.example.picro.data_controller.DecimalFormater;
import com.example.picro.data_controller.FirebaseController;
import com.example.picro.data_controller.FirebaseViewHolder;
import com.example.picro.data_model.PaymentRecord;
import com.example.picro.interface_passenger.ActivityTopUp;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, FirebaseController.ResultHandler{

    Intent intentSettings;
    FirebaseController controller = new FirebaseController();
    FirebaseDatabase databases = FirebaseDatabase.getInstance();
    DatabaseReference ref;
    private RecyclerView rvRecord;
    private ArrayList<PaymentRecord> list = new ArrayList<>();
    FirebaseRecyclerOptions<PaymentRecord> options;
    FirebaseRecyclerAdapter<PaymentRecord, FirebaseViewHolder> adapters;

    String uid, serial, username;
    int amount;
    TextView text_amount, protection_label, text_username;
    ConstraintLayout protection_body;

    // ON CREATE
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setActionBarTitle("Picro | List");
        disableActionBar();

        SpinKitView spins  = findViewById(R.id.spinKit);
        spins.setVisibility(View.VISIBLE);

        LinearLayout fund = findViewById(R.id.funds);
        fund.setVisibility(View.INVISIBLE);

        // set the setter
        controller.setResultHandler(this);

        /** start get shared prefenences data **/
        // dont touch this part!!
        uid      = getShareData("UID");
        serial   = getShareData("SERIAL");
        username = getShareData("UNAME");
        /** end get shared prefenences data **/

        /** element initialization */
        elementInit();

        /** start get amount of user balance **/
        controller.getChildValueRealTime("picro_cards_manifest/" + serial + "/pica_amount");
        /** end get amount of user balance **/

        // RV RECORD DATA
        //rvRecord = findViewById(R.id.rv_record);
        //rvRecord.setNestedScrollingEnabled(false);
        //list.addAll(ActivityData.getListData());
        //showRecyclerList();
    }

    // SHOW RECYCLER LIST
    private void showRecyclerList() {
    /*
        rvRecord.setLayoutManager(new LinearLayoutManager(this));
        DataAdaperList dataAdaperList = new DataAdaperList(list);
        rvRecord.setAdapter(dataAdaperList);

        dataAdaperList.setOnItemClickCallback(new DataAdaperList.OnItemClickCallback() {
            @Override
            public void onItemClicked(RecordData data) {
                showSelectedItem(data);
            }
        });*/
    }

    // button init
    private void elementInit(){

        /** logout button **/
        ImageView logout = findViewById(R.id.power_off);
        logout.setOnClickListener(this);

        /** button bayar **/
        LinearLayout button_bayar = findViewById(R.id.menu_bayar);
        button_bayar.setOnClickListener(this);

        /** button topup **/
        LinearLayout button_topup = findViewById(R.id.menu_topup);
        button_topup.setOnClickListener(this);

        /** button transfer **/
        LinearLayout button_tranf = findViewById(R.id.menu_transfer);
        button_tranf.setOnClickListener(this);

        /** button help **/
        LinearLayout button_helps = findViewById(R.id.menu_petunjuk);
        button_helps.setOnClickListener(this);

        /** start text view and label declaration **/
        text_amount      = findViewById(R.id.amount);
        protection_body  = findViewById(R.id.protection_body);
        protection_label = findViewById(R.id.protection_label);
        text_username    = findViewById(R.id.greet);
        String greeting  = "Halo, " + username;
        text_username.setText(greeting);
        /** end text view and label declaration **/

        /** start recycler view identifier **/
        rvRecord = findViewById(R.id.rv_record);
        rvRecord.setNestedScrollingEnabled(false);
        rvRecord.setLayoutManager(new LinearLayoutManager(this));

        ref = databases.getReference("picro_payment/" + serial);

        options = new FirebaseRecyclerOptions.Builder<PaymentRecord>().setQuery(ref, PaymentRecord.class).build();

        adapters = new FirebaseRecyclerAdapter<PaymentRecord, FirebaseViewHolder>(options) {

            @Override
            protected void onBindViewHolder(FirebaseViewHolder holder, int i, PaymentRecord model) {

                holder.to.setText(model.getTo());
                holder.from.setText(model.getFrom());
                holder.timestamp.setText(String.valueOf(model.getTimestamp()));

                if(model.getType().equals("PAYMENT")){
                    holder.id.setText("Bayar Mikrolet");
                    holder.amount.setText("- Rp. " + String.valueOf(DecimalFormater.goToDecimal(model.getAmount())));
                    holder.amount.setTextColor(getResources().getColor(R.color.danger));
                }

                else if(model.getType().equals("TOPUP")){
                    holder.id.setText("Top Up Saldo");
                    holder.amount.setText("+ Rp. " + String.valueOf(DecimalFormater.goToDecimal(model.getAmount())));
                    holder.amount.setTextColor(getResources().getColor(R.color.colorPrimary));
                }

                holder.date_record.setText(model.getDaterecord());
                holder.type.setText(model.getType());
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
    protected void onStart() {
        super.onStart();
        if(adapters!=null){
            adapters.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adapters!=null){
            adapters.stopListening();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(adapters!=null){
            adapters.startListening();
        }
    }

    // set action bar
    private void setActionBarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    /* show selected item
    private void showSelectedItem(RecordData record) {
        intentSettings = new Intent(MainActivity.this, DetailRecord.class);
        intentSettings.putExtra(DetailRecord.INDEX,record.getIndex());
        startActivity(intentSettings);
    }*/

    // disable action bar
    private void disableActionBar(){
        getSupportActionBar().hide();
    }

    // menu navigation
    @Override
    public void onClick(View view) {

        int selector = view.getId();

        if(selector == R.id.power_off){
            intentSettings = new Intent(MainActivity.this, ActivitySplash.class);
            SharedPreferences shared = getSharedPreferences("rootUser", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = shared.edit();
            editor.clear();
            editor.commit();
            startActivity(intentSettings);
            finish();
        }

        // MENU BAYAR
        else if(selector == R.id.menu_bayar){
            intentSettings = new Intent(MainActivity.this, ActivityScanner.class);
            Bundle extras = new Bundle();
            extras.putString("SCANNER_MODE", "PAYMENT");
            intentSettings.putExtras(extras);
            startActivity(intentSettings);
        }

        // MENU TOPUP - ActivityTopUp
        else if(selector == R.id.menu_topup){
            intentSettings = new Intent(MainActivity.this, ActivityTopUp.class);
            startActivity(intentSettings);
        }

        // MENU TRANSFER - ActivityTransfer
        else if(selector == R.id.menu_transfer){
            intentSettings = new Intent(MainActivity.this, ActivityTransfer.class);
            startActivity(intentSettings);
        }

        // MENU HELP - ActivityHelp
        else if(selector == R.id.menu_petunjuk) {
            intentSettings = new Intent(MainActivity.this, ActivityHelp.class);
            startActivity(intentSettings);
        }

    }

    @Override
    public void setValueStatus(String path, int status) {}

    /*--- start shared preferences ---*/

    @Override
    public void valueListener(String path, DataSnapshot data) {

        amount = Integer.parseInt(String.valueOf(data.getValue()));
        String fix_amount = "";

        if(amount >= 0){
            fix_amount = "Rp. " + String.valueOf(DecimalFormater.goToDecimal(amount));
            protection_body.setBackgroundResource(R.drawable.rounded_menu_green);
            protection_label.setText("Cash Protection aktif");
        }

        else{
            fix_amount = "Rp. 0";
            protection_body.setBackgroundResource(R.drawable.rounded_menu_danger);
            protection_label.setText("Cash Protection tidak aktif");
        }

        SpinKitView spins  = findViewById(R.id.spinKit);
        spins.setVisibility(View.INVISIBLE);

        LinearLayout fund = findViewById(R.id.funds);
        fund.setVisibility(View.VISIBLE);

        text_amount.setText(fix_amount);

    }

    // GET SHARE DATA
    public String getShareData(String selector){
        SharedPreferences shared = getSharedPreferences("rootUser", Context.MODE_PRIVATE);
        return shared.getString(selector, null);
    }

    /*--- end shared preferences ---*/
}

