package com.example.newsmreader.Dtabase;

public class  Tables_ {
    ///Table Name
    public static final String TABLE_CUSTOMER = "table_customer";
    public static  final  String TABLE_SLAB = "table_slab";
    public static  final  String TABLE_READING = "table_reading";
    public static  final  String TABLE_BILL = "table_bill";

    //// query for database  ///////////////////////////
    public static String CUSTOMER_TABLE = "create table '" + TABLE_CUSTOMER+ "' (ID INTEGER PRIMARY KEY AUTOINCREMENT" +
            ",CustomerID TEXT" +
            ",Consumer TEXT" +
            ",consumer_no TEXT" +
            ",mobile TEXT" +
            ",address TEXT" +
            ",Location TEXT" +
            ",category TEXT" +
            ",meter_number TEXT" +
            ",PrevReading TEXT" +
            ",PrevBalance TEXT" +
            ",AdditionalCharges TEXT" +
            ",ServiceCharge TEXT" +
            ",EnableServiceCharge TEXT" +
            ",Latefee TEXT" +
            ",Minrent TEXT" +
            ",catid TEXT" +
            ",PrevReadingDate TEXT" +
            ",PrevBillDate TEXT" +
            ",BasedonReadDate TEXT" +
            ",Days TEXT" +
            ",Passwords TEXT" +
            ",NoofMonths TEXT" +
            ",Latitude TEXT" +
            ",Longitude TEXT" +
            ",readingexists TEXT"+
            ",isRead TEXT"+
            ",isBill TEXT"+

            ")";







    public static String SLAB_QUERY=  "create table "+ TABLE_SLAB + " (ID INTEGER PRIMARY kEY AUTOINCREMENT" +
            ",CID TEXT" +
            ",TITLE TEXT" +
            ",UNITFROM TEXT" +
            ",UNITTO TEXT" +
            ",RATE TEXT" +
            ",ABOVE TEXT" +
            ",MinRent TEXT )";



    public static String READING_QUERY = "create table " + TABLE_READING + " (ID INTEGER PRIMARY KEY AUTOINCREMENT" +
            ",mobile TEXT" +
            ",meter_number TEXT" +
            ",Latitude TEXT" +
            ",Longitude TEXT" +
            ",Importeddate TEXT" +
            ",customer_guid TEXT" +
            ",reading TEXT" +
            ",Previous_Bal TEXT" +
            ",MinRent TEXT" +
            ",latefee TEXT" +
            ",Additional TEXT" +
            ",read_date TEXT" +
            ",read_time TEXT" +
            ",owner_guid TEXT" +
            ",BillAmount TEXT" +
            ",SpotBilling TEXT" +
            ",Total TEXT"+
            ",SpotBillingCharge TEXT" +
            ",PrevReading TEXT" +
            ",Billid TEXT" +
            ",average TEXT"+
            ",Send TEXT"+
            ",Saved TEXT"+
            ",usage TEXT"+
            ",hold TEXT"+
            ",closed TEXT"+
            ")";





    public static String BILL_QUERY = "create table " + TABLE_BILL + " (ID INTEGER PRIMARY KEY AUTOINCREMENT" +
            ",customer_guid TEXT" +
            ",amount TEXT" +
            ",recieved TEXT" +
            ",balance TEXT" +
            ",latefee TEXT"+
            ",payment_date TEXT" +
            ",owner_guid TEXT" +
            ",Addtnl TEXT" +
            ",BillRecvd TEXT" +
            ",Paid_time TEXT" +
            ",SpotBilling TEXT" +
            ",SpotBillingCharge TEXT" +
            ",RecNo TEXT" +
            ",online TEXT" +
            ",Reference  TEXT"+
            ",Send TEXT"+
            ",Saved TEXT"+
            ")";






































}
