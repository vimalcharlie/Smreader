package com.example.newsmreader.Models;

public class ReadPrintModel {
    String customer_guid;
    String reading;
    String Previous_Bal;
    String read_date;
    String MinRent;
    String latefee;
    String Additional;
    String read_time;
    String owner_guid;
    String UsageAmt;
    String Total;
    String SpotBillingCharge;
    String Previous_read;
    String mobile;
    String meter_number;
    String consumer_no;
    String category;
    String PrevReadingDate;
    String Consumer;
    String Invoice;
    String Duedate;
    String Location;

    String header;
    String header1;
    String header2;
    String address;
    String Companymobile;
    String content;
    String User;
    String NoofMonths;
    String Passwords;
    String average;


    public ReadPrintModel(String customer_guid, String reading, String previous_Bal, String read_date, String minRent, String latefee, String additional, String read_time, String owner_guid, String billAmount, String total, String spotBillingCharge, String prevReading, String mobile, String meter_number, String consumer_no, String category, String prevReadingDate, String consumer, String invoice, String duedate, String location, String header, String header1, String header2, String address, String companymobile, String content, String user, String noofMonths, String password,String average) {
        this.customer_guid = customer_guid;
        this.reading = reading;
        Previous_Bal = previous_Bal;
        this.read_date = read_date;
        MinRent = minRent;
        this.latefee = latefee;
        Additional = additional;
        this.read_time = read_time;
        this.owner_guid = owner_guid;
        UsageAmt = billAmount;
        Total = total;
        SpotBillingCharge = spotBillingCharge;
        Previous_read = prevReading;
        this.mobile = mobile;
        this.meter_number = meter_number;
        this.consumer_no = consumer_no;
        this.category = category;
        PrevReadingDate = prevReadingDate;
        Consumer = consumer;
        this.Invoice = invoice;
        Duedate = duedate;
        this.Location = location;
        this.header = header;
        this.header1 = header1;
        this.header2 = header2;
        this.address = address;
        Companymobile = companymobile;
        this.content = content;
        this.User = user;
        NoofMonths = noofMonths;
        this.Passwords=password;
        this.average=average;

    }

    public String getAverage() {
        return average;
    }

    public String getPassword(){
        return Passwords;
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

    public String getRead_date() {
        return read_date;
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

    public String getRead_time() {
        return read_time;
    }

    public String getOwner_guid() {
        return owner_guid;
    }

    public String getBillAmount() {
        return UsageAmt;
    }

    public String getTotal() {
        return Total;
    }

    public String getSpotBillingCharge() {
        return SpotBillingCharge;
    }

    public String getPrevReading() {
        return Previous_read;
    }

    public String getMobile() {
        return mobile;
    }

    public String getMeter_number() {
        return meter_number;
    }

    public String getConsumer_no() {
        return consumer_no;
    }

    public String getCategory() {
        return category;
    }

    public String getPrevReadingDate() {
        return PrevReadingDate;
    }

    public String getConsumer() {
        return Consumer;
    }

    public String getInvoice() {
        return Invoice;
    }

    public String getDuedate() {
        return Duedate;
    }

    public String getLocation() {
        return Location;
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

    public String getCompanymobile() {
        return Companymobile;
    }

    public String getContent() {
        return content;
    }

    public String getUser() {
        return User;
    }

    public String getNoofMonths() {
        return NoofMonths;
    }
}
