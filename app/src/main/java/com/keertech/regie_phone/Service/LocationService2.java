package com.keertech.regie_phone.Service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.keertech.regie_phone.ProcessService;

/**
 * Created by soup on 2017/5/31.
 */

public class LocationService2 extends Service{
    private MyBinder binder;
    private MyConn conn;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        binder = new MyBinder();
        if(conn==null) conn = new MyConn();
    }

    @Override
    public void onTrimMemory(int level){

    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        System.out.println("LocationService2 onStart");

//		Notification.Builder builder = new Notification.Builder(this);
//		builder.setSmallIcon(R.mipmap.ic_launcher);
//		startForeground(250, builder.build());

        LocationService2.this.bindService(new Intent(this,UpdateLocationService.class),conn, Context.BIND_IMPORTANT);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub

        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Intent intent = new Intent(this, UpdateLocationService.class);
        startService(intent);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return binder;
    }

    class MyBinder extends ProcessService.Stub{

        @Override
        public String getServiceName() throws RemoteException {
            return "I am SecondService";
        }
    }

    class  MyConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i("Info","与FirtService连接成功"+LocationService2.this.getApplicationInfo().processName);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            // 启动FirstService
            LocationService2.this.startService(new Intent(LocationService2.this,UpdateLocationService.class));
            //绑定FirstService
            LocationService2.this.bindService(new Intent(LocationService2.this,UpdateLocationService.class),conn, Context.BIND_IMPORTANT);
        }
    }
}
