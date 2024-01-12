package com.nolan.utills.glidelibrary;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;

/**
 * 作者 zf
 * 日期 2024/1/12
 */
public class GlideUtil {
    public static void loadImgWithListener(Context context, String url, ImageView target, RequestListener<Drawable> drawableRequestListener) {
        Glide.with(context) // 使用当前的Activity或Fragment
                .load(url) // 加载图片的URL
                //添加加载失败重加载逻辑
                .listener(drawableRequestListener)
                .into(target);
    }

    public static void loadImgAndResize(Context context, String url, ImageView target, int width, int height) {
        Glide.with(context) // 使用当前的Activity或Fragment
                .load(url) // 加载图片的URL
                .apply(new RequestOptions()
                        .override(width, height) // 压缩图片的宽高
                )
                //添加加载失败重加载逻辑
                .into(target);
    }

    public static void loadImg(Context context, String url, ImageView target) {
        Glide.with(context) // 使用当前的Activity或Fragment
                .load(url) // 加载图片的URL
                //添加加载失败重加载逻辑
                .into(target);
    }

}
