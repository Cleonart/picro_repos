package com.example.picro.data_controller;

import java.text.SimpleDateFormat;
import java.util.Date;

public class IdFormatter {

    public static String paymentId() {
        SimpleDateFormat sf = new SimpleDateFormat("ddMMyyyHHmmss");
        String data = "PAYMENT" + String.valueOf(sf.format(new Date()));
        return data;
    }

    public static String topUpId(){
        SimpleDateFormat sf = new SimpleDateFormat("ddMMyyyHHmmss");
        String data = "TOPUP_TEMPOR" + String.valueOf(sf.format(new Date()));
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

    public static int timestamp(){
        SimpleDateFormat sa = new SimpleDateFormat("HH");
        SimpleDateFormat sb = new SimpleDateFormat("mm");
        SimpleDateFormat sc = new SimpleDateFormat("ss");

        int th = Integer.parseInt(String.valueOf(sa.format(new Date())));
        int tm = Integer.parseInt(String.valueOf(sb.format(new Date())));
        int ts = Integer.parseInt(String.valueOf(sc.format(new Date())));

        th = th * 3600;
        tm = tm * 60;

        int times = th + tm + ts;

        return times;
    }

    public static int timestampDifference(int now, int from){
        int latency = (now - from) / 60;
        return latency;
    }

    public static int timestampraw(int now, int from){
        int latency = Integer.parseInt(String.valueOf(now - from));
        return latency;
    }
}
