package com.example.newsmreader.reading;

import android.Manifest;
import android.annotation.SuppressLint;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsmreader.Adapter.ReadFragAdapter2;
import com.example.newsmreader.AllstaticObj;
import com.example.newsmreader.Bluethhoth.DeviceScanBean;
import com.example.newsmreader.Bluethhoth.SharePreferecnceUtils;
import com.example.newsmreader.Dtabase.Column_;
import com.example.newsmreader.Dtabase.DataBase;
import com.example.newsmreader.Models.CustomerModel;
import com.example.newsmreader.PrintClass;
import com.example.newsmreader.R;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.skydoves.elasticviews.ElasticButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import site.haoyin.lib.bluetooth.CXBlueTooth;
import site.haoyin.lib.bluetooth.listener.IBlueToothListener;

public class ReadFragtwo extends Fragment implements onReprintListner{
    RecyclerView recyclerView;
    ReadFragAdapter2 adapter;
    ArrayList<CustomerModel> mList=new ArrayList<>();
    DataBase dataBase;
    TabLayout tabLayout;

    PrintClass printClass;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.readcomplete_frag,container,false);
        dataBase=new DataBase(getActivity());
        printClass=new PrintClass();
        tabLayout=getActivity().findViewById(R.id.tabLayout);



        recyclerView=view.findViewById(R.id.reyclr);
        adapter=new ReadFragAdapter2(getActivity(),mList,getActivity(),this);

        recyclerView.setAdapter(adapter);
        return view;
    }



    @Override
    public void onResume() {
        super.onResume();
        String where_condition=" WHERE "+ Column_.isRead+"='1'  AND readingexists='0'";
        mList=dataBase.CustomerList(where_condition);
        tabLayout.getTabAt(1).view.getTab().setText("Completed ("+mList.size()+")");
        if(mList.size()>0){
            Collections.reverse(mList);
            adapter.setItems(mList);
            adapter.notifyDataSetChanged();
        }else {



        }



    }

    @Override
    public void onReprint(String id) {
        ViewGroup viewGroup = getActivity().findViewById(android.R.id.content);

        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.alertbox3, viewGroup, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        TextView textView=dialogView.findViewById(R.id.txt);
        textView.setText("Do you want to do the\nreprint?");
        ElasticButton bt_yes = dialogView.findViewById(R.id.bt_yes);
        ElasticButton bt_no = dialogView.findViewById(R.id.bt_no);
        bt_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(AllstaticObj.whichPrinter==1){
                    printClass.Getdata(getActivity(),id,AllstaticObj.whichPrinter);
                } else if (AllstaticObj.whichPrinter==2) {
                    set_BlueThooth(id);
                } else if (AllstaticObj.whichPrinter==3) {
                    printClass.Getdata(getActivity(),id,AllstaticObj.whichPrinter);
                }
                dialog.dismiss();


            }
        });
        bt_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                dialog.cancel();


            }
        });


        dialog.show();
        dialog.setCancelable(false);
    }
    public void filter_(String s){

        ArrayList<CustomerModel> t_list=new ArrayList<>();
        for (CustomerModel obj:mList) {
            if (obj.getConsumer_no().contains(s)
                    ||obj.getConsumer().toLowerCase().contains(s.toLowerCase())
                    ||obj.getMeter_number().contains(s)){
                t_list.add(obj);


            }

        }
        Collections.reverse(t_list);
        try {
            adapter.setItems(t_list);
            adapter.notifyDataSetChanged();
        }catch (NullPointerException e){

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

    public void set_BlueThooth(String id) {


        BT = CXBlueTooth.getInstance();
        BT.setBlueToothListener(new IBlueToothListener() {
            @Override
            public void onConnected() {
                CXBlueTooth.getInstance().setConnected(true);
                mHandler.sendEmptyMessageDelayed(MSG_CONN_SCAN, 1000);
                printClass.Getdata(getActivity(),id, AllstaticObj.whichPrinter);
                dialog.dismiss();



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



        if (CXBlueTooth.getInstance().isConnected()) {


            printClass.Getdata(getActivity(),id,AllstaticObj.whichPrinter);

        } else {

            setbluethooth();
        }


    }




    private void setbluethooth() {


        if (CXBlueTooth.getInstance().isBlueToothOpen()) {
            startScan();
        } else {
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
            builder.setTitle("Open the bluethooth");
            builder.setMessage("The Bluethooth");
            builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
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
                    SharePreferecnceUtils.setPREFERENCES(getActivity(), new Gson().toJson(deviceScanBean));
                    txtConnectdevice.setText(deviceScanBean.getDeviceName() + deviceScanBean.getMacAddress());
                    break;
                case MSG_DISCONN_SCAN:
                    SharePreferecnceUtils.clearALLPREFERENCES(getActivity());
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

        ViewGroup viewGroup = getActivity().findViewById(android.R.id.content);

        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.alertset_bluetooth, viewGroup, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

        ArrayAdapter spinnerAdapter1 = new ArrayAdapter<String>(getActivity(), R.layout.sp_txt, lists_str);

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == getActivity().RESULT_OK) {
                startScan();
            }
            if (resultCode == getActivity().RESULT_CANCELED) {

            }
        } else {

        }

    }

















}
