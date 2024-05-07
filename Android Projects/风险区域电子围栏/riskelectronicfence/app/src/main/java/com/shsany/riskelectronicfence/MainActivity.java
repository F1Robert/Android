package com.shsany.riskelectronicfence;

import static com.shsany.riskelectronicfence.data.SharedPreferencesData.saveSettings;
import static com.shsany.riskelectronicfence.util.DeviceIdExtractor.isUWBConnect;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.shsany.riskelectronicfence.data.AlarmPacket;
import com.shsany.riskelectronicfence.data.ConfigData;
import com.shsany.riskelectronicfence.data.SharedPreferencesData;
import com.shsany.riskelectronicfence.mqtt.MyMqtt;
import com.shsany.riskelectronicfence.ui.AlarmDialog;
import com.shsany.riskelectronicfence.ui.CaStDialog;
import com.shsany.riskelectronicfence.util.DeviceIdExtractor;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

@UnstableApi
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity_Fence";
    public static DatagramSocket socket1;
    public static DatagramSocket socket2;
    public static Handler mHandler;
    public Context mcontext;
    public AlarmDialog alarmDialog;

    public static Handler getHandler() {
        return mHandler;
    }

    private TextView dateTextView, dayOfWeekTextView, timeTextView;
    ImageView imageView, isAlarm, isStop;
    ImageView st;

    // 设置RTSP流地址
    String rtspUrl = "rtsp://admin:abc12345@192.168.1.99:554/Streaming/Channels/201";
    String rtspUrl2 = "rtsp://admin:abc12345@192.168.1.252/cam/realmonitor?channel=7&subtype=0";
    PlayerView playerView;
    PlayerView playerView2;
    CaStDialog caDialog;
    ImageView dr;
    ImageView dy;
    String[] jjcasArray;

    /*
     * 摄像头相关
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

    }

    //mqtt连接
    public void initMqtt() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyMqtt mqtt = new MyMqtt("Pad", callback, false);
            }
        }).start();
    }

    public void initMqtt(String[] args) {
        if (!isUWBConnect) {

        }
        if (args[3].equals("")) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (args[3].length() < 3) {
                    Log.e(TAG, "mqtt: 正在使用匿名连接");
                    MyMqtt mqtt = new MyMqtt("Pad", callback, true, null, null);
                } else {
                    Log.e(TAG, "mqtt: 正在使用加密连接");
                    MyMqtt mqtt = new MyMqtt("Pad", args, callback, false);
                }
            }
        }).start();
    }

    /*
     * mqtt消息
     * */
    public MqttCallback callback = new MqttCallback() {
        @Override
        public void connectionLost(Throwable cause) {
            isUWBConnect = false;
            Log.e(TAG, "connectionLost: 连接到mqtt服务器失败，请检查mqtt服务器和pad设置");
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            if (topic.equals("UWB/ALARM")) {
                Log.e(TAG, "messageArrived: 收到mqtt服务器消息" + message.toString());
                AlarmPacket messagePacket = new Gson().fromJson(String.valueOf(message), AlarmPacket.class);
                Message message1 = new Message();
                message1.what = 300;
                message1.obj = messagePacket;
                mHandler.sendMessage(message1);
            }
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            Log.e(TAG, "deliveryComplete: " + token.toString());
        }


    };

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

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

    public void initUI() {
        imageView = findViewById(R.id.gifImageView);
        isAlarm = findViewById(R.id.alarming);
        isStop = findViewById(R.id.stop);
        timeTextView = findViewById(R.id.time_h);
        dateTextView = findViewById(R.id.time_y);
        dayOfWeekTextView = findViewById(R.id.time_d);
        st = findViewById(R.id.st);
        dr = findViewById(R.id.dr);
        dy = findViewById(R.id.dy);
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
        initUI();
        initCa();
        initRTSP();
        initHandler();
        updateDate();
        initMqtt();
        mcontext = this;
        initAlarmDialog();
        jjcasArray = getResources().getStringArray(R.array.jjcas);
    }

    public void checkIds(String id) {
        for (String s : jjcasArray) {
            if (s.equals(id)) {

            } else {

            }
        }
    }

    public void initHandler() {
        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == 100) {
                    dr.setVisibility(View.VISIBLE);
                    dy.setVisibility(View.INVISIBLE);
                    int[] dec = (int[]) msg.obj;
                    Toast.makeText(getApplicationContext(), "Success!当前距离为" + dec[2], Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "警告！当前距离小于8米！", Toast.LENGTH_SHORT).show();
                } else if (msg.what == 101) {
                    dy.setVisibility(View.INVISIBLE);
                } else if (msg.what == 102) {
                    dy.setVisibility(View.VISIBLE);
                    dr.setVisibility(View.INVISIBLE);
                    int[] dec = (int[]) msg.obj;
                    Toast.makeText(getApplicationContext(), "Success!当前距离为" + dec[2], Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "警告！当前距离小于16米！", Toast.LENGTH_SHORT).show();
                } else if (msg.what == 103) {
                    dr.setVisibility(View.INVISIBLE);
                } else if (msg.what == 104) {
                    dr.setVisibility(View.INVISIBLE);
                    dy.setVisibility(View.INVISIBLE);
                    int[] dec = (int[]) msg.obj;
                    Toast.makeText(getApplicationContext(), "Success!当前距离为" + dec[2], Toast.LENGTH_SHORT).show();
                } else if (msg.what == 200) {
                    initSt((String[]) msg.obj);
                } else if (msg.what == 300) {
                    AlarmPacket alarmPacket = (AlarmPacket) msg.obj;
                    if (alarmPacket.getALAMRIN() == 1) {
                        isAlarm.setImageResource(R.drawable.rd);
                        if (alarmDialog != null) {
                            alarmDialog.dismiss();
                            alarmDialog = new AlarmDialog(mcontext);
                            alarmDialog.show();
                        } else {
                            alarmDialog = new AlarmDialog(mcontext);
                            alarmDialog.show();
                        }
                    } else {
                        isAlarm.setImageResource(R.drawable.gr);
                    }

                    if (alarmPacket.getALARMR() == 1) {
                        isStop.setImageResource(R.drawable.rd);
                    } else {
                        isStop.setImageResource(R.drawable.gr);
                    }
                }
                if (caDialog != null)
                    caDialog.dismiss();
            }
        };

        // 每秒更新一次时间
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                updateTime();
                mHandler.postDelayed(this, 1000);
            }
        });
    }

    public void initAlarmDialog() {
        alarmDialog = new AlarmDialog(mcontext);
    }

    public void initSt(String[] args) {
        initConfigData(args);
        initRTSP(args);
        initMqtt(args);
        SharedPreferencesData.Settings settings = new SharedPreferencesData.Settings(args[9],
                "",
                args[0],
                args[7],
                args[8], DeviceIdExtractor.extractDeviceIds(args[6]), args[2], args[3], args[4]);
        saveSettings(mcontext, settings);
    }

    public void initConfigData(String[] args) {
        ConfigData configData = new ConfigData(args[9], "", args[1], args[7], args[8], initBmd(args[6]));
        Log.e(TAG, "initConfigData: " + configData.toJson());
        Message message = new Message();
        message.what = 100;
        message.obj = configData.toJson();
        MyMqtt.mqHandler.sendMessage(message);
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
        if (args[0].equals("")) {
            return;
        }
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
}