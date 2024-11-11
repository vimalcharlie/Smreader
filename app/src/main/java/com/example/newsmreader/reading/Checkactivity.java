package com.example.newsmreader.reading;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.example.newsmreader.Models.BillModel;
import com.example.newsmreader.Models.CustomerModel;
import com.example.newsmreader.R;

public class Checkactivity extends AppCompatActivity {
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkactivity);
        webView = findViewById(R.id.webView);
        Intent intent=getIntent();


       BillModel billModel=(BillModel) intent.getSerializableExtra("key2");
        CustomerModel cModel=(CustomerModel) intent.getSerializableExtra("key3");

        // Enable JavaScript in the WebView
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Add a JavaScript interface for communication
        webView.addJavascriptInterface(new WebAppInterface(), "Android");

        // Set a WebChromeClient to handle JavaScript alerts, etc.
        webView.setWebChromeClient(new WebChromeClient());

        // Load your HTML page with embedded JavaScript
        String data="payer_name=" +cModel.getConsumer() + "&payer_phone=" + cModel.getMobile()+"&payer-email="+"vimal@gmail"+"&payeramount="+billModel.getRecieved().trim();
        webView.postUrl("https://www.smpay.in/icici/Payment.php",data.getBytes());
    }

    // JavaScript interface class
    public class WebAppInterface {
        @JavascriptInterface
        public void sendJsonData(String jsonData) {
            // Handle the JSON data received from the WebView
            // For example, you can parse the JSON string and use the data
            // in your native Android code
            try {

                Toast.makeText(getApplicationContext(),jsonData,Toast.LENGTH_LONG).show();
                finish();


                // Assume jsonData is a JSON string
                // JSONObject jsonObject = new JSONObject(jsonData);
                // String value = jsonObject.getString("key");
                // Handle the extracted data accordingly
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}