package com.example.picro;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.view.View;

import java.util.ArrayList;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.picro.data_controller.DecimalFormater;
import com.example.picro.data_controller.FirebaseController;
import com.google.firebase.database.DataSnapshot;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, FirebaseController.ResultHandler{

    Intent intentSettings;
    FirebaseController controller = new FirebaseController();
    private RecyclerView rvRecord;
    private ArrayList<RecordData> list = new ArrayList<>();

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

        controller.setResultHandler(this);

        uid    = getShareData("UID");
        serial = getShareData("SERIAL");
        username = getShareData("UNAME");

        text_amount = findViewById(R.id.amount);
        protection_body = findViewById(R.id.protection_body);
        protection_label = findViewById(R.id.protection_label);
        text_username    = findViewById(R.id.greet);
        String greeting = "Halo, " + username;
        text_username.setText(greeting);

        // init button
        buttonInit();

        // real time data set
        //controller.getChildValueOneTime("picro_passengers/" + uid + "/username"); //get name
        controller.getChildValueRealTime("picro_cards_manifest/" + serial + "/pica_amount");

        // RV RECORD DATA
        rvRecord = findViewById(R.id.rv_record);
        rvRecord.setNestedScrollingEnabled(false);
        list.addAll(ActivityData.getListData());
        showRecyclerList();
    }

    // SHOW RECYCLER LIST
    private void showRecyclerList() {
        rvRecord.setLayoutManager(new LinearLayoutManager(this));
        DataAdaperList dataAdaperList = new DataAdaperList(list);
        rvRecord.setAdapter(dataAdaperList);

        dataAdaperList.setOnItemClickCallback(new DataAdaperList.OnItemClickCallback() {
            @Override
            public void onItemClicked(RecordData data) {
                showSelectedItem(data);
            }
        });
    }

    // button init
    private void buttonInit(){
        // CIRCLE IMAGE VIEW
        CircleImageView profile = findViewById(R.id.power_off);
        profile.setOnClickListener(this);

        // BUTTON BAYAR
        LinearLayout button_bayar = findViewById(R.id.menu_bayar);
        button_bayar.setOnClickListener(this);

        // BUTTON TOPUP
        LinearLayout button_topup = findViewById(R.id.menu_topup);
        button_topup.setOnClickListener(this);

        // BUTTON TRANSFER
        LinearLayout button_tranf = findViewById(R.id.menu_transfer);
        button_tranf.setOnClickListener(this);

        // BUTTON HELP
        LinearLayout button_helps = findViewById(R.id.menu_petunjuk);
        button_helps.setOnClickListener(this);
    }

    // SET ACTION BAR
    private void setActionBarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    // SHOW SELECTED ITEM
    private void showSelectedItem(RecordData record) {
        intentSettings = new Intent(MainActivity.this, DetailRecord.class);
        intentSettings.putExtra(DetailRecord.INDEX,record.getIndex());
        startActivity(intentSettings);
    }

    // DISABLE ACTION BAR
    private void disableActionBar(){
        getSupportActionBar().hide();
    }

    // MENU NAVIGATION
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

        // MENU BAYAR - ActivityPay
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
        }

        // MENU TRANSFER - ActivityTransfer
        else if(selector == R.id.menu_transfer){
            intentSettings = new Intent(MainActivity.this, ActivityTransfer.class);
        }

        // MENU HELP - ActivityHelp
        else if(selector == R.id.menu_petunjuk) {
            intentSettings = new Intent(MainActivity.this, ActivityHelp.class);
        }


    }

    @Override
    public void setValueStatus(String path, int status) {}

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

        text_amount.setText(fix_amount);
    }

    // GET SHARE DATA
    public String getShareData(String selector){
        SharedPreferences shared = getSharedPreferences("rootUser", Context.MODE_PRIVATE);
        return shared.getString(selector, null);
    }
}

