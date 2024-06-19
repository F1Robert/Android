package com.shsany.riskelectronicfence.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeviceIdExtractor {
    public static String[] extractDeviceIds(String input) {
        // 使用正则表达式匹配逗号、中文逗号、英文逗号、空格
        Pattern pattern = Pattern.compile("[,，\\s]");
        // 使用正则表达式分割字符串
        String[] parts = pattern.split(input);
        // 用于存储提取出的设备ID号
        String[] deviceIds = new String[parts.length];
        // 使用正则表达式匹配设备ID号
        Pattern deviceIdPattern = Pattern.compile("\\d+");
        for (int i = 0; i < parts.length; i++) {
            Matcher matcher = deviceIdPattern.matcher(parts[i]);
            // 如果找到匹配的设备ID号，则存入数组中
            if (matcher.find()) {
                deviceIds[i] = matcher.group();
            }
        }
        return deviceIds;
    }
}

