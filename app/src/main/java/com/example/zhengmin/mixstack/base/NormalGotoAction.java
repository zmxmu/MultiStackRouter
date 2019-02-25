package com.example.zhengmin.mixstack.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.zhengmin.mixstack.activity.BaseFragmentActivity;

import static com.example.zhengmin.mixstack.base.RouterManager.BUNDLE_KEY_FRAGMENT;
import static com.example.zhengmin.mixstack.base.RouterManager.BUNDLE_KEY_PATH;

public class NormalGotoAction implements GotoAction {
    @Override
    public boolean gotoPage(Context context, String path, Bundle bundle, int itemType) {
        switch (itemType){
            case RouterItem.ROUTER_TYPE_ACTIVITY:
                RouterManager.getInstance().startNewActivity(context,path,bundle);
                break;
            case RouterItem.ROUTER_TYPE_FRAGMENT:
                BaseFragmentActivity baseFragmentActivity = RouterManager.getInstance().getLastContainer();
                if(baseFragmentActivity!=null){
                    bundle.putString(BUNDLE_KEY_FRAGMENT,path);
                    baseFragmentActivity.addFragment(bundle,path);
                }
                else{
                    Intent intent = new Intent(context,BaseFragmentActivity.class);
                    bundle.putString(BUNDLE_KEY_PATH,BaseFragmentActivity.class.getName());
                    bundle.putString(BUNDLE_KEY_FRAGMENT,path);
                    intent.putExtras(bundle);
                    context.startActivity(intent,bundle);
                }
                break;
        }
        return true;
    }
}
