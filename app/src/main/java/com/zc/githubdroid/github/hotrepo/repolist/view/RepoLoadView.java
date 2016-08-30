package com.zc.githubdroid.github.hotrepo.repolist.view;

import com.zc.githubdroid.github.hotrepo.repolist.model.Repo;

import java.util.List;

/**
 *
 *  上拉加载的接口
 */
public interface RepoLoadView {

    /**
     * 1.显示加载视图
     * 2.隐藏加载视图
     * 3.加载失败
     * 4.加载完成，拿到数据进行视图更新
     */
    //    1.显示加载视图
     void showLoadingView();

    //    2.隐藏加载视图
     void hideLoadView();

    //    3.加载失败
     void showLoadError(String msg);

    //    4.加载完成，拿到数据进行视图更新
     void addLoadDota(List<Repo> list);//将数据传过来


}
