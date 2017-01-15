package com.shenhua.pc_controller.widget;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.Window;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.shenhua.pc_controller.R;
import com.shenhua.pc_controller.base.BaseBottomSheetDialog;
import com.shenhua.pc_controller.core.SocketImpl;
import com.shenhua.pc_controller.utils.SocketCallback;

import static com.shenhua.pc_controller.utils.StringUtils.SYSTEM_GET_VOLUME;
import static com.shenhua.pc_controller.utils.StringUtils.SYSTEM_SET_VOLUME;

/**
 * Created by shenhua on 1/5/2017.
 * Email shenhuanet@126.com
 */
public class SettingDialog extends BaseBottomSheetDialog {

    public SettingDialog(@NonNull Activity activity, @LayoutRes int contentViewRes) {
        super(activity, contentViewRes);
    }

    @Override
    protected void onBindContentView(Window window) {
        final AppCompatSeekBar seekBar = (AppCompatSeekBar) window.findViewById(R.id.seek_volume);
        final TextView volumeTv = (TextView) window.findViewById(R.id.tv_volume);
        SocketImpl.getInstance().communicate(SYSTEM_GET_VOLUME, new SocketCallback() {
            @Override
            public void onSuccess(Object msg) {
                String s = new String((byte[]) msg);
                seekBar.setProgress(Integer.valueOf(s));
                volumeTv.setText(String.format(getContext().getResources().getString(R.string.string_volume), Integer.valueOf(s)));
            }

            @Override
            public void onFailed(int errorCode, String msg) {
                Toast.makeText(getContext(), "系统音量获取失败", Toast.LENGTH_SHORT).show();
                volumeTv.setText(String.format(getContext().getString(R.string.string_volume), 10));
                seekBar.setProgress(10);
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                SocketImpl.getInstance().communicate(SYSTEM_SET_VOLUME + i, null);
                volumeTv.setText(String.format(getContext().getResources().getString(R.string.string_volume), i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
