package com.example.newsmreader.ComplaintList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.newsmreader.Adapter.RegisterListAdapter;
import com.example.newsmreader.Dtabase.DataBase;
import com.example.newsmreader.Models.CustomerModel;
import com.example.newsmreader.R;
import com.example.newsmreader.RegisterListPage;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class CustomerList extends Fragment {
    RecyclerView reyclr;
    RegisterListAdapter adapter;
    ArrayList<CustomerModel> mList;
    DataBase dataBase;
    TabLayout tabLayout;
    SwipeRefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.statement_frag,container,false);
        reyclr=view.findViewById(R.id.reyclr);
        refreshLayout=view.findViewById(R.id.pullToRefresh);
        dataBase=new DataBase(getActivity());

        tabLayout=getActivity().findViewById(R.id.tabLayout);


        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(false);



            }
        });

        return view;

    }

    public void filter_(String s){
        try {
            if(mList!=null) {
                ArrayList<CustomerModel> t_list = new ArrayList<>();
                for (CustomerModel obj : mList) {
                    if (obj.getConsumer_no().contains(s)
                            || obj.getConsumer().toLowerCase().contains(s.toLowerCase())
                            || obj.getMeter_number().contains(s)) {
                        t_list.add(obj);


                    }

                }
                adapter.setItems(t_list);
                adapter.notifyDataSetChanged();
            }
        }catch (NullPointerException e){

        }




    }

    @Override
    public void onResume() {
        super.onResume();
        mList=dataBase.CustomerList("");
        adapter=new RegisterListAdapter(getActivity(),mList);
        reyclr.setAdapter(adapter);
    }
}
