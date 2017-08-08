package com.ljj.unknown.util;

import com.ljj.unknown.bean.Friend;
import com.ljj.unknown.bean.FriendInfo;
import com.ljj.unknown.bean.User;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;
import java.util.ArrayList;
import java.util.List;
import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by hyc on 2017/8/8 14:32
 */

public class FriendUtil {

    /**
     * 朋友类信息处理网络加载回调监听
     * 请求并执行成功会回调onSuccess方法
     * 失败回调onError方法
     */
    public interface OnFriendDealListener{
        void onSuccess();

        void onError(String error);
    }

    /**
     * 添加朋友，将用户信息和被添加人的信息统一成朋友类进行信息关联
     * @param friendUser 添加的朋友用户信息
     * @param listener   添加监听
     */
    public static void addFriend(final User friendUser , final OnFriendDealListener listener){
        Friend friend = new Friend();
        Friend friend1 = new Friend();
        friend.setUser(BmobUser.getCurrentUser(User.class));
        friend1.setUser(friendUser);
        friend.setFriend(friendUser);
        friend1.setFriend(BmobUser.getCurrentUser(User.class));
        List<BmobObject> friends= new ArrayList<>();
        new BmobBatch().insertBatch(friends).doBatch(new QueryListListener<BatchResult>() {
            @Override
            public void done(List<BatchResult> list, BmobException e) {
                if ( e== null ){
                    if (list.size() == 2){
                        saveFriendToLocal(list.get(0).getObjectId(),friendUser);
                        listener.onSuccess();
                    }
                }else {
                    if (e.getErrorCode() == 9016){
                        listener.onError("网络不给力");
                    }else {
                        listener.onError(e.getMessage());
                    }
                }
            }
        });
    }

    /**
     * 将朋友的信息保存到本地数据库
     * @param id 对应上传云端Friend类中的object id 信息
     * @param friendUser 添加朋友的信息
     */
    public static void saveFriendToLocal(String id,User friendUser){
        Connector.getDatabase();
        FriendInfo friendInfo = new FriendInfo();
        friendInfo.setUserId(BmobUser.getCurrentUser(User.class).getObjectId());
        friendInfo.setFriendId(friendUser.getObjectId());
        friendInfo.setFriendHead(friendUser.getHeadUrl());
        friendInfo.setFriendNickname(friendUser.getNickname());
        friendInfo.setFriendUsername(friendUser.getUsername());
        friendInfo.save();
    }

    /**
     * 删除本地数据库中的朋友信息
     * @param friendId 朋友的id信息
     */
    public static void deleteLocalFriend(String friendId){
        String userId = BmobUser.getCurrentUser(User.class).getObjectId();
        for (FriendInfo friendInfo : DataSupport.findAll(FriendInfo.class)) {
            if (friendInfo.getUserId().equals(userId) && friendInfo.getFriendId().equals(friendId)){
                friendInfo.delete();
                break;
            }
        }
    }

    /**
     * 删除朋友记录信息（注意点：删除需要传入friend 对象的object id
     * 所以需要先把FriendInfo中的friendInfoId赋值给这个friend中对应的object id）
     * 这里不会自动删除本地的朋友信息，需要自行调用deleteLocalFriend方法删除本地
     * 所对应的朋友信息
     * @param friend  朋友信息
     * @param listener 删除回调监听
     */
    public static void deleteFriendInfo(Friend friend , final OnFriendDealListener listener){
        friend.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if ( e== null){
                    listener.onSuccess();
                }else {
                    if (e.getErrorCode() == 9016){
                        listener.onError("网络不给力");
                    }else {
                        listener.onError(e.getMessage());
                    }
                }
            }
        });
    }

    /**
     *更新朋友信息，删除当前用户所对应的本地朋友信息，再请求云端朋友信息存入本地数据库。进行信息更新
     * @param user  对应的当前用户
     * @param listener  更新回调监听
     */
    public static void updateFriendInfo(User user, final OnFriendDealListener listener){
        String userId = BmobUser.getCurrentUser(User.class).getObjectId();
        for (FriendInfo friendInfo : DataSupport.findAll(FriendInfo.class)) {
            if (friendInfo.getUserId().equals(userId)){
                friendInfo.delete();
            }
        }
        BmobQuery<Friend> query = new BmobQuery<>();
        query.addWhereEqualTo("user",user);
        query.include("friend");
        query.findObjects(new FindListener<Friend>() {
            @Override
            public void done(List<Friend> list, BmobException e) {
                if (e == null){
                    for (Friend friend : list) {
                        saveFriendToLocal(friend.getObjectId(),friend.getFriend());
                    }
                    listener.onSuccess();
                }else{
                    if (e.getErrorCode() == 9016){
                        listener.onError("网络不给力");
                    }else {
                        listener.onError(e.getMessage());
                    }
                }
            }
        });
    }

    /**
     * 查询当前用户所对应的本地朋友用户信息
     * @param user  当前用户
     * @return      用户所对应的朋友信息
     */
    public static List<FriendInfo> getFriendInfo(User user){
        List<FriendInfo> friends = new ArrayList<>();
        for (FriendInfo friendInfo : DataSupport.findAll(FriendInfo.class)) {
            if (friendInfo.getUserId().equals(user.getObjectId())){
                friends.add(friendInfo);
            }
        }
        return friends;
    }

}
