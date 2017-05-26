package com.keertech.regie_phone.Activity.CommonPosition;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.keertech.regie_phone.BaseActivity;
import com.keertech.regie_phone.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.keertech.regie_phone.Activity.CustomerInfo.CustomerInfo.PersonnelLocationMapActivity.convertViewToBitmap;

/**
 * Created by soup on 2017/5/26.
 */

public class CommonMapActivity extends BaseActivity{

    private MapView mMapView;
    private BaiduMap mBaiduMap;

    private String json = "";

    LinearLayout makerinfo_l;
    TextView makerinfo;

    private boolean isFirstLoc = true;
    private LocationClient mLocClient;

    ArrayList<JSONObject> datas = new ArrayList<>();
    private MyLocationListenner myListener = new MyLocationListenner();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_map);
        setToolbarTitle("市管员位置");
        showBack();

        json = getIntent().getStringExtra("json");

        makerinfo_l = (LinearLayout) findViewById(R.id.makerinfo_l);
        makerinfo = (TextView) findViewById(R.id.makerinfo);

        mMapView = (MapView) findViewById(R.id.sgywz_mapview);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);

        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        final LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(100);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("是否只使用GPS定位");
        builder.setTitle("提示");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
                mLocClient.setLocOption(option);
                mLocClient.start();
            }
        });
        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
                mLocClient.setLocOption(option);
                mLocClient.start();
            }
        });
        builder.create().show();

        try {
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);

                String img = object.getString("img");

                if (img.equals("red")) {
                    object.put("onLine", false);
                } else {
                    object.put("onLine", true);
                }

                object.put("show", true);

                datas.add(object);
            }

            addMakers();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addMakers(){
        for(JSONObject object: datas){
            try {
                String bname = object.getString("name");
                final String deptName = object.getString("simplyname");
                final String lastLocatetime = object.getString("lastlocatetime");
                double longitudebd = object.getDouble("lng");
                double latitudebd = object.getDouble("lat");
                boolean onLine = object.getBoolean("onLine");

                final LatLng point = new LatLng(latitudebd, longitudebd);

                View convertView = LayoutInflater.from(this).inflate(R.layout.maker, null);
                ImageView maker_iv = (ImageView)convertView.findViewById(R.id.maker_iv);
                TextView maker_tv = (TextView) convertView.findViewById(R.id.maker_tv);

                if(onLine){
                    maker_iv.setImageResource(R.drawable.zx);
                }else{
                    maker_iv.setImageResource(R.drawable.lx);
                }

                maker_tv.setText(bname);

                Bitmap bitmap = convertViewToBitmap(convertView);
                BitmapDescriptor bd = BitmapDescriptorFactory.fromBitmap(bitmap);
                MarkerOptions option = new MarkerOptions()
                        .position(point)
                        .icon(bd).title(deptName + "\n" + lastLocatetime);
                //在地图上添加Marker，并显示
                Marker marker =(Marker) mBaiduMap.addOverlay(option);
                marker.setTitle(deptName + "\n" + lastLocatetime);

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker arg0) {
                // TODO Auto-generated method stub
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(arg0.getPosition(), 15);
                mBaiduMap.animateMapStatus(u);
                makerinfo.setText(arg0.getTitle());
                makerinfo_l.setVisibility(View.VISIBLE);
                return false;
            }
        });
    }

    /**
     * 定位SDK监听函数
     */
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
                    MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, 12);
                    mBaiduMap.animateMapStatus(u);

                }
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }
}
