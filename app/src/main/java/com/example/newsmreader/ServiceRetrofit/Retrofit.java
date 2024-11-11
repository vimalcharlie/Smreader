package com.example.newsmreader.ServiceRetrofit;




import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.converter.gson.GsonConverterFactory;

public class Retrofit {
    private static Retrofit instance;

    public static Retrofit getInstance(){
        if(instance == null){
            instance = new Retrofit();
        }
        return instance;
    }





    private static retrofit2.Retrofit retrofit(){
        return new retrofit2.Retrofit.Builder()
                .baseUrl("https://www.smreader.net/")
                .client(okHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    private static OkHttpClient okHttpClient(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        return client;
    }

    public static Api getApi(){
        return retrofit().create(Api.class);
    }




}