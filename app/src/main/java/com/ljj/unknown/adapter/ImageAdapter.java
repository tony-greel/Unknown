package com.ljj.unknown.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ljj.unknown.R;

import java.util.List;

/**
 * Created by hyc on 2017/8/13 08:48
 */

public class ImageAdapter extends BaseQuickAdapter<String> {

    public ImageAdapter(List<String> data) {
        super(R.layout.item_image, data);
    }

    private View.OnClickListener listener;

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, String s) {
        if (baseViewHolder.getAdapterPosition() == getData().size()-1){
            Glide.with(mContext)
                    .load(R.drawable.add_image)
                    .into((ImageView) baseViewHolder.getView(R.id.iv_item_image));
            if (listener != null){
                baseViewHolder.setOnClickListener(R.id.iv_item_image,listener);
            }
        }else {
            Glide.with(mContext)
                    .load(s)
                    .into((ImageView) baseViewHolder.getView(R.id.iv_item_image));
            baseViewHolder.setOnClickListener(R.id.iv_item_image, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            baseViewHolder.setOnLongClickListener(R.id.iv_item_image, new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    remove(baseViewHolder.getAdapterPosition());
                    return true;
                }
            });
        }

    }

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }
}
