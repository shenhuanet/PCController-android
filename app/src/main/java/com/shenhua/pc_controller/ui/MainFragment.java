package com.shenhua.pc_controller.ui;

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

import com.shenhua.pc_controller.App;
import com.shenhua.pc_controller.R;
import com.shenhua.pc_controller.base.BaseAlertDialog;
import com.shenhua.pc_controller.utils.SocketCallback;
import com.shenhua.pc_controller.utils.SocketUtils;
import com.shenhua.pc_controller.widget.SendMessageDialog;
import com.shenhua.pc_controller.widget.SettingDialog;
import com.shenhua.pc_controller.widget.TouchView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.shenhua.pc_controller.utils.StringUtils.ACTION_CLICK_LEFT;
import static com.shenhua.pc_controller.utils.StringUtils.ACTION_DOWN;
import static com.shenhua.pc_controller.utils.StringUtils.ACTION_ENTER;
import static com.shenhua.pc_controller.utils.StringUtils.ACTION_LEFT;
import static com.shenhua.pc_controller.utils.StringUtils.ACTION_RIGHT;
import static com.shenhua.pc_controller.utils.StringUtils.ACTION_UP;
import static com.shenhua.pc_controller.utils.StringUtils.SYSTEM_AWAKE;
import static com.shenhua.pc_controller.utils.StringUtils.SYSTEM_CLOSE_DISPLAY;
import static com.shenhua.pc_controller.utils.StringUtils.SYSTEM_CLOSE_SCREEN;
import static com.shenhua.pc_controller.utils.StringUtils.SYSTEM_LOCK;
import static com.shenhua.pc_controller.utils.StringUtils.SYSTEM_LOGOUT;
import static com.shenhua.pc_controller.utils.StringUtils.SYSTEM_RESTART;
import static com.shenhua.pc_controller.utils.StringUtils.SYSTEM_SCREEN_SAVERS;
import static com.shenhua.pc_controller.utils.StringUtils.SYSTEM_SHUTDOWN;
import static com.shenhua.pc_controller.utils.StringUtils.SYSTEM_SLEEP;

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
                //System.out.println("shenhua sout:---------->" + "横" + dx + "" + "纵" + dy);
                SocketUtils.getInstance().communicate("横" + dx * 0.1 + "" + "纵" + dy * 0.1, 119, null);
            }

            @Override
            public void onClick() {
                SocketUtils.getInstance().communicate(ACTION_CLICK_LEFT, null);
            }
        });
        mTouchView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //Vibrator vibrator = (Vibrator) getActivity().getSystemService(Service.VIBRATOR_SERVICE);
                //vibrator.vibrate(80);
                //SocketUtils.getInstance().communicate(ACTION_CLICK_RIGHT, null);
                return true;
            }
        });
    }

    @OnClick({R.id.btn_copy, R.id.btn_file, R.id.btn_esc, R.id.btn_send})
    void clicks(View v) {
        switch (v.getId()) {
            case R.id.btn_copy:

                break;
            case R.id.btn_file:
                ((MainActivity) getActivity()).showToast("功能未实现");
                break;
            case R.id.btn_esc:

                break;
            case R.id.btn_send:
                new SendMessageDialog(getActivity(), R.layout.bottomsheet_send_message).show();
                break;
        }
    }

    @OnClick({R.id.btn_up, R.id.btn_down, R.id.btn_left, R.id.btn_right, R.id.btn_enter})
    void orientationClicks(View v) {
        switch (v.getId()) {
            case R.id.btn_up:
                SocketUtils.getInstance().communicate(ACTION_UP, null);
                break;
            case R.id.btn_down:
                SocketUtils.getInstance().communicate(ACTION_DOWN, null);
                break;
            case R.id.btn_left:
                SocketUtils.getInstance().communicate(ACTION_LEFT, null);
                break;
            case R.id.btn_right:
                SocketUtils.getInstance().communicate(ACTION_RIGHT, null);
                break;
            case R.id.btn_enter:
                SocketUtils.getInstance().communicate(ACTION_ENTER, null);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        App app = (App) getActivity().getApplication();
        if (app.isConnect())
            Toast.makeText(getActivity(), (getResources().getString(R.string.app_name) + "正在后台运行"), Toast.LENGTH_SHORT).show();
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
                new BaseAlertDialog(getContext(), "确定要锁定计算机吗？") {
                    @Override
                    public void onPositiveButtonClick() {
                        SocketUtils.getInstance().communicate(SYSTEM_LOCK, null);
                    }
                }.show();
                break;
            case R.id.system_menu_item_shutdown:
                new BaseAlertDialog(getContext(), "确定要关闭计算机吗？") {
                    @Override
                    public void onPositiveButtonClick() {
                        SocketUtils.getInstance().communicate(SYSTEM_SHUTDOWN, new SocketCallback() {
                            @Override
                            public void onSuccess(String msg) {
                                if (msg.equals("already shutdown")) {
                                    ((MainActivity) getActivity()).replace(new SplashFragment());
                                }
                            }

                            @Override
                            public void onFailed(int errorCode, String msg) {

                            }
                        });
                    }
                }.show();
                break;
            case R.id.system_menu_item_restart:
                new BaseAlertDialog(getContext(), "确定要重启计算机吗？") {
                    @Override
                    public void onPositiveButtonClick() {
                        SocketUtils.getInstance().communicate(SYSTEM_RESTART, new SocketCallback() {
                            @Override
                            public void onSuccess(String msg) {
                                if (msg.equals("already restart")) {
                                    ((MainActivity) getActivity()).replace(new SplashFragment());
                                }
                            }

                            @Override
                            public void onFailed(int errorCode, String msg) {

                            }
                        });
                    }
                }.show();
                break;
            case R.id.system_menu_item_logout:
                new BaseAlertDialog(getContext(), "确定要注销当前用户吗？") {
                    @Override
                    public void onPositiveButtonClick() {
                        SocketUtils.getInstance().communicate(SYSTEM_LOGOUT, new SocketCallback() {
                            @Override
                            public void onSuccess(String msg) {
                                if (msg.equals("already logout")) {
                                    ((MainActivity) getActivity()).replace(new SplashFragment());
                                }
                            }

                            @Override
                            public void onFailed(int errorCode, String msg) {

                            }
                        });
                    }
                }.show();
                break;
            case R.id.system_menu_item_sleep:
                new BaseAlertDialog(getContext(), "确定要休眠计算机吗？") {
                    @Override
                    public void onPositiveButtonClick() {
                        SocketUtils.getInstance().communicate(SYSTEM_SLEEP, new SocketCallback() {
                            @Override
                            public void onSuccess(String msg) {
                                if (msg.equals("already sleep")) {
                                    ((MainActivity) getActivity()).replace(new SplashFragment());
                                }
                            }

                            @Override
                            public void onFailed(int errorCode, String msg) {

                            }
                        });
                    }
                }.show();
                break;
            case R.id.system_menu_item_awake:
                SocketUtils.getInstance().communicate(SYSTEM_AWAKE, null);
                break;
            case R.id.system_menu_item_close_screen:
                SocketUtils.getInstance().communicate(SYSTEM_CLOSE_SCREEN, null);
                break;
            case R.id.system_menu_item_close_display:
                SocketUtils.getInstance().communicate(SYSTEM_CLOSE_DISPLAY, null);
                break;
            case R.id.system_menu_item_screen_savers:
                SocketUtils.getInstance().communicate(SYSTEM_SCREEN_SAVERS, null);
                break;
            case R.id.system_menu_item_get_tasks:
                getActivity().startActivity(new Intent(getActivity(), SystemTasksActivity.class));
                break;
            case R.id.system_menu_item_dis_connect:
                final App app = (App) getActivity().getApplication();
                String host = app.getHost();
                SocketUtils.getInstance().connect(host, 118, false, new SocketCallback() {
                    @Override
                    public void onSuccess(String msg) {
                        Toast.makeText(getActivity(), "已断开连接", Toast.LENGTH_SHORT).show();
                        ((MainActivity) getActivity()).replace(new SplashFragment());
                        app.setConnect(false);
                    }

                    @Override
                    public void onFailed(int errorCode, String msg) {
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.setting:
                new SettingDialog(getActivity(), R.layout.bottomsheet_setting).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
