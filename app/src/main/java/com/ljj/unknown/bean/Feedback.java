package com.ljj.unknown.bean;
import cn.bmob.v3.BmobObject;
/**
 * Created by Administrator on 2017/8/11.
 */

public class Feedback extends BmobObject {

    //反馈内容
    private String content;
    //联系方式
    private String contacts;

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getContacts() {
        return contacts;
    }
    public void setContacts(String contacts) {
        this.contacts = contacts;
    }



}

