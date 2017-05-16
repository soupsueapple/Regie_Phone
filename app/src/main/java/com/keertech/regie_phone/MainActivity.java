package com.keertech.regie_phone;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.keertech.regie_phone.Activity.ProblemChecking.ProblemCheckingMainActivity;
import com.keertech.regie_phone.Activity.ReportComplanints.ReportComplanintsMainActivity;
import com.keertech.regie_phone.Activity.SystemSetting.SystemSettingActivity;
import com.keertech.regie_phone.Activity.XZFW.XZFWMainActivity;
import com.keertech.regie_phone.Constant.Constant;
import com.keertech.regie_phone.Models.Apps;
import com.keertech.regie_phone.Models.TerraceApp;
import com.keertech.regie_phone.Network.HttpClient;
import com.keertech.regie_phone.Utility.KeerAlertDialog;
import com.keertech.regie_phone.Utility.StringUtility;
import com.keertech.regie_phone.Utility.VibrateHelp;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private ArrayList<Apps> apps = new ArrayList<>();
    private ArrayList<TerraceApp> terraceApps = new ArrayList<>();
    private int apkId = 2;

    private boolean isNotice = false;
    private String datajson = "";

    private RecyclerView recyclerView;

    private int[] icons = {R.drawable.xzfw, R.drawable.wthc, R.drawable.jbts, R.drawable.jyhxx, R.drawable.wzhxx, R.drawable.afjc, R.drawable.sjjc, R.drawable.zxhd, R.drawable.wtqd, R.drawable.sgywz, R.drawable.lshkc, R.drawable.jygjj, R.drawable.kqgl, R.drawable.sgywz, R.drawable.tzgg, R.drawable.xtsz};
    private String[] names = {"行政服务", "问题核查", "举报投诉", "经营户信息", "无证户信息", "暗访检查", "随机检查", "专销互动", "问题清单","市管员位置", "零售户勘查", "卷烟购进价", "考勤管理", "经营户位置", "通知公告", "系统设置"};

    GridRecyclerAdapter adapter;

    private String username = "";
    private String password = "";

    private void assignViews() {

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
//        gridLayoutManager.offsetChildrenHorizontal(60);
//        gridLayoutManager.offsetChildrenVertical(60);
        //设置Item增加、移除动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new GridRecyclerAdapter();
        recyclerView.setAdapter(adapter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setToolbarTitle("武汉烟草移动办公平台");

        apps = (ArrayList<Apps>) getIntent().getSerializableExtra("apps");
        apkId = getIntent().getIntExtra("apkId", 2);
        username = getIntent().getStringExtra("username");
        password = getIntent().getStringExtra("password");

        assignViews();

        try {
            rangeForLoginUser();

            getApps();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void rangeForLoginUser() throws JSONException {

        RequestParams params = new RequestParams();
        params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\""+Constant.MWB_Base_URL+"locationRange!getRangeForLoginUser.action\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

        HttpClient.post(Constant.EXEC, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    if (StringUtility.isSuccess(response)) {
                        String messageSting = response.getString("message");

                        JSONObject message = new JSONObject(messageSting);

                        if (StringUtility.isSuccess(message)) {
                            double range = message.getDouble("range");
                            Constant.INTO_SHOP = range;
                        }else{
                            showToast(response.getString("message"), MainActivity.this);
                        }
                    } else {
                        showToast(response.getString("message"), MainActivity.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showNetworkError(MainActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                showNetworkError(MainActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                showNetworkError(MainActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void getApps() {
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);

        RequestParams params = new RequestParams();
        params.put("apkId", apkId);// 2 apkId

        HttpClient.post("getApps.action", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                pd.dismiss();
                super.onSuccess(statusCode, headers, response);

                System.out.println("response: " + response.toString());

                if (StringUtility.isSuccess(response)) {
                    try {
                        JSONArray data = response.getJSONArray("data");

                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);

                            TerraceApp app = new TerraceApp(object.getString("username"), object.getString("appname"), object.getInt("appid"), object.getString("code"), object.getString("password"));
                            terraceApps.add(app);
                        }

                        noticeMessage();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(MainActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(MainActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(MainActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });


    }

    private void noticeMessage() {

        HttpClient.post("message.action", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (StringUtility.isSuccess(response)) {
                    try {
                        JSONArray data = response.getJSONArray("data");

                        if (data.length() > 0) {
                            isNotice = true;
                            adapter.notifyDataSetChanged();
                            datajson = data.toString();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.main_action_myaccount){

            myAccount();

        }else if (id == R.id.main_action_exit){

            logout();

        }
        return super.onOptionsItemSelected(item);
    }

    private void myAccount(){


        View layout = LayoutInflater.from(this).inflate(R.layout.custom_account_dialog, (ViewGroup) findViewById(R.id.custom_dialog));
        final EditText upsd = (EditText)layout.findViewById(R.id.dialog_unpassword_ed);
        final EditText npsd = (EditText)layout.findViewById(R.id.dialog_newpassword_ed);
        final EditText qrmm = (EditText)layout.findViewById(R.id.dialog_newpassword_qr);

        new AlertDialog.Builder(this).setTitle("帐号:"+username).setView(layout).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final KeerAlertDialog pd = showKeerAlertDialog(R.string.sending);

                if (upsd.getText().toString().length() == 0) {
                    Toast.makeText(MainActivity.this,"请输入原密码",Toast.LENGTH_LONG).show();
                } else if (npsd.getText().toString().length() == 0) {
                    Toast.makeText(MainActivity.this,"请输入新密码",Toast.LENGTH_LONG).show();
                } else if (qrmm.getText().toString().length() == 0) {
                    Toast.makeText(MainActivity.this,"请输入确认密码",Toast.LENGTH_LONG).show();
                } else if(!qrmm.getText().toString().equals(npsd.getText().toString())){
                    Toast.makeText(MainActivity.this,"确认密码和新密码不一致",Toast.LENGTH_LONG).show();
                } else {
                    RequestParams params = new RequestParams();
                    params.put("username",username);
                    params.put("password",upsd.getText().toString());
                    params.put("newPassword",npsd.getText().toString());

                    HttpClient.post("updatePwd.action", params, new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);

                            pd.dismiss();

                            if(StringUtility.isSuccess(response)){
                                Toast.makeText(MainActivity.this,"密码修改成功",Toast.LENGTH_LONG).show();
                            }else {
                                try {
                                    Toast.makeText(MainActivity.this,response.getString("message"),Toast.LENGTH_LONG).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            pd.dismiss();
                            showNetworkError(MainActivity.this);
                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            pd.dismiss();
                            showNetworkError(MainActivity.this);
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                            pd.dismiss();
                            showNetworkError(MainActivity.this);
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            }
        }).setNegativeButton("取消", null).show();
    }

    private void logout(){
        HttpClient.post("logout.action", null, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    class GridRecyclerAdapter extends RecyclerView.Adapter<GridRecyclerAdapter.GridRecyclerHolder>{

        @Override
        public GridRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            GridRecyclerHolder holder = new GridRecyclerHolder(LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main_recycler_view_item, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(GridRecyclerHolder holder, int position) {
            holder.iv.setImageResource(icons[position]);

            if(isNotice){
                holder.notice_tv.setVisibility(View.VISIBLE);
            }else{
                holder.notice_tv.setVisibility(View.GONE);
            }

            final String name = names[position];
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent;
                    Bundle bundle = new Bundle();

                    VibrateHelp.vSimple(view.getContext());

                    switch (name){
                        case "行政服务":
                            intent = new Intent(MainActivity.this, XZFWMainActivity.class);
                            startActivity(intent);
                            break;
                        case "问题核查":
                            intent = new Intent(MainActivity.this, ProblemCheckingMainActivity.class);
                            startActivity(intent);
                            break;
                        case "举报投诉":
                            intent = new Intent(MainActivity.this, ReportComplanintsMainActivity.class);
                            startActivity(intent);
                            break;
                        case "经营户信息":

                            break;
                        case "暗访检查":

                            break;
                        case "无证户信息":

                            break;
                        case "专销互动":

                            break;
                        case "随机检查":

                            break;
                        case "问题清单":

                            break;
                        case "市管员位置":

                            break;
                        case "零售户勘查":

                            break;
                        case "卷烟购进价":

                            break;
                        case "考勤管理":

                            break;
                        case "通知公告":

                            break;
                        case "系统设置":
                            intent = new Intent(MainActivity.this, SystemSettingActivity.class);
                            bundle.putSerializable("apps", terraceApps);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            break;
                        case "经营户位置":

                            break;
                    }

                }
            });
        }

        @Override
        public int getItemCount() {
            return names.length;
        }

        class GridRecyclerHolder extends RecyclerView.ViewHolder{

            ImageView iv;
            TextView notice_tv;

            public GridRecyclerHolder(View itemView) {
                super(itemView);
                iv = (ImageView) itemView.findViewById(R.id.main_gridview_item_iv);
                notice_tv = (TextView) itemView.findViewById(R.id.notice_tv);
            }
        }
    }
}
