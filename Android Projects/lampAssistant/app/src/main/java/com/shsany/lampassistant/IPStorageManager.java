package com.shsany.lampassistant;

/**
 * 作者 zf
 * 日期 2024/6/19
 */

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IPStorageManager {
    private static final String PREF_NAME = "IPAddresses";
    private static final String KEY_IPS = "ip_list";
    private static final int MAX_IP_COUNT = 5;
    private SharedPreferences sharedPreferences;

    public IPStorageManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // 获取存储的IP地址列表
    public List<String> getIPAddresses() {
        String savedIPs = sharedPreferences.getString(KEY_IPS, "");
        if (savedIPs.isEmpty()) {
            return new ArrayList<>();
        } else {
            return new ArrayList<>(Arrays.asList(savedIPs.split(",")));
        }
    }

    // 添加新的IP地址，处理超过限制的情况
    public void addIPAddress(String ipAddress) {
        List<String> ipList = getIPAddresses();

        // 如果已经存在，则不重复添加
        if (ipList.contains(ipAddress)) {
            return;
        }

        // 添加新IP地址到列表
        ipList.add(ipAddress);

        // 如果超过了最大存储数量，移除第一个元素（最旧的IP地址）
        if (ipList.size() > MAX_IP_COUNT) {
            ipList.remove(0);
        }

        // 保存更新后的IP地址列表
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_IPS, joinList(ipList));
        editor.apply();
    }

    // 将列表转换成逗号分隔的字符串
    private String joinList(List<String> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if (i < list.size() - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }
}
