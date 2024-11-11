package com.example.newsmreader.Models;

import java.io.Serializable;

public class ReadModel implements Serializable {
    private String mobile;
    private String meter_number;
    private String Latitude;
    private String Longitude;
    private String Importeddate;
    private String customer_guid;
    private String reading;
    private String Previous_Bal;
    private String MinRent;
    private String latefee;
    private String Additional;
    private String read_date;
    private String read_time;
    private String owner_guid;
    private String BillAmount;
    private String SpotBilling;
    private String SpotBillingCharge;
    private String PrevReading;
    private String Billid;
    private String total;
    private  String Usage;
    private String average;

    private String hold;

    private String closed;


    public String getHold() {
        return hold;
    }

    public String getClosed() {
        return closed;
    }

    public String getTotal() {
        return total;
    }

    public String getUsage() {
        return Usage;
    }

    public ReadModel(String mobile, String meter_number, String latitude, String longitude, String importeddate, String customer_guid, String reading, String previous_Bal, String minRent, String latefee, String additional, String read_date, String read_time, String owner_guid, String billAmount, String spotBilling, String spotBillingCharge, String prevReading, String billid, String total, String Usage,String average,String hold,String closed) {
        this.mobile = mobile;
        this.meter_number = meter_number;
        Latitude = latitude;
        Longitude = longitude;
        Importeddate = importeddate;
        this.customer_guid = customer_guid;
        this.reading = reading;
        Previous_Bal = previous_Bal;
        MinRent = minRent;
        this.latefee = latefee;
        Additional = additional;
        this.read_date = read_date;
        this.read_time = read_time;
        this.owner_guid = owner_guid;
        BillAmount = billAmount;
        SpotBilling = spotBilling;
        SpotBillingCharge = spotBillingCharge;
        PrevReading = prevReading;
        Billid = billid;
        this.total=total;
        this.Usage=Usage;
        this.average=average;
        this.hold=hold;
        this.closed=closed;


    }

    public String getAverage() {
        return average;
    }

    public String getMobile() {
        return mobile;
    }

    public String getMeter_number() {
        return meter_number;
    }

    public String getLatitude() {
        return Latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public String getImporteddate() {
        return Importeddate;
    }

    public String getCustomer_guid() {
        return customer_guid;
    }

    public String getReading() {
        return reading;
    }

    public String getPrevious_Bal() {
        return Previous_Bal;
    }

    public String getMinRent() {
        return MinRent;
    }

    public String getLatefee() {
        return latefee;
    }

    public String getAdditional() {
        return Additional;
    }

    public String getRead_date() {
        return read_date;
    }

    public String getRead_time() {
        return read_time;
    }

    public String getOwner_guid() {
        return owner_guid;
    }

    public String getBillAmount() {
        return BillAmount;
    }

    public String getSpotBilling() {
        return SpotBilling;
    }

    public String getSpotBillingCharge() {
        return SpotBillingCharge;
    }

    public String getPrevReading() {
        return PrevReading;
    }

    public String getBillid() {
        return Billid;
    }


    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setMeter_number(String meter_number) {
        this.meter_number = meter_number;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public void setImporteddate(String importeddate) {
        Importeddate = importeddate;
    }

    public void setCustomer_guid(String customer_guid) {
        this.customer_guid = customer_guid;
    }

    public void setReading(String reading) {
        this.reading = reading;
    }

    public void setPrevious_Bal(String previous_Bal) {
        Previous_Bal = previous_Bal;
    }

    public void setMinRent(String minRent) {
        MinRent = minRent;
    }

    public void setLatefee(String latefee) {
        this.latefee = latefee;
    }

    public void setAdditional(String additional) {
        Additional = additional;
    }

    public void setRead_date(String read_date) {
        this.read_date = read_date;
    }

    public void setRead_time(String read_time) {
        this.read_time = read_time;
    }

    public void setOwner_guid(String owner_guid) {
        this.owner_guid = owner_guid;
    }

    public void setBillAmount(String billAmount) {
        BillAmount = billAmount;
    }

    public void setSpotBilling(String spotBilling) {
        SpotBilling = spotBilling;
    }

    public void setSpotBillingCharge(String spotBillingCharge) {
        SpotBillingCharge = spotBillingCharge;
    }

    public void setPrevReading(String prevReading) {
        PrevReading = prevReading;
    }

    public void setBillid(String billid) {
        Billid = billid;
    }
}
