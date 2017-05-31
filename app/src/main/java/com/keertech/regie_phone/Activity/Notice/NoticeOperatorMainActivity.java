package com.keertech.regie_phone.Activity.Notice;

import android.content.Intent;
import android.os.Bundle;
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

import com.keertech.regie_phone.Activity.CustomerInfo.CustomerInfo.CustomerInfoActivity;
import com.keertech.regie_phone.BaseActivity;
import com.keertech.regie_phone.Listener.ViewClickVibrate;
import com.keertech.regie_phone.R;
import com.keertech.regie_phone.Utility.StringUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by soup on 2017/5/31.
 */

public class NoticeOperatorMainActivity extends BaseActivity{

    private ArrayList<JSONObject> datas = new ArrayList<>();

    RecyclerAdapter recyclerAdapter = new RecyclerAdapter();

    private LinearLayout linearLayout16;
    private EditText licenseTv;
    private EditText shopnameTv;
    private EditText nameTv;
    private TextView communityTv;
    private TextView porpertiyTv;
    private TextView commonTv;
    private LinearLayout ll;
    private CheckBox statusTv;
    private CheckBox notHasPic;
    private CheckBox notHasPosition;
    private RecyclerView recyclerView;
    private FloatingActionButton searchFab;
    private FloatingActionButton mapFab;

    private void assignViews() {
        linearLayout16 = (LinearLayout) findViewById(R.id.linearLayout16);
        licenseTv = (EditText) findViewById(R.id.license_tv);
        shopnameTv = (EditText) findViewById(R.id.shopname_tv);
        nameTv = (EditText) findViewById(R.id.name_tv);
        communityTv = (TextView) findViewById(R.id.community_tv);
        porpertiyTv = (TextView) findViewById(R.id.porpertiy_tv);
        commonTv = (TextView) findViewById(R.id.common_tv);
        ll = (LinearLayout) findViewById(R.id.ll);
        statusTv = (CheckBox) findViewById(R.id.status_tv);
        notHasPic = (CheckBox) findViewById(R.id.not_has_pic);
        notHasPosition = (CheckBox) findViewById(R.id.not_has_position);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        searchFab = (FloatingActionButton) findViewById(R.id.search_fab);
        mapFab = (FloatingActionButton) findViewById(R.id.map_fab);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_customer_info);
        setToolbarTitle("经营户信息");
        showBack();

        assignViews();
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>{

        @Override
        public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerHolder holder = new RecyclerHolder(LayoutInflater.from(NoticeOperatorMainActivity.this).inflate(R.layout.fragment_customer_info_recyclerview_item, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerHolder holder, int position) {

            final JSONObject object = datas.get(position);

            try {
                String liceNo = object.getString("liceNo");
                if(liceNo.equals("null")) liceNo = "";

                holder.licenseTv.setText(liceNo);
                holder.shopnameTv.setText(object.getString("shopName"));
                holder.businessNoTv.setText(object.getString("businessNo").length() > 0 ? object.getString("businessNo") : "无");
                holder.businessNoTv.setText(object.getString("businessNo").equals("null") ? "无":object.getString("businessNo"));
                holder.nameTv.setText(object.getString("chargerName"));
                holder.dateTv.setText(object.getString("deliverTime").replaceAll(" 00:00:00", ""));

                if(object.getInt("door_photo") == 1){
                    holder.picTv.setVisibility(View.VISIBLE);
                }else{
                    holder.picTv.setVisibility(View.GONE);
                }

                int mark = object.getInt("mark");

                if(mark==1){
                    holder.markTv.setVisibility(View.VISIBLE);
                }else{
                    holder.markTv.setVisibility(View.GONE);
                }

                Double bd_latitude = null;

                if(StringUtility.notObjEmpty(object, "bd_latitude")) bd_latitude = object.getDouble("bd_latitude");

                if(bd_latitude == null){
                    holder.shopnameTv.setTextColor(getResources().getColor(R.color.red));
                }else {
                    if (bd_latitude == 0) {
                        holder.shopnameTv.setTextColor(getResources().getColor(R.color.red));
                    } else {
                        holder.shopnameTv.setTextColor(getResources().getColor(R.color.color_black));
                    }
                }

                holder.lookUpTv.setOnClickListener(new ViewClickVibrate(){

                    @Override
                    public void onClick(View view) {
                        super.onClick(view);

                        try {
                            Intent intent = new Intent(NoticeOperatorMainActivity.this, CustomerInfoActivity.class);
                            intent.putExtra("id", object.getString("id"));
                            if (!object.isNull("needLocation"))
                                intent.putExtra("needLocation", object.getInt("needLocation"));
                            else intent.putExtra("needLocation", false);
                            startActivity(intent);
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
            private TextView businessNoTv;
            private TextView nameTv;
            private TextView dateTv;
            private TextView shopnameTv;
            private TextView lookUpTv, picTv, markTv;

            private void assignViews(View itemView) {
                licenseTv = (TextView) itemView.findViewById(R.id.license_tv);
                businessNoTv = (TextView) itemView.findViewById(R.id.business_no_tv);
                nameTv = (TextView) itemView.findViewById(R.id.name_tv);
                dateTv = (TextView) itemView.findViewById(R.id.date_tv);
                shopnameTv = (TextView) itemView.findViewById(R.id.shopname_tv);
                lookUpTv = (TextView) itemView.findViewById(R.id.look_up_tv);
                picTv = (TextView) itemView.findViewById(R.id.pic_tv);
                markTv = (TextView) itemView.findViewById(R.id.mark_tv);
            }


            public RecyclerHolder(View itemView) {
                super(itemView);

                assignViews(itemView);
            }
        }
    }
}
