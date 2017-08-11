package com.ljj.unknown.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ljj.unknown.R;
import com.ljj.unknown.adapter.ChatAdapter;
import com.ljj.unknown.bean.User;
import com.ljj.unknown.util.ImUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by hyc on 2017/8/11 14:15
 */

public class ChatActivity extends BaseActivity {


    ChatAdapter adapter;
    @Bind(R.id.tv_chat)
    TextView tvChat;
    @Bind(R.id.tb_chat)
    Toolbar tbChat;
    @Bind(R.id.rc_chat)
    RecyclerView rcChat;
    @Bind(R.id.btn_chat_add)
    Button btnChatAdd;
    @Bind(R.id.btn_chat_emo)
    Button btnChatEmo;
    @Bind(R.id.et_chat)
    EditText etChat;
    @Bind(R.id.btn_speak)
    Button btnSpeak;
    @Bind(R.id.btn_chat_voice)
    Button btnChatVoice;
    @Bind(R.id.btn_chat_keyboard)
    Button btnChatKeyboard;
    @Bind(R.id.btn_chat_send)
    Button btnChatSend;

    private BmobIMConversation conversation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setToolBar(R.id.tb_chat);
        initHome();
        ButterKnife.bind(this);
        if (ImUtil.isConnected) {
            conversation = BmobIMConversation.obtain(BmobIMClient.getInstance(), (BmobIMConversation) getIntent().getSerializableExtra("ljj"));
        }

        etChat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(etChat.getText().toString())) {
                    btnChatVoice.setVisibility(View.VISIBLE);
                    btnChatSend.setVisibility(View.GONE);
                } else {
                    btnChatSend.setVisibility(View.VISIBLE);
                    btnChatVoice.setVisibility(View.GONE);
                }
            }
        });
        List<BmobIMMessage> messages = new ArrayList<>();
        adapter = new ChatAdapter(messages);
        rcChat.setLayoutManager(new LinearLayoutManager(this));
        rcChat.setItemAnimator(new DefaultItemAnimator());
        rcChat.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public void sendTextMessage() {
        BmobIMTextMessage msg = new BmobIMTextMessage();
        String message = etChat.getText().toString();
        msg.setContent(message);
        etChat.setText("");
        msg.setFromId(BmobUser.getCurrentUser(User.class).getObjectId());
        final int position = addTextMessage(msg);
        conversation.sendMessage(msg, new MessageSendListener() {
            @Override
            public void done(BmobIMMessage bmobIMMessage, BmobException e) {
                adapter.sendMessageSuccess(position);
            }
        });
    }

    public int addTextMessage(BmobIMMessage bmobIMMessage) {
        adapter.addNewMessage(rcChat, bmobIMMessage);
        return adapter.getItemCount() - 1;
    }


    @OnClick({R.id.btn_chat_add, R.id.btn_chat_voice, R.id.btn_chat_keyboard, R.id.btn_chat_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_chat_add:
                break;
            case R.id.btn_chat_voice:
                break;
            case R.id.btn_chat_keyboard:
                break;
            case R.id.btn_chat_send:
                sendTextMessage();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BmobIMMessage event){
        adapter.addNewMessage(rcChat,event);
    }
}
