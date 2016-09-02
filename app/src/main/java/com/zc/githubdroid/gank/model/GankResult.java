package com.zc.githubdroid.gank.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 123 on 2016/9/1.
 *      干货结果
 */
public class GankResult {

    private List<String> category;//分类

    private boolean error;//错误

    private Result result;//结果

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    //
    public static class Result{

        @SerializedName("Android")
        private List<GankItem> androidItems;

        public List<GankItem> getAndroidItems()  {
            return androidItems;
        }
    }
}
