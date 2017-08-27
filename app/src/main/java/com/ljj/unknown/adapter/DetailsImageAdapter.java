package com.ljj.unknown.adapter;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ljj.unknown.R;
import com.ljj.unknown.activity.ImageActivity;

import java.util.List;

/**
 * Created by hyc on 2017/7/8 10:44
 */

public class DetailsImageAdapter extends BaseQuickAdapter<String>  {

    /**
     * 适配器初始化
     * @param layoutResId   设置加载的布局文件Id
     * @param data          设置数据源
     */
    public DetailsImageAdapter(int layoutResId, List<String> data) {
        super(layoutResId,data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final String s) {
        Glide.with(mContext)
                .load(s)
                .into((ImageView) baseViewHolder.getView(R.id.item_details_image));
        baseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ImageActivity.class);
                intent.putExtra("image",s);
                mContext.startActivity(intent);
            }
        });
    }
}
