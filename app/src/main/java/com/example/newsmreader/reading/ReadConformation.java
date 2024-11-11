package com.example.newsmreader.reading;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
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
import com.example.newsmreader.MainActivity;
import com.example.newsmreader.Models.BillModel;
import com.example.newsmreader.Models.CustomerModel;
import com.example.newsmreader.Models.ReadModel;
import com.example.newsmreader.PrintClass;
import com.example.newsmreader.R;
import com.example.newsmreader.RegisterPage;
import com.example.newsmreader.S_P;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import com.skydoves.elasticviews.ElasticButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import site.haoyin.lib.bluetooth.CXBlueTooth;
import site.haoyin.lib.bluetooth.listener.IBlueToothListener;

public class ReadConformation extends AppCompatActivity {
    ReadModel readModel;
    CustomerModel cModel;
    PrintClass printClass;

    DataBase db;
    TextView txt_total, txt_fine,
            txt_prebal, serviceAmt,
            txt_addAmt, txt_minrent, txt_consumer,
            txt_usage2, current_reading, txt_pre_reading, txt_pre_Rdate, txt_due, txt_billid,
            txt_category, txt_meter_no,
            txt_area, txt_name, txt_usage;

    public static String[] MY_PERMISSIONS_STORAGE = {
            "android.permission.CAMERA",
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.MOUNT_UNMOUNT_FILESYSTEMS"};
    public static final int REQUEST_EXTERNAL_STORAGE = 1;

      public  static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=this;
        setContentView(R.layout.activity_read_conformation);
        db = new DataBase(this);
        printClass = new PrintClass();
        txt_total = findViewById(R.id.txt_total);
        txt_fine = findViewById(R.id.txt_fine);
        txt_prebal = findViewById(R.id.txt_prebal);
        txt_consumer = findViewById(R.id.txt_consumer);
        serviceAmt = findViewById(R.id.serviceAmt);
        txt_addAmt = findViewById(R.id.txt_addAmt);
        txt_minrent = findViewById(R.id.txt_minrent);
        txt_usage2 = findViewById(R.id.txt_usage2);
        current_reading = findViewById(R.id.current_reading);
        txt_pre_reading = findViewById(R.id.txt_pre_reading);
        txt_pre_Rdate = findViewById(R.id.txt_pre_Rdate);
        txt_due = findViewById(R.id.txt_due);
        txt_billid = findViewById(R.id.txt_billid);
        txt_category = findViewById(R.id.txt_category);
        txt_meter_no = findViewById(R.id.txt_meter_no);
        txt_area = findViewById(R.id.txt_area);
        txt_name = findViewById(R.id.txt_name);
        txt_usage = findViewById(R.id.txt_usage);
        Intent i = getIntent();
        readModel = (ReadModel) i.getSerializableExtra("key1");
        cModel = (CustomerModel) i.getSerializableExtra("key2");
        setView();
    }

    public void setView() {
        txt_total.setText(S_P.R + readModel.getTotal());
        txt_fine.setText(S_P.R + cModel.getLatefee());
        txt_prebal.setText(S_P.R + cModel.getPrevBalance());
        serviceAmt.setText(S_P.R + cModel.getServiceCharge());
        txt_addAmt.setText(S_P.R + cModel.getAdditionalCharges());
        txt_minrent.setText(S_P.R + cModel.getMinrent());
        txt_usage2.setText(readModel.getUsage());
        txt_consumer.setText(cModel.getConsumer_no());
        current_reading.setText(readModel.getReading());
        txt_pre_reading.setText(cModel.getPrevReading());
        txt_pre_Rdate.setText(cModel.getPrevReadingDate());
        txt_due.setText(cModel.getDays());
        txt_billid.setText(readModel.getBillid());
        txt_category.setText(cModel.getCategory());
        txt_meter_no.setText(cModel.getMeter_number());
        txt_area.setText(cModel.getLocation());
        txt_name.setText(cModel.getConsumer());
        txt_usage.setText(S_P.R + readModel.getBillAmount());
    }


    public void savetoDbase(View v) {
        int id = v.getId();
        if (id == R.id.Bt_save) {
            if (db.insertreading(readModel,getApplicationContext())) {
                ReadProfile.Readpro.finish();
                TakeReadingPage.TakeRead.finish();
                DynamicToast.make(this, "Inserted").show();


                finish();
            } else {
                Snackbar.make(findViewById(R.id.root_rl), "Something went wrong!", Snackbar.LENGTH_LONG).show();

            }


        } else if (id == R.id.Bt_print) {

            showalertbox();

        }
    }

    public void showalertbox() {
        String where_conditon2 = " WHERE customer_guid ='" + cModel.getCustomerID() + "'";

        BillModel bb=db.SingleBill(where_conditon2);
        if(bb!=null){

            requestPermission();
            return;
        }




        ViewGroup viewGroup = findViewById(android.R.id.content);

        View dialogView = LayoutInflater.from(ReadConformation.this).inflate(R.layout.alertbox3, viewGroup, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(ReadConformation.this);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        ElasticButton bt_yes = dialogView.findViewById(R.id.bt_yes);
        ElasticButton bt_no = dialogView.findViewById(R.id.bt_no);
        bt_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (db.insertreading(readModel,getApplicationContext())) {
                    ReadProfile.Readpro.finish();
                    TakeReadingPage.TakeRead.finish();
                    finish();
                    Intent intent = new Intent(ReadConformation.this, Billing_throughreading.class);
                    intent.putExtra("key1", cModel.getConsumer_no());

                    startActivity(intent);
                    dialog.cancel();
                }


            }
        });
        bt_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                requestPermission();
                dialog.cancel();


            }
        });


        dialog.show();
        dialog.setCancelable(false);
    }

    private void requestPermission() {
        //检测是否有写的权限
        //Check if there is write permission
        int checkCallPhonePermission = ContextCompat.checkSelfPermission(ReadConformation.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(ReadConformation.this, MY_PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        } else {

            if (db.insertreading(readModel,getApplicationContext())) {

                if (AllstaticObj.whichPrinter == 1) {
                    printClass.Getdata(ReadConformation.this, cModel.getCustomerID(), AllstaticObj.whichPrinter);
                    fullback();


                } else if (AllstaticObj.whichPrinter == 2) {
                 set_BlueThooth();



                } else if (AllstaticObj.whichPrinter == 3) {
                    printClass.Getdata(ReadConformation.this, cModel.getCustomerID(), AllstaticObj.whichPrinter);
                    fullback();


                }else {
                    finish();


                }



            } else {
                Snackbar.make(findViewById(R.id.root_rl), "Something  went Wrong", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {

                requestPermission();
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
                printClass.Getdata(ReadConformation.this,cModel.getCustomerID(), AllstaticObj.whichPrinter);
                dialog.dismiss();
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
            printClass.Getdata(ReadConformation.this,cModel.getCustomerID(), AllstaticObj.whichPrinter);
            fullback();

        } else {

            setbluethooth();
        }



    }

    public void fullback(){



        ReadProfile.Readpro.finish();
        TakeReadingPage.TakeRead.finish();
        finish();
    }


    private void setbluethooth() {


        if (CXBlueTooth.getInstance().isBlueToothOpen()) {
            startScan();
        } else {
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ReadConformation.this);
            builder.setTitle("Open the bluethooth");
            builder.setMessage("The Bluethooth");
            builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    if (ActivityCompat.checkSelfPermission(ReadConformation.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
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
            androidx.appcompat.app.AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }


    }


    @SuppressLint("HandlerLeak")
    private android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_CONN_SCAN:
                    SharePreferecnceUtils.setPREFERENCES(ReadConformation.this, new Gson().toJson(deviceScanBean));
                    txtConnectdevice.setText(deviceScanBean.getDeviceName() + deviceScanBean.getMacAddress());
                    break;
                case MSG_DISCONN_SCAN:
                    SharePreferecnceUtils.clearALLPREFERENCES(ReadConformation.this);
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

        View dialogView = LayoutInflater.from(ReadConformation.this).inflate(R.layout.alertset_bluetooth, viewGroup, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(ReadConformation.this);
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

        ArrayAdapter spinnerAdapter1 = new ArrayAdapter<String>(ReadConformation.this, R.layout.sp_txt, lists_str);

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
            } else {
                finish();
            }

    }


}