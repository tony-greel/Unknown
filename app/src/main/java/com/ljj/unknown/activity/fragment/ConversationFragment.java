package com.ljj.unknown.activity.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ljj.unknown.R;

/**
 * Created by Administrator on 2017/8/4.
 */

/**
 * 会话界面
 */

public class ConversationFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frament_conversation,container,false);
        return view;
    }
}
