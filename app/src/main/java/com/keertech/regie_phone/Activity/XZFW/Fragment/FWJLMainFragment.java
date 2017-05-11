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

import com.keertech.regie_phone.Activity.XZFW.ServiceRecord.ServiceRecordInfoListActivity;
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

public class FWJLMainFragment extends BaseFragment{

    private LinearLayout monthLl;
    private TextView monthTv;
    private RecyclerView recyclerView;

    private ArrayList<JSONObject> datas = new ArrayList<>();

    RecyclerAdapter recyclerAdapter = new RecyclerAdapter();

    Calendar calendar = Calendar.getInstance();

    private void assignViews(View convertView) {
        monthLl = (LinearLayout) convertView.findViewById(R.id.month_ll);
        monthTv = (TextView) convertView.findViewById(R.id.month_tv);
        recyclerView = (RecyclerView) convertView.findViewById(recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerAdapter);

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

                                monthTv.setText(year + "" + month);

                                postProgress();
                            }
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));

                dp.show();

            }
        });

        monthTv.setText(DateTimeUtil.getFormatDate(new Date(), DateTimeUtil.MONTHFORMAT));

        postProgress();
    }

    private void postProgress(){
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams params = new RequestParams();
        params.put("data","{\"postHandler\":[],\"preHandler\":[],\"executor\": {\"url\":\""+ Constant.MWB_Base_URL+"marketInspect!postProgress.action?privilegeFlag=VIEW&_query.yearMonth="+monthTv.getText().toString()+"\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

        HttpClient.post(Constant.EXEC, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if(pd != null) pd.dismiss();

                try {

                    if(StringUtility.isSuccess(response)) {
                        String messageSting = response.getString("message");
                        System.out.println(messageSting);
                        JSONObject message = new JSONObject(messageSting);

                        if (StringUtility.isSuccess(message)) {

                            JSONArray data = message.getJSONArray("data");

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject object = data.getJSONObject(i);
                                datas.add(object);
                            }

                            recyclerAdapter.notifyDataSetChanged();

                        } else {
                            showToast(message.getString("message"), getActivity());
                        }
                    }else {
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
            RecyclerHolder holder = new RecyclerHolder(LayoutInflater.from(FWJLMainFragment.this.getActivity()).inflate(R.layout.fragment_service_record_recylerview_item, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerHolder holder, int position) {
            JSONObject object = datas.get(position);

            try {
                final String name = object.getString("name");
                final String deptName = object.getString("deptName");
                String num = object.getString("inspectNum");
                String totalNum = object.getString("totalNum");
                final String postId = object.getString("postId");

                final String number = num+"/"+totalNum;

                holder.nameTv.setText(name);
                holder.streetTv.setText(deptName);
                holder.serviceNumTv.setText(number);

                holder.serviceInfoTv.setOnClickListener(new ViewClickVibrate(){

                    @Override
                    public void onClick(View view) {
                        super.onClick(view);

                        Constant.isYc = false;

                        Intent intent = new Intent(getActivity(), ServiceRecordInfoListActivity.class);
                        intent.putExtra("name", name);
                        intent.putExtra("deptName", deptName);
                        intent.putExtra("number", number);
                        intent.putExtra("postId", postId);
                        intent.putExtra("date", monthTv.getText().toString());
                        startActivity(intent);

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

            private TextView nameTv;
            private TextView streetTv;
            private TextView serviceNumTv;
            private TextView serviceInfoTv;

            private void assignViews(View itemView) {
                nameTv = (TextView) itemView.findViewById(R.id.name_tv);
                streetTv = (TextView) itemView.findViewById(R.id.street_tv);
                serviceNumTv = (TextView) itemView.findViewById(R.id.service_num_tv);
                serviceInfoTv = (TextView) itemView.findViewById(R.id.service_info_tv);
            }


            public RecyclerHolder(View itemView) {
                super(itemView);

                assignViews(itemView);
            }
        }
    }
}
