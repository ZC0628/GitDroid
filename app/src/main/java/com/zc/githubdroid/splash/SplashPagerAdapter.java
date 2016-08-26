package com.zc.githubdroid.splash;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.zc.githubdroid.splash.pager.Pager0;
import com.zc.githubdroid.splash.pager.Pager1;
import com.zc.githubdroid.splash.pager.Pager2;


/**
 *
 *      ViewPager的适配器
 */
public class SplashPagerAdapter extends PagerAdapter {

    //添加数据View   用来存储数据
    private View[] views;

    public SplashPagerAdapter(Context context) {
        //初始化数据并添加到Views数组中
        views = new View[]{new Pager0(context),new Pager1(context),new Pager2(context)};
    }

    // 告诉返回的是哪一个
    public View getView(int position){
        return views[position];
    }

    @Override public int getCount() {
        //判断是否为空，为null就等于 0 。不为null就等于 views的长度
        return views==null?0:views.length;
    }

    @Override public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override public Object instantiateItem(ViewGroup container, int position) {
        container.addView(views[position]);
        return views[position];//添加视图
    }

    @Override public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views[position]);//删除视图
    }
}
