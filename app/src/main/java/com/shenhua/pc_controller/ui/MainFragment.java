package com.shenhua.pc_controller.ui;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
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
import com.shenhua.pc_controller.utils.SocketCallback;
import com.shenhua.pc_controller.utils.SocketUtils;
import com.shenhua.pc_controller.widget.SendMessageDialog;
import com.shenhua.pc_controller.widget.SettingDialog;
import com.shenhua.pc_controller.widget.TouchView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.shenhua.pc_controller.utils.StringUtils.ACTION_CLICK_LEFT;
import static com.shenhua.pc_controller.utils.StringUtils.ACTION_CLICK_RIGHT;
import static com.shenhua.pc_controller.utils.StringUtils.SYSTEM_SHUTDOWN;

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
                new SendMessageDialog(getActivity(), R.layout.bottomsheet_send_message).show();
                break;
        }
    }

    @OnClick({R.id.btn_up, R.id.btn_down, R.id.btn_left, R.id.btn_right, R.id.btn_enter})
    void orientationClicks(View v) {
        switch (v.getId()) {
            case R.id.btn_up:
//                BaseAlertDialog dialog = new BaseAlertDialog(getContext()).show();

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
                new SettingDialog(getActivity(), R.layout.bottomsheet_setting).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
