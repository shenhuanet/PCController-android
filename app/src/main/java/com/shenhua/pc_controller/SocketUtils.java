package com.shenhua.pc_controller;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.shenhua.pc_controller.SocketCallback.FAILED;
import static com.shenhua.pc_controller.SocketCallback.SUCCESS;
import static com.shenhua.pc_controller.StringUtils.ACTION_CONNECT;
import static com.shenhua.pc_controller.StringUtils.ACTION_DIS_CONNECT;

/**
 * Created by Shenhua on 1/1/2017.
 * e-mail shenhuanet@126.com
 */
public class SocketUtils {

    private static SocketUtils instance;
    private static final int CONNECT_TIMEOUT = 5000;
    private static final int COMMUNICATE_TIMEOUT = 6000;
    private String host;
    private int port;

    // 所有异步任务共用一个线程池，避免资源浪费，如若遇到多线程并发任务，需重新指派线程
    private static ExecutorService service;

    public static SocketUtils getInstance() {
        if (instance == null) {
            instance = new SocketUtils();
        }
        if (service == null) {
            service = Executors.newSingleThreadExecutor();
        }
        return instance;
    }

    /**
     * 连接或者断开连接
     *
     * @param host    host
     * @param port    port
     * @param connect true is connect,false is disconnect.
     * @return str
     */
    public void connect(final String host, final int port, final boolean connect, final SocketCallback callback) {
        if (TextUtils.isEmpty(host) || port <= 0)
            callback.obtainMessage(FAILED, "Please check the host and port is right!").sendToTarget();
        this.host = host;
        this.port = port;
        send(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket(host, port);
                    socket.setSoTimeout(CONNECT_TIMEOUT);
                    BufferedReader bufferedReader = getBufferedReader(socket);
                    PrintWriter printWriter = getPrintWriter(socket);
                    printWriter.println(connect ? ACTION_CONNECT : ACTION_DIS_CONNECT);
                    String response = "";
                    for (; ; ) {
                        String str2 = bufferedReader.readLine();
                        if ((str2 == null)) break;
                        response = response + str2;
                    }
                    socket.close();
                    bufferedReader.close();
                    printWriter.close();
                    if (callback == null) return;
                    callback.obtainMessage(SUCCESS, response).sendToTarget();
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                    if (callback == null) return;
                    callback.obtainMessage(FAILED, (connect ? "Connect" : "Disconnect") + " Time out.")
                            .sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
                    if (callback == null) return;
                    callback.obtainMessage(FAILED, "An error in the " + (connect ? "connect" : "disconnect"))
                            .sendToTarget();
                }
            }
        });
    }

    public void communicate(final String action, final SocketCallback callback) {
        send(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket(host, port);
                    socket.setSoTimeout(COMMUNICATE_TIMEOUT);
                    BufferedReader bufferedReader = getBufferedReader(socket);
                    PrintWriter printWriter = getPrintWriter(socket);
                    printWriter.println(action);
                    String response = "";
                    for (; ; ) {
                        String str2 = bufferedReader.readLine();
                        if ((str2 == null)) break;
                        response = response + str2;
                    }
                    socket.close();
                    bufferedReader.close();
                    printWriter.close();
                    if (callback != null) {
                        callback.obtainMessage(SUCCESS, response).sendToTarget();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    if (callback != null) callback.obtainMessage(FAILED, "error").sendToTarget();
                }
            }
        });
    }

    public void communicate(final String action, final int port, final SocketCallback callback) {
        send(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket(host, port);
                    socket.setSoTimeout(COMMUNICATE_TIMEOUT);
                    BufferedReader bufferedReader = getBufferedReader(socket);
                    PrintWriter printWriter = getPrintWriter(socket);
                    printWriter.println(action);
                    String response = "";
                    for (; ; ) {
                        String str2 = bufferedReader.readLine();
                        if ((str2 == null)) break;
                        response = response + str2;
                    }
                    socket.close();
                    bufferedReader.close();
                    printWriter.close();
                    if (callback != null) {
                        callback.obtainMessage(SUCCESS, response).sendToTarget();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    if (callback != null) callback.obtainMessage(FAILED, "error").sendToTarget();
                }
            }
        });
    }

    private void send(Runnable runnable) {
        if (runnable != null) {
            service.submit(runnable);
        }
    }

    private BufferedReader getBufferedReader(Socket socket) throws IOException {
        return new BufferedReader(
                new InputStreamReader(socket.getInputStream(), "utf-8"));
    }

    private PrintWriter getPrintWriter(Socket socket) throws IOException {
        return new PrintWriter(
                new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream(), "UTF-8")), true);
    }

}
