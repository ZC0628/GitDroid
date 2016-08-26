package com.zc.githubdroid;

import android.os.AsyncTask;


import com.zc.githubdroid.commons.LogUtils;

import java.util.ArrayList;

/**
 * 业务类，主要完成数据加载，异步任务，通知视图进行改变
 *
 *  下拉刷新和上拉加载
 */
public class RepoListPresenter {

    private int count = 0;//从0开始

   private RepoListView repoListView;// 具有刷新和加载方法的接口

    public RepoListPresenter(RepoListView repoListView) {

        this.repoListView = repoListView;
    }

    public void refresh(){
        //显示刷新
        repoListView.showContentView();
        new Refresh().execute();
    }

    //加载更多
    public void LoadMore(){
        //要加载数据了，显示加载数据的布局
        repoListView.showLoadingView();
        LogUtils.i("3");
        new LoadMore().execute();//执行操作

    }


//    下拉刷新数据加载 （异步）
    class Refresh extends AsyncTask<Void,Void,Void> {
        @Override protected Void doInBackground(Void... params) {
            //模拟数据的刷新，先停3秒
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
        //进行数据的更新
        @Override protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ArrayList<String> list = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                list.add("刷新"+(count++));
            }
        //拿到刷新得到的数据
            repoListView.refreshData(list);
            //停止刷新
            repoListView.stopRefresh();
        }
    }


    //上拉加载数据更多 （异步）
    class LoadMore extends AsyncTask<Void,Void,Void> {
        @Override protected Void doInBackground(Void... params) {
            //模拟数据的刷新，先停3秒
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
        //进行数据的更新
        @Override protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ArrayList<String> list = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                list.add("加载"+(count++));
            }
            LogUtils.i("7");
            //加载出来的数据添加上去
            repoListView.addLoadDota(list);
            LogUtils.i("8");
            //加载完成了，将加载的视图隐藏掉
            repoListView.hideLoadView();
            LogUtils.i("9");
        }
    }

}
