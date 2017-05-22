package com.keertech.regie_phone.Activity.NothaveLicenseCustomer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.keertech.regie_phone.R.id.recycler_view;

/**
 * Created by soup on 2017/5/22.
 */

public class NothaveLicenseCustomerMainActivity extends BaseActivity{

    private LinearLayout linearLayout17;
    private EditText nameTv;
    private EditText shopnameTv;
    private LinearLayout linearLayout18;
    private EditText addressTv;
    private TextView searchTv;
    private RecyclerView recyclerView;
    private FloatingActionButton addFb;

    private ArrayList<JSONObject> datas = new ArrayList<>();

    RecyclerAdapter recyclerAdapter = new RecyclerAdapter();

    private void assignViews() {
        linearLayout17 = (LinearLayout) findViewById(R.id.linearLayout17);
        nameTv = (EditText) findViewById(R.id.name_tv);
        shopnameTv = (EditText) findViewById(R.id.shopname_tv);
        linearLayout18 = (LinearLayout) findViewById(R.id.linearLayout18);
        addressTv = (EditText) findViewById(R.id.address_tv);
        searchTv = (TextView) findViewById(R.id.search_tv);
        recyclerView = (RecyclerView) findViewById(recycler_view);
        addFb = (FloatingActionButton) findViewById(R.id.add_fb);

        searchTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                loadData();
            }
        });

        addFb.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                Intent intent = new Intent(NothaveLicenseCustomerMainActivity.this, NothaveLicenseCustomerInfoActivity.class);
                intent.putExtra("id","");
                intent.putExtra("relationId","");
                intent.putExtra("sourceType","00");
                intent.putExtra("name",nameTv.getText().toString());
                intent.putExtra("shopname",shopnameTv.getText().toString());
                intent.putExtra("address",addressTv.getText().toString());
                intent.putExtra("title", "创建");
                startActivity(intent);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);

        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Constant.isRefreshUNID){
            Constant.isRefreshUNID = false;
            loadData();
        }
    }

    private void loadData(){
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        StringBuffer buffer = new StringBuffer("");

        if(!StringUtility.isEmpty(nameTv.getText().toString())) buffer.append("&_query.name="+nameTv.getText().toString());
        if(!StringUtility.isEmpty(shopnameTv.getText().toString())) buffer.append("&_query.shopName="+shopnameTv.getText().toString());
        if(!StringUtility.isEmpty(addressTv.getText().toString())) buffer.append("&_query.address="+addressTv.getText().toString());

        RequestParams params = new RequestParams();
        params.put("data","{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\""+ Constant.MWB_Base_URL+"noLiceRegistry!searchBeans.action?privilegeFlag=VIEW"+buffer+"\",\"type\":\"WebExecutor\",\"method\":\"GET\"},\"app\":\"1001\"}");

        HttpClient.post(Constant.EXEC, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                pd.dismiss();

                try {
                    if(StringUtility.isSuccess(response)){
                        String messageSting = response.getString("message");

                        JSONObject message = new JSONObject(messageSting);

                        if (StringUtility.isSuccess(message)) {

                            if (datas.size() > 0) datas.clear();

                            JSONArray jsonArray = message.getJSONArray("data");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                datas.add(object);
                            }

                            recyclerAdapter.notifyDataSetChanged();
                        } else {
                            showToast(message.getString("message"), NothaveLicenseCustomerMainActivity.this);
                        }
                    }else{
                        showToast(response.getString("message"), NothaveLicenseCustomerMainActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(NothaveLicenseCustomerMainActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(NothaveLicenseCustomerMainActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(NothaveLicenseCustomerMainActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nohave_license_customer_main);
        setToolbarTitle("无证户信息");
        showBack();

        assignViews();
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>{

        @Override
        public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerHolder holder = new RecyclerHolder(LayoutInflater.from(NothaveLicenseCustomerMainActivity.this).inflate(R.layout.activity_nohave_license_customer_main_recyclerview_item, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerHolder holder, int position) {
            JSONObject object = datas.get(position);

            String idStr = "";
            try {
                idStr = object.getString("id");

                String name = object.getString("name");
                String shopName = object.getString("shopName");
                String address = object.getString("address");

                JSONObject post = object.getJSONObject("post");
                JSONObject user = post.getJSONObject("user");

                int managerstateId = object.getInt("managerstateId");

                String managerstate = "";

                switch (managerstateId){
                    case 2:
                        managerstate = "转向经营";
                        break;
                    case 3:
                        managerstate = "没有经营";
                        break;
                    case 4:
                        managerstate = "继续经营";
                        break;
                    case 5:
                        managerstate = "办证经营";
                        break;
                }

                int measuresId = object.getInt("measuresId");

                String measures = "";

                switch (measuresId){
                    case 1:
                        measures = "取缔";
                        break;
                    case 2:
                        measures = "查处";
                        break;
                    case 3:
                        measures = "办证";
                        break;
                    case 4:
                        measures = "宣传教育";
                        break;
                }

                String createdDate = object.getString("createdDate");

                holder.nameTv.setText(name);
                holder.shopnameTv.setText(shopName);
                holder.addressTv.setText(address);
                holder.commonTv.setText("市管员: "+user.getString("name"));
                holder.statusTv.setText(managerstate);
                holder.measureTv.setText(measures);
                holder.timeTv.setText(createdDate);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            final String id = idStr;

            holder.editTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    Intent intent = new Intent(UNIDCustomerActivity.this, UNIDCustomerEditActivity.class);
//                    intent.putExtra("id",id);
//                    intent.putExtra("relationId","");
//                    intent.putExtra("sourceType","00");
//                    intent.putExtra("name","");
//                    intent.putExtra("shopname","");
//                    intent.putExtra("address","");
//                    intent.putExtra("title", "编辑");
//                    UNIDCustomerActivity.this.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        class RecyclerHolder extends RecyclerView.ViewHolder{

            private TextView nameTv;
            private TextView shopnameTv;
            private TextView commonTv;
            private TextView statusTv;
            private TextView measureTv;
            private TextView timeTv;
            private TextView addressTv;
            private TextView editTv;

            private void assignViews(View itemView) {
                nameTv = (TextView) itemView.findViewById(R.id.name_tv);
                shopnameTv = (TextView) itemView.findViewById(R.id.shopname_tv);
                commonTv = (TextView) itemView.findViewById(R.id.common_tv);
                statusTv = (TextView) itemView.findViewById(R.id.status_tv);
                measureTv = (TextView) itemView.findViewById(R.id.measure_tv);
                timeTv = (TextView) itemView.findViewById(R.id.time_tv);
                addressTv = (TextView) itemView.findViewById(R.id.address_tv);
                editTv = (TextView) itemView.findViewById(R.id.edit_tv);
            }


            public RecyclerHolder(View itemView) {
                super(itemView);
                assignViews(itemView);
            }
        }
    }
}
