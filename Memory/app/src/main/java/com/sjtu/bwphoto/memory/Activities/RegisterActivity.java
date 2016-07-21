package com.sjtu.bwphoto.memory.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sjtu.bwphoto.memory.Class.RestUtil;
import com.sjtu.bwphoto.memory.Class.ServerUrl;
import com.sjtu.bwphoto.memory.Class.User;
import com.sjtu.bwphoto.memory.R;

import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;

public class RegisterActivity extends Activity {

    private EditText mAccount;
    private EditText mPwd;
    private EditText mEmail;
    private EditText mAge;
    private EditText mBirth;
    private EditText mContent;

    //private DatePicker datePicker;

    private Button mRegisterButton;
    private View registerView;
    //private static RestTemplate restTp = new RestTemplate();
    private final static ServerUrl url = new ServerUrl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                .penaltyLog().penaltyDeath().build());

//        datePicker = (DatePicker) findViewById(R.id.dpPicker);
//
//        datePicker.init(2013, 8, 20, new DatePicker.OnDateChangedListener() {
//            @Override
//            public void onDateChanged(DatePicker view, int year,int monthOfYear, int dayOfMonth) {
//                // 获取一个日历对象，并初始化为当前选中的时间
//                java.util.Calendar calendar = java.util.Calendar.getInstance();
//                calendar.set(year, monthOfYear, dayOfMonth);
//                SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日  HH:mm");
//                Toast.makeText(RegisterActivity.this,format.format(calendar.getTime()), Toast.LENGTH_SHORT).show();
//            }
//        });

        mAccount = (EditText) findViewById(R.id.account);
        mPwd = (EditText) findViewById(R.id.pwd);
        mEmail = (EditText) findViewById(R.id.email);
        mAge = (EditText) findViewById(R.id.age);
        mBirth = (EditText) findViewById(R.id.birth);
        mContent = (EditText) findViewById(R.id.content);

        mRegisterButton = (Button) findViewById(R.id.login_btn_register);
        mRegisterButton.setOnClickListener(mListener);

        registerView=findViewById(R.id.login_view);
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

    public void register() {
        if (isValid()) {
            String userName = mAccount.getText().toString().trim();
            String userPwd = mPwd.getText().toString().trim();
            String userEmail = mEmail.getText().toString().trim();
            int userAge = Integer.parseInt(mAge.getText().toString());
            String userBirth = mBirth.getText().toString().trim();
            String userContent = mContent.getText().toString();
            User mUser = new User(userName,userPwd,userEmail,userAge,userBirth,userContent);
            String result = RestUtil.getSession().postForObject(url.url+"/identity/reg", mUser, String.class);
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
        } /*else if (mEmail.getText().toString().contains("@")) {
            Toast.makeText(this, getString(R.string.email_invalid),
                    Toast.LENGTH_SHORT).show();
            return false;
        }*/
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
