package com.windylee.scene.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by windylee on 4/12/17.
 */

public class ToastUtils {

    private static Toast mToast = null;

    public static void showToast(Context context, String text) {
        if (mToast == null) {
            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    public static void cancelToast(Context context) {
        if (mToast != null) {
            mToast.cancel();
        }
    }

}
