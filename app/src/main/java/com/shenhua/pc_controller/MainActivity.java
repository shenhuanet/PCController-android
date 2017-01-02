package com.shenhua.pc_controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private String ip1 = "192.168.2.108";
    private String ip2 = "192.168.137.1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        replace(new SplashFragment());

    }

    public void replace(Fragment fragment){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_main, fragment)
                .commit();
    }

    public void ipConnect(View view) {
        System.out.println("shenhua sout:" + "开始连接");
        SocketUtils.getInstance().connect(ip2, 118, true, new SocketCallback() {
            @Override
            public void onSuccess(String msg) {
                showToast(msg);
            }

            @Override
            public void onFailed(int errorCode, String msg) {
                showToast(msg);
            }
        });
    }

    public void disConnect(View view) {
        System.out.println("shenhua sout:" + "开始断开连接");
        SocketUtils.getInstance().connect(ip2, 118, false, new SocketCallback() {
            @Override
            public void onSuccess(String str) {
                showToast(str);
            }

            @Override
            public void onFailed(int errorCode, String msg) {
                showToast(msg);
            }
        });
    }

    public void shanchu(View view) {
        SocketUtils.getInstance().communicate("#退格键");
    }

    private void showToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}
