package com.ljj.unknown.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ljj.unknown.R;
import com.ljj.unknown.bean.User;
import com.ljj.unknown.util.FriendUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/8/8.
 */

public class PersonalInformation extends BaseActivity implements View.OnClickListener {


    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.btn_add_friends)
    Button btnAddFriends;
    @Bind(R.id.btn_send_message)
    Button btnSendMessage;
    @Bind(R.id.tb_persona)
    Toolbar tbPersona;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalinformation);
        ButterKnife.bind(this);
        setToolBar(R.id.tb_persona);
        initHome();
        initialization();
    }

    private void initialization() {
        btnAddFriends.setOnClickListener(this);
        btnSendMessage.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if(v == btnAddFriends){

            FriendUtil.addFriend();
        }
    }
}
