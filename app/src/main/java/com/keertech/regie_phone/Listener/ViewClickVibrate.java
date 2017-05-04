package com.keertech.regie_phone.Listener;

import android.view.View;

import com.keertech.regie_phone.Utility.VibrateHelp;

/**
 * Created by soup on 2017/5/4.
 */

public class ViewClickVibrate implements View.OnClickListener{
    @Override
    public void onClick(View view) {
        VibrateHelp.vSimple(view.getContext());
    }
}
