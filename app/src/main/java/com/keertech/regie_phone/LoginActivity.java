package com.keertech.regie_phone;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.keertech.regie_phone.Constant.Constant;
import com.keertech.regie_phone.Models.Apps;
import com.keertech.regie_phone.Network.HttpClient;
import com.keertech.regie_phone.Utility.KeerAlertDialog;
import com.keertech.regie_phone.Utility.StringUtility;
import com.keertech.regie_phone.Utility.VibrateHelp;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

import static com.keertech.regie_phone.R.id.password_et;
import static com.keertech.regie_phone.R.id.username_et;

/**
 * Created by soup on 2017/4/27.
 */

public class LoginActivity extends BaseActivity{

    private static final int INTERNET = 1;
    private static final int CHANGE_NETWORK_STATE = 2;
    private static final int ACCESS_NETWORK_STATE = 3;
    private static final int CHANGE_WIFI_STATE = 4;
    private static final int ACCESS_WIFI_STATE = 5;
    private static final int RECEIVE_BOOT_COMPLETED = 6;
    private static final int MOUNT_UNMOUNT_FILESYSTEMS = 7;
    private static final int WRITE_EXTERNAL_STORAGE = 8;
    private static final int RECORD_AUDIO = 9;
    private static final int CALL_PHONE = 10;
    private static final int ACCESS_COARSE_LOCATION = 11;
    private static final int ACCESS_LOCATION_EXTRA_COMMANDS = 12;
    private static final int ACCESS_FINE_LOCATION = 13;
    private static final int READ_CONTACTS = 14;
    private static final int WRITE_CONTACTS = 15;
    private static final int READ_PHONE_STATE = 16;
    private static final int SEND_SMS = 17;
    private static final int VIBRATE = 18;
    private static final int CAMERA = 19;
    private static final int READ_LOGS = 20;
    private static final int KILL_BACKGROUND_PROCESSES = 21;
    private static final int SYSTEM_ALERT_WINDOW = 22;
    private static final int WAKE_LOCK = 23;
    private static final int WRITE_SETTINGS = 24;
    private static final int DISABLE_KEYGUARD = 25;
    private static final int READ_EXTERNAL_STORAGE = 26;

    private EditText usernameEt;
    private EditText passwordEt;
    private CheckBox autoLoginCb;
    private TextView loginTv;

    private int apkId = 2;
    private ArrayList<Apps> apps = new ArrayList<>();

    private void assignViews() {
        getToolbar().setVisibility(View.GONE);

        usernameEt = (EditText) findViewById(username_et);
        passwordEt = (EditText) findViewById(password_et);
        autoLoginCb = (CheckBox) findViewById(R.id.auto_login_cb);
        loginTv = (TextView) findViewById(R.id.login_tv);

        loginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VibrateHelp.vSimple(view.getContext());

                doLogin();
            }
        });

        autoLoginCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    if (!StringUtility.isEmpty(usernameEt.getText().toString())
                            && !StringUtility.isEmpty(passwordEt.getText().toString()))
                        StringUtility.putSharedPreferences(LoginActivity.this, Constant.SharedPreferencesLogin,
                                Constant.SharedPreferencesLoginKey, true);
                } else {
                    StringUtility.putSharedPreferences(LoginActivity.this, Constant.SharedPreferencesLogin,
                            Constant.SharedPreferencesLoginKey, false);
                }
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        assignViews();

        PackageInfo packageInfo;

        try {
            packageInfo = this.getPackageManager().getPackageInfo(
                    "com.qihoo360.mobilesafe", 0);
            if(packageInfo != null){
                showToast("请卸载360,以免频繁离线影响考核成绩", this);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if(isMarshmallow()){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                    != PackageManager.PERMISSION_GRANTED) {

                MPermissions.requestPermissions(this, INTERNET, Manifest.permission.INTERNET);

            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CHANGE_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED){
                MPermissions.requestPermissions(this, CHANGE_NETWORK_STATE, Manifest.permission.CHANGE_NETWORK_STATE);
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED){
                MPermissions.requestPermissions(this, CHANGE_WIFI_STATE, Manifest.permission.CHANGE_WIFI_STATE);
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED){
                MPermissions.requestPermissions(this, ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_NETWORK_STATE);
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED){
                MPermissions.requestPermissions(this, ACCESS_WIFI_STATE, Manifest.permission.ACCESS_WIFI_STATE);
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_BOOT_COMPLETED) != PackageManager.PERMISSION_GRANTED){
                MPermissions.requestPermissions(this, RECEIVE_BOOT_COMPLETED, Manifest.permission.RECEIVE_BOOT_COMPLETED);
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS) != PackageManager.PERMISSION_GRANTED){
                MPermissions.requestPermissions(this, MOUNT_UNMOUNT_FILESYSTEMS, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS);
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                MPermissions.requestPermissions(this, WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
                MPermissions.requestPermissions(this, RECORD_AUDIO, Manifest.permission.RECORD_AUDIO);
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                MPermissions.requestPermissions(this, CALL_PHONE, Manifest.permission.CALL_PHONE);
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                MPermissions.requestPermissions(this, ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS) != PackageManager.PERMISSION_GRANTED){
                MPermissions.requestPermissions(this, ACCESS_LOCATION_EXTRA_COMMANDS, Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS);
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                MPermissions.requestPermissions(this, ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
                MPermissions.requestPermissions(this, READ_CONTACTS, Manifest.permission.READ_CONTACTS);
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED){
                MPermissions.requestPermissions(this, WRITE_CONTACTS, Manifest.permission.WRITE_CONTACTS);
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
                MPermissions.requestPermissions(this, READ_PHONE_STATE, Manifest.permission.READ_PHONE_STATE);
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
                MPermissions.requestPermissions(this, SEND_SMS, Manifest.permission.SEND_SMS);
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED){
                MPermissions.requestPermissions(this, VIBRATE, Manifest.permission.VIBRATE);
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                MPermissions.requestPermissions(this, CAMERA, Manifest.permission.CAMERA);
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_LOGS) != PackageManager.PERMISSION_GRANTED){
                MPermissions.requestPermissions(this, READ_LOGS, Manifest.permission.READ_LOGS);
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.KILL_BACKGROUND_PROCESSES) != PackageManager.PERMISSION_GRANTED){
                MPermissions.requestPermissions(this, KILL_BACKGROUND_PROCESSES, Manifest.permission.KILL_BACKGROUND_PROCESSES);
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED){
                MPermissions.requestPermissions(this, SYSTEM_ALERT_WINDOW, Manifest.permission.SYSTEM_ALERT_WINDOW);
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED){
                MPermissions.requestPermissions(this, WAKE_LOCK, Manifest.permission.WAKE_LOCK);
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_SETTINGS) != PackageManager.PERMISSION_GRANTED){
                MPermissions.requestPermissions(this, WRITE_SETTINGS, Manifest.permission.WRITE_SETTINGS);
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.DISABLE_KEYGUARD) != PackageManager.PERMISSION_GRANTED){
                MPermissions.requestPermissions(this, DISABLE_KEYGUARD, Manifest.permission.DISABLE_KEYGUARD);
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                MPermissions.requestPermissions(this, READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
            }

        }

        TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

        String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        Constant.deviceId = deviceUuid.toString();

        boolean isAutoLogin = StringUtility.getSharedPreferencesForBoolean(LoginActivity.this,
                Constant.SharedPreferencesLogin, Constant.SharedPreferencesLoginKey);
        String username = StringUtility.getSharedPreferencesForString(this, Constant.SharedPreferencesLogin,
                Constant.SharedPreferencesLoginUserName);
        String password = StringUtility.getSharedPreferencesForString(this, Constant.SharedPreferencesLogin,
                Constant.SharedPreferencesLoginUserPassword);

        StringUtility.putSharedPreferences(this, "deviceId", "deviceId", Constant.deviceId);

        usernameEt.setText(username);
        passwordEt.setText(password);

        if (isAutoLogin) {
            autoLoginCb.setChecked(true);
            this.doLogin();
        } else {
            autoLoginCb.setChecked(false);
        }
    }

    private void doLogin(){
        if(usernameEt.getText().toString().length() == 0){
            LoginActivity.this.showToast("请输入用户名", this);

            return;
        }

        if(passwordEt.getText().toString().length() == 0){
            LoginActivity.this.showToast("请输入密码", this);

            return;
        }

        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loginning);
        pd.show();

        StringUtility.putSharedPreferences(this, Constant.SharedPreferencesLogin,
                Constant.SharedPreferencesLoginUserName, this.usernameEt.getText().toString());
        StringUtility.putSharedPreferences(this, Constant.SharedPreferencesLogin,
                Constant.SharedPreferencesLoginUserPassword, this.passwordEt.getText().toString());

        RequestParams params = new RequestParams();
        params.put("deviceId", Constant.deviceId);
        params.put("username", usernameEt.getText().toString());
        params.put("password", passwordEt.getText().toString());

        HttpClient.post(Constant.Login_URL, params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                pd.dismiss();


                try {
                    if (StringUtility.isSuccess(response)) {
                        String userId = response.getString("userId");
                        Constant.userId = userId;
                        StringUtility.putSharedPreferences(getApplicationContext(), "id", "id", userId);

                        JSONArray datas = response.getJSONArray("data");

                        for (int i = 0; i < datas.length(); i++) {
                            JSONObject object = datas.getJSONObject(i);

                            Apps app = new Apps(new Integer(object.getString("id")), object.getString("apkName"), object.getString("name"), object.getString("apkUrl"), object.getString("apkEntry"), object.getString("apkVer"), object.getString("createdDate"), new Integer(object.getString("apkSize")), object.getString("modifiedDate"), new Integer(object.getString("version")));

                            if (app.getId() != apkId) apps.add(app);
                        }

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("apps", apps);
                        intent.putExtra("apkId", apkId);
                        intent.putExtra("username", usernameEt.getText().toString());
                        intent.putExtra("password", passwordEt.getText().toString());
                        startActivity(intent);

                        finish();

                    } else {
                        LoginActivity.this.showToast(response.getString("message"), LoginActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(LoginActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(LoginActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(LoginActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

    }

    private boolean isMarshmallow() {

        return Build.VERSION.SDK_INT >= 23;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @PermissionGrant(LoginActivity.INTERNET)
    public void requestInternetSuccess(){

    }

    @PermissionDenied(LoginActivity.INTERNET)
    public void requestInternetFailed(){

    }
}
