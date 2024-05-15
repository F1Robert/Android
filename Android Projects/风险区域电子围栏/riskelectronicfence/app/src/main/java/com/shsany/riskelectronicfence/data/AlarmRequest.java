package com.shsany.riskelectronicfence.data;

public class AlarmRequest {
    private String func;
    private Data data;

    public static class Data {
        private String camera_name;
        private String rtsp_url;
        private String message_id;
        private int LSB_id;
        private int tag_range;
        private String person_name;
        private int alarm_red;
        private int alarm_yellow;

        // Getters and setters

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

        public int getLSB_id() {
            return LSB_id;
        }

        public void setLSB_id(int LSB_id) {
            this.LSB_id = LSB_id;
        }

        public int getTag_range() {
            return tag_range;
        }

        public void setTag_range(int tag_range) {
            this.tag_range = tag_range;
        }

        public String getPerson_name() {
            return person_name;
        }

        public void setPerson_name(String person_name) {
            this.person_name = person_name;
        }

        public int getAlarm_red() {
            return alarm_red;
        }

        public void setAlarm_red(int alarm_red) {
            this.alarm_red = alarm_red;
        }

        public int getAlarm_yellow() {
            return alarm_yellow;
        }

        public void setAlarm_yellow(int alarm_yellow) {
            this.alarm_yellow = alarm_yellow;
        }
    }

    public String getFunc() {
        return func;
    }

    public void setFunc(String func) {
        this.func = func;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
