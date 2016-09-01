package com.zc.githubdroid.favorite.model;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.apache.commons.io.IOUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 本地仓库表
 */

@DatabaseTable(tableName = "local_repo")//本地仓库表名
public class LocalRepo {

    /**
      id : 892275
      name : retrofit
      full_name : square/retrofit
      description : Type-safe HTTP client for Android and Java by Square, Inc.
      stargazers_count : 13283
      forks_count : 2656
      avatar_url : https://avatars.githubusercontent.com/u/82592?v=3
      group : {"id":1,"name":"网络连接"}
     *
     */

    public static final String COLUMN_GROUP_ID="group_id";

    //主键 ，不能重复，一般是递增
    @DatabaseField(id = true)
    private int id;

    @DatabaseField
    private String name;//字段

    @DatabaseField(columnName = "full_name")
    //序列化名字——在进行Json转换的时候会自动找到full_name，而对应的就是实体类中的fullname
    @SerializedName("full_name")
    private String fullName;

    @DatabaseField
    private String description;

    @DatabaseField(columnName = "start_count")//显示数据库的字段的时候
    @SerializedName("stargazers_count")
    private int stargazersCount;

    @DatabaseField(columnName = "forks_count")
    @SerializedName("forks_count")
    private int forksCount;

    @DatabaseField(columnName = "avatar_url")
    @SerializedName("avatar_url")
    private String avatarUrl;

    //外键，主要的作用是关联另外一张表中的数据，这个字段是我们数据库中另外一个表，这个字段就是一个外键
    //foreign = true——是外键就true
    @DatabaseField(columnName = COLUMN_GROUP_ID,foreign = true,canBeNull = true)//可以为空
    @SerializedName("group")

    //仓库类别表
    private RepoGroup repoGroup;

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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStargazersCount() {
        return stargazersCount;
    }

    public void setStargazersCount(int stargazersCount) {
        this.stargazersCount = stargazersCount;
    }

    public int getForksCount() {
        return forksCount;
    }

    public void setForksCount(int forksCount) {
        this.forksCount = forksCount;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    private static List<LocalRepo> localRepoList;

    //获取本地仓库表
    public static List<LocalRepo> getDefaultLocalRepo(Context context){
        if (localRepoList != null){//为空
            return localRepoList;
        }
        try {
            InputStream inputStream = context.getAssets().open("defaultrepos.json");
            String content = IOUtil.toString(inputStream);
            Gson gson = new Gson();
            //字符串转换成List集合
            /*
            new TypeToken<List<LocalRepo>>(){}.getType()
            字符串要转换成的格式是List集合
             */
            localRepoList = gson.fromJson(content,new TypeToken<List<LocalRepo>>(){}.getType());
            return localRepoList;//不为空
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
