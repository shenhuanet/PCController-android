package com.shenhua.pc_controller.core;

import android.graphics.Bitmap;

import com.shenhua.pc_controller.utils.SocketCallback;
import com.shenhua.pc_controller.utils.StringUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Shenhua on 1/15/2017.
 * e-mail shenhuanet@126.com
 */
public class SocketImpl {

    private static SocketImpl instance;
    private static ExecutorService service;

    public static SocketImpl getInstance() {
        if (instance == null) instance = new SocketImpl();
        if (service == null) service = Executors.newSingleThreadExecutor();
        return instance;
    }

    public void connect(SocketCallback callback) {
        service.submit(new SocketApi(StringUtils.PORT_NORMAL, StringUtils.ACTION_CONNECT, callback));
    }

    public void disConnect(SocketCallback callback) {
        service.submit(new SocketApi(StringUtils.PORT_NORMAL, StringUtils.ACTION_DIS_CONNECT, callback));
    }

    public void communicate(String action) {
        service.submit(new SocketApi(StringUtils.PORT_NORMAL, action, null));
    }

    public void communicate(String action, SocketCallback callback) {
        service.submit(new SocketApi(StringUtils.PORT_NORMAL, action, callback));
    }

    public void moveCursor(String xy) {
        service.submit(new SocketApi(StringUtils.PORT_CURSOR, xy, null));
    }

    public void readImage(SocketCallback callback) {
        service.submit(new SocketApi(StringUtils.PORT_IMAGE, StringUtils.READ_IMAGE, callback));
    }

    public void sendImage(Bitmap bitmap) {
        if (bitmap != null)
            service.submit(new SocketApi(StringUtils.PORT_IMAGE, bitmap));
    }
}
