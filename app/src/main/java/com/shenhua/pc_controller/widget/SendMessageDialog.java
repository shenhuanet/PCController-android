package com.shenhua.pc_controller.widget;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;

import com.shenhua.pc_controller.R;
import com.shenhua.pc_controller.base.BaseBottomSheetDialog;
import com.shenhua.pc_controller.utils.SocketCallback;
import com.shenhua.pc_controller.utils.SocketUtils;

import static com.shenhua.pc_controller.utils.StringUtils.EDIT_COPY;

/**
 * Created by shenhua on 1/5/2017.
 * Email shenhuanet@126.com
 */
public class SendMessageDialog extends BaseBottomSheetDialog implements View.OnClickListener {

    public SendMessageDialog(@NonNull Activity activity, @LayoutRes int contentViewRes) {
        super(activity, contentViewRes);
    }

    @Override
    protected void onBindContentView(Window window) {
        window.findViewById(R.id.btn_copy).setOnClickListener(this);
        window.findViewById(R.id.btn_paste).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_copy:
                SocketUtils.getInstance().communicate(EDIT_COPY, new SocketCallback() {
                    @Override
                    public void onSuccess(String msg) {
                        ClipboardManager cm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                        cm.setText(msg);
                    }

                    @Override
                    public void onFailed(int errorCode, String msg) {

                    }
                });
                break;
            case R.id.btn_paste:
                // SocketUtils.getInstance().communicate(EDIT_PASTE, null);
                break;
        }
    }
}
