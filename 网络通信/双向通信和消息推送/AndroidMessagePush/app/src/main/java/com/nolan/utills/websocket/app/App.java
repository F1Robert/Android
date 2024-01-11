package com.nolan.utills.websocket.app;

/**
 * 作者 zf
 * 日期 2024/1/11
 */
import android.app.Application;

import com.nolan.utills.websocket.util.UpdateManager;

/**
 * 作者 zf
 * 日期 2024/1/11
 */
public class App extends Application {
    @Override
    public void onCreate() {
        // 初始化更新服务
        UpdateManager.init(this);
        super.onCreate();
    }
}
