package com.ljj.unknown.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.imnjh.imagepicker.SImagePicker;
import com.imnjh.imagepicker.activity.PhotoPickerActivity;
import com.ljj.unknown.R;
import com.ljj.unknown.adapter.ChatAdapter;
import com.ljj.unknown.bean.User;
import com.ljj.unknown.util.ImUtil;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.newim.bean.BmobIMAudioMessage;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMImageMessage;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.core.BmobRecordManager;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.newim.listener.MessagesQueryListener;
import cn.bmob.newim.listener.OnRecordChangeListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by hyc on 2017/8/11 14:15
 */

public class ChatActivity extends BaseActivity {


    @Bind(R.id.tv_chat_name)
    TextView tvChatName;
    @Bind(R.id.tb_chat)
    Toolbar tbChat;
    @Bind(R.id.rc_view)
    RecyclerView rcChat;
    @Bind(R.id.sw_refresh)
    SwipeRefreshLayout swRefresh;
    @Bind(R.id.iv_record)
    ImageView ivRecord;
    @Bind(R.id.tv_voice_tips)
    TextView tvVoiceTips;
    @Bind(R.id.layout_record)
    RelativeLayout layoutRecord;
    @Bind(R.id.btn_chat_add)
    Button btnChatAdd;
    @Bind(R.id.btn_chat_emo)
    Button btnChatEmo;
    @Bind(R.id.edit_msg)
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
    private int limit = 10;
    private Drawable[] drawable_Anims;
    BmobRecordManager recordManager;

    private ChatAdapter adapter;

    public static final int REQUEST_CODE_IMAGE = 101;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setToolBar(R.id.tb_chat);
        initHome();
        ButterKnife.bind(this);
        if (ImUtil.isConnected) {
            conversation = BmobIMConversation.obtain(BmobIMClient.getInstance(), (BmobIMConversation) getIntent().getSerializableExtra("ljj"));
            tvChatName.setText(conversation.getConversationTitle());
        }
        initVoice();
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

        conversation.queryMessages(null, limit, new MessagesQueryListener() {
            @Override
            public void done(List<BmobIMMessage> list, BmobException e) {
                if (e == null) {
                    adapter = new ChatAdapter(ChatActivity.this,list);
                    rcChat.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
                    rcChat.setItemAnimator(new DefaultItemAnimator());
                    rcChat.setAdapter(adapter);
                }
            }
        });

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.call){
            showProgressDialog();
            final String channel = BmobUser.getCurrentUser(User.class).getObjectId();
            BmobIMTextMessage msg = new BmobIMTextMessage();
            msg.setContent("#video^chat#");
            msg.setFromId(channel);
            conversation.sendMessage(msg, new MessageSendListener() {
                @Override
                public void done(BmobIMMessage bmobIMMessage, BmobException e) {
                    dismiss();
                    if (e == null){
                        Intent intent = new Intent(ChatActivity.this,VideoChatViewActivity.class);
                        intent.putExtra("channel",channel);
                        startActivity(intent);
                    }else {
                        Toast.makeText(mActivity, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            return true;
        }
        return super.onOptionsItemSelected(item);
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

    public void sendImageMessage(final String path) {
        BmobIMImageMessage image = new BmobIMImageMessage(path);
        image.setFromId(BmobUser.getCurrentUser(User.class).getObjectId());
        final int position = addTextMessage(image);
        conversation.sendMessage(image, new MessageSendListener() {
            @Override
            public void done(BmobIMMessage bmobIMMessage, BmobException e) {
                adapter.sendMessageSuccess(position);
            }
        });
    }

    public void sendVoiceMessage(String local, int length) {
        Toast.makeText(mActivity, "发送语音", Toast.LENGTH_SHORT).show();
        BmobIMAudioMessage audio = new BmobIMAudioMessage(local);
        audio.setDuration(length);
        audio.setFromId(BmobUser.getCurrentUser(User.class).getObjectId());
        final int position = addTextMessage(audio);
        conversation.sendMessage(audio, new MessageSendListener() {
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
                if (getCcamra() && getStorage()) {
                    SImagePicker
                            .from(ChatActivity.this)
                            .maxCount(1)
                            .rowCount(3)
                            .showCamera(true)
                            .pickMode(SImagePicker.MODE_IMAGE)
                            .forResult(REQUEST_CODE_IMAGE);
                }
                break;
            case R.id.btn_chat_voice:
                if(getRecord() && getStorage()) {
                    etChat.setVisibility(View.GONE);
                    btnChatVoice.setVisibility(View.GONE);
                    btnChatKeyboard.setVisibility(View.VISIBLE);
                    btnSpeak.setVisibility(View.VISIBLE);
                    hideSoftInputView();
                }
                break;
            case R.id.btn_chat_keyboard:
                etChat.setVisibility(View.VISIBLE);
                btnSpeak.setVisibility(View.GONE);
                btnChatVoice.setVisibility(View.VISIBLE);
                btnChatKeyboard.setVisibility(View.GONE);
                break;
            case R.id.btn_chat_send:
                sendTextMessage();
                break;
        }
    }

    private void hideSoftInputView() {
        InputMethodManager manager = ((InputMethodManager)
                this.getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (getWindow().getAttributes().softInputMode
                != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null) {
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BmobIMMessage event) {
        adapter.addNewMessage(rcChat, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_IMAGE) {
            final ArrayList<String> pathList = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT_SELECTION);
            final Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
            options.width = 600;
            options.height = 600;
            Tiny.getInstance()
                    .source(pathList.get(0))
                    .asFile()
                    .withOptions(options)
                    .compress(new FileCallback() {
                        @Override
                        public void callback(boolean isSuccess, String outfile) {
                            if (isSuccess) {
                                sendImageMessage(outfile);
                            } else {
                                Toast.makeText(ChatActivity.this, "压缩失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void initVoice() {
        btnSpeak.setOnTouchListener(new VoiceTouchListener());
        drawable_Anims = new Drawable[]{
                getResources().getDrawable(R.mipmap.chat_icon_voice2),
                getResources().getDrawable(R.mipmap.chat_icon_voice3),
                getResources().getDrawable(R.mipmap.chat_icon_voice4),
                getResources().getDrawable(R.mipmap.chat_icon_voice5),
                getResources().getDrawable(R.mipmap.chat_icon_voice6)};
        recordManager = BmobRecordManager.getInstance(this);
        recordManager.setOnRecordChangeListener(new OnRecordChangeListener() {
            @Override
            public void onVolumnChanged(int value) {
                ivRecord.setImageDrawable(drawable_Anims[value]);
            }

            @Override
            public void onTimeChanged(int recordTime, String localPath) {
                if (recordTime >= BmobRecordManager.MAX_RECORD_TIME) {
                    btnSpeak.setPressed(false);
                    btnSpeak.setClickable(false);
                    layoutRecord.setVisibility(View.INVISIBLE);
                    sendVoiceMessage(localPath, recordTime);
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            btnSpeak.setClickable(true);
                        }
                    }, 1000);
                }
            }
        });
    }

    class VoiceTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (!android.os.Environment.getExternalStorageState().equals(
                            android.os.Environment.MEDIA_MOUNTED)) {
                        toastShow("发送语音需要sdcard支持！");
                        return false;
                    }
                    try {
                        v.setPressed(true);
                        layoutRecord.setVisibility(View.VISIBLE);
                        tvVoiceTips.setText(getString(R.string.voice_cancel_tips));
                        // 开始录音
                        recordManager.startRecording(conversation.getConversationId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                case MotionEvent.ACTION_MOVE: {
                    if (event.getY() < 0) {
                        tvVoiceTips.setText(getString(R.string.voice_cancel_tips));
                        tvVoiceTips.setTextColor(Color.RED);
                    } else {
                        tvVoiceTips.setText(getString(R.string.voice_up_tips));
                        tvVoiceTips.setTextColor(Color.WHITE);
                    }
                    return true;
                }
                case MotionEvent.ACTION_UP:
                    v.setPressed(false);
                    layoutRecord.setVisibility(View.INVISIBLE);
                    try {
                        if (event.getY() < 0) {
                            recordManager.cancelRecording();
                        } else {
                            int recordTime = recordManager.stopRecording();
                            if (recordTime > 1) {
                                sendVoiceMessage(recordManager.getRecordFilePath(conversation.getConversationId()),recordTime);
                            } else {
                                layoutRecord.setVisibility(View.GONE);
                                showShortToast().show();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                default:
                    return false;
            }
        }
    }
    Toast toast;

    private Toast showShortToast() {
        if (toast == null) {
            toast = new Toast(this);
        }
        View view = LayoutInflater.from(this).inflate(
                R.layout.include_chat_voice_short, null);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        return toast;
    }

}
