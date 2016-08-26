package com.zc.githubdroid.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.zc.githubdroid.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 *  上拉加载时出现的尾部布局   自定义布局
 *  创建的ListView的尾布局，主要实现上拉加载的视图实现
 */
public class FooterView extends FrameLayout{

    private static final int STATE_LOADING = 0;//正在加载的状态
    private static final int STATE_ERROR = 1;//加载错误的状态
    private static final int STATE_COMPLETE = 2;//加载完成的状态

    private int state = STATE_LOADING;//状态
    @BindView(R.id.progressBar) ProgressBar progressBar;//进行加载的时候，显示进度条
    @BindView(R.id.tv_error) TextView tv_error;//加载发生错误



    public FooterView(Context context) {
        this(context, null);//调用两个参数的
    }

    public FooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);//调用三个参数的
    }

    public FooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();//实现方法
    }

    //布局的填充
    private void initView(){
        LayoutInflater.from(getContext()).inflate(R.layout.content_load_footer, this, true);
        //  @BindView 创建出来一定要进行绑定
        ButterKnife.bind(this);
    }

    /**
     * 1.进行加载的时候，显示进度条
     * 2.出现错误的视图
     * 3.是不是正在加载
     * 4.是不是加载完成
     */
    //1.显示正在加载的时候
    public void showLoading(){
        state = STATE_LOADING;
        progressBar.setVisibility(VISIBLE);
        tv_error.setVisibility(GONE);
    }

    //2.显示错误的视图
    public void showError(){
        state = STATE_ERROR;
        progressBar.setVisibility(GONE);
        tv_error.setVisibility(VISIBLE);


    }

    //3.是不是正在加载
    public boolean isLoading(){

        return state == STATE_LOADING;
    }

//    4.是不是加载完成
    public boolean iscomplete(){


        return state == STATE_COMPLETE;
    }

}
