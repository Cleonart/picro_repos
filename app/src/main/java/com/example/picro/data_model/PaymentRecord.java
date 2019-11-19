package com.example.picro.data_model;

public class PaymentRecord{

    // variable data
    private String from;
    private String to;
    private String type;
    private String date_record;
    private int amount;
    private double timestamp;

    public PaymentRecord(){ }

    // function
    public void setFrom(String from) {
        this.from = from;
    }
    public String getFrom() {
        return from;
    }

    public void setTo(String to) {
        this.to = to;
    }
    public String getTo() {
        return to;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
    public int getAmount() {
        return amount;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }

    public void setDaterecord(String date_record) {
        this.date_record = date_record;
    }
    public String getDaterecord() {
        return date_record;
    }

    public void setTimestamp(double timestamp) {
        this.timestamp = timestamp;
    }
    public double getTimestamp() {
        return timestamp;
    }
}
