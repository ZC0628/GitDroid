package com.zc.githubdroid.network;

import com.zc.githubdroid.login.model.AccessToken;
import com.zc.githubdroid.login.model.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 *
        放置接口请求API
 */
public interface GithubApi {

    //Github开发者，申请的信息
    String CLIENT_ID="312b04e96e115f1dca73";//gqq：b3e74e2bfb623295d629
    String CLIENT_SECRET="6f4877231c33a8af17184befb252cac15b937043";//553af778b07f66221efc8225e7c20491f88c3169

    //申请时填写的标志（重定向标记）
    String CALL_BACK="feicui";

    //授权登陆时可访问域
    String AUTH_SCOPE="user,public_repo,repo";

    //登录页面的网址(WebView来进行访问)
    String AUTH_URL = "https://github.com/login/oauth/authorize?client_id="+CLIENT_ID+"&scope="+AUTH_SCOPE;

    /**
     * 获得访问令牌Token
     * @return
     */
    @FormUrlEncoded//以表单的形式进行提交
    @POST("https://github.com/login/oauth/access_token")//POST解析
    @Headers("Accept: application/json")//请求头
    Call<AccessToken> getOAuthToken(
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("code") String code);

    /**
     * 使用Token获取用户信息
     * @return
     */
    @GET("user")
    Call<User> getUser();

}
