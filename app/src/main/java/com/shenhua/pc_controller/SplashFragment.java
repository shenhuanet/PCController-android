package com.shenhua.pc_controller;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Shenhua on 1/1/2017.
 * e-mail shenhuanet@126.com
 */
public class SplashFragment extends Fragment {

    private View rootView;
    @BindView(R.id.btn_connect)
    Button mConnectBtn;
    @BindView(R.id.editText_ip)
    TextInputEditText mEditText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.frag_splash, container, false);
            ButterKnife.bind(this, rootView);
        }
        ViewGroup group = (ViewGroup) rootView.getParent();
        if (group != null)
            group.removeView(rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final String ip = mEditText.getText().toString();
        mConnectBtn.setEnabled(ip.length() != 0);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mEditText.getText().toString().length() != 0) {
                    mConnectBtn.setEnabled(true);
                } else {
                    mConnectBtn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @OnClick(R.id.btn_connect)
    void click(View view) {
        App app = (App) getActivity().getApplication();
        app.setHost(mEditText.getText().toString());
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("正在连接...");
        dialog.show();
        SocketUtils.getInstance().connect(app.getHost(), 118, true, new SocketCallback() {
            @Override
            public void onSuccess(String msg) {
                dialog.dismiss();
                Toast.makeText(getActivity(), "连接成功", Toast.LENGTH_SHORT).show();
                ((MainActivity) getActivity()).replace(new MainFragment());
            }

            @Override
            public void onFailed(int errorCode, String msg) {
                dialog.dismiss();
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
