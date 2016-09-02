package com.zc.githubdroid.favorite.model;

import android.support.annotation.NonNull;

import com.zc.githubdroid.github.hotrepo.repolist.model.Repo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 16-9-2.
 *  仓库转换
 *  将Repo转换成LocalRepo
 *  在方法中传入Repo，返回的是LocalRepo
 */
public class RepoConverter {

    /*
    {
    "id": 22374063,
    "name": "android-best-practices",
    "full_name": "futurice/android-best-practices",
    "avatar_url": "https://avatars.githubusercontent.com/u/852157?v=3",
    "description": "Do's and Don'ts for Android development, by Futurice developers",
    "stargazers_count": 10469,
    "forks_count": 1974
  }
     */
    //在方法中传入Repo，返回的是LocalRepo

    /**
     * 将一个仓库数据转换为本地仓库数据
     * @param repo
     * @return
     */
    public static @NonNull LocalRepo convert(@NonNull Repo repo) {//传入不能为空
        LocalRepo localRepo = new LocalRepo();
    localRepo.setId(repo.getId());//将ID设置到本地仓库上
    localRepo.setName(repo.getName());
    localRepo.setName(repo.getFullName());
    localRepo.setAvatarUrl(repo.getOwner().getAvatar());//加载图片的网址。先获取拥有者
    localRepo.setDescription(repo.getDescription());//描述
    localRepo.setStargazersCount(repo.getStarCount());
    localRepo.setForksCount(repo.getForksCount());
    localRepo.setRepoGroup(null);//没有这个类别
    return localRepo;
    }

    //多个数据收藏（集合转换）
    public static @NonNull List<LocalRepo> convertALL(@NonNull List<Repo> repos){
        ArrayList<LocalRepo> localRepos = new ArrayList<LocalRepo>();//本地仓库集合
        for (Repo repo:repos) {
            localRepos.add(convert(repo));
        }
    return localRepos;
        }

}
