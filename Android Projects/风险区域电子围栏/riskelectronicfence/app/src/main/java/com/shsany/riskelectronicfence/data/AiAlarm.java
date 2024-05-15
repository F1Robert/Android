package com.shsany.riskelectronicfence.data;

public class AiAlarm {
    private int FUNC;
    private String camera_name;
    private String rtsp_url;
    private String message_id;
    private int AID;
    private int RANGE;
    private String person_name;
    private int AMARMR;
    private int AMARMY;

    public int getFUNC() {
        return FUNC;
    }

    public void setFUNC(int FUNC) {
        this.FUNC = FUNC;
    }

    public String getCamera_name() {
        return camera_name;
    }

    public void setCamera_name(String camera_name) {
        this.camera_name = camera_name;
    }

    public String getRtsp_url() {
        return rtsp_url;
    }

    public void setRtsp_url(String rtsp_url) {
        this.rtsp_url = rtsp_url;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public int getAID() {
        return AID;
    }

    public void setAID(int AID) {
        this.AID = AID;
    }

    public int getRANGE() {
        return RANGE;
    }

    public void setRANGE(int RANGE) {
        this.RANGE = RANGE;
    }

    public String getPerson_name() {
        return person_name;
    }

    public void setPerson_name(String person_name) {
        this.person_name = person_name;
    }

    public int getAMARMR() {
        return AMARMR;
    }

    public void setAMARMR(int AMARMR) {
        this.AMARMR = AMARMR;
    }

    public int getAMARMY() {
        return AMARMY;
    }

    public void setAMARMY(int AMARMY) {
        this.AMARMY = AMARMY;
    }
}
