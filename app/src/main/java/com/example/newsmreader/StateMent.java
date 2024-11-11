package com.example.newsmreader;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.newsmreader.btsheet.BottomsheetNav;
import com.example.newsmreader.frontScreens.ViewPagerAdapter;
import com.example.newsmreader.reading.ReadFragOne;
import com.example.newsmreader.reading.ReadFragtwo;
import com.example.newsmreader.statement.StateFragOne;
import com.example.newsmreader.statement.StateFragtwo;
import com.google.android.material.tabs.TabLayout;

public class StateMent extends AppCompatActivity implements onFilterClickListner {
    ViewPager viewPager;
    static TabLayout tabLayout;
    ImageView img_bt;
    EditText et_filter;
    ImageView img_close;


    StateFragOne stateFragOne;
    StateFragtwo stateFragtwo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statement);
        img_close = findViewById(R.id.img_close);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        img_bt = findViewById(R.id.img_bt);
        et_filter = findViewById(R.id.et_filter);
        stateFragOne = new StateFragOne();
        stateFragtwo = new StateFragtwo();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(stateFragOne, "Reading");
        adapter.addFragment(stateFragtwo, "Billing");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#E3A735"));
        tabLayout.setTabTextColors(Color.parseColor("#CCFFFFFF"), Color.parseColor("#E3A735"));
//        tabLayout.getTabAt(0).view.getTab().setText("Reading (10)");
//        tabLayout.getTabAt(1).view.getTab().setText("Billing (12)");

        img_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                BottomsheetNav bottomSheetFragment = new BottomsheetNav();
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());


            }
        });
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                et_filter.setText("");


            }
        });


        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                stateFragOne.setView(editable.toString().toLowerCase());
                stateFragtwo.setView(editable.toString().toLowerCase());


            }
        };
        et_filter.addTextChangedListener(textWatcher);


    }

    @Override
    public void onFliterClick(String fromDate, String Todate, String location) {


        stateFragOne._getthedata("", fromDate, Todate, location);

        stateFragtwo._getinitial_data("", fromDate, Todate, location);


    }

    public void back_state(View v) {
        onBackPressed();
    }
}
