package com.sjtu.bwphoto.memory.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.sjtu.bwphoto.memory.Class.Resource.Songresult;
import com.sjtu.bwphoto.memory.Class.Resource.Tag;
import com.sjtu.bwphoto.memory.Class.RestUtil;
import com.sjtu.bwphoto.memory.Class.ServerUrl;
import com.sjtu.bwphoto.memory.R;

import java.util.ArrayList;

public class TagActivity extends AppCompatActivity {
    private EditText TagName;
    private String tag_name;
    private String userName;
    private Button BtnConfirm;
    private Button BtnBack;
    private Button BtnTag1;
    private Button BtnTag2;
    private int res_id;
    private final static ServerUrl url = new ServerUrl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_tag);

        //Receive Data from last activity
        Bundle bundle = this.getIntent().getExtras();
        userName = bundle.getString("userName");
        res_id = bundle.getInt("res_id");

        TagName = (EditText) findViewById(R.id.tag);
        BtnConfirm = (Button) findViewById(R.id.confirm);
        BtnBack = (Button) findViewById(R.id.btn_back);
        BtnTag1 = (Button) findViewById(R.id.tag1);
        BtnTag2 = (Button) findViewById(R.id.tag2);

        BtnConfirm.setOnClickListener(mListener);
        BtnBack.setOnClickListener(mListener);
    }

    View.OnClickListener mListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.confirm:
                    confirm();
                    break;
                case R.id.btn_back:
                    back();
                    break;
                case R.id.tag1:
                    tag1();
                    break;
                case R.id.tag2:
                    tag2();
                    break;
            }
        }
    };

    public void back() {
        Intent intent = new Intent(TagActivity.this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userName", userName);
        intent.putExtras(bundle);
        startActivity(intent);
        TagActivity.this.finish();
    }

    public void confirm() {
        tag_name =  TagName.getText().toString().trim();
        Tag tag = new Tag();
        tag.setResource_id(res_id);
        tag.setTag_name(tag_name);
        String result = RestUtil.postForObject(url.url+"/resources/"+res_id+"/tags/"+tag_name, tag, String.class);
        System.out.println("Tag result: "+ result);
        if (result.contains("success")) {
            Toast.makeText(TagActivity.this, "Add tag success", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(TagActivity.this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("userName", userName);
            intent.putExtras(bundle);
            startActivity(intent);
            TagActivity.this.finish();
        }
    }

    public void tag1() {
        String s = BtnTag1.getText().toString();
        TagName.setText(BtnTag1.getText());
    }

    public void tag2() {
        String s = BtnTag2.getText().toString();
        TagName.setText(BtnTag2.getText());
    }

}
