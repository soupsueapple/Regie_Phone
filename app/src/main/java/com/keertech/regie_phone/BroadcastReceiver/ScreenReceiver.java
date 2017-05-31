package com.keertech.regie_phone.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.keertech.regie_phone.Service.LocationService;
import com.keertech.regie_phone.KeepLiveActivity;
import com.keertech.regie_phone.RegieApplication;
import com.keertech.regie_phone.Utility.CheckTopTask;

/**
 * Created by soup on 2017/3/29.
 */

public class ScreenReceiver extends BroadcastReceiver{

    private static final String TAG = "[ScreenReceiver]";

    private Handler mHandler = new Handler(Looper.getMainLooper());
    private CheckTopTask mCheckTopTask = new CheckTopTask(RegieApplication.getContext());

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        if (Intent.ACTION_SCREEN_ON.equals(action)) { // 点亮屏幕未解锁
            Log.d(TAG, "============开屏===============");
            context.startService(new Intent(context, LocationService.class));

        } else if (Intent.ACTION_SCREEN_OFF.equals(action)) { // 锁屏
            Log.d(TAG, "============锁屏===============");

            CheckTopTask.startForeground(context);
            mHandler.postDelayed(mCheckTopTask, 3000);

        } else if (Intent.ACTION_USER_PRESENT.equals(action)) { // 解锁
            Log.d(TAG, "============解锁===============");

            KeepLiveActivity liveActivity = KeepLiveActivity.instance != null ? KeepLiveActivity.instance.get() : null;
            if (liveActivity != null) {
                liveActivity.finishSelf();
            }

            mHandler.removeCallbacks(mCheckTopTask);

        }

    }
}
