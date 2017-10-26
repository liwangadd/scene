package com.windylee.scene.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;

import com.afollestad.materialdialogs.MaterialDialog;
import com.windylee.scene.R;
import com.windylee.scene.widget.seekbar.BubbleSeekBar;

import java.util.List;

/**
 * Created by windylee on 4/18/17.
 */

public class DialogUtils {

    public static MaterialDialog showInputDialog(Context context, String title, String hint, MaterialDialog.InputCallback callback) {
        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title(title)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input("", hint, callback).build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        return dialog;
    }

    public static MaterialDialog showListDialog(Context context, String title, List<String> itemsContent, MaterialDialog.ListCallback callback) {
        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title(title)
                .items(itemsContent)
                .itemsCallback(callback)
                .build();
        dialog.show();
        return dialog;
    }

    public static BubbleSeekBar showSeekBarDialog(Context context, String title, boolean isDefault, int pos,
                                                  int max, DialogInterface.OnClickListener listener) {
        BubbleSeekBar mSeekBar = new BubbleSeekBar(context);
        if (!isDefault) {
            mSeekBar.getConfigBuilder().min(30).max(max).showSectionText().progress(pos)
                    .sectionTextColor(context.getResources().getColor(R.color.color_red_light))
                    .secondTrackColor(context.getResources().getColor(R.color.color_red))
                    .build();
        } else {
            mSeekBar.getConfigBuilder().min(0).max(max).showSectionText().progress(pos)
                    .sectionCount(max).seekBySection()
                    .sectionTextColor(context.getResources().getColor(R.color.color_red_light))
                    .secondTrackColor(context.getResources().getColor(R.color.color_red))
                    .build();
        }
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage("")
                .setView(mSeekBar)
                .setPositiveButton("OK", listener).show();
        return mSeekBar;
    }

}
