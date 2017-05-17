package com.keertech.regie_phone.Activity.ReportComplanints.Finished;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
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

import static com.keertech.regie_phone.R.id.recycler_view;

/**
 * Created by soup on 2017/5/17.
 */

public class FinishedActivity extends BaseActivity{

    private TextView textView15;
    private TextView tradenameTv;
    private TextView textView16;
    private TextView nameTv;
    private TextView phoneTv;
    private TextView timeTv;
    private TextView textView17;
    private TextView contentTv;
    private TextView textView18;
    private TextView businessTypeTv;
    private TextView textView19;
    private TextView workTypeTv;
    private TextView textView20;
    private TextView brandTv;
    private RecyclerView recyclerView;

    private ArrayList<JSONObject> data = new ArrayList<>();

    RecyclerAdapter recyclerAdapter = new RecyclerAdapter();

    private int reportcomplaintype;

    private int taskid = 0;

    private String phoneNo = "";


    private void assignViews() {
        textView15 = (TextView) findViewById(R.id.textView15);
        tradenameTv = (TextView) findViewById(R.id.tradename_tv);
        textView16 = (TextView) findViewById(R.id.textView16);
        nameTv = (TextView) findViewById(R.id.name_tv);
        phoneTv = (TextView) findViewById(R.id.phone_tv);
        timeTv = (TextView) findViewById(R.id.time_tv);
        textView17 = (TextView) findViewById(R.id.textView17);
        contentTv = (TextView) findViewById(R.id.content_tv);
        textView18 = (TextView) findViewById(R.id.textView18);
        businessTypeTv = (TextView) findViewById(R.id.business_type_tv);
        textView19 = (TextView) findViewById(R.id.textView19);
        workTypeTv = (TextView) findViewById(R.id.work_type_tv);
        textView20 = (TextView) findViewById(R.id.textView20);
        brandTv = (TextView) findViewById(R.id.brand_tv);
        recyclerView = (RecyclerView) findViewById(recycler_view);

        contentTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                if(contentTv.getText().toString().length()> 0){
                    showToast(contentTv.getText().toString(), FinishedActivity.this);
                }
            }
        });

        phoneTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                if (!StringUtility.isEmpty(phoneNo)) {
                    Intent phoneIntent = new Intent(
                            "android.intent.action.CALL", Uri.parse("tel:"
                            + phoneNo));
                    startActivity(phoneIntent);
                }
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);

        try {
            String jsonString = getIntent().getStringExtra("data");
            JSONObject jsonObject = new JSONObject(jsonString);

            reportcomplaintype = jsonObject.getInt("reportcomplaintype");
            taskid = jsonObject.getInt("id");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        setToolbarTitle(reportcomplaintype == 2 ? "查看举报" : "查看投诉");

        if (reportcomplaintype == 2) {
            textView20.setVisibility(View.VISIBLE);
            brandTv.setVisibility(View.VISIBLE);
            loadJbData();
        } else if (reportcomplaintype == 3) {
            textView20.setVisibility(View.GONE);
            brandTv.setVisibility(View.GONE);
            loadTsData();
        }

        loadLC();
    }

    private void loadJbData() {
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams params = new RequestParams();
        params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MIIS_Base_URL + "reportManage!findBean.action?privilegeFlag=VIEW&bean.id=" + taskid + "\",\"type\":\"WebExecutor\",\"method\":\"GET\"},\"app\":\"1003\"}");

        HttpClient.post(Constant.EXEC, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                pd.dismiss();

                try {
                    if (StringUtility.isSuccess(response)) {
                        String messageSting = response.getString("message");

                        JSONObject message = new JSONObject(messageSting);

                        if (StringUtility.isSuccess(message)) {

                            JSONObject data = message.getJSONObject("data");

                            String cigLicenseNo = data.getString("cigLicenseNo");
                            String tradename = data.getString("tradename");
                            String customerName = data.getString("customerName");
                            phoneNo = data.getString("phoneNo");
                            String workOrderDate = data.getString("workOrderDate");
                            String workOrderContent = data.getString("workOrderContent");

                            JSONObject workOrderType = data.getJSONObject("workOrderType");
                            JSONObject parentWOType = workOrderType.getJSONObject("parentWOType");

                            if(reportcomplaintype == 2) {
                                String cigaretteBrand = data.getString("cigaretteBrand");
                                brandTv.setText(cigaretteBrand + " ");// + cigCount + "条"
                            }

                            String rejectReason = data.getString("rejectReason");
                            String backReason = data.getString("backReason");

                            if (StringUtility.isEmpty(rejectReason)) rejectReason = "";
                            if (StringUtility.isEmpty(backReason)) backReason = "";

                            textView15.setText(cigLicenseNo);
                            tradenameTv.setText(tradename);
                            nameTv.setText(customerName);
                            phoneTv.setText(phoneNo);
                            timeTv.setText(workOrderDate);
                            contentTv.setText(workOrderContent);
                            businessTypeTv.setText(parentWOType.getString("name"));
                            workTypeTv.setText(workOrderType.getString("name"));


                        } else {
                            pd.dismiss();
                            showToast(message.getString("message"), FinishedActivity.this);
                        }
                    } else {
                        showToast(response.getString("message"), FinishedActivity.this);
                    }
                } catch (JSONException e) {
                    pd.dismiss();
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(FinishedActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(FinishedActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(FinishedActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void loadTsData() {
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams params = new RequestParams();
        params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MIIS_Base_URL + "complainManage!findBean.action?privilegeFlag=VIEW&bean.id=" + taskid + "\",\"type\":\"WebExecutor\",\"method\":\"GET\"},\"app\":\"1003\"}");

        HttpClient.post(Constant.EXEC, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                pd.dismiss();

                try {
                    if (StringUtility.isSuccess(response)) {
                        String messageSting = response.getString("message");

                        JSONObject message = new JSONObject(messageSting);

                        if (StringUtility.isSuccess(message)) {

                            JSONObject data = message.getJSONObject("data");

                            String cigLicenseNo = data.getString("cigLicenseNo");
                            String tradename = data.getString("tradename");
                            String customerName = data.getString("customerName");
                            phoneNo = data.getString("phoneNo");
                            String workOrderDate = data.getString("workOrderDate");
                            String workOrderContent = data.getString("workOrderContent");

                            JSONObject workOrderType = data.getJSONObject("workOrderType");
                            JSONObject parentWOType = workOrderType.getJSONObject("parentWOType");

                            String rejectReason = data.getString("rejectReason");
                            String backReason = data.getString("backReason");

                            if (StringUtility.isEmpty(rejectReason)) rejectReason = "";
                            if (StringUtility.isEmpty(backReason)) backReason = "";

                            textView15.setText(cigLicenseNo);
                            tradenameTv.setText(tradename);
                            nameTv.setText(customerName);
                            phoneTv.setText(phoneNo);
                            timeTv.setText(workOrderDate);
                            contentTv.setText(workOrderContent);
                            businessTypeTv.setText(parentWOType.getString("name"));
                            workTypeTv.setText(workOrderType.getString("name"));

                        } else {
                            pd.dismiss();
                            showToast(message.getString("message"), FinishedActivity.this);
                        }
                    }else {
                        showToast(response.getString("message"), FinishedActivity.this);
                    }
                } catch (JSONException e) {
                    pd.dismiss();
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(FinishedActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(FinishedActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(FinishedActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void loadLC(){

        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams params = new RequestParams();
        params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\""+Constant.MIIS_Base_URL+"reportComplainLog!searchBeans.action?privilegeFlag=VIEW&_query.reportComplainId="+taskid+"\",\"type\":\"WebExecutor\",\"method\":\"GET\"},\"app\":\"1003\"}");

        HttpClient.post(Constant.EXEC, params, new JsonHttpResponseHandler(){
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

                            for(int i=data.length()-1;i>=0 ;i--){
                                JSONObject object = data.getJSONObject(i);
                                FinishedActivity.this.data.add(object);
                            }

                            recyclerAdapter.notifyDataSetChanged();

                        } else {
                            showToast(message.getString("message"), FinishedActivity.this);
                        }

                    } else {
                        showToast(response.getString("message"), FinishedActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(FinishedActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(FinishedActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(FinishedActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished);
        showBack();
        assignViews();
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>{

        @Override
        public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerHolder holder = new RecyclerHolder(LayoutInflater.from(FinishedActivity.this).inflate(R.layout.activity_finished_recyclerview_item, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerHolder holder, int position) {

            final JSONObject object = data.get(position);

            try {

                JSONObject subUser = object.getJSONObject("subUser");

                final String note = object.getString("note");

                holder.nameTv.setText(subUser.getString("name"));
                holder.timeTv.setText(object.getString("actionDate"));
                holder.actionTv.setText(object.getString("predicate"));
                holder.contentTv.setText(note);

                holder.rl.setOnClickListener(new ViewClickVibrate(){

                    @Override
                    public void onClick(View view) {
                        super.onClick(view);
                        showToast(note, FinishedActivity.this);
                    }
                });


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class RecyclerHolder extends RecyclerView.ViewHolder{

            private RelativeLayout rl;
            private TextView nameTv;
            private TextView timeTv;
            private TextView actionTv;
            private TextView contentTv;

            private void assignViews(View itemView) {
                rl = (RelativeLayout) itemView.findViewById(R.id.rl);
                nameTv = (TextView) itemView.findViewById(R.id.name_tv);
                timeTv = (TextView) itemView.findViewById(R.id.time_tv);
                actionTv = (TextView) itemView.findViewById(R.id.action_tv);
                contentTv = (TextView) itemView.findViewById(R.id.content_tv);
            }



            public RecyclerHolder(View itemView) {
                super(itemView);
                assignViews(itemView);
            }
        }
    }
}
