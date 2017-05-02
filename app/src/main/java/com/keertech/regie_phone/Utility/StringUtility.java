package com.keertech.regie_phone.Utility;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by soup on 2017/5/2.
 */

public class StringUtility {

    public static boolean notObjEmpty(JSONObject object, String key){
        if(object.isNull(key)){
            return false;
        }else{
            return true;
        }
    }

    public static boolean isEmpty(String str){
        if(str != null && str.length() > 0 && !"".equals(str)) return false;

        return true;
    }

    public static boolean isSuccess(JSONObject response){
        try {
            return response.getBoolean("success");
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    //SharedPreferences
    private static SharedPreferences getSharedPreferences(Context context, String where){
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                where, Context.MODE_PRIVATE);
        return sharedPreferences;
    }

    private static SharedPreferences.Editor getSharedPreferencesEditor(Context context, String where){
        SharedPreferences sharedPreferences = StringUtility.getSharedPreferences(context, where);

        SharedPreferences.Editor editor =   sharedPreferences.edit();

        return editor;
    }

    public static void putSharedPreferences(Context context, String where, String key, String value){
        SharedPreferences.Editor editor = StringUtility.getSharedPreferencesEditor(context, where);
        editor.putString(key, value);
        editor.commit();
    }

    public static void putSharedPreferences(Context context, String where, String key, int value){
        SharedPreferences.Editor editor = StringUtility.getSharedPreferencesEditor(context, where);
        editor.putInt(key, value);
        editor.commit();
    }

    public static void putSharedPreferences(Context context, String where, String key, boolean value){
        SharedPreferences.Editor editor = StringUtility.getSharedPreferencesEditor(context, where);
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void putSharedPreferences(Context context, String where, String key, float value){
        SharedPreferences.Editor editor = StringUtility.getSharedPreferencesEditor(context, where);
        editor.putFloat(key, value);
        editor.commit();
    }

    public static String getSharedPreferencesForString(Context context, String where, String key){
        SharedPreferences sharedPreferences = StringUtility.getSharedPreferences(context, where);
        return sharedPreferences.getString(key,"");
    }

    public static int getSharedPreferencesForInt(Context context, String where, String key){
        SharedPreferences sharedPreferences = StringUtility.getSharedPreferences(context, where);
        return sharedPreferences.getInt(key, 0);
    }

    public static boolean getSharedPreferencesForBoolean(Context context, String where, String key){
        SharedPreferences sharedPreferences = StringUtility.getSharedPreferences(context, where);
        return sharedPreferences.getBoolean(key, false);
    }

    public static float getSharedPreferencesForFloat(Context context, String where, String key){
        SharedPreferences sharedPreferences = StringUtility.getSharedPreferences(context, where);
        return sharedPreferences.getFloat(key, 0.0f);
    }

}
