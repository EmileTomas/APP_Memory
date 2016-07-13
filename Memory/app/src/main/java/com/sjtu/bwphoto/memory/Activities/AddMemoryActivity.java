package com.sjtu.bwphoto.memory.Activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.sjtu.bwphoto.memory.R;

public class AddMemoryActivity extends Activity {
    private Button BtnUpload;
    private Button BtnCancle;
    private ImageView PicView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_memory);

        BtnUpload = (Button) findViewById(R.id.btn_upload);
        BtnCancle = (Button) findViewById(R.id.btn_cancle);
        PicView = (ImageView) findViewById(R.id.pic_view);

        BtnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddMemoryActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        BtnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddMemoryActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //注册OnlongClick监听器
        PicView.setOnLongClickListener(new PicOnLongClick());

    }

    //长按图片放大
    private class PicOnLongClick implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View view){
            return true;
        }
    }

}
