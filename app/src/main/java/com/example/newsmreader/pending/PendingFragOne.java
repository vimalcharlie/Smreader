package com.example.newsmreader.pending;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.newsmreader.Adapter.PendingAdapter;
import com.example.newsmreader.Adapter.StatementAdapter;
import com.example.newsmreader.AllstaticObj;
import com.example.newsmreader.Models.ReadPrintModel;
import com.example.newsmreader.Models.ReadPrintModelArray;
import com.example.newsmreader.Models.StamentreadModel;
import com.example.newsmreader.Models.StamentreadModelArray;
import com.example.newsmreader.PrintClass;
import com.example.newsmreader.R;
import com.example.newsmreader.S_P;
import com.example.newsmreader.ServiceRetrofit.Retrofit;
import com.google.android.material.tabs.TabLayout;
import com.skydoves.elasticviews.ElasticButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingFragOne extends Fragment {

    RecyclerView recyclerView;
    PendingAdapter adapter;

    ArrayList<StamentreadModel> main_list = new ArrayList<>();
    private Retrofit retrofit = Retrofit.getInstance();

    TabLayout tableLayout;
    PrintClass printClass;
    SwipeRefreshLayout pullToRefresh;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.statement_frag, container, false);
        recyclerView = v.findViewById(R.id.reyclr);
        printClass=new PrintClass();
        pullToRefresh=v.findViewById(R.id.pullToRefresh);
        adapter = new PendingAdapter(getActivity(), main_list);
        recyclerView.setAdapter(adapter);
        tableLayout = getActivity().findViewById(R.id.tabLayout);

        getBasicDetais();

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                _getthedata("", S_P.from_state_date, S_P.to_state_date, AllstaticObj.WhichLocation);
                pullToRefresh.setRefreshing(false);
            }
        });


        _getthedata("", S_P.from_state_date, S_P.to_state_date, AllstaticObj.WhichLocation);

        return v;
    }


    public void _getthedata(String filter, String fromdate, String todate, String location) {

        pullToRefresh.setRefreshing(true);
        main_list = new ArrayList<>();

        retrofit.getApi().READ_STATE(branch_code, "PR", filter, fromdate, todate, location, User_id)
                .enqueue(new Callback<StamentreadModelArray>() {
                    @Override
                    public void onResponse(Call<StamentreadModelArray> call, Response<StamentreadModelArray> response) {
                        pullToRefresh.setRefreshing(false);
                        if (response.isSuccessful()) {
                            System.out.println(response.body().toString());

                            if (response.body().getStatus().equalsIgnoreCase("true")) {
                                ArrayList<StamentreadModel> list = response.body().getData();

                                for (StamentreadModel obj : list) {

                                    main_list.add(new StamentreadModel(obj.getId(), obj.getConsumer(), obj.getConsumer_no(), obj.getReading(), obj.getTotal()));

                                    adapter.setItems(main_list);
                                    adapter.notifyDataSetChanged();


                                }


                            } else {


                                adapter.setItems(main_list);
                                adapter.notifyDataSetChanged();


                            }


                        } else {


                            adapter.setItems(main_list);
                            adapter.notifyDataSetChanged();


                        }

                        tableLayout.getTabAt(0).view.getTab().setText("Reading (" + main_list.size() + ")");



                    }

                    @Override
                    public void onFailure(Call<StamentreadModelArray> call, Throwable t) {
                        pullToRefresh.setRefreshing(false);
                    }
                });


    }


    public void setView(String filter) {
        ArrayList<StamentreadModel> temp_list = new ArrayList<>();
        if (main_list.size() != 0) {
            for (StamentreadModel obj : main_list) {
                if (obj.getConsumer_no().toLowerCase().contains(filter) || obj.getConsumer().toLowerCase().contains(filter))
                    temp_list.add(obj);
            }

            adapter.setItems(temp_list);
            adapter.notifyDataSetChanged();
        }


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




}
