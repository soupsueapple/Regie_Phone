package com.keertech.regie_phone.Activity.CommonPosition;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
 * Created by soup on 2017/5/26.
 */

public class CommonPositionListActivity extends BaseActivity{

    private ArrayList<JSONObject> datas = new ArrayList<>();
    RecyclerAdapter recyclerAdapter = new RecyclerAdapter();

    int zxrs = 0;
    int lxrs = 0;

    private TextView onlineTv;
    private TextView offlineTv;
    private RecyclerView recyclerView;

    JSONArray jsonArray = new JSONArray();

    private void assignViews() {
        onlineTv = (TextView) findViewById(R.id.online_tv);
        offlineTv = (TextView) findViewById(R.id.offline_tv);
        recyclerView = (RecyclerView) findViewById(recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(CommonPositionListActivity.this));
        recyclerView.setAdapter(recyclerAdapter);

        getLocations();
    }

    private void getLocations(){
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams params = new RequestParams();
        params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\""+ Constant.MIIS_Base_URL+"marketDisplay!searchBeans.action?privilegeFlag=VIEW\",\"type\":\"WebExecutor\"},\"app\":\"1003\"}");

        HttpClient.post(Constant.EXEC, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {

                    if (StringUtility.isSuccess(response)) {
                        String messageSting = response.getString("message");

                        JSONObject message = new JSONObject(messageSting);

                        if (StringUtility.isSuccess(message)) {

                            jsonArray = message.getJSONArray("data");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                object.put("isChecked", false);

                                String img = object.getString("img");

                                if (img.equals("red")) {
                                    lxrs += 1;
                                    object.put("onLine", false);
                                } else {
                                    zxrs += 1;
                                    object.put("onLine", true);
                                }

                                object.put("show", true);

                                datas.add(object);
                            }

                            recyclerAdapter.notifyDataSetChanged();

                            onlineTv.setText(zxrs + "");
                            offlineTv.setText(lxrs+ "");
                        } else {
                            showToast(message.getString("message"), CommonPositionListActivity.this);
                        }
                    } else {
                        showToast(response.getString("message"), CommonPositionListActivity.this);
                    }
                }catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(CommonPositionListActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(CommonPositionListActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(CommonPositionListActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_positison_list);
        setToolbarTitle("市管员位置");
        showBack();

        assignViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_position_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_common){
            Intent intent = new Intent(this, CommonMapActivity.class);
            intent.putExtra("json", jsonArray.toString());
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>{

        @Override
        public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerHolder holder = new RecyclerHolder(LayoutInflater.from(CommonPositionListActivity.this).inflate(R.layout.activity_common_positison_list_recyclerview_item, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerHolder holder, int position) {
            try {
                final int index = position;

                final JSONObject object = datas.get(position);
                String bname = object.getString("name");
                boolean onLine = object.getBoolean("onLine");

                holder.nameTv.setText(bname);

                if(onLine){
                    holder.statusTv.setBackgroundResource(R.drawable.zx);
                }else{
                    holder.statusTv.setBackgroundResource(R.drawable.lx);
                }

                holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        // TODO Auto-generated method stub


                    }
                });


            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        class RecyclerHolder extends RecyclerView.ViewHolder{

            private CheckBox cb;
            private TextView nameTv;
            private TextView statusTv;

            private void assignViews(View itemView) {
                cb = (CheckBox) itemView.findViewById(R.id.cb);
                nameTv = (TextView) itemView.findViewById(R.id.name_tv);
                statusTv = (TextView) itemView.findViewById(R.id.status_tv);
            }


            public RecyclerHolder(View itemView) {
                super(itemView);
                assignViews(itemView);
            }
        }
    }
}
