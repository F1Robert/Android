package com.shsany.riskelectronicfence.data;

/*
 * UWB端报警上传数据
 * */
public class AlarmPacket {
    private int AID;
    private int TID;
    private int RANGE;
    private int ALAMRIN;
    private int ALARMY;
    private int ALARMR;

    public AlarmPacket(int AID, int TID, int RANGE, int ALAMRIN, int ALARMY, int ALARMR) {
        this.AID = AID;
        this.TID = TID;
        this.RANGE = RANGE;
        this.ALAMRIN = ALAMRIN;
        this.ALARMY = ALARMY;
        this.ALARMR = ALARMR;
    }

    // 添加getter和setter方法

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

    public int getALAMRIN() {
        return ALAMRIN;
    }

    public void setALAMRIN(int ALAMRIN) {
        this.ALAMRIN = ALAMRIN;
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
        return "MessagePacket{" +
                "AID=" + AID +
                ", TID=" + TID +
                ", RANGE=" + RANGE +
                ", ALAMRIN=" + ALAMRIN +
                ", ALARMY=" + ALARMY +
                ", ALARMR=" + ALARMR +
                '}';
    }
}


