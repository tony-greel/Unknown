package com.ljj.unknown.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ljj.unknown.R;
import com.ljj.unknown.adapter.DetailsImageAdapter;
import com.ljj.unknown.bean.Post;
import com.ljj.unknown.util.FriendUtil;
import com.ljj.unknown.util.LifeUtil;
import com.sackcentury.shinebuttonlib.ShineButton;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class DetailsActivity extends BaseActivity {

    @Bind(R.id.tb_post_details)
    Toolbar tbPostDetails;
    @Bind(R.id.cv_post_head)
    CircleImageView cvPostHead;
    @Bind(R.id.tv_post_nickname)
    TextView tvPostNickname;
    @Bind(R.id.tv_post_time)
    TextView tvPostTime;
    @Bind(R.id.tv_post_content)
    TextView tvPostContent;
    @Bind(R.id.rv_image_post)
    RecyclerView rvImagePost;
    @Bind(R.id.tv_comment_count)
    TextView tvCommentCount;
    @Bind(R.id.post_details_comment)
    TextView postDetailsComment;
    @Bind(R.id.et_comment_input)
    EditText etCommentInput;
    @Bind(R.id.sb_post_like)
    ShineButton sbPostLike;
    @Bind(R.id.tv_like_comment)
    TextView tvLikeComment;
    private Post post;

    private DetailsImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        setToolBar(R.id.tb_post_details);
        post = (Post) getIntent().getSerializableExtra("post");
        Glide.with(this)
                .load(post.getPublisher().getHeadUrl())
                .into(cvPostHead);
        if (TextUtils.isEmpty(post.getPublisher().getNickname())){
            tvPostNickname.setText(post.getPublisher().getUsername());
        }else {
            tvPostNickname.setText(post.getPublisher().getNickname());
        }
        tvPostContent.setText(post.getContent());
        if (post.getComment() != null && post.getComment().length > 0){
            for (int i = 0; i < post.getComment().length; i++) {
                String s = post.getComment()[i];
                postDetailsComment.append(s.split("#")[1] + "\n");
            }
        }
        if (post.getImages() !=null && post.getImages().length != 0){
            List<String> imageUrl  = new ArrayList<>();//集合转数组
            for (int i = 0; i < post.getImages().length; i++) {
                imageUrl.add(post.getImages()[i]);
            }
            if (post.getImages().length == 1){
                adapter = new DetailsImageAdapter(R.layout.item_one_image,imageUrl);
                rvImagePost.setLayoutManager(new LinearLayoutManager(this));
            }else if (post.getImages().length == 2){
                adapter = new DetailsImageAdapter(R.layout.item_two_image,imageUrl);
                rvImagePost.setLayoutManager(new GridLayoutManager(this ,2));
            }else {
                adapter = new DetailsImageAdapter(R.layout.item_im_details, imageUrl);
                rvImagePost.setLayoutManager(new GridLayoutManager(this,3));
            }
            rvImagePost.setAdapter(adapter);
        }
        SimpleDateFormat text = new SimpleDateFormat("yyyy-MM-dd-hh-mm");
        tvPostTime.setText(text.format(new Date(post.getPublishTime())));
        initHome();
        etCommentInput.setImeOptions(EditorInfo.IME_ACTION_SEND);
        etCommentInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode()
                        && KeyEvent.ACTION_DOWN == event.getAction())) {
                    postDetailsComment.append(etCommentInput.getText().toString()+"\n");
                    LifeUtil.comment(post, etCommentInput.getText().toString(), new FriendUtil.OnFriendDealListener() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(String error) {
                            Toast.makeText(mActivity, error, Toast.LENGTH_SHORT).show();
                        }
                    });
                    etCommentInput.setText("");
                    return true;
                }
                return false;
            }
        });
        //查询该帖子是否被当前用户点赞
        if (LifeUtil.hadLike(post.getObjectId())){
            //点赞了
            sbPostLike.setChecked(true);
            sbPostLike.setEnabled(false);
        }else {
            //未点赞
            sbPostLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sbPostLike.setEnabled(false);
                    LifeUtil.likePost(post);
                }
            });
        }
    }
}
