package com.example.picro;

public class UniversalPicroData {

    static private int pica_state = 0;

    public static void setPica_state(int pica) {
        pica_state = pica;
    }

    public static boolean getPica_state() {

        if(pica_state == 0){
            return false;
        }

        else{
            return true;
        }

    }
}
