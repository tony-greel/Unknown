package com.ljj.unknown.activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ljj.unknown.R;
import com.ljj.unknown.bean.User;
import com.ljj.unknown.util.FriendUtil;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.newim.event.MessageEvent;

public class NotificationActivity extends BaseActivity {

    @Bind(R.id.iv_notification)
    ImageView ivNotification;
    @Bind(R.id.tv_notification)
    TextView tvNotification;
    @Bind(R.id.ll_notification)
    LinearLayout llNotification;
    @Bind(R.id.btn_notification_agree)
    Button btnNotificationAgree;
    @Bind(R.id.btn_notification_refuse)
    Button btnNotificationRefuse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_notification_agree, R.id.btn_notification_refuse})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_notification_agree:
                MessageEvent event = (MessageEvent) getIntent().getSerializableExtra("apply");
                User user = new User();
                user.setObjectId(event.getMessage().getFromId());
                FriendUtil.addFriend(user, new FriendUtil.OnFriendDealListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(mActivity, "添加成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(mActivity, "添加失败", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.btn_notification_refuse:
                break;
        }
    }
}
