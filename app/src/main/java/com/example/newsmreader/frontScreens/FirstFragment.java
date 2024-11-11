package com.example.newsmreader.frontScreens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.newsmreader.BillingPage;
import com.example.newsmreader.Dtabase.DataBase;
import com.example.newsmreader.Dtabase.Tables_;
import com.example.newsmreader.MainActivity;
import com.example.newsmreader.Payment_conformaion_page;
import com.example.newsmreader.Pending;
import com.example.newsmreader.R;
import com.example.newsmreader.ReaderPage;
import com.example.newsmreader.RegisterListPage;
import com.example.newsmreader.RegisterPage;
import com.example.newsmreader.S_P;
import com.example.newsmreader.ServiceRetrofit.Retrofit;
import com.example.newsmreader.StateMent;
import com.example.newsmreader.frontScreens.progressBar.OnProgressChangeListner;
import com.example.newsmreader.frontScreens.progressBar.RoundProgressBar;

import com.example.newsmreader.onFeatchdataListner;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.skydoves.elasticviews.ElasticLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;
import nl.joery.animatedbottombar.AnimatedBottomBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirstFragment extends Fragment {

    private RoundProgressBar roundProgressBar, roundProgressBar1, roundProgressBar2;
    ElasticLayout readingPage, billpage, add_complaint, statement,pending;
    TextView min_txt_three,min_txt_two,min_txt_one,txt_per3,txt_per2,txt_per1,txt_name;
    private Retrofit retrofit = Retrofit.getInstance();
    onFeatchdataListner onFeatchdataListner;
    DataBase dataBase;
    CircleImageView profile;
    AnimatedBottomBar bottom_bar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fristfragment_a, container, false);
        dataBase=new DataBase(getActivity());
        bottom_bar = getActivity().findViewById(R.id.bottom_bar);
        profile=v.findViewById(R.id.profile);
        txt_per3=v.findViewById(R.id.txt_per3);
        txt_name=v.findViewById(R.id.txt_name);
        txt_per1=v.findViewById(R.id.txt_per1);
        txt_per2=v.findViewById(R.id.txt_per2);
        min_txt_one=v.findViewById(R.id.min_txt_one);
        min_txt_two=v.findViewById(R.id.min_txt_two);
        min_txt_three=v.findViewById(R.id.min_txt_three);
        readingPage = v.findViewById(R.id.readingPage);
        billpage = v.findViewById(R.id.billpage);
        add_complaint = v.findViewById(R.id.add_complaint);
        statement = v.findViewById(R.id.statement);
        pending=v.findViewById(R.id.pending);
        getBasicDetais();


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottom_bar.selectTabAt(0, true);
            }
        });
        onFeatchdataListner=(MainActivity) getActivity();
        roundProgressBar = v.findViewById(R.id.roundProgressBar);
        roundProgressBar1 = v.findViewById(R.id.roundProgressBar1);
        roundProgressBar2 = v.findViewById(R.id.roundProgressBar2);
        roundProgressBar.setOnProgressChangeListner(new OnProgressChangeListner() {
            @Override
            public void onProgressChange(float progess) {

                int xx = (int) progess;
                Log.e("progess", xx + "");

            }
        });
        roundProgressBar1.setOnProgressChangeListner(new OnProgressChangeListner() {
            @Override
            public void onProgressChange(float progess) {

                int xx = (int) progess;
                Log.e("progess", xx + "");

            }
        });
        roundProgressBar2.setOnProgressChangeListner(new OnProgressChangeListner() {
            @Override
            public void onProgressChange(float progess) {

                int xx = (int) progess;
                Log.e("progess", xx + "");

            }
        });


        _onclick_buttons(readingPage, new Intent(getActivity(), ReaderPage.class),false);
        _onclick_buttons(billpage, new Intent(getActivity(), BillingPage.class),false);
        _onclick_buttons(add_complaint, new Intent(getActivity(), RegisterListPage.class),false);
        _onclick_buttons(statement, new Intent(getActivity(), StateMent.class),true);
        _onclick_buttons(pending, new Intent(getActivity(), Pending.class),true);
        return v;
    }


    private void _onclick_buttons(ElasticLayout elasticLayout, Intent intent,Boolean isOnline) {

        if (isOnline) {
            elasticLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(intent);

                }
            });
        }else {


            elasticLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (Importdata()){
                        startActivity(intent);
                    }else {
                       onFeatchdataListner.onFetch();
                    }
                }
            });


        }


    }



    public boolean Importdata(){
        if (dataBase.getCount(Tables_.TABLE_CUSTOMER)==0){
           return  false;
        }
       return true;



    }


    @Override
    public void onResume() {
        super.onResume();
        getdashborsitems();

    }

    String amt[] = new String[10];
    int persentage[] = new int[10];
    public void getdashborsitems() {

        retrofit.getApi().getDashbordItem(branch_code, User_id)
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()) {


                            JsonObject responseData = response.body();
                            if (responseData != null) {

                                JsonElement property = responseData.get("1");
                                getdata(0,property,"Amount");
                                JsonElement property2 = responseData.get("2");
                                getdata(1,property2,"ReadingCount");
                               JsonElement property3 = responseData.get("3");
                                getdata(2,property3,"BillingCount");


                                setThedashView();


                            }
                        }
                    }


                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });


    }

    private void setThedashView() {


        txt_per1.setText(""+persentage[0]+"%");
        txt_per2.setText(""+persentage[1]+"%");
        txt_per3.setText(""+persentage[2]+"%");


        min_txt_one.setText(S_P.R+amt[0]);
        min_txt_two.setText(amt[1]);
        min_txt_three.setText(amt[2]);

       roundProgressBar.setCurrentprogess(persentage[2],true);
        roundProgressBar1.setCurrentprogess(persentage[0],true);
        roundProgressBar2.setCurrentprogess(persentage[1],true);



    }


    public void getdata(int i,JsonElement property,String tag){

        if (property.isJsonArray()) {

            JsonArray jsonArray = property.getAsJsonArray();


            for (JsonElement element : jsonArray) {
                if (element.isJsonObject()) {
                    JsonObject jsonObject = element.getAsJsonObject();


                    amt[i] = jsonObject.get(tag).getAsString();
                    persentage[i] = jsonObject.get("%").getAsInt();
                }


            }
        }


    }


    String branch_code, User_id,readername;

    private void getBasicDetais() {


        String retrieve = S_P.getJsonData(getActivity());
        try {
            JSONObject jsonObject = new JSONObject(retrieve);
            branch_code = jsonObject.getString("branch_code");
            User_id = jsonObject.getString("id");
            readername=jsonObject.getString("name");
            txt_name.setText("Hello "+readername);


        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


    }
}
