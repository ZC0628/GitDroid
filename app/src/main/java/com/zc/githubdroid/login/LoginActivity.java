package com.zc.githubdroid.login;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zc.githubdroid.MainActivity;
import com.zc.githubdroid.R;
import com.zc.githubdroid.commons.ActivityUtils;
import com.zc.githubdroid.commons.LogUtils;
import com.zc.githubdroid.network.GithubApi;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifImageView;

/**
 *         登录界面
 *  1.使用WebView来加载登录网址，实现登录账户的填写及登录
 *  2.是否同意授权。如果同意授权，重定向另一个URL，会有一个临时授权码code
 *  3.拿到临时授权码之后，使用API来获得用户Token
 *  4.获得用户Token之后，访问user，public_repo，repo。主要为了拿到用户信息展示出来
 *  5.
 *
 */
public class LoginActivity extends AppCompatActivity implements LoginView{

    @BindView(R.id.toolbar) Toolbar toolbar;//登录标题
    @BindView(R.id.webView) WebView webView;//加载登录网址
    @BindView(R.id.gifImageView) GifImageView gifImageView;//登录加载时候的gif动画

    private LoginPresenter loginPresenter ;//业务流程
    private ActivityUtils activityUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUtils = new ActivityUtils(this);

        setContentView(R.layout.activity_login);
//        ButterKnife.bind(this);//绑定
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);
        loginPresenter = new LoginPresenter(this);
        setSupportActionBar(toolbar);//设置到ActionBar上，以ActionBar的形式展示Toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//设置向后点击的返回
        initWebView();//初始化WebView，和WebView的简单配置
    }

    private void initWebView() {
        //点击登录的时候删除之前所有的登录信息
        //删除所有的Cookie ,主要是为了清除登录记录
        CookieManager cookiemanager = CookieManager.getInstance();
        cookiemanager.removeAllCookie();//清除所有记录

        //用WebView加载URL——登录界面的网址
        webView.loadUrl(GithubApi.AUTH_URL);
        webView.setFocusable(true);//获得焦点
        //在有焦点的情况下可以获得的手指触摸点击
        webView.setFocusableInTouchMode(true);

        //设置WebView进度的监听，让他加载完之后，隐藏动画，显示WebView
        webView.setWebChromeClient(webChromeClient);
        //监听WebView页面刷新的时候,重定向
        webView.setWebViewClient(webViewClient);

    }

    //WebView进度的监听
    private WebChromeClient webChromeClient = new WebChromeClient(){
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if(newProgress >= 100){// >= 100——加载完成
                gifImageView.setVisibility(View.GONE);
            }


        }
    };

    //监听WebView页面刷新的时候
    //当WebView刷新的时候，这个方法就会触发，刷新包括当我们的账户填写错误，输对了，授权完了也会刷新
    private WebViewClient webViewClient = new WebViewClient(){
            //是否重新加载页面URl
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //String类型的URL——>Uri
            Uri uri = Uri.parse(url);
            //申请时候的重定向标志是不是网址上进行重定向的标志
            if(GithubApi.CALL_BACK.equals(uri.getScheme())){
                //code——临时授权码，
                String code = uri.getQueryParameter("code");
                LogUtils.d("code"+code);
                //得到临时授权码，去执行操作来获取Token
                //利用code获得用户Token
                loginPresenter.login(code);
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }
    };

    /**
     * 需要实现的视图
     * 1.直接跳转到主页面（授权登录完成，提示登录成功）
     * 2.显示信息
     * 3.显示我们的加载的画面（Gif动画）
     * 4.页面进行刷新，重新进行请求
     */
    @Override
    public void gotoMainActivity() {
        //跳转到主页面
        activityUtils.startActivity(MainActivity.class);
        finish();
    }

    @Override
    public void showMessage(String msg) {
        activityUtils.showToast(msg);
    }

    @Override
    public void showProgress() {
        gifImageView.setVisibility(View.VISIBLE);
    }

    @Override
    public void resetWebView() {
            initWebView();
    }



}
