package com.example.picro.data_model;

public class PaymentRecord{

    // variable data
    private String id;
    private String from;
    private String to;
    private String type;
    private String date_record;
    private int amount;
    private double timestamp;

    public PaymentRecord(){ }

    // id
    public String getId() { return id; }
    public void setId(String id) { this.id = id;  }

    // from
    public void setFrom(String from) {
        this.from = from;
    }
    public String getFrom() {
        return from;
    }

    // to
    public void setTo(String to) {
        this.to = to;
    }
    public String getTo() {
        return to;
    }

    // amount
    public void setAmount(int amount) {
        this.amount = amount;
    }
    public int getAmount() {
        return amount;
    }

    // type
    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }

    // date record
    public void setDaterecord(String date_record) {
        this.date_record = date_record;
    }
    public String getDaterecord() {
        return date_record;
    }

    // timestamp
    public void setTimestamp(double timestamp) {
        this.timestamp = timestamp;
    }
    public double getTimestamp() {
        return timestamp;
    }
}
