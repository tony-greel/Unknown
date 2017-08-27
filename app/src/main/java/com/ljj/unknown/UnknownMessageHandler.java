package com.ljj.unknown;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;

import com.ljj.unknown.activity.NotificationActivity;
import com.ljj.unknown.bean.User;

import org.greenrobot.eventbus.EventBus;
import java.util.List;
import java.util.Map;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.BmobIMMessageHandler;
import cn.bmob.v3.BmobUser;


import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Administrator on 2017/7/30.
 */

public class UnknownMessageHandler extends BmobIMMessageHandler {

    @Override
    public void onMessageReceive(MessageEvent messageEvent) {
        super.onMessageReceive(messageEvent);
        excuteMessage(messageEvent);


    }

    @Override
    public void onOfflineReceive(OfflineMessageEvent offlineMessageEvent) {
        super.onOfflineReceive(offlineMessageEvent);
        Map<String,List<MessageEvent>> map =offlineMessageEvent.getEventMap();
        //挨个检测下离线消息所属的用户的信息是否需要更新
        for (Map.Entry<String, List<MessageEvent>> entry : map.entrySet()) {
            List<MessageEvent> list =entry.getValue();
            int size = list.size();
            for(int i=0;i<size;i++){
                excuteMessage(list.get(i));
            }
        }
    }

    private void excuteMessage(MessageEvent messageEvent) {
        if (messageEvent.getMessage().getMsgType().equals("add")){
            Intent intent = new Intent(UnknownApplication.getContext(), NotificationActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("apply",messageEvent);
            intent.putExtras(bundle);
            PendingIntent pi = PendingIntent.getActivity(UnknownApplication.getContext(),0,intent,0);
            NotificationManager manager = (NotificationManager)UnknownApplication.getContext().getSystemService(NOTIFICATION_SERVICE);
            Notification notification = new NotificationCompat.Builder(UnknownApplication.getContext())
                    .setContentTitle("好友申请")
                    .setContentText("。。。。。。")
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setLargeIcon(BitmapFactory.decodeResource(UnknownApplication.getContext().getResources(),R.mipmap.ic_launcher_round))
                    .setContentIntent(pi)
                    .build();
            manager.notify(1, notification);

            return;

        }else {
            EventBus.getDefault().post(messageEvent.getMessage());

        }
    }


}
