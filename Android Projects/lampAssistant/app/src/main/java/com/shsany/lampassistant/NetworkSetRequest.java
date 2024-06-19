package com.shsany.lampassistant;

/**
 * 作者 zf
 * 日期 2024/6/19
 */

import android.os.AsyncTask;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkSetRequest extends AsyncTask<String, Void, String> {

    private static final String TAG = "矿灯设置";

    @Override
    protected String doInBackground(String... params) {
        String deviceIp = params[0];
        String param = params[1];
        String type = params[2];
        String property = "";
        switch (type) {
            case "wifi_ssid":
                property = "wifi_ssid";
                break;
            case "wifi_pwd":
                property = "wifi_pwd";
                break;
            case "wifi_ip":
                property = "wifi_ip";
                break;
            case "net_mask":
                property = "net_mask";
                break;
            case "gate_way":
                property = "gate_way";
                break;
            case "device_id":
                property = "device_id";
                break;
            case "http_host":
                property = "http_host";
                break;
            case "ftp_host":
                property = "ftp_host";
                break;
            case "ftp_root":
                property = "ftp_root";
                break;
            case "ftp_user":
                property = "ftp_user";
                break;
            case "ftp_pwd":
                property = "ftp_pwd";
                break;
            case "cap_sec":
                property = "cap_sec";
                break;
            case "opt_tag ":
                property = "opt_tag";
                break;
        }

        String urlString = "http://" + deviceIp + "/cgi-bin/Config.cgi?action=set&property=" + property + "&value=" + param;
        String result = "";

        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

            int responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                result = response.toString();
            } else {
                result = "Request failed. Response Code: " + responseCode;
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = "Exception: " + e.getMessage();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        // 在这里处理请求结果，例如更新UI
        Log.e(TAG, "onPostExecute: " + result);
        if (result.contains("OK")) {
            EventBus.getDefault().post(new MessageEvent("设置成功", ""));
        }
    }
}
