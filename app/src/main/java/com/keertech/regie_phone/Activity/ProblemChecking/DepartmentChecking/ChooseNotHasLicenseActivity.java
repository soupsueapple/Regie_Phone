package com.keertech.regie_phone.Activity.ProblemChecking.DepartmentChecking;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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
 * Created by soup on 2017/5/12.
 */

public class ChooseNotHasLicenseActivity extends BaseActivity{

    private LinearLayout linearLayout15;
    private EditText shopnameTv;
    private TextView searchTv;
    private RecyclerView recyclerView;
    private FloatingActionButton saveFb;

    ArrayList<JSONObject> datas = new ArrayList<>();
    RecyclerAdapter recyclerAdapter = new RecyclerAdapter();

    int index = 0;

    String id = "";

    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            recyclerAdapter.notifyDataSetChanged();
        }
    };

    private void assignViews() {
        linearLayout15 = (LinearLayout) findViewById(R.id.linearLayout15);
        shopnameTv = (EditText) findViewById(R.id.shopname_tv);
        searchTv = (TextView) findViewById(R.id.search_tv);
        recyclerView = (RecyclerView) findViewById(recycler_view);
        saveFb = (FloatingActionButton) findViewById(R.id.save_fb);

        searchTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                if (datas.size() > 0) datas.clear();
                index = 0;
                searchBeans();
            }
        });

        saveFb.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                addCust();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);

        searchBeans();
    }

    private void searchBeans(){
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams requestParams = new RequestParams();
        requestParams.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MWB_Base_URL + "noLiceRegistrySearch!searchBeans.action?privilegeFlag=VIEW&start="+index+"&limit=30&_query.name="+shopnameTv.getText().toString()+"\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

        HttpClient.post(Constant.EXEC, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                pd.dismiss();

                try {
                    if (StringUtility.isSuccess(response)) {
                        String messageSting = response.getString("message");

                        JSONObject message = new JSONObject(messageSting);

                        if (StringUtility.isSuccess(message)) {

                            JSONArray data = message.getJSONArray("data");

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject object = data.getJSONObject(i);
                                object.put("checked", false);
                                datas.add(object);
                            }

                            recyclerAdapter.notifyDataSetChanged();

                        } else {
                            showToast(response.getString("message"), ChooseNotHasLicenseActivity.this);
                        }
                    } else {
                        showToast(response.getString("message"), ChooseNotHasLicenseActivity.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(ChooseNotHasLicenseActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(ChooseNotHasLicenseActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(ChooseNotHasLicenseActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void addCust(){

        StringBuffer buffer = new StringBuffer("");

        for(JSONObject object : datas){
            try {
                if(object.getBoolean("checked")){
                    if(buffer.toString().length()>0){
                        buffer.append(","+object.getString("id"));
                    }else{
                        buffer.append(object.getString("id"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams requestParams = new RequestParams();
        requestParams.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MWB_Base_URL + "concentratePunish!m_addNoLice.action?privilegeFlag=EDIT&bean.id="+id+"&ids="+buffer.toString()+"\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

        HttpClient.post(Constant.EXEC, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                pd.dismiss();

                try {
                    if (StringUtility.isSuccess(response)) {
                        String messageSting = response.getString("message");

                        JSONObject message = new JSONObject(messageSting);

                        if (StringUtility.isSuccess(message)) {
                            Constant.isRefresNoJZZZS = true;
                            finish();
                        } else {
                            showToast(response.getString("message"), ChooseNotHasLicenseActivity.this);
                        }
                    } else {
                        showToast(response.getString("message"), ChooseNotHasLicenseActivity.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(ChooseNotHasLicenseActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(ChooseNotHasLicenseActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(ChooseNotHasLicenseActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_not_has_license);
        setToolbarTitle("新增无证户");
        showBack();

        assignViews();
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>{

        @Override
        public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerHolder holder = new RecyclerHolder(LayoutInflater.from(ChooseNotHasLicenseActivity.this).inflate(R.layout.activity_choose_not_has_license_recyclerview_item, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerHolder holder, int position) {
            final JSONObject object = datas.get(position);

            try {


                holder.nameTv.setText(object.getString("name"));
                holder.managerTv.setText(object.getString("managerName"));
                holder.departmentTv.setText(object.getString("marketerName"));
                holder.addressTv.setText(object.getString("address"));

                if(object.getBoolean("checked")){
                    holder.checkbox.setChecked(true);
                }else{
                    holder.checkbox.setChecked(false);
                }

                holder.checkbox.setOnClickListener(new ViewClickVibrate(){

                    @Override
                    public void onClick(View view) {
                        super.onClick(view);

                        try {

                            if (object.getBoolean("checked")) {
                                object.put("checked", false);
                            } else {
                                object.put("checked", true);
                            }

                            Message msg = Message.obtain();
                            handler.sendMessage(msg);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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
            private TextView departmentTv;
            private TextView managerTv;
            private TextView addressTv;
            private CheckBox checkbox;

            private void assignViews(View itemView) {
                nameTv = (TextView) itemView.findViewById(R.id.name_tv);
                departmentTv = (TextView) itemView.findViewById(R.id.department_tv);
                managerTv = (TextView) itemView.findViewById(R.id.manager_tv);
                addressTv = (TextView) itemView.findViewById(R.id.address_tv);
                checkbox = (CheckBox) itemView.findViewById(R.id.checkbox);
            }


            public RecyclerHolder(View itemView) {
                super(itemView);

                assignViews(itemView);
            }
        }
    }
}
