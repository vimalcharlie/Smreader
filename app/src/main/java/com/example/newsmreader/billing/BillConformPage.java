package com.example.newsmreader.billing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.newsmreader.Dtabase.DataBase;
import com.example.newsmreader.Models.BillModel;
import com.example.newsmreader.Models.CustomerModel;
import com.example.newsmreader.R;
import com.google.android.material.snackbar.Snackbar;

public class BillConformPage extends AppCompatActivity {


    TextView txt_received, txt_name,txt_fine ,txt_area, txt_consumer, txt_meter_no, txt_category, txt_billid, txt_receipt_date, txt_pre_Bdate, txt_addAmt, serviceAmt, txt_prebal, txt_payable, txt_balance;

    CustomerModel cModel;
    BillModel billModel;
    DataBase db;
    public  static Activity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=this;
        setContentView(R.layout.activity_bill_conformation);
        txt_received = findViewById(R.id.txt_received);
        txt_fine=findViewById(R.id.txt_fine);
        txt_name = findViewById(R.id.txt_name);
        txt_area = findViewById(R.id.txt_area);
        txt_consumer = findViewById(R.id.txt_consumer);
        txt_meter_no = findViewById(R.id.txt_meter_no);
        txt_category = findViewById(R.id.txt_category);
        txt_billid = findViewById(R.id.txt_billid);
        txt_receipt_date = findViewById(R.id.txt_receipt_date);
        txt_pre_Bdate = findViewById(R.id.txt_pre_Bdate);
        txt_addAmt = findViewById(R.id.txt_addAmt);
        serviceAmt = findViewById(R.id.serviceAmt);
        txt_prebal = findViewById(R.id.txt_prebal);
        txt_payable = findViewById(R.id.txt_payable);
        txt_balance = findViewById(R.id.txt_balance);
        db=new DataBase(BillConformPage.this);

        Intent intent = getIntent();
        cModel = (CustomerModel) intent.getSerializableExtra("key2");
        billModel = (BillModel) intent.getSerializableExtra("key1");


        setView_();


    }

    private void setView_() {
        txt_received.setText(billModel.getRecieved());
        txt_name.setText(cModel.getConsumer());
        txt_area.setText(cModel.getLocation());
        txt_consumer.setText(cModel.getConsumer_no());
        txt_meter_no.setText(cModel.getMeter_number());
        txt_category.setText(cModel.getCategory());
        txt_billid.setText(billModel.getRecNo());
        txt_receipt_date.setText(billModel.getPayment_date());
        txt_pre_Bdate.setText(cModel.getPrevBillDate());
        txt_addAmt.setText(cModel.getAdditionalCharges());
        serviceAmt.setText(cModel.getServiceCharge());
        txt_prebal.setText(cModel.getPrevBalance());
        txt_payable.setText(billModel.getAmount());
        txt_balance.setText(billModel.getBalance());
        txt_fine.setText(cModel.getLatefee());
    }
    public void savetoDbase(View v){

      int id=v.getId();
      if(id==R.id.Bt_save){


          Intent intent=new Intent(BillConformPage.this, CashMode.class);
          intent.putExtra("key1","1");
          intent.putExtra("key2",billModel);
          intent.putExtra("key3",cModel);
          startActivity(intent);


      }else if(id==R.id.Bt_print){

          Intent intent=new Intent(BillConformPage.this, CashMode.class);
          intent.putExtra("key1","2");
          intent.putExtra("key2",billModel);
          intent.putExtra("key3",cModel);
          startActivity(intent);



      }





    }
}
