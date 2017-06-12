package com.keertech.regie_phone.Activity.ProblemChecking.DepartmentChecking;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.keertech.regie_phone.Activity.ProblemChecking.CheckingActivity;
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
 * Created by soup on 2017/5/15.
 */

public class DepartmentCustomerCheckingActivity extends BaseActivity{

    private TextView noLicenseCheckingTv;
    private TextView addCustomerTv;
    private TextView checkedRecordTv;
    private RecyclerView recyclerView;

    private ArrayList<JSONObject> datas = new ArrayList<>();

    RecyclerAdapter recyclerAdapter = new RecyclerAdapter();

    int type = 0;
    int postType = 0;
    boolean ck = false;

    String id = "";

    JSONObject object;

    private void assignViews() {
        noLicenseCheckingTv = (TextView) findViewById(R.id.no_license_checking_tv);
        addCustomerTv = (TextView) findViewById(R.id.add_customer_tv);
        checkedRecordTv = (TextView) findViewById(R.id.checked_record_tv);
        recyclerView = (RecyclerView) findViewById(recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(DepartmentCustomerCheckingActivity.this));
        recyclerView.setAdapter(recyclerAdapter);

        noLicenseCheckingTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                Intent intent = new Intent(DepartmentCustomerCheckingActivity.this, DepartmentNoLicenseCheckingActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("object", object.toString());
                intent.putExtra("type", type);
                intent.putExtra("postType", postType);
                intent.putExtra("ck", ck);
                startActivity(intent);
            }
        });

        addCustomerTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                Intent intent = new Intent(DepartmentCustomerCheckingActivity.this, ChooseCustomerActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        checkedRecordTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                Intent intent = new Intent(DepartmentCustomerCheckingActivity.this, CheckedReportActivity.class);
                intent.putExtra("object", object.toString());
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        id = getIntent().getStringExtra("id");
        type = getIntent().getIntExtra("type", 0);
        postType = getIntent().getIntExtra("postType", 0);
        ck = getIntent().getBooleanExtra("ck", false);
        try {
            object = new JSONObject(getIntent().getStringExtra("object"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Constant.isRefresJZZZSGrid = false;

        if(!ck) {
            if (type == 1 && postType == 1) {
                addCustomerTv.setVisibility(View.VISIBLE);
                checkedRecordTv.setVisibility(View.VISIBLE);
            } else {
                addCustomerTv.setVisibility(View.GONE);
                checkedRecordTv.setVisibility(View.GONE);
            }
        }else {
            addCustomerTv.setVisibility(View.GONE);
            checkedRecordTv.setVisibility(View.GONE);
        }

        loadCust();
    }


    private void loadCust(){
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams requestParams = new RequestParams();
        requestParams.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MWB_Base_URL + "concentratePunish!m_loadCust.action?privilegeFlag=VIEW&bean.id=" + id + "\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

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
                            showToast(response.getString("message"), DepartmentCustomerCheckingActivity.this);
                        }
                    } else {
                        showToast(response.getString("message"), DepartmentCustomerCheckingActivity.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(DepartmentCustomerCheckingActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(DepartmentCustomerCheckingActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(DepartmentCustomerCheckingActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_customer_checking);
        setToolbarTitle("经营户");
        showBack();

        assignViews();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(Constant.isRefresJZZZSGrid){
            finish();
        }

        if(Constant.isRefresJZZZS){
            loadCust();
            Constant.isRefresJZZZS = false;
            Constant.isRefresJZZZSGrid = true;
        }


    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>{

        @Override
        public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerHolder holder = new RecyclerHolder(LayoutInflater.from(DepartmentCustomerCheckingActivity.this).inflate(R.layout.activity_department_customer_checking_recyclerview_item, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerHolder holder, int position) {

            final JSONObject object = datas.get(position);

            try {

                JSONObject customer = object.getJSONObject("customer");

                holder.shopnameTv.setText(customer.getString("shopName"));
                holder.licenseTv.setText(customer.getString("liceNo"));
                holder.commonTv.setText(customer.getString("chargerName"));

                Integer measures = null;

                if(StringUtility.notObjEmpty(object, "measures")) measures = object.getInt("measures");

                if (measures !=null) {
                    if(measures != 1) {

                        String string = "";

                        if(measures == 2){
                            string = "出具《先行教育通知书》";
                        }else if(measures == 3){
                            string = "出具《责令整改通知书》";
                        }else if(measures == 4){
                            string = "涉案物品先行登记保存";
                        }

                        final String str = string;

                        holder.abnormalTv.setBackgroundResource(R.drawable.warning);
                        holder.abnormalTv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(DepartmentCustomerCheckingActivity.this);
                                builder.setMessage(str);
                                builder.setTitle("异常内容");
                                builder.setPositiveButton("关闭", null);
                                builder.create().show();
                            }
                        });
                    }else{
                        holder.abnormalTv.setBackgroundResource(R.drawable.unwarning);
                        holder.abnormalTv.setOnClickListener(null);
                    }
                } else {
                    holder.abnormalTv.setBackgroundResource(R.drawable.unwarning);
                    holder.abnormalTv.setOnClickListener(null);
                }

                int level = object.getInt("level");

                if(level == 1){
                    holder.departmentTv.setText("市管组");
                }else if(level == 2){
                    holder.departmentTv.setText("管理所");
                }else{
                    holder.departmentTv.setText("区局");
                }

                JSONObject concentratePunish = object.getJSONObject("concentratePunish");

                if(StringUtility.notObjEmpty(concentratePunish, "planCheckDate"))holder.dateTv.setText(concentratePunish.getString("planCheckDate").substring(0, 10));

                final String id = object.getString("id");

                final int idInt = object.getInt("id");

                final String concentratePunishId = concentratePunish.getString("id");

                final int checkStatus = object.getInt("checkStatus");

                if(checkStatus == 2){
                    holder.checkingTv.setText("开始检查");
                }else{
                    holder.checkingTv.setText("修改");
                }

                holder.checkingTv.setOnClickListener(new ViewClickVibrate() {
                    @Override
                    public void onClick(View v) {
                        super.onClick(v);
                        Intent intent = new Intent(DepartmentCustomerCheckingActivity.this, CheckingActivity.class);
                        intent.putExtra("id", idInt);
                        intent.putExtra("status", checkStatus);
                        intent.putExtra("concentratePunish", concentratePunishId);
                        intent.putExtra("noLiceRegistryId", "");
                        intent.putExtra("postType", 0);
                        intent.putExtra("noLice", false);
                        intent.putExtra("ck", ck);
                        startActivity(intent);
                    }
                });

                holder.delTv.setOnClickListener(new ViewClickVibrate() {
                    @Override
                    public void onClick(View v) {
                        super.onClick(v);
                        AlertDialog.Builder builder = new AlertDialog.Builder(DepartmentCustomerCheckingActivity.this);
                        builder.setMessage("删除检查对象");
                        builder.setTitle("提示");
                        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteMC(id);
                            }
                        });
                        builder.setNegativeButton("取消", null);
                        builder.create().show();
                    }
                });

                if(!ck) {
                    if (type == 1 && postType == 1) {
                        holder.delTv.setVisibility(View.VISIBLE);
                    } else {
                        holder.delTv.setVisibility(View.GONE);
                    }

                    holder.rl.setOnClickListener(null);
                }else{
                    holder.delTv.setVisibility(View.GONE);
                    holder.checkingTv.setVisibility(View.GONE);

                    holder.rl.setOnClickListener(new ViewClickVibrate(){

                        @Override
                        public void onClick(View view) {
                            super.onClick(view);

                            Intent intent = new Intent(DepartmentCustomerCheckingActivity.this, CheckingActivity.class);
                            intent.putExtra("id", idInt);
                            intent.putExtra("status", 0);
                            intent.putExtra("postType", 0);
                            intent.putExtra("ck", true);
                            startActivity(intent);
                        }
                    });
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        private void deleteMC(String id){

            final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
            pd.show();

            RequestParams requestParams = new RequestParams();
            requestParams.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MWB_Base_URL + "concentratePunish!m_deleteMC.action?privilegeFlag=VIEW&detailId="+id+"\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

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
                                loadCust();
                            } else {
                                showToast(response.getString("message"), DepartmentCustomerCheckingActivity.this);
                            }
                        } else {
                            showToast(response.getString("message"), DepartmentCustomerCheckingActivity.this);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    pd.dismiss();
                    showNetworkError(DepartmentCustomerCheckingActivity.this);
                    super.onFailure(statusCode, headers, responseString, throwable);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    pd.dismiss();
                    showNetworkError(DepartmentCustomerCheckingActivity.this);
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    pd.dismiss();
                    showNetworkError(DepartmentCustomerCheckingActivity.this);
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        class RecyclerHolder extends RecyclerView.ViewHolder{

            private TextView licenseTv;
            private TextView commonTv;
            private TextView departmentTv;
            private TextView dateTv;
            private TextView shopnameTv;
            private TextView abnormalTv;
            private TextView delTv;
            private TextView checkingTv;
            private RelativeLayout rl;

            private void assignViews(View itemView) {
                licenseTv = (TextView) itemView.findViewById(R.id.license_tv);
                commonTv = (TextView) itemView.findViewById(R.id.common_tv);
                departmentTv = (TextView) itemView.findViewById(R.id.department_tv);
                dateTv = (TextView) itemView.findViewById(R.id.date_tv);
                shopnameTv = (TextView) itemView.findViewById(R.id.shopname_tv);
                abnormalTv = (TextView) itemView.findViewById(R.id.abnormal_tv);
                delTv = (TextView) itemView.findViewById(R.id.del_tv);
                checkingTv = (TextView) itemView.findViewById(R.id.checking_tv);
                rl = (RelativeLayout) itemView.findViewById(R.id.rl);
            }


            public RecyclerHolder(View itemView) {
                super(itemView);

                assignViews(itemView);
            }
        }
    }
}
