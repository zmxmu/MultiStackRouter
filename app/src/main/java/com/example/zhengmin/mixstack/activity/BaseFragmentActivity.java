package com.example.zhengmin.mixstack.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.example.zhengmin.mixstack.R;
import com.example.zhengmin.mixstack.fragment.BlankFragment;

public class BaseFragmentActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_fragment);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.content,new BlankFragment());
        fragmentTransaction.commit();
    }
}
