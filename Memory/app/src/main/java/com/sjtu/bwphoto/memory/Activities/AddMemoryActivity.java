package com.sjtu.bwphoto.memory.Activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

import com.sjtu.bwphoto.memory.R;

public class AddMemoryActivity extends Activity {
    private Button BtnUpload;
    private Button BtnCancle;
    private Switch BtnPublic;
    private ImageView PicView;
    private EditText Content;
    private String MemContent;
    private Boolean Sharable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_memory);

        BtnUpload = (Button) findViewById(R.id.btn_upload);
        BtnCancle = (Button) findViewById(R.id.btn_cancle);
        BtnPublic = (Switch) findViewById(R.id.btn_public);
        PicView = (ImageView) findViewById(R.id.pic_view);
        Content = (EditText) findViewById(R.id.edit_area);

        BtnCancle.setOnClickListener(mListener);
        BtnUpload.setOnClickListener(mListener);

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
                    upload();
                    break;
                case R.id.btn_upload:
                    cancle();
                    break;
                case R.id.btn_public:
                    set_share();
                    break;
            }
        }
    };

    public void upload() {
        MemContent =  Content.getText().toString().trim();
        Intent intent = new Intent(AddMemoryActivity.this, MainActivity.class);
        startActivity(intent);
        AddMemoryActivity.this.finish();
    }

    public void cancle() {
        Intent intent = new Intent(AddMemoryActivity.this, MainActivity.class);
        startActivity(intent);
        AddMemoryActivity.this.finish();
    }

    public void set_share() {
        Sharable = true;
    }

    //长按图片放大
    private class PicOnLongClick implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View view){
            return true;
        }
    }

}
