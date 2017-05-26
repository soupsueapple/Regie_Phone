package com.keertech.regie_phone.Activity.ProblemList;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.keertech.regie_phone.BaseActivity;
import com.keertech.regie_phone.Constant.Constant;
import com.keertech.regie_phone.Network.HttpClient;
import com.keertech.regie_phone.R;
import com.keertech.regie_phone.Utility.KeerAlertDialog;
import com.keertech.regie_phone.Utility.StringUtility;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by soup on 2017/5/26.
 */

public class ReportComplaintActivity extends BaseActivity{

    private ArrayList<JSONObject> datas = new ArrayList<>();

    RecyclerAdapter recyclerAdapter = new RecyclerAdapter();

    private RecyclerView recyclerView;

    private void assignViews() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);

        searchData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_list);
        setToolbarTitle("举报投诉");
        showBack();

        assignViews();
    }

    private void searchData(){

        final KeerAlertDialog pd = showKeerAlertDialog(R.string.loading);
        pd.show();

        HttpClient.get(Constant.EXEC + "?data=%7B%22postHandler%22:%5B%5D,%22preHandler%22:%5B%5D,%22executor%22:%7B%22url%22:%22" + Constant.MIIS_Base_URL +
                        "complanInformDispose!searchBeans.action%3fprivilegeFlag=VIEW" + "%26_query.statusFlag=true%22,%22type%22:%22WebExecutor%22%7D,%22app%22:%221003%22%7D",
                null, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        pd.dismiss();

                        if (datas.size() > 0) datas.clear();

                        try {
                            if(StringUtility.isSuccess(response)) {
                                System.out.println("response: " + response.toString());
                                String messageSting = response.getString("message");

                                JSONObject message = new JSONObject(messageSting);

                                if (StringUtility.isSuccess(message)) {
                                    JSONArray data = message.getJSONArray("data");

                                    for (int i = 0; i < data.length(); i++) {
                                        JSONObject object = data.getJSONObject(i);

                                        datas.add(object);
                                    }

                                    recyclerAdapter.notifyDataSetChanged();
                                }
                            }else {
                                showToast(response.getString("message"), ReportComplaintActivity.this);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        pd.dismiss();
                        showNetworkError(ReportComplaintActivity.this);
                        super.onFailure(statusCode, headers, responseString, throwable);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        pd.dismiss();
                        showNetworkError(ReportComplaintActivity.this);
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        pd.dismiss();
                        showNetworkError(ReportComplaintActivity.this);
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }
                });
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>{

        @Override
        public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerHolder holder = new RecyclerHolder(LayoutInflater.from(ReportComplaintActivity.this).inflate(R.layout.activity_problem_list_recyclerview_item, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerHolder holder, int position) {
            JSONObject object = datas.get(position);

            try {

                String billcode = object.getString("billcode");
                String customername = object.getString("customername");
                int reporttype = object.getInt("reporttype");
                String workdate = object.getString("workdate");
                String content = object.getString("content");
                int replystatus = object.getInt("workstatus");

                holder.problemNoTv.setText(billcode);
                holder.nameTv.setText(customername);
                holder.typeTv.setText(reporttype==2?"举报":"投诉");
                holder.dateTv.setText(workdate);
                holder.contentTv.setText(content);
                holder.statusTv.setText(replystatus==0?"未回复":"已回复");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        class RecyclerHolder extends RecyclerView.ViewHolder{

            private TextView problemNoTv;
            private TextView nameTv;
            private TextView typeTv;
            private TextView statusTv;
            private TextView contentTv;
            private TextView dateTv;

            private void assignViews(View itemView) {
                problemNoTv = (TextView) itemView.findViewById(R.id.problem_no_tv);
                nameTv = (TextView) itemView.findViewById(R.id.name_tv);
                typeTv = (TextView) itemView.findViewById(R.id.type_tv);
                statusTv = (TextView) itemView.findViewById(R.id.status_tv);
                contentTv = (TextView) itemView.findViewById(R.id.content_tv);
                dateTv = (TextView) itemView.findViewById(R.id.date_tv);
            }


            public RecyclerHolder(View itemView) {
                super(itemView);
                assignViews(itemView);
            }
        }
    }

}
