package com.keertech.regie_phone.Activity.XZFW.ServicePlan;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.keertech.regie_phone.BaseActivity;
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

/**
 * Created by soup on 2017/5/9.
 */

public class ServicePlanInfoActivity extends BaseActivity{

    ArrayList<JSONObject> datas = new ArrayList<>();

    private TextView weekNameTv;
    private LinearLayout linearLayout5;
    private TextView addPlanInfoTv;
    private TextView clear_plan_info_tv;
    private RecyclerView recyclerView;

    RecyclerAdapter recyclerAdapter = new RecyclerAdapter();

    boolean isAddPlan = false;

    String id = "";
    String yearMonth = "";

    private void assignViews() {
        isAddPlan = getIntent().getBooleanExtra("isAddPlan", false);
        id = getIntent().getStringExtra("id");
        yearMonth = getIntent().getStringExtra("yearMonth");

        weekNameTv = (TextView) findViewById(R.id.week_name_tv);
        linearLayout5 = (LinearLayout) findViewById(R.id.linearLayout5);
        addPlanInfoTv = (TextView) findViewById(R.id.add_plan_info_tv);
        clear_plan_info_tv = (TextView) findViewById(R.id.clear_plan_info_tv);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        weekNameTv.setText(getIntent().getStringExtra("weekname"));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);

        if(!isAddPlan) linearLayout5.setVisibility(View.GONE);

        addPlanInfoTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);

                Intent intent = new Intent(ServicePlanInfoActivity.this, AddServicePlanAcvitivy.class);
                intent.putExtra("yearMonth", yearMonth);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        clear_plan_info_tv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                cleanWeekPlan();
            }
        });

        inspectWeekPlanList(id);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(Constant.isRefresPlan){
            Constant.isRefresPlan = false;

            inspectWeekPlanList(id);
        }
    }

    private void inspectWeekPlanList(String id){
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams requestParams = new RequestParams();
        requestParams.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\""+ Constant.MWB_Base_URL+"inspectWeekPlan!inspectWeekPlanList.action?inspectWeek.id="+id+"\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

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

                            if(datas.size() > 0) datas.clear();

                            for(int i=0;i<data.length();i++){
                                JSONObject object = data.optJSONObject(i);
                                datas.add(object);
                            }

                            recyclerAdapter.notifyDataSetChanged();

                        } else {
                            showToast(response.getString("message"), ServicePlanInfoActivity.this);
                        }
                    } else {
                        showToast(response.getString("message"), ServicePlanInfoActivity.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(ServicePlanInfoActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(ServicePlanInfoActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(ServicePlanInfoActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void cleanWeekPlan(){
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams requestParams = new RequestParams();
        requestParams.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\""+ Constant.MWB_Base_URL+"inspectWeekPlan!cleanWeekPlan.action?inspectWeek.id="+id+"\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

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

                            datas.clear();
                            recyclerAdapter.notifyDataSetChanged();

                        } else {
                            showToast(response.getString("message"), ServicePlanInfoActivity.this);
                        }
                    } else {
                        showToast(response.getString("message"), ServicePlanInfoActivity.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(ServicePlanInfoActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(ServicePlanInfoActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(ServicePlanInfoActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_plan_info);
        setToolbarTitle("服务计划");
        showBack();

        assignViews();
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>{

        @Override
        public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerHolder holder = new RecyclerHolder(LayoutInflater.from(ServicePlanInfoActivity.this).inflate(R.layout.activity_service_plan_info_recylerview_item, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerHolder holder, int position) {
            final JSONObject object = datas.get(position);

            try {

                final String planId = object.getString("planId");

                holder.licenseTv.setText(object.getString("liceNo"));
                holder.shopnameTv.setText(object.getString("shopName"));
                holder.nameTv.setText(object.getString("chargerName"));
                holder.phoneTv.setText(object.getString("streetName"));

                if(!isAddPlan) {

                    holder.delTv.setVisibility(View.GONE);

                }else{
                    holder.delTv.setVisibility(View.VISIBLE);

                }

                holder.delTv.setOnClickListener(new ViewClickVibrate() {
                    @Override
                    public void onClick(View v) {
                        super.onClick(v);

                        AlertDialog.Builder builder = new AlertDialog.Builder(ServicePlanInfoActivity.this);
                        builder.setMessage("是否删除");
                        builder.setTitle("提示");
                        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                delCust(planId, object);
                            }
                        });
                        builder.setNegativeButton("取消", null);
                        builder.create().show();
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        private void delCust(String id, final JSONObject object){
            final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
            pd.show();

            RequestParams requestParams = new RequestParams();
            requestParams.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\""+ Constant.MWB_Base_URL+"inspectWeekPlan!m_delCust.action?bean.id="+id+"\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

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

                                datas.remove(object);
                                recyclerAdapter.notifyDataSetChanged();

                            } else {
                                showToast(response.getString("message"), ServicePlanInfoActivity.this);
                            }
                        } else {
                            showToast(response.getString("message"), ServicePlanInfoActivity.this);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    pd.dismiss();
                    showNetworkError(ServicePlanInfoActivity.this);
                    super.onFailure(statusCode, headers, responseString, throwable);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    pd.dismiss();
                    showNetworkError(ServicePlanInfoActivity.this);
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    pd.dismiss();
                    showNetworkError(ServicePlanInfoActivity.this);
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        class RecyclerHolder extends RecyclerView.ViewHolder{

            private TextView shopnameTv;
            private TextView licenseTv;
            private TextView nameTv;
            private TextView phoneTv;
            private TextView delTv;

            private void assignViews(View itemView) {
                shopnameTv = (TextView) itemView.findViewById(R.id.shopname_tv);
                licenseTv = (TextView) itemView.findViewById(R.id.license_tv);
                nameTv = (TextView) itemView.findViewById(R.id.name_tv);
                phoneTv = (TextView) itemView.findViewById(R.id.phone_tv);
                delTv = (TextView) itemView.findViewById(R.id.del_tv);
            }


            public RecyclerHolder(View itemView) {
                super(itemView);
                assignViews(itemView);
            }
        }
    }
}
