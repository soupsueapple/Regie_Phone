package com.keertech.regie_phone.Activity.SalesInteraction.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.keertech.regie_phone.Activity.SalesInteraction.NotHaveLicenseCustomerInformationReportTableActivity;
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
 * Created by soup on 2017/5/25.
 */

public class NotHaveLicenseCustomerInformationReportTableFragment extends BaseFragment{

    private RecyclerView recyclerView;

    private ArrayList<JSONObject> datas = new ArrayList<JSONObject>();
    RecyclerAdapter recyclerAdapter = new RecyclerAdapter();

    private void assignViews(View convertView) {
        recyclerView = (RecyclerView) convertView.findViewById(recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerAdapter);

        hdNoLicenseSubmit();

    }

    @Override
    public void onResume() {
        super.onResume();
        if(Constant.isRefresWZH){
            Constant.isRefresWZH = false;

            hdNoLicenseSubmit();
        }
    }

    private void hdNoLicenseSubmit(){
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams requestParams = new RequestParams();
        requestParams.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\""+ Constant.MWB_Base_URL+"hdNoLicenseSubmit!searchBeans.action?privilegeFlag=VIEW\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

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


                            if (datas.size() > 0)
                                datas.clear();

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View convertView = inflater.inflate(R.layout.fragment_customer_information_report_table, null);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        assignViews(convertView);

        return convertView;
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>{

        @Override
        public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerHolder holder = new RecyclerHolder(LayoutInflater.from(NotHaveLicenseCustomerInformationReportTableFragment.this.getActivity()).inflate(R.layout.fragment_nothave_license_customer_information_report_table_recyclerview_item, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerHolder holder, int position) {
            JSONObject object = datas.get(position);

            try {
                holder.shopnameTv.setText(object.getString("shopName"));
                holder.nameTv.setText(object.getString("name"));
                holder.phoneTv.setText(object.getString("phone"));
                holder.isIntentTv.setText(object.getInt("hasDesire") == 1?"有办证意向":"无办证意向");
                holder.reportDateTv.setText(object.getString("createdDate"));

                if(StringUtility.notObjEmpty(object, "resultStatus")) {
                    int resultStatus = object.getInt("resultStatus");

                    if (resultStatus == 1) {
                        holder.measureResultTv.setText("情况属实");
                    } else if (resultStatus == 2) {
                        holder.measureResultTv.setText("情况不属实");
                    } else {
                        holder.measureResultTv.setText("未处理");
                    }
                }else {
                    holder.measureResultTv.setText("未处理");
                }

                if(StringUtility.notObjEmpty(object, "resultDate")){
                    String resultDate = object.getString("resultDate");

                    holder.inspectDateTv.setText(resultDate);
                }else{
                    holder.inspectDateTv.setText("");
                }

                final String id = object.getString("id");
                int resultStatus = 0;

                if(StringUtility.notObjEmpty(object, "resultStatus")){
                    resultStatus = object.getInt("resultStatus");
                }

                final int result = resultStatus;

                holder.searchTv.setOnClickListener(new ViewClickVibrate(){

                    @Override
                    public void onClick(View view) {
                        super.onClick(view);
                        Intent intent = new Intent(getActivity(), NotHaveLicenseCustomerInformationReportTableActivity.class);
                        intent.putExtra("id", id);
                        intent.putExtra("resultStatus", result);
                        getActivity().startActivity(intent);
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
            private TextView phoneTv;
            private TextView isIntentTv;
            private TextView reportDateTv;
            private TextView measureResultTv;
            private TextView inspectDateTv;
            private TextView searchTv;

            private void assignViews(View itemView) {
                shopnameTv = (TextView) itemView.findViewById(R.id.shopname_tv);
                nameTv = (TextView) itemView.findViewById(R.id.name_tv);
                phoneTv = (TextView) itemView.findViewById(R.id.phone_tv);
                isIntentTv = (TextView) itemView.findViewById(R.id.is_intent_tv);
                reportDateTv = (TextView) itemView.findViewById(R.id.report_date_tv);
                measureResultTv = (TextView) itemView.findViewById(R.id.measure_result_tv);
                inspectDateTv = (TextView) itemView.findViewById(R.id.inspect_date_tv);
                searchTv = (TextView) itemView.findViewById(R.id.search_tv);
            }


            public RecyclerHolder(View itemView) {
                super(itemView);
                assignViews(itemView);
            }
        }
    }

}
