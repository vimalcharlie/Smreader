package com.example.newsmreader.ServiceRetrofit;

import com.example.newsmreader.Models.BillPrintModel;
import com.example.newsmreader.Models.BillPrintModelArray;
import com.example.newsmreader.Models.ComplaintModelArray;
import com.example.newsmreader.Models.Complaint_ListArray;
import com.example.newsmreader.Models.CustomerModelArray;
import com.example.newsmreader.Models.LocationModelArray;
import com.example.newsmreader.Models.LocationfilterModel;
import com.example.newsmreader.Models.LoginModelArray;
import com.example.newsmreader.Models.ReadPrintModelArray;
import com.example.newsmreader.Models.ReadingsaveModel;
import com.example.newsmreader.Models.SettingModelArray;
import com.example.newsmreader.Models.SlabModelArray;
import com.example.newsmreader.Models.StamentreadModelArray;
import com.example.newsmreader.Models.StateMentModelArray;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Api {
     //get the reading state ment
    @FormUrlEncoded
    @POST("app/Readersapp/statement.php")
    Call<StamentreadModelArray> READ_STATE(
            @Field("branchcode") String branchcode ,
            @Field("TransType") String TransType ,
            @Field("CommonFilter") String CommonFilter ,
            @Field("datefrom") String datefrom ,
            @Field("dateto") String dateto ,
            @Field("location") String location ,
            @Field("Userid") String Userid
    );

    // get the billing state ment
    @FormUrlEncoded
    @POST("app/Readersapp/statement.php")
    Call<StateMentModelArray> Bill_STATE(
            @Field("branchcode") String branchcode ,
            @Field("TransType") String TransType ,
            @Field("CommonFilter") String CommonFilter ,
            @Field("datefrom") String datefrom ,
            @Field("dateto") String dateto ,
            @Field("location") String location ,
            @Field("Userid") String Userid
    );

    // get the login details
    @FormUrlEncoded
    @POST("app/Readersapp/login.php")
    Call<LoginModelArray> Login_Api(
            @Field("branchcode") String branchcode ,
            @Field("email") String email ,
            @Field("pwd") String pwd

    );


    @FormUrlEncoded
    @POST("app/Readersapp/location.php")
    Call<LocationModelArray> GetFullLocation(
            @Field("branchcode") String branchcode ,
            @Field("User") String User


    );
    @FormUrlEncoded
    @POST("app/Readersapp/locationfiltered.php")
    Call<LocationfilterModel> GetFilterLocation(
            @Field("branchcode") String branchcode ,
            @Field("User") String User,
            @Field("location") String location



    );



    @FormUrlEncoded
    @POST("app/Readersapp/customernew.php")
    Call<CustomerModelArray> GetCustomerList(
            @Field("branchcode") String branchcode ,
            @Field("Userid") String User,
            @Field("Appcode") String Appcode,
            @Field("Cnt") String Cnt,
            @Field("location") String location

    );




    @FormUrlEncoded
    @POST("app/Readersapp/Slab.php")
    Call<SlabModelArray> GetSlab(
            @Field("branchcode") String branchcode


    );



    @FormUrlEncoded
    @POST("app/Readersapp/Settings.php")
    Call<SettingModelArray> GetSettiings(
            @Field("branchcode") String branchcode,
            @Field("Userid") String Userid
    );


    @FormUrlEncoded
    @POST("app/Readersapp/ReadingSync.php")

    Call<ReadingsaveModel> upload_Reading(
            @Field("branchcode") String branchcode
            ,@Field("mobile") String mobile
            ,@Field("meter_number") String meter_number
            ,@Field("Latitude") String Latitude
            ,@Field("Longitude") String Longitude
            ,@Field("Importeddate") String Importeddate
            ,@Field("customer_guid") String customer_guid
            ,@Field("reading") String reading
            ,@Field("Previous_Bal") String Previous_Bal
            ,@Field("MinRent") String MinRent
            ,@Field("latefee") String latefee
            ,@Field("Additional") String Additional
            ,@Field("read_date") String read_date
            ,@Field("read_time") String read_time
            ,@Field("owner_guid") String owner_guid
            ,@Field("BillAmount") String BillAmount
            ,@Field("SpotBilling") String SpotBilling
            ,@Field("Total") String Total
            ,@Field("SpotBillingCharge") String SpotBillingCharge
            ,@Field("PrevReading") String PrevReading
            ,@Field("Billid") String Billid
            ,@Field("average") String average
            ,@Field("hold")    String hold
            ,@Field("closed") String closed
    );

    @FormUrlEncoded
    @POST("app/Readersapp/BillingSync.php")
    Call<ReadingsaveModel> upload_Bill(
             @Field("branchcode") String branchcode
            , @Field("customer_guid") String customer_guid
            , @Field("amount") String amount
            , @Field("recieved") String recieved
            , @Field("balance") String balance
            , @Field("latefee") String latefee
            , @Field("payment_date") String payment_date
            , @Field("owner_guid") String owner_guid
            , @Field("Addtnl") String Addtnl
            , @Field("BillRecvd") String BillRecvd
            , @Field("Paid_time") String Paid_time
            , @Field("SpotBilling") String SpotBilling
            , @Field("SpotBillingCharge") String SpotBillingCharge
            , @Field("RecNo") String RecNo
            , @Field("online") String online
            ,@Field("Importeddate") String Importeddate
            , @Field("Reference") String Reference


    );




    @FormUrlEncoded
    @POST("app/Readersapp/ServiceCategory-App.php")
    Call<Complaint_ListArray> Get_complaint_list(
            @Field("branchcode") String branchcode



    );


    @Multipart
    @POST("app/Readersapp/Servicesave.php")
    Call<EstimateArray> uploadfile(
            @Part("branchcode") RequestBody branchcode,
            @Part("customer_guid") RequestBody customer_guid,
            @Part("complaint") RequestBody complaint,
            @Part("service_date") RequestBody service_date,
            @Part("OwnerGuid") RequestBody OwnerGuid,
            @Part("complaint_type") RequestBody complaint_type,
            @Part MultipartBody.Part image
    );


    @FormUrlEncoded
    @POST("app/Readersapp/Dashboard.php")
    Call<JsonObject> getDashbordItem(
            @Field("branchcode") String branchcode,
            @Field("Userid") String Userid


    );





    @FormUrlEncoded
    @POST("app/Readersapp/ReadingRePrintDetails.php")
    Call<ReadPrintModelArray> getReprintReading(
            @Field("branchcode") String branchcode,
            @Field("id") String Userid);



    @FormUrlEncoded
    @POST("app/Readersapp/BillingRePrintDetails.php")
    Call<BillPrintModelArray> getReprintBill(
            @Field("branchcode") String branchcode,
            @Field("id") String Userid


    );


    @FormUrlEncoded
    @POST("app/Readersapp/ServiceList.php")
    Call<ComplaintModelArray> getComplaintList(
            @Field("branchcode") String branchcode,
            @Field("OwnerGuid") String Userid


    );


}
