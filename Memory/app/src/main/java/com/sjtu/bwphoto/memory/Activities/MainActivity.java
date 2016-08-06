package com.sjtu.bwphoto.memory.Activities;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.sjtu.bwphoto.memory.Class.Resource.Resource;
import com.sjtu.bwphoto.memory.Class.RestUtil;
import com.sjtu.bwphoto.memory.Class.ServerUrl;
import com.sjtu.bwphoto.memory.Class.Util.FloatingActionsMenu;
import com.sjtu.bwphoto.memory.Class.Adapter.TabsPagerAdapter;
import com.sjtu.bwphoto.memory.Fragement.PersonalFragment;
import com.sjtu.bwphoto.memory.Fragement.RecentFragment;
import com.sjtu.bwphoto.memory.Fragement.RecommendFragment;
import com.sjtu.bwphoto.memory.R;

import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String IMAGE_UNSPECIFIED = "image/*";
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int ALBUM_REQUEST_CODE = 2;
    private static final int Scan_REQUEST_CODE = 3;
    private static final int Music_REQUEST_CODE = 4;
    private static final int COMMENT_VISIBLE = 1;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static  Context mainActivityContext;

    private String user_name;  // user name
    private Resource resource;  // resource
    private int res_id;  // resource id
    private String fileName;  // file name
    private Uri imageUri;  // image path

    private final static ServerUrl url = new ServerUrl();

    // Tab titles
    private PersonalFragment personalFragment;
    private RecentFragment recentFragment;
    private RecommendFragment recommendFragment;
    private ArrayList<Fragment> list_fragment;
    private ArrayList<String> list_tab;

    // widgets
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabsPagerAdapter tabsPagerAdapter;
    private FloatingActionsMenu menuMultipleActions;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    //Comment View
    private LinearLayout commentLayout;
    private ImageView outside;
    private EditText commentTextEdit;
    private InputMethodManager imm;
    /*
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivityContext=this;

        //Receive Data from last activity
        Bundle bundle = this.getIntent().getExtras();
        user_name = bundle.getString("userName");
        System.out.println(user_name);

        initial_widget();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }

    /*
     * initial_widget()
     */
    private void initial_widget() {
        menuMultipleActions = (FloatingActionsMenu) findViewById(R.id.menuFAB); // Floating Action Button
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

        //initial tab titles
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

        //Set Nav_View
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setScrimColor(getResources().getColor(android.R.color.transparent));
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //set CommentView
        //Here has some problem 可以通过创建新的View将之消费点击事件
        commentLayout = (LinearLayout) findViewById(R.id.commentView);
        commentTextEdit = (EditText) findViewById(R.id.commentBox);
        outside = (ImageView) findViewById(R.id.outside);
        imm = (InputMethodManager) commentTextEdit.getContext().getSystemService(Service.INPUT_METHOD_SERVICE);
        outside.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commentLayout.setVisibility(View.GONE);
                setFABState(2);
                imm.hideSoftInputFromWindow(commentTextEdit.getWindowToken(), 0);

            }
        });

        // Click camera button
        findViewById(R.id.cameraFAB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("camera button clicked !");
                takePic();
            }
        });

        // Click album button
        findViewById(R.id.albumFAB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("album button clicked !");
                getPic();
            }
        });

        // Click bar code button
        findViewById(R.id.bcodeFAB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("scan button clicked !");
                scan();
            }
        });

        //Click music button
        findViewById(R.id.bmusicFAB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("music button clicked !");
                getMusic();
            }
        });


    }


    /*
     * Take picture()
     */
    public void takePic() {
        //获取resource id
        System.out.println(url.url + "/resources");
        resource = RestUtil.postForObject(url.url + "/resources", null, Resource.class);
        res_id = resource.getId();
        System.out.println("MainActivity: Resource id get, " + res_id);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            //生成文件名
            new DateFormat();
            String name = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
            System.out.println("Image name formed: " + name);

            //------------------------通用位置---------------------failed
            //通过sd卡根目录存储访问，需传送imageUri，res_id，user_name到下一个activity
//            if (isSDCardCanUse()) {
//                File output = new File(Environment.getExternalStorageDirectory(),name);  //获取sd卡根目录
//                try {
//                    if (output.exists()){
//                        output.delete();
//                    }
//                    output.createNewFile();
//                } catch (IOException e){
//                    e.printStackTrace();
//                }
//                imageUri = Uri.fromFile(output);
//                Intent takePicIntent = new Intent("android.media.action.IMAGE_CAPTURE");
//                takePicIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);  //设置输出地址
//                startActivityForResult(takePicIntent, CAMERA_REQUEST_CODE);  //启动相机  返回CAMERA_REQUEST_CODE
//            }else{
//                Toast.makeText(MainActivity.this, "没有SD卡", Toast.LENGTH_LONG).show();
//            }
            //---------------------------------------------------------------

            //----------------store in /sdcard/DCIM/Camera-------------------
            //通过指定路径存储访问，需传送fileName，res_id，user_name到下一个activity
            fileName = "/sdcard/DCIM/Camera/" + name;
            if (isSDCardCanUse()) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(fileName)));
                takePictureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            } else {
                Toast.makeText(MainActivity.this, "没有SD卡", Toast.LENGTH_LONG).show();
            }
            //---------------------------------------------------------------
        }
    }

    /*
     * Choose photo from album
     */
    public void getPic() {
        //获取resource id
        System.out.println(url.url + "/resources");
        resource = RestUtil.postForObject(url.url + "/resources", null, Resource.class);
        res_id = resource.getId();
        System.out.println("MainActivity: Resource id get, " + res_id);
        //Toast.makeText(MainActivity.this,"Album Button clicked",Toast.LENGTH_LONG).show();
        Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
        albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(albumIntent, ALBUM_REQUEST_CODE);
    }

    /*
     * Scan bar code
     */
    public void scan() {
        //Toast.makeText(MainActivity.this,"Scan Button clicked",Toast.LENGTH_LONG).show();
        //获取resource id
        System.out.println(url.url + "/resources");
        resource = RestUtil.postForObject(url.url + "/resources", null, Resource.class);
        res_id = resource.getId();
        System.out.println("MainActivity: Resource id get, " + res_id);
        Intent intent = new Intent(MainActivity.this,ScanCaptureActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userName", user_name);
        bundle.putInt("res_id", res_id);
        intent.putExtras(bundle);
        startActivity(intent);
        MainActivity.this.finish();
    }

    /*
     * Get music
     */
    public void getMusic() {
        //获取resource id
        System.out.println(url.url + "/resources");
        resource = RestUtil.postForObject(url.url + "/resources", null, Resource.class);
        res_id = resource.getId();
        System.out.println("MainActivity: Resource id get, " + res_id);
        //default iamge
        System.out.println(url.url+"/resources/"+res_id+"/image");
        String croppedName = "music.jpg";
        File file = new File("/sdcard/DCIM/Camera/", croppedName);
        croppedName = "/sdcard/DCIM/Camera/"+croppedName;
        try {
            FileInputStream out = new FileInputStream(file);
            out.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String result = RestUtil.uploadFile(url.url+"/resources/"+res_id+"/image", new FileSystemResource(file), fileName,String.class);
        if (result.contains("success")){
            System.out.println("Add music: default image upload success!!!");
        }
        else System.out.println("Add music: default image upload fail!!!");
        //Toast.makeText(MainActivity.this,"Music Button clicked",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(MainActivity.this,MusicSearchActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userName", user_name);
        bundle.putString("croppedName", croppedName);
        bundle.putInt("res_id",res_id);
        intent.putExtras(bundle);
        startActivity(intent);
        MainActivity.this.finish();
    }

    /*
     * onActivityResult()
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Intent intent = new Intent(MainActivity.this, CropperActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("fileName", fileName);
                    //intent.setData(imageUri);
                    bundle.putString("userName", user_name);
                    bundle.putInt("res_id", res_id);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    MainActivity.this.finish();
                }
                break;

            case ALBUM_REQUEST_CODE:
                    if (data == null) {
                        System.out.println("MainActivity album result : data == null");
                        return;
                    }
                    Intent intent = new Intent(MainActivity.this, CropperActivity.class);
                    Bundle bundle = new Bundle();
                    Uri originalUri = data.getData();  //获得图片的uri
                    String[] proj = {MediaStore.Images.Media.DATA};
                    Cursor cursor = managedQuery(originalUri, proj, null, null, null);
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String path = cursor.getString(column_index);
                    System.out.println("main activity album path:" + path);
                    bundle.putString("fileName", path);
                    bundle.putString("userName", user_name);
                    bundle.putInt("res_id", res_id);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    MainActivity.this.finish();
                break;

            case Scan_REQUEST_CODE:
                if (requestCode == RESULT_OK) {

                }
                break;

            case Music_REQUEST_CODE:
                if (requestCode == RESULT_OK) {

                }
                break;

            default:
                break;
        }
    }

    /*
     * see if SD card is available
     */
    private boolean isSDCardCanUse() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /*
     * Create a file Uri for saving an image or video - from developer.android
     */
    private static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /*
     * Create a File for saving an image or video - from developer.android
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

    //used by MsgRecyleAdapter and fragments
    public View getMainActivityRootView(){
        return findViewById(R.id.drawer_layout);
    }

    public void setFABState(int state) {
        if (state == 1)
            menuMultipleActions.setVisibility(View.GONE);
        else if (state == 2) {
            Message msg = new Message();
            msg.what = COMMENT_VISIBLE;
            mHandler.sendMessageDelayed(msg, 350);
        }

    }

    //used by database
    public String getUserAccount() {
        return user_name;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        if (id == R.id.nav_notify) {

            Intent intent = new Intent(MainActivity.this, FriendApplyListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_friendlist) {

        }

        return true;
    }


    private android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case COMMENT_VISIBLE:
                    menuMultipleActions.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };
}