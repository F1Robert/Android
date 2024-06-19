package com.shsany.lampassistant;

import android.os.AsyncTask;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class NetworkGetRequest extends AsyncTask<String, Void, Map<String, String>> {

    private static final String TAG = "获取数据中";
    private String deviceIp;

    public NetworkGetRequest(String deviceIp) {
        this.deviceIp = deviceIp;
    }

    @Override
    protected Map<String, String> doInBackground(String... params) {
        String[] properties = {
                "wifi_ssid", "wifi_pwd", "wifi_ip", "net_mask", "gate_way",
                "device_id", "http_host", "ftp_host", "ftp_root", "ftp_user",
                "ftp_pwd", "cap_sec", "opt_tag"
        };

        Map<String, String> results = new HashMap<>();

        for (String property : properties) {
            String urlString = "http://" + deviceIp + "/cgi-bin/Config.cgi?action=get&property=" + property;
            String result = sendGetRequest(urlString);
            results.put(property, result);
        }

        return results;
    }

    private String sendGetRequest(String urlString) {
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

            int responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {

                in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String inputLine = in.readLine(); // 只读取第一行内容
                if (inputLine != null) {
                    result.append(inputLine);
                }
            } else {
                result.append("Request failed. Response Code: ").append(responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.append("Exception: ").append(e.getMessage());
        } finally {
            // 关闭 BufferedReader 和 HttpURLConnection
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result.toString();
    }

    @Override
    protected void onPostExecute(Map<String, String> results) {
        super.onPostExecute(results);
        // 在这里处理所有属性的请求结果，例如更新UI
        for (Map.Entry<String, String> entry : results.entrySet()) {
            String property = entry.getKey();
            String value = entry.getValue();
            EventBus.getDefault().post(new MessageEvent(property,value));
            // 处理每个属性的值
            Log.e(TAG, value);
        }
    }
}
