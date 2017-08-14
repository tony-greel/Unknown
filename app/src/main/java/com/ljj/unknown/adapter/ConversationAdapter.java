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

import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.v3.exception.BmobException;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hyc on 2017/8/4 11:39
 */

public class ConversationAdapter extends BaseQuickAdapter<BmobIMConversation> {

    public ConversationAdapter(List<BmobIMConversation> data) {
        super(R.layout.item_conversation, data);
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final BmobIMConversation bmobIMConversation) {
        if (TextUtils.isEmpty(bmobIMConversation.getConversationIcon())){
            Glide.with(mContext)
                    .load(R.drawable.ic_portrait_but)
                    .into((CircleImageView) baseViewHolder.getView(R.id.cv_message_head));
        }else {
            Glide.with(mContext)
                    .load(bmobIMConversation.getConversationIcon())
                    .into((CircleImageView) baseViewHolder.getView(R.id.cv_message_head));
        }
        baseViewHolder.setText(R.id.tv_message_nickname,bmobIMConversation.getConversationTitle());
        if (bmobIMConversation.getMessages().size() >0 ){
            baseViewHolder.setText(R.id.tv_message_content,bmobIMConversation.getMessages().get(0).getContent());
        }
        long distanceTime = System.currentTimeMillis() - bmobIMConversation.getUpdateTime();
        if (distanceTime < 1000L*60L*60L*24L){
            if (distanceTime < 1000L*60L*60L){
                if (distanceTime < 1000L*60L){
                    baseViewHolder.setText(R.id.tv_message_time,"刚刚");
                }else {
                    baseViewHolder.setText(R.id.tv_message_time,(distanceTime/(1000L*60L))+"分钟前");
                }
            }else {
                baseViewHolder.setText(R.id.tv_message_time,(distanceTime/(1000L*60L*60L))+"小时前");
            }
        }else {
            baseViewHolder.setText(R.id.tv_message_time,(distanceTime/(1000L*60L*60L*24L))+"天前");
        }

        baseViewHolder.setOnClickListener(R.id.rl_message_item, new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                BmobIMUserInfo info= new BmobIMUserInfo();
                info.setAvatar(bmobIMConversation.getConversationIcon());
                info.setUserId(bmobIMConversation.getConversationId());
                info.setName((bmobIMConversation.getConversationTitle()));
                BmobIM.getInstance().startPrivateConversation(info, new ConversationListener() {
                    @Override
                    public void done(BmobIMConversation bmobIMConversation, BmobException e) {
                        if(e == null){
                            Intent intent = new Intent(mContext, ChatActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("ljj", bmobIMConversation);
                            intent.putExtras(bundle);
                            mContext.startActivity(intent);
                        } else {
                            Toast.makeText(mContext, "开启会话出错", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
    }
}
