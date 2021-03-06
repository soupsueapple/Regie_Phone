package com.keertech.regie_phone;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;

import com.keertech.regie_phone.Utility.KeerAlertDialog;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by soup on 2017/5/3.
 */

public class BaseFragmentActivity extends FragmentActivity{

    protected void showToast(String string, Context context) {

        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(string);
            builder.setTitle("提示");
            builder.setPositiveButton("确认", null);
            builder.create().show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void showNetworkError(Context context) {
        this.showToast("服务链接错误", context);
    }

    protected KeerAlertDialog showKeerAlertDialog(int strID){
        return new KeerAlertDialog(this, getResources().getString(strID));
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getLocalClassName());
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getLocalClassName());
        MobclickAgent.onPause(this);
    }
}
