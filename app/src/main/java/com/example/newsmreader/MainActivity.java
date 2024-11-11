package com.example.newsmreader;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newsmreader.Dtabase.DataBase;
import com.example.newsmreader.Dtabase.Tables_;
import com.example.newsmreader.Models.CustomerModel;
import com.example.newsmreader.Models.CustomerModelArray;
import com.example.newsmreader.Models.LocationModel;
import com.example.newsmreader.Models.LocationModelArray;
import com.example.newsmreader.Models.LocationfilterModel;
import com.example.newsmreader.Models.SettingModel;
import com.example.newsmreader.Models.SettingModelArray;
import com.example.newsmreader.Models.SlabModel;
import com.example.newsmreader.Models.SlabModelArray;
import com.example.newsmreader.ServiceRetrofit.Retrofit;
import com.example.newsmreader.frontScreens.FirstFragment;
import com.example.newsmreader.frontScreens.SecondFragment;
import com.example.newsmreader.frontScreens.ThirdFragment;
import com.example.newsmreader.frontScreens.ViewPagerAdapter;
import com.example.newsmreader.frontScreens.progressBar.OnProgressChangeListner;
import com.example.newsmreader.frontScreens.progressBar.RoundProgressBar;
import com.google.android.flexbox.FlexboxLayout;
import com.skydoves.elasticviews.ElasticButton;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import kotlin.Pair;
import nl.joery.animatedbottombar.AnimatedBottomBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vpos.apipackage.PosApiHelper;

public class MainActivity extends AppCompatActivity implements onFeatchdataListner {

    AnimatedBottomBar bottom_bar;
    ViewPager viewPager;
    private Retrofit retrofit = Retrofit.getInstance();
    DataBase dataBase;
    ArrayList<CustomerModel> mList_Cust;
    SecondFragment fragment3=new SecondFragment();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //////////////////////////
        dataBase = new DataBase(MainActivity.this);
        /////////////////////////////
        bottom_bar = findViewById(R.id.bottom_bar);
        viewPager = findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ThirdFragment(), "ONE");
        adapter.addFragment(new FirstFragment(), "TWO");
        adapter.addFragment(fragment3, "THREE");
        viewPager.setAdapter(adapter);
        bottom_bar.setupWithViewPager(viewPager);
        bottom_bar.selectTabAt(1, true);
        getBasicDetais();
        S_P.setDates(new SimpleDateFormat("yyyy-MM-1").format(Calendar.getInstance().getTime()),
                new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));


    }

    AlertDialog ProgressDialog;
    TextView txt;

    public void alertDialog() {
        LayoutInflater layoutInflater = getLayoutInflater();
        View dialogeview = layoutInflater.inflate(R.layout.progressdialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(dialogeview);
        txt = dialogeview.findViewById(R.id.txt);
        ProgressDialog = builder.create();
        ProgressDialog.show();
        ProgressDialog.setCancelable(false);


    }

    ArrayList<LocationModel> mList;

    private void getLocation_() {
        txt.setText("Fetching location please wait");
        mList = new ArrayList<>();
        retrofit.getApi().GetFullLocation(branch_code, User_id).
                enqueue(new Callback<LocationModelArray>() {
                    @Override
                    public void onResponse(Call<LocationModelArray> call, Response<LocationModelArray> response) {

                        if (response.isSuccessful()) {
                            if (response.body().getStatus().equalsIgnoreCase("true")) {
                                ProgressDialog.cancel();
                                ArrayList<LocationModel> list = response.body().getData();
                                for (LocationModel obj : list) {
                                    mList.add(obj);
                                }
                                getthe_alert_box();
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<LocationModelArray> call, Throwable t) {

                      ProgressDialog.dismiss();


                    }
                });


    }

    public void getthe_alert_box() {


        LayoutInflater layoutInflater = getLayoutInflater();
        View dialogeview = layoutInflater.inflate(R.layout.bg_select_view, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(dialogeview);
        AlertDialog dialog = builder.create();

        FlexboxLayout flexboxLayout = dialogeview.findViewById(R.id.flexboxLayout);
        ElasticButton bt_done = dialogeview.findViewById(R.id.bt_done);
        ElasticButton reset = dialogeview.findViewById(R.id.reset);
        bt_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(srtList.size()!=0){
                    dialog.dismiss();
                    alertDialog();
                    getTheFlteredLocation();
                }




            }
        });
        addDynamicTextViews(flexboxLayout,reset);

        dialog.show();
        dialog.setCancelable(false);


    }

    int Count = 0;

    private void getTheFlteredLocation() {
        txt.setText("Please wait a moment while we are adding \nyour data!!!");
        Count = 0;
        final String loc_st = String.valueOf(srtList).replace("[", "").replace("]", "");
        retrofit.getApi().GetFilterLocation(branch_code, User_id, loc_st).
                enqueue(new Callback<LocationfilterModel>() {
                    @Override
                    public void onResponse(Call<LocationfilterModel> call, Response<LocationfilterModel> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getStatus().equalsIgnoreCase("true")) {
                                Count = Integer.parseInt(response.body().getData());
                                S_P.setPREFERENCES(MainActivity.this, S_P.Importeddate, response.body().getImporteddate());
                                S_P.setPREFERENCES(MainActivity.this,S_P.Billingpattern,response.body().getBillingpattern());


                                CusttomerAsyn custtomerAsyn = new CusttomerAsyn();
                                custtomerAsyn.doInBackground("0");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<LocationfilterModel> call, Throwable t) {
                        ProgressDialog.dismiss();
                    }
                });
    }

    @Override
    public void onFetch() {


        alertDialog();
        getLocation_();


    }

    private class CusttomerAsyn extends AsyncTask<String, String, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            txt.setText("Please wait a moment while we are adding \ncustomer  (" + Count + ") !!!");

        }

        @Override
        protected Boolean doInBackground(String... strings) {

            final String loc_st = String.valueOf(srtList).replace("[", "").replace("]", "");
            final boolean isInsert[] = {false};
            mList_Cust = new ArrayList<>();
            Log.e("BranchCode>>>", branch_code + "...........................");
            Log.e("User>>>>>>>>>", User_id + "...........................");
            Log.e("Location>>", loc_st.toString() + "...........................");
            Log.e("Count>>>>>", strings[0] + "...........................");

            retrofit.getApi().GetCustomerList(branch_code, User_id,getResources().getString(R.string.appCode), strings[0], loc_st).
                    enqueue(new Callback<CustomerModelArray>() {
                        @Override
                        public void onResponse(Call<CustomerModelArray> call, Response<CustomerModelArray> response) {
                            Log.e(">>>>>>>>>", "vvvvvvvvvvvvv????????????" + call.toString());
                            if (response.isSuccessful()) {
                                if (response.body().getStatus().equalsIgnoreCase("true")) {
                                    Log.e(">>>>>>>>>", "<???????????????????" + response.body().toString());
                                    ArrayList<CustomerModel> list = response.body().getData();
                                    for (CustomerModel obj : list)
                                        mList_Cust.add(new CustomerModel(obj.getCustomerID(), obj.getConsumer().trim(), obj.getConsumer_no(),
                                                obj.getMobile(), obj.getAddress().trim(), obj.getLocation().trim(), obj.getCategory(), obj.getMeter_number(), obj.getPrevReading(),
                                                obj.getPrevBalance(), obj.getAdditionalCharges(), obj.getServiceCharge(), obj.getEnableServiceCharge(), obj.getLatefee(),
                                                obj.getMinrent(), obj.getCatid(), obj.getPrevReadingDate(), obj.getPrevBillDate(), obj.getBasedonReadDate(), obj.getDays(),
                                                obj.getPasswords(), obj.getNoofMonths(), obj.getLatitude(), obj.getLongitude(), obj.getReadingexists()));


                                    if (dataBase.insertConsumerList(mList_Cust)) {

                                        CusttomerAsyn custtomerAsyn = new CusttomerAsyn();
                                        custtomerAsyn.execute(mList_Cust.get(mList_Cust.size() - 1).getCustomerID());
                                        Count = Count - 10;

                                    }
                                } else if (response.body().getStatus().equalsIgnoreCase("1")) {
                                    ProgressDialog.dismiss();
                                    android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(MainActivity.this);
                                    alertDialog.setTitle("Update !");
                                    alertDialog.setCancelable(false);
                                    alertDialog.setMessage("Please update the application ");


                                    alertDialog.setPositiveButton("Cancel",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // dialog.dismiss();
                                                    finish();
                                                }
                                            });
                                    alertDialog.show();
                                } else if (response.body().getStatus().equalsIgnoreCase("2")) {
                                    ProgressDialog.dismiss();
                                    android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(MainActivity.this);
                                    alertDialog.setTitle("Expired !");
                                    alertDialog.setCancelable(false);
                                    alertDialog.setMessage("Software expired ! ");


                                    alertDialog.setPositiveButton("Cancel",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    finish();
                                                }
                                            });
                                    alertDialog.show();
                                } else {

                                    SlabSync slabSync = new SlabSync();
                                    slabSync.doInBackground();


                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<CustomerModelArray> call, Throwable t) {
                            Log.e("onFailure", "vvvvvvvvvvvvv????????????" + t.toString());
                            Log.e("onFailure", "vvvvvvvvvvvvv????????????" + call.toString());
                            ProgressDialog.dismiss();
                        }
                    });


            return isInsert[0];
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean check) {
            super.onPostExecute(check);


        }
    }

    ArrayList<SlabModel> mslabList;

    private class SlabSync extends AsyncTask<String, String, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            mslabList = new ArrayList<>();
            txt.setText("This might take little longer than expected, \nplease be patient.");
            retrofit.getApi().GetSlab(branch_code).
                    enqueue(new Callback<SlabModelArray>() {
                        @Override
                        public void onResponse(Call<SlabModelArray> call, Response<SlabModelArray> response) {
                            if (response.isSuccessful()) {
                                if (response.body().getStatus().equalsIgnoreCase("true")) {
                                    ArrayList<SlabModel> list = response.body().getData();

                                    for (SlabModel obj : list) {
                                        mslabList.add(new SlabModel(obj.getId(), obj.getCategory_guid(), obj.getTitle(), obj.getUnit_from(), obj.getUnit_to(), obj.getRate(), obj.getAbove(), obj.getMinRent()));
                                    }
                                    if (dataBase.insertSlab(mslabList)) {
                                        getSettins();

                                    }

                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<SlabModelArray> call, Throwable t) {

                        }
                    });


            return null;
        }
    }


    public void getSettins() {
        txt.setText("Almost done !!");
        retrofit.getApi().GetSettiings(branch_code, User_id).
                enqueue(new Callback<SettingModelArray>() {
                    @Override
                    public void onResponse(Call<SettingModelArray> call, Response<SettingModelArray> response) {

                        if (response.isSuccessful()) {
                            if (response.body().getStatus().equalsIgnoreCase("true")) {

                                ArrayList<SettingModel> list = response.body().getData();
                                S_P.setPREFERENCESint(MainActivity.this, S_P.READ_NO, Integer.parseInt(list.get(0).getReadBillnumber()) + 1);
                                S_P.setPREFERENCESint(MainActivity.this, S_P.BILL_NO, Integer.parseInt(list.get(0).getBillnumber()) + 1);
                                S_P.setPREFERENCES(MainActivity.this, S_P.ENABLE_PAY, list.get(0).getEnable_payment());
                                S_P.setPREFERENCES(MainActivity.this, S_P.image_id, list.get(0).getImage_id());
                                S_P.setPREFERENCES(MainActivity.this, S_P.CollectioninPrint, list.get(0).getCollectioninPrint());
                                S_P.setPREFERENCES(MainActivity.this, S_P.is_online, list.get(0).getOnline());
                                S_P.setPREFERENCES(MainActivity.this, S_P.is_poweredby, list.get(0).getPoweredBy());
                                AllstaticObj.getPREFERENCES(MainActivity.this);
                                ProgressDialog.dismiss();

                            }


                        }


                    }

                    @Override
                    public void onFailure(Call<SettingModelArray> call, Throwable t) {

                    }
                });

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
    protected void onResume() {
        super.onResume();
        AllstaticObj.getPREFERENCES(MainActivity.this);
        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        // Configure the behavior of the hidden system bars.
        windowInsetsController.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        );

        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());
        windowInsetsController.show(WindowInsetsCompat.Type.statusBars());


    }


    ///get the dynamic text view
    ArrayList srtList;

    private void addDynamicTextViews(FlexboxLayout flexboxLayout,ElasticButton reset) {

        srtList = new ArrayList();
        for (LocationModel obj : mList) {

            TextView textView = new TextView(this);
            textView.setText(obj.getName());
            textView.setTextSize(15);
            textView.setBackgroundResource(R.drawable.strockbutton2); // Optional: Add background drawable
            textView.setPadding(16, 8, 16, 8); // Optional: Add padding
            FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(
                    FlexboxLayout.LayoutParams.WRAP_CONTENT,
                    FlexboxLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 8, 8, 8); // Optional: Set margins
            textView.setLayoutParams(params);

            flexboxLayout.addView(textView);
        }

        boolean isOpen[] = new boolean[200];
        for (int i = 0; i < flexboxLayout.getChildCount(); i++) {
            final int position = i;
            TextView childView = (TextView) flexboxLayout.getChildAt(i);
            isOpen[i] = false;

            childView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (position == 0) {
                        if (isOpen[position]) {
                            isOpen[position] = false;
                            childView.setTextColor(Color.parseColor("#000000"));
                            childView.setBackground(getResources().getDrawable(R.drawable.strockbutton2));
                            srtList = new ArrayList<>();
                        } else {
                            isOpen[position] = true;
                            childView.setTextColor(Color.parseColor("#ffffff"));
                            childView.setBackground(getResources().getDrawable(R.drawable.all_button));
                            srtList = new ArrayList<>();
                            srtList.add(0);


                            for (int j = 0; j < flexboxLayout.getChildCount(); j++) {
                                final int position1 = j;
                                TextView childView1 = (TextView) flexboxLayout.getChildAt(j);
                                if (position1 == 0) {
                                    continue;
                                }


                                isOpen[position1] = false;
                                childView1.setTextColor(Color.parseColor("#000000"));
                                childView1.setBackground(getResources().getDrawable(R.drawable.strockbutton2));


                            }


                        }

                    } else {

                        if (isOpen[position]) {
                            childView.setTextColor(Color.parseColor("#000000"));
                            childView.setBackground(getResources().getDrawable(R.drawable.strockbutton2));
                            isOpen[position] = false;
                            srtList.remove(mList.get(position).getId());

                        } else if (!isOpen[position] && !isOpen[0]) {
                            isOpen[position] = true;
                            childView.setTextColor(Color.parseColor("#FFFFFF"));
                            childView.setBackground(getResources().getDrawable(R.drawable.all_button));
                            srtList.add(mList.get(position).getId());
                        }


                    }


                }
            });
        }



        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < flexboxLayout.getChildCount(); i++) {

                    TextView childView = (TextView) flexboxLayout.getChildAt(i);
                    childView.setTextColor(Color.parseColor("#000000"));
                    childView.setBackground(getResources().getDrawable(R.drawable.strockbutton2));
                    isOpen[i] = false;


                }



                srtList.clear();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();


        PosApiHelper.getInstance().SysSetPower(1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PosApiHelper.getInstance().SysSetPower(0);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {

        ViewGroup viewGroup = findViewById(android.R.id.cut);

        View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.alertbox3, viewGroup, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        TextView textView=dialogView.findViewById(R.id.txt);
        textView.setText("Do you want to do the\nexit the app?");
        ElasticButton bt_yes = dialogView.findViewById(R.id.bt_yes);
        ElasticButton bt_no = dialogView.findViewById(R.id.bt_no);
        bt_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dataBase.Droptables()){
                 finish();
                }





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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if(requestCode==1){
                    fragment3.setlays(true);
                }else if(requestCode==2) {

                }

            } else {
//                Toast.makeText(MainActivity.this,R.string.title_permission,Toast.LENGTH_SHORT).show();
                fragment3.setlays(false);
            }
        }
    }
}