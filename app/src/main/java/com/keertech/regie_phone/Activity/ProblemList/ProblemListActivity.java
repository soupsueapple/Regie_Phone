package com.keertech.regie_phone.Activity.ProblemList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.keertech.regie_phone.BaseActivity;
import com.keertech.regie_phone.Listener.ViewClickVibrate;
import com.keertech.regie_phone.R;

/**
 * Created by soup on 2017/5/26.
 */

public class ProblemListActivity extends BaseActivity{

    String[] names = {"举报投诉", "案件督办", "证件期限", "市场调查", "APCD 反馈", "客户经理提报"};

    RecyclerAdapter recyclerAdapter = new RecyclerAdapter();

    private RecyclerView recyclerView;

    private void assignViews() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_list);
        setToolbarTitle("问题清单");
        showBack();

        assignViews();
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>{

        @Override
        public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerHolder holder = new RecyclerHolder(LayoutInflater.from(ProblemListActivity.this).inflate(R.layout.activity_problem_list_recyclerview_item, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerHolder holder, int position) {
            final String name = names[position];

            holder.nameTv.setText(name);

            holder.ll.setOnClickListener(new ViewClickVibrate(){
                Intent intent;

                @Override
                public void onClick(View view) {
                    super.onClick(view);
                    switch (name){
                        case "举报投诉":
                            intent = new Intent(ProblemListActivity.this, ReportComplaintActivity.class);
                            startActivity(intent);
                            break;
                        case "案件督办":
                            intent = new Intent(ProblemListActivity.this, CaseSuperViseActivity.class);
                            startActivity(intent);
                            break;
                        case "证件期限":
                            intent = new Intent(ProblemListActivity.this, PapersDeadlineActivity.class);
                            startActivity(intent);
                            break;
                        case "市场调查":
                            intent = new Intent(ProblemListActivity.this, MarketSurveyActivity.class);
                            startActivity(intent);
                            break;
                        case "APCD 反馈":
                            intent = new Intent(ProblemListActivity.this, APCDFeedbackActivity.class);
                            startActivity(intent);
                            break;
                        case "客户经理提报":
                            intent = new Intent(ProblemListActivity.this, ManagerReportActivity.class);
                            startActivity(intent);
                            break;
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return names.length;
        }

        class RecyclerHolder extends RecyclerView.ViewHolder{

            private LinearLayout ll;
            private TextView nameTv;

            private void assignViews(View itemView) {
                ll = (LinearLayout) itemView.findViewById(R.id.ll);
                nameTv = (TextView) itemView.findViewById(R.id.name_tv);
            }


            public RecyclerHolder(View itemView) {
                super(itemView);
                assignViews(itemView);
            }
        }
    }
}
