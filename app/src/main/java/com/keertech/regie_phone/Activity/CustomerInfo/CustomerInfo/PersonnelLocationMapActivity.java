package com.keertech.regie_phone.Activity.CustomerInfo.CustomerInfo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapTouchListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.keertech.regie_phone.Adapter.SetAdapter;
import com.keertech.regie_phone.BaseActivity;
import com.keertech.regie_phone.R;
import com.keertech.regie_phone.Utility.StringUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PersonnelLocationMapActivity extends BaseActivity {

	private TextView zxrs_tv;
	private TextView lxrs_tv;

	private ListView sgywz_list;

	private MapView mMapView;
	private BaiduMap mBaiduMap;
	// private LocationClient mLocClient;

	private String json = "";

	LinearLayout makerinfo_l;
	TextView makerinfo;

	private boolean isFirstLoc = true;
	private LocationClient mLocClient;
	private MyLocationListenner myListener = new MyLocationListenner();

	ArrayList<JSONObject> datas;
	ArrayList<Marker> markers;

	DataAdapter dataAdapter;
	
	int zxrs = 0;
	int lxrs = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personnel_location_activity);
		setToolbarTitle("市管员位置");
		showBack();

		SDKInitializer.initialize(this);

		json = getIntent().getStringExtra("json");

		zxrs_tv = (TextView) findViewById(R.id.zxrs_tv);
		lxrs_tv = (TextView) findViewById(R.id.lxrs_tv);
		sgywz_list = (ListView) findViewById(R.id.sgywz_list);

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

		datas = new ArrayList<JSONObject>();
		markers = new ArrayList<Marker>();

		dataAdapter = new DataAdapter(this, datas);
		sgywz_list.setAdapter(dataAdapter);

		try {
			if (json.length() > 0) {
				JSONObject response = new JSONObject(json);

				if (StringUtility.isSuccess(response)) {
					String messageSting = response.getString("message");
					System.out.println("messageSting : " + messageSting);
					JSONObject message = new JSONObject(messageSting);

					if (StringUtility.isSuccess(message)) {

						JSONArray jsonArray = message.getJSONArray("data");

						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject object = jsonArray.getJSONObject(i);
//							String lastLocatetime = object.getString("lastlocatetime");
//							java.util.Date date = DateTimeUtil.getFormatDate(lastLocatetime);
//							long currentTimeMillis = System.currentTimeMillis() - date.getTime();
//							System.out.println("currentTimeMillis : "+currentTimeMillis);
							
							String img = object.getString("img");
							
							if (img.equals("red")) {
								lxrs += 1;
								object.put("onLine", false);
							} else {
								zxrs += 1;
								object.put("onLine", true);
							}

							object.put("show", true);

							datas.add(object);
						}

						dataAdapter.setObjects(datas);
						dataAdapter.notifyDataSetChanged();
						
						addMakers();

						zxrs_tv.setText(zxrs + "");
						lxrs_tv.setText(lxrs+ "");
					} else {
						showToast(message.getString("message"), PersonnelLocationMapActivity.this);
					}
				} else {
					showToast(response.getString("message"), PersonnelLocationMapActivity.this);
				}
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		

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
//				InfoWindow mInfoWindow = new InfoWindow(bd, point, -47, new OnInfoWindowClickListener() {
//
//					@Override
//					public void onInfoWindowClick() {
//						// TODO Auto-generated method stub
//						MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(point, 12);
//						mBaiduMap.animateMapStatus(u);
//						makerinfo.setText(deptName + "/n" + lastLocatetime);
//						makerinfo_l.setVisibility(View.VISIBLE);
//					}
//				});
				MarkerOptions option = new MarkerOptions()
				    .position(point)  
				    .icon(bd).title(deptName + "\n" + lastLocatetime);  
				//在地图上添加Marker，并显示  
				Marker marker =(Marker) mBaiduMap.addOverlay(option);
//				marker.setVisible(false);
				marker.setTitle(deptName + "\n" + lastLocatetime);
				markers.add(marker);
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			
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

	public static Bitmap convertViewToBitmap(View view) {
		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();

		return bitmap;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
	};

	class DataAdapter extends SetAdapter {

		private ArrayList<JSONObject> datas;
		private LayoutInflater inflater;

		public DataAdapter(Context context, ArrayList<JSONObject> datas) {
			super(context, datas);
			this.datas = datas;
			inflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup viewGroup) {
			// TODO Auto-generated method stub
			ViewHolder holder;

			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.sgywz_list_item, null);

				holder.sgywz_item_cb = (CheckBox) convertView.findViewById(R.id.sgywz_item_cb);
				holder.textView1 = (TextView) convertView.findViewById(R.id.sgywz_item_tv1);
				holder.textView2 = (ImageView) convertView.findViewById(R.id.sgywz_item_tv2);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			try {
				final int index = position;
				
				final JSONObject object = datas.get(position);
				String bname = object.getString("name");
				boolean onLine = object.getBoolean("onLine");

				holder.textView1.setText(bname);
				
				if(onLine){
					holder.textView2.setImageResource(R.drawable.zx);
				}else{
					holder.textView2.setImageResource(R.drawable.lx);
				}

				holder.sgywz_item_cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						// TODO Auto-generated method stub
						Marker marker = markers.get(index);

						if (isChecked) {
							marker.setVisible(true);
						} else {
							marker.setVisible(false);
						}

					}
				});
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return convertView;
		}

		final class ViewHolder {
			CheckBox sgywz_item_cb;
			TextView textView1;
			ImageView textView2;
		}

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

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

}
