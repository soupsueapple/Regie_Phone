package com.keertech.regie_phone.Activity.Prospecting;

import android.content.Intent;
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
 * Created by soup on 2017/5/26.
 */

public class ProspectingListActivity extends BaseActivity{

    private ArrayList<JSONObject> datas = new ArrayList<>();

    RecyclerAdapter recyclerAdapter = new RecyclerAdapter();

    private RecyclerView recyclerView;

    private void assignViews() {
        recyclerView = (RecyclerView) findViewById(recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);

        getParameter();
    }

    private void getParameter(){
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams params = new RequestParams();
        params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\""+Constant.MWB_Base_URL+"customerInfo!queryProspecting.action\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

        HttpClient.post(Constant.EXEC, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
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
                            showToast(response.getString("message"), ProspectingListActivity.this);
                        }
                    } else {
                        showToast(response.getString("message"), ProspectingListActivity.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(ProspectingListActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(ProspectingListActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(ProspectingListActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prospecting_list);
        setToolbarTitle("零售户勘查");
        assignViews();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(Constant.isRefresRe){
            getParameter();
            Constant.isRefresRe = false;
        }
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>{

        @Override
        public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerHolder holder = new RecyclerHolder(LayoutInflater.from(ProspectingListActivity.this).inflate(R.layout.activity_prospecting_list_recyclerview_item, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerHolder holder, int position) {
            final JSONObject object = datas.get(position);

            try {
                holder.shopnameTv.setText(object.getString("storename"));
                holder.nameTv.setText(object.getString("custname"));
                holder.addressTv.setText(object.getString("custaddress"));
                holder.applyDateTv.setText(object.getString("applyd").substring(0,10));
                holder.phoneTv.setText(object.getString("custtelphone"));
                holder.typeTv.setText(object.getString("transmodename"));
                holder.idTv.setText(object.getString("idcardno"));
                if(StringUtility.notObjEmpty(object, "acceptd")) holder.acceptDateTv.setText(object.getString("acceptd").substring(0, 10));

                Double longitude = null;
                Double latitude = null;

                if(StringUtility.notObjEmpty(object, "longitude"))longitude = object.getDouble("longitude");
                if(StringUtility.notObjEmpty(object, "latitude"))latitude = object.getDouble("latitude");

                if(longitude == null || latitude == null){
                    holder.statusTv.setText("未办理");
                }else{
                    holder.statusTv.setText("已办理");
                }

                final String id = object.getString("rowid");

                holder.searchTv.setOnClickListener(new ViewClickVibrate(){

                    @Override
                    public void onClick(View view) {
                        super.onClick(view);


                        Intent intent = new Intent(ProspectingListActivity.this, ProspectingInfoActivity.class);
                        intent.putExtra("id", id);
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
            private TextView nameTv;
            private TextView applyDateTv;
            private TextView acceptDateTv;
            private TextView addressTv;
            private TextView phoneTv;
            private TextView typeTv;
            private TextView idTv;
            private TextView statusTv;
            private TextView searchTv;
            private LinearLayout linearLayout22;

            private void assignViews(View itemView) {
                shopnameTv = (TextView) itemView.findViewById(R.id.shopname_tv);
                nameTv = (TextView) itemView.findViewById(R.id.name_tv);
                applyDateTv = (TextView) itemView.findViewById(R.id.apply_date_tv);
                acceptDateTv = (TextView) itemView.findViewById(R.id.accept_date_tv);
                addressTv = (TextView) itemView.findViewById(R.id.address_tv);
                phoneTv = (TextView) itemView.findViewById(R.id.phone_tv);
                typeTv = (TextView) itemView.findViewById(R.id.type_tv);
                idTv = (TextView) itemView.findViewById(R.id.id_tv);
                statusTv = (TextView) itemView.findViewById(R.id.status_tv);
                searchTv = (TextView) itemView.findViewById(R.id.search_tv);
                linearLayout22 = (LinearLayout) itemView.findViewById(R.id.linearLayout22);
            }


            public RecyclerHolder(View itemView) {
                super(itemView);
                assignViews(itemView);
            }
        }
    }
}
