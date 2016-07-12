package com.sjtu.bwphoto.memory.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.renderscript.RenderScript;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;

import com.sjtu.bwphoto.memory.Class.Util.FloatingActionsMenu;
import com.sjtu.bwphoto.memory.Class.Util.TabsPagerAdapter;
import com.sjtu.bwphoto.memory.Fragement.PersonalFragment;
import com.sjtu.bwphoto.memory.Fragement.RecentFragment;
import com.sjtu.bwphoto.memory.Fragement.RecommendFragment;
import com.sjtu.bwphoto.memory.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private String user_name;

    // Camera Album
    private static final String IMAGE_UNSPECIFIED = "image/*";
    static final int CAMERA_REQUEST_CODE = 1;
    static final int ALBUM_REQUEST_CODE = 2;
    static final int CROP_REQUEST_CODE = 3;
    private ImageView mImageView;

    // Tab titles
    private PersonalFragment personalFragment;
    private RecentFragment recentFragment;
    private RecommendFragment recommendFragment;
    private ArrayList<Fragment> list_fragment;
    private ArrayList<String> list_tab;

    //widgets
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabsPagerAdapter tabsPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Receive Data from last activity
        Bundle bundle = this.getIntent().getExtras();
        user_name = bundle.getString("userName");

        mImageView = (ImageView) findViewById(R.id.mImageView);

        // Floating Action Button
        final FloatingActionsMenu menuMultipleActions = (FloatingActionsMenu) findViewById(R.id.menuFAB);

        findViewById(R.id.cameraFAB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //.makeText(MainActivity.this, "Clicked camera Floating Action Button", Toast.LENGTH_SHORT).show();
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
                }
            }
        });

        findViewById(R.id.albumFAB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "Clicked album Floating Action Button", Toast.LENGTH_SHORT).show();
                Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
                albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(albumIntent, ALBUM_REQUEST_CODE);
            }
        });

        initial_widget();
    }

    private void initial_widget() {
        tabLayout=(TabLayout) findViewById(R.id.tablayout);
        viewPager =(ViewPager) findViewById(R.id.pager);

        //initial fragments and organizes by list
        personalFragment=new PersonalFragment();
        recentFragment= new RecentFragment();
        recommendFragment = new RecommendFragment();
        list_fragment = new ArrayList<>();
        list_fragment.add(recentFragment);
        list_fragment.add(personalFragment);
        list_fragment.add(recommendFragment);

        //intial tab titles
        list_tab =new ArrayList<>();
        list_tab.add("Recent");
        list_tab.add("Personal");
        list_tab.add("Recommend");

        //set tabs
        tabLayout.addTab(tabLayout.newTab().setText(list_tab.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(list_tab.get(1)));
        tabLayout.addTab(tabLayout.newTab().setText(list_tab.get(2)));

        //set Adapter for ViewPager
        tabsPagerAdapter = new TabsPagerAdapter(this.getSupportFragmentManager(),list_fragment,list_tab);
        viewPager.setAdapter(tabsPagerAdapter);
        //TabLayout加载viewpager
        tabLayout.setupWithViewPager(viewPager);

    }

    // Get the Thumbnail -- 显示拍到的照片
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(imageBitmap);
        }
        if (requestCode == ALBUM_REQUEST_CODE && requestCode == RESULT_OK) {
            if (data == null) return;
            startCrop(data.getData());
        }
    }

    /**
     * 一个简易的裁剪
     *
     * @param uri
     */
    private void startCrop(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");//调用Android系统自带的一个图片剪裁页面,
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");//进行修剪
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 500);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_REQUEST_CODE);
    }

    /**
     * 判断sdcard卡是否可用
     * @return 布尔类型 true 可用 false 不可用
     */
    private boolean isSDCardCanUser() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
}