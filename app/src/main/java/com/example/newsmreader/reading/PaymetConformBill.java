package com.example.newsmreader.reading;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newsmreader.AllstaticObj;
import com.example.newsmreader.Bluethhoth.DeviceScanBean;
import com.example.newsmreader.Bluethhoth.SharePreferecnceUtils;
import com.example.newsmreader.Dtabase.DataBase;
import com.example.newsmreader.Models.BillModel;
import com.example.newsmreader.Models.CustomerModel;
import com.example.newsmreader.Payment_conformaion_page;
import com.example.newsmreader.PrintClass;
import com.example.newsmreader.R;
import com.example.newsmreader.S_P;
import com.example.newsmreader.billing.CashMode;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import site.haoyin.lib.bluetooth.CXBlueTooth;
import site.haoyin.lib.bluetooth.listener.IBlueToothListener;

public class PaymetConformBill extends AppCompatActivity {

    Context mContext;
    WebView webview;
    ProgressDialog progress;
    DataBase dataBase;
    PrintClass printClass;
    BillModel billModel;
    CustomerModel cModel;
    String is_which;
    TextView txt_Status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        printClass=new PrintClass();
        setContentView(R.layout.activity_payment_conformaion_page);
        dataBase=new DataBase(getApplicationContext());
        Intent intent = getIntent();
        is_which = intent.getStringExtra("key1");
        billModel = (BillModel) intent.getSerializableExtra("key2");
        cModel = (CustomerModel) intent.getSerializableExtra("key3");

       getBasicDetais();
       String email=branch_code+"."+cModel.getConsumer_no()+"@gmail.com";





        mContext = PaymetConformBill.this;
        progress = new ProgressDialog(this);
        progress.setMessage("Loading...");
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(true);
        webview = (WebView) findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setSupportMultipleWindows(true);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.addJavascriptInterface(new WebAppInterface(), "Android");
        //webview.setWebChromeClient(new WebChromeClient());
        webview.setWebViewClient(new MyWebViewClient());



        show();
        String data="payer_name=" +cModel.getConsumer() + "&payer_phone=" + cModel.getMobile()+"&payer-email="+email+"&payeramount="+billModel.getRecieved().trim();
        webview.postUrl("https://www.smpay.in/icici/Payment.php",data.getBytes());














    }











    public class WebAppInterface {
        @JavascriptInterface
        public void sendJsonData(String jsonData) {


            Intent resultIntent = new Intent();
            resultIntent.putExtra("key1", jsonData);
            setResult(RESULT_OK, resultIntent);
            finish();
            //  Toast.makeText(getApplicationContext(), jsonData, Toast.LENGTH_LONG).show();

        }
    }






    public class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);



        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(webview, url);
            if (progress.isShowing())
                hide();

        }
    }

    public void show() {
        if (!((Activity) mContext).isFinishing())
            if (progress != null && !progress.isShowing())
                progress.show();

    }

    public void hide() {
        if (!((Activity) mContext).isFinishing())
            if (progress != null && progress.isShowing())
                progress.dismiss();

    }






    String branch_code, User_id;

    private void getBasicDetais() {


        String retrieve = S_P.getJsonData(getApplicationContext());
        try {
            JSONObject jsonObject = new JSONObject(retrieve);
            branch_code = jsonObject.getString("branch_code");
            User_id = jsonObject.getString("id");


        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


    }



    @Override
    public void onBackPressed() {

        Intent resultIntent = new Intent();
        resultIntent.putExtra("key1", "0");
        setResult(RESULT_OK, resultIntent);
        finish();
        super.onBackPressed();

    }




}