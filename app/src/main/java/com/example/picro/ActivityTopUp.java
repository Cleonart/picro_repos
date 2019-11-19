package com.example.picro;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import me.abhinay.input.CurrencyEditText;

public class ActivityTopUp extends AppCompatActivity implements View.OnClickListener{

    CurrencyEditText etInput;
    LinearLayout rp_9000,rp_19000,rp_29000;
    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topup);
        disableActionBar();
        buttonListener();
        etInputInitialize();
    }

    private void disableActionBar(){
        getSupportActionBar().hide();
    }

    // BUTTON LISTENER
    public void buttonListener(){
        backButton = findViewById(R.id.back);
        backButton.setOnClickListener(this);

        rp_9000 = findViewById(R.id.rp_9000);
        rp_9000.setOnClickListener(this);

        rp_19000 = findViewById(R.id.rp_19000);
        rp_19000.setOnClickListener(this);

        rp_29000 = findViewById(R.id.rp_29000);
        rp_29000.setOnClickListener(this);

    }

    // INPUT TYPE INITIALIZATION
    private void etInputInitialize(){
        etInput = findViewById(R.id.etInput);
        etInput.setCurrency("Rp ");
        etInput.setDelimiter(false);
        etInput.setSpacing(false);
        etInput.setDecimals(false);
        //Make sure that Decimals is set as false if a custom Separator is used
        etInput.setSeparator(",");
    }

    // ON CLICK OVERRIDE
    @Override
    public void onClick(View view) {

        switch(view.getId()){

            case R.id.back:
                finish();
                break;

            case R.id.rp_9000:
                etInput.setText(R.string.rp_9000);
                break;

            case R.id.rp_19000:
                etInput.setText(R.string.rp_19000);
                break;

            case R.id.rp_29000:
                etInput.setText(R.string.rp_29000);
                break;
        }

    }

}
