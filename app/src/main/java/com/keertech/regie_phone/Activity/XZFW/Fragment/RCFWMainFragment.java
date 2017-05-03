package com.keertech.regie_phone.Activity.XZFW.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.keertech.regie_phone.BaseFragment;
import com.keertech.regie_phone.R;

/**
 * Created by soup on 2017/5/3.
 */

public class RCFWMainFragment extends BaseFragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View convertView = inflater.inflate(R.layout.fragment_xzfw_rcfw, null);

        return convertView;
    }
}
