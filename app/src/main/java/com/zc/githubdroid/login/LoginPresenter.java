package com.zc.githubdroid.login;


import com.zc.githubdroid.login.model.AccessToken;
import com.zc.githubdroid.login.model.User;
import com.zc.githubdroid.login.model.UserRepo;
import com.zc.githubdroid.network.GithubApi;
import com.zc.githubdroid.network.GithubClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 *      业务流程
 */
public class LoginPresenter {

    private Call<AccessToken> tokenCall;
    private Call<User> userCall;

    private LoginView loginView;

    public LoginPresenter(LoginView loginView) {
        this.loginView = loginView;
    }

    /**
     * 登录，完成的工作：使用code 获取Token，再换取用户信息
     *
     * @param code
     */
    public void login(String code) {
        loginView.showProgress();
        //tokenCall请求存在的话
        if (tokenCall != null) {
            tokenCall.cancel();//关闭
        }
        //获取Token      GithubClient实现GithubApi
        tokenCall = GithubClient.getInstance().getOAuthToken(
                GithubApi.CLIENT_ID,
                GithubApi.CLIENT_SECRET,
                code);
        tokenCall.enqueue(tokenCallback);//异步处理，回调操作
    }

    //获取Token返回
    private Callback<AccessToken> tokenCallback = new Callback<AccessToken>() {
        //成功返回
        @Override public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
            AccessToken accessToken = response.body();
            String token = accessToken.getAccessToken();
            //保存用户Token
            UserRepo.setAccessToken(token);

            //使用Token来获取用户信息
            if (userCall!=null){
                userCall.cancel();
            }
            userCall = GithubClient.getInstance().getUser();
            userCall.enqueue(userCallback);
        }

        //请求失败
        @Override public void onFailure(Call<AccessToken> call, Throwable t) {
            /**
             * 1.显示请求失败信息
             * 2.显示我们的进度加载的画面
             * 3.页面进行刷新，重新进行请求
             */
            loginView.showMessage(t.getMessage());
            loginView.showProgress();
            loginView.resetWebView();
        }
    };

    //请求用户信息
    private Callback<User> userCallback = new Callback<User>() {
        @Override public void onResponse(Call<User> call, Response<User> response) {
            //请求完成
            //存储用户信息user
            User user = response.body();
            UserRepo.setUser(user);

            /**
             * 1.提示登录成功
             * 2.直接调转到主页面
             */
            loginView.showMessage("登陆成功");
            loginView.gotoMainActivity();
        }

        @Override public void onFailure(Call<User> call, Throwable t) {
            //请求失败
            /**
             * 1.显示请求失败信息
             * 2.显示我们的进度加载的画面
             * 3.页面进行刷新，重新进行请求
             */
            loginView.showMessage(t.getMessage());
            loginView.showProgress();
            loginView.resetWebView();
        }
    };




}
