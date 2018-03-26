package com.besthings.pdm.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import com.besthings.bean.BaseRet;
import com.besthings.pdm.R;
import com.besthings.pdm.utils.ACache;
import com.besthings.pdm.utils.ImageUtil;
import com.besthings.pdm.utils.NativeImageUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import cn.hzw.graffiti.GraffitiActivity;
import cn.hzw.graffiti.GraffitiParams;
import cn.hzw.graffiti.imagepicker.ImageLoader;

public class BuyerSampleNotesActivity extends BaseActivity {
    private static final String TAG = BuyerSampleNotesActivity.class.getSimpleName();
    private static final int REQ_CODE_GRAFFITI = 101;
    private static final String DOODLE_TEMP_PATH = Environment.getExternalStorageDirectory().getPath() + "/" + "temp.jpg";
    //请求缩略图信号标识
    private static int REQUEST_THUMBNAIL = 1;
    //请求原图信号标识
    private static int REQUEST_ORIGINAL = 2;
    //请求照相机权限标识
    private static int TAKE_PHOTO_REQUEST_CODE = 0;
    //SD卡的路径
    private String mSDPath;
    //图片存储路径
    private String mPicPath;
    private String cPicPath;

    String mMID;
    Bitmap mBitmap;
    ImageButton mCapture;
    EditText mEtNotes;
    EditText mEtRemark;
    Button mBtnOK;
    Button mBtnCancel;
    Button mBtnEdit;
    Button mBtnChoose;
    String mBase64 = "";
    boolean mResult;
    AlertDialog mPbDialog;
    String mSeriesID;
    String mMaker;
    List<String> mExplodeSpilt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_sample_notes);

        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, TAKE_PHOTO_REQUEST_CODE);
            }
        }

        mCapture = (ImageButton) findViewById(R.id.buyer_sample_notes_ibCapture);
        mEtNotes = (EditText) findViewById(R.id.buyer_sample_notes_etNotes);
        mEtRemark = (EditText) findViewById(R.id.buyer_sample_notes_etRemark);
        mBtnOK = (Button) findViewById(R.id.buyer_sample_notes_btnOK);
        mBtnCancel = (Button) findViewById(R.id.buyer_sample_notes_btnCancel);
        mBtnEdit = (Button) findViewById(R.id.buyer_sample_notes_btnEdit);
        mBtnChoose = (Button) findViewById(R.id.buyer_sample_notes_btnChoose);

        try {
            mMaker = ACache.get(BuyerSampleNotesActivity.this).getAsString("Maker");
            mSeriesID = myApp.NetHelper.getSeriesIDBean().getRet().get(0).getSeriesID();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mSDPath = Environment.getExternalStorageDirectory().getPath() + "/" + System.currentTimeMillis();
        cPicPath = mSDPath + "temp_c.jpg";
        mPicPath = "";
        if (getIntent().hasExtra("MID")) {
            mMID = getIntent().getStringExtra("MID");
            try {
                String url = myApp.NetHelperEx.getSamplePictureBean(mMID).getRet();
                mCapture.setScaleType(ImageView.ScaleType.FIT_CENTER);
                mBitmap = ImageUtil.getURLImage(url);
                mCapture.setImageBitmap(mBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BuyerSampleNotesActivity.this.finish();
            }
        });

        mBtnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == mBitmap) {
                    showAlert("请先添加图片");
                    return;
                }

                Bitmap bm =((BitmapDrawable) mCapture.getDrawable()).getBitmap();
                mBase64 = ImageUtil.bitmapToBase64(bm);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mPbDialog = showLoadingPB(false);
                            }
                        });

                        try {
                            mResult = myApp.NetHelperEx.addSampleNotes(mSeriesID, mMID, "4", mMaker, mEtNotes.getText().toString(), mEtRemark.getText().toString(), ".jpg", mBase64);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mPbDialog.dismiss();
                                if (mResult) {
                                    showAlert("添加记录成功");

                                } else {
                                    showAlert("添加失败");
                                }
                            }
                        });


                    }
                }).start();


            }
        });

        mCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
            }
        });
        mBtnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showExplodeSpiltList();
            }
        });

        mBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == mBitmap) {
                    showAlert("图片为空，无法进行编辑标注");
                    return;
                }
                try {
                    mBtnEdit.setClickable(false);
                    mCapture.setClickable(false);
                    File file = new File(DOODLE_TEMP_PATH);
                    FileOutputStream fos = new FileOutputStream(file);
                    mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
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

                GraffitiActivity.startActivityForResult(BuyerSampleNotesActivity.this, params, REQ_CODE_GRAFFITI);
            }
        });

    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mPicPath = mSDPath + "temp.jpg";
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mPicPath)));
        startActivityForResult(intent, REQUEST_ORIGINAL);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
        }
        return true;
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
                    NativeImageUtil.compressBitmap(mPicPath, cPicPath);
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
        if (requestCode == REQ_CODE_GRAFFITI) {
            if (data == null) {
                return;
            }
            if (resultCode == GraffitiActivity.RESULT_OK) {
                String path = data.getStringExtra(GraffitiActivity.KEY_IMAGE_PATH);
                if (TextUtils.isEmpty(path)) {
                    return;
                }
                ImageLoader.getInstance(this).display(findViewById(R.id.buyer_sample_notes_ibCapture), path);
                NativeImageUtil.compressBitmap(path, cPicPath);
            } else if (resultCode == GraffitiActivity.RESULT_ERROR) {
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBtnEdit.setClickable(true);
        mCapture.setClickable(true);
    }

    private AlertDialog showLoadingPB(boolean state) {
        LayoutInflater inflater = getLayoutInflater();
        final View progressbar = inflater.inflate(R.layout.item_progressbar, null);
        AlertDialog.Builder normalDialog = null;
        normalDialog = new AlertDialog.Builder(this)
                .setTitle("系统提示")
                .setCancelable(state)
                .setView(progressbar);
        return normalDialog.show();
    }

    @Override
    public void showAlert(String message) {
        if (message.equals("添加记录成功")) {
            new AlertDialog.Builder(this)
                    .setTitle("系统提示")
                    .setMessage(message)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    })
                    .show();
        } else {
            super.showAlert(message);
        }
    }

    private void showExplodeSpiltList() {
        mExplodeSpilt = new ArrayList<>();
        try {
            for (BaseRet item : myApp.NetHelperEx.getExplodeSpiltBean(mSeriesID).getRet()) {
                mExplodeSpilt.add(item.getTypeName());
            }

            final String[] arrString = mExplodeSpilt.toArray(new String[mExplodeSpilt.size()]) ;

            AlertDialog.Builder listDialog =
                    new AlertDialog.Builder(BuyerSampleNotesActivity.this);
            listDialog.setTitle("选择爆点拆分")
                    .setItems(arrString, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mEtRemark.setText(arrString[which]);
                        }
                    })
                    .show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
