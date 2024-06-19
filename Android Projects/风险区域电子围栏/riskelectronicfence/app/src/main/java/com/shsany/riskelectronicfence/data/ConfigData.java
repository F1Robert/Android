package com.shsany.riskelectronicfence.data;

import com.google.gson.Gson;

/*
 * UWB端设置下发数据
 * */
public class ConfigData {
    /*
    0表示PAD设置
    1表示设备注册
    2表示AI报警
    3表示UWB报警
    */
    private int FUNC = 0;
    private String AIIP;
    private String ALMIP;
    private String CIP;
    private String ALARMY;
    private String ALARMR;
    private String[] WLIST;
    private int WKMODE;

    // 构造函数
    public ConfigData(String AIIP, String ALMIP, String CIP, String ALARMY, String ALARMR, String[] WLIST) {
        this.FUNC = 0;
        this.AIIP = AIIP;
        this.ALMIP = ALMIP;
        this.CIP = CIP;
        this.ALARMY = ALARMY;
        this.ALARMR = ALARMR;
        this.WLIST = WLIST;
    }

    public ConfigData() {
        this.FUNC = 0;
        this.AIIP = "128.1.30.21";
        this.ALMIP = "128.1.30.23";
        this.CIP = "128.1.30.22";
        this.ALARMY = "3.2";
        this.ALARMR = "6.4";
        this.WLIST = new String[]{"50036", "50037", "50038"};
    }

    public ConfigData(int wkMode) {
        this.FUNC = 0;
        this.AIIP = "128.1.30.21";
        this.ALMIP = "128.1.30.23";
        this.CIP = "128.1.30.22";
        this.ALARMY = "3.2";
        this.ALARMR = "6.4";
        this.WLIST = new String[]{"50036", "50037", "50038"};
        this.WKMODE = wkMode;
    }

    // 将对象转换为 JSON 字符串
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
