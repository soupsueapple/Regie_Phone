package com.keertech.regie_phone.Activity.CheckManage;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import java.util.Calendar;

import static com.keertech.regie_phone.R.id.recycler_view;
import static com.keertech.regie_phone.R.id.status_tv;

/**
 * Created by soup on 2017/5/27.
 */

public class CheckManageActivity extends BaseActivity{

    ArrayList<JSONObject> datas = new ArrayList<>();
    RecyclerAdapter recyclerAdapter = new RecyclerAdapter();

    boolean isReview = false;

    Calendar calendar = Calendar.getInstance();

    String type = "";

    String[] leaveTypeValues = {"请假", "公务", "设备故障"};
    String[] leaveTypeKeys = {"1", "2", "3"};

    private LinearLayout dateLl;
    private TextView dateTv;
    private EditText nameEt;
    private TextView typeTv;
    private TextView searchTv;
    private TextView dspTv;
    private TextView yspTv;
    private RecyclerView recyclerView;

    private void assignViews() {
        dateLl = (LinearLayout) findViewById(R.id.date_ll);
        dateTv = (TextView) findViewById(R.id.date_tv);
        nameEt = (EditText) findViewById(R.id.name_et);
        typeTv = (TextView) findViewById(R.id.type_tv);
        searchTv = (TextView) findViewById(R.id.search_tv);
        dspTv = (TextView) findViewById(R.id.dsp_tv);
        yspTv = (TextView) findViewById(R.id.ysp_tv);
        recyclerView = (RecyclerView) findViewById(recycler_view);

        dateLl.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                DatePickerDialog dp = new DatePickerDialog(CheckManageActivity.this,
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

                                dateTv.setText(date);
                            }
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));

                dp.show();
            }
        });

        typeTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                AlertDialog alertDialog = new AlertDialog.Builder(CheckManageActivity.this).setTitle("选择类型").setItems(leaveTypeValues, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        typeTv.setText(leaveTypeValues[which]);
                        type = leaveTypeKeys[which];
                    }
                }).create();
                alertDialog.show();
            }
        });

        searchTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                getList();
            }
        });

        dspTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                dspTv.setBackgroundColor(getResources().getColor(R.color.gray_black));
                dspTv.setTextColor(getResources().getColor(R.color.gray_white));

                yspTv.setBackgroundColor(getResources().getColor(R.color.gray_white));
                yspTv.setTextColor(getResources().getColor(R.color.gray_black));

                isReview = false;

                getList();
            }
        });

        yspTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                dspTv.setBackgroundColor(getResources().getColor(R.color.gray_white));
                dspTv.setTextColor(getResources().getColor(R.color.gray_black));

                yspTv.setBackgroundColor(getResources().getColor(R.color.gray_black));
                yspTv.setTextColor(getResources().getColor(R.color.gray_white));

                isReview = true;

                taskList();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(CheckManageActivity.this));
        recyclerView.setAdapter(recyclerAdapter);

        dspTv.setBackgroundColor(getResources().getColor(R.color.gray_black));
        dspTv.setTextColor(getResources().getColor(R.color.gray_white));

        getList();
    }

    private void getList(){
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams requestParams = new RequestParams();
        requestParams.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\""+ Constant.MIIS_Base_URL+"leaveBill!searchBeans.action?privilegeFlag=VIEW&_query.minTime="+dateTv.getText().toString()+"&_query.leaveType="+type+"\",\"type\":\"WebExecutor\"},\"app\":\"1003\"}");

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

                            JSONArray data = message.getJSONArray("data");

                            if (datas.size() > 0) datas.clear();

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject object = data.getJSONObject(i);

                                datas.add(object);
                            }

                            recyclerAdapter.notifyDataSetChanged();

                        } else {
                            showToast(response.getString("message"), CheckManageActivity.this);
                        }
                    } else {
                        showToast(response.getString("message"), CheckManageActivity.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(CheckManageActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(CheckManageActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(CheckManageActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void taskList(){
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams requestParams = new RequestParams();
        requestParams.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\""+ Constant.MIIS_Base_URL+"leaveBillFlow!taskList.action?privilegeFlag=VIEW&_query.minTime="+dateTv.getText().toString()+"&_query.leaveType="+type+"\",\"type\":\"WebExecutor\"},\"app\":\"1003\"}");

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

                            JSONArray data = message.getJSONArray("data");

                            if (datas.size() > 0) datas.clear();

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject object = data.getJSONObject(i);

                                datas.add(object);
                            }

                            recyclerAdapter.notifyDataSetChanged();

                        } else {
                            showToast(response.getString("message"), CheckManageActivity.this);
                        }
                    } else {
                        showToast(response.getString("message"), CheckManageActivity.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(CheckManageActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(CheckManageActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(CheckManageActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Constant.isRefresCheck){
            Constant.isRefresCheck = false;

            if(isReview)taskList(); else getList();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_manage);
        setToolbarTitle("考勤管理");
        showBack();

        assignViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.check_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_save){
            Intent intent = new Intent(this, CreateCheckActivity.class);
            intent.putExtra("title", "创建");
            intent.putExtra("id", "");
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>{

        @Override
        public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerHolder holder = new RecyclerHolder(LayoutInflater.from(CheckManageActivity.this).inflate(R.layout.activity_check_manage_recyclerview_item, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerHolder holder, int position) {
            final JSONObject object = datas.get(position);

            try {
                JSONObject leaveUser = object.getJSONObject("leaveUser");
                JSONObject department = leaveUser.getJSONObject("department");
                int leaveType = object.getInt("leaveType");

                String leaveTypeStr = "";
                String statusStr = "";

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

                if(StringUtility.notObjEmpty(object, "status")){
                    int status = object.getInt("status");
                    switch (status){
                        case 0:
                            statusStr = "待提交";
                            break;
                        case 1:
                            statusStr = "待审核";
                            break;
                        case 2:
                            statusStr = "审核通过";
                            break;
                        case 3:
                            statusStr = "审核不通过";
                            break;
                    }
                }

                holder.personTv.setText(leaveUser.getString("name"));
                holder.departmentTv.setText(department.getString("simplyName"));
                holder.typeTv.setText(leaveTypeStr);
                holder.dateTv.setText(object.getString("leaveDate").substring(0, 10));
                holder.statusTv.setText(statusStr);

                holder.rl.setOnClickListener(new ViewClickVibrate(){

                    @Override
                    public void onClick(View view) {
                        super.onClick(view);
                        try {
                            String id = "";
                            if(isReview) id = object.getString("taskid"); else  id = object.getString("id");
                            int status = object.getInt("status");
                            Intent intent = new Intent(CheckManageActivity.this, CreateCheckActivity.class);
                            if(isReview) intent.putExtra("title", "审批"); else intent.putExtra("title", "详情");
                            intent.putExtra("id", id);
                            intent.putExtra("status", status);
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        class RecyclerHolder extends RecyclerView.ViewHolder{

            private TextView personTv;
            private TextView departmentTv;
            private TextView typeTv;
            private TextView dateTv;
            private TextView statusTv;
            RelativeLayout rl;

            private void assignViews(View itemView) {
                personTv = (TextView) itemView.findViewById(R.id.person_tv);
                departmentTv = (TextView) itemView.findViewById(R.id.department_tv);
                typeTv = (TextView) itemView.findViewById(R.id.type_tv);
                dateTv = (TextView) itemView.findViewById(R.id.date_tv);
                statusTv = (TextView) itemView.findViewById(status_tv);
                rl = (RelativeLayout) itemView.findViewById(R.id.rl);
            }


            public RecyclerHolder(View itemView) {
                super(itemView);
                assignViews(itemView);
            }
        }
    }
}
