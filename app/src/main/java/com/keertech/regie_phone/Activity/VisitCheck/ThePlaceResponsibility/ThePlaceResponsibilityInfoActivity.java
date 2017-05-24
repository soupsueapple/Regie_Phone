package com.keertech.regie_phone.Activity.VisitCheck.ThePlaceResponsibility;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.keertech.regie_phone.BaseActivity;
import com.keertech.regie_phone.Constant.Constant;
import com.keertech.regie_phone.Listener.ViewClickVibrate;
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
 * Created by soup on 2017/5/23.
 */

public class ThePlaceResponsibilityInfoActivity extends BaseActivity{

    private TextView shopnameTv;
    private TextView checkedDateTv;
    private TextView addressTv;
    private TextView tv1;
    private RadioGroup secretSalesGp;
    private RadioButton secretSalesYesRb;
    private RadioButton secretSalesNoRb;
    private RadioGroup publiclySalesGp;
    private TextView textView24;
    private RadioButton publiclySalesYesRb;
    private RadioButton publiclySalesNoRb;
    private RadioGroup notlicenseSalesGp;
    private RadioButton notlicenseSalesYesRb;
    private RadioButton notlicenseSalesNoRb;
    private TextView textView25;
    private RecyclerView recyclerView;
    private EditText responsibilityCommonEt;
    private TextView chooseResponsibilityCommonTv;
    private EditText cooperateCommonEt;
    private TextView chooseCooperateCommonTv;
    private EditText superintendentOpinionEt;
    private EditText returnReasonEt;

    RecyclerAdapter recyclerAdapter = new RecyclerAdapter();

    ArrayList<JSONObject> datas = new ArrayList<>();

    String taskId = "";

    int isAZSM = 0, isGKBM = 0, isWZJY = 0;

    String[] names, ids;

    final int ZRSGY = 1, XTSGY = 2;

    String postId = "", assistPostId = "";

    private void assignViews() {
        shopnameTv = (TextView) findViewById(R.id.shopname_tv);
        checkedDateTv = (TextView) findViewById(R.id.checked_date_tv);
        addressTv = (TextView) findViewById(R.id.address_tv);
        tv1 = (TextView) findViewById(R.id.tv1);
        secretSalesGp = (RadioGroup) findViewById(R.id.secret_sales_gp);
        secretSalesYesRb = (RadioButton) findViewById(R.id.secret_sales_yes_rb);
        secretSalesNoRb = (RadioButton) findViewById(R.id.secret_sales_no_rb);
        publiclySalesGp = (RadioGroup) findViewById(R.id.publicly_sales_gp);
        textView24 = (TextView) findViewById(R.id.textView24);
        publiclySalesYesRb = (RadioButton) findViewById(R.id.publicly_sales_yes_rb);
        publiclySalesNoRb = (RadioButton) findViewById(R.id.publicly_sales_no_rb);
        notlicenseSalesGp = (RadioGroup) findViewById(R.id.notlicense_sales_gp);
        notlicenseSalesYesRb = (RadioButton) findViewById(R.id.notlicense_sales_yes_rb);
        notlicenseSalesNoRb = (RadioButton) findViewById(R.id.notlicense_sales_no_rb);
        textView25 = (TextView) findViewById(R.id.textView25);
        recyclerView = (RecyclerView) findViewById(recycler_view);
        responsibilityCommonEt = (EditText) findViewById(R.id.responsibility_common_et);
        chooseResponsibilityCommonTv = (TextView) findViewById(R.id.choose_responsibility_common_tv);
        cooperateCommonEt = (EditText) findViewById(R.id.cooperate_common_et);
        chooseCooperateCommonTv = (TextView) findViewById(R.id.choose_cooperate_common_tv);
        superintendentOpinionEt = (EditText) findViewById(R.id.superintendent_opinion_et);
        returnReasonEt = (EditText) findViewById(R.id.return_reason_et);

        publiclySalesGp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.publicly_sales_yes_rb){
                    isGKBM = 1;
                }else if(i == R.id.publicly_sales_no_rb){
                    isGKBM = 0;
                }
            }
        });

        notlicenseSalesGp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.notlicense_sales_yes_rb){
                    isWZJY = 1;
                }else if(i == R.id.notlicense_sales_no_rb){
                    isWZJY = 0;
                }
            }
        });

        secretSalesGp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.secret_sales_yes_rb){
                    isAZSM = 1;
                }else if(i == R.id.secret_sales_no_rb){
                    isAZSM = 0;
                }
            }
        });

        chooseCooperateCommonTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                showItemDialog(ZRSGY);
            }
        });

        chooseCooperateCommonTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                showItemDialog(XTSGY);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);

        taskId = getIntent().getStringExtra("taskId");

        findBean();
        searchBeans();
    }

    private void showItemDialog(final int tag){

        AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle("请选择").setItems(names, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(tag == ZRSGY){

                    responsibilityCommonEt.setText(names[which]);

                    postId = ids[which];

                }else if(tag == XTSGY){

                    cooperateCommonEt.setText(names[which]);

                    assistPostId = ids[which];

                }
            }
        }).create();
        alertDialog.show();
    }

    private void findBean() {
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams params = new RequestParams();
        params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MWB_Base_URL + "marketSecretlyCheckFlow!findBean.action?taskId="+taskId+"\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

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

                            JSONObject data = message.getJSONObject("data");

                            shopnameTv.setText(data.getString("shopName"));
                            addressTv.setText(data.getString("shopAddress"));
                            checkedDateTv.setText(data.getString("checkDate").substring(0, 10));

                            isAZSM = data.getInt("isAZSM");
                            isWZJY = data.getInt("isWZJY");
                            isGKBM = data.getInt("isGKBM");

                            if(isAZSM == 1){
                                secretSalesYesRb.setChecked(true);
                                secretSalesNoRb.setChecked(false);

                            }else{
                                secretSalesYesRb.setChecked(false);
                                secretSalesYesRb.setChecked(true);

                            }

                            if(isGKBM == 1){
                                publiclySalesYesRb.setChecked(true);
                                publiclySalesNoRb.setChecked(false);

                            }else{
                                publiclySalesYesRb.setChecked(false);
                                publiclySalesNoRb.setChecked(true);

                            }

                            if(isWZJY == 1){
                                notlicenseSalesYesRb.setChecked(true);
                                notlicenseSalesNoRb.setChecked(false);


                            }else{
                                notlicenseSalesYesRb.setChecked(false);
                                notlicenseSalesNoRb.setChecked(true);
                            }

                            String id = data.getString("id");

                            findDetails(id);

                        } else {
                            showToast(message.getString("message"), ThePlaceResponsibilityInfoActivity.this);
                        }
                    }else{
                        showToast(response.getString("message"), ThePlaceResponsibilityInfoActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(ThePlaceResponsibilityInfoActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(ThePlaceResponsibilityInfoActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(ThePlaceResponsibilityInfoActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void findDetails(String id) {
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams params = new RequestParams();
        params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MWB_Base_URL + "marketSecretlyCheckFlow!findDetails.action?bean.id="+id+"\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

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

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject object = data.getJSONObject(i);
                                datas.add(object);
                            }

                            recyclerAdapter.notifyDataSetChanged();

                        } else {
                            showToast(message.getString("message"), ThePlaceResponsibilityInfoActivity.this);
                        }
                    }else{
                        showToast(response.getString("message"), ThePlaceResponsibilityInfoActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(ThePlaceResponsibilityInfoActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(ThePlaceResponsibilityInfoActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(ThePlaceResponsibilityInfoActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void searchBeans() {
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams params = new RequestParams();
        params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MWB_Base_URL + "postManage!searchBeans.action?privilegeFlag=VIEW&_query.name=SGY\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

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

                            names = new String[data.length()];
                            ids = new String[data.length()];

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject jsonObject = data.getJSONObject(i);
                                JSONObject user = jsonObject.getJSONObject("user");

                                String id = jsonObject.getString("id");
                                String name = user.getString("name");

                                names[i] = name;
                                ids[i] = id;


                            }



                        } else {
                            showToast(message.getString("message"), ThePlaceResponsibilityInfoActivity.this);
                        }
                    }else{
                        showToast(response.getString("message"), ThePlaceResponsibilityInfoActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(ThePlaceResponsibilityInfoActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(ThePlaceResponsibilityInfoActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(ThePlaceResponsibilityInfoActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theplace_responsibility_info);
        setToolbarTitle("详情");

        assignViews();
    }

    private void assignPost() {
        if(superintendentOpinionEt.getText().toString().length() == 0){
            showToast("所长意见", this);

            return;
        }

        if(postId.length() == 0 || assistPostId.length() == 0){
            showToast("请选择责任市管员与协同市管员", ThePlaceResponsibilityInfoActivity.this);

            return;
        }

        if(postId.equals(assistPostId)){
            showToast("责任市管员与协同市管员不能为同一人, 请重新选择", ThePlaceResponsibilityInfoActivity.this);

            return;
        }

        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams params = new RequestParams();
        params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MWB_Base_URL + "marketSecretlyCheckFlow!assignPost.action?taskId="+taskId+"&name=分派&flowStatus=5&postId="+postId+"&assistPostId="+assistPostId+"&deptOpinions="+superintendentOpinionEt.getText().toString()+"\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

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

                            Constant.isRefreshBSZR = true;
                            finish();

                        } else {
                            showToast(message.getString("message"), ThePlaceResponsibilityInfoActivity.this);
                        }
                    }else{
                        showToast(response.getString("message"), ThePlaceResponsibilityInfoActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(ThePlaceResponsibilityInfoActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(ThePlaceResponsibilityInfoActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(ThePlaceResponsibilityInfoActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void back() {
        if(returnReasonEt.getText().toString().length() == 0){
            showToast("请输入退回理由", this);

            return;
        }

        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams params = new RequestParams();
        params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MWB_Base_URL + "marketSecretlyCheckFlow!back.action?taskId="+taskId+"&name=退回&flowStatus=6&returnReasons="+returnReasonEt.getText().toString()+"\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

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

                            Constant.isRefreshBSZR = true;
                            finish();

                        } else {
                            showToast(message.getString("message"), ThePlaceResponsibilityInfoActivity.this);
                        }
                    }else{
                        showToast(response.getString("message"), ThePlaceResponsibilityInfoActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(ThePlaceResponsibilityInfoActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(ThePlaceResponsibilityInfoActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(ThePlaceResponsibilityInfoActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.theplace_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.assign_action){
            assignPost();
            return true;
        }

        if(id == R.id.return_action){
            back();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>{

        @Override
        public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerHolder holder = new RecyclerHolder(LayoutInflater.from(ThePlaceResponsibilityInfoActivity.this).inflate(R.layout.theplace_responsibility_info_recyclerview_item, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerHolder holder, int position) {
            JSONObject object = datas.get(position);

            try {
                holder.tv1.setText(object.getString("cigName"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        class RecyclerHolder extends RecyclerView.ViewHolder{

            TextView tv1;

            public RecyclerHolder(View itemView) {
                super(itemView);
                tv1 = (TextView) itemView.findViewById(R.id.tv1);
            }
        }
    }
}
