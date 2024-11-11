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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.example.newsmreader.Models.BillPrintModel;
import com.example.newsmreader.Models.BillPrintModelArray;
import com.example.newsmreader.Models.ReadPrintModel;
import com.example.newsmreader.Models.ReadPrintModelArray;
import com.example.newsmreader.Models.StamentreadModel;
import com.example.newsmreader.Models.StateMentModel;
import com.example.newsmreader.Models.StateMentModelArray;
import com.example.newsmreader.PrintClass;
import com.example.newsmreader.R;
import com.example.newsmreader.S_P;
import com.example.newsmreader.ServiceRetrofit.Retrofit;
import com.example.newsmreader.btsheet.BottomNav;
import com.example.newsmreader.btsheet.StateBillNav;
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

public class StateFragtwo extends Fragment implements onReprintListner {
    RecyclerView recyclerView;
    StatementAdapter2 adapter;
    ElasticButton knowmore;

    ArrayList<StateMentModel> main_list = new ArrayList<>();
    TabLayout tabLayout;

    private Retrofit retrofit = Retrofit.getInstance();
    PrintClass printClass;
    SwipeRefreshLayout pullToRefresh;
    LinearLayout lnr;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.statement_frag_bill, container, false);
        knowmore=view.findViewById(R.id.knowmore);
        recyclerView = view.findViewById(R.id.reyclr);
        txt_total = view.findViewById(R.id.txt_total);
        lnr=view.findViewById(R.id.lnr);
        pullToRefresh = view.findViewById(R.id.pullToRefresh);
        printClass = new PrintClass();
        tabLayout = getActivity().findViewById(R.id.tabLayout);
        adapter = new StatementAdapter2(getActivity(), main_list, this);
        recyclerView.setAdapter(adapter);
        getBasicDetais();

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                _getinitial_data("", S_P.from_state_date, S_P.to_state_date, AllstaticObj.WhichLocation);
                pullToRefresh.setRefreshing(false);
            }
        });
        knowmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                StateBillNav stateBillNav = new StateBillNav();
                stateBillNav.show(getActivity().getSupportFragmentManager(), stateBillNav.getTag());
            }
        });

        _getinitial_data("", S_P.from_state_date, S_P.to_state_date, AllstaticObj.WhichLocation);
        return view;
    }

    long onlineamt, cashamt;
    TextView txt_total;

    public void _getinitial_data(String filter, String fromdate, String todate, String location) {
        lnr.setVisibility(View.VISIBLE);
        main_list = new ArrayList<>();
        pullToRefresh.setRefreshing(true);
        retrofit.getApi().Bill_STATE(branch_code, "BS", "" + filter, "" + fromdate, "" + todate, location, User_id)
                .enqueue(new Callback<StateMentModelArray>() {
                    @Override
                    public void onResponse(Call<StateMentModelArray> call, Response<StateMentModelArray> response) {
                        pullToRefresh.setRefreshing(false);
                        if (response.isSuccessful()) {
                            if (response.body().getStatus().equalsIgnoreCase("true")) {

                                ArrayList<StateMentModel> list = response.body().getData();

                                for (StateMentModel obj : list) {


                                    main_list.add(new StateMentModel(obj.getConsumer(), obj.getConsumer_no(), obj.getReceived(), obj.getTotal(), obj.getId(), obj.getOnline(), obj.getCash()));
                                    Log.e(">>>>>>>", ">>>>");


                                }
                                onlineamt = Long.parseLong(main_list.get(0).getOnline());
                                cashamt = Long.parseLong(main_list.get(0).getCash());

                                AllstaticObj.onlineamt=onlineamt;
                                AllstaticObj.cashamt=cashamt;
                                txt_total.setText(S_P.R+String.valueOf(onlineamt + cashamt));
                                adapter.setItems(main_list);
                                adapter.notifyDataSetChanged();

                                if (main_list.size()==0){
                                    lnr.setVisibility(View.GONE);

                                }else {
                                    lnr.setVisibility(View.VISIBLE);
                                }


                            } else {
                                lnr.setVisibility(View.GONE);

                                adapter.setItems(main_list);
                                adapter.notifyDataSetChanged();
                            }


                        } else {
                            lnr.setVisibility(View.GONE);

                            adapter.setItems(main_list);
                            adapter.notifyDataSetChanged();
                        }

                        tabLayout.getTabAt(1).view.getTab().setText("Billing (" + main_list.size() + ")");

                    }

                    @Override
                    public void onFailure(Call<StateMentModelArray> call, Throwable t) {
                        pullToRefresh.setRefreshing(false);
                        lnr.setVisibility(View.GONE);

                    }
                });


    }


    public void setView(String filter) {
        ArrayList<StateMentModel> temp_list = new ArrayList<>();
        if (main_list.size() != 0) {
            for (StateMentModel obj : main_list) {
                if (obj.getConsumer_no().toLowerCase().contains(filter) || obj.getConsumer().toLowerCase().contains(filter))
                    temp_list.add(obj);
            }

            if (temp_list.size()==main_list.size()){
                lnr.setVisibility(View.VISIBLE);
            }else {
                lnr.setVisibility(View.GONE);

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


    ProgressBar progressBar;
    AlertDialog dialog1;

    @Override
    public void onReprint(String id) {
        ViewGroup viewGroup = getActivity().findViewById(android.R.id.content);

        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.alertbox3, viewGroup, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView);
        dialog1 = builder.create();
        TextView textView = dialogView.findViewById(R.id.txt);
        progressBar = dialogView.findViewById(R.id.progressBar);
        textView.setText("Do you want to do the\nreprint?");
        ElasticButton bt_yes = dialogView.findViewById(R.id.bt_yes);
        ElasticButton bt_no = dialogView.findViewById(R.id.bt_no);
        bt_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                getPrintdetails(id);
                progressBar.setVisibility(View.VISIBLE);

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

        retrofit.getApi().getReprintBill(branch_code, id)
                .enqueue(new Callback<BillPrintModelArray>() {
                    @Override
                    public void onResponse(Call<BillPrintModelArray> call, Response<BillPrintModelArray> response) {

                        if (response.isSuccessful()) {
                            if (response.body().getStatus().equalsIgnoreCase("true")) {
                                dialog1.dismiss();
                                ArrayList<BillPrintModel> readPrintModel = response.body().getData();

                                if (AllstaticObj.whichPrinter == 2) {
                                    set_BlueThooth(readPrintModel.get(0));


                                } else {
                                    printClass.BillingReprint(readPrintModel.get(0), AllstaticObj.whichPrinter, getActivity());
                                }


                            }


                        }


                    }

                    @Override
                    public void onFailure(Call<BillPrintModelArray> call, Throwable t) {
                        dialog1.dismiss();
                        Snackbar.make(getActivity().findViewById(R.id.root_Rl), "OOPS! Something went wrong", Snackbar.LENGTH_LONG).show();

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

    public void set_BlueThooth(BillPrintModel readPrintModel) {


        BT = CXBlueTooth.getInstance();
        BT.setBlueToothListener(new IBlueToothListener() {
            @Override
            public void onConnected() {
                CXBlueTooth.getInstance().setConnected(true);
                mHandler.sendEmptyMessageDelayed(MSG_CONN_SCAN, 1000);
                printClass.BillingReprint(readPrintModel, AllstaticObj.whichPrinter, getActivity());
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
            printClass.BillingReprint(readPrintModel, AllstaticObj.whichPrinter, getActivity());

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
