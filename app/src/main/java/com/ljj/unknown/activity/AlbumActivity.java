package com.ljj.unknown.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ljj.unknown.R;
import com.ljj.unknown.adapter.AlbumAdapter;
import com.ljj.unknown.bean.Post;
import com.ljj.unknown.bean.User;
import com.ljj.unknown.util.LifeUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2017/8/17.
 */

public class AlbumActivity extends BaseActivity {


    @Bind(R.id.tb_search)
    Toolbar tbSearch;
    @Bind(R.id.rv_albumadapter)
    RecyclerView rvAlbumadapter;
    AlbumAdapter adapter;
    @Bind(R.id.srl_album)
    SmartRefreshLayout srlAlbum;

    private int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_albumadapter);
        setToolBar(R.id.tb_search);
        ButterKnife.bind(this);
        initHome();
        adapter = new AlbumAdapter(this,new ArrayList<Post>());
        rvAlbumadapter.setLayoutManager(new LinearLayoutManager(this));
        rvAlbumadapter.setItemAnimator(new DefaultItemAnimator());
        rvAlbumadapter.setAdapter(adapter);
        srlAlbum.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                LifeUtil.queryAlbum(BmobUser.getCurrentUser(User.class),0, new LifeUtil.QueryLifeListener() {
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


        srlAlbum.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
                LifeUtil.queryAlbum(BmobUser.getCurrentUser(User.class),page, new LifeUtil.QueryLifeListener() {
                    @Override
                    public void onSuccess(List<Post> data) {
                        page++;
                        refreshlayout.finishLoadmore();
                        if(data.size() > 0){
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
        srlAlbum.autoRefresh();
    }


}
