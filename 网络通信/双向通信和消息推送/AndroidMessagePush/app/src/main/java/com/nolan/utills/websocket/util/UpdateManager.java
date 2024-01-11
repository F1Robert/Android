package com.nolan.utills.websocket.util;

/**
 * 作者 zf
 * 日期 2024/1/11
 */

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

public class UpdateManager {
    public static UpdateManager instance;

    public String url;
    public long downloadId;

    public static UpdateManager getInstance() {
        return instance;
    }

    public static void init(Context context) {
        instance = new UpdateManager(context);
    }

    private Context context;

    public UpdateManager(Context context) {
        this.context = context;
    }

    public long downloadAndInstallUpdate() {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle("App更新"); // 设置下载通知的标题
        request.setDescription("正在下载新版本..."); // 设置下载通知的描述
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); // 显示下载通知
        request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, "app_update.apk"); // 设置下载后文件的存储路径和文件名
        downloadId = downloadManager.enqueue(request);
        // 可以监听下载完成事件，并触发安装
        // 当下载完成后，可以通过监听 BroadcastReceiver 来处理下载完成事件，然后触发安装
        // 在 BroadcastReceiver 中处理下载完成事件，参考下面代码
        return downloadId;
    }

    public static long getDownloadId() {
        return instance.downloadId;
    }
}
