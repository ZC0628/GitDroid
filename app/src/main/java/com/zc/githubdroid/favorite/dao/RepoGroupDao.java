package com.zc.githubdroid.favorite.dao;


import com.j256.ormlite.dao.Dao;
import com.zc.githubdroid.favorite.model.RepoGroup;

import java.sql.SQLException;
import java.util.List;

/**
 *      完成对RepoGroup表进行操作
 */
public class RepoGroupDao {

    //创建Dao文件——仓库类别，ID
    private Dao<RepoGroup,Long> dao;

    public RepoGroupDao(DBHelp dbHelp) {
        try {
            //初始化Dao文件
            dao = dbHelp.getDao(RepoGroup.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 对于仓库类别表进行添加和更新
     * @param repoGroup 类别表
     */
    public void createOrUpdate(RepoGroup repoGroup){
        try {
            //添加和更新
            dao.createOrUpdate(repoGroup);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 对于仓库类别表进行添加和更新
     *  有多个数据进行同时更改的时候
     */
    public void createOrUpdate(List<RepoGroup> list){
        for (RepoGroup repo:list) {
            createOrUpdate(repo);
        }
    }

    /**
     * 查询所有的
     * @return select * from RepoGroup
     */
    public List<RepoGroup> queryForAll(){
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据id去进行查询
     * @param id
     * @return
     */
    public RepoGroup queryForId(long id){
        try {
            return dao.queryForId(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
