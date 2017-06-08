package com.keertech.regie_phone.Service;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.keertech.regie_phone.Constant.Constant;
import com.keertech.regie_phone.Network.HttpClient;
import com.keertech.regie_phone.ProcessService;
import com.keertech.regie_phone.R;
import com.keertech.regie_phone.RegieApplication;
import com.keertech.regie_phone.Utility.DateTimeUtil;
import com.keertech.regie_phone.Utility.StringUtility;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;

import static com.keertech.regie_phone.Constant.Constant.userId;

/**
 * Created by soup on 2017/6/1.
 */

public class UpdateLocationService extends Service{

    public LocationClient mLocationClient;
    public MyLocationListener mMyLocationListener;

    private PowerManager.WakeLock mWakeLock = null;

    private MyBinder binder;
    private MyConn conn;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        System.out.println("onCreate");

        binder = new MyBinder();
        if(conn==null) conn = new MyConn();

        mLocationClient = new LocationClient(getApplicationContext());
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);

        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度，默认值gcj02
        option.setScanSpan(Constant.LOCATION_INTERVAL_TIME);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
        mLocationClient.requestLocation();
    }

    private void acquireWakeLock() {
        if (null == mWakeLock) {
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE,
                    this.getClass().getCanonicalName());
            if (null != mWakeLock) {
                mWakeLock.acquire();
                System.out.println("电源锁申请成功");
            }
        }
    }

    // 释放设备电源锁
    private void releaseWakeLock() {
        if (null != mWakeLock) {
            System.out.println("停止电源锁");
            mWakeLock.release();
            mWakeLock = null;
        }
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        UpdateLocationService.this.bindService(new Intent(this,LocationService2.class),conn, Context.BIND_IMPORTANT);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub

        acquireWakeLock();

        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        releaseWakeLock();

        Intent intent = new Intent(this, LocationService2.class);
        startService(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Notification notification = getDefaultNotification(
                    (String) msg.obj, false);
            startForeground(Constant.NOTIFICATION_ID, notification);

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(Constant.NOTIFICATION_ID, notification);
        }
    };

    public static Notification getDefaultNotification(String str, boolean isRed) {
        Context context = RegieApplication.getContext();
        Intent intent = new Intent(Constant.ACTION_NOTIFICATION_CLICK);

        RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.mote);
        view.setTextViewText(R.id.textView2, str);
        if (isRed)
            view.setTextColor(R.id.textView2, Color.RED);

//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.logo).setContentTitle("通知").setTicker("新消息");
        mBuilder.setPriority(Notification.PRIORITY_MIN);
        mBuilder.setAutoCancel(true);

//        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setContent(view);
        mBuilder.setAutoCancel(true);

        System.out.println("getDefaultNotification");

        return mBuilder.build();
    }

    private void doLogin(final double lat, final double lng) {
        String username = StringUtility.getSharedPreferencesForString(this, Constant.SharedPreferencesLogin,
                Constant.SharedPreferencesLoginUserName);
        String password = StringUtility.getSharedPreferencesForString(this, Constant.SharedPreferencesLogin,
                Constant.SharedPreferencesLoginUserPassword);

        String deviceId = StringUtility.getSharedPreferencesForString(this, "deviceId", "deviceId");

        RequestParams params = new RequestParams();
        params.put("deviceId", deviceId);
        params.put("username", username);
        params.put("password", password);

        HttpClient.post(Constant.Login_URL, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (StringUtility.isSuccess(response)) {
                    upLocation(lat, lng);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("responseString", statusCode + "");
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {

                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    private void upLocation(final double lat, final double lng) {

        String locationTime = StringUtility.getSharedPreferencesForString(getApplicationContext(),
                Constant.LOCATION_TIME, Constant.LOCATION_TIME);

        System.out.println("upLocation" + locationTime);

        if (locationTime.length() > 0) {
            Date locationDate = DateTimeUtil.getFormatDate(locationTime, DateTimeUtil.TIME_FORMAT);

            Date nowDate = new Date();

            if (nowDate.getTime() - locationDate.getTime() < 1000 * 60 * 15) {
                return;
            }
        }

        System.out.println(Constant.Base_URL + "location.action?userId=" + userId + "&longitude_bd=" + lng
                + "&latitude_bd=" + lat + "");

        RequestParams params = new RequestParams();
        params.put("userId", userId);
        params.put("longitude_bd", lng);
        params.put("latitude_bd", lat);

        HttpClient.get(Constant.Base_URL + "location.action" , params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (StringUtility.isSuccess(response)) {

                    System.out.println("定位上传成功");

                    Constant.upLocationTime = System.currentTimeMillis();

                    StringUtility.putSharedPreferences(getApplicationContext(), Constant.LOCATION_TIME,
                            Constant.LOCATION_TIME, DateTimeUtil.getCurrDateTimeStr());

                    Notification notification = getDefaultNotification(
                            "定位上传成功: " + DateTimeUtil.getCurrDateTimeStr(), false);
                    startForeground(Constant.NOTIFICATION_ID, notification);
                } else {
                    doLogin(lat, lng);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString,
                                  Throwable throwable) {

                System.out.println("定位上传失败");

                Notification notification = getDefaultNotification(
                        "定位上传失败: " + DateTimeUtil.getCurrDateTimeStr(), true);
                startForeground(Constant.NOTIFICATION_ID, notification);

//                MobclickAgent.reportError(getApplicationContext(), "定位上传失败" + Constant.userId + "\n" + throwable.toString() + "\n" + responseString);

                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                  JSONObject errorResponse) {

                System.out.println("定位上传失败");

                Notification notification = getDefaultNotification(
                        "定位上传失败: " + DateTimeUtil.getCurrDateTimeStr(), true);
                startForeground(Constant.NOTIFICATION_ID, notification);

//                MobclickAgent.reportError(getApplicationContext(), "定位上传失败" + Constant.userId + "\n" + throwable.toString() + "\n" + errorResponse.toString());

                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                  JSONArray errorResponse) {

                System.out.println("定位上传失败");

                Notification notification = getDefaultNotification(
                        "定位上传失败: " + DateTimeUtil.getCurrDateTimeStr(), true);
                startForeground(Constant.NOTIFICATION_ID, notification);

//                MobclickAgent.reportError(getApplicationContext(), "定位上传失败" + Constant.userId + "\n" + throwable.toString());

                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation.getLocType() == 61 || bdLocation.getLocType() == 161) {
                userId = StringUtility.getSharedPreferencesForString(getApplicationContext(), "id", "id");

                if (!StringUtility.isEmpty(userId)) {

                    System.out.println("upLocation: " + bdLocation.getLatitude() + "   " + bdLocation.getLongitude());

                    upLocation(bdLocation.getLatitude(), bdLocation.getLongitude());

                }
            } else {
                System.out.println("定位失败");
//                MobclickAgent.reportError(getApplicationContext(), "定位失败" + Constant.userId);
            }

        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    class MyBinder extends ProcessService.Stub{
        @Override
        public String getServiceName() throws RemoteException {
            return "I am FirstService";
        }
    }

    class  MyConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            ActivityManager activityManager = (ActivityManager) UpdateLocationService.this
                    .getSystemService(Context.ACTIVITY_SERVICE);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            UpdateLocationService.this.startService(new Intent(UpdateLocationService.this,LocationService2.class));
            //绑定FirstService
            UpdateLocationService.this.bindService(new Intent(UpdateLocationService.this,LocationService2.class),conn, Context.BIND_IMPORTANT);
        }
    }

}
