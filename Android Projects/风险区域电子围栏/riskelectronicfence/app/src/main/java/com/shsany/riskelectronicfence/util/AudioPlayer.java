package com.shsany.riskelectronicfence.util;

import android.content.Context;
import android.media.MediaPlayer;

public class AudioPlayer {

    private MediaPlayer mediaPlayer;

    public void play(Context context, int resId) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, resId);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stop(); // 播放完成后停止播放
                }
            });
        }

        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start(); // 开始播放
        }
    }

    public void stop() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop(); // 停止播放
            mediaPlayer.release(); // 释放资源
            mediaPlayer = null;
        }
    }
}
