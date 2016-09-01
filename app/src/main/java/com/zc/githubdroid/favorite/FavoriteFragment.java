package com.zc.githubdroid.favorite;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.zc.githubdroid.R;
import com.zc.githubdroid.favorite.dao.DBHelp;
import com.zc.githubdroid.favorite.dao.LocalRepoDao;
import com.zc.githubdroid.favorite.dao.RepoGroupDao;
import com.zc.githubdroid.favorite.model.RepoGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *      我的收藏
 */
public class FavoriteFragment extends Fragment implements PopupMenu.OnMenuItemClickListener {

    @BindView(R.id.tvGroupType) TextView tvGroupType;//“全部”
    @BindView(R.id.btnFilter) ImageButton btnFilter;//显示菜单
    @BindView(R.id.listView) ListView listView;//列表

    private RepoGroupDao repoGroupDao;
    private LocalRepoDao localRepoDao;
//    private FavoriteAdapter adapter;
    private int currentRepoGroupId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repoGroupDao = new RepoGroupDao(DBHelp.getInstance(getContext()));
        localRepoDao = new LocalRepoDao(DBHelp.getInstance(getContext()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
//        adapter = new FavoriteAdapter();
//        listView.setAdapter(adapter);

        //默认显示的是全部的数据
//        setData(R.id.repo_group_all);
    }

    @OnClick(R.id.btnFilter)
    public void showPopupMenu(View view){
        PopupMenu popupMenu = new PopupMenu(getContext(),view);

        //给弹出式菜单PopupMenu填充本地Menu
        popupMenu.inflate(R.menu.menu_popup_repo_groups);
        //弹出菜单的点击
        popupMenu.setOnMenuItemClickListener(this);

        /** 我们自己在类别表里面其他的分类，怎么进行填充
         * 1.拿到Menu
         * 2.读取数据库的数据
         * 3.数据填充到menu上
         */
        Menu menu = popupMenu.getMenu();
        //查询所有类别
        List<RepoGroup> repoGroups = repoGroupDao.queryForAll();
        for (RepoGroup repo:repoGroups) {
            //Menu.NONE——只有一个一级菜单，并没有进行分组和分类
            //item的ID
            //排序
            //名字
            menu.add(Menu.NONE,repo.getId(),Menu.NONE,repo.getName());
        }
        popupMenu.show();//展示
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        /**
         * 1.改变标题
         * 2.数据改变
         */
        //改变标题（全部，未分类....）
        tvGroupType.setText(item.getTitle().toString());
        //根据我们点击的仓库类别的id去获取不同的类别仓库信息
        currentRepoGroupId = item.getItemId();
//        setData(currentRepoGroupId);
        return true;
    }

//    private void setData(int groupId) {
//        switch (groupId){
//            case R.id.repo_group_all:
//                adapter.setData(localRepoDao.queryAll());
//                break;
//            case R.id.repo_group_no:
//                adapter.setData(localRepoDao.queryNoGroup());
//                break;
//            default:
//                adapter.setData(localRepoDao.queryForGroupId(currentRepoGroupId));
//                break;
//        }
//    }
}
