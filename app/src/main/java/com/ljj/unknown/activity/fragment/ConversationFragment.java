package com.ljj.unknown.activity.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ljj.unknown.R;
import com.ljj.unknown.UnknownApplication;
import com.ljj.unknown.adapter.ConversationAdapter;
import com.ljj.unknown.util.FriendUtil;
import com.ljj.unknown.util.ImUtil;
import com.scwang.smartrefresh.header.DeliveryHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;

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
            public void onRefresh(final RefreshLayout refreshlayout) {
                if (ImUtil.isConnected){
                    adapter.setNewData(BmobIM.getInstance().loadAllConversation());
                    refreshlayout.finishRefresh();
                }else if (ImUtil.hadError){
                    ImUtil.connectServer(new FriendUtil.OnFriendDealListener() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(UnknownApplication.getContext(), "连接成功", Toast.LENGTH_SHORT).show();
                            adapter.setNewData(BmobIM.getInstance().loadAllConversation());
                            refreshlayout.finishRefresh();
                        }

                        @Override
                        public void onError(String error) {
                            refreshlayout.finishRefresh();
                        }
                    });
                }else {
                    Toast.makeText(getActivity(), "请先打开网络", Toast.LENGTH_SHORT).show();
                    refreshlayout.finishRefresh();
                }

            }
        });
        srlConversation.setEnableLoadmore(false);
        srlConversation.setHeaderHeight(250);
        DeliveryHeader header = new DeliveryHeader(getActivity());
        srlConversation.setRefreshHeader(header);
        rvConversation.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvConversation.setItemAnimator(new DefaultItemAnimator());
        if (ImUtil.isConnected){
            adapter = new ConversationAdapter(BmobIM.getInstance().loadAllConversation());
        }
        adapter = new ConversationAdapter(null);
        rvConversation.setAdapter(adapter);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
