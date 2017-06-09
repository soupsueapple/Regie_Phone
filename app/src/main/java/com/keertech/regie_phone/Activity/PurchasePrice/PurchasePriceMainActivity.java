package com.keertech.regie_phone.Activity.PurchasePrice;

import android.app.DatePickerDialog;
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
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import static com.keertech.regie_phone.R.id.recycler_view;

/**
 * Created by soup on 2017/5/27.
 */

public class PurchasePriceMainActivity extends BaseActivity{

    Calendar calendar = Calendar.getInstance();

    ArrayList<JSONObject> datas = new ArrayList<>();
    ArrayList<JSONObject> smokes = new ArrayList<>();

    String currentCigCode = "";
    String currentCigName = "";

    RecyclerAdapter recyclerAdapter = new RecyclerAdapter();

    private LinearLayout startDateLl;
    private TextView startDateTv;
    private LinearLayout endDateLl;
    private TextView endDateTv;
    private RecyclerView recyclerView;

    private void assignViews() {
        startDateLl = (LinearLayout) findViewById(R.id.start_date_ll);
        startDateTv = (TextView) findViewById(R.id.start_date_tv);
        endDateLl = (LinearLayout) findViewById(R.id.end_date_ll);
        endDateTv = (TextView) findViewById(R.id.end_date_tv);
        recyclerView = (RecyclerView) findViewById(recycler_view);

        startDateLl.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                DatePickerDialog dp = new DatePickerDialog(PurchasePriceMainActivity.this,
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
                DatePickerDialog dp = new DatePickerDialog(PurchasePriceMainActivity.this,
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


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);

        getList();
    }

    private void getList(){
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams requestParams = new RequestParams();
        requestParams.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\""+ Constant.MWB_Base_URL+"cigPriceCollection!getList.action?privilegeFlag=VIEW&_query.minDate="+startDateTv.getText().toString()+"&_query.maxDate="+endDateTv.getText().toString()+"\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

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
                            showToast(response.getString("message"), PurchasePriceMainActivity.this);
                        }
                    } else {
                        showToast(response.getString("message"), PurchasePriceMainActivity.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(PurchasePriceMainActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(PurchasePriceMainActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(PurchasePriceMainActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_price);
        setToolbarTitle("卷烟购进价");
        showBack();

        assignViews();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(Constant.isRefresPurchasePirce){
            Constant.isRefresPurchasePirce = false;
            getList();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.purchase_price_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_search){
            getList();
            return true;
        }

        if(id == R.id.action_add){
            Intent intent = new Intent(PurchasePriceMainActivity.this, AddPurchasePirceActivity.class);
            intent.putExtra("isAdd", true);
            intent.putExtra("postId", "");
            intent.putExtra("collectionDate", "");
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>{

        @Override
        public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerHolder holder = new RecyclerHolder(LayoutInflater.from(PurchasePriceMainActivity.this).inflate(R.layout.activity_purchase_price_recyclerview_item, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerHolder holder, int position) {
            JSONObject object = datas.get(position);

            try {
                String cigName = object.getString("postId");
                String purchasePrice = object.getString("name");
                final String collectionDate = object.getString("collectionDate");

                final String id = object.getString("postId");

                holder.idTv.setText(cigName);
                holder.nameTv.setText(purchasePrice);
                holder.dateTv.setText(collectionDate);

                holder.ll.setOnClickListener(new ViewClickVibrate(){

                    @Override
                    public void onClick(View view) {
                        super.onClick(view);
                        Intent intent = new Intent(PurchasePriceMainActivity.this, AddPurchasePirceActivity.class);
                        intent.putExtra("isAdd", false);
                        intent.putExtra("collectionDate", collectionDate);
                        intent.putExtra("postId", id);
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
            private TextView idTv;
            private TextView nameTv;
            private TextView dateTv;

            private void assignViews(View itemView) {
                ll = (LinearLayout) itemView.findViewById(R.id.ll);
                idTv = (TextView) itemView.findViewById(R.id.id_tv);
                nameTv = (TextView) itemView.findViewById(R.id.name_tv);
                dateTv = (TextView) itemView.findViewById(R.id.date_tv);
            }


            public RecyclerHolder(View itemView) {
                super(itemView);
                assignViews(itemView);
            }
        }
    }
}
