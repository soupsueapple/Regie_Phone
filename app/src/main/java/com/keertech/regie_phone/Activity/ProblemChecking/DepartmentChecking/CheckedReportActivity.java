package com.keertech.regie_phone.Activity.ProblemChecking.DepartmentChecking;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.keertech.regie_phone.Activity.ProblemChecking.CheckingActivity;
import com.keertech.regie_phone.BaseActivity;
import com.keertech.regie_phone.Constant.Constant;
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
 * Created by soup on 2017/5/15.
 */

public class CheckedReportActivity extends BaseActivity{

    private TextView checkedDateTv;
    private LinearLayout customerCheckedLl;
    private TextView customerCheckedNumTv;
    private LinearLayout noLicenseCheckedLl;
    private TextView noLicenseCheckedNumTv;
    private TextView textView11;
    private TextView principalTv;
    private TextView textView12;
    private TextView checkedPeopleTv;
    private RecyclerView recyclerView;
    private EditText policeNumEt;
    private EditText businessNumEt;
    private EditText streetNumEt;
    private EditText checkedReportEt;

    ArrayList<JSONObject> datas = new ArrayList<>();

    RecyclerAdapter recyclerAdapter = new RecyclerAdapter();

    String id = "";

    int cnum = 0;
    int ctotalnum = 0;

    int nnum = 0;
    int ntotalnum = 0;

    JSONObject object;

    private void assignViews() {
        checkedDateTv = (TextView) findViewById(R.id.checked_date_tv);
        customerCheckedLl = (LinearLayout) findViewById(R.id.customer_checked_ll);
        customerCheckedNumTv = (TextView) findViewById(R.id.customer_checked_num_tv);
        noLicenseCheckedLl = (LinearLayout) findViewById(R.id.no_license_checked_ll);
        noLicenseCheckedNumTv = (TextView) findViewById(R.id.no_license_checked_num_tv);
        textView11 = (TextView) findViewById(R.id.textView11);
        principalTv = (TextView) findViewById(R.id.principal_tv);
        textView12 = (TextView) findViewById(R.id.textView12);
        checkedPeopleTv = (TextView) findViewById(R.id.checked_people_tv);
        recyclerView = (RecyclerView) findViewById(recycler_view);
        policeNumEt = (EditText) findViewById(R.id.police_num_et);
        businessNumEt = (EditText) findViewById(R.id.business_num_et);
        streetNumEt = (EditText) findViewById(R.id.street_num_et);
        checkedReportEt = (EditText) findViewById(R.id.checked_report_et);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        try {
            object = new JSONObject(getIntent().getStringExtra("object"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        id = getIntent().getStringExtra("id");

        try {
            checkedDateTv.setText(object.getString("plancheckdate").substring(0, 10));

            cnum = object.getInt("cnum");
            ctotalnum = object.getInt("ctotalnum");

            nnum = object.getInt("nnum");
            ntotalnum = object.getInt("ntotalnum");

            customerCheckedNumTv.setText(cnum + "/" + ctotalnum);
            customerCheckedNumTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

            noLicenseCheckedNumTv.setText(nnum + "/" + ntotalnum);
            noLicenseCheckedNumTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

            principalTv.setText(object.getString("chargername"));
            checkedPeopleTv.setText(object.getString("checkpersonnames"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Constant.isRefresJZZZSGrid = false;

        loadAbnormalClient();
    }

    private void loadAbnormalClient() {
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams requestParams = new RequestParams();
        requestParams.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MWB_Base_URL + "concentratePunish!m_loadAbnormalClient.action?privilegeFlag=VIEW&bean.id=" + id + "\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

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

                            if (datas.size() > 0) datas.clear();

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject object = data.getJSONObject(i);

                                datas.add(object);
                            }

                            recyclerAdapter.notifyDataSetChanged();

                        } else {
                            showToast(response.getString("message"), CheckedReportActivity.this);
                        }
                    } else {
                        showToast(response.getString("message"), CheckedReportActivity.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(CheckedReportActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(CheckedReportActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(CheckedReportActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void complete() {
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams requestParams = new RequestParams();
        requestParams.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MWB_Base_URL + "concentratePunish!m_complete.action?privilegeFlag=EDIT&bean.id=" + id + "&bean.policeNum=" + policeNumEt.getText().toString() + "&bean.businessNum=" + businessNumEt.getText().toString() + "&bean.streetNum=" + streetNumEt.getText().toString() + "&bean.jobRequirement=" + checkedReportEt.getText().toString() + "\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

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
                            Constant.isRefresJZZZSGrid = true;
                            finish();
                        } else {
                            showToast(response.getString("message"), CheckedReportActivity.this);
                        }
                    } else {
                        showToast(response.getString("message"), CheckedReportActivity.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(CheckedReportActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(CheckedReportActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(CheckedReportActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checked_report);
        setToolbarTitle("核查报告");
        showBack();

        assignViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.end_checking_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_end_checking){

            if(checkedReportEt.getText().toString().length() == 0){
                showToast("请输入检查小结", this);
            }else if(cnum != ctotalnum && nnum != ntotalnum){
                showToast("检查没有完成", this);
            }else{
                complete();
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>{

        @Override
        public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerHolder holder = new RecyclerHolder(LayoutInflater.from(CheckedReportActivity.this).inflate(R.layout.activity_checked_report_recyclerview_item, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerHolder holder, int position) {

            JSONObject object = datas.get(position);

            try {

                boolean noLice = false;

                if (StringUtility.notObjEmpty(object, "customer")) {
                    JSONObject customer = object.getJSONObject("customer");

                    String shopName = customer.getString("shopName");
                    String liceNo = customer.getString("liceNo");
                    String chargerName = customer.getString("chargerName");

                    holder.shopnameTv.setText(shopName);
                    holder.licenseTv.setText(liceNo);
                    holder.nameTv.setText(chargerName);

                    noLice = false;

                }
                if (StringUtility.notObjEmpty(object, "noLiceRegistry")) {
                    JSONObject noLiceRegistry = object.getJSONObject("noLiceRegistry");

                    String shopName = noLiceRegistry.getString("shopName");
                    String name = noLiceRegistry.getString("name");

                    holder.shopnameTv.setText(shopName);
                    holder.nameTv.setText(name);

                    noLice = true;
                }

                int measures = object.getInt("measures");

                switch (measures){
                    case 1:
                        holder.measureTv.setText("法律法规宣传");
                        break;
                    case 2:
                        holder.measureTv.setText("出具《先行教育通知书》");
                        break;
                    case 3:
                        holder.measureTv.setText("出具《责令整改通知书》");
                        break;
                    case 4:
                        holder.measureTv.setText("涉案物品先行登记保存通知书");
                        break;
                }


                int isIntoCorpCheck = object.getInt("isIntoCorpCheck");

                final int id = object.getInt("id");
                final int checkStatus = object.getInt("checkStatus");

                final boolean noLiceB = noLice;

                holder.lookUpTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(CheckedReportActivity.this, CheckingActivity.class);
                        intent.putExtra("id", id);
                        intent.putExtra("status", checkStatus);
                        intent.putExtra("concentratePunish", CheckedReportActivity.this.id);
                        intent.putExtra("postType", 0);
                        intent.putExtra("noLice", noLiceB);
                        startActivity(intent);
                    }
                });

                int intoCorpCheck;

                if (isIntoCorpCheck == 1) {
                    holder.reportTv.setText("撤销");

                    intoCorpCheck = 0;
                } else {
                    holder.reportTv.setText("上报");

                    intoCorpCheck = 1;
                }

                final int intoCorpCheckC = intoCorpCheck;

                holder.reportTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(CheckedReportActivity.this);
                        builder.setMessage(intoCorpCheckC == 1 ? "是否上报" : "是否撤销");
                        builder.setTitle("提示");
                        builder.setPositiveButton(intoCorpCheckC == 1 ? "上报" : "撤销", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                abnormalClientHandle(id, intoCorpCheckC);
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

        private void abnormalClientHandle(int id, int intoCorpCheck) {
            final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
            pd.show();

            RequestParams requestParams = new RequestParams();
            requestParams.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MWB_Base_URL + "concentratePunish!m_abnormalClientHandle.action?privilegeFlag=EDIT&detailId=" + id + "&isIntoCorpCheck=" + intoCorpCheck + "\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

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
                                loadAbnormalClient();
                            } else {
                                showToast(response.getString("message"), CheckedReportActivity.this);
                            }
                        } else {
                            showToast(response.getString("message"), CheckedReportActivity.this);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    pd.dismiss();
                    showNetworkError(CheckedReportActivity.this);
                    super.onFailure(statusCode, headers, responseString, throwable);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    pd.dismiss();
                    showNetworkError(CheckedReportActivity.this);
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    pd.dismiss();
                    showNetworkError(CheckedReportActivity.this);
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
            private TextView nameTv;
            private TextView licenseTv;
            private TextView measureTv;
            private TextView reportTv;
            private TextView lookUpTv;

            private void assignViews(View itemView) {
                shopnameTv = (TextView) itemView.findViewById(R.id.shopname_tv);
                nameTv = (TextView) itemView.findViewById(R.id.name_tv);
                licenseTv = (TextView) itemView.findViewById(R.id.license_tv);
                measureTv = (TextView) itemView.findViewById(R.id.measure_tv);
                reportTv = (TextView) itemView.findViewById(R.id.report_tv);
                lookUpTv = (TextView) itemView.findViewById(R.id.look_up_tv);
            }


            public RecyclerHolder(View itemView) {
                super(itemView);

                assignViews(itemView);
            }
        }
    }
}
