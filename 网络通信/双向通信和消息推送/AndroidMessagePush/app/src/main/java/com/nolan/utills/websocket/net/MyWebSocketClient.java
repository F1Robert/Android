package com.nolan.utills.websocket.net;

/**
 * 作者 zf
 * 日期 2024/1/10
 */

import android.util.Log;

import com.nolan.utills.websocket.util.MessageEvent;
import com.nolan.utills.websocket.util.UpdateManager;

import org.greenrobot.eventbus.EventBus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class MyWebSocketClient {

    public static final String TAG = "zf";
    private OkHttpClient client;
    private WebSocket webSocket;

    public MyWebSocketClient() {
        client = new OkHttpClient();
    }

    public void connectWebSocket() {
        Request request = new Request.Builder()
                .url("ws://192.168.1.197:7860/ws") // 替换为你的WebSocket服务器地址和路径
                .build();

        WebSocketListener listener = new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                Log.e(TAG, "双向通信已打开: ");
                // WebSocket连接已打开
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                // 收到文本消息
                Log.e(TAG, "服务器消息:" + text);
                if (text.contains("版本更新")) {
                    // 发现新版本
                    // 使用正则表达式提取 URL
                    // 找到 "版本更新" 字符串在 input 中的位置
                    int startIndex = text.indexOf("版本更新");
                    if (startIndex != -1) {
                        // 提取版本更新字符后的所有内容
                        String url = text.substring(startIndex+4);
                        // 打印版本更新的 URL
                        UpdateManager.getInstance().url = url;
                        EventBus.getDefault().post(new MessageEvent("检测到更新"+url));
                    } else {
                        Log.e(TAG, "未找到 \"版本更新\" 字符串");
                    }
                }
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                // 收到二进制消息
                Log.e(TAG, "服务器消息:" + bytes);
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                // WebSocket正在关闭
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                // WebSocket已关闭
                Log.e(TAG, "双向通信已关闭: ");
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                // 连接失败
                Log.e(TAG, "双向通信打开失败: ");
                if (response != null) {
                    Log.e(TAG, "onFailure: " + response.message() + "消息体" + response.body().toString());
                }
                if (t != null) {
                    Log.e(TAG, "onFailure: " + t.getMessage());
                }
            }
        };

        webSocket = client.newWebSocket(request, listener);
    }

    public void sendMessage(String message) {
        if (webSocket != null) {
            webSocket.send(message);
            Log.e(TAG, "sendMessage: " + message);
        }
    }

    public void closeWebSocket() {
        if (webSocket != null) {
            webSocket.close(1000, "Goodbye, WebSocket!");
        }
    }
}
