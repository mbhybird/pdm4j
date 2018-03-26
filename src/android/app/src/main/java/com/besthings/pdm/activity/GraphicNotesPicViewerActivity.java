package com.besthings.pdm.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.besthings.pdm.R;
import com.besthings.pdm.utils.ACache;
import com.besthings.pdm.utils.ImageUtil;
import com.besthings.pdm.utils.NativeImageUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import cn.hzw.graffiti.GraffitiActivity;
import cn.hzw.graffiti.GraffitiParams;
import cn.hzw.graffiti.imagepicker.ImageLoader;


public class GraphicNotesPicViewerActivity extends BaseActivity {
    private static final String TAG = GraphicNotesPicViewerActivity.class.getSimpleName();
    ProgressBar pbUpload;
    Button btnGraphicUpload;
    Button btnGraphicNotes;
    String mGraphicNotesPicMID;
    String mGraphicNotesPicURL;
    private static final int REQ_CODE_GRAFFITI = 101;
    private static final String DOODLE_TEMP_PATH = Environment.getExternalStorageDirectory().getPath() + "/" + System.currentTimeMillis()+ "temp.jpg";
    private static String mGraphicNotesPicBase64 = null;
    String cPath = Environment.getExternalStorageDirectory().getPath() + "/" + System.currentTimeMillis()+ "temp_c.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphic_notes_pic_viewer);
        getSupportActionBar().hide();

        btnGraphicNotes = (Button) findViewById(R.id.graphic_btnGraphicNotes);
        btnGraphicUpload = (Button) findViewById(R.id.graphic_btnNotesUpload);
        Button btnPassed = (Button) findViewById(R.id.graphic_btnPassed);
        Button btnCancel = (Button) findViewById(R.id.graphic_btnCancel);
        final ImageView ivPic = (ImageView) findViewById(R.id.graphic_ivPic);
        pbUpload = (ProgressBar) findViewById(R.id.graphic_pbUpload);

        Intent intentGET = getIntent();
        mGraphicNotesPicMID = intentGET.getStringExtra("imgMID");
        try {
            mGraphicNotesPicURL = myApp.NetHelperEx.getDesignFilePicBean(mGraphicNotesPicMID).getRet();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final String Maker = ACache.get(GraphicNotesPicViewerActivity.this).getAsString("Maker");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, REQ_CODE_GRAFFITI);
            }
        }

        // if Pic is null
        ivPic.setImageBitmap(ImageUtil.getURLImage(mGraphicNotesPicURL));

        btnGraphicNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnGraphicNotes.setClickable(false);
                if (!mGraphicNotesPicURL.equals("")) {
                    try {
                        Bitmap bmp = ImageUtil.getURLImage(mGraphicNotesPicURL);
                        File file = new File(DOODLE_TEMP_PATH);
                        FileOutputStream fos = new FileOutputStream(file);
                        bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
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

                    GraffitiActivity.startActivityForResult(GraphicNotesPicViewerActivity.this, params, REQ_CODE_GRAFFITI);

                } else {
                    showAlert("缺少款式图片");
                }

            }
        });

        // button of upload
        btnGraphicUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btnGraphicUpload.setEnabled(false);
                                pbUpload.setVisibility(View.VISIBLE);
                            }
                        });
                        if (mGraphicNotesPicBase64 != null) {
                            try {
                                myApp.NetHelperEx.notesUpload(mGraphicNotesPicMID, "4", Maker, ".jpg", mGraphicNotesPicBase64);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showConfirmDialog("upload");
                                    btnGraphicUpload.setEnabled(true);
                                    pbUpload.setVisibility(View.INVISIBLE);
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showConfirmDialog("noNotes");
                                    btnGraphicUpload.setEnabled(true);
                                    pbUpload.setVisibility(View.INVISIBLE);
                                }
                            });
                        }

                    }
                }).start();
            }
        });

        // button of audit passed
        btnPassed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    myApp.NetHelperEx.auditDesignFile(1, mGraphicNotesPicMID, Maker);
                    showConfirmDialog("passed");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        // button of cancel
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GraphicNotesPicViewerActivity.this.finish();
            }
        });

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
        if (requestCode == REQ_CODE_GRAFFITI) {
            if (data == null) {
                return;
            }
            if (resultCode == GraffitiActivity.RESULT_OK) {
                String path = data.getStringExtra(GraffitiActivity.KEY_IMAGE_PATH);
                if (TextUtils.isEmpty(path)) {
                    return;
                }
                ImageLoader.getInstance(this).display(findViewById(R.id.graphic_ivPic), path);
                NativeImageUtil.compressBitmap(path, cPath);
                Bitmap bitmap = BitmapFactory.decodeFile(cPath);
                mGraphicNotesPicBase64 = ImageUtil.bitmapToBase64(bitmap);
            } else if (resultCode == GraffitiActivity.RESULT_ERROR) {
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void showConfirmDialog(final String mes) {
        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("系统提示");
        switch (mes) {
            case "upload":
                builder.setMessage("批注上传成功");
                break;
            case "passed":
                builder.setMessage("审核通过！");
                break;
            case "noNotes":
                builder.setMessage("请先进行批注再上传");
                break;
        }
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (mes) {
                    case "passed":
                        Intent intent = new Intent();
                        intent.putExtra("NotesResult","success");
                        setResult(RESULT_OK, intent);
                        finish();
                        break;
                }
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        btnGraphicNotes.setClickable(true);
    }
}
