package com.keertech.regie_phone;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.keertech.regie_phone.Constant.Constant;
import com.keertech.regie_phone.Listener.ViewClickVibrate;
import com.keertech.regie_phone.Utility.KeerAlertDialog;

import java.io.File;
import java.io.FileInputStream;

public abstract class BaseActivity extends AppCompatActivity {
    private LinearLayout contentView = null;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        if (mToolbar != null) {
            //将Toolbar显示到界面
            setSupportActionBar(mToolbar);
        }
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {

        if (contentView == null && R.layout.activity_base == layoutResID) {
            super.setContentView(R.layout.activity_base);
            contentView = (LinearLayout) findViewById(R.id.layout_center);
            contentView.removeAllViews();

        } else if (layoutResID != R.layout.activity_base) {
            View addView = LayoutInflater.from(this).inflate(layoutResID, null);
            contentView.addView(addView, new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

            //不要改变下面三者的顺序
            beforeSetActionBar();
            afterSettingActionBar();

        }
    }

    public void beforeSetActionBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setEnabled(true);
    }



    private void afterSettingActionBar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            //隐藏标题栏
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mToolbar.setNavigationOnClickListener(new ViewClickVibrate() {
            @Override
            public void onClick(View v) {
                super.onClick(v);
                finish();
            }
        });
    }

    protected void setToolbarTitle(String title){
        getToolbar().setTitle(title);
    }


    public void showBack(){
        mToolbar.setNavigationIcon(R.drawable.back);
    }

    protected void showToast(String string, Context context) {

        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(string);
            builder.setTitle("提示");
            builder.setPositiveButton("确认", null);
            builder.create().show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void showNetworkError(Context context) {
        this.showToast("服务链接错误", context);
    }

    protected KeerAlertDialog showKeerAlertDialog(int strID){
        return new KeerAlertDialog(this, getResources().getString(strID));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * this Activity of tool bar.
     * 获取头部.
     * @return support.v7.widget.Toolbar.
     */
    public Toolbar getToolbar() {
        return mToolbar;
    }

    protected File getFile(String name){
        File sdcard = Environment.getExternalStorageDirectory();
        String path = sdcard.getPath()+File.separator+ Constant.Base_path;
        String fileName = path + File.separator + name;
        File myCaptureFile = new File(fileName);

        return myCaptureFile;
    }

    protected Bitmap decodeFile(File f, int req_Height, int req_Width){
        try {
            //decode image size
            BitmapFactory.Options o1 = new BitmapFactory.Options();
            o1.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o1);


            //Find the correct scale value. It should be the power of 2.
            int width_tmp = o1.outWidth;
            int height_tmp = o1.outHeight;
            int scale = 1;

            if(width_tmp > req_Width || height_tmp > req_Height)
            {
                int heightRatio = Math.round((float) height_tmp / (float) req_Height);
                int widthRatio = Math.round((float) width_tmp / (float) req_Width);


                scale = heightRatio < widthRatio ? heightRatio : widthRatio;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            o2.inScaled = false;
            return BitmapFactory.decodeFile(f.getAbsolutePath(),o2);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }


}
