package com.besthings.pdm.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.besthings.pdm.R;
import com.besthings.pdm.utils.ImageUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.hzw.graffiti.GraffitiActivity;
import cn.hzw.graffiti.GraffitiParams;
import cn.hzw.graffiti.imagepicker.ImageLoader;

public class DoodleActivity extends BaseActivity {

    private static final int REQ_CODE_GRAFFITI = 101;
    private static final String IMAGE_URL = "http://www.buzztech.mo/download/images/wechat.png";
    private static final String DOODLE_TEMP_PATH = Environment.getExternalStorageDirectory().getPath() + "/" + "temp.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doodle);

        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, REQ_CODE_GRAFFITI);
            }
        }

        Button btnDoodle = (Button) findViewById(R.id.doodle_btnDoodle);
        btnDoodle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Bitmap bmp = ImageUtil.getURLImage(IMAGE_URL);
                    File file = new File(DOODLE_TEMP_PATH);
                    FileOutputStream fos = new FileOutputStream(file);
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                // 涂鸦参数
                GraffitiParams params = new GraffitiParams();
                // 图片路径
                params.mImagePath = DOODLE_TEMP_PATH;

                params.mPaintSize = 20;

                GraffitiActivity.startActivityForResult(DoodleActivity.this, params, REQ_CODE_GRAFFITI);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_GRAFFITI) {
            if (data == null) {
                return;
            }
            if (resultCode == GraffitiActivity.RESULT_OK) {
                String path = data.getStringExtra(GraffitiActivity.KEY_IMAGE_PATH);
                if (TextUtils.isEmpty(path)) {
                    return;
                }
                ImageLoader.getInstance(this).display(findViewById(R.id.doodle_ivDoodleResult), path);
                ((EditText) findViewById(R.id.et_base64)).setText(ImageUtil.bitmapToBase64(BitmapFactory.decodeFile(path)));
                //Toast.makeText(getApplicationContext(), ImageUtil.bitmapToBase64(BitmapFactory.decodeFile(path)), Toast.LENGTH_LONG).show();
            } else if (resultCode == GraffitiActivity.RESULT_ERROR) {
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
