package com.ljj.unknown.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ljj.unknown.R;
import java.util.List;
import cn.bmob.newim.bean.BmobIMConversation;

/**
 * Created by hyc on 2017/8/4 11:39
 */

public class ConversationAdapter extends BaseQuickAdapter<BmobIMConversation> {

    public ConversationAdapter(List<BmobIMConversation> data) {
        super(R.layout.item_conversation, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, BmobIMConversation bmobIMConversation) {

    }
}
