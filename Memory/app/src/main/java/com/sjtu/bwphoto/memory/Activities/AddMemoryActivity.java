package com.sjtu.bwphoto.memory.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

import com.sjtu.bwphoto.memory.Class.Resource;
import com.sjtu.bwphoto.memory.Class.RestUtil;
import com.sjtu.bwphoto.memory.Class.ServerUrl;
import com.sjtu.bwphoto.memory.R;

import org.springframework.web.client.RestTemplate;

public class AddMemoryActivity extends Activity {
    private Button BtnUpload;
    private Button BtnCancle;
    private Switch BtnPublic;
    private ImageView PicView;
    private EditText Content;
    private String MemContent;
    private Boolean Sharable;
    private String userName;
    private String croppedName;
    private Bitmap cropped;
    private int res_id;

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

        System.out.println("Add memory activity: "+croppedName);
        cropped = BitmapFactory.decodeFile(croppedName);
        if (cropped == null) System.out.println("Add memory activity: cropped image not get!!!!!!!!!");

//        Intent intent=getIntent();
//        if(intent!=null) {
//            System.out.println("add memory reached");
//            cropped = intent.getParcelableExtra("Image");
//        }

        BtnUpload = (Button) findViewById(R.id.btn_upload);
        BtnCancle = (Button) findViewById(R.id.btn_cancle);
        BtnPublic = (Switch) findViewById(R.id.btn_public);
        PicView = (ImageView) findViewById(R.id.pic_view);
        Content = (EditText) findViewById(R.id.edit_area);

        BtnCancle.setOnClickListener(mListener);
        BtnUpload.setOnClickListener(mListener);

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
            }
        }
    };

    public void upload() {
        MemContent =  Content.getText().toString().trim();
        Resource resource = new Resource();
        resource.setContent(MemContent);
        resource.setId(res_id);
        String result = RestUtil.postForObject(url.url+"/resources/"+res_id+"/words", resource, String.class);
        System.out.println(url.url+"/resources/"+res_id+"/words");
        if (result.contains("success")) System.out.println("upload word Success!!!!!");
        else System.out.println("upload word Fail!!!!!");
        Intent intent = new Intent(AddMemoryActivity.this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userName",userName);
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
    }

    //长按图片放大
    private class PicOnLongClick implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View view){
            return true;
        }
    }

}
