package com.keertech.regie_phone.Activity.PurchasePrice;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.keertech.regie_phone.Adapter.SetAdapter;
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

import java.util.ArrayList;

/**
 * Created by soup on 2017/5/27.
 */

public class AddPurchasePirceActivity extends BaseActivity{

    ListView list;

    ArrayList<JSONObject> datas = new ArrayList<>();

    DataAdapter dataAdapter;

    boolean isAdd = false;
    String collectionDate = "";
    String postId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_purchase_pirce);
        setToolbarTitle("创建");
        showBack();

        list = (ListView) findViewById(R.id.list);

        isAdd = getIntent().getBooleanExtra("isAdd", false);
        collectionDate = getIntent().getStringExtra("collectionDate");
        postId = getIntent().getStringExtra("postId");

        if(isAdd) getAddList();
        else update();

        dataAdapter = new DataAdapter(this);
        list.setAdapter(dataAdapter);
    }

    private void getAddList(){
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams requestParams = new RequestParams();
        requestParams.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\""+ Constant.MWB_Base_URL+"cigPriceCollection!add.action\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

        HttpClient.post(Constant.EXEC, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                System.out.println("response: " + response.toString());
                pd.dismiss();

                try {
                    if (StringUtility.isSuccess(response)) {
                        String messageSting = response.getString("message");

                        JSONObject message = new JSONObject(messageSting);

                        if (StringUtility.isSuccess(message)) {

                            JSONArray data = message.getJSONArray("data");

                            if (datas.size() > 0) datas.clear();

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject object = data.getJSONObject(i);

                                datas.add(object);
                            }

                            dataAdapter.setObjects(datas);
                            dataAdapter.notifyDataSetChanged();

                        } else {
                            showToast(response.getString("message"), AddPurchasePirceActivity.this);
                        }
                    } else {
                        showToast(response.getString("message"), AddPurchasePirceActivity.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(AddPurchasePirceActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(AddPurchasePirceActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(AddPurchasePirceActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void update(){
        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams requestParams = new RequestParams();
        requestParams.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MWB_Base_URL + "cigPriceCollection!update.action?collectionDate=" + collectionDate + "&postId=" + postId + "\",\"type\":\"WebExecutor\"},\"app\":\"1001\"}");

        HttpClient.post(Constant.EXEC, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                System.out.println("response: " + response.toString());
                pd.dismiss();

                try {
                    if (StringUtility.isSuccess(response)) {
                        String messageSting = response.getString("message");

                        JSONObject message = new JSONObject(messageSting);

                        if (StringUtility.isSuccess(message)) {

                            JSONArray data = message.getJSONArray("data");

                            if (datas.size() > 0) datas.clear();

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject object = data.getJSONObject(i);
//                                if(object.getString("purchasePrice").equals("null")){
//                                    object.put("purchasePrice", "0.0");
//                                }

                                datas.add(object);
                            }

                            dataAdapter.setObjects(datas);
                            dataAdapter.notifyDataSetChanged();

                        } else {
                            showToast(response.getString("message"), AddPurchasePirceActivity.this);
                        }
                    } else {
                        showToast(response.getString("message"), AddPurchasePirceActivity.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(AddPurchasePirceActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(AddPurchasePirceActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(AddPurchasePirceActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void save(){
        StringBuffer buffer = new StringBuffer("");

        for (int i = 0; i < datas.size(); i++) {
            JSONObject object = datas.get(i);
            if(i > 0) buffer.append(",");
            try {
                buffer.append("\"beanList[" + i + "].cigCode\":\"" + object.getString("cigCode") + "\"");
                buffer.append(",\"beanList[" + i + "].cigName\":\"" + object.getString("cigName") + "\"");

                if("null".equals(object.getString("purchasePrice2")))buffer.append(",\"beanList[" + i + "].purchasePrice2\":\"0.0\"");
                else buffer.append(",\"beanList[" + i + "].purchasePrice2\":\"" + object.getString("purchasePrice2") + "\"");

                if(!isAdd){
                    buffer.append(",\"beanList[" + i + "].id\":\"" + object.getString("id") + "\"");
                    buffer.append(",\"beanList[" + i + "].version\":\"" + object.getString("version") + "\"");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        RequestParams requestParams = new RequestParams();
        requestParams.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\""+ Constant.MWB_Base_URL+"cigPriceCollection!save.action\",\"parameter\":{"+buffer.toString()+"},\"type\":\"WebExecutor\",\"method\":\"POST\"},\"app\":\"1001\"}");

        HttpClient.post(Constant.EXEC, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                System.out.println("response: " + response.toString());
                pd.dismiss();

                try {
                    if (StringUtility.isSuccess(response)) {
                        String messageSting = response.getString("message");

                        JSONObject message = new JSONObject(messageSting);

                        if (StringUtility.isSuccess(message)) {
                            Constant.isRefresPurchasePirce = true;
                            finish();
                        } else {
                            showToast(response.getString("message"), AddPurchasePirceActivity.this);
                        }
                    } else {
                        showToast(response.getString("message"), AddPurchasePirceActivity.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                showNetworkError(AddPurchasePirceActivity.this);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pd.dismiss();
                showNetworkError(AddPurchasePirceActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                pd.dismiss();
                showNetworkError(AddPurchasePirceActivity.this);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_save){
            save();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class DataAdapter extends SetAdapter {

        //        private ArrayList<JSONObject> datas;
        private LayoutInflater inflater;

        public DataAdapter(Context context){
            super(context);
            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            final ViewHolder holder;

            if (convertView == null){
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.activity_add_purchase_pirce_recyclerview_item, null);
                holder.textView1 = (TextView) convertView.findViewById(R.id.tv1);
                holder.editText1 = (EditText) convertView.findViewById(R.id.ed1);
                holder.editText1.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                holder.textView3 = (TextView) convertView.findViewById(R.id.tv3);
                holder.editText2 = (EditText) convertView.findViewById(R.id.ed2);
                holder.editText2.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);


                holder.editText1.setTag(position + 1000);
                holder.editText2.setTag(position + 2000);

                class MyTextWatcher implements TextWatcher {

                    private ViewHolder mHolder;

                    public MyTextWatcher(ViewHolder holder) {

                        mHolder = holder;

                    }


                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s != null && !"".equals(s.toString())){
                            int position = (Integer) mHolder.editText1.getTag() - 1000;
                            try {
                                datas.get(position).put("purchasePrice", s.toString());
                                dataAdapter.setObjects(datas);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                class MyTextWatcher2 implements TextWatcher {

                    private ViewHolder mHolder;

                    public MyTextWatcher2(ViewHolder holder) {

                        mHolder = holder;

                    }


                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s != null && !"".equals(s.toString())){
                            int position = (Integer) mHolder.editText2.getTag() - 2000;
                            try {
                                datas.get(position).put("purchasePrice2", s.toString());
                                dataAdapter.setObjects(datas);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                holder.editText1.addTextChangedListener(new MyTextWatcher(holder));
                holder.editText2.addTextChangedListener(new MyTextWatcher2(holder));

                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
                holder.editText1.setTag(position + 1000);
                holder.editText2.setTag(position + 2000);
            }

            final JSONObject object = datas.get(position);

            try {
                String cigName = object.getString("cigName");
                String purchasePrice = object.getString("purchasePrice");
                String cigCode = object.getString("cigCode");
                String purchasePrice2 = object.getString("purchasePrice2");

                holder.textView3.setText(cigCode);
                holder.textView1.setText(cigName);

                if(purchasePrice.equals("0.0") || purchasePrice.equals("null")) purchasePrice = "";
                if(purchasePrice2.equals("0.0") || purchasePrice2.equals("null")) purchasePrice2 = "";

                holder.editText1.setText(purchasePrice);
                holder.editText2.setText(purchasePrice2);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return convertView;
        }

        final class ViewHolder {
            TextView textView1;
            EditText editText1;
            TextView textView3;
            EditText editText2;
        }
    }
}
