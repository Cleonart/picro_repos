package com.example.picro.data_controller;

import java.text.SimpleDateFormat;
import java.util.Date;

public class IdFormatter {

    public static String paymentId() {
        SimpleDateFormat sf = new SimpleDateFormat("ddMMyyyHHmmss");
        String data = "PAYMENT" + String.valueOf(sf.format(new Date()));
        return data;
    }

    public static String getDate() {
        SimpleDateFormat sf = new SimpleDateFormat("dd.MM.yyyy");
        String data = String.valueOf(sf.format(new Date()));
        return data;
    }

    public static String getTime() {
        SimpleDateFormat date = new SimpleDateFormat("HH mm ss");
        String current = String.valueOf(date.format(new Date()));
        return current;
    }

    public static double timestamp(){
        SimpleDateFormat sa = new SimpleDateFormat("ddMMyyyHHmmss");
        double ts = Double.parseDouble(String.valueOf(sa.format(new Date())));
        return ts;
    }

}
