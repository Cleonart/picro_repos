package com.example.picro;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import java.util.ArrayList;
import android.widget.LinearLayout;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Intent intentSettings;

    private RecyclerView rvRecord;
    private ArrayList<RecordData> list = new ArrayList<>();

    // ON CREATE
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setActionBarTitle("Picro | List");
        disableActionBar();

        // CIRCLE IMAGE VIEW
        CircleImageView profile = findViewById(R.id.profile_photo);
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

        if(selector == R.id.profile_photo){
            intentSettings = new Intent(MainActivity.this, ActivitySplash.class);
        }

        // MENU BAYAR - ActivityPay
        else if(selector == R.id.menu_bayar){
            intentSettings = new Intent(MainActivity.this, ActivityPay.class);
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
        else if(selector == R.id.menu_petunjuk){
            intentSettings = new Intent(MainActivity.this, ActivityHelp.class);
        }

        startActivity(intentSettings);

    }

}

