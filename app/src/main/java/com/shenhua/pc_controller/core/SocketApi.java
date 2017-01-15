package com.shenhua.pc_controller.core;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.shenhua.pc_controller.App;
import com.shenhua.pc_controller.utils.SocketCallback;
import com.shenhua.pc_controller.utils.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Created by Shenhua on 1/15/2017.
 * e-mail shenhuanet@126.com
 */
class SocketApi implements Runnable {

    private String host;
    private int port;
    private static final int CONNECT_TIMEOUT = 30 * 1000;
    private SocketCallback callback;
    private String action;
    private Bitmap sendBitmap;

    SocketApi(int port, String action, SocketCallback callback) {
        this.host = App.getInstance().getHost();
        this.port = port;
        this.action = action;
        this.callback = callback;
    }

    SocketApi(int port, Bitmap bitmap) {
        this.host = App.getInstance().getHost();
        this.port = port;
        this.action = "";
        this.sendBitmap = bitmap;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(host, port);
            socket.setKeepAlive(true);
            socket.setSoTimeout(CONNECT_TIMEOUT);
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            switch (port) {
                case StringUtils.PORT_NORMAL:
                    doCommunicate(inputStream, outputStream);
                    break;
                case StringUtils.PORT_CURSOR:
                    doMoveCursor(inputStream, outputStream);
                    break;
                case StringUtils.PORT_SENSOR:
                    // doMoveSensor();
                    break;
                case StringUtils.PORT_IMAGE:
                    doTransferImage(inputStream, outputStream);
                    break;
                case StringUtils.PORT_FILE:
                    doTransferFile();
                    break;
            }
            socket.close();
            inputStream.close();
            outputStream.close();
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            if (callback != null)
                callback.obtainMessage(SocketCallback.FAILED, "Socket Timeout.").sendToTarget();
        } catch (IOException e) {
            e.printStackTrace();
            if (callback != null)
                callback.obtainMessage(SocketCallback.FAILED, "An error with the IOException").sendToTarget();
        }
    }

    // 文件传输
    private void doTransferFile() {

    }

    // 图片传输
    private void doTransferImage(InputStream inputStream, OutputStream outputStream) throws IOException {
        if (TextUtils.isEmpty(action)) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(outputStream);
            sendBitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            dos.write(bos.toByteArray());
            bos.close();
            dos.close();
            return;
        }
        outputStream.write(action.getBytes("utf-8"));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int byteRead;
        byte[] buffer = new byte[1024];
        while ((byteRead = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, byteRead);
        }
        byte[] b = bos.toByteArray();
        if (b.length == 0) {
            if (callback != null)
                callback.obtainMessage(SocketCallback.FAILED, "PC端剪切板中没有图片").sendToTarget();
            return;
        }
        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        if (callback != null)
            callback.obtainMessage(SocketCallback.SUCCESS, bitmap).sendToTarget();
        bos.close();
    }

    // 控制鼠标移动
    private void doMoveCursor(InputStream inputStream, OutputStream outputStream) throws IOException {
        doCommunicate(inputStream, outputStream);
    }

    // 信息通讯
    private void doCommunicate(InputStream inputStream, OutputStream outputStream) throws IOException {
        outputStream.write(action.getBytes("utf-8"));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int byteRead;
        byte[] buffer = new byte[1024];
        while ((byteRead = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, byteRead);
        }
        byte[] b = bos.toByteArray();
        if (callback != null)
            callback.obtainMessage(SocketCallback.SUCCESS, b).sendToTarget();
        bos.close();
    }
}
