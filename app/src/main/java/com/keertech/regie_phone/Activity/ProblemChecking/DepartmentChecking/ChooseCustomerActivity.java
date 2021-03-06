package com.keertech.regie_phone.Activity.ProblemChecking.DepartmentChecking;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class ChooseCustomerActivity extends BaseActivity{

    private LinearLayout linearLayout12;
    private EditText licenseTv;
    private EditText shopnameTv;
    private EditText nameTv;
    private TextView communityTv;
    private TextView porpertiyTv;
    private TextView commonTv;
    private TextView searchTv;
    private RecyclerView recyclerView;
    private FloatingActionButton saveTv;

    ArrayList<JSONObject> datas = new ArrayList<>();

    RecyclerAdapter recyclerAdapter = new RecyclerAdapter();

    int index = 0;

    String id = "";

    String[] communityNames;
    String[] communityIDs;

    String[] porpertiyNames = {"食杂店", "便利店", "超市", "商场", "名烟名酒名茶", "娱乐服务", "行业自办店", "其它"};
    String[] porpertiyCodes = {"104101", "104102", "104103", "104104", "104105", "104106", "104108", "104107"};

    String[] commonNames;
    String[] commonIds;

    String currentCommunityId = "";
    String currentporpertiyCode = "";
    String currentcommonId = "";

    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            recyclerAdapter.notifyDataSetChanged();
        }
    };

    private void assignViews() {
        linearLayout12 = (LinearLayout) findViewById(R.id.linearLayout12);
        licenseTv = (EditText) findViewById(R.id.license_tv);
        shopnameTv = (EditText) findViewById(R.id.shopname_tv);
        nameTv = (EditText) findViewById(R.id.name_tv);
        communityTv = (TextView) findViewById(R.id.community_tv);
        porpertiyTv = (TextView) findViewById(R.id.porpertiy_tv);
        commonTv = (TextView) findViewById(R.id.common_tv);
        searchTv = (TextView) findViewById(R.id.search_tv);
        recyclerView = (RecyclerView) findViewById(recycler_view);
        saveTv = (FloatingActionButton) findViewById(R.id.save_tv);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);

        id = getIntent().getStringExtra("id");

        communityTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                showCommunityItemDialog();
            }
        });

        porpertiyTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                showPorpertiyItemDialog();
            }
        });

        commonTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                showCommonItemDialog();
            }
        });

        searchTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                if (datas.size() > 0) datas.clear();
                index = 0;
                searchBeans();
            }
        });

        saveTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                addCust();
            }
        });

        loadingCommon();
        loadingCommunity();
//        searchBeans();
    }

    private void showCommunityItemDialog(){

        if(communityNames == null || communityIDs == null) return;

        AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle("选择社区").setItems(communityNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                communityTv.setText(communityNames[which]);
                currentCommunityId = communityIDs[which];
            }
        }).create();
        alertDialog.show();
    }

    private void showPorpertiyItemDialog(){

        AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle("选择业态").setItems(porpertiyNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                porpertiyTv.setText(porpertiyNames[which]);
                currentporpertiyCode = porpertiyCodes[which];
            }
        }).create();
        alertDialog.show();
    }

    private void showCommonItemDialog(){
        if(commonNames == null || commonIds == null) return;

        AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle("选择市管员").setItems(commonNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                commonTv.setText(commonNames[which]);
                currentcommonId = commonIds[which];
            }
        }).create();
        alertDialog.show();
    }

    private void loadingCommon(){
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams requestParams = new RequestParams();
        requestParams.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\""+ Constant.MWB_Base_URL+"common!findPostList.action\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

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

                            JSONArray data = message.optJSONArray("data");

                            commonNames = new String[data.length()];
                            commonIds = new String[data.length()];

                            for(int i=0;i<data.length();i++){
                                JSONObject object = data.optJSONObject(i);

                                String postUser = object.optString("postUser");
                                String id = object.getString("id");

                                commonNames[i] = postUser;
                                commonIds[i] = id;
                            }

                            if(data.length() > 1) {
                                commonTv.setVisibility(View.VISIBLE);
                            }else{
                                commonTv.setVisibility(View.GONE);
                            }

                        } else {
                            showToast(response.getString("message"), ChooseCustomerActivity.this);
                        }
                    } else {
                        showToast(response.getString("message"), ChooseCustomerActivity.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(ChooseCustomerActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(ChooseCustomerActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(ChooseCustomerActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void loadingCommunity(){
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams requestParams = new RequestParams();
        requestParams.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\""+ Constant.MWB_Base_URL+"communityInfo!searchBeans.action?privilegeFlag=VIEW\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

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

                            JSONArray data = message.optJSONArray("data");

                            communityNames = new String[data.length()];
                            communityIDs = new String[data.length()];

                            for(int i=0;i<data.length();i++){
                                JSONObject object = data.optJSONObject(i);

                                String communityName = object.optString("communityName");
                                String id = object.getString("id");

                                communityNames[i] = communityName;
                                communityIDs[i] = id;
                            }


                        } else {
                            showToast(response.getString("message"), ChooseCustomerActivity.this);
                        }
                    } else {
                        showToast(response.getString("message"), ChooseCustomerActivity.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(ChooseCustomerActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(ChooseCustomerActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(ChooseCustomerActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void searchBeans(){
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        String other =  (currentcommonId.length() > 0 ? "&_query.postId=" + currentcommonId : "");
        other = other + (currentporpertiyCode.length() > 0 ? "&_query.porpertiy_code=" + currentporpertiyCode : "");
        other = other + (currentCommunityId.length() > 0 ? "&_query.communityId=" + currentCommunityId : "");

        RequestParams requestParams = new RequestParams();
        requestParams.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MWB_Base_URL + "customerInfo!searchBeans.action?privilegeFlag=VIEW&start="+index+"&_query.chargerName="+nameTv.getText().toString()+"&_query.liceNo="+licenseTv.getText().toString()+"&_query.shopName="+shopnameTv.getText().toString()+ other +"\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

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

                            if(datas.size() > 0) datas.clear();

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject object = data.getJSONObject(i);
                                object.put("checked", false);
                                datas.add(object);
                            }

                            recyclerAdapter.notifyDataSetChanged();

                        } else {
                            showToast(response.getString("message"), ChooseCustomerActivity.this);
                        }
                    } else {
                        showToast(response.getString("message"), ChooseCustomerActivity.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(ChooseCustomerActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(ChooseCustomerActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(ChooseCustomerActivity.this);
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
        requestParams.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MWB_Base_URL + "concentratePunish!m_addCust.action?privilegeFlag=EDIT&bean.id="+id+"&ids="+buffer.toString()+"\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

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
                            Constant.isRefresJZZZS = true;
                            finish();
                        } else {
                            showToast(response.getString("message"), ChooseCustomerActivity.this);
                        }
                    } else {
                        showToast(response.getString("message"), ChooseCustomerActivity.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(ChooseCustomerActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(ChooseCustomerActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(ChooseCustomerActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_customer);
        setToolbarTitle("新增经营户");
        showBack();

        assignViews();
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>{

        @Override
        public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerHolder holder = new RecyclerHolder(LayoutInflater.from(ChooseCustomerActivity.this).inflate(R.layout.activity_choose_customer_recyclerview_item, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerHolder holder, int position) {
            final JSONObject object = datas.get(position);

            try {

                JSONObject department = object.getJSONObject("department");

                holder.licenseTv.setText(object.getString("liceNo"));
                holder.shopnameTv.setText(object.getString("shopName"));
                holder.departmentTv.setText(department.getString("shortName"));
                holder.nameTv.setText(object.getString("chargerName"));
                holder.phoneTv.setText(object.getString("orderPhone"));

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

            private TextView licenseTv;
            private TextView departmentTv;
            private TextView nameTv;
            private TextView phoneTv;
            private TextView shopnameTv;
            private CheckBox checkbox;

            private void assignViews(View itemView) {
                licenseTv = (TextView) itemView.findViewById(R.id.license_tv);
                departmentTv = (TextView) itemView.findViewById(R.id.department_tv);
                nameTv = (TextView) itemView.findViewById(R.id.name_tv);
                phoneTv = (TextView) itemView.findViewById(R.id.phone_tv);
                shopnameTv = (TextView) itemView.findViewById(R.id.shopname_tv);
                checkbox = (CheckBox) itemView.findViewById(R.id.checkbox);
            }


            public RecyclerHolder(View itemView) {
                super(itemView);
                assignViews(itemView);
            }
        }
    }
}
