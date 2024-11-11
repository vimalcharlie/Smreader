package com.example.newsmreader;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextSwitcher;
import android.widget.Toast;

import com.example.newsmreader.Models.LoginModel;
import com.example.newsmreader.Models.LoginModelArray;
import com.example.newsmreader.ServiceRetrofit.EstimateArray;
import com.example.newsmreader.ServiceRetrofit.Retrofit;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {


    EditText ext_customerId,editText,editText2;
    Button bt_login;


    private Retrofit retrofit=Retrofit.getInstance();
    ArrayList<LoginModel> mList=new ArrayList<>();




    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        // Configure the behavior of the hidden system bars.
        windowInsetsController.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        );

        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());
        setContentView(R.layout.activity_login);
        swipeRefreshLayout=findViewById(R.id.swipe);
        ext_customerId=findViewById(R.id.ext_customerId);
        editText=findViewById(R.id.editText);
        editText2=findViewById(R.id.editText2);
        bt_login=findViewById(R.id.bt_login);














        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                 if(Validate()){



                     _Login(ext_customerId.getText().toString(),editText.getText().toString(),editText2.getText().toString());



                     }



                 }





            }
        );








    }

    private void _Login(String toString, String toString1, String toString2) {
        swipeRefreshLayout.setRefreshing(true);

        retrofit.getApi().Login_Api(toString,toString1,toString2).enqueue(new Callback<LoginModelArray>() {
            @Override
            public void onResponse(Call<LoginModelArray> call, Response<LoginModelArray> response) {
                swipeRefreshLayout.setRefreshing(false);
                if (response.isSuccessful()){

                    if (response.body().getStatus().equalsIgnoreCase("True")){
                        ArrayList<LoginModel> list=response.body().getData();
                        JSONObject jsonObject = new JSONObject();
                        for (LoginModel obj:list) {
                            try {
                                jsonObject.put("branch_code", obj.getBranch_code());
                                jsonObject.put("name", obj.getName());
                                jsonObject.put("email", obj.getEmail());
                                jsonObject.put("address", obj.getAddress());
                                jsonObject.put("Useraddress",obj.getUseraddress());

                                jsonObject.put("password", obj.getPassword());
                                jsonObject.put("id", obj.getId());
                                jsonObject.put("UserMobile", obj.getUserMobile());

                                jsonObject.put("header", obj.getHeader());
                                jsonObject.put("header1", obj.getHeader1());
                                jsonObject.put("header2", obj.getHeader2());

                                jsonObject.put("mobile", obj.getMobile());
                                jsonObject.put("content", obj.getContent());

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                        String jsonString = StringEscapeUtils.unescapeHtml4(jsonObject.toString());
                        S_P.saveJsonData(getApplicationContext(),jsonString);

                        startActivity(new Intent(Login.this, NotificationPage.class));
                        finish();
                    }else {

                        Snackbar.make(findViewById(R.id.root_rl),"Invalid credentials!",Snackbar.LENGTH_LONG).show();


                    }

                }
            }

            @Override
            public void onFailure(Call<LoginModelArray> call, Throwable t) {
                Snackbar.make(findViewById(R.id.root_rl),"OOPS! NO INTERNET",Snackbar.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);

            }
        });





    }

    private boolean Validate() {

        return  true;
    }
}