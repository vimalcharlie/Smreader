package com.example.newsmreader;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

public class S_P {

    public static String from_state_date;
    public static String to_state_date;

    public static String R = "₹";

    private static final String PREF_NAME = "MyAppPreferences";
    public static final String printSet = "printSet";
    public  static final String is_online="Online";
    public  static final String is_poweredby="PoweredBy";

    private static final String KEY_JSON_DATA = "json_data";
    public static final String READ_NO = "ReadBillnumber";
    public static final String BILL_NO = "Billnumber";
    public static String image_id = "image_id";
    public static String CollectioninPrint = "CollectioninPrint";

    public static String ENABLE_PAY = "enable_payment";
    public static String Importeddate = "Importeddate";
    public static String Billingpattern = "Billingpattern";

    // Save JSON data to SharedPreferences
    public static void saveJsonData(Context context, String jsonData) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_JSON_DATA, jsonData);
        editor.apply();
    }

    // Retrieve JSON data from SharedPreferences
    public static String getJsonData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(KEY_JSON_DATA, null);
    }


    public static void setPREFERENCESint(Context context, String key, int value) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getPREFERENCESint(Context context, String key) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        int value = sharedpreferences.getInt(key, 0);
        return value;

    }


    public static void setPREFERENCES(Context context, String key, String value) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getPREFERENCES(Context context, String key) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedpreferences.getString(key, null);

    }

    public static void setDates(String from, String to) {

     from_state_date=from;
     to_state_date=to;




    }


    public static void clearALLPREFERENCES(Context context ){

     SharedPreferences   sharedpreferences =context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.apply();

    }




    public static void increment_the_readcount(Context context) {

        SharedPreferences sharedpreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        int value = sharedpreferences.getInt(S_P.READ_NO, 0);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(S_P.READ_NO, value+1);
        editor.commit();

    }


    public static void increment_the_Billcount(Context context) {

        SharedPreferences sharedpreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        int value = sharedpreferences.getInt(S_P.BILL_NO, 0);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(S_P.BILL_NO, value+1);
        editor.commit();

    }



}
