package com.keertech.regie_phone.Activity.RandomCheck;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.keertech.regie_phone.Activity.RandomCheck.Fragment.RandomCheckFragment;
import com.keertech.regie_phone.Activity.RandomCheck.Fragment.RandomCheckRecordFragment;
import com.keertech.regie_phone.BaseFragmentActivity;
import com.keertech.regie_phone.Listener.ViewClickVibrate;
import com.keertech.regie_phone.R;
import com.keertech.regie_phone.Utility.VibrateHelp;

/**
 * Created by soup on 2017/5/24.
 */

public class RandomCheckMainActivity extends BaseFragmentActivity implements View.OnClickListener{

    private TextView back_tv;
    private TextView title_tv;
    private LinearLayout linearLayout;
    private LinearLayout randomCheckLl;
    private TextView randomCheckTv;
    private LinearLayout checkedRecordLl;
    private TextView checkedRecordTv;
    private FrameLayout realtabcontent;

    private void assignViews() {
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        randomCheckLl = (LinearLayout) findViewById(R.id.random_check_ll);
        randomCheckTv = (TextView) findViewById(R.id.random_check_tv);
        checkedRecordLl = (LinearLayout) findViewById(R.id.checked_record_ll);
        checkedRecordTv = (TextView) findViewById(R.id.checked_record_tv);
        realtabcontent = (FrameLayout) findViewById(R.id.realtabcontent);
        back_tv = (TextView) findViewById(R.id.back_tv);
        title_tv = (TextView) findViewById(R.id.title_tv);

        title_tv.setText("随机检查");
        back_tv.setOnClickListener(new ViewClickVibrate() {
            @Override
            public void onClick(View view) {
                super.onClick(view);
                finish();
            }
        });

        randomCheckLl.setOnClickListener(this);
        checkedRecordLl.setOnClickListener(this);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        RandomCheckFragment randomCheckFragment = new RandomCheckFragment();
        ft.add(R.id.realtabcontent, randomCheckFragment, "random");
        ft.commit();

        randomCheckTv.setBackgroundResource(R.drawable.sjjcw1);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_check_main);

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
            case R.id.random_check_ll:
                title_tv.setText("随机检查");
                RandomCheckFragment randomCheckFragment = (RandomCheckFragment) fm.findFragmentByTag("random");
                if(randomCheckFragment == null){
                    randomCheckFragment = new RandomCheckFragment();
                    ft.add(R.id.realtabcontent, randomCheckFragment, "random");
                }
                showFragment("random", fm, ft);
                randomCheckTv.setBackgroundResource(R.drawable.sjjcw1);
                checkedRecordTv.setBackgroundResource(R.drawable.jcjl0);
                break;
            case R.id.checked_record_ll:
                title_tv.setText("检查记录");
                RandomCheckRecordFragment randomCheckRecordFragment = (RandomCheckRecordFragment) fm.findFragmentByTag("record");
                if(randomCheckRecordFragment == null){
                    randomCheckRecordFragment = new RandomCheckRecordFragment();
                    ft.add(R.id.realtabcontent, randomCheckRecordFragment, "record");
                }
                showFragment("record", fm, ft);
                randomCheckTv.setBackgroundResource(R.drawable.sjjcw0);
                checkedRecordTv.setBackgroundResource(R.drawable.jcjl1);
                break;
        }

        ft.commitAllowingStateLoss();
    }

    @Override
    public void onClick(View view) {
        VibrateHelp.vSimple(view.getContext());
        changeBgByMenu(view);
    }
}
