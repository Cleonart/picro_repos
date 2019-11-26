package com.example.picro.activity_fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.picro.R;

public class TopUpStatus extends AppCompatActivity {

    private CountDownTimer countDownTimer;
    private long timeLeftInMilliseconds;
    TextView data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topup_status);
        getSupportActionBar().hide();

        int seconds_      = 0;
        int minutes_      = 0;

        // get bundles extra
        Bundle extras    = getIntent().getExtras();
        float time_raw   = Float.parseFloat(String.valueOf(extras.getString("TIME_RAW")));
        String amount    = extras.getString("AMOUNT");
        minutes_          = (int) time_raw;

        // convert data time raw
        time_raw = ((time_raw - minutes_) * 0.6f) * 60000; // get the seconds
        seconds_  = (int) time_raw;
        minutes_  = (30 - minutes_) * 60000;               // get the minutes

        timeLeftInMilliseconds = minutes_ + seconds_;

        // subcaption data
        String stats = "Silahkan selesaikan pembayaran anda berjumlah Rp. " + amount + " sebelum batas waktu dibawah";
        TextView status = findViewById(R.id.sub_status);
        status.setText(stats);

        // back button
        RelativeLayout backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        data = findViewById(R.id.timer);
        data.setText(String.valueOf(timeLeftInMilliseconds));

        countDownTimer = new CountDownTimer(timeLeftInMilliseconds, 1000) {

            @Override
            public void onTick(long data_time) {
                long minutes = data_time / 60000;
                int seconds = (int) data_time % 60000;
                seconds = seconds / 1000;

                String time_left;
                time_left = "";

                if(minutes < 10){
                    time_left += "0";
                }
                time_left += minutes;
                time_left+= " : ";

                if(seconds < 10){
                    time_left += "0";
                }

                time_left += seconds;
                data.setText(time_left);

                data.setText(String.valueOf(time_left));
            }

            @Override
            public void onFinish() {

            }
        };

        countDownTimer.start();

    }

    public void startCount(){



    }

    public void updateTimer(){


    }
}
