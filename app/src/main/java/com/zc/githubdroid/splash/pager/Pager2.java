package com.zc.githubdroid.splash.pager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.zc.githubdroid.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 *
 */
public class Pager2 extends FrameLayout {

    @BindView(R.id.ivBubble1) ImageView ivBubble1;
    @BindView(R.id.ivBubble2) ImageView ivBubble2;
    @BindView(R.id.ivBubble3) ImageView ivBubble3;

    /*三个构造方法
       * 1.一般仅在代码中使用
       * 2.在布局中也有使用
       * 3.在布局中使用，并且设置了Style
       * */
//    1.一般仅在代码中使用
    public Pager2(Context context) {
        //null——样式是空的时候调用本类中的构造方法，就是下一个
        this(context, null);
    }

//    2.在布局中也有使用    AttributeSet——资源
    public Pager2(Context context, AttributeSet attrs) {
        //0——空的时候调用下一个构造方法（在布局中添加style属性的时候）
        this(context, attrs, 0);
    }

//    3.在布局中使用，并且设置了Style    defStyleAttr——样式
    public Pager2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.content_pager_2, this, true);
        ButterKnife.bind(this);//绑定
        ivBubble1.setVisibility(GONE);
        ivBubble2.setVisibility(GONE);
        ivBubble3.setVisibility(GONE);
    }

    //显示页面独有的动画
    public void showAnimation() {
        /**
         * YoYo.with(Techniques.Tada)——进入时候的效果
         .duration(700) 持续多长时间
         .playOn(findViewById(R.id.edit_area)); //播放在谁的身上
         */

       /* if (ivBubble1.getVisibility()!=VISIBLE){*/
            postDelayed(new Runnable() {
                @Override public void run() {
                    ivBubble1.setVisibility(VISIBLE);
                    YoYo.with(Techniques.FadeInLeft).duration(300).playOn(ivBubble1);
                }
            },100);//延迟的毫秒时间
            postDelayed(new Runnable() {
                @Override public void run() {
                    ivBubble2.setVisibility(VISIBLE);
                    YoYo.with(Techniques.FadeInRight).duration(300).playOn(ivBubble2);
                }
            },600);//在以上100毫秒的基础上，加上500后执行
            postDelayed(new Runnable() {
                @Override public void run() {
                    ivBubble3.setVisibility(VISIBLE);
                    YoYo.with(Techniques.FadeInDown).duration(300).playOn(ivBubble3);
                }
            },1100);//在以上600毫秒的基础上，加上500后执行

        }
    }
//}
