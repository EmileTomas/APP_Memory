package com.sjtu.bwphoto.memory.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sjtu.bwphoto.memory.Class.Resource.Resource;
import com.sjtu.bwphoto.memory.Class.RestUtil;
import com.sjtu.bwphoto.memory.Class.ServerUrl;
import com.sjtu.bwphoto.memory.Class.Resource.Songresult;
import com.sjtu.bwphoto.memory.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AddMemoryActivity extends Activity {
    private Button BtnUpload;
    private Button BtnCancle;
    private Button BtnMusic;
    private Switch BtnPublic;
    private ImageView PicView;
    private ImageView FullView;
    private EditText Content;
    private String MemContent;
    private Boolean Sharable;
    private String userName;
    private String croppedName;
    private Uri imageUri;
    private Bitmap cropped;
    private int res_id;
    private Songresult song = new Songresult();

    private final static ServerUrl url = new ServerUrl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_memory);

        //Receive Data from last activity
        Bundle bundle = this.getIntent().getExtras();
        userName = bundle.getString("userName");
        res_id = bundle.getInt("res_id");
        croppedName = bundle.getString("croppedName");
        //imageUri = this.getIntent().getData();

//        try {
//            InputStream in = getContentResolver().o(croppedName);
//            cropped = BitmapFactory.decodeStream(in);
//            in.close();
//        } catch (FileNotFoundException e){
//            e.printStackTrace();
//        } catch (IOException e){
//            e.printStackTrace();
//        }

        cropped = BitmapFactory.decodeFile(croppedName);

        BtnUpload = (Button) findViewById(R.id.btn_upload);
        BtnCancle = (Button) findViewById(R.id.btn_cancle);
        BtnMusic = (Button) findViewById(R.id.add_music);
        BtnPublic = (Switch) findViewById(R.id.btn_public);
        PicView = (ImageView) findViewById(R.id.pic_view);
        FullView = (ImageView) findViewById(R.id.view_full);
        Content = (EditText) findViewById(R.id.edit_area);

        BtnCancle.setOnClickListener(mListener);
        BtnUpload.setOnClickListener(mListener);
        BtnMusic.setOnClickListener(mListener);

        PicView.setImageBitmap(cropped);
        //注册OnlongClick监听器
        PicView.setOnLongClickListener(new PicOnLongClick());

    }

    /*
     * Push Button to call function
     */
    View.OnClickListener mListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_cancle:
                    cancle();
                    break;
                case R.id.btn_upload:
                    upload();
                    break;
                case R.id.btn_public:
                    set_share();
                    break;
                case R.id.add_music:
                    add_music();
                    break;
            }
        }
    };

    public void upload() {
        MemContent =  Content.getText().toString().trim();
        Resource resource = new Resource();
        resource.setContent(MemContent);
        resource.setId(res_id);
        String result = RestUtil.postForObject(url.url+"/resources/"+res_id+"/words", resource, String.class);
//        String result2 = RestUtil.getForObject(url.url + "/resources/" + res_id + "/public", String.class);
        System.out.println(url.url+"/resources/"+res_id+"/words");
        if (result.contains("success")) {
            System.out.println("upload word Success!!!!!");
            Toast.makeText(AddMemoryActivity.this,"Upload success", Toast.LENGTH_SHORT);
        }
        else System.out.println("upload word Fail!!!!!");
        Intent intent = new Intent(AddMemoryActivity.this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userName",userName);
//        bundle.putInt("res_id",res_id);
        intent.putExtras(bundle);
        startActivity(intent);
        AddMemoryActivity.this.finish();
    }

    public void cancle() {
        Intent intent = new Intent(AddMemoryActivity.this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userName",userName);
        intent.putExtras(bundle);
        startActivity(intent);
        AddMemoryActivity.this.finish();
    }

    public void set_share() {
        Sharable = BtnPublic.getShowText();
        /*System.out.println("Sharable : " + Sharable);
        if (Sharable) {
            String result = RestUtil.getForObject(url.url + "/resources/" + res_id + "/public", String.class);
            System.out.println("Public result: " + result);
            Toast.makeText(AddMemoryActivity.this, "Set public", Toast.LENGTH_SHORT).show();
        }
        else{
            String result = RestUtil.getForObject(url.url + "/resources/" + res_id + "/private", String.class);
            System.out.println("Private result: " + result);
            Toast.makeText(AddMemoryActivity.this, "Set private", Toast.LENGTH_SHORT).show();
        }
        */
        String result = RestUtil.getForObject(url.url + "/resources/" + res_id + "/public", String.class);
    }

    public void add_music() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String httpUrl = "http://apis.baidu.com/geekery/music/query";
                String httpArg = "s=%E5%8D%81%E5%B9%B4&size=10&page=1";
                String jsonResult = request(httpUrl, httpArg);
                if (jsonResult != null) System.out.println("add_music:json result down line");
                System.out.println("Music result :"+jsonResult);
                try{
                    ObjectMapper objectMapper = new ObjectMapper();
                    song = objectMapper.readValue(jsonResult, Songresult.class);
                    String s = song.getData().getData().get(0).getHash();
                    if(s==null)System.out.println("s is null!!!!!!!!!!");
                    if(s!=null)System.out.println("s is not null!!!!!!!!!!");
                    System.out.println(s);
                } catch (IOException e){
                    e.printStackTrace();
                    System.out.println("AddMemoryMusic:164error");
                }
            }
        }).start();
        Intent intent = new Intent(AddMemoryActivity.this, MusicSearchActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userName", userName);
        bundle.putString("croppedName", croppedName);
        bundle.putInt("res_id", res_id);
        intent.putExtras(bundle);
        startActivity(intent);
        AddMemoryActivity.this.finish();
    }

    public static String request(String httpUrl, String httpArg) {
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        httpUrl = httpUrl + "?" + httpArg;
        System.out.println("AddMusicMemory : "+httpUrl);

        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            // 填入apikey到HTTP header
            connection.setRequestProperty("apikey", "fad22d042fecdcc0be3244f57faa757f");
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //长按图片放大
    private class PicOnLongClick implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View view){
            FullView.setImageBitmap(cropped);
            return true;
        }
    }

}
