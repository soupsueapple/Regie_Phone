package com.keertech.regie_phone.Activity.Notice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.keertech.regie_phone.Activity.CustomerInfo.CustomerInfoMainFragmentActivity;
import com.keertech.regie_phone.Activity.ProblemList.ReportComplaintActivity;
import com.keertech.regie_phone.Activity.VisitCheck.VisitCheckMainActivity;
import com.keertech.regie_phone.BaseActivity;
import com.keertech.regie_phone.Listener.ViewClickVibrate;
import com.keertech.regie_phone.R;
import com.keertech.regie_phone.Utility.StringUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.keertech.regie_phone.R.id.recycler_view;

/**
 * Created by soup on 2017/5/31.
 */

public class NoticeActivity extends BaseActivity{

    private ArrayList<JSONObject> datas = new ArrayList<>();

    RecyclerAdapter recyclerAdapter = new RecyclerAdapter();

    private RecyclerView recyclerView;

    private void assignViews() {
        recyclerView = (RecyclerView) findViewById(recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);

        String json = getIntent().getStringExtra("json");

        if(!StringUtility.isEmpty(json)){
            try {
                JSONArray data = new JSONArray(json);

                for(int i=0;i<data.length();i++){
                    JSONObject object = data.getJSONObject(i);
                    datas.add(object);
                }

                recyclerAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        setToolbarTitle("通知公告");
        showBack();

        assignViews();
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>{

        @Override
        public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerHolder holder = new RecyclerHolder(LayoutInflater.from(NoticeActivity.this).inflate(R.layout.activity_notice_recyclerview_item, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerHolder holder, int position) {
            JSONObject object = datas.get(position);
            try {
                final String type = object.getString("type");
                String message = object.getString("message");

                holder.titleTv.setText(message);

                holder.rl.setOnClickListener(new ViewClickVibrate(){

                    @Override
                    public void onClick(View view) {
                        super.onClick(view);
                        if(type.equals("1")){
                            Intent intent = new Intent(NoticeActivity.this, ReportComplaintActivity.class);
                            startActivity(intent);
                        }else if(type.equals("2")){
                            Intent intent = new Intent(NoticeActivity.this, CustomerInfoMainFragmentActivity.class);
                            intent.putExtra("url", "customerInfo!expireCustomerList.action");
                            startActivity(intent);
                        }else if(type.equals("3")){
                            Intent intent = new Intent(NoticeActivity.this, CustomerInfoMainFragmentActivity.class);
                            intent.putExtra("url", "customerInfo!allocationCustList.action");
                            startActivity(intent);
                        }else if(type.equals("4")){
                            Intent intent = new Intent(NoticeActivity.this, VisitCheckMainActivity.class);
                            startActivity(intent);
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

            private TextView textView22;
            private TextView titleTv;
            RelativeLayout rl;

            private void assignViews(View itemView) {
                textView22 = (TextView) itemView.findViewById(R.id.textView22);
                titleTv = (TextView) itemView.findViewById(R.id.title_tv);
                rl = (RelativeLayout) itemView.findViewById(R.id.rl);
            }


            public RecyclerHolder(View itemView) {
                super(itemView);
                assignViews(itemView);
            }
        }
    }

}
