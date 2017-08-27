package com.ljj.unknown.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ljj.unknown.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bluemobi.dylan.photoview.library.PhotoView;

public class ImageActivity extends BaseActivity {

    @Bind(R.id.tv_chat_name)
    TextView tvChatName;
    @Bind(R.id.pv_image)
    PhotoView pvImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ButterKnife.bind(this);
        setToolBar(R.id.tb_image);
        initHome();
        Glide.with(this)
                .load(getIntent().getStringExtra("image"))
                .into(pvImage);
    }
}
