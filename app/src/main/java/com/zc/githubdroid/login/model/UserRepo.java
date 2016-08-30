package com.zc.githubdroid.login.model;

/**
 *
 *主要是为了暂时存储我们的Token值以及请求得到的个人信息
 */
public class UserRepo {

    private UserRepo(){}

    private static String accessToken;//Token值

    private static User user;//用户信息

    // 当前是否有token
    public static boolean hasAccessToken(){
        return accessToken != null;
    }
    // 当前是否是"空的"(还没有登陆)
    public static boolean isEmpty(){
        return accessToken == null || user == null;
    }
    // 清除信息
    public static void clear(){
        accessToken = null;
        user = null;
    }

    public static void setAccessToken(String accessToken) {
        UserRepo.accessToken = accessToken;
    }

    public static void setUser(User user) {
        UserRepo.user = user;
    }

    //获取Token值
    public static String getAccessToken() {
        return accessToken;
    }

    public static User getUser() {
        return user;
    }



}
