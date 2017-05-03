package com.keertech.regie_phone.Activity.XZFW;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.keertech.regie_phone.Activity.XZFW.Fragment.RCFWMainFragment;
import com.keertech.regie_phone.BaseFragmentActivity;
import com.keertech.regie_phone.R;

/**
 * Created by soup on 2017/5/3.
 */

public class XZFWMainActivity extends BaseFragmentActivity{

    private TextView back_tv;
    private TextView title_tv;
    private LinearLayout rcfuLl;
    private TextView rcfuTv;
    private LinearLayout fwjhLl;
    private TextView fwjhTv;
    private LinearLayout fwjlLl;
    private TextView fwjlTv;
    private LinearLayout fwycLl;
    private TextView fwycTv;

    private void assignViews() {
        back_tv = (TextView) findViewById(R.id.back_tv);
        title_tv = (TextView) findViewById(R.id.title_tv);
        rcfuLl = (LinearLayout) findViewById(R.id.rcfu_ll);
        rcfuTv = (TextView) findViewById(R.id.rcfu_tv);
        fwjhLl = (LinearLayout) findViewById(R.id.fwjh_ll);
        fwjhTv = (TextView) findViewById(R.id.fwjh_tv);
        fwjlLl = (LinearLayout) findViewById(R.id.fwjl_ll);
        fwjlTv = (TextView) findViewById(R.id.fwjl_tv);
        fwycLl = (LinearLayout) findViewById(R.id.fwyc_ll);
        fwycTv = (TextView) findViewById(R.id.fwyc_tv);

        title_tv.setText("行政服务");
        back_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        RCFWMainFragment rcfwMainFragment = new RCFWMainFragment();
        ft.add(R.id.realtabcontent, rcfwMainFragment, "rcfw");
        ft.commit();

        rcfuTv.setBackgroundResource(R.drawable.rcfu1);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xzfw_main);

        assignViews();
    }

    private void showFragment(String tag, FragmentManager fm, FragmentTransaction ft) {
        if (fm.getFragments().size() > 0) {
            for (Fragment fragment : fm.getFragments()) {
                if (fragment.getTag().equals(tag)) {
                    ft.show(fragment);
                } else {
                    ft.hide(fragment);
                }

            }
        }
    }
}
