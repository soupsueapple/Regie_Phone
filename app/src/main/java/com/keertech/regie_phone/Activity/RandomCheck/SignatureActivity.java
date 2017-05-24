package com.keertech.regie_phone.Activity.RandomCheck;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.keertech.regie_phone.BaseActivity;
import com.keertech.regie_phone.Constant.Constant;
import com.keertech.regie_phone.View.SignatureView;

import java.io.File;

/**
 * Created by soup on 2016/12/20.
 */

public class SignatureActivity extends BaseActivity {

    SignatureView signatureView;

    Button save_bt, reset_bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarTitle(getIntent().getStringExtra("title"));

        setContentView(com.keertech.regie_phone.R.layout.signature_activity);

        signatureView = (SignatureView) findViewById(com.keertech.regie_phone.R.id.signatureView);
        save_bt = (Button) findViewById(com.keertech.regie_phone.R.id.save_bt);
        reset_bt = (Button) findViewById(com.keertech.regie_phone.R.id.reset_bt);

        save_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = String.valueOf(System.currentTimeMillis()) + ".PNG";
                File sdcard = Environment.getExternalStorageDirectory();
                String path = sdcard.getPath()+ File.separator+ Constant.Base_path;
                String fileName = path + File.separator + name;
                if(signatureView.saveBitmapToFile(fileName)){
                    signatureView.reset();
//                    Toast.makeText(SignatureActivity.this, "保存成功", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(SignatureActivity.this, RandomCheckInfoActivity.class);
                    intent.putExtra("fileName", fileName);
                    intent.putExtra("name", name);
                    setResult(RESULT_OK, intent);
                    finish();

                }else{
                    Toast.makeText(SignatureActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

        reset_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signatureView.reset();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
