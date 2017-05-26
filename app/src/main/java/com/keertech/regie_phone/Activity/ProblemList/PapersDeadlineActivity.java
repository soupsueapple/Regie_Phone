package com.keertech.regie_phone.Activity.ProblemList;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
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

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by soup on 2017/5/26.
 */

public class PapersDeadlineActivity extends BaseActivity{

    private ArrayList<JSONObject> datas = new ArrayList<>();

    Calendar calendar = Calendar.getInstance();

    private String startDate = "";
    private String endDate = "";

    RecyclerAdapter recyclerAdapter = new RecyclerAdapter();

    private LinearLayout startDateLl;
    private TextView startDateTv;
    private LinearLayout endDateLl;
    private TextView endDateTv;
    private TextView searchTv;
    private RecyclerView recyclerView;
    private EditText problem_no_et;

    private void assignViews() {
        startDateLl = (LinearLayout) findViewById(R.id.start_date_ll);
        startDateTv = (TextView) findViewById(R.id.start_date_tv);
        endDateLl = (LinearLayout) findViewById(R.id.end_date_ll);
        endDateTv = (TextView) findViewById(R.id.end_date_tv);
        searchTv = (TextView) findViewById(R.id.search_tv);
        problem_no_et = (EditText) findViewById(R.id.problem_no_et);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        startDateLl.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                DatePickerDialog dp =  new DatePickerDialog(PapersDeadlineActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String month = "";
                                String day = "";

                                monthOfYear+=1;
                                if(monthOfYear<10)month = "0"+monthOfYear;
                                else month = monthOfYear+"";

                                if(dayOfMonth<10)day = "0"+dayOfMonth;
                                else day = dayOfMonth+"";

                                startDateTv.setText(year+"-"+month+"-"+day);

                                startDate = year+"-"+month+"-"+day;
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
                DatePickerDialog dp = new DatePickerDialog(PapersDeadlineActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                String month = "";
                                String day = "";

                                monthOfYear += 1;
                                if (monthOfYear < 10) month = "0" + monthOfYear;
                                else month = monthOfYear + "";

                                if (dayOfMonth < 10) day = "0" + dayOfMonth;
                                else day = dayOfMonth + "";

                                endDateTv.setText(year + "-" + month + "-" + day);

                                endDate = year + "-" + month + "-" + day;
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
                searchData(problem_no_et.getText().toString(), startDate, endDate);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);

        searchData(problem_no_et.getText().toString(), startDate, endDate);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_supervise);
        setToolbarTitle("证件期限");
        showBack();

        assignViews();
    }

    private void searchData(String billcode, String startDate, String endDate){
        String other = billcode.length()>0?"%26_query.billcode="+billcode:"";
        other = other + (startDate.length()>0?"%26_query.startDate="+startDate:"");
        other = other + (endDate.length()>0?"%26_query.endDate="+endDate:"");

        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        HttpClient.get(Constant.EXEC + "?data=%7B%22postHandler%22:%5B%5D,%22preHandler%22:%5B%5D,%22executor%22:%7B%22url%22:%22" + Constant.MIIS_Base_URL +
                        "credentialsDispose!searchBeans.action%3fprivilegeFlag=VIEW" + other + "%26_query.statusFlag=true%22,%22type%22:%22WebExecutor%22%7D,%22app%22:%221003%22%7D",
                null, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        pd.dismiss();

                        if (datas.size() > 0) datas.clear();

                        try {
//                            response = new JSONObject(response.toString().replaceAll(" ",""));
                            if(StringUtility.isSuccess(response)) {
                                System.out.println("response: " + response.toString());
                                String messageSting = response.getString("message");

                                JSONObject message = new JSONObject(messageSting);

                                if (StringUtility.isSuccess(message)) {
                                    JSONArray data = message.getJSONArray("data");

                                    for (int i = 0; i < data.length(); i++) {
                                        JSONObject object = data.getJSONObject(i);

                                        datas.add(object);
                                    }

                                    recyclerAdapter.notifyDataSetChanged();
                                }
                            }else {
                                showToast(response.getString("message"), PapersDeadlineActivity.this);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        pd.dismiss();
                        showNetworkError(PapersDeadlineActivity.this);
                        super.onFailure(statusCode, headers, responseString, throwable);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        pd.dismiss();
                        showNetworkError(PapersDeadlineActivity.this);
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        pd.dismiss();
                        showNetworkError(PapersDeadlineActivity.this);
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }
                });
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>{

        @Override
        public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerHolder holder = new RecyclerHolder(LayoutInflater.from(PapersDeadlineActivity.this).inflate(R.layout.activity_case_supervise_recyclerview_item, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerHolder holder, int position) {
            JSONObject object = datas.get(position);

            try {

                String billcode = object.getString("billcode");
                String dname = object.getString("dname");
                String uname = object.getString("uname");
                String licenceno = object.getString("licenceno");
                String clientname = object.getString("clientname");
                int replystatus = object.getInt("replystatus");

                holder.problemNoTv.setText(billcode);
                holder.departmentTv.setText(dname);
                holder.typeTv.setText(licenceno);
                holder.principalTv.setText(uname);
                holder.contentTv.setText(clientname);
                holder.statusTv.setText(replystatus==0?"未回复":"已回复");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        class RecyclerHolder extends RecyclerView.ViewHolder{

            private TextView problemNoTv;
            private TextView typeTv;
            private TextView departmentTv;
            private TextView principalTv;
            private TextView statusTv;
            private TextView contentTv;

            private void assignViews(View itemView) {
                problemNoTv = (TextView) itemView.findViewById(R.id.problem_no_tv);
                typeTv = (TextView) itemView.findViewById(R.id.type_tv);
                departmentTv = (TextView) itemView.findViewById(R.id.department_tv);
                principalTv = (TextView) itemView.findViewById(R.id.principal_tv);
                statusTv = (TextView) itemView.findViewById(R.id.status_tv);
                contentTv = (TextView) itemView.findViewById(R.id.content_tv);
            }


            public RecyclerHolder(View itemView) {
                super(itemView);
                assignViews(itemView);
            }
        }
    }

}
