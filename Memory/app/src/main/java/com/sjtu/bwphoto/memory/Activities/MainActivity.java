package com.sjtu.bwphoto.memory.Activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
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
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.sjtu.bwphoto.memory.Class.AuthImageDownloader;
import com.sjtu.bwphoto.memory.Class.Resource;
import com.sjtu.bwphoto.memory.Class.RestUtil;
import com.sjtu.bwphoto.memory.Class.ServerUrl;
import com.sjtu.bwphoto.memory.Class.User;
import com.sjtu.bwphoto.memory.Class.Util.FloatingActionButton;
import com.sjtu.bwphoto.memory.Class.Util.FloatingActionsMenu;
import com.sjtu.bwphoto.memory.Class.Util.TabsPagerAdapter;
import com.sjtu.bwphoto.memory.Fragement.PersonalFragment;
import com.sjtu.bwphoto.memory.Fragement.RecentFragment;
import com.sjtu.bwphoto.memory.Fragement.RecommendFragment;
import com.sjtu.bwphoto.memory.R;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private String user_name;
    private final static ServerUrl url = new ServerUrl();
    private Resource resource;
    private int res_id;

    // Camera Album
    private static final String IMAGE_UNSPECIFIED = "image/*";
    static final int CAMERA_REQUEST_CODE = 1;
    static final int ALBUM_REQUEST_CODE = 2;
    static final int CROP_REQUEST_CODE = 3;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private String fileName;
    private Uri imageUri;

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
    private FloatingActionsMenu menuMultipleActions;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Receive Data from last activity
        Bundle bundle = this.getIntent().getExtras();
        user_name = bundle.getString("userName");
        System.out.println(user_name);

        initial_widget();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }

    private void initial_widget() {
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager = (ViewPager) findViewById(R.id.pager);

        //initial fragments and organizes by list
        personalFragment = new PersonalFragment();
        recentFragment = new RecentFragment();
        recommendFragment = new RecommendFragment();
        list_fragment = new ArrayList<>();
        list_fragment.add(recentFragment);
        list_fragment.add(personalFragment);
        list_fragment.add(recommendFragment);

        //intial tab titles
        list_tab = new ArrayList<>();
        list_tab.add("Recent");
        list_tab.add("Personal");
        list_tab.add("Recommend");

        //set tabs
        tabLayout.addTab(tabLayout.newTab().setText(list_tab.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(list_tab.get(1)));
        tabLayout.addTab(tabLayout.newTab().setText(list_tab.get(2)));

        //set Adapter for ViewPager
        tabsPagerAdapter = new TabsPagerAdapter(this.getSupportFragmentManager(), list_fragment, list_tab);
        viewPager.setAdapter(tabsPagerAdapter);
        viewPager.setOffscreenPageLimit(2);

        //TabLayout加载viewpager
        tabLayout.setupWithViewPager(viewPager, true);

        // Floating Action Button
        menuMultipleActions = (FloatingActionsMenu) findViewById(R.id.menuFAB);

        // Click camera button
        findViewById(R.id.cameraFAB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取resource id
                resource = RestUtil.postForObject(url.url+"/resources",null, Resource.class);
                res_id = resource.getId();
                System.out.println(res_id);
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    //生成文件名
                    new DateFormat();
                    String name = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
                    System.out.println(name);

                    //------------------------通用位置---------------------
                    if (isSDCardCanUse()) {
                        File output = new File(Environment.getExternalStorageDirectory(),name);  //获取sd卡根目录
                        try {
                            if (output.exists()){
                                output.delete();
                            }
                            output.createNewFile();
                        } catch (IOException e){
                            e.printStackTrace();
                        }
                        imageUri = Uri.fromFile(output);
                        Intent takePicIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                        takePicIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);  //设置输出地址
                        startActivityForResult(takePicIntent, CAMERA_REQUEST_CODE);  //启动相机  返回CAMERA_REQUEST_CODE
                    }else{
                        Toast.makeText(MainActivity.this, "没有SD卡", Toast.LENGTH_LONG).show();
                    }
                    //---------------------------------------------------------------

                    //----------------store in /sdcard/DCIM/Camera-------------------
//                    fileName = "/sdcard/DCIM/Camera/" + name;
//                    if (isSDCardCanUse()) {
//                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(fileName)));
//                        takePictureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
//                        startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
//                    }else{
//                        Toast.makeText(MainActivity.this, "没有SD卡", Toast.LENGTH_LONG).show();
//                    }
                    //---------------------------------------------------------------
                }
            }
        });

        // Click album button
        findViewById(R.id.albumFAB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
                albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(albumIntent, ALBUM_REQUEST_CODE);
            }
        });

        // Click bar code button
        findViewById(R.id.bcodeFAB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    //跳转至裁剪
                    Intent intent = new Intent(MainActivity.this, CropperActivity.class);
                    Bundle bundle = new Bundle();
                    fileName="what";
                    bundle.putString("fileName", fileName);
                    intent.setData(imageUri);
                    bundle.putString("userName", user_name);
                    bundle.putInt("res_id", res_id);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    MainActivity.this.finish();
                }
                break;

            case ALBUM_REQUEST_CODE:
                if (requestCode == RESULT_OK) {
                    if (data == null) {
                        System.out.println("main acti : data == null");
                        return;
                    }
                    //跳转至裁剪
                    Intent intent = new Intent(MainActivity.this, CropperActivity.class);
                    Bundle bundle = new Bundle();
                    Uri originalUri = data.getData();  //获得图片的uri
                    String[] proj = {MediaStore.Images.Media.DATA};
                    Cursor cursor = managedQuery(originalUri, proj, null, null, null);
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String path = cursor.getString(column_index);
                    System.out.println("main acti path:"+path);
                    bundle.putString("fileName",path);
                    bundle.putString("userName",user_name);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    MainActivity.this.finish();
                }
                break;

            default:
                break;
        }
    }

    //if SD card is available
    private boolean isSDCardCanUse() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * Create a file Uri for saving an image or video
     */
    private static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * Create a File for saving an image or video
     */
    private static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    //used by database
    public String getUserAccount() {
        return user_name;
    }

    //used by MsgRecyleAdapter and fragments
    public void setFABState(int state){
        if(state==1)
            menuMultipleActions.setVisibility(View.GONE);
        else if(state==2)
            menuMultipleActions.setVisibility(View.VISIBLE);
    }

    public RecentFragment getRecentFragment(){
        return recentFragment;
    }
}