package com.keertech.regie_phone.Activity.XZFW.RCFW;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.keertech.regie_phone.BaseActivity;
import com.keertech.regie_phone.Constant.Constant;
import com.keertech.regie_phone.Listener.ViewClickVibrate;
import com.keertech.regie_phone.Models.Image;
import com.keertech.regie_phone.Network.HttpClient;
import com.keertech.regie_phone.R;
import com.keertech.regie_phone.Utility.ImagePagerActivity;
import com.keertech.regie_phone.Utility.KeerAlertDialog;
import com.keertech.regie_phone.Utility.StringUtility;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static com.keertech.regie_phone.R.id.recycler_view;

/**
 * Created by soup on 2017/5/8.
 */

public class ImageActivity extends BaseActivity{

    private ArrayList<Image> Uris;

    private RecyclerAdapter gridviewAdapter;

    private TextView takePhoto;
    private RecyclerView recyclerView;

    private DisplayMetrics dm;

    private final static int TAKE_ORIGINAL_PIC=127;

    KeerAlertDialog pd;

    private boolean isSingle = false;

    private String liceNo = "";

    private int onFailure = 0;

    Uri outputFileUri;

    private void assignViews() {
        takePhoto = (TextView) findViewById(R.id.take_photo);
        recyclerView = (RecyclerView) findViewById(recycler_view);

        takePhoto.setOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                selectFromTake();
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        setToolbarTitle("证据收集");
        showBack();

        getToolbar().setNavigationOnClickListener(new ViewClickVibrate(){

            @Override
            public void onClick(View view) {
                super.onClick(view);
                onDataResult();
                finish();
            }
        });

        assignViews();

        isSingle = getIntent().getBooleanExtra("isSingle", false);
        liceNo = getIntent().getStringExtra("liceNo");

        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        if(isSingle) Uris = (ArrayList<Image>) getIntent().getSerializableExtra("mtzUris");
        else Uris = (ArrayList<Image>) getIntent().getSerializableExtra("zjzUris");

        if(Uris==null) Uris = new ArrayList<>();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);

        recyclerView.setLayoutManager(gridLayoutManager);
        gridviewAdapter = new RecyclerAdapter();
        recyclerView.setAdapter(gridviewAdapter);
    }

    private void selectFromTake() {
        outputFileUri =Uri.fromFile(new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis()+".jpg"));

        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

        startActivityForResult(intent, TAKE_ORIGINAL_PIC);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == TAKE_ORIGINAL_PIC){
            if (resultCode==RESULT_OK){
                String name = isSingle?liceNo + ".jpg":String.valueOf(System.currentTimeMillis()) + ".jpg";

                try {
                    Bitmap bm= BitmapFactory.decodeStream(getContentResolver().
                            openInputStream(outputFileUri));

                    File sdcard = Environment.getExternalStorageDirectory();
                    String path = sdcard.getPath()+File.separator+ Constant.Base_path;
                    String fileName = path + File.separator + name;
                    File myCaptureFile = new File(fileName);
                    FileOutputStream out = new FileOutputStream(myCaptureFile);
					/* 采用压缩转档方法 */
                    bm.compress(Bitmap.CompressFormat.JPEG, 20, out);

					/* 调用flush()方法，更新BufferStream */
                    out.flush();

					/* 结束OutputStream */
                    out.close();

                    Image image = new Image();
                    image.setUrl(myCaptureFile.getAbsolutePath());
                    image.setName(name);
                    image.setSuccess(false);

                    if(isSingle) {
                        if(Uris.size()>0) Uris.clear();
                    }else if(Uris.size()==10){
                        Uris.remove(0);
                    }

                    Uris.add(image);
                    gridviewAdapter.notifyDataSetChanged();
//                    gridviewAdapter = new RecyclerAdapter();
//                    recyclerView.setAdapter(gridviewAdapter);
                    bm.recycle();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void uploadImage(File file, String filename,final int f){

        String action = isSingle?"marketInspect!uploadDoorP.action":"marketInspect!uploadEvidenceP.action";

        RequestParams params = new RequestParams();
        params.put("data", "{\"postHandler\":[],\"preHandler\":[],\"executor\":{\"url\":\"" + Constant.MWB_Base_URL + action + "\"," +
                "\"parameter\":{\"upload\":\"$uploadFile\"}," +
                "\"type\":\"WebExecutor\",\"method\":\"POST\"},\"app\":\"1001\"}");

        try {
            params.put("uploadFile",file);
            params.put("uploadFileName",filename);
            params.put("uploadContentType","image/jpeg");

            HttpClient.post(Constant.EXEC, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    System.out.println("response: " + response.toString());
                    if(f==Uris.size()-1) pd.dismiss();

                    try {
                        String messageSting = response.getString("message");
                        JSONObject message = new JSONObject(messageSting);
                        if(StringUtility.isSuccess(message)) {

                            Image image = Uris.get(f);
                            image.setSuccess(true);
                            image.setName(message.getString("newFileName"));

                        }else{
                            onFailure += 1;
                            if(f==Uris.size()-1){
                                pd.dismiss();
                                if(onFailure>0){
                                    showToast("有"+onFailure+"张照片上传失败,请重试", ImageActivity.this);
                                    onFailure = 0;
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    showNetworkError(ImageActivity.this);
                    super.onFailure(statusCode, headers, responseString, throwable);
                    onFailure += 1;
                    if(f==Uris.size()-1){
                        pd.dismiss();
                        if(onFailure>0){
                            showToast("有"+onFailure+"张照片上传失败,请重试", ImageActivity.this);
                            onFailure = 0;
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    onFailure += 1;
                    if(f==Uris.size()-1){
                        pd.dismiss();
                        if(onFailure>0){
                            showToast("有"+onFailure+"张照片上传失败,请重试", ImageActivity.this);
                            onFailure = 0;
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    onFailure += 1;
                    if(f==Uris.size()-1){
                        pd.dismiss();
                        if(onFailure>0){
                            showToast("有"+onFailure+"张照片上传失败,请重试", ImageActivity.this);
                            onFailure = 0;
                        }
                    }
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void onDataResult(){
        if(Uris.size()>0) {
            Bundle bundle = new Bundle();

            bundle.putSerializable(isSingle?"mtzUris":"zjzUris",Uris);
            Intent intent = new Intent();
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            onDataResult();
            finish();
        }

        return false;

    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>{

        @Override
        public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerHolder holder = new RecyclerHolder(LayoutInflater.from(ImageActivity.this).inflate(R.layout.activity_image_item, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerHolder holder, int position) {

            final int p = position;

            Image image = Uris.get(position);

            File file = new File(image.getUrl());

            Drawable drawable =new BitmapDrawable(decodeFile(file, 144, 226));
            holder.imageItem.setImageDrawable(drawable);

            holder.image_ll.setOnClickListener(new ViewClickVibrate(){

                @Override
                public void onClick(View view) {
                    super.onClick(view);
                    showItemDialog(p);
                }
            });

        }

        @Override
        public int getItemCount() {
            return Uris.size();
        }

        private void showItemDialog(final int p){

            String[] value = {"查看", "删除"};

            AlertDialog alertDialog = new AlertDialog.Builder(ImageActivity.this).setTitle("提示").setItems(value, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(which == 0){
                        Intent intent = new Intent(ImageActivity.this, ImagePagerActivity.class);
                        intent.putExtra("image_index", p);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("image_urls", Uris);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }else if(which == 1){
                        AlertDialog.Builder builder = new AlertDialog.Builder(ImageActivity.this);
                        builder.setMessage("是否删除照片");
                        builder.setTitle("提示");
                        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Image image = Uris.get(p);
                                File file = new File(image.getUrl());
                                if(file.exists()) file.delete();
                                Uris.remove(p);
                                gridviewAdapter.notifyDataSetChanged();
                            }
                        });
                        builder.setNegativeButton("取消", null);
                        builder.create().show();
                    }
                }
            }).create();
            alertDialog.show();
        }

        private Bitmap decodeFile(File f,int req_Height,int req_Width){
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

        class RecyclerHolder extends RecyclerView.ViewHolder{

            private ImageView imageItem;
            private LinearLayout image_ll;

            private void assignViews(View itemView) {
                imageItem = (ImageView) itemView.findViewById(R.id.image_iv);
                image_ll = (LinearLayout) itemView.findViewById(R.id.image_ll);
            }


            public RecyclerHolder(View itemView) {
                super(itemView);
                assignViews(itemView);
            }
        }
    }
}
