package com.ljj.unknown.adapter;

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
//        Glide.with(mContext)
//                .load(friendInfo.getFriendHead())
//                .into((ImageView) baseViewHolder.getView(R.id.iv_item_friend_head));
//        baseViewHolder.setText(R.id.tv_item_friend_nickname,friendInfo.getFriendNickname());
    }
}
