package com.example.newsmreader.Models;

public class SettingModel {
    private String enable_payment;
    private String ReadBillnumber;
    private String Billnumber;
    private String image_id;

    private String CollectioninPrint;
    private  String Online;
    private  String PoweredBy;


    public String getPoweredBy(){
        return PoweredBy;
    }
    public String getOnline() {
        return Online;
    }

    public String getImage_id() {
        return image_id;
    }

    public String getCollectioninPrint() {
        return CollectioninPrint;
    }

    public String getEnable_payment() {
        return enable_payment;
    }

    public String getReadBillnumber() {
        return ReadBillnumber;
    }

    public String getBillnumber() {
        return Billnumber;
    }
}
