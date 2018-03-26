package com.besthings.pdm.activity;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.besthings.pdm.R;
import com.besthings.pdm.utils.NativeImageUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MyDesignCaptureActivity extends BaseActivity {
    private static final String TAG = MyDesignCaptureActivity.class.getSimpleName();
    //请求缩略图信号标识
    private static int REQUEST_THUMBNAIL = 1;
    //请求原图信号标识
    private static int REQUEST_ORIGINAL = 2;
    //请求照相机权限标识
    private static int TAKE_PHOTO_REQUEST_CODE = 0;
    ImageButton mCapture;
    //SD卡的路径
    private String mSDPath;
    //图片存储路径
    private String mPicPath;
    private String cPicPath;
    Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_design_capture);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, TAKE_PHOTO_REQUEST_CODE);
            }
        }

        mSDPath = Environment.getExternalStorageDirectory().getPath() + "/" + System.currentTimeMillis();
        mPicPath = "";

        getSupportActionBar().hide();

        Button btnOK = (Button) findViewById(R.id.my_design_capture_btnOK);
        Button btnCancel = (Button) findViewById(R.id.my_design_capture_btnCancel);
        mCapture = (ImageButton) findViewById(R.id.my_design_capture_ibCapture);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBitmap == null) {
                    showAlert("请先进行拍照！");
                    return;
                }
                Intent it = new Intent(MyDesignCaptureActivity.this, MyDesignCaptureEditActivity.class);
                it.putExtra("capture", cPicPath);
                startActivity(it);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDesignCaptureActivity.this.finish();
            }
        });

        mCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
            }
        });
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mPicPath = mSDPath + "temp.jpg";
        cPicPath = mSDPath + "temp_c.jpg";
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mPicPath)));
        startActivityForResult(intent, REQUEST_ORIGINAL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_THUMBNAIL) {//对应第一种方法
                /**
                 * 通过这种方法取出的拍摄会默认压缩，因为如果相机的像素比较高拍摄出来的图会比较高清，
                 * 如果图太大会造成内存溢出（OOM），因此此种方法会默认给图片尽心压缩
                 */
                Bundle bundle = data.getExtras();
                mBitmap = (Bitmap) bundle.get("data");
                mCapture.setImageBitmap(mBitmap);
                mCapture.setScaleType(ImageView.ScaleType.FIT_CENTER);
            } else if (requestCode == REQUEST_ORIGINAL) {//对应第二种方法
                /**
                 * 这种方法是通过内存卡的路径进行读取图片，所以的到的图片是拍摄的原图
                 */
                FileInputStream fis = null;
                try {
                    //调用libJpeg终极压缩
                    NativeImageUtil.compressBitmap(mPicPath, cPicPath);
                    //替换路径
                    mPicPath = cPicPath;
                    //把图片转化为字节流
                    fis = new FileInputStream(mPicPath);
                    //把流转化图片
                    mBitmap = BitmapFactory.decodeStream(fis);
                    mCapture.setImageBitmap(mBitmap);
                    mCapture.setScaleType(ImageView.ScaleType.FIT_CENTER);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fis.close();//关闭流
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
        }
        return true;
    }
}
