package com.keertech.regie_phone.Activity.VisitCheck;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.keertech.regie_phone.Activity.VisitCheck.Fragment.MySelfResponsibilityFragment;
import com.keertech.regie_phone.Activity.VisitCheck.Fragment.SuperintendentVerifyFragment;
import com.keertech.regie_phone.Activity.VisitCheck.Fragment.ThePlaceResponsibilityFragment;
import com.keertech.regie_phone.BaseFragmentActivity;
import com.keertech.regie_phone.Listener.ViewClickVibrate;
import com.keertech.regie_phone.R;
import com.keertech.regie_phone.Utility.VibrateHelp;

/**
 * Created by soup on 2017/5/23.
 */

public class VisitCheckMainActivity extends BaseFragmentActivity implements View.OnClickListener{

    private LinearLayout linearLayout;
    private LinearLayout theplaceResponsibilityLl;
    private TextView theplaceResponsilibilityTv;
    private LinearLayout myselfResiponsibilityLl;
    private TextView myselfResiponsibilityTv;
    private LinearLayout superintendentVerifyLl;
    private TextView superintendentVerifyTv;
    private FrameLayout realtabcontent;
    private TextView back_tv;
    private TextView title_tv;

    private void assignViews() {
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        theplaceResponsibilityLl = (LinearLayout) findViewById(R.id.theplace_responsibility_ll);
        theplaceResponsilibilityTv = (TextView) findViewById(R.id.theplace_responsilibility_tv);
        myselfResiponsibilityLl = (LinearLayout) findViewById(R.id.myself_resiponsibility_ll);
        myselfResiponsibilityTv = (TextView) findViewById(R.id.myself_resiponsibility_tv);
        superintendentVerifyLl = (LinearLayout) findViewById(R.id.superintendent_verify_ll);
        superintendentVerifyTv = (TextView) findViewById(R.id.superintendent_verify_tv);
        realtabcontent = (FrameLayout) findViewById(R.id.realtabcontent);
        back_tv = (TextView) findViewById(R.id.back_tv);
        title_tv = (TextView) findViewById(R.id.title_tv);

        theplaceResponsibilityLl.setOnClickListener(this);
        myselfResiponsibilityLl.setOnClickListener(this);
        superintendentVerifyLl.setOnClickListener(this);

        title_tv.setText("本所责任");
        back_tv.setOnClickListener(new ViewClickVibrate() {
            @Override
            public void onClick(View view) {
                super.onClick(view);
                finish();
            }
        });

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ThePlaceResponsibilityFragment thePlaceResponsibilityFragment = new ThePlaceResponsibilityFragment();
        ft.add(R.id.realtabcontent, thePlaceResponsibilityFragment, "thePlace");
        ft.commit();

        theplaceResponsilibilityTv.setBackgroundResource(R.drawable.bszr1);
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_check_main);

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
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        switch (view.getId()){
            case R.id.theplace_responsibility_ll:
                title_tv.setText("本所责任");

                ThePlaceResponsibilityFragment thePlaceResponsibilityFragment = (ThePlaceResponsibilityFragment) fm.findFragmentByTag("thePlace");
                if(thePlaceResponsibilityFragment == null){
                    thePlaceResponsibilityFragment = new ThePlaceResponsibilityFragment();
                    ft.add(R.id.realtabcontent, thePlaceResponsibilityFragment, "thePlace");
                }
                showFragment("thePlace", fm, ft);

                theplaceResponsilibilityTv.setBackgroundResource(R.drawable.bszr1);
                myselfResiponsibilityTv.setBackgroundResource(R.drawable.brzr0);
                superintendentVerifyTv.setBackgroundResource(R.drawable.szqr0);
                break;
            case R.id.myself_resiponsibility_ll:
                title_tv.setText("本人责任");

                MySelfResponsibilityFragment mySelfResponsibilityFragment = (MySelfResponsibilityFragment) fm.findFragmentByTag("mySelf");
                if(mySelfResponsibilityFragment == null){
                    mySelfResponsibilityFragment = new MySelfResponsibilityFragment();
                    ft.add(R.id.realtabcontent, mySelfResponsibilityFragment, "mySelf");
                }
                showFragment("mySelf", fm, ft);

                theplaceResponsilibilityTv.setBackgroundResource(R.drawable.bszr0);
                myselfResiponsibilityTv.setBackgroundResource(R.drawable.brzr1);
                superintendentVerifyTv.setBackgroundResource(R.drawable.szqr0);
                break;
            case R.id.superintendent_verify_ll:
                title_tv.setText("所长确认");

                SuperintendentVerifyFragment superintendentVerifyFragment = (SuperintendentVerifyFragment) fm.findFragmentByTag("super");
                if(superintendentVerifyFragment == null){
                    superintendentVerifyFragment = new SuperintendentVerifyFragment();
                    ft.add(R.id.realtabcontent, superintendentVerifyFragment, "super");
                }
                showFragment("super", fm, ft);

                theplaceResponsilibilityTv.setBackgroundResource(R.drawable.bszr0);
                myselfResiponsibilityTv.setBackgroundResource(R.drawable.brzr0);
                superintendentVerifyTv.setBackgroundResource(R.drawable.szqr1);
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
