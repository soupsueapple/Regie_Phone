package com.keertech.regie_phone.Activity.ReportComplanints;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.keertech.regie_phone.Activity.ReportComplanints.ToBeContinued.ToBeContinuedActivity;
import com.keertech.regie_phone.BaseFragment;
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

import static com.keertech.regie_phone.R.id.recycler_view;

/**
 * Created by soup on 2017/5/16.
 */

public class ToBeContinuedFragment extends BaseFragment{

    private RecyclerView recyclerView;

    private ArrayList<JSONObject> datas = new ArrayList<>();

    RecyclerAdapter recyclerAdapter = new RecyclerAdapter();

    private void assignViews(View convertView) {
        recyclerView = (RecyclerView) convertView.findViewById(recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerAdapter);

        loadData();
    }

    private void loadData(){
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams params = new RequestParams();
        params.put("data","{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\""+Constant.MIIS_Base_URL+"complainManageFlow!taskList.action\",\"parameter\":{},\"type\":\"WebExecutor\",\"method\":\"POST\"},\"app\":\"1003\"}");

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

                            if (datas.size() > 0) datas.clear();

                            JSONArray jsonArray = message.getJSONArray("data");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                datas.add(jsonObject);
                            }

                            recyclerAdapter.notifyDataSetChanged();

                        } else {
                            showToast(message.getString("message"), getActivity());
                        }
                    }else {
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
    public void onResume() {
        super.onResume();
        if(Constant.isRefreshTSJB){
            loadData();
            Constant.isRefreshTSJB = false;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View convertView = inflater.inflate(R.layout.fragment_report_complanints_to_be_continued, null);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        assignViews(convertView);

        return convertView;
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>{

        @Override
        public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerHolder holder = new RecyclerHolder(LayoutInflater.from(ToBeContinuedFragment.this.getActivity()).inflate(R.layout.fragment_report_complanints_to_be_continued_recyclerview_item, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerHolder holder, int position) {
            JSONObject object = datas.get(position);

            final String jsonString = object.toString();

            try {
                String cigLicenseNo = object.getString("ciglicenseno");
                String tradename = object.getString("tradename");
                String customerName = object.getString("customername");
                String workOrderDate = object.getString("workorderdate");
                String phoneType = object.getString("phonetype");

                holder.licenseTv.setText(cigLicenseNo);
                holder.shopnameTv.setText(tradename);
                holder.nameTv.setText(customerName);
                holder.timeTv.setText(workOrderDate);
                holder.sourceTv.setText(StringUtility.isEmpty(phoneType)?"":phoneType);

                holder.lookUpTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ToBeContinuedActivity.class);
                        intent.putExtra("data", jsonString);
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

            private TextView licenseTv;
            private TextView nameTv;
            private TextView timeTv;
            private TextView sourceTv;
            private TextView shopnameTv;
            private TextView lookUpTv;

            private void assignViews(View itemView) {
                licenseTv = (TextView) itemView.findViewById(R.id.license_tv);
                nameTv = (TextView) itemView.findViewById(R.id.name_tv);
                timeTv = (TextView) itemView.findViewById(R.id.time_tv);
                sourceTv = (TextView) itemView.findViewById(R.id.source_tv);
                shopnameTv = (TextView) itemView.findViewById(R.id.shopname_tv);
                lookUpTv = (TextView) itemView.findViewById(R.id.look_up_tv);
            }


            public RecyclerHolder(View itemView) {
                super(itemView);
                assignViews(itemView);
            }
        }
    }
}
