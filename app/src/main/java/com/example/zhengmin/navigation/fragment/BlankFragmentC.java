package com.example.zhengmin.navigation.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zhengmin.navigation.R;
import com.syswin.msgseal.navigation.BaseFragment;
import com.syswin.msgseal.navigation.PageNavigation;
import com.syswin.msgseal.navigation.annotation.Page;

import static com.syswin.msgseal.navigation.NavigationFlags.GOTO_SINGLE;

/**
 * A simple {@link Fragment} subclass.
 */
@Page(url = "BlankFragmentC")
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
                Bundle bundle = new Bundle();
                bundle.putString("key", "abc");

//                RouterManager.getInstance().goTo(getActivity(),"com.example.zhengmin.navigation.activity.MainActivity",
//                        RouterManager.GOTO_ACTION_NORMAL,bundle);
                PageNavigation.getInstance().goTo(getActivity(), "MainActivity2",
                        GOTO_SINGLE, bundle);
//                RouterManager.getInstance().goTo(getActivity(),"BlankFragmentA",
//                        new SingleGotoAction(),bundle);
//                RouterManager.getInstance().goBack(getActivity(),"BlankFragmentA",bundle);
//                RouterManager.getInstance().goBack(getActivity(),
//                        "com.example.zhengmin.navigation.activity.MainActivity",bundle);
            }
        });
        return view;
    }

}
