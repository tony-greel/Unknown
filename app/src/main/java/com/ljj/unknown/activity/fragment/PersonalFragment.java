package com.ljj.unknown.activity.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ljj.unknown.R;
import com.ljj.unknown.activity.ScanActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/8/4.
 */

/**
 * 个人信息界面
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frament_personal, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.iv_scan_code, R.id.rl_personal_info, R.id.rl_album, R.id.rl_life, R.id.rl_feedback, R.id.rl_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_scan_code:
                startActivity(new Intent(getActivity(), ScanActivity.class));
                break;
            case R.id.rl_personal_info:
                break;
            case R.id.rl_album:
                break;
            case R.id.rl_life:
                break;
            case R.id.rl_feedback:
                break;
            case R.id.rl_setting:
                break;
        }
    }
}
