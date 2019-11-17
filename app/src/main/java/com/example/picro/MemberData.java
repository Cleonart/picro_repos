package com.example.picro;

public class MemberData {

    // data construct
    private String username;
    private int auth_code;
    private String pica_uid;
    private String pica_serial_number;

    public MemberData(){

    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    // auth code
    public int getAuth_code() {
        return auth_code;
    }

    public void setAuth_code(int auth_code) {
        this.auth_code = auth_code;
    }

    // pica uid
    public String getPica_uid() {
        return pica_uid;
    }

    public void setPica_uid(String pica_uid) {
        this.pica_uid = pica_uid;
    }

    // pica serial number
    public String getCard_serial_number() {
        return pica_serial_number;
    }

    public void setCard_serial_number(String card_serial_number) {
        this.pica_serial_number = card_serial_number;
    }
}
