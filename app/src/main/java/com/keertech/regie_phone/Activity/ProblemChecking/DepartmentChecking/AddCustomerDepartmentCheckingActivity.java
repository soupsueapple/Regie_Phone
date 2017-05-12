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

import static com.keertech.regie_phone.R.id.recycler_view;

/**
 * Created by soup on 2017/5/12.
 */

public class AddCustomerDepartmentCheckingActivity extends BaseActivity{

    private LinearLayout linearLayout11;
    private TextView addTv;
    private TextView netxTv;
    private TextView doneTv;
    private RecyclerView recyclerView;

    ArrayList<JSONObject> datas = new ArrayList<>();

    RecyclerAdapter recyclerAdapter = new RecyclerAdapter();

    String id = "";

    private void assignViews() {
        linearLayout11 = (LinearLayout) findViewById(R.id.linearLayout11);
        addTv = (TextView) findViewById(R.id.add_tv);
        netxTv = (TextView) findViewById(R.id.netx_tv);
        doneTv = (TextView) findViewById(R.id.done_tv);
        recyclerView = (RecyclerView) findViewById(recycler_view);

        addTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);

                Intent intent = new Intent(AddCustomerDepartmentCheckingActivity.this, ChooseCustomerActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        netxTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);

                if(datas.size() == 0){
                    showToast("请先添加经营户", AddCustomerDepartmentCheckingActivity.this);

                    return;
                }

                Intent intent = new Intent(AddCustomerDepartmentCheckingActivity.this, AddNotHasLicenseActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        doneTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);

                if(datas.size() == 0){
                    showToast("请先添加经营户", AddCustomerDepartmentCheckingActivity.this);

                    return;
                }

                Constant.isRefresJZZZSFinish = true;
                finish();
            }
        });

        id = getIntent().getStringExtra("id");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);

        loadCust();
    }

    private void loadCust(){
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams requestParams = new RequestParams();
        requestParams.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MWB_Base_URL + "concentratePunish!m_loadCust.action?privilegeFlag=VIEW&bean.id="+id+"\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

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

                            if(datas.size() > 0) datas.clear();

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject object = data.getJSONObject(i);

                                datas.add(object);
                            }

                            recyclerAdapter.notifyDataSetChanged();

                        } else {
                            showToast(response.getString("message"), AddCustomerDepartmentCheckingActivity.this);
                        }
                    } else {
                        showToast(response.getString("message"), AddCustomerDepartmentCheckingActivity.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(AddCustomerDepartmentCheckingActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(AddCustomerDepartmentCheckingActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(AddCustomerDepartmentCheckingActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer_department_checking);
        setToolbarTitle("新增经营户");
        showBack();

        assignViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Constant.isRefresJZZZS){
            loadCust();
            Constant.isRefresJZZZS = false;
        }
        if(Constant.isRefresJZZZSFinish){
            finish();
        }
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>{

        @Override
        public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerHolder holder = new RecyclerHolder(LayoutInflater.from(AddCustomerDepartmentCheckingActivity.this).inflate(R.layout.activity_add_customer_department_checking_recyclerview_item, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerHolder holder, int position) {
            JSONObject object = datas.get(position);

            try {
                JSONObject customer = object.getJSONObject("customer");

                final String id = object.getString("id");

                holder.addressTv.setText(customer.getString("shopName"));
                holder.licenseTv.setText(customer.getString("liceNo"));
                holder.nameTv.setText(customer.getString("chargerName"));
                holder.delTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddCustomerDepartmentCheckingActivity.this);
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
                                showToast(response.getString("message"), AddCustomerDepartmentCheckingActivity.this);
                            }
                        } else {
                            showToast(response.getString("message"), AddCustomerDepartmentCheckingActivity.this);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    pd.dismiss();
                    showNetworkError(AddCustomerDepartmentCheckingActivity.this);
                    super.onFailure(statusCode, headers, responseString, throwable);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    pd.dismiss();
                    showNetworkError(AddCustomerDepartmentCheckingActivity.this);
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    pd.dismiss();
                    showNetworkError(AddCustomerDepartmentCheckingActivity.this);
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
            private TextView nameTv;
            private TextView addressTv;
            private TextView delTv;

            private void assignViews(View itemView) {
                licenseTv = (TextView) itemView.findViewById(R.id.license_tv);
                nameTv = (TextView) itemView.findViewById(R.id.name_tv);
                addressTv = (TextView) itemView.findViewById(R.id.address_tv);
                delTv = (TextView) itemView.findViewById(R.id.del_tv);
            }


            public RecyclerHolder(View itemView) {
                super(itemView);
                assignViews(itemView);
            }
        }
    }
}
