package com.shenhua.pc_controller;

import android.app.Application;

/**
 * Created by Shenhua on 1/3/2017.
 * e-mail shenhuanet@126.com
 */
public class App extends Application {

    private String host;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
