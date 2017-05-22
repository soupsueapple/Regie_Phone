package com.keertech.regie_phone.Activity.NothaveLicenseCustomer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.keertech.regie_phone.BaseActivity;
import com.keertech.regie_phone.Constant.Constant;
import com.keertech.regie_phone.Network.HttpClient;
import com.keertech.regie_phone.R;
import com.keertech.regie_phone.Utility.KeerAlertDialog;
import com.keertech.regie_phone.Utility.StringUtility;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    private void loadInfo() {
        if (!StringUtility.isEmpty(id)) {
            final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
            pd.show();

            RequestParams params = new RequestParams();
            params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MWB_Base_URL + "noLiceRegistry!findBean.action?privilegeFlag=VIEW&bean.id=" + id + "\",\"type\":\"WebExecutor\",\"method\":\"GET\"},\"app\":\"1001\"}");

            HttpClient.post(Constant.EXEC, params, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    pd.dismiss();

                    try {
                        if(StringUtility.isSuccess(response)){

                            String messageSting = response.getString("message");

                            JSONObject message = new JSONObject(messageSting);

                            if (StringUtility.isSuccess(message)) {

                                JSONObject data = message.getJSONObject("data");

                                id = StringUtility.isEmpty(data.getString("id"))?"":data.getString("id");

                                String sourceType = StringUtility.isEmpty(data.getString("sourceType"))?"":data.getString("sourceType");

                                NothaveLicenseCustomerInfoActivity.this.sourceType = sourceType;

                                if(sourceType.equals("00")){
                                    informationSourceTv.setText("行政服务发现");
                                }else if(sourceType.equals("01")){
                                    informationSourceTv.setText("第三方调查");
                                }else if(sourceType.equals("02")){
                                    informationSourceTv.setText("网格系统");
                                }else if(sourceType.equals("03")){
                                    informationSourceTv.setText("市局调查");
                                }else if(sourceType.equals("04")){
                                    informationSourceTv.setText("省局检查");
                                }

                                nameEt.setText(StringUtility.isEmpty(data.getString("name"))?"":data.getString("name"));
                                shopnameTv.setText(StringUtility.isEmpty(data.getString("shopName"))?"":data.getString("shopName"));
                                addressEt.setText(StringUtility.isEmpty(data.getString("address"))?"":data.getString("address"));

                                JSONObject community = data.getJSONObject("community");
                                JSONObject street = community.getJSONObject("street");

                                streetId = StringUtility.isEmpty(street.getString("id"))?"":street.getString("id");
                                communityId = StringUtility.isEmpty(community.getString("id"))?"":community.getString("id");

                                identityAddressEt.setText(StringUtility.isEmpty(data.getString("hometown"))?"":data.getString("hometown"));

                                String gender = StringUtility.isEmpty(data.getString("gender"))?"":data.getString("gender");

                                NothaveLicenseCustomerInfoActivity.this.gender = gender;

                                if(gender.equals("1")){
                                    genderTv.setText("男");
                                }else if(gender.equals("2")){
                                    genderTv.setText("女");
                                }

                                String support_groups = StringUtility.isEmpty(data.getString("support_groups"))?"":data.getString("support_groups");
                                NothaveLicenseCustomerInfoActivity.this.support_groups = support_groups;

                                if(support_groups.equals("107101")){//{"107101","107102","107103","107104","107105"}; {"残疾人","军烈属","失独家庭","孤寡老人","其它"};
                                    helpingGroupsTv.setText("残疾人");
                                }else if(support_groups.equals("107102")){
                                    helpingGroupsTv.setText("军烈属");
                                }else if(support_groups.equals("107103")){
                                    helpingGroupsTv.setText("失独家庭");
                                }else if(support_groups.equals("107104")){
                                    helpingGroupsTv.setText("孤寡老人");
                                }else if(support_groups.equals("107105")){
                                    helpingGroupsTv.setText("其它");
                                }

                                String  isPunish = StringUtility.isEmpty(data.getString("isPunish"))?"":data.getString("isPunish");
                                NothaveLicenseCustomerInfoActivity.this.isPunish = isPunish;

                                if(isPunish.equals("0")){
                                    isPunishTv.setText("否");
                                }else if(isPunish.equals("1")){
                                    isPunishTv.setText("是");
                                }

                                String  hasDesire = StringUtility.isEmpty(data.getString("hasDesire"))?"":data.getString("hasDesire");
                                NothaveLicenseCustomerInfoActivity.this.hasDesire = hasDesire;

                                if(hasDesire.equals("0")){
                                    isDesireTv.setText("无");
                                }else if(hasDesire.equals("1")){
                                    isDesireTv.setText("有");
                                }

                                String  reasonableLayout = StringUtility.isEmpty(data.getString("reasonableLayout"))?"":data.getString("reasonableLayout");
                                NothaveLicenseCustomerInfoActivity.this.reasonableLayout = reasonableLayout;

                                if(reasonableLayout.equals("0")){
                                    isInReasonLayoutTv.setText("否");
                                }else if(reasonableLayout.equals("1")){
                                    isInReasonLayoutTv.setText("是");
                                }

                                String  hasBusinessPremises = StringUtility.isEmpty(data.getString("hasBusinessPremises"))?"":data.getString("hasBusinessPremises");
                                NothaveLicenseCustomerInfoActivity.this.hasBusinessPremises = hasBusinessPremises;

                                if(hasBusinessPremises.equals("0")){
                                    isRegularAddressTv.setText("无");
                                }else if(hasBusinessPremises.equals("1")){
                                    isRegularAddressTv.setText("有");
                                }

                                otherReasonTv.setText(StringUtility.isEmpty(data.getString("otherReasons"))?"":data.getString("otherReasons"));
                                isManagePlanTv.setText(StringUtility.isEmpty(data.getString("managementPlan"))?"":data.getString("managementPlan"));

                                String  measuresId = StringUtility.isEmpty(data.getString("measuresId"))?"":data.getString("measuresId");
                                NothaveLicenseCustomerInfoActivity.this.measuresId = measuresId;

                                if(measuresId.equals("1")){
                                    measureTv.setText("取缔");
                                }else if(measuresId.equals("2")){
                                    measureTv.setText("查处");
                                }else if(measuresId.equals("3")){
                                    measureTv.setText("办证");
                                }else if(measuresId.equals("4")){
                                    measureTv.setText("宣传教育");
                                }

                                String  managerstateId = StringUtility.isEmpty(data.getString("managerstateId"))?"":data.getString("managerstateId");
                                NothaveLicenseCustomerInfoActivity.this.managerstateId = managerstateId;

                                if(managerstateId.equals("2")){
                                    statusTv.setText("转向经营");
                                }else if(managerstateId.equals("3")){
                                    statusTv.setText("没有经营");
                                }else if(managerstateId.equals("4")){
                                    statusTv.setText("继续经营");
                                }else if(managerstateId.equals("5")){
                                    statusTv.setText("办证经营");
                                }

                                version = StringUtility.isEmpty(data.getString("version"))?"":data.getString("version");
                                valid = StringUtility.isEmpty(data.getString("valid"))?"":data.getString("valid");

                                loadStreet(false);
                                loadCommunity(streetId, false);
                            }else {
                                showToast(message.getString("message"), NothaveLicenseCustomerInfoActivity.this);
                            }

                        }else{
                            showToast(response.getString("message"), NothaveLicenseCustomerInfoActivity.this);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    pd.dismiss();
                    showNetworkError(NothaveLicenseCustomerInfoActivity.this);
                    super.onFailure(statusCode, headers, responseString, throwable);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    pd.dismiss();
                    showNetworkError(NothaveLicenseCustomerInfoActivity.this);
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    pd.dismiss();
                    showNetworkError(NothaveLicenseCustomerInfoActivity.this);
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });
        }
    }

    private void loadStreet(final boolean isClick){
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams params = new RequestParams();
        params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\""+Constant.MWB_Base_URL+"streetInfo!searchBeans.action?privilegeFlag=VIEW&start=0&limit=100\",\"type\":\"WebExecutor\",\"method\":\"GET\"},\"app\":\"1001\"}");

        HttpClient.post(Constant.EXEC, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                pd.dismiss();

                try {
                    if(StringUtility.isSuccess(response)){

                        String messageSting = response.getString("message");

                        JSONObject message = new JSONObject(messageSting);

                        if (StringUtility.isSuccess(message)) {

                            JSONArray data = message.getJSONArray("data");

                            JD_Key = new String[data.length()];
                            JD_Value = new String[data.length()];

                            for(int i=0;i<data.length();i++){
                                JSONObject object = data.getJSONObject(i);

                                JD_Key[i] = object.getString("id");
                                JD_Value[i] = object.getString("streetName");
                            }

                            if(!isClick) {
                                if (!StringUtility.isEmpty(streetId)) {
                                    for (int i = 0; i < JD_Key.length; i++) {
                                        String street = JD_Key[i];
                                        if (street.equals(streetId)) {
                                            addressEt.setText(JD_Value[i]);
                                        }
                                    }
                                }
                            }else {
                                chickDialog("经营场所所在街道", "bean.community.street.id", JD_Key, JD_Value, addressEt);
                            }

                        }else {
                            showToast(message.getString("message"), NothaveLicenseCustomerInfoActivity.this);
                        }

                    }else{
                        showToast(response.getString("message"), NothaveLicenseCustomerInfoActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(NothaveLicenseCustomerInfoActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(NothaveLicenseCustomerInfoActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(NothaveLicenseCustomerInfoActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void loadCommunity(String streetId, final boolean isClick){

        if(!StringUtility.isEmpty(streetId)) {

            final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
            pd.show();

            RequestParams params = new RequestParams();
            params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MWB_Base_URL + "communityInfo!searchBeans.action?privilegeFlag=VIEW&_query.streetId=" + streetId + "\",\"type\":\"WebExecutor\",\"method\":\"GET\"},\"app\":\"1001\"}");

            HttpClient.post(Constant.EXEC, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    pd.dismiss();

                    try {
                        if (StringUtility.isSuccess(response)) {

                            String messageSting = response.getString("message");

                            JSONObject message = new JSONObject(messageSting);

                            if (StringUtility.isSuccess(message)) {

                                JSONArray data = message.getJSONArray("data");

                                SQ_Key = new String[data.length()];
                                SQ_Value = new String[data.length()];

                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject object = data.getJSONObject(i);

                                    SQ_Key[i] = object.getString("id");
                                    SQ_Value[i] = object.getString("communityName");
                                }

                                if(!isClick) {
                                    if (!StringUtility.isEmpty(communityId)) {
                                        for (int i = 0; i < SQ_Key.length; i++) {
                                            String street = SQ_Key[i];
                                            if (street.equals(communityId)) {
                                                communityTv.setText(SQ_Value[i]);
                                            }
                                        }
                                    }
                                }else {
                                    chickDialog("经营场所所在社区", "bean.community.id", SQ_Key, SQ_Value, communityTv);
                                }

                            } else {
                                showToast(message.getString("message"), NothaveLicenseCustomerInfoActivity.this);
                            }

                        } else {
                            showToast(response.getString("message"), NothaveLicenseCustomerInfoActivity.this);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    pd.dismiss();
                    showNetworkError(NothaveLicenseCustomerInfoActivity.this);
                    super.onFailure(statusCode, headers, responseString, throwable);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    pd.dismiss();
                    showNetworkError(NothaveLicenseCustomerInfoActivity.this);
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    pd.dismiss();
                    showNetworkError(NothaveLicenseCustomerInfoActivity.this);
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });
        }
    }

    private void chickDialog(final String tip,final String bean, final String[] key, final String[] value,final TextView textView){

        AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle(tip).setItems(value, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (bean.equals("bean.community.street.id")) streetId = key[which];
                if (bean.equals("bean.community.id")) communityId = key[which];
                if (bean.equals("bean.sourceType")) sourceType = key[which];
                if (bean.equals("bean.support_groups")) support_groups = key[which];
                if (bean.equals("bean.gender")) gender = key[which];
                if (bean.equals("bean.isPunish")) isPunish = key[which];
                if (bean.equals("bean.hasDesire")) hasDesire = key[which];
                if (bean.equals("bean.reasonableLayout")) reasonableLayout = key[which];
                if (bean.equals("bean.hasBusinessPremises")) hasBusinessPremises = key[which];
                if (bean.equals("bean.measuresId")) measuresId = key[which];
                if (bean.equals("bean.managerstateId")) managerstateId = key[which];

                textView.setText(value[which]);
            }
        }).create();
        alertDialog.show();

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
