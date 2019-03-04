package com.syswin.msgseal.navigation;

import android.app.Activity;
import android.util.DisplayMetrics;

import com.syswin.msgseal.navigation.animator.PageTransferAnimator;
import com.syswin.msgseal.navigation.animator.SlideLeftRightAnimator;
import com.syswin.msgseal.navigation.animator.SlideUpDownAnimator;
import com.syswin.msgseal.navigation.entity.PageItem;

/**
 * 页面导航器帮助类
 */
public class NavigationHelper {
    //跳转动画启动
    public static final int ANIMATOR_START = 0;
    //跳转动画结束
    public static final int ANIMATOR_FINISH = 1;
    //侧滑动画
    public static final int ANIMATOR_SLIDE_LEFT_RIGHT = 0;
    //上下滑动动画
    public static final int ANIMATOR_SLIDE_UP_DOWN = 1;

    public static final int ANIMATOR_ARRAY[][][] = {
            {{R.anim.slide_in_right, R.anim.slide_out_left}, {R.anim.slide_in_left, R.anim.slide_out_right}},
            {{R.anim.slide_in_bottom, R.anim.slide_out_top}, {R.anim.slide_in_top, R.anim.slide_out_bottom}},
    };

    /**
     * 根据动画类型获得具体的动画执行对象
     *
     * @param animatorType 动画类型
     */
    public static PageTransferAnimator initAnimator(Activity activity, int animatorType) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindow().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        switch (animatorType) {
            case ANIMATOR_SLIDE_LEFT_RIGHT:
                return new SlideLeftRightAnimator(activity, displayMetrics.widthPixels);
            case ANIMATOR_SLIDE_UP_DOWN:
                return new SlideUpDownAnimator(activity, displayMetrics.heightPixels);
        }
        return null;
    }

    /**
     * 执行退出动画
     */
    public static void executeExitAnimator(Activity activity) {
        PageItem topItem = PageNavigation.getInstance().getTopItem();
        int animatorArray[] = ANIMATOR_ARRAY[topItem.getAnimatorType()][ANIMATOR_FINISH];
        if (animatorArray != null && animatorArray.length == 2) {
            activity.overridePendingTransition(animatorArray[0], animatorArray[1]);
        }
    }
}
