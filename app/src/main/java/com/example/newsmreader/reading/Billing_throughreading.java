package com.example.newsmreader.reading;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.newsmreader.Dtabase.Column_;
import com.example.newsmreader.Dtabase.DataBase;
import com.example.newsmreader.Models.BillModel;
import com.example.newsmreader.Models.CustomerModel;
import com.example.newsmreader.Models.ReadModel;
import com.example.newsmreader.PrintClass;
import com.example.newsmreader.R;
import com.example.newsmreader.S_P;
import com.example.newsmreader.billing.BillConformPage;
import com.example.newsmreader.billing.TakeBillingPage;
import com.google.android.material.snackbar.Snackbar;
import com.skydoves.elasticviews.ElasticButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Billing_throughreading extends AppCompatActivity {
    TextView txt_fine,amt_service_amt,txt_add_amt,txt_pre_amt,txtBillAmt,balance;
    EditText Et_received_amt;
    LinearLayout lnr_next;
    BillModel billModel;
    CustomerModel cModel;
    DataBase dataBase;
    String consumer_number;

    public  static Activity readthrough;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readthrough=this;
        dataBase=new DataBase(this);

        setContentView(R.layout.activity_take_billing_page);
        lnr_next=findViewById(R.id.lnr_next);
        txt_fine=findViewById(R.id.txt_fine);
        amt_service_amt=findViewById(R.id.amt_service_amt);
        txt_add_amt=findViewById(R.id.txt_add_amt);
        txt_pre_amt=findViewById(R.id.txt_pre_amt);
        txtBillAmt=findViewById(R.id.txtBillAmt);
        balance=findViewById(R.id.balance);
        Et_received_amt=findViewById(R.id.Et_received_amt);

        Intent i=getIntent();
        consumer_number=i.getStringExtra("key1");





        popupKeybord(0);





        Et_received_amt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (validate_()) {
                        popupKeybord(1);
                        balance.setText(S_P.R+calculate());
                        saveValue_To();

                    } else {
                        Snackbar.make(findViewById(R.id.root_Rl), "Enter the valid reading !", Snackbar.LENGTH_LONG).show();
                        popupKeybord(0);


                    }


                    return true;
                }
                return false;
            }
        });
        lnr_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate_()) {
                    popupKeybord(1);
                    balance.setText(S_P.R+calculate());
                    saveValue_To();


                    Intent i=new Intent(Billing_throughreading.this, ReadBIllConfrom.class);
                    i.putExtra("key1",  billModel);
                    i.putExtra("key2", cModel);
                    startActivity(i);

                } else {
                    Snackbar.make(findViewById(R.id.root_Rl), "Enter the valid amount !", Snackbar.LENGTH_LONG).show();
                    popupKeybord(0);


                }

            }
        });
    }



    String branch_code, User_id,Billid,ImportDate,Billval;
    ReadModel readModel;
    private void getBasicDetais() {
        String where_conditon2 = " WHERE customer_guid ='" + cModel.getCustomerID() + "'";
        readModel = dataBase.Singleread(where_conditon2);
        Billval = S_P.getPREFERENCES(Billing_throughreading.this, S_P.Billingpattern);
        if (readModel == null) {
            testval =0;
        } else {
            testval =0;
            if(Billval.equalsIgnoreCase("1")){
                testval=Long.parseLong(cModel.getMinrent());
            }else if(Billval.equalsIgnoreCase("2")){
                double billamt=Double.parseDouble(readModel.getBillAmount());
                double minrent=Double.parseDouble(readModel.getMinRent());

                testval=(new Double(minrent)).longValue()+(new Double(billamt)).longValue();
                //testval=Long.parseLong(readModel.getMinRent())+Long.parseLong(readModel.getBillAmount());
            }else if(Billval.equalsIgnoreCase("3")){
                double billamt=Double.parseDouble(readModel.getBillAmount());
                double minrent=Double.parseDouble(readModel.getMinRent());
                double spotcharge=Double.parseDouble(readModel.getSpotBillingCharge());
                testval=(new Double(minrent)).longValue()+(new Double(billamt)).longValue()+(new Double(spotcharge)).longValue();

               // testval=Long.parseLong(readModel.getMinRent())+Long.parseLong(readModel.getBillAmount())+Long.parseLong(readModel.getSpotBillingCharge());
            }
            if(Long.parseLong(readModel.getPrevious_Bal())<0 && testval>0){

                testval=testval+Long.parseLong(readModel.getPrevious_Bal());
            }
            Et_received_amt.setHint("Enter amount atleast "+S_P.R+testval);
        }
        ImportDate=S_P.getPREFERENCES(Billing_throughreading.this, S_P.Importeddate);
        String retrieve = S_P.getJsonData(getApplicationContext());
        try {
            JSONObject jsonObject = new JSONObject(retrieve);
            branch_code = jsonObject.getString("branch_code");
            User_id = jsonObject.getString("id");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        int BillCont = S_P.getPREFERENCESint(Billing_throughreading.this, S_P.BILL_NO);
        Billid= String.valueOf("B" +User_id+"12"+"2023"+BillCont);


    }
    private int calculate() {
        int balance=0;
        if (sum() >= Integer.parseInt(Et_received_amt.getText().toString())) {
            balance = sum() - Integer.parseInt(Et_received_amt.getText().toString());
        } else if (sum() < Integer.parseInt(Et_received_amt.getText().toString())) {
            if (sum() <= 0) {
                balance = -Integer.parseInt(Et_received_amt.getText().toString()) + sum();
            } else {
                balance=-(Integer.parseInt(Et_received_amt.getText().toString())-sum());
            }
        }
        return balance;
    }
    public void saveValue_To(){
        billModel=new BillModel(cModel.getCustomerID()
                ,""+sum(),Et_received_amt.getText().toString(),
                ""+calculate(),""+cModel.getLatefee(),""+ImportDate,""+User_id,""+cModel.getAdditionalCharges()
                ,Et_received_amt.getText().toString(),""+new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()),""+cModel.getEnableServiceCharge(),""+cModel.getServiceCharge(),
                ""+Billid,"0","0");
    }
    private void setView_s() {
        txt_fine.setText(S_P.R+cModel.getLatefee());
        amt_service_amt.setText(S_P.R+cModel.getServiceCharge());
        txt_add_amt.setText(S_P.R+cModel.getAdditionalCharges());
        txt_pre_amt.setText(S_P.R+cModel.getPrevBalance());
        txtBillAmt.setText(S_P.R+sum());
    }
    public  int sum (){
        return (int) (Double.parseDouble(cModel.getPrevBalance()) + Double.parseDouble(cModel.getLatefee()) + Double.parseDouble(cModel.getAdditionalCharges()) + Double.parseDouble(cModel.getServiceCharge()));
    }
    public void popupKeybord(int is_) {
        if (is_ == 0) {
            new Handler().postDelayed(new Runnable() {

                public void run() {
                    Et_received_amt.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0f, 0f, 0));
                    Et_received_amt.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0f, 0f, 0));
                    Et_received_amt.setSelection(Et_received_amt.getText().length());

                }
            }, 300);
        } else {
            new Handler().postDelayed(new Runnable() {

                public void run() {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(Et_received_amt.getWindowToken(), 0);

                }
            }, 300);
        }
    }
    long testval=0;
    private boolean validate_() {
        if (TextUtils.isEmpty(Et_received_amt.getText().toString())) {
            return false;
        }else if(testval>Long.parseLong(Et_received_amt.getText().toString())){
            return false;
        }


        return true;
    }
    @Override
    protected void onResume() {
        super.onResume();

        String where_condition=" WHERE "+ Column_.Consumer_No+"='"+consumer_number+"'";
        cModel=dataBase.Single_Customer(where_condition);
        if(cModel.getEnableServiceCharge().equalsIgnoreCase("Bill-true")){
            cModel.setEnableServiceCharge("1");

        }else {
            cModel.setServiceCharge("0");
            cModel.setEnableServiceCharge("0");

        }

        setView_s();
        getBasicDetais();

    }


}