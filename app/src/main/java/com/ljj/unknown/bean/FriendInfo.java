package com.ljj.unknown.bean;

import org.litepal.crud.DataSupport;

/**
 * 朋友信息类，保存在本地数据库用于朋友列表信息的展示及发送消息同步用户信息等 Created by hyc on 2017/8/8 14:20
 */

public class FriendInfo extends DataSupport{
    /**
     * 本地数据库中对应的id
     */
    private int id;
    /**
     * 云端数据库中所对应的object id
     */
    private String friendInfoId;
    /**
     *对用的用户id
     */
    private String userId;
    /**
     * 朋友的id
     */
    private String friendId;
    /**
     * 朋友的昵称
     */
    private String friendNickname;
    /**
     * 朋友的用户名
     */
    private String friendUsername;
    /**
     * 朋友的头像地址
     */
    private String friendHead;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getFriendNickname() {
        return friendNickname;
    }

    public void setFriendNickname(String friendNickname) {
        this.friendNickname = friendNickname;
    }

    public String getFriendUsername() {
        return friendUsername;
    }

    public void setFriendUsername(String friendUsername) {
        this.friendUsername = friendUsername;
    }

    public String getFriendHead() {
        return friendHead;
    }

    public void setFriendHead(String friendHead) {
        this.friendHead = friendHead;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFriendInfoId() {
        return friendInfoId;
    }

    public void setFriendInfoId(String friendInfoId) {
        this.friendInfoId = friendInfoId;
    }

}
