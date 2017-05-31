package com.keertech.regie_phone;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;

import com.baidu.mapapi.SDKInitializer;
import com.keertech.regie_phone.BroadcastReceiver.ScreenReceiver;
import com.keertech.regie_phone.Constant.Constant;
import com.keertech.regie_phone.Utility.StringUtility;

import java.io.File;

/**
 * Created by soup on 2017/5/3.
 */

public class RegieApplication extends Application{

    protected static Context context;

    @Override
    public void onCreate() {
        SDKInitializer.initialize(this);
        super.onCreate();

        StringUtility.putSharedPreferences(getApplicationContext(), Constant.LOCATION_TIME, Constant.LOCATION_TIME,"");

        context = getApplicationContext();

        registerScreenReceiver();

        File sdcard = Environment.getExternalStorageDirectory();
        String path = sdcard.getPath()+File.separator+ Constant.Base_path;
        File pathFile = new File(path);
        if(!pathFile.exists()){
            pathFile.mkdirs();
        }
    }

    /**
     * 注册screen状态广播接收器
     */
    private void registerScreenReceiver() {

        ScreenReceiver screenReceiver = new ScreenReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        registerReceiver(screenReceiver, filter);
    }

    public static Context getContext() {
        return context;
    }
}
