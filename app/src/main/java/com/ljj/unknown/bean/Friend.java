package com.ljj.unknown.bean;

import cn.bmob.v3.BmobObject;

/**
 * 朋友类，当用户添加一个朋友后，会在云端形成一条添加好友记录用于用户的好友信息同步和更新等功能 Created by hyc on 2017/8/8 14:17
 */

public class Friend extends BmobObject {
    /**
     * 对应的操作用户的个人信息
     */
    private User user;
    /**
     * 被添加用户的个人信息
     */
    private User friend;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getFriend() {
        return friend;
    }

    public void setFriend(User friend) {
        this.friend = friend;
    }
}
