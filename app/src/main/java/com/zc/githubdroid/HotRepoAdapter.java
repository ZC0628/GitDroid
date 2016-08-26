package com.zc.githubdroid;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 *
 *  “最热门”的适配器
 *
 */
public class HotRepoAdapter extends FragmentPagerAdapter {

    public HotRepoAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override public Fragment getItem(int position) {
        //最热门显示的Listview
        return new HotRepoListFragment();
    }

    @Override public int getCount() {
        //显示的条目个数
        return 10;
    }


     @Override
     public CharSequence getPageTitle(int position) {
         //显示的标题
        return "Java"+position+"GitHub";
    }
}
