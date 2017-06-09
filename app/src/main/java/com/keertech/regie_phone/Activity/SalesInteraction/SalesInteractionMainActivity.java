package com.keertech.regie_phone.Activity.SalesInteraction;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.keertech.regie_phone.Activity.SalesInteraction.Fragment.CustomerInformationReportTableFragment;
import com.keertech.regie_phone.Activity.SalesInteraction.Fragment.NotHaveLicenseCustomerInformationReportTableFragment;
import com.keertech.regie_phone.BaseActivity;
import com.keertech.regie_phone.BaseFragmentActivity;
import com.keertech.regie_phone.Listener.ViewClickVibrate;
import com.keertech.regie_phone.R;
import com.keertech.regie_phone.Utility.VibrateHelp;

/**
 * Created by soup on 2017/5/25.
 */

public class SalesInteractionMainActivity extends BaseFragmentActivity implements View.OnClickListener{

    private LinearLayout linearLayout;
    private LinearLayout customerInformationReportTableLl;
    private TextView customerInformationReportTableTv;
    private LinearLayout notHaveLicenseCustomerInformationReportTableLl;
    private TextView notHaveLicenseCustomerInformationReportTableTv;
    private FrameLayout realtabcontent;
    private TextView back_tv;
    private TextView title_tv;

    private void assignViews() {
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        customerInformationReportTableLl = (LinearLayout) findViewById(R.id.customer_information_report_table_ll);
        customerInformationReportTableTv = (TextView) findViewById(R.id.customer_information_report_table_tv);
        notHaveLicenseCustomerInformationReportTableLl = (LinearLayout) findViewById(R.id.not_have_license_customer_information_report_table_ll);
        notHaveLicenseCustomerInformationReportTableTv = (TextView) findViewById(R.id.not_have_license_customer_information_report_table_tv);
        realtabcontent = (FrameLayout) findViewById(R.id.realtabcontent);
        back_tv = (TextView) findViewById(R.id.back_tv);
        title_tv = (TextView) findViewById(R.id.title_tv);

        title_tv.setText("有证户信息提报表");
        back_tv.setOnClickListener(new ViewClickVibrate() {
            @Override
            public void onClick(View view) {
                super.onClick(view);
                finish();
            }
        });

        customerInformationReportTableLl.setOnClickListener(this);
        notHaveLicenseCustomerInformationReportTableLl.setOnClickListener(this);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        CustomerInformationReportTableFragment customerInformationReportTableFragment = new CustomerInformationReportTableFragment();
        ft.add(R.id.realtabcontent, customerInformationReportTableFragment, "customer");
        ft.commit();

        customerInformationReportTableTv.setBackgroundResource(R.drawable.zm_yzhxxtbb1);
        notHaveLicenseCustomerInformationReportTableTv.setBackgroundResource(R.drawable.zm_wzhxxtbb0);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_interaction_main);

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
            case R.id.customer_information_report_table_ll:
                title_tv.setText("有证户信息提报表");
                CustomerInformationReportTableFragment customerInformationReportTableFragment = (CustomerInformationReportTableFragment) fm.findFragmentByTag("customer");
                if(customerInformationReportTableFragment == null){
                    customerInformationReportTableFragment = new CustomerInformationReportTableFragment();
                    ft.add(R.id.realtabcontent, customerInformationReportTableFragment, "customer");
                }
                showFragment("customer", fm, ft);
                customerInformationReportTableTv.setBackgroundResource(R.drawable.zm_yzhxxtbb1);
                notHaveLicenseCustomerInformationReportTableTv.setBackgroundResource(R.drawable.zm_wzhxxtbb0);
                break;
            case R.id.not_have_license_customer_information_report_table_ll:
                title_tv.setText("无证户信息提报表");
                NotHaveLicenseCustomerInformationReportTableFragment notHaveLicenseCustomerInformationReportTableFragment = (NotHaveLicenseCustomerInformationReportTableFragment) fm.findFragmentByTag("notlicense");
                if(notHaveLicenseCustomerInformationReportTableFragment == null){
                    notHaveLicenseCustomerInformationReportTableFragment = new NotHaveLicenseCustomerInformationReportTableFragment();
                    ft.add(R.id.realtabcontent, notHaveLicenseCustomerInformationReportTableFragment, "notlicense");
                }
                showFragment("notlicense", fm, ft);
                customerInformationReportTableTv.setBackgroundResource(R.drawable.zm_yzhxxtbb0);
                notHaveLicenseCustomerInformationReportTableTv.setBackgroundResource(R.drawable.zm_wzhxxtbb1);
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
