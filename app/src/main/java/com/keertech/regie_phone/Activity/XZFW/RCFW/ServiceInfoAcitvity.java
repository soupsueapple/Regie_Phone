package com.keertech.regie_phone.Activity.XZFW.RCFW;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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

import com.keertech.regie_phone.BaseActivity;
import com.keertech.regie_phone.Listener.ViewClickVibrate;
import com.keertech.regie_phone.Models.MarketInspect;
import com.keertech.regie_phone.Models.MarketInspectCig;
import com.keertech.regie_phone.R;
import com.keertech.regie_phone.Utility.StringUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    private String customerId = "";

    private TextView shopnameTv;
    private TextView licenseTv;
    private TextView address;
    private TextView nameTv;
    private TextView phoneTv;
    private TextView randomAbnormalTv;
    private TextView licenseAbnormalTv;
    private TextView businessAbnormalTv;
    private TextView apceAbnormalTv;
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
    private TextView outshopTv;
    private TextView markCheckTargetTv;

    private void assignViews() {
        shopnameTv = (TextView) findViewById(R.id.shopname_tv);
        licenseTv = (TextView) findViewById(R.id.license_tv);
        address = (TextView) findViewById(R.id.address);
        nameTv = (TextView) findViewById(R.id.name_tv);
        phoneTv = (TextView) findViewById(R.id.phone_tv);
        randomAbnormalTv = (TextView) findViewById(R.id.random_abnormal_tv);
        licenseAbnormalTv = (TextView) findViewById(R.id.license_abnormal_tv);
        businessAbnormalTv = (TextView) findViewById(R.id.business_abnormal_tv);
        apceAbnormalTv = (TextView) findViewById(R.id.apce_abnormal_tv);
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
        outshopTv = (TextView) findViewById(R.id.outshop_tv);
        markCheckTargetTv = (TextView) findViewById(R.id.mark_check_target_tv);

        abnormalInfoRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        abnormalInfoRecyclerview.setAdapter(recyclerAdapter);

        addAbnormalInfoTv.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                customDialog();
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

        final AlertDialog pd = new AlertDialog.Builder(ServiceInfoAcitvity.this).setTitle("提示").setView(layout).setPositiveButton("确定", new DialogInterface.OnClickListener(){

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_info);
        setToolbarTitle("服务");
        showBack();

        assignViews();
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
