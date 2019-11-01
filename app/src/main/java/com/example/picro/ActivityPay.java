package com.example.picro;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityPay extends AppCompatActivity implements View.OnClickListener {

    Button payment;
    RelativeLayout backButton;
    TextView legend;
    private int button_state = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        disableActionBar();

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(this);

        payment = findViewById(R.id.pay_button);
        payment.setOnClickListener(this);

        legend = findViewById(R.id.ap_legend);

        if(UniversalPicroData.getPica_state()){
            payment.setText(R.string.ap_button_end);
        }

        else{
            payment.setText(R.string.ap_button_start);
        }

    }

    private void disableActionBar(){
        getSupportActionBar().hide();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.backButton:
                finish();
                break;

            case R.id.pay_button:

                if(this.button_state == 0){
                    payment.setText(R.string.ap_button_end);
                    legend.setText(R.string.ap_legend_start);
                    payment.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    this.button_state = 1;
                }

                else{
                    payment.setText(R.string.ap_button_start);
                    legend.setText(R.string.ap_legend_end);
                    payment.setBackgroundColor(getResources().getColor(R.color.danger));
                    this.button_state = 0;
                }

                break;
        }

    }
}
