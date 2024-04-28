package com.shsany.riskelectronicfence.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.shsany.riskelectronicfence.R;
public class STDialog extends Dialog {
    public TextView fenceStView;
    public TextView udpTest;
    public TextView exitSt;
    public TextView aiCaSt;

    UDPDialog udpDialog;
    FCDialog fcDialog;

    CaStDialog caDialog;

    public STDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_setting);
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
        exitSt = findViewById(R.id.exit_st);
        aiCaSt = findViewById(R.id.ca_st);
        udpDialog = new UDPDialog(getContext());
        fcDialog = new FCDialog(getContext());
        caDialog = new CaStDialog(getContext());
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
        aiCaSt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDismiss();
                if (caDialog == null) {
                    caDialog = new CaStDialog(getContext());
                    caDialog.show();
                } else if (caDialog.isShowing()) {
                    caDialog.dismiss();
                    caDialog.show();
                } else {
                    caDialog.show();
                }
            }
        });
    }

    public void initExitStViewListener() {
        exitSt.setOnClickListener(new View.OnClickListener() {
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
        if (caDialog != null) {
            caDialog.dismiss();
            caDialog = null;
        }
    }
}
