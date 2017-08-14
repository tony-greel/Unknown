package com.ljj.unknown.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.ljj.unknown.R;
import com.ljj.unknown.UnknownApplication;
import com.ljj.unknown.bean.Post;
import com.ljj.unknown.util.FriendUtil;
import com.ljj.unknown.util.LifeUtil;
import com.sackcentury.shinebuttonlib.ShineButton;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.ljj.unknown.UnknownApplication.getContext;

/**
 * Created by hyc on 2017/8/12 18:38
 */

public class LifeAdapter extends RecyclerView.Adapter {

    private List<Post> posts;

    private int width;
    private int height;

    public LifeAdapter(){
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();
    }

    @Override
    public int getItemViewType(int position) {
        int imageCount = 0;
        if (posts.get(position).getImages() != null) {
            imageCount = posts.get(position).getImages().length;
        }
        if (imageCount < 2) {

            return 1;
        } else if (imageCount == 2) {
            return 2;
        } else {
            return 3;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
                return new LifeOneImageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_one_life, parent, false));
            case 2:
                return new LifeTwoImageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_two_life, parent, false));
            case 3:
                return new LifeThereImageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_there_life, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LifeOneImageViewHolder){
            LifeOneImageViewHolder holder1 = (LifeOneImageViewHolder) holder;
            holder1.load(posts.get(position),position);
        }else if (holder instanceof LifeTwoImageViewHolder){
            LifeTwoImageViewHolder holder1 = (LifeTwoImageViewHolder) holder;
            holder1.load(posts.get(position),position);
        }else if (holder instanceof LifeThereImageViewHolder){
            LifeThereImageViewHolder holder1 = (LifeThereImageViewHolder) holder;
            holder1.load(posts.get(position),position);
        }
    }

    @Override
    public int getItemCount() {
        if (posts == null){
            return 0;
        }
        return posts.size();
    }

    public void setPosts(List<Post> posts) {
        if (posts != null && posts.size() > 0){
            this.posts = posts;
            notifyDataSetChanged();
        }

    }

    class LifeOneImageViewHolder extends RecyclerView.ViewHolder {

        CircleImageView cvLifeHead;
        TextView tvLifeItemNickname;
        TextView tvLifeItemTime;
        TextView tvLifeItemContent;
        TextView tvLifeItemComment;
        EditText etLifeItemComment;
        ShineButton sbLife;
        ImageView imageView;

        public LifeOneImageViewHolder(View itemView) {
            super(itemView);
            cvLifeHead = (CircleImageView) itemView.findViewById(R.id.cv_life_head);
            tvLifeItemNickname = (TextView) itemView.findViewById(R.id.tv_life_item_nickname);
            tvLifeItemTime = (TextView) itemView.findViewById(R.id.tv_life_item_time);
            tvLifeItemComment = (TextView) itemView.findViewById(R.id.tv_life_item_comment);
            tvLifeItemContent = (TextView) itemView.findViewById(R.id.tv_life_item_content);
            etLifeItemComment = (EditText) itemView.findViewById(R.id.et_life_item_comment);
            sbLife = (ShineButton) itemView.findViewById(R.id.sb_life);
            imageView = (ImageView) itemView.findViewById(R.id.iv_life_image);
        }

        public void load(final Post post, final int position){
            if (post.getImages() == null || post.getImages().length ==0){
                imageView.setVisibility(View.GONE);
            }else {
                imageView.setVisibility(View.VISIBLE);
                Glide.with(getContext()).load(post.getImages()[0]).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        if (resource != null){
                            Log.i("测试","像素点"+resource.getWidth()+"  "+resource.getHeight());
                            if (resource.getWidth() > 1*width/2){
                                Glide.with(getContext())
                                        .load(post.getImages()[0])
                                        .override(1*width/2,resource.getHeight()*1*width/(2*resource.getWidth()))
                                        .into(imageView);
                                Log.i("测试","像素点"+(1*width/2)+"  "+(resource.getHeight()*1*width/(2*resource.getWidth())));
                            }else if (resource.getHeight() > 1*height/2){
                                Glide.with(getContext())
                                        .load(post.getImages()[0])
                                        .override(resource.getWidth()*1*height/(2*resource.getHeight()),1*height/2)
                                        .into(imageView);
                                Log.i("测试","像素点"+(resource.getWidth()*1*height/(2*resource.getHeight()))+"  "+(1*height/2));
                            }else {
                                Glide.with(getContext())
                                        .load(post.getImages()[0])
                                        .override(resource.getWidth(),resource.getHeight())
                                        .into(imageView);
                            }
                        }
                    }
                });
                if (post.getComment() != null && post.getComment().length>0){
                    tvLifeItemComment.setText("");
                    for (String s : post.getComment()) {
                        tvLifeItemComment.append(s.split("#")[1]+"\n");

                    }
                }
                etLifeItemComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEND
                                || actionId == EditorInfo.IME_ACTION_DONE
                                || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode()
                                && KeyEvent.ACTION_DOWN == event.getAction())) {
                            Post post1 = new Post();
                            post1=LifeUtil.comment(post, etLifeItemComment.getText().toString(), new FriendUtil.OnFriendDealListener() {
                                @Override
                                public void onSuccess() {
                                    
                                }

                                @Override
                                public void onError(String error) {
                                    Toast.makeText(UnknownApplication.getContext(), error, Toast.LENGTH_SHORT).show();
                                }
                            });
                            etLifeItemComment.setText("");
                            posts.remove(position);
                            posts.add(position,post1);
                            if (post.getComment() != null && post.getComment().length>0){
                                tvLifeItemComment.setText("");
                                for (String s : post.getComment()) {
                                    tvLifeItemComment.append(s.split("#")[1]+"\n");
                                }
                            }
                            return true;
                        }
                        return false;
                    }
                });
            }
            Glide.with(getContext())
                    .load(post.getPublisher().getHeadUrl())
                    .into(cvLifeHead);
            tvLifeItemNickname.setText(post.getPublisher().getNickname());
            tvLifeItemContent.setText(post.getContent());
            long distanceTime = System.currentTimeMillis() - post.getPublishTime();
            if (distanceTime < 1000L*60L*60L*24L){
                if (distanceTime < 1000L*60L*60L){
                    if (distanceTime < 1000L*60L){
                        tvLifeItemTime.setText("刚刚");
                    }else {
                        tvLifeItemTime.setText((distanceTime/(1000L*60L))+"分钟前");
                    }
                }else {
                    tvLifeItemTime.setText((distanceTime/(1000L*60L*60L))+"小时前");
                }
            }else {
                tvLifeItemTime.setText((distanceTime/(1000L*60L*60L*24L))+"天前");
            }
            if (LifeUtil.hadLike(post.getObjectId())){
                sbLife.setChecked(true);
                sbLife.setEnabled(false);
            }else {
                sbLife.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LifeUtil.likePost(post);
                        sbLife.setChecked(true);
                        sbLife.setEnabled(false);
                    }
                });
            }
        }
    }

    class LifeTwoImageViewHolder extends RecyclerView.ViewHolder {

        CircleImageView cvLifeHead;
        TextView tvLifeItemNickname;
        TextView tvLifeItemTime;
        TextView tvLifeItemContent;
        ImageView ivLifeItemImage1;
        ImageView ivLifeItemImage2;
        TextView tvLifeItemComment;
        EditText etLifeItemComment;
        ShineButton sbLife;

        public LifeTwoImageViewHolder(View itemView) {
            super(itemView);
            cvLifeHead = (CircleImageView) itemView.findViewById(R.id.cv_life_head);
            tvLifeItemNickname = (TextView) itemView.findViewById(R.id.tv_life_item_nickname);
            tvLifeItemTime = (TextView) itemView.findViewById(R.id.tv_life_item_time);
            tvLifeItemComment = (TextView) itemView.findViewById(R.id.tv_life_item_comment);
            tvLifeItemContent = (TextView) itemView.findViewById(R.id.tv_life_item_content);
            etLifeItemComment = (EditText) itemView.findViewById(R.id.et_life_item_comment);
            sbLife = (ShineButton) itemView.findViewById(R.id.sb_life);
            ivLifeItemImage1 = (ImageView) itemView.findViewById(R.id.iv_life_item_image1);
            ivLifeItemImage2 = (ImageView) itemView.findViewById(R.id.iv_life_item_image2);
        }

        public void load(final Post post, final int position){

            Glide.with(getContext())
                    .load(post.getImages()[0])
                    .into(ivLifeItemImage1);

            Glide.with(getContext())
                    .load(post.getImages()[1])
                    .into(ivLifeItemImage2);

            Glide.with(getContext())
                    .load(post.getPublisher().getHeadUrl())
                    .into(cvLifeHead);
            tvLifeItemNickname.setText(post.getPublisher().getNickname());
            tvLifeItemContent.setText(post.getContent());
            long distanceTime = System.currentTimeMillis() - post.getPublishTime();
            if (distanceTime < 1000L*60L*60L*24L){
                if (distanceTime < 1000L*60L*60L){
                    if (distanceTime < 1000L*60L){
                        tvLifeItemTime.setText("刚刚");
                    }else {
                        tvLifeItemTime.setText((distanceTime/(1000L*60L))+"分钟前");
                    }
                }else {
                    tvLifeItemTime.setText((distanceTime/(1000L*60L*60L))+"小时前");
                }
            }else {
                tvLifeItemTime.setText((distanceTime/(1000L*60L*60L*24L))+"天前");
            }
            if (post.getComment() != null && post.getComment().length>0){
                tvLifeItemComment.setText("");
                for (String s : post.getComment()) {
                    tvLifeItemComment.append(s.split("#")[1]+"\n");
                }
            }
            etLifeItemComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEND
                            || actionId == EditorInfo.IME_ACTION_DONE
                            || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode()
                            && KeyEvent.ACTION_DOWN == event.getAction())) {
                        Post post1 = new Post();
                        post1=LifeUtil.comment(post, etLifeItemComment.getText().toString(), new FriendUtil.OnFriendDealListener() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(UnknownApplication.getContext(), error, Toast.LENGTH_SHORT).show();
                            }
                        });
                        etLifeItemComment.setText("");
                        posts.remove(position);
                        posts.add(position,post1);
                        if (post.getComment() != null && post.getComment().length>0){
                            tvLifeItemComment.setText("");
                            for (String s : post.getComment()) {
                                tvLifeItemComment.append(s.split("#")[1]+"\n");
                            }
                        }
                        return true;
                    }
                    return false;
                }
            });
            if (LifeUtil.hadLike(post.getObjectId())){
                sbLife.setChecked(true);
                sbLife.setEnabled(false);
            }else {
                sbLife.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LifeUtil.likePost(post);
                        sbLife.setChecked(true);
                        sbLife.setEnabled(false);
                    }
                });
            }
        }
    }


    class LifeThereImageViewHolder extends RecyclerView.ViewHolder {

        CircleImageView cvLifeHead;
        TextView tvLifeItemNickname;
        TextView tvLifeItemTime;
        TextView tvLifeItemContent;
        ImageView ivLifeItemImage1;
        ImageView ivLifeItemImage2;
        ImageView ivLifeItemImage3;
        TextView tvLifeImageCount;
        TextView tvLifeItemComment;
        EditText etLifeItemComment;
        ShineButton sbLife;

        public LifeThereImageViewHolder(View itemView) {
            super(itemView);
            cvLifeHead = (CircleImageView) itemView.findViewById(R.id.cv_life_head);
            tvLifeItemNickname = (TextView) itemView.findViewById(R.id.tv_life_item_nickname);
            tvLifeItemTime = (TextView) itemView.findViewById(R.id.tv_life_item_time);
            tvLifeItemComment = (TextView) itemView.findViewById(R.id.tv_life_item_comment);
            tvLifeItemContent = (TextView) itemView.findViewById(R.id.tv_life_item_content);
            etLifeItemComment = (EditText) itemView.findViewById(R.id.et_life_item_comment);
            sbLife = (ShineButton) itemView.findViewById(R.id.sb_life);
            ivLifeItemImage1 = (ImageView) itemView.findViewById(R.id.iv_life_item_image1);
            ivLifeItemImage2 = (ImageView) itemView.findViewById(R.id.iv_life_item_image2);
            ivLifeItemImage3 = (ImageView) itemView.findViewById(R.id.iv_life_item_image3);
            tvLifeImageCount = (TextView) itemView.findViewById(R.id.tv_life_image_count);
        }
        public void load(final Post post, final int position){

            Glide.with(getContext())
                    .load(post.getImages()[0])
                    .into(ivLifeItemImage1);

            Glide.with(getContext())
                    .load(post.getImages()[1])
                    .into(ivLifeItemImage2);

            Glide.with(getContext())
                    .load(post.getImages()[2])
                    .into(ivLifeItemImage3);

            if (post.getImages().length>3){
                tvLifeImageCount.setVisibility(View.VISIBLE);
                tvLifeImageCount.setText("+"+(post.getImages().length-3));
            }else {
                tvLifeImageCount.setVisibility(View.GONE);
            }

            Glide.with(getContext())
                    .load(post.getPublisher().getHeadUrl())
                    .into(cvLifeHead);
            tvLifeItemNickname.setText(post.getPublisher().getNickname());
            tvLifeItemContent.setText(post.getContent());
            long distanceTime = System.currentTimeMillis() - post.getPublishTime();
            if (distanceTime < 1000L*60L*60L*24L){
                if (distanceTime < 1000L*60L*60L){
                    if (distanceTime < 1000L*60L){
                        tvLifeItemTime.setText("刚刚");
                    }else {
                        tvLifeItemTime.setText((distanceTime/(1000L*60L))+"分钟前");
                    }
                }else {
                    tvLifeItemTime.setText((distanceTime/(1000L*60L*60L))+"小时前");
                }
            }else {
                tvLifeItemTime.setText((distanceTime/(1000L*60L*60L*24L))+"天前");
            }
            if (post.getComment() != null && post.getComment().length>0){
                tvLifeItemComment.setText("");
                for (String s : post.getComment()) {
                    tvLifeItemComment.append(s.split("#")[1]+"\n");
                }
            }
            etLifeItemComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEND
                            || actionId == EditorInfo.IME_ACTION_DONE
                            || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode()
                            && KeyEvent.ACTION_DOWN == event.getAction())) {
                        Post post1 = new Post();
                        post1=LifeUtil.comment(post, etLifeItemComment.getText().toString(), new FriendUtil.OnFriendDealListener() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(UnknownApplication.getContext(), error, Toast.LENGTH_SHORT).show();
                            }
                        });
                        etLifeItemComment.setText("");
                        posts.remove(position);
                        posts.add(position,post1);
                        if (post.getComment() != null && post.getComment().length>0){
                            tvLifeItemComment.setText("");
                            for (String s : post.getComment()) {
                                tvLifeItemComment.append(s.split("#")[1]+"\n");
                            }
                        }
                        return true;
                    }
                    return false;
                }
            });
            if (LifeUtil.hadLike(post.getObjectId())){
                sbLife.setChecked(true);
                sbLife.setEnabled(false);
            }else {
                sbLife.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LifeUtil.likePost(post);
                        sbLife.setChecked(true);
                        sbLife.setEnabled(false);
                    }
                });
            }
        }
    }

    public void addData(List<Post> data){
        if (posts == null){
            posts = new ArrayList<>();
        }
        if (data !=null && data.size()>0){
            for (Post post : data) {
                posts.add(post);
                notifyItemInserted(posts.size()-1);
            }
        }
    }


}
