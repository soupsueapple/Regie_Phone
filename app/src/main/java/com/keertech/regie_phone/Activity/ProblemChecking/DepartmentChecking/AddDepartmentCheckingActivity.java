package com.keertech.regie_phone.Activity.ProblemChecking.DepartmentChecking;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by soup on 2017/5/12.
 */

public class AddDepartmentCheckingActivity extends BaseActivity{

    private LinearLayout linearLayout9;
    private LinearLayout startDateLl;
    private TextView startDateTv;
    private LinearLayout endDateLl;
    private TextView endDateTv;
    private EditText checkingPeopleTv;
    private TextView selectCheckingPeopleTv;
    private EditText principalTv;
    private TextView nextTv;

    Calendar calendar = Calendar.getInstance();

    String[] users;
    String[] departments;
    String[] ids;

    StringBuffer checkPersonNames = new StringBuffer("");
    StringBuffer checkPersonIds = new StringBuffer("");

    HashSet<Integer> set = new HashSet<>();

    private void assignViews() {
        linearLayout9 = (LinearLayout) findViewById(R.id.linearLayout9);
        startDateLl = (LinearLayout) findViewById(R.id.start_date_ll);
        startDateTv = (TextView) findViewById(R.id.start_date_tv);
        endDateLl = (LinearLayout) findViewById(R.id.end_date_ll);
        endDateTv = (TextView) findViewById(R.id.end_date_tv);
        checkingPeopleTv = (EditText) findViewById(R.id.checking_people_tv);
        selectCheckingPeopleTv = (TextView) findViewById(R.id.select_checking_people_tv);
        principalTv = (EditText) findViewById(R.id.principal_tv);
        nextTv = (TextView) findViewById(R.id.next_tv);

        startDateLl.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);

                DatePickerDialog dp = new DatePickerDialog(AddDepartmentCheckingActivity.this,
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

                                startDateTv.setText(date);
                            }
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));

                dp.show();

            }
        });

        endDateLl.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);

                DatePickerDialog dp = new DatePickerDialog(AddDepartmentCheckingActivity.this,
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

                                if(startDateTv.getText().toString().length() > 0){


                                    final Date plancheckDate = DateTimeUtil.getFormatDate(date, DateTimeUtil.DATE_FORMAT);
                                    final Date currentDate = DateTimeUtil.getFormatDate(startDateTv.getText().toString(), DateTimeUtil.DATE_FORMAT);

                                    if(currentDate.getTime() <= plancheckDate.getTime()){
                                        endDateTv.setText(date);
                                    }else{
                                        showToast("结束日期不能小于开始日期", AddDepartmentCheckingActivity.this);
                                    }
                                }else{
                                    showToast("请填写开始日期", AddDepartmentCheckingActivity.this);
                                }


                            }
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));

                dp.show();

            }
        });

        nextTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);

                if(startDateTv.getText().toString().equals("请选择")){
                    showToast("请选择开始日期", AddDepartmentCheckingActivity.this);
                    return;
                }

                if(endDateTv.getText().toString().equals("请选择")){
                    showToast("请选择结束日期", AddDepartmentCheckingActivity.this);
                    return;
                }

                if(startDateTv.getText().toString().length() > 0 && ids.length > 0){

                    final Date plancheckDate = DateTimeUtil.getFormatDate(startDateTv.getText().toString(), DateTimeUtil.DATE_FORMAT);
                    final Date currentDate = new Date();

                    if(currentDate.getTime() < plancheckDate.getTime() || startDateTv.getText().toString().equals(DateTimeUtil.getCurrDateStr())){
                        saveHead();
                    }else{
                        showToast("选择日期已过去", AddDepartmentCheckingActivity.this);
                    }


                }

            }
        });

        selectCheckingPeopleTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);

                showMultiChoiceItemsDialog();
            }
        });

        searchBeans();
    }

    private void showMultiChoiceItemsDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle("选择参与人员").setMultiChoiceItems(users, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if(isChecked){
                    set.add(which);
                }else{
                    set.remove(which);
                }
            }
        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StringBuffer bufferId = new StringBuffer("");
                StringBuffer bufferName = new StringBuffer("");

                Iterator ite = set.iterator();
                while (ite.hasNext())
                {
                    Integer index = (Integer) ite.next();

                    if(bufferId.length() > 0) bufferId.append(","+ids[index.intValue()]); else bufferId.append(""+ids[index.intValue()]);
                    if(bufferName.length() > 0) bufferName.append(","+users[index.intValue()]); else bufferName.append(""+users[index.intValue()]);
                }

                checkPersonNames.append(bufferName.toString());
                checkPersonIds.append(bufferId.toString());

                checkingPeopleTv.setText(bufferName.toString());

                set.clear();
            }
        }).setNegativeButton("取消", null).create();
        alertDialog.show();
    }

    private void searchBeans(){
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams requestParams = new RequestParams();
        requestParams.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MWB_Base_URL + "postManage!searchBeans.action?privilegeFlag=VIEW\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

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

                            users = new String[data.length()];
                            departments = new String[data.length()];
                            ids = new String[data.length()];

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject object = data.getJSONObject(i);
                                JSONObject user = object.getJSONObject("user");
                                JSONObject department = object.getJSONObject("department");

                                users[i] = user.getString("name");
                                departments[i] = department.getString("name");
                                ids[i] = object.getString("id");
                            }

                        } else {
                            showToast(response.getString("message"), AddDepartmentCheckingActivity.this);
                        }
                    } else {
                        showToast(response.getString("message"), AddDepartmentCheckingActivity.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(AddDepartmentCheckingActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(AddDepartmentCheckingActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(AddDepartmentCheckingActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void saveHead(){
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        String date1 = startDateTv.getText().toString().equals("请选择")?"":startDateTv.getText().toString();
        String date2 = endDateTv.getText().toString().equals("请选择")?"":endDateTv.getText().toString();

        RequestParams requestParams = new RequestParams();
        requestParams.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MWB_Base_URL + "concentratePunish!m_saveHead.action?privilegeFlag=EDIT&bean.planCheckDate="+date1+"&bean.checkPersonNames="+checkPersonNames.toString()+"&bean.checkPersonIds="+checkPersonIds.toString()+"&bean.endCheckDate="+date2+"\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

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

                            String id = message.getString("id");

                            Intent intent = new Intent(AddDepartmentCheckingActivity.this, AddCustomerDepartmentCheckingActivity.class);
                            intent.putExtra("id", id);
                            AddDepartmentCheckingActivity.this.startActivity(intent);

                            Constant.isRefresJZZZSGrid = true;

                        } else {
                            showToast(response.getString("message"), AddDepartmentCheckingActivity.this);
                        }
                    } else {
                        showToast(response.getString("message"), AddDepartmentCheckingActivity.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(AddDepartmentCheckingActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(AddDepartmentCheckingActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(AddDepartmentCheckingActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(Constant.isRefresJZZZSFinish){
            Constant.isRefresJZZZSFinish = false;
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_department_checking);
        setToolbarTitle("新增");
        showBack();

        assignViews();
    }
}
