package com.zc.githubdroid.favorite.model;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.apache.commons.io.IOUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 *      仓库类别表
 */

//注解说明这是一个数据库表
@DatabaseTable(tableName = "repostiory_group")//数据库表——仓库类别
public class RepoGroup {
    /**
     * id : 1
     * name : 网络连接
     */

    //主键--不能重复，一般作为唯一标示
    @DatabaseField(id = true)//数据库字段
    private int id;

    @DatabaseField(columnName = "NAME")
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //本地数据
    private static List<RepoGroup> repoGroupList;

    //读取本地数据
    public static List<RepoGroup> getDefaultGroup(Context context){
        if (repoGroupList != null){
            return repoGroupList;
        }
        try {
            InputStream inputStream = context.getAssets().open("repogroup.json");//读取本地资源
            String content = IOUtil.toString(inputStream);//流转换成字符串
            //字符串转换成集合
            Gson gson = new Gson();
            repoGroupList = gson.fromJson(content,new TypeToken<List<RepoGroup>>(){}.getType());
            return repoGroupList;
        } catch (IOException e) {
            throw new RuntimeException(e);//抛出运行时异常
        }
    }
}
