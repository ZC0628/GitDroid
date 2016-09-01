package com.zc.githubdroid;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;
import com.zc.githubdroid.commons.ActivityUtils;
import com.zc.githubdroid.commons.LogUtils;
import com.zc.githubdroid.github.hotrepo.HotRepoFragment;
import com.zc.githubdroid.github.hotuser.HotUserFragment;
import com.zc.githubdroid.login.LoginActivity;
import com.zc.githubdroid.login.model.UserRepo;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *      主页面
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout; // 抽屉(包含内容+侧滑菜单)
    @BindView(R.id.navigationView)
    NavigationView navigationView; // 侧滑菜单视图
    @BindView(R.id.toolbar) Toolbar toolbar;

    // 热门仓库Fragment
    private HotRepoFragment hotRepoFragment;
    private HotUserFragment hotUserFragment;
    private Button btLogin;//登录按钮
    private ImageView ivIcon;//用户的头像
    private ActivityUtils activityUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置当前视图(也就是说，更改了当前视图内容,将导至onContentChanged方法触发)
        setContentView(R.layout.activity_main);

        // 设置navigationView的监听器
       /* navigationView.setNavigationItemSelectedListener(this);
        // 构建抽屉的监听
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        toggle.syncState();// 根据drawerlayout同步其当前状态
        // 设置抽屉监听
        drawerLayout.addDrawerListener(toggle);*/

    }

    @Override public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);
        activityUtils = new ActivityUtils(this);
        // ActionBar处理
        setSupportActionBar(toolbar);
        // 设置navigationView的监听器
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(// 构建抽屉的监听
                this, drawerLayout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        // 根据drawerlayout同步其当前状态
        toggle.syncState();
        // 设置抽屉监听
        drawerLayout.addDrawerListener(toggle);

        //得到NavigationView里面头布局的控件
         // 第一个位置是：头布局
        btLogin = ButterKnife.findById(navigationView.getHeaderView(0), R.id.btnLogin);
        ivIcon = ButterKnife.findById(navigationView.getHeaderView(0), R.id.ivIcon);

        //点击 切换账号 跳转到注册界面
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                activityUtils.startActivity(LoginActivity.class);
                finish();
            }
        });

        // 默认显示的是热门仓库fragment
        hotRepoFragment = new HotRepoFragment();
        replaceFragment(hotRepoFragment);
    }

    //主要做了我们基本登录信息的改变
    @Override
    protected void onStart() {
        super.onStart();
        if(UserRepo.isEmpty()){//空的
            btLogin.setText(R.string.login_github);
            return;
        }
        //有的话
        btLogin.setText(R.string.switch_account);
        //设置主页面的标题
        getSupportActionBar().setTitle(UserRepo.getUser().getLogin());
        LogUtils.d("名字"+UserRepo.getUser().getName());

        //设置用户头像  用picasso获取图片路径
        Picasso.with(this).load(UserRepo.getUser().getAvatar()).into(ivIcon);
//        ImageLoader.getInstance().displayImage(UserRepo.getUser().getAvatar(),ivIcon);
    }


    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    // 侧滑菜单监听器
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // 将默认选中项“手动”设置为false
        //选中的话就不能再点击
        if (item.isChecked()) {
            item.setChecked(false);
        }
        // 根据选择做切换
        switch (item.getItemId()) {
            // 热门仓库
            case R.id.github_hot_repo:
                if (!hotRepoFragment.isAdded()) {
                    replaceFragment(hotRepoFragment);
                }
                break;
            // 热门开发者
            case R.id.github_hot_coder:
                if (hotUserFragment == null) hotUserFragment = new HotUserFragment();
                if (!hotUserFragment.isAdded()) {
                    replaceFragment(hotUserFragment);
                }
                break;
            // 我的收藏
            case R.id.arsenal_my_repo:
                //TODO 收藏
                break;
            // 每日干货
            case R.id.tips_daily:
                //TODO 每日干货
                break;
        }
        // 关闭drawerLayout  从左往右
        drawerLayout.closeDrawer(GravityCompat.START);

        // 返回true，代表将该菜单项变为checked状态
        return true;
    }

    //当按返回键的时候侧滑菜单关闭
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //drawerlayout是不是在打开状态
        //如果是已经打开的状态了，按下返回键的时候就关闭菜单
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }



}
