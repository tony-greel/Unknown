package com.ljj.unknown.activity.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ljj.unknown.R;
import com.ljj.unknown.adapter.ContractAdapter;
import com.ljj.unknown.bean.FriendInfo;
import com.ljj.unknown.bean.User;
import com.ljj.unknown.util.FriendUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;


import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2017/8/4.
 */

/**
 * 联系人界面
 */

public class ContactsFragment extends Fragment {
    @Bind(R.id.rv_contracts)
    RecyclerView rvContracts;
    @Bind(R.id.srl_contracts)
    SmartRefreshLayout srlContracts;
    private ContractAdapter contractAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frament_contacts, container, false);
        ButterKnife.bind(this, view);
        srlContracts.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
//                FriendUtil.updateFriendInfo(BmobUser.getCurrentUser(User.class), new FriendUtil.OnFriendDealListener() {
//                    @Override
//                    public void onSuccess() {
//
//                    }
//
//                    @Override
//                    public void onError(String error) {
//
//                    }
//                });
                refreshlayout.finishRefresh(2000);
            }
        });
        srlContracts.setHeaderHeight(250);
        List<FriendInfo> list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            list.add(new FriendInfo());
        }
        contractAdapter = new ContractAdapter(list);
        rvContracts.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvContracts.setAdapter(contractAdapter);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
