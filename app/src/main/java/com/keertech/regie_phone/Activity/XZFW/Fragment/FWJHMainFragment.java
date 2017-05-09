package com.keertech.regie_phone.Activity.XZFW.Fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.keertech.regie_phone.Activity.XZFW.ServicePlanInfoActivity;
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
 * Created by soup on 2017/5/3.
 */

public class FWJHMainFragment extends BaseFragment{

    ArrayList<JSONObject> dateDatas = new ArrayList<>();

    private LinearLayout monthLl;
    private TextView month_tv;
    private RecyclerView recyclerView;

    Calendar calendar = Calendar.getInstance();

    RecyclerAdapter datesRecyclerAdapter = new RecyclerAdapter();

    boolean isThisWeek;
    boolean isPreviouslyWeek;

    private void assignViews(View convertView) {
        monthLl = (LinearLayout) convertView.findViewById(R.id.month_ll);
        month_tv = (TextView) convertView.findViewById(R.id.month_tv);
        recyclerView = (RecyclerView) convertView.findViewById(recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(datesRecyclerAdapter);

        month_tv.setText(DateTimeUtil.getFormatDate(new Date(), DateTimeUtil.MONTHFORMAT));

        monthLl.setOnClickListener(new ViewClickVibrate(){

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

                                String date = year + "" + month;

                                month_tv.setText(date);

                                weekList();
                            }
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));

                dp.show();
            }
        });

        weekList();
    }

    private void weekList(){
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams requestParams = new RequestParams();
        requestParams.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\""+ Constant.MWB_Base_URL+"inspectWeekPlan!weekList.action?yearMonth="+month_tv.getText().toString()+"\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

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

                            JSONArray data = message.optJSONArray("data");

                            if(dateDatas.size() > 0) dateDatas.clear();

                            for(int i=0;i<data.length();i++){
                                JSONObject object = data.optJSONObject(i);
                                dateDatas.add(object);
                            }

                            datesRecyclerAdapter.notifyDataSetChanged();

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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View convertView = inflater.inflate(R.layout.fragment_service_plan, null);

        assignViews(convertView);

        return convertView;
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>{

        @Override
        public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerHolder holder = new RecyclerHolder(LayoutInflater.from(FWJHMainFragment.this.getActivity()).inflate(R.layout.fragment_service_plan_recyclerview_item, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerHolder holder, int position) {

            final JSONObject object = dateDatas.get(position);

            try {
                final String id = object.getString("id");

                final String startDate =  object.getString("startDate").substring(0, 10);
                final String endDate =  object.getString("endDate").substring(0, 10);
                final String name = object.getString("name");

                final String currenWeek = object.getString("startDate").substring(0, 10) + " --- " + object.getString("endDate").substring(0, 10);

                holder.weekNameTv.setText(name);
                holder.weekScopeTv.setText(currenWeek);

                holder.weekInfoTv.setOnClickListener(new ViewClickVibrate() {
                    @Override
                    public void onClick(View v) {
                        super.onClick(v);
                        Date currentDate = DateTimeUtil.getFormatDate(DateTimeUtil.getCurrDateStr());

                        if(currentDate.getTime() > DateTimeUtil.getFormatDate(endDate).getTime()){
                            isPreviouslyWeek = true;
                        }else{
                            isPreviouslyWeek = false;
                        }

                        if(currentDate.getTime() >= DateTimeUtil.getFormatDate(startDate).getTime() && currentDate.getTime() <= DateTimeUtil.getFormatDate(endDate).getTime()){
                            isThisWeek = true;
                        }else{
                            isThisWeek = false;
                        }

                        boolean isAddPlan;

                        if(isThisWeek || isPreviouslyWeek){
                            isAddPlan = false;
                        }else{
                            isAddPlan = true;
                        }

                        Intent intent = new Intent(getActivity(), ServicePlanInfoActivity.class);
                        intent.putExtra("yearMonth", month_tv.getText().toString());
                        intent.putExtra("id", id);
                        intent.putExtra("isAddPlan", isAddPlan);
                        intent.putExtra("weekname", name + " " + currenWeek);
                        getActivity().startActivity(intent);

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return dateDatas.size();
        }

        class RecyclerHolder extends RecyclerView.ViewHolder{

            private TextView weekNameTv;
            private TextView weekScopeTv;
            private TextView weekInfoTv;

            private void assignViews(View itemView) {
                weekNameTv = (TextView) itemView.findViewById(R.id.week_name_tv);
                weekScopeTv = (TextView) itemView.findViewById(R.id.week_scope_tv);
                weekInfoTv = (TextView) itemView.findViewById(R.id.week_info_tv);
            }

            public RecyclerHolder(View itemView) {
                super(itemView);
                assignViews(itemView);
            }
        }
    }
}
