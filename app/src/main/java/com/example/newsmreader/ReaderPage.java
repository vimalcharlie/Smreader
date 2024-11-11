package com.example.newsmreader;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.newsmreader.frontScreens.ViewPagerAdapter;
import com.example.newsmreader.reading.ReadFragOne;
import com.example.newsmreader.reading.ReadFragtwo;
import com.example.newsmreader.reading.ReadProfile;
import com.example.newsmreader.reading.ScannerBarcodeActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ReaderPage extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    ImageView imv_scanner;
    public static EditText et_search;
    ReadFragOne readFragOne;
    ReadFragtwo readFragtwo;
    ImageView clr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader_page);
        imv_scanner = findViewById(R.id.imv_scanner);
        tabLayout = findViewById(R.id.tabLayout);
        clr = findViewById(R.id.clr);
        viewPager = findViewById(R.id.viewPager);
        et_search = findViewById(R.id.et_search);
        readFragtwo = new ReadFragtwo();
        readFragOne = new ReadFragOne();
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(readFragOne, "Pending");
        adapter.addFragment(readFragtwo, "Completed");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#E3A735"));
        tabLayout.setTabTextColors(Color.parseColor("#CCFFFFFF"), Color.parseColor("#E3A735"));
        tabLayout.getTabAt(0).view.getTab().setText("Pending(0)");
        tabLayout.getTabAt(1).view.getTab().setText("Completed(1)");


        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                readFragOne.filter_(editable.toString());
                readFragtwo.filter_(editable.toString());

            }
        };
        et_search.addTextChangedListener(textWatcher);
        clr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_search.setText("");
            }
        });
        imv_scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReaderPage.this, ScannerBarcodeActivity.class);
                startActivityForResult(intent, 2);// Activity is started with requestCode 2
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2) {
            String result = data.getStringExtra("key");
            readFragOne.Qr_filter_(result);
            Toast.makeText(this, "Consumer No.:" + result, Toast.LENGTH_SHORT).show();

        } else {


            Toast.makeText(this, "QR not found!", Toast.LENGTH_SHORT).show();
        }
    }

    public void back_(View v) {
        onBackPressed();
    }

}