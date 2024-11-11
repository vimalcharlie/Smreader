package com.example.newsmreader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newsmreader.Models.Complaint_List;
import com.example.newsmreader.Models.Complaint_ListArray;
import com.example.newsmreader.Models.CustomerModel;
import com.example.newsmreader.ServiceRetrofit.EstimateArray;
import com.example.newsmreader.ServiceRetrofit.Retrofit;
import com.example.newsmreader.reading.TakeReadingPage;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import me.echodev.resizer.Resizer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterPage extends AppCompatActivity {

    CustomerModel cModel;
    TextView txt_location;
    ArrayList<Complaint_List> mList = new ArrayList<>();
    Spinner sp_complaints;
    LinearLayout lnr_image;
    private static final int REQUEST_CAMERA = 1;
    ImageView img_view;
    EditText et_complaints;
    ImageView imv_abd;
    private Retrofit retrofit = Retrofit.getInstance();
    SwipeRefreshLayout swipe;
    public static String[] MY_PERMISSIONS_STORAGE = {
            "android.permission.CAMERA",
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.MOUNT_UNMOUNT_FILESYSTEMS"};
    public static final int REQUEST_EXTERNAL_STORAGE = 1;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        progressBar = findViewById(R.id.progressBar);
        imv_abd = findViewById(R.id.imv_abd);
        swipe = findViewById(R.id.swipe);
        et_complaints = findViewById(R.id.et_complaints);
        txt_location = findViewById(R.id.txt_location);
        sp_complaints = findViewById(R.id.sp_complaints);
        img_view = findViewById(R.id.img_view);
        lnr_image = findViewById(R.id.lnr_image);
        Intent intent = getIntent();
        cModel = (CustomerModel) intent.getSerializableExtra("key1");
        txt_location.setText(cModel.getLocation());
        getBasicDetais();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        lnr_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission();


            }
        });
        image = BitmapFactory.decodeResource(getResources(), R.drawable.noimage);
        getCompalits();

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCompalits();
            }
        });
    }


    private void requestPermission() {
        //检测是否有写的权限
        //Check if there is write permission
        int checkCallPhonePermission = ContextCompat.checkSelfPermission(RegisterPage.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(RegisterPage.this, MY_PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_CAMERA);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CAMERA);
            } else {
//                Toast.makeText(MainActivity.this,R.string.title_permission,Toast.LENGTH_SHORT).show();
                requestPermission();
            }
        }

    }


    Bitmap image;

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA) {
            try {
                image = (Bitmap) data.getExtras().get("data");
                img_view.setImageBitmap(image);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    private void getCompalits() {
        swipe.setRefreshing(true);


        mList = new ArrayList<>();
        retrofit.getApi().Get_complaint_list(branch_code)
                .enqueue(new Callback<Complaint_ListArray>() {
                    @Override
                    public void onResponse(Call<Complaint_ListArray> call, Response<Complaint_ListArray> response) {
                        swipe.setRefreshing(false);
                        if (response.isSuccessful()) {
                            if (response.body().getStatus().equalsIgnoreCase("true")) {
                                //  Toast.makeText(RegisterPage.this, response.body().getStatus(), Toast.LENGTH_SHORT).show();
                                ArrayList<Complaint_List> list = response.body().getData();
                                for (Complaint_List obj : list) {
                                    mList.add(new Complaint_List(obj.getId(), obj.getName()));
                                }
                                setSpinner();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Complaint_ListArray> call, Throwable t) {
                        swipe.setRefreshing(false);

                    }
                });
    }

    private void setSpinner() {
        ArrayAdapter complaints = new ArrayAdapter<Complaint_List>(RegisterPage.this, R.layout.spinner_llst, mList);
        complaints.setDropDownViewResource(R.layout.spinner_llst);
        sp_complaints.setAdapter(complaints);
    }


    public void uploadComplaint(View v) {

        if (mList.size() == 0) {
            getCompalits();
        } else {

            if (sp_complaints.getSelectedItem().toString().equalsIgnoreCase("Select")) {


                Snackbar.make(findViewById(R.id.root_rl),"Select any Complaint",Snackbar.LENGTH_LONG).show();
            } else {
                settoserver();


            }


        }


    }

    private void settoserver() {
        progressBar.setVisibility(View.VISIBLE);

        File filesDir = RegisterPage.this.getFilesDir();
        File imageFile = new File(filesDir, "name" + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            image.compress(Bitmap.CompressFormat.JPEG, 100, os);

            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }


        Bitmap resizedImage = null;
        try {

            resizedImage = new Resizer(RegisterPage.this)
                    .setTargetLength(480)
                    .setSourceImage(imageFile)
                    .getResizedBitmap();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Random random = new Random();
        String id = String.format("%04d", random.nextInt(10000000));

        File filesDir1 = RegisterPage.this.getFilesDir();
        File imageFile1 = new File(filesDir1, id + ".jpg");

        OutputStream os1;
        try {
            os1 = new FileOutputStream(imageFile1);
            resizedImage.compress(Bitmap.CompressFormat.JPEG, 100, os1);


            os1.flush();
            os1.close();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }
        //Parsing any Media type file
        MultipartBody.Part fileToUpload = null;
        try {
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile1);

            fileToUpload = MultipartBody.Part.createFormData("image", imageFile1.getName(), requestBody);
        } catch (Exception e) {
            e.printStackTrace();
        }


        RequestBody brnch = RequestBody.create(MediaType.parse("text/plain"), branch_code);
        RequestBody OwnerGuid = RequestBody.create(MediaType.parse("text/plain"), User_id);
        RequestBody customer_guid = RequestBody.create(MediaType.parse("text/plain"), cModel.getCustomerID());
        RequestBody complaintq = RequestBody.create(MediaType.parse("text/plain"), et_complaints.getText().toString());
        RequestBody service_date = RequestBody.create(MediaType.parse("text/plain"), ImportDate);
        RequestBody complaint_type_t = RequestBody.create(MediaType.parse("text/plain"), mList.get(sp_complaints.getSelectedItemPosition() + 1).getId());

        retrofit.getApi().uploadfile(brnch, customer_guid, complaintq, service_date, OwnerGuid, complaint_type_t, fileToUpload)
                .enqueue(new Callback<EstimateArray>() {
                    @Override
                    public void onResponse(Call<EstimateArray> call, Response<EstimateArray> response) {
                        progressBar.setVisibility(View.GONE);
                        Log.e(">>>>>>", "nsnsnsnsnsnsnsn" + response.body().getData().toString());
                        Log.e(">>>>>>", "nsnsnsnsnsnsnsn" + response.body().toString());
                        if (response.isSuccessful()) {
                            if (response.body().getStatus().equalsIgnoreCase("true")) {
                                finish();
                            } else {

                            }


                        }


                    }

                    @Override
                    public void onFailure(Call<EstimateArray> call, Throwable t) {

                        progressBar.setVisibility(View.GONE);
                    }
                });


    }


    String branch_code, User_id, ImportDate;

    private void getBasicDetais() {
        ImportDate = S_P.getPREFERENCES(RegisterPage.this, S_P.Importeddate);
        String retrieve = S_P.getJsonData(getApplicationContext());
        try {
            JSONObject jsonObject = new JSONObject(retrieve);
            branch_code = jsonObject.getString("branch_code");
            User_id = jsonObject.getString("id");


        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


    }



}