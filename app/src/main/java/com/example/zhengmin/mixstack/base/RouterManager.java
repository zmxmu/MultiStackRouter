package com.example.zhengmin.mixstack.base;

import android.app.Activity;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.zhengmin.mixstack.activity.BaseFragmentActivity;
import com.example.zhengmin.mixstack.fragment.BaseFragment;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Stack;

/**
 * Created by zhengmin on 2019/2/21.
 */

public class RouterManager {
    public static final int ROUTER_TYPE_NORMAL = 0;
    public static final int ROUTER_TYPE_SINGLE = 1;
    public static final int ROUTER_TYPE_CLEAR = 2;

    public static final String BUNDLE_KEY_FRAGMENT = "fragmentCls";
    public static final String BUNDLE_KEY_PARAMS = "params";
    private RouterManager(){};
    private static volatile RouterManager instance;
    public  static  RouterManager  getInstance(){
        if(instance == null ){
            synchronized (RouterManager.class){
                if(instance == null){
                    instance =  new RouterManager();
                }
            }
        }
        return instance;
    }

    public Stack<RouterItem> getStack() {
        return mStack;
    }

    private Stack<RouterItem> mStack = new Stack<>();
    private int getItemType(Class clz){
        int result  = RouterItem.ROUTER_TYPE_NONE;

        while(!TextUtils.equals(clz.getSimpleName(),"Object")){
            if(TextUtils.equals(clz.getSimpleName(),"Fragment")){
                return RouterItem.ROUTER_TYPE_FRAGMENT;
            }
            else if(TextUtils.equals(clz.getSimpleName(),"BaseFragmentActivity")){
                return RouterItem.ROUTER_TYPE_CONTAINER;
            }
            else if(TextUtils.equals(clz.getSimpleName(),"Activity")){
                return RouterItem.ROUTER_TYPE_ACTIVITY;
            }
            else{
                clz = clz.getSuperclass();
            }
        }
        return result;
    }
    public boolean jumpTo(Context context,String path, int routerType, Bundle bundle){
        boolean result = false;
        Class clz;
        try {
            clz = Class.forName(path);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return result;
        }
        int itemType = getItemType(clz);

        if(itemType == RouterItem.ROUTER_TYPE_NONE){
            return false;
        }
        switch (routerType){
            case ROUTER_TYPE_NORMAL:
                switch (itemType){
                    case RouterItem.ROUTER_TYPE_ACTIVITY:
                        startNewActivity(context,path,bundle);
                        break;
                    case RouterItem.ROUTER_TYPE_FRAGMENT:
                        BaseFragmentActivity baseFragmentActivity = getLastContainer();
                        if(baseFragmentActivity!=null){
                            baseFragmentActivity.addFragment(bundle,path);
                        }
                        else{
                            Intent intent = new Intent(context,BaseFragmentActivity.class);
                            if(bundle == null){
                                bundle = new Bundle();
                            }
                            bundle.putString(BUNDLE_KEY_FRAGMENT,path);
                            intent.putExtras(bundle);
                            context.startActivity(intent,bundle);
                        }
                        break;
                }
                break;
        }
        return result;
    }
    private void startNewActivity(Context context,String path,Bundle bundle){
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName(context.getClass().getName(),path);
        intent.setComponent(componentName);
        context.startActivity(intent,bundle);
    }
    private int getPathIndex(String path){
        for(int i = mStack.size()-1;i>=0;i--){
            if(TextUtils.equals(path,mStack.get(i).getRouterKey())){
                return i;
            }
        }
        return -1;
    }
    public BaseFragmentActivity getLastContainer(){
        for(int i = mStack.size()-1;i>=0;i--){
            if(mStack.get(i).getType()== RouterItem.ROUTER_TYPE_CONTAINER){
                return (BaseFragmentActivity)((ActivityItem)mStack.get(i)).getActivityWR().get();
            }
        }
        return null;
    }
    public BaseFragment getTopFragment(){
        RouterItem currentItem;
        if(!mStack.isEmpty()){
            currentItem = (RouterItem) mStack.peek();
            if(currentItem.getType() == RouterItem.ROUTER_TYPE_FRAGMENT){
                return ((FragmentItem)currentItem).getFragmentWR().get();
            }
            else{
                return null;
            }
        }
        else{
            return null;
        }
    }
    public BaseFragment getSubTopFragment(){
        RouterItem currentItem;
        if(mStack.size()>2){
            currentItem = mStack.get(mStack.size()-1);
            if(currentItem!=null && currentItem.getType() == RouterItem.ROUTER_TYPE_FRAGMENT){
                currentItem = mStack.get(mStack.size()-2);
                if(currentItem!=null && currentItem.getType() == RouterItem.ROUTER_TYPE_FRAGMENT){
                    return ((FragmentItem)currentItem).getFragmentWR().get();
                }
                else{
                    return null;
                }
            }
            else{
                return null;
            }
        }
        else{
            return null;
        }
    }
}
