package com.example.newsmreader;

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
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newsmreader.Bluethhoth.DeviceScanBean;
import com.example.newsmreader.Bluethhoth.SharePreferecnceUtils;
import com.example.newsmreader.Dtabase.DataBase;
import com.example.newsmreader.Dtabase.Tables_;
import com.example.newsmreader.Models.BillModel;
import com.example.newsmreader.Models.CustomerModel;
import com.example.newsmreader.billing.CashMode;
import com.example.newsmreader.billing.TakeBillingPage;
import com.example.newsmreader.reading.Checkactivity;
import com.example.newsmreader.reading.ReadConformation;
import com.example.newsmreader.reading.ReadProfile;
import com.example.newsmreader.reading.TakeReadingPage;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import site.haoyin.lib.bluetooth.CXBlueTooth;
import site.haoyin.lib.bluetooth.listener.IBlueToothListener;

public class Payment_conformaion_page extends AppCompatActivity {
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
        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            setContentView(R.layout.activity_payment_conformaion_page);
            dataBase=new DataBase(this);
            txt_Status=findViewById(R.id.txt_Status);
            printClass=new PrintClass();
            Intent intent=getIntent();
            billModel=(BillModel) intent.getSerializableExtra("key2");
            cModel=(CustomerModel) intent.getSerializableExtra("key3");
            is_which=intent.getStringExtra("key1");

            getBasicDetais();
            mContext = Payment_conformaion_page.this;
            progress = new ProgressDialog(this);
            progress.setMessage("Loading...");
            progress.setCanceledOnTouchOutside(false);
            progress.setCancelable(true);
            webview = (WebView) findViewById(R.id.webview);
            webview.getSettings().setJavaScriptEnabled(true);
            webview.getSettings().setSupportMultipleWindows(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                webview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }

            webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

            webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            webview.addJavascriptInterface(new WebAppInterface(), "Android");
            webview.setWebChromeClient(new WebChromeClient());
            webview.setWebViewClient(new MyWebViewClient());
            String email=branch_code+"."+cModel.getConsumer_no()+"@gmail.com";


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

               // if (url.equalsIgnoreCase("https://www.smpay.in/icici/Successpage.php") ) {

                 //   webview.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
               // }
              //  Log.e(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>",url);
            }
        }

//        @SuppressWarnings("unused")
//        class MyJavaScriptInterface {
//            @JavascriptInterface
//            public void sendJsonData(String html) {
//
//
//
//                String status ="Return: "+html+"\n";
//
//                if (html.contains("Status") ) {
//
//                    webview.setVisibility(View.GONE);
//
//
//                    html=html.replace("<html><head></head><body>","");
//                    html=html.replace("</body></html>","");
//                    status =status+"removed: "+html+"\n";
//                    try {
//
//                    JSONObject jsonObject = new JSONObject(html);
//                    String statu = jsonObject.getString("Status");
//                    String data = jsonObject.getString("data");
//
//                    if (statu.equalsIgnoreCase("True") ){
//                        status =status+"Status: "+statu+"\n";
//                        Toast.makeText(getApplicationContext(),html,Toast.LENGTH_LONG).show();
//                        Log.e(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>",data);
//
//
//
//                       if (dataBase.insertBilling(billModel,cModel,getApplicationContext())){
//                           status =status+"Inssert first: "+data+"\n";
//
//                           if (dataBase.update_Bill(billModel.getCustomer_guid(),data)){
//                               status =status+"Inssert Second: "+billModel.getCustomer_guid()+"\n";
//                               if (is_which.equalsIgnoreCase("2")){
//
//                                   if (AllstaticObj.whichPrinter==1){
//                                       printClass.printFrombill(Payment_conformaion_page.this,cModel.getCustomerID(),AllstaticObj.whichPrinter);
//                                       fullback();
//                                   }else if(AllstaticObj.whichPrinter==2){
//                                       set_BlueThooth();
//
//
//                                   } else if (AllstaticObj.whichPrinter==3) {
//
//                                       printClass.printFrombill(Payment_conformaion_page.this,cModel.getCustomerID(),AllstaticObj.whichPrinter);
//
//                                   }
//
//
//                               }
//                            //   Toast.makeText(getApplicationContext(),"Sucess",Toast.LENGTH_LONG).show();
//
//
//
//                           }
//
//
//
//
//
//
//
//                       }
//
//
//
//
//
//
//                    }else {
//
//                        status =status+"Status: "+statu+"\n";
//
//
//                    }
//
//
//                } catch (JSONException e) {
//                    throw new RuntimeException(e);
//                }
//
//
//
//
//                }else {
//                    status = "Status Not Known!";
//                }
//
//                txt_Status.setText(status);
//                Toast.makeText(getApplicationContext(),status,Toast.LENGTH_LONG);
//
//                finish();
//            }
//        }



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

    private boolean isRedirectUrl(String url) {
        // Implement your logic to determine if the URL is a redirect URL
        // For example, check if it matches a certain pattern

        return url.startsWith("https://eazypay.icicibank.com/upiPayment");
    }






//    //////////////////////////new bluethooth//////////////////////////////
//    private ArrayList<DeviceScanBean> lists = new ArrayList<DeviceScanBean>();
//    private List<String> lists_str = new ArrayList<>();
//    private DeviceScanBean deviceScanBean;
//    private static final int REQUEST_ENABLE_BT = 1;
//    private CXBlueTooth BT;
//    private static final int MSG_CONN_SCAN = 1001;
//    private static final int MSG_DISCONN_SCAN = 1002;
//
//    public void set_BlueThooth() {
//
//
//        BT = CXBlueTooth.getInstance();
//        BT.setBlueToothListener(new IBlueToothListener() {
//            @Override
//            public void onConnected() {
//                CXBlueTooth.getInstance().setConnected(true);
//                mHandler.sendEmptyMessageDelayed(MSG_CONN_SCAN, 1000);
//                dialog.dismiss();
//                printClass.printFrombill(Payment_conformaion_page.this,cModel.getCustomerID(), AllstaticObj.whichPrinter);
//                fullback();
//
//
//            }
//
//            @Override
//            public void onDisConnected() {
//                CXBlueTooth.getInstance().setConnected(false);
//                mHandler.sendEmptyMessageDelayed(MSG_DISCONN_SCAN, 1000);
//
//
//            }
//
//            @Override
//            public void onError() {
//                mHandler.sendEmptyMessageDelayed(MSG_DISCONN_SCAN, 1000);
//                CXBlueTooth.getInstance().setConnected(false);
//
//            }
//        });
//        // CXBlueTooth.getInstance().disconnect();
//
//
//        if (CXBlueTooth.getInstance().isConnected()) {
//
//            //  DeviceScanBean deviceScanBeanSharePre = new Gson().fromJson(source, DeviceScanBean.class);
//            printClass.printFrombill(Payment_conformaion_page.this,cModel.getCustomerID(), AllstaticObj.whichPrinter);
//            fullback();
//
//        } else {
//
//            setbluethooth();
//        }
//
//
//
//    }
//
//    public void fullback(){
//
//
//
//        ReadProfile.Readpro.finish();
//        TakeReadingPage.TakeRead.finish();
//        CashMode.activity.finish();
//        finish();
//    }
//
//
//    private void setbluethooth() {
//
//
//        if (CXBlueTooth.getInstance().isBlueToothOpen()) {
//            startScan();
//        } else {
//            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Payment_conformaion_page.this);
//            builder.setTitle("Open the bluethooth");
//            builder.setMessage("The Bluethooth");
//            builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                    if (ActivityCompat.checkSelfPermission(Payment_conformaion_page.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
//                        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
//                        dialogInterface.cancel();
//                    }
//
//
//
//                }
//            });
//            builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    dialogInterface.cancel();
//
//
//                }
//            });
//            androidx.appcompat.app.AlertDialog alertDialog = builder.create();
//            alertDialog.show();
//
//        }
//
//
//    }
//
//
//    @SuppressLint("HandlerLeak")
//    private android.os.Handler mHandler = new android.os.Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case MSG_CONN_SCAN:
//                    SharePreferecnceUtils.setPREFERENCES(Payment_conformaion_page.this, new Gson().toJson(deviceScanBean));
//                    txtConnectdevice.setText(deviceScanBean.getDeviceName() + deviceScanBean.getMacAddress());
//                    break;
//                case MSG_DISCONN_SCAN:
//                    SharePreferecnceUtils.clearALLPREFERENCES(Payment_conformaion_page.this);
//                    txtConnectdevice.setText("");
//                    break;
//
//                default:
//                    break;
//            }
//        }
//
//    };
//
//    TextView txtConnectdevice;
//    ListView list_item;
//    AlertDialog dialog;
//
//    @SuppressLint("MissingPermission")
//    public void startScan() {
//        CXBlueTooth.getInstance().scanDevices();
//        Set<BluetoothDevice> paireDevices = CXBlueTooth.getInstance().getBondedDevices();
//        Log.e("TAG", "paireDevices SIZE==" + paireDevices.size());
//        if (paireDevices.size() > 0) {
//            A:
//            for (BluetoothDevice device : paireDevices) {
//                for (DeviceScanBean list : lists) {
//                    if (list.getBluetoothDevice().getAddress().equals(device.getAddress())) {
//                        break A;
//                    }
//                }
//                if (!TextUtils.isEmpty(device.getName())) {
//                    lists.add(new DeviceScanBean(device, 0, new byte[0]));
//                    lists_str.add(device.getName() + "\n" + device.getAddress());
//                }
//            }
//        }
//
//        ViewGroup viewGroup = findViewById(android.R.id.content);
//
//        View dialogView = LayoutInflater.from(Payment_conformaion_page.this).inflate(R.layout.alertset_bluetooth, viewGroup, false);
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(Payment_conformaion_page.this);
//        builder.setView(dialogView);
//        dialog = builder.create();
//        dialog.setCancelable(false);
//        txtConnectdevice = dialogView.findViewById(R.id.txtConnectdevice);
//        list_item = dialogView.findViewById(R.id.list_item);
//        ImageView imv_close = dialogView.findViewById(R.id.imv_close);
//        imv_close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                dialog.dismiss();
//            }
//        });
//
//        ArrayAdapter spinnerAdapter1 = new ArrayAdapter<String>(Payment_conformaion_page.this, R.layout.sp_txt, lists_str);
//
//        list_item.setAdapter(spinnerAdapter1);
//
//
//        list_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (lists.size() != 0) {
//                    deviceScanBean = lists.get(position);
//                    if (deviceScanBean != null && deviceScanBean.getBluetoothDevice() != null) {
//                        CXBlueTooth.getInstance().stopScan();
//                        CXBlueTooth.getInstance().disconnect();
//                        CXBlueTooth.getInstance().conn(deviceScanBean.getBluetoothDevice());
//
//
//                    }
//
//                }
//
//            }
//
//
//        });
//
//
//        dialog.show();
//
//
//    }
//
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == REQUEST_ENABLE_BT) {
//            if (resultCode == RESULT_OK) {
//                startScan();
//            }
//            if (resultCode == RESULT_CANCELED) {
//                finish();
//            }
//        } else {
//            finish();
//        }
//
//    }


    @Override
    public void onBackPressed() {

        Intent resultIntent = new Intent();
        resultIntent.putExtra("key1", "0");
        setResult(RESULT_OK, resultIntent);
        finish();
        super.onBackPressed();

    }
}