package com.keertech.regie_phone.Activity.CustomerInfo.CustomerInfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.keertech.regie_phone.BaseActivity;
import com.keertech.regie_phone.Constant.Constant;
import com.keertech.regie_phone.Listener.ViewClickVibrate;
import com.keertech.regie_phone.Models.Operator;
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

/**
 * Created by soup on 2017/5/22.
 */

public class PunishActivity extends BaseActivity{

    private LinearLayout dateLl;
    private TextView dateTv;
    private TextView searchTv;
    private RecyclerView recyclerView;

    RecyclerAdapter recyclerAdapter = new RecyclerAdapter();

    private ArrayList<JSONObject> datas = new ArrayList<>();

    private Operator operator;

    private String date = "";

    Calendar calendar = Calendar.getInstance();

    private void assignViews() {
        dateLl = (LinearLayout) findViewById(R.id.date_ll);
        dateTv = (TextView) findViewById(R.id.date_tv);
        searchTv = (TextView) findViewById(R.id.search_tv);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);

        ll.setVisibility(View.GONE);

        operator =(Operator) getIntent().getSerializableExtra("operator");

        searchDate();
    }

    private void searchDate(){
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams params = new RequestParams();
        params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\""+ Constant.MWB_Base_URL+"customerInfo!punish.action?privilegeFlag=VIEW&_query.liceNo="+operator.getLiceNo()+"\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

        HttpClient.post(Constant.EXEC, params, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        System.out.println("response: " + response.toString());
                        pd.dismiss();

                        if (datas.size() > 0) datas.clear();

                        try {
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

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        pd.dismiss();
                        showNetworkError(PunishActivity.this);
                        super.onFailure(statusCode, headers, responseString, throwable);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        pd.dismiss();
                        showNetworkError(PunishActivity.this);
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        pd.dismiss();
                        showNetworkError(PunishActivity.this);
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }
                });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_goods);
        setToolbarTitle("行政处罚");
        showBack();

        assignViews();
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>{

        @Override
        public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerHolder holder = new RecyclerHolder(LayoutInflater.from(PunishActivity.this).inflate(R.layout.activity_order_goods_recyclerview_item, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerHolder holder, int position) {
            JSONObject object = datas.get(position);

            try {
                String save_date = object.getString("saveDate");
                String case_type = object.getString("caseType");
                String case_reason = object.getString("caseReason");

                final String case_id = object.getString("caseId");

                holder.dateTv.setText(save_date.substring(0,10));
                holder.numberTv.setText(case_type);
                holder.moneyTv.setText(case_reason);

                holder.ll.setOnClickListener(new ViewClickVibrate(){

                    @Override
                    public void onClick(View view) {
                        super.onClick(view);
                        Intent intent = new Intent(PunishActivity.this, PunishInfoActivity.class);
                        intent.putExtra("case_id", case_id);
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

            private LinearLayout ll;
            private TextView dateTv;
            private TextView numberTv;
            private TextView moneyTv;

            private void assignViews(View itemView) {
                ll = (LinearLayout) itemView.findViewById(R.id.ll);
                dateTv = (TextView) itemView.findViewById(R.id.date_tv);
                numberTv = (TextView) itemView.findViewById(R.id.number_tv);
                moneyTv = (TextView) itemView.findViewById(R.id.money_tv);
            }


            public RecyclerHolder(View itemView) {
                super(itemView);
                assignViews(itemView);
            }
        }
    }

}
