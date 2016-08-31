package com.zc.githubdroid.github.repoInfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zc.githubdroid.R;
import com.zc.githubdroid.commons.ActivityUtils;
import com.zc.githubdroid.github.hotrepo.repolist.model.Repo;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 16-8-30.
 *          仓库详情页
 */
public class RepoInfoActivity extends AppCompatActivity implements RepoInfoPresenter.RepoInfoView{

    /**
     * 1.展示仓库详情
     *
     *
     *
     */

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.ivIcon) ImageView ivIcon;
    @BindView(R.id.tvRepoName) TextView tvRepoName;
    @BindView(R.id.tvRepoStars) TextView tvRepoStars;
    @BindView(R.id.tvRepoInfo) TextView tvRepoInfo;
    @BindView(R.id.webView) WebView webView;
    @BindView(R.id.progressBar) ProgressBar progressBar;

    private ActivityUtils activityUtils;//工具类
    private Repo repo;//仓库
    private final static String KEY_REPO = "key_repo";//只在仓库中用到的key
    private RepoInfoPresenter repoInfoPresenter;//业务处理详情页

    //防止提供方法，被别人修改   @NonNull——限制不能为空
    public static void open(Context context,@NonNull Repo repo){
        Intent intent = new Intent(context,RepoInfoActivity.class);
        intent.putExtra(KEY_REPO, repo);
        context.startActivity(intent);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUtils = new ActivityUtils(this);
        //设置View布局的时候就会触发调用onContentChanged
        setContentView(R.layout.activity_repo_info);

        //详情页
        repoInfoPresenter = new RepoInfoPresenter(this);
        repoInfoPresenter.getReadme(repo);


    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);

        //初始化
        repo = (Repo) getIntent().getSerializableExtra(KEY_REPO);

        setSupportActionBar(toolbar);//Toolbar放到Actionbar上
        //设置返回键
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //设置Toolbar的标题
        getSupportActionBar().setTitle(repo.getName());//仓库名
        //全称
        tvRepoName.setText(repo.getFullName());
        //描述
        tvRepoInfo.setText(repo.getDescription());
        //星星和fork数量    占位符 %d    后面的是取代占位符
        tvRepoStars.setText(String.format("start: %d  fork: %d", repo.getStarCount(), repo.getForksCount()));
        //显示图片
        ImageLoader.getInstance().displayImage(repo.getOwner().getAvatar(),ivIcon);

    }

    //选项菜单
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
     //item的ID是安卓自带的桌面返回键就页面返回
        if(item.getItemId() == android.R.id.home){
            finish();
     }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showMessage(String msg) {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void setData(String htmlContent) {
        //加载html字符串数据
        webView.loadData(htmlContent,"text/html", "UTF-8");

    }
}
