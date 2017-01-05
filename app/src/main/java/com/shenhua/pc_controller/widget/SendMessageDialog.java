package com.shenhua.pc_controller.widget;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.Window;

import com.shenhua.pc_controller.base.BaseBottomSheetDialog;

/**
 * Created by shenhua on 1/5/2017.
 * Email shenhuanet@126.com
 */
public class SendMessageDialog extends BaseBottomSheetDialog {

    public SendMessageDialog(@NonNull Activity activity, @LayoutRes int contentViewRes) {
        super(activity, contentViewRes);
    }

    @Override
    protected void onBindContentView(Window window) {

    }
}
