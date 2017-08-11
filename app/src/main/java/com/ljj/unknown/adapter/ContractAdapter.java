package com.ljj.unknown.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ljj.unknown.R;
import com.ljj.unknown.activity.ChatActivity;
import com.ljj.unknown.bean.FriendInfo;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by hyc on 2017/8/8 18:54
 */

public class ContractAdapter extends BaseQuickAdapter<FriendInfo>{


    public ContractAdapter(List<FriendInfo> data) {
        super(R.layout.item_contract, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final FriendInfo friendInfo) {
        if (TextUtils.isEmpty(friendInfo.getFriendHead())){
            Glide.with(mContext)
                    .load(R.drawable.ic_portrait_but)
                    .into((ImageView) baseViewHolder.getView(R.id.iv_item_friend_head));
        }else {
            Glide.with(mContext)
                    .load(friendInfo.getFriendHead())
                    .into((ImageView) baseViewHolder.getView(R.id.iv_item_friend_head));
        }
        if (TextUtils.isEmpty(friendInfo.getFriendNickname())){
            baseViewHolder.setText(R.id.tv_item_friend_nickname,friendInfo.getFriendUsername());
        }else {
            baseViewHolder.setText(R.id.tv_item_friend_nickname,friendInfo.getFriendNickname());
        }

        baseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobIMUserInfo info = new BmobIMUserInfo();
                info.setAvatar(friendInfo.getFriendHead());
                info.setUserId(friendInfo.getFriendId());
                if (TextUtils.isEmpty(friendInfo.getFriendNickname())){
                    info.setName(friendInfo.getFriendUsername());
                }else {
                    info.setName(friendInfo.getFriendNickname());
                }
                BmobIM.getInstance().startPrivateConversation(info, new ConversationListener() {
                    @Override
                    public void done(BmobIMConversation bmobIMConversation, BmobException e) {
                        if (e == null){
                            Intent intent = new Intent(mContext, ChatActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("ljj", bmobIMConversation);
                            intent.putExtras(bundle);
                            mContext.startActivity(intent);
                        }else {
                            Toast.makeText(mContext, "开启会话出错", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}
