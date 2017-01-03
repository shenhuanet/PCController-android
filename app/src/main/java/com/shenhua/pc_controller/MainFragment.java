package com.shenhua.pc_controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.shenhua.pc_controller.StringUtils.ACTION_CLICK_LEFT;
import static com.shenhua.pc_controller.StringUtils.ACTION_CLICK_RIGHT;
import static com.shenhua.pc_controller.StringUtils.SYSTEM_SHUTDOWN;

/**
 * Created by Shenhua on 1/1/2017.
 * e-mail shenhuanet@126.com
 */
public class MainFragment extends Fragment {

    private View rootView;
    @BindView(R.id.touchView)
    TouchView mTouchView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.frag_main, container, false);
            ButterKnife.bind(this, rootView);
            setHasOptionsMenu(true);
        }
        ViewGroup group = (ViewGroup) rootView.getParent();
        if (group != null)
            group.removeView(rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTouchView.setOnViewTouchListener(new TouchView.OnViewTouchListener() {
            @Override
            public void onMove(int dx, int dy) {
//                port=119
            }

            @Override
            public void onClick() {
                SocketUtils.getInstance().communicate(ACTION_CLICK_LEFT);
            }
        });
        mTouchView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                SocketUtils.getInstance().communicate(ACTION_CLICK_RIGHT);
                // 长按时震动
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_system, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.system_menu_item_lock:
                // SocketUtils.getInstance().communicate("");
                break;
            case R.id.system_menu_item_shutdown:
                SocketUtils.getInstance().communicate(SYSTEM_SHUTDOWN);
                break;
            case R.id.system_menu_item_restart:
                // SocketUtils.getInstance().communicate("");
                break;
            case R.id.system_menu_item_logout:
                // SocketUtils.getInstance().communicate("");
                break;
            case R.id.system_menu_item_sleep:
                // SocketUtils.getInstance().communicate("");
                break;
            case R.id.system_menu_item_awake:
                // SocketUtils.getInstance().communicate("");
                break;
            case R.id.system_menu_item_close_display:
                // SocketUtils.getInstance().communicate("");
                break;
            case R.id.system_menu_item_screen_savers:
                // SocketUtils.getInstance().communicate("");
                break;
            case R.id.system_menu_item_get_tasks:
                getActivity().startActivity(new Intent(getActivity(), SystemTasksActivity.class));
                break;
            case R.id.system_menu_item_dis_connect:
                App app = (App) getActivity().getApplication();
                String host = app.getHost();
                SocketUtils.getInstance().connect(host, 118, false, new SocketCallback() {
                    @Override
                    public void onSuccess(String msg) {
                        Toast.makeText(getActivity(), "已断开连接", Toast.LENGTH_SHORT).show();
                        ((MainActivity) getActivity()).replace(new SplashFragment());
                    }

                    @Override
                    public void onFailed(int errorCode, String msg) {
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
