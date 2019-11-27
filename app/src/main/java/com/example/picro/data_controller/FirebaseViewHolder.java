package com.example.picro.data_controller;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.picro.R;

public class FirebaseViewHolder extends RecyclerView.ViewHolder {

    public TextView id, from,to, type,date_record,amount,timestamp;
    public ConstraintLayout layout_cons;

    public FirebaseViewHolder(@NonNull View itemView) {
        super(itemView);
        layout_cons = itemView.findViewById(R.id.container_layout);
        id = itemView.findViewById(R.id.tv_id);
        from = itemView.findViewById(R.id.tv_from);
        to   = itemView.findViewById(R.id.tv_to);
        type = itemView.findViewById(R.id.tv_type);
        date_record = itemView.findViewById(R.id.tv_date);
        amount = itemView.findViewById(R.id.tv_amount);
        timestamp = itemView.findViewById(R.id.tv_timestamp);
    }
}
