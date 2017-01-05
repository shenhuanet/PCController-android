package com.shenhua.pc_controller.base;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.util.Log;

/**
 * Created by shenhua on 1/5/2017.
 * Email shenhuanet@126.com
 */
public class BaseAlertDialog {

    private static final String TAG = "BaseAlertDialog";
    private AlertDialog.Builder builder;

    public BaseAlertDialog(@NonNull Context context, @StringRes int resId) {
        this(context, context.getResources().getString(resId));
    }

    public BaseAlertDialog(@NonNull Context context, String msg) {
        if (builder == null)
            builder = new AlertDialog.Builder(context);
        builder.setTitle("提示");
        builder.setMessage("确定吗？");
        if (msg != null) {
            builder.setMessage(msg);
        }
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onNegativeButtonClick();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onPositiveButtonClick();
            }
        });
    }

    protected void onPositiveButtonClick() {
        Log.i(TAG, "onPositiveButtonClick");
    }

    protected void onNegativeButtonClick() {
        Log.i(TAG, "onNegativeButtonClick");
    }

    public void show() {
        builder.show();
    }

}
