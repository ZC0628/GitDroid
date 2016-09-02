package com.zc.githubdroid.favorite;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.zc.githubdroid.R;
import com.zc.githubdroid.favorite.dao.DBHelp;
import com.zc.githubdroid.favorite.dao.LocalRepoDao;
import com.zc.githubdroid.favorite.dao.RepoGroupDao;
import com.zc.githubdroid.favorite.model.LocalRepo;
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
    private FavoriteAdapter adapter;
    private int currentRepoGroupId;
    private LocalRepo currentlocalRepo;//当前操作的本地仓库

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
        adapter = new FavoriteAdapter();
        listView.setAdapter(adapter);

        //默认显示的是全部的数据
        setData(R.id.repo_group_all);

        //注册上下文菜单——表明我们的菜单是作用到Listview身上的
        registerForContextMenu(listView);



    }

    //弹出式菜单
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
        setData(currentRepoGroupId);
        return true;
    }


    private void setData(int groupId) {
        switch (groupId){
            case R.id.repo_group_all://全部
                adapter.setData(localRepoDao.queryAll());//到本地仓库表查询所有的
                break;
            case R.id.repo_group_no://未分类
                adapter.setData(localRepoDao.queryNoGroup());
                break;
            default:
                //通过ID来查询（网络连接...）
                adapter.setData(localRepoDao.queryForGroupId(currentRepoGroupId));
                break;
        }
    }

    /**
     * 上下文菜单ContextMenu——作用到谁身上弹出的菜单
     *1.表明我们作用到谁身上...作用到Listview上
     *2.创建出上下文菜单——移动至，删除
     *3.监听点击的菜单哪一项
     *4.
     */
    /*
    ContextMenu menu——上下文菜单
    View v——作用到谁身上的
    ContextMenu.ContextMenuInfo menuInfo——上下文菜单的基本详细信息
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //是不是展示在Listview上
        if(v.getId() == R.id.listView){
            /**
             * 使用ContextMenuInfo实现类完成我们选择的仓库获取
             * position  利用adapter来获取我们当前作用的仓库
             *
             */
        //基本的menuInfo就在adapterContextMenuInfo中实现
            AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
            int position = adapterContextMenuInfo.position;//获得位置
            //获取当前操作的本地仓库
            currentlocalRepo = adapter.getItem(position);




            //用菜单填充器将menu填充到ContextMenu
            MenuInflater menuInflater = getActivity().getMenuInflater();//得到菜单填充器
            menuInflater.inflate(R.menu.menu_context_favorite,menu);//menu,填充到上下文菜单上

        //将我们数据库类别表里面的类别数据填充到移动至的子菜单里面
            SubMenu subMenu = menu.findItem(R.id.sub_menu_move).getSubMenu();//找到移动至菜单并获得子菜单
        //仓库类别表查询所有的类别信息
            List<RepoGroup> repoGroups = repoGroupDao.queryForAll();
            //利用增强For循环去添加子菜单
            for (RepoGroup repogroup:repoGroups) {
                subMenu.add(R.id.menu_group_move,repogroup.getId(),Menu.NONE,repogroup.getName());
            }




        }
    }

    //监听点击的菜单哪一项
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();//点击哪一项Item的ID
        if ( id == R.id.delete) {//点击的是不是删除的ID
            //删除作用的仓库
            localRepoDao.delete(currentlocalRepo);//这个里面是要操作的仓库
            //重置数据
            setData(currentRepoGroupId);
            return true;
        }
        //移动至中的ID
        int groupID = item.getGroupId();
        if(groupID == R.id.menu_group_move){
         //移动的操作.未分类/网络连接 （数据库中获取）
        if(id == R.id.repo_group_no){//未分类
        //将我们作用的仓库类别改为未分类，也就是类别为null
        currentlocalRepo.setRepoGroup(null);
        }else{
            //得到我们点击的是哪一个类别，将我们当前的仓库类别改为当前点击的类别
            RepoGroup repoGroup = repoGroupDao.queryForId(id);//根据ID查询当前类别
        currentlocalRepo.setRepoGroup(repoGroup);//
        }
        //数据库的更新
         localRepoDao.createOrUpdate(currentlocalRepo);
        setData(currentRepoGroupId);//数据重新获取
        return true;
        }
        return super.onContextItemSelected(item);
    }
}
