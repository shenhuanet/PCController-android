package com.shenhua.pc_controller;

import android.app.Application;

/**
 * Created by Shenhua on 1/3/2017.
 * e-mail shenhuanet@126.com
 */
public class App extends Application {

    private String host;
    private boolean isConnect = false;
    public static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static App getInstance() {
        return instance;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public boolean isConnect() {
        return isConnect;
    }

    public void setConnect(boolean connect) {
        isConnect = connect;
    }
}
