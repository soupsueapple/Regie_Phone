package com.keertech.regie_phone.Activity.ProblemChecking;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.keertech.regie_phone.BaseActivity;
import com.keertech.regie_phone.Constant.Constant;
import com.keertech.regie_phone.Listener.ViewClickVibrate;
import com.keertech.regie_phone.Models.MarketInspectCig;
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

import java.util.ArrayList;

import static com.keertech.regie_phone.R.id.recycler_view;

/**
 * Created by soup on 2017/5/11.
 */

public class CheckingActivity extends BaseActivity{

    private MarketInspectCig currentMarketInspectCig;

    private ArrayList<MarketInspectCig> marketInspectCigs = new ArrayList<>();

    RecyclerAdapter recyclerAdapter = new RecyclerAdapter();

    TJRecyclerAdapter tJRecyclerAdapter = new TJRecyclerAdapter();

    private ArrayList<String> tjItems = new ArrayList<>();

    private int id = 0;

    private int status = 0;

    private JSONObject data;

    private String[] measuresStr = {"法律法规宣传", "出具《先行教育通知书》", "出具《责令整改通知书》", "涉案物品先行登记保存通知书"};

    private String[] Brandkey = new String[]{"42020015", "42020035", "42020051", "42020053", "42020075", "42020050", "42020033", "42020037", "42020054", "42020060", "42020012", "42020013", "42020036", "42020065", "42020006", "42020011", "42020036", "42020039", "42020052", "42020005", "42020066", "XX6", "42020009", "XX7", "42020001", "XX8", "42020018", "42020028", "42020067", "XX10", "42010079", "42010024", "42010015", "42010007", "42010020", "42010005", "53010017", "53010043", "33010009", "53210033", "31020002", "31020004", "44020006", "53220002", "53220001", "51010027", "51010014", "31010002" , "42020035", "42020060", "42020015", "43010003", "43010010" , "43010001" , "43010002" , "43010039", "43500001", "53070005", "53250001", "32010016", "36010013", "36500008", "42020030", "43010024", "43010031", "43010032", "43500028", "33020012", "33500017", "88030002", "88060037", "88130003", "88200003", "88220002", "88310001", "88480002", "53220021", "53250009", "88010012", "88010008", "31030004", "32010003", "33020015", "33500016", "88220003", "88230004", "88240002", "88310002", "88480001", "88570002", "33010013", "33080004", "88150004", "42090004", "88680150", "88680047", "88680198", "88680197", "88680255", "88680287", "42020075", "42020076", "88680042","88680065","88680079", "88680077", "88680076", "88680075", "88680093", "88680300", "88680299", "88680329", "88680331", "88680333", "88680343", "0"};

    private String[] BrandValue = new String[]{"黄鹤楼(软1916)", "黄鹤楼(硬1916)", "黄鹤楼(硬感恩)", "黄鹤楼(硬为了谁)", "黄鹤楼(红景天)(罚)", "黄鹤楼(硬品道)", "黄鹤楼(硬漫天游)", "黄鹤楼（软漫天游)", "黄鹤楼(硬问道)", "黄鹤楼(硬梯杷)", "黄鹤楼(软珍品)", "黄鹤楼(硬珍品)", "黄鹤楼（软论道)", "黄鹤楼（硬春秋)", "黄鹤楼(软满天星)", "黄鹤楼(硬满天星)", "黄鹤楼(软论道)", "黄鹤楼(硬论道)", "黄鹤楼(硬论道)", "黄鹤楼(软红)", "黄鹤楼(软雅韵)", "黄鹤楼(硬雅韵)", "黄鹤楼(硬红)", "黄鹤楼(硬鸿运)", "黄鹤楼(软蓝)", "黄鹤楼(软鸿运)", "黄鹤楼(硬金砂)", "黄鹤楼(软金砂)", "黄鹤楼(天下名楼)", "黄鹤楼(鄂尔多斯)", "红金龙(晓楼)", "红金龙(硬红火之舞)", "红金龙(硬火之舞)", "红金龙(软精品)", "红金龙(硬神州腾龙)", "红金龙(硬红)", "云烟(紫)", "云烟(软紫)", "利群(新版)", "红塔山(硬经典)", "中华(硬)", "中华(软)", "双喜(软经典)", "玉溪(软)", "玉溪(硬)", "娇子(时代阳光)", "娇子(X)", "中南海(8mg)", "黄鹤楼(硬1916)", "黄鹤楼(硬梯杷)", "黄鹤楼(软1916)", "白沙(精品)", "白沙(特制精品)", "白沙(软)" , "白沙(硬)", "白沙(精品二代)","芙蓉王(硬)", "茶花(94mm)", "阿诗玛(硬)", "南京(九五)", "金圣(盛世典藏)", "赣(硬蓝)", "黄鹤楼(硬磨砂出口)", "利事(硬蓝马)", "白沙(和天下)", "白沙(和钻石)", "芙蓉王(钻石)", "雄狮(硬)", "大红鹰(英文)", "硬盒520", "555(硬醇)", "硬盒PEEL(橙味王)", "DJMIX(草莓味)", "软盒摩尔", "硬盒BALCK DKVIL", "BLACKDEUIL", "玉溪(磨沙专供出口)", "阿诗玛(专供出口)", "硬盒白万宝路", "硬盒红万宝路", "牡丹(软)", "南京(绿)", "雄狮(薄荷)", "大红鹰(软新品)", "硬盒More", "硬盒登喜路", "JEWELS（橙色）", "软盒BLACK DEVIL（黑魔）", "硬盒BLACK DEVIL（黑魔）", "硬盒RAISON", "利群(蓝)", "双叶", "硬盒希尔顿", "软盒游泳", "黄鹤楼(软蓝专供出口)", "BLACKDEVII(黑魔)", "黄鹤楼（全家福）", "黄鹤楼（硬同心）", "ESSE（蓝）", "黄鹤楼（好运）", "黄鹤楼(铁盒1916)(假)(罚)", "黄鹤楼(铁盒漫天游)(假)(罚)", "大重九", "黄鹤楼1916（硬白皮）", "游泳", "圆球", "大公鸡", "永光", "星火", "黄鹤楼（红红火火）", "黄鹤楼（中国梦）", "黄鹤楼（天骄圣地）", "黄鹤楼（知音）", "黄鹤楼（峡谷情）", "钻石（荷花）", "其它"};

    private int isIntoDeptCheck;

    private int isIntoCorpCheck;

    private String concentratePunishId = "";

    private String noLiceRegistryId = "";

    boolean noLice = false;

    boolean ck= false;

    private TextView licenseTv;
    private TextView nameTv;
    private TextView addressTv;
    private TextView textView8;
    private TextView checkingPeople;
    private TextView addAbnormalInfoTv;
    private RecyclerView recyclerView;
    private EditText measureEt;
    private LinearLayout chooseLl;
    private TextView addNoticeNumTv;
    private RecyclerView tjRecyclerView;
    private EditText abnormalFeedbackEt;
    private LinearLayout buttonsLl;
    private TextView endCheckedTv;
    private TextView reportManagementTv;
    private TextView reportDistrictTv;
    private TextView confirmTv;
    private TextView doneCheckedJcd;
    private TextView shopnameTv;
    private TextView noticeNumEt;

    private void assignViews() {
        licenseTv = (TextView) findViewById(R.id.license_tv);
        nameTv = (TextView) findViewById(R.id.name_tv);
        shopnameTv = (TextView) findViewById(R.id.shopname_tv);
        addressTv = (TextView) findViewById(R.id.address_tv);
        textView8 = (TextView) findViewById(R.id.textView8);
        checkingPeople = (TextView) findViewById(R.id.checking_people);
        addAbnormalInfoTv = (TextView) findViewById(R.id.add_abnormal_info_tv);
        recyclerView = (RecyclerView) findViewById(recycler_view);
        measureEt = (EditText) findViewById(R.id.measure_et);
        chooseLl = (LinearLayout) findViewById(R.id.choose_ll);
        addNoticeNumTv = (TextView) findViewById(R.id.add_notice_num_tv);
        tjRecyclerView = (RecyclerView) findViewById(R.id.tj_recycler_view);
        abnormalFeedbackEt = (EditText) findViewById(R.id.abnormal_feedback_et);
        buttonsLl = (LinearLayout) findViewById(R.id.buttons_ll);
        endCheckedTv = (TextView) findViewById(R.id.end_checked_tv);
        reportManagementTv = (TextView) findViewById(R.id.report_management_tv);
        reportDistrictTv = (TextView) findViewById(R.id.report_district_tv);
        confirmTv = (TextView) findViewById(R.id.confirm_tv);
        doneCheckedJcd = (TextView) findViewById(R.id.done_checked_jcd);
        noticeNumEt = (EditText) findViewById(R.id.notice_num_et);

        addAbnormalInfoTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                customDialog();
            }
        });

        chooseLl.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);

                AlertDialog alertDialog = new AlertDialog.Builder(CheckingActivity.this).setTitle("请选择").setItems(measuresStr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 3) noticeNumEt.setInputType(InputType.TYPE_CLASS_NUMBER );
                        else noticeNumEt.setInputType(InputType.TYPE_CLASS_TEXT );
                        measureEt.setText(measuresStr[which]);
                        try {
                            data.put("measures", which+1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).create();
                alertDialog.show();

            }
        });

        endCheckedTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);

                try {
                    if(StringUtility.notObjEmpty(data, "measures")) {

                        if(measureEt.getText().toString().length() == 0){
                            showToast("请选择处理措施", CheckingActivity.this);
                        }else if (data.getInt("measures") > 1 && tjItems.size() == 0) {
                            showToast("请输入通知书号",CheckingActivity.this);
                        } else {
                            outShop();
                        }
                    }else{
                        outShop();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        reportManagementTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);

                AlertDialog.Builder builder = new AlertDialog.Builder(CheckingActivity.this);
                builder.setMessage("提示");
                builder.setTitle("是否提报到管理所");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isIntoDeptCheck = 1;
                    }
                });
                builder.setNegativeButton("否", null);
                builder.create().show();

            }
        });

        reportDistrictTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);

                AlertDialog.Builder builder = new AlertDialog.Builder(CheckingActivity.this);
                builder.setMessage("提示");
                builder.setTitle("是否提报到区局");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isIntoCorpCheck = 1;
                    }
                });
                builder.setNegativeButton("否", null);
                builder.create().show();

            }
        });

        confirmTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);

                confirm();
            }
        });

        doneCheckedJcd.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);

                try {
                    if(StringUtility.notObjEmpty(data, "measures")) {

                        if (data.getInt("measures") > 1 && tjItems.size() == 0) {
                            showToast("请输入通知书号", CheckingActivity.this);
                        } else {
                            outShop();
                        }
                    }else{
                        outShop();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        addNoticeNumTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                final EditText editText = new EditText(CheckingActivity.this);

                new AlertDialog.Builder(CheckingActivity.this).setTitle("通知书号").setView(editText).setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tjItems.add(editText.getText().toString());
                                tJRecyclerAdapter.notifyDataSetChanged();

                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                            }
                        }).setNegativeButton("取消", null).show();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.offsetChildrenHorizontal(2);
        gridLayoutManager.offsetChildrenVertical(2);

        tjRecyclerView.setItemAnimator(new DefaultItemAnimator());
        tjRecyclerView.setLayoutManager(gridLayoutManager);
        tjRecyclerView.setAdapter(tJRecyclerAdapter);

        id = getIntent().getIntExtra("id", 0);
        status = getIntent().getIntExtra("status", 0);
        concentratePunishId = getIntent().getStringExtra("concentratePunish");
        noLiceRegistryId = getIntent().getStringExtra("noLiceRegistryId");
        ck = getIntent().getBooleanExtra("ck", false);

        if(status == 1){
            buttonsLl.setVisibility(View.VISIBLE);
            confirmTv.setVisibility(View.GONE);
        }else if(status == 5){
            buttonsLl.setVisibility(View.GONE);
            confirmTv.setVisibility(View.VISIBLE);

            addAbnormalInfoTv.setVisibility(View.GONE);
        }else{
            buttonsLl.setVisibility(View.GONE);
            confirmTv.setVisibility(View.GONE);

            addAbnormalInfoTv.setVisibility(View.GONE);
        }

        int postType = getIntent().getIntExtra("postType", 0);
        if(postType == 2){
            buttonsLl.setVisibility(View.VISIBLE);
            reportManagementTv.setVisibility(View.GONE);
            reportDistrictTv.setVisibility(View.VISIBLE);
            doneCheckedJcd.setVisibility(View.GONE);
        }else if(postType == 1){
            buttonsLl.setVisibility(View.VISIBLE);
            reportManagementTv.setVisibility(View.VISIBLE);
            reportDistrictTv.setVisibility(View.GONE);
            doneCheckedJcd.setVisibility(View.GONE);
        }else if(postType == 0){
            reportManagementTv.setVisibility(View.GONE);
            reportDistrictTv.setVisibility(View.GONE);
            doneCheckedJcd.setVisibility(View.VISIBLE);
            buttonsLl.setVisibility(View.VISIBLE);
            endCheckedTv.setVisibility(View.GONE);
            addAbnormalInfoTv.setVisibility(View.VISIBLE);
        }

        if(ck){
            reportManagementTv.setVisibility(View.GONE);
            reportManagementTv.setVisibility(View.GONE);
            doneCheckedJcd.setVisibility(View.GONE);
            buttonsLl.setVisibility(View.GONE);
            endCheckedTv.setVisibility(View.GONE);
            addAbnormalInfoTv.setVisibility(View.GONE);
        }
        Constant.isRefresJZZZSGrid = false;

        loadData();
    }

    private void loadData(){
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams params = new RequestParams();
        params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\""+ Constant.MWB_Base_URL+"marketCheck!intoShop.action?privilegeFlag=VIEW&_query.id="+id+"\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

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

                            data = message.getJSONObject("data");

                            String liceNo = "";
                            String shopName = "";
                            String chargerName = "";
                            String shopAddress = "";

                            if(StringUtility.notObjEmpty(data, "customer")) {
                                JSONObject customer = data.getJSONObject("customer");
                                liceNo = customer.getString("liceNo");
                                shopName = customer.getString("shopName");
                                chargerName = customer.getString("chargerName");
                                shopAddress = customer.getString("shopAddress");
                            }else if (StringUtility.notObjEmpty(data, "noLiceRegistry")) {
                                JSONObject noLiceRegistry = data.getJSONObject("noLiceRegistry");
                                shopAddress = noLiceRegistry.getString("address");
                                shopName = noLiceRegistry.getString("shopName");
                                chargerName = noLiceRegistry.getString("name");
                            }

                            shopnameTv.setText(shopName);
                            addressTv.setText(shopAddress);
                            licenseTv.setText(liceNo);
                            nameTv.setText(chargerName);

                            if(StringUtility.notObjEmpty(data, "noticeNumber")){
                                String noticeNumber = data.getString("noticeNumber");
                                if(noticeNumber.length() > 0){
                                    String[] noticeNumbers = noticeNumber.split(",");

                                    for(String n: noticeNumbers){
                                        tjItems.add(n);
                                    }

                                    tJRecyclerAdapter.notifyDataSetChanged();
                                }
                            }


                            if(!data.isNull("reason")){
                                String reason = data.getString("reason");
                                if(reason.length() > 0){
                                    showToast("异常内容: " + reason, CheckingActivity.this);
                                }
                            }


                            if (StringUtility.notObjEmpty(data, "measures")) {
                                int measures = data.getInt("measures");

                                measureEt.setText(measuresStr[measures - 1]);
                            }

                            if (StringUtility.notObjEmpty(data, "noticeNumber")) {
                                String noticeNumber = data.getString("noticeNumber");
                                noticeNumEt.setText(noticeNumber);
                            }

                            if (StringUtility.notObjEmpty(data, "feedbackContent")) {
                                String feedbackContent = data.getString("feedbackContent");
                                abnormalFeedbackEt.setText(feedbackContent);
                            }

                            final String checkPersons = data.getString("checkPersons");
                            String[] ps = checkPersons.split(",");
                            checkingPeople.setText(checkPersons);

                            if(StringUtility.notObjEmpty(data, "concentratePunish")){
                                JSONObject concentratePunish = data.getJSONObject("concentratePunish");

                                String checkPersonNames = concentratePunish.getString("checkPersonNames");

                                checkingPeople.setText(checkPersonNames);

                            }

                            loadListData();
                        } else {
                            showToast(response.getString("message"), CheckingActivity.this);
                        }
                    } else {
                        showToast(response.getString("message"), CheckingActivity.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(CheckingActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(CheckingActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(CheckingActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void loadListData() {
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams params = new RequestParams();
        params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MWB_Base_URL + "marketCheck!findDetails.action?privilegeFlag=VIEW&bean.id=" + id + "\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

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
                            System.out.println("message: " + message.toString());

                            JSONArray data = message.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject object = data.getJSONObject(i);
                                MarketInspectCig cig = new MarketInspectCig();
                                cig.setType(object.getInt("type"));
                                cig.setCigName(object.getString("cigName"));
                                cig.setCigCode(object.getString("cigCode"));
                                cig.setAmount(object.getString("amount"));
                                marketInspectCigs.add(cig);
                            }

                            recyclerAdapter.notifyDataSetChanged();

                        } else {
                            showToast(message.getString("message"), CheckingActivity.this);
                        }
                    }else{
                        showToast(response.getString("message"), CheckingActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(CheckingActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(CheckingActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(CheckingActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void outShop() throws JSONException {
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        StringBuffer buffer = new StringBuffer();
        if(StringUtility.notObjEmpty(data, "customer")) buffer.append("&bean.customer.id=" + data.getJSONObject("customer").getString("id"));
        if(StringUtility.notObjEmpty(data, "customer")) buffer.append("&bean.customer.version=" + data.getJSONObject("customer").getString("version"));
        buffer.append("&bean.id=" + data.getString("id"));
        buffer.append("&bean.version=" + data.getString("version"));
        buffer.append("&bean.dataSource=" + data.getString("dataSource"));
        buffer.append("&bean.level=" + data.getString("level"));
        buffer.append("&bean.reason=" + data.getString("reason"));
        if(StringUtility.notObjEmpty(data, "measures")) buffer.append("&bean.measures=" + data.getInt("measures"));

        buffer.append("&bean.feedbackContent=" + abnormalFeedbackEt.getText().toString());
        buffer.append("&bean.charger=" + data.getString("charger"));
        buffer.append("&bean.checkPersons=" + data.getString("checkPersons"));
        buffer.append("&bean.isIntoDeptCheck=" + isIntoDeptCheck);
        buffer.append("&bean.isIntoCorpCheck=" + isIntoCorpCheck);

        StringBuffer noticeNum = new StringBuffer("");

        for(int i=0;i<tjItems.size();i++){
            if(noticeNum.length() > 0){
                noticeNum.append(","+tjItems.get(i));
            }else{
                noticeNum.append(tjItems.get(i));
            }
        }

        buffer.append("&bean.noticeNumber=" + noticeNum.toString());

        if(concentratePunishId != null && concentratePunishId.length() > 0) {
            buffer.append("&bean.concentratePunish.id=" + concentratePunishId);

            noLice = getIntent().getBooleanExtra("noLice", false);
        }

        if(noLiceRegistryId != null && noLiceRegistryId.length() > 0){
            buffer.append("&bean.noLiceRegistry.id=" + noLiceRegistryId);
        }

        if (marketInspectCigs.size() > 0) {
            for (int i = 0; i < marketInspectCigs.size(); i++) {
                MarketInspectCig marketInspectCig = marketInspectCigs.get(i);
                buffer.append("&bean.cigList[" + i + "].type=" + marketInspectCig.getType() + "&bean.cigList[" + i + "].cigName=" + marketInspectCig.getCigName() + "&bean.cigList[" + i + "].cigCode=" + marketInspectCig.getCigCode() + "&bean.cigList[" + i + "].amount=" + marketInspectCig.getAmount());
            }
        }

        System.out.println("buffer: " + buffer);

        RequestParams params = new RequestParams();
        params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\""+Constant.MWB_Base_URL+"marketCheck!outShop.action?privilegeFlag=EDIT"+buffer+"\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

        HttpClient.post(Constant.EXEC, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                pd.dismiss();

                try {
                    if(StringUtility.isSuccess(response)) {
                        System.out.println("response: " + response.toString());

                        String messageSting = response.getString("message");

                        JSONObject message = new JSONObject(messageSting);

                        if (StringUtility.isSuccess(message)) {
                            if(concentratePunishId != null && concentratePunishId.length() > 0 || (noLiceRegistryId != null && noLiceRegistryId.length() > 0)){
                                if(noLice)Constant.isRefresNoJZZZS = true; else Constant.isRefresJZZZS = true;
                            }else {
                                Constant.isRefreshSCJC = true;
                            }
                            finish();
                        } else {
                            showToast(message.getString("message"), CheckingActivity.this);
                        }
                    }else{
                        showToast(response.getString("message"), CheckingActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(CheckingActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(CheckingActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(CheckingActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void confirm(){
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams params = new RequestParams();
        params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\""+Constant.MWB_Base_URL+"marketCheck!confirm.action?privilegeFlag=EDIT&bean.id="+id+"\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

        HttpClient.post(Constant.EXEC, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                pd.dismiss();

                try {
                    if (StringUtility.isSuccess(response)) {
                        System.out.println("response: " + response.toString());

                        String messageSting = response.getString("message");

                        JSONObject message = new JSONObject(messageSting);

                        if (StringUtility.isSuccess(message)) {
                            System.out.println("message: " + message.toString());
                            Constant.isRefreshSCJC = true;
                            finish();
                        } else {
                            showToast(message.getString("message"), CheckingActivity.this);
                        }
                    } else {
                        showToast(response.getString("message"), CheckingActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(CheckingActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(CheckingActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(CheckingActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

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
                    showToast("请选择异常类型", CheckingActivity.this);
                } else if (currentMarketInspectCig.getCigName() == null) {
                    showToast("请选择品规", CheckingActivity.this);
                } else if (jd_dilog_ed2.getText().toString().length() == 0) {
                    showToast("请输入数量", CheckingActivity.this);
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

        AlertDialog alertDialog = new AlertDialog.Builder(CheckingActivity.this).setTitle("请选择").setItems(value, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == key.length - 1) {
                    edlog();
                } else {
                    currentMarketInspectCig.setCigCode(key[which]);
                    currentMarketInspectCig.setCigName(value[which]);
                    customDialog();
                }
            }
        }).create();
        alertDialog.show();

    }

    private void edlog(){
        View layout = LayoutInflater.from(this).inflate(R.layout.custom_edit_dialog2, (ViewGroup) findViewById(R.id.custom_dialog));
        final EditText username = (EditText)layout.findViewById(R.id.dialog_username_ed);
        final EditText password = (EditText)layout.findViewById(R.id.dialog_password_ed);

        final AlertDialog pd = new AlertDialog.Builder(this).setTitle("提示").setView(layout).setPositiveButton("确定", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (username.getText().toString().length() == 0) {
                    CheckingActivity.this.showToast("请输入卷烟代码",CheckingActivity.this);
                } else if (password.getText().toString().length() == 0) {
                    CheckingActivity.this.showToast("请输入卷烟品牌",CheckingActivity.this);
                } else {
                    currentMarketInspectCig.setCigCode(username.getText().toString());
                    currentMarketInspectCig.setCigName(password.getText().toString());
                    customDialog();
                }
            }
        }).setNegativeButton("取消", null).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checking);
        setToolbarTitle("核查");
        showBack();

        assignViews();
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>{

        @Override
        public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerHolder holder = new RecyclerHolder(LayoutInflater.from(CheckingActivity.this).inflate(R.layout.activity_xzfw_rcfw_service_info_abnormal_info_recyclerview, parent, false));
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

    class TJRecyclerAdapter extends RecyclerView.Adapter<TJRecyclerAdapter.RecyclerHolder> {

        @Override
        public TJRecyclerAdapter.RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerHolder holder = new RecyclerHolder(LayoutInflater.from(CheckingActivity.this).inflate(R.layout.tj_item, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(TJRecyclerAdapter.RecyclerHolder h, int position) {

            String str = tjItems.get(position);
            h.tj_item_tv.setText(str);

            final int p = position;

            h.del_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tjItems.remove(p);
                    tJRecyclerAdapter.notifyDataSetChanged();
                }
            });

        }

        @Override
        public int getItemCount() {
            return tjItems.size();
        }

        class RecyclerHolder extends RecyclerView.ViewHolder{

            public TextView tj_item_tv, del_tv;

            public RecyclerHolder(View itemView) {
                super(itemView);
                tj_item_tv = (TextView) itemView.findViewById(R.id.tj_item_tv);
                del_tv = (TextView) itemView.findViewById(R.id.del_tv);
            }
        }
    }
}
