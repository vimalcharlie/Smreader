package com.example.newsmreader;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.newsmreader.Adapter.RegisterListAdapter;
import com.example.newsmreader.ComplaintList.ComplaintList;
import com.example.newsmreader.ComplaintList.CustomerList;
import com.example.newsmreader.Dtabase.DataBase;
import com.example.newsmreader.Models.CustomerModel;
import com.example.newsmreader.frontScreens.ViewPagerAdapter;
import com.example.newsmreader.reading.ReadFragOne;
import com.example.newsmreader.reading.ReadFragtwo;
import com.google.android.material.tabs.TabLayout;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.ArrayList;

public class RegisterListPage extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;

    public  static  EditText et_search;
    CustomerList fragone;
    ComplaintList fragtwo;
    ImageView clr;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerlist_page);

        tabLayout=findViewById(R.id.tabLayout);
        clr=findViewById(R.id.clr);
        viewPager=findViewById(R.id.viewPager);
        et_search=findViewById(R.id.et_search);
        fragone=new CustomerList();
        fragtwo=new ComplaintList();
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(fragone, "Pending");
        adapter.addFragment(fragtwo, "Completed");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#E3A735"));
        tabLayout.setTabTextColors(Color.parseColor("#CCFFFFFF"), Color.parseColor("#E3A735"));
        tabLayout.getTabAt(0).view.getTab().setText("Consumer");
        tabLayout.getTabAt(1).view.getTab().setText("Complaint");

        TextWatcher textWatcher=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                fragone.filter_(editable.toString());
                fragtwo.filter_(editable.toString());

            }
        };
        et_search.addTextChangedListener(textWatcher);
        clr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_search.setText("");
            }
        });







    }
    public void back_reg(View v){
        onBackPressed();
    }

}
