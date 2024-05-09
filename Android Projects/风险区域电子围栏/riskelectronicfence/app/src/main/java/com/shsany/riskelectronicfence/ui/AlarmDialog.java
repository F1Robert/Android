package com.shsany.riskelectronicfence.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.shsany.riskelectronicfence.R;

public class AlarmDialog extends Dialog {

    private TextView countdownTextView, confirm, cancel, alarmText;
    private CountDownTimer countDownTimer;

    public AlarmDialog(Context context, int id, int range) {
        super(context);
        setContentView(R.layout.dialog_alarm);

        alarmText = findViewById(R.id.countdown);
        countdownTextView = findViewById(R.id.countdown_text_view);
        confirm = findViewById(R.id.confirm);
        cancel = findViewById(R.id.cancel);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        // 初始化倒计时器，设置总时间为15秒，间隔为1秒
        countDownTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                // 在 TextView 中显示剩余时间
                countdownTextView.setText(millisUntilFinished / 1000 + "请及时确认");
            }

            public void onFinish() {
                // 倒计时结束后关闭 Dialog
                dismiss();
            }
        };
        initAlarmText(id, range);
    }

    public void initAlarmText(int id, int range) {
        if (range > -1) {
            alarmText.setText("掘进机" + id + "发出报警\n当前距离为" + range / 1000 + "米");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 在 Dialog 创建时启动倒计时
        countDownTimer.start();
    }
}

