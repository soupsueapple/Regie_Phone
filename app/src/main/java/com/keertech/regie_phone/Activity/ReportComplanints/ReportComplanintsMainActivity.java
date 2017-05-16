package com.keertech.regie_phone.Activity.ReportComplanints;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.platform.comapi.map.L;
import com.keertech.regie_phone.BaseFragmentActivity;
import com.keertech.regie_phone.Listener.ViewClickVibrate;
import com.keertech.regie_phone.R;
import com.keertech.regie_phone.Utility.VibrateHelp;

/**
 * Created by soup on 2017/5/16.
 */

public class ReportComplanintsMainActivity extends BaseFragmentActivity implements View.OnClickListener{

    private LinearLayout linearLayout;
    private LinearLayout toBeContinuedLl;
    private TextView toBeContinuedTv;
    private LinearLayout finishedLl;
    private TextView finishedTv;
    private FrameLayout realtabcontent;
    private TextView back_tv;
    private TextView title_tv;

    private void assignViews() {

        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        toBeContinuedLl = (LinearLayout) findViewById(R.id.to_be_continued_ll);
        toBeContinuedTv = (TextView) findViewById(R.id.to_be_continued_tv);
        finishedLl = (LinearLayout) findViewById(R.id.finished_ll);
        finishedTv = (TextView) findViewById(R.id.finished_tv);
        realtabcontent = (FrameLayout) findViewById(R.id.realtabcontent);
        back_tv = (TextView) findViewById(R.id.back_tv);
        title_tv = (TextView) findViewById(R.id.title_tv);

        title_tv.setText("日常服务");
        back_tv.setOnClickListener(new ViewClickVibrate() {
            @Override
            public void onClick(View view) {
                super.onClick(view);
                finish();
            }
        });

        toBeContinuedLl.setOnClickListener(this);
        finishedLl.setOnClickListener(this);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ToBeContinuedFragment toBeContinuedFragment = new ToBeContinuedFragment();
        ft.add(R.id.realtabcontent, toBeContinuedFragment, "toBeContinued");
        ft.commit();

        toBeContinuedTv.setBackgroundResource(R.drawable.wwc1);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_complantnts_main);

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
            case R.id.to_be_continued_ll:
                title_tv.setText("未处理");
                ToBeContinuedFragment toBeContinuedFragment = (ToBeContinuedFragment) fm.findFragmentByTag("toBeContinued");
                if(toBeContinuedFragment == null){
                    toBeContinuedFragment = new ToBeContinuedFragment();
                    ft.add(R.id.realtabcontent, toBeContinuedFragment, "toBeContinued");
                }
                showFragment("toBeContinued", fm, ft);
                toBeContinuedTv.setBackgroundResource(R.drawable.wwc1);
                finishedTv.setBackgroundResource(R.drawable.ywc0);
                break;
            case R.id.finished_ll:
                title_tv.setText("已完成");
                FinishedFragment finishedFragment = (FinishedFragment) fm.findFragmentByTag("finished");
                if(finishedFragment == null){
                    finishedFragment = new FinishedFragment();
                    ft.add(R.id.realtabcontent, finishedFragment, "finished");
                }
                showFragment("finished", fm, ft);
                toBeContinuedTv.setBackgroundResource(R.drawable.wwc0);
                finishedTv.setBackgroundResource(R.drawable.ywc1);
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
