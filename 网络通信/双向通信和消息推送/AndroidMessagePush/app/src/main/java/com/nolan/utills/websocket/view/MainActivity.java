package com.nolan.utills.websocket.view;

import static com.nolan.utills.websocket.util.DownloadCompleteReceiver.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.firestore.v1.Cursor;
import com.nolan.utills.websocket.databinding.ActivityMainBinding;
import com.nolan.utills.websocket.databinding.UpdateTipBinding;
import com.nolan.utills.websocket.net.MyWebSocketClient;
import com.nolan.utills.websocket.R;
import com.nolan.utills.websocket.util.MessageEvent;
import com.nolan.utills.websocket.util.UpdateManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends AppCompatActivity {
    private MyWebSocketClient webSocketClient;
    private ActivityMainBinding binding;
    private UpdateTipBinding updateTipBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //视图绑定
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        updateTipBinding = UpdateTipBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    @Override
    protected void onResume() {
        // 在Activity恢复时重新连接WebSocket
        initWebSocket();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 在Activity销毁时关闭WebSocket连接
        webSocketClient.closeWebSocket();
    }

    public void requestPermission() {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        int requestCode = 100; // 请求码可以是任何非负整数，用于识别请求
        requestPermissions(permissions, requestCode);
    }

    public void init() {
        requestPermission();
        initComponents(binding);
    }

    public void initWebSocket() {
        //初始化WebSocket客户端
        //网络连接
        //不能在主线程进行
        //因此新开一个线程进行
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                webSocketClient = new MyWebSocketClient();
                webSocketClient.connectWebSocket();
            }
        }, 5000);

    }

    public void initComponents(ActivityMainBinding binding) {
        binding.exact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webSocketClient.sendMessage("检测更新");
            }
        });
    }

    public void showDownload(Context context) {
        //下载
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("正在下载");
        progressDialog.setMessage("请稍候...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setIndeterminate(false); // 设置为false以显示进度条
        progressDialog.setCancelable(false); // 设置为false以防止用户取消下载
        progressDialog.show();
    }

    public void showUpdateDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.update_tip);
        dialog.findViewById(R.id.updateBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在这里处理用户点击更新按钮的逻辑
                // 例如，启动下载更新的操作
                UpdateManager.getInstance().downloadAndInstallUpdate();
                showDownload(MainActivity.this);
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.cancelBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在这里处理用户点击取消按钮的逻辑
                // 例如，关闭对话框或者取消更新操作
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Subscribe
    public void onMessageEvent(MessageEvent event) {
        Log.e(TAG, "onMessageEvent: " + event.getMessage());
        String message = event.getMessage();
        // 在这里处理接收到的消息
        if (message.equals("确认更新")) {
            showDownload(this);
        }
        if (message.contains("检测到更新")) {
            Log.e(TAG, "onMessageEvent: 更新窗口");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showUpdateDialog();
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this); // 注册事件
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this); // 注销事件
    }
}
