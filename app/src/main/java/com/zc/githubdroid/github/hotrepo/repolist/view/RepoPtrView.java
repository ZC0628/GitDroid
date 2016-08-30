package com.zc.githubdroid.github.hotrepo.repolist.view;

import com.zc.githubdroid.github.hotrepo.repolist.model.Repo;

import java.util.List;

/**
 *
 *    刷新的接口
 */
public interface RepoPtrView {

    /**
     * 1.显示刷新,显示刷新的视图
     * 2.停止刷新
     * 3.加载错误、网络加载失败
     * 4.刷新的数据为空，显示空白页面
     * 5.拿到刷新得到数据
     */

//    拿到刷新得到数据
    void refreshData(List<Repo> list);

//    显示刷新的视图
    void showContentView();

//    停止刷新
    void stopRefresh();

//  刷新的数据为空，显示空白页面
    void showEmptyView();

//  加载错误、网络加载失败
    void showErrorView(String errormsg);

    //弹出吐司
    void showMessage(String msg);

}
