package com.ljj.unknown.bean;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2017/7/30.
 */

public class User extends BmobUser {
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 头像地址
     */
    private String headUrl;
    /**
     * 性别
     */
    private String sex;
    /**
     * 地址
     */
    private String address;
    /**
     * 生日
     */
    private String birthDay;
    /**
     * 介绍
     */
    private String introduction;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getIntroducation() {
        return introduction;
    }

    public void setIntroducation(String introduction) {
        this.introduction = introduction;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
