package com.keertech.regie_phone.Activity.XZFW.ServiceRecord;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.keertech.regie_phone.BaseActivity;
import com.keertech.regie_phone.Constant.Constant;
import com.keertech.regie_phone.Listener.ViewClickVibrate;
import com.keertech.regie_phone.Models.ImageDrawable;
import com.keertech.regie_phone.Network.HttpClient;
import com.keertech.regie_phone.Utility.KeerAlertDialog;
import com.keertech.regie_phone.Utility.StringUtility;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import static com.keertech.regie_phone.R.id.recycler_view;

/**
 * Created by soup on 2017/5/10.
 */

public class ServiceRecordInfoActivity extends BaseActivity{

    private ImageView evidenceIv;
    private TextView textView;
    private TextView licenseTv;
    private TextView shopnameTv;
    private TextView addressTv;
    private TextView customerTv;
    private TextView textView3;
    private TextView nameTv;
    private TextView serviceDateTv;
    private TextView serviceDurationTimeTv;
    private TextView startTime;
    private TextView endTime;
    private TextView textView4;
    private TextView textView5;
    private TextView notShowLicenseTv;
    private LinearLayout linearLayout7;
    private TextView textView6;
    private TextView notMatchingTv;
    private TextView notMatchingNoteTv;
    private LinearLayout linearLayout8;
    private TextView textView7;
    private TextView addressChangedTv;
    private TextView addressChangedNoteTv;
    private RecyclerView recyclerView;

    private Menu menu;

    private ArrayList<JSONObject> datas = new ArrayList<>();

    RecyclerAdapter recyclerAdapter = new RecyclerAdapter();

    private ArrayList<ImageDrawable> evidenceDrawables = new ArrayList<>();

    private String id;
    private String times;
    private String evidencePhotos = "";
    boolean DELETE = false;

    private void assignViews() {
        evidenceIv = (ImageView) findViewById(com.keertech.regie_phone.R.id.evidence_iv);
        textView = (TextView) findViewById(com.keertech.regie_phone.R.id.textView);
        licenseTv = (TextView) findViewById(com.keertech.regie_phone.R.id.license_tv);
        shopnameTv = (TextView) findViewById(com.keertech.regie_phone.R.id.shopname_tv);
        addressTv = (TextView) findViewById(com.keertech.regie_phone.R.id.address_tv);
        customerTv = (TextView) findViewById(com.keertech.regie_phone.R.id.customer_tv);
        textView3 = (TextView) findViewById(com.keertech.regie_phone.R.id.textView3);
        nameTv = (TextView) findViewById(com.keertech.regie_phone.R.id.name_tv);
        serviceDateTv = (TextView) findViewById(com.keertech.regie_phone.R.id.service_date_tv);
        serviceDurationTimeTv = (TextView) findViewById(com.keertech.regie_phone.R.id.service_duration_time_tv);
        startTime = (TextView) findViewById(com.keertech.regie_phone.R.id.start_time);
        endTime = (TextView) findViewById(com.keertech.regie_phone.R.id.end_time);
        textView4 = (TextView) findViewById(com.keertech.regie_phone.R.id.textView4);
        textView5 = (TextView) findViewById(com.keertech.regie_phone.R.id.textView5);
        notShowLicenseTv = (TextView) findViewById(com.keertech.regie_phone.R.id.not_show_license_tv);
        linearLayout7 = (LinearLayout) findViewById(com.keertech.regie_phone.R.id.linearLayout7);
        textView6 = (TextView) findViewById(com.keertech.regie_phone.R.id.textView6);
        notMatchingTv = (TextView) findViewById(com.keertech.regie_phone.R.id.not_matching_tv);
        notMatchingNoteTv = (TextView) findViewById(com.keertech.regie_phone.R.id.not_matching_note_tv);
        linearLayout8 = (LinearLayout) findViewById(com.keertech.regie_phone.R.id.linearLayout8);
        textView7 = (TextView) findViewById(com.keertech.regie_phone.R.id.textView7);
        addressChangedTv = (TextView) findViewById(com.keertech.regie_phone.R.id.address_changed_tv);
        addressChangedNoteTv = (TextView) findViewById(com.keertech.regie_phone.R.id.address_changed_note_tv);
        recyclerView = (RecyclerView) findViewById(recycler_view);

        evidenceIv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                Intent intent = new Intent(ServiceRecordInfoActivity.this, LookImageActivity.class);
                intent.putExtra("imageDrawables",evidenceDrawables);
                startActivity(intent);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);

        id = getIntent().getStringExtra("id");
        times = getIntent().getStringExtra("times");

        loadData();
    }

    private void loadData() {
        final KeerAlertDialog pd = showKeerAlertDialog(com.keertech.regie_phone.R.string.loading);
        pd.show();

        RequestParams params = new RequestParams();
        params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MWB_Base_URL + "marketInspect!findBean.action?privilegeFlag=VIEW&bean.id=" + id + "\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

        HttpClient.post(Constant.EXEC, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                pd.dismiss();

                try {
                    String messageSting = response.getString("message");

                    JSONObject message = new JSONObject(messageSting);

                    if (StringUtility.isSuccess(message)) {
                        JSONObject data = message.getJSONObject("data");
                        JSONObject customer = data.getJSONObject("customer");
                        JSONObject creator = data.getJSONObject("creator");

                        licenseTv.setText(customer.getString("liceNo"));
                        shopnameTv.setText(customer.getString("shopName"));
                        addressTv.setText(customer.getString("shopAddress"));
                        customerTv.setText(customer.getString("chargerName"));
                        nameTv.setText(creator.getString("name"));
                        serviceDateTv.setText(data.getString("createdDate").substring(0, 10));
                        startTime.setText(data.getString("inDate"));
                        endTime.setText(data.getString("outDate"));
                        notShowLicenseTv.setText(data.getInt("isLightCard") == 1 ? "是" : "否");
                        notMatchingTv.setText(data.getInt("isRzConform") == 1 ? "是" : "否");
                        addressChangedTv.setText(data.getInt("isAddressChange") == 1 ? "是" : "否");

                        if (!StringUtility.isEmpty(data.getString("rzConform_Remark")))
                            notMatchingNoteTv.setText(data.getString("rzConform_Remark"));
                        if (!StringUtility.isEmpty(data.getString("addressChange_Remark")))
                            addressChangedNoteTv.setText(data.getString("addressChange_Remark"));

                        serviceDurationTimeTv.setText(StringUtility.isEmpty(times) ? "0" : times + "分");


                        evidencePhotos = data.getString("evidencePhotos");

                        if (!StringUtility.isEmpty(evidencePhotos)) {
                            String[] names = evidencePhotos.split(";");

                            for (String name : names) {
                                loadEvidenceP(name);
                            }
                        }

                        if (!data.isNull("privilegeMap")) {
                            JSONObject privilegeMap = data.getJSONObject("privilegeMap");
                            DELETE = privilegeMap.getBoolean("DELETE");
                            if (!DELETE) menu.removeItem(com.keertech.regie_phone.R.id.action_del_record);
                        } else {
                            menu.removeItem(com.keertech.regie_phone.R.id.action_del_record);
                        }

                        loadListData();
                    } else {
                        showToast(message.getString("message"), ServiceRecordInfoActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(ServiceRecordInfoActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(ServiceRecordInfoActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(ServiceRecordInfoActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void loadEvidenceP(String fileName){
        String[] names = fileName.split("/");
        final String name = names[names.length-1];

        File file = (getFile(name));

        if(file.exists()){
            try {;

                Bitmap bitmap  = BitmapFactory.decodeFile(file.getAbsolutePath());
                Drawable drawable =new BitmapDrawable(bitmap);
                evidenceIv.setImageDrawable(drawable);

                evidenceDrawables.add(new ImageDrawable(name));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else {
            RequestParams params = new RequestParams();
            params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MWB_Base_URL + "marketInspect!loadEvidenceP.action?privilegeFlag=VIEW&fileName=" + fileName + "\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");
            HttpClient.post(Constant.EXEC, params, new FileAsyncHttpResponseHandler(file) {
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, File file) {
                    try {

                        Drawable drawable =new BitmapDrawable(decodeFile(file, 244, 326));
                        evidenceIv.setImageDrawable(drawable);

                        evidenceDrawables.add(new ImageDrawable(name));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }

    private void loadListData() {

        final KeerAlertDialog pd = showKeerAlertDialog(com.keertech.regie_phone.R.string.loading);

        RequestParams params = new RequestParams();
        params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MWB_Base_URL + "marketInspect!findDetails.action?privilegeFlag=VIEW&bean.id=" + id + "\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

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
                            for (int i = 0; i < data.length(); i++) {
                                datas.add(data.getJSONObject(i));

                            }

                            recyclerAdapter.notifyDataSetChanged();

                        } else {
                            showToast(message.getString("message"), ServiceRecordInfoActivity.this);
                        }
                    }else{
                        showToast(response.getString("message"), ServiceRecordInfoActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(ServiceRecordInfoActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(ServiceRecordInfoActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(ServiceRecordInfoActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void del(){
        final KeerAlertDialog pd = showKeerAlertDialog(com.keertech.regie_phone.R.string.sending);
        pd.show();

        RequestParams params = new RequestParams();
        params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\""+Constant.MWB_Base_URL+"marketInspect!delete.action?privilegeFlag=DELETE&bean.id="+id+"\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");//"&bean.version="+version+

        HttpClient.post(Constant.EXEC, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                pd.dismiss();

                try {
                    String messageSting = response.getString("message");

                    JSONObject message = new JSONObject(messageSting);

                    if (StringUtility.isSuccess(message)) {
                        Constant.isRefreshXCJ2 = true;
                        if(Constant.isYc){
                            Constant.isRefreshXCJ3 = true;
                        }else {
                            Constant.isRefreshXCJ2 = true;
                        }
                        finish();
                    } else {
                        showToast(message.getString("message"), ServiceRecordInfoActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(ServiceRecordInfoActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(ServiceRecordInfoActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(ServiceRecordInfoActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(com.keertech.regie_phone.R.menu.del_record_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == com.keertech.regie_phone.R.id.action_del_record){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("是否确定删除此服务记录");
            builder.setTitle("提示");
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    del();
                }
            });
            builder.create().show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.keertech.regie_phone.R.layout.activity_service_record_info);
        setToolbarTitle("服务记录");
        showBack();

        assignViews();
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>{

        @Override
        public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerHolder holder = new RecyclerHolder(LayoutInflater.from(ServiceRecordInfoActivity.this).inflate(com.keertech.regie_phone.R.layout.activity_xzfw_rcfw_service_info_abnormal_info_recyclerview, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerHolder holder, int position) {

            JSONObject object = datas.get(position);

            try {

                int type = object.getInt("type");

                switch (type){
                    case 1:
                        holder.abnormalTypeTv.setText("公开摆卖");
                        break;
                    case 2:
                        holder.abnormalTypeTv.setText("暗中售卖");
                        break;
                    case 3:
                        holder.abnormalTypeTv.setText("销售假烟");
                        break;
                }

                holder.varietyTv.setText(object.getString("cigName"));
                holder.numberTv.setText(object.getString("amount"));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        class RecyclerHolder extends RecyclerView.ViewHolder{

            private TextView abnormalTypeTv;
            private TextView varietyTv;
            private TextView numberTv;

            private void holderAssignViews(View itemView) {
                abnormalTypeTv = (TextView) itemView.findViewById(com.keertech.regie_phone.R.id.abnormal_type_tv);
                varietyTv = (TextView) itemView.findViewById(com.keertech.regie_phone.R.id.variety_tv);
                numberTv = (TextView) itemView.findViewById(com.keertech.regie_phone.R.id.number_tv);
            }

            public RecyclerHolder(View itemView) {
                super(itemView);
                holderAssignViews(itemView);
            }
        }
    }
}
