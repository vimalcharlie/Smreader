package com.example.newsmreader.Jobservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.newsmreader.R;

public class CheckJobservice extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        SharedPreferences sharedPreferences = getSharedPreferences("My pref", Context.MODE_PRIVATE);

        // Edit the values in SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("key1", "old Value");  // Change the value as needed
        editor.apply();

        long desiredTimeInMillis =10000;
        JobSchedulerHelper.scheduleJob(this, 1, desiredTimeInMillis);
    }
}