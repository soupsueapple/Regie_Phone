package com.keertech.regie_phone;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import com.keertech.regie_phone.Utility.KeerAlertDialog;

/**
 * Created by soup on 2017/5/3.
 */

public class BaseFragment extends Fragment{



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
        return new KeerAlertDialog(getActivity(), getResources().getString(strID));
    }

}
