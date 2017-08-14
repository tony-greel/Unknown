package com.ljj.unknown.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.ljj.unknown.R;
import com.ljj.unknown.adapter.LifeAdapter;
import com.ljj.unknown.bean.Post;
import com.ljj.unknown.util.LifeUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import qiu.niorgai.StatusBarCompat;

public class LifeCircleActivity extends BaseActivity {

    @Bind(R.id.iv_life_background)
    ImageView ivLifeBackground;
    @Bind(R.id.rv_life)
    RecyclerView rvLife;
    @Bind(R.id.srl_life)
    SmartRefreshLayout srlLife;
    LifeAdapter adapter;
    private int page=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.translucentStatusBar(this);
        setContentView(R.layout.activity_life_circle);
        ButterKnife.bind(this);
        setToolBar(R.id.tb_life_circle);
        initHome();
        adapter = new LifeAdapter();
        rvLife.setLayoutManager(new LinearLayoutManager(this));
        rvLife.setItemAnimator(new DefaultItemAnimator());
        rvLife.setAdapter(adapter);
        srlLife.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                LifeUtil.queryLifeUtil(0, new LifeUtil.QueryLifeListener() {
                    @Override
                    public void onSuccess(List<Post> data) {
                        page = 1;
                        adapter.setPosts(data);
                        refreshlayout.setLoadmoreFinished(false);
                        refreshlayout.finishRefresh();
                    }

                    @Override
                    public void onError(String error) {
                        refreshlayout.finishRefresh();
                        Toast.makeText(mActivity, error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        srlLife.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
                LifeUtil.queryLifeUtil(page, new LifeUtil.QueryLifeListener() {
                    @Override
                    public void onSuccess(List<Post> data) {
                        page++;
                        refreshlayout.finishLoadmore();
                        if (data.size()>0){
                            adapter.addData(data);
                        }else {
                            refreshlayout.setLoadmoreFinished(true);
                        }


                    }

                    @Override
                    public void onError(String error) {
                        refreshlayout.finishLoadmore();
                        Toast.makeText(mActivity, error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        srlLife.autoRefresh();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_life,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.publish){
            startActivity(new Intent(this,PublishActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    
}
