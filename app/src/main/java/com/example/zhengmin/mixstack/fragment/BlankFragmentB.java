package com.example.zhengmin.mixstack.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zhengmin.mixstack.R;
import com.syswin.msgseal.mixstack.BaseFragment;
import com.syswin.msgseal.mixstack.action.NormalGotoAction;
import com.syswin.msgseal.mixstack.RouterManager;
import com.syswin.msgseal.routeprocessor.MixStackRoute;


/**
 * A simple {@link Fragment} subclass.
 */
@MixStackRoute(url = "BlankFragmentB")
public class BlankFragmentB extends BaseFragment {


    public BlankFragmentB() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank_fragment2, container, false);
        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle =  new Bundle();
                bundle.putString("key","abc");
                RouterManager.getInstance().goTo(getActivity(),"BlankFragmentC",
                        new NormalGotoAction(),bundle);
            }
        });
        return view;
    }

}
