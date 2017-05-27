package com.keertech.regie_phone.Activity.CheckManage;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.keertech.regie_phone.BaseActivity;
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

import java.util.Calendar;
import java.util.Date;

import butterknife.ButterKnife;

import static com.keertech.regie_phone.R.id.del_tv;

/**
 * Created by soup on 2017/5/27.
 */

public class CreateCheckActivity extends BaseActivity{

    String id = "";

    String version = "";

    Calendar calendar = Calendar.getInstance();

    String[] leaveTypeValues = {"请假", "公务", "设备故障"};
    String[] leaveTypeKeys = {"1", "2", "3"};

    String type = "";

    private LinearLayout dateLl;
    private EditText dateEd;
    private LinearLayout typeLl;
    private TextView typeEd;
    private EditText noteEd;
    private TextView delTv;
    private TextView createTv;
    private TextView spTv;
    private TextView spbtgTv;

    private void assignViews(String title, int status) {
        dateLl = (LinearLayout) findViewById(R.id.date_ll);
        dateEd = (EditText) findViewById(R.id.date_ed);
        typeLl = (LinearLayout) findViewById(R.id.type_ll);
        typeEd = (TextView) findViewById(R.id.type_ed);
        noteEd = (EditText) findViewById(R.id.note_ed);
        delTv = (TextView) findViewById(del_tv);
        createTv = (TextView) findViewById(R.id.create_tv);
        spTv = (TextView) findViewById(R.id.sp_tv);
        spbtgTv = (TextView) findViewById(R.id.spbtg_tv);

        dateLl.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                DatePickerDialog dp = new DatePickerDialog(CreateCheckActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String month = "";

                                monthOfYear += 1;
                                if (monthOfYear < 10) month = "0" + monthOfYear;
                                else month = monthOfYear + "";

                                String day = "";

                                if (dayOfMonth < 10) day = "0" + dayOfMonth;
                                else day = dayOfMonth + "";

                                String date = year + "-" + month + "-" +day;

                                Date choose = DateTimeUtil.getFormatDate(date);

                                Date today = DateTimeUtil.getCurrDate();

                                if(choose.getTime() > today.getTime() || date.equals(DateTimeUtil.getCurrDateStr())){
                                    dateEd.setText(date);
                                }else{
                                    showToast("选择的时间不能小于今天", CreateCheckActivity.this);
                                }
                            }
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));

                dp.show();
            }
        });

        createTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                if(dateEd.getText().toString().length() == 0){
                    showToast("请选择时间", CreateCheckActivity.this);
                }else if(type.length() == 0){
                    showToast("请选择类型", CreateCheckActivity.this);
                }else if(noteEd.getText().length() == 0){
                    showToast("请填写备注", CreateCheckActivity.this);
                }else{
                    save();
                }
            }
        });

        typeLl.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                AlertDialog alertDialog = new AlertDialog.Builder(CreateCheckActivity.this).setTitle("选择类型").setItems(leaveTypeValues, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        typeEd.setText(leaveTypeValues[which]);
                        type = leaveTypeKeys[which];
                    }
                }).create();
                alertDialog.show();
            }
        });

        spTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                Date choose = DateTimeUtil.getFormatDate(dateEd.getText().toString());

                Date today = DateTimeUtil.getCurrDate();

                if(choose.getTime() > today.getTime() || dateEd.getText().toString().equals(DateTimeUtil.getCurrDateStr())){
                    approval(2);
                }else{
                    showToast("时间已过期", CreateCheckActivity.this);
                }
            }
        });

        spbtgTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                Date choose = DateTimeUtil.getFormatDate(dateEd.getText().toString());

                Date today = DateTimeUtil.getCurrDate();

                if(choose.getTime() > today.getTime() || dateEd.getText().toString().equals(DateTimeUtil.getCurrDateStr())){
                    approval(3);
                }else{
                    showToast("时间已过期", CreateCheckActivity.this);
                }
            }
        });

        delTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                remove();
            }
        });

        if(title.equals("详情")){
            createTv.setVisibility(View.GONE);
            delTv.setVisibility(View.GONE);
            spTv.setVisibility(View.GONE);
            spbtgTv.setVisibility(View.GONE);

            findBean();

        }else if(title.equals("创建")){
            createTv.setVisibility(View.VISIBLE);
            delTv.setVisibility(View.GONE);
            spTv.setVisibility(View.GONE);
            spbtgTv.setVisibility(View.GONE);
        }else if(title.equals("审批")){

            if(status != 2 && status != 3){
                createTv.setVisibility(View.GONE);
                delTv.setVisibility(View.GONE);
                spTv.setVisibility(View.VISIBLE);
                spbtgTv.setVisibility(View.VISIBLE);
            }else{
                createTv.setVisibility(View.GONE);
                delTv.setVisibility(View.VISIBLE);
                spTv.setVisibility(View.GONE);
                spbtgTv.setVisibility(View.GONE);
            }

            leaveBillFlowfindBean();
        }
    }

    private void save() {
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams requestParams = new RequestParams();
        requestParams.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MIIS_Base_URL + "leaveBill!save.action?privilegeFlag=ADD&bean.leaveDate="+dateEd.getText().toString()+"&bean.leaveType="+type+"&bean.remark="+noteEd.getText().toString()+"&bean.status=1\",\"type\":\"WebExecutor\"},\"app\":\"1003\"}");

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
                            Constant.isRefresCheck = true;
                            finish();
                        } else {
                            showToast(response.getString("message"), CreateCheckActivity.this);
                        }
                    } else {
                        showToast(response.getString("message"), CreateCheckActivity.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(CreateCheckActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(CreateCheckActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(CreateCheckActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void findBean() {
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams requestParams = new RequestParams();
        requestParams.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MIIS_Base_URL + "leaveBill!findBean.action?privilegeFlag=VIEW&bean.id="+id+"\",\"type\":\"WebExecutor\"},\"app\":\"1003\"}");

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
                            JSONObject object = message.getJSONObject("data");
                            JSONObject privilegeMap = object.getJSONObject("privilegeMap");

                            boolean DODELETE = privilegeMap.getBoolean("DODELETE");

                            version = object.getString("version");

                            if(DODELETE){
                                delTv.setVisibility(View.VISIBLE);
                            }else{
                                delTv.setVisibility(View.GONE);
                            }

                            int leaveType = object.getInt("leaveType");

                            String leaveTypeStr = "";

                            switch (leaveType){
                                case 1:
                                    leaveTypeStr = "事假";
                                    break;
                                case 2:
                                    leaveTypeStr = "公务";
                                    break;
                                case 3:
                                    leaveTypeStr = "设备故障";
                                    break;
                            }

                            dateEd.setText(object.getString("leaveDate").substring(0, 10));
                            typeEd.setText(leaveTypeStr);
                            noteEd.setText(object.getString("remark"));

                        } else {
                            showToast(response.getString("message"), CreateCheckActivity.this);
                        }
                    } else {
                        showToast(response.getString("message"), CreateCheckActivity.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(CreateCheckActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(CreateCheckActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(CreateCheckActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void leaveBillFlowfindBean() {
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams requestParams = new RequestParams();
        requestParams.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MIIS_Base_URL + "leaveBillFlow!findBean.action?privilegeFlag=VIEW&taskId="+id+"\",\"type\":\"WebExecutor\"},\"app\":\"1003\"}");

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
                            JSONObject object = message.getJSONObject("data");

                            int leaveType = object.getInt("leaveType");

                            String leaveTypeStr = "";

                            switch (leaveType){
                                case 1:
                                    leaveTypeStr = "请假";
                                    break;
                                case 2:
                                    leaveTypeStr = "公务";
                                    break;
                                case 3:
                                    leaveTypeStr = "设备故障";
                                    break;
                            }

                            dateEd.setText(object.getString("leaveDate").substring(0, 10));
                            typeEd.setText(leaveTypeStr);
                            noteEd.setText(object.getString("remark"));

                        } else {
                            showToast(response.getString("message"), CreateCheckActivity.this);
                        }
                    } else {
                        showToast(response.getString("message"), CreateCheckActivity.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(CreateCheckActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(CreateCheckActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(CreateCheckActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void approval(int status) {
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams requestParams = new RequestParams();
        requestParams.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MIIS_Base_URL + "leaveBillFlow!process.action?privilegeFlag=EDIT&taskId="+id+"&status="+status+"\",\"type\":\"WebExecutor\"},\"app\":\"1003\"}");

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
                            Constant.isRefresCheck = true;
                            finish();
                        } else {
                            showToast(response.getString("message"), CreateCheckActivity.this);
                        }
                    } else {
                        showToast(response.getString("message"), CreateCheckActivity.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(CreateCheckActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(CreateCheckActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(CreateCheckActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void remove() {
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams requestParams = new RequestParams();
        requestParams.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MIIS_Base_URL + "leaveBill!remove.action?privilegeFlag=DELETE&bean.id="+id+"&bean.version="+version+"\",\"type\":\"WebExecutor\"},\"app\":\"1003\"}");

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
                            Constant.isRefresCheck = true;
                            finish();
                        } else {
                            showToast(response.getString("message"), CreateCheckActivity.this);
                        }
                    } else {
                        showToast(response.getString("message"), CreateCheckActivity.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(CreateCheckActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(CreateCheckActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(CreateCheckActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String title = getIntent().getStringExtra("title");

        setContentView(R.layout.activity_create_check);
        setToolbarTitle(title);
        showBack();

        ButterKnife.bind(this);

        id = getIntent().getStringExtra("id");

        int status = getIntent().getIntExtra("status", 0);

        assignViews(title, status);
    }

}
