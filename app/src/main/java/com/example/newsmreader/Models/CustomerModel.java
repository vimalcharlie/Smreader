package com.example.newsmreader.Models;

import java.io.Serializable;

public class CustomerModel implements Serializable {

    private String CustomerID;
    private String Consumer;
    private String consumer_no;
    private String mobile;
    private String address;

    private String Location;
    private String category;
    private String meter_number;
    private String PrevReading;
    private String PrevBalance;
    private String AdditionalCharges;
    private String ServiceCharge;
    private String EnableServiceCharge;
    private String Latefee;
    private String Minrent;
    private String catid;
    private String PrevReadingDate;
    private  String PrevBillDate;
    private String BasedonReadDate;
    private String Days;

    private String Passwords;

    private String NoofMonths;

    private String Latitude;
    private String Longitude;
    private  String readingexists;

    public CustomerModel(String customerID, String consumer, String consumer_no, String mobile, String address, String location, String category, String meter_number, String prevReading, String prevBalance, String additionalCharges, String serviceCharge, String enableServiceCharge, String latefee, String minrent, String catid, String prevReadingDate, String prevBillDate, String basedonReadDate, String days, String passwords, String noofMonths, String latitude, String longitude,String readingexists) {
        CustomerID = customerID;
        Consumer = consumer;
        this.consumer_no = consumer_no;
        this.mobile = mobile;
        this.address = address;
        Location = location;
        this.category = category;
        this.meter_number = meter_number;
        PrevReading = prevReading;
        PrevBalance = prevBalance;
        AdditionalCharges = additionalCharges;
        ServiceCharge = serviceCharge;
        EnableServiceCharge = enableServiceCharge;
        Latefee = latefee;
        Minrent = minrent;
        this.catid = catid;
        PrevReadingDate = prevReadingDate;
        PrevBillDate = prevBillDate;
        BasedonReadDate = basedonReadDate;
        Days = days;
        Passwords = passwords;
        NoofMonths = noofMonths;
        Latitude = latitude;
        Longitude = longitude;
        this.readingexists=readingexists;
    }


    public String getReadingexists() {
        return readingexists;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public String getConsumer() {
        return Consumer;
    }

    public String getConsumer_no() {
        return consumer_no;
    }

    public String getMobile() {
        return mobile;
    }

    public String getAddress() {
        return address;
    }

    public String getLocation() {
        return Location;
    }

    public String getCategory() {
        return category;
    }

    public String getMeter_number() {
        return meter_number;
    }

    public String getPrevReading() {
        return PrevReading;
    }

    public String getPrevBalance() {
        return PrevBalance;
    }

    public String getAdditionalCharges() {
        return AdditionalCharges;
    }

    public String getServiceCharge() {
        return ServiceCharge;
    }

    public String getEnableServiceCharge() {
        return EnableServiceCharge;
    }

    public String getLatefee() {
        return Latefee;
    }

    public String getMinrent() {
        return Minrent;
    }

    public void setMinrent(String minrent) {
        Minrent = minrent;
    }

    public String getCatid() {
        return catid;
    }

    public String getPrevReadingDate() {
        return PrevReadingDate;
    }

    public String getPrevBillDate() {
        return PrevBillDate;
    }

    public String getBasedonReadDate() {
        return BasedonReadDate;
    }

    public String getDays() {
        return Days;
    }

    public String getPasswords() {
        return Passwords;
    }

    public String getNoofMonths() {
        return NoofMonths;
    }

    public String getLatitude() {
        return Latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

     ///////////*******Setters ****************//////
    public void setMeter_number(String meter_number) {
        this.meter_number = meter_number;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setServiceCharge(String serviceCharge) {
        ServiceCharge = serviceCharge;
    }

    public void setEnableServiceCharge(String enableServiceCharge) {
        EnableServiceCharge = enableServiceCharge;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }
}
