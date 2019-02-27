package com.syswin.msgseal.navigation;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

import com.syswin.msgseal.navigation.animator.PageTransferAnimator;
import com.syswin.msgseal.navigation.animator.SlideLeftRightAnimator;
import com.syswin.msgseal.navigation.animator.SlideUpDownAnimator;

import static com.syswin.msgseal.navigation.RouterManager.ANIMATOR_SLIDE_LEFT_RIGHT;
import static com.syswin.msgseal.navigation.RouterManager.ANIMATOR_SLIDE_UP_DOWN;

public class NavigationHelper {
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
