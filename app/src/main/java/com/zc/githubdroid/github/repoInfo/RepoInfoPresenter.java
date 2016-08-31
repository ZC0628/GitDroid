package com.zc.githubdroid.github.repoInfo;


import android.util.Base64;

import com.zc.githubdroid.commons.LogUtils;
import com.zc.githubdroid.github.hotrepo.repolist.model.Repo;
import com.zc.githubdroid.network.GithubClient;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 主要进行业务的处理：
 * 1.获取readme文件
 * 2.对文件进行解密
 * 3.获取文件的内容
 */
public class RepoInfoPresenter {

    private RepoInfoView repoInfoView;

    //主要完成视图 ——内部接口
    public interface RepoInfoView {
        /**
         * 1.显示进度条
         * 2.隐藏进度条
         * 3.显示信息
         * 4.加载完数据，显示数据
         */

        void showProgress();

        void hideProgress();

        void showMessage(String msg);

        void setData(String htmlContent);
    }

    private Call<RepoContentResult> repoContentCall;
    private Call<ResponseBody> responseBodyCall;

    public RepoInfoPresenter(RepoInfoView repoInfoView) {
        this.repoInfoView = repoInfoView;
    }


    public void getReadme(Repo repo) {
        repoInfoView.showProgress();//显示进度条

        String owner = repo.getOwner().getLogin();//登录所用账号
        String name = repo.getName();//得到仓库的名字
        if (repoContentCall != null) {//已经构建过了或者请求了
            repoContentCall.cancel();
        }
        //构建请求
        repoContentCall = GithubClient.getInstance().getReadme(owner, name);
        repoContentCall.enqueue(repoContentCallback);

    }

    //异步任务
    private Callback<RepoContentResult> repoContentCallback = new Callback<RepoContentResult>() {
        @Override public void onResponse(Call<RepoContentResult> call, Response<RepoContentResult> response) {
            String content = response.body().getContent();

            //Base64的解码操作   加密encode
            byte[] body = Base64.decode(content, Base64.DEFAULT);//默认的形式

            //根据readme获得MarkDown文件内容，这个内容要以HTML形式展示出来
            MediaType mediatype = MediaType.parse("text/plain");
            RequestBody requestBody = RequestBody.create(mediatype, body);
            responseBodyCall = GithubClient.getInstance().markDown(requestBody);//获取markDown文件
            responseBodyCall.enqueue(responseBodyCallback);

        }

        @Override public void onFailure(Call<RepoContentResult> call, Throwable t) {
            repoInfoView.hideProgress();//隐藏进度条
            repoInfoView.showMessage(t.getMessage());//展示错误信息
        }
    };


    private Callback<ResponseBody> responseBodyCallback = new Callback<ResponseBody>() {
        @Override public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            //数据已经获取出来了，隐藏进度条
            repoInfoView.hideProgress();
            try {
                String htmlContent = response.body().string();//最后要展示出来的内容
                LogUtils.d(htmlContent);
                //获取到的数据填充上
                repoInfoView.setData(htmlContent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override public void onFailure(Call<ResponseBody> call, Throwable t) {
            repoInfoView.hideProgress();//隐藏进度条
            repoInfoView.showMessage(t.getMessage());//展示错误信息
        }
    };
}
