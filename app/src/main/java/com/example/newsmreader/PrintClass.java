package com.example.newsmreader;

import android.app.Activity;
import android.device.PrinterManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.newsmreader.Dtabase.Column_;
import com.example.newsmreader.Dtabase.DataBase;
import com.example.newsmreader.Models.BillModel;
import com.example.newsmreader.Models.BillPrintModel;
import com.example.newsmreader.Models.CustomerModel;
import com.example.newsmreader.Models.ReadModel;
import com.example.newsmreader.Models.ReadPrintModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import site.haoyin.lib.CXComm;
import site.haoyin.lib.bluetooth.CXBlueTooth;
import site.haoyin.lib.bluetooth.listener.IReceiveDataListener;
import site.haoyin.lib.escpos.CXEscpos;
import site.haoyin.lib.utils.ByteUtil;
import vpos.apipackage.PosApiHelper;
import vpos.apipackage.PrintInitException;

public class PrintClass {
    private PrinterManager printer;
    PosApiHelper posApiHelper;
    DataBase dataBase;
    CustomerModel Cmodel;
    ReadModel readModel;
    ReadPrintModel readPrintModel;
    BillPrintModel billPrintModel;
    BillModel billModel;
    boolean is_fromBill = false;
    public Activity activity;


    public void Getdata(Activity activity, String cunsumerId, int printSelect) {
        this.activity = activity;
        dataBase = new DataBase(activity);
        posApiHelper = PosApiHelper.getInstance();
        String where_condition = " WHERE " + Column_.CustomerID + "='" + cunsumerId + "'";
        String where_conditon2 = " WHERE customer_guid ='" + cunsumerId + "'";
        Cmodel = dataBase.Single_Customer(where_condition);
        readModel = dataBase.Singleread(where_conditon2);

        getBasicDetais(activity);
        setprint();
        if (printSelect == 1) {
            printCiontech = new Print_Ciontech();
            printCiontech.start();
        } else if (printSelect == 2) {
            setbluethooth();

        } else if (printSelect == 3) {
            printer = new PrinterManager();
            urovoPrint = new UrovoPrint();
            urovoPrint.start();


        } else {
            Toast.makeText(activity, "Please Select A Printer", Toast.LENGTH_SHORT).show();
        }
    }

    public void Getdata2(Activity activity, String cunsumerId, int printSelect) {
        this.activity = activity;
        dataBase = new DataBase(activity);
        posApiHelper = PosApiHelper.getInstance();
        String where_condition = " WHERE " + Column_.CustomerID + "='" + cunsumerId + "'";
        String where_conditon2 = " WHERE customer_guid ='" + cunsumerId + "'";
        Cmodel = dataBase.Single_Customer(where_condition);
        readModel = dataBase.Singleread(where_conditon2);
        billModel = dataBase.SingleBill(where_conditon2);
        is_fromBill = true;

        getBasicDetais(activity);
        setPrint2();
        if (printSelect == 1) {
            printCiontech = new Print_Ciontech();
            printCiontech.start();
        } else if (printSelect == 2) {
            setbluethooth();

        } else if (printSelect == 3) {
            printer = new PrinterManager();
            urovoPrint = new UrovoPrint();
            urovoPrint.start();

        } else {
            Toast.makeText(activity, "Please Select A Printer", Toast.LENGTH_SHORT).show();
        }
    }


    public void printFrombill(Activity activity, String cunsumerId, int printSelect) {
        this.activity = activity;

        dataBase = new DataBase(activity);
        posApiHelper = PosApiHelper.getInstance();
        String where_condition = " WHERE " + Column_.CustomerID + "='" + cunsumerId + "'";
        String where_conditon2 = " WHERE customer_guid ='" + cunsumerId + "'";
        Cmodel = dataBase.Single_Customer(where_condition);
        billModel = dataBase.SingleBill(where_conditon2);
        getBasicDetais(activity);
        setPrint3();
        if (printSelect == 1) {
            printCiontechBill = new Print_CiontechBill();
            printCiontechBill.start();
        } else if (printSelect == 2) {
            setbluethoothbill();

        } else if (printSelect == 3) {
            printer = new PrinterManager();
            urovoPrintBill = new UrovoPrint_Bill();
            if (urovoPrintBill.isThreadFinished()){
                urovoPrintBill.start();
            }else {
                Toast.makeText(activity, "Please wait a while Printing", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(activity, "Please Select A Printer", Toast.LENGTH_SHORT).show();
        }

    }


    public void readingReprint(ReadPrintModel readPrintModel, int printSelect, Activity activity) {
        this.activity = activity;
        this.readPrintModel = readPrintModel;
        posApiHelper = PosApiHelper.getInstance();
        getBasicDetais(activity);
        if (printSelect == 1) {
            printCiontech = new Print_Ciontech();
            printCiontech.start();
        } else if (printSelect == 2) {
            setbluethooth();

        } else if (printSelect == 3) {
            printer = new PrinterManager();
            urovoPrint = new UrovoPrint();
            urovoPrint.start();

        } else {
            Toast.makeText(activity, "Please Select A Printer", Toast.LENGTH_SHORT).show();
        }


    }


    public void BillingReprint(BillPrintModel billPrintModel, int printSelect, Activity activity) {
        this.activity = activity;
        this.billPrintModel = billPrintModel;
        posApiHelper = PosApiHelper.getInstance();
        getBasicDetais(activity);
        if (printSelect == 1) {
            printCiontechBill = new Print_CiontechBill();
            printCiontechBill.start();
        } else if (printSelect == 2) {
            setbluethoothbill();

        } else if (printSelect == 3) {
            printer = new PrinterManager();
            urovoPrintBill = new UrovoPrint_Bill();
            if (urovoPrintBill.isThreadFinished()){
                urovoPrintBill.start();
            }else {
                Toast.makeText(activity, "Please wait a while Printing", Toast.LENGTH_SHORT).show();
            }


        } else {
            Toast.makeText(activity, "Please Select A Printer", Toast.LENGTH_SHORT).show();
        }


    }

    String branch_code, User_id, header, header1, header2, address, mobile, content, readername;


    private void getBasicDetais(Activity activity) {
        String retrieve = S_P.getJsonData(activity);
        try {
            JSONObject jsonObject = new JSONObject(retrieve);
            branch_code = jsonObject.getString("branch_code");
            User_id = jsonObject.getString("id");
            address = jsonObject.getString("address");
            header = jsonObject.getString("header");
            readername = jsonObject.getString("name");
            header1 = jsonObject.getString("header1");
            header2 = jsonObject.getString("header2");
            mobile = jsonObject.getString("mobile");
            content = jsonObject.getString("content");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


    }


    public boolean setprint() {
        readPrintModel = new ReadPrintModel("" + readModel.getCustomer_guid(), "" + readModel.getReading()
                , "" + readModel.getPrevious_Bal(), "" + readModel.getRead_date()
                , "" + readModel.getMinRent(), "" + readModel.getLatefee()
                , "" + readModel.getAdditional(), "" + readModel.getRead_time()
                , "" + User_id, "" + readModel.getBillAmount()
                , "" + readModel.getTotal()
                , "" + readModel.getSpotBillingCharge()
                , "" + readModel.getPrevReading(), "" + readModel.getMobile()
                , "" + readModel.getMeter_number()
                , "" + Cmodel.getConsumer_no()
                , "" + Cmodel.getCategory(), "" + Cmodel.getPrevReadingDate()
                , "" + Cmodel.getConsumer(), "" + readModel.getBillid()
                , "" + Cmodel.getDays(), "" + Cmodel.getLocation()
                , "" + header, "" + header1, "" + header2, "" + address
                , "" + mobile, "" + content
                , "" + readername, "", "" + Cmodel.getPasswords(), readModel.getAverage());
        if (readPrintModel == null) {

            return false;
        }
        return true;
    }

    public boolean setPrint2() {


        billPrintModel = new BillPrintModel("", "", ""
                , "", "", "", "", "" + billModel.getRecieved()
                , "" + billModel.getBalance(), "" + billModel.getRecNo(), "", "", "", "", "", ""
                , "", "", ""
                , "" + billModel.getSpotBillingCharge(), "", "" + Cmodel.getMeter_number());

        readPrintModel = new ReadPrintModel("" + readModel.getCustomer_guid(), "" + readModel.getReading()
                , "" + readModel.getPrevious_Bal(), "" + readModel.getRead_date()
                , "" + readModel.getMinRent(), "" + readModel.getLatefee()
                , "" + readModel.getAdditional(), "" + readModel.getRead_time()
                , "" + User_id, "" + readModel.getBillAmount()
                , "" + readModel.getTotal()
                , "" + readModel.getSpotBillingCharge()
                , "" + readModel.getPrevReading(), "" + readModel.getMobile()
                , "" + readModel.getMeter_number()
                , "" + Cmodel.getConsumer_no()
                , "" + Cmodel.getCategory(), "" + Cmodel.getPrevReadingDate()
                , "" + Cmodel.getConsumer(), "" + readModel.getBillid()
                , "" + Cmodel.getDays(), "" + Cmodel.getLocation()
                , "" + header, "" + header1, "" + header2, "" + address
                , "" + mobile, "" + content
                , "" + readername, "", "" + Cmodel.getPasswords(), readModel.getAverage());
        if (readPrintModel == null || billPrintModel == null) {

            return false;
        }
        return true;


    }


    public boolean setPrint3() {


        billPrintModel = new BillPrintModel("" + billModel.getPayment_date()
                , "" + Cmodel.getConsumer_no(), "" + Cmodel.getConsumer()
                , "" + Cmodel.getCategory(), "" + billModel.getAmount()
                , "" + billModel.getLatefee(), "" + billModel.getAddtnl()
                , "" + billModel.getRecieved()
                , "" + billModel.getBalance(), "" + billModel.getRecNo()
                , "" + Cmodel.getMobile(), "" + Cmodel.getLocation()
                , "" + mobile, "" + header, "" + header1, "" + header2
                , "" + address, "" + content, "" + readername
                , "" + billModel.getSpotBillingCharge(), "" + Cmodel.getPasswords()
                , "" + Cmodel.getMeter_number());


        if (billPrintModel == null) {

            return false;
        }
        return true;


    }


    Print_Ciontech printCiontech = null;

    public class Print_Ciontech extends Thread {
        public Print_Ciontech() {

        }

        public void run() {

            synchronized (this) {
                posApiHelper.PrintInit(2, 16, 16, 0x33);
                if (AllstaticObj.whichImg.equalsIgnoreCase("0")) {

                } else if (AllstaticObj.whichImg.equalsIgnoreCase("1")) {
                    Bitmap bmp = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.logofinalqrr);
                    posApiHelper.PrintBmp(bmp);
                } else if (AllstaticObj.whichImg.equalsIgnoreCase("2")) {
                    Bitmap bmp = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.logofinalqr);
                    posApiHelper.PrintBmp(bmp);
                }
                posApiHelper.PrintSetFont((byte) 16, (byte) 16, (byte) 0x33);
                posApiHelper.PrintStr(readPrintModel.getHeader() + "\n");
                posApiHelper.PrintStr(readPrintModel.getHeader1() + "\n");
                posApiHelper.PrintStr(readPrintModel.getHeader2() + "\n");
                posApiHelper.PrintStr(readPrintModel.getAddress() + "\n");
                posApiHelper.PrintStr(readPrintModel.getCompanymobile() + "\n");
                posApiHelper.PrintSetFont((byte) 24, (byte) 24, (byte) 0x00);
                posApiHelper.PrintStr("  (Demand/Disconnection Notice)");
                posApiHelper.PrintSetFont((byte) 16, (byte) 16, (byte) 0x33);
                posApiHelper.PrintStr("........................");
                posApiHelper.PrintStr("Name:" + readPrintModel.getConsumer());
                posApiHelper.PrintStr("Area:" + readPrintModel.getLocation());
                posApiHelper.PrintStr("Consumer#  :" + readPrintModel.getConsumer_no());
                posApiHelper.PrintStr("Contact    :" + readPrintModel.getMobile());
                posApiHelper.PrintStr("Meter#     :" + readPrintModel.getMeter_number());
                posApiHelper.PrintStr("Tariff     :" + readPrintModel.getCategory());
                posApiHelper.PrintStr("                    \n");
                posApiHelper.PrintStr("Bill# :" + readPrintModel.getInvoice());
                posApiHelper.PrintStr("Pres. Rd Dt:" + readPrintModel.getRead_date());
                posApiHelper.PrintStr("Prev. Rd Dt:" + readPrintModel.getPrevReadingDate());
                posApiHelper.PrintStr("Due Dt     :" + readPrintModel.getDuedate());
                posApiHelper.PrintStr("\n");
                posApiHelper.PrintStr("Prev. Rd   :" + readPrintModel.getPrevReading());
                posApiHelper.PrintStr("Curr. Rd   :" + readPrintModel.getReading());
                posApiHelper.PrintStr("Usage      :" + String.valueOf(Integer.parseInt(readPrintModel.getReading()) - Integer.parseInt(readPrintModel.getPrevReading())));
                posApiHelper.PrintStr("........................");
                posApiHelper.PrintStr("Min Rent    :" + readPrintModel.getMinRent() + "(" + readPrintModel.getAverage() + "Month)");
                posApiHelper.PrintStr("Add Usg Amt :" + readPrintModel.getBillAmount());
                posApiHelper.PrintStr("Prev Bal    :" + readPrintModel.getPrevious_Bal());
                posApiHelper.PrintStr("Addl. Amt   :" + readPrintModel.getAdditional());
                posApiHelper.PrintStr("Service Amt :" + readPrintModel.getSpotBillingCharge());
                posApiHelper.PrintStr("Fine        :" + readPrintModel.getLatefee());
                posApiHelper.PrintSetFont((byte) 24, (byte) 24, (byte) 0x33);
                posApiHelper.PrintStr("Payable:" + readPrintModel.getTotal());
                if (is_fromBill) {
                    posApiHelper.PrintSetFont((byte) 16, (byte) 16, (byte) 0x33);
                    posApiHelper.PrintStr("........................");
                    posApiHelper.PrintStr("Rec# :" + billPrintModel.getInvoice() + "\n");
                    if (billPrintModel.getServiceCharge().equalsIgnoreCase("0")) {
                        posApiHelper.PrintSetFont((byte) 24, (byte) 24, (byte) 0x33);
                        posApiHelper.PrintStr("Rcvd.  :" + new Double(Double.parseDouble(billPrintModel.getRecieved())).longValue() + "\n");
                        posApiHelper.PrintSetFont((byte) 16, (byte) 16, (byte) 0x33);
                        long val = new Double(Double.parseDouble(billPrintModel.getBalance())).longValue();
                        if (val < 0) {
                            String str = String.valueOf(val).replace("-", "");
                            posApiHelper.PrintStr("Adv Balance:" + str + "\n");
                        } else {
                            posApiHelper.PrintStr("Balance    :" + val + "\n");
                        }
                    } else {
                        posApiHelper.PrintStr("Service Amt :" + billPrintModel.getServiceCharge() + "\n");
                        posApiHelper.PrintSetFont((byte) 24, (byte) 24, (byte) 0x33);
                        posApiHelper.PrintStr("Rcvd.  :" + new Double(Double.parseDouble(billPrintModel.getRecieved())).longValue() + "\n");
                        posApiHelper.PrintSetFont((byte) 16, (byte) 16, (byte) 0x33);
                        long val = new Double(Double.parseDouble(billPrintModel.getBalance())).longValue();
                        if (val < 0) {
                            String str = String.valueOf(val).replace("-", "");
                            posApiHelper.PrintStr("Adv Balance:" + str + "\n");
                        } else {
                            posApiHelper.PrintStr("Balance    :" + val + "\n");
                        }

                    }
                }
                posApiHelper.PrintSetFont((byte) 16, (byte) 16, (byte) 0x33);
                posApiHelper.PrintStr("************************");
                posApiHelper.PrintStr(readPrintModel.getUser());
                posApiHelper.PrintStr("Meter Reader");
                if (AllstaticObj.isPay) {
                    posApiHelper.PrintStr("........................");
                    posApiHelper.PrintSetFont((byte) 24, (byte) 24, (byte) 0x00);
                    posApiHelper.PrintStr("Pay online at - www.smpay.in ");
                    posApiHelper.PrintStr("Download the Smpay App from ");
                    posApiHelper.PrintStr("Google Play Store");
                    posApiHelper.PrintStr("\n");
                    posApiHelper.PrintSetFont((byte) 24, (byte) 24, (byte) 0x00);
                    posApiHelper.PrintStr("Online payment password  :");
                    posApiHelper.PrintSetFont((byte) 16, (byte) 16, (byte) 0x33);
                    posApiHelper.PrintStr(readPrintModel.getPassword());
                    if(AllstaticObj.is_OnlinePay.equalsIgnoreCase("1")){

                        Bitmap bmp = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.qrcion);
                        posApiHelper.PrintBmp(bmp);
                    }
                   
                }
                posApiHelper.PrintStr("........................");
                posApiHelper.PrintSetFont((byte) 24, (byte) 24, (byte) 0x00);
                posApiHelper.PrintStr(content);
                posApiHelper.PrintStr("\n");
                if (AllstaticObj.isCollecionprint.equalsIgnoreCase("1")) {
                    posApiHelper.PrintSetFont((byte) 16, (byte) 16, (byte) 0x33);
                    posApiHelper.PrintStr("Collection Date");
                    posApiHelper.PrintStr("************************");
                    posApiHelper.PrintStr("\n");
                    posApiHelper.PrintStr("    \n");
                    posApiHelper.PrintStr("************************");
                }
                //if(AllstaticObj.onPoweredby.equalsIgnoreCase("1")){

                    posApiHelper.PrintSetFont((byte) 24, (byte) 24, (byte) 0x00);
                    posApiHelper.PrintStr("             Powered By");
                    posApiHelper.PrintSetFont((byte) 24, (byte) 24, (byte) 0x00);
                    posApiHelper.PrintStr(" Workmate Infotech Pvt Ltd");



               // }
                posApiHelper.PrintStr("\n");



                posApiHelper.PrintStr("\n");
                posApiHelper.PrintStr("    \n");


                int finalret = posApiHelper.PrintStart();


                try {
                    posApiHelper.PrintOpen();
                } catch (PrintInitException e) {
                    throw new RuntimeException(e);
                }

                posApiHelper.PrintClose();

            }


        }

    }


    Print_CiontechBill printCiontechBill = null;

    public class Print_CiontechBill extends Thread {
        public Print_CiontechBill() {

        }

        public void run() {

            synchronized (this) {
                int ret = posApiHelper.PrintInit(2, 16, 16, 0x33);
                if (AllstaticObj.whichImg.equalsIgnoreCase("0")) {

                } else if (AllstaticObj.whichImg.equalsIgnoreCase("1")) {
                    Bitmap bmp = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.logofinalqrr);
                    posApiHelper.PrintBmp(bmp);
                } else if (AllstaticObj.whichImg.equalsIgnoreCase("2")) {
                    Bitmap bmp = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.logofinalqr);
                    posApiHelper.PrintBmp(bmp);
                }

                posApiHelper.PrintSetFont((byte) 24, (byte) 24, (byte) 0x00);

                posApiHelper.PrintSetFont((byte) 16, (byte) 16, (byte) 0x33);
                posApiHelper.PrintStr(billPrintModel.getHeader() + "\n");
                posApiHelper.PrintStr(billPrintModel.getHeader1() + "\n");
                posApiHelper.PrintStr(billPrintModel.getHeader2() + "\n");
                posApiHelper.PrintStr(billPrintModel.getAddress() + "\n");
                if (ret != 0) {
                    return;
                }
                posApiHelper.PrintStr(billPrintModel.getCompanymobile() + "\n");
                posApiHelper.PrintStr("  (Cash Receipt)");
                posApiHelper.PrintStr("........................");
                //////////////////////////////////////////////////////////
                posApiHelper.PrintStr("Name:" + billPrintModel.getConsumer());
                posApiHelper.PrintStr("Area:" + billPrintModel.getLocation());
                posApiHelper.PrintStr("Consumer#   :" + billPrintModel.getConsumer_no());
                posApiHelper.PrintStr("Contact     :" + billPrintModel.getMobile());
                posApiHelper.PrintStr("Meter#      :" + billPrintModel.getMeter_number());
                posApiHelper.PrintStr("Tariff      :" + billPrintModel.getCategory());
                /////////////////////////////////////////////////////////
                posApiHelper.PrintStr("                    \n");

                /////////////////////////////////////////////////////////
                posApiHelper.PrintStr("Rec# : " + billPrintModel.getInvoice() + "\n");
                posApiHelper.PrintStr("Rcpt Dt     :" + billPrintModel.getPayment_date() + "\n");
                /////////////////////////////////////////////////////////
                posApiHelper.PrintStr("                    \n");

                /////////////////////////////////////////////////////////
                posApiHelper.PrintStr("Bill  Amt   :" + billPrintModel.getAmount() + "\n");
                posApiHelper.PrintStr("Addl. Amt   :" + billPrintModel.getAddtnl());
                posApiHelper.PrintStr("Service Amt :" + billPrintModel.getServiceCharge() + "\n");
                posApiHelper.PrintStr("Fine        :" + billPrintModel.getLatefee());

                Double d = Double.parseDouble(billPrintModel.getAmount()) + Double.parseDouble(billPrintModel.getAddtnl()) + Double.parseDouble(billPrintModel.getLatefee());

                posApiHelper.PrintStr("Payable     :" + String.valueOf((int) Math.round((double) d)));
                ////////////////////////////////////////////////////////
                posApiHelper.PrintStr("                    \n");
                posApiHelper.PrintSetFont((byte) 24, (byte) 24, (byte) 0x33);
                new Double(Double.parseDouble(billPrintModel.getRecieved())).longValue();
                posApiHelper.PrintStr("Rcvd.  :" + new Double(Double.parseDouble(billPrintModel.getRecieved())).longValue() + "\n");
                posApiHelper.PrintSetFont((byte) 16, (byte) 16, (byte) 0x33);
                long val = new Double(Double.parseDouble(billPrintModel.getBalance())).longValue();
                if (val < 0) {
                    String str = String.valueOf(val).replace("-", "");
                    posApiHelper.PrintStr("Adv Balance:" + str + "\n");
                } else {
                    posApiHelper.PrintStr("Balance    :" + val + "\n");
                }
                posApiHelper.PrintStr("************************");
                posApiHelper.PrintStr(billPrintModel.getUsername());
                posApiHelper.PrintStr("Meter Reader");
                if (AllstaticObj.isPay) {
                    posApiHelper.PrintStr("........................");
                    posApiHelper.PrintSetFont((byte) 24, (byte) 24, (byte) 0x00);
                    posApiHelper.PrintStr("Pay online at - www.smpay.in ");
                    posApiHelper.PrintStr("Download the Smpay App from ");
                    posApiHelper.PrintStr("Google Play Store");
                    posApiHelper.PrintStr("\n");
                    posApiHelper.PrintSetFont((byte) 24, (byte) 24, (byte) 0x00);
                    posApiHelper.PrintStr("Online payment password  :");
                    posApiHelper.PrintSetFont((byte) 16, (byte) 16, (byte) 0x33);
                    posApiHelper.PrintStr(billPrintModel.getPasswords());
                    if(AllstaticObj.is_OnlinePay.equalsIgnoreCase("1")){

                        Bitmap bmp = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.qrcion);
                        posApiHelper.PrintBmp(bmp);



                    }

                }
                posApiHelper.PrintStr("........................");
                posApiHelper.PrintSetFont((byte) 24, (byte) 24, (byte) 0x00);
                posApiHelper.PrintStr(content);
                posApiHelper.PrintStr("                                        \n");
              //  if(AllstaticObj.onPoweredby.equalsIgnoreCase("1")){

                    posApiHelper.PrintSetFont((byte) 16, (byte) 16, (byte) 0x33);
                    posApiHelper.PrintStr("        Powered By");
                    posApiHelper.PrintSetFont((byte) 24, (byte) 24, (byte) 0x00);
                    posApiHelper.PrintStr("Workmate Infotech Pvt Ltd");



             //   }

                posApiHelper.PrintStart();
                int finalret = posApiHelper.PrintStart();
                try {
                    posApiHelper.PrintOpen();
                } catch (PrintInitException e) {
//....
                }
                posApiHelper.PrintClose();

            }


        }

    }


    private CXComm cxcomm;

    private void setbluethooth() {
        cxcomm = CXBlueTooth.getInstance();
        cxcomm.recv(new IReceiveDataListener() {
            @Override
            public void onReceiveData(byte[] data) {
                try {
                    int len = ByteUtil.returnActualLength(data);
                    byte[] actualData = new byte[len];
                    System.arraycopy(data, 0, actualData, 0, len);
                    String content = new String(actualData, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
        printBlutooth = new Print_BlueTooth();
        printBlutooth.start();
    }


    Print_BlueTooth printBlutooth = null;

    public class Print_BlueTooth extends Thread {
        public Print_BlueTooth() {

        }

        public void run() {

            synchronized (this) {

                CXEscpos cxEscpos = CXEscpos.getInstance(cxcomm, 0);

                cxEscpos.InitializePrinter();

                cxEscpos.SelectPrintModes(CXEscpos.FONT.FONTA, CXEscpos.ENABLE.OFF, CXEscpos.ENABLE.OFF, CXEscpos.ENABLE.OFF, CXEscpos.ENABLE.OFF);


                Bitmap b = BitmapFactory.decodeResource(activity.getResources(), R.drawable.jj_logo);
                cxEscpos.Bitmap(500, 500, 360, 170, b, CXEscpos.BITMAP_MODE.MODE0.getValue());
                cxEscpos.SelectJustification(CXEscpos.JUSTIFICATION.LEFT);

                cxEscpos.PrintAndFeedLines((byte) 1);
                if (!TextUtils.isEmpty(header)) {
                    cxEscpos.Text(header.trim() + "\n");
                }
                if (!TextUtils.isEmpty(header1)) {
                    cxEscpos.Text(header1.trim() + "\n");
                }
                if (!TextUtils.isEmpty(header2)) {
                    cxEscpos.Text(header2.trim() + "\n");
                }
                if (!TextUtils.isEmpty(address)) {
                    cxEscpos.Text(address.trim() + "\n");
                }
                if (!TextUtils.isEmpty(mobile)) {
                    cxEscpos.Text(mobile.trim() + "\n");
                }
                cxEscpos.Text("  (Demand/Disconnection Notice)" + "\n");
                cxEscpos.Text("............................" + "\n");
                cxEscpos.Text("Name:" + readPrintModel.getConsumer() + "\n");
                cxEscpos.Text("Area:" + readPrintModel.getLocation() + "\n");
                cxEscpos.Text("Consumer#  :" + readPrintModel.getConsumer_no() + "\n");
                cxEscpos.Text("Contact    :" + readPrintModel.getMobile() + "\n");
                cxEscpos.Text("Meter#     :" + readPrintModel.getMeter_number() + "\n");
                cxEscpos.Text("Tariff     :" + readPrintModel.getCategory() + "\n");

                cxEscpos.Text("\n");
                cxEscpos.Text("Bill# :" + readPrintModel.getInvoice() + "\n");
                cxEscpos.Text("Pres. Rd Dt:" + readPrintModel.getRead_date() + "\n");
                cxEscpos.Text("Prev. Rd Dt:" + readPrintModel.getPrevReadingDate() + "\n");
                cxEscpos.Text("Due Dt     :" + readPrintModel.getDuedate() + "\n");
                cxEscpos.Text("\n");


                cxEscpos.Text("Prev. Rd   :" + readPrintModel.getPrevReading() + "\n");
                cxEscpos.Text("Curr. Rd   :" + readPrintModel.getReading() + "\n");

                double uasge = Double.parseDouble(readPrintModel.getReading().trim()) - Double.parseDouble(readPrintModel.getPrevReading().trim());
                int usg = (int) uasge;
                cxEscpos.Text("Usage      :" + String.valueOf(usg + "\n"));


                cxEscpos.Text("............................" + "\n");
                cxEscpos.Text("Min Rent    :" + readPrintModel.getMinRent() + "(" + readPrintModel.getAverage() + " Months)" + "\n");

                cxEscpos.Text("Add Usg Amt :" + readPrintModel.getBillAmount() + "\n");
                cxEscpos.Text("Prev Bal    :" + readPrintModel.getPrevious_Bal() + "\n");
                cxEscpos.Text("Addl. Amt   :" + readPrintModel.getAdditional() + "\n");
                cxEscpos.Text("Service Amt :" + readPrintModel.getSpotBillingCharge() + "\n");

                cxEscpos.Text("Fine        :" + readPrintModel.getLatefee() + "\n");
                cxEscpos.Text("Payable     :" + readPrintModel.getTotal() + "\n");
                if (is_fromBill) {
                    /* 设置打印居中 */
                    cxEscpos.SelectJustification(CXEscpos.JUSTIFICATION.LEFT);
                    /* 设置为倍高倍宽 */
                    cxEscpos.SelectPrintModes(CXEscpos.FONT.FONTA, CXEscpos.ENABLE.OFF, CXEscpos.ENABLE.OFF, CXEscpos.ENABLE.OFF, CXEscpos.ENABLE.OFF);
                    cxEscpos.Text("................................" + "\n");
                    cxEscpos.Text("Rec# :" + billPrintModel.getInvoice() + "\n");

                    //////////////////////////////////////////////////////////
                    if (billPrintModel.getServiceCharge().equalsIgnoreCase("0")) {
                        cxEscpos.Text("Rcvd.      :" + new Double(Double.parseDouble(billPrintModel.getRecieved())).longValue() + "\n");
                        long val = new Double(Double.parseDouble(billPrintModel.getBalance())).longValue();

                        if (val < 0) {
                            String str = String.valueOf(val).replace("-", "");
                            cxEscpos.Text("Adv Balance:" + str + "\n");

                        } else {
                            cxEscpos.Text("Balance    :" + val + "\n");

                        }


                    } else {
                        cxEscpos.Text("Service Amt :" + billPrintModel.getServiceCharge() + "\n");
                        cxEscpos.Text("Rcvd.  :" + new Double(Double.parseDouble(billPrintModel.getRecieved())).longValue() + "\n");
                        long val = new Double(Double.parseDouble(billPrintModel.getBalance())).longValue();

                        if (val < 0) {
                            String str = String.valueOf(val).replace("-", "");
                            cxEscpos.Text("Adv Balance:" + str + "\n");

                        } else {
                            cxEscpos.Text("Balance    :" + val + "\n");

                        }
                    }

                }


                cxEscpos.Text("****************************" + "\n");
                cxEscpos.Text(readPrintModel.getUser() + "\n");
                cxEscpos.Text("Meter Reader" + "\n");

                if (AllstaticObj.isPay) {
                    cxEscpos.Text("........................" + "\n");
                    cxEscpos.Text("Pay online at - www.smpay.in " + "\n");
                    cxEscpos.Text("Download the Smpay App from " + "\n");
                    cxEscpos.Text("Google Play Store" + "\n");
                    cxEscpos.Text("Online payment password  :" + "\n");
                    cxEscpos.Text(readPrintModel.getPassword() + "\n");
                    if(AllstaticObj.is_OnlinePay.equalsIgnoreCase("1")){
                        cxEscpos.Text("" + "\n");
                        cxEscpos.SelectJustification(CXEscpos.JUSTIFICATION.CENTER);
                        Bitmap b1 = BitmapFactory.decodeResource(activity.getResources(), R.drawable.qr);
                        cxEscpos.Bitmap(400, 400, 120, 120, b1, CXEscpos.BITMAP_MODE.MODE0.getValue());
                    }



                }
                cxEscpos.Text(".............................." + "\n");

                cxEscpos.Text(content + "\n");
                if (AllstaticObj.isCollecionprint.equalsIgnoreCase("1")) {
                    cxEscpos.Text("Collection Date" + "\n");
                    cxEscpos.Text("*******************************" + "\n");
                    cxEscpos.Text("" + "\n");
                    cxEscpos.Text("" + "\n");
                    cxEscpos.Text("********************************" + "\n");
                }
                //if(AllstaticObj.onPoweredby.equalsIgnoreCase("1")){

                    cxEscpos.SelectJustification(CXEscpos.JUSTIFICATION.CENTER);
                    cxEscpos.Text(" Powered By"+ "\n");

                    cxEscpos.Text("Workmate Infotech Pvt Ltd"+ "\n");



              //  }
//                if(AllstaticObj.is_OnlinePay.equalsIgnoreCase("1")){
//                    cxEscpos.Text("" + "\n");
//                    cxEscpos.SelectJustification(CXEscpos.JUSTIFICATION.CENTER);
//                    Bitmap b1 = BitmapFactory.decodeResource(activity.getResources(), R.drawable.qr);
//                    cxEscpos.Bitmap(400, 400, 120, 120, b1, CXEscpos.BITMAP_MODE.MODE0.getValue());
//                }
                cxEscpos.PrintAndFeedLines((byte) 3);
            }


        }


    }


    private void setbluethoothbill() {
        cxcomm = CXBlueTooth.getInstance();
        cxcomm.recv(new IReceiveDataListener() {
            @Override
            public void onReceiveData(byte[] data) {
                try {
                    int len = ByteUtil.returnActualLength(data);
                    byte[] actualData = new byte[len];
                    System.arraycopy(data, 0, actualData, 0, len);
                    String content = new String(actualData, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
        printBlueToothBill = new Print_BlueTooth_Bill();
        printBlueToothBill.start();
    }


    Print_BlueTooth_Bill printBlueToothBill = null;

    public class Print_BlueTooth_Bill extends Thread {
        public Print_BlueTooth_Bill() {

        }

        public void run() {

            synchronized (this) {

                CXEscpos cxEscpos = CXEscpos.getInstance(cxcomm, 0);
                /*初始化打印机*/
                cxEscpos.InitializePrinter();
                /*走纸开关*/

                /* 设置打印居中 */
                cxEscpos.SelectJustification(CXEscpos.JUSTIFICATION.LEFT);
                /* 设置为倍高倍宽 */
                cxEscpos.SelectPrintModes(CXEscpos.FONT.FONTA, CXEscpos.ENABLE.OFF, CXEscpos.ENABLE.OFF, CXEscpos.ENABLE.OFF, CXEscpos.ENABLE.OFF);
                Bitmap b = BitmapFactory.decodeResource(activity.getResources(), R.drawable.jj_logo);
                cxEscpos.Bitmap(500, 500, 360, 170, b, CXEscpos.BITMAP_MODE.MODE0.getValue());
                cxEscpos.SelectJustification(CXEscpos.JUSTIFICATION.LEFT);
                cxEscpos.PrintAndFeedLines((byte) 1);
                cxEscpos.Text(header.toUpperCase().trim() + "\n");
                cxEscpos.Text(header1.toUpperCase().trim() + "\n");
                cxEscpos.Text(header2.toUpperCase().trim() + "\n");

                cxEscpos.Text(address.trim() + "\n");
                cxEscpos.Text(mobile.trim() + "\n");
                cxEscpos.Text("  (Cash Receipt)" + "\n");
                cxEscpos.Text("............................".trim() + "\n");
                cxEscpos.Text("Name:" + billPrintModel.getConsumer().trim() + "\n");
                cxEscpos.Text("Area:" + billPrintModel.getLocation().trim() + "\n");
                cxEscpos.Text("Consumer#  :" + billPrintModel.getConsumer_no().trim() + "\n");
                cxEscpos.Text("Contact    :" + billPrintModel.getMobile().trim() + "\n");
                cxEscpos.Text("Meter#     :" + billPrintModel.getMeter_number().trim() + "\n");
                cxEscpos.Text("Tariff     :" + billPrintModel.getCategory().trim() + "\n");

                cxEscpos.Text("Rec# : " + billPrintModel.getInvoice() + "\n");
                cxEscpos.Text("Rcpt Dt     :" + billPrintModel.getPayment_date() + "\n");
                cxEscpos.Text("" + "\n");

                cxEscpos.Text("Bill  Amt   :" + billPrintModel.getAmount() + "\n");
                cxEscpos.Text("Addl. Amt   :" + billPrintModel.getAddtnl() + "\n");
                cxEscpos.Text("Service Amt :" + billPrintModel.getServiceCharge() + "\n");
                cxEscpos.Text("Fine        :" + billPrintModel.getLatefee() + "\n");
                Double d = Double.parseDouble(billPrintModel.getAmount()) + Double.parseDouble(billPrintModel.getAddtnl()) + Double.parseDouble(billPrintModel.getLatefee());
                cxEscpos.Text("Payable     :" + String.valueOf((int) Math.round((double) d)) + "\n");

                ////////////////////////////////////////////////////////
                new Double(Double.parseDouble(billPrintModel.getRecieved())).longValue();
                cxEscpos.Text("Rcvd.       :" + new Double(Double.parseDouble(billPrintModel.getRecieved())).longValue() + "\n");
                long val = new Double(Double.parseDouble(billPrintModel.getBalance())).longValue();

                if (val < 0) {
                    String str = String.valueOf(val).replace("-", "");
                    cxEscpos.Text("Adv Balance:" + str + "\n");

                } else {
                    cxEscpos.Text("Balance    :" + val + "\n");

                }
                cxEscpos.Text("********************************" + "\n");
                cxEscpos.Text(billPrintModel.getUsername().trim() + "\n");
                cxEscpos.Text("Meter Reader" + "\n");
                if (AllstaticObj.isPay) {
                    cxEscpos.Text("........................" + "\n");
                    cxEscpos.Text("Pay online at - www.smpay.in " + "\n");
                    cxEscpos.Text("Download the Smpay App from " + "\n");
                    cxEscpos.Text("Google Play Store" + "\n");
                    cxEscpos.Text("Online payment password  :" + "\n");
                    cxEscpos.Text(billPrintModel.getPasswords() + "\n");
                    if(AllstaticObj.is_OnlinePay.equalsIgnoreCase("1")){
                        cxEscpos.Text("" + "\n");
                        cxEscpos.SelectJustification(CXEscpos.JUSTIFICATION.CENTER);
                        Bitmap b1 = BitmapFactory.decodeResource(activity.getResources(), R.drawable.qr);
                        cxEscpos.Bitmap(400, 400, 120, 120, b1, CXEscpos.BITMAP_MODE.MODE0.getValue());
                    }

                }
                cxEscpos.Text(".............................." + "\n");
                cxEscpos.Text(billPrintModel.getContent() + "\n");
               // if(AllstaticObj.onPoweredby.equalsIgnoreCase("1")){

                    cxEscpos.SelectJustification(CXEscpos.JUSTIFICATION.CENTER);
                    cxEscpos.Text(" Powered By"+ "\n");

                    cxEscpos.Text("Workmate Infotech Pvt Ltd"+ "\n");



               // }

                cxEscpos.Text("" + "\n");
                cxEscpos.PrintAndFeedLines((byte) 3);


            }

        }


    }


    ///////////////////////////////////////////////////////////////////////
    private static int _XVALUE = 384;
    private static int _YVALUE = 26;
    private static int _YVALUE2 = 24;
    private static int _YVALUE3 = 30;
    private final int _YVALUE6 = 30;

    UrovoPrint urovoPrint = null;
    public class UrovoPrint extends Thread {

        public UrovoPrint() {


        }

        public void run() {
            synchronized (this) {
                int height = 0;
                printer.prn_open();
                printer.prn_setupPage(_XVALUE, -1);
                printer.prn_clearPage();


                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
                opts.inDensity = activity.getResources().getDisplayMetrics().densityDpi;
                opts.inTargetDensity = activity.getResources().getDisplayMetrics().densityDpi;


                if (AllstaticObj.whichImg.equalsIgnoreCase("0")) {

                } else if (AllstaticObj.whichImg.equalsIgnoreCase("1")) {
                    Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.logofinalqrr, opts);
                    printer.prn_drawBitmap(bitmap, 84, height);
                    height += 110;
                } else if (AllstaticObj.whichImg.equalsIgnoreCase("2")) {
                    Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.logofinalqr, opts);
                    printer.prn_drawBitmap(bitmap, 84, height);
                    height += 110;
                }


                Prn_Str(header, _YVALUE6, height);
                height += _YVALUE + 8;
                Prn_Str(header1, _YVALUE6, height);
                height += _YVALUE + 8;
                Prn_Str(header2, _YVALUE6, height);
                height += _YVALUE + 8;

                Prn_Str(address, _YVALUE6, height);
                height += _YVALUE + 8;
                Prn_Str(mobile, _YVALUE6, height);
                height += _YVALUE + 8;
                Prn_Str("(Demand/Disconnection Notice)", _YVALUE2, height);
                height += _YVALUE3;
                Prn_Str(".................................................." + "\n", _YVALUE, height);

                height += _YVALUE3 + 4;
                Prn_Str_Bold("Name:" + readPrintModel.getConsumer(), _YVALUE, height);
                height += _YVALUE3;
                Prn_Str_Bold("Area:" + readPrintModel.getLocation(), _YVALUE, height);
                height += _YVALUE3;
                Prn_Str_Bold("Consumer#  :" + readPrintModel.getConsumer_no(), _YVALUE, height);
                height += _YVALUE3;
                Prn_Str_Bold("Contact    :" + readPrintModel.getMobile(), _YVALUE, height);
                height += _YVALUE3;
                Prn_Str_Bold("Meter#     :" + readPrintModel.getMeter_number(), _YVALUE, height);
                height += _YVALUE3;
                Prn_Str_Bold("Tariff     :" + readPrintModel.getCategory(), _YVALUE, height);

                height += _YVALUE + 10;
                Prn_Str_Bold("", _YVALUE, height);
                height += _YVALUE3;
                Prn_Str_Bold("Bill# :" + readPrintModel.getInvoice(), _YVALUE, height);
                height += _YVALUE3;
                Prn_Str_Bold("Pres. Rd Dt:" + readPrintModel.getRead_date(), _YVALUE, height);
                height += _YVALUE3;
                Prn_Str_Bold("Prev. Rd Dt:" + readPrintModel.getPrevReadingDate(), _YVALUE, height);
                height += _YVALUE3;
                Prn_Str_Bold("Due Dt     :" + readPrintModel.getDuedate(), _YVALUE, height);
                height += _YVALUE + 12;


                Prn_Str_Bold("Prev. Rd   :" + readPrintModel.getPrevReading(), _YVALUE, height);
                height += _YVALUE;
                Prn_Str_Bold("Curr. Rd   :" + readPrintModel.getReading(), _YVALUE, height);
                height += _YVALUE;
                Prn_Str_Bold("Usage      :" + String.valueOf(Integer.parseInt(readPrintModel.getReading()) - Integer.parseInt(readPrintModel.getPrevReading())), _YVALUE, height);

                height += _YVALUE + 10;
                Prn_Str(".................................................." + "\n", _YVALUE, height);


                height += _YVALUE + 2;
                Prn_Str_Bold("Min Rent    :" + readPrintModel.getMinRent() + "(" + AllstaticObj.avgmonth + "Month)", _YVALUE, height);
                height += _YVALUE3;
                Prn_Str_Bold("Usage Amt   :" + readPrintModel.getBillAmount(), _YVALUE, height);
                height += _YVALUE3;
                Prn_Str_Bold("Prev Bal    :" + readPrintModel.getPrevious_Bal(), _YVALUE, height);
                height += _YVALUE3;
                Prn_Str_Bold("Addl. Amt   :" + readPrintModel.getAdditional(), _YVALUE, height);
                height += _YVALUE3;
                Prn_Str_Bold("Service Amt :" + readPrintModel.getSpotBillingCharge(), _YVALUE, height);
                height += _YVALUE3;
                Prn_Str_Bold("Fine        :" + readPrintModel.getLatefee(), _YVALUE, height);


                height += _YVALUE3;
                Prn_Str_Bold("Payable  :" + readPrintModel.getTotal(), _YVALUE + 10, height);
                height += _YVALUE + 15;


                if (is_fromBill) {
                    Prn_Str(".................................................." + "\n", _YVALUE, height);
                    height += _YVALUE + 2;
                    Prn_Str_Bold("Rec# :" + billPrintModel.getInvoice(), _YVALUE , height);
                    height += _YVALUE3;



                    //////////////////////////////////////////////////////////
                    if (billPrintModel.getServiceCharge().equalsIgnoreCase("0")) {
                        Prn_Str_Bold("Rcvd.      " + new Double(Double.parseDouble(billPrintModel.getRecieved())).longValue() + "\n", _YVALUE + 10, height);
                        height += _YVALUE + 15;
                        long val = new Double(Double.parseDouble(billPrintModel.getBalance())).longValue();

                        if (val < 0) {
                            String str = String.valueOf(val).replace("-", "");
                            Prn_Str_Bold("Adv Balance:" +str + "\n", _YVALUE + 10, height);
                            height += _YVALUE + 15;

                        } else {
                            Prn_Str_Bold("Balance    " +val + "\n", _YVALUE + 10, height);
                            height += _YVALUE + 15;

                        }


                    } else {
                        Prn_Str_Bold("Service Amt :" + billPrintModel.getServiceCharge(), _YVALUE , height);
                        height += _YVALUE3;

                        Prn_Str_Bold("Rcvd.      " + new Double(Double.parseDouble(billPrintModel.getRecieved())).longValue() + "\n", _YVALUE + 10, height);
                        height += _YVALUE + 15;
                        long val = new Double(Double.parseDouble(billPrintModel.getBalance())).longValue();

                        if (val < 0) {
                            String str = String.valueOf(val).replace("-", "");
                            Prn_Str_Bold("Adv Balance:" +str + "\n", _YVALUE + 10, height);
                            height += _YVALUE + 15;

                        } else {
                            Prn_Str_Bold("Balance    " +val + "\n", _YVALUE + 10, height);
                            height += _YVALUE + 15;

                        }
                    }

                }


                Prn_Str("************************************************" + "\n", _YVALUE, height);

                height += _YVALUE3;
                Prn_Str_Bold(readPrintModel.getUser(), _YVALUE, height);
                height += _YVALUE3;
                Prn_Str_Bold("Meter Reader", _YVALUE, height);

                if (AllstaticObj.isPay) {


                    height += _YVALUE3;
                    Prn_Str(".................................................." + "\n", _YVALUE, height);

                    height += _YVALUE3;
                    Prn_Str_Bold("Pay online at - www.smpay.in ", _YVALUE, height);

                    height += _YVALUE3;
                    Prn_Str_Bold("Download the Smpay App from ", _YVALUE, height);
                    height += _YVALUE3;
                    Prn_Str_Bold("Google Play Store", _YVALUE, height);
                    height += _YVALUE3;
                    Prn_Str_Bold("", _YVALUE, height);
                    height += _YVALUE3;
                    Prn_Str_Bold("Online payment password   :", _YVALUE, height);
                    height += _YVALUE3;
                    Prn_Str_Bold(readPrintModel.getPassword(), _YVALUE, height);


                    height += _YVALUE3;


                }

                Prn_Str(".................................................." + "\n", _YVALUE, height);



                height += _YVALUE + 2;
                Prn_Str_Bold(content, _YVALUE, height);
                height += _YVALUE3;
                Prn_Str_Bold("", _YVALUE, height);

                height += _YVALUE3;
                Prn_Str_Bold("Collection Date", _YVALUE, height);


                height += _YVALUE3;
                Prn_Str("************************************************" + "\n", _YVALUE, height);

                height += _YVALUE3;
                Prn_Str_Bold(" ", _YVALUE, height);
                height += _YVALUE3;
                Prn_Str_Bold(" ", _YVALUE, height);
                height += _YVALUE3;
                Prn_Str("************************************************" + "\n", _YVALUE, height);

                    height += _YVALUE3;
                    Prn_Str_Bold("            Powered By", _YVALUE, height);
                    height += _YVALUE3;
                    Prn_Str_Bold("  Workmate Infotech Pvt Ltd", _YVALUE, height);




                height += _YVALUE3;
                Prn_Str_Bold("", _YVALUE, height);
                height += _YVALUE3;
                Prn_Str("" + "\n", _YVALUE, height);
                height += _YVALUE3;
                Prn_Str("" + "\n", _YVALUE, height);
                printer.prn_printPage(0);

            }
        }
    }


    UrovoPrint_Bill urovoPrintBill = null;
    public class UrovoPrint_Bill extends Thread {
        boolean ThreadRunning=true;
        public boolean isThreadFinished() {
            return ThreadRunning;
        }
        public UrovoPrint_Bill() {

        }

        public void run() {
            synchronized (this) {
                ThreadRunning=false;
                int height = 30;
                printer.prn_open();
                printer.prn_setupPage(_XVALUE, -1);
                printer.prn_clearPage();

                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
                opts.inDensity = activity.getResources().getDisplayMetrics().densityDpi;
                opts.inTargetDensity = activity.getResources().getDisplayMetrics().densityDpi;

                if (AllstaticObj.whichImg.equalsIgnoreCase("0")){

                } else if (AllstaticObj.whichImg.equalsIgnoreCase("1")){
                    Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.logofinalqrr, opts);
                    printer.prn_drawBitmap(bitmap, 84, height);
                    height += 105;
                } else if (AllstaticObj.whichImg.equalsIgnoreCase("2")){
                    Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.logofinalqr, opts);
                    printer.prn_drawBitmap(bitmap, 84, height);
                    height += 105;
                }

                Prn_Str(header, _YVALUE6, height);
                height += _YVALUE+8;
                Prn_Str(header1, _YVALUE6, height);
                height += _YVALUE+8;
                Prn_Str(header2, _YVALUE6, height);
                height += _YVALUE+8;
                Prn_Str(address, _YVALUE6, height);
                height += _YVALUE+8;
                Prn_Str(mobile, _YVALUE6, height);
                height += _YVALUE+8;
                Prn_Str("    (Cash Receipt)", _YVALUE2, height);
                height += _YVALUE3;
                Prn_Str(".................................................." + "\n", _YVALUE, height);
                //////////////////////////////////////////////////////////

                height += _YVALUE3+4;
                Prn_Str_Bold("Name:"+billPrintModel.getConsumer(), _YVALUE, height);
                height += _YVALUE3;
                Prn_Str_Bold("Area:"+billPrintModel.getLocation(), _YVALUE, height);
                height += _YVALUE3;
                Prn_Str_Bold("Consumer#   :"+billPrintModel.getConsumer_no(), _YVALUE, height);
                height += _YVALUE3;
                Prn_Str_Bold("Contact     :"+billPrintModel.getMobile(), _YVALUE, height);
                height += _YVALUE3;
                Prn_Str_Bold("Meter#      :"+billPrintModel.getMeter_number(), _YVALUE, height);
                height += _YVALUE3;
                Prn_Str_Bold("Tariff      :"+billPrintModel.getCategory(), _YVALUE, height);


                /////////////////////////////////////////////////////////
                height += _YVALUE3;
                Prn_Str_Bold("  ", _YVALUE, height);

                /////////////////////////////////////////////////////////


                height += _YVALUE3;
                Prn_Str_Bold("Rec# : "+billPrintModel.getInvoice(), _YVALUE, height);
                height += _YVALUE3;
                Prn_Str_Bold("Rcpt Dt     :"+billPrintModel.getPayment_date(), _YVALUE, height);

                /////////////////////////////////////////////////////////
                height += _YVALUE3;
                Prn_Str_Bold("  ", _YVALUE, height);

                /////////////////////////////////////////////////////////
                height += _YVALUE3;
                Prn_Str_Bold("Bill  Amt   :"+billPrintModel.getAmount(), _YVALUE, height);
                height += _YVALUE3;
                Prn_Str_Bold("Addl. Amt   :"+billPrintModel.getAddtnl(), _YVALUE, height);
                height += _YVALUE3;
                Prn_Str_Bold("Service Amt :"+billPrintModel.getServiceCharge(), _YVALUE, height);
                height += _YVALUE3;
                Prn_Str_Bold("Fine        :"+billPrintModel.getLatefee(), _YVALUE, height);
                height += _YVALUE3;



                Double d=Double.parseDouble(billPrintModel.getAmount()) + Double.parseDouble(billPrintModel.getAddtnl())+Double.parseDouble(billPrintModel.getLatefee());

                Prn_Str_Bold("Payable     :"+ String.valueOf((int) Math.round((double) d)), _YVALUE, height);

                ////////////////////////////////////////////////////////
                height += _YVALUE3;
                Prn_Str_Bold("  ", _YVALUE, height);
                height += _YVALUE3;
                Prn_Str_Bold("Rcvd.  :"+new Double(Double.parseDouble(billPrintModel.getRecieved())).longValue(), _YVALUE6+5, height);
                height += _YVALUE3+12;
                Prn_Str_Bold("Balance    :"+new Double(Double.parseDouble(billPrintModel.getBalance())).longValue(), _YVALUE6, height);
                height += _YVALUE3;
                Prn_Str_Bold("", _YVALUE, height);
                height += _YVALUE3+5;
                Prn_Str_Bold("************************************************"+"\n", _YVALUE, height);
                height += _YVALUE3+2;
                Prn_Str_Bold(billPrintModel.getUsername(), _YVALUE, height);
                height += _YVALUE3;
                Prn_Str_Bold("Meter Reader", _YVALUE, height);

                if (AllstaticObj.isPay) {


                    height += _YVALUE3;
                    Prn_Str(".................................................." + "\n", _YVALUE, height);

                    height += _YVALUE3;
                    Prn_Str_Bold("Pay online at - www.smpay.in ", _YVALUE, height);

                    height += _YVALUE3;
                    Prn_Str_Bold("Download the Smpay App from ", _YVALUE, height);
                    height += _YVALUE3;
                    Prn_Str_Bold("Google Play Store", _YVALUE, height);
                    height += _YVALUE3;
                    Prn_Str_Bold("", _YVALUE, height);
                    height += _YVALUE3;
                    Prn_Str_Bold("Online payment password   :", _YVALUE, height);
                    height += _YVALUE3;
                    Prn_Str_Bold(billPrintModel.getPasswords(), _YVALUE, height);
                    height += _YVALUE3;


                }
                Prn_Str(".................................................." + "\n", _YVALUE, height);

                height += _YVALUE3;
                Prn_Str_Bold(content, _YVALUE, height);
               // if(AllstaticObj.onPoweredby.equalsIgnoreCase("1")){
                    height += _YVALUE3;
                    Prn_Str_Bold("            Powered By", _YVALUE, height);
                    height += _YVALUE3;
                    Prn_Str_Bold("  Workmate Infotech Pvt Ltd", _YVALUE, height);


               // }
                height += _YVALUE3;
                Prn_Str(""+"\n", _YVALUE, height);
                height += _YVALUE3;
                Prn_Str(""+"\n", _YVALUE, height);
                height += _YVALUE3;
                Prn_Str(""+"\n", _YVALUE, height);




                printer.prn_printPage(0);
                ThreadRunning=true;

            }
        }
    }







    private final static String STR_FONT_VALUE_SONG = "simsun";

    private int Prn_Str(String msg, int fontSize, int height) {
        return printer.prn_drawText(msg, 0, height, STR_FONT_VALUE_SONG, fontSize, false,
                false, 0);
    }

    private int Prn_Str_Bold(String msg, int fontSize, int height) {
        return printer.prn_drawText(msg, 0, height, STR_FONT_VALUE_SONG, fontSize, true,
                false, 0);
    }


}



