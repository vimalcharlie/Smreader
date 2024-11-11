package com.example.newsmreader.Models;

import java.io.Serializable;

public class BillModel implements Serializable {

    private String customer_guid;
    private String amount;



    private String recieved;

    private String balance;
    private  String latefee;
    private String payment_date;

    private String owner_guid;
    private String Addtnl;
    private String BillRecvd;
    private String Paid_time;
    private String SpotBilling;
    private String SpotBillingCharge;
    private String RecNo;
    private String online;
    private String Reference;




    public BillModel(String customer_guid, String amount, String recieved, String balance,String latefee, String payment_date, String owner_guid, String addtnl, String billRecvd, String paid_time, String spotBilling, String spotBillingCharge, String recNo, String online,String reference) {
        this.customer_guid = customer_guid;
        this.amount = amount;
        this.recieved = recieved;
        this.balance = balance;
        this.latefee=latefee;
        this.payment_date = payment_date;
        this.owner_guid = owner_guid;
        Addtnl = addtnl;
        BillRecvd = billRecvd;
        Paid_time = paid_time;
        SpotBilling = spotBilling;
        SpotBillingCharge = spotBillingCharge;
        RecNo = recNo;
        this.online = online;
        this.Reference=reference;
    }

    public String getReference() {
        return Reference;
    }

    public String getCustomer_guid() {
        return customer_guid;
    }

    public String getAmount() {
        return amount;
    }

    public String getRecieved() {
        return recieved;
    }

    public String getBalance() {
        return balance;
    }

    public String getPayment_date() {
        return payment_date;
    }

    public String getOwner_guid() {
        return owner_guid;
    }

    public String getAddtnl() {
        return Addtnl;
    }

    public String getBillRecvd() {
        return BillRecvd;
    }

    public String getPaid_time() {
        return Paid_time;
    }

    public String getSpotBilling() {
        return SpotBilling;
    }

    public String getSpotBillingCharge() {
        return SpotBillingCharge;
    }

    public String getRecNo() {
        return RecNo;
    }

    public String getOnline() {
        return online;
    }
    public String getLatefee() {
        return latefee;
    }
}
