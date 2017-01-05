package com.shenhua.pc_controller.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

/**
 * Created by shenhua on 1/5/2017.
 * Email shenhuanet@126.com
 */
public class BaseAlertDialog {

    public BaseAlertDialog(@NonNull Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提示");
        builder.setMessage("确定吗？");
        builder.show();
    }

}
