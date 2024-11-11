package com.example.newsmreader.reading;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.newsmreader.Adapter.MonthAdapter;
import com.example.newsmreader.AllstaticObj;
import com.example.newsmreader.Dtabase.Column_;
import com.example.newsmreader.Dtabase.DataBase;
import com.example.newsmreader.Models.CustomerModel;
import com.example.newsmreader.Models.ReadModel;
import com.example.newsmreader.Models.SlabModel;
import com.example.newsmreader.R;
import com.example.newsmreader.S_P;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class TakeReadingPage extends AppCompatActivity implements OnAverageChangeListner {
    EditText readingEt;

    TextView txt_pre_reading, Current_reading, usage_amt, txt_minrent, txt_preamt, txt_addamt, txt_serviceamt, txt_fine, txt_total;

    CustomerModel cModel;
    Spinner spinner_selectMode;
    View activityRootView;

    ArrayList<SlabModel> sList;
    DataBase db;

    public static Activity TakeRead;
    RecyclerView recycler;
    ReadModel readModel;
    MonthAdapter adapter;
    Long temp_min;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_reading_page);
        spinner_selectMode = findViewById(R.id.spinner_selectMode);

        String[] modes = {"Default", "Meter fault", "Door Closed"};
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, modes);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_selectMode.setAdapter(aa);
        spinner_selectMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    hold = "0";
                    closed = "0";
                } else if (i == 1) {
                    hold = "1";
                    closed = "0";
                } else if (i == 2) {
                    hold = "0";
                    closed = "1";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        TakeRead = this;
        ///////initial innitialzation//////////////////////
        db = new DataBase(this);
        AllstaticObj.avgmonth = "1";
        getBasicDetais();
        adapter = new MonthAdapter(TakeReadingPage.this, this);
        recycler = findViewById(R.id.reyclr);
        LinearLayoutManager layoutManager = new LinearLayoutManager(TakeReadingPage.this, LinearLayoutManager.HORIZONTAL, false);
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(adapter);


        txt_pre_reading = findViewById(R.id.txt_pre_reading);
        Current_reading = findViewById(R.id.Current_reading);
        usage_amt = findViewById(R.id.usage_amt);
        txt_minrent = findViewById(R.id.txt_minrent);
        txt_preamt = findViewById(R.id.txt_preamt);
        txt_addamt = findViewById(R.id.txt_addamt);
        txt_serviceamt = findViewById(R.id.txt_serviceamt);
        txt_fine = findViewById(R.id.txt_fine);
        txt_total = findViewById(R.id.txt_total);
        readingEt = (EditText) findViewById(R.id.readingEt);
        activityRootView = findViewById(R.id.root_Rl);

        Intent i = getIntent();
        cModel = (CustomerModel) i.getSerializableExtra("key1");
        setviess_();

        popupKeybord(0);


        readingEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (validate_()) {

                        popupKeybord(1);
                        calculate();

                    } else {
                        Snackbar.make(findViewById(R.id.root_Rl), "Enter the valid reading !", Snackbar.LENGTH_LONG).show();

                        popupKeybord(0);


                    }


                    return true;
                }
                return false;
            }
        });


    }

    public void popupKeybord(int is_) {
        if (is_ == 0) {
            new Handler().postDelayed(new Runnable() {

                public void run() {
                    readingEt.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0f, 0f, 0));
                    readingEt.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0f, 0f, 0));
                    readingEt.setSelection(readingEt.getText().length());

                }
            }, 300);
        } else {
            new Handler().postDelayed(new Runnable() {

                public void run() {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(readingEt.getWindowToken(), 0);

                }
            }, 300);
        }
    }

    private void setviess_() {
        txt_pre_reading.setText(cModel.getPrevReading());
        txt_minrent.setText(String.format("₹%s", cModel.getMinrent()));
        txt_preamt.setText(String.format("₹%s", cModel.getPrevBalance()));
        txt_addamt.setText(String.format("₹%s", cModel.getAdditionalCharges()));
        txt_serviceamt.setText(String.format("₹%s", cModel.getServiceCharge()));
        txt_fine.setText(String.format("₹%s", cModel.getLatefee()));
        temp_min = Long.parseLong(cModel.getMinrent());

    }

    public void nextpageTakeReading(View v) {


        if (validate_()) {

            popupKeybord(1);
            calculate();

            Intent i = new Intent(TakeReadingPage.this, ReadConformation.class);
            i.putExtra("key1", readModel);
            i.putExtra("key2", cModel);
            startActivity(i);

        } else {
            Snackbar.make(findViewById(R.id.root_Rl), "Enter the valid reading !", Snackbar.LENGTH_LONG).show();

            popupKeybord(0);


        }


    }

    double total;

    public void calculate() {
        total = 0;
        long unit = Long.parseLong(readingEt.getText().toString()) - Long.parseLong(cModel.getPrevReading());
        Current_reading.setText(String.valueOf(unit));
        double f = new Double(unit);

        int nths = Integer.parseInt(AllstaticObj.avgmonth);


        unit = (long) Math.floor((double) f / nths);
        for (int i = 0; i < sList.size(); i++) {
            String unitfrom = sList.get(i).getUnit_from();
            String unitto = sList.get(i).getUnit_to();
            Log.e("UNIT", unitfrom + "-" + unitto);
            Log.e("unitfrom", unitfrom + "-" + unitfrom);

            String diffrence;
            if (unitfrom.equals("0")) {
                diffrence = String.valueOf(Integer.parseInt(unitto) - Integer.parseInt(unitfrom));
                Log.e("diffrence0", diffrence);


            } else {
                diffrence = String.valueOf(Integer.parseInt(unitto) + 1 - Integer.parseInt(unitfrom));
                Log.e("diffrence0", diffrence);

            }
            String rate = sList.get(i).getRate();
            Log.e("rate", rate);

            if (unit != 0) {
                Log.e("diffrence AND UNIT", diffrence + "-" + unit + "-" + rate);

                if (unit >= Integer.valueOf(diffrence)) {
                    String cur_unit = diffrence;
                    Log.e("cur_unit", cur_unit);
                    double unittotal = Double.parseDouble(cur_unit) * Double.parseDouble(rate);
                    total = total + unittotal;
                    unit = unit - Integer.parseInt(cur_unit);
                } else {
                    double unitTotal = unit * Double.parseDouble(rate);
                    total = total + unitTotal;
                    unit = 0;
                }
            }

        }

        total = total * nths;
        usage_amt.setText(S_P.R + total);
        long finaltotal = Math.round(getTotal(total));

        txt_total.setText(String.valueOf(finaltotal));

        saveValue_To(String.valueOf(total),String.valueOf(finaltotal));


    }

    private double getTotal(double tl) {

        double rt_total = Double.parseDouble(cModel.getPrevBalance()) +
                Double.parseDouble(cModel.getServiceCharge()) +
                Double.parseDouble(cModel.getAdditionalCharges()) +
                Double.parseDouble(cModel.getLatefee()) +
                Double.parseDouble(cModel.getMinrent()) + tl;


        return rt_total;
    }

    String hold="0", closed="0";

    private void saveValue_To(String temp,String temp2) {

        readModel = new ReadModel(cModel.getMobile(), cModel.getMeter_number(), cModel.getLatitude(), cModel.getLongitude(),
                ImportDate, "" + cModel.getCustomerID(), readingEt.getText().toString(), cModel.getPrevBalance(),
                cModel.getMinrent(), cModel.getLatefee(), cModel.getAdditionalCharges(), ImportDate
                , "" + new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()), User_id,
                ""+temp, cModel.getEnableServiceCharge(), cModel.getServiceCharge(), cModel.getPrevReading(),
                Billid, temp2, Current_reading.getText().toString(), AllstaticObj.avgmonth, "" + hold, "" + closed);

    }

    private boolean validate_() {

        if (TextUtils.isEmpty(readingEt.getText().toString())) {
            return false;
        } else if (Long.parseLong(txt_pre_reading.getText().toString()) > Long.parseLong(readingEt.getText().toString())) {
            return false;
        }


        return true;
    }

    public void set_minrent() {
        long value = temp_min * Long.parseLong(AllstaticObj.avgmonth);
        cModel.setMinrent("" + value);
        txt_minrent.setText(S_P.R + value);


    }


    String branch_code, User_id, Billid, ImportDate;


    private void getBasicDetais() {
        ImportDate = S_P.getPREFERENCES(TakeReadingPage.this, S_P.Importeddate);
        String retrieve = S_P.getJsonData(getApplicationContext());
        try {
            JSONObject jsonObject = new JSONObject(retrieve);
            branch_code = jsonObject.getString("branch_code");
            User_id = jsonObject.getString("id");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        int readCount = S_P.getPREFERENCESint(TakeReadingPage.this, S_P.READ_NO);
        Billid = String.valueOf("R" + User_id + "12" + "2023" + readCount);


    }

    @Override
    protected void onResume() {
        super.onResume();
        sList = db.Slablist(Column_.CID, cModel.getCatid());
    }

    @Override
    public void OnChange() {
        set_minrent();
        if (validate_()) {
            calculate();


        }

    }
}