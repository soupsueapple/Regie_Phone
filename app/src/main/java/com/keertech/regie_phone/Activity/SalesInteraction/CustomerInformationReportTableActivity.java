package com.keertech.regie_phone.Activity.SalesInteraction;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
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

/**
 * Created by soup on 2017/5/25.
 */

public class CustomerInformationReportTableActivity extends BaseActivity{

    private EditText licenseEt;
    private EditText shopnameEt;
    private EditText nameEt;
    private EditText corpnameEt;
    private EditText commonEt;
    private EditText deptnameEt;
    private EditText addressEt;
    private CheckBox operatorTransactionYesCb;
    private CheckBox operatorTransactionNoCb;
    private CheckBox addressTransactionYesCb;
    private CheckBox addressTransactionNoCb;
    private CheckBox notShowLicenseYesCb;
    private CheckBox notShowLicenseNoCb;
    private CheckBox secretSalesLawlessnessBrandYesCb;
    private CheckBox secretSalesLawlessnessBrandNoCb;
    private CheckBox publicitySalesLawlessnessBrandYesCb;
    private CheckBox publicitySalesLawlessnessBrandNoCb;
    private CheckBox lawlessnessChannelYesCb;
    private CheckBox lawlessnessChannelNoCb;
    private CheckBox salesFakeCigaretteYesCb;
    private CheckBox salesFakeCigaretteNoCb;
    private TextView toCheckingTv;
    private TextView notTrueTv;

    private void assignViews() {
        licenseEt = (EditText) findViewById(R.id.license_et);
        shopnameEt = (EditText) findViewById(R.id.shopname_et);
        nameEt = (EditText) findViewById(R.id.name_et);
        corpnameEt = (EditText) findViewById(R.id.corpname_et);
        commonEt = (EditText) findViewById(R.id.common_et);
        deptnameEt = (EditText) findViewById(R.id.deptname_et);
        addressEt = (EditText) findViewById(R.id.address_et);
        operatorTransactionYesCb = (CheckBox) findViewById(R.id.operator_transaction_yes_cb);
        operatorTransactionNoCb = (CheckBox) findViewById(R.id.operator_transaction_no_cb);
        addressTransactionYesCb = (CheckBox) findViewById(R.id.address_transaction_yes_cb);
        addressTransactionNoCb = (CheckBox) findViewById(R.id.address_transaction_no_cb);
        notShowLicenseYesCb = (CheckBox) findViewById(R.id.not_show_license_yes_cb);
        notShowLicenseNoCb = (CheckBox) findViewById(R.id.not_show_license_no_cb);
        secretSalesLawlessnessBrandYesCb = (CheckBox) findViewById(R.id.secret_sales_lawlessness_brand_yes_cb);
        secretSalesLawlessnessBrandNoCb = (CheckBox) findViewById(R.id.secret_sales_lawlessness_brand_no_cb);
        publicitySalesLawlessnessBrandYesCb = (CheckBox) findViewById(R.id.publicity_sales_lawlessness_brand_yes_cb);
        publicitySalesLawlessnessBrandNoCb = (CheckBox) findViewById(R.id.publicity_sales_lawlessness_brand_no_cb);
        lawlessnessChannelYesCb = (CheckBox) findViewById(R.id.lawlessness_channel_yes_cb);
        lawlessnessChannelNoCb = (CheckBox) findViewById(R.id.lawlessness_channel_no_cb);
        salesFakeCigaretteYesCb = (CheckBox) findViewById(R.id.sales_fake_cigarette_yes_cb);
        salesFakeCigaretteNoCb = (CheckBox) findViewById(R.id.sales_fake_cigarette_no_cb);
        toCheckingTv = (TextView) findViewById(R.id.to_checking_tv);
        notTrueTv = (TextView) findViewById(R.id.not_true_tv);

        if(getIntent().getIntExtra("resultStatus", 0) == 1 || getIntent().getIntExtra("resultStatus", 0) == 2) {
            toCheckingTv.setVisibility(View.GONE);
            notTrueTv.setVisibility(View.GONE);
        } else {
            toCheckingTv.setVisibility(View.VISIBLE);
            notTrueTv.setVisibility(View.VISIBLE);
        }

        toCheckingTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                hdCustomerSubmitSure();
            }
        });

        notTrueTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                hdCustomerSubmitVeto();
            }
        });

        hdNoLicenseSubmitFindBean();
    }

    private void hdCustomerSubmitVeto(){
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams requestParams = new RequestParams();
        requestParams.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\""+ Constant.MWB_Base_URL+"hdCustomerSubmit!veto.action?privilegeFlag=EDIT&id="+getIntent().getStringExtra("id")+"\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

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

                            Constant.isRefresYZHBSS = true;

                            finish();

                        } else {
                            showToast(response.getString("message"), CustomerInformationReportTableActivity.this);
                        }
                    } else {
                        showToast(response.getString("message"), CustomerInformationReportTableActivity.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(CustomerInformationReportTableActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(CustomerInformationReportTableActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(CustomerInformationReportTableActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void hdCustomerSubmitSure(){
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams requestParams = new RequestParams();
        requestParams.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\""+ Constant.MWB_Base_URL+"hdCustomerSubmit!sure.action?privilegeFlag=EDIT&id="+getIntent().getStringExtra("id")+"\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

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

                            Constant.isRefresYZHBSS = true;

                            finish();

                        } else {
                            showToast(response.getString("message"), CustomerInformationReportTableActivity.this);
                        }
                    } else {
                        showToast(response.getString("message"), CustomerInformationReportTableActivity.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(CustomerInformationReportTableActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(CustomerInformationReportTableActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(CustomerInformationReportTableActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void hdNoLicenseSubmitFindBean(){
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams requestParams = new RequestParams();
        requestParams.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\""+ Constant.MWB_Base_URL+"hdCustomerSubmit!findBean.action?privilegeFlag=VIEW&id="+getIntent().getStringExtra("id")+"\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

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

                            JSONObject marketername = message.getJSONObject("data");

                            String liceno = marketername.getString("liceno");
                            String shopname = marketername.getString("shopname");
                            String chargername = marketername.getString("chargername");
                            String corpname = marketername.getString("corpname");
                            String deptname = marketername.getString("deptname");
                            String name = marketername.getString("marketername");
                            String shopaddress = marketername.getString("shopaddress");

                            licenseEt.setText(liceno);
                            shopnameEt.setText(shopname);
                            nameEt.setText(chargername);
                            corpnameEt.setText(corpname);
                            commonEt.setText(name);
                            deptnameEt.setText(deptname);
                            addressEt.setText(shopaddress);

                            int isLightCard = marketername.getInt("isLightCard");
                            if(isLightCard == 1) notShowLicenseYesCb.setChecked(true);
                            else notShowLicenseNoCb.setChecked(true);

                            int isRzConform = marketername.getInt("isRzConform");
                            if(isRzConform == 1) operatorTransactionYesCb.setChecked(true);
                            else operatorTransactionNoCb.setChecked(true);

                            int isAddressChange = marketername.getInt("isAddressChange");
                            if(isAddressChange == 1) addressTransactionYesCb.setChecked(true);
                            else addressTransactionNoCb.setChecked(true);

                            int isAZSM = marketername.getInt("isAZSM");
                            if(isAZSM == 1) secretSalesLawlessnessBrandYesCb.setChecked(true);
                            else secretSalesLawlessnessBrandNoCb.setChecked(true);

                            int isGKBM = marketername.getInt("isGKBM");
                            if(isGKBM == 1) publicitySalesLawlessnessBrandYesCb.setChecked(true);
                            else publicitySalesLawlessnessBrandNoCb.setChecked(true);

                            int isFQD = marketername.getInt("isFQD");
                            if(isFQD == 1) lawlessnessChannelYesCb.setChecked(true);
                            else lawlessnessChannelNoCb.setChecked(true);

                            int isJY = marketername.getInt("isJY");
                            if(isJY == 1) salesFakeCigaretteYesCb.setChecked(true);
                            else salesFakeCigaretteNoCb.setChecked(true);

                        } else {
                            showToast(response.getString("message"), CustomerInformationReportTableActivity.this);
                        }
                    } else {
                        showToast(response.getString("message"), CustomerInformationReportTableActivity.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(CustomerInformationReportTableActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(CustomerInformationReportTableActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(CustomerInformationReportTableActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_information_report_table);
        setToolbarTitle("有证户信息");
        showBack();

        assignViews();
    }
}
