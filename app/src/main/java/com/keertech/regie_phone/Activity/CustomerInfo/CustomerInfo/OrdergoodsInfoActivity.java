package com.keertech.regie_phone.Activity.CustomerInfo.CustomerInfo;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.keertech.regie_phone.BaseActivity;
import com.keertech.regie_phone.Constant.Constant;
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

/**
 * Created by soup on 2017/5/22.
 */

public class OrdergoodsInfoActivity extends BaseActivity{

    private RecyclerView recyclerView;

    ArrayList<JSONObject> datas = new ArrayList<>();

    RecyclerAdapter recyclerAdapter = new RecyclerAdapter();

    private void assignViews() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);

        String liceNo = getIntent().getStringExtra("liceNo");
        String sellD = getIntent().getStringExtra("sellD");

        chickData(liceNo, sellD);
    }

    private void chickData(String liceNo,String sellD){
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams params = new RequestParams();
        params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\""+ Constant.MWB_Base_URL+"customerInfo!orderDetail.action?privilegeFlag=VIEW&_query.liceNo="+liceNo+"&_query.sellD="+sellD+"\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

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
                        showNetworkError(OrdergoodsInfoActivity.this);
                        super.onFailure(statusCode, headers, responseString, throwable);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        pd.dismiss();
                        showNetworkError(OrdergoodsInfoActivity.this);
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        pd.dismiss();
                        showNetworkError(OrdergoodsInfoActivity.this);
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_goods_info);
        setToolbarTitle("订货信息");
        showBack();

        assignViews();
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>{

        @Override
        public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerHolder holder = new RecyclerHolder(LayoutInflater.from(OrdergoodsInfoActivity.this).inflate(R.layout.activity_order_goods_info_recyclerview_item, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerHolder holder, int position) {
            JSONObject object = datas.get(position);

            try {
                String cigName = object.getString("cigName");
                String sellAmount = object.getString("sellAmount");
                String sellTotalMoney = object.getString("sellTotalMoney");

                holder.brandTv.setText(StringUtility.isEmpty(cigName)?"":cigName);
                holder.numberTv.setText(StringUtility.isEmpty(sellAmount)?"":sellAmount);
                holder.moneyTv.setText(StringUtility.isEmpty(sellTotalMoney)?"":sellTotalMoney );

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
            private TextView brandTv;
            private TextView numberTv;
            private TextView moneyTv;

            private void assignViews(View itemView) {
                ll = (LinearLayout) itemView.findViewById(R.id.ll);
                brandTv = (TextView) itemView.findViewById(R.id.brand_tv);
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
