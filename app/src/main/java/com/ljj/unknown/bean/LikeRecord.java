package com.ljj.unknown.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by hyc on 2017/8/13 15:35
 */

public class LikeRecord extends DataSupport{

    private int id;

    private String postId;

    private String userId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
