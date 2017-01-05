package com.shenhua.pc_controller.utils;

import android.os.Handler;
import android.os.Message;

/**
 * Created by Shenhua on 1/1/2017.
 * e-mail shenhuanet@126.com
 */
public abstract class SocketCallback extends Handler {

    public static final int SUCCESS = 1;
    public static final int FAILED = 0;

    public abstract void onSuccess(String msg);

    public abstract void onFailed(int errorCode, String msg);

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case SUCCESS:
                // msg.obj是回调返对象
                onSuccess(msg.obj.toString());
                break;
            case FAILED:
                // msg.arg1是errorCode
                onFailed(msg.arg1, msg.obj.toString());
                break;
        }
    }
}
