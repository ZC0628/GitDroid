package com.zc.githubdroid.favorite.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.zc.githubdroid.favorite.model.LocalRepo;
import com.zc.githubdroid.favorite.model.RepoGroup;

import java.sql.SQLException;

/**
 *      数据库——增删改查
 */
public class DBHelp extends OrmLiteSqliteOpenHelper{

    private static final String DB_NAME="repo_favorite.db";//数据库的名字
    private static final int VERSION = 2;//更新版本号判断数据库有没有更新，数据库有更改要提升版本号
    //对数据库中的数据或字段进行更改后版本号要进行更新，才会调用onUpgrade

    private static DBHelp dbHelp;
    private Context context;

    //单例
    public static synchronized DBHelp getInstance(Context context){
        if (dbHelp == null){
            //getApplicationContext——程序一运行上下文已经存在的
            dbHelp = new DBHelp(context.getApplicationContext());
        }
        return dbHelp;
    }

    private DBHelp(Context context) {
        super(context, DB_NAME, null, VERSION);//数据库名字，工厂，版本号
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        //对表进行创建
        try {
            //TableUtils——创建类别表（单纯的创建出来，里面是空的，没有数据）
            TableUtils.createTableIfNotExists(connectionSource, RepoGroup.class);//表不存在的话
            //创建本地仓库表
            TableUtils.createTableIfNotExists(connectionSource, LocalRepo.class);//

            //将本地的数据填充到数据库表中
            new RepoGroupDao(this).createOrUpdate(RepoGroup.getDefaultGroup(context));
            new LocalRepoDao(this).createOrUpdate(LocalRepo.getDefaultLocalRepo(context));
        } catch (SQLException e) {
            throw new RuntimeException(e);//抛出
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        //对表进行更新---方法：先删除，再创建
        try {
            //先删除表
            TableUtils.dropTable(connectionSource,RepoGroup.class,true);//true——是不是忽略错误
            TableUtils.dropTable(connectionSource,LocalRepo.class,true);
            onCreate(database,connectionSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
