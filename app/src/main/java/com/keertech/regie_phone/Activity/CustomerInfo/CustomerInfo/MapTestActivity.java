package com.keertech.regie_phone.Activity.CustomerInfo.CustomerInfo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.keertech.regie_phone.BaseActivity;
import com.keertech.regie_phone.Constant.Constant;
import com.keertech.regie_phone.Network.HttpClient;
import com.keertech.regie_phone.R;
import com.keertech.regie_phone.Utility.KeerAlertDialog;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MapTestActivity extends BaseActivity {
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private LocationClient mLocClient;
	private MyLocationListenner myListener = new MyLocationListenner();
	private boolean isFirstLoc = true;

	GeoCoder poiSearch;

	private TextView lshkc_dz;
	private TextView confirm;
	private TextView wx_tv;

	/** 国测局 经度 */
	private double longitude = 0.0;

	/** 国测局 纬度 */
	private double latitude = 0.0;

	/** 百度 经度 */
	private double bd_longitude = 0.0;

	/** 百度 纬度 */
	private double bd_latitude = 0.0;

	private String address = "";

	int needLocation = 1;

	KeerAlertDialog pd = null;

	Handler handler = new Handler();

	Runnable runnable;

	double latitude_kh = 0.0;
	double longitude_kh = 0.0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_test);
		setToolbarTitle("经营户位置");
		showBack();

		SDKInitializer.initialize(this);

		latitude_kh = getIntent().getDoubleExtra("latitude", bd_latitude);
		longitude_kh = getIntent().getDoubleExtra("longitude", bd_longitude);

		needLocation = getIntent().getIntExtra("needLocation", 1);

		lshkc_dz = (TextView) findViewById(R.id.lshkc_dz);
		confirm = (TextView) findViewById(R.id.confirm);
		wx_tv = (TextView) findViewById(R.id.wx_tv);

		if (needLocation == 0)
			confirm.setVisibility(View.GONE);
		else
			confirm.setVisibility(View.VISIBLE);

		System.out.println("needLocation: " + needLocation);

		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (bd_longitude != 0.0 && bd_latitude != 0.0)
					convertBaidu2GCJ(bd_longitude, bd_latitude);
			}
		});

		mMapView = (MapView) findViewById(R.id.map_view);
		mBaiduMap = mMapView.getMap();

		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setLocationMode(needLocation > 0 ? LocationMode.Device_Sensors : LocationMode.Hight_Accuracy);
		// option.setLocationMode(LocationMode.Device_Sensors);
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);

		poiSearch = GeoCoder.newInstance();
		poiSearch.setOnGetGeoCodeResultListener(listener);

		pd = showKeerAlertDialog(R.string.locationing);
		pd.show();

		runnable = new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				// 在此处添加执行的代码

				if (isFirstLoc) {
					isFirstLoc = true;
					mLocClient.stop();
					mLocClient.getLocOption().setLocationMode(LocationMode.Hight_Accuracy);
					mLocClient.start();
					if (!pd.isShowing()) {
						pd = showKeerAlertDialog(R.string.locationing);
						pd.show();
					}
				}
			}
		};
		handler.postDelayed(runnable, 1000 * 60);

		mLocClient.setLocOption(option);
		mLocClient.start();

		addMakers(latitude_kh, longitude_kh);

		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		locationManager.addGpsStatusListener(statusListener);
	}

	private List<GpsSatellite> numSatelliteList = new ArrayList<GpsSatellite>();

	private final GpsStatus.Listener statusListener = new GpsStatus.Listener() {
		public void onGpsStatusChanged(int event) { // GPS状态变化时的回调，如卫星数
			LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			if (ActivityCompat.checkSelfPermission(MapTestActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				// TODO: Consider calling
				//    ActivityCompat#requestPermissions
				// here to request the missing permissions, and then overriding
				//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
				//                                          int[] grantResults)
				// to handle the case where the user grants the permission. See the documentation
				// for ActivityCompat#requestPermissions for more details.
				return;
			}
			GpsStatus status = locationManager.getGpsStatus(null); //取当前状态
			int satelliteInfo = updateGpsStatus(event, status);
            if(satelliteInfo <= 3) wx_tv.setTextColor(getResources().getColor(R.color.red));
            wx_tv.setText(satelliteInfo + "");  
        }  
    };
    
    private int updateGpsStatus(int event, GpsStatus status) {  
        int num = 0; 
        if (status == null) {  
        	num = 0;  
        } else if (event == GpsStatus.GPS_EVENT_SATELLITE_STATUS) {  
            int maxSatellites = status.getMaxSatellites();  
            Iterator<GpsSatellite> it = status.getSatellites().iterator();  
            numSatelliteList.clear();  
            int count = 0;  
            while (it.hasNext() && count <= maxSatellites) {  
                GpsSatellite s = it.next();  
                numSatelliteList.add(s);  
                count++;  
            }  
            
            num = numSatelliteList.size();
        }  
          
        return num;  
    } 

	private void addMakers(double bd_latitude, double bd_longitude) {

		final LatLng point = new LatLng(bd_latitude, bd_longitude);

		View convertView = LayoutInflater.from(this).inflate(R.layout.maker, null);
		ImageView maker_iv = (ImageView) convertView.findViewById(R.id.maker_iv);
		TextView maker_tv = (TextView) convertView.findViewById(R.id.maker_tv);

		maker_iv.setImageResource(R.drawable.lx);

		maker_tv.setVisibility(View.GONE);
		;

		Bitmap bitmap = PersonnelLocationMapActivity.convertViewToBitmap(convertView);
		BitmapDescriptor bd = BitmapDescriptorFactory.fromBitmap(bitmap);
		MarkerOptions option = new MarkerOptions().position(point).icon(bd);// .title(distance);
		// 在地图上添加Marker，并显示
		// Marker marker = (Marker)
		mBaiduMap.addOverlay(option);
		// marker.setVisible(false);
		// marker.setTitle(distance);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mLocClient.isStarted())
			mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();

		handler.removeCallbacks(runnable);
	};

	OnMapStatusChangeListener mlistener = new OnMapStatusChangeListener() {
		/**
		 * 手势操作地图，设置地图状态等操作导致地图状态开始改变。
		 * 
		 * @param status
		 *            地图状态改变开始时的地图状态
		 */
		public void onMapStatusChangeStart(MapStatus status) {
		}

		/**
		 * 地图状态变化中
		 * 
		 * @param status
		 *            当前地图状态
		 */
		public void onMapStatusChange(MapStatus status) {
		}

		/**
		 * 地图状态改变结束
		 * 
		 * @param status
		 *            地图状态改变结束后的地图状态
		 */
		public void onMapStatusChangeFinish(MapStatus status) {
			poiSearch.reverseGeoCode(
					new ReverseGeoCodeOption().location(new LatLng(status.target.latitude, status.target.longitude)));
		}
	};

	OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
		public void onGetGeoCodeResult(GeoCodeResult result) {
			if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {

			}
		}

		@Override
		public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
			// TODO Auto-generated method stub
			lshkc_dz.setText("纬度：" + result.getLocation().latitude + " " + "经度：" + result.getLocation().longitude + " "
					+ " " + "地址：" + result.getAddress());

			bd_latitude = result.getLocation().latitude;
			bd_longitude = result.getLocation().longitude;
			address = result.getAddress();
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_daoh, menu);
		return true;
	}

	String[] modes = { "公交", "驾车", "步行" };

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.baidu_map) {
			
			try {
				Intent intent = Intent.getIntent("intent://map/direction?origin=latlng:" + bd_latitude
						+ "," + bd_longitude + "|name:我的位置&destination=latlng:" + latitude_kh + ","
						+ longitude_kh + "|name:客戶位置&mode=" + "driving"
						+ "&region=武汉&src=mobileworkterrace#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
				startActivity(intent);
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void convertBaidu2GCJ(Double lng, Double lat) {
		final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
		pd.show();

		HttpClient.get("http://api.zdoz.net/bd2gcj.aspx?lat=" + lat + "&lng=" + lng, null,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
						// TODO Auto-generated method stub
						super.onSuccess(statusCode, headers, response);
						pd.dismiss();

						try {
							longitude = response.getDouble("Lng");
							latitude = response.getDouble("Lat");

							Intent intent = new Intent(Constant.MAP_RECEIVER);
							intent.putExtra("bd_latitude", bd_latitude);
							intent.putExtra("bd_longitude", bd_longitude);
							intent.putExtra("latitude", latitude);
							intent.putExtra("longitude", longitude);
							intent.putExtra("address", address);
							MapTestActivity.this.sendBroadcast(intent);

							finish();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							showNetworkError(MapTestActivity.this);
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers, String responseString,
							Throwable throwable) {
						// TODO Auto-generated method stub
						pd.dismiss();
						showNetworkError(MapTestActivity.this);
						super.onFailure(statusCode, headers, responseString, throwable);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers, Throwable throwable,
							JSONArray errorResponse) {
						// TODO Auto-generated method stub
						pd.dismiss();
						showNetworkError(MapTestActivity.this);
						super.onFailure(statusCode, headers, throwable, errorResponse);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers, Throwable throwable,
							JSONObject errorResponse) {
						// TODO Auto-generated method stub
						pd.dismiss();
						showNetworkError(MapTestActivity.this);
						super.onFailure(statusCode, headers, throwable, errorResponse);
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
			if (location == null || mMapView == null) {
				pd.dismiss();
				return;
			}
			if (location.getLocType() == 61 || location.getLocType() == 161) {
				MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
						// 此处设置开发者获取到的方向信息，顺时针0-360
						.direction(100).latitude(location.getLatitude()).longitude(location.getLongitude()).build();
				mBaiduMap.setMyLocationData(locData);

				bd_latitude = location.getLatitude();
				bd_longitude = location.getLongitude();

				LatLng start = new LatLng(bd_latitude, bd_longitude);

				poiSearch.reverseGeoCode(new ReverseGeoCodeOption()
						.location(new LatLng(location.getLatitude(), location.getLongitude())));

				if (isFirstLoc) {
					isFirstLoc = false;

					MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(start, 19);
					mBaiduMap.animateMapStatus(u);
				}

				pd.dismiss();
			}else if(location.getLocType() == 62 || location.getLocType() == 63){
				pd.dismiss();
			}
		}

		@Override
		public void onConnectHotSpotMessage(String s, int i) {

		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}
}
