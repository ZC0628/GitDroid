package com.zc.githubdroid.login;

/**
 *
 *
 */
public interface LoginView {

    /**
     * 1.直接跳转到主页面（授权登录完成，提示登录成功）
     * 2.显示信息
     * 3.显示我们的加载的画面（Gif动画）
     * 4.页面进行刷新，重新进行请求
     */
    //1.直接跳转到主页面
    void gotoMainActivity();

//    2.显示信息
    void showMessage(String msg);

//    3.显示我们的加载的画面（Gif动画）
    void showProgress();

//    4.页面进行刷新，重新进行请求   重置WebView
    void resetWebView();
}
