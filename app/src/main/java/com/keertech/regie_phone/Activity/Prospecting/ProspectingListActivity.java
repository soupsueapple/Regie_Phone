package com.keertech.regie_phone.Activity.Prospecting;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.keertech.regie_phone.BaseActivity;
import com.keertech.regie_phone.R;

/**
 * Created by soup on 2017/5/26.
 */

public class ProspectingListActivity extends BaseActivity{

    private RecyclerView recyclerView;

    private void assignViews() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prospecting_list);
        setToolbarTitle("零售户勘查");
        assignViews();
    }
}
