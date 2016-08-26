package com.zc.githubdroid.splash;

import android.animation.ArgbEvaluator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;


import com.zc.githubdroid.R;
import com.zc.githubdroid.splash.pager.Pager2;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

/**
 *
 *   引导页面
 */
public class SplashPagerFragment extends Fragment {


    @BindView(R.id.ivPhoneFont) ImageView ivPhoneFont;
    @BindView(R.id.layoutPhone) FrameLayout layoutPhone;//会缩放的手机
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.indicator)
    CircleIndicator indicator;//ViewPager指示器，三个小圆点
    @BindView(R.id.content) FrameLayout frameLayout;

    @BindColor(R.color.colorGreen) int colorGreen;
    @BindColor(R.color.colorRed) int colorRed;
    @BindColor(R.color.colorYellow) int colorYellow;

    private SplashPagerAdapter adapter;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash_pager, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new SplashPagerAdapter(getContext());
        viewPager.setAdapter(adapter);
        indicator.setViewPager(viewPager);

        //ViewPager的监听
        //颜色的监听
        viewPager.addOnPageChangeListener(pageColorListener);
        //手机动画效果的监听
        viewPager.addOnPageChangeListener(phoneViewListener);

    }

    //设置的ViewPager背景颜色的监听
    private ViewPager.OnPageChangeListener pageColorListener = new ViewPager.OnPageChangeListener() {

        //带透明度颜色的生成
        ArgbEvaluator argbEvaluator = new ArgbEvaluator();

        /*
        滑动的时候——滑动的时候颜色的改变就可以在这里执行
        positionOffset——偏移量
        positionOffsetPixels——像素
         */

        @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            // 第一个页面
            if (position == 0) {
                //颜色的转变
                int color = (int) argbEvaluator.evaluate(positionOffset, colorGreen, colorRed);
                //设置到整体的布局上
                frameLayout.setBackgroundColor(color);
                return;//结束函数
            }
            //第二个页面
            if (position==1){
                int color = (int) argbEvaluator.evaluate(positionOffset, colorRed, colorYellow);
                frameLayout.setBackgroundColor(color);
                return;
            }
        }

        //页面选择
        @Override public void onPageSelected(int position) {

        }

        //滑动时候页面的改变
        @Override public void onPageScrollStateChanged(int state) {

        }
    };

    //实现“手机”的动画（缩放，移动，透明度）
    private ViewPager.OnPageChangeListener phoneViewListener = new ViewPager.OnPageChangeListener() {

        @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (position==0){
                //手机缩放的效果
                /**
                 * positionOffset  0-1  偏移量
                 */
            //偏移量为0没有移动的时候，手机的大小
                float scale = 0.3f +positionOffset*0.7f;
                layoutPhone.setScaleY(scale);
                layoutPhone.setScaleX(scale);

                //手机移动的效果  360的话就超出屏幕了
                int scroll = (int) ((positionOffset-1)*180);
                layoutPhone.setTranslationX(scroll);

                //手机内字体图片透明度的改变
                ivPhoneFont.setAlpha(positionOffset);
            }
            //手机从第二页面切换动画时
            //根据像素的偏移设置移动的效果
            if (position==1){
                layoutPhone.setTranslationX(-positionOffsetPixels);
            }
        }

        //滑动到最后一个页面
        @Override public void onPageSelected(int position) {
            if (position==2){
                Pager2 pager2 = (Pager2) adapter.getView(position);
                pager2.showAnimation();
            }
        }

        @Override public void onPageScrollStateChanged(int state) {

        }
    };
}
