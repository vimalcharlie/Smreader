package com.example.newsmreader.billing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newsmreader.Dtabase.Column_;
import com.example.newsmreader.Dtabase.DataBase;
import com.example.newsmreader.Models.CustomerModel;
import com.example.newsmreader.R;
import com.example.newsmreader.reading.ReadProfile;
import com.example.newsmreader.reading.TakeReadingPage;
import com.example.newsmreader.statement.MainActivity;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class BillingProfile extends AppCompatActivity {
    CustomerModel cModel=null;
    TextView txt_name,txtaddress2,txtarea2, txtpreread, txtlastreaddate2,txtlastbilldate2;
    EditText Et_phone2,txtmeterreading;
    DataBase dataBase;
    String consumer_number;
    public static Activity Bill_profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bill_profile=this;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        setContentView(R.layout.activity_billing_profile);
        dataBase=new DataBase(this);
        txt_name=findViewById(R.id.txt_name);
        txtaddress2=findViewById(R.id.txtaddress2);
        txtarea2=findViewById(R.id.txtarea2);
        txtmeterreading=findViewById(R.id.txtmeterreading);
        txtpreread=findViewById(R.id.txtpreread);
        txtlastreaddate2=findViewById(R.id.txtlastreaddate2);
        txtlastbilldate2=findViewById(R.id.txtlastbilldate2);
        Et_phone2=findViewById(R.id.Et_phone2);

        Et_phone2.clearFocus();
        txtmeterreading.clearFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

         Intent i=getIntent();
        consumer_number=i.getStringExtra("key1");



        getLatestLocation();



    }

    private void setviews() {
        txt_name.setText(cModel.getConsumer());
        Et_phone2.setText(cModel.getMobile());
        txtaddress2.setText(cModel.getAddress());
        txtarea2.setText(cModel.getLocation());
        txtmeterreading.setText(cModel.getMeter_number());
        txtpreread.setText(cModel.getPrevBalance());
        txtlastreaddate2.setText(cModel.getPrevReadingDate());
        txtlastbilldate2.setText(cModel.getPrevBillDate());



    }

    public  void nextpage(View v){

        if (cModel==null){
            finish();
        }

        if(validate_()){
            cModel.setMobile(Et_phone2.getText().toString());
            cModel.setMeter_number(txtmeterreading.getText().toString());
            if (currentlocation!=null){

                cModel.setLongitude(""+currentlocation.getLongitude());
                cModel.setLatitude(""+currentlocation.getLatitude());



            }

            if(cModel.getEnableServiceCharge().equalsIgnoreCase("Bill-true")){
                cModel.setEnableServiceCharge("1");

            }else {
                cModel.setServiceCharge("0");
                cModel.setEnableServiceCharge("0");

            }

            Intent i = new Intent(BillingProfile.this, TakeBillingPage.class);
            i.putExtra("key1", cModel);
            startActivity(i);
        }





    }

    private boolean validate_() {

        if(TextUtils.isEmpty(Et_phone2.getText().toString())||Et_phone2.getText().toString().equalsIgnoreCase("0000000000") ) {

            return false;


        }else if( Et_phone2.getText().length()>12 &&Et_phone2.getText().toString().length()<10){

            return false;
        }




        return true;



    }


    @Override
    protected void onResume() {
        super.onResume();

        String where_condition=" WHERE "+ Column_.Consumer_No+"='"+consumer_number+"'";
        cModel=dataBase.Single_Customer(where_condition);
        setviews();


    }



    public final int FINE_PERMMISION_CODE = 1;
    public final int REQUEST_ENABLE_GPS=2;
    Location currentlocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    public void getLatestLocation() {


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},FINE_PERMMISION_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location!=null){
                    currentlocation=location;

                }else {
                 //   Toast.makeText(getApplicationContext(),"Some error happen",Toast.LENGTH_LONG).show();

                }

            }
        });


        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"addOnFailureListener",Toast.LENGTH_LONG).show();

                if (e instanceof ResolvableApiException) {
                    try {
                        // Prompt the user to enable GPS
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(BillingProfile.this, REQUEST_ENABLE_GPS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error
                    }
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==FINE_PERMMISION_CODE){
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getLatestLocation();

            }else {
                //Toast.makeText(getApplicationContext(),"permission ungrand",Toast.LENGTH_LONG).show();


            }

        }else if(requestCode==REQUEST_ENABLE_GPS){
            if(requestCode== Activity.RESULT_OK){

                getLatestLocation();

            }else {
               // Toast.makeText(getApplicationContext(),"gpsDenied",Toast.LENGTH_LONG).show();

            }





        }else {

          //  Toast.makeText(getApplicationContext(),"nothing",Toast.LENGTH_LONG).show();


        }
    }













}