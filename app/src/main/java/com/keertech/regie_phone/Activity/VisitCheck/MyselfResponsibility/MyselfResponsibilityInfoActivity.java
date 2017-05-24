package com.keertech.regie_phone.Activity.VisitCheck.MyselfResponsibility;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
 * Created by soup on 2017/5/24.
 */

public class MyselfResponsibilityInfoActivity extends BaseActivity{

    ArrayList<JSONObject> datas = new ArrayList<>();

    RecyclerAdapter recyclerAdapter = new RecyclerAdapter();

    String taskId = "", yzhId = "", wzhId = "";

    int isAZSM = 0, isGKBM = 0, isWZJY = 0;

    String[] names, ids;

    final int YZH = 1, WZH = 2;

    int type = 0;

    private TextView shopnameTv;
    private TextView checkedDateTv;
    private TextView addressTv;
    private TextView tv1;
    private RadioGroup secretSalesGp;
    private RadioButton secretSalesYesRb;
    private RadioButton secretSalesNoRb;
    private RadioGroup publiclySalesGp;
    private TextView textView24;
    private RadioButton publiclySalesYesRb;
    private RadioButton publiclySalesNoRb;
    private RadioGroup notlicenseSalesGp;
    private RadioButton notlicenseSalesYesRb;
    private RadioButton notlicenseSalesNoRb;
    private TextView textView25;
    private RecyclerView recyclerView;
    private LinearLayout licenseLl;
    private CheckBox licenseCb;
    private EditText licenseCustomerTv;
    private TextView matchLicenseTv;
    private TextView cancelLicenseTv;
    private LinearLayout notLicenseLl;
    private CheckBox notLicenseCb;
    private EditText notLicenseCustomerTv;
    private TextView matchNotLicenseTv;
    private TextView cancelNotLicenseTv;
    private LinearLayout ll;
    private EditText returnReasonEt;
    private TextView verifyTv;
    private TextView returnTv;

    private void assignViews() {
        shopnameTv = (TextView) findViewById(R.id.shopname_tv);
        checkedDateTv = (TextView) findViewById(R.id.checked_date_tv);
        addressTv = (TextView) findViewById(R.id.address_tv);
        tv1 = (TextView) findViewById(R.id.tv1);
        secretSalesGp = (RadioGroup) findViewById(R.id.secret_sales_gp);
        secretSalesYesRb = (RadioButton) findViewById(R.id.secret_sales_yes_rb);
        secretSalesNoRb = (RadioButton) findViewById(R.id.secret_sales_no_rb);
        publiclySalesGp = (RadioGroup) findViewById(R.id.publicly_sales_gp);
        textView24 = (TextView) findViewById(R.id.textView24);
        publiclySalesYesRb = (RadioButton) findViewById(R.id.publicly_sales_yes_rb);
        publiclySalesNoRb = (RadioButton) findViewById(R.id.publicly_sales_no_rb);
        notlicenseSalesGp = (RadioGroup) findViewById(R.id.notlicense_sales_gp);
        notlicenseSalesYesRb = (RadioButton) findViewById(R.id.notlicense_sales_yes_rb);
        notlicenseSalesNoRb = (RadioButton) findViewById(R.id.notlicense_sales_no_rb);
        textView25 = (TextView) findViewById(R.id.textView25);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        licenseLl = (LinearLayout) findViewById(R.id.license_ll);
        licenseCb = (CheckBox) findViewById(R.id.license_cb);
        licenseCustomerTv = (EditText) findViewById(R.id.license_customer_tv);
        matchLicenseTv = (TextView) findViewById(R.id.match_license_tv);
        cancelLicenseTv = (TextView) findViewById(R.id.cancel_license_tv);
        notLicenseLl = (LinearLayout) findViewById(R.id.not_license_ll);
        notLicenseCb = (CheckBox) findViewById(R.id.not_license_cb);
        notLicenseCustomerTv = (EditText) findViewById(R.id.not_license_customer_tv);
        matchNotLicenseTv = (TextView) findViewById(R.id.match_not_license_tv);
        cancelNotLicenseTv = (TextView) findViewById(R.id.cancel_not_license_tv);
        ll = (LinearLayout) findViewById(R.id.ll);
        returnReasonEt = (EditText) findViewById(R.id.return_reason_et);
        verifyTv = (TextView) findViewById(R.id.verify_tv);
        returnTv = (TextView) findViewById(R.id.return_tv);

        publiclySalesGp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.publicly_sales_yes_rb){
                    isGKBM = 1;
                }else if(i == R.id.publicly_sales_no_rb){
                    isGKBM = 0;
                }
            }
        });

        notlicenseSalesGp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.notlicense_sales_yes_rb){
                    isWZJY = 1;
                }else if(i == R.id.notlicense_sales_no_rb){
                    isWZJY = 0;
                }
            }
        });

        secretSalesGp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.secret_sales_yes_rb){
                    isAZSM = 1;
                }else if(i == R.id.secret_sales_no_rb){
                    isAZSM = 0;
                }
            }
        });

        cancelLicenseTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                yzhId = "";
                licenseCustomerTv.setText("");
                licenseCb.setChecked(false);
            }
        });

        cancelNotLicenseTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                wzhId = "";
                notLicenseCustomerTv.setText("");
                notLicenseCb.setChecked(false);
            }
        });

        matchLicenseTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                Intent intent = new Intent(MyselfResponsibilityInfoActivity.this, ChooseCustomerActivity.class);
                startActivityForResult(intent, YZH);
            }
        });

        matchNotLicenseTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                Intent intent = new Intent(MyselfResponsibilityInfoActivity.this, ChooseNotHasLicenseActivity.class);
                startActivityForResult(intent, WZH);
            }
        });

        verifyTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                intoCheck();
            }
        });

        returnTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                back();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);

        taskId = getIntent().getStringExtra("taskId");

        findBean();
    }

    private void back() {
        if(returnReasonEt.getText().toString().length() == 0){
            showToast("请输入退回理由", this);

            return;
        }

        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams params = new RequestParams();
        params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MWB_Base_URL + "marketSecretlyCheckFlow!back.action?taskId="+taskId+"&name=退回&flowStatus=8&returnReasons="+returnReasonEt.getText().toString()+"\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

        HttpClient.post(Constant.EXEC, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                pd.dismiss();

                try {
                    if(StringUtility.isSuccess(response)) {
                        String messageSting = response.getString("message");

                        JSONObject message = new JSONObject(messageSting);

                        if (StringUtility.isSuccess(message)) {

                            Constant.isRefreshBRZR = true;
                            finish();

                        } else {
                            showToast(message.getString("message"), MyselfResponsibilityInfoActivity.this);
                        }
                    }else{
                        showToast(response.getString("message"), MyselfResponsibilityInfoActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(MyselfResponsibilityInfoActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(MyselfResponsibilityInfoActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(MyselfResponsibilityInfoActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void intoCheck() {

        if(type != 3) {

            if (yzhId.length() == 0 && wzhId.length() == 0) {
                showToast("请选择有证户或无证户", this);

                return;
            }

            if (yzhId.length() > 0 && wzhId.length() > 0) {
                showToast("有证户和无证户只能选其一", this);

                return;
            }
        }

        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams params = new RequestParams();
        params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MWB_Base_URL + "marketSecretlyCheckFlow!intoCheck.action?taskId="+taskId+"&name=纳入检查&flowStatus=7&customerId="+yzhId+"&noLiceRegistryId="+wzhId+"\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

        HttpClient.post(Constant.EXEC, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                pd.dismiss();

                try {
                    if(StringUtility.isSuccess(response)) {
                        String messageSting = response.getString("message");

                        JSONObject message = new JSONObject(messageSting);

                        if (StringUtility.isSuccess(message)) {

                            Constant.isRefreshBRZR = true;
                            finish();

                        } else {
                            showToast(message.getString("message"), MyselfResponsibilityInfoActivity.this);
                        }
                    }else{
                        showToast(response.getString("message"), MyselfResponsibilityInfoActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(MyselfResponsibilityInfoActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(MyselfResponsibilityInfoActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(MyselfResponsibilityInfoActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case YZH:
                    yzhId = data.getStringExtra("id");
                    licenseCustomerTv.setText(data.getStringExtra("liceNo"));
                    licenseCb.setChecked(true);
                    break;
                case WZH:
                    wzhId = data.getStringExtra("id");
                    notLicenseCustomerTv.setText(data.getStringExtra("name"));
                    notLicenseCb.setChecked(true);
                    break;
            }
        }
    }

    private void findBean() {
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams params = new RequestParams();
        params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MWB_Base_URL + "marketSecretlyCheckFlow!findBean.action?taskId="+taskId+"\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

        HttpClient.post(Constant.EXEC, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                pd.dismiss();

                try {
                    if(StringUtility.isSuccess(response)) {
                        String messageSting = response.getString("message");

                        JSONObject message = new JSONObject(messageSting);

                        if (StringUtility.isSuccess(message)) {

                            JSONObject data = message.getJSONObject("data");

                            shopnameTv.setText( data.getString("shopName"));
                            addressTv.setText(data.getString("shopAddress"));
                            checkedDateTv.setText(data.getString("checkDate").substring(0, 10));

                            isAZSM = data.getInt("isAZSM");
                            isWZJY = data.getInt("isWZJY");
                            isGKBM = data.getInt("isGKBM");

                            if(isAZSM == 1){
                                secretSalesYesRb.setChecked(true);
                                secretSalesNoRb.setChecked(false);

                            }else{
                                secretSalesYesRb.setChecked(false);
                                secretSalesYesRb.setChecked(true);

                            }

                            if(isGKBM == 1){
                                publiclySalesYesRb.setChecked(true);
                                publiclySalesNoRb.setChecked(false);

                            }else{
                                publiclySalesYesRb.setChecked(false);
                                publiclySalesNoRb.setChecked(true);

                            }

                            if(isWZJY == 1){
                                notlicenseSalesYesRb.setChecked(true);
                                notlicenseSalesNoRb.setChecked(false);


                            }else{
                                notlicenseSalesYesRb.setChecked(false);
                                notlicenseSalesNoRb.setChecked(true);
                            }

                            String id = data.getString("id");

                            findDetails(id);

                            type = data.getInt("type");

                            if(type == 3){

                                int hasLiceno = data.getInt("hasLiceno");



                                if(hasLiceno == 1) {
                                    JSONObject customer = data.getJSONObject("customer");

                                    yzhId = customer.getString("id");

                                    licenseCb.setVisibility(View.GONE);
                                    matchLicenseTv.setVisibility(View.GONE);
                                    cancelLicenseTv.setVisibility(View.GONE);

                                    notLicenseCb.setVisibility(View.GONE);
                                    notLicenseCustomerTv.setVisibility(View.GONE);
                                    matchNotLicenseTv.setVisibility(View.GONE);
                                    cancelNotLicenseTv.setVisibility(View.GONE);

                                    licenseCustomerTv.setText(data.getString("liceno"));

                                    returnTv.setVisibility(View.GONE);
                                    ll.setVisibility(View.GONE);
                                }

                            }

                        } else {
                            showToast(message.getString("message"), MyselfResponsibilityInfoActivity.this);
                        }
                    }else{
                        showToast(response.getString("message"), MyselfResponsibilityInfoActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(MyselfResponsibilityInfoActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(MyselfResponsibilityInfoActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(MyselfResponsibilityInfoActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void findDetails(String id) {
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams params = new RequestParams();
        params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MWB_Base_URL + "marketSecretlyCheckFlow!findDetails.action?bean.id="+id+"\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

        HttpClient.post(Constant.EXEC, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                pd.dismiss();

                try {
                    if(StringUtility.isSuccess(response)) {
                        String messageSting = response.getString("message");

                        JSONObject message = new JSONObject(messageSting);

                        if (StringUtility.isSuccess(message)) {

                            JSONArray data = message.getJSONArray("data");

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject object = data.getJSONObject(i);
                                datas.add(object);
                            }

                            recyclerAdapter.notifyDataSetChanged();

                        } else {
                            showToast(message.getString("message"), MyselfResponsibilityInfoActivity.this);
                        }
                    }else{
                        showToast(response.getString("message"), MyselfResponsibilityInfoActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(MyselfResponsibilityInfoActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(MyselfResponsibilityInfoActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(MyselfResponsibilityInfoActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myself_responsibility_info);
        setToolbarTitle("详情");
        showBack();

        assignViews();
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>{

        @Override
        public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerHolder holder = new RecyclerHolder(LayoutInflater.from(MyselfResponsibilityInfoActivity.this).inflate(R.layout.theplace_responsibility_info_recyclerview_item, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerHolder holder, int position) {
            JSONObject object = datas.get(position);

            try {
                holder.tv1.setText(object.getString("cigName"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        class RecyclerHolder extends RecyclerView.ViewHolder{

            TextView tv1;

            public RecyclerHolder(View itemView) {
                super(itemView);
                tv1 = (TextView) itemView.findViewById(R.id.tv1);
            }
        }
    }

}
