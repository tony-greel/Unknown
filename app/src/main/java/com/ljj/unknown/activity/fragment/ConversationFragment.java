package com.ljj.unknown.activity.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ljj.unknown.R;
import com.ljj.unknown.adapter.ConversationAdapter;
import com.scwang.smartrefresh.header.DeliveryHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/8/4.
 */

/**
 * 会话界面
 */

public class ConversationFragment extends Fragment {

    @Bind(R.id.rv_conversation)
    RecyclerView rvConversation;
    @Bind(R.id.srl_conversation)
    SmartRefreshLayout srlConversation;
    private ConversationAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frament_conversation, container, false);
        ButterKnife.bind(this, view);
        srlConversation.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000);
            }
        });
        srlConversation.setEnableLoadmore(false);
        srlConversation.setHeaderHeight(150);
        srlConversation.setRefreshHeader(new DeliveryHeader(getActivity()));
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
