package com.shsany.riskelectronicfence.ui;

import static com.shsany.riskelectronicfence.data.SharedPreferencesData.loadSettings;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.media3.common.util.UnstableApi;

import com.shsany.riskelectronicfence.MainActivity;
import com.shsany.riskelectronicfence.R;
import com.shsany.riskelectronicfence.data.SharedPreferencesData;


public class CaStDialog extends Dialog {
    public TextView fenceStView, udpTest, exitCa, saveCa;
    public EditText ai, rcx, pad, hq, rq, mqtt_fwq, mqtt_uname, mqtt_pwd, hmd, bmd, ai_fwq;

    public CaStDialog(@NonNull Context context) {
        super(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置对话框全屏显示
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setContentView(R.layout.dialog_ca);
        setTitle("设置");
        initUI();
        initListeners();
    }

    /*
     * 界面初始化
     * */
    public void initUI() {
        fenceStView = findViewById(R.id.fence_st);
        udpTest = findViewById(R.id.udp_test);
        ai = findViewById(R.id.alarm_ip_edittext);
        rcx = findViewById(R.id.infrared_camera_ip_edittext);
        pad = findViewById(R.id.pad_ip_edittext);
        hq = findViewById(R.id.yellow_r);
        rq = findViewById(R.id.red_r);
        hmd = findViewById(R.id.hmd);
        bmd = findViewById(R.id.bmd);
        mqtt_fwq = findViewById(R.id.mqtt_fwq);
        mqtt_uname = findViewById(R.id.mqtt_uname);
        mqtt_pwd = findViewById(R.id.mqtt_pwd);
        ai_fwq = findViewById(R.id.ai_fwq);
        exitCa = findViewById(R.id.exit_ca);
        saveCa = findViewById(R.id.save_ca);
        initListeners();
//        initDefaultMqtt();
        loadDataSettings();
    }

    public void initDefaultMqtt() {
        mqtt_fwq.setText("tcp://192.168.0.100:1883");
        mqtt_uname.setText("admin");
        mqtt_pwd.setText("123456");
        ai.setText("128.1.30.21");
        rcx.setText("128.1.30.23");
        ai_fwq.setText("128.1.30.22");
        rq.setText("3.2");
        hq.setText("6.2");
    }

    public void loadDataSettings() {
        SharedPreferencesData.Settings settings = loadSettings(getContext());
        mqtt_fwq.setText(settings.getUwbMqtt());
        mqtt_uname.setText(settings.getUwbUserName());
        mqtt_pwd.setText(settings.getUwbPwd());
        String bmdS = new String();
        for (String s : settings.getWhite_list()) {
            bmdS = bmdS + s + " ";
        }
        bmd.setText(bmdS);
        ai_fwq.setText(settings.getAI_setveri_ip());
        ai.setText(settings.getCamare_ip());
        rcx.setText(settings.getCamare_ip());
        rq.setText(settings.getRed_r());
        hq.setText(settings.getYellow_r());
    }

    /*
     * 设置监听器
     * */
    public void initListeners() {
        initSaveCaListener();
        initExitStViewListener();
    }


    public void initSaveCaListener() {
        saveCa.setOnClickListener(new View.OnClickListener() {
            @OptIn(markerClass = UnstableApi.class)
            @Override
            public void onClick(View v) {
                // 显示加载动画
                ProgressDialog progressDialog = new ProgressDialog(getContext(), R.style.InterfaceTitleTextStyleLoading);
                progressDialog.setMessage("设置保存中..."); // 设置消息
                progressDialog.setCancelable(true); // 设置是否可以通过返回键取消对话框
                progressDialog.show(); // 显示对话框

                // 获取对话框的窗口
                Window window = progressDialog.getWindow();
                if (window != null) {
                    // 设置对话框的大小
                    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                    layoutParams.copyFrom(window.getAttributes());
                    layoutParams.width = 400; // 设置宽度为400像素
                    layoutParams.height = 100; // 设置高度为300像素
                    layoutParams.gravity = Gravity.CENTER; // 设置居中显示
                    window.setAttributes(layoutParams);
                }

                // 延迟一秒后关闭加载动画
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                    }
                }, 1000);

                dismiss();
                Message msg = new Message();
                String[] args = new String[]{
                        String.valueOf(ai.getText()),
                        String.valueOf(rcx.getText()),
                        String.valueOf(mqtt_fwq.getText()),
                        String.valueOf(mqtt_uname.getText()),
                        String.valueOf(mqtt_pwd.getText()),
                        String.valueOf(hmd.getText()),
                        String.valueOf(bmd.getText()),
                        String.valueOf(hq.getText()),
                        String.valueOf(rq.getText()),
                        String.valueOf(ai_fwq.getText())
                };
                msg.obj = args;
                msg.what = 200;
                MainActivity.mHandler.sendMessage(msg);
            }
        });
    }

    public void initExitStViewListener() {
        exitCa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}
