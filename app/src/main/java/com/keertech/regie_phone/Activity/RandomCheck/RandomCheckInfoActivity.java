package com.keertech.regie_phone.Activity.RandomCheck;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import static com.keertech.regie_phone.R.id.recycler_view;

/**
 * Created by soup on 2017/5/24.
 */

public class RandomCheckInfoActivity extends BaseActivity{

    String id = "";

    private ArrayList<String> tjItems = new ArrayList<>();

    TJRecyclerAdapter tjRecyclerAdapter = new TJRecyclerAdapter();

    RecyclerAdapter recyclerAdapter = new RecyclerAdapter();

    private String[] measuresStr = {"守法经营", "开具先行教育通知书", "开具责令整改通知书", "开具涉案物品先行登记保存通知书"};

    private ArrayList<MarketInspectCig> marketInspectCigs = new ArrayList<>();

    String[] cigNames, cigCode;

    private MarketInspectCig currentMarketInspectCig;

    String version = "";

    int measures = 1;

    boolean isLook = false;

    final int qianMin = 1;

    String fileName = "", newFileName = "", name = "";

    private TextView shopnameTv;
    private TextView licenseTv;
    private TextView nameTv;
    private TextView phoneTv;
    private LinearLayout linearLayout19;
    private TextView textView21;
    private TextView addAbnormalInfoTv;
    private RecyclerView recyclerView;
    private LinearLayout measureLl;
    private EditText measureEt;
    private TextView chooseMeasureTv;
    private LinearLayout bookNumberLl;
    private TextView addBookNumberTv;
    private TextView bookNumberRecyclerTv;
    private RecyclerView bookNumberRecyclerView;
    private EditText bookNumberRecyclerEt;
    private LinearLayout checkGroupOpinionLl;
    private EditText checkGroupOpinionEt;
    private LinearLayout checkObjectOpinionLl;
    private EditText checkObjectOpinionEt;
    private TextView autographTv;
    private CheckBox rejectCb;
    private TextView eyewitnessAutographTv;

    private void assignViews() {
        shopnameTv = (TextView) findViewById(R.id.shopname_tv);
        licenseTv = (TextView) findViewById(R.id.license_tv);
        nameTv = (TextView) findViewById(R.id.name_tv);
        phoneTv = (TextView) findViewById(R.id.phone_tv);
        linearLayout19 = (LinearLayout) findViewById(R.id.linearLayout19);
        textView21 = (TextView) findViewById(R.id.textView21);
        addAbnormalInfoTv = (TextView) findViewById(R.id.add_abnormal_info_tv);
        recyclerView = (RecyclerView) findViewById(recycler_view);
        measureLl = (LinearLayout) findViewById(R.id.measure_ll);
        measureEt = (EditText) findViewById(R.id.measure_et);
        chooseMeasureTv = (TextView) findViewById(R.id.choose_measure_tv);
        bookNumberLl = (LinearLayout) findViewById(R.id.book_number_ll);
        addBookNumberTv = (TextView) findViewById(R.id.add_book_number_tv);
        bookNumberRecyclerTv = (TextView) findViewById(R.id.book_number_recycler_tv);
        bookNumberRecyclerView = (RecyclerView) findViewById(R.id.book_number_recycler_view);
        bookNumberRecyclerEt = (EditText) findViewById(R.id.book_number_recycler_et);
        checkGroupOpinionLl = (LinearLayout) findViewById(R.id.check_group_opinion_ll);
        checkGroupOpinionEt = (EditText) findViewById(R.id.check_group_opinion_et);
        checkObjectOpinionLl = (LinearLayout) findViewById(R.id.check_object_opinion_ll);
        checkObjectOpinionEt = (EditText) findViewById(R.id.check_object_opinion_et);
        autographTv = (TextView) findViewById(R.id.autograph_tv);
        rejectCb = (CheckBox) findViewById(R.id.reject_cb);
        eyewitnessAutographTv = (TextView) findViewById(R.id.eyewitness_autograph_tv);

        addBookNumberTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                final EditText editText = new EditText(RandomCheckInfoActivity.this);

                new AlertDialog.Builder(RandomCheckInfoActivity.this).setTitle("通知书号").setView(editText).setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tjItems.add(editText.getText().toString());
                                recyclerAdapter.notifyDataSetChanged();

                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                            }
                        }).setNegativeButton("取消", null).show();
            }
        });

        autographTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                if(newFileName.length() > 0 || fileName.length() > 0){
                    return;
                }

                if(rejectCb.isChecked()){
                    showToast("请见证人签名", RandomCheckInfoActivity.this);
                    return;
                }

                if(checkObjectOpinionEt.getText().toString().length() == 0){
                    showToast("请填写被检查对象意见", RandomCheckInfoActivity.this);
                    return;
                }

                Intent intent = new Intent(RandomCheckInfoActivity.this, SignatureActivity.class);
                intent.putExtra("title", "签名");
                startActivityForResult(intent, qianMin);
            }
        });

        eyewitnessAutographTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                if(newFileName.length() > 0 || fileName.length() > 0){
                    return;
                }

                if(rejectCb.isChecked()) {
                    Intent intent = new Intent(RandomCheckInfoActivity.this, SignatureActivity.class);
                    intent.putExtra("title", "见证人签名");
                    startActivityForResult(intent, qianMin);
                }
            }
        });

        addAbnormalInfoTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                customDialog();
            }
        });

        chooseMeasureTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                AlertDialog alertDialog = new AlertDialog.Builder(RandomCheckInfoActivity.this).setTitle("请选择").setItems(measuresStr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 3) bookNumberRecyclerEt.setInputType(InputType.TYPE_CLASS_NUMBER );
                        else bookNumberRecyclerEt.setInputType(InputType.TYPE_CLASS_TEXT );
                        measureEt.setText(measuresStr[which]);
                        measures = which+1;
                    }
                }).create();
                alertDialog.show();
            }
        });

        TextWatcher textWatcher = new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if(newFileName.length() > 0) newFileName = "";
                if(fileName.length() > 0) fileName = "";
                if(name.length() > 0) name = "";

                autographTv.setText("点击签名");
                autographTv.setBackground(null);
            }
        };

        checkGroupOpinionEt.addTextChangedListener(textWatcher);
        checkObjectOpinionEt.addTextChangedListener(textWatcher);

        recyclerView.setLayoutManager(new LinearLayoutManager(RandomCheckInfoActivity.this));
        recyclerView.setAdapter(recyclerAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 5);
        gridLayoutManager.offsetChildrenHorizontal(2);
        gridLayoutManager.offsetChildrenVertical(2);

        bookNumberRecyclerView.setLayoutManager(gridLayoutManager);
        bookNumberRecyclerView.setAdapter(tjRecyclerAdapter);

        id = getIntent().getStringExtra("id");

        isLook = getIntent().getBooleanExtra("isLook", false);

        String shopName = getIntent().getStringExtra("shopName");
        String liceNo = getIntent().getStringExtra("liceNo");
        String chargerName = getIntent().getStringExtra("chargerName");
        String contactphone = getIntent().getStringExtra("contactphone");

        if(contactphone.equals("null")) contactphone = "";

        shopnameTv.setText(shopName);
        licenseTv.setText(liceNo);
        nameTv.setText(chargerName);
        phoneTv.setText(contactphone);

        if(!isLook) {
            bookNumberRecyclerTv.setVisibility(View.GONE);

            marketCheckmarketCheck();
            intoShop();
        }else{

            findBean();

            addAbnormalInfoTv.setVisibility(View.GONE);
            chooseMeasureTv.setVisibility(View.GONE);
            addBookNumberTv.setVisibility(View.GONE);

        }
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
                    showToast("请选择异常类型", RandomCheckInfoActivity.this);
                } else if (currentMarketInspectCig.getCigName() == null) {
                    showToast("请选择品规", RandomCheckInfoActivity.this);
                } else if (jd_dilog_ed2.getText().toString().length() == 0) {
                    showToast("请输入数量", RandomCheckInfoActivity.this);
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
                chickDialog(cigCode, cigNames, jd_dilog_tv1);
            }
        });


    }

    private void chickDialog(final String[] key, final String[] value, final TextView textView) {

        AlertDialog alertDialog = new AlertDialog.Builder(RandomCheckInfoActivity.this).setTitle("请选择").setItems(value, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
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
                    RandomCheckInfoActivity.this.showToast("请输入卷烟代码",RandomCheckInfoActivity.this);
                } else if (password.getText().toString().length() == 0) {
                    RandomCheckInfoActivity.this.showToast("请输入卷烟品牌",RandomCheckInfoActivity.this);
                } else {
                    currentMarketInspectCig.setCigCode(username.getText().toString());
                    currentMarketInspectCig.setCigName(password.getText().toString());
                    customDialog();
                }
            }
        }).setNegativeButton("取消", null).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == qianMin){
                fileName = data.getStringExtra("fileName");
                name = data.getStringExtra("name");
                autographTv.setText("");
                eyewitnessAutographTv.setText("");

                File file = new File(fileName);

                Bitmap bitmap  = BitmapFactory.decodeFile(file.getAbsolutePath());
                Drawable drawable =new BitmapDrawable(bitmap);
                if(!rejectCb.isChecked()){

                    autographTv.setBackground(drawable);
                }
                else{

                    eyewitnessAutographTv.setBackground(drawable);
                }
            }
        }
    }

    private void findBean() {
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams params = new RequestParams();
        params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MWB_Base_URL + "checkCustomer!findBean.action?bean.id="+id+"\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

        HttpClient.post(Constant.EXEC, params, new JsonHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                pd.dismiss();

                try {
                    if(StringUtility.isSuccess(response)) {
                        String messageSting = response.getString("message");

                        JSONObject message = new JSONObject(messageSting);

                        if (StringUtility.isSuccess(message)) {

                            JSONObject data = message.getJSONObject("data");

                            version = data.getString("version");

                            int rejectOption = data.getInt("rejectOption");

                            if(rejectOption == 1){
                                rejectCb.setChecked(true);
                            }else{
                                rejectCb.setChecked(false);
                            }

                            if (StringUtility.notObjEmpty(data, "measures")) {
                                measures = data.getInt("measures");

                                measureEt.setText(measuresStr[measures - 1]);
                            }

                            if (StringUtility.notObjEmpty(data, "noticeNumber")) {
                                String noticeNumber = data.getString("noticeNumber");

                                bookNumberRecyclerTv.setText(noticeNumber);
                            }

                            if (StringUtility.notObjEmpty(data, "feedbackContent")) {
                                String feedbackContent = data.getString("feedbackContent");
                                checkGroupOpinionEt.setText(feedbackContent);
                                checkGroupOpinionEt.setEnabled(false);
                            }

                            if (StringUtility.notObjEmpty(data, "liableContent")) {
                                String liableContent = data.getString("liableContent");
                                checkObjectOpinionEt.setText(liableContent);
                                checkObjectOpinionEt.setEnabled(false);
                            }

                            if (StringUtility.notObjEmpty(data, "customerOption")) {
                                String customerOption = data.getString("customerOption");
                                newFileName = customerOption;
                                loadImage(customerOption);
                            }

                            if (StringUtility.notObjEmpty(data, "witnessOption")) {
                                String witnessOption = data.getString("witnessOption");
                                newFileName = witnessOption;
                                loadImage(witnessOption);
                            }

                            findDetails();

                        } else {
                            showToast(message.getString("message"), RandomCheckInfoActivity.this);
                        }
                    }else{
                        showToast(response.getString("message"), RandomCheckInfoActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(RandomCheckInfoActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(RandomCheckInfoActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(RandomCheckInfoActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void marketCheckmarketCheck() {
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams params = new RequestParams();
        params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MWB_Base_URL + "marketSecretlyCheckOption!list.action\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

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

                            JSONArray data = message.getJSONArray("data");

                            cigNames = new String[data.length() + 1];
                            cigCode = new String[data.length() + 1];

                            cigNames[0] = "其他";
                            cigCode[0] = "";

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject object = data.getJSONObject(i);

                                cigNames[i + 1] = object.getString("cigName");
                                cigCode[i + 1] = object.getString("cigCode");
                            }

                        } else {
                            showToast(message.getString("message"), RandomCheckInfoActivity.this);
                        }
                    }else{
                        showToast(response.getString("message"), RandomCheckInfoActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(RandomCheckInfoActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(RandomCheckInfoActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(RandomCheckInfoActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void intoShop() {
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams params = new RequestParams();
        params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MWB_Base_URL + "checkCustomer!intoShop.action?_query.id="+id+"\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

        HttpClient.post(Constant.EXEC, params, new JsonHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                pd.dismiss();

                try {
                    if(StringUtility.isSuccess(response)) {
                        String messageSting = response.getString("message");

                        JSONObject message = new JSONObject(messageSting);

                        if (StringUtility.isSuccess(message)) {

                            JSONObject data = message.getJSONObject("data");

                            version = data.getString("version");

                            int rejectOption = data.getInt("rejectOption");

                            if(rejectOption == 1){
                                rejectCb.setChecked(true);
                            }else{
                                rejectCb.setChecked(false);
                            }

                            if (StringUtility.notObjEmpty(data, "measures")) {
                                measures = data.getInt("measures");

                                measureEt.setText(measuresStr[measures - 1]);
                            }

                            if (StringUtility.notObjEmpty(data, "noticeNumber")) {
                                String noticeNumber = data.getString("noticeNumber");

                                if(noticeNumber.length() > 0) {
                                    String[] noticeNumbers = noticeNumber.split(",");

                                    for (String str : noticeNumbers) {
                                        tjItems.add(str);
                                    }

                                    tjRecyclerAdapter.notifyDataSetChanged();
                                }
                            }

                            if (StringUtility.notObjEmpty(data, "feedbackContent")) {
                                String feedbackContent = data.getString("feedbackContent");
                                checkGroupOpinionEt.setText(feedbackContent);
                            }

                            if (StringUtility.notObjEmpty(data, "liableContent")) {
                                String liableContent = data.getString("liableContent");
                                checkObjectOpinionEt.setText(liableContent);
                            }

                            if (StringUtility.notObjEmpty(data, "customerOption")) {
                                String customerOption = data.getString("customerOption");
                                newFileName = customerOption;
                                loadImage(customerOption);
                            }


                            if (StringUtility.notObjEmpty(data, "witnessOption")) {
                                String witnessOption = data.getString("witnessOption");
                                newFileName = witnessOption;
                                loadImage(witnessOption);
                            }


                            findDetails();

                        } else {
                            showToast(message.getString("message"), RandomCheckInfoActivity.this);
                        }
                    }else{
                        showToast(response.getString("message"), RandomCheckInfoActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(RandomCheckInfoActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(RandomCheckInfoActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(RandomCheckInfoActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void findDetails() {
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams params = new RequestParams();
        params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MWB_Base_URL + "checkCustomer!findDetails.action?bean.id=" + id + "\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

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
                            showToast(message.getString("message"), RandomCheckInfoActivity.this);
                        }
                    }else{
                        showToast(response.getString("message"), RandomCheckInfoActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(RandomCheckInfoActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(RandomCheckInfoActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(RandomCheckInfoActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void loadImage(final String fileName){
        String[] names = fileName.split("/");
        final String name = names[names.length-1];

        File file = (getFile(name));

        if(file.exists()){
            try {
                Bitmap bitmap  = BitmapFactory.decodeFile(file.getAbsolutePath());
                Drawable drawable =new BitmapDrawable(bitmap);
                if(!rejectCb.isChecked()){
                    autographTv.setText("");
                    autographTv.setBackground(drawable);
                }
                else{
                    eyewitnessAutographTv.setText("");
                    eyewitnessAutographTv.setBackground(drawable);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else {
            RequestParams params = new RequestParams();
            params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MWB_Base_URL + "checkCustomer!loadCustomerOption.action?privilegeFlag=VIEW&fileName=" + fileName + "\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");
            HttpClient.post(Constant.EXEC, params, new FileAsyncHttpResponseHandler(file) {
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, File file) {
                    try {

                        Bitmap bitmap1 = BitmapFactory.decodeFile(file.getAbsolutePath());
                        Drawable drawable =new BitmapDrawable(bitmap1);
                        if(!rejectCb.isChecked()){
                            autographTv.setText("");
                            autographTv.setBackground(drawable);
                        }
                        else{
                            eyewitnessAutographTv.setText("");
                            eyewitnessAutographTv.setBackground(drawable);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }

    private void uploadImage(File file, String filename){
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.uploading_autograhf);
        pd.show();

        String action = "checkCustomer!uploadCustomerOption.action";

        RequestParams params = new RequestParams();
        params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MWB_Base_URL + action + "\"," +
                "\"parameter\":{\"upload\":\"$uploadFile\"}," +
                "\"type\":\"WebExecutor\",\"method\":\"POST\"},\"app\":\"1001\"}");

        try {
            params.put("uploadFile",file);
            params.put("uploadFileName",filename);
            params.put("uploadContentType","png");

            HttpClient.post(Constant.EXEC, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    pd.dismiss();

                    try {
                        String messageSting = response.getString("message");
                        JSONObject message = new JSONObject(messageSting);
                        if(StringUtility.isSuccess(message)) {

                            newFileName = message.getString("newFileName");

                            try {
                                outShop();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else{
                            showToast("签名上传失败,请重试", RandomCheckInfoActivity.this);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    pd.dismiss();
                    showNetworkError(RandomCheckInfoActivity.this);
                    super.onFailure(statusCode, headers, responseString, throwable);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    pd.dismiss();
                    showNetworkError(RandomCheckInfoActivity.this);
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    pd.dismiss();
                    showNetworkError(RandomCheckInfoActivity.this);
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void outShop() throws JSONException {
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.sending);
        pd.show();

        StringBuffer buffer = new StringBuffer();
        buffer.append("bean.id=" + id);
        buffer.append("&bean.version=" + version);
        if(measures > 0)buffer.append("&bean.measures=" + measures);

        buffer.append("&bean.feedbackContent=" + checkGroupOpinionEt.getText().toString());
        buffer.append("&bean.liableContent=" + checkObjectOpinionEt.getText().toString());

        if(!rejectCb.isChecked()) buffer.append("&bean.customerOption=" + newFileName);
        else buffer.append("&bean.witnessOption=" + newFileName);

        if(!rejectCb.isChecked()) buffer.append("&bean.rejectOption=" + 0);
        else buffer.append("&bean.rejectOption=" + 1);

        StringBuffer noticeNum = new StringBuffer("");

        for(int i=0;i<tjItems.size();i++){
            if(noticeNum.length() > 0){
                noticeNum.append(","+tjItems.get(i));
            }else{
                noticeNum.append(tjItems.get(i));
            }
        }

        buffer.append("&bean.noticeNumber=" + noticeNum.toString());

        if (marketInspectCigs.size() > 0) {
            for (int i = 0; i < marketInspectCigs.size(); i++) {
                MarketInspectCig marketInspectCig = marketInspectCigs.get(i);
                buffer.append("&bean.cigList[" + i + "].type=" + marketInspectCig.getType() + "&bean.cigList[" + i + "].cigName=" + marketInspectCig.getCigName() + "&bean.cigList[" + i + "].cigCode=" + marketInspectCig.getCigCode() + "&bean.cigList[" + i + "].amount=" + marketInspectCig.getAmount());
            }
        }

        System.out.println("buffer: " + buffer);

        RequestParams params = new RequestParams();
        params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\""+Constant.MWB_Base_URL+"checkCustomer!outShop.action?"+buffer+"\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

        HttpClient.post(Constant.EXEC, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                pd.dismiss();

                try {
                    if(StringUtility.isSuccess(response)) {

                        String messageSting = response.getString("message");

                        JSONObject message = new JSONObject(messageSting);

                        if (StringUtility.isSuccess(message)) {
                            Constant.isRefreshRandom = true;
                            finish();
                        } else {
                            showToast(message.getString("message"), RandomCheckInfoActivity.this);
                        }
                    }else{
                        showToast(response.getString("message"), RandomCheckInfoActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(RandomCheckInfoActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(RandomCheckInfoActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(RandomCheckInfoActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_check_info);
        setToolbarTitle("检查详情");
        showBack();

        assignViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.checking_done_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_done){
            if(!isLook){
                if(!rejectCb.isChecked()){
                    if (checkGroupOpinionEt.getText().toString().length() == 0) {
                        showToast("请填写检查组意见", this);
                        return true;
                    }
                }

                if(!rejectCb.isChecked()) {
                    if (checkObjectOpinionEt.getText().toString().length() == 0) {
                        showToast("请填写被检查对象意见", this);
                        return true;
                    }
                }

                if(fileName.length() == 0){
                    if(!rejectCb.isChecked())showToast("请签名", this);
                    else showToast("需见证人签名", this);
                    return true;
                }

                uploadImage(new File(fileName), name);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>{

        @Override
        public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerHolder holder = new RecyclerHolder(LayoutInflater.from(RandomCheckInfoActivity.this).inflate(R.layout.activity_xzfw_rcfw_service_info_abnormal_info_recyclerview, parent, false));
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
            RecyclerHolder holder = new RecyclerHolder(LayoutInflater.from(RandomCheckInfoActivity.this).inflate(R.layout.tj_item, parent, false));
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
                    tjRecyclerAdapter.notifyDataSetChanged();
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
