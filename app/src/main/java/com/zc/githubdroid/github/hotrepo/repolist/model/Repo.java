package com.zc.githubdroid.github.hotrepo.repolist.model;

import com.google.gson.annotations.SerializedName;
import com.zc.githubdroid.login.model.User;

import java.io.Serializable;

/**
 *     仓库的类
 */
public class Repo implements Serializable{
    /**
     * {
     "id": 29028775,
     "name": "react-native",
     "full_name": "facebook/react-native",
     "owner": {
     "login": "facebook",
     "id": 69631,
     "avatar_url": "https://avatars.githubusercontent.com/u/69631?v=3",
     },
     "description": "A framework for building native apps with React.",
     "stargazers_count": 33961,
     "forks_count": 7122,
     }
     */

    private int id;
    private String name;//简易名字

    //全名
    @SerializedName("full_name")
    private String fullName;

    //描述
    private String description;

    //星星数量
    @SerializedName("stargazers_count")
    private int starCount;

    //复刻数量
    @SerializedName("forks_count")
    private int forksCount;

    //仓库拥有者
    private User owner;

    public User getOwner() {
        return owner;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return fullName;
    }

    public String getDescription() {
        return description;
    }

    public int getStarCount() {
        return starCount;
    }

    public int getForksCount() {
        return forksCount;
    }
}
