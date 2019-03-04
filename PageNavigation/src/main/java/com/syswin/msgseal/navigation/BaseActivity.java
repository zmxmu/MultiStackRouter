package com.syswin.msgseal.navigation;

import android.app.Activity;

public class BaseActivity extends Activity {
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavigationHelper.executeExitAnimator(this);
    }
}
