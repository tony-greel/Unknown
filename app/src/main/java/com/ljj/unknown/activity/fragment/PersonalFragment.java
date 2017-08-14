package com.ljj.unknown.activity.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.imnjh.imagepicker.SImagePicker;
import com.ljj.unknown.R;
import com.ljj.unknown.activity.BaseActivity;
import com.ljj.unknown.activity.FeedbackActivity;
import com.ljj.unknown.activity.LifeCircleActivity;
import com.ljj.unknown.bean.User;
import com.ljj.unknown.other.CacheManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2017/8/10.
 */

public class PersonalFragment extends Fragment {

    @Bind(R.id.tv_nickname)
    TextView tvNickname;
    @Bind(R.id.tv_username)
    TextView tvUsername;
    @Bind(R.id.iv_scan_code)
    ImageView ivScanCode;
    @Bind(R.id.rl_personal_info)
    RelativeLayout rlPersonalInfo;
    @Bind(R.id.rl_album)
    RelativeLayout rlAlbum;
    @Bind(R.id.rl_life)
    RelativeLayout rlLife;
    @Bind(R.id.rl_feedback)
    RelativeLayout rlFeedback;
    @Bind(R.id.rl_setting)
    RelativeLayout rlSetting;
    @Bind(R.id.iv_personal)
    ImageView ivPersonal;

    BaseActivity baseActivity;

    public static final int REQUEST_CODE_AVATAR = 100;

    public static final String AVATAR_FILE_NAME = "avatar.png";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frament_personal, container, false);
        ButterKnife.bind(this, view);

        if (TextUtils.isEmpty(BmobUser.getCurrentUser(User.class).getHeadUrl())) {
            Glide.with(this)
                    .load(R.drawable.ic_portrait_but)
                    .into(ivPersonal);
        } else {
            Glide.with(this)
                    .load(BmobUser.getCurrentUser(User.class).getHeadUrl())
                    .into(ivPersonal);
        }

        User user = User.getCurrentUser(User.class);
        tvNickname.setText(user.getNickname());
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.iv_personal, R.id.rl_life, R.id.rl_feedback})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_personal:
                if (baseActivity.getStorage() && baseActivity.getCcamra()) {
                    SImagePicker
                            .from(getActivity())
                            .pickMode(SImagePicker.MODE_AVATAR)
                            .showCamera(true)
                            .cropFilePath(
                                    CacheManager.getInstance().getImageInnerCache()
                                            .getAbsolutePath(AVATAR_FILE_NAME))
                            .forResult(REQUEST_CODE_AVATAR);
                }
                break;
            case R.id.rl_life:
                startActivity(new Intent(getActivity(), LifeCircleActivity.class));
                break;
            case R.id.rl_feedback:
                Intent intent = new Intent(getActivity(), FeedbackActivity.class);
                startActivity(intent);
                break;
        }
    }
}