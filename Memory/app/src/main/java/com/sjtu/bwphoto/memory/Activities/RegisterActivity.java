package com.sjtu.bwphoto.memory.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sjtu.bwphoto.memory.Class.RestUtil;
import com.sjtu.bwphoto.memory.Class.ServerUrl;
import com.sjtu.bwphoto.memory.Class.User;
import com.sjtu.bwphoto.memory.R;

public class RegisterActivity extends Activity {
//    部件声明
    private EditText mAccount;
    private EditText mPwd;
    private EditText mEmail;
    private EditText mAge;
    private EditText mBirth;
    private EditText mContent;
    private Button mRegisterButton;
    private View registerView;

//    服务器地址
    private final static ServerUrl url = new ServerUrl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        mAccount = (EditText) findViewById(R.id.account);
        mPwd = (EditText) findViewById(R.id.pwd);
        mEmail = (EditText) findViewById(R.id.email);
        mAge = (EditText) findViewById(R.id.age);
        mBirth = (EditText) findViewById(R.id.birth);
        mContent = (EditText) findViewById(R.id.content);
        registerView=findViewById(R.id.login_view);
        mRegisterButton = (Button) findViewById(R.id.login_btn_register);
        mRegisterButton.setOnClickListener(mListener);
    }

    /*
     * Push Button to call function
     */
    View.OnClickListener mListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.login_btn_register:
                    register();
                    break;
            }
        }
    };

    /*
     * register function
     */
    public void register() {
        if (isValid()) {
            //获取输入
            String userName = mAccount.getText().toString().trim();
            String userPwd = mPwd.getText().toString().trim();
            String userEmail = mEmail.getText().toString().trim();
            int userAge = Integer.parseInt(mAge.getText().toString());
            String userBirth = mBirth.getText().toString().trim();
            String userContent = mContent.getText().toString();
            //形成user对象并传到服务器
            User mUser = new User(userName,userPwd,userEmail,userAge,userBirth,userContent);
            System.out.println(url.url+"/identity/reg");
            String result = RestUtil.postForObject(url.url+"/identity/reg", mUser, String.class);
            System.out.println(result);
            if (result.contains("success")){
                Toast.makeText(this, getString(R.string.register_sucess),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, FirstActivity.class);
                startActivity(intent);
                RegisterActivity.this.finish();
            }
            else {
                Toast.makeText(this, getString(R.string.register_fail),Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*
     * isvalid function
     */
    public boolean isValid() {
        if (mAccount.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.account_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (mPwd.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.pwd_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (mEmail.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.email_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (mAge.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.age_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (mBirth.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.birth_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (mContent.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.content_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
