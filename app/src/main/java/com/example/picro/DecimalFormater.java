package com.example.picro;

import java.text.DecimalFormat;

public class DecimalFormater {

    static private DecimalFormat formater;

    public static String goToDecimal(int value){

        if(value <= 9999){
            formater = new DecimalFormat("0,000");
        }

        else if(value <= 99999){
            formater = new DecimalFormat("00,000");
        }

        else if(value <= 999999){
            formater = new DecimalFormat("000,000");
        }

        return String.valueOf(formater.format(value));
    }

}
