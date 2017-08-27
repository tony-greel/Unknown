package com.ljj.unknown.util;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.ljj.unknown.UnknownApplication;
import com.ljj.unknown.bean.FriendInfo;
import com.ljj.unknown.bean.Like;
import com.ljj.unknown.bean.LikeRecord;
import com.ljj.unknown.bean.Post;
import com.ljj.unknown.bean.User;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 朋友圈工具类 Created by hyc on 2017/8/13 09:38
 */

public class LifeUtil {




    public interface QueryLifeListener{
        void onSuccess(List<Post> data);
        void onError(String error);
    }

    public static void queryLifeUtil(int page , final QueryLifeListener lifeListener){
        BmobQuery<Post> query = new BmobQuery<>();
        //注释的部分为添加只查询自己好友发布帖子的功能，因查询条件过多而放弃
//        List<BmobQuery<Post>> queryList = new ArrayList<>();
//        for (FriendInfo friendInfo : FriendUtil.getFriendInfo(BmobUser.getCurrentUser(User.class))) {
//            User user = new User();
//            user.setObjectId(friendInfo.getFriendId());
//            BmobQuery<Post> subQuery = new BmobQuery<>();
//            subQuery.addWhereEqualTo("publisher",user);
//            queryList.add(subQuery);
//        }
//        BmobQuery<Post> subQuery = new BmobQuery<>();
//        subQuery.addWhereEqualTo("publisher", BmobUser.getCurrentUser(User.class));
//        queryList.add(subQuery);
        query.include("publisher");
//        query.or(queryList);
        query.setLimit(10);
        query.setSkip(page*10);
        query.order("-createdAt");
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (e == null){
                    lifeListener.onSuccess(list);
                }else {
                    if (e.getErrorCode() == 9016){
                        lifeListener.onError("网络不给力");
                    }else {
                        lifeListener.onError(e.getMessage());
                    }
                }
            }
        });
    }

    /**
     * 查询单个用户的相册信息
     * @param user   所需查询相册的发布人
     * @param page   查询的页码
     * @param lifeListener   查询的监听
     */
    public static void queryAlbum(User user,int page , final QueryLifeListener lifeListener){
        BmobQuery<Post> query = new BmobQuery<>();
        query.addWhereEqualTo("publisher", user);
        query.include("publisher");
        query.setLimit(20);
        query.setSkip(page*20);
        query.order("-createdAt");
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (e == null){
                    lifeListener.onSuccess(list);
                }else {
                    if (e.getErrorCode() == 9016){
                        lifeListener.onError("网络不给力");
                    }else {
                        lifeListener.onError(e.getMessage());
                    }
                }
            }
        });
    }

    public static Post comment(Post post, String msg, final FriendUtil.OnFriendDealListener listener){
        int size = 0;
        if (post.getComment() != null){
            size = post.getComment().length+1;
        }else {
            size++;
        }
        User user = BmobUser.getCurrentUser(User.class);
        String[] comment = new String[size];
        if (size > 1){
            for (int i = 0; i < post.getComment().length; i++) {
                comment[i] = post.getComment()[i];
            }
        }

        comment[size-1] = user.getObjectId()+"#"+(TextUtils.isEmpty(user.getNickname())?user.getUsername():user.getNickname())+"："+msg;
        Log.i("评论",comment[size-1]);
        post.getComment();
        Post post1 = new Post();
        post1.setComment(comment);
        post1.update(post.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    listener.onSuccess();
                }else {
                    if (e.getErrorCode() == 9016){
                        listener.onError("网络不可用");
                    }else {
                        listener.onError(e.getMessage());
                    }
                }
            }
        });

        post.setComment(comment);
        return post;
    }

    public static boolean hadLike(String postId){
        String userId = BmobUser.getCurrentUser(User.class).getObjectId();
        for (LikeRecord record : DataSupport.findAll(LikeRecord.class)) {
            if (record.getUserId().equals(userId) && record.getPostId().equals(postId)){
                return true;
            }
        }
        return false;
    }

    public static void likePost(Post post){
        User user = BmobUser.getCurrentUser(User.class);
        LikeRecord record =new LikeRecord();
        record.setUserId(user.getObjectId());
        record.setPostId(post.getObjectId());
        record.save();
        Like like = new Like();
        like.setLiker(user);
        like.setPost(post);
        like.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e != null && e.getErrorCode() == 9016){
                    Toast.makeText(UnknownApplication.getContext(),"网络不给力", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
