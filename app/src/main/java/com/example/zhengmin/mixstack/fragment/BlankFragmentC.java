package com.example.zhengmin.mixstack.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zhengmin.mixstack.R;
import com.syswin.msgseal.mixstack.BaseFragment;
import com.syswin.msgseal.mixstack.RouterManager;
import com.syswin.msgseal.mixstack.action.SingleGotoAction;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragmentC extends BaseFragment {


    public BlankFragmentC() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank_fragment_c, container, false);
        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle =  new Bundle();
                bundle.putString("key","abc");

//                Intent intent = new Intent();
//                ComponentName componentName = new ComponentName(getActivity().getPackageName(),MainActivity.class.getName());
//                intent.setComponent(componentName);
                //intent.putExtras(bundle);
//                getActivity().startActivity(intent);
                RouterManager.getInstance().goTo(getActivity(),"com.example.zhengmin.mixstack.fragment.BlankFragmentA",
                        new SingleGotoAction(),bundle);
//                RouterManager.getInstance().goTo(getActivity(),"com.example.zhengmin.mixstack.activity.MainActivity",
//                        new SingleGotoAction(),bundle);
            }
        });
        return view;
    }

}
