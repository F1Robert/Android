package com.shsany.lampassistant;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.rtsp.RtspMediaSource;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.ui.AspectRatioFrameLayout;
import androidx.media3.ui.PlayerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.Arrays;
import java.util.List;

@UnstableApi
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "RTSP";
    private AutoCompleteTextView autoCompleteTextView;
    private ImageButton settingsButton, playButton, qpButton;
    private PlayerView videoView;
    private ExoPlayer player;
    private String rtspUrl = "rtsp://192.168.1.219/liveRTSP/av4";
    private static boolean isFull = false;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 101) {
                String ip = msg.obj.toString();
                initRTSP(ip);
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        autoCompleteTextView = findViewById(R.id.auto_complete_text_view);
        settingsButton = findViewById(R.id.settings_button);
        playButton = findViewById(R.id.play_button);
        qpButton = findViewById(R.id.qp_button);
        videoView = findViewById(R.id.video_view);

        IPStorageManager ipStorageManager = new IPStorageManager(this);
        List<String> savedIPs = ipStorageManager.getIPAddresses();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, savedIPs);
        autoCompleteTextView.setAdapter(adapter);

        // 点击事件处理
        autoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 手动触发下拉框显示
                autoCompleteTextView.showDropDown();
            }
        });

        autoCompleteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // 如果失去焦点，收起下拉框
                    autoCompleteTextView.dismissDropDown();
                }
            }
        });

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 处理点击列表项的逻辑，这里可以根据position获取选中的项
                // 然后收起下拉框
                autoCompleteTextView.dismissDropDown();
            }
        });

        // 设置AutoCompleteTextView的点击事件处理
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = (String) parent.getItemAtPosition(position);
                autoCompleteTextView.setText(selectedOption);
                Toast.makeText(MainActivity.this, "选中: " + selectedOption, Toast.LENGTH_SHORT).show();
            }
        });

        // 设置按钮的点击事件处理
        settingsButton.setOnClickListener(v -> {
            // 启动设置界面
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
        });

        // 播放按钮的点击事件处理
        playButton.setOnClickListener(v -> {
            //重新加载rtsp流
            Message message = new Message();
            message.what = 101;
            message.obj = autoCompleteTextView.getText().toString();
            mHandler.sendMessage(message);
            ipStorageManager.addIPAddress(autoCompleteTextView.getText().toString());
            refreshAutoCompleteTextView(adapter);
        });

        qpButton.setOnClickListener(v -> {
            if (!isFull) {
                videoView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
                qpButton.setBackgroundResource(R.drawable.tcqp);
            } else {
                videoView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
                qpButton.setBackgroundResource(R.drawable.qp);
            }
            isFull = !isFull;
        });

        //加载播放器
        initRTSP();
    }

    public void refreshAutoCompleteTextView(ArrayAdapter<String> adapter) {
        adapter.notifyDataSetChanged();
    }

    // 使用设置中设置的参数进行RTSP流摄像头初始化
    public void initRTSP(String ip) {
        if (ip == null || ip.equals("")) {
            Toast.makeText(this, "请输入正确的地址！", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "rtsp://" + ip + "/liveRTSP/av4";
        Log.e(TAG, "initRTSP: " + url);
        MediaSource mediaSource = new RtspMediaSource.Factory().createMediaSource(MediaItem.fromUri(url));
        if (player == null) {
            player = new ExoPlayer.Builder(this).build();
        }
        player.setMediaSource(mediaSource);
        videoView.setPlayer(player);
        player.prepare();
    }

    // 初始默认地址的RTSP流摄像头初始化
    public void initRTSP() {
        Log.e(TAG, "initRTSP: args: rtsp://192.168.1.219/liveRTSP/av4");
        MediaSource mediaSource = new RtspMediaSource.Factory().createMediaSource(MediaItem.fromUri(rtspUrl));
        if (player == null) {
            player = new ExoPlayer.Builder(this).build();
        }
        player.setMediaSource(mediaSource);
        videoView.setPlayer(player);
        // 设置布局参数
        //videoView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
        player.prepare();
    }
}