package com.shsany.riskelectronicfence.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class SharedPreferencesData {

    private static final String SHARED_PREFERENCES_NAME = "shared_preferences";

    // 方法用于保存设置到 SharedPreferences
    public static void saveSettings(Context context, Settings settings) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(settings);
        editor.putString("settings", json);
        editor.apply();
    }

    // 方法用于从 SharedPreferences 加载设置
    public static Settings loadSettings(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("settings", "");
        Type type = new TypeToken<Settings>() {
        }.getType();
        if (json.equals("")) {
            Settings settings = new Settings("tcp://128.1.30.21:1883", "", "128.1.30.22", "128.1.30.25","6.4", "3.2", new String[]{"50036", "50037", "50038"}, "tcp://192.168.0.100:1883", "admin", "123456");
            json = gson.toJson(settings);
            saveSettings(context, settings);
        }
        return gson.fromJson(json, type);
    }

    // 定义一个类来保存你的字段
    public static class Settings {
        private String AI_setveri_ip;
        private String BSG_ip;
        private String camare_ip;
        private String ai_camare_ip;
        private String yellow_r;
        private String red_r;
        private String[] White_list;
        private String uwbMqtt;
        private String uwbUserName;
        private String uwbPwd;

        // 添加构造函数和 getter/setter 方法
        // ...

        public Settings(String AI_setveri_ip, String BSG_ip, String camare_ip, String ai_camare_ip ,String yellow_r, String red_r, String[] white_list, String uwbMqtt, String uwbUserName, String uwbPwd) {
            this.AI_setveri_ip = AI_setveri_ip;
            this.BSG_ip = BSG_ip;
            this.ai_camare_ip = ai_camare_ip;
            this.camare_ip = camare_ip;
            this.yellow_r = yellow_r;
            this.red_r = red_r;
            this.White_list = white_list;
            this.uwbMqtt = uwbMqtt;
            this.uwbUserName = uwbUserName;
            this.uwbPwd = uwbPwd;
        }

        public String getAi_camare_ip() {
            return ai_camare_ip;
        }

        public void setAi_camare_ip(String ai_camare_ip) {
            this.ai_camare_ip = ai_camare_ip;
        }

        public String getAI_setveri_ip() {
            return AI_setveri_ip;
        }

        public void setAI_setveri_ip(String AI_setveri_ip) {
            this.AI_setveri_ip = AI_setveri_ip;
        }

        public String getBSG_ip() {
            return BSG_ip;
        }

        public void setBSG_ip(String BSG_ip) {
            this.BSG_ip = BSG_ip;
        }

        public String getCamare_ip() {
            return camare_ip;
        }

        public void setCamare_ip(String camare_ip) {
            this.camare_ip = camare_ip;
        }

        public String getYellow_r() {
            return yellow_r;
        }

        public void setYellow_r(String yellow_r) {
            this.yellow_r = yellow_r;
        }

        public String getRed_r() {
            return red_r;
        }

        public void setRed_r(String red_r) {
            this.red_r = red_r;
        }

        public String[] getWhite_list() {
            return White_list;
        }

        public void setWhite_list(String[] white_list) {
            White_list = white_list;
        }

        public String getUwbMqtt() {
            return uwbMqtt;
        }

        public void setUwbMqtt(String uwbMqtt) {
            this.uwbMqtt = uwbMqtt;
        }

        public String getUwbUserName() {
            return uwbUserName;
        }

        public void setUwbUserName(String uwbUserName) {
            this.uwbUserName = uwbUserName;
        }

        public String getUwbPwd() {
            return uwbPwd;
        }

        public void setUwbPwd(String uwbPwd) {
            this.uwbPwd = uwbPwd;
        }
    }


}
