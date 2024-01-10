package com.nolan.utills.nolanwindowmanagerlib;

/**
 * 作者 zf
 * 日期 2024/1/10
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class WindowManagerUtils {
    /*
     * 接收一个布局文件，将目标view作为root传入
     * 后续可以根据root进行布局管理操作
     * */
    // 静态方法，用于设置内容视图并关联传入的View
    public static View setContentViewWithWindowManager(Context context, int layoutResourceId, View root, int type) {
        /*
         * 先请求悬浮创建视图的权限
         * */
        if (!Settings.canDrawOverlays(context)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.getPackageName()));
            context.startActivity(intent);
        }
        // 使用LayoutInflater从布局资源文件中创建视图
        root = LayoutInflater.from(context).inflate(layoutResourceId, null);
        // 获取WindowManager
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params = null;
        switch (type) {
            case 1:
                // 定义WindowManager.LayoutParams来设置视图的属性
                params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, // 这是一个适用于悬浮窗口的类型
                        //WindowManager.LayoutParams.TYPE_APPLICATION_STARTING,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, // 不需要获取焦点
                        android.graphics.PixelFormat.TRANSLUCENT // 使用透明背景
                );
                break;
            case 2:
                params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_APPLICATION, // 这是一个适用于普通界面
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, // 不需要获取焦点
                        android.graphics.PixelFormat.TRANSLUCENT // 使用透明背景
                );
                break;
            case 3:
                params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_SYSTEM_DIALOG, // 这是一个适用普通对话框
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, // 不需要获取焦点
                        android.graphics.PixelFormat.TRANSLUCENT // 使用透明背景
                );
        }
        // 将视图添加到WindowManager中
        windowManager.addView(root, params);
        return root;
    }

    public static View setContentViewWithWindowManagerWithLocation(Context context, int layoutResourceId, View root, int type, int x, int y) {
        /*
         * 先请求悬浮创建视图的权限
         * */
        if (!Settings.canDrawOverlays(context)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.getPackageName()));
            context.startActivity(intent);
        }
        // 使用LayoutInflater从布局资源文件中创建视图
        root = LayoutInflater.from(context).inflate(layoutResourceId, null);
        // 获取WindowManager
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params = null;
        switch (type) {
            case 1:
                // 定义WindowManager.LayoutParams来设置视图的属性
                params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, // 这是一个适用于悬浮窗口的类型
                        //WindowManager.LayoutParams.TYPE_APPLICATION_STARTING,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, // 不需要获取焦点
                        android.graphics.PixelFormat.TRANSLUCENT // 使用透明背景
                );
                break;
            case 2:
                params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_APPLICATION, // 这是一个适用于普通界面
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, // 不需要获取焦点
                        android.graphics.PixelFormat.TRANSLUCENT // 使用透明背景
                );
                break;
            case 3:
                params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_SYSTEM_DIALOG, // 这是一个适用普通对话框
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, // 不需要获取焦点
                        android.graphics.PixelFormat.TRANSLUCENT // 使用透明背景
                );
        }
        //设置布局在屏幕上的位置
        params.x = x;
        params.y = y;
        // 将视图添加到WindowManager中
        windowManager.addView(root, params);
        return root;
    }
}
