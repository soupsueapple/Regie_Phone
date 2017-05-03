package com.keertech.regie_phone.Activity.SystemSetting;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.keertech.regie_phone.Adapter.SetAdapter;
import com.keertech.regie_phone.BaseActivity;
import com.keertech.regie_phone.Constant.Constant;
import com.keertech.regie_phone.Models.TerraceApp;
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

import java.util.List;

/**
 * Created by soup on 2017/5/3.
 */

public class SystemSettingActivity extends BaseActivity{

    private ListView listView;

    private MyAdaper adaper;

    private List<TerraceApp> terraceApps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_setting);

        setToolbarTitle("系统设置");
        showBack();

        terraceApps = (List<TerraceApp>) getIntent().getSerializableExtra("apps");

        initView();

        adaper = new MyAdaper(this, terraceApps);
        listView.setAdapter(adaper);
    }

    private void initView(){
        listView = (ListView) findViewById(R.id.system_setting_listview);
    }


    private void getApps(){

        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loginning);

        RequestParams params = new RequestParams();
        params.put("apkId","2");//app.getId();

        HttpClient.post("getApps.action", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                pd.dismiss();
                super.onSuccess(statusCode, headers, response);

                System.out.println("response: " + response.toString());

                terraceApps.clear();

                if(StringUtility.isSuccess(response)){
                    try {
                        JSONArray data = response.getJSONArray("data");

                        for(int i=0;i<data.length();i++){
                            JSONObject object = data.getJSONObject(i);

                            TerraceApp app = new TerraceApp(object.getString("username"),object.getString("appname"),object.getInt("appid"),object.getString("code"),object.getString("password"));
                            terraceApps.add(app);
                        }

                        SystemSettingActivity.this.adaper.setObjects(terraceApps);
                        SystemSettingActivity.this.adaper.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        showToast(response.getString("message"), SystemSettingActivity.this);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(SystemSettingActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(SystemSettingActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(SystemSettingActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private class MyAdaper extends SetAdapter {
        private Context context;
        private List<TerraceApp> terraceApps;
        LayoutInflater inflater;

        public MyAdaper(Context context, List<TerraceApp> terraceApps) {
            super(context, terraceApps);
            this.context = context;
            this.terraceApps = terraceApps;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.system_setting_recycler_view_item, null);
                holder.appname_tv = (TextView) convertView.findViewById(R.id.appname_tv);
                holder.appusername_tv = (TextView) convertView.findViewById(R.id.appusername_tv);
                holder.apppassword_tv = (TextView) convertView.findViewById(R.id.apppassword_tv);
                holder.appedit_tv = (TextView) convertView.findViewById(R.id.appedit_tv);
                holder.jiebang_tv = (TextView) convertView.findViewById(R.id.jiebang_tv);

                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }


            final TerraceApp app = this.terraceApps.get(position);
            holder.appname_tv.setText(app.getAppname());
            holder.appusername_tv.setText(StringUtility.isEmpty(app.getUsername())?"":app.getUsername());
            holder.apppassword_tv.setText(StringUtility.isEmpty(app.getPassword())?"":app.getPassword());

            holder.appedit_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    View layout = inflater.inflate(R.layout.custom_edit_dialog, (ViewGroup)findViewById(R.id.custom_dialog));
                    final EditText username = (EditText)layout.findViewById(R.id.dialog_username_ed);
                    final EditText password = (EditText)layout.findViewById(R.id.dialog_password_ed);

                    new AlertDialog.Builder(context).setTitle("设置帐号").setView(layout).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (username.getText().toString().length() == 0) {
                                SystemSettingActivity.this.showToast("请输入用户名",SystemSettingActivity.this);
                            } else if (password.getText().toString().length() == 0) {
                                SystemSettingActivity.this.showToast("请输入密码",SystemSettingActivity.this);
                            } else {
                                final KeerAlertDialog pd = SystemSettingActivity.this.showKeerAlertDialog(R.string.sending);

                                RequestParams params = new RequestParams();
                                params.put("appId", app.getAppid()+"");
                                params.put("userId", Constant.userId);
                                params.put("username",username.getText().toString());
                                params.put("password", password.getText().toString());

                                HttpClient.post("registerAccount.action", params, new JsonHttpResponseHandler() {

                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        super.onSuccess(statusCode, headers, response);
                                        pd.dismiss();

                                        if (StringUtility.isSuccess(response)) {
                                            SystemSettingActivity.this.getApps();
                                        } else {
                                            try {
                                                SystemSettingActivity.this.showToast(response.getString("message"),SystemSettingActivity.this);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }

                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                        pd.dismiss();
                                        showNetworkError(SystemSettingActivity.this);
                                        super.onFailure(statusCode, headers, throwable, errorResponse);
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                                        pd.dismiss();
                                        showNetworkError(SystemSettingActivity.this);
                                        super.onFailure(statusCode, headers, throwable, errorResponse);
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                        pd.dismiss();
                                        showNetworkError(SystemSettingActivity.this);
                                        super.onFailure(statusCode, headers, responseString, throwable);
                                    }
                                });
                            }
                        }
                    }).setNegativeButton("取消", null).show();
                }
            });

            holder.jiebang_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("是否要解除" + app.getAppname() + "的账号绑定");
                    builder.setTitle("提示");
                    builder.setPositiveButton("解除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            app.setUsername("");
                            app.setPassword("");

                            adaper.notifyDataSetChanged();
                        }
                    });
                    builder.setNegativeButton("取消", null);
                    builder.create().show();
                }
            });


            return convertView;
        }

        final class ViewHolder {
            TextView appname_tv;
            TextView appusername_tv;
            TextView apppassword_tv;
            TextView appedit_tv;
            TextView jiebang_tv;
        }
    }
}
