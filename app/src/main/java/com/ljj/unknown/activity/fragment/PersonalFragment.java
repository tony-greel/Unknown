package com.ljj.unknown.activity.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ljj.unknown.R;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/8/4.
 */

/**
 * 个人信息界面
 */

public class PersonalFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.frament_personal, container, false);
        return view;
    }
}
