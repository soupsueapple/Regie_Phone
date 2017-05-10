package com.keertech.regie_phone.Activity.XZFW.ServiceRecord;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import static com.keertech.regie_phone.R.id.recycler_view;

/**
 * Created by soup on 2017/5/10.
 */

public class ServiceRecordInfoListActivity extends BaseActivity{

    private TextView nameTv;
    private TextView streetTv;
    private TextView serviceNumTv;
    private RecyclerView recyclerView;

    private ArrayList<JSONObject> datas = new ArrayList<>();
    private RecyclerAdapter recyclerAdapter = new RecyclerAdapter();

    private void assignViews() {
        nameTv = (TextView) findViewById(R.id.name_tv);
        streetTv = (TextView) findViewById(R.id.street_tv);
        serviceNumTv = (TextView) findViewById(R.id.service_num_tv);
        recyclerView = (RecyclerView) findViewById(recycler_view);

        nameTv.setText(getIntent().getStringExtra("name"));
        streetTv.setText(getIntent().getStringExtra("deptName"));
        serviceNumTv.setText(getIntent().getStringExtra("number"));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);

        loadRightList(getIntent().getStringExtra("date"), getIntent().getStringExtra("postId"));
    }

    private void loadRightList(String startDate,String postId){
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams params = new RequestParams();
        params.put("data","{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\""+ Constant.MWB_Base_URL+"marketInspect!recordList.action?privilegeFlag=VIEW&_query.yearMonth="+startDate+"&_query.postId="+postId+"\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

        HttpClient.post(Constant.EXEC, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                pd.dismiss();

                try {

                    if(StringUtility.isSuccess(response)) {
                        String messageSting = response.getString("message");

                        JSONObject message = new JSONObject(messageSting);

                        if (StringUtility.isSuccess(message)) {

                            JSONArray data = message.getJSONArray("data");

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject object = data.getJSONObject(i);

                                if(StringUtility.isEmpty(object.getString("times"))) object.put("times", 0);

                                datas.add(object);
                            }

                            recyclerAdapter.notifyDataSetChanged();

                        } else {
                            showToast(message.getString("message"), ServiceRecordInfoListActivity.this);
                        }
                    }else{
                        showToast(response.getString("message"), ServiceRecordInfoListActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(ServiceRecordInfoListActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(ServiceRecordInfoListActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(ServiceRecordInfoListActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(Constant.isRefreshXCJ2){
            loadRightList(getIntent().getStringExtra("date"), getIntent().getStringExtra("postId"));
            Constant.isRefreshXCJ2 = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_record_info_list);
        setToolbarTitle("服务记录");
        showBack();

        assignViews();
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>{

        @Override
        public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerHolder holder = new RecyclerHolder(LayoutInflater.from(ServiceRecordInfoListActivity.this).inflate(R.layout.activity_service_record_info_listitem, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerHolder holder, int position) {

            JSONObject object = datas.get(position);

            try {
                String createdDate = object.getString("createdDate");
                String liceNo = object.getString("liceNo");
                String shopName = object.getString("shopName");
                String chargerName = object.getString("chargerName");
                final String times = object.getString("times");
                final String id = object.getString("id");

                holder.licenseTv.setText(liceNo);
                holder.shopnameTv.setText(shopName);
                holder.nameTv.setText(chargerName);
                holder.serviceTimeTv.setText(createdDate);
                holder.serviceDurationTimeTv.setText(times);

                holder.ll.setOnClickListener(new ViewClickVibrate(){

                    @Override
                    public void onClick(View view) {
                        super.onClick(view);

                        Intent intent = new Intent(ServiceRecordInfoListActivity.this, ServiceRecordInfoActivity.class);
                        intent.putExtra("id", id);
                        intent.putExtra("times", times);
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

            private RelativeLayout ll;
            private TextView nameTv;
            private TextView serviceTimeTv;
            private TextView licenseTv;
            private TextView serviceDurationTimeTv;
            private TextView shopnameTv;

            private void assignViews(View itemView) {
                ll = (RelativeLayout) itemView.findViewById(R.id.ll);
                nameTv = (TextView) itemView.findViewById(R.id.name_tv);
                serviceTimeTv = (TextView) itemView.findViewById(R.id.service_time_tv);
                licenseTv = (TextView) itemView.findViewById(R.id.license_tv);
                serviceDurationTimeTv = (TextView) itemView.findViewById(R.id.service_duration_time_tv);
                shopnameTv = (TextView) itemView.findViewById(R.id.shopname_tv);
            }


            public RecyclerHolder(View itemView) {
                super(itemView);

                assignViews(itemView);
            }
        }
    }

}
