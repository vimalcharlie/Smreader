package com.example.newsmreader.frontScreens;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cspos.BuildConfig;
import com.example.newsmreader.Adapter.settingRecycler;
import com.example.newsmreader.AllstaticObj;
import com.example.newsmreader.Dtabase.DataBase;
import com.example.newsmreader.Dtabase.Tables_;
import com.example.newsmreader.Models.BillModel;
import com.example.newsmreader.Models.ReadModel;
import com.example.newsmreader.Models.ReadingsaveModel;
import com.example.newsmreader.R;
import com.example.newsmreader.RegisterPage;
import com.example.newsmreader.S_P;
import com.example.newsmreader.ServiceRetrofit.Retrofit;
import com.example.newsmreader.SucessPage;
import com.example.newsmreader.billing.TakeBillingPage;
import com.example.newsmreader.btsheet.BottomNav;
import com.example.newsmreader.btsheet.Bottomsheet2;
import com.example.newsmreader.onClickPrintSetting;
import com.example.newsmreader.onFeatchdataListner;
import com.google.android.material.snackbar.Snackbar;
import com.skydoves.elasticviews.ElasticLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SecondFragment extends Fragment implements onClickPrintSetting {

    settingRecycler settingRecycler;
    RecyclerView reyview;
    ElasticLayout export_bt;
    TextView txt_readCount, txt_billCount,importdate,txt_click;
    DataBase dataBase;
    private Retrofit retrofit = Retrofit.getInstance();
    ProgressBar progressBar;
    LinearLayout linearLayout;
    ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       View v = inflater.inflate(R.layout.secondfragment_a, container, false);
        imageView=v.findViewById(R.id.imgclick);
        dataBase = new DataBase(getActivity());
        linearLayout=v.findViewById(R.id.root_rl);
        txt_readCount = v.findViewById(R.id.txt_readCount);
        progressBar=v.findViewById(R.id.progressBar);
        importdate=v.findViewById(R.id.importdate);
        txt_billCount = v.findViewById(R.id.txt_billCount);
        export_bt = v.findViewById(R.id.export_bt);
        reyview = v.findViewById(R.id.reyview);
        txt_click=v.findViewById(R.id.txt_click);
        txt_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BottomNav bottomSheetFragment = new BottomNav();
                bottomSheetFragment.show(getActivity().getSupportFragmentManager(), bottomSheetFragment.getTag());
            }
        });
        settingRecycler = new settingRecycler(getActivity(),this::onClick);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        reyview.setLayoutManager(linearLayoutManager);
        reyview.setAdapter(settingRecycler);
        getBasicDetais();


        export_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                savetheSingleData(1);


            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkpermission();

            }
        });


        return v;
    }

    private void savetheSingleData(int is_) {

        initializeValues();
        if (dataBase.getCount(Tables_.TABLE_READING) > 0) {
            progressBar.setVisibility(View.VISIBLE);
            ReadModel readModel = dataBase.getreadingList();
            savetodatabase(readModel);
        } else if (dataBase.getCount(Tables_.TABLE_BILL) > 0) {
            BillModel billModel = dataBase.getBillList();
            savetodatabaseBill(billModel);

        } else {
            if (is_ == 0) {
                progressBar.setVisibility(View.GONE);

                Snackbar.make(getView().findViewById(R.id.root_rl), "Sync successful", Snackbar.LENGTH_LONG).show();
                dataBase.Droptables();


            } else {
                Snackbar.make(getView().findViewById(R.id.root_rl), "There is No Data To Sync !", Snackbar.LENGTH_LONG).show();

            }


        }
    }

    private void savetodatabaseBill(BillModel billModel) {
        retrofit.getApi().upload_Bill("" + branch_code
                        , "" + billModel.getCustomer_guid()
                        , "" + billModel.getAmount()
                        , "" + billModel.getRecieved()
                        , "" + billModel.getBalance()
                        , "" + billModel.getLatefee()
                        , "" + billModel.getPayment_date()
                        , "" + billModel.getOwner_guid()
                        , "" + billModel.getAddtnl()
                        , "" + billModel.getBillRecvd()
                        , "" + billModel.getPaid_time()
                        , "" + billModel.getSpotBilling()
                        , "" + billModel.getSpotBillingCharge()
                        , "" + billModel.getRecNo()
                        , "" + billModel.getOnline()
                        , "" +ImportDate
                        , "" + billModel.getReference())
                .enqueue(new Callback<ReadingsaveModel>() {
                    @Override
                    public void onResponse(Call<ReadingsaveModel> call, Response<ReadingsaveModel> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getStatus().equalsIgnoreCase("true")) {

                                if (dataBase.Droptablebillingrow(response.body().getData().toString())) {
                                    savetheSingleData(0);

                                } else {
                                    Snackbar.make(getView().findViewById(R.id.root_rl), "Something  went wrong", Snackbar.LENGTH_LONG).show();
                                }

                            } else {
                                Snackbar.make(getView().findViewById(R.id.root_rl), "Something  went wrong", Snackbar.LENGTH_LONG).show();


                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<ReadingsaveModel> call, Throwable t) {
                        Snackbar.make(getView().findViewById(R.id.root_rl), "OOPS! NO INTERNET", Snackbar.LENGTH_LONG).show();

                    }
                });


    }



    private void savetodatabase(ReadModel readModel) {

        retrofit.getApi().upload_Reading("" + branch_code, "" + readModel.getMobile(), "" + readModel.getMeter_number(), readModel.getLatitude()
                        , "" + readModel.getLongitude(), "" + readModel.getImporteddate(), "" + readModel.getCustomer_guid()
                        , "" + readModel.getReading(), "" + readModel.getPrevious_Bal(), "" + readModel.getMinRent(), "" + readModel.getLatefee()
                        , "" + readModel.getAdditional(), "" + readModel.getRead_date(), "" + readModel.getRead_time(), "" + readModel.getOwner_guid()
                        , "" + readModel.getBillAmount(), "" + readModel.getSpotBilling(), "" + readModel.getTotal()
                        , "" + readModel.getSpotBillingCharge(), "" + readModel.getPrevReading(), "" + readModel.getBillid()
                        ,""+readModel.getAverage(),readModel.getHold(),readModel.getClosed())
                .enqueue(new Callback<ReadingsaveModel>() {
                    @Override
                    public void onResponse(Call<ReadingsaveModel> call, Response<ReadingsaveModel> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getStatus().equalsIgnoreCase("true")) {
                                String data = response.body().getData().toString();
                                if (dataBase.Droptablereadingrow(data)) {

                                    savetheSingleData(0);
                                } else {
                                    Snackbar.make(getView().findViewById(R.id.root_rl), "Something  went wrong", Snackbar.LENGTH_LONG).show();
                                }

                            } else {
                                Snackbar.make(getView().findViewById(R.id.root_rl), "Something  went wrong", Snackbar.LENGTH_LONG).show();

                            }


                        }


                    }

                    @Override
                    public void onFailure(Call<ReadingsaveModel> call, Throwable t) {
                        Log.e(">>>>>>>>>>>>>>>", ">>>>>>>>>>>>>>>>>>>>>>>>>>>" + t.toString());
                        Log.e("call,", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + call.toString());
                        Snackbar.make(getView().findViewById(R.id.root_rl), t.toString(), Snackbar.LENGTH_LONG).show();

                    }
                });


    }


    String branch_code, User_id,ImportDate;

    private void getBasicDetais() {

        ImportDate=S_P.getPREFERENCES(getActivity(), S_P.Importeddate);
        String retrieve = S_P.getJsonData(getActivity());
        try {
            JSONObject jsonObject = new JSONObject(retrieve);
            branch_code = jsonObject.getString("branch_code");
            User_id = jsonObject.getString("id");


        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


    }

    private void initializeValues() {
        txt_readCount.setText("" + dataBase.getCount(Tables_.TABLE_READING));
        txt_billCount.setText("" + dataBase.getCount(Tables_.TABLE_BILL));
        importdate.setText(ImportDate);
    }

    @Override
    public void onResume() {
        super.onResume();
        initializeValues();
    }






    public static final int REQUEST_EXTERNAL_BLUETHOOTHSCAN = 1;

    public static String[] MY_PERMISSIONS = {
            "android.permission.BLUETOOTH_SCAN",
            "android.permission.BLUETOOTH_CONNECT",
            "android.permission.MANAGE_EXTERNAL_STORAGE"
         };
    public  void requestPermission(){


        int checkPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.BLUETOOTH_SCAN);
        int checkPermission2 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.BLUETOOTH_CONNECT);

        if (checkPermission != PackageManager.PERMISSION_GRANTED ||checkPermission2 != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),MY_PERMISSIONS, REQUEST_EXTERNAL_BLUETHOOTHSCAN);
        }  else {
            S_P.setPREFERENCESint(c,S_P.printSet,+count+1);
            AllstaticObj.getPREFERENCES(c);
            settingRecycler.notifyDataSetChanged();


        }





    }


       Context c;
       int count;
    @Override
    public void onClick(Context context, int count) {

        c=context;
        this.count=count;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ){

            requestPermission();
        } else{
            S_P.setPREFERENCESint(c,S_P.printSet,+count+1);
            AllstaticObj.getPREFERENCES(c);
            settingRecycler.notifyDataSetChanged();
        }




    }




    public void setlays(boolean _is){
        if(_is){
            S_P.setPREFERENCESint(getActivity(),S_P.printSet,+count+1);
            AllstaticObj.getPREFERENCES((getActivity()));
            settingRecycler.notifyDataSetChanged();
        }else {
            Snackbar.make(linearLayout,"Permission un granted",Snackbar.LENGTH_LONG).show();
        }




    }



    private  void checkpermission(){


        shareDatabase();


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
//               Toast.makeText(getActivity(),"1",Toast.LENGTH_SHORT).show();
//            }else {
//                Toast.makeText(getActivity(),"2",Toast.LENGTH_SHORT).show();
//
//                shareDatabase();
//
//            }
//        }
    }



    private File copyDatabaseToExternalStorage() throws IOException {
        File externalFile = new File(getActivity().getExternalFilesDir(null), "smreaders.db");
        try (InputStream inputStream = new FileInputStream(getActivity().getDatabasePath("smreaders.db"));
             OutputStream outputStream = new FileOutputStream(externalFile)) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        }
        return externalFile;
    }


    private void shareDatabase() {
        try {
            File dbFile = copyDatabaseToExternalStorage();
            Uri dbUri = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".fileprovider", dbFile);

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("application/octet-stream");
            intent.putExtra(Intent.EXTRA_STREAM, dbUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivity(Intent.createChooser(intent, "Share Database"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
