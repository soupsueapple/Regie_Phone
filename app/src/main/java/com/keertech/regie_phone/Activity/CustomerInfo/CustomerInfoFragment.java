package com.keertech.regie_phone.Activity.CustomerInfo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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

import com.keertech.regie_phone.Activity.CustomerInfo.CustomerInfo.CucstomerInfoActivity;
import com.keertech.regie_phone.Activity.CustomerInfo.CustomerInfo.CustomerInfoActivity;
import com.keertech.regie_phone.BaseFragment;
import com.keertech.regie_phone.Constant.Constant;
import com.keertech.regie_phone.Listener.ViewClickVibrate;
import com.keertech.regie_phone.Network.HttpClient;
import com.keertech.regie_phone.R;
import com.keertech.regie_phone.Utility.DateTimeUtil;
import com.keertech.regie_phone.Utility.KeerAlertDialog;
import com.keertech.regie_phone.Utility.StringUtility;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static com.keertech.regie_phone.R.id.recycler_view;

/**
 * Created by soup on 2017/5/17.
 */

public class CustomerInfoFragment extends BaseFragment{

    private LinearLayout linearLayout16;
    private EditText licenseTv;
    private EditText shopnameTv;
    private EditText nameTv;
    private TextView communityTv;
    private TextView porpertiyTv;
    private TextView commonTv;
    private CheckBox statusTv;
    private CheckBox notHasPic;
    private CheckBox notHasPosition;
    private RecyclerView recyclerView;
    private FloatingActionButton searchFab;
    private FloatingActionButton mapFab;

    private ArrayList<JSONObject> datas = new ArrayList<>();

    private ArrayList<JSONObject> allDatas = new ArrayList<>();

    private ArrayList<JSONObject> notDoorPhoto = new ArrayList<>();

    private ArrayList<JSONObject> mapdatas = new ArrayList<>();

    private int index = 0;

    private Handler mHandler;

    private boolean otherCondition = false;

    String[] communityNames;
    String[] communityIDs;

    String[] porpertiyNames = {"食杂店", "便利店", "超市", "商场", "名烟名酒名茶", "娱乐服务", "行业自办店", "其它"};
    String[] porpertiyCodes = {"104101", "104102", "104103", "104104", "104105", "104106", "104108", "104107"};

    String[] commonNames;
    String[] commonIds;

    String currentCommunityId = "";
    String currentporpertiyCode = "";
    String currentcommonId = "";

    RecyclerAdapter recyclerAdapter = new RecyclerAdapter();

    private void assignViews(View convertView) {
        linearLayout16 = (LinearLayout) convertView.findViewById(R.id.linearLayout16);
        licenseTv = (EditText) convertView.findViewById(R.id.license_tv);
        shopnameTv = (EditText) convertView.findViewById(R.id.shopname_tv);
        nameTv = (EditText) convertView.findViewById(R.id.name_tv);
        communityTv = (TextView) convertView.findViewById(R.id.community_tv);
        porpertiyTv = (TextView) convertView.findViewById(R.id.porpertiy_tv);
        commonTv = (TextView) convertView.findViewById(R.id.common_tv);
        statusTv = (CheckBox) convertView.findViewById(R.id.status_tv);
        notHasPic = (CheckBox) convertView.findViewById(R.id.not_has_pic);
        notHasPosition = (CheckBox) convertView.findViewById(R.id.not_has_position);
        recyclerView = (RecyclerView) convertView.findViewById(recycler_view);
        searchFab = (FloatingActionButton) convertView.findViewById(R.id.search_fab);
        mapFab = (FloatingActionButton) convertView.findViewById(R.id.map_fab);

        communityTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                showCommunityItemDialog();
            }
        });

        porpertiyTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                showPorpertiyItemDialog();
            }
        });

        commonTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                showCommonItemDialog();
            }
        });

        searchFab.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                searchCustomerInfo(licenseTv.getText().toString(), shopnameTv.getText().toString(), nameTv.getText().toString());
            }
        });

        statusTv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if (isChecked) otherCondition = true;
                else otherCondition = false;

                searchCustomerInfo(licenseTv.getText().toString(), shopnameTv.getText().toString(), nameTv.getText().toString());
            }
        });

        notHasPic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if (isChecked) {
                    datas = notDoorPhoto;
                } else {
                    datas = allDatas;
                }

                recyclerAdapter.notifyDataSetChanged();
            }
        });

        notHasPosition.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if(isChecked){
                    searchNotLocationCustomer(licenseTv.getText().toString(), shopnameTv.getText().toString(), nameTv.getText().toString());
                }else{
                    searchCustomerInfo(licenseTv.getText().toString(), shopnameTv.getText().toString(), nameTv.getText().toString());
                }
            }
        });

        mapFab.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                fileWrite(mapdatas.toString());
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerAdapter);

        mHandler = new Handler();

        loadingCommunity();
        loadingCommon();
        searchCustomerInfo(licenseTv.getText().toString(), shopnameTv.getText().toString(), nameTv.getText().toString());
    }

    private void fileWrite(String json){
        File sdcard = Environment.getExternalStorageDirectory();
        String path = sdcard.getPath()+File.separator+Constant.Base_path;
        String fileName = path + File.separator + DateTimeUtil.getCurrDateTimeStr()+".txt";

        try {
            FileWriter writer=new FileWriter(fileName);
            writer.write(json);
            writer.close();

            Intent intent = new Intent(getActivity(), CustomerMapActivity.class);
            intent.putExtra("fileName", fileName);
            startActivity(intent);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showCommunityItemDialog() {

        if (communityNames == null || communityIDs == null) return;

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).setTitle("选择社区").setItems(communityNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                communityTv.setText(communityNames[which]);
                currentCommunityId = communityIDs[which];
            }
        }).create();
        alertDialog.show();
    }

    private void showPorpertiyItemDialog(){

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).setTitle("选择业态").setItems(porpertiyNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                porpertiyTv.setText(porpertiyNames[which]);
                currentporpertiyCode = porpertiyCodes[which];
            }
        }).create();
        alertDialog.show();
    }

    private void showCommonItemDialog(){
        if(commonNames == null || commonIds == null) return;

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).setTitle("选择市管员").setItems(commonNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                commonTv.setText(commonNames[which]);
                currentcommonId = commonIds[which];
            }
        }).create();
        alertDialog.show();
    }

    private void loadingCommunity(){
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams requestParams = new RequestParams();
        requestParams.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\""+ Constant.MWB_Base_URL+"communityInfo!searchBeans.action?privilegeFlag=VIEW\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

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
                showNetworkError(getActivity());
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(getActivity());
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(getActivity());
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void loadingCommon(){
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams requestParams = new RequestParams();
        requestParams.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\""+Constant.MWB_Base_URL+"common!findPostList.action\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

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

                            commonNames = new String[data.length()];
                            commonIds = new String[data.length()];

                            for(int i=0;i<data.length();i++){
                                JSONObject object = data.optJSONObject(i);

                                String postUser = object.optString("postUser");
                                String id = object.getString("id");

                                commonNames[i] = postUser;
                                commonIds[i] = id;
                            }

                            if(data.length() > 1) commonTv.setVisibility(View.VISIBLE);
                            else commonTv.setVisibility(View.GONE);

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
                showNetworkError(getActivity());
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(getActivity());
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(getActivity());
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void searchCustomerInfo(String liceNo, String shopName, String chargerName) {

        String other = liceNo.length() > 0 ? "&_query.liceNo=" + liceNo : "";
        other = other + (shopName.length() > 0 ? "&_query.shopName=" + shopName : "");
        other = other + (chargerName.length() > 0 ? "&_query.chargerName=" + chargerName : "");
        other = other + (currentcommonId.length() > 0 ? "&_query.postId=" + currentcommonId : "");
        other = other + (currentporpertiyCode.length() > 0 ? "&_query.porpertiy_code=" + currentporpertiyCode : "");
        other = other + (currentCommunityId.length() > 0 ? "&_query.communityId=" + currentCommunityId : "");

        if(otherCondition) other = other +"&_query.otherCondition=7";

        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams requestParams = new RequestParams();
        requestParams.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\""+ Constant.MWB_Base_URL +"customerInfo!searchBeans.action?privilegeFlag=VIEW&start="+index+"&limit=20"+other+"\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");


        HttpClient.post(Constant.EXEC, requestParams, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                pd.dismiss();

                try {

                    if (StringUtility.isSuccess(response)) {
                        String messageSting = response.getString("message");

                        JSONObject message = new JSONObject(messageSting);

                        if (StringUtility.isSuccess(message)) {


                            JSONArray data = message.getJSONArray("data");

                            if (mapdatas.size() > 0) mapdatas.clear();
                            if(notDoorPhoto.size() > 0) notDoorPhoto.clear();
                            if(allDatas.size() > 0) allDatas.clear();

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject object = data.getJSONObject(i);

                                if(!allDatas.contains(object)) allDatas.add(object);
                                if(!mapdatas.contains(object)) mapdatas.add(object);

                                Integer door_photo = object.getInt("door_photo");

                                if(door_photo == 0){
                                    if(!notDoorPhoto.contains(object)) notDoorPhoto.add(object);
                                }
                            }

                            datas = allDatas;

                            recyclerAdapter.notifyDataSetChanged();

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
                showNetworkError(getActivity());
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(getActivity());
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(getActivity());
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

    }

    private void searchNotLocationCustomer(String liceNo, String shopName, String chargerName) {

        String other = liceNo.length() > 0 ? "&_query.liceNo=" + liceNo : "";
        other = other + (shopName.length() > 0 ? "&_query.shopName=" + shopName : "");
        other = other + (chargerName.length() > 0 ? "&_query.chargerName=" + chargerName : "");
        other = other + (currentcommonId.length() > 0 ? "&_query.postId=" + currentcommonId : "");
        other = other + (currentporpertiyCode.length() > 0 ? "&_query.porpertiy_code=" + currentporpertiyCode : "");
        other = other + (currentCommunityId.length() > 0 ? "&_query.communityId=" + currentCommunityId : "");

        other = other +"&_query.otherCondition=5";

        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams requestParams = new RequestParams();
        requestParams.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\""+Constant.MWB_Base_URL + "customerInfo!searchBeans.action?privilegeFlag=VIEW&start="+index+"&limit=20"+other+"\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");


        HttpClient.post(Constant.EXEC, requestParams, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                pd.dismiss();


                try {

                    if (StringUtility.isSuccess(response)) {
                        String messageSting = response.getString("message");

                        JSONObject message = new JSONObject(messageSting);

                        if (StringUtility.isSuccess(message)) {


                            JSONArray data = message.getJSONArray("data");

                            if (mapdatas.size() > 0) mapdatas.clear();
                            if(notDoorPhoto.size() > 0) notDoorPhoto.clear();
                            if(allDatas.size() > 0) allDatas.clear();

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject object = data.getJSONObject(i);

                                allDatas.add(object);
                            }
                            datas = allDatas;

                            recyclerAdapter.notifyDataSetChanged();

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
                showNetworkError(getActivity());
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(getActivity());
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(getActivity());
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View convertView = inflater.inflate(R.layout.fragment_customer_info, null);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        assignViews(convertView);

        return convertView;
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>{

        @Override
        public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerHolder holder = new RecyclerHolder(LayoutInflater.from(CustomerInfoFragment.this.getActivity()).inflate(R.layout.fragment_customer_info_recyclerview_item, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerHolder holder, int position) {

            final JSONObject object = datas.get(position);

            try {
                String liceNo = object.getString("liceNo");
                if(liceNo.equals("null")) liceNo = "";

                holder.licenseTv.setText(liceNo);
                holder.shopnameTv.setText(object.getString("shopName"));
                holder.businessNoTv.setText(object.getString("businessNo").length() > 0 ? object.getString("businessNo") : "无");
                holder.businessNoTv.setText(object.getString("businessNo").equals("null") ? "无":object.getString("businessNo"));
                holder.nameTv.setText(object.getString("chargerName"));
                holder.dateTv.setText(object.getString("deliverTime").replaceAll(" 00:00:00", ""));

                if(object.getInt("door_photo") == 1){
                    holder.picTv.setVisibility(View.VISIBLE);
                }else{
                    holder.picTv.setVisibility(View.GONE);
                }

                int mark = object.getInt("mark");

                if(mark==1){
                    holder.markTv.setVisibility(View.VISIBLE);
                }else{
                    holder.markTv.setVisibility(View.GONE);
                }

                Double bd_latitude = null;

                if(StringUtility.notObjEmpty(object, "bd_latitude")) bd_latitude = object.getDouble("bd_latitude");

                if(bd_latitude == null){
                    holder.shopnameTv.setTextColor(getActivity().getResources().getColor(R.color.red));
                }else {
                    if (bd_latitude == 0) {
                        holder.shopnameTv.setTextColor(getActivity().getResources().getColor(R.color.red));
                    } else {
                        holder.shopnameTv.setTextColor(getActivity().getResources().getColor(R.color.color_black));
                    }
                }

                holder.lookUpTv.setOnClickListener(new ViewClickVibrate(){

                    @Override
                    public void onClick(View view) {
                        super.onClick(view);

                        try {
                            Intent intent = new Intent(getActivity(), CustomerInfoActivity.class);
                            intent.putExtra("id", object.getString("id"));
                            if (!object.isNull("needLocation"))
                                intent.putExtra("needLocation", object.getInt("needLocation"));
                            else intent.putExtra("needLocation", false);
                            getActivity().startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        class RecyclerHolder extends RecyclerView.ViewHolder{

            private TextView licenseTv;
            private TextView businessNoTv;
            private TextView nameTv;
            private TextView dateTv;
            private TextView shopnameTv;
            private TextView lookUpTv, picTv, markTv;

            private void assignViews(View itemView) {
                licenseTv = (TextView) itemView.findViewById(R.id.license_tv);
                businessNoTv = (TextView) itemView.findViewById(R.id.business_no_tv);
                nameTv = (TextView) itemView.findViewById(R.id.name_tv);
                dateTv = (TextView) itemView.findViewById(R.id.date_tv);
                shopnameTv = (TextView) itemView.findViewById(R.id.shopname_tv);
                lookUpTv = (TextView) itemView.findViewById(R.id.look_up_tv);
                picTv = (TextView) itemView.findViewById(R.id.pic_tv);
                markTv = (TextView) itemView.findViewById(R.id.mark_tv);
            }


            public RecyclerHolder(View itemView) {
                super(itemView);

                assignViews(itemView);
            }
        }
    }

}
