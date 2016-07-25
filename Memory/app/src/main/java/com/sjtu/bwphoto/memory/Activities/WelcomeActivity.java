package com.sjtu.bwphoto.memory.Activities;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.sjtu.bwphoto.memory.TextView.SecretTextView;
import com.sjtu.bwphoto.memory.R;

public class WelcomeActivity extends Activity {
    SecretTextView secretTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        全屏显示
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);

//        动态文字部分
        secretTextView = (SecretTextView)findViewById(R.id.textview1);
        secretTextView.setDuration(2300);
        secretTextView.show();
        secretTextView = (SecretTextView)findViewById(R.id.textview2);
        secretTextView.setDuration(2300);
        secretTextView.show();
        secretTextView = (SecretTextView)findViewById(R.id.textview3);
        secretTextView.setDuration(2300);
        secretTextView.show();

//        自动跳转
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this, FirstActivity.class);
                startActivity(intent);
                WelcomeActivity.this.finish();
            }
        }, 2500);
    }
}
