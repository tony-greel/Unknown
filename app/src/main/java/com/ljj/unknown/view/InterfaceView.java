package com.ljj.unknown.view;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ljj.unknown.R;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import qiu.niorgai.StatusBarCompat;

/**
 * Created by Administrator on 2017/8/4.
 */

public class InterfaceView extends FragmentActivity {

    @Bind(R.id.connector_View)
    ViewPager connector_View;
    @Bind(R.id.pull_to_refresh)
    PullToRefreshView mPullToRefreshView;

    private View contacts_fragment;//联系人界面

    private View conversation_fragment;//会话界面

    private View personal_fragment;//个人信息界面

    private List<View> viewList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager_interface);
        ButterKnife.bind(this);
        StatusBarCompat.translucentStatusBar(this);//去掉状态栏

        listdata();

        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, 3000);
            }
        });
    }

    private void listdata() {
        LayoutInflater inflater = getLayoutInflater();
        conversation_fragment = inflater.inflate(R.layout.frament_conversation, null);
        contacts_fragment = inflater.inflate(R.layout.frament_contacts, null);
        personal_fragment = inflater.inflate(R.layout.frament_personal, null);

        viewList = new ArrayList<View>();//将要分页显示View装入数组中
        viewList.add(conversation_fragment);
        viewList.add(contacts_fragment);
        viewList.add(personal_fragment);

        PagerAdapter pagerAdapter = new PagerAdapter() {
            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                // TODO Auto-generated method stub
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                // 返回要滑动个数
                return viewList.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                // 从当前container中删除指定位置(posi tion)的View
                container.removeView(viewList.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                // 做了两件事，第一：将当前视图添加到container中，第二：返回当前View
                container.addView(viewList.get(position));

                return viewList.get(position);
            }
        };
        connector_View.setAdapter(pagerAdapter);
    }

}
