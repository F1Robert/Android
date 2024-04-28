package com.shsany.riskelectronicfence.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.media3.common.util.UnstableApi;

import com.shsany.riskelectronicfence.MainActivity;
import com.shsany.riskelectronicfence.R;


public class CaStDialog extends Dialog {
    public TextView fenceStView, udpTest, exitCa, saveCa;
    public EditText er, ai;
    UDPDialog udpDialog;
    FCDialog fcDialog;

    public CaStDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_ca);
        setTitle("摄像头设置");
        initUI();
        initSaveCaListener();
        initExitCaListener();
    }

    /*
     * 界面初始化
     * */
    public void initUI() {
        fenceStView = findViewById(R.id.fence_st);
        udpTest = findViewById(R.id.udp_test);
        er = findViewById(R.id.exit_st);
        ai = findViewById(R.id.ca_st);
        exitCa = findViewById(R.id.exit_ca);
        saveCa = findViewById(R.id.save_ca);
        udpDialog = new UDPDialog(getContext());
        fcDialog = new FCDialog(getContext());
    }

    /*
     * 设置监听器
     * */
    public void initListeners() {
        initFenceStViewListener();
        initUdpTestViewListener();
        initExitStViewListener();
        initCaStViewListener();
    }

    public void initExitCaListener() {
        exitCa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void initSaveCaListener() {
        saveCa.setOnClickListener(new View.OnClickListener() {
            @OptIn(markerClass = UnstableApi.class)
            @Override
            public void onClick(View v) {
                dismiss();
                Message msg = new Message();
                String[] args = new String[]{String.valueOf(ai.getText()), String.valueOf(er.getText())};
                msg.obj = args;
                msg.what = 200;
                MainActivity.mHandler.sendMessage(msg);
            }
        });
    }

    public void initFenceStViewListener() {
        fenceStView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDismiss();
                if (fcDialog == null) {
                    fcDialog = new FCDialog(getContext());
                    fcDialog.show();
                } else if (fcDialog.isShowing()) {
                    fcDialog.dismiss();
                    fcDialog.show();
                } else {
                    fcDialog.show();
                }
            }
        });
    }

    public void initUdpTestViewListener() {
        udpTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDismiss();
                if (udpDialog == null) {
                    udpDialog = new UDPDialog(getContext());
                    udpDialog.show();
                } else if (udpDialog.isShowing()) {
                    udpDialog.dismiss();
                    udpDialog.show();
                } else {
                    udpDialog.show();
                }
            }
        });
    }

    public void initCaStViewListener() {
        udpTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDismiss();
                if (udpDialog == null) {
                    udpDialog = new UDPDialog(getContext());
                    udpDialog.show();
                } else if (udpDialog.isShowing()) {
                    udpDialog.dismiss();
                    udpDialog.show();
                } else {
                    udpDialog.show();
                }
            }
        });
    }

    public void initExitStViewListener() {
        er.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void mDismiss() {
        if (udpDialog != null) {
            udpDialog.dismiss();
            udpDialog = null;
        }
        if (fcDialog != null) {
            fcDialog.dismiss();
            fcDialog = null;
        }
    }
}
