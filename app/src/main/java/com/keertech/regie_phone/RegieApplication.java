package com.keertech.regie_phone;

import android.app.Application;
import android.os.Environment;

import com.keertech.regie_phone.Constant.Constant;

import java.io.File;

/**
 * Created by soup on 2017/5/3.
 */

public class RegieApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        File sdcard = Environment.getExternalStorageDirectory();
        String path = sdcard.getPath()+File.separator+ Constant.Base_path;
        File pathFile = new File(path);
        if(!pathFile.exists()){
            pathFile.mkdirs();
        }
    }
}
