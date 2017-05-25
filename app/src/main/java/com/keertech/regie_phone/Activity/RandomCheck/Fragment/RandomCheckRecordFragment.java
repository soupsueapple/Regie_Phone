package com.keertech.regie_phone.Activity.RandomCheck.Fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.keertech.regie_phone.Activity.RandomCheck.RandomCheckInfoActivity;
import com.keertech.regie_phone.BaseFragment;
import com.keertech.regie_phone.Constant.Constant;
import com.keertech.regie_phone.Listener.ViewClickVibrate;
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
import java.util.Calendar;
import java.util.Date;

import static com.keertech.regie_phone.R.id.recycler_view;

/**
 * Created by soup on 2017/5/24.
 */

public class RandomCheckRecordFragment extends BaseFragment{

    Calendar calendar = Calendar.getInstance();
    private ArrayList<JSONObject> datas = new ArrayList<>();
    RecyclerAdapter recyclerAdapter = new RecyclerAdapter();

    private LinearLayout dateLl;
    private TextView dateTv;
    private TextView searchTv;
    private RecyclerView recyclerView;

    private void assignViews(View convertView) {
        dateLl = (LinearLayout) convertView.findViewById(R.id.date_ll);
        dateTv = (TextView) convertView.findViewById(R.id.date_tv);
        searchTv = (TextView) convertView.findViewById(R.id.search_tv);
        recyclerView = (RecyclerView) convertView.findViewById(recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerAdapter);

        dateLl.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                DatePickerDialog dp = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String month = "";

                                monthOfYear += 1;
                                if (monthOfYear < 10) month = "0" + monthOfYear;
                                else month = monthOfYear + "";

                                String date = year + "" + month + "";
                                dateTv.setText(date);
                            }
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));

                dp.show();
            }
        });

        searchTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                recordList();
            }
        });

        dateTv.setText(DateTimeUtil.getFormatDate(new Date(), DateTimeUtil.MONTHFORMAT));

        recordList();
    }


    private void recordList() {
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams params = new RequestParams();
        params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MWB_Base_URL + "checkCustomer!recordList.action?_query.yearMonth="+dateTv.getText().toString()+"\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

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
        View convertView = inflater.inflate(R.layout.fragment_random_check_record, null);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        assignViews(convertView);

        return convertView;
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>{

        @Override
        public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerHolder holder = new RecyclerHolder(LayoutInflater.from(RandomCheckRecordFragment.this.getActivity()).inflate(R.layout.fragment_random_check_record_recyclerview_item, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerHolder holder, int position) {
            JSONObject object = datas.get(position);

            try {

                final String id = object.getString("id");
                final String shopName = object.getString("shopName");
                final String liceNo = object.getString("liceNo");
                final String chargerName = object.getString("chargerName");
                final String contactphone = object.getString("contactphone").equals("null")?"":object.getString("contactphone");

                holder.shopnameTv.setText(shopName);
                holder.licenseTv.setText(liceNo);
                holder.nameTv.setText(chargerName);
                holder.phoneTv.setText(contactphone);
                holder.timeTv.setText(object.getString("outDate"));

                searchTv.setOnClickListener(new ViewClickVibrate(){

                    @Override
                    public void onClick(View view) {
                        super.onClick(view);
                        Intent intent = new Intent(getActivity(), RandomCheckInfoActivity.class);
                        intent.putExtra("id", id);
                        intent.putExtra("shopName", shopName);
                        intent.putExtra("liceNo", liceNo);
                        intent.putExtra("chargerName", chargerName);
                        intent.putExtra("contactphone", contactphone);
                        intent.putExtra("isLook", true);
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
            private TextView licenseTv;
            private TextView timeTv;
            private TextView shopnameTv;

            private void assignViews(View itemView) {
                nameTv = (TextView) itemView.findViewById(R.id.name_tv);
                phoneTv = (TextView) itemView.findViewById(R.id.phone_tv);
                licenseTv = (TextView) itemView.findViewById(R.id.license_tv);
                timeTv = (TextView) itemView.findViewById(R.id.time_tv);
                shopnameTv = (TextView) itemView.findViewById(R.id.shopname_tv);
            }


            public RecyclerHolder(View itemView) {
                super(itemView);
                assignViews(itemView);
            }
        }
    }

}
