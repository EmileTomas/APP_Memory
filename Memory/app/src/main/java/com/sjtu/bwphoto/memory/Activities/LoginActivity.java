package com.sjtu.bwphoto.memory.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.sjtu.bwphoto.memory.Class.AuthImageDownloader;
import com.sjtu.bwphoto.memory.Class.RestUtil;
import com.sjtu.bwphoto.memory.Class.ServerUrl;
import com.sjtu.bwphoto.memory.Class.User;
import com.sjtu.bwphoto.memory.R;

public class LoginActivity extends Activity {
    //各部件声明
    private EditText mAccount;
    private EditText mPwd;
    private Button mLoginButton;
    private Button mRegisterButton;
    //服务器地址
    private final static ServerUrl url = new ServerUrl();

    /*
    * Called when the activity is first created.
    */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        mAccount = (EditText) findViewById(R.id.login_edit_account);
        mPwd = (EditText) findViewById(R.id.login_edit_pwd);
        mLoginButton = (Button) findViewById(R.id.login_btn_login);
        mLoginButton.setOnClickListener(mListener);
        mRegisterButton = (Button) findViewById(R.id.loginToReg);
        mRegisterButton.setOnClickListener(mListener);
    }

    /*
     * Push Button to call function
     */
    OnClickListener mListener = new OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.login_btn_login:
                    login();
                    break;
                case R.id.loginToReg:
                    register();
                    break;
            }
        }
    };

    /*
     * Push register
     */
    public void register() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        LoginActivity.this.finish();
    }

    /*
     * Push login
     */
    public void login() {
        if (isUserNameAndPwdValid()) {
            //获取输入 包装为User对象 传到服务器
            String userName = mAccount.getText().toString().trim();
            String userPwd = mPwd.getText().toString().trim();
            User mUser = new User(userName,userPwd);
            System.out.println(url.url+"/login");
            String result = RestUtil.postForObject(url.url+"/login", mUser, String.class);
            System.out.println(result);

            // config the default imageloader
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                    .imageDownloader(new AuthImageDownloader(getApplicationContext()))
                    .build();
            ImageLoader.getInstance().destroy();
            ImageLoader.getInstance().init(config);

            //处理服务器返回结果
            if (result.contains("success")) {
                Toast.makeText(this, getString(R.string.login_sucess), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, CropperActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("userName",userName);
                intent.putExtras(bundle);
                startActivity(intent);
                LoginActivity.this.finish();
            }
            else {
                Toast.makeText(this, getString(R.string.login_fail),Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*
     * isvalid function
     */
    public boolean isUserNameAndPwdValid() {
        if (mAccount.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.account_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (mPwd.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.pwd_empty),
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
