package com.keertech.regie_phone.Activity.CustomerInfo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapLoadedCallback;
import com.baidu.mapapi.map.BaiduMap.OnMapTouchListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.keertech.regie_phone.BaseActivity;
import com.keertech.regie_phone.R;
import com.keertech.regie_phone.Utility.ClusterItem;
import com.keertech.regie_phone.Utility.ClusterManager;
import com.keertech.regie_phone.Utility.StringUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomerMapActivity extends BaseActivity implements OnMapLoadedCallback {

	private MapView mMapView;
	private BaiduMap mBaiduMap;

	private boolean isFirstLoc = true;
	private LocationClient mLocClient;
	private MyLocationListenner myListener = new MyLocationListenner();

	private ArrayList<JSONObject> datas;

	TextView makerinfo;
	LinearLayout makerinfo_l;

	private ClusterManager<MyItem> mClusterManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setToolbarTitle("客户位置");
		setContentView(R.layout.salesmap);
		showBack();

		SDKInitializer.initialize(this);

		datas = new ArrayList<JSONObject>();

		mMapView = (MapView) findViewById(R.id.mapview);
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setMyLocationEnabled(true);

		makerinfo_l = (LinearLayout) findViewById(R.id.makerinfo_l);
		makerinfo = (TextView) findViewById(R.id.makerinfo);

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
				option.setLocationMode(LocationMode.Device_Sensors);
				mLocClient.setLocOption(option);
				mLocClient.start();
			}
		});
		builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				option.setLocationMode(LocationMode.Hight_Accuracy);
				mLocClient.setLocOption(option);
				mLocClient.start();
			}
		});
		builder.create().show();

		mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
			@Override
			public void onMapClick(LatLng latLng) {
				makerinfo_l.setVisibility(View.GONE);
			}

			@Override
			public boolean onMapPoiClick(MapPoi mapPoi) {
				return false;
			}
		});

		mBaiduMap.setOnMapTouchListener(new OnMapTouchListener() {
			/**
			 * 当用户触摸地图时回调函数
			 *
			 * @param event
			 *            触摸事件
			 */
			public void onTouch(MotionEvent event) {
				makerinfo_l.setVisibility(View.GONE);
			}
		});

		mClusterManager = new ClusterManager<MyItem>(this, mBaiduMap);
	}

	private String fileRead(String fileName) {

		try {
			File file = new File(fileName);

			BufferedReader bf = new BufferedReader(new FileReader(file));

			String content = "";
			StringBuilder sb = new StringBuilder();

			while (content != null) {
				content = bf.readLine();

				if (content == null) {
					break;
				}

				sb.append(content.trim());
			}

			bf.close();
			return sb.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "";
	}

	private void addMakers(LatLng ll) throws JSONException {

		for (JSONObject object : datas) {
			try {
				String bname = object.getString("shopName");
				final String distance = object.getString("distance");
				double longitudebd = object.getDouble("bd_longitude");
				double latitudebd = object.getDouble("bd_latitude");

				if (longitudebd > 0 && latitudebd > 0) {
					final LatLng point = new LatLng(latitudebd, longitudebd);

					// View convertView =
					// LayoutInflater.from(this).inflate(R.layout.maker, null);
					// ImageView maker_iv = (ImageView)
					// convertView.findViewById(R.id.maker_iv);
					// TextView maker_tv = (TextView)
					// convertView.findViewById(R.id.maker_tv);
					// maker_tv.setVisibility(View.GONE);
					//
					// maker_iv.setImageResource(R.drawable.zx);
					//
					// maker_tv.setText(bname);
					//
					// Bitmap bitmap =
					// PersonnelLocationMapActivity.convertViewToBitmap(convertView);
					// BitmapDescriptor bd =
					// BitmapDescriptorFactory.fromBitmap(bitmap);
					// MarkerOptions option = new
					// MarkerOptions().position(point).icon(bd).title(distance);
					// // 在地图上添加Marker，并显示
					// Marker marker = (Marker) mBaiduMap.addOverlay(option);
					// // marker.setVisible(false);
					// marker.setTitle(distance);
					// markers.add(marker);

					List<MyItem> items = new ArrayList<MyItem>();
					items.add(new MyItem(point, object));
					mClusterManager.addItems(items);
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		mBaiduMap.setOnMapStatusChangeListener(mClusterManager);

		// mClusterManager.setOnClusterClickListener(new
		// ClusterManager.OnClusterClickListener<MyItem>() {
		// @Override
		// public boolean onClusterClick(Cluster<MyItem> cluster) {
		// Toast.makeText(CustomerMapActivity.this, "cluster",
		// Toast.LENGTH_SHORT).show();
		//
		// return false;
		// }
		// });
		//
		// mClusterManager.setOnClusterItemClickListener(new
		// ClusterManager.OnClusterItemClickListener<MyItem>() {
		// @Override
		// public boolean onClusterItemClick(MyItem item) {
		//// makerinfo_l.setVisibility(View.VISIBLE);
		// Toast.makeText(CustomerMapActivity.this, "Test",
		// Toast.LENGTH_SHORT).show();
		// return false;
		// }
		// });

		MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, 12);
		mBaiduMap.animateMapStatus(u);

		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker arg0) {
				// Toast.makeText(CustomerMapActivity.this,
				// arg0.getPosition().longitude+"-"+arg0.getPosition().latitude,
				// Toast.LENGTH_SHORT).show();

				JSONObject jsonObject = null;

				for (JSONObject object : datas) {
					double longitudebd = object.optDouble("bd_longitude");
					double latitudebd = object.optDouble("bd_latitude");

					if (longitudebd == arg0.getPosition().longitude && latitudebd == arg0.getPosition().latitude) {
						jsonObject = object;
					}
				}

				if (jsonObject != null) {
					
					MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(arg0.getPosition(), 18);
					mBaiduMap.animateMapStatus(u);
					
					String contactphone = "";
					if(StringUtility.notObjEmpty(jsonObject, "contactphone")) contactphone = jsonObject.optString("contactphone");
					
					makerinfo.setText("专卖证号：" + jsonObject.optString("liceNo") +"\n"+"店名：" + jsonObject.optString("shopName")+"\n"+"经营人：" + jsonObject.optString("chargerName")+"\n"+"电话：" + contactphone);
					makerinfo_l.setVisibility(View.VISIBLE);
				}

				return false;
			}
		});
	}

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

					try {
						JSONArray array = new JSONArray(fileRead(getIntent().getStringExtra("fileName")));

						for (int i = 0; i < array.length(); i++) {
							JSONObject object = array.getJSONObject(i);

							if (StringUtility.notObjEmpty(object, "bd_longitude")
									&& StringUtility.notObjEmpty(object, "bd_latitude")) {
								double longitudebd = object.getDouble("bd_longitude");
								double latitudebd = object.getDouble("bd_latitude");

								LatLng end = new LatLng(latitudebd, longitudebd);

								int distance = (int) DistanceUtil.getDistance(ll, end);

								object.put("distance", "距离 " + distance + " 米");

								datas.add(object);
							}

						}

						addMakers(ll);

					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}
			}
		}

		@Override
		public void onConnectHotSpotMessage(String s, int i) {

		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	/**
	 * 每个Marker点，包含Marker点坐标以及图标
	 */
	public class MyItem implements ClusterItem {
		private final LatLng mPosition;
		private JSONObject object;

		public MyItem(LatLng latLng, JSONObject object) {
			mPosition = latLng;
			this.object = object;
		}

		@Override
		public LatLng getPosition() {
			return mPosition;
		}

		@Override
		public BitmapDescriptor getBitmapDescriptor() {
			return BitmapDescriptorFactory.fromResource(R.drawable.lx);
		}
	}

	@Override
	public void onMapLoaded() {
		// TODO Auto-generated method stub

	}

}
