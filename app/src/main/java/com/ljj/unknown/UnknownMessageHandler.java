package com.ljj.unknown;

import org.greenrobot.eventbus.EventBus;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.BmobIMMessageHandler;

/**
 * Created by Administrator on 2017/7/30.
 */

public class UnknownMessageHandler extends BmobIMMessageHandler {

    @Override
    public void onMessageReceive(MessageEvent messageEvent) {
        super.onMessageReceive(messageEvent);
        EventBus.getDefault().post(messageEvent.getMessage());

    }

    @Override
    public void onOfflineReceive(OfflineMessageEvent offlineMessageEvent) {
        super.onOfflineReceive(offlineMessageEvent);

    }
}
