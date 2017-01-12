package com.shenhua.pc_controller.utils;

import android.os.Handler;
import android.os.Message;

/**
 * Created by Shenhua on 1/1/2017.
 * e-mail shenhuanet@126.com
 */
public abstract class SocketCallback<T> extends Handler {

    public static final int SUCCESS = 1;
    public static final int FAILED = 0;

    public abstract void onSuccess(T msg);

    public abstract void onFailed(int errorCode, String msg);

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case SUCCESS:
                // msg.obj是回调返对象
                onSuccess((T) msg.obj);
                break;
            case FAILED:
                // msg.arg1是errorCode
                onFailed(msg.arg1, msg.obj.toString());
                break;
        }
    }
}
