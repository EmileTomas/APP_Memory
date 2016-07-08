package com.sjtu.bwphoto.memory.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.sjtu.bwphoto.memory.R;

public class FirstActivity extends Activity {
    private Button RegisterButton;
    private Button LoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_first);

//        AssetManager mgr=getAssets();
//        Typeface tf= Typeface.createFromAsset(mgr, "fonts/幼圆.ttf");
//        TextView text = (TextView) findViewById(R.id.register);
//        text.setTypeface(tf);

        RegisterButton = (Button) findViewById(R.id.register);
        LoginButton = (Button) findViewById(R.id.login);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(FirstActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(FirstActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
