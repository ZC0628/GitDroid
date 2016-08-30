package com.zc.githubdroid.github.hotrepo.repolist.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *   回应的结果
 */
public class RepoResult {

    /**
     * "total_count": 2074901,
     * "incomplete_results": false,
     * "items":[{}] ——集合
     */
    @SerializedName("total_count")
    private int totalCount;

    @SerializedName("incomplete_results")
    private boolean incompleteResults;

    @SerializedName("items")
    private List<Repo> repoList;


    public int getTotalCount() {
        return totalCount;
    }


    public boolean isIncompleteResults() {
        return incompleteResults;
    }


    public List<Repo> getRepoList() {
        return repoList;
    }
}
