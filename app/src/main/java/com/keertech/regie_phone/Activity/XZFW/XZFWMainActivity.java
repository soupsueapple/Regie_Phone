package com.keertech.regie_phone.Activity.XZFW;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.keertech.regie_phone.Activity.XZFW.Fragment.FWJHMainFragment;
import com.keertech.regie_phone.Activity.XZFW.Fragment.FWJLMainFragment;
import com.keertech.regie_phone.Activity.XZFW.Fragment.FWYCMainFragment;
import com.keertech.regie_phone.Activity.XZFW.Fragment.RCFWMainFragment;
import com.keertech.regie_phone.BaseFragmentActivity;
import com.keertech.regie_phone.Constant.Constant;
import com.keertech.regie_phone.Listener.ViewClickVibrate;
import com.keertech.regie_phone.R;
import com.keertech.regie_phone.Utility.VibrateHelp;


/**
 * Created by soup on 2017/5/3.
 */

public class XZFWMainActivity extends BaseFragmentActivity implements View.OnClickListener{

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
    private TextView map_fab;
    private TextView refresh_fab;

    RCFWMainFragment rcfwMainFragment;

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
        map_fab = (TextView) findViewById(R.id.map_fab);
        refresh_fab = (TextView) findViewById(R.id.refresh_fab);

        title_tv.setText("日常服务");
        back_tv.setOnClickListener(new ViewClickVibrate() {
            @Override
            public void onClick(View view) {
                super.onClick(view);
                finish();
            }
        });

        rcfuLl.setOnClickListener(this);
        fwjhLl.setOnClickListener(this);
        fwjlLl.setOnClickListener(this);
        fwycLl.setOnClickListener(this);

        map_fab.setOnClickListener(new ViewClickVibrate() {
            @Override
            public void onClick(View view) {
                super.onClick(view);

                Intent intent = new Intent(XZFWMainActivity.this, MyLocationMapActivity.class);
                startActivity(intent);

            }
        });

        refresh_fab.setOnClickListener(new ViewClickVibrate() {
            @Override
            public void onClick(View view) {
                super.onClick(view);
                AlertDialog.Builder builder = new AlertDialog.Builder(XZFWMainActivity.this);
                builder.setMessage("是否只使用GPS定位");
                builder.setTitle("提示");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        rcfwMainFragment.locationWithGPS();
                    }
                });
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        rcfwMainFragment.location();
                    }
                });
                builder.create().show();
            }
        });

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        rcfwMainFragment = new RCFWMainFragment();
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

    private void changeBgByMenu(View view){
        VibrateHelp.vSimple(view.getContext());

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        switch (view.getId()){
            case R.id.rcfu_ll:
                title_tv.setText("日常服务");

                rcfwMainFragment = (RCFWMainFragment) fm.findFragmentByTag("rcfw");
                if(rcfwMainFragment == null){
                    rcfwMainFragment = new RCFWMainFragment();
                    ft.add(R.id.realtabcontent, rcfwMainFragment, "rcfw");
                }
                showFragment("rcfw", fm, ft);

                rcfuTv.setBackgroundResource(R.drawable.rcfu1);
                fwjhTv.setBackgroundResource(R.drawable.fwjh0);
                fwjlTv.setBackgroundResource(R.drawable.fwjl0);
                fwycTv.setBackgroundResource(R.drawable.fwyc0);

                map_fab.setVisibility(View.VISIBLE);
                refresh_fab.setVisibility(View.VISIBLE);

                break;
            case R.id.fwjh_ll:
                title_tv.setText("服务计划");

                FWJHMainFragment fwjhMainFragment = (FWJHMainFragment) fm.findFragmentByTag("fwjh");
                if(fwjhMainFragment == null){
                    fwjhMainFragment = new FWJHMainFragment();
                    ft.add(R.id.realtabcontent, fwjhMainFragment, "fwjh");
                }
                showFragment("fwjh", fm, ft);

                rcfuTv.setBackgroundResource(R.drawable.rcfu0);
                fwjhTv.setBackgroundResource(R.drawable.fwjh1);
                fwjlTv.setBackgroundResource(R.drawable.fwjl0);
                fwycTv.setBackgroundResource(R.drawable.fwyc0);

                map_fab.setVisibility(View.GONE);
                refresh_fab.setVisibility(View.GONE);

                break;
            case R.id.fwjl_ll:
                title_tv.setText("服务记录");

                FWJLMainFragment fwjlMainFragment = (FWJLMainFragment) fm.findFragmentByTag("fwjl");
                if(fwjlMainFragment == null){
                    fwjlMainFragment = new FWJLMainFragment();
                    ft.add(R.id.realtabcontent, fwjlMainFragment, "fwjl");
                }
                showFragment("fwjl", fm, ft);

                rcfuTv.setBackgroundResource(R.drawable.rcfu0);
                fwjhTv.setBackgroundResource(R.drawable.fwjh0);
                fwjlTv.setBackgroundResource(R.drawable.fwjl1);
                fwycTv.setBackgroundResource(R.drawable.fwyc0);

                map_fab.setVisibility(View.GONE);
                refresh_fab.setVisibility(View.GONE);

                break;
            case R.id.fwyc_ll:
                title_tv.setText("服务异常");

                FWYCMainFragment fwycMainFragment = (FWYCMainFragment) fm.findFragmentByTag("fwyc");
                if(fwycMainFragment == null){
                    fwycMainFragment = new FWYCMainFragment();
                    ft.add(R.id.realtabcontent, fwycMainFragment, "fwyc");
                }
                showFragment("fwyc", fm, ft);

                rcfuTv.setBackgroundResource(R.drawable.rcfu0);
                fwjhTv.setBackgroundResource(R.drawable.fwjh0);
                fwjlTv.setBackgroundResource(R.drawable.fwjl0);
                fwycTv.setBackgroundResource(R.drawable.fwyc1);

                map_fab.setVisibility(View.GONE);
                refresh_fab.setVisibility(View.GONE);

                break;
        }

        ft.commitAllowingStateLoss();
    }

    @Override
    public void onClick(View view) {
        changeBgByMenu(view);
    }
}
