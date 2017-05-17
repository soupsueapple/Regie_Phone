package com.keertech.regie_phone.Activity.ReportComplanints.ToBeContinued;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.keertech.regie_phone.Adapter.SetAdapter;
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
 * Created by soup on 2017/5/16.
 */

public class ToBeContinuedActivity extends BaseActivity{

    private int type;

    private int reportcomplaintype;

    private String taskid;

    private String id;

    private String[] zpKey;
    private String[] zpValue;

    private String phoneNo = "";

    String cigLicenseNo = "";
    private String tradename = "";

    private TextView licenseTv;
    private TextView phoneTv;
    private TextView shopnameTv;
    private TextView typeTv;
    private TextView nameTv;
    private TextView timeTv;
    private TextView textView9;
    private TextView contentTv;
    private TextView textView10;
    private TextView workTypeTv;
    private TextView textView13;
    private TextView brandTv;
    private LinearLayout linearLayout14;
    private TextView textView14;
    private TextView toBeContinuedInfoTv;
    private EditText toBeContinuedInfoEt;
    private LinearLayout disposeLl;
    private TextView disposeTv;
    private TextView rejectTv;
    private TextView toCheckingTv;

    private void assignViews() {
        licenseTv = (TextView) findViewById(R.id.license_tv);
        phoneTv = (TextView) findViewById(R.id.phone_tv);
        shopnameTv = (TextView) findViewById(R.id.shopname_tv);
        typeTv = (TextView) findViewById(R.id.type_tv);
        nameTv = (TextView) findViewById(R.id.name_tv);
        timeTv = (TextView) findViewById(R.id.time_tv);
        textView9 = (TextView) findViewById(R.id.textView9);
        contentTv = (TextView) findViewById(R.id.content_tv);
        textView10 = (TextView) findViewById(R.id.textView10);
        workTypeTv = (TextView) findViewById(R.id.work_type_tv);
        textView13 = (TextView) findViewById(R.id.textView13);
        brandTv = (TextView) findViewById(R.id.brand_tv);
        linearLayout14 = (LinearLayout) findViewById(R.id.linearLayout14);
        textView14 = (TextView) findViewById(R.id.textView14);
        toBeContinuedInfoTv = (TextView) findViewById(R.id.to_be_continued_info_tv);
        toBeContinuedInfoEt = (EditText) findViewById(R.id.to_be_continued_info_et);
        disposeLl = (LinearLayout) findViewById(R.id.dispose_ll);
        disposeTv = (TextView) findViewById(R.id.dispose_tv);
        rejectTv = (TextView) findViewById(R.id.reject_tv);
        toCheckingTv = (TextView) findViewById(R.id.to_checking_tv);

        licenseTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);

                View layout = LayoutInflater.from(ToBeContinuedActivity.this).inflate(R.layout.custom_edit_dialog3, (ViewGroup)findViewById(R.id.custom_dialog));
                final EditText  cigLicenseNo = (EditText)layout.findViewById(R.id.dialog_username_ed);
                cigLicenseNo.setInputType(InputType.TYPE_CLASS_NUMBER );
                final EditText tradename = (EditText)layout.findViewById(R.id.dialog_password_ed);

                new AlertDialog.Builder(ToBeContinuedActivity.this).setTitle("搜索客户").setView(layout).setPositiveButton("搜索", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        searchCustomerInfo(cigLicenseNo.getText().toString(), tradename.getText().toString());
                    }
                }).setNegativeButton("取消", null).show();
            }
        });

        shopnameTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                View layout = LayoutInflater.from(ToBeContinuedActivity.this).inflate(R.layout.custom_edit_dialog3, (ViewGroup)findViewById(R.id.custom_dialog));
                final EditText  cigLicenseNo = (EditText)layout.findViewById(R.id.dialog_username_ed);
                final EditText tradename = (EditText)layout.findViewById(R.id.dialog_password_ed);

                new AlertDialog.Builder(ToBeContinuedActivity.this).setTitle("搜索客户").setView(layout).setPositiveButton("搜索", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        searchCustomerInfo(cigLicenseNo.getText().toString(), tradename.getText().toString());
                    }
                }).setNegativeButton("取消", null).show();
            }
        });

        phoneTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                if(!StringUtility.isEmpty(phoneNo)){
                    Intent phoneIntent = new Intent(
                            "android.intent.action.CALL", Uri.parse("tel:"
                            + phoneNo));
                    startActivity(phoneIntent);
                }
            }
        });

        contentTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                if(contentTv.getText().toString().length()> 0){
                    showToast(contentTv.getText().toString(), ToBeContinuedActivity.this);
                }
            }
        });

        disposeTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                if(toBeContinuedInfoTv.getText().toString().length()>0){
                    if(reportcomplaintype==2 && type==1){
                        doBl1();
                    }else if(reportcomplaintype==3 && type==1){
                        doBl2();
                    }
                }else {
                    showToast("请输入处理结果", ToBeContinuedActivity.this);
                }
            }
        });

        rejectTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                if(reportcomplaintype==2 && type==1){
                    doBh1();
                }else if(reportcomplaintype==3 && type==1){
                    doBh2();
                }
            }
        });

        toCheckingTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                final EditText editText = new EditText(ToBeContinuedActivity.this);

                new AlertDialog.Builder(ToBeContinuedActivity.this).setTitle("异常内容").setView(editText).setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!StringUtility.isEmpty(editText.getText().toString())) {
                                    uploadJCDXData(editText.getText().toString());
                                }
                            }
                        }).setNegativeButton("取消", null).show();
            }
        });

        if (reportcomplaintype == 2 && type == 2) {
            textView13.setVisibility(View.VISIBLE);
            brandTv.setVisibility(View.VISIBLE);
            toBeContinuedInfoTv.setVisibility(View.VISIBLE);

            disposeLl.setVisibility(View.GONE);
            toBeContinuedInfoEt.setVisibility(View.GONE);

            loadData1();
        } else if (reportcomplaintype == 3 && type == 2) {
            textView13.setVisibility(View.GONE);
            brandTv.setVisibility(View.GONE);
            toBeContinuedInfoTv.setVisibility(View.VISIBLE);

            disposeLl.setVisibility(View.GONE);
            toBeContinuedInfoEt.setVisibility(View.GONE);

            loadData2();
        } else if (reportcomplaintype == 2 && type == 1) {
            textView13.setVisibility(View.VISIBLE);
            brandTv.setVisibility(View.VISIBLE);
            toBeContinuedInfoTv.setVisibility(View.GONE);

            disposeLl.setVisibility(View.VISIBLE);
            toBeContinuedInfoEt.setVisibility(View.VISIBLE);

            loadData1();
        } else if (reportcomplaintype == 3 && type == 1) {
            textView13.setVisibility(View.GONE);
            brandTv.setVisibility(View.GONE);
            toBeContinuedInfoTv.setVisibility(View.GONE);

            disposeLl.setVisibility(View.VISIBLE);
            toBeContinuedInfoEt.setVisibility(View.VISIBLE);

            loadData2();
        }
    }

    private void uploadJCDXData(String context){

        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);

        RequestParams params = new RequestParams();
        params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\""+Constant.MWB_Base_URL+"marketInspect!handleMarketCheck4Pad.action?licenseNo="+cigLicenseNo+"&reason="+context+"&relationId="+id+"\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

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
                            showToast("已列为检查对象", ToBeContinuedActivity.this);
                        } else {
                            pd.dismiss();
                            showToast(message.getString("message"), ToBeContinuedActivity.this);
                        }
                    } else {
                        showToast(response.getString("message"), ToBeContinuedActivity.this);
                    }
                } catch (JSONException e) {
                    pd.dismiss();
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(ToBeContinuedActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(ToBeContinuedActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(ToBeContinuedActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void doBl1() {
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.sending);
        pd.show();

        RequestParams params = new RequestParams();
        params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MIIS_Base_URL + "reportManageFlow!process.action?taskId=" + taskid + "&name=反馈&bean.workOrderAnswer=" + toBeContinuedInfoEt.getText().toString() + "&bean.cigLicenseNo="+cigLicenseNo+"&bean.tradename="+tradename+"\",\"type\":\"WebExecutor\",\"method\":\"GET\"},\"app\":\"1003\"}");

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
                            Constant.isRefreshTSJB = true;
                            finish();
                        } else {
                            showToast(message.getString("message"), ToBeContinuedActivity.this);
                        }
                    } else {
                        showToast(response.getString("message"), ToBeContinuedActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(ToBeContinuedActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(ToBeContinuedActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(ToBeContinuedActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void doBl2() {
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.sending);
        pd.show();

        RequestParams params = new RequestParams();
        params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\""+Constant.MIIS_Base_URL+"complainManageFlow!process.action?taskId="+taskid+"&name=反馈&bean.workOrderAnswer="+toBeContinuedInfoEt.getText().toString()+"&bean.cigLicenseNo="+cigLicenseNo+"&bean.tradename="+tradename+"\",\"type\":\"WebExecutor\",\"method\":\"GET\"},\"app\":\"1003\"}");

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
                            Constant.isRefreshTSJB = true;
                            finish();
                        } else {
                            showToast(message.getString("message"), ToBeContinuedActivity.this);
                        }
                    } else {
                        showToast(response.getString("message"), ToBeContinuedActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(ToBeContinuedActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(ToBeContinuedActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(ToBeContinuedActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void doBh1() {
        final EditText editText = new EditText(this);
        new AlertDialog.Builder(this).setTitle("驳回理由").setView(editText).setPositiveButton("提交",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!StringUtility.isEmpty(editText.getText().toString())) {
                            final KeerAlertDialog pd = showKeerAlertDialog(R.string.sending);
                            pd.show();

                            RequestParams params = new RequestParams();
                            params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MIIS_Base_URL + "reportManageFlow!process.action?taskId=" + taskid + "&name=退回所长&bean.backReason=" + toBeContinuedInfoEt.getText().toString() + "&bean.cigLicenseNo="+cigLicenseNo+"\",\"type\":\"WebExecutor\",\"method\":\"GET\"},\"app\":\"1003\"}");

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
                                                Constant.isRefreshTSJB = true;
                                                finish();
                                            } else {
                                                showToast(message.getString("message"), ToBeContinuedActivity.this);
                                            }
                                        } else {
                                            showToast(response.getString("message"), ToBeContinuedActivity.this);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                    pd.dismiss();
                                    showNetworkError(ToBeContinuedActivity.this);
                                    super.onFailure(statusCode, headers, responseString, throwable);
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                    pd.dismiss();
                                    showNetworkError(ToBeContinuedActivity.this);
                                    super.onFailure(statusCode, headers, throwable, errorResponse);
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                                    pd.dismiss();
                                    showNetworkError(ToBeContinuedActivity.this);
                                    super.onFailure(statusCode, headers, throwable, errorResponse);
                                }
                            });
                        } else {
                            Toast.makeText(ToBeContinuedActivity.this, "请输入驳回理由", Toast.LENGTH_LONG).show();
                        }
                    }
                }).setNegativeButton("取消", null).show();


    }

    private void doBh2() {
        final EditText editText = new EditText(this);
        new AlertDialog.Builder(this).setTitle("驳回理由").setView(editText).setPositiveButton("提交",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!StringUtility.isEmpty(editText.getText().toString())) {
                            final KeerAlertDialog pd = showKeerAlertDialog(R.string.sending);
                            pd.show();

                            RequestParams params = new RequestParams();
                            params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MIIS_Base_URL + "complainManageFlow!process.action?taskId=" + taskid + "&name=退回所长&bean.backReason=" + editText.getText().toString() + "&bean.cigLicenseNo="+cigLicenseNo+"&bean.tradename="+tradename+"\",\"type\":\"WebExecutor\",\"method\":\"GET\"},\"app\":\"1003\"}");

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
                                                Constant.isRefreshTSJB = true;
                                                finish();
                                            } else {
                                                showToast(message.getString("message"), ToBeContinuedActivity.this);
                                            }
                                        } else {
                                            showToast(response.getString("message"), ToBeContinuedActivity.this);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                    pd.dismiss();
                                    showNetworkError(ToBeContinuedActivity.this);
                                    super.onFailure(statusCode, headers, responseString, throwable);
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                    pd.dismiss();
                                    showNetworkError(ToBeContinuedActivity.this);
                                    super.onFailure(statusCode, headers, throwable, errorResponse);
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                                    pd.dismiss();
                                    showNetworkError(ToBeContinuedActivity.this);
                                    super.onFailure(statusCode, headers, throwable, errorResponse);
                                }
                            });
                        } else {
                            Toast.makeText(ToBeContinuedActivity.this, "请输入驳回理由", Toast.LENGTH_LONG).show();
                        }
                    }
                }).setNegativeButton("取消", null).show();


    }

    private void loadData1() {
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams params = new RequestParams();
        params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MIIS_Base_URL + "reportManageFlow!findBean.action\",\"parameter\":{\"taskId\":\"" + taskid + "\"},\"type\":\"WebExecutor\",\"method\":\"POST\"},\"app\":\"1003\"}");

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

                            id = data.getString("id");

                            cigLicenseNo = data.getString("cigLicenseNo");
                            tradename = data.getString("tradename");
                            String customerName = data.getString("customerName");
                            phoneNo = data.getString("phoneNo");
                            String workOrderDate = data.getString("workOrderDate");
                            String workOrderContent = data.getString("workOrderContent");

                            JSONObject workOrderType = data.getJSONObject("workOrderType");
                            JSONObject parentWOType = workOrderType.getJSONObject("parentWOType");

                            if(reportcomplaintype == 2) {
                                String cigaretteBrand = data.getString("cigaretteBrand");
                                brandTv.setText(cigaretteBrand + " " );
                            }

                            String rejectReason = data.getString("rejectReason");
                            String backReason = data.getString("backReason");

                            if (StringUtility.isEmpty(rejectReason)) rejectReason = "";
                            if (StringUtility.isEmpty(backReason)) backReason = "";

                            licenseTv.setText(" 许可证号：" + cigLicenseNo);
                            shopnameTv.setText(" 商店名称：" + tradename);
                            nameTv.setText( customerName);
                            phoneTv.setText( phoneNo);
                            timeTv.setText(workOrderDate);
                            contentTv.setText(workOrderContent);
                            workTypeTv.setText(parentWOType.getString("name")+ " 举报投诉方式：" + workOrderType.getString("name"));

                            toBeContinuedInfoTv.setText("驳回原因：" + rejectReason + "\n\n" + "退回原因：" + backReason);

                            loadZP1();

                        } else {
                            pd.dismiss();
                            showToast(message.getString("message"), ToBeContinuedActivity.this);
                        }
                    }else{
                        showToast(response.getString("message"), ToBeContinuedActivity.this);
                    }
                } catch (JSONException e) {
                    pd.dismiss();
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(ToBeContinuedActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(ToBeContinuedActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(ToBeContinuedActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void loadZP1() {
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams params = new RequestParams();
        params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MIIS_Base_URL + "complainManageFlow!findMarketUsers.action\",\"parameter\":{},\"type\":\"WebExecutor\",\"method\":\"POST\"},\"app\":\"1003\"}");

        HttpClient.post(Constant.EXEC, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                pd.dismiss();

                try {
                    String messageSting = response.getString("message");

                    JSONObject message = new JSONObject(messageSting);

                    if (StringUtility.isSuccess(message)) {

                        JSONArray data = message.getJSONArray("data");

                        zpKey = new String[data.length()];
                        zpValue = new String[data.length()];

                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);
                            zpKey[i] = object.getString("id");
                            zpValue[i] = object.getString("name");
                        }

                    } else {
                        showToast(message.getString("message"), ToBeContinuedActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(ToBeContinuedActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(ToBeContinuedActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(ToBeContinuedActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void loadData2() {
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams params = new RequestParams();
        params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MIIS_Base_URL + "complainManageFlow!findBean.action?taskId=" + taskid + "\",\"type\":\"WebExecutor\",\"method\":\"GET\"},\"app\":\"1003\"}");

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

                            id = data.getString("id");

                            cigLicenseNo = data.getString("cigLicenseNo");
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

                            licenseTv.setText(cigLicenseNo);
                            shopnameTv.setText( tradename);
                            nameTv.setText(customerName);
                            phoneTv.setText(phoneNo);
                            timeTv.setText( workOrderDate);
                            contentTv.setText(workOrderContent);
                            workTypeTv.setText(parentWOType.getString("name") + " 举报投诉方式：" + workOrderType.getString("name"));
                            toBeContinuedInfoTv.setText("驳回原因：" + rejectReason + "\n\n" + "退回原因：" + backReason);

                            loadZP2();

                        } else {
                            pd.dismiss();
                            showToast(message.getString("message"), ToBeContinuedActivity.this);
                        }
                    }else {
                        showToast(response.getString("message"), ToBeContinuedActivity.this);
                    }
                } catch (JSONException e) {
                    pd.dismiss();
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(ToBeContinuedActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(ToBeContinuedActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(ToBeContinuedActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void loadZP2() {
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams params = new RequestParams();
        params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MIIS_Base_URL + "complainManageFlow!findMarketUsers.action\",\"type\":\"WebExecutor\",\"method\":\"GET\"},\"app\":\"1003\"}");

        HttpClient.post(Constant.EXEC, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                pd.dismiss();

                try {
                    String messageSting = response.getString("message");

                    JSONObject message = new JSONObject(messageSting);

                    if (StringUtility.isSuccess(message)) {

                        JSONArray data = message.getJSONArray("data");

                        zpKey = new String[data.length()];
                        zpValue = new String[data.length()];

                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);
                            zpKey[i] = object.getString("id");
                            zpValue[i] = object.getString("name");
                        }

                    } else {
                        showToast(message.getString("message"), ToBeContinuedActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(ToBeContinuedActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(ToBeContinuedActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(ToBeContinuedActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void searchCustomerInfo(final String liceNo, final String shopName) {
        String other = liceNo.length() > 0 ? "&_query.liceNo=" + liceNo : "";
        other = other + (shopName.length() > 0 ? "&_query.shopName=" + shopName : "");

        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams params = new RequestParams();
        params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MIIS_Base_URL + "customerInfo!searchAll.action?fprivilegeFlag=VIEW&start="+other+"\",\"type\":\"WebExecutor\",\"method\":\"GET\"},\"app\":\"1003\"}");

        HttpClient.post(Constant.EXEC , params, new JsonHttpResponseHandler() {

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

                            ArrayList<JSONObject> datas = new ArrayList<JSONObject>();

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject object = data.getJSONObject(i);
                                datas.add(object);
                            }

                            final ArrayList<JSONObject> customers = datas;

                            LayoutInflater inflater = LayoutInflater.from(ToBeContinuedActivity.this);
                            View layout = inflater.inflate(R.layout.customer_dialog, (ViewGroup) ToBeContinuedActivity.this.findViewById(R.id.custom_dialog));
                            ListView listView = (ListView) layout.findViewById(R.id.list);

                            CustomerDataAdapter customerDataAdapter = new CustomerDataAdapter(ToBeContinuedActivity.this, datas);
                            listView.setAdapter(customerDataAdapter);

                            final AlertDialog alert = new AlertDialog.Builder(ToBeContinuedActivity.this).setTitle("客户列表").setView(layout).setNegativeButton("关闭", null).show();

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    JSONObject customer = customers.get(position);

                                    try {
                                        cigLicenseNo = customer.getString("liceNo");
                                        tradename = customer.getString("shopName");

                                        licenseTv.setText(cigLicenseNo);
                                        shopnameTv.setText( tradename);

                                        alert.dismiss();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                        } else {
                            showToast(response.getString("message"), ToBeContinuedActivity.this);
                        }
                    } else {
                        showToast(response.getString("message"), ToBeContinuedActivity.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(ToBeContinuedActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(ToBeContinuedActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(ToBeContinuedActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    class CustomerDataAdapter extends SetAdapter {

        private ArrayList<JSONObject> datas;
        private LayoutInflater inflater;

        public CustomerDataAdapter(Context context, ArrayList<JSONObject> datas){
            super(context, datas);

            this.datas = datas;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            final ViewHolder holder;

            if (convertView == null){
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.customer_listitem, null);
                holder.textView1 = (TextView) convertView.findViewById(R.id.tv1);
                holder.textView2 = (TextView) convertView.findViewById(R.id.tv2);

                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            final JSONObject jsonObject = this.datas.get(position);

            try {
                holder.textView1.setText(jsonObject.getString("liceNo"));
                holder.textView2.setText(jsonObject.getString("shopName"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return convertView;
        }

        final class ViewHolder {
            TextView textView1;
            TextView textView2;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            String jsonString = getIntent().getStringExtra("data");
            JSONObject jsonObject = new JSONObject(jsonString);

            reportcomplaintype = jsonObject.getInt("reportcomplaintype");
            type = jsonObject.getInt("type");
            taskid = jsonObject.getString("taskid");


            setToolbarTitle(reportcomplaintype == 2 ? "处理举报" : "处理投诉");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_to_be_continued);
        showBack();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        assignViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (type == 2) {
            getMenuInflater().inflate(R.menu.wwc_menu, menu);
        } else {
            getMenuInflater().inflate(R.menu.inspection_menu, menu);
        }


        return super.onCreateOptionsMenu(menu);
    }

    private void doZp1(String id) {
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.sending);
        pd.show();

        RequestParams params = new RequestParams();
        params.add("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MIIS_Base_URL + "reportManageFlow!process.action?taskId=" + taskid + "&name=指派&marketUserId=" + id + "&bean.id=" + this.id + "\",\"type\":\"WebExecutor\",\"method\":\"GET\"},\"app\":\"1003\"}");

        HttpClient.post(Constant.EXEC, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                pd.dismiss();

                try {
                    String messageSting = response.getString("message");

                    JSONObject message = new JSONObject(messageSting);

                    if (StringUtility.isSuccess(message)) {
                        Constant.isRefreshTSJB = true;
                        finish();
                    } else {
                        showToast(message.getString("message"), ToBeContinuedActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(ToBeContinuedActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(ToBeContinuedActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(ToBeContinuedActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void doZp2(String id) {
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.sending);
        pd.show();

        RequestParams params = new RequestParams();
        params.add("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MIIS_Base_URL + "complainManageFlow!process.action?taskId=" + taskid + "&name=指派&marketUserId=" + id + "&bean.id=" + this.id + "\",\"type\":\"WebExecutor\",\"method\":\"GET\"},\"app\":\"1003\"}");

        HttpClient.post(Constant.EXEC, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                pd.dismiss();

                try {
                    String messageSting = response.getString("message");

                    JSONObject message = new JSONObject(messageSting);

                    if (StringUtility.isSuccess(message)) {
                        Constant.isRefreshTSJB = true;
                        finish();
                    } else {
                        showToast(message.getString("message"), ToBeContinuedActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(ToBeContinuedActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(ToBeContinuedActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(ToBeContinuedActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void doTh1() {
        final EditText editText = new EditText(this);
        new AlertDialog.Builder(this).setTitle("退回理由").setView(editText).setPositiveButton("提交",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!StringUtility.isEmpty(editText.getText().toString())) {
                            final KeerAlertDialog pd = showKeerAlertDialog(R.string.sending);
                            pd.show();

                            RequestParams params = new RequestParams();
                            params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MIIS_Base_URL + "reportManageFlow!process.action?taskId=" + taskid + "&name=退回&bean.backReason=" + editText.getText().toString() + "&bean.cigLicenseNo="+cigLicenseNo+"&bean.tradename="+tradename+"\",\"type\":\"WebExecutor\",\"method\":\"GET\"},\"app\":\"1003\"}");

                            HttpClient.post(Constant.EXEC, params, new JsonHttpResponseHandler() {

                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    super.onSuccess(statusCode, headers, response);
                                    pd.dismiss();

                                    try {
                                        String messageSting = response.getString("message");

                                        JSONObject message = new JSONObject(messageSting);

                                        if (StringUtility.isSuccess(message)) {
                                            Constant.isRefreshTSJB = true;
                                            finish();
                                        } else {
                                            showToast(message.getString("message"), ToBeContinuedActivity.this);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                    pd.dismiss();
                                    showNetworkError(ToBeContinuedActivity.this);
                                    super.onFailure(statusCode, headers, responseString, throwable);
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                    pd.dismiss();
                                    showNetworkError(ToBeContinuedActivity.this);
                                    super.onFailure(statusCode, headers, throwable, errorResponse);
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                                    pd.dismiss();
                                    showNetworkError(ToBeContinuedActivity.this);
                                    super.onFailure(statusCode, headers, throwable, errorResponse);
                                }
                            });
                        }
                    }
                }).setNegativeButton("取消", null).show();
    }

    private void doTh2() {
        final EditText editText = new EditText(this);
        new AlertDialog.Builder(this).setTitle("退回理由").setView(editText).setPositiveButton("提交",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!StringUtility.isEmpty(editText.getText().toString())) {
                            final KeerAlertDialog pd = showKeerAlertDialog(R.string.sending);
                            pd.show();

                            RequestParams params = new RequestParams();
                            params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MIIS_Base_URL + "complainManageFlow!process.action?taskId=" + taskid + "&name=退回&bean.backReason=" + editText.getText().toString() + "&bean.cigLicenseNo="+cigLicenseNo+"&bean.tradename="+tradename+"\",\"type\":\"WebExecutor\",\"method\":\"GET\"},\"app\":\"1003\"}");

                            HttpClient.post(Constant.EXEC, params, new JsonHttpResponseHandler() {

                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    super.onSuccess(statusCode, headers, response);
                                    pd.dismiss();

                                    try {
                                        String messageSting = response.getString("message");

                                        JSONObject message = new JSONObject(messageSting);

                                        if (StringUtility.isSuccess(message)) {
                                            Constant.isRefreshTSJB = true;
                                            finish();
                                        } else {
                                            showToast(message.getString("message"), ToBeContinuedActivity.this);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                    pd.dismiss();
                                    showNetworkError(ToBeContinuedActivity.this);
                                    super.onFailure(statusCode, headers, responseString, throwable);
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                    pd.dismiss();
                                    showNetworkError(ToBeContinuedActivity.this);
                                    super.onFailure(statusCode, headers, throwable, errorResponse);
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                                    pd.dismiss();
                                    showNetworkError(ToBeContinuedActivity.this);
                                    super.onFailure(statusCode, headers, throwable, errorResponse);
                                }
                            });
                        }
                    }
                }).setNegativeButton("取消", null).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (type == 2) {
            if (id == R.id.action_zp) {
                if (reportcomplaintype == 2 && type == 2) {
                    if (zpKey != null && zpValue != null) {
                        AlertDialog alertDialog = new AlertDialog.Builder(ToBeContinuedActivity.this).setTitle("请选择").setItems(zpValue, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String id = zpKey[which];

                                doZp1(id);
                            }
                        }).create();
                        alertDialog.show();
                    }
                } else if (reportcomplaintype == 3 && type == 2) {
                    if (zpKey != null && zpValue != null) {
                        AlertDialog alertDialog = new AlertDialog.Builder(ToBeContinuedActivity.this).setTitle("请选择").setItems(zpValue, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String id = zpKey[which];

                                doZp2(id);
                            }
                        }).create();
                        alertDialog.show();
                    }
                }

                return true;
            }

            if (id == R.id.action_th) {
                if (reportcomplaintype == 2 && type == 2) {
                    doTh1();
                } else if (reportcomplaintype == 3 && type == 2) {
                    doTh2();
                }
            }
        }

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
