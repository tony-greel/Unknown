package com.ljj.unknown.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ljj.unknown.R;
import com.ljj.unknown.UnknownApplication;
import com.ljj.unknown.activity.DetailsActivity;
import com.ljj.unknown.bean.Friend;
import com.ljj.unknown.bean.Post;
import com.ljj.unknown.bean.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobUser;


/**
 * Created by Administrator on 2017/8/15.
 */

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.MyViewHolder> {

    private List<Post> postList;

    private Context context;

    public AlbumAdapter (Context context,List<Post> postList){
        this.postList=postList;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate
                (R.layout.item_album,parent,false));
    }

    @Override
    public void onBindViewHolder(AlbumAdapter.MyViewHolder holder, int position) {
        holder.load(postList.get(position),position);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView iv_head;
        private TextView tv_time;
        private TextView tv_content;
        private View item;


        public MyViewHolder(View itemView) {
            super(itemView);
            iv_head = (ImageView) itemView.findViewById(R.id.iv_image_picture);
            tv_time = (TextView) itemView.findViewById(R.id.tv_image_time);
            tv_content = (TextView) itemView.findViewById(R.id.tv_image_content);
            this.item = itemView;
        }

        public void load(final Post post, int position) {
            if (post.getImages() != null && post.getImages().length>0 ){
                iv_head.setVisibility(View.VISIBLE);
                Glide.with(UnknownApplication.getContext())
                        .load(post.getImages()[0])
                        .into(iv_head);
            }else {
                iv_head.setVisibility(View.GONE);
            }
            SimpleDateFormat format = new SimpleDateFormat("MM月dd日");
            tv_time.setText(format.format(new Date(post.getPublishTime())));
            tv_content.setText(post.getContent());
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("post",post);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }
    }

    public void setPosts(List<Post>posts){
        if (posts != null && posts.size() > 0){
            this.postList = posts;
            notifyDataSetChanged();
        }
    }

    public void addData(List<Post>data){ //加载更多
        if (data !=null && data.size()>0){
            for (Post post: data){
                postList.add(post);
                notifyItemInserted(postList.size()-1);
            }
        }
    }

}
