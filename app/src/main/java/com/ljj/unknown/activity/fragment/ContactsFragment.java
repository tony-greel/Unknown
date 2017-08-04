package com.ljj.unknown.activity.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ljj.unknown.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yalantis.phoenix.PullToRefreshView;
import com.yalantis.phoenix.refresh_view.SunRefreshView;

import butterknife.Bind;
import butterknife.ButterKnife;

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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frament_contacts, container, false);
        ButterKnife.bind(this, view);
        srlContracts.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000);
            }
        });
        srlContracts.setHeaderHeight(250);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
