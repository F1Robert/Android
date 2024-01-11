package com.nolan.utills.websocket.util;

/**
 * 作者 zf
 * 日期 2024/1/11
 */
public class MessageEvent {
    private String message;

    public MessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
