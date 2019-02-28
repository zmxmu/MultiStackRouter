package com.example.zhengmin.navigation.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.example.zhengmin.navigation.R;
import com.syswin.msgseal.navigation.RouterManager;
import com.syswin.msgseal.routeprocessor.PageNavigationRoute;

@PageNavigationRoute(url = "MainActivity2")
public class MainActivity2 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, FragmentContainerActivity.class);
//                MainActivity.this.startActivity(intent);
                Bundle bundle = new Bundle();
                bundle.putString("key","test");
                RouterManager.getInstance().goTo(MainActivity2.this,
                        "com.example.zhengmin.navigation.activity.MainActivity",
                        RouterManager.GOTO_ACTION_SINGLE,bundle);}});
    }

}
