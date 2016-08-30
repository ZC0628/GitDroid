package com.zc.githubdroid.github.hotrepo;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zc.githubdroid.Language;
import com.zc.githubdroid.github.hotrepo.repolist.HotRepoListFragment;

import java.util.List;
/**
 *
 *  “最热门”的适配器
 *
 */
public class HotRepoAdapter extends FragmentPagerAdapter {

    private List<Language> languages;

    public HotRepoAdapter(FragmentManager fm,Context context) {
        super(fm);
        languages = Language.getDefaultLanguage(context);//获得
    }

    @Override public Fragment getItem(int position) {
        //最热门显示的Listview
        //获得数据
        return HotRepoListFragment.getInstance(languages.get(position));
    }

    @Override public int getCount() {
        //显示的条目个数
        //如果是空的就等于0，不为空就等于他的长度
        return languages == null?0:languages.size();
    }

     @Override
     public CharSequence getPageTitle(int position) {
         //显示的标题
        return languages.get(position).getName();
    }
}
