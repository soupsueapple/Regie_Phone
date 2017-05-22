package com.keertech.regie_phone.Activity.NothaveLicenseCustomer;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.keertech.regie_phone.BaseActivity;
import com.keertech.regie_phone.R;

/**
 * Created by soup on 2017/5/22.
 */

public class NothaveLicenseCustomerInfoActivity extends BaseActivity{

    private TextView informationSourceTv;
    private TextView shopnameTv;
    private TextView genderTv;
    private EditText identityAddressEt;
    private EditText nameEt;
    private EditText addressEt;
    private TextView streetTv;
    private TextView communityTv;
    private TextView helpingGroupsTv;
    private TextView isPunishTv;
    private TextView isDesireTv;
    private TextView isInReasonLayoutTv;
    private TextView isRegularAddressTv;
    private TextView otherReasonTv;
    private TextView isManagePlanTv;
    private TextView statusTv;
    private TextView measureTv;

    private String sourceType = "";
    private String support_groups = "";
    private String gender = "";
    private String isPunish = "";
    private String hasDesire = "";
    private String reasonableLayout = "";
    private String hasBusinessPremises = "";
    private String managerstateId = "";
    private String measuresId = "";

    private String id = "";

    private String[] WZHXXLY_Key = new String[]{"00", "01", "02", "03", "04", "06"};
    private String[] WZHXXLY_Value = new String[]{"行政服务发现", "第三方调查", "网格系统", "市局调查", "省局检查", "客户经理提报"};

    private String[] XB_key = new String[]{"1", "2"};
    private String[] XB_Value = new String[]{"男", "女"};

    String[] BFQT_Key = new String[]{"107101","107102","107103","107104","107105"};
    String[] BFQT_Value = new String[]{"残疾人","军烈属","失独家庭","孤寡老人","其它"};

    private String[] SFCF_key = new String[]{"1", "0"};
    private String[] SFCF_Value = new String[]{"是", "否"};

    private String[] BZYY_key = new String[]{"1", "0"};
    private String[] BZYY_Value = new String[]{"有", "无"};

    private String[] BJYQ_key = new String[]{"1", "0"};
    private String[] BJYQ_Value = new String[]{"是", "否"};

    private String[] GDJYCS_key = new String[]{"1", "0"};
    private String[] GDJYCS_Value = new String[]{"有", "无"};

    private String[] CQCS_Key = new String[]{"1", "2", "3", "4"};
    private String[] CQCS_Value = new String[]{"取缔", "查处", "办证", "宣传教育"};

    private String[] JYZK_Key = new String[]{"2", "3", "4", "5"};
    private String[] JYZK_Value = new String[]{"转向经营", "没有经营", "继续经营", "办证经营"};

    private String[] SQ_Key;
    private String[] SQ_Value;

    private String[] JD_Key;
    private String[] JD_Value;

    private String version = "";
    private String valid = "";

    private String streetId = "";
    private String communityId = "";

    private String relationId = "";

    private void assignViews() {
        informationSourceTv = (TextView) findViewById(R.id.information_source_tv);
        shopnameTv = (TextView) findViewById(R.id.shopname_tv);
        genderTv = (TextView) findViewById(R.id.gender_tv);
        identityAddressEt = (EditText) findViewById(R.id.identity_address_et);
        nameEt = (EditText) findViewById(R.id.name_et);
        addressEt = (EditText) findViewById(R.id.address_et);
        streetTv = (TextView) findViewById(R.id.street_tv);
        communityTv = (TextView) findViewById(R.id.community_tv);
        helpingGroupsTv = (TextView) findViewById(R.id.helping_groups_tv);
        isPunishTv = (TextView) findViewById(R.id.is_punish_tv);
        isDesireTv = (TextView) findViewById(R.id.is_desire_tv);
        isInReasonLayoutTv = (TextView) findViewById(R.id.is_in_reason_layout_tv);
        isRegularAddressTv = (TextView) findViewById(R.id.is_regular_address_tv);
        otherReasonTv = (TextView) findViewById(R.id.other_reason_tv);
        isManagePlanTv = (TextView) findViewById(R.id.is_manage_plan_tv);
        statusTv = (TextView) findViewById(R.id.status_tv);
        measureTv = (TextView) findViewById(R.id.measure_tv);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        id = getIntent().getStringExtra("id");
        nameEt.setText(getIntent().getStringExtra("name"));
        shopnameTv.setText(getIntent().getStringExtra("shopname"));
        addressEt.setText(getIntent().getStringExtra("address"));
        relationId = getIntent().getStringExtra("relationId");
        sourceType = getIntent().getStringExtra("sourceType");

        if(sourceType.equals("06")){
            informationSourceTv.setText("客户经理提报");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nothave_license_customer_info);
        setToolbarTitle("无证户信息");
        showBack();

        assignViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.inspection_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_save){

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
