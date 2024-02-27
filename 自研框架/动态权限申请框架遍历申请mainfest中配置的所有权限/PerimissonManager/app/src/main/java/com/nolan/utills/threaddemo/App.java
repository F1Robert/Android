package com.nolan.utills.threaddemo;

import android.app.Application;
import android.util.Log;

import com.nolan.utills.permissonlib.PermissionManager;

/**
 * 作者 zf
 * 日期 2024/1/15
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PermissionManager.init(this);
    }
}
