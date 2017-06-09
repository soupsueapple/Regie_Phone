package com.keertech.regie_phone.Activity.RandomCheck.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.keertech.regie_phone.Activity.CustomerInfo.AllCityCustomerInfo.CustomerInfoActivity;
import com.keertech.regie_phone.Activity.RandomCheck.RandomCheckInfoActivity;
import com.keertech.regie_phone.BaseFragment;
import com.keertech.regie_phone.Constant.Constant;
import com.keertech.regie_phone.Network.HttpClient;
import com.keertech.regie_phone.R;
import com.keertech.regie_phone.Utility.DateTimeUtil;
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
 * Created by soup on 2017/5/24.
 */

public class RandomCheckFragment extends BaseFragment{

    private TextView checkingPeopleTv;
    private TextView dateTv;
    private TextView checkedNumberTv;
    private RecyclerView recyclerView;

    private ArrayList<JSONObject> datas = new ArrayList<>();

    RecyclerAdapter recyclerAdapter = new RecyclerAdapter();

    private void assignViews(View convertView) {
        checkingPeopleTv = (TextView) convertView.findViewById(R.id.checking_people_tv);
        dateTv = (TextView) convertView.findViewById(R.id.date_tv);
        checkedNumberTv = (TextView) convertView.findViewById(R.id.checked_number_tv);
        recyclerView = (RecyclerView) convertView.findViewById(recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerAdapter);

        customerList();
    }

    private void customerList() {
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams params = new RequestParams();
        params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MWB_Base_URL + "checkCustomer!customerList.action\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

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

                            dateTv.setText(DateTimeUtil.getFormatDate(DateTimeUtil.getCurrDate(), DateTimeUtil.MONTHFORMAT));

                            String ym = message.getString("ym");
                            String checkers = message.getString("checkers");
                            JSONArray list = message.getJSONArray("list");

                            checkingPeopleTv.setText("检查人：" + checkers);
                            dateTv.setText(ym);

                            if(datas.size() > 0) datas.clear();

                            int yjc = 0;

                            for (int i = 0; i < list.length(); i++) {
                                JSONObject object = list.getJSONObject(i);
                                int status = object.getInt("status");
                                if(status == 1) yjc += 1;
                                datas.add(object);
                            }

                            checkedNumberTv.setText(yjc + "/" + datas.size());

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
        View convertView = inflater.inflate(R.layout.fragment_random_check, null);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        assignViews(convertView);

        return convertView;
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>{

        @Override
        public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerHolder holder = new RecyclerHolder(LayoutInflater.from(RandomCheckFragment.this.getActivity()).inflate(R.layout.fragment_random_check_recyclerview_item, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerHolder holder, int position) {
            final JSONObject object = datas.get(position);

            try {

                final String id = object.getString("id");
                final String customerId = object.getString("customerId");
                final String shopName = object.getString("shopName");
                final String liceNo = object.getString("liceNo");
                final String chargerName = object.getString("chargerName");
                final String contactphone = object.getString("contactphone").equals("null")?"":object.getString("contactphone");

                int status = object.getInt("status");

                if(status == 1){
                    holder.statusTv.setText("已检查");
                    holder.checkingTv.setText("修改");
                    holder.statusTv.setTextColor(getResources().getColor(R.color.gray_green));
                }else{
                    holder.statusTv.setText("未检查");
                    holder.checkingTv.setText("开始检查");
                    holder.statusTv.setTextColor(getResources().getColor(R.color.red));
                }

                holder.shopnameTv.setText(shopName);
                holder.licenseTv.setText(liceNo);
                holder.nameTv.setText(chargerName);
                holder.phoneTv.setText(contactphone);

                holder.shopnameTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(getActivity(), CustomerInfoActivity.class);
                        intent.putExtra("id", customerId);
                        intent.putExtra("needLocation", 0);
                        getActivity().startActivity(intent);
                    }
                });

                holder.checkingTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), RandomCheckInfoActivity.class);
                        intent.putExtra("id", id);
                        intent.putExtra("shopName", shopName);
                        intent.putExtra("liceNo", liceNo);
                        intent.putExtra("chargerName", chargerName);
                        intent.putExtra("contactphone", contactphone);
                        intent.putExtra("isLook", false);
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

            private TextView nameTv;
            private TextView phoneTv;
            private TextView statusTv;
            private TextView licenseTv;
            private TextView shopnameTv;
            private TextView checkingTv;

            private void assignViews(View itemView) {
                nameTv = (TextView) itemView.findViewById(R.id.name_tv);
                phoneTv = (TextView) itemView.findViewById(R.id.phone_tv);
                statusTv = (TextView) itemView.findViewById(R.id.status_tv);
                licenseTv = (TextView) itemView.findViewById(R.id.license_tv);
                shopnameTv = (TextView) itemView.findViewById(R.id.shopname_tv);
                checkingTv = (TextView) itemView.findViewById(R.id.checking_tv);
            }



            public RecyclerHolder(View itemView) {
                super(itemView);
                assignViews(itemView);
            }
        }
    }
}
