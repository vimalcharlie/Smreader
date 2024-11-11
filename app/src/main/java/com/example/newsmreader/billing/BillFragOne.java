package com.example.newsmreader.billing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsmreader.Adapter.BillFragAdapter;
import com.example.newsmreader.Adapter.ReadFragAdapter;
import com.example.newsmreader.Dtabase.Column_;
import com.example.newsmreader.Dtabase.DataBase;
import com.example.newsmreader.Models.CustomerModel;
import com.example.newsmreader.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class BillFragOne extends Fragment {

     RecyclerView recyclerView;
     BillFragAdapter billFragAdapter;


    DataBase dataBase;
    ArrayList<CustomerModel> mList=new ArrayList<>();
    TabLayout tabLayout;





    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.billpending_frag,container,false);
        recyclerView=v.findViewById(R.id.reyclr);
        dataBase=new DataBase(getActivity());
        tabLayout=getActivity().findViewById(R.id.tabLayout);
        billFragAdapter=new BillFragAdapter(getActivity(),mList);
        recyclerView.setAdapter(billFragAdapter);






        return v;
    }


    @Override
    public void onResume() {
        super.onResume();



        String where_condition=" WHERE "+Column_.isBill+"='0'";
        mList=dataBase.CustomerList(where_condition);
        tabLayout.getTabAt(0).view.getTab().setText("Pending ("+mList.size()+")");
        if(mList.size()>0){

            billFragAdapter.setItems(mList);
            billFragAdapter.notifyDataSetChanged();
        }else {



        }
    }

    public void filter_(String s){

        ArrayList<CustomerModel> t_list=new ArrayList<>();
        for (CustomerModel obj:mList) {
            if (obj.getConsumer_no().contains(s)
                    ||obj.getConsumer().toLowerCase().contains(s.toLowerCase())
                    ||obj.getMeter_number().contains(s)){
                t_list.add(obj);


            }

        }
        try {
            billFragAdapter.setItems(t_list);
            billFragAdapter.notifyDataSetChanged();
        }catch (NullPointerException e){

        }





    }
}
