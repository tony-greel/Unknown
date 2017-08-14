package com.ljj.unknown.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by hyc on 2017/8/12 18:16
 */

public class Post extends BmobObject {

    /**
     * 发布者
     */
    private User publisher;
    /**
     * 发布时间
     */
    private long publishTime;
    /**
     * 内容
     */
    private String content;
    /**
     * 心情图片
     */
    private String[] images;
    /**
     * 评论
     */
    private String[] comment;

    public User getPublisher() {
        return publisher;
    }

    public void setPublisher(User publisher) {
        this.publisher = publisher;
    }


    public long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }

    public String[] getComment() {
        return comment;
    }

    public void setComment(String[] comment) {
        this.comment = comment;
    }
}
