package com.example.zhengmin.navigation.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.zhengmin.navigation.R;
import com.syswin.msgseal.navigation.BaseActivity;
import com.syswin.msgseal.navigation.RouterManager;
import com.syswin.msgseal.routeprocessor.PageRoute;

@PageRoute(url = "MainActivity2")
public class MainActivity2 extends BaseActivity {

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
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, FragmentContainerActivity.class);
//                MainActivity.this.startActivity(intent);
                Bundle bundle = new Bundle();
                bundle.putString("key","test");
                RouterManager.getInstance().goTo(MainActivity2.this,
                        "MainActivity3",
                        RouterManager.GOTO_ACTION_SINGLE,bundle);}});
    }
    @Override
    protected void onResume(){
        super.onResume();
        Log.e(this.getClass().getSimpleName(),"onResume");
    }
    @Override
    protected void onPause(){
        super.onPause();
        Log.e(this.getClass().getSimpleName(),"onPause");
    }
}
