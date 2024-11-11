package com.example.newsmreader.reading;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.newsmreader.AllstaticObj;
import com.example.newsmreader.Bluethhoth.DeviceScanBean;
import com.example.newsmreader.Bluethhoth.SharePreferecnceUtils;
import com.example.newsmreader.Dtabase.DataBase;
import com.example.newsmreader.Models.BillModel;
import com.example.newsmreader.Models.CustomerModel;
import com.example.newsmreader.Payment_conformaion_page;
import com.example.newsmreader.PrintClass;
import com.example.newsmreader.R;
import com.example.newsmreader.billing.BillConformPage;
import com.example.newsmreader.billing.BillingProfile;
import com.example.newsmreader.billing.TakeBillingPage;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import site.haoyin.lib.bluetooth.CXBlueTooth;
import site.haoyin.lib.bluetooth.listener.IBlueToothListener;

public class CashMode extends AppCompatActivity {
    BillModel billModel;
    CustomerModel cModel;

    DataBase dataBase;
    PrintClass printClass;
    public  static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        printClass = new PrintClass();
        dataBase=new DataBase(this);
        activity=this;
        setContentView(R.layout.activity_cash_mode);
        Intent intent = getIntent();

        billModel = (BillModel) intent.getSerializableExtra("key2");
        cModel = (CustomerModel) intent.getSerializableExtra("key3");
        AllstaticObj.getPREFERENCES(com.example.newsmreader.reading.CashMode.this);

    }

    public void savedata(View v) {
        int id = v.getId();

        if (id == R.id.cash) {

            if (dataBase.insertBilling(billModel, cModel,getApplicationContext())) {





                    if (AllstaticObj.whichPrinter==1){
                        printClass.Getdata2(CashMode.this,cModel.getCustomerID(),AllstaticObj.whichPrinter);
                         fullback();
                    }else if (AllstaticObj.whichPrinter==2){
                        set_BlueThooth();
                    } else if (AllstaticObj.whichPrinter==3) {
                        printClass.Getdata2(CashMode.this,cModel.getCustomerID(),AllstaticObj.whichPrinter);
                        fullback();
                    }else {



                    }





            }

        } else if (id == R.id.online) {
            if(AllstaticObj.is_OnlinePay.equalsIgnoreCase("1")){
                Intent intent=new Intent(CashMode.this, PaymetConformBill.class);

                intent.putExtra("key2",billModel);
                intent.putExtra("key3",cModel);
                startActivityForResult(intent,101);
            }else {
                Snackbar.make(findViewById(R.id.root_rl),"You don't have access to this funtion!",Snackbar.LENGTH_LONG).show();
            }


        }


    }


    //////////////////////////new bluethooth//////////////////////////////
    private ArrayList<DeviceScanBean> lists = new ArrayList<DeviceScanBean>();
    private List<String> lists_str = new ArrayList<>();
    private DeviceScanBean deviceScanBean;
    private static final int REQUEST_ENABLE_BT = 1;
    private CXBlueTooth BT;
    private static final int MSG_CONN_SCAN = 1001;
    private static final int MSG_DISCONN_SCAN = 1002;

    public void set_BlueThooth() {


        BT = CXBlueTooth.getInstance();
        BT.setBlueToothListener(new IBlueToothListener() {
            @Override
            public void onConnected() {
                CXBlueTooth.getInstance().setConnected(true);
                mHandler.sendEmptyMessageDelayed(MSG_CONN_SCAN, 1000);
                printClass.Getdata2(CashMode.this,cModel.getCustomerID(),AllstaticObj.whichPrinter);                dialog.dismiss();
                fullback();


            }

            @Override
            public void onDisConnected() {
                CXBlueTooth.getInstance().setConnected(false);
                mHandler.sendEmptyMessageDelayed(MSG_DISCONN_SCAN, 1000);


            }

            @Override
            public void onError() {
                mHandler.sendEmptyMessageDelayed(MSG_DISCONN_SCAN, 1000);
                CXBlueTooth.getInstance().setConnected(false);

            }
        });
        // CXBlueTooth.getInstance().disconnect();


        if (CXBlueTooth.getInstance().isConnected()) {

            //  DeviceScanBean deviceScanBeanSharePre = new Gson().fromJson(source, DeviceScanBean.class);
            printClass.Getdata2(CashMode.this,cModel.getCustomerID(),AllstaticObj.whichPrinter);            fullback();

        } else {

            setbluethooth();
        }


    }

    public void fullback() {
      Billing_throughreading.readthrough.finish();
      ReadBIllConfrom.activity.finish();
      finish();
    }


    private void setbluethooth() {


        if (CXBlueTooth.getInstance().isBlueToothOpen()) {
            startScan();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(CashMode.this);
            builder.setTitle("Open the bluethooth");
            builder.setMessage("The Bluethooth");
            builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    if (ActivityCompat.checkSelfPermission(CashMode.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                        dialogInterface.cancel();
                    }


                }
            });
            builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();


                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }


    }


    @SuppressLint("HandlerLeak")
    private android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_CONN_SCAN:
                    SharePreferecnceUtils.setPREFERENCES(CashMode.this, new Gson().toJson(deviceScanBean));
                    txtConnectdevice.setText(deviceScanBean.getDeviceName() + deviceScanBean.getMacAddress());
                    break;
                case MSG_DISCONN_SCAN:
                    SharePreferecnceUtils.clearALLPREFERENCES(CashMode.this);
                    txtConnectdevice.setText("");
                    break;

                default:
                    break;
            }
        }

    };

    TextView txtConnectdevice;
    ListView list_item;
    AlertDialog dialog;

    @SuppressLint("MissingPermission")
    public void startScan() {
        CXBlueTooth.getInstance().scanDevices();
        Set<BluetoothDevice> paireDevices = CXBlueTooth.getInstance().getBondedDevices();
        Log.e("TAG", "paireDevices SIZE==" + paireDevices.size());
        if (paireDevices.size() > 0) {
            A:
            for (BluetoothDevice device : paireDevices) {
                for (DeviceScanBean list : lists) {
                    if (list.getBluetoothDevice().getAddress().equals(device.getAddress())) {
                        break A;
                    }
                }
                if (!TextUtils.isEmpty(device.getName())) {
                    lists.add(new DeviceScanBean(device, 0, new byte[0]));
                    lists_str.add(device.getName() + "\n" + device.getAddress());
                }
            }
        }

        ViewGroup viewGroup = findViewById(android.R.id.content);

        View dialogView = LayoutInflater.from(CashMode.this).inflate(R.layout.alertset_bluetooth, viewGroup, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(CashMode.this);
        builder.setView(dialogView);
        dialog = builder.create();
        dialog.setCancelable(false);
        txtConnectdevice = dialogView.findViewById(R.id.txtConnectdevice);
        list_item = dialogView.findViewById(R.id.list_item);
        ImageView imv_close = dialogView.findViewById(R.id.imv_close);
        imv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                dialog.dismiss();
            }
        });

        ArrayAdapter spinnerAdapter1 = new ArrayAdapter<String>(CashMode.this, R.layout.sp_txt, lists_str);

        list_item.setAdapter(spinnerAdapter1);


        list_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (lists.size() != 0) {
                    deviceScanBean = lists.get(position);
                    if (deviceScanBean != null && deviceScanBean.getBluetoothDevice() != null) {
                        CXBlueTooth.getInstance().stopScan();
                        CXBlueTooth.getInstance().disconnect();
                        CXBlueTooth.getInstance().conn(deviceScanBean.getBluetoothDevice());


                    }

                }

            }


        });


        dialog.show();


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                startScan();
            }
            if (resultCode == RESULT_CANCELED) {
                finish();
            }
        } else if (requestCode==101) {
            if (data.getStringExtra("key1").equalsIgnoreCase("0")){

            }else {
                setThedata(data.getStringExtra("key1"));
            }


        } else {
            finish();
        }

    }

    private void setThedata(String key1) {




            try {

                JSONObject jsonObject = new JSONObject(key1);
                String status = jsonObject.getString("Status");
                String data = jsonObject.getString("data");

                if (status.equalsIgnoreCase("True") ){


                    if (dataBase.insertBilling(billModel,cModel,getApplicationContext())){

                        if (dataBase.update_Bill(billModel.getCustomer_guid(),data)){


                                if (AllstaticObj.whichPrinter==1){
                                    printClass.Getdata2(CashMode.this,cModel.getCustomerID(),AllstaticObj.whichPrinter);
                                    fullback();
                                }else if(AllstaticObj.whichPrinter==2){
                                    set_BlueThooth();
                                } else if (AllstaticObj.whichPrinter==3) {
                                    printClass.Getdata2(CashMode.this,cModel.getCustomerID(),AllstaticObj.whichPrinter);
                                    fullback();
                                }else {
                                    Snackbar.make(findViewById(R.id.root_rl),"Please select the printer",Snackbar.LENGTH_LONG).show();
                                    fullback();
                                }


                            }
                            Toast.makeText(getApplicationContext(),"Sucess",Toast.LENGTH_LONG).show();



                        }







                    }









            } catch (JSONException e) {
                throw new RuntimeException(e);
            }





    }


}