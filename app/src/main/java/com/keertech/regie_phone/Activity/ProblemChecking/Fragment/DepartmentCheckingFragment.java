package com.keertech.regie_phone.Activity.ProblemChecking.Fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.keertech.regie_phone.Activity.ProblemChecking.DepartmentChecking.AddDepartmentCheckingActivity;
import com.keertech.regie_phone.BaseFragment;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.keertech.regie_phone.R.id.recycler_view;

/**
 * Created by soup on 2017/5/11.
 */

public class DepartmentCheckingFragment extends BaseFragment{

    ArrayList<JSONObject> datas = new ArrayList<>();
    ArrayList<JSONObject> allDatas = new ArrayList<>();
    ArrayList<JSONObject> glszzDatas = new ArrayList<>();
    ArrayList<JSONObject> qjzzDatas = new ArrayList<>();

    RecyclerAdapter recyclerAdapter = new RecyclerAdapter();

    int postType = 0;

    Calendar calendar = Calendar.getInstance();

    private LinearLayout linearLayout9;
    private LinearLayout startDateLl;
    private TextView startDateTv;
    private LinearLayout endDateLl;
    private TextView endDateTv;
    private TextView searchTv;
    private LinearLayout linearLayout10;
    private RadioGroup rg;
    private RadioButton allRb;
    private RadioButton groupRb;
    private RadioButton departmentRb;
    private RecyclerView recyclerView;
    private FloatingActionButton addFb;
    private TextView tis_tv;

    private void assignViews(View convertView) {
        linearLayout9 = (LinearLayout) convertView.findViewById(R.id.linearLayout9);
        startDateLl = (LinearLayout) convertView.findViewById(R.id.start_date_ll);
        startDateTv = (TextView) convertView.findViewById(R.id.start_date_tv);
        endDateLl = (LinearLayout) convertView.findViewById(R.id.end_date_ll);
        endDateTv = (TextView) convertView.findViewById(R.id.end_date_tv);
        searchTv = (TextView) convertView.findViewById(R.id.search_tv);
        linearLayout10 = (LinearLayout) convertView.findViewById(R.id.linearLayout10);
        rg = (RadioGroup) convertView.findViewById(R.id.rg);
        allRb = (RadioButton) convertView.findViewById(R.id.all_rb);
        groupRb = (RadioButton) convertView.findViewById(R.id.group_rb);
        departmentRb = (RadioButton) convertView.findViewById(R.id.department_rb);
        recyclerView = (RecyclerView) convertView.findViewById(recycler_view);
        addFb = (FloatingActionButton) convertView.findViewById(R.id.add_fb);
        tis_tv = (TextView) convertView.findViewById(R.id.tis_tv);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerAdapter);

        addFb.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);

                Intent intent = new Intent(getActivity(), AddDepartmentCheckingActivity.class);
                startActivity(intent);
            }
        });

        startDateLl.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);

                DatePickerDialog dp = new DatePickerDialog(getActivity(),
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

                DatePickerDialog dp = new DatePickerDialog(getActivity(),
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

                                endDateTv.setText(date);
                            }
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));

                dp.show();
            }
        });

        searchTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                searchBeans();
            }
        });

        addFb.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
            }
        });

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.all_rb:
                        datas = allDatas;
                        recyclerAdapter.notifyDataSetChanged();
                        break;
                    case R.id.group_rb:
                        datas = glszzDatas;
                        recyclerAdapter.notifyDataSetChanged();
                        break;
                    case R.id.department_rb:
                        datas = qjzzDatas;
                        recyclerAdapter.notifyDataSetChanged();
                        break;
                }
            }
        });

        startDateTv.setText(DateTimeUtil.getCurrDateStr());
        endDateTv.setText(DateTimeUtil.getCurrDateStr());

        concentratePunish();
        searchBeans();
    }

    private void concentratePunish(){
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams requestParams = new RequestParams();
        requestParams.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\""+Constant.MWB_Base_URL+"concentratePunish!m_post.action\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

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

                            postType = message.getInt("postType");

                            if(postType == 1){
                                addFb.setVisibility(View.VISIBLE);
                                tis_tv.setVisibility(View.VISIBLE);

                            }else{
                                addFb.setVisibility(View.GONE);
                                tis_tv.setVisibility(View.GONE);
                            }

                        } else {
                            showToast(response.getString("message"), getActivity());
                        }
                    } else {
                        showToast(response.getString("message"), getActivity());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(getActivity());
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(getActivity());
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(getActivity());
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }


    private void searchBeans(){
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams requestParams = new RequestParams();
        requestParams.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\""+Constant.MWB_Base_URL+"concentratePunish!m_searchBeans.action?privilegeFlag=VIEW&_query.beginDate="+startDateTv.getText().toString()+"&_query.endDate="+endDateTv.getText().toString()+"\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

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

                            if(datas.size() > 0) datas.clear();
                            if(allDatas.size() > 0) allDatas.clear();
                            if(glszzDatas.size() > 0) glszzDatas.clear();
                            if(qjzzDatas.size() > 0) qjzzDatas.clear();

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject object = data.getJSONObject(i);
                                allDatas.add(object);

                                int type = object.getInt("type");

                                if(type == 1){
                                    glszzDatas.add(object);
                                }else if(type == 2){
                                    qjzzDatas.add(object);
                                }
                            }

                            datas = allDatas;

                            recyclerAdapter.notifyDataSetChanged();

                        } else {
                            showToast(response.getString("message"), getActivity());
                        }
                    } else {
                        showToast(response.getString("message"), getActivity());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(getActivity());
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(getActivity());
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(getActivity());
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(Constant.isRefresJZZZSGrid){
            searchBeans();
            Constant.isRefresJZZZSGrid = false;
        }

        if(Constant.isRefresJJFinish){
            Constant.isRefresJJFinish = false;
            searchBeans();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View convertView = inflater.inflate(R.layout.fragment_department_checking, null);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        assignViews(convertView);

        return convertView;
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>{

        @Override
        public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerHolder holder = new RecyclerHolder(LayoutInflater.from(DepartmentCheckingFragment.this.getActivity()).inflate(R.layout.fragment_department_checking_recyclerview_item, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerHolder holder, int position) {
            final JSONObject object = datas.get(position);

            try {
                final String plancheckdateString = object.getString("plancheckdate");

                holder.startDateTv.setText(plancheckdateString.substring(0, 10));

                if(StringUtility.notObjEmpty(object, "endcheckdate")){
                    final String endcheckdate = object.getString("endcheckdate");
                    holder.endDateTv.setText(" -- "+ endcheckdate.substring(0, 10));
                }

                int status = object.getInt("status");

                String cnum = object.getString("cnum");
                String ctotalnum = object.getString("ctotalnum");

                String nnum = object.getString("nnum");
                String ntotalnum = object.getString("ntotalnum");

                holder.customerNumberTv.setText(cnum + "/" + ctotalnum);
                holder.customerNumberTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

                holder.nothasLicenseTv.setText(nnum + "/" + ntotalnum);
                holder.nothasLicenseTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

                holder.departmentTv.setText(object.getString("deptname"));
                holder.principalTv.setText("负责人:"+object.getString("chargername"));
                holder.checkingPeopleTv.setText("检查人:"+object.getString("checkpersonnames"));

                final String id = object.getString("id");

                final int type = object.getInt("type");

                final int p = position;

                if(postType == 1){
                    holder.rl.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            final JSONObject object = datas.get(p);

                            try {
                                final String ID = object.getString("id");
                                final int status = object.getInt("status");

                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setMessage("是否删除本次集中整治");
                                builder.setTitle("提示");
                                builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(status == 1) delete(ID, object); else showToast("集中整治已完成无法删除!", getActivity());
                                    }
                                });
                                builder.setNegativeButton("取消", null);

                                builder.create().show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            return false;
                        }
                    });
                }else{
                    holder.rl.setOnLongClickListener(null);
                }

                if(status == 1){
                    holder.customerNumberTv.setTextColor(getResources().getColor(R.color.red));
                    holder.nothasLicenseTv.setTextColor(getResources().getColor(R.color.red));
                    holder.statusTv.setTextColor(getResources().getColor(R.color.red));

                    holder.statusTv.setText("进行中");

                    final Date plancheckDate = DateTimeUtil.getFormatDate(plancheckdateString, DateTimeUtil.TIME_FORMAT);
                    final Date currentDate = new Date();

                    holder.customerLl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(currentDate.getTime() >= plancheckDate.getTime()) {

//                                Intent intent = new Intent(getActivity(), CustomerActivity.class);
//                                intent.putExtra("object", object.toString());
//                                intent.putExtra("id", id);
//                                intent.putExtra("type", type);
//                                intent.putExtra("postType", postType);
//                                startActivity(intent);
                            }else{
                                showToast("检查时间还没有到", getActivity());
                            }
                        }
                    });

                    holder.nothasLicenseLl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(currentDate.getTime() >= plancheckDate.getTime()) {
//                                Intent intent = new Intent(getActivity(), NoLiceRegistryActivity.class);
//                                intent.putExtra("id", id);
//                                intent.putExtra("object", object.toString());
//                                intent.putExtra("type", type);
//                                intent.putExtra("postType", postType);
//                                startActivity(intent);

                            }else{
                                showToast("检查时间还没有到", getActivity());
                            }
                        }
                    });


                }else{
                    holder.customerNumberTv.setTextColor(getResources().getColor(R.color.gray_black));
                    holder.nothasLicenseTv.setTextColor(getResources().getColor(R.color.gray_black));
                    holder.statusTv.setTextColor(getResources().getColor(R.color.gray_black));

                    holder.statusTv.setText("已结束");

                    holder.customerLl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            Intent intent = new Intent(getActivity(), CustomerActivity.class);
//                            intent.putExtra("object", object.toString());
//                            intent.putExtra("id", id);
//                            intent.putExtra("type", type);
//                            intent.putExtra("postType", postType);
//                            intent.putExtra("ck", true);
//                            startActivity(intent);
                        }
                    });
                    holder.nothasLicenseLl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            Intent intent = new Intent(getActivity(), CustomerActivity.class);
//                            intent.putExtra("object", object.toString());
//                            intent.putExtra("id", id);
//                            intent.putExtra("type", type);
//                            intent.putExtra("postType", postType);
//                            intent.putExtra("ck", true);
//                            startActivity(intent);
                        }
                    });
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private void delete(String id, final JSONObject object){
            final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
            pd.show();

            RequestParams requestParams = new RequestParams();
            requestParams.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\""+ Constant.MWB_Base_URL+"concentratePunish!delete.action?bean.id="+id+"\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

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

                                datas.remove(object);

                                if(glszzDatas.size() > 0) glszzDatas.clear();
                                if(qjzzDatas.size() > 0) qjzzDatas.clear();

                                for (int i = 0; i < datas.size(); i++) {
                                    JSONObject object = datas.get(i);

                                    int type = object.getInt("type");

                                    if(type == 1){
                                        glszzDatas.add(object);
                                    }else if(type == 2){
                                        qjzzDatas.add(object);
                                    }
                                }

                                recyclerAdapter.notifyDataSetChanged();

                            } else {
                                showToast(response.getString("message"), getActivity());
                            }
                        } else {
                            showToast(response.getString("message"), getActivity());
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    pd.dismiss();
                    showNetworkError(getActivity());
                    super.onFailure(statusCode, headers, responseString, throwable);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    pd.dismiss();
                    showNetworkError(getActivity());
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    pd.dismiss();
                    showNetworkError(getActivity());
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        class RecyclerHolder extends RecyclerView.ViewHolder{

            private RelativeLayout rl;
            private TextView startDateTv;
            private TextView endDateTv;
            private TextView departmentTv;
            private TextView principalTv;
            private TextView checkingPeopleTv;
            private LinearLayout customerLl;
            private TextView customerNumberTv;
            private LinearLayout nothasLicenseLl;
            private TextView nothasLicenseTv;
            private TextView statusTv;

            private void assignViews(View itemView) {
                rl = (RelativeLayout) itemView.findViewById(R.id.rl);
                startDateTv = (TextView) itemView.findViewById(R.id.start_date_tv);
                endDateTv = (TextView) itemView.findViewById(R.id.end_date_tv);
                departmentTv = (TextView) itemView.findViewById(R.id.department_tv);
                principalTv = (TextView) itemView.findViewById(R.id.principal_tv);
                checkingPeopleTv = (TextView) itemView.findViewById(R.id.checking_people_tv);
                customerLl = (LinearLayout) itemView.findViewById(R.id.customer_ll);
                customerNumberTv = (TextView) itemView.findViewById(R.id.customer_number_tv);
                nothasLicenseLl = (LinearLayout) itemView.findViewById(R.id.nothas_license_ll);
                nothasLicenseTv = (TextView) itemView.findViewById(R.id.nothas_license_tv);
                statusTv = (TextView) itemView.findViewById(R.id.status_tv);
            }


            public RecyclerHolder(View itemView) {
                super(itemView);
                assignViews(itemView);
            }
        }
    }
}
