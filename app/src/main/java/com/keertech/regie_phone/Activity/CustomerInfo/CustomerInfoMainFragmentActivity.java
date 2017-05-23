package com.keertech.regie_phone.Activity.CustomerInfo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.keertech.regie_phone.BaseFragmentActivity;
import com.keertech.regie_phone.Listener.ViewClickVibrate;
import com.keertech.regie_phone.R;
import com.keertech.regie_phone.Utility.VibrateHelp;


/**
 * Created by soup on 2017/5/17.
 */

public class CustomerInfoMainFragmentActivity extends BaseFragmentActivity implements View.OnClickListener{

    private LinearLayout linearLayout;
    private LinearLayout customerLl;
    private TextView customerTv;
    private LinearLayout allCityCustomerLl;
    private TextView allCityCustomerTv;
    private FrameLayout realtabcontent;
    private TextView back_tv;
    private TextView title_tv;

    private void assignViews() {
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        customerLl = (LinearLayout) findViewById(R.id.customer_ll);
        customerTv = (TextView) findViewById(R.id.customer_tv);
        allCityCustomerLl = (LinearLayout) findViewById(R.id.all_city_customer_ll);
        allCityCustomerTv = (TextView) findViewById(R.id.all_city_customer_tv);
        realtabcontent = (FrameLayout) findViewById(R.id.realtabcontent);
        back_tv = (TextView) findViewById(R.id.back_tv);
        title_tv = (TextView) findViewById(R.id.title_tv);

        title_tv.setText("经营户信息");
        back_tv.setOnClickListener(new ViewClickVibrate() {
            @Override
            public void onClick(View view) {
                super.onClick(view);
                finish();
            }
        });

        customerLl.setOnClickListener(this);
        allCityCustomerLl.setOnClickListener(this);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        CustomerInfoFragment customerInfoFragment = new CustomerInfoFragment();
        ft.add(R.id.realtabcontent, customerInfoFragment, "customer");
        ft.commit();

        customerTv.setBackgroundResource(R.drawable.jyhxx1);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_info_main);

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
            case R.id.customer_ll:
                title_tv.setText("经营户信息");
                CustomerInfoFragment customerInfoFragment = (CustomerInfoFragment) fm.findFragmentByTag("customer");
                if(customerInfoFragment == null){
                    customerInfoFragment = new CustomerInfoFragment();
                    ft.add(R.id.realtabcontent, customerInfoFragment, "customer");
                }
                showFragment("customer", fm, ft);

                customerTv.setBackgroundResource(R.drawable.jyhxx1);
                allCityCustomerTv.setBackgroundResource(R.drawable.qsjyh0);

                break;
            case R.id.all_city_customer_ll:
                title_tv.setText("全市经营户信息");
                AllCityCustomerInfoFragment allCityCustomerFragment = (AllCityCustomerInfoFragment) fm.findFragmentByTag("allCity");
                if(allCityCustomerFragment == null){
                    allCityCustomerFragment = new AllCityCustomerInfoFragment();
                    ft.add(R.id.realtabcontent, allCityCustomerFragment, "allCity");
                }
                showFragment("allCity", fm, ft);

                customerTv.setBackgroundResource(R.drawable.jyhxx0);
                allCityCustomerTv.setBackgroundResource(R.drawable.qsjyh1);
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
