package com.shenhua.pc_controller.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.shenhua.pc_controller.App;
import com.shenhua.pc_controller.R;
import com.shenhua.pc_controller.base.BaseAlertDialog;
import com.shenhua.pc_controller.core.SocketImpl;
import com.shenhua.pc_controller.utils.SocketCallback;
import com.shenhua.pc_controller.widget.SendMessageDialog;
import com.shenhua.pc_controller.widget.SettingDialog;
import com.shenhua.pc_controller.widget.TouchView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.shenhua.pc_controller.utils.StringUtils.ACTION_CLICK_LEFT;
import static com.shenhua.pc_controller.utils.StringUtils.ACTION_DOWN;
import static com.shenhua.pc_controller.utils.StringUtils.ACTION_ENTER;
import static com.shenhua.pc_controller.utils.StringUtils.ACTION_ESC;
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
    @BindView(R.id.img_bg)
    ImageView imageView;

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
                SocketImpl.getInstance().moveCursor("横" + dx * 0.1 + "" + "纵" + dy * 0.1);
            }

            @Override
            public void onClick() {
                SocketImpl.getInstance().communicate(ACTION_CLICK_LEFT);
            }
        });
        mTouchView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // TODO: 1/15/2017 view 触摸过程中导致长按事件
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
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setItems(getResources().getStringArray(R.array.pic_items), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            getImage();
                        } else if (i == 1) {
                            setImage();
                        }
                    }

                    // 发送图片
                    private void setImage() {
                        // TODO: 1/15/2017 从相册取出图片，转为bitmap图片发送 startActivityForResult
                        // SocketImpl.getInstance().sendImage(BitmapFactory.decodeResource(getResources(), R.drawable.ic_send));
                    }

                    // 取出图片
                    private void getImage() {
                        final ProgressDialog dialog = new ProgressDialog(getContext());
                        dialog.setMessage("图片加载中");
                        dialog.show();
                        SocketImpl.getInstance().readImage(new SocketCallback() {
                            @Override
                            public void onSuccess(Object msg) {
                                dialog.dismiss();
                                startActivity(new Intent(getActivity(), ShowPictureActivity.class)
                                        .putExtra("img", (Bitmap) msg));
                            }

                            @Override
                            public void onFailed(int errorCode, String msg) {
                                dialog.dismiss();
                                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                break;
            case R.id.btn_file:
                // TODO: 1/16/2017 打开和关闭FTP功能
                break;
            case R.id.btn_esc:
                SocketImpl.getInstance().communicate(ACTION_ESC);
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
                SocketImpl.getInstance().communicate(ACTION_UP);
                break;
            case R.id.btn_down:
                SocketImpl.getInstance().communicate(ACTION_DOWN);
                break;
            case R.id.btn_left:
                SocketImpl.getInstance().communicate(ACTION_LEFT);
                break;
            case R.id.btn_right:
                SocketImpl.getInstance().communicate(ACTION_RIGHT);
                break;
            case R.id.btn_enter:
                SocketImpl.getInstance().communicate(ACTION_ENTER);
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
                        SocketImpl.getInstance().communicate(SYSTEM_LOCK);
                    }
                }.show();
                break;
            case R.id.system_menu_item_shutdown:
                new BaseAlertDialog(getContext(), "确定要关闭计算机吗？") {
                    @Override
                    public void onPositiveButtonClick() {
                        SocketImpl.getInstance().communicate(SYSTEM_SHUTDOWN, new SocketCallback() {
                            @Override
                            public void onSuccess(Object msg) {
                                String s = new String((byte[]) msg);
                                if (s.equals("already shutdown")) {
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
                        SocketImpl.getInstance().communicate(SYSTEM_RESTART, new SocketCallback() {
                            @Override
                            public void onSuccess(Object msg) {
                                String s = new String((byte[]) msg);
                                if (s.equals("already restart")) {
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
                        SocketImpl.getInstance().communicate(SYSTEM_LOGOUT, new SocketCallback() {
                            @Override
                            public void onSuccess(Object msg) {
                                String s = new String((byte[]) msg);
                                if (s.equals("already logout")) {
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
                        SocketImpl.getInstance().communicate(SYSTEM_SLEEP, new SocketCallback() {
                            @Override
                            public void onSuccess(Object msg) {
                                String s = new String((byte[]) msg);
                                if (s.equals("already sleep")) {
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
                SocketImpl.getInstance().communicate(SYSTEM_AWAKE);
                break;
            case R.id.system_menu_item_close_screen:
                SocketImpl.getInstance().communicate(SYSTEM_CLOSE_SCREEN);
                break;
            case R.id.system_menu_item_close_display:
                SocketImpl.getInstance().communicate(SYSTEM_CLOSE_DISPLAY);
                break;
            case R.id.system_menu_item_screen_savers:
                SocketImpl.getInstance().communicate(SYSTEM_SCREEN_SAVERS);
                break;
            case R.id.system_menu_item_get_tasks:
                getActivity().startActivity(new Intent(getActivity(), SystemTasksActivity.class));
                break;
            case R.id.system_menu_item_dis_connect:
                SocketImpl.getInstance().disConnect(new SocketCallback() {
                    @Override
                    public void onSuccess(Object msg) {
                        Toast.makeText(getActivity(), "已断开连接", Toast.LENGTH_SHORT).show();
                        ((MainActivity) getActivity()).replace(new SplashFragment());
                        App app = (App) getActivity().getApplication();
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
