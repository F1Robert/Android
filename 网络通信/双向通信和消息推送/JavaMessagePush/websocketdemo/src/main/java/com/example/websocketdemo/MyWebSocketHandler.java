package com.example.websocketdemo;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class MyWebSocketHandler extends TextWebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 连接建立时调用
        super.afterConnectionEstablished(session);
        // 可以在此处执行一些初始化操作
        System.out.println("WebSocket连接成功");
        // 可以在此处执行一些初始化操作
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 处理来自客户端的文本消息
        String payload = message.getPayload();
        //session.sendMessage(new TextMessage("收到消息: " + payload));
        if (payload.equals("检测更新")) {
            //检测更新的逻辑
            session.sendMessage(new TextMessage("版本更新" + "http://192.168.1.197:7860/download/app-debug.apk"));
            // 可以在此处执行一些处理逻辑
            System.out.println("发出消息: " + "http://192.168.1.197:7860/download/app-debug.apk");
        }
    }


    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        // 处理传输错误
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 连接关闭时调用
        super.afterConnectionClosed(session, status);
        // 可以在此处执行一些清理操作
    }

}
