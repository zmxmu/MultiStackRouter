package com.example.zhengmin.navigation.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.zhengmin.navigation.R;
import com.syswin.msgseal.navigation.BaseActivity;
import com.syswin.msgseal.navigation.PageNavigation;
import com.syswin.msgseal.navigation.annotation.Page;

import static com.syswin.msgseal.navigation.NavigationFlags.GOTO_NORMAL;

@Page(url = "com.example.zhengmin.navigation.activity.MainActivity")
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, FragmentContainerActivity.class);
//                MainActivity.this.startActivity(intent);
                Bundle bundle = new Bundle();
                bundle.putString("key","test");
                PageNavigation.getInstance().goTo(MainActivity.this,
                        "BlankFragmentA",
                        GOTO_NORMAL,bundle);
            }
        });
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
