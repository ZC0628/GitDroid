package com.zc.githubdroid.github.hotuser;


import com.zc.githubdroid.commons.LogUtils;
import com.zc.githubdroid.login.model.User;
import com.zc.githubdroid.network.GithubClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 123 on 2016/8/30.
 */
public class HotUserPresenter {

    public interface HotUserView{
        /**
         *  刷新视图分析
         * 1.重要的一点：得到网络请求来的数据
         2.显示刷新视图
         3.停止刷新
         4.加载失败，显示失败信息
         5.加载数据为空，没有数据显示空的页面
         6.显示错误的页面
         */

        void showRefreshView();

        void refreshData(List<User> users);

        void stopRefresh();

        void showMessage(String msg);

        void showErrorView();

        void showEmptyView();

        /**
         * 上拉加载视图分析
         * 1.加载出来的数据进行填充
         * 2.显示加载的视图，显示正在加载
         * 3.隐藏加载的视图
         */
        //显示正在加载的视图
        void showLoadView();

        //
        void hideLoadView();

        //加载出来的数据
        void addLoadData(List<User> list);
    }

    private int nextPage = 1;
    private Call<HotUserResult> userCall;
    private HotUserView hotUserView;//接口

    public HotUserPresenter(HotUserView hotUserView) {//接口实例化
        this.hotUserView = hotUserView;
    }

    //刷新的业务
    public void refresh(){
        hotUserView.showRefreshView();//
        hotUserView.hideLoadView();
        nextPage = 1;
        if (userCall!=null){//有数据，构建完成
            userCall.cancel();//取消
        }
        userCall = GithubClient.getInstance().searchUsers("followers:>1000", nextPage);
        userCall.enqueue(userCallback);
    }

    //加载的业务
    public void loadMore(){
        hotUserView.showLoadView();//显示加载视图
        if (userCall!=null){
            userCall.cancel();
        }
        userCall = GithubClient.getInstance().searchUsers("followers:>1000", nextPage);
        userCall.enqueue(loadMoreCallback);
    }

    //进行异步任务（加载）
    private Callback<HotUserResult> loadMoreCallback = new Callback<HotUserResult>() {
        @Override
        public void onResponse(Call<HotUserResult> call, Response<HotUserResult> response) {

            hotUserView.hideLoadView();//隐藏加载视图
            if (response.isSuccessful()){//请求有没有成功
                HotUserResult hotUserResult = response.body();
               if (hotUserResult == null){
                   //数据为空，空页面
                   hotUserView.showErrorView();
                   hotUserView.showMessage("结果为空");
                   return;
               }
                List<User> users = hotUserResult.getUsers();
                //数据拿到了，是不是要通知视图拿到数据
                hotUserView.addLoadData(users);
                nextPage++;//页数在之前的基础上进行增加
                return;
            }
            //没有成功的话，显示个错误信息——响应码在200~299是数据请求成功的状态
            hotUserView.showMessage("响应码："+response.code());
        }

        @Override
        public void onFailure(Call<HotUserResult> call, Throwable t) {
            hotUserView.hideLoadView();//隐藏加载视图
            hotUserView.showMessage("加载失败："+t.getMessage());
        }
    };



    //进行异步任务（刷新）
    private Callback<HotUserResult> userCallback = new Callback<HotUserResult>() {
        @Override
        public void onResponse(Call<HotUserResult> call, Response<HotUserResult> response) {
            if (response.isSuccessful()){
                HotUserResult hotUserResult = response.body();//取出响应体
                if (hotUserResult==null){
                    //显示加载的结果为空，提示信息
                    hotUserView.showMessage("结果为空！");
                    return;
                }
                //通知视图进行数据填充
                List<User> users = hotUserResult.getUsers();//获得用户
                LogUtils.d("获取的数据："+hotUserResult.getTotalCount());//有没有获取到总的数量
                hotUserView.refreshData(users);//获取到的用户数据填充到Listview上进行展示
                hotUserView.stopRefresh();
                //显示到Fragment上
                nextPage = 2;
            }
        }

        @Override
        public void onFailure(Call<HotUserResult> call, Throwable t) {
            //显示加载失败的信息以及视图的改变
            hotUserView.stopRefresh();
            hotUserView.showMessage("刷新失败："+t.getMessage());
        }
    };
}
