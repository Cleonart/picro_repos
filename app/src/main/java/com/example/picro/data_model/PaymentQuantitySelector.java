package com.example.picro.data_model;

public class PaymentQuantitySelector {

    private String instance;
    private int coinImage;
    private int index;

    public PaymentQuantitySelector(String instance, int coinImage, int index){
        this.instance = instance;
        this.coinImage = coinImage;
        this.index = index;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public int getCoinImage() {
        return coinImage;
    }

    public void setCoinImage(int coinImage) {
        this.coinImage = coinImage;
    }

    public int getIndex() {
        return index;
    }
}
