package com.shenhua.pc_controller.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.shenhua.pc_controller.R;
import com.shenhua.pc_controller.base.BaseActivity;

import java.io.File;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoView;

import static com.shenhua.pc_controller.utils.FileUtils.DIR_NAME;
import static com.shenhua.pc_controller.utils.FileUtils.saveBitmapToSDCard;

/**
 * Created by shenhua on 1/16/2017.
 * Email shenhuanet@126.com
 */
public class ShowPictureActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.photoView)
    PhotoView mPhotoView;
    private Bitmap bitmap;
    private String fileName;// TODO: 1/16/2017   如果图片是同一张，那么名字就不要重复了

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showpic);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle("图片");
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setDisplayShowHomeEnabled(true);
        }
        bitmap = getIntent().getParcelableExtra("img");
        mPhotoView.setImageBitmap(bitmap);
    }

    @OnClick({R.id.iv_save, R.id.iv_share})
    void click(View view) {
        switch (view.getId()) {
            case R.id.iv_save:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            saveBitmapToSDCard(ShowPictureActivity.this, bitmap, String.valueOf(new Date().getTime()) + ".png", DIR_NAME, true);
                            showToast("保存成功");
                        } catch (Exception e) {
                            e.printStackTrace();
                            showToast("保存失败");
                        }
                    }
                }).start();
                break;
            case R.id.iv_share:
                final ProgressDialog dialog = new ProgressDialog(ShowPictureActivity.this);
                dialog.setMessage("正在创建分享");
                dialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final String path = saveBitmapToSDCard(ShowPictureActivity.this, bitmap, String.valueOf(new Date().getTime()), DIR_NAME, false);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.dismiss();
                                    Intent intent = new Intent(Intent.ACTION_SEND);
                                    intent.setType("image/*");
                                    Uri uri = Uri.fromFile(new File(path));
                                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                                    startActivity(Intent.createChooser(intent, "选择一个要分享到的应用"));
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.dismiss();
                                    showToast("分享失败");
                                }
                            });
                        }
                    }
                }).start();
                break;
        }
    }

}