package com.shsany.riskelectronicfence;

import static com.shsany.riskelectronicfence.data.SharedPreferencesData.saveSettings;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.rtsp.RtspMediaSource;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.ui.PlayerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.shsany.riskelectronicfence.data.AiAlarm;
import com.shsany.riskelectronicfence.data.AlarmPacket;
import com.shsany.riskelectronicfence.data.ConfigData;
import com.shsany.riskelectronicfence.data.SharedPreferencesData;
import com.shsany.riskelectronicfence.mqtt.MyMqtt;
import com.shsany.riskelectronicfence.observa.UWBConnectObservable;
import com.shsany.riskelectronicfence.observa.UWBConnectObserver;
import com.shsany.riskelectronicfence.ui.AlarmDialog;
import com.shsany.riskelectronicfence.ui.CaStDialog;
import com.shsany.riskelectronicfence.util.AudioPlayer;
import com.shsany.riskelectronicfence.util.DeviceIdExtractor;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@UnstableApi
public class MainActivity extends AppCompatActivity implements UWBConnectObserver {
    private static final String TAG = "MainActivity_Fence";
    public static DatagramSocket socket1, socket2;
    public static Handler mHandler;
    public Context mcontext;
    public AlarmDialog alarmDialog;
    public AudioPlayer audioPlayer;
    public SharedPreferencesData.Settings settings;
    //缩写说明 jj表示掘进机 jjT表示卡号一栏 jr表示掘进机距离一栏 js表示掘进机状态一栏
    public TextView dateTextView, dayOfWeekTextView, timeTextView, jjT1, jjT2, jjT3, jjT4, jjT5, jr1, jr2, jr3, js1, js2, js3, wkMode;
    //缩写说明 imageView表示雷达扫描图 isAlarm表示报警的图标 isStop表示停机的图标 dr表示dot red:红点 dy表示dot yellow:黄点 st表示setting 设置
    public ImageView imageView, isAlarm, isStop, dr, dy, st;

    // 设置RTSP流地址
    public String rtspUrl = "rtsp://admin:abc12345@192.168.1.99:554/Streaming/Channels/201";
    public String rtspUrl2 = "rtsp://admin:abc12345@192.168.1.252/cam/realmonitor?channel=7&subtype=0";
    //rtsp流摄像头播放器
    public PlayerView playerView, playerView2;
    public CaStDialog caDialog;
    //jtArray掘进机卡号数组 jrArray掘进机距离数组 jsArray掘进机状态数组
    public TextView[] jtArray, jrArray, jsArray;
    public Animation blinkAnimation;

    private PopupWindow popupWindow;
    private List<String> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    //初始化自动执行mqtt连接
    public void initMqtt() {
        UWBConnectObservable.addObserver(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyMqtt mqtt = new MyMqtt("Pad", callback, false, settings.getAI_setveri_ip());
            }
        }).start();
    }

    public void initWkMode() {
        // 初始化选项列表
        options = new ArrayList<>();
        options.add("正常模式");
        options.add("检修模式");
        wkMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow();
            }
        });
    }

    private void showPopupWindow() {
        if (popupWindow == null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, options);
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setBackgroundResource(R.drawable.tb);

            for (int i = 0; i < adapter.getCount(); i++) {
                TextView textView = new TextView(this);
                textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                textView.setText(adapter.getItem(i));
                textView.setGravity(Gravity.CENTER);
                textView.setTextAppearance(R.style.InterfaceTextStyle); // 设置字体样式
                textView.setPadding(0, 12, 12, 12);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handleOptionClick(((TextView) v).getText().toString());
                        popupWindow.dismiss();
                    }
                });
                layout.addView(textView);
            }

            popupWindow = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    // 可以在弹窗消失时执行一些操作
                }
            });
        }

        popupWindow.showAsDropDown(wkMode);
    }

    private void handleOptionClick(String option) {
        if (option.equals("检修模式")) {
            ConfigData configData = new ConfigData(1);
            Log.e(TAG, "initConfigData: " + configData.toJson());
            Message message = new Message();
            message.what = 100;
            message.obj = configData.toJson();
            MyMqtt.mqHandler.sendMessage(message);
            // 在这里处理检修模式的点击事件
            wkMode.setText("检修模式");
            Toast.makeText(this, "检修模式", Toast.LENGTH_SHORT).show();
        } else {
            wkMode.setText("正常模式");
            Toast.makeText(this, "正常模式", Toast.LENGTH_SHORT).show();
        }
    }

    public void initTimerMqtt() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
//                        Thread.sleep(5000); // 休眠5秒钟
                        ConfigData configData = new ConfigData();
                        Log.e(TAG, "initConfigData: " + configData.toJson());
                        Message message = new Message();
                        message.what = 100;
                        message.obj = configData.toJson();
                        MyMqtt.mqHandler.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    //点击设置之后再执行一次mqtt连接
    public void initMqtt(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyMqtt mqtt = new MyMqtt("Pad", args, callback, false);
            }
        }).start();
    }

    //mqtt消息
    public MqttCallback callback = new MqttCallback() {
        @Override
        public void connectionLost(Throwable cause) {
            Log.e(TAG, "connectionLost: 连接到mqtt服务器失败，请检查mqtt服务器和pad设置");
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            if (topic.equals("UWB/ALARM")) {
                // 解析 JSON 数据中的 FUNC 值
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(String.valueOf(message), JsonObject.class);
                int func = jsonObject.get("FUNC").getAsInt();
                if (func == 4) {
                    AiAlarm aiAlarm = new Gson().fromJson(String.valueOf(message), AiAlarm.class);
                    Message message1 = new Message();
                    message1.what = 300;
                    message1.obj = aiAlarm;
                    mHandler.sendMessage(message1);
                } else {
                    AlarmPacket messagePacket = new Gson().fromJson(String.valueOf(message), AlarmPacket.class);
                    Message message1 = new Message();
                    message1.what = 300;
                    message1.obj = messagePacket;
                    mHandler.sendMessage(message1);
                }
                Log.e(TAG, "messageArrived: 收到mqtt服务器消息" + message.toString());
            }
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            Log.e(TAG, "deliveryComplete: " + token.toString());
        }
    };

    public void initCa() {
        st.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (caDialog == null) {
                    caDialog = new CaStDialog(mcontext);
                    caDialog.show();
                } else if (caDialog.isShowing()) {
                    caDialog.dismiss();
                    caDialog.show();
                } else {
                    caDialog.show();
                }
            }
        });
    }

    /*
     * 初始化UI组件
     * */
    public void initUI() {
        imageView = findViewById(R.id.gifImageView);
        isAlarm = findViewById(R.id.alarming);
        isStop = findViewById(R.id.stop);
        timeTextView = findViewById(R.id.time_h);
        dateTextView = findViewById(R.id.time_y);
        dayOfWeekTextView = findViewById(R.id.time_d);
        jjT1 = findViewById(R.id.j_ca_id0);
        jjT2 = findViewById(R.id.j_ca_id1);
        jjT3 = findViewById(R.id.j_ca_id2);
        jjT4 = findViewById(R.id.j_ca_id3);
        jjT5 = findViewById(R.id.j_ca_id4);
        jr1 = findViewById(R.id.j_rang_0);
        jr2 = findViewById(R.id.j_rang_1);
        jr3 = findViewById(R.id.j_rang_2);
        js1 = findViewById(R.id.j_status_0);
        js2 = findViewById(R.id.j_status_1);
        js3 = findViewById(R.id.j_status_2);
        wkMode = findViewById(R.id.wkMode);
        st = findViewById(R.id.st);
        dr = findViewById(R.id.dr);
        dy = findViewById(R.id.dy);
        //加载雷达扫描动画
        Glide.with(this).asGif().load(R.raw.anime).into(imageView);
        playerView = findViewById(R.id.player1);
        playerView2 = findViewById(R.id.player2);
    }

    private void updateDate() {
        // 获取当前日期和时间
        Calendar calendar = Calendar.getInstance();

        // 获取星期
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String[] weekdays = getResources().getStringArray(R.array.weekdays); // 假设您有包含星期名称的字符串数组资源
        String currentDayOfWeek = weekdays[dayOfWeek - 1];

        // 获取日期
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        String currentDate = dateFormat.format(calendar.getTime());

        // 更新 TextView 的文本
        dateTextView.setText(currentDate);
        dayOfWeekTextView.setText(currentDayOfWeek);
    }

    private void updateTime() {
        // 获取当前时间
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String currentTime = dateFormat.format(new Date());

        // 更新 TextView 的文本
        timeTextView.setText(currentTime);
    }

    public void init() {
        mcontext = this;
        initSettings();
        initUI();
        initCa();
        initHandler();
        updateDate();
        updateTimeSec();
        initAlarmDialog();
        initAudioPlayer();
        initArrays();
        initWkMode();
        initRTSP();
        initMqtt();
    }

    public void initArrays() {
        blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blink_animation);
        jtArray = new TextView[]{jjT1, jjT2, jjT3, jjT4, jjT5};
        jrArray = new TextView[]{jr1, jr2, jr3};
        jsArray = new TextView[]{js1, js2, js3};
    }

    public void initSettings() {
        settings = SharedPreferencesData.loadSettings(mcontext);
    }

    public void initAudioPlayer() {
        audioPlayer = new AudioPlayer();
    }

    //初始化handler的处理逻辑
    public void initHandler() {
        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == 200) {
                    initSt((String[]) msg.obj);
                } else if (msg.what == 300) {
                    if (msg.obj instanceof AiAlarm) {
                        AiAlarm aiAlarm = (AiAlarm) msg.obj;
                        analysisAiAlarm(aiAlarm);
                    } else {
                        AlarmPacket alarmPacket = (AlarmPacket) msg.obj;
                        analysisUwbAlarm(alarmPacket);
                    }
                }
                if (caDialog != null)
                    caDialog.dismiss();
            }
        };
    }

    public void analysisAiAlarm(AiAlarm aiAlarm) {
        ifShowUwbAlarmDialog(aiAlarm);
    }

    public void updateTimeSec() {
        // 每秒更新一次时间
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                updateTime();
                mHandler.postDelayed(this, 1000);
            }
        });
    }

    //解析Uwb的报警信息
    public void analysisUwbAlarm(AlarmPacket alarmPacket) {
        //判断是否弹出报警提示框
        ifShowUwbAlarmDialog(alarmPacket);
        //更新距离
        analysisJr(String.valueOf(alarmPacket.getTID()), alarmPacket.getRANGE());
        //如果当前UWB报警距离小于设定的范围则报警
        if (analysisDr(alarmPacket.getRANGE())) {

        } else {
            analysisDy(alarmPacket.getRANGE());
        }
    }

    public void ifShowUwbAlarmDialog(AlarmPacket alarmPacket) {
        //是否报警
        isAlarm(alarmPacket);
        //是否停机
        isStop(alarmPacket);
    }

    public void ifShowUwbAlarmDialog(AiAlarm alarmPacket) {
        //是否报警
        isAlarm(alarmPacket);
        //是否停机
        isStop(alarmPacket);
    }

    public void isStop(AlarmPacket alarmPacket) {
        if (alarmPacket.getALARMR() == 1) {
            isStop.setImageResource(R.drawable.rd);
        } else {
            isStop.setImageResource(R.drawable.gr);
        }
    }

    public void isStop(AiAlarm alarmPacket) {
        if (alarmPacket.getAMARMR() == 1) {
            isStop.setImageResource(R.drawable.rd);
        } else {
            isStop.setImageResource(R.drawable.gr);
        }
    }

    public void isAlarm(AlarmPacket alarmPacket) {
        if (alarmPacket.getALARMY() == 1 || alarmPacket.getALARMR() == 1) {
            isAlarm.setImageResource(R.drawable.rd);
            showAlarmDialog(alarmPacket);
        } else {
            isAlarm.setImageResource(R.drawable.gr);
        }
    }

    public void isAlarm(AiAlarm alarmPacket) {
        if (alarmPacket.getAMARMR() == 1 || alarmPacket.getAMARMY() == 1) {
            isAlarm.setImageResource(R.drawable.rd);
            showAlarmDialog(alarmPacket);
        } else {
            isAlarm.setImageResource(R.drawable.gr);
        }
    }


    public void showAlarmDialog(AlarmPacket alarmPacket) {
        if (alarmDialog != null) {
            alarmDialog.dismiss();
            alarmDialog = new AlarmDialog(mcontext, alarmPacket.getTID(), alarmPacket.getRANGE());
            alarmDialog.show();
        } else {
            alarmDialog = new AlarmDialog(mcontext, alarmPacket.getTID(), alarmPacket.getRANGE());
            alarmDialog.show();
        }
    }

    public void showAlarmDialog(AiAlarm alarmPacket) {
        if (alarmDialog != null) {
            alarmDialog.dismiss();
            alarmDialog = new AlarmDialog(mcontext, alarmPacket.getPerson_name(), alarmPacket.getRANGE());
            alarmDialog.show();
        } else {
            alarmDialog = new AlarmDialog(mcontext, alarmPacket.getPerson_name(), alarmPacket.getRANGE());
            alarmDialog.show();
        }
    }

    public void analysisJr(String id, int range) {
        float r = Float.parseFloat(settings.getRed_r());
        float y = Float.parseFloat(settings.getYellow_r());
        for (int i = 0; i < jtArray.length; i++) {
            if (jtArray[i].getText().equals(id)) {
                if (i == 0) {
                    if (range / 1000 < r) {
                        jrArray[0].setTextColor(Color.RED);
                        jsArray[0].setText("报警");
                        jsArray[0].setTextColor(Color.RED);
                    } else if (range / 1000 < y) {
                        jrArray[0].setTextColor(Color.YELLOW);
                        jsArray[0].setText("报警");
                        jsArray[0].setTextColor(Color.YELLOW);
                    }
                    jrArray[0].setText(range / 1000 + "米");
                }
                if (i == 1) {
                    if (range / 1000 < r) {
                        jrArray[1].setTextColor(Color.RED);
                        jsArray[1].setText("报警");
                        jsArray[1].setTextColor(Color.RED);
                    } else if (range / 1000 < y) {
                        jrArray[1].setTextColor(Color.YELLOW);
                        jsArray[1].setText("报警");
                        jsArray[1].setTextColor(Color.YELLOW);
                    }
                    jrArray[1].setText(range / 1000 + "米");
                }
                if (i == 2) {
                    jrArray[2].setText(range / 1000 + "米");
                    if (range / 1000 < r) {
                        jsArray[2].setText("报警");
                        jsArray[2].setTextColor(Color.RED);
                        jrArray[2].setTextColor(Color.RED);
                    } else if (range / 1000 < y) {
                        jrArray[2].setTextColor(Color.YELLOW);
                        jsArray[2].setText("报警");
                        jsArray[2].setTextColor(Color.YELLOW);
                    }
                }
                return;
            }
        }
    }

    public boolean analysisDr(int range) {
        if (range == 0) {
            return true;
        }
        if (range / 1000 < Float.parseFloat(settings.getRed_r())) {
            dr.setVisibility(View.VISIBLE);
            dr.startAnimation(blinkAnimation);
//
            return true;
        } else {
            dr.setVisibility(View.INVISIBLE);
            return false;
        }
    }

    public boolean analysisDy(int range) {
        if (range == 0) {
            return true;
        }
        if (range / 1000 < Float.parseFloat(settings.getYellow_r())) {
            dy.setVisibility(View.VISIBLE);
            dy.startAnimation(blinkAnimation);
//            audioPlayer.play(mcontext, R.raw.bjq);
            return true;
        } else {
            dy.setVisibility(View.INVISIBLE);
            return false;
        }
    }

    public void analysisJs(String id, String range) {
        int r = Integer.parseInt(range) / 100;
        for (int i = 0; i < jtArray.length; i++) {
            if (jtArray[i].getText().equals(id)) {
                if (i == 0) {
                    jsArray[0].setText(r + "米");
                }
                if (i == 1) {
                    jsArray[1].setText(r + "米");
                }
                if (i == 2) {
                    jsArray[2].setText(r + "米");
                }
            }
        }
    }

    public void initAlarmDialog() {
        alarmDialog = new AlarmDialog(mcontext, -1, -1);
    }

    public void initSt(String[] args) {
        initConfigData(args);
        initRTSP(args);
        initMqtt(args);
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferencesData.Settings settings = new SharedPreferencesData.Settings(args[9],
                        "",
                        args[0],
                        args[1],
                        args[7],
                        args[8], DeviceIdExtractor.extractDeviceIds(args[6]), args[2], args[3], args[4]);
                saveSettings(mcontext, settings);
            }
        }).start();
    }

    public void initConfigData(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ConfigData configData = new ConfigData(args[9], "", args[1], args[7], args[8], initBmd(args[6]));
                Log.e(TAG, "initConfigData: " + configData.toJson());
                Message message = new Message();
                message.what = 100;
                message.obj = configData.toJson();
                MyMqtt.mqHandler.sendMessage(message);
            }
        }).start();
    }

    public void initHmd(String[] args) {
        if (args[5].equals("")) {

        } else {

        }
    }

    /*
     * 白名单处理
     * */
    public String[] initBmd(String arg) {
        if (arg.equals("")) {
            return new String[]{""};
        } else {
            /*
             * 解析白名单
             * */
            return DeviceIdExtractor.extractDeviceIds(arg);
        }
    }


    public void initHq(String[] args) {
        if (args[7].equals("")) {

        } else {

        }
    }

    public void initRq(String[] args) {
        if (args[8].equals("")) {

        } else {

        }
    }

    public void initRTSP(String[] args) {
        Log.e(TAG, "initRTSP: args: " + Arrays.toString(args));
        MediaSource mediaSource = new RtspMediaSource.Factory().createMediaSource(MediaItem.fromUri(args[0]));
        ExoPlayer player = new ExoPlayer.Builder(this).build();
        player.setMediaSource(mediaSource);
        playerView.setPlayer(player);
        player.prepare();

        MediaSource mediaSource2 = new RtspMediaSource.Factory().createMediaSource(MediaItem.fromUri(args[1]));
        ExoPlayer player2 = new ExoPlayer.Builder(this).build();
        player2.setMediaSource(mediaSource2);

        playerView2.setPlayer(player2);
        player2.prepare();
    }

    public void initRTSP() {
        rtspUrl = settings.getAi_camare_ip();
        rtspUrl2 = settings.getCamare_ip();
        MediaSource mediaSource = new RtspMediaSource.Factory().createMediaSource(MediaItem.fromUri(rtspUrl));
        ExoPlayer player = new ExoPlayer.Builder(this).build();
        player.setMediaSource(mediaSource);
        playerView.setPlayer(player);
        player.prepare();

        MediaSource mediaSource2 = new RtspMediaSource.Factory().createMediaSource(MediaItem.fromUri(rtspUrl));
        ExoPlayer player2 = new ExoPlayer.Builder(this).build();
        player2.setMediaSource(mediaSource2);

        playerView2.setPlayer(player2);
        player2.prepare();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (caDialog != null) {
            caDialog.dismiss();
            caDialog = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (caDialog != null) {
            caDialog.dismiss();
            caDialog = null;
        }
    }

    public void initSocket1(int port) {
        setSocket1(port);
        receiveDataThread();
    }

    public static void setSocket1(int port) {
        try {
            socket1 = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public static void setSocket2(int port) {
        try {
            socket2 = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private void receiveDataThread() {
        new Thread(new Runnable() {
            @OptIn(markerClass = UnstableApi.class)
            @Override
            public void run() {
                try {
                    while (true) {
                        byte[] buffer = new byte[45];
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                        MainActivity.socket1.receive(packet);
                        byte[] hexData = packet.getData();
                        int[] decryptData = handleReceive(hexData);
                        if (decryptData != null) {
                            if (decryptData[2] < 8) {
                                Message nMsg = new Message();
                                nMsg.what = 100;
                                nMsg.obj = decryptData;
                                MainActivity.mHandler.sendMessage(nMsg);
                            } else if (decryptData[2] < 16) {
                                Message nMsg = new Message();
                                nMsg.what = 102;
                                nMsg.obj = decryptData;
                                MainActivity.mHandler.sendMessage(nMsg);
                            } else {
                                Message nMsg = new Message();
                                nMsg.what = 104;
                                nMsg.obj = decryptData;
                                MainActivity.mHandler.sendMessage(nMsg);
                            }
                        } else {
                            Message msg = new Message();
                            msg.obj = "Error 数据解析错误，错误的数据为:" + hexData.toString();
                            Toast.makeText(getApplicationContext(), msg.what, Toast.LENGTH_SHORT).show();
                            Log.e("UDP receive", "Received data:" + msg.obj.toString());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public int[] handleReceive(byte[] hexData) {
        byte typeByte = hexData[2];
        String hexString = String.format("0x%02X", typeByte & 0xFF);

        if (hexString.equals("0xA1")) {
            int startIndex = 11;
            int messageLength = hexData.length - startIndex - 4;
            if (messageLength % 30 == 0) {
                for (int i = 0; i < messageLength / 30; i++) {
                    int messageStartIndex = startIndex + i * 30;
                    int messageEndIndex = messageStartIndex + 30;
                    byte[] message = Arrays.copyOfRange(hexData, messageStartIndex, messageEndIndex);
                    //int keyA = 0xA;
                    int keyA = -96;
                    byte fifthByte = message[4];
                    byte sixthByte = message[5];
                    byte seventhByte = message[6];
                    byte decryptedFifthByte = (byte) (fifthByte ^ keyA);
                    byte decryptedSixthByte = (byte) (sixthByte ^ keyA);
                    byte decryptedSeventhByte = (byte) (seventhByte ^ keyA);
                    Log.e("Decrypted Fifth Byte", "解密后的第五字节数据: " + decryptedFifthByte);
                    Log.e("Decrypted Sixth Byte", "解密后的第六字节数据: " + decryptedSixthByte);
                    Log.e("Decrypted Seventh Byte", "解密后的第七字节数据: " + decryptedSeventhByte);
                    int[] decrypted = new int[3];
                    decrypted[0] = decryptedFifthByte & 0xFF;
                    decrypted[1] = decryptedSixthByte & 0xFF;
                    decrypted[2] = decryptedSeventhByte & 0xFF;
                    return decrypted;
                }
            } else {
                return null;
            }
        }
        return null;
    }

    @Override
    public void onUWBConnectChanged(boolean isUWBConnect) {
        if (isUWBConnect) {
            Toast.makeText(mcontext, "UWB数据服务已连接", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mcontext, "UWB数据服务断开", Toast.LENGTH_SHORT).show();
        }
    }
}