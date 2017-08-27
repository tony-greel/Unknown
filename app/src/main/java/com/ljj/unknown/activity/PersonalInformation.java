package com.ljj.unknown.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ljj.unknown.R;
import com.ljj.unknown.bean.FriendInfo;
import com.ljj.unknown.bean.MessageRequest;
import com.ljj.unknown.bean.User;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

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
    @Bind(R.id.iv_personalinform)
    ImageView ivPersonalinform;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalinformation);
        ButterKnife.bind(this);
        setToolBar(R.id.tb_persona);
        initHome();
        initialization();

        if (TextUtils.isEmpty(BmobUser.getCurrentUser(User.class).getHeadUrl())) {
            Glide.with(this)
                    .load(R.drawable.ic_portrait_but)
                    .into(ivPersonalinform);
        } else {
            Glide.with(this)
                    .load(BmobUser.getCurrentUser(User.class).getHeadUrl())
                    .into(ivPersonalinform);
        }
    }

    private void initialization() {
        btnAddFriends.setOnClickListener(this);
        btnSendMessage.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == btnAddFriends) {
            User user = (User) getIntent().getSerializableExtra("friend");
            BmobIMUserInfo info = new BmobIMUserInfo();
            info.setAvatar(user.getHeadUrl());
            info.setName(user.getUsername());
            info.setUserId(user.getObjectId());
            sendAddFriendMessage(info);
            return;
        }

        if (v == btnSendMessage) {
            User user = (User) getIntent().getSerializableExtra("friend");
            final BmobIMUserInfo info = new BmobIMUserInfo();
            info.setAvatar(user.getHeadUrl());
            info.setUserId(user.getObjectId());
            info.setName(user.getUsername());
            BmobIM.getInstance().startPrivateConversation(info, new ConversationListener() {
                @Override
                public void done(BmobIMConversation bmobIMConversation, BmobException e) {
                    if (e == null){
                        Intent intent = new Intent(PersonalInformation.this, ChatActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("ljj", bmobIMConversation);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }else {
                        Log.i("YAG",e.getMessage());
                        Toast.makeText(PersonalInformation.this, "开启会话出错", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void sendAddFriendMessage(BmobIMUserInfo info) {
        //启动一个会话，如果isTransient设置为true,则不会创建在本地会话表中创建记录，
        //设置isTransient设置为false,则会在本地数据库的会话列表中先创建（如果没有）与该用户的会话信息，且将用户信息存储到本地的用户表中
        BmobIM.getInstance().startPrivateConversation(info, true, new ConversationListener() {
            @Override
            public void done(BmobIMConversation bmobIMConversation, BmobException e) {
                if (e == null) {
                    //这个obtain方法才是真正创建一个管理消息发送的会话
                    BmobIMConversation conversation = BmobIMConversation.obtain(BmobIMClient.getInstance(), bmobIMConversation);
                    MessageRequest msg = new MessageRequest();
                    User currentUser = BmobUser.getCurrentUser(User.class);
                    msg.setContent("很高兴认识你，可以加个好友吗?");//给对方的一个留言信息
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", currentUser.getUsername());//发送者姓名，这里只是举个例子，其实可以不需要传发送者的信息过去
                    map.put("avatar", currentUser.getHeadUrl());//发送者的头像
                    map.put("uid", currentUser.getObjectId());//发送者的uid
                    msg.setExtraMap(map);
                    conversation.sendMessage(msg, new MessageSendListener() {
                        @Override
                        public void done(BmobIMMessage msg, BmobException e) {
                            if (e == null) {//发送成功
                                toastShow("好友请求发送成功，等待验证");
                            } else {//发送失败
                                toastShow("发送失败:" + e.getMessage());
                            }
                        }
                    });
                } else {
                    Toast.makeText(mActivity, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
