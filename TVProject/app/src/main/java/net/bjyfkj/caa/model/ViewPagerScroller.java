package net.bjyfkj.caa.model;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

/**
 * Created by YFKJ-1 on 2016/11/9.
 */
public class ViewPagerScroller extends Scroller {
    private int mScrollDuration = 2000;             // 滑动速度

    /**
     * 设置速度速度
     *
     * @param duration
     */
    public void setScrollDuration(int duration) {
        this.mScrollDuration = duration;
    }

    public ViewPagerScroller(Context context) {
        super(context);
    }

    public ViewPagerScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public ViewPagerScroller(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mScrollDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, mScrollDuration);
    }


    public void initViewPagerScroll(ViewPager viewPager) {
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            mScroller.set(viewPager, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

    //这个是设置viewPager切换过度时间的类
//        ViewPagerScroller scroller = new ViewPagerScroller(context);
//        scroller.setScrollDuration(0);
//        scroller.initViewPagerScroll(viewPager);  //这个是设置切换过渡时间为0毫秒
//
//        ViewPagerScroller scroller=new ViewPagerScroller(context);
//        scroller.setScrollDuration(2000);
//        scroller.initViewPagerScroll(viewPager);//这个是设置切换过渡时间为2秒
//        }
