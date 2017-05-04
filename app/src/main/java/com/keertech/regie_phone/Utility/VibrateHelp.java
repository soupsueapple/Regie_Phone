package com.keertech.regie_phone.Utility;

import android.content.Context;
import android.os.Vibrator;

/**
 * Created by soup on 2017/5/4.
 */

public class VibrateHelp {

    private static Vibrator vibrator;

    public static void vSimple(Context context) {
        vibrator = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
        vibrator.vibrate(60);
    }

    public static void vComplicated(Context context, long[] pattern, int repeate) {
        vibrator = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
        vibrator.vibrate(pattern, repeate);
    }

    public static void stop() {
        if (vibrator != null) {
            vibrator.cancel();
        }
    }

}
