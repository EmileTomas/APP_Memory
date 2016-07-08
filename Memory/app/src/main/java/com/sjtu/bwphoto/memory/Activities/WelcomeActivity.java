package com.sjtu.bwphoto.memory.Activities;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.sjtu.bwphoto.memory.TextView.SecretTextView;
import com.sjtu.bwphoto.memory.R;

public class WelcomeActivity extends Activity {
    SecretTextView secretTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);

        secretTextView = (SecretTextView)findViewById(R.id.textview1);
        secretTextView.setDuration(2300);
        secretTextView.show();
        secretTextView = (SecretTextView)findViewById(R.id.textview2);
        secretTextView.setDuration(2300);
        secretTextView.show();
        secretTextView = (SecretTextView)findViewById(R.id.textview3);
        secretTextView.setDuration(2300);
        secretTextView.show();

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
