package com.example.newsmreader;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.newsmreader.billing.BillFragOne;
import com.example.newsmreader.billing.BillFragtwo;
import com.example.newsmreader.frontScreens.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class BillingPage extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    static EditText ext_filter;
    BillFragOne billFragOne;
    BillFragtwo billFragtwo;
    ImageView imv_scanner,clr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing_page);
        ext_filter=findViewById(R.id.ext_filter);
        clr=findViewById(R.id.clr);

        billFragOne=new BillFragOne();
        billFragtwo=new BillFragtwo();
        imv_scanner=findViewById(R.id.imv_scanner);

        ext_filter.clearFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        tabLayout=findViewById(R.id.tabLayout);
        viewPager=findViewById(R.id.viewPager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(billFragOne, "Pending");
        adapter.addFragment(billFragtwo, "Completed");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#E3A735"));
        tabLayout.setTabTextColors(Color.parseColor("#CCFFFFFF"), Color.parseColor("#E3A735"));
        tabLayout.getTabAt(0).view.getTab().setText("Pending(0)");
        tabLayout.getTabAt(1).view.getTab().setText("Completed(1)");

        TextWatcher textWatcher=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                billFragOne.filter_(editable.toString());
                billFragtwo.filter_(editable.toString());
            }
        };
        ext_filter.addTextChangedListener(textWatcher);
        clr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ext_filter.setText("");
            }
        });
        imv_scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(BillingPage.this, ScanActivity.class);
//                startActivity(intent);
            }
        });

    }

    public void back_bill(View v){
        onBackPressed();
    }
}