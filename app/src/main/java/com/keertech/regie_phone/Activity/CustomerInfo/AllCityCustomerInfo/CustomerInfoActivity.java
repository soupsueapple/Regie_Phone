package com.keertech.regie_phone.Activity.CustomerInfo.AllCityCustomerInfo;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.keertech.regie_phone.Activity.CustomerInfo.CustomerInfo.MapTestActivity;
import com.keertech.regie_phone.BaseActivity;
import com.keertech.regie_phone.BroadcastReceiver.MapBroadcastReceiver;
import com.keertech.regie_phone.Constant.Constant;
import com.keertech.regie_phone.Listener.ViewClickVibrate;
import com.keertech.regie_phone.Models.BizStatus;
import com.keertech.regie_phone.Models.Community;
import com.keertech.regie_phone.Models.Corp;
import com.keertech.regie_phone.Models.Department;
import com.keertech.regie_phone.Models.Operator;
import com.keertech.regie_phone.Models.Post;
import com.keertech.regie_phone.Network.HttpClient;
import com.keertech.regie_phone.Observer.MapObserver;
import com.keertech.regie_phone.R;
import com.keertech.regie_phone.Utility.KeerAlertDialog;
import com.keertech.regie_phone.Utility.StringUtility;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Calendar;


/**
 * Created by soup on 2017/5/18.
 */

public class CustomerInfoActivity extends BaseActivity {

    private TextView shopnameTv;
    private TextView addressTv;
    private TextView staredTv;
    private TextView phoneTv;
    private TextView takePhotoTv;
    private TextView licenseTv;
    private TextView licenseBookerTv;
    private TextView orderPhoneTv;
    private TextView identityCardTv;
    private TextView businessNoTv;
    private TextView releaseCardDateTv;
    private TextView effectiveDateTv;
    private TextView regionalismTv;
    private TextView departmentTv;
    private TextView streetTv;
    private TextView communityTv;
    private TextView commonTv;
    private TextView managerTv;
    private TextView businessStatusTv;
    private TextView trustLevelTv;
    private TextView punishNumTv;
    private TextView orderInfoTv;
    private LinearLayout realNameLl;
    private TextView realNameTv;
    private LinearLayout birthdayLl;
    private TextView birthdayTv;
    private LinearLayout businessSectionLl;
    private TextView businessSectionTv;
    private LinearLayout connectPhoneLl;
    private TextView connectPhoneTv;
    private LinearLayout practitionerNumLl;
    private TextView practitionerNumTv;
    private LinearLayout businessYearLl;
    private TextView businessYearTv;
    private LinearLayout businessTypeLl;
    private TextView businessTypeTv;
    private LinearLayout porpertiyLl;
    private TextView porpertiyTv;
    private LinearLayout storePorpertiyLl;
    private TextView storePorpertiyTv;
    private LinearLayout provinceLl;
    private TextView provinceTv;
    private LinearLayout cityLl;
    private TextView cityTv;
    private LinearLayout countyLl;
    private TextView countyTv;
    private LinearLayout helpingGroupsLl;
    private TextView helpingGroupsTv;
    private LinearLayout addressStatusLl;
    private TextView addressStatusTv;
    private LinearLayout businessNumberLl;
    private TextView businessNumberTv;

    String[] JYDDkey = new String[]{"103101", "103102", "103103", "103104", "103105", "103106", "103107"};
    String[] JYDDValue = new String[]{"繁华商圈", "主要街道", "背街小巷", "居民区", "旅客集散地", "学院区", "其它"};

    String[] SCLXKey = new String[]{"102101", "102102", "102103"};
    String[] SCLXValue = new String[]{"城市", "城镇", "农村"};

    String[] JYYTKey = new String[]{"104101", "104102", "104103", "104104", "104105", "104106", "104107", "104108"};
    String[] JYYTValue = new String[]{"食杂店", "便利店", "超市", "商场", "名烟名酒名茶", "娱乐服务", "其它", "行业自办店"};

    String[] MDXZKey = new String[]{"101101", "101102"};
    String[] MDXZValue = new String[]{"自有", "租赁"};

    String[] BFQTKey = new String[]{"107101", "107102", "107103", "107104", "107105"};
    String[] BFQTValue = new String[]{"残疾人", "军烈属", "失独家庭", "孤寡老人", "其它"};

    String[] DZZTKey = new String[]{"1", "2", "3"};
    String[] DZZTValue = new String[]{"原址经营", "待拆迁", "已拆迁"};

    String[] ProvinceKey;
    String[] ProvinceValue;

    String[] CityKey;
    String[] CityValue;

    String[] CountyKey;
    String[] CountyValue;

    private String provinceId = "";

    private String cityId = "";

    private Operator operator;

    Calendar calendar = Calendar.getInstance();

    String business_type = "";

    String market_type = "";

    String porpertiy_code = "";

    String store_nature = "";

    String support_groups = "";

    String addrProvince = "";

    String addrCity = "";

    String addrCounty = "";

    /**国测局 经度 */
    private double longitude = 0.0;

    /**国测局 纬度 */
    private double latitude = 0.0;

    /**百度 经度 */
    private double bd_longitude = 0.0;

    /**百度 纬度 */
    private double bd_latitude = 0.0;

    private String address = "";

    private String addressState = "";

    private Integer mark;

    private String[] phones;

    private int needLocation = 1;

    private static MapObserver mapObserver;

    private void assignViews() {
        shopnameTv = (TextView) findViewById(R.id.shopname_tv);
        addressTv = (TextView) findViewById(R.id.address_tv);
        staredTv = (TextView) findViewById(R.id.stared_tv);
        phoneTv = (TextView) findViewById(R.id.phone_tv);
        takePhotoTv = (TextView) findViewById(R.id.take_photo_tv);
        licenseTv = (TextView) findViewById(R.id.license_tv);
        licenseBookerTv = (TextView) findViewById(R.id.license_booker_tv);
        orderPhoneTv = (TextView) findViewById(R.id.order_phone_tv);
        identityCardTv = (TextView) findViewById(R.id.identity_card_tv);
        businessNoTv = (TextView) findViewById(R.id.business_no_tv);
        releaseCardDateTv = (TextView) findViewById(R.id.release_card_date_tv);
        effectiveDateTv = (TextView) findViewById(R.id.effective_date_tv);
        regionalismTv = (TextView) findViewById(R.id.regionalism_tv);
        departmentTv = (TextView) findViewById(R.id.department_tv);
        streetTv = (TextView) findViewById(R.id.street_tv);
        communityTv = (TextView) findViewById(R.id.community_tv);
        commonTv = (TextView) findViewById(R.id.common_tv);
        managerTv = (TextView) findViewById(R.id.manager_tv);
        businessStatusTv = (TextView) findViewById(R.id.business_status_tv);
        trustLevelTv = (TextView) findViewById(R.id.trust_level_tv);
        punishNumTv = (TextView) findViewById(R.id.punish_num_tv);
        orderInfoTv = (TextView) findViewById(R.id.order_info_tv);
        realNameLl = (LinearLayout) findViewById(R.id.real_name_ll);
        realNameTv = (TextView) findViewById(R.id.real_name_tv);
        birthdayLl = (LinearLayout) findViewById(R.id.birthday_ll);
        birthdayTv = (TextView) findViewById(R.id.birthday_tv);
        businessSectionLl = (LinearLayout) findViewById(R.id.business_section_ll);
        businessSectionTv = (TextView) findViewById(R.id.business_section_tv);
        connectPhoneLl = (LinearLayout) findViewById(R.id.connect_phone_ll);
        connectPhoneTv = (TextView) findViewById(R.id.connect_phone_tv);
        practitionerNumLl = (LinearLayout) findViewById(R.id.practitioner_num_ll);
        practitionerNumTv = (TextView) findViewById(R.id.practitioner_num_tv);
        businessYearLl = (LinearLayout) findViewById(R.id.business_year_ll);
        businessYearTv = (TextView) findViewById(R.id.business_year_tv);
        businessTypeLl = (LinearLayout) findViewById(R.id.business_type_ll);
        businessTypeTv = (TextView) findViewById(R.id.business_type_tv);
        porpertiyLl = (LinearLayout) findViewById(R.id.porpertiy_ll);
        porpertiyTv = (TextView) findViewById(R.id.porpertiy_tv);
        storePorpertiyLl = (LinearLayout) findViewById(R.id.store_porpertiy_ll);
        storePorpertiyTv = (TextView) findViewById(R.id.store_porpertiy_tv);
        provinceLl = (LinearLayout) findViewById(R.id.province_ll);
        provinceTv = (TextView) findViewById(R.id.province_tv);
        cityLl = (LinearLayout) findViewById(R.id.city_ll);
        cityTv = (TextView) findViewById(R.id.city_tv);
        countyLl = (LinearLayout) findViewById(R.id.county_ll);
        countyTv = (TextView) findViewById(R.id.county_tv);
        helpingGroupsLl = (LinearLayout) findViewById(R.id.helping_groups_ll);
        helpingGroupsTv = (TextView) findViewById(R.id.helping_groups_tv);
        addressStatusLl = (LinearLayout) findViewById(R.id.address_status_ll);
        addressStatusTv = (TextView) findViewById(R.id.address_status_tv);
        businessNumberLl = (LinearLayout) findViewById(R.id.business_number_ll);
        businessNumberTv = (TextView) findViewById(R.id.business_number_tv);

        loadInfo(getIntent().getStringExtra("id"));
        needLocation = getIntent().getIntExtra("needLocation", 1);

        mapObserver = new MapObserver(){
            @Override
            public void mapObserver(double bd_latitude, double bd_longitude, double latitude, double longitude, String address) {
                super.mapObserver(bd_latitude, bd_longitude, latitude, longitude, address);
                CustomerInfoActivity.this.bd_latitude = bd_latitude;
                CustomerInfoActivity.this.bd_longitude = bd_longitude;
                CustomerInfoActivity.this.latitude = latitude;
                CustomerInfoActivity.this.longitude = longitude;
                CustomerInfoActivity.this.address = address;

                AlertDialog.Builder builder = new AlertDialog.Builder(CustomerInfoActivity.this);
                builder.setMessage("是否上传经营户新的坐标");
                builder.setTitle("提示");
                builder.setPositiveButton("上传", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                        uploadLocation();
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();
            }
        };

        MapBroadcastReceiver.registerSwitchPluginReceiver(this);
        MapBroadcastReceiver.registerObserver(mapObserver);

        phoneTv.setOnClickListener(new ViewClickVibrate() {

            @Override
            public void onClick(View view) {
                super.onClick(view);
                if (phones != null) {
                    if (phones.length == 2) {
                        AlertDialog alertDialog = new AlertDialog.Builder(CustomerInfoActivity.this).setTitle("拨打电话").setItems(phones, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phones[which]));
                                if (ActivityCompat.checkSelfPermission(CustomerInfoActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    showToast("没有获得拨打电话权限，请在本 App 对应的设置中打开", CustomerInfoActivity.this);
                                    return;
                                }
                                startActivity(intent);
                            }
                        }).create();
                        alertDialog.show();
                    }else if(phones.length == 1){
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phones[0]));
                        if (ActivityCompat.checkSelfPermission(CustomerInfoActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            showToast("没有获得拨打电话权限，请在本 App 对应的设置中打开", CustomerInfoActivity.this);
                            return;
                        }
                        startActivity(intent);
                    }
                }
            }
        });

    }

    private void editDialog(final String tip,final String bean, final TextView textView){
        final EditText editText = new EditText(this);
        if(bean.equals("bean.birthYear") || bean.equals("bean.practitionerNum") || bean.equals("bean.contactphone")){
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            if(bean.equals("bean.birthYear")) editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
        }

        new AlertDialog.Builder(this).setTitle(tip).setView(editText).setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!StringUtility.isEmpty(editText.getText().toString())) {

                            if(bean.equals("bean.businessNo")) businessNumberTv.setText(editText.getText().toString());

                            if (bean.equals("bean.birthYear")) {
                                int year = new Integer(editText.getText().toString()).intValue();
                                if (year >= 2015 || year <= 1930) {
                                    showToast("请输入正确的年份", CustomerInfoActivity.this);
                                } else {
                                    textView.setText(editText.getText().toString());
                                    upload(bean);
                                }
                            } else {
                                textView.setText(editText.getText().toString());
                                upload(bean);
                            }
                        }
                    }
                }).setNegativeButton("取消", null).show();

    }

    private void uploadLocation(){
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.sending);
        pd.show();

        RequestParams params = new RequestParams();
        params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\""+Constant.MWB_Base_URL+"customerInfo!updateLocation.action?privilegeFlag=EDIT&bean.id="+operator.getId()+"&bean.latitude="+latitude+"&bean.longitude="+longitude+"&bean.bd_latitude="+bd_latitude+"&bean.bd_longitude="+bd_longitude+"\",\"type\":\"WebExecutor\",\"method\":\"GET\"},\"app\":\"1001\"}");

        HttpClient.post(Constant.EXEC, params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                pd.dismiss();

                if(StringUtility.isSuccess(response)){
                    try {

                        String messageSting = response.getString("message");
                        JSONObject message = new JSONObject(messageSting);

                        if (StringUtility.isSuccess(message)){
                            Constant.isRefreshJYH = true;

                        }else{
                            showToast(message.getString("message"), CustomerInfoActivity.this);
                        }
                    } catch (JSONException e) {
                        try {
                            showToast(response.getString("message"), CustomerInfoActivity.this);
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(CustomerInfoActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(CustomerInfoActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(CustomerInfoActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

        DecimalFormat df = new DecimalFormat("#.0000000000");

        RequestParams params1 = new RequestParams();
        params1.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"http://10.69.0.12/whicss-regie/licence/clientApply/saveClientPositionForKR.do?clientApply.clientCode="+operator.getLiceNo()+"&clientApply.longitude="+df.format(longitude)+"&clientApply.latitude="+df.format(latitude)+"\",\"type\":\"WebExecutor\"},\"app\":\"1004\"}");

        HttpClient.post(Constant.EXEC, params1, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                if(responseString.contains("\"success\":false")){
                    showToast(responseString, CustomerInfoActivity.this);
                }

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                if(responseString.contains("\"success\":false")){
                    showToast(responseString, CustomerInfoActivity.this);
                }else{
                }
            }
        });
    }

    private void loadInfo(String id){
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams params = new RequestParams();
        params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\""+ Constant.MWB_Base_URL+"customerInfo!findBean.action?privilegeFlag=VIEW&bean.id="+id+"\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

        HttpClient.post(Constant.EXEC, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                pd.dismiss();

                String messageSting = null;
                try {
                    if(StringUtility.isSuccess(response)) {
                        messageSting = response.getString("message");
                        JSONObject message = new JSONObject(messageSting);

                        if (StringUtility.isSuccess(message)) {

                            JSONObject object = message.getJSONObject("data");

                            JSONObject corpJson = StringUtility.notObjEmpty(object, "corp") ? object.getJSONObject("corp") : null;
                            JSONObject departmentJson = StringUtility.notObjEmpty(object, "department") ? object.getJSONObject("department") : null;
                            JSONObject communityJson = StringUtility.notObjEmpty(object, "community") ? object.getJSONObject("community") : null;
                            JSONObject streetJson = null;
                            if(communityJson !=null ){
                                streetJson = StringUtility.notObjEmpty(communityJson, "street") ? communityJson.getJSONObject("street") : null;
                            }

                            JSONObject postJson = StringUtility.notObjEmpty(object, "post") ? object.getJSONObject("post") : null;
                            JSONObject userJson = null;

                            if(postJson !=null) {
                                userJson = StringUtility.notObjEmpty(postJson, "user") ? postJson.getJSONObject("user") : null;
                            }
                            JSONObject bizStatusJson = StringUtility.notObjEmpty(object, "bizStatus") ? object.getJSONObject("bizStatus") : null;

                            operator = new Operator();
                            operator.setId(object.getString("id"));
                            if(StringUtility.notObjEmpty(object, "shopName"))operator.setShopName(object.getString("shopName"));
                            if(StringUtility.notObjEmpty(object, "shopAddress"))operator.setShopAddress(object.getString("shopAddress"));
                            if(StringUtility.notObjEmpty(object, "liceNo"))operator.setLiceNo(object.getString("liceNo"));
                            if(StringUtility.notObjEmpty(object, "chargerName"))operator.setChargerName(object.getString("chargerName"));
                            if(StringUtility.notObjEmpty(object, "orderPhone")) operator.setOrderPhone(object.getString("orderPhone"));
                            if(StringUtility.notObjEmpty(object, "idCard")) operator.setIdCard(object.getString("idCard"));
                            if(StringUtility.notObjEmpty(object, "businessNo")) operator.setBusinessNo(object.getString("businessNo"));
                            if(StringUtility.notObjEmpty(object, "deliverTime"))operator.setDeliverTime(object.getString("deliverTime"));
                            if(StringUtility.notObjEmpty(object, "avaiTime")) operator.setAvaiTime(object.getString("avaiTime"));
                            if(StringUtility.notObjEmpty(object, "customerManager"))operator.setCustomerManager(object.getString("customerManager"));
                            if(StringUtility.notObjEmpty(object, "grade"))operator.setGrade(object.getInt("grade")); else operator.setGrade(0);
                            if(StringUtility.notObjEmpty(object, "punishNum")) operator.setPunishNum(object.getString("punishNum"));
                            if(StringUtility.notObjEmpty(object, "real_operate_person")) operator.setReal_operate_person(object.getString("real_operate_person"));
                            if(StringUtility.notObjEmpty(object, "birthYear")) operator.setBirthYear(object.getString("birthYear"));
                            if(StringUtility.notObjEmpty(object, "business_type")) operator.setBusiness_type(object.getString("business_type"));
                            if(StringUtility.notObjEmpty(object, "addrProvince")) operator.setAddrProvince(object.getString("addrProvince"));
                            if(StringUtility.notObjEmpty(object, "addrCity")) operator.setAddrCity(object.getString("addrCity"));
                            if(StringUtility.notObjEmpty(object, "addrCounty")) operator.setAddrCounty(object.getString("addrCounty"));
                            if(StringUtility.notObjEmpty(object, "operating_life"))  operator.setOperating_life(object.getString("operating_life"));
                            if(StringUtility.notObjEmpty(object, "market_type")) operator.setMarket_type(object.getString("market_type"));
                            if(StringUtility.notObjEmpty(object, "porpertiy_code")) operator.setPorpertiy_code(object.getString("porpertiy_code"));
                            if(StringUtility.notObjEmpty(object, "store_nature")) operator.setStore_nature(object.getString("store_nature"));
                            if(StringUtility.notObjEmpty(object, "practitionerNum")) operator.setPractitionerNum(object.getString("practitionerNum"));
                            if(StringUtility.notObjEmpty(object, "contactphone")) operator.setContactphone(object.getString("contactphone"));
                            if(StringUtility.notObjEmpty(object, "support_groups"))operator.setSupport_groups(object.getString("support_groups"));

                            if(!StringUtility.isEmpty(object.getString("bd_latitude"))) operator.setBd_latitude(object.getDouble("bd_latitude")); else operator.setBd_latitude(0.0);
                            if(!StringUtility.isEmpty(object.getString("bd_latitude"))) operator.setBd_longitude(object.getDouble("bd_longitude")); else operator.setBd_longitude(0.0);


                            if(operator.getContactphone().length() > 0 && operator.getOrderPhone().length() > 0){
                                phones = new String[2];
                                phones[0] = operator.getContactphone();
                                phones[1] = operator.getOrderPhone();
                            }else if(operator.getContactphone().length() > 0 && operator.getOrderPhone().length() == 0){
                                phones = new String[1];
                                phones[0] = operator.getContactphone();
                            }else if(operator.getContactphone().length() == 0 && operator.getOrderPhone().length() > 0){
                                phones = new String[1];
                                phones[0] = operator.getOrderPhone();
                            }

                            Corp corp = new Corp();
                            if (corpJson != null) {
                                corp.setName(corpJson.getString("name"));
                                operator.setCorp(corp);
                            }

                            Department department = new Department();
                            if (departmentJson != null) {
                                department.setName(departmentJson.getString("name"));
                                operator.setDepartment(department);
                            }

                            Community community = new Community();
                            Community.StreetEntity streetEntity = new Community.StreetEntity();
                            if (communityJson != null && streetJson != null) {
                                streetEntity.setStreetName(streetJson.getString("streetName"));

                                community.setCommunityName(communityJson.getString("communityName"));

                            }
                            community.setStreet(streetEntity);
                            operator.setCommunity(community);

                            Post post = new Post();
                            Post.UserEntity userEntity = new Post.UserEntity();
                            if (userJson != null) {
                                userEntity.setName(userJson.getString("name"));

                            }
                            post.setUser(userEntity);
                            operator.setPost(post);

                            BizStatus bizStatus = new BizStatus();
                            if (bizStatusJson != null) {
                                bizStatus.setName(bizStatusJson.getString("name"));

                            }
                            operator.setBizStatus(bizStatus);

                            if(StringUtility.notObjEmpty(object, "addressState"))operator.setAddressState(object.getString("addressState"));

                            if(StringUtility.notObjEmpty(object, "mark"))operator.setMark(object.getInt("mark"));

                            mark = operator.getMark();

                            if(mark == 1){
                                staredTv.setBackgroundResource(R.drawable.star1);
                            }else {
                                staredTv.setBackgroundResource(R.drawable.star0);
                            }

                            if(!StringUtility.isEmpty(operator.getAddressState())) {
                                addressState = operator.getAddressState();

                                for (int i = 0; i < DZZTKey.length; i++) {
                                    if (addressState.equals(DZZTKey[i])) {
                                        addressStatusTv.setText(DZZTValue[i]);
                                    }
                                }
                            }

                            licenseTv.setText(operator.getLiceNo());
                            licenseBookerTv.setText(operator.getChargerName());
                            identityCardTv.setText(operator.getIdCard());
                            businessNoTv.setText(operator.getBusinessNo());
                            businessNumberTv.setText(operator.getBusinessNo());
                            releaseCardDateTv.setText(operator.getDeliverTime());
                            effectiveDateTv.setText(operator.getAvaiTime());
                            if(!StringUtility.isEmpty(operator.getCorp().getName())) regionalismTv.setText(operator.getCorp().getName());
                            if(!StringUtility.isEmpty(operator.getDepartment().getName())) departmentTv.setText(operator.getDepartment().getName());
                            if(!StringUtility.isEmpty(operator.getCommunity().getStreet().getStreetName())) streetTv.setText(operator.getCommunity().getStreet().getStreetName());
                            if(!StringUtility.isEmpty(operator.getCommunity().getCommunityName()))  communityTv.setText(operator.getCommunity().getCommunityName());
                            if(!StringUtility.isEmpty(operator.getPost().getUser().getName())) commonTv.setText(operator.getPost().getUser().getName());
                            managerTv.setText(operator.getCustomerManager());
                            if(!StringUtility.isEmpty(operator.getBizStatus().getName())) businessStatusTv.setText(operator.getBizStatus().getName());
                            punishNumTv.setText(StringUtility.isEmpty(operator.getPunishNum())?"0":operator.getPunishNum());
                            orderPhoneTv.setText(operator.getOrderPhone());

                            realNameTv.setText(StringUtility.isEmpty(operator.getReal_operate_person())?"":operator.getReal_operate_person());
                            birthdayTv.setText(StringUtility.isEmpty(operator.getBirthYear())?"":operator.getBirthYear());


                            if(!StringUtility.isEmpty(operator.getBusiness_type())){
                                business_type = operator.getBusiness_type();
                                for(int i=0;i<JYDDkey.length;i++){
                                    if(operator.getBusiness_type().equals(JYDDkey[i])){
                                        businessSectionTv.setText(JYDDValue[i]);
                                    }
                                }
                            }

                            connectPhoneTv.setText(StringUtility.isEmpty(operator.getContactphone())?"":operator.getContactphone());
                            businessYearTv.setText(StringUtility.isEmpty(operator.getOperating_life())?"":operator.getOperating_life());

                            if(!StringUtility.isEmpty(operator.getMarket_type())){
                                market_type = operator.getMarket_type();
                                for(int i=0;i<SCLXKey.length;i++){
                                    if(operator.getMarket_type().equals(SCLXKey[i])){
                                        businessTypeTv.setText(SCLXValue[i]);
                                    }
                                }
                            }

                            if(!StringUtility.isEmpty(operator.getPorpertiy_code())){
                                porpertiy_code = operator.getPorpertiy_code();
                                for(int i=0;i<JYYTKey.length;i++){
                                    if(operator.getPorpertiy_code().equals(JYYTKey[i])){
                                        porpertiyTv.setText(JYYTValue[i]);
                                    }
                                }
                            }

                            if(!StringUtility.isEmpty(operator.getStore_nature())){
                                store_nature = operator.getStore_nature();
                                for(int i=0;i<MDXZKey.length;i++){
                                    if(operator.getStore_nature().equals(MDXZKey[i])){
                                        storePorpertiyTv.setText(MDXZValue[i]);
                                    }
                                }
                            }

                            practitionerNumTv.setText(StringUtility.isEmpty(operator.getPractitionerNum())?"":operator.getPractitionerNum());

                            cityTv.setText(StringUtility.isEmpty(operator.getAddrCity())?"":operator.getAddrCity());
                            countyTv.setText(StringUtility.isEmpty(operator.getAddrCounty())?"":operator.getAddrCounty());

                            if(!StringUtility.isEmpty(operator.getSupport_groups())) {
                                support_groups = operator.getSupport_groups();
                                for (int i = 0; i < BFQTKey.length; i++) {
                                    if(operator.getSupport_groups().equals(BFQTKey[i])){
                                        provinceTv.setText(BFQTValue[i]);
                                    }
                                }
                            }

                            shopnameTv.setText(operator.getShopName());
                            addressTv.setText(operator.getShopAddress());

                            switch (operator.getGrade()){
                                case 1:
                                    trustLevelTv.setBackgroundResource(R.drawable.xx1);
                                    break;
                                case 2:
                                    trustLevelTv.setBackgroundResource(R.drawable.xx2);
                                    break;
                                case 3:
                                    trustLevelTv.setBackgroundResource(R.drawable.xx3);
                                    break;
                                case 4:
                                    trustLevelTv.setBackgroundResource(R.drawable.xx4);
                                    break;
                                case 5:
                                    trustLevelTv.setBackgroundResource(R.drawable.xx5);
                                    break;
                            }

                            addrProvince = operator.getAddrProvince();
                            addrCity = operator.getAddrCity();
                            addrCounty = operator.getAddrCounty();

                            provinceId = operator.getAddrProvince();
                            cityId = operator.getAddrCity();

                            if(!StringUtility.isEmpty(operator.getAddressState())) {
                                addressState = operator.getAddressState();

                                for (int i = 0; i < DZZTKey.length; i++) {
                                    if (addressState.equals(DZZTKey[i])) {
                                        addressStatusTv.setText(DZZTValue[i]);
                                    }
                                }
                            }

                            loadAddrProvince(true);
                            if(!StringUtility.isEmpty(provinceId) ) loadAddrCity(true);
                            if(!StringUtility.isEmpty(cityId)) loadAddrCounty(true);

                        }else{
                            showToast(message.getString("message"), CustomerInfoActivity.this);
                        }
                    }else{
                        showToast(response.getString("message"), CustomerInfoActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(CustomerInfoActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(CustomerInfoActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(CustomerInfoActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void chickDialog(final String tip,final String bean, final String[] key, final String[] value,final TextView textView){

        AlertDialog alertDialog = new AlertDialog.Builder(CustomerInfoActivity.this).setTitle(tip).setItems(value, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (tip.equals("身份证住址所在省份")) {
                    provinceId = key[which];
                } else if (tip.equals("身份证住址所在城市")) {
                    cityId = key[which];
                }
                if (bean.equals("bean.business_type")) business_type = key[which];
                if (bean.equals("bean.market_type")) market_type = key[which];
                if (bean.equals("bean.porpertiy_code")) porpertiy_code = key[which];
                if (bean.equals("bean.store_nature")) store_nature = key[which];
                if (bean.equals("bean.support_groups")) support_groups = key[which];
                if (bean.equals("bean.addrProvince")) addrProvince = key[which];
                if (bean.equals("bean.addrCity")) addrCity = key[which];
                if (bean.equals("bean.addrCounty")) addrCounty = key[which];
                if(bean.equals("bean.addressState")) addressState = key[which];

                upload(bean);
                textView.setText(value[which]);
            }
        }).create();
        alertDialog.show();

    }

    private void upload(String bean){
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams params = new RequestParams();
        params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MWB_Base_URL + "customerInfo!mobileSave.action?privilegeFlag=VIEW"+getBuffer()+"&bean.id="+operator.getId()+"&bean.liceNo="+operator.getLiceNo()+"\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

        HttpClient.post(Constant.EXEC, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                pd.dismiss();

                try {
                    if(StringUtility.isSuccess(response)) {
                        String messageSting = response.getString("message");
                        JSONObject message = new JSONObject(messageSting);

                        if (StringUtility.isSuccess(message)) {
                            Constant.isRefreshJYH = true;

                            Toast.makeText(CustomerInfoActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                        }else{
                            showToast(message.getString("message"), CustomerInfoActivity.this);
                        }
                    }else{
                        showToast(response.getString("message"), CustomerInfoActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(CustomerInfoActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(CustomerInfoActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(CustomerInfoActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private String getBuffer(){
        StringBuffer buffer = new StringBuffer("");

        if(!StringUtility.isEmpty(realNameTv.getText().toString()))buffer.append("&bean.real_operate_person="+realNameTv.getText().toString());
        if(!StringUtility.isEmpty(birthdayTv.getText().toString()))buffer.append("&bean.birthYear="+birthdayTv.getText().toString());
        if(!StringUtility.isEmpty(business_type))buffer.append("&bean.business_type="+business_type);
        if(!StringUtility.isEmpty(connectPhoneTv.getText().toString()))buffer.append("&bean.contactphone="+connectPhoneTv.getText().toString());
        if(!StringUtility.isEmpty(market_type))buffer.append("&bean.market_type="+market_type);
        if(!StringUtility.isEmpty(porpertiy_code))buffer.append("&bean.porpertiy_code="+porpertiy_code);
        if(!StringUtility.isEmpty(store_nature))buffer.append("&bean.store_nature="+store_nature);
        if(!StringUtility.isEmpty(practitionerNumTv.getText().toString()))buffer.append("&bean.practitionerNum="+practitionerNumTv.getText().toString());
        if(!StringUtility.isEmpty(support_groups))buffer.append("&bean.support_groups="+support_groups);
        if(!StringUtility.isEmpty(addrProvince))buffer.append("&bean.addrProvince="+addrProvince);
        if(!StringUtility.isEmpty(addrCity))buffer.append("&bean.addrCity="+addrCity);
        if(!StringUtility.isEmpty(addrCounty))buffer.append("&bean.addrCounty="+addrCounty);
        if(!StringUtility.isEmpty(addressState)) buffer.append("&bean.addressState="+addressState);
        if(!StringUtility.isEmpty(businessNumberTv.getText().toString()))buffer.append("&bean.businessNo="+businessNumberTv.getText().toString());
        buffer.append("&bean.mark="+mark);

        return buffer.toString();
    }

    private void loadAddrProvince(final boolean isStart){
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        HttpClient.get(Constant.EXEC+"?data=%7B%22postHandler%22:%5B%5D,%22preHandler%22:%5B%5D,%22executor%22:%7B%22url%22:%22"+Constant.CISS_Base_URL+"hisCustomer!findProvince.action%3fprivilegeFlag=VIEW%22,%22type%22:%22WebExecutor%22%7D,%22app%22:%221002%22%7D",
                null, new JsonHttpResponseHandler(){

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        System.out.println("response: " + response.toString());
                        pd.dismiss();

                        String messageSting = null;
                        try {
                            if(StringUtility.isSuccess(response)) {
                                messageSting = response.getString("message");
                                JSONObject message = new JSONObject(messageSting);

                                if (StringUtility.isSuccess(message)) {
                                    JSONArray data = message.getJSONArray("data");

                                    ProvinceKey = new String[data.length()];
                                    ProvinceValue = new String[data.length()];

                                    for (int i = 0; i < data.length(); i++) {
                                        JSONObject object = data.getJSONObject(i);

                                        ProvinceKey[i] = object.getString("code");
                                        ProvinceValue[i] = object.getString("name");

                                        if (isStart) {
                                            if (!StringUtility.isEmpty(operator.getAddrProvince())) {
                                                if (operator.getAddrProvince().equals(object.getString("code"))) {
                                                    provinceTv.setText(object.getString("name"));
                                                }
                                            }
                                        }
                                    }

                                    if (!isStart)
                                        chickDialog("身份证住址所在省份", "bean.addrProvince", ProvinceKey, ProvinceValue, provinceTv);
                                }else{
                                    showToast(message.getString("message"), CustomerInfoActivity.this);
                                }
                            }else{
                                showToast(response.getString("message"), CustomerInfoActivity.this);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        pd.dismiss();
                        showNetworkError(CustomerInfoActivity.this);
                        super.onFailure(statusCode, headers, responseString, throwable);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        pd.dismiss();
                        showNetworkError(CustomerInfoActivity.this);
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        pd.dismiss();
                        showNetworkError(CustomerInfoActivity.this);
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }
                });
    }

    private void loadAddrCity(final boolean isStart){

        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        HttpClient.get(Constant.EXEC+"?data=%7B%22postHandler%22:%5B%5D,%22preHandler%22:%5B%5D,%22executor%22:%7B%22url%22:%22"+"hisCustomer!findCity.action%3fprivilegeFlag=VIEW%26_query.provinceId="+provinceId+"%22,%22type%22:%22WebExecutor%22%7D,%22app%22:%221002%22%7D",
                null, new JsonHttpResponseHandler(){

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        System.out.println("response: " + response.toString());
                        pd.dismiss();

                        String messageSting = null;
                        try {
                            if(StringUtility.isSuccess(response)) {
                                messageSting = response.getString("message");
                                JSONObject message = new JSONObject(messageSting);

                                if (StringUtility.isSuccess(message)) {
                                    JSONArray data = message.getJSONArray("data");

                                    CityKey = new String[data.length()];
                                    CityValue = new String[data.length()];

                                    System.out.println("data: " + data.toString());
                                    for (int i = 0; i < data.length(); i++) {
                                        JSONObject object = data.getJSONObject(i);
                                        CityKey[i] = object.getString("code");
                                        CityValue[i] = object.getString("name");

                                        if (isStart) {
                                            if (!StringUtility.isEmpty(operator.getAddrCity())) {
                                                if (operator.getAddrCity().equals(object.getString("code"))) {
                                                    cityTv.setText(object.getString("name"));
                                                }
                                            }
                                        }
                                    }

                                    if (!isStart)
                                        chickDialog("身份证住址所在城市", "bean.addrCity", CityKey, CityValue, cityTv);
                                }else{
                                    showToast(message.getString("message"), CustomerInfoActivity.this);
                                }
                            }else{
                                showToast(response.getString("message"), CustomerInfoActivity.this);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        pd.dismiss();
                        showNetworkError(CustomerInfoActivity.this);
                        super.onFailure(statusCode, headers, responseString, throwable);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        pd.dismiss();
                        showNetworkError(CustomerInfoActivity.this);
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        pd.dismiss();
                        showNetworkError(CustomerInfoActivity.this);
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }
                });
    }

    private void loadAddrCounty(final boolean isStart){
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        HttpClient.get(Constant.EXEC+"?data=%7B%22postHandler%22:%5B%5D,%22preHandler%22:%5B%5D,%22executor%22:%7B%22url%22:%22"+Constant.CISS_Base_URL+"hisCustomer!findCity.action%3fprivilegeFlag=VIEW%26_query.cityId="+cityId+"%22,%22type%22:%22WebExecutor%22%7D,%22app%22:%221002%22%7D",
                null, new JsonHttpResponseHandler(){

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        System.out.println("response: " + response.toString());
                        pd.dismiss();

                        String messageSting = null;
                        try {
                            if(StringUtility.isSuccess(response)) {
                                messageSting = response.getString("message");
                                JSONObject message = new JSONObject(messageSting);

                                if (StringUtility.isSuccess(message)) {
                                    JSONArray data = message.getJSONArray("data");

                                    CountyKey = new String[data.length()];
                                    CountyValue = new String[data.length()];

                                    System.out.println("data: " + data.toString());
                                    for (int i = 0; i < data.length(); i++) {
                                        JSONObject object = data.getJSONObject(i);
                                        CountyKey[i] = object.getString("code");
                                        CountyValue[i] = object.getString("name");

                                        if (isStart) {
                                            if (!StringUtility.isEmpty(operator.getAddrCounty())) {
                                                if (operator.getAddrCounty().equals(object.getString("code"))) {
                                                    countyTv.setText(object.getString("name"));
                                                }
                                            }
                                        }
                                    }

                                    if (!isStart)
                                        chickDialog("身份证住址所在县", "bean.addrCounty", CountyKey, CountyValue, countyTv);
                                }else{
                                    showToast(message.getString("message"), CustomerInfoActivity.this);
                                }
                            }else{
                                showToast(response.getString("message"), CustomerInfoActivity.this);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        pd.dismiss();
                        showNetworkError(CustomerInfoActivity.this);
                        super.onFailure(statusCode, headers, responseString, throwable);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        pd.dismiss();
                        showNetworkError(CustomerInfoActivity.this);
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        pd.dismiss();
                        showNetworkError(CustomerInfoActivity.this);
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_info2);
        setToolbarTitle("经营户信息");
        showBack();

        assignViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MapBroadcastReceiver.unRegisterSwitchPluginReceiver(this);
        MapBroadcastReceiver.removeRegisterObserver(mapObserver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_map){
            Intent intent = new Intent(this, MapTestActivity.class);
            intent.putExtra("latitude", operator.getBd_latitude());
            intent.putExtra("longitude", operator.getBd_longitude());
            intent.putExtra("needLocation", needLocation);
            startActivity(intent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
