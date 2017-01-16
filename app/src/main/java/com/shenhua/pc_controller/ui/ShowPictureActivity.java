package com.shenhua.pc_controller.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.shenhua.pc_controller.R;

import java.io.File;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnLongClick;
import uk.co.senab.photoview.PhotoView;

import static com.shenhua.pc_controller.utils.FileUtils.DIR_NAME;
import static com.shenhua.pc_controller.utils.FileUtils.saveBitmapToSDCard;

/**
 * Created by shenhua on 1/16/2017.
 * Email shenhuanet@126.com
 */
public class ShowPictureActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.photoView)
    PhotoView mPhotoView;
    Bitmap bitmap;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
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

    @OnLongClick(R.id.photoView)
    void longClick(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(getResources().getStringArray(R.array.picshow_items), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, final int i) {
                if (i == 0) { // 分享
                    ProgressDialog dialog = new ProgressDialog(ShowPictureActivity.this);
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
                                        Intent intent = new Intent(Intent.ACTION_SEND);
                                        intent.setType("image/*");
                                        Uri uri = Uri.fromFile(new File(path));
                                        intent.putExtra(Intent.EXTRA_STREAM, uri);
                                        startActivity(Intent.createChooser(intent, "选择一个要分享到的应用"));
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } else { // 保存
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
                }
            }
        });
    }

    private void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ShowPictureActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

}