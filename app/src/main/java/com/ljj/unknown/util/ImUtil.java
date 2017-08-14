package com.ljj.unknown.util;

import android.util.Log;

import com.ljj.unknown.bean.User;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.newim.listener.FileDownloadListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by hyc on 2017/8/11 14:19
 */

public class ImUtil {

    /**
     * 判断是否连接服务器
     */
    public static boolean isConnected = false;

    public static boolean hadError = false;

    public static void connectServer(){
        initConnectListener();
        if (BmobUser.getCurrentUser(User.class) == null){
            return;
        }
        BmobIM.connect(BmobUser.getCurrentUser(User.class).getObjectId(), new ConnectListener() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null){
                    hadError = false;
                }else {
                    hadError = true;
                }
            }
        });

    }

    public static void connectServer(final FriendUtil.OnFriendDealListener listener){
        initConnectListener();
        if (BmobUser.getCurrentUser(User.class) == null){
            return;
        }
        BmobIM.connect(BmobUser.getCurrentUser(User.class).getObjectId(), new ConnectListener() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null){
                    listener.onSuccess();
                    hadError = false;
                }else {
                    listener.onError(e.getMessage());
                    hadError = true;
                }
            }
        });

    }

    public static void initConnectListener(){
        BmobIM.getInstance().setOnConnectStatusChangeListener(new ConnectStatusChangeListener() {
            @Override
            public void onChange(ConnectionStatus status) {
                switch (status.getCode()){
                    //断开连接
                    case 0:
                        isConnected = false;
                        break;
                    //正在连接
                    case 1:
                        break;
                    //连接成功
                    case 2:
                        isConnected = true;
                        break;
                    //网络不可用
                    case -1:
                        isConnected = false;
                        break;
                    //另一台设备登录
                    case -2:
                        break;
                }
                Log.i("连接",status.getMsg() +"  "+status.getCode());
            }
        });
    }

}
