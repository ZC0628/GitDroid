package com.zc.githubdroid.github.hotuser;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.mugen.Mugen;
import com.mugen.MugenCallbacks;
import com.zc.githubdroid.R;
import com.zc.githubdroid.commons.ActivityUtils;
import com.zc.githubdroid.components.FooterView;
import com.zc.githubdroid.login.model.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.StoreHouseHeader;


/**
 *
 */
public class HotUserFragment extends Fragment implements HotUserPresenter.HotUserView{

    @BindView(R.id.lvRepos)
    ListView lvUsers;
    @BindView(R.id.ptrClassicFrameLayout)
    PtrClassicFrameLayout ptrClassicFrameLayout;
    @BindView(R.id.emptyView)
    TextView emptyView;
    @BindView(R.id.errorView)
    TextView errorView;
    private HotUserAdapter adapter;

    private ActivityUtils activityUtils;
    private HotUserPresenter presenter;
    private FooterView footerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hot_user, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        activityUtils = new ActivityUtils(this);
        presenter = new HotUserPresenter(this);
        adapter = new HotUserAdapter();


        //下拉刷新的方法
        initPullToRefresh();

        //上拉加载的方法
        initLoadMore();

        //如果没有数据，就在200毫秒之后自动刷新
        if (adapter.getCount() <= 0) {
            ptrClassicFrameLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ptrClassicFrameLayout.autoRefresh();
                }
            }, 200);
        }

        lvUsers.addFooterView(footerView);//将尾部视图添加到Listview上
        lvUsers.setAdapter(adapter);//设置适配器
        lvUsers.removeFooterView(footerView);//移除视图
    }

    //加载的基本配置
    private void initLoadMore() {
        footerView = new FooterView(getContext());
        Mugen.with(lvUsers, new MugenCallbacks() {
            //2.
            @Override
            public void onLoadMore() {
                presenter.loadMore();
            }

            //1.判断是不是正在加载
            @Override
            public boolean isLoading() {
                return lvUsers.getFooterViewsCount() > 0 && footerView.isLoading();
            }

            //3.有没有加载完
            @Override
            public boolean hasLoadedAllItems() {
                return lvUsers.getFooterViewsCount() > 0 && footerView.iscomplete();
            }
        }).start();
    }

    //刷新的基本配置
    private void initPullToRefresh() {
        //两次刷新时间靠近就不去触发
        ptrClassicFrameLayout.setLastUpdateTimeRelateObject(this);
        ptrClassicFrameLayout.setDurationToClose(2000);//刷新关闭时间

        //完成刷新的操作
        ptrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                presenter.refresh();
            }
        });

        // 以下代码（只是修改了header样式）
        StoreHouseHeader header = new StoreHouseHeader(getContext());
        header.initWithString("I LIKE " + " JAVA");
        header.setPadding(0, 60, 0, 60);
        // 修改Ptr的HeaderView效果
        ptrClassicFrameLayout.setHeaderView(header);
        ptrClassicFrameLayout.addPtrUIHandler(header);
        ptrClassicFrameLayout.setBackgroundResource(R.color.colorRefresh);
    }


    /**
     * 下拉刷新的视图
     */
    @Override
    public void showRefreshView() {
        ptrClassicFrameLayout.setVisibility(View.VISIBLE);//显示刷新的控件
        emptyView.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
    }

    @Override
    public void refreshData(List<User> list) {
        adapter.clear();//刷新的是新的数据，适配器先清空之前的数据
        adapter.addAll(list);//刷新出来的所有数据加载上去
    }

    @Override
    public void stopRefresh() {
        ptrClassicFrameLayout.refreshComplete();
    }

    @Override
    public void showMessage(String msg) {
        activityUtils.showToast(msg);
    }

    @Override
    public void showErrorView() {
        ptrClassicFrameLayout.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showEmptyView() {
        ptrClassicFrameLayout.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
    }

    /**
     * 主要是上拉加载的视图实现
     */
    @Override
    public void showLoadView() {
        //判断有没有添加尾部布局
        if (lvUsers.getFooterViewsCount() == 0){
            lvUsers.addFooterView(footerView);//添加尾部布局
        }
        footerView.showLoading();//显示正在加载
    }

    @Override
    public void hideLoadView() {
        lvUsers.removeFooterView(footerView);//移除尾部布局
    }

    @Override
    public void addLoadData(List<User> list) {
        adapter.addAll(list);
    }


}
