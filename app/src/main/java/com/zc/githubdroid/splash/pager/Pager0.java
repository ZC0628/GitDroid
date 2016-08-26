package com.zc.githubdroid.splash.pager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.zc.githubdroid.R;


public class Pager0 extends FrameLayout {

    //在代码中调用的时候
    public Pager0(Context context) {
        //null——样式是空的时候调用本类中的构造方法，就是下一个
        this(context, null);
    }

    // 在布局中使用的时候
    public Pager0(Context context, AttributeSet attrs) {
    //0——空的时候调用下一个构造方法（在布局中添加style属性的时候）
        this(context, attrs, 0);
    }

    //在布局中使用的时候添加的一些属性style
    public Pager0(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        //布局加载器获取上下文，获取布局并添加到Pager0
        LayoutInflater.from(getContext()).inflate(R.layout.content_pager_0, this, true);
    }
}
