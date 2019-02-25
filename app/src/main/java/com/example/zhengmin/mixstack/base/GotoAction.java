package com.example.zhengmin.mixstack.base;

import android.content.Context;
import android.os.Bundle;

public interface GotoAction {
    boolean gotoPage(Context context, String path, Bundle bundle,int itemType);
}
