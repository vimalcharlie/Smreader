package com.example.newsmreader.btsheet;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ak.ColoredDate;
import com.ak.KalendarView;
import com.example.newsmreader.Adapter.LocationAdapter;
import com.example.newsmreader.AllstaticObj;
import com.example.newsmreader.Models.LocationModel;
import com.example.newsmreader.Models.LocationModelArray;
import com.example.newsmreader.R;
import com.example.newsmreader.S_P;
import com.example.newsmreader.ServiceRetrofit.Retrofit;
import com.example.newsmreader.StateMent;
import com.example.newsmreader.onFilterClickListner;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.skydoves.elasticviews.ElasticButton;
import com.skydoves.elasticviews.ElasticLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomsheetNav extends BottomSheetDialogFragment {
    private onFilterClickListner onFilterClickListner;
    ElasticButton bt_done,cancel;


    public BottomsheetNav() {
        // Required empty public constructor
    }

    ConstraintLayout todate, fromdate;


    TextView fromdate_txt, todate_txt;
    View view;
    ImageView imv1, imv2;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    LocationAdapter adapter;
    ArrayList<LocationModel> mList = new ArrayList<>();
    private Retrofit retrofit=new Retrofit();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.bottomnav, container, true);
        todate = view.findViewById(R.id.todate);
        progressBar = view.findViewById(R.id.progressBar);
        recyclerView = view.findViewById(R.id.reyclr);
        fromdate = view.findViewById(R.id.fromdate);
        bt_done = view.findViewById(R.id.bt_done);
        cancel=view.findViewById(R.id.cancel);
        imv1 = view.findViewById(R.id.imv1);
        imv2 = view.findViewById(R.id.imv2);

        fromdate_txt = view.findViewById(R.id.fromdate_txt);
        todate_txt = view.findViewById(R.id.todate_txt);

        fromdate_txt.setText(S_P.from_state_date);
        todate_txt.setText(S_P.to_state_date);


        adapter = new LocationAdapter(getActivity(), mList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        getBasicDetais();
        getLocation_();
        todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                showDate(todate_txt);


            }
        });

        onFilterClickListner = (StateMent) getActivity();


        fromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDate(fromdate_txt);


            }
        });


        bt_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (valuevation()) {


                    onFilterClickListner.onFliterClick(fromdate_txt.getText().toString(), todate_txt.getText().toString(), AllstaticObj.WhichLocation);
                    dismiss();


                }


            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        return view;
    }


    public void showDate(TextView textView) {


        String date[] = new String[10];
        LayoutInflater layoutInflater = getLayoutInflater();
        View dialogeview = layoutInflater.inflate(R.layout.alertcalender, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogeview);
        AlertDialog dialog = builder.create();
        KalendarView kalendar = dialogeview.findViewById(R.id.kalendar);

        kalendar.setDateSelector(new KalendarView.DateSelector() {
            @Override
            public void onDateClicked(Date selectedDate) {


                int month = selectedDate.getMonth() + 1;
                int days = selectedDate.getDate();
                int year = selectedDate.getYear() + 1900;
                date[0] = String.valueOf(year + "-" + month + "-" + days);
                textView.setText(date[0]);
                dialog.dismiss();

            }
        });


        dialog.show();


    }


    public boolean valuevation() {


        if (fromdate_txt.getText().toString().equalsIgnoreCase("from")) {

            imv1.setImageResource(R.drawable.baseline_info_24);


            return false;

        }
        if (todate_txt.getText().toString().equalsIgnoreCase("to")) {
            imv2.setImageResource(R.drawable.baseline_info_24);
            return false;
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH);

        Date d1 = null, d2 = null;
        try {
            d1 = formatter.parse(fromdate_txt.getText().toString());
            d2 = formatter.parse(todate_txt.getText().toString());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


        if (d1.before(d2) ||d1.equals(d2)) {
            S_P.to_state_date = todate_txt.getText().toString();
            S_P.from_state_date = fromdate_txt.getText().toString();

            return true;


        } else {
            Snackbar.make(view.findViewById(R.id.innerroot), "Invalid dates", Snackbar.LENGTH_LONG).show();
            return false;

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


    private void getLocation_() {
       progressBar.setVisibility(View.VISIBLE);
        mList = new ArrayList<>();
        retrofit.getApi().GetFullLocation(branch_code, User_id).
                enqueue(new Callback<LocationModelArray>() {
                    @Override
                    public void onResponse(Call<LocationModelArray> call, Response<LocationModelArray> response) {

                        if (response.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            if (response.body().getStatus().equalsIgnoreCase("true")) {

                                ArrayList<LocationModel> list = response.body().getData();
                                for (LocationModel obj : list) {
                                    mList.add(obj);
                                }
                                adapter.setItems(mList);
                                adapter.notifyDataSetChanged();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<LocationModelArray> call, Throwable t) {

                 progressBar.setVisibility(View.GONE);


                    }
                });


    }


}
