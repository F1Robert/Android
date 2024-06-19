package com.shsany.lampassistant;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者 zf
 * 日期 2024/6/19
 */
public class SettingActivity extends AppCompatActivity {
    private static final String TAG = "SettingActivity";
    private RecyclerView recyclerView;
    private SettingsAdapter adapter;
    private List<SettingItem> settingItems;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        progressBar = findViewById(R.id.progressBar);
        loadData();
        //初始化recycleView
        initRecycleView();
        //初始化值
        initValues();
    }

    public void initRecycleView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        settingItems = new ArrayList<>();
        adapter = new SettingsAdapter(settingItems);
        recyclerView.setAdapter(adapter);
    }

    public void refreshAdapter() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        progressBar.setVisibility(View.GONE);
    }

    public void initValues() {
        new NetworkGetRequest("192.168.1.219").execute();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        // 接收事件并更新 UI
        String message = event.getMessage();
        String value = event.getValue();
        // 判断 message 是否包含特定的字符串，然后选择添加对应的 SettingItem
        if (message.contains("wifi_ssid")) {
            Log.e(TAG, "onMessageEvent: " + message + ":" + value);
            settingItems.add(new SettingItem("wifi_ssid", value, "shsanylamp"));
        }
        if (message.contains("wifi_pwd")) {
            Log.e(TAG, "onMessageEvent: " + message + ":" + value);
            settingItems.add(new SettingItem("wifi_pwd", value, "12345678"));
        }
        if (message.contains("wifi_ip")) {
            Log.e(TAG, "onMessageEvent: " + message + ":" + value);
            settingItems.add(new SettingItem("wifi_ip", value, "192.168.10.101"));
        }
        if (message.contains("net_mask")) {
            Log.e(TAG, "onMessageEvent: " + message + ":" + value);
            settingItems.add(new SettingItem("net_mask", value, "255.255.255.0"));
        }
        if (message.contains("gate_way")) {
            Log.e(TAG, "onMessageEvent: " + message + ":" + value);
            settingItems.add(new SettingItem("gate_way", value, "192.168.10.1"));
        }
        if (message.contains("device_id")) {
            Log.e(TAG, "onMessageEvent: " + message + ":" + value);
            settingItems.add(new SettingItem("device_id", value, "SY0001"));
        }
        if (message.contains("http_host")) {
            Log.e(TAG, "onMessageEvent: " + message + ":" + value);
            settingItems.add(new SettingItem("http_host", value, "192.168.10.194:8080"));
        }
        if (message.contains("ftp_host")) {
            Log.e(TAG, "onMessageEvent: " + message + ":" + value);
            settingItems.add(new SettingItem("ftp_host", value, "192.168.10.194"));
        }
        if (message.contains("ftp_root")) {
            Log.e(TAG, "onMessageEvent: " + message + ":" + value);
            settingItems.add(new SettingItem("ftp_root", value, "home_ftp_video/kyai"));
        }
        if (message.contains("ftp_user")) {
            Log.e(TAG, "onMessageEvent: " + message + ":" + value);
            settingItems.add(new SettingItem("ftp_user", value, "ftp"));
        }
        if (message.contains("ftp_pwd")) {
            Log.e(TAG, "onMessageEvent: " + message + ":" + value);
            settingItems.add(new SettingItem("ftp_pwd", value, "123456"));
        }
        if (message.contains("cap_sec")) {
            Log.e(TAG, "onMessageEvent: " + message + ":" + value);
            settingItems.add(new SettingItem("cap_sec", value, "600"));
        }
        if (message.contains("成功")) {
            Toast.makeText(this, "设置成功", Toast.LENGTH_SHORT).show();
        }
        refreshAdapter();
    }

    private void loadData() {
        // 显示加载动画
        progressBar.setVisibility(View.VISIBLE);
    }
}

