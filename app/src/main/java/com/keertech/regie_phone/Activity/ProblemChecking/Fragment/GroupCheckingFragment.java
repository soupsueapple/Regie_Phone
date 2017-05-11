package com.keertech.regie_phone.Activity.ProblemChecking.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.keertech.regie_phone.Activity.ProblemChecking.CheckingActivity;
import com.keertech.regie_phone.BaseFragment;
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
 * Created by soup on 2017/5/11.
 */

public class GroupCheckingFragment extends BaseFragment{

    private EditText licenseTv;
    private EditText shopnameTv;
    private EditText nameTv;
    private TextView searchTv;
    private RecyclerView recyclerView;

    private ArrayList<JSONObject> datas = new ArrayList<>();

    RecyclerAdapter recyclerAdapter = new RecyclerAdapter();

    private void assignViews(View convertView) {
        licenseTv = (EditText) convertView.findViewById(R.id.license_tv);
        shopnameTv = (EditText) convertView.findViewById(R.id.shopname_tv);
        nameTv = (EditText) convertView.findViewById(R.id.name_tv);
        searchTv = (TextView) convertView.findViewById(R.id.search_tv);
        recyclerView = (RecyclerView) convertView.findViewById(recycler_view);

        searchTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);

                loadData(licenseTv.getText().toString(), shopnameTv.getText().toString(), nameTv.getText().toString());
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerAdapter);

        loadData(licenseTv.getText().toString(), shopnameTv.getText().toString(), nameTv.getText().toString());
    }

    private void loadData(String liceNo, String shopName, String chargerName){
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        String other = liceNo.length() > 0 ? "&_query.liceNo=" + liceNo : "";
        other = other + (shopName.length() > 0 ? "&_query.shopName=" + shopName : "");
        other = other + (chargerName.length() > 0 ? "&_query.chargerName=" + chargerName : "");

        RequestParams requestParams = new RequestParams();
        requestParams.put("data","{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\""+ Constant.MWB_Base_URL+"marketCheck!customerList.action?privilegeFlag=VIEW"+other+"\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

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

                            if(datas.size() > 0 )datas.clear();

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject object = data.getJSONObject(i);

                                datas.add(object);
                            }

                            recyclerAdapter.notifyDataSetChanged();
                        } else {
                            showToast(response.getString("message"), getActivity());
                        }
                    } else {
                        showToast(response.getString("message"), getActivity());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(getActivity());
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(getActivity());
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(getActivity());
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View convertView = inflater.inflate(R.layout.fragment_group_checking, null);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        assignViews(convertView);

        return convertView;
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>{

        @Override
        public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerHolder holder = new RecyclerHolder(LayoutInflater.from(GroupCheckingFragment.this.getActivity()).inflate(R.layout.fragment_group_checking_recyclerview_item, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerHolder holder, int position) {

            final JSONObject object = datas.get(position);

            try {

                String shopName = object.getString("shopName");
                String chargerName = object.getString("chargerName");

                int dataSource = object.getInt("dataSource");
                int level = StringUtility.notObjEmpty(object, "level")?object.getInt("level"):1;
                final int status = object.getInt("status");

                if(dataSource == 1){
                    holder.sourceTv.setText("行政服务");
                }else if(dataSource == 2){
                    holder.sourceTv.setText("举报投诉");
                }else if(dataSource == 3){
                    holder.sourceTv.setText("APCD预警");
                }else if(dataSource == 5){
                    holder.sourceTv.setText("专销互动");
                }else if(dataSource == 6){
                    holder.sourceTv.setText("暗访检查");
                }else if(dataSource == 61){
                    holder.sourceTv.setText("省局暗访检查");
                }else if(dataSource == 62){
                    holder.sourceTv.setText("市局暗访检查");
                }else if(dataSource == 63){
                    holder.sourceTv.setText("第三方检查");
                }else if(dataSource == 64){
                    holder.sourceTv.setText("区局暗访检查");
                }

                if(level == 1){
                    holder.levelTv.setText("市管组");
                }else if(level == 2){
                    holder.levelTv.setText("管理所");
                }else if(level == 3){
                    holder.levelTv.setText("区局");
                }

                final int id = object.getInt("id");



                if(!object.isNull("liceNo")) {
                    String liceNo = object.getString("liceNo");
                    holder.licenseTv.setText(liceNo);

                    if(status == 1){
                        holder.checkingTv.setText("开始核查");
                        holder.checkingTv.setBackgroundResource(R.drawable.corners_green_bg);

                        holder.checkingTv.setOnClickListener(new ViewClickVibrate() {
                            @Override
                            public void onClick(View v) {
                                super.onClick(v);
                                try {
                                    loadyc(object);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }else if(status == 2){
                        holder.checkingTv.setText("完成核查");
                        holder.checkingTv.setBackgroundResource(R.drawable.corners_gray_bg);

                        holder.checkingTv.setOnClickListener(null);
                    }else if(status == 3){
                        holder.checkingTv.setText("完成");
                        holder.checkingTv.setBackgroundResource(R.drawable.corners_gray_bg);
                        holder.checkingTv.setOnClickListener(null);
                    }else if(status == 4){
                        holder.checkingTv.setText("等待核查");
                        holder.checkingTv.setBackgroundResource(R.drawable.corners_gray_bg);

                        holder.checkingTv.setOnClickListener(null);
                    }else if(status == 5){
                        holder.checkingTv.setText("待确认");
                        holder.checkingTv.setBackgroundResource(R.drawable.corners_green_bg);

                        holder.checkingTv.setOnClickListener(new ViewClickVibrate() {
                            @Override
                            public void onClick(View v) {
                                super.onClick(v);

                                try {
                                    loadyc(object);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }else if(status == 6){
                        holder.checkingTv.setText("已确认");
                        holder.checkingTv.setBackgroundResource(R.drawable.corners_gray_bg);

                        holder.checkingTv.setOnClickListener(null);
                    }
                }else{
                    holder.licenseTv.setText("");

                    if(status == 1){
                        holder.checkingTv.setText("开始核查");
                        holder.checkingTv.setBackgroundResource(R.drawable.corners_green_bg);

                        holder.checkingTv.setOnClickListener(new ViewClickVibrate() {
                            @Override
                            public void onClick(View v) {
                                super.onClick(v);
                                Intent intent = new Intent(GroupCheckingFragment.this.getActivity(), CheckingActivity.class);
                                intent.putExtra("id", id);
                                intent.putExtra("status", status);
                                startActivity(intent);
                            }
                        });
                    }else if(status == 2){
                        holder.checkingTv.setText("完成核查");
                        holder.checkingTv.setBackgroundResource(R.drawable.corners_gray_bg);

                        holder.checkingTv.setOnClickListener(null);
                    }else if(status == 3){
                        holder.checkingTv.setText("完成");
                        holder.checkingTv.setBackgroundResource(R.drawable.corners_gray_bg);
                        holder.checkingTv.setOnClickListener(null);
                    }else if(status == 4){
                        holder.checkingTv.setText("等待核查");
                        holder.checkingTv.setBackgroundResource(R.drawable.corners_gray_bg);

                        holder.checkingTv.setOnClickListener(null);
                    }else if(status == 5){
                        holder.checkingTv.setText("待确认");
                        holder.checkingTv.setBackgroundResource(R.drawable.corners_green_bg);

                        holder.checkingTv.setOnClickListener(new ViewClickVibrate() {
                            @Override
                            public void onClick(View v) {
                                super.onClick(v);

                                Intent intent = new Intent(GroupCheckingFragment.this.getActivity(), CheckingActivity.class);
                                intent.putExtra("id", id);
                                intent.putExtra("status", status);
                                startActivity(intent);
                            }
                        });
                    }else if(status == 6){
                        holder.checkingTv.setText("已确认");
                        holder.checkingTv.setBackgroundResource(R.drawable.corners_gray_bg);

                        holder.checkingTv.setOnClickListener(null);
                    }

                }
                holder.shopnameTv.setText(shopName);
                holder.nameTv.setText(chargerName);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        private void loadyc(JSONObject object) throws JSONException {

            final String customerId = object.getString("customerId");
            final int status = object.getInt("status");
            final int id = object.getInt("id");
            final int postType = object.getInt("postType");

            final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
            pd.show();

            RequestParams params = new RequestParams();
            params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\""+Constant.MWB_Base_URL+"marketInspect!customerAbnormal.action?privilegeFlag=VIEW&_query.customerId="+customerId+"\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

            HttpClient.post(Constant.EXEC, params, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    pd.dismiss();

                    try {
                        if (StringUtility.isSuccess(response)) {
                            String messageSting = response.getString("message");

                            JSONObject message = new JSONObject(messageSting);

                            if (StringUtility.isSuccess(message)) {

                                final String zjAbnormal = message.getString("zjAbnormal");
                                final String jyAbnormal = message.getString("jyAbnormal");
                                final String apcdAbnormal = message.getString("apcdAbnormal");
                                final String dsfAbnormal = message.getString("dsfAbnormal");

                                StringBuffer buffer = new StringBuffer("");
                                if(zjAbnormal.length() > 0)buffer.append("证件异常：\n"+zjAbnormal+"\n");
                                if(jyAbnormal.length() > 0)buffer.append("经营异常：\n"+jyAbnormal+"\n");
                                if(apcdAbnormal.length() > 0)buffer.append("APCD异常：\n"+apcdAbnormal+"\n");
                                if(dsfAbnormal.length() > 0)buffer.append("第三方调查异常：\n"+dsfAbnormal+"\n");

                                if(buffer.length() > 0) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(GroupCheckingFragment.this.getActivity());
                                    builder.setMessage(buffer.toString());
                                    builder.setTitle("异常信息");
                                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(GroupCheckingFragment.this.getActivity(), CheckingActivity.class);
                                            intent.putExtra("id", id);
                                            intent.putExtra("status", status);
                                            intent.putExtra("postType", postType);
                                            intent.putExtra("concentratePunish", "");
                                            startActivity(intent);
                                        }
                                    });
                                    builder.create().show();
                                }else{
                                    Intent intent = new Intent(GroupCheckingFragment.this.getActivity(), CheckingActivity.class);
                                    intent.putExtra("id", id);
                                    intent.putExtra("status", status);
                                    startActivity(intent);
                                }

                            }else{
                                showToast(response.getString("message"), getActivity());
                            }
                        } else {
                            showToast(response.getString("message"), getActivity());
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    pd.dismiss();
                    showNetworkError(getActivity());
                    super.onFailure(statusCode, headers, responseString, throwable);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    pd.dismiss();
                    showNetworkError(getActivity());
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    pd.dismiss();
                    showNetworkError(getActivity());
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        class RecyclerHolder extends RecyclerView.ViewHolder{

            private TextView shopnameTv;
            private TextView licenseTv;
            private TextView nameTv;
            private TextView sourceTv;
            private TextView levelTv;
            private TextView checkingTv;

            private void assignViews(View itemView) {
                shopnameTv = (TextView) itemView.findViewById(R.id.shopname_tv);
                licenseTv = (TextView) itemView.findViewById(R.id.license_tv);
                nameTv = (TextView) itemView.findViewById(R.id.name_tv);
                sourceTv = (TextView) itemView.findViewById(R.id.source_tv);
                levelTv = (TextView) itemView.findViewById(R.id.level_tv);
                checkingTv = (TextView) itemView.findViewById(R.id.checking_tv);
            }


            public RecyclerHolder(View itemView) {
                super(itemView);
                assignViews(itemView);
            }
        }
    }
}
