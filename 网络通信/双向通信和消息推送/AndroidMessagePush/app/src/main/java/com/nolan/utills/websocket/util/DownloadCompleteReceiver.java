package com.nolan.utills.websocket.util;

/**
 * 作者 zf
 * 日期 2024/1/11
 */

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.File;

public class DownloadCompleteReceiver extends BroadcastReceiver {
    public static final String TAG = "zf";
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
            String url = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/" + "app_update.apk";
            startInstall(context, url);
        }
    }
    private void startInstall(Context context, String url) {
        Uri contentUri = FileProvider.getUriForFile(context, "com.nolan.utills.websocket", new File(url));
        Intent installIntent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
        installIntent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        //从非activity中启动添加以下标签
        installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // 授予 URI 读取权限
        installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(installIntent);
        Log.e(TAG, "startInstall:开始安装 文件路径为:" + contentUri);
        Log.e(TAG, "startInstall:开始安装 文件原始路径为:" + url);
    }
}
