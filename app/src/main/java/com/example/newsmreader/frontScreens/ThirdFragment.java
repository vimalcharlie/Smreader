package com.example.newsmreader.frontScreens;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.newsmreader.AllstaticObj;
import com.example.newsmreader.Dtabase.DataBase;
import com.example.newsmreader.Dtabase.Tables_;
import com.example.newsmreader.Login;
import com.example.newsmreader.R;
import com.example.newsmreader.S_P;
import com.google.android.material.snackbar.Snackbar;
import com.skydoves.elasticviews.ElasticButton;

import org.json.JSONException;
import org.json.JSONObject;

public class ThirdFragment extends Fragment {
    TextView txt_name, txtphone2, txtaddress2, txtarea2;
    DataBase dataBase;
    ConstraintLayout imcsignout;
    View v;
    RelativeLayout relativeLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         v = inflater.inflate(R.layout.profilepage, container, false);
        dataBase = new DataBase(getActivity());
        relativeLayout=v.findViewById(R.id.root_rl);
        txt_name = v.findViewById(R.id.txt_name);
        txtphone2 = v.findViewById(R.id.txtphone2);
        txtaddress2 = v.findViewById(R.id.txtaddress2);
        txtarea2 = v.findViewById(R.id.txtarea2);
        imcsignout=v.findViewById(R.id.imcsignout);

        imcsignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signout();
            }
        });

        getBasicDetais(getActivity());
        txt_name.setText(readername);
        txtphone2.setText(mobile);
        txtaddress2.setText(address);
        txtarea2.setText(mail);


        return v;
    }


    String branch_code, User_id, header, header1, header2, address, mobile, content, mail, readername;


    private void getBasicDetais(Activity activity) {
        String retrieve = S_P.getJsonData(activity);
        try {
            JSONObject jsonObject = new JSONObject(retrieve);
            branch_code = jsonObject.getString("branch_code");
            User_id = jsonObject.getString("id");
            address = jsonObject.getString("Useraddress");
            header = jsonObject.getString("header");
            readername = jsonObject.getString("name");
            header1 = jsonObject.getString("header1");
            header2 = jsonObject.getString("header2");
            mobile = jsonObject.getString("UserMobile");
            content = jsonObject.getString("content");
            mail = jsonObject.getString("email");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


    }

    public void signout() {
     long count=dataBase.getCount(Tables_.TABLE_READING);
     long count2=dataBase.getCount(Tables_.TABLE_BILL);


     if (count2>0 || count>0){
         Snackbar.make(relativeLayout,"Export the values first",Snackbar.LENGTH_LONG).show();

     }else {


         ViewGroup viewGroup = getActivity().findViewById(android.R.id.cut);

         View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.alertbox3, viewGroup, false);

         AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
         builder.setView(dialogView);
         AlertDialog dialog = builder.create();
         TextView textView=dialogView.findViewById(R.id.txt);
         textView.setText("Do you want to do the\nLogout?");
         ElasticButton bt_yes = dialogView.findViewById(R.id.bt_yes);
         ElasticButton bt_no = dialogView.findViewById(R.id.bt_no);
         bt_yes.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if(dataBase.Droptables()){
                     S_P.clearALLPREFERENCES(getActivity());
                     Intent intent=new Intent(getActivity(), Login.class);
                     startActivity(intent);
                     getActivity().finish();
                 }





             }
         });
         bt_no.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {


                 dialog.cancel();


             }
         });


         dialog.show();
         dialog.setCancelable(false);



     }

    }
}
