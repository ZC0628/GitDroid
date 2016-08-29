package com.zc.githubdroid.github.hotrepo.repolist;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mugen.Mugen;
import com.mugen.MugenCallbacks;
import com.zc.githubdroid.R;
import com.zc.githubdroid.commons.LogUtils;
import com.zc.githubdroid.components.FooterView;
import com.zc.githubdroid.github.hotrepo.repolist.view.RepoListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.StoreHouseHeader;


/**
 * 业务类：主要完成数据加载，通知视图改变
 *  TalLayout标题下的Listview
 */
public class HotRepoListFragment extends Fragment implements RepoListView {


    @BindView(R.id.lvRepos) ListView lvRepos;
    @BindView(R.id.ptrClassicFrameLayout) PtrClassicFrameLayout ptrFrameLayout;//下拉刷新
    @BindView(R.id.emptyView) TextView emptyView;//空的页面
    @BindView(R.id.errorView) TextView errorView;//加载错误、网络加载失败

    private List<String> data;

    private RepoListPresenter presenter;
    private ArrayAdapter<String> adapter;
    private FooterView footerView;//尾部的布局

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_repo_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new RepoListPresenter(this);

        //初始添加数据
        data = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            data.add("测试数据"+i);
        }
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,data);

        //判断适配器是否空的，有没有数据
        if (adapter.getCount()==0){
            ptrFrameLayout.postDelayed(new Runnable() {//延时刷新
                @Override public void run() {
                    ptrFrameLayout.autoRefresh();//自动刷新
                }
            },200);//延时2秒
        }
        //刷新
        initPullToRefresh();

        //上拉加载
        /**
         * 当ListView滑动到最后一条再继续滑动，触发加载
         * 加载完成，移除添加的加载的布局
         */
        //加载更多
        initLoadMore();

        lvRepos.addFooterView(footerView);//将尾部视图添加到Listview上
        lvRepos.setAdapter(adapter);//设置适配器
        lvRepos.removeFooterView(footerView);//移除视图
    }

    private void initLoadMore() {
        footerView = new FooterView(getContext());

        /**
         * 实现上拉加载监听  Mugen
         */
        Mugen.with(lvRepos, new MugenCallbacks() {
            //当Listview滑动到最后的时候，触发这个方法进行加载
            @Override
            public void onLoadMore() {
                LogUtils.i("1");
                //上拉加载数据业务的完成
                presenter.LoadMore();
                LogUtils.i("2");

            }


            //是否正在加载，是否将尾部布局添加到Listview上
            @Override
            public boolean isLoading() {
                LogUtils.i("5");
                return lvRepos.getFooterViewsCount() > 0 && footerView.isLoading();
            }

            //是不是加载完成的数据，是否将尾部布局添加到Listview上
            @Override
            public boolean hasLoadedAllItems() {
                LogUtils.i("6");
                //已经添加上去 并且 是否加载完成
                return lvRepos.getFooterViewsCount() > 0 && footerView.iscomplete();
            }
        }).start();
    }

    private void initPullToRefresh() {
        // 使用当前对象做为key，来记录上一次的刷新时间,如果两次下拉太近，将不会触发新刷新
        ptrFrameLayout.setLastUpdateTimeRelateObject(this);
        // 关闭header所用时长
        ptrFrameLayout.setDurationToCloseHeader(1500);
        // 以下代码（只是修改了header样式）改变刷新的视图
        //header——要改变刷新的样式
        StoreHouseHeader header = new StoreHouseHeader(getContext());
        //设置刷新过程中文字的显示
        header.initWithString("I LIKE " + " JAVA");
        header.setPadding(0, 60, 0, 60);
        // 修改Ptr的HeaderView效果到ptrFrameLayout上
        ptrFrameLayout.setHeaderView(header);
        ptrFrameLayout.addPtrUIHandler(header);
        ptrFrameLayout.setBackgroundResource(R.color.colorRefresh);


        // 下拉刷新监听处理
        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            // 当你"下拉时",将触发此方法
            @Override public void onRefreshBegin(PtrFrameLayout frame) {
                // 去做数据的加载，做具体的业务
                // 也就是说，你要抛开视图，到后台线程去做你的业务处理(数据刷新加载)
                presenter.refresh();
            }
        });
    }


    //下拉刷新刷新的时候视图
    /**
     * 1.显示刷新,显示刷新的视图
     * 2.停止刷新
     * 3.加载错误、网络加载失败
     * 4.刷新的数据为空，显示空白页面
     * 5.拿到刷新得到数据
     */

//    拿到刷新得到数据
    @Override public void refreshData(List<String> list) {
        data.clear();//刷新后将之前的数据进行清空
        data.addAll(list);//得到所有的数据
        adapter.notifyDataSetChanged();
    }

//    显示刷新的视图
    @Override public void showContentView() {
        ptrFrameLayout.setVisibility(View.VISIBLE);//下拉刷新显示
        emptyView.setVisibility(View.GONE);//空的页面隐藏
        errorView.setVisibility(View.GONE);//加载失败隐藏
    }

//    停止刷新
    @Override public void stopRefresh() {
        ptrFrameLayout.refreshComplete();//停止刷新
    }

//    刷新的数据为空，显示空白页面
    @Override public void showEmptyView() {
        ptrFrameLayout.setVisibility(View.GONE);//下拉刷新隐藏
        emptyView.setVisibility(View.VISIBLE);//空的显示
        errorView.setVisibility(View.GONE);//加载失败隐藏
    }

//    加载错误、网络加载失败
    @Override public void showErrorView(String errormsg) {
        ptrFrameLayout.setVisibility(View.GONE);//下拉刷新隐藏
        emptyView.setVisibility(View.GONE);//空的页面隐藏
        errorView.setVisibility(View.VISIBLE);//加载失败显示
    }





    //上拉加载视图分析
    /**
     * 1.显示加载视图
     * 2.隐藏加载视图
     * 3.加载失败
     * 4.加载完成，拿到数据进行视图更新
     */

//    1.显示加载视图
    @Override
    public void showLoadingView() {
        //是否添加尾部布局
        if (lvRepos.getFooterViewsCount() == 0){
            lvRepos.addFooterView(footerView);//等于0就是没有添加，则添加进去
        }
        footerView.showLoading();//显示加载的视图
    }

//    2.隐藏加载视图
    @Override
    public void hideLoadView() {
        lvRepos.removeFooterView(footerView);//移除掉视图
    }

//    3.加载失败，显示错误视图
    @Override
    public void showLoadError(String mag) {
        if (lvRepos.getFooterViewsCount() == 0){
            lvRepos.addFooterView(footerView);//等于0就是没有添加，则添加进去
        }
        footerView.showError();//显示错误视图

    }

//    4.加载完成，拿到数据进行视图更新
    @Override
    public void addLoadDota(List<String> list) {
        data.addAll(list);
        adapter.notifyDataSetChanged();

    }


}
