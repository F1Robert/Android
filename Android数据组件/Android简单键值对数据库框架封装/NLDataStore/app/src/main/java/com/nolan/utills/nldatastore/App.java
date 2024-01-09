package com.nolan.utills.nldatastore;

import android.app.Application;
import android.util.Log;

import com.nolan.utills.nldata.NLData;

/**
 * 作者 zf
 * 日期 2024/1/2
 */
public class App extends Application {
    @Override
    public void onCreate() {
        Log.e(NLData.TAG, "onCreate: 初始化");
        NLData.init(this);
        super.onCreate();
    }
}
