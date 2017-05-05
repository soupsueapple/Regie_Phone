package com.keertech.regie_phone.Activity.XZFW;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.keertech.regie_phone.BaseActivity;
import com.keertech.regie_phone.R;

/**
 * Created by soup on 2017/5/5.
 */

public class MyLocationMapActivity extends BaseActivity{
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private LocationClient mLocClient;
    private boolean isFirstLoc = true;

    private MyLocationListenner myListener = new MyLocationListenner();
    GeoCoder poiSearch;

    private TextView myl_dz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mylocation);
        setToolbarTitle("我的位置");
        showBack();

        myl_dz = (TextView) findViewById(R.id.myl_dz);

        mMapView = (MapView) findViewById(R.id.myl_map_view);
        mBaiduMap = mMapView.getMap();

        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        final LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("是否只使用GPS定位");
        builder.setTitle("提示");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(mLocClient.isStarted()) mLocClient.stop();
                mLocClient.getLocOption().setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
                mLocClient.start();
            }
        });
        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(mLocClient.isStarted()) mLocClient.stop();
                mLocClient.getLocOption().setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
                mLocClient.start();
            }
        });
        builder.create().show();

        poiSearch = GeoCoder.newInstance();
        poiSearch.setOnGetGeoCodeResultListener(listener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mLocClient.isStarted()) mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
    };

    OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
        public void onGetGeoCodeResult(GeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {

            }
        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            // TODO Auto-generated method stub
            myl_dz.setText("纬度：" + result.getLocation().latitude + " " + "经度：" + result.getLocation().longitude + " "
                    + " " + "地址：" + result.getAddress());
        }
    };

    public class MyLocationListenner implements BDLocationListener {
        public MyLocationListenner() {
        }

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null)
                return;
            if (location.getLocType() == 61 || location.getLocType() == 161) {


                MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(100).latitude(location.getLatitude()).longitude(location.getLongitude()).build();
                mBaiduMap.setMyLocationData(locData);
                if (isFirstLoc) {
                    isFirstLoc = false;

                    LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                    MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, 18);
                    mBaiduMap.animateMapStatus(u);
                    poiSearch.reverseGeoCode(new ReverseGeoCodeOption()
                            .location(new LatLng(location.getLatitude(), location.getLongitude())));

                }
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }
}
