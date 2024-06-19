package com.shsany.lampassistant;

/**
 * 作者 zf
 * 日期 2024/6/19
 */
public class MessageEvent {
    private String message;
    private String value;
    public MessageEvent(String message) {
        this.message = message;
    }

    public MessageEvent(String message, String value) {
        this.message = message;
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
