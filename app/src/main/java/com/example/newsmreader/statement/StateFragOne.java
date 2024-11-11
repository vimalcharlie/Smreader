package com.example.newsmreader.statement;

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
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.newsmreader.Adapter.StatementAdapter;
import com.example.newsmreader.Adapter.StatementAdapter2;
import com.example.newsmreader.AllstaticObj;
import com.example.newsmreader.Bluethhoth.DeviceScanBean;
import com.example.newsmreader.Bluethhoth.SharePreferecnceUtils;
import com.example.newsmreader.Models.ReadPrintModel;
import com.example.newsmreader.Models.ReadPrintModelArray;
import com.example.newsmreader.Models.StamentreadModel;
import com.example.newsmreader.Models.StamentreadModelArray;
import com.example.newsmreader.Models.StateMentModel;
import com.example.newsmreader.Models.StateMentModelArray;
import com.example.newsmreader.PrintClass;
import com.example.newsmreader.R;
import com.example.newsmreader.S_P;
import com.example.newsmreader.ServiceRetrofit.Retrofit;
import com.example.newsmreader.StateMent;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.skydoves.elasticviews.ElasticButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import site.haoyin.lib.bluetooth.CXBlueTooth;
import site.haoyin.lib.bluetooth.listener.IBlueToothListener;

public class StateFragOne extends Fragment implements onReprintListner {
    RecyclerView recyclerView;
    StatementAdapter adapter;

    ArrayList<StamentreadModel> main_list = new ArrayList<>();
    private Retrofit retrofit = Retrofit.getInstance();

    TabLayout tableLayout;
    PrintClass printClass;
    SwipeRefreshLayout pullToRefresh;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.statement_frag, container, false);
        recyclerView = v.findViewById(R.id.reyclr);
        pullToRefresh = v.findViewById(R.id.pullToRefresh);

        printClass = new PrintClass();
        adapter = new StatementAdapter(getActivity(), main_list, this);
        recyclerView.setAdapter(adapter);
        tableLayout = getActivity().findViewById(R.id.tabLayout);
        getBasicDetais();


        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                _getthedata("", S_P.from_state_date, S_P.to_state_date, AllstaticObj.WhichLocation);
                pullToRefresh.setRefreshing(false);
            }
        });


        _getthedata("", S_P.from_state_date, S_P.to_state_date, AllstaticObj.WhichLocation);

        return v;
    }


    public void _getthedata(String filter, String fromdate, String todate, String location) {
        pullToRefresh.setRefreshing(true);

        main_list = new ArrayList<>();

        retrofit.getApi().READ_STATE(branch_code, "RS", filter, fromdate, todate, location, User_id)
                .enqueue(new Callback<StamentreadModelArray>() {
                    @Override
                    public void onResponse(Call<StamentreadModelArray> call, Response<StamentreadModelArray> response) {
                        pullToRefresh.setRefreshing(false);
                        if (response.isSuccessful()) {
                            System.out.println(response.body().toString());

                            if (response.body().getStatus().equalsIgnoreCase("true")) {
                                pullToRefresh.setRefreshing(false);
                                ArrayList<StamentreadModel> list = response.body().getData();

                                for (StamentreadModel obj : list) {

                                    main_list.add(new StamentreadModel(obj.getId(), obj.getConsumer(), obj.getConsumer_no(), obj.getReading(), obj.getTotal()));


                                }
                                adapter.setItems(main_list);
                                adapter.notifyDataSetChanged();


                            } else {


                                adapter.setItems(main_list);
                                adapter.notifyDataSetChanged();


                            }


                        } else {


                            adapter.setItems(main_list);
                            adapter.notifyDataSetChanged();


                        }
                        tableLayout.getTabAt(0).view.getTab().setText("Reading (" + main_list.size() + ")");


                    }

                    @Override
                    public void onFailure(Call<StamentreadModelArray> call, Throwable t) {
                        pullToRefresh.setRefreshing(false);
                    }
                });


    }


    public void setView(String filter) {
        ArrayList<StamentreadModel> temp_list = new ArrayList<>();
        if (main_list.size() != 0) {
            for (StamentreadModel obj : main_list) {
                if (obj.getConsumer_no().toLowerCase().contains(filter) || obj.getConsumer().toLowerCase().contains(filter))
                    temp_list.add(obj);
            }

            adapter.setItems(temp_list);
            adapter.notifyDataSetChanged();
        }


    }

    String branch_code, User_id;

    private void getBasicDetais() {


        String retrieve = S_P.getJsonData(getActivity());
        try {
            JSONObject jsonObject = new JSONObject(retrieve);
            branch_code = jsonObject.getString("branch_code");
            User_id = jsonObject.getString("id");


        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


    }

    AlertDialog dialog1;
    ProgressBar progressBar;

    @Override
    public void onReprint(String id) {
        ViewGroup viewGroup = getActivity().findViewById(android.R.id.content);

        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.alertbox3, viewGroup, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView);
        dialog1 = builder.create();
        TextView textView = dialogView.findViewById(R.id.txt);
        progressBar=dialogView.findViewById(R.id.progressBar);
        textView.setText("Do you want to do the\nreprint?");
        ElasticButton bt_yes = dialogView.findViewById(R.id.bt_yes);
        ElasticButton bt_no = dialogView.findViewById(R.id.bt_no);

        bt_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar.setVisibility(View.VISIBLE);
                getPrintdetails(id);


            }
        });
        bt_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                dialog1.cancel();


            }
        });


        dialog1.show();
        dialog1.setCancelable(false);
    }

    private void getPrintdetails(String id) {

        retrofit.getApi().getReprintReading(branch_code, id)
                .enqueue(new Callback<ReadPrintModelArray>() {
                    @Override
                    public void onResponse(Call<ReadPrintModelArray> call, Response<ReadPrintModelArray> response) {

                        if (response.isSuccessful()) {
                            if (response.body().getStatus().equalsIgnoreCase("true")) {
                                dialog1.dismiss();
                                ArrayList<ReadPrintModel> readPrintModel = response.body().getData();
                                Log.e(">>>>>>>>", "" + readPrintModel.get(0).getCustomer_guid());
                                Log.e("getReading>>>>>>>>", "" + readPrintModel.get(0).getReading());
                                Log.e("getPrevious_Bal>>>>>>>>", "" + readPrintModel.get(0).getPrevious_Bal());
                                Log.e("getRead_date>>>>>>>>", "" + readPrintModel.get(0).getRead_date());
                                Log.e("getLatefee>>>>>>>>", "" + readPrintModel.get(0).getMinRent());
                                Log.e("getLatefee>>>>>>>>", "" + readPrintModel.get(0).getLatefee());
                                Log.e("getAdditional>>>>>>>>", "" + readPrintModel.get(0).getAdditional());
                                Log.e("getLatefee>>>>>>>>", "" + readPrintModel.get(0).getLatefee());
                                Log.e("getOwner_guid>>>>>>>>", "" + readPrintModel.get(0).getOwner_guid());
                                Log.e("getBillAmount>>>>>>>>", "" + readPrintModel.get(0).getBillAmount());
                                Log.e("getTotal>>>>>>>>", "" + readPrintModel.get(0).getTotal());
                                Log.e("getSpotBilling>>>>>>>>", "" + readPrintModel.get(0).getSpotBillingCharge());
                                Log.e("getPrevReading>>>>>>>>", "" + readPrintModel.get(0).getPrevReading());
                                Log.e("getMobile>>>>>>>>", "" + readPrintModel.get(0).getMobile());
                                Log.e("getMeter_number>>>>>>>>", "" + readPrintModel.get(0).getMeter_number());
                                Log.e("getPrevReadingDa>>", "" + readPrintModel.get(0).getPrevReadingDate());
                                Log.e("getPrevReadingD>>>>>>", "" + readPrintModel.get(0).getPrevReadingDate());
                                Log.e("getConsumer>>>>>>>>", "" + readPrintModel.get(0).getConsumer());
                                Log.e("getInvoice>>>>>>>>", "" + readPrintModel.get(0).getInvoice());
                                Log.e("getDuedate>>>>>>>>", "" + readPrintModel.get(0).getDuedate());
                                Log.e("getLocation>>>>>>>>", "" + readPrintModel.get(0).getLocation());
                                Log.e(">>>>>>>getUser>", "" + readPrintModel.get(0).getUser());
                                Log.e(">>>>>>>>getNoofMonths", "" + readPrintModel.get(0).getNoofMonths());
                                Log.e(">>>>>>>>getPassword", "" + readPrintModel.get(0).getPassword());
                                if (AllstaticObj.whichPrinter == 2) {
                                    set_BlueThooth(readPrintModel.get(0));
                                } else {
                                    printClass.readingReprint(readPrintModel.get(0), AllstaticObj.whichPrinter, getActivity());
                                }

                            }


                        }


                    }

                    @Override
                    public void onFailure(Call<ReadPrintModelArray> call, Throwable t) {
                        dialog1.dismiss();
                        Snackbar.make(getActivity().findViewById(R.id.root_Rl),"OOPS! Something went wrong",Snackbar.LENGTH_LONG).show();
                    }
                });
    }


    //////////////////////////new bluethooth//////////////////////////////
    private ArrayList<DeviceScanBean> lists = new ArrayList<DeviceScanBean>();
    private List<String> lists_str = new ArrayList<>();
    private DeviceScanBean deviceScanBean;
    private static final int REQUEST_ENABLE_BT = 1;
    private CXBlueTooth BT;
    private static final int MSG_CONN_SCAN = 1001;
    private static final int MSG_DISCONN_SCAN = 1002;

    public void set_BlueThooth(ReadPrintModel readPrintModel) {


        BT = CXBlueTooth.getInstance();
        BT.setBlueToothListener(new IBlueToothListener() {
            @Override
            public void onConnected() {
                CXBlueTooth.getInstance().setConnected(true);
                mHandler.sendEmptyMessageDelayed(MSG_CONN_SCAN, 1000);
                printClass.readingReprint(readPrintModel, AllstaticObj.whichPrinter, getActivity());
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
            printClass.readingReprint(readPrintModel, AllstaticObj.whichPrinter, getActivity());

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
