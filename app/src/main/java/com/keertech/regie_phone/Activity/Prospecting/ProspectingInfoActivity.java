package com.keertech.regie_phone.Activity.Prospecting;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.keertech.regie_phone.Activity.CustomerInfo.CustomerInfo.MapTestActivity;
import com.keertech.regie_phone.BaseActivity;
import com.keertech.regie_phone.BroadcastReceiver.MapBroadcastReceiver;
import com.keertech.regie_phone.Constant.Constant;
import com.keertech.regie_phone.Listener.ViewClickVibrate;
import com.keertech.regie_phone.Network.HttpClient;
import com.keertech.regie_phone.Observer.MapObserver;
import com.keertech.regie_phone.R;
import com.keertech.regie_phone.Utility.KeerAlertDialog;
import com.keertech.regie_phone.Utility.StringUtility;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

/**
 * Created by soup on 2017/5/27.
 */

public class ProspectingInfoActivity extends BaseActivity{

    private TextView saveTv;

    private String id = "";

    private double lat = 0.0;
    private double lng = 0.0;
    private String applyState = "";

    private static MapObserver mapObserver;

    private void assignViews() {
        saveTv = (TextView) findViewById(R.id.save_tv);
        saveTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                if(lat != 0 && lng  !=0 ) sendData();
                else showToast("请先定位", ProspectingInfoActivity.this);
            }
        });

        id = getIntent().getStringExtra("id");

        mapObserver = new MapObserver(){
            @Override
            public void mapObserver(double bd_latitude, double bd_longitude, double latitude, double longitude, String address) {
                super.mapObserver(bd_latitude, bd_longitude, latitude, longitude, address);
                lat = latitude;
                lng = longitude;
            }
        };

        MapBroadcastReceiver.registerSwitchPluginReceiver(this);
        MapBroadcastReceiver.registerObserver(mapObserver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MapBroadcastReceiver.unRegisterSwitchPluginReceiver(this);
        MapBroadcastReceiver.removeRegisterObserver(mapObserver);
    }

    private void sendData(){
        DecimalFormat df = new DecimalFormat("#.0000000000");

        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams params = new RequestParams();
        params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\""+ Constant.MWB_Base_URL+"customerInfo!updateCoordinate.action?_query.rowId="+id+"&_query.longitude="+df.format(lng)+"&_query.latitude="+df.format(lat)+"\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

        HttpClient.post(Constant.EXEC, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                pd.dismiss();
                try {
                    if (StringUtility.isSuccess(response)) {
                        String messageSting = response.getString("message");

                        JSONObject message = new JSONObject(messageSting);

                        if (StringUtility.isSuccess(message)) {

                            finish();

                        } else {
                            showToast(response.getString("message"), ProspectingInfoActivity.this);
                        }
                    } else {
                        showToast(response.getString("message"), ProspectingInfoActivity.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(ProspectingInfoActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(ProspectingInfoActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(ProspectingInfoActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prospecting_info);
        setToolbarTitle("编辑信息");
        showBack();

        assignViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.prospecting_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_map){
            Intent intent = new Intent(ProspectingInfoActivity.this, MapTestActivity.class);
            startActivity(intent);

            return true;
        }

        if(id == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
