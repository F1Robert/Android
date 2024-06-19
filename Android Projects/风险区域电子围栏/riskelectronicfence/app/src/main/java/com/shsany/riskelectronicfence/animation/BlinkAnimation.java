package com.shsany.riskelectronicfence.animation;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

public class BlinkAnimation {

    private static Animation blinkAnimation;

    public static void blink(final ImageView imageView) {
        if (blinkAnimation == null || !blinkAnimation.hasStarted() || blinkAnimation.hasEnded()) {
            blinkAnimation = createBlinkAnimation();
            imageView.startAnimation(blinkAnimation);
        }
    }

    private static Animation createBlinkAnimation() {
        AnimationSet animationSet = new AnimationSet(true);

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(500);
        fadeIn.setInterpolator(new LinearInterpolator());

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setDuration(500);
        fadeOut.setStartOffset(500);
        fadeOut.setInterpolator(new LinearInterpolator());

        animationSet.addAnimation(fadeIn);
        animationSet.addAnimation(fadeOut);
        animationSet.setRepeatMode(Animation.RESTART);
        animationSet.setRepeatCount(Animation.INFINITE);

        return animationSet;
    }

    public static void stopBlink() {
        if (blinkAnimation != null) {
            blinkAnimation.cancel();
            blinkAnimation = null;
        }
    }


    public static void setVisibilityWithBlink(final ImageView imageView) {
        blink(imageView);
    }
}
