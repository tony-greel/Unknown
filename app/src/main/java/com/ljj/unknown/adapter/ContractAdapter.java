package com.ljj.unknown.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ljj.unknown.R;
import com.ljj.unknown.bean.FriendInfo;
import java.util.List;

/**
 * Created by hyc on 2017/8/8 18:54
 */

public class ContractAdapter extends BaseQuickAdapter<FriendInfo>{


    public ContractAdapter(List<FriendInfo> data) {
        super(R.layout.item_contract, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, FriendInfo friendInfo) {
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

    }
}
