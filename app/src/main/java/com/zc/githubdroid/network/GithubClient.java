package com.zc.githubdroid.network;


import com.zc.githubdroid.login.model.AccessToken;
import com.zc.githubdroid.login.model.User;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;

/**
 *
 *
 */
public class GithubClient implements GithubApi{

    private static GithubClient githubClient;
    private final GithubApi githubApi;

    public static GithubClient getInstance(){
        if (githubClient==null){
            githubClient = new GithubClient();
        }
        return githubClient;
    }

    private GithubClient(){

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //添加Token拦截器
                .addInterceptor(new TokenInterceptor())
                .build();

        //配置Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")//网址
                .client(okHttpClient)//设置okHttpClient

    //Retrofit 强大的功能：Gson转换器----将我们的数据请求的结果进行json转换，转换为我们需要的类型,例如类或者集合
                .addConverterFactory(GsonConverterFactory.create())//添加一个转换工厂。用Gson转换工厂创建出来
                .build();

        //创建GithubApi 接口
        githubApi = retrofit.create(GithubApi.class);
    }



    @Override public Call<AccessToken> getOAuthToken(
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("code") String code) {
        return githubApi.getOAuthToken(clientId, clientSecret, code);
    }

    @Override public Call<User> getUser() {
        return githubApi.getUser();
    }
}
