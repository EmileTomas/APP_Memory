package com.sjtu.bwphoto.memory.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.sjtu.bwphoto.memory.R;

public class FirstActivity extends Activity {
//    登陆注册按钮声明
    private Button RegisterButton;
    private Button LoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_first);

//        绑定对应的配置文件元素
        RegisterButton = (Button) findViewById(R.id.register);
        LoginButton = (Button) findViewById(R.id.login);

//        设置相应动作
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(FirstActivity.this, LoginActivity.class);
                startActivity(intent);
                FirstActivity.this.finish();
            }
        });

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(FirstActivity.this, RegisterActivity.class);
                startActivity(intent);
                FirstActivity.this.finish();
            }
        });
    }
}
