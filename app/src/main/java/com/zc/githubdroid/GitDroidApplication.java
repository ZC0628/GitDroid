package com.zc.githubdroid;

import android.app.Application;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


public class GitDroidApplication extends Application {

    @Override public void onCreate() {
        super.onCreate();

        //
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_avatar)//空的话
                .showImageOnFail(R.drawable.ic_avatar)//显示图片失败的时候
                .cacheInMemory(true)//打开内存缓存
                .cacheOnDisk(true)//打开硬盘缓存
                .build();

        //配置
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this)
                .memoryCacheSize(1024 * 1024 * 10)//内存缓存大小
                .defaultDisplayImageOptions(options)
                .build();

        //添加到ImageLoader上
        ImageLoader.getInstance().init(configuration);

    }
}
