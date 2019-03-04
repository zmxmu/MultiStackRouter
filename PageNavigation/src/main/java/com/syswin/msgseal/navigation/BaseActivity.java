package com.syswin.msgseal.navigation;

import android.app.Activity;

public class BaseActivity extends Activity {
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        PageNavigation.getInstance().go(this,NavigationFlags.GOBACK_NORMAL);
    }
}
