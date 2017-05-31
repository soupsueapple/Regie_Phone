package com.keertech.regie_phone.Activity.SalesInteraction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.keertech.regie_phone.Activity.NothaveLicenseCustomer.NothaveLicenseCustomerInfoActivity;
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

/**
 * Created by soup on 2017/5/25.
 */

public class NotHaveLicenseCustomerInformationReportTableActivity extends BaseActivity{

    private EditText shopnameEt;
    private EditText addressEt;
    private EditText nameEt;
    private EditText phoneEt;
    private CheckBox bzyxY;
    private CheckBox bzyxW;
    private TextView registerTv;
    private TextView notTrueTv;

    JSONObject object;

    private void assignViews() {
        shopnameEt = (EditText) findViewById(R.id.shopname_et);
        addressEt = (EditText) findViewById(R.id.address_et);
        nameEt = (EditText) findViewById(R.id.name_et);
        phoneEt = (EditText) findViewById(R.id.phone_et);
        bzyxY = (CheckBox) findViewById(R.id.bzyx_y);
        bzyxW = (CheckBox) findViewById(R.id.bzyx_w);
        registerTv = (TextView) findViewById(R.id.register_tv);
        notTrueTv = (TextView) findViewById(R.id.not_true_tv);

        if(getIntent().getIntExtra("resultStatus", 0) == 1 || getIntent().getIntExtra("resultStatus", 0) == 2){
            registerTv.setVisibility(View.GONE);
            notTrueTv.setVisibility(View.GONE);
        } else {
            registerTv.setVisibility(View.VISIBLE);
            notTrueTv.setVisibility(View.VISIBLE);
        }

        notTrueTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                hdNoLicenseSubmitVeto();
            }
        });

        registerTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                try {
                    String shopName = object.getString("shopName");
                    String name = object.getString("name");
                    String address = object.getString("address");

                    Constant.isWZHTB = true;

                    Intent intent = new Intent(NotHaveLicenseCustomerInformationReportTableActivity.this, NothaveLicenseCustomerInfoActivity.class);
                    intent.putExtra("id","");
                    intent.putExtra("relationId",getIntent().getStringExtra("id"));
                    intent.putExtra("sourceType","06");
                    intent.putExtra("name",name);
                    intent.putExtra("shopname",shopName);
                    intent.putExtra("address",address);
                    intent.putExtra("title", "创建");
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        hdNoLicenseSubmitFindBean(getIntent().getStringExtra("id"));
    }

    private void hdNoLicenseSubmitFindBean(String id){
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams requestParams = new RequestParams();
        requestParams.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\""+ Constant.MWB_Base_URL+"hdNoLicenseSubmit!findBean.action?privilegeFlag=VIEW&id="+id+"\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

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

                            object = message.getJSONObject("data");

                            String shopName = object.getString("shopName");
                            String name = object.getString("name");
                            String phone = object.getString("phone");
                            String address = object.getString("address");
                            int hasDesire = object.getInt("hasDesire");

                            shopnameEt.setText(shopName);
                            nameEt.setText(name);
                            phoneEt.setText(phone);
                            addressEt.setText(address);

                            if(hasDesire == 1) bzyxY.setChecked(true); else bzyxW.setChecked(true);

                        } else {
                            showToast(response.getString("message"), NotHaveLicenseCustomerInformationReportTableActivity.this);
                        }
                    } else {
                        showToast(response.getString("message"), NotHaveLicenseCustomerInformationReportTableActivity.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(NotHaveLicenseCustomerInformationReportTableActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(NotHaveLicenseCustomerInformationReportTableActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(NotHaveLicenseCustomerInformationReportTableActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void hdNoLicenseSubmitVeto(){
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams requestParams = new RequestParams();
        requestParams.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\""+ Constant.MWB_Base_URL+"hdNoLicenseSubmit!veto.action?privilegeFlag=EDIT&id="+getIntent().getStringExtra("id")+"\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

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

                            Constant.isRefresWZH = true;

                            finish();

                        } else {
                            showToast(response.getString("message"), NotHaveLicenseCustomerInformationReportTableActivity.this);
                        }
                    } else {
                        showToast(response.getString("message"), NotHaveLicenseCustomerInformationReportTableActivity.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(NotHaveLicenseCustomerInformationReportTableActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(NotHaveLicenseCustomerInformationReportTableActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(NotHaveLicenseCustomerInformationReportTableActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nothave_license_customer_information_report_table);
        setToolbarTitle("无证户信息");
        showBack();

        assignViews();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(Constant.isWZHTBFinish){
            Constant.isWZHTBFinish = false;
            Constant.isRefresWZH = true;

            finish();
        }
    }
}
