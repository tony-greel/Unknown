package com.ljj.unknown.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by hyc on 2017/8/13 15:30
 */

public class Like extends BmobObject{

    private User liker ;

    private Post post;

    public User getLiker() {
        return liker;
    }

    public void setLiker(User liker) {
        this.liker = liker;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
