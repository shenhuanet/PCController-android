package com.shenhua.pc_controller;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.shenhua.pc_controller.StringUtils.ACTION_CLICK_LEFT;
import static com.shenhua.pc_controller.StringUtils.ACTION_CLICK_RIGHT;
import static com.shenhua.pc_controller.StringUtils.SYSTEM_GET_VOLUME;
import static com.shenhua.pc_controller.StringUtils.SYSTEM_SET_VOLUME;
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
                //System.out.println("shenhua sout:---------->" + "横" + dx + "" + "纵" + dy);
                SocketUtils.getInstance().communicate("横" + dx + "" + "纵" + dy, 119, null);
            }

            @Override
            public void onClick() {
                SocketUtils.getInstance().communicate(ACTION_CLICK_LEFT, null);
            }
        });
        mTouchView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Vibrator vibrator = (Vibrator) getActivity().getSystemService(Service.VIBRATOR_SERVICE);
                vibrator.vibrate(80);
                SocketUtils.getInstance().communicate(ACTION_CLICK_RIGHT, null);
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
                BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
                dialog.setContentView(R.layout.bottomsheet_send_message);
                dialog.show();
                break;
        }
    }

    @OnClick({R.id.btn_up, R.id.btn_down, R.id.btn_left, R.id.btn_right, R.id.btn_enter})
    void orientationClicks(View v) {
        switch (v.getId()) {
            case R.id.btn_up:

                break;
            case R.id.btn_down:

                break;
            case R.id.btn_left:

                break;
            case R.id.btn_right:

                break;
            case R.id.btn_enter:

                break;
        }
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
                SocketUtils.getInstance().communicate(SYSTEM_SHUTDOWN, null);
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
            case R.id.setting:
                showSettingDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSettingDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        dialog.setContentView(R.layout.bottomsheet_setting);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        final AppCompatSeekBar seekBar = (AppCompatSeekBar) window.findViewById(R.id.seek_volume);
        final TextView volumeTv = (TextView) window.findViewById(R.id.tv_volume);
        SocketUtils.getInstance().communicate(SYSTEM_GET_VOLUME, new SocketCallback() {
            @Override
            public void onSuccess(String msg) {
                seekBar.setProgress(Integer.valueOf(msg));
                volumeTv.setText(String.format(getResources().getString(R.string.string_volume), Integer.valueOf(msg)));
            }

            @Override
            public void onFailed(int errorCode, String msg) {
                Toast.makeText(getActivity(), "系统音量获取失败", Toast.LENGTH_SHORT).show();
                volumeTv.setText(String.format(getResources().getString(R.string.string_volume), 10));
                seekBar.setProgress(10);
            }
        });
        dialog.show();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                SocketUtils.getInstance().communicate(SYSTEM_SET_VOLUME + i, null);
                volumeTv.setText(String.format(getResources().getString(R.string.string_volume), Integer.valueOf(i)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
