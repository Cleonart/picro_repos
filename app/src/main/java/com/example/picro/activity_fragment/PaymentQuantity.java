package com.example.picro.activity_fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.picro.R;
import com.example.picro.data_model.PaymentQuantitySelector;

import java.util.ArrayList;

public class PaymentQuantity extends ArrayAdapter<PaymentQuantitySelector> {

    public PaymentQuantity(Context context, ArrayList<PaymentQuantitySelector> selectorArrayList){
         super(context, 0, selectorArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int pos, View convertView, ViewGroup parent){

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.spinner_item,parent, false
            );
        }

        ImageView coin = convertView.findViewById(R.id.icon_data);
        TextView instance = convertView.findViewById(R.id.item);

        PaymentQuantitySelector current = getItem(pos);

        if(current != null) {
            coin.setImageResource(current.getCoinImage());
            instance.setText(current.getInstance());
        }

        return convertView;
    }
}
