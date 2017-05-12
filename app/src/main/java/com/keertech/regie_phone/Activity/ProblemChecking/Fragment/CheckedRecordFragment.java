package com.keertech.regie_phone.Activity.ProblemChecking.Fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.keertech.regie_phone.Activity.ProblemChecking.CheckingActivity;
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

public class CheckedRecordFragment extends BaseFragment{

    private LinearLayout dateLl;
    private TextView dateTv;
    private TextView searchTv;
    private RecyclerView recyclerView;

    private ArrayList<JSONObject> datas = new ArrayList<>();

    RecyclerAdapter recyclerAdapter = new RecyclerAdapter();

    private String startDate = "";

    Calendar calendar = Calendar.getInstance();

    private void assignViews(View convertView) {
        dateLl = (LinearLayout) convertView.findViewById(R.id.date_ll);
        dateTv = (TextView) convertView.findViewById(R.id.date_tv);
        searchTv = (TextView) convertView.findViewById(R.id.search_tv);
        recyclerView = (RecyclerView) convertView.findViewById(recycler_view);

        searchTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);

                loadData();
            }
        });

        dateLl.setOnClickListener(new ViewClickVibrate(){

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

                                dateTv.setText(year + "" + month);
                                startDate = dateTv.getText().toString();


                            }
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));

                dp.show();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerAdapter);

        startDate = DateTimeUtil.getFormatDate(new Date(), DateTimeUtil.MONTHFORMAT);
        dateTv.setText(startDate);

        loadData();
    }

    private void loadData(){
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams requestParams = new RequestParams();
        requestParams.put("data","{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\""+ Constant.MWB_Base_URL+"marketCheck!recordList.action?privilegeFlag=VIEW&_query.yearMonth="+startDate+"\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

        HttpClient.post(Constant.EXEC, requestParams, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                pd.dismiss();

                try {
                    if (StringUtility.isSuccess(response)) {
                        String messageSting = response.getString("message");

                        JSONObject message = new JSONObject(messageSting);



                        if (StringUtility.isSuccess(message)) {
                            if(datas.size() > 0 )datas.clear();

                            JSONArray data = message.getJSONArray("data");

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject object = data.getJSONObject(i);

                                datas.add(object);
                            }

                            recyclerAdapter.notifyDataSetChanged();
                        }else{
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
        View convertView = inflater.inflate(R.layout.fragment_check_record, null);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        assignViews(convertView);

        return convertView;
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>{

        @Override
        public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerHolder holder = new RecyclerHolder(LayoutInflater.from(CheckedRecordFragment.this.getActivity()).inflate(R.layout.fragment_group_checking_recyclerview_item, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerHolder holder, int position) {

            final JSONObject object = datas.get(position);

            try {
                String liceNo = object.getString("liceNo");
                String shopName = object.getString("shopName");
                String chargerName = object.getString("chargerName");
                final int id = object.getInt("id");

                int dataSource = object.getInt("dataSource");
                int level = object.getInt("level");

                if(dataSource == 1){
                    holder.sourceTv.setText("行政服务");
                }else if(dataSource == 2){
                    holder.sourceTv.setText("举报投诉");
                }else if(dataSource == 3){
                    holder.sourceTv.setText("APCD预警");
                }

                String happenDate = object.getString("happenDate");

                if(level == 1){
                    holder.levelTv.setText("市管组" + " " + happenDate.substring(0, 10));
                }else if(level == 2){
                    holder.levelTv.setText("管理所"+ " " + happenDate.substring(0, 10));
                }else if(level == 3){
                    holder.levelTv.setText("区局"+ " " + happenDate.substring(0, 10));
                }


                holder.licenseTv.setText(liceNo);
                holder.shopnameTv.setText(shopName);
                holder.nameTv.setText(chargerName);

                holder.checkingTv.setText("查看");

                holder.checkingTv.setOnClickListener(new ViewClickVibrate(){

                    @Override
                    public void onClick(View view) {
                        super.onClick(view);

                        Intent intent = new Intent(getActivity(), CheckingActivity.class);
                        intent.putExtra("id", id);
                        intent.putExtra("status", 0);
                        intent.putExtra("postType", 0);
                        intent.putExtra("ck", true);
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

            private TextView shopnameTv;
            private TextView licenseTv;
            private TextView nameTv;
            private TextView sourceTv;
            private TextView levelTv;
            private TextView checkingTv;

            private void assignViews(View itemView) {
                shopnameTv = (TextView) itemView.findViewById(R.id.shopname_tv);
                licenseTv = (TextView) itemView.findViewById(R.id.license_tv);
                nameTv = (TextView) itemView.findViewById(R.id.name_tv);
                sourceTv = (TextView) itemView.findViewById(R.id.source_tv);
                levelTv = (TextView) itemView.findViewById(R.id.level_tv);
                checkingTv = (TextView) itemView.findViewById(R.id.checking_tv);
            }


            public RecyclerHolder(View itemView) {
                super(itemView);
                assignViews(itemView);
            }
        }
    }
}
