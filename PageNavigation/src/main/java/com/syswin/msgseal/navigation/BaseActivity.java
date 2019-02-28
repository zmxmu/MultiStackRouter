package com.syswin.msgseal.navigation;

import android.app.Activity;

import com.syswin.msgseal.navigation.model.PageItem;

import static com.syswin.msgseal.navigation.NavigationHelper.ANIMATOR_FINISH;

public class BaseActivity extends Activity {
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavigationHelper.executeExitAnimator(this);
    }

}
