package com.example.zhengmin.mixstack.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.example.zhengmin.mixstack.R;
import com.example.zhengmin.mixstack.base.RouterManager;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, BaseFragmentActivity.class);
//                MainActivity.this.startActivity(intent);
                Bundle bundle = new Bundle();
                bundle.putString("key","test");
                RouterManager.getInstance().jumpTo(MainActivity.this,
                        "com.example.zhengmin.mixstack.fragment.BlankFragment",
                        RouterManager.ROUTER_TYPE_NORMAL,bundle);
            }
        });
    }
}
