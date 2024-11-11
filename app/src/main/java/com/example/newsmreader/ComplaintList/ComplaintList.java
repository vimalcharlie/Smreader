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

import com.example.newsmreader.Adapter.ComplaintAdapter;
import com.example.newsmreader.Models.ComplaintModel;
import com.example.newsmreader.Models.ComplaintModelArray;
import com.example.newsmreader.Models.Complaint_List;
import com.example.newsmreader.Models.Complaint_ListArray;
import com.example.newsmreader.Models.CustomerModel;
import com.example.newsmreader.R;
import com.example.newsmreader.S_P;
import com.example.newsmreader.ServiceRetrofit.Retrofit;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComplaintList extends Fragment {
    ComplaintAdapter complaintAdapter;
    ArrayList<ComplaintModel> mlist = new ArrayList<>();
    RecyclerView recyclerView;
    private Retrofit retrofit = Retrofit.getInstance();
    SwipeRefreshLayout refreshLayout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.statement_frag, container, false);
        recyclerView = view.findViewById(R.id.reyclr);
        refreshLayout = view.findViewById(R.id.pullToRefresh);
        complaintAdapter = new ComplaintAdapter(getActivity(), mlist);
        recyclerView.setAdapter(complaintAdapter);
        getBasicDetais();
        getComplaintList();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getComplaintList();


            }
        });


        return view;

    }


    public void getComplaintList() {
        refreshLayout.setRefreshing(true);
        mlist = new ArrayList<>();
        retrofit.getApi()
                .getComplaintList(branch_code, User_id)
                .enqueue(new Callback<ComplaintModelArray>() {
                    @Override
                    public void onResponse(Call<ComplaintModelArray> call, Response<ComplaintModelArray> response) {
                        refreshLayout.setRefreshing(false);
                        if (response.isSuccessful() && response.body().getStatus().equalsIgnoreCase("True")) {
                            ArrayList<ComplaintModel> slist = response.body().getData();
                            for (ComplaintModel obj : slist) {
                                mlist.add(obj);

                            }
                            complaintAdapter.setList(mlist);


                        }
                    }

                    @Override
                    public void onFailure(Call<ComplaintModelArray> call, Throwable t) {
                        refreshLayout.setRefreshing(false);
                    }
                });


    }


    String branch_code, User_id;

    private void getBasicDetais() {


        String retrieve = S_P.getJsonData(getActivity());
        try {
            JSONObject jsonObject = new JSONObject(retrieve);
            branch_code = jsonObject.getString("branch_code");
            User_id = jsonObject.getString("id");


        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


    }


    public void filter_(String s) throws NullPointerException{

        if(mlist!=null) {
            ArrayList<ComplaintModel> t_list = new ArrayList<>();
            for (ComplaintModel obj : mlist) {
                if (obj.getConsumer_no().contains(s)) {
                    t_list.add(obj);


                }

            }
            try {
                complaintAdapter.setList(t_list);
                complaintAdapter.notifyDataSetChanged();
            }catch (NullPointerException e){

            }

        }

    }






}
