package com.example.picro;

public class RecordData {

    // VARIABLE DECLARATION
    private int record_index;
    private String record_id;
    private String record_detail;
    private int record_photo;

    public RecordData(int r_index, String r_id, String r_detail, int r_image){
        this.record_index  = r_index;
        this.record_id     = r_id;
        this.record_detail = r_detail;
        this.record_photo  = r_image;
    }

    // GET METHOD
    public int    getIndex()  { return record_index; }
    public String getId()     { return record_id; }
    public String getDetail() { return record_detail; }
    public int    getImage()  { return record_photo; }

}
