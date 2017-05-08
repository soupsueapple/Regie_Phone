package com.keertech.regie_phone.Activity.XZFW.RCFW;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.IdRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.keertech.core.json.Json;
import com.keertech.regie_phone.BaseActivity;
import com.keertech.regie_phone.Constant.Constant;
import com.keertech.regie_phone.Listener.ViewClickVibrate;
import com.keertech.regie_phone.Models.CustomerInfo;
import com.keertech.regie_phone.Models.Image;
import com.keertech.regie_phone.Models.MarketInspect;
import com.keertech.regie_phone.Models.MarketInspectCig;
import com.keertech.regie_phone.Network.HttpClient;
import com.keertech.regie_phone.Observer.ScreenObserver;
import com.keertech.regie_phone.R;
import com.keertech.regie_phone.Utility.DateTimeUtil;
import com.keertech.regie_phone.Utility.KeerAlertDialog;
import com.keertech.regie_phone.Utility.StringUtility;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.keertech.regie_phone.R.id.shopname_tv;

/**
 * Created by soup on 2017/5/5.
 */

public class ServiceInfoAcitvity extends BaseActivity{

    RecyclerAdapter recyclerAdapter = new RecyclerAdapter();

    private MarketInspect marketInspect;
    private MarketInspectCig currentMarketInspectCig;

    private ArrayList<MarketInspectCig> marketInspectCigs = new ArrayList<>();

    private final static int TAKE_ORIGINAL_PIC = 127;

    private double latitude;
    private double longitude;

    private String[] wheres = new String[]{"市管组", "管理所"};

    private String[] Brandkey = new String[]{"1", "42020015", "42020035", "42020051", "42020053", "42020075", "42020050", "42020033", "42020037", "42020054", "42020060", "42020012", "42020013", "42020036", "42020065", "42020006", "42020011", "42020036", "42020039", "42020052", "42020005", "42020066", "XX6", "42020009", "XX7", "42020001", "XX8", "42020018", "42020028", "42020067", "XX10", "42010079", "42010024", "42010015", "42010007", "42010020", "42010005", "53010017", "53010043", "33010009", "53210033", "31020002", "31020004", "44020006", "53220002", "53220001", "51010027", "51010014", "31010002" , "42020035", "42020060", "42020015", "43010003", "43010010" , "43010001" , "43010002" , "43010039", "43500001", "53070005", "53250001", "32010016", "36010013", "36500008", "42020030", "43010024", "43010031", "43010032", "43500028", "33020012", "33500017", "88030002", "88060037", "88130003", "88200003", "88220002", "88310001", "88480002", "53220021", "53250009", "88010012", "88010008", "31030004", "32010003", "33020015", "33500016", "88220003", "88230004", "88240002", "88310002", "88480001", "88570002", "33010013", "33080004", "88150004", "42090004", "88680150", "88680047", "88680198", "88680197", "88680255", "88680287", "42020075", "42020076", "88680042","88680065","88680079", "88680077", "88680076", "88680075", "88680093", "88680300", "88680299", "88680329", "88680331", "88680333", "88680343", "0"};

    private String[] BrandValue = new String[]{"搜索", "黄鹤楼(软1916)", "黄鹤楼(硬1916)", "黄鹤楼(硬感恩)", "黄鹤楼(硬为了谁)", "黄鹤楼(红景天)(罚)", "黄鹤楼(硬品道)", "黄鹤楼(硬漫天游)", "黄鹤楼（软漫天游)", "黄鹤楼(硬问道)", "黄鹤楼(硬梯杷)", "黄鹤楼(软珍品)", "黄鹤楼(硬珍品)", "黄鹤楼（软论道)", "黄鹤楼（硬春秋)", "黄鹤楼(软满天星)", "黄鹤楼(硬满天星)", "黄鹤楼(软论道)", "黄鹤楼(硬论道)", "黄鹤楼(硬论道)", "黄鹤楼(软红)", "黄鹤楼(软雅韵)", "黄鹤楼(硬雅韵)", "黄鹤楼(硬红)", "黄鹤楼(硬鸿运)", "黄鹤楼(软蓝)", "黄鹤楼(软鸿运)", "黄鹤楼(硬金砂)", "黄鹤楼(软金砂)", "黄鹤楼(天下名楼)", "黄鹤楼(鄂尔多斯)", "红金龙(晓楼)", "红金龙(硬红火之舞)", "红金龙(硬火之舞)", "红金龙(软精品)", "红金龙(硬神州腾龙)", "红金龙(硬红)", "云烟(紫)", "云烟(软紫)", "利群(新版)", "红塔山(硬经典)", "中华(硬)", "中华(软)", "双喜(软经典)", "玉溪(软)", "玉溪(硬)", "娇子(时代阳光)", "娇子(X)", "中南海(8mg)", "黄鹤楼(硬1916)", "黄鹤楼(硬梯杷)", "黄鹤楼(软1916)", "白沙(精品)", "白沙(特制精品)", "白沙(软)" , "白沙(硬)", "白沙(精品二代)","芙蓉王(硬)", "茶花(94mm)", "阿诗玛(硬)", "南京(九五)", "金圣(盛世典藏)", "赣(硬蓝)", "黄鹤楼(硬磨砂出口)", "利事(硬蓝马)", "白沙(和天下)", "白沙(和钻石)", "芙蓉王(钻石)", "雄狮(硬)", "大红鹰(英文)", "硬盒520", "555(硬醇)", "硬盒PEEL(橙味王)", "DJMIX(草莓味)", "软盒摩尔", "硬盒BALCK DKVIL", "BLACKDEUIL", "玉溪(磨沙专供出口)", "阿诗玛(专供出口)", "硬盒白万宝路", "硬盒红万宝路", "牡丹(软)", "南京(绿)", "雄狮(薄荷)", "大红鹰(软新品)", "硬盒More", "硬盒登喜路", "JEWELS（橙色）", "软盒BLACK DEVIL（黑魔）", "硬盒BLACK DEVIL（黑魔）", "硬盒RAISON", "利群(蓝)", "双叶", "硬盒希尔顿", "软盒游泳", "黄鹤楼(软蓝专供出口)", "BLACKDEVII(黑魔)", "黄鹤楼（全家福）", "黄鹤楼（硬同心）", "ESSE（蓝）", "黄鹤楼（好运）", "黄鹤楼(铁盒1916)(假)(罚)", "黄鹤楼(铁盒漫天游)(假)(罚)", "大重九", "黄鹤楼1916（硬白皮）", "游泳", "圆球", "大公鸡", "永光", "星火", "黄鹤楼（红红火火）", "黄鹤楼（中国梦）", "黄鹤楼（天骄圣地）", "黄鹤楼（知音）", "黄鹤楼（峡谷情）", "钻石（荷花）", "其它"};

    private HashMap<String, String> brandMap = new HashMap<>();

    JSONObject data;

    private String customerId = "";

    private TextView shopnameTv;
    private TextView licenseTv;
    private TextView address;
    private TextView nameTv;
    private TextView phoneTv;
    private TextView third_abnormal_tv;
    private TextView licenseAbnormalTv;
    private TextView businessAbnormalTv;
    private TextView apceAbnormalTv;
    private TextView random_abnormal_tv;
    private TextView takeEvidenceTv;
    private Chronometer chronometer;
    private TextView addAbnormalInfoTv;
    private RecyclerView abnormalInfoRecyclerview;
    private RadioGroup notShowLicenseRg;
    private RadioButton notShowLicenseYesRb;
    private RadioButton notShowLicenseNoRb;
    private RadioGroup notMatchingRg;
    private RadioButton notMatchingYesRb;
    private RadioButton notMatchingNoRb;
    private TextView notMatchingNoteTv;
    private RadioGroup addressChangedRg;
    private RadioButton addressChangedYesRb;
    private RadioButton addressChangedNoRb;
    private TextView addressChangedNoteTv;
    private RadioGroup publicityRuleRg;
    private RadioButton publicityRuleYesRb;
    private RadioButton publicityRuleNoRb;
    private EditText otherAbnormalInfoEt;
    private EditText apcd_abnormal_info_et;
    private TextView outshopTv;
    private TextView markCheckTargetTv;
    private LinearLayout apcd_ll;

    String inspectWeekId  = "";

    private String liceNo = "";

    private String zjAbnormal = "";
    private String jyAbnormal = "";
    private String apcdAbnormal = "";
    private String dsfAbnormal = "";
    private String lotteryAbnormal = "";

    private int apcdAbnormalFlag = 0;
    private int zjAbnormalFlag = 0;
    private int dsfAbnormalFlag = 0;
    private int jyAbnormalFlag = 0;
    private int lotteryAbnormalFlag = 0;

    boolean isOutShop;

    private final static int MTZ = 99;
    private final static int ZJZ = 98;

    private boolean unlocation = false;

    private ArrayList<Image> mtzUris;
    private ArrayList<Image> zjzUris;

    private int onFailure = 0;

    private ScreenObserver mScreenObserver;

    private void assignViews(Bundle savedInstanceState) {
        shopnameTv = (TextView) findViewById(shopname_tv);
        licenseTv = (TextView) findViewById(R.id.license_tv);
        address = (TextView) findViewById(R.id.address);
        nameTv = (TextView) findViewById(R.id.name_tv);
        phoneTv = (TextView) findViewById(R.id.phone_tv);
        third_abnormal_tv = (TextView) findViewById(R.id.third_abnormal_tv);
        licenseAbnormalTv = (TextView) findViewById(R.id.license_abnormal_tv);
        businessAbnormalTv = (TextView) findViewById(R.id.business_abnormal_tv);
        apceAbnormalTv = (TextView) findViewById(R.id.apce_abnormal_tv);
        random_abnormal_tv = (TextView) findViewById(R.id.random_abnormal_tv);
        takeEvidenceTv = (TextView) findViewById(R.id.take_evidence_tv);
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        addAbnormalInfoTv = (TextView) findViewById(R.id.add_abnormal_info_tv);
        abnormalInfoRecyclerview = (RecyclerView) findViewById(R.id.abnormal_info_recyclerview);
        notShowLicenseRg = (RadioGroup) findViewById(R.id.not_show_license_rg);
        notShowLicenseYesRb = (RadioButton) findViewById(R.id.not_show_license_yes_rb);
        notShowLicenseNoRb = (RadioButton) findViewById(R.id.not_show_license_no_rb);
        notMatchingRg = (RadioGroup) findViewById(R.id.not_matching_rg);
        notMatchingYesRb = (RadioButton) findViewById(R.id.not_matching_yes_rb);
        notMatchingNoRb = (RadioButton) findViewById(R.id.not_matching_no_rb);
        notMatchingNoteTv = (TextView) findViewById(R.id.not_matching_note_tv);
        addressChangedRg = (RadioGroup) findViewById(R.id.address_changed_rg);
        addressChangedYesRb = (RadioButton) findViewById(R.id.address_changed_yes_rb);
        addressChangedNoRb = (RadioButton) findViewById(R.id.address_changed_no_rb);
        addressChangedNoteTv = (TextView) findViewById(R.id.address_changed_note_tv);
        publicityRuleRg = (RadioGroup) findViewById(R.id.publicity_rule_rg);
        publicityRuleYesRb = (RadioButton) findViewById(R.id.publicity_rule_yes_rb);
        publicityRuleNoRb = (RadioButton) findViewById(R.id.publicity_rule_no_rb);
        otherAbnormalInfoEt = (EditText) findViewById(R.id.other_abnormal_info_et);
        apcd_abnormal_info_et = (EditText) findViewById(R.id.apcd_abnormal_info_et);
        outshopTv = (TextView) findViewById(R.id.outshop_tv);
        markCheckTargetTv = (TextView) findViewById(R.id.mark_check_target_tv);
        apcd_ll = (LinearLayout) findViewById(R.id.apcd_ll);

        abnormalInfoRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        abnormalInfoRecyclerview.setAdapter(recyclerAdapter);

        addAbnormalInfoTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                customDialog();
            }
        });

        takeEvidenceTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                Intent intent = new Intent(ServiceInfoAcitvity.this, ImageActivity.class);
                intent.putExtra("isSingle", false);
                intent.putExtra("liceNo", liceNo);
                if (zjzUris != null) intent.putExtra("zjzUris", zjzUris);
                startActivityForResult(intent, ZJZ);
            }
        });

        third_abnormal_tv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                if(!StringUtility.isEmpty(dsfAbnormal)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ServiceInfoAcitvity.this);
                    builder.setMessage(dsfAbnormal);
                    builder.setTitle("第三方调查异常");
                    builder.setPositiveButton("确认", null);
                    builder.create().show();
                }
            }
        });

        licenseAbnormalTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                if(!StringUtility.isEmpty(zjAbnormal)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ServiceInfoAcitvity.this);
                    builder.setMessage(zjAbnormal);
                    builder.setTitle("证件异常");
                    builder.setPositiveButton("确认", null);
                    builder.create().show();
                }
            }
        });

        businessAbnormalTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                if(!StringUtility.isEmpty(jyAbnormal)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ServiceInfoAcitvity.this);
                    builder.setMessage(jyAbnormal);
                    builder.setTitle("经营异常");
                    builder.setPositiveButton("确认", null);
                    builder.create().show();
                }
            }
        });

        apceAbnormalTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                if(!StringUtility.isEmpty(apcdAbnormal)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ServiceInfoAcitvity.this);
                    builder.setMessage(apcdAbnormal);
                    builder.setTitle("APCD异常");
                    builder.setPositiveButton("确认", null);
                    builder.create().show();
                }
            }
        });

        random_abnormal_tv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                if(!StringUtility.isEmpty(lotteryAbnormal)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ServiceInfoAcitvity.this);
                    builder.setMessage(lotteryAbnormal);
                    builder.setTitle("随机检查异常");
                    builder.setPositiveButton("确认", null);
                    builder.create().show();
                }
            }
        });

        getToolbar().setNavigationOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                backOutShop();
            }
        });

        outshopTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);

                if(marketInspectCigs == null ) marketInspectCigs = new ArrayList<>();

                if(marketInspectCigs.size() > 0 && marketInspect.getIsPropagandaLR() == -1){
                    showToast("请选择，是否宣传法律法规",ServiceInfoAcitvity.this);
                }else if(apcdAbnormalFlag == 1 && apcd_abnormal_info_et.getText().toString().length() == 0){
                    showToast("请输入APCD异常反馈",ServiceInfoAcitvity.this);
                }else if ((System.currentTimeMillis() - marketInspect.getInDate().getTime()) <= 60 * 1000){
                    showToast("服务时长必需大于1分钟",ServiceInfoAcitvity.this);
                }else {
                    if (unlocation) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ServiceInfoAcitvity.this);
                        builder.setMessage("无店铺坐标无法出店");
                        builder.setTitle("提示");
                        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ServiceInfoAcitvity.this.finish();
                            }
                        });
                        builder.create().show();
                    } else {

                        if (mtzUris.size() == 0) {
                            onFailure = 0;
                            marketInspect.setCigList(marketInspectCigs);
                            marketInspect.setOutDate(new Date());
                            if (zjzUris.size() > 0) {
                                uploadAllImage(zjzUris, false);
                            } else {
                                outShop();
                            }
                        } else if (mtzUris.size() > 0) {
                            onFailure = 0;
                            marketInspect.setCigList(marketInspectCigs);
                            marketInspect.setOutDate(new Date());
                            uploadAllImage(mtzUris, true);
                        }
                    }
                }

            }
        });

        markCheckTargetTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);

                AlertDialog alertDialog = new AlertDialog.Builder(ServiceInfoAcitvity.this).setTitle("列为检查对象").setItems(wheres, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        marketInspect.setIsIntoCheck(1);
                        marketInspect.setCheckLevel(which + 1);

                        final EditText editText = new EditText(ServiceInfoAcitvity.this);

                        new AlertDialog.Builder(ServiceInfoAcitvity.this).setTitle("原因").setView(editText).setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (!StringUtility.isEmpty(editText.getText().toString())) {
                                            marketInspect.setIntoCheckReason(editText.getText().toString());
                                        }
                                    }
                                }).setNegativeButton("取消", null).show();
                    }
                }).create();
                alertDialog.show();

            }
        });

        inspectWeekId = getIntent().getStringExtra("inspectWeekId");

        liceNo = getIntent().getStringExtra("liceNo");

        customerId = getIntent().getStringExtra("customerId");

        zjAbnormal = getIntent().getStringExtra("zjAbnormal");
        jyAbnormal = getIntent().getStringExtra("jyAbnormal");
        apcdAbnormal = getIntent().getStringExtra("apcdAbnormal");
        dsfAbnormal = getIntent().getStringExtra("dsfAbnormal");
        lotteryAbnormal = getIntent().getStringExtra("lotteryAbnormal");

        if (null != savedInstanceState){
            marketInspect = (MarketInspect) savedInstanceState.getSerializable("obj");
            marketInspectCigs = (ArrayList<MarketInspectCig>) savedInstanceState.getSerializable("list");
            currentMarketInspectCig = (MarketInspectCig) savedInstanceState.getSerializable("cobj");
        }else{

            if(StringUtility.getSharedPreferencesForBoolean(this, "isOutShopDraft", "draft")) {
                customerId = StringUtility.getSharedPreferencesForInt(this, "draftKey", "draftKey") + "";

                String s = StringUtility.getSharedPreferencesForString(this, "outShop", customerId);
                marketInspect = s.length() > 0 ? Json.fromJson(MarketInspect.class, s) : null;

                zjAbnormalFlag = StringUtility.getSharedPreferencesForInt(this, "zjAbnormalFlag", customerId);
                jyAbnormalFlag = StringUtility.getSharedPreferencesForInt(this, "jyAbnormalFlag", customerId);
                apcdAbnormalFlag = StringUtility.getSharedPreferencesForInt(this, "apcdAbnormalFlag", customerId);
                dsfAbnormalFlag = StringUtility.getSharedPreferencesForInt(this, "dsfAbnormalFlag", customerId);
                lotteryAbnormalFlag = StringUtility.getSharedPreferencesForInt(this, "lotteryAbnormalFlag", customerId);
                inspectWeekId = StringUtility.getSharedPreferencesForString(this, "inspectWeekId", inspectWeekId);
            }

            mtzUris = new ArrayList<>();
            zjzUris = new ArrayList<>();

            if(marketInspect == null){

                marketInspectCigs = new ArrayList<>();

                marketInspect = new MarketInspect();

                CustomerInfo customerInfo = new CustomerInfo();
                marketInspect.setCustomer(customerInfo);
                marketInspect.setIsLightCard(new Integer(0));
                marketInspect.setIsRzConform(new Integer(0));
                marketInspect.setIsAddressChange(new Integer(0));
                marketInspect.setIllegalBusiness(new Integer(0));

                marketInspect.setInDate(new Date());

                marketInspect.getCustomer().setId(new Integer(customerId));

                latitude = getIntent().getDoubleExtra("latitude", 0.0);
                longitude = getIntent().getDoubleExtra("longitude", 0.0);
                marketInspect.setLatitude_bd(latitude);
                marketInspect.setLongitude_bd(longitude);

                zjAbnormalFlag = getIntent().getIntExtra("zjAbnormalFlag", 0);
                jyAbnormalFlag = getIntent().getIntExtra("jyAbnormalFlag", 0);
                apcdAbnormalFlag = getIntent().getIntExtra("apcdAbnormalFlag", 0);
                dsfAbnormalFlag = getIntent().getIntExtra("dsfAbnormalFlag", 0);
                lotteryAbnormalFlag = getIntent().getIntExtra("lotteryAbnormalFlag", 0);

            }else{
                if(marketInspect.getCustomer() != null ){
                    marketInspect.getCustomer().setId(new Integer(customerId));
                }else{
                    CustomerInfo customerInfo = new CustomerInfo();
                    marketInspect.setCustomer(customerInfo);
                    marketInspect.getCustomer().setId(new Integer(customerId));
                }

                latitude = marketInspect.getLatitude_bd();
                longitude = marketInspect.getLongitude_bd();

                marketInspectCigs = (ArrayList<MarketInspectCig>) marketInspect.getCigList();

                if(marketInspect.getDoorPhotos().length() > 0){

                    Image image = new Image();
                    image.setName(marketInspect.getDoorPhotos());

                    File sdcard = Environment.getExternalStorageDirectory();
                    String path = sdcard.getPath()+File.separator+ Constant.Base_path;
                    String fileName = path + File.separator + image.getName();
                    File myCaptureFile = new File(fileName);

                    image.setUrl(myCaptureFile.getAbsolutePath());

                    image.setSuccess(false);

                    mtzUris.add(image);
                }

                if(marketInspect.getEvidencePhotos().length() > 0){
                    String[] names = marketInspect.getEvidencePhotos().split(";");

                    for(String name : names){
                        Image image = new Image();
                        image.setName(name);

                        File sdcard = Environment.getExternalStorageDirectory();
                        String path = sdcard.getPath()+File.separator+Constant.Base_path;
                        String fileName = path + File.separator + image.getName();
                        File myCaptureFile = new File(fileName);

                        image.setUrl(myCaptureFile.getAbsolutePath());

                        image.setSuccess(false);

                        zjzUris.add(image);
                    }
                }

                if(marketInspect.getApcdFeedback().length() > 0) apcd_abnormal_info_et.setText(marketInspect.getAddressChange());

                if(marketInspect.getOtherAbnormal().length() > 0) otherAbnormalInfoEt.setText(marketInspect.getOtherAbnormal());

                notMatchingNoteTv.setText(marketInspect.getRzConform());
                addressChangedNoteTv.setText(marketInspect.getAddressChange());
            }

            currentMarketInspectCig = new MarketInspectCig();

        }

        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
            }
        });

        if(marketInspect.getInDate() != null) {
            chronometer.setBase(SystemClock.elapsedRealtime() - (System.currentTimeMillis() - marketInspect.getInDate().getTime()));
            chronometer.start();
        }

        loadCustomerData();

        if(marketInspect.getIsLightCard() != null) {
            if (marketInspect.getIsLightCard() == 1) {
                notShowLicenseYesRb.setChecked(true);
                notShowLicenseNoRb.setChecked(false);
            } else {
                notShowLicenseYesRb.setChecked(false);
                notShowLicenseNoRb.setChecked(true);
            }
        }else{
            marketInspect.setIsLightCard(new Integer(0));
            notShowLicenseYesRb.setChecked(false);
            notShowLicenseNoRb.setChecked(true);
        }

        if(marketInspect.getIsRzConform() == 1) {
            notMatchingYesRb.setChecked(true);
            notMatchingNoRb.setChecked(false);
        }else{
            notMatchingYesRb.setChecked(false);
            notMatchingNoRb.setChecked(true);
        }

        if(marketInspect.getIsAddressChange() == 1) {
            addressChangedYesRb.setChecked(true);
            addressChangedNoRb.setChecked(false);
        }else{
            addressChangedYesRb.setChecked(false);
            addressChangedNoRb.setChecked(true);
        }

        if(marketInspect.getIsPropagandaLR() == 1) {
            publicityRuleYesRb.setChecked(true);
            publicityRuleNoRb.setChecked(false);
        }else{
            publicityRuleYesRb.setChecked(false);
            publicityRuleNoRb.setChecked(true);
        }

        notShowLicenseRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int id) {
                switch (id){
                    case R.id.not_show_license_yes_rb:
                        marketInspect.setIsLightCard(new Integer(1));
                        break;
                    case R.id.not_show_license_no_rb:
                        marketInspect.setIsLightCard(new Integer(0));
                        break;
                }
            }
        });

        notMatchingRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int id) {
                switch (id){
                    case R.id.not_matching_yes_rb:
                        marketInspect.setIsRzConform(new Integer(1));
                        break;
                    case R.id.not_matching_no_rb:
                        marketInspect.setIsRzConform(new Integer(0));
                        break;
                }

                editDialog("实际经营人", "rzConform_Remark", notMatchingNoteTv);
            }
        });

        addressChangedRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int id) {
                switch (id){
                    case R.id.address_changed_yes_rb:
                        marketInspect.setIsAddressChange(new Integer(1));
                        break;
                    case R.id.address_changed_no_rb:
                        marketInspect.setIsAddressChange(new Integer(0));
                        break;
                }
                editDialog("经营地址", "addressChange_Remark", addressChangedNoteTv);
            }
        });

        publicityRuleRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int id) {
                switch (id){
                    case R.id.publicity_rule_yes_rb:
                        marketInspect.setIsPropagandaLR(new Integer(1));
                        break;
                    case R.id.publicity_rule_no_rb:
                        marketInspect.setIsPropagandaLR(new Integer(0));
                        break;
                }
            }
        });

        if(zjAbnormalFlag == 1) licenseAbnormalTv.setBackgroundResource(R.drawable.zjyc); else licenseAbnormalTv.setBackgroundResource(R.drawable.zjwyc);
        if(jyAbnormalFlag == 1) businessAbnormalTv.setBackgroundResource(R.drawable.jyyc); else businessAbnormalTv.setBackgroundResource(R.drawable.jywyc);
        if(apcdAbnormalFlag == 1) apceAbnormalTv.setBackgroundResource(R.drawable.apcdyc); else apceAbnormalTv.setBackgroundResource(R.drawable.apcdwyc);
        if(dsfAbnormalFlag == 1) third_abnormal_tv.setBackgroundResource(R.drawable.dsfyc); else third_abnormal_tv.setBackgroundResource(R.drawable.dsfwyc);
        if(lotteryAbnormalFlag == 1) random_abnormal_tv.setBackgroundResource(R.drawable.sjyc); else random_abnormal_tv.setBackgroundResource(R.drawable.sjwyc);

        if(apcdAbnormalFlag == 1) apcd_ll.setVisibility(View.VISIBLE); else apcd_ll.setVisibility(View.GONE);

        recyclerAdapter.notifyDataSetChanged();

        mScreenObserver = new ScreenObserver(this);
        mScreenObserver.requestScreenStateUpdate(new ScreenObserver.ScreenStateListener() {
            @Override
            public void onScreenOn() {
                doSomethingOnScreenOn();
            }

            @Override
            public void onScreenOff() {
                doSomethingOnScreenOff();
            }
        });

    }

    private void doSomethingOnScreenOn() {

    }

    private void doSomethingOnScreenOff() {
        saveDraft();
    }

    private void editDialog(final String tip, final String bean, final TextView textView) {
        final EditText editText = new EditText(this);

        new AlertDialog.Builder(this).setTitle(tip).setView(editText).setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!StringUtility.isEmpty(editText.getText().toString())) {
                            textView.setText(editText.getText().toString());
                        }
                    }
                }).setNegativeButton("取消", null).show();

    }

    private void saveDraft(){

        if (!isOutShop){
            marketInspect.setCigList(marketInspectCigs);

            StringBuffer mtzbuffer = new StringBuffer("");

            if(mtzUris != null) {
                for (int i = 0; i < mtzUris.size(); i++) {
                    Image image = mtzUris.get(i);
                    if (i != mtzUris.size() - 1) {
                        mtzbuffer.append(image.getName() + ";");
                    } else {
                        mtzbuffer.append(image.getName());
                    }
                }
            }

            marketInspect.setDoorPhotos(mtzbuffer.toString());

            StringBuffer zjzbuffer = new StringBuffer("");

            if(zjzUris != null) {
                for (int i = 0; i < zjzUris.size(); i++) {
                    Image image = zjzUris.get(i);
                    if (i != zjzUris.size() - 1) {
                        zjzbuffer.append(image.getName() + ";");
                    } else {
                        zjzbuffer.append(image.getName());
                    }
                }
            }

            marketInspect.setEvidencePhotos(zjzbuffer.toString());

            if (notMatchingNoteTv.getText().toString().length() > 0) marketInspect.setRzConform(notMatchingNoteTv.getText().toString());

            if (addressChangedNoteTv.getText().toString().length() > 0) marketInspect.setAddressChange(addressChangedNoteTv.getText().toString());

            if (apcd_abnormal_info_et.getText().toString().length() > 0) marketInspect.setApcdFeedback(apcd_abnormal_info_et.getText().toString());

            if (otherAbnormalInfoEt.getText().toString().length() > 0) marketInspect.setOtherAbnormal(otherAbnormalInfoEt.getText().toString());

            StringUtility.putSharedPreferences(this, "outShop", marketInspect.getCustomer().getId() + "", Json.toJson(marketInspect));

            StringUtility.putSharedPreferences(this, "draftKey", "draftKey", marketInspect.getCustomer().getId());

            StringUtility.putSharedPreferences(this, "isOutShopDraft","draft",true);

            StringUtility.putSharedPreferences(this, "zjAbnormalFlag",marketInspect.getCustomer().getId()+"", zjAbnormalFlag);
            StringUtility.putSharedPreferences(this, "jyAbnormalFlag",marketInspect.getCustomer().getId()+"", jyAbnormalFlag);
            StringUtility.putSharedPreferences(this, "apcdAbnormalFlag",marketInspect.getCustomer().getId()+"", apcdAbnormalFlag);
            StringUtility.putSharedPreferences(this, "dsfAbnormalFlag",marketInspect.getCustomer().getId()+"", dsfAbnormalFlag);

            StringUtility.putSharedPreferences(this, "inspectWeekId",inspectWeekId, inspectWeekId);

            Constant.isRefreshXCJL = true;
        }

    }

    private void uploadAllImage(ArrayList<Image> Uris, boolean isSingle) {
        if (Uris.size() > 0) {
            KeerAlertDialog pd = showKeerAlertDialog(R.string.sending);

            int update = 0;

            ArrayList<Image> temp = new ArrayList<>();

            for (int i = 0; i < Uris.size(); i++) {
                Image image = Uris.get(i);
                if (!image.isSuccess()) {
                    image.setIndex(i);
                    update += 1;
                    temp.add(image);
                }
            }

            for (int i = 0; i < temp.size(); i++) {
                Image image = temp.get(i);
                File file = new File(image.getUrl());
                String name = image.getName();
                uploadImage(file, name, i, temp.size(), isSingle, Uris, image.getIndex());
            }

            if (update == 0) pd.dismiss();
        }
    }

    private void uploadImage(File file, String filename, final int f, final int imageSize, final boolean isSingle, final ArrayList<Image> Uris, final int index) {

        final KeerAlertDialog pd = showKeerAlertDialog(R.string.sending);

        String action = isSingle ? "marketInspect!uploadDoorP.action" : "marketInspect!uploadEvidenceP.action";

        RequestParams params = new RequestParams();
        params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MWB_Base_URL + action + "\"," +
                "\"parameter\":{\"upload\":\"$uploadFile\"}," +
                "\"type\":\"WebExecutor\",\"method\":\"POST\"},\"app\":\"1001\"}");

        try {
            params.put("uploadFile", file);
            params.put("uploadFileName", filename);
            params.put("uploadContentType", "image/jpeg");

            HttpClient.post(Constant.EXEC, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    pd.dismiss();

                    try {
                        String messageSting = response.getString("message");
                        JSONObject message = new JSONObject(messageSting);
                        if (StringUtility.isSuccess(message)) {

                            Image image = Uris.get(index);
                            image.setSuccess(true);
                            image.setName(message.getString("newFileName"));

                            if (f == imageSize - 1) {
                                if (onFailure == 0) {
                                    if (isSingle) {
                                        if (zjzUris.size() > 0) {
                                            uploadAllImage(zjzUris, false);
                                        } else {
                                            outShop();
                                        }
                                    } else outShop();
                                }
                            }

                        } else {
                            onFailure += 1;
                            if (f == imageSize - 1) {
                                pd.dismiss();
                                if (onFailure > 0) {
                                    pd.dismiss();
                                    showToast("有" + onFailure + "张照片上传失败,请重试", ServiceInfoAcitvity.this);
                                    onFailure = 0;
                                }
                            }
                        }
                    } catch (JSONException e) {
                        pd.dismiss();
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    showNetworkError(ServiceInfoAcitvity.this);
                    super.onFailure(statusCode, headers, responseString, throwable);
                    onFailure += 1;
                    if (f == imageSize) {
                        pd.dismiss();
                        if (onFailure > 0) {
                            showToast("有" + onFailure + "张照片上传失败,请重试", ServiceInfoAcitvity.this);
                            onFailure = 0;
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    onFailure += 1;
                    pd.dismiss();
                    if (f == imageSize) {

                        if (onFailure > 0) {
                            showToast("有" + onFailure + "张照片上传失败,请重试", ServiceInfoAcitvity.this);
                            onFailure = 0;
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    onFailure += 1;
                    pd.dismiss();
                    if (f == imageSize) {

                        if (onFailure > 0) {
                            showToast("有" + onFailure + "张照片上传失败,请重试", ServiceInfoAcitvity.this);
                            onFailure = 0;
                        }
                    }
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void loadCustomerData(){
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loginning);
        pd.show();

        RequestParams params = new RequestParams();
        params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\""+Constant.MWB_Base_URL+"marketInspect!intoShop.action?privilegeFlag=VIEW&_query.customerId="+customerId+"\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

        HttpClient.post(Constant.EXEC, params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                pd.dismiss();

                try {

                    String messageSting = response.getString("message");

                    JSONObject message = new JSONObject(messageSting);

                    if (StringUtility.isSuccess(message)) {

                        data = message.getJSONObject("data");

                        JSONObject customer = data.getJSONObject("customer");

                        String liceNo = customer.getString("liceNo");
                        String shopName = customer.getString("shopName");
                        String chargerName = customer.getString("chargerName");
                        String shopAddress = customer.getString("shopAddress");
                        String contactphone = customer.getString("contactphone");

                        shopnameTv.setText( shopName);
                        address.setText(shopAddress);
                        licenseTv.setText(liceNo);
                        nameTv.setText(chargerName);
                        phoneTv.setText(StringUtility.isEmpty(contactphone) ? "" : contactphone);

                    } else {
                        isOutShop = true;

                        AlertDialog.Builder builder = new AlertDialog.Builder(ServiceInfoAcitvity.this);
                        builder.setMessage(message.getString("message"));
                        builder.setTitle("提示");
                        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Constant.isRefreshXCJL = false;
                                Constant.isRefreshRCXC = true;

                                StringUtility.putSharedPreferences(ServiceInfoAcitvity.this, "outShop",customerId + "", "");
                                StringUtility.putSharedPreferences(ServiceInfoAcitvity.this, "draftKey", "draftKey",0);
                                StringUtility.putSharedPreferences(ServiceInfoAcitvity.this, "isOutShopDraft","draft",false);

                                StringUtility.putSharedPreferences(ServiceInfoAcitvity.this, "zjAbnormalFlag",customerId+"", 0);
                                StringUtility.putSharedPreferences(ServiceInfoAcitvity.this, "jyAbnormalFlag",customerId+"", 0);
                                StringUtility.putSharedPreferences(ServiceInfoAcitvity.this, "apcdAbnormalFlag",customerId+"", 0);
                                StringUtility.putSharedPreferences(ServiceInfoAcitvity.this, "dsfAbnormalFlag",customerId+"", 0);
                                StringUtility.putSharedPreferences(ServiceInfoAcitvity.this, "lotteryAbnormalFlag",lotteryAbnormalFlag+"", 0);
                                ServiceInfoAcitvity.this.finish();
                            }
                        });
                        builder.create().show();
                    }

                } catch (JSONException e) {
                    isOutShop = true;
                    unlocation = true;
                    AlertDialog.Builder builder = new AlertDialog.Builder(ServiceInfoAcitvity.this);
                    builder.setMessage("无店铺坐标无法进店");
                    builder.setTitle("提示");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ServiceInfoAcitvity.this.finish();
                        }
                    });
                    builder.create().show();
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(ServiceInfoAcitvity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(ServiceInfoAcitvity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(ServiceInfoAcitvity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    /**
     * 新增异常
     */
    private void customDialog() {
        if (currentMarketInspectCig == null) currentMarketInspectCig = new MarketInspectCig();

        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.jd_dilog, (ViewGroup) findViewById(R.id.dialog));
        layout.setFocusable(true);
        layout.setFocusableInTouchMode(true);

        final CheckBox jd_dilog_gkbm = (CheckBox) layout.findViewById(R.id.jd_dilog_gkbm);
        final CheckBox jd_dilog_ajsm = (CheckBox) layout.findViewById(R.id.jd_dilog_ajsm);
        final CheckBox jd_dilog_xsjy = (CheckBox) layout.findViewById(R.id.jd_dilog_xsjy);

        LinearLayout jd_dilog_pg = (LinearLayout) layout.findViewById(R.id.jd_dilog_pg);

        final TextView jd_dilog_tv1 = (TextView) layout.findViewById(R.id.jd_dilog_tv1);
        final EditText jd_dilog_ed2 = (EditText) layout.findViewById(R.id.jd_dilog_ed2);

        if (currentMarketInspectCig.getType() != null) {
            switch (currentMarketInspectCig.getType()) {
                case 1:
                    jd_dilog_gkbm.setChecked(true);
                    jd_dilog_ajsm.setChecked(false);
                    jd_dilog_xsjy.setChecked(false);
                    break;
                case 2:
                    jd_dilog_gkbm.setChecked(false);
                    jd_dilog_ajsm.setChecked(true);
                    jd_dilog_xsjy.setChecked(false);
                    break;
                case 3:
                    jd_dilog_gkbm.setChecked(false);
                    jd_dilog_ajsm.setChecked(false);
                    jd_dilog_xsjy.setChecked(true);
                    break;
            }
        }

        if (!StringUtility.isEmpty(currentMarketInspectCig.getCigName()))
            jd_dilog_tv1.setText(currentMarketInspectCig.getCigName());
        if (currentMarketInspectCig.getAmount() != null)
            jd_dilog_ed2.setText(currentMarketInspectCig.getAmount());

        final AlertDialog pd = new AlertDialog.Builder(this).setTitle("新增经营异常信息").setView(layout).setPositiveButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (currentMarketInspectCig.getType() == null) {
                    showToast("请选择异常类型", ServiceInfoAcitvity.this);
                } else if (currentMarketInspectCig.getCigName() == null) {
                    showToast("请选择品规", ServiceInfoAcitvity.this);
                } else if (jd_dilog_ed2.getText().toString().length() == 0) {
                    showToast("请输入数量", ServiceInfoAcitvity.this);
                } else {
                    currentMarketInspectCig.setAmount(jd_dilog_ed2.getText().toString());
                    marketInspectCigs.add(currentMarketInspectCig);
                    currentMarketInspectCig = null;

                    recyclerAdapter.notifyDataSetChanged();
                }
            }
        }).show();

        jd_dilog_gkbm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                currentMarketInspectCig.setType(1);
                if (isChecked) {
                    jd_dilog_gkbm.setChecked(true);
                    jd_dilog_ajsm.setChecked(false);
                    jd_dilog_xsjy.setChecked(false);
                }
            }
        });

        jd_dilog_ajsm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    currentMarketInspectCig.setType(2);
                    jd_dilog_gkbm.setChecked(false);
                    jd_dilog_ajsm.setChecked(true);
                    jd_dilog_xsjy.setChecked(false);
                }
            }
        });

        jd_dilog_xsjy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    currentMarketInspectCig.setType(3);
                    jd_dilog_gkbm.setChecked(false);
                    jd_dilog_ajsm.setChecked(false);
                    jd_dilog_xsjy.setChecked(true);
                }
            }
        });

        jd_dilog_pg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.dismiss();
                chickDialog(Brandkey, BrandValue, jd_dilog_tv1);
            }
        });


    }

    private void chickDialog(final String[] key, final String[] value, final TextView textView) {

        AlertDialog alertDialog = new AlertDialog.Builder(ServiceInfoAcitvity.this).setTitle("请选择").setItems(value, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == key.length - 1) {
                    edlog();
                }else if (which == 0) {
                    searchBrand();
                } else if (which != key.length - 1 && which != 0) {
                    currentMarketInspectCig.setCigCode(key[which]);
                    currentMarketInspectCig.setCigName(value[which]);
                    customDialog();
                }
            }
        }).create();
        alertDialog.show();

    }

    private void edlog(){
        View layout = LayoutInflater.from(this).inflate(R.layout.custom_edit_dialog2, (ViewGroup)findViewById(R.id.custom_dialog));
        final EditText username = (EditText)layout.findViewById(R.id.dialog_username_ed);
        final EditText password = (EditText)layout.findViewById(R.id.dialog_password_ed);

        new AlertDialog.Builder(ServiceInfoAcitvity.this).setTitle("提示").setView(layout).setPositiveButton("确定", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (username.getText().toString().length() == 0) {
                    ServiceInfoAcitvity.this.showToast("请输入卷烟代码",ServiceInfoAcitvity.this);
                } else if (password.getText().toString().length() == 0) {
                    ServiceInfoAcitvity.this.showToast("请输入卷烟品牌",ServiceInfoAcitvity.this);
                } else {
                    currentMarketInspectCig.setCigCode(username.getText().toString());
                    currentMarketInspectCig.setCigName(password.getText().toString());
                    customDialog();
                }
            }
        }).setNegativeButton("取消", null).show();
    }

    private void searchBrand(){
        final EditText editText = new EditText(this);

        new AlertDialog.Builder(this).setTitle("搜索卷烟").setView(editText).setPositiveButton("搜索",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ArrayList<HashMap<String, String>> temp = new ArrayList<HashMap<String, String>>();

                        for (Map.Entry<String, String> entry : brandMap.entrySet()) {
                            if (entry.getKey().contains(editText.getText().toString()) || entry.getValue().contains(editText.getText().toString())) {
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put(entry.getKey(), entry.getValue());

                                temp.add(map);
                            }
                        }

                        String[] searchBrandKey = new String[temp.size()];
                        String[] searchBrandValue = new String[temp.size()];

                        for (int i = 0; i < temp.size(); i++) {
                            HashMap<String, String> map = temp.get(i);
                            for (Map.Entry<String, String> entry : map.entrySet()) {
                                searchBrandKey[i] = entry.getKey();
                                searchBrandValue[i] = entry.getValue();
                            }
                        }

                        chickDialogSearchBrand(searchBrandKey, searchBrandValue, null);

                    }
                }).setNegativeButton("取消", null).show();
    }

    private void chickDialogSearchBrand(final String[] key, final String[] value, final TextView textView) {

        AlertDialog alertDialog = new AlertDialog.Builder(ServiceInfoAcitvity.this).setTitle("请选择").setItems(value, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                currentMarketInspectCig.setCigCode(key[which]);
                currentMarketInspectCig.setCigName(value[which]);
                customDialog();
            }
        }).create();
        alertDialog.show();

    }

    private void outShop() {

        StringBuffer mtzbuffer = new StringBuffer("");

        for (int i = 0; i < mtzUris.size(); i++) {
            Image image = mtzUris.get(i);
            if (image.isSuccess()) {
                if (i != mtzUris.size() - 1) {
                    mtzbuffer.append(image.getName() + ";");
                } else {
                    mtzbuffer.append(image.getName());
                }
            }
        }

        marketInspect.setDoorPhotos(mtzbuffer.toString());

        StringBuffer zjzbuffer = new StringBuffer("");

        for (int i = 0; i < zjzUris.size(); i++) {
            Image image = zjzUris.get(i);
            if (image.isSuccess()) {
                if (i != zjzUris.size() - 1) {
                    zjzbuffer.append(image.getName() + ";");
                } else {
                    zjzbuffer.append(image.getName());
                }
            }
        }

        marketInspect.setEvidencePhotos(zjzbuffer.toString());

        StringBuffer buffer = new StringBuffer();

        buffer.append("&bean.customer.id=" + marketInspect.getCustomer().getId());
        buffer.append("&bean.isLightCard=" + marketInspect.getIsLightCard().intValue());
        buffer.append("&bean.isRzConform=" + marketInspect.getIsRzConform().intValue());
        buffer.append("&bean.isAddressChange=" + marketInspect.getIsAddressChange().intValue());
        buffer.append("&bean.inDate=" + DateTimeUtil.getFormatDate(marketInspect.getInDate()!=null?marketInspect.getInDate():new Date(), DateTimeUtil.TIME_FORMAT));
        buffer.append("&bean.outDate=" + DateTimeUtil.getFormatDate(new Date(), DateTimeUtil.TIME_FORMAT));
        buffer.append("&bean.longitude_bd=" + marketInspect.getLongitude_bd());
        buffer.append("&bean.latitude_bd=" + marketInspect.getLatitude_bd());
        if(marketInspect.getIsPropagandaLR() == -1) marketInspect.setIsPropagandaLR(0);
        buffer.append("&bean.isPropagandaLR=" + marketInspect.getIsPropagandaLR());
        buffer.append("&bean.isIntoCheck=" + marketInspect.getIsIntoCheck());
        buffer.append("&bean.intoCheckReason=" + marketInspect.getIntoCheckReason());
        buffer.append("&bean.inspectWeekId=" + inspectWeekId);
        if( apcd_abnormal_info_et.getText().toString().length() > 0)buffer.append("&bean.apcdFeedback=" + apcd_abnormal_info_et.getText().toString());
        if( otherAbnormalInfoEt.getText().toString().length() > 0)buffer.append("&bean.otherAbnormal=" + otherAbnormalInfoEt.getText().toString());

        if (!StringUtility.isEmpty(marketInspect.getDoorPhotos()))
            buffer.append("&bean.doorPhotos=" + marketInspect.getDoorPhotos());
        if (!StringUtility.isEmpty(marketInspect.getEvidencePhotos()))
            buffer.append("&bean.evidencePhotos=" + marketInspect.getEvidencePhotos());

        if (notMatchingNoteTv.getText().toString().length() > 0)
            buffer.append("&bean.rzConform_Remark=" + notMatchingNoteTv.getText().toString());
        if (addressChangedNoteTv.getText().toString().length() > 0)
            buffer.append("&bean.addressChange_Remark=" + addressChangedNoteTv.getText().toString());

        if(marketInspect.getCheckLevel() > 0) buffer.append("&bean.checkLevel=" + marketInspect.getCheckLevel());

        if (marketInspect.getCigList().size() > 0) {
            for (int i = 0; i < marketInspect.getCigList().size(); i++) {
                MarketInspectCig marketInspectCig = marketInspect.getCigList().get(i);
                buffer.append("&bean.cigList[" + i + "].type=" + marketInspectCig.getType() + "&bean.cigList[" + i + "].cigName=" + marketInspectCig.getCigName() + "&bean.cigList[" + i + "].cigCode=" + marketInspectCig.getCigCode() + "&bean.cigList[" + i + "].amount=" + marketInspectCig.getAmount());
            }
        }

        final KeerAlertDialog pd = showKeerAlertDialog(R.string.sending);

        String base = "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MWB_Base_URL + "marketInspect!outShop.action?privilegeFlag=EDIT" + buffer + "\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}";

        RequestParams params = new RequestParams();
        params.add("data", base);

        HttpClient.post(Constant.EXEC,
                params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        pd.dismiss();
                        System.out.println("response: " + response.toString());
                        isOutShop = true;
                        try {

                            if (StringUtility.isSuccess(response)) {

                                String messageSting = response.getString("message");

                                JSONObject message = new JSONObject(messageSting);

                                if (!StringUtility.isSuccess(message)) {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(ServiceInfoAcitvity.this);
                                    builder.setMessage(message.getString("message"));
                                    builder.setTitle("提示");
                                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            ServiceInfoAcitvity.this.finish();
                                        }
                                    });
                                    builder.create().show();
                                } else {

                                    Constant.isRefreshXCJL = true;
                                    Constant.isRefreshRCXC = true;

                                    StringUtility.putSharedPreferences(ServiceInfoAcitvity.this, "outShop", marketInspect.getCustomer().getId() + "", "");
                                    StringUtility.putSharedPreferences(ServiceInfoAcitvity.this, "draftKey", "draftKey",0);
                                    StringUtility.putSharedPreferences(ServiceInfoAcitvity.this, "isOutShopDraft","draft",false);

                                    StringUtility.putSharedPreferences(ServiceInfoAcitvity.this, "zjAbnormalFlag",marketInspect.getCustomer().getId()+"", 0);
                                    StringUtility.putSharedPreferences(ServiceInfoAcitvity.this, "jyAbnormalFlag",marketInspect.getCustomer().getId()+"", 0);
                                    StringUtility.putSharedPreferences(ServiceInfoAcitvity.this, "apcdAbnormalFlag",marketInspect.getCustomer().getId()+"", 0);
                                    StringUtility.putSharedPreferences(ServiceInfoAcitvity.this, "dsfAbnormalFlag",marketInspect.getCustomer().getId()+"", 0);
                                    finish();
                                }
                            } else {
                                showToast(response.getString("message"), ServiceInfoAcitvity.this);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        pd.dismiss();
                        showNetworkError(ServiceInfoAcitvity.this);
                        super.onFailure(statusCode, headers, responseString, throwable);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        pd.dismiss();
                        showNetworkError(ServiceInfoAcitvity.this);
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        pd.dismiss();
                        showNetworkError(ServiceInfoAcitvity.this);
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }
                });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("list", marketInspectCigs);
        outState.putSerializable("obj", marketInspect);
        outState.putSerializable("cobj", currentMarketInspectCig);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case MTZ:
                if (resultCode == RESULT_OK) {
                    mtzUris = (ArrayList<Image>) data.getSerializableExtra("mtzUris");
                }
                break;
            case ZJZ:
                if (resultCode == RESULT_OK) {
                    zjzUris = (ArrayList<Image>) data.getSerializableExtra("zjzUris");
                }
                break;
        }
    }

    private void backOutShop(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("是否出店");
        builder.setTitle("提示");
        builder.setPositiveButton("出店", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(marketInspectCigs.size() > 0 && marketInspect.getIsPropagandaLR() == -1){
                    showToast("请选择，是否宣传法律法规",ServiceInfoAcitvity.this);
                }else if(apcdAbnormalFlag == 1 && apcd_abnormal_info_et.getText().toString().length() == 0){
                    showToast("请输入APCD异常反馈",ServiceInfoAcitvity.this);
                }else if ((System.currentTimeMillis() - marketInspect.getInDate().getTime()) <= 60 * 1000){
                    showToast("服务时长必需大于1分钟",ServiceInfoAcitvity.this);
                }else {
                    if (unlocation) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ServiceInfoAcitvity.this);
                        builder.setMessage("无店铺坐标无法出店");
                        builder.setTitle("提示");
                        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ServiceInfoAcitvity.this.finish();
                            }
                        });
                        builder.create().show();
                    } else {

                        if (mtzUris.size() == 0) {
                            onFailure = 0;
                            marketInspect.setCigList(marketInspectCigs);
                            marketInspect.setOutDate(new Date());
                            if (zjzUris.size() > 0) {
                                uploadAllImage(zjzUris, false);
                            } else {
                                outShop();
                            }
                        } else if (mtzUris.size() > 0) {
                            onFailure = 0;
                            marketInspect.setCigList(marketInspectCigs);
                            marketInspect.setOutDate(new Date());
                            uploadAllImage(mtzUris, true);
                        }
                    }
                }
            }
        });
        builder.setNegativeButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveDraft();
                finish();
            }
        });
        builder.create().show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            backOutShop();

        }

        return false;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.base_info_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.action_customer){
//            Intent intent = new Intent(this, OperatorInfoActivity.class);
//            intent.putExtra("id",customerId);
//            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_info);
        setToolbarTitle("服务");
        showBack();

        assignViews(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mScreenObserver.stopScreenStateUpdate();
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>{

        @Override
        public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerHolder holder = new RecyclerHolder(LayoutInflater.from(ServiceInfoAcitvity.this).inflate(R.layout.activity_xzfw_rcfw_service_info_abnormal_info_recyclerview, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerHolder holder, int position) {
            MarketInspectCig object = marketInspectCigs.get(position);

            String str = "";

            switch (object.getType()) {
                case 1:
                    str = "公开摆卖";
                    break;
                case 2:
                    str = "暗中售卖";
                    break;
                case 3:
                    str = "销售假烟";
                    break;
            }

            holder.abnormalTypeTv.setText(str);
            holder.varietyTv.setText(object.getCigName());
            holder.numberTv.setText(object.getAmount());
        }

        @Override
        public int getItemCount() {
            return marketInspectCigs.size();
        }

        class RecyclerHolder extends RecyclerView.ViewHolder{

            private TextView abnormalTypeTv;
            private TextView varietyTv;
            private TextView numberTv;

            private void holderAssignViews(View itemView) {
                abnormalTypeTv = (TextView) itemView.findViewById(R.id.abnormal_type_tv);
                varietyTv = (TextView) itemView.findViewById(R.id.variety_tv);
                numberTv = (TextView) itemView.findViewById(R.id.number_tv);
            }


            public RecyclerHolder(View itemView) {
                super(itemView);
                this.holderAssignViews(itemView);
            }
        }
    }
}
