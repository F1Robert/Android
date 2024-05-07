package com.shsany.riskelectronicfence.data;

import com.google.gson.Gson;

/*
* UWB端设置下发数据
* */
public class ConfigData {
    private String AI_setveri_ip;
    private String BSG_ip;
    private String camare_ip;
    private String yellow_r;
    private String red_r;
    private String[] White_list;

    // 构造函数
    public ConfigData(String AI_setveri_ip, String BSG_ip, String camare_ip, String yellow_r, String red_r, String[] White_list) {
        this.AI_setveri_ip = AI_setveri_ip;
        this.BSG_ip = BSG_ip;
        this.camare_ip = camare_ip;
        this.yellow_r = yellow_r;
        this.red_r = red_r;
        this.White_list = White_list;
    }

    // 将对象转换为 JSON 字符串
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
