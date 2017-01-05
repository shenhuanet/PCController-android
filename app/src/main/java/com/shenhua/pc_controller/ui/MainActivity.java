package com.shenhua.pc_controller.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import com.shenhua.pc_controller.App;
import com.shenhua.pc_controller.R;
import com.shenhua.pc_controller.base.BaseActivity;
import com.shenhua.pc_controller.utils.SocketUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupActionBar(toolbar, false);

//        replace(new SplashFragment());
        replace(new MainFragment());
    }

    public void replace(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.right_in, R.anim.right_out)
                .replace(R.id.activity_main, fragment)
                .commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        showToast(getResources().getString(R.string.app_name) + "正在后台运行");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SocketUtils.getInstance().connect(((App) getApplication()).getHost(), 118, false, null);
    }
}
