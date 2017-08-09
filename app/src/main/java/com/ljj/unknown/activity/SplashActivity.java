package com.ljj.unknown.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.ljj.unknown.R;
import com.ljj.unknown.bean.User;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2017/8/9.
 */

public class SplashActivity extends AppCompatActivity {

    public static final long SPLASH_DELAY_MILLIS = 2000;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                remember_long();
            }
        }, SPLASH_DELAY_MILLIS);
    }

    private void remember_long() {
        User user = BmobUser.getCurrentUser(User.class);
        if(user != null){
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            SplashActivity.this.startActivity(intent);
            SplashActivity.this.finish();
        }else {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            SplashActivity.this.startActivity(intent);
            SplashActivity.this.finish();
        }
    }
}
