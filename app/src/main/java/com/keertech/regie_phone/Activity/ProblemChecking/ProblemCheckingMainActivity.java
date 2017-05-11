package com.keertech.regie_phone.Activity.ProblemChecking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.keertech.regie_phone.Activity.ProblemChecking.Fragment.CheckedRecordFragment;
import com.keertech.regie_phone.Activity.ProblemChecking.Fragment.DepartmentCheckingFragment;
import com.keertech.regie_phone.Activity.ProblemChecking.Fragment.GroupCheckingFragment;
import com.keertech.regie_phone.BaseFragmentActivity;
import com.keertech.regie_phone.Listener.ViewClickVibrate;
import com.keertech.regie_phone.R;
import com.keertech.regie_phone.Utility.VibrateHelp;

/**
 * Created by soup on 2017/5/11.
 */

public class ProblemCheckingMainActivity extends BaseFragmentActivity implements View.OnClickListener{


    private LinearLayout linearLayout;
    private LinearLayout groupCheckingLl;
    private TextView groupCheckingTv;
    private LinearLayout checkedRecordLl;
    private TextView checkedRecordTv;
    private LinearLayout departmentCheckingLl;
    private TextView departmentCheckingTv;
    private FrameLayout realtabcontent;
    private TextView back_tv;
    private TextView title_tv;

    private void assignViews() {
        back_tv = (TextView) findViewById(R.id.back_tv);
        title_tv = (TextView) findViewById(R.id.title_tv);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        groupCheckingLl = (LinearLayout) findViewById(R.id.group_checking_ll);
        groupCheckingTv = (TextView) findViewById(R.id.group_checking_tv);
        checkedRecordLl = (LinearLayout) findViewById(R.id.checked_record_ll);
        checkedRecordTv = (TextView) findViewById(R.id.checked_record_tv);
        departmentCheckingLl = (LinearLayout) findViewById(R.id.department_checking_ll);
        departmentCheckingTv = (TextView) findViewById(R.id.department_checking_tv);
        realtabcontent = (FrameLayout) findViewById(R.id.realtabcontent);

        groupCheckingLl.setOnClickListener(this);
        checkedRecordLl.setOnClickListener(this);
        departmentCheckingLl.setOnClickListener(this);

        title_tv.setText("问题核查");
        back_tv.setOnClickListener(new ViewClickVibrate() {
            @Override
            public void onClick(View view) {
                super.onClick(view);
                finish();
            }
        });

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        GroupCheckingFragment groupCheckingFragment = new GroupCheckingFragment();
        ft.add(R.id.realtabcontent, groupCheckingFragment, "sgzhc");
        ft.commit();

        groupCheckingTv.setBackgroundResource(R.drawable.sgzhc1);
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
            case R.id.group_checking_ll:
                title_tv.setText("市管组核查");

                GroupCheckingFragment groupCheckingFragment = (GroupCheckingFragment) fm.findFragmentByTag("sgzhc");
                if(groupCheckingFragment == null){
                    groupCheckingFragment = new GroupCheckingFragment();
                    ft.add(R.id.realtabcontent, groupCheckingFragment, "sgzhc");
                }
                showFragment("sgzhc", fm, ft);

                groupCheckingTv.setBackgroundResource(R.drawable.sgzhc1);
                checkedRecordTv.setBackgroundResource(R.drawable.hcjl0);
                departmentCheckingTv.setBackgroundResource(R.drawable.bmhc0);
                break;
            case R.id.checked_record_ll:
                title_tv.setText("核查记录");

                CheckedRecordFragment checkedRecordFragment = (CheckedRecordFragment) fm.findFragmentByTag("hcjl");
                if(checkedRecordFragment == null){
                    checkedRecordFragment = new CheckedRecordFragment();
                    ft.add(R.id.realtabcontent, checkedRecordFragment, "hcjl");
                }
                showFragment("hcjl", fm, ft);

                groupCheckingTv.setBackgroundResource(R.drawable.sgzhc0);
                checkedRecordTv.setBackgroundResource(R.drawable.hcjl1);
                departmentCheckingTv.setBackgroundResource(R.drawable.bmhc0);
                break;
            case R.id.department_checking_ll:
                title_tv.setText("部门核查");

                DepartmentCheckingFragment departmentCheckingFragment = (DepartmentCheckingFragment) fm.findFragmentByTag("bmhc");
                if(departmentCheckingFragment == null){
                    departmentCheckingFragment = new DepartmentCheckingFragment();
                    ft.add(R.id.realtabcontent, departmentCheckingFragment, "bmhc");
                }
                showFragment("bmhc", fm, ft);

                groupCheckingTv.setBackgroundResource(R.drawable.sgzhc0);
                checkedRecordTv.setBackgroundResource(R.drawable.hcjl0);
                departmentCheckingTv.setBackgroundResource(R.drawable.bmhc1);
                break;
        }

        ft.commitAllowingStateLoss();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_checking_main);

        assignViews();
    }

    @Override
    public void onClick(View view) {
        VibrateHelp.vSimple(view.getContext());
        changeBgByMenu(view);
    }
}
