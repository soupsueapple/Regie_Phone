package com.keertech.regie_phone.Constant;

import android.content.Context;

/**
 * Created by soup on 2017/5/2.
 */

public class Constant {

    public final static String Base_path = "regie";

    public static String  getFilePath(Context context){
        return context.getFilesDir().getPath()+"/";
    }

    public final static String Base_URL = "http://221.232.151.12:1000/mg/mg!";
    public final static String MWB_Base_URL = "http://10.69.0.188:8080/newmwb/";
    public final static String CISS_Base_URL = "http://10.69.0.107:8080/CISS/";
    public final static String MIIS_Base_URL = "http://10.69.0.108:9080/miis/";

//    public final static String Base_URL = "http://192.168.9.9:8080/mg/mg!";
//    public final static String MWB_Base_URL = "http://192.168.9.9:8080/newmwb/";
//    public final static String CISS_Base_URL = "http://10.69.0.107:8080/CISS/";
//    public final static String MIIS_Base_URL = "http://192.168.9.9:8080/miis/";

    public final static String EXEC = "exec.action";

    public static String userId = "";
    public static String deviceId = "";

    public final static String Login_URL = "login.action";

    public  static double INTO_SHOP = 1000.00;

    public final static String SharedPreferencesLogin = "Login";
    public final static String SharedPreferencesLoginKey = "AutoLogin";
    public final static String SharedPreferencesLoginUserName = "username";
    public final static String SharedPreferencesLoginUserPassword = "password";

    public static String apkVer = "1.0";

    public static boolean isRefreshXCJL = false;
    public static boolean isRefreshRCXC = false;
    public static boolean isRefresPlan = false;
    public static boolean isRefreshXCJ2 = false;
    public static boolean isRefreshXCJ3 = false;
    public static boolean isYc = false;

    public static boolean isRefresJZZZSGrid = false;
    public static boolean isRefresNoJZZZS = false;
    public static boolean isRefresJZZZS = false;
    public static boolean isRefreshSCJC = false;
    public static boolean isRefresJJFinish = false;
    public static boolean isRefresJZZZSFinish = false;

}
