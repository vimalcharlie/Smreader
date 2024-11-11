package com.example.newsmreader;

import android.content.Context;
import android.content.SharedPreferences;

public class AllstaticObj {
    private static final String PREF_NAME = "MyAppPreferences";
    public static boolean isPay = false;
    public static String whichImg = "0";
    public static String avgmonth = "1";
    public static String WhichLocation="0";
    public static String isCollecionprint = "0";
    public static int whichPrinter = 0;
    public static String is_OnlinePay="0";
    public  static  long onlineamt=0;
    public  static  String onPoweredby="0";
    public static long cashamt=0;
    public static long total=0;


    public static void getPREFERENCES(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String s = sharedpreferences.getString(S_P.ENABLE_PAY, "0");
        whichImg = sharedpreferences.getString(S_P.image_id, "0");
        isCollecionprint = sharedpreferences.getString(S_P.CollectioninPrint, "0");
        whichPrinter = sharedpreferences.getInt(S_P.printSet, 0);
        is_OnlinePay=sharedpreferences.getString(S_P.is_online,"0");
        onPoweredby=sharedpreferences.getString(S_P.is_poweredby,"0");
        if (s.equalsIgnoreCase("1")) {
            isPay = true;
        } else {
            isPay = false;
        }


    }


}
