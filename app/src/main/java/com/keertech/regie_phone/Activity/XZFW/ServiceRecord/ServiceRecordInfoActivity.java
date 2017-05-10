package com.keertech.regie_phone.Activity.XZFW.ServiceRecord;

import android.os.Bundle;
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

    private String id;
    private String times;
    private String evidencePhotos = "";
    boolean DELETE = false;

    private void assignViews() {
        evidenceIv = (ImageView) findViewById(R.id.evidence_iv);
        textView = (TextView) findViewById(R.id.textView);
        licenseTv = (TextView) findViewById(R.id.license_tv);
        shopnameTv = (TextView) findViewById(R.id.shopname_tv);
        addressTv = (TextView) findViewById(R.id.address_tv);
        customerTv = (TextView) findViewById(R.id.customer_tv);
        textView3 = (TextView) findViewById(R.id.textView3);
        nameTv = (TextView) findViewById(R.id.name_tv);
        serviceDateTv = (TextView) findViewById(R.id.service_date_tv);
        serviceDurationTimeTv = (TextView) findViewById(R.id.service_duration_time_tv);
        startTime = (TextView) findViewById(R.id.start_time);
        endTime = (TextView) findViewById(R.id.end_time);
        textView4 = (TextView) findViewById(R.id.textView4);
        textView5 = (TextView) findViewById(R.id.textView5);
        notShowLicenseTv = (TextView) findViewById(R.id.not_show_license_tv);
        linearLayout7 = (LinearLayout) findViewById(R.id.linearLayout7);
        textView6 = (TextView) findViewById(R.id.textView6);
        notMatchingTv = (TextView) findViewById(R.id.not_matching_tv);
        notMatchingNoteTv = (TextView) findViewById(R.id.not_matching_note_tv);
        linearLayout8 = (LinearLayout) findViewById(R.id.linearLayout8);
        textView7 = (TextView) findViewById(R.id.textView7);
        addressChangedTv = (TextView) findViewById(R.id.address_changed_tv);
        addressChangedNoteTv = (TextView) findViewById(R.id.address_changed_note_tv);
        recyclerView = (RecyclerView) findViewById(recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);

        id = getIntent().getStringExtra("id");
        times = getIntent().getStringExtra("times");

        loadData();
    }

    private void loadData() {
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams params = new RequestParams();
        params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MWB_Base_URL + "marketInspect!findBean.action?privilegeFlag=VIEW&bean.id=" + id + "\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

        HttpClient.post(Constant.EXEC, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

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
//                                loadEvidenceP(name);
                            }
                        }

                        if (!data.isNull("privilegeMap")) {
                            JSONObject privilegeMap = data.getJSONObject("privilegeMap");
                            DELETE = privilegeMap.getBoolean("DELETE");
                            if (!DELETE) menu.removeItem(R.id.action_del_record);
                        } else {
                            menu.removeItem(R.id.action_del_record);
                        }

//                        loadListData(pd);
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
        getMenuInflater().inflate(R.menu.del_record_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_del_record){

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_record_info);
        setToolbarTitle("服务记录");
        showBack();

        assignViews();
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>{

        @Override
        public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerHolder holder = new RecyclerHolder(LayoutInflater.from(ServiceRecordInfoActivity.this).inflate(R.layout.activity_xzfw_rcfw_service_info_abnormal_info_recyclerview, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        class RecyclerHolder extends RecyclerView.ViewHolder{

            public RecyclerHolder(View itemView) {
                super(itemView);
            }
        }
    }
}
