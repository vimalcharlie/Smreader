package com.example.newsmreader.Models;

public class BillPrintModel {

    String payment_date;
    String consumer_no;
    String Consumer;
    String category;
    String amount;
    String latefee;
    String Additional;
    String recieved;
    String balance;
    String invoice;
    String   mobile;
    String Location;
    String  Companymobile;
    String header;
    String header1;
    String header2;
    String address;
    String Footer;
    String username;
    String ServiceCharge;
    String Passwords;
    String   meter_number;


    public BillPrintModel(String payment_date, String consumer_no, String consumer, String category, String amount, String latefee, String addtnl, String recieved, String balance, String invoice, String mobile, String location, String companymobile, String header, String header1, String header2, String address, String content, String username, String serviceCharge, String passwords,String meter_number) {
        this.payment_date = payment_date;
        this.consumer_no = consumer_no;
        Consumer = consumer;
        this.category = category;
        this.amount = amount;
        this.latefee = latefee;
        Additional = addtnl;
        this.recieved = recieved;
        this.balance = balance;
        this.invoice = invoice;
        this.mobile = mobile;
        this.Location = location;
        Companymobile = companymobile;
        this.header = header;
        this.header1 = header1;
        this.header2 = header2;
        this.address = address;
        this.Footer = content;
        this.username = username;
        ServiceCharge = serviceCharge;
        Passwords = passwords;
        this.meter_number=meter_number;
    }

    public String getMeter_number() {
        return meter_number;
    }

    public String getPayment_date() {
        return payment_date;
    }

    public String getConsumer_no() {
        return consumer_no;
    }

    public String getConsumer() {
        return Consumer;
    }

    public String getCategory() {
        return category;
    }

    public String getAmount() {
        return amount;
    }

    public String getLatefee() {
        return latefee;
    }

    public String getAddtnl() {
        return Additional;
    }

    public String getRecieved() {
        return recieved;
    }

    public String getBalance() {
        return balance;
    }

    public String getInvoice() {
        return invoice;
    }

    public String getMobile() {
        return mobile;
    }

    public String getLocation() {
        return Location;
    }

    public String getCompanymobile() {
        return Companymobile;
    }

    public String getHeader() {
        return header;
    }

    public String getHeader1() {
        return header1;
    }

    public String getHeader2() {
        return header2;
    }

    public String getAddress() {
        return address;
    }

    public String getContent() {
        return Footer;
    }

    public String getUsername() {
        return username;
    }

    public String getServiceCharge() {
        return ServiceCharge;
    }

    public String getPasswords() {
        return Passwords;
    }
}
