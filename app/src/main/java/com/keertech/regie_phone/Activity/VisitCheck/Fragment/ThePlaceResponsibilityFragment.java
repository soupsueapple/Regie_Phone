package com.keertech.regie_phone.Activity.VisitCheck.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.keertech.regie_phone.Activity.VisitCheck.ThePlaceResponsibility.ThePlaceResponsibilityInfoActivity;
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
 * Created by soup on 2017/5/23.
 */

public class ThePlaceResponsibilityFragment extends BaseFragment{

    private RecyclerView recyclerView;

    ArrayList<JSONObject> datas = new ArrayList<>();

    RecyclerAdapter recyclerAdapter = new RecyclerAdapter();

    private void assignViews(View convertView) {
        recyclerView = (RecyclerView) convertView.findViewById(recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerAdapter);

        assignPostList();
    }

    private void assignPostList() {
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams params = new RequestParams();
        params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MWB_Base_URL + "marketSecretlyCheckMobile!assignPostList.action\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

        HttpClient.post(Constant.EXEC, params, new JsonHttpResponseHandler() {
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

                            if(datas.size() > 0) datas.clear();

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject object = data.getJSONObject(i);
                                datas.add(object);
                            }

                            recyclerAdapter.notifyDataSetChanged();

                        } else {
                            showToast(message.getString("message"), getActivity());
                        }
                    }else{
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
        View convertView = inflater.inflate(R.layout.fragment_theplace, null);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        assignViews(convertView);

        return convertView;
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>{

        @Override
        public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerHolder holder = new RecyclerHolder(LayoutInflater.from(ThePlaceResponsibilityFragment.this.getActivity()).inflate(R.layout.fragment_theplace_recyclerview_item, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerHolder holder, int position) {
            JSONObject object = datas.get(position);

            try {
                holder.shopnameTv.setText(object.getString("shopName"));
                holder.addressTv.setText(object.getString("shopAddress"));
                if(!object.isNull("checkDate"))holder.checkedDateTv.setText(object.getString("checkDate").substring(0, 10));

                int isAZSM = object.getInt("isAZSM");
                int isGKBM = object.getInt("isGKBM");
                int isWZJY = object.getInt("isWZJY");

                StringBuffer status = new StringBuffer("");

                if(isAZSM == 1) status.append("暗中售卖");

                if(isGKBM == 1) {
                    if(status.length() > 0) status.append(", 公开摆卖");
                    else status.append("公开摆卖");
                }

                if(isWZJY == 1){
                    if(status.length() > 0) status.append(", 无证经营");
                    else status.append("无证经营");
                }

                holder.resultTv.setText(status.length() > 0 ? status.toString(): "正常");

                final String taskId = object.getString("taskId");

                holder.searchTv.setOnClickListener(new ViewClickVibrate() {
                    @Override
                    public void onClick(View v) {
                        super.onClick(v);
                        Intent intent = new Intent(getActivity(), ThePlaceResponsibilityInfoActivity.class);
                        intent.putExtra("taskId", taskId);
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
            private TextView resultTv;
            private TextView checkedDateTv;
            private TextView addressTv;
            private TextView searchTv;

            private void assignViews(View itemView) {
                shopnameTv = (TextView) itemView.findViewById(R.id.shopname_tv);
                resultTv = (TextView) itemView.findViewById(R.id.result_tv);
                checkedDateTv = (TextView) itemView.findViewById(R.id.checked_date_tv);
                addressTv = (TextView) itemView.findViewById(R.id.address_tv);
                searchTv = (TextView) itemView.findViewById(R.id.search_tv);
            }


            public RecyclerHolder(View itemView) {
                super(itemView);
                assignViews(itemView);
            }
        }
    }

}
