package com.example.newsmreader.Bluethhoth;

import android.content.Context;
import android.content.SharedPreferences;


public class SharePreferecnceUtils {
    static SharedPreferences sharedpreferences;
    private final static String SHARE_CSMART = "printer";

    private final static String SHARE_CONNECTDEVICE ="share.connectdevice";



    public static String getConectDevice(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHARE_CSMART, Context.MODE_PRIVATE);
        return sp.getString(SHARE_CONNECTDEVICE, "");
    }
    public static void setPREFERENCES(Context context,String value){
        sharedpreferences =context.getSharedPreferences(SHARE_CSMART, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(SHARE_CONNECTDEVICE,value);
        editor.commit();
    }

    public static void clearALLPREFERENCES(Context context){

        sharedpreferences =context.getSharedPreferences(SHARE_CSMART, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.apply();

    }


}
