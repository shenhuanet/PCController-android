package com.shenhua.pc_controller.base;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

/**
 * Created by shenhua on 1/5/2017.
 * Email shenhuanet@126.com
 */
public abstract class BaseBottomSheetDialog extends BottomSheetDialog {

    private Activity activity;

    public BaseBottomSheetDialog(@NonNull Activity activity, View contentView) {
        super(activity);
        this.activity = activity;
        if (contentView != null) {
            intView(contentView);
        }
    }

    public BaseBottomSheetDialog(@NonNull Activity activity, @LayoutRes int contentViewRes) {
        super(activity);
        this.activity = activity;
        if (contentViewRes != 0) {
            View view = LayoutInflater.from(activity).inflate(contentViewRes, null);
            intView(view);
        }
    }

    private void intView(View view) {
        Log.i("BaseBottomSheetDialog:", "initView");
        setContentView(view);
        Window window = getWindow();
        if (window == null) {
            Log.i("BaseBottomSheetDialog:", "initView filed,please check something right.");
            return;
        }
        onBindContentView(getWindow());
    }

    protected abstract void onBindContentView(Window window);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("BaseBottomSheetDialog:", "onCreate");
        int screenHeight = getScreenHeight(activity);
        int statusBarHeight = getStatusBarHeight(getContext());
        int dialogHeight = screenHeight - statusBarHeight;
        Window window = getWindow();
        if (window == null) return;
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);
    }

    private static int getScreenHeight(Activity activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.heightPixels;
    }

    private static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = res.getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }
}
