package com.keertech.regie_phone.Activity.XZFW.ServicePlan;

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
import android.widget.CompoundButton;
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
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by soup on 2017/5/9.
 */

public class AddServicePlanAcvitivy extends BaseActivity{

    private CheckBox selelctAllCb;
    private CheckBox showNotDoneCb;
    private RecyclerView recyclerView;
    private FloatingActionButton addFb;
    private TextView select_num_tv;

    ArrayList<JSONObject> datas = new ArrayList<>();

    RecyclerAdapter recyclerAdapter = new RecyclerAdapter();

    boolean isXccs = false;
    boolean isZmzh = false;
    boolean isJieDao = false;
    boolean isShequ = false;

    String id = "";

    String yearMonth = "";

    int isFinish = 0;

    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            recyclerAdapter.notifyDataSetChanged();
        }
    };

    private void assignViews() {
        selelctAllCb = (CheckBox) findViewById(R.id.selelct_all_cb);
        showNotDoneCb = (CheckBox) findViewById(R.id.show_not_done_cb);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        addFb = (FloatingActionButton) findViewById(R.id.add_fb);
        select_num_tv = (TextView) findViewById(R.id.select_num_tv);

        addFb.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);

                addCust();
            }
        });

        selelctAllCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){

                    for(JSONObject o: datas){
                        try {
                            o.put("checked", true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    recyclerAdapter.notifyDataSetChanged();

                }else{

                    for(JSONObject o: datas){
                        try {
                            o.put("checked", false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    recyclerAdapter.notifyDataSetChanged();

                }
            }
        });

        showNotDoneCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    isFinish = 1;
                    searchCustromers();
                }else{
                    isFinish = 0;
                    searchCustromers();
                }
            }
        });

        id = getIntent().getStringExtra("id");
        yearMonth = getIntent().getStringExtra("yearMonth");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);

        searchCustromers();
    }

    private void searchCustromers(){
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams requestParams = new RequestParams();
        requestParams.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MWB_Base_URL + "inspectWeekPlan!customerList.action?yearMonth="+yearMonth+"&isFinish="+isFinish+"\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

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
                            showToast(response.getString("message"), AddServicePlanAcvitivy.this);
                        }
                    } else {
                        showToast(response.getString("message"), AddServicePlanAcvitivy.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(AddServicePlanAcvitivy.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(AddServicePlanAcvitivy.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(AddServicePlanAcvitivy.this);
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

        if(buffer.toString().length() == 0){
            showToast("请选择经营户", this);

            return;
        }

        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams requestParams = new RequestParams();
        requestParams.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MWB_Base_URL + "inspectWeekPlan!m_addCust.action?inspectWeek.id="+id+"&ids="+buffer.toString()+"\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

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
                            Constant.isRefresPlan = true;
                            finish();
                        } else {
                            showToast(response.getString("message"), AddServicePlanAcvitivy.this);
                        }
                    } else {
                        showToast(response.getString("message"), AddServicePlanAcvitivy.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(AddServicePlanAcvitivy.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(AddServicePlanAcvitivy.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(AddServicePlanAcvitivy.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_plan_add);
        setToolbarTitle("添加服务计划");
        showBack();

        assignViews();
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>{

        @Override
        public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerHolder holder = new RecyclerHolder(LayoutInflater.from(AddServicePlanAcvitivy.this).inflate(R.layout.activity_service_plan_add_recylerview_item, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerHolder holder, int position) {
            final JSONObject object = datas.get(position);

            try {

                holder.licenseTv.setText(object.getString("liceNo"));
                holder.shopnameTv.setText(object.getString("shopName"));
                holder.streetTv.setText(object.getString("streetName"));
                holder.communityTv.setText(object.getString("communityName"));
                holder.nameTv.setText(object.getString("chargerName"));

                int mustCount = object.getInt("mustCount");
                int finishCount = object.getInt("finishCount");

                holder.serviceNumTv.setText(finishCount + "/" + mustCount);

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

                            int num = 0;

                            for(JSONObject o: datas){
                                if (o.getBoolean("checked")) num += 1;
                            }

                            select_num_tv.setText("你已选择"+ num + "个经营户");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

                holder.rl.setOnClickListener(new ViewClickVibrate(){

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

                            int num = 0;

                            for(JSONObject o: datas){
                                if (o.getBoolean("checked")) num += 1;
                            }

                            select_num_tv.setText("你已选择"+ num + "个经营户");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

                holder.licenseTv.setOnClickListener(new ViewClickVibrate(){

                    @Override
                    public void onClick(View view) {
                        super.onClick(view);

                        if(isZmzh) {
                            isZmzh = false;
                            Collections.sort(datas, comparator_zh2);
                        }else{
                            isZmzh = true;
                            Collections.sort(datas, comparator_zh1);
                        }

                        Message msg = Message.obtain();
                        handler.sendMessage(msg);
                    }
                });


                holder.serviceNumTv.setOnClickListener(new ViewClickVibrate(){

                    @Override
                    public void onClick(View view) {
                        super.onClick(view);

                        if(isXccs) {
                            isXccs = false;
                            Collections.sort(datas, comparator_xccs2);
                        }else{
                            isXccs = true;
                            Collections.sort(datas, comparator_xccs1);
                        }

                        Message msg = Message.obtain();
                        handler.sendMessage(msg);

                    }
                });

                holder.streetTv.setOnClickListener(new ViewClickVibrate(){

                    @Override
                    public void onClick(View view) {
                        super.onClick(view);

                        if(isJieDao) {
                            isJieDao = false;
                            Collections.sort(datas, comparator_jd2);
                        }else{
                            isJieDao = true;
                            Collections.sort(datas, comparator_jd1);
                        }

                        Message msg = Message.obtain();
                        handler.sendMessage(msg);

                    }
                });

                holder.communityTv.setOnClickListener(new ViewClickVibrate(){

                    @Override
                    public void onClick(View view) {
                        super.onClick(view);

                        if(isShequ) {
                            isShequ = false;
                            Collections.sort(datas, comparator_sq2);
                        }else{
                            isShequ = true;
                            Collections.sort(datas, comparator_sq1);
                        }

                        Message msg = Message.obtain();
                        handler.sendMessage(msg);


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
            private TextView streetTv;
            private TextView communityTv;
            private CheckBox checkbox;
            private TextView serviceNumTv;
            RelativeLayout rl;

            private void assignViews(View itemView) {
                shopnameTv = (TextView) itemView.findViewById(R.id.shopname_tv);
                licenseTv = (TextView) itemView.findViewById(R.id.license_tv);
                nameTv = (TextView) itemView.findViewById(R.id.name_tv);
                streetTv = (TextView) itemView.findViewById(R.id.street_tv);
                communityTv = (TextView) itemView.findViewById(R.id.community_tv);
                checkbox = (CheckBox) itemView.findViewById(R.id.checkbox);
                serviceNumTv = (TextView) itemView.findViewById(R.id.service_num_tv);
                rl = (RelativeLayout)itemView.findViewById(R.id.rl);
            }


            public RecyclerHolder(View itemView) {
                super(itemView);
                assignViews(itemView);
            }
        }
    }

    final Comparator<JSONObject> comparator_xccs1 = new Comparator<JSONObject>() {
        @Override
        public int compare(JSONObject lhs, JSONObject rhs) {
            try {

                int mustCountLn = lhs.getInt("mustCount");
                int finishCountLn = lhs.getInt("finishCount");

                int mustCountRn = rhs.getInt("mustCount");
                int finishCountRn = rhs.getInt("finishCount");

                int  ln = mustCountLn - finishCountLn;
                int  rn = mustCountRn - finishCountRn;

                int dw = 0;

                if (ln < rn) dw = 1;
                else if (ln > rn) dw = -1;
                else if (ln == rn) dw = 0;

                return dw;



            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }
    };

    final Comparator<JSONObject> comparator_xccs2 = new Comparator<JSONObject>() {
        @Override
        public int compare(JSONObject lhs, JSONObject rhs) {
            try {

                int mustCountLn = lhs.getInt("mustCount");
                int finishCountLn = lhs.getInt("finishCount");

                int mustCountRn = rhs.getInt("mustCount");
                int finishCountRn = rhs.getInt("finishCount");

                int  ln = mustCountLn - finishCountLn;
                int  rn = mustCountRn - finishCountRn;

                int dw = 0;

                if (ln > rn) dw = 1;
                else if (ln < rn) dw = -1;
                else if (ln == rn) dw = 0;

                return dw;



            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }
    };

    final Comparator<JSONObject> comparator_zh1 = new Comparator<JSONObject>() {
        @Override
        public int compare(JSONObject lhs, JSONObject rhs) {
            try {
                long ln = lhs.getLong("liceNo");
                long rn = rhs.getLong("liceNo");

                int dw = 0;

                if (ln < rn) dw = 1;
                else if (ln > rn) dw = -1;
                else if (ln == rn) dw = 0;

                return dw;

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }
    };

    final Comparator<JSONObject> comparator_zh2 = new Comparator<JSONObject>() {
        @Override
        public int compare(JSONObject lhs, JSONObject rhs) {
            try {
                long ln = lhs.getLong("liceNo");
                long rn = rhs.getLong("liceNo");

                int dw = 0;

                if (ln > rn) dw = 1;
                else if (ln < rn) dw = -1;
                else if (ln == rn) dw = 0;

                return dw;

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }
    };

    final Comparator<JSONObject> comparator_jd1 = new Comparator<JSONObject>() {
        @Override
        public int compare(JSONObject lhs, JSONObject rhs) {
            try {
                int ln = lhs.getInt("streetId");
                int rn = rhs.getInt("streetId");

                int dw = 0;

                if (ln < rn) dw = 1;
                else if (ln > rn) dw = -1;
                else if (ln == rn) dw = 0;

                return dw;

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }
    };

    final Comparator<JSONObject> comparator_jd2 = new Comparator<JSONObject>() {
        @Override
        public int compare(JSONObject lhs, JSONObject rhs) {
            try {
                int ln = lhs.getInt("streetId");
                int rn = rhs.getInt("streetId");

                int dw = 0;

                if (ln > rn) dw = 1;
                else if (ln < rn) dw = -1;
                else if (ln == rn) dw = 0;

                return dw;

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }
    };

    final Comparator<JSONObject> comparator_sq1 = new Comparator<JSONObject>() {
        @Override
        public int compare(JSONObject lhs, JSONObject rhs) {
            try {
                int ln = lhs.getInt("communityId");
                int rn = rhs.getInt("communityId");

                int dw = 0;

                if (ln < rn) dw = 1;
                else if (ln > rn) dw = -1;
                else if (ln == rn) dw = 0;

                return dw;

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }
    };

    final Comparator<JSONObject> comparator_sq2 = new Comparator<JSONObject>() {
        @Override
        public int compare(JSONObject lhs, JSONObject rhs) {
            try {
                int ln = lhs.getInt("communityId");
                int rn = rhs.getInt("communityId");

                int dw = 0;

                if (ln > rn) dw = 1;
                else if (ln < rn) dw = -1;
                else if (ln == rn) dw = 0;

                return dw;

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }
    };
}
