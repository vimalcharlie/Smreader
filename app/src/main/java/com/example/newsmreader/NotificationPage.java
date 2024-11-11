package com.example.newsmreader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class NotificationPage extends AppCompatActivity {
    TextView txt_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);
        txt_name=findViewById(R.id.txt_name);
        getBasicDetais();
        txt_name.setText(name);
    }
    public void next_(View view){

        startActivity(new Intent(NotificationPage.this,MainActivity.class));
        finish();
    }


    String name;

    private void getBasicDetais() {


        String retrieve = S_P.getJsonData(getApplicationContext());
        try {
            JSONObject jsonObject = new JSONObject(retrieve);
            name = jsonObject.getString("name");



        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


    }
}