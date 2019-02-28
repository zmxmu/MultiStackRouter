package com.example.zhengmin.navigation.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zhengmin.navigation.R;
import com.syswin.msgseal.navigation.BaseFragment;
import com.syswin.msgseal.navigation.action.NormalGotoAction;
import com.syswin.msgseal.navigation.RouterManager;
import com.syswin.msgseal.routeprocessor.PageNavigationRoute;

import static com.syswin.msgseal.navigation.NavigationHelper.ANIMATOR_SLIDE_UP_DOWN;


/**
 * A simple {@link Fragment} subclass.
 */
@PageNavigationRoute(url = "BlankFragmentB")
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
                        RouterManager.GOTO_ACTION_NORMAL,bundle,ANIMATOR_SLIDE_UP_DOWN);
            }
        });
        return view;
    }

}
