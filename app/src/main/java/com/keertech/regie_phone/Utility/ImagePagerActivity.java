package com.keertech.regie_phone.Utility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.keertech.regie_phone.Constant.Constant;
import com.keertech.regie_phone.Models.Image;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

/**
 * Created by soup on 15/9/23.
 */
public class ImagePagerActivity extends FragmentActivity {

    private static final String STATE_POSITION = "STATE_POSITION";
    public static final String EXTRA_IMAGE_INDEX = "image_index";
    public static final String EXTRA_IMAGE_URLS = "image_urls";

    private HackyViewPager mPager;
    private int pagerPosition;
    private TextView indicator;

    private ArrayList<Image> urls;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.keertech.regie_phone.R.layout.image_detail_pager);

        pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
        urls = (ArrayList<Image>) getIntent().getSerializableExtra(EXTRA_IMAGE_URLS);

        mPager = (HackyViewPager) findViewById(com.keertech.regie_phone.R.id.pager);
        ImagePagerAdapter mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), urls);
        mPager.setAdapter(mAdapter);
        indicator = (TextView) findViewById(com.keertech.regie_phone.R.id.indicator);

        indicator.setText(pagerPosition + 1 +"/"+urls.size());
        // 更新下标
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int arg0) {

                indicator.setText(arg0 + 1 +"/"+urls.size());
            }

        });
        if (savedInstanceState != null) {
            pagerPosition = savedInstanceState.getInt(STATE_POSITION);
        }

        mPager.setCurrentItem(pagerPosition);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_POSITION, mPager.getCurrentItem());
    }

    private class ImagePagerAdapter extends FragmentStatePagerAdapter {

        public ArrayList<Image> fileList;

        public ImagePagerAdapter(FragmentManager fm, ArrayList<Image> fileList) {
            super(fm);
            this.fileList = fileList;
        }

        @Override
        public int getCount() {
            return fileList == null ? 0 : fileList.size();
        }

        @Override
        public Fragment getItem(int position) {
            Image image = fileList.get(position);
//            ImageDetailFragment imageDetailFragment = new ImageDetailFragment();
            return ImageDetailFragment.newInstance(image);
        }

    }

    public static class ImageDetailFragment extends Fragment {

        ImageView mImageView;
        String fileName;

//        public  ImageDetailFragment s(Image image) {
//             ImageDetailFragment f = new ImageDetailFragment();
//
//            final Bundle args = new Bundle();
//            args.putString("url", image.getName());
//            f.setArguments(args);
//
//            return f;
//        }

        public static ImageDetailFragment newInstance(Image image) {
            final ImageDetailFragment f = new ImageDetailFragment();

            final Bundle args = new Bundle();
            args.putString("url", image.getName());
            f.setArguments(args);

            return f;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            fileName = getArguments() != null ? getArguments().getString("url")
                    : null;
        }

        private File getFile(String name){
            File sdcard = Environment.getExternalStorageDirectory();
            String path = sdcard.getPath()+ File.separator+ Constant.Base_path;
            String fileName = path + File.separator + name;
            File myCaptureFile = new File(fileName);

            return myCaptureFile;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View v = inflater.inflate(com.keertech.regie_phone.R.layout.image_detail_fragment,
                    container, false);
            mImageView = (ImageView) v.findViewById(com.keertech.regie_phone.R.id.image);

//            mImageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ImagePagerActivity.this.finish();
//                }
//            });

            String[] names = fileName.split("/");
            final String name = names[names.length-1];

            File file = (getFile(name));

//            Bitmap bitmap  = BitmapFactory.decodeFile(file.getAbsolutePath());
//            Drawable drawable =new BitmapDrawable(bitmap);
//            mImageView.setImageDrawable(drawable);
//
//            File file = new File(image.getUrl());

            Drawable drawable =new BitmapDrawable(decodeFile(file, 144, 226));
            mImageView.setImageDrawable(drawable);

            return v;
        }

        private Bitmap decodeFile(File f, int req_Height, int req_Width){
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

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
        }
    }

}
