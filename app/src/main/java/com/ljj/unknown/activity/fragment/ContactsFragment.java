package com.ljj.unknown.activity.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.ljj.unknown.R;
import com.ljj.unknown.adapter.ContractAdapter;

import com.ljj.unknown.bean.User;
import com.ljj.unknown.util.FriendUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;



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
            public void onRefresh(final RefreshLayout refreshlayout) {
                FriendUtil.updateFriendInfo(BmobUser.getCurrentUser(User.class), new FriendUtil.OnFriendDealListener() {
                    @Override
                    public void onSuccess() {
                        load();
                        refreshlayout.finishRefresh();
                    }

                    @Override
                    public void onError(String error) {
                        refreshlayout.finishRefresh();
                        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        srlContracts.setHeaderHeight(250);
        contractAdapter = new ContractAdapter(null);
        rvContracts.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvContracts.setAdapter(contractAdapter);
        load();
        return view;
    }

    public void load(){
        contractAdapter.setNewData(FriendUtil.getFriendInfo(BmobUser.getCurrentUser(User.class)));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
