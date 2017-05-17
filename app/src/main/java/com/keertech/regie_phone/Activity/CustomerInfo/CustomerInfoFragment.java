package com.keertech.regie_phone.Activity.CustomerInfo;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.keertech.regie_phone.BaseFragment;
import com.keertech.regie_phone.Listener.ViewClickVibrate;
import com.keertech.regie_phone.R;
import com.keertech.regie_phone.Utility.StringUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by soup on 2017/5/17.
 */

public class CustomerInfoFragment extends BaseFragment{

    private LinearLayout linearLayout16;
    private EditText licenseTv;
    private EditText shopnameTv;
    private EditText nameTv;
    private TextView communityTv;
    private TextView porpertiyTv;
    private TextView commonTv;
    private CheckBox statusTv;
    private CheckBox notHasPic;
    private CheckBox notHasPosition;
    private RecyclerView recyclerView;
    private FloatingActionButton searchFab;
    private FloatingActionButton mapFab;

    private ArrayList<JSONObject> datas = new ArrayList<>();

    private ArrayList<JSONObject> notDoorPhoto = new ArrayList<>();

    private ArrayList<JSONObject> mapdatas = new ArrayList<>();

    private int index = 0;

    private Handler mHandler;

    private boolean otherCondition = false;

    String[] communityNames;
    String[] communityIDs;

    String[] porpertiyNames = {"食杂店", "便利店", "超市", "商场", "名烟名酒名茶", "娱乐服务", "行业自办店", "其它"};
    String[] porpertiyCodes = {"104101", "104102", "104103", "104104", "104105", "104106", "104108", "104107"};

    String[] commonNames;
    String[] commonIds;

    String currentCommunityId = "";
    String currentporpertiyCode = "";
    String currentcommonId = "";

    private void assignViews(View convertView) {
        linearLayout16 = (LinearLayout) convertView.findViewById(R.id.linearLayout16);
        licenseTv = (EditText) convertView.findViewById(R.id.license_tv);
        shopnameTv = (EditText) convertView.findViewById(R.id.shopname_tv);
        nameTv = (EditText) convertView.findViewById(R.id.name_tv);
        communityTv = (TextView) convertView.findViewById(R.id.community_tv);
        porpertiyTv = (TextView) convertView.findViewById(R.id.porpertiy_tv);
        commonTv = (TextView) convertView.findViewById(R.id.common_tv);
        statusTv = (CheckBox) convertView.findViewById(R.id.status_tv);
        notHasPic = (CheckBox) convertView.findViewById(R.id.not_has_pic);
        notHasPosition = (CheckBox) convertView.findViewById(R.id.not_has_position);
        recyclerView = (RecyclerView) convertView.findViewById(R.id.recycler_view);
        searchFab = (FloatingActionButton) convertView.findViewById(R.id.search_fab);
        mapFab = (FloatingActionButton) convertView.findViewById(R.id.map_fab);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View convertView = inflater.inflate(R.layout.fragment_customer_info, null);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        assignViews(convertView);

        return convertView;
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>{

        @Override
        public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerHolder holder = new RecyclerHolder(LayoutInflater.from(CustomerInfoFragment.this.getActivity()).inflate(R.layout.fragment_customer_info_recyclerview_item, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerHolder holder, int position) {

            JSONObject object = datas.get(position);

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
                    holder.shopnameTv.setTextColor(getActivity().getResources().getColor(R.color.red));
                }else {
                    if (bd_latitude == 0) {
                        holder.shopnameTv.setTextColor(getActivity().getResources().getColor(R.color.red));
                    } else {
                        holder.shopnameTv.setTextColor(getActivity().getResources().getColor(R.color.color_black));
                    }
                }

                holder.lookUpTv.setOnClickListener(new ViewClickVibrate(){

                    @Override
                    public void onClick(View view) {
                        super.onClick(view);
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
