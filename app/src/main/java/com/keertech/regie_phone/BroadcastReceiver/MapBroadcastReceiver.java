package com.keertech.regie_phone.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.keertech.regie_phone.Observer.MapObserver;

/**
 * Created by soup on 15/9/8.
 */
public class MapBroadcastReceiver extends BroadcastReceiver {

    private static BroadcastReceiver receiver;

    private static MapObserver mapObserver;

    private final static String MAP_RECEIVER = "com.keer.client.map";

    private static BroadcastReceiver getReceiver() {
        if (receiver == null) {
            receiver = new MapBroadcastReceiver();
        }
        return receiver;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equalsIgnoreCase(MAP_RECEIVER)) {
            double bd_latitude = intent.getDoubleExtra("bd_latitude", 0.0);
            double bd_longitude = intent.getDoubleExtra("bd_longitude", 0.0);
            double latitude = intent.getDoubleExtra("latitude", 0.0);
            double longitude = intent.getDoubleExtra("longitude", 0.0);
            String address = intent.getStringExtra("address");

            notifyObserver(bd_latitude, bd_longitude, latitude, longitude, address);
        }
    }

    public static void registerSwitchPluginReceiver(Context mContext) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(MAP_RECEIVER);
        mContext.getApplicationContext().registerReceiver(getReceiver(), filter);
    }

    public static void unRegisterSwitchPluginReceiver(Context mContext) {
        if (receiver != null) {
            try {
                mContext.getApplicationContext().unregisterReceiver(receiver);
            } catch (Exception e) {
            }
        }
    }

    private void notifyObserver(double bd_latitude, double bd_longitude, double latitude, double longitude, String address) {
        mapObserver.mapObserver(bd_latitude, bd_longitude, latitude, longitude, address);
    }

    /**
     * 注册切换插件观察者
     *
     * @param
     */
    public static void registerObserver(MapObserver observer) {
        mapObserver = observer;
    }

    /**
     * 注销切换插件观察者
     *
     * @param
     */
    public static void removeRegisterObserver(MapObserver observer) {
        mapObserver = observer;
        mapObserver = null;
    }
}
