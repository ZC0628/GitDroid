package com.zc.githubdroid.splash;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import com.zc.githubdroid.MainActivity;
import com.zc.githubdroid.R;
import com.zc.githubdroid.commons.ActivityUtils;
import com.zc.githubdroid.login.LoginActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *    程序运行的第一个界面
 *    登录GITHUB和直接使用
 */
public class SplashActivity extends AppCompatActivity {

    private ActivityUtils activityUtils;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        activityUtils = new ActivityUtils(this);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.btnLogin) void login() {
        activityUtils.startActivity(LoginActivity.class);
        finish();
    }

    @OnClick(R.id.btnEnter) void enter() {
        activityUtils.startActivity(MainActivity.class);
        finish();
    }
}
