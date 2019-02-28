package com.syswin.msgseal.navigation;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

import com.syswin.msgseal.navigation.animator.PageTransferAnimator;
import com.syswin.msgseal.navigation.animator.SlideLeftRightAnimator;
import com.syswin.msgseal.navigation.animator.SlideUpDownAnimator;

public class NavigationHelper {
    public static final int ANIMATOR_START = 0;
    public static final int ANIMATOR_FINISH = 1;

    public static final int ANIMATOR_SLIDE_LEFT_RIGHT = 0;
    public static final int ANIMATOR_SLIDE_UP_DOWN = 1;

    public static final int ANIMATOR_ARRAY[][][]= {
            {{R.anim.slide_in_right,R.anim.slide_out_left},{R.anim.slide_in_left,R.anim.slide_out_right}},
            {{R.anim.slide_in_bottom,R.anim.slide_out_top},{R.anim.slide_in_top,R.anim.slide_out_bottom}},
    };
    public static PageTransferAnimator initAnimator(Activity activity,int animatorType){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindow().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        switch (animatorType){
            case ANIMATOR_SLIDE_LEFT_RIGHT:
                return new SlideLeftRightAnimator(activity,displayMetrics.widthPixels);
            case ANIMATOR_SLIDE_UP_DOWN:
                return new SlideUpDownAnimator(activity,displayMetrics.heightPixels);
        }
        return null;
    }
}
