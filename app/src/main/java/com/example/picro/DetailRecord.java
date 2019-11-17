package com.example.picro;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;

public class DetailRecord extends AppCompatActivity{

    public static final String INDEX = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_item_detail);
        disableActionBar();

        // DECLARATION OF TEXT VIEW
        TextView  record_id     = findViewById(R.id.record_id);
        TextView  record_detail = findViewById(R.id.record_detail);
        ImageView record_image  = findViewById(R.id.record_image);

        String id = getIntent().getStringExtra(INDEX);
        int id_data = getIntent().getIntExtra(INDEX, 999);

        record_id.setText(ActivityData.getId(id_data));
        record_detail.setText(ActivityData.getData(id_data));
        record_image.setImageResource(ActivityData.getImage(id_data));

        RelativeLayout backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void disableActionBar(){
        getSupportActionBar().hide();
    }
}
