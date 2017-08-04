package com.ljj.unknown.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ljj.unknown.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import qiu.niorgai.StatusBarCompat;

/**
 * Created by Administrator on 2017/7/30.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    @Bind(R.id.long_img)
    ImageView longImg;
    @Bind(R.id.long_but)
    Button longBut;
    @Bind(R.id.et_login_username)
    EditText etLoginUsername;
    @Bind(R.id.et_login_password)
    EditText etLoginPassword;
    @Bind(R.id.linearLayout)
    LinearLayout linearLayout;
    @Bind(R.id.register_but)
    Button registerBut;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initial();//初始化

    }

    private void initial() {
        StatusBarCompat.translucentStatusBar(this);//去掉状态栏
        ButterKnife.bind(this);
        longBut.setOnClickListener(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.longin);
                longImg.startAnimation(animation);
            }
        }, 100);
    }

    @Override
    public void onClick(View v) {
        if (v == longBut) {
        }

        if(v == registerBut){
            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
        }
    }
}
