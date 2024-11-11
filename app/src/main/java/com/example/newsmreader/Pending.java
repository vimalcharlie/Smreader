package com.example.newsmreader;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.newsmreader.btsheet.Bottomsheet2;
import com.example.newsmreader.btsheet.BottomsheetNav;
import com.example.newsmreader.frontScreens.ViewPagerAdapter;
import com.example.newsmreader.pending.PendingFragOne;
import com.example.newsmreader.pending.PendingFragTwo;
import com.example.newsmreader.statement.StateFragOne;
import com.example.newsmreader.statement.StateFragtwo;
import com.google.android.material.tabs.TabLayout;

public class Pending extends AppCompatActivity  implements  onFilterClickListner{
    ViewPager viewPager;
    static TabLayout tabLayout;
    ImageView img_bt;
    EditText et_filter;



    PendingFragOne pendingFragOne;
    PendingFragTwo pendingFragTwo;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        viewPager=findViewById(R.id.viewPager);
        tabLayout=findViewById(R.id.tabLayout);
        img_bt=findViewById(R.id.img_bt);
        et_filter=findViewById(R.id.et_filter);
        pendingFragOne=new PendingFragOne();
        pendingFragTwo=new PendingFragTwo();

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(pendingFragOne, "Reading");
        adapter.addFragment(pendingFragTwo, "Billing");


        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#E3A735"));
        tabLayout.setTabTextColors(Color.parseColor("#CCFFFFFF"), Color.parseColor("#E3A735"));
//        tabLayout.getTabAt(0).view.getTab().setText("Reading (10)");
//        tabLayout.getTabAt(1).view.getTab().setText("Billing (12)");

        img_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Bottomsheet2 bottomSheetFragment = new Bottomsheet2();
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());



            }
        });


        TextWatcher textWatcher=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
              //  stateFragOne.setView(editable.toString().toLowerCase());



            }
        };
        et_filter.addTextChangedListener(textWatcher);



    }

    @Override
    public void onFliterClick(String fromDate, String Todate, String location) {
        pendingFragOne._getthedata("",fromDate,Todate,location);
        pendingFragTwo._getthedata("",fromDate,Todate,location);
    }


    public void back_pend(View v){
        onBackPressed();
    }
}