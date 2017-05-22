package com.keertech.regie_phone.Activity.CustomerInfo.CustomerInfo;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.keertech.regie_phone.BaseActivity;
import com.keertech.regie_phone.Constant.Constant;
import com.keertech.regie_phone.Listener.ViewClickVibrate;
import com.keertech.regie_phone.Models.Operator;
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
 * Created by soup on 2017/5/22.
 */

public class OrdergoodsActivity extends BaseActivity{

    private LinearLayout dateLl;
    private TextView dateTv;
    private TextView searchTv;
    private RecyclerView recyclerView;

    private ArrayList<JSONObject> datas = new ArrayList<>();

    private Operator operator;

    private String date = "";

    Calendar calendar = Calendar.getInstance();

    RecyclerAdapter recyclerAdapter = new RecyclerAdapter();

    private void assignViews() {
        dateLl = (LinearLayout) findViewById(R.id.date_ll);
        dateTv = (TextView) findViewById(R.id.date_tv);
        searchTv = (TextView) findViewById(R.id.search_tv);
        recyclerView = (RecyclerView) findViewById(recycler_view);

        dateLl.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                DatePickerDialog dp =  new DatePickerDialog(OrdergoodsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String month = "";

                                monthOfYear+=1;
                                if(monthOfYear<10)month = "0"+monthOfYear;
                                else month = monthOfYear+"";

                                dateTv.setText(year+"-"+month);

                                date = year+month;
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
                if(!StringUtility.isEmpty(date)){
                    searchDate();
                }else{
                    showToast("请选择日期",OrdergoodsActivity.this);
                }
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);

        operator =(Operator) getIntent().getSerializableExtra("operator");

        date = DateTimeUtil.getFormatDate(new Date(), DateTimeUtil.MONTHFORMAT);

        String date_ = DateTimeUtil.getFormatDate(new Date(), DateTimeUtil.MONTH_FORMAT);
        dateTv.setText(date_);
        searchDate();
    }

    private void searchDate(){
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams params = new RequestParams();
        params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\""+ Constant.MWB_Base_URL+"customerInfo!monthOrder.action?privilegeFlag=VIEW&_query.liceNo="+operator.getLiceNo()+"&_query.yearMonth="+date+"\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

        HttpClient.post(Constant.EXEC, params, new JsonHttpResponseHandler(){

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        System.out.println("response: " + response.toString());
                        pd.dismiss();

                        if(datas.size()>0)datas.clear();

                        try {
                            String messageSting = response.getString("message");

                            JSONObject message = new JSONObject(messageSting);

                            if (StringUtility.isSuccess(message)){
                                JSONArray data = message.getJSONArray("data");

                                for(int i =0;i<data.length();i++){
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
                        showNetworkError(OrdergoodsActivity.this);
                        super.onFailure(statusCode, headers, responseString, throwable);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        pd.dismiss();
                        showNetworkError(OrdergoodsActivity.this);
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        pd.dismiss();
                        showNetworkError(OrdergoodsActivity.this);
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_goods);
        setToolbarTitle("订货信息");
        showBack();

        assignViews();
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>{

        @Override
        public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerHolder holder = new RecyclerHolder(LayoutInflater.from(OrdergoodsActivity.this).inflate(R.layout.activity_order_goods_recyclerview_item, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerHolder holder, int position) {

            JSONObject object = datas.get(position);

            try {
                final String sellD = object.getString("sellD");
                final String liceNo = object.getString("liceNo");
                String sellTotalMoney = object.getString("sellTotalMoney");
                String sellAmount = object.getString("sellAmount");

                holder.dateTv.setText(StringUtility.isEmpty(sellD)?"":sellD );
                holder.numberTv.setText(StringUtility.isEmpty(sellAmount)?"":sellAmount );
                holder.moneyTv.setText(StringUtility.isEmpty(sellTotalMoney)?"":sellTotalMoney );

                holder.ll.setOnClickListener(new ViewClickVibrate(){

                    @Override
                    public void onClick(View view) {
                        super.onClick(view);

                        Intent intent = new Intent(OrdergoodsActivity.this, OrdergoodsInfoActivity.class);
                        intent.putExtra("liceNo", liceNo);
                        intent.putExtra("sellD", sellD);
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
