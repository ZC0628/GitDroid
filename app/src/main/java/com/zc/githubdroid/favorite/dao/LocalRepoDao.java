package com.zc.githubdroid.favorite.dao;


import com.j256.ormlite.dao.Dao;
import com.zc.githubdroid.favorite.model.LocalRepo;

import java.sql.SQLException;
import java.util.List;

/**
 * 本地仓库的Dao文件 ——本地仓库表的增删改查
 */

public class LocalRepoDao {

    private Dao<LocalRepo,Long> dao;//本地仓库数据库表，ID

    public LocalRepoDao(DBHelp dbHelp) {
        try {
            //获取Dao文件，转入本地仓库数据库表
            dao = dbHelp.getDao(LocalRepo.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 添加和更新数据
     */

    public void createOrUpdate(LocalRepo localRepo){
        try {
            dao.createOrUpdate(localRepo);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 添加和更新多条数据
     */
    public void createOrUpdate(List<LocalRepo> list){
        for (LocalRepo localrepo:list) {
            createOrUpdate(localrepo);
        }
    }

    /**
     * 删除
     * @param localrepo  哪一条
     */
    public void delete(LocalRepo localrepo){
        try {
            dao.delete(localrepo);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询---根据本地仓库的外键（仓库类别表）的id，相应就查到不同类别的仓库信息
     */
    public List<LocalRepo> queryForGroupId(int groupId){
        try {
            return dao.queryForEq(LocalRepo.COLUMN_GROUP_ID,groupId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询未分类的仓库数据
     * @return
     */
    public List<LocalRepo> queryNoGroup(){
        try {
            return dao.queryBuilder().where().isNull(LocalRepo.COLUMN_GROUP_ID).query();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询所有的数据
     * @return
     */
    public List<LocalRepo> queryAll(){
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
