package com.keertech.regie_phone.Activity.ReportComplanints.ToBeContinued;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.keertech.regie_phone.BaseActivity;
import com.keertech.regie_phone.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by soup on 2017/5/16.
 */

public class ToBeContinuedActivity extends BaseActivity{

    private int type;

    private int reportcomplaintype;

    private String taskid;

    private String id;

    private String[] zpKey;
    private String[] zpValue;

    private String phoneNo = "";

    String cigLicenseNo = "";
    private String tradename = "";

    private TextView licenseTv;
    private TextView phoneTv;
    private TextView shopnameTv;
    private TextView typeTv;
    private TextView nameTv;
    private TextView timeTv;
    private TextView textView9;
    private TextView contentTv;
    private TextView textView10;
    private TextView workTypeTv;
    private TextView textView13;
    private TextView brandTv;
    private LinearLayout linearLayout14;
    private TextView textView14;
    private TextView toBeContinuedInfoTv;
    private EditText toBeContinuedInfoEt;
    private LinearLayout disposeLl;
    private TextView disposeTv;
    private TextView rejectTv;
    private TextView toCheckingTv;

    private void assignViews() {
        licenseTv = (TextView) findViewById(R.id.license_tv);
        phoneTv = (TextView) findViewById(R.id.phone_tv);
        shopnameTv = (TextView) findViewById(R.id.shopname_tv);
        typeTv = (TextView) findViewById(R.id.type_tv);
        nameTv = (TextView) findViewById(R.id.name_tv);
        timeTv = (TextView) findViewById(R.id.time_tv);
        textView9 = (TextView) findViewById(R.id.textView9);
        contentTv = (TextView) findViewById(R.id.content_tv);
        textView10 = (TextView) findViewById(R.id.textView10);
        workTypeTv = (TextView) findViewById(R.id.work_type_tv);
        textView13 = (TextView) findViewById(R.id.textView13);
        brandTv = (TextView) findViewById(R.id.brand_tv);
        linearLayout14 = (LinearLayout) findViewById(R.id.linearLayout14);
        textView14 = (TextView) findViewById(R.id.textView14);
        toBeContinuedInfoTv = (TextView) findViewById(R.id.to_be_continued_info_tv);
        toBeContinuedInfoEt = (EditText) findViewById(R.id.to_be_continued_info_et);
        disposeLl = (LinearLayout) findViewById(R.id.dispose_ll);
        disposeTv = (TextView) findViewById(R.id.dispose_tv);
        rejectTv = (TextView) findViewById(R.id.reject_tv);
        toCheckingTv = (TextView) findViewById(R.id.to_checking_tv);

        try {
            String jsonString = getIntent().getStringExtra("data");
            JSONObject jsonObject = new JSONObject(jsonString);

            reportcomplaintype = jsonObject.getInt("reportcomplaintype");
            type = jsonObject.getInt("type");
            taskid = jsonObject.getString("taskid");


            setToolbarTitle(reportcomplaintype == 2 ? "处理举报" : "处理投诉");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_be_continued);
        showBack();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        assignViews();
    }
}
