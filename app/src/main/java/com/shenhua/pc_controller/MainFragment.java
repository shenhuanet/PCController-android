package com.shenhua.pc_controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

import static com.shenhua.pc_controller.StringUtils.SYSTEM_SHUTDOWN;

/**
 * Created by Shenhua on 1/1/2017.
 * e-mail shenhuanet@126.com
 */
public class MainFragment extends Fragment {

    private View rootView;

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
            case R.id.system_menu_item_shutdown:
                SocketUtils.getInstance().communicate(SYSTEM_SHUTDOWN);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
