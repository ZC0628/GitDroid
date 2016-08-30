package com.zc.githubdroid.github.hotrepo.repolist;


import com.zc.githubdroid.Language;
import com.zc.githubdroid.commons.LogUtils;
import com.zc.githubdroid.github.hotrepo.repolist.model.Repo;
import com.zc.githubdroid.github.hotrepo.repolist.model.RepoResult;
import com.zc.githubdroid.github.hotrepo.repolist.view.RepoListView;
import com.zc.githubdroid.network.GithubClient;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 业务类，主要完成数据加载，异步任务，通知视图进行改变
 *
 *  下拉刷新和上拉加载
 */
public class RepoListPresenter {

    private int count = 0;//从0开始

    private Language language;//语言
    private int nextpage = 1;//下一页

   private RepoListView repoListView;// 具有刷新和加载方法的接口
    private Call<RepoResult> repoCall;//回应的结果

    public RepoListPresenter(RepoListView repoListView,Language language) {
        this.repoListView = repoListView;
        this.language = language;
    }

    public void refresh(){
        //显示刷新
        repoListView.showContentView();
//        new Refresh().execute();
        //每次刷新都是从1开始
        nextpage = 1;
        repoCall = GithubClient.getInstance().searchRepos("language:" + language.getPath(), nextpage);
        repoCall.enqueue(repoCallback);

    }

    //加载更多
    public void LoadMore(){
        //要加载数据了，显示加载数据的布局
        repoListView.showLoadingView();
        LogUtils.i("3");
//        new LoadMore().execute();//执行操作
        repoCall = GithubClient.getInstance().searchRepos("language:" + language.getPath(), nextpage);
        repoCall.enqueue(loadMoreCallback);

    }

    //下拉刷新异步获取仓库
    private Callback<RepoResult> repoCallback = new Callback<RepoResult>() {
        @Override public void onResponse(Call<RepoResult> call, Response<RepoResult> response) {
            repoListView.stopRefresh();///停止刷新
            RepoResult repoResult = response.body();//得到响应的结果

            //获取到结果为空null
            if (repoResult == null) {
                repoListView.showErrorView("结果为空");
                return;
            }

            //结果不为空 0或者有数据
            if (repoResult.getTotalCount() <= 0) {//总的个数
                repoListView.refreshData(null);//刷新空的数据，告诉里面数据是空的
                repoListView.showEmptyView();//显示空的布局
                return;
            }

            //有数据
            List<Repo> repoList = repoResult.getRepoList();
            repoListView.refreshData(repoList);//刷新出来的数据设置上去
            //下拉刷新第一页数据完成，下一次进行加载从第二页开始
            nextpage = 2;
        }

        //失败的处理
        @Override public void onFailure(Call<RepoResult> call, Throwable t) {
            repoListView.stopRefresh();
            repoListView.showMessage(t.getMessage());
        }
    };

        //上拉加载（异步）
    private Callback<RepoResult> loadMoreCallback = new Callback<RepoResult>() {
            @Override
            public void onResponse(Call<RepoResult> call, Response<RepoResult> response) {
            //获取到数据
            RepoResult repoResult = response.body();
            //空的话就显示加载错误，结果为空
            if (repoResult == null){
                repoListView.showErrorView("结果为空");
                return;
            }
            //不为空。就取出
            List<Repo> repos = repoResult.getRepoList();//得到集合
            repoListView.addLoadDota(repos);//通知视图更改
            nextpage ++;//页数累加
            }

            @Override
            public void onFailure(Call<RepoResult> call, Throwable t) {
            repoListView.hideLoadView();//隐藏视图
            repoListView.showErrorView(t.getMessage());//得到错误信息
            }
        };








//    下拉刷新数据加载 （异步）
   /* class Refresh extends AsyncTask<Void,Void,Void> {
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
    }*/


    //上拉加载数据更多 （异步）
    /*class LoadMore extends AsyncTask<Void,Void,Void> {
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
    }*/

}
