package com.shsany.lampassistant;

import android.net.NetworkRequest;

/**
 * 作者 zf
 * 日期 2024/6/19
 */
public class SettingItem {
    private String name;
    private String value;
    private String defaultValue;

    public SettingItem(String name, String value, String defaultValue) {
        this.name = name;
        this.value = value;
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String type, String value) {
        new NetworkSetRequest().execute("192.168.1.219", value, type);
        this.value = value;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
