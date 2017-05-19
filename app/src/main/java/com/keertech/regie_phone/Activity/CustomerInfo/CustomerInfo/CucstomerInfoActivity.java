package com.keertech.regie_phone.Activity.CustomerInfo.CustomerInfo;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.keertech.regie_phone.BaseActivity;
import com.keertech.regie_phone.Models.Operator;
import com.keertech.regie_phone.R;

import java.util.Calendar;

/**
 * Created by soup on 2017/5/18.
 */

public class CucstomerInfoActivity extends BaseActivity{

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

    String[] JYDDkey = new String[]{"103101","103102","103103","103104","103105","103106","103107"};
    String[] JYDDValue = new String[]{"繁华商圈","主要街道","背街小巷","居民区","旅客集散地","学院区","其它"};

    String[] SCLXKey = new String[]{"102101","102102","102103"};
    String[] SCLXValue = new String[]{"城市","城镇","农村"};

    String[] JYYTKey = new String[]{"104101","104102","104103","104104","104105","104106","104107","104108"};
    String[] JYYTValue = new String[]{"食杂店","便利店","超市","商场","名烟名酒名茶","娱乐服务","其它","行业自办店"};

    String[] MDXZKey = new String[]{"101101","101102"};
    String[] MDXZValue = new String[]{"自有","租赁"};

    String[] BFQTKey = new String[]{"107101","107102","107103","107104","107105"};
    String[] BFQTValue = new String[]{"残疾人","军烈属","失独家庭","孤寡老人","其它"};

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
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_info);
        setToolbarTitle("经营户信息");
        showBack();

        assignViews();
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
