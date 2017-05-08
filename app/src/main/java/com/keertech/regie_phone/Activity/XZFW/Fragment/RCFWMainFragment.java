package com.keertech.regie_phone.Activity.XZFW.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.keertech.regie_phone.Activity.XZFW.RCFW.ServiceInfoAcitvity;
import com.keertech.regie_phone.BaseFragment;
import com.keertech.regie_phone.Constant.Constant;
import com.keertech.regie_phone.Listener.ViewClickVibrate;
import com.keertech.regie_phone.Network.HttpClient;
import com.keertech.regie_phone.R;
import com.keertech.regie_phone.Utility.KeerAlertDialog;
import com.keertech.regie_phone.Utility.StringUtility;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.keertech.regie_phone.R.id.recycler_view;
import static com.keertech.regie_phone.R.id.wsckh_cb;
import static com.keertech.regie_phone.R.id.yc_cb;

/**
 * Created by soup on 2017/5/3.
 */

public class RCFWMainFragment extends BaseFragment{

    private LinearLayout linearLayout2;
    private EditText xkzhEt;
    private EditText dmEt;
    private LinearLayout linearLayout3;
    private EditText jyrEt;
    private LinearLayout sqLl;
    private TextView sqTv;
    private TextView wxcNumTv;
    private CheckBox wsckhCb;
    private CheckBox ycCb;
    private RecyclerView recyclerView;
    private FloatingActionButton searchFab;

    ArrayList<JSONObject> allDatas = new ArrayList<>();
    ArrayList<JSONObject> datas = new ArrayList<>();
    private ArrayList<JSONObject> wxcdatas = new ArrayList<>();
    private ArrayList<JSONObject> ycdatas = new ArrayList<>();

    RecyclerAdapter recyclerAdapter;

    public LocationClient mLocationClient;
    public MyLocationListener mMyLocationListener;

    String[] communityNames;
    String[] communityIDs;

    String currentCommunityId = "";

    private double latitude = 0.0;
    private double longitude = 0.0;

    private int drafts ;

    boolean isMc;
    boolean isXkz;
    boolean isJyr;
    boolean isXccs;
    boolean isJl;
    boolean isSheQu;

    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            wxcNumTv.setText((String)msg.obj);
            recyclerAdapter.notifyDataSetChanged();
        }
    };

    private void assignViews(View convertView) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        linearLayout2 = (LinearLayout) convertView.findViewById(R.id.linearLayout2);
        xkzhEt = (EditText) convertView.findViewById(R.id.xkzh_et);
        dmEt = (EditText) convertView.findViewById(R.id.dm_et);
        linearLayout3 = (LinearLayout) convertView.findViewById(R.id.linearLayout3);
        jyrEt = (EditText) convertView.findViewById(R.id.jyr_et);
        sqLl = (LinearLayout) convertView.findViewById(R.id.sq_ll);
        sqTv = (TextView) convertView.findViewById(R.id.sq_tv);
        wxcNumTv = (TextView) convertView.findViewById(R.id.wxc_num_tv);
        wsckhCb = (CheckBox) convertView.findViewById(wsckh_cb);
        ycCb = (CheckBox) convertView.findViewById(yc_cb);
        recyclerView = (RecyclerView) convertView.findViewById(recycler_view);
        searchFab = (FloatingActionButton) convertView.findViewById(R.id.search_fab);

        recyclerAdapter = new RecyclerAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerAdapter);

        wsckhCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    datas = wxcdatas;
                    recyclerAdapter.notifyDataSetChanged();
                }else{
                    datas = allDatas;
                    recyclerAdapter.notifyDataSetChanged();
                }
            }
        });

        ycCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    datas = ycdatas;
                    recyclerAdapter.notifyDataSetChanged();
                }else{
                    datas = allDatas;
                    recyclerAdapter.notifyDataSetChanged();
                }
            }
        });

        sqLl.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                showItemDialog();
            }
        });

        searchFab.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                loadData(xkzhEt.getText().toString(), dmEt.getText().toString(), jyrEt.getText().toString());
            }
        });

        mLocationClient = new LocationClient(getContext());
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);

        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        mLocationClient.setLocOption(option);

        loadingCommunity();

        if(StringUtility.getSharedPreferencesForBoolean(getActivity(), "isOutShopDraft", "draft")){
            Intent intent = new Intent(RCFWMainFragment.this.getActivity(), ServiceInfoAcitvity.class);
            RCFWMainFragment.this.startActivity(intent);
        }else {
            location();
        }

    }

    public void location(){
        drafts = 0;

        mLocationClient.getLocOption().setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        mLocationClient.start();
    }

    public void locationWithGPS(){
        drafts = 0;

        mLocationClient.getLocOption().setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
        mLocationClient.start();
    }

    private void loadData(String liceNo, String shopName, String chargerName){

        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        String other = liceNo.length() > 0 ? "&_query.liceNo=" + liceNo : "";
        other = other + (shopName.length() > 0 ? "&_query.shopName=" + shopName : "");
        other = other + (chargerName.length() > 0 ? "&_query.chargerName=" + chargerName : "");
        other = other + (currentCommunityId.length() > 0 ? "&_query.communityId=" + currentCommunityId : "");

        RequestParams params = new RequestParams();
        params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\""+ Constant.MWB_Base_URL + "marketInspect!customerList.action" +"?privilegeFlag=VIEW&start="+0+"&limit=300&_query.lng="+longitude+"&_query.lat="+latitude+other+"\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

        HttpClient.post(Constant.EXEC, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                pd.dismiss();

                datas.clear();
                wxcdatas.clear();
                ycdatas.clear();

                try {
                    if (StringUtility.isSuccess(response)) {
                        String messageSting = response.getString("message");

                        JSONObject message = new JSONObject(messageSting);

                        if (StringUtility.isSuccess(message)) {

                            JSONArray data = message.getJSONArray("data");


                            for (int i = 0; i < data.length(); i++) {
                                JSONObject object = data.getJSONObject(i);


                                String customerId = object.getString("customerId");
                                String s = StringUtility.getSharedPreferencesForString(getActivity(), "outShop", customerId + "");

                                if(s.length() > 0){
                                    drafts += 1;
                                }

                                allDatas.add(object);

                                int num = object.getInt("num");
                                int targetNum = object.getInt("targetNum");

                                if ( num < targetNum ) {
                                    wxcdatas.add(object);
                                }

                                final int zjAbnormalFlag = object.getInt("zjAbnormalFlag");
                                final int jyAbnormalFlag = object.getInt("jyAbnormalFlag");
                                final int apcdAbnormalFlag = object.getInt("apcdAbnormalFlag");
                                final int dsfAbnormalFlag = object.getInt("dsfAbnormalFlag");

                                if (zjAbnormalFlag == 1 || jyAbnormalFlag == 1 || apcdAbnormalFlag == 1 || dsfAbnormalFlag == 1) {
                                    ycdatas.add(object);
                                }

                            }


                            datas = allDatas;

                            Collections.sort(datas, comparator_jl2);
                            Collections.sort(wxcdatas, comparator_jl2);
                            Collections.sort(ycdatas, comparator_jl2);

                            int totalCount = message.getInt("totalCount");
                            int inspectNum = message.getInt("inspectNum");


                            Message msg = Message.obtain();
                            msg.obj = totalCount - inspectNum + "";
                            handler.sendMessage(msg);
                        }else{
                            showToast(response.getString("message"), getActivity());
                        }
                    } else {
                        showToast(response.getString("message"), getActivity());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                RCFWMainFragment.this.showNetworkError(getActivity());
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                RCFWMainFragment.this.showNetworkError(getActivity());
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                RCFWMainFragment.this.showNetworkError(getActivity());
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

    }

    private void loadingCommunity(){
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams requestParams = new RequestParams();
        requestParams.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\""+Constant.MWB_Base_URL+"communityInfo!searchBeans.action?privilegeFlag=VIEW\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

        HttpClient.post(Constant.EXEC, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                System.out.println("response: " + response.toString());
                pd.dismiss();

                try {
                    if (StringUtility.isSuccess(response)) {
                        String messageSting = response.getString("message");

                        JSONObject message = new JSONObject(messageSting);

                        if (StringUtility.isSuccess(message)) {

                            JSONArray data = message.optJSONArray("data");

                            communityNames = new String[data.length()];
                            communityIDs = new String[data.length()];

                            for(int i=0;i<data.length();i++){
                                JSONObject object = data.optJSONObject(i);

                                String communityName = object.optString("communityName");
                                String id = object.getString("id");

                                communityNames[i] = communityName;
                                communityIDs[i] = id;
                            }


                        } else {
                            showToast(response.getString("message"), getActivity());
                        }
                    } else {
                        showToast(response.getString("message"), getActivity());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                RCFWMainFragment.this.showNetworkError(getActivity());
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                RCFWMainFragment.this.showNetworkError(getActivity());
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                RCFWMainFragment.this.showNetworkError(getActivity());
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void showItemDialog(){

        if(communityNames == null || communityIDs == null) return;

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).setTitle("选择社区").setItems(communityNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sqTv.setText(communityNames[which]);
                currentCommunityId = communityIDs[which];
            }
        }).create();
        alertDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();

        if(Constant.isRefreshXCJL){
            Constant.isRefreshXCJL = false;

            drafts = 0;

            location();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mLocationClient.isStarted()) mLocationClient.stop();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View convertView = inflater.inflate(R.layout.fragment_xzfw_rcfw, null);

        assignViews(convertView);

        return convertView;
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>{

        @Override
        public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerHolder holder = new RecyclerHolder(LayoutInflater.from(RCFWMainFragment.this.getActivity()).inflate(R.layout.xzfw_rcfw_recyclerview_item, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final RecyclerHolder holder, int position) {
            final JSONObject object = datas.get(position);

            try {
                final String customerId = object.getString("customerId");
                final String liceNo = object.getString("liceNo");
                String shopName = object.getString("shopName");
                String chargerName = object.getString("chargerName");
                int num = object.getInt("num");
                int targetNum = object.getInt("targetNum");
                String communityName = object.optString("communityName");

                double bdlongitude = object.getDouble("bdlongitude");
                double bdlatitude = object.getDouble("bdlatitude");

                final int zjAbnormalFlag = object.getInt("zjAbnormalFlag");
                final int jyAbnormalFlag = object.getInt("jyAbnormalFlag");
                final int apcdAbnormalFlag = object.getInt("apcdAbnormalFlag");
                final int dsfAbnormalFlag = object.getInt("dsfAbnormalFlag");
                final int lotteryAbnormalFlag = object.getInt("lotteryAbnormalFlag");

                final String inspectWeekId = object.getString("inspectWeekId");

                if(num < targetNum) holder.csTv.setTextColor(getActivity().getResources().getColor(R.color.red));
                else holder.csTv.setTextColor(getActivity().getResources().getColor(R.color.color_black));

                Integer distance = null;

                if(StringUtility.notObjEmpty(object, "distance")) distance = object.getInt("distance");

                holder.xkzhTv.setText(liceNo);
                holder.dmTv.setText(shopName);
                holder.xmTv.setText(chargerName);
                holder.sqTv.setText(communityName);
                holder.csTv.setText(num + "/" + targetNum);

                holder.jd_tv.setOnClickListener(new ViewClickVibrate(){

                    @Override
                    public void onClick(View view) {
                        super.onClick(view);

                        if (zjAbnormalFlag == 1 || jyAbnormalFlag == 1 || apcdAbnormalFlag == 1 || dsfAbnormalFlag == 1 || lotteryAbnormalFlag == 1) {

                            try {
                                loadyc(object);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else {

                            Intent intent = new Intent(getActivity(), ServiceInfoAcitvity.class);
                            intent.putExtra("customerId", customerId);
                            intent.putExtra("liceNo", liceNo);
                            intent.putExtra("latitude", latitude);
                            intent.putExtra("longitude", longitude);
                            intent.putExtra("zjAbnormalFlag", zjAbnormalFlag);
                            intent.putExtra("jyAbnormalFlag", jyAbnormalFlag);
                            intent.putExtra("apcdAbnormalFlag", apcdAbnormalFlag);
                            intent.putExtra("dsfAbnormalFlag", dsfAbnormalFlag);
                            intent.putExtra("lotteryAbnormalFlag", lotteryAbnormalFlag);
                            intent.putExtra("inspectWeekId", inspectWeekId);
                            RCFWMainFragment.this.startActivity(intent);

                        }
                    }
                });


                if(distance == null) holder.jlTv.setText("--"); else  holder.jlTv.setText(distance + "米");

                if (zjAbnormalFlag == 1 || jyAbnormalFlag == 1 || apcdAbnormalFlag == 1 || dsfAbnormalFlag == 1 || lotteryAbnormalFlag == 1) {
                    holder.ycTv.setBackgroundResource(R.drawable.warning);
                    holder.ycTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                loadyc2(object);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    holder.ycTv.setBackgroundResource(R.drawable.unwarning);
                    holder.ycTv.setOnClickListener(null);
                }

                if (bdlongitude == 0 && bdlatitude == 0) {
                    holder.ycTv.setTextColor(getResources().getColor(R.color.red));
                } else {
                    holder.ycTv.setTextColor(getResources().getColor(R.color.color_black));

                }

                String s = StringUtility.getSharedPreferencesForString(RCFWMainFragment.this.getActivity(), "outShop", customerId + "");

                if ((bdlongitude == 0 && bdlatitude == 0) || distance > Constant.INTO_SHOP || distance == null){

//                    holder.jd_tv.setVisibility(View.GONE);

                    holder.xkzhTv.setTextColor(getResources().getColor(R.color.red));

                }else{

                    if (drafts > 0){
                        if (s.length() > 0){

//                            holder.jd_tv.setVisibility(View.VISIBLE);

                            holder.xkzhTv.setTextColor(getResources().getColor(R.color.color_black));

                        }else{

//                            holder.jd_tv.setVisibility(View.GONE);

                            holder.xkzhTv.setTextColor(getResources().getColor(R.color.red));

                        }
                    }else{

//                        holder.jd_tv.setVisibility(View.VISIBLE);

                        holder.xkzhTv.setTextColor(getResources().getColor(R.color.color_black));

                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        private void loadyc(JSONObject object) throws JSONException {
            final String customerId = object.getString("customerId");
            final String liceNo = object.getString("liceNo");

            final int zjAbnormalFlag = object.getInt("zjAbnormalFlag");
            final int jyAbnormalFlag = object.getInt("jyAbnormalFlag");
            final int apcdAbnormalFlag = object.getInt("apcdAbnormalFlag");
            final int dsfAbnormalFlag = object.getInt("dsfAbnormalFlag");
            final int lotteryAbnormalFlag = object.getInt("lotteryAbnormalFlag");

            final String inspectWeekId = object.getString("inspectWeekId");

            final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
            pd.dismiss();

            RequestParams params = new RequestParams();
            params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\""+Constant.MWB_Base_URL+"marketInspect!customerAbnormal.action?privilegeFlag=VIEW&_query.customerId="+customerId+"\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

            HttpClient.post(Constant.EXEC, params, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    System.out.println("response: "+response.toString());
                    pd.dismiss();

                    try {
                        if (StringUtility.isSuccess(response)) {
                            String messageSting = response.getString("message");

                            JSONObject message = new JSONObject(messageSting);

                            if (StringUtility.isSuccess(message)) {

                                final String zjAbnormal = message.getString("zjAbnormal");
                                final String jyAbnormal = message.getString("jyAbnormal");
                                final String apcdAbnormal = message.getString("apcdAbnormal");
                                final String dsfAbnormal = message.getString("dsfAbnormal");
                                final String lotteryAbnormal = message.getString("lotteryAbnormal");

                                StringBuffer buffer = new StringBuffer("");
                                if(zjAbnormal.length() > 0)buffer.append("证件异常：\n"+zjAbnormal+"\n");
                                if(jyAbnormal.length() > 0)buffer.append("经营异常：\n"+jyAbnormal+"\n");
                                if(apcdAbnormal.length() > 0)buffer.append("APCD异常：\n"+apcdAbnormal+"\n");
                                if(dsfAbnormal.length() > 0)buffer.append("第三方调查异常：\n"+dsfAbnormal+"\n");
                                if(lotteryAbnormal.length() > 0)buffer.append("随机检查异常：\n"+lotteryAbnormal+"\n");

                                AlertDialog.Builder builder = new AlertDialog.Builder(RCFWMainFragment.this.getActivity());
                                builder.setMessage(buffer.toString());
                                builder.setTitle("异常信息");
                                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent intent = new Intent(getActivity(), ServiceInfoAcitvity.class);
                                        intent.putExtra("customerId", customerId);
                                        intent.putExtra("liceNo", liceNo);
                                        intent.putExtra("latitude", latitude);
                                        intent.putExtra("longitude", longitude);
                                        intent.putExtra("zjAbnormalFlag", zjAbnormalFlag);
                                        intent.putExtra("jyAbnormalFlag", jyAbnormalFlag);
                                        intent.putExtra("apcdAbnormalFlag", apcdAbnormalFlag);
                                        intent.putExtra("dsfAbnormalFlag", dsfAbnormalFlag);
                                        intent.putExtra("lotteryAbnormalFlag", lotteryAbnormalFlag);
                                        intent.putExtra("inspectWeekId", inspectWeekId);

                                        intent.putExtra("zjAbnormal", zjAbnormal);
                                        intent.putExtra("jyAbnormal", jyAbnormal);
                                        intent.putExtra("apcdAbnormal", apcdAbnormal);
                                        intent.putExtra("dsfAbnormal", dsfAbnormal);
                                        intent.putExtra("lotteryAbnormal", lotteryAbnormal);
                                        intent.putExtra("inspectWeekId", inspectWeekId);

                                        RCFWMainFragment.this.startActivity(intent);
                                    }
                                });
                                builder.create().show();

                            }else{
                                showToast(response.getString("message"), getActivity());
                            }
                        } else {
                            showToast(response.getString("message"), getActivity());
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    pd.dismiss();
                    showNetworkError(RCFWMainFragment.this.getActivity());
                    super.onFailure(statusCode, headers, responseString, throwable);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    pd.dismiss();
                    showNetworkError(RCFWMainFragment.this.getActivity());
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    pd.dismiss();
                    showNetworkError(RCFWMainFragment.this.getActivity());
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });
        }

        private void loadyc2(JSONObject object) throws JSONException {
            final String customerId = object.getString("customerId");
            final String liceNo = object.getString("liceNo");

            final int zjAbnormalFlag = object.getInt("zjAbnormalFlag");
            final int jyAbnormalFlag = object.getInt("jyAbnormalFlag");
            final int apcdAbnormalFlag = object.getInt("apcdAbnormalFlag");
            final int dsfAbnormalFlag = object.getInt("dsfAbnormalFlag");
            final int lotteryAbnormalFlag = object.getInt("lotteryAbnormalFlag");

            final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
            pd.show();

            RequestParams params = new RequestParams();
            params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MWB_Base_URL + "marketInspect!customerAbnormal.action?privilegeFlag=VIEW&_query.customerId=" + customerId + "\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

            HttpClient.post(Constant.EXEC, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    System.out.println("response: " + response.toString());
                    pd.dismiss();

                    try {
                        if (StringUtility.isSuccess(response)) {
                            String messageSting = response.getString("message");

                            JSONObject message = new JSONObject(messageSting);

                            if (StringUtility.isSuccess(message)) {

                                final String zjAbnormal = message.getString("zjAbnormal");
                                final String jyAbnormal = message.getString("jyAbnormal");
                                final String apcdAbnormal = message.getString("apcdAbnormal");
                                final String dsfAbnormal = message.getString("dsfAbnormal");
                                final String lotteryAbnormal = message.getString("lotteryAbnormal");

                                StringBuffer buffer = new StringBuffer("");
                                if (zjAbnormal.length() > 0)
                                    buffer.append("证件异常：\n" + zjAbnormal + "\n");
                                if (jyAbnormal.length() > 0)
                                    buffer.append("经营异常：\n" + jyAbnormal + "\n");
                                if (apcdAbnormal.length() > 0)
                                    buffer.append("APCD异常：\n" + apcdAbnormal + "\n");
                                if (dsfAbnormal.length() > 0)
                                    buffer.append("第三方调查异常：\n" + dsfAbnormal + "\n");
                                if (lotteryAbnormal.length() > 0)
                                    buffer.append("随机检查异常：\n" + lotteryAbnormal + "\n");

                                AlertDialog.Builder builder = new AlertDialog.Builder(RCFWMainFragment.this.getActivity());
                                builder.setMessage(buffer.toString());
                                builder.setTitle("异常信息");
                                builder.setPositiveButton("确认", null);
                                builder.create().show();

                            } else {
                                showToast(response.getString("message"), getActivity());
                            }
                        } else {
                            showToast(response.getString("message"), getActivity());
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    pd.dismiss();
                    showNetworkError(RCFWMainFragment.this.getActivity());
                    super.onFailure(statusCode, headers, responseString, throwable);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    pd.dismiss();
                    showNetworkError(RCFWMainFragment.this.getActivity());
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    pd.dismiss();
                    showNetworkError(RCFWMainFragment.this.getActivity());
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        class RecyclerHolder extends RecyclerView.ViewHolder{

            private TextView xkzhTv;
            private TextView dmTv;
            private TextView xmTv;
            private TextView sqTv;
            private TextView csTv;
            private TextView jlTv;
            private TextView ycTv, px_tv, jd_tv;

            private void assignViews(View itemView) {
                xkzhTv = (TextView) itemView.findViewById(R.id.xkzh_tv);
                dmTv = (TextView) itemView.findViewById(R.id.dm_tv);
                xmTv = (TextView) itemView.findViewById(R.id.xm_tv);
                sqTv = (TextView) itemView.findViewById(R.id.sq_tv);
                csTv = (TextView) itemView.findViewById(R.id.cs_tv);
                jlTv = (TextView) itemView.findViewById(R.id.jl_tv);
                ycTv = (TextView) itemView.findViewById(R.id.yc_tv);
                px_tv = (TextView) itemView.findViewById(R.id.px_tv);
                jd_tv = (TextView) itemView.findViewById(R.id.jd_tv);

                dmTv.setOnClickListener(new ViewClickVibrate(){

                    @Override
                    public void onClick(View view) {
                        super.onClick(view);

                        if(isMc) {
                            isMc = false;
                        }else{
                            isMc = true;
                        }

                        if(wsckhCb.isChecked()){
                            Collections.sort(wxcdatas, comparator_mc);
                            recyclerAdapter.notifyDataSetChanged();
                        }else if(ycCb.isChecked()){
                            Collections.sort(ycdatas, comparator_mc);
                            recyclerAdapter.notifyDataSetChanged();
                        }else {
                            Collections.sort(datas, comparator_mc);
                            recyclerAdapter.notifyDataSetChanged();
                        }
                    }
                });

                xkzhTv.setOnClickListener(new ViewClickVibrate(){

                    @Override
                    public void onClick(View view) {
                        super.onClick(view);

                        if(isXkz) {
                            isXkz = false;

                            if(wsckhCb.isChecked()){
                                Collections.sort(wxcdatas, comparator_zh1);

                                recyclerAdapter.notifyDataSetChanged();
                            }else if(ycCb.isChecked()){
                                Collections.sort(ycdatas, comparator_zh1);

                                recyclerAdapter.notifyDataSetChanged();
                            }else {
                                Collections.sort(datas, comparator_zh1);

                                recyclerAdapter.notifyDataSetChanged();
                            }
                        }else{
                            isXkz = true;

                            if(wsckhCb.isChecked()){
                                Collections.sort(wxcdatas, comparator_zh2);

                                recyclerAdapter.notifyDataSetChanged();
                            }else if(ycCb.isChecked()){
                                Collections.sort(ycdatas, comparator_zh2);

                                recyclerAdapter.notifyDataSetChanged();
                            }else {
                                Collections.sort(datas, comparator_zh2);

                                recyclerAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });

                xmTv.setOnClickListener(new ViewClickVibrate(){

                    @Override
                    public void onClick(View view) {
                        super.onClick(view);

                        if(isJyr) {
                            isJyr = false;
                        } else {
                            isJyr = true;
                        }

                        if(wsckhCb.isChecked()){
                            Collections.sort(wxcdatas, comparator_jyr);

                            recyclerAdapter.notifyDataSetChanged();
                        }else if(ycCb.isChecked()){
                            Collections.sort(ycdatas, comparator_jyr);

                            recyclerAdapter.notifyDataSetChanged();
                        }else {
                            Collections.sort(datas, comparator_jyr);

                            recyclerAdapter.notifyDataSetChanged();
                        }
                    }
                });

                sqTv.setOnClickListener(new ViewClickVibrate(){

                    @Override
                    public void onClick(View view) {
                        super.onClick(view);

                        if(isSheQu) {
                            isSheQu = false;
                        } else {
                            isSheQu = true;
                        }

                        if(wsckhCb.isChecked()){
                            Collections.sort(wxcdatas, comparator_shequ);

                            recyclerAdapter.notifyDataSetChanged();
                        }else if(ycCb.isChecked()){
                            Collections.sort(ycdatas, comparator_shequ);

                            recyclerAdapter.notifyDataSetChanged();
                        }else {
                            Collections.sort(datas, comparator_shequ);

                            recyclerAdapter.notifyDataSetChanged();
                        }
                    }
                });

                csTv.setOnClickListener(new ViewClickVibrate(){

                    @Override
                    public void onClick(View view) {
                        super.onClick(view);

                        if(isXccs) {
                            isXccs = false;


                            if(wsckhCb.isChecked()){
                                Collections.sort(wxcdatas, comparator_xccs2);
                                recyclerAdapter.notifyDataSetChanged();
                            }else if(ycCb.isChecked()){
                                Collections.sort(ycdatas, comparator_xccs2);
                                recyclerAdapter.notifyDataSetChanged();
                            }else {
                                Collections.sort(datas, comparator_xccs2);
                                recyclerAdapter.notifyDataSetChanged();
                            }
                        }else{
                            isXccs = true;

                            if(wsckhCb.isChecked()){
                                Collections.sort(wxcdatas, comparator_xccs1);
                                recyclerAdapter.notifyDataSetChanged();
                            }else if(ycCb.isChecked()){
                                Collections.sort(ycdatas, comparator_xccs1);
                                recyclerAdapter.notifyDataSetChanged();
                            }else {
                                Collections.sort(datas, comparator_xccs1);
                                recyclerAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });

                jlTv.setOnClickListener(new ViewClickVibrate() {
                    @Override
                    public void onClick(View v) {
                        super.onClick(v);

                        if(isJl) {
                            isJl = false;

                            if(wsckhCb.isChecked()){
                                Collections.sort(wxcdatas, comparator_jl1);

                                recyclerAdapter.notifyDataSetChanged();
                            }else if(ycCb.isChecked()){
                                Collections.sort(ycdatas, comparator_jl1);

                                recyclerAdapter.notifyDataSetChanged();
                            }else {
                                Collections.sort(datas, comparator_jl1);

                                recyclerAdapter.notifyDataSetChanged();
                            }
                        }else{
                            isJl = true;

                            if(wsckhCb.isChecked()){
                                Collections.sort(wxcdatas, comparator_jl2);

                                recyclerAdapter.notifyDataSetChanged();
                            }else if(ycCb.isChecked()){
                                Collections.sort(ycdatas, comparator_jl2);

                                recyclerAdapter.notifyDataSetChanged();
                            }else {
                                Collections.sort(datas, comparator_jl2);

                                recyclerAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
            }


            public RecyclerHolder(View itemView) {
                super(itemView);

                assignViews(itemView);
            }
        }
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation.getLocType() == 61 || bdLocation.getLocType() == 161) {

                latitude = bdLocation.getLatitude();
                longitude = bdLocation.getLongitude();

                loadData(xkzhEt.getText().toString(), dmEt.getText().toString(), jyrEt.getText().toString());

            } else {
                RCFWMainFragment.this.showToast("定位失败", RCFWMainFragment.this.getActivity());
            }

            mLocationClient.stop();
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    final Comparator<JSONObject> comparator_mc = new Comparator<JSONObject>() {
        @Override
        public int compare(JSONObject lhs, JSONObject rhs) {
            try {
                String  ln = lhs.getString("shopName");
                String  rn = rhs.getString("shopName");

                if(ln.equals(rn)) return 1;
                else return -1;

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }
    };

    final Comparator<JSONObject> comparator_zh1 = new Comparator<JSONObject>() {
        @Override
        public int compare(JSONObject lhs, JSONObject rhs) {
            try {
                long ln = lhs.getLong("liceNo");
                long rn = rhs.getLong("liceNo");

                int dw = 0;

                if (ln < rn) dw = 1;
                else if (ln > rn) dw = -1;
                else if (ln == rn) dw = 0;

                return dw;

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }
    };

    final Comparator<JSONObject> comparator_zh2 = new Comparator<JSONObject>() {
        @Override
        public int compare(JSONObject lhs, JSONObject rhs) {
            try {
                long ln = lhs.getLong("liceNo");
                long rn = rhs.getLong("liceNo");

                int dw = 0;

                if (ln > rn) dw = 1;
                else if (ln < rn) dw = -1;
                else if (ln == rn) dw = 0;

                return dw;

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }
    };

    final Comparator<JSONObject> comparator_jyr = new Comparator<JSONObject>() {
        @Override
        public int compare(JSONObject lhs, JSONObject rhs) {
            try {
                String  ln = lhs.getString("chargerName");
                String  rn = rhs.getString("chargerName");

                if(ln.equals(rn)) return 1;
                else return -1;

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }
    };

    final Comparator<JSONObject> comparator_shequ = new Comparator<JSONObject>() {
        @Override
        public int compare(JSONObject lhs, JSONObject rhs) {
            try {
                String  ln = lhs.getString("communityName");
                String  rn = rhs.getString("communityName");

                if(ln.equals(rn)) return 1;
                else return -1;

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }
    };

    final Comparator<JSONObject> comparator_xccs1 = new Comparator<JSONObject>() {
        @Override
        public int compare(JSONObject lhs, JSONObject rhs) {
            try {

                int  ln = lhs.getInt("targetNum");
                int  rn = rhs.getInt("targetNum");

                int dw = 0;

                if (ln < rn) dw = 1;
                else if (ln > rn) dw = -1;
                else if (ln == rn) dw = 0;

                return dw;



            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }
    };

    final Comparator<JSONObject> comparator_xccs2 = new Comparator<JSONObject>() {
        @Override
        public int compare(JSONObject lhs, JSONObject rhs) {
            try {

                int  ln = lhs.getInt("targetNum");
                int  rn = rhs.getInt("targetNum");

                int dw = 0;

                if (ln > rn) dw = 1;
                else if (ln < rn) dw = -1;
                else if (ln == rn) dw = 0;

                return dw;



            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }
    };

    final Comparator<JSONObject> comparator_jl1 = new Comparator<JSONObject>() {
        @Override
        public int compare(JSONObject lhs, JSONObject rhs) {
            try {

                int  ln = lhs.getInt("distance");
                int  rn = rhs.getInt("distance");

                int dw = 0;

                if (ln < rn) dw = 1;
                else if (ln > rn) dw = -1;
                else if (ln == rn) dw = 0;

                return dw;



            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }
    };

    final Comparator<JSONObject> comparator_jl2 = new Comparator<JSONObject>() {
        @Override
        public int compare(JSONObject lhs, JSONObject rhs) {
            try {
                int  ln = lhs.getInt("distance");
                int  rn = rhs.getInt("distance");

                int dw = 0;

                if (ln > rn) dw = 1;
                else if (ln < rn) dw = -1;
                else if (ln == rn) dw = 0;

                return dw;



            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }
    };
}
