package com.zc.githubdroid.network;


import com.zc.githubdroid.login.model.UserRepo;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 *
 *    Token拦截器
 */
public class TokenInterceptor implements Interceptor{

    //拦截方法
    @Override public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();//得到请求的内容
        Request.Builder builder = request.newBuilder();//构建者模式

        //判断用户存储的类中有没有Token
        if (UserRepo.hasAccessToken()){
            //创建请求头的格式：Authorization: token OAUTH-TOKEN
            //有就添加到请求头上
            builder.header("Authorization","token "+UserRepo.getAccessToken());
        }
        //再去请求的时候拦截Token
        Response response = chain.proceed(builder.build());
        //如果请求成功的
        if (response.isSuccessful()){
            return response;//返回请求（响应的结果）
        }
        if (response.code() == 401 || response.code() == 403){
            throw new IOException("未经授权的");
        }else {
            throw new IOException("响应码："+response.code());
        }
    }
}
