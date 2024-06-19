package com.shsany.riskelectronicfence.data;

/*
 * UWB端报警上传数据
 * */
public class AlarmPacket {
    /*
    0表示PAD设置
    1表示设备注册
    2表示AI报警
    3表示UWB报警
    */
    private int FUNC;
    private int AID;
    private int TID;
    private int RANGE;
    private int ALARMIN;
    private int ALARMY;
    private int ALARMR;
    // 添加getter和setter方法
    public int getFUNC() {
        return FUNC;
    }

    public void setFUNC(int FUNC) {
        this.FUNC = FUNC;
    }

    public int getAID() {
        return AID;
    }

    public void setAID(int AID) {
        this.AID = AID;
    }

    public int getTID() {
        return TID;
    }

    public void setTID(int TID) {
        this.TID = TID;
    }

    public int getRANGE() {
        return RANGE;
    }

    public void setRANGE(int RANGE) {
        this.RANGE = RANGE;
    }

    public int getALARMIN() {
        return ALARMIN;
    }

    public void setALARMIN(int ALARMIN) {
        this.ALARMIN = ALARMIN;
    }

    public int getALARMY() {
        return ALARMY;
    }

    public void setALARMY(int ALARMY) {
        this.ALARMY = ALARMY;
    }

    public int getALARMR() {
        return ALARMR;
    }

    public void setALARMR(int ALARMR) {
        this.ALARMR = ALARMR;
    }

    @Override
    public String toString() {
        return "AlarmPacket{" +
                "FUNC=" + FUNC +
                ", AID=" + AID +
                ", TID=" + TID +
                ", RANGE=" + RANGE +
                ", ALARMIN=" + ALARMIN +
                ", ALARMY=" + ALARMY +
                ", ALARMR=" + ALARMR +
                '}';
    }
}


