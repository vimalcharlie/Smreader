package com.example.newsmreader.reading;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsmreader.Adapter.ReadFragAdapter;
import com.example.newsmreader.Dtabase.Column_;
import com.example.newsmreader.Dtabase.DataBase;
import com.example.newsmreader.Models.CustomerModel;
import com.example.newsmreader.R;
import com.example.newsmreader.ReaderPage;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;


public class ReadFragOne extends Fragment {


    RecyclerView recyclerView;
    ReadFragAdapter adapter;
    DataBase dataBase;
    ArrayList<CustomerModel> mList=new ArrayList<>();
    TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.readpending_frag,container,false);
        recyclerView=view.findViewById(R.id.reyclr);
        dataBase=new DataBase(getActivity());
        tabLayout=getActivity().findViewById(R.id.tabLayout);
        adapter=new ReadFragAdapter(getActivity(),mList);
        recyclerView.setAdapter(adapter);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        String where_condition=" WHERE "+Column_.isRead+"='0'  AND readingexists='0'";
        mList=dataBase.CustomerList(where_condition);
        tabLayout.getTabAt(0).view.getTab().setText("Pending ("+mList.size()+")");
        if(mList.size()>0){

            adapter.setItems(mList);
            adapter.notifyDataSetChanged();
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
        adapter.setItems(t_list);
        adapter.notifyDataSetChanged();
        }catch (NullPointerException e){

        }


    }


    public void Qr_filter_(String s){


        for (CustomerModel obj:mList) {
            if (obj.getConsumer_no().contains(s)){
                Intent i = new Intent(getActivity(), ReadProfile.class);
                i.putExtra("key1", obj.getConsumer_no());
                startActivity(i);
            }

        }



    }
}
