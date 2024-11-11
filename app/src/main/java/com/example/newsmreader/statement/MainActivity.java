package com.example.newsmreader.statement;

import android.Manifest;
import android.app.Activity;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.newsmreader.R;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {
    TextView showLocation;
    Button btnGetLocation;
    public final int FINE_PERMMISION_CODE = 1;
    public final int REQUEST_ENABLE_GPS=2;
    Location currentlocation;
    FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        showLocation = findViewById(R.id.showLocation);
        btnGetLocation = findViewById(R.id.btnGetLocation);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);



        btnGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              getLatestLocation();

            }
        });


    }

























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
                    showLocation.setText("Longitude :"+currentlocation.getLongitude()+"\n Latitude :"+currentlocation.getLatitude());

                }else {
                    Toast.makeText(getApplicationContext(),"Some error happen",Toast.LENGTH_LONG).show();

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
                        resolvable.startResolutionForResult(MainActivity.this, REQUEST_ENABLE_GPS);
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
                Toast.makeText(getApplicationContext(),"permission ungrand",Toast.LENGTH_LONG).show();


            }

        }else if(requestCode==REQUEST_ENABLE_GPS){
            if(requestCode== Activity.RESULT_OK){

               getLatestLocation();

            }else {
                Toast.makeText(getApplicationContext(),"gpsDenied",Toast.LENGTH_LONG).show();

            }





        }else {



        }
    }
}