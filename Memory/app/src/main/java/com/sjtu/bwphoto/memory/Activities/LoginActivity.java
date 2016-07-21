package com.sjtu.bwphoto.memory.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sjtu.bwphoto.memory.Class.RestUtil;
import com.sjtu.bwphoto.memory.Class.ServerUrl;
import com.sjtu.bwphoto.memory.Class.User;
import com.sjtu.bwphoto.memory.R;

import org.springframework.web.client.RestTemplate;

public class LoginActivity extends Activity {
    private EditText mAccount;
    private EditText mPwd;
    private Button mLoginButton;
    private Button mRegisterButton;
    //private static RestTemplate restTp = new RestTemplate();
    private final static ServerUrl url = new ServerUrl();

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                .penaltyLog().penaltyDeath().build());

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

    public void register() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        LoginActivity.this.finish();
    }

    public void login() {
        if (isUserNameAndPwdValid()) {
            String userName = mAccount.getText().toString().trim();
            String userPwd = mPwd.getText().toString().trim();
            User mUser = new User(userName,userPwd);
            System.out.println(url.url+"/login");
            String result = RestUtil.getSession().postForObject(url.url+"/login", mUser, String.class);
            System.out.println(result.toString());
            System.out.println(userName);
            if (result.contains("success")) {
                Toast.makeText(this, getString(R.string.login_sucess), Toast.LENGTH_SHORT).show(); //simple information display
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("userName",userName);
                intent.putExtras(bundle);
                startActivity(intent);
                LoginActivity.this.finish();
            }
            else {
                //login failed,user does't exist
                Toast.makeText(this, getString(R.string.login_fail),Toast.LENGTH_SHORT).show();
            }
        }
    }

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
