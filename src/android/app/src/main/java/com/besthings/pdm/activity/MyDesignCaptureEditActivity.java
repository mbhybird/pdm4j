package com.besthings.pdm.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.besthings.bean.BaseRet;
import com.besthings.pdm.R;
import com.besthings.pdm.utils.ACache;
import com.besthings.pdm.utils.ImageUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyDesignCaptureEditActivity extends BaseActivity {
    boolean mIsCaptureEmpty = true;
    Spinner spBrand;
    Spinner spTheme;
    Spinner spBand;
    Spinner spYear;
    Spinner spSeason;
    Spinner spStyle;
    String mSeriesID;
    Bitmap mBitmap = null;
    ProgressBar pb;
    boolean mResult = false;
    Button btnOK;
    EditText etStyleNo;
    EditText etStyleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_design_capture_edit);

        getSupportActionBar().hide();

        btnOK = (Button) findViewById(R.id.my_design_capture_edit_btnUpload);
        Button btnCancel = (Button) findViewById(R.id.my_design_capture_edit_btnCancel);
        ImageView ivCapture = (ImageView) findViewById(R.id.my_design_capture_edit_ivCapture);
        spBrand = (Spinner) findViewById(R.id.my_design_capture_edit_spBrand);
        spTheme = (Spinner) findViewById(R.id.my_design_capture_edit_spTheme);
        spBand = (Spinner) findViewById(R.id.my_design_capture_edit_spBand);
        spYear = (Spinner) findViewById(R.id.my_design_capture_edit_spYear);
        spSeason = (Spinner) findViewById(R.id.my_design_capture_edit_spSeason);
        spStyle = (Spinner) findViewById(R.id.my_design_capture_edit_spStyle);
        pb = (ProgressBar) findViewById(R.id.my_design_capture_edit_progress);
        etStyleNo = (EditText) findViewById(R.id.my_design_capture_edit_etStyleNo);
        etStyleName = (EditText) findViewById(R.id.my_design_capture_edit_etStyleName);

        try {
            mSeriesID = myApp.NetHelper.getSeriesIDBean().getRet().get(0).getSeriesID();
            initDrawdownItems(mSeriesID);
        }catch (Exception e) {
            e.printStackTrace();
        }

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIsCaptureEmpty) {
                    showAlert("图片为空，请重新拍照！");
                    return;
                }

                if (TextUtils.isEmpty(etStyleNo.getText().toString())) {
                    showAlert("款号必须填写，请检查！");
                    return;
                }

                if (TextUtils.isEmpty(etStyleName.getText().toString())) {
                    showAlert("款式名称必须填写，请检查！");
                    return;
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btnOK.setEnabled(false);
                                pb.setVisibility(View.VISIBLE);
                            }
                        });

                        String base64 = "";
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                            if (mBitmap != null) {
                                base64 = ImageUtil.bitmapToBase64(mBitmap);
                            }
                        }

                        try {
                            mResult = myApp.NetHelper.addNewDesign(
                                    mSeriesID,
                                    etStyleNo.getText().toString(),
                                    spBrand.getSelectedItem().toString(),
                                    ACache.get(MyDesignCaptureEditActivity.this).getAsString("Maker"),
                                    spBand.getSelectedItem().toString(),
                                    spStyle.getSelectedItem().toString(),
                                    spYear.getSelectedItem().toString(),
                                    spSeason.getSelectedItem().toString(),
                                    spTheme.getSelectedItem().toString(),
                                    etStyleName.getText().toString(),
                                    base64,
                                    new File(getIntent().getStringExtra("capture")));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mResult) {
                                    btnOK.setEnabled(true);
                                    pb.setVisibility(View.INVISIBLE);
                                    new AlertDialog.Builder(MyDesignCaptureEditActivity.this)
                                            .setTitle("系统提示")
                                            .setMessage("成功添加设计稿!")
                                            .setPositiveButton("返回", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.dismiss();
                                                    Intent it = new Intent(MyDesignCaptureEditActivity.this, MyDesignActivity.class);
                                                    it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    it.putExtra("title", "我的设计");
                                                    startActivity(it);
                                                    MyDesignCaptureEditActivity.this.finish();
                                                }
                                            }).show();
                                } else {
                                    btnOK.setEnabled(true);
                                    pb.setVisibility(View.INVISIBLE);
                                    showAlert("添加不成功！");
                                }
                            }
                        });
                    }
                }).start();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDesignCaptureEditActivity.this.finish();
            }
        });

        if (getIntent().hasExtra("capture")) {
            String picPath = getIntent().getStringExtra("capture");
            if (!picPath.equals("")) {
                FileInputStream fis = null;
                File file = new File(picPath);
                try {
                    if (file.exists()) {
                        //把图片转化为字节流
                        fis = new FileInputStream(picPath);
                        //把流转化图片
                        mBitmap = BitmapFactory.decodeStream(fis);
                        //mBitmap = ImageUtil.getCompressBitmap(picPath);
                        ivCapture.setImageBitmap(mBitmap);
                        ivCapture.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        mIsCaptureEmpty = false;
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        file = null;
                        if (fis != null) {
                            fis.close();//关闭流
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void initDrawdownItems(String seriesID) {
        //Brand
        List<String> brandItems = null;
        try {
            brandItems = myApp.NetHelper.getBrandBean(seriesID).getRet();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> brandAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, brandItems);
        brandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBrand.setAdapter(brandAdapter);

        //Style
        List<String> styleItems = null;
        try {
            styleItems = myApp.NetHelper.getStyleBean(seriesID).getRet();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> styleAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, styleItems);
        styleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spStyle.setAdapter(styleAdapter);

        List<BaseRet> RetItems = null;
        List<String> baseItems = null;

        //Theme
        try {
            RetItems = myApp.NetHelper.getThemeBean(seriesID).getRet();
            baseItems = new ArrayList<>();
            for (BaseRet br : RetItems) {
                baseItems.add(br.getTypeName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> themeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, baseItems);
        themeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTheme.setAdapter(themeAdapter);

        //Band
        try {
            RetItems = myApp.NetHelper.getBandBean(seriesID).getRet();
            baseItems = new ArrayList<>();
            for (BaseRet br : RetItems) {
                baseItems.add(br.getTypeName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> bandAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, baseItems);
        bandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBand.setAdapter(bandAdapter);

        //Year
        try {
            RetItems = myApp.NetHelper.getYeanBean(seriesID).getRet();
            baseItems = new ArrayList<>();
            for (BaseRet br : RetItems) {
                baseItems.add(br.getTypeName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, baseItems);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spYear.setAdapter(yearAdapter);

        //Season
        try {
            RetItems = myApp.NetHelper.getSeasonBean(seriesID).getRet();
            baseItems = new ArrayList<>();
            for (BaseRet br : RetItems) {
                baseItems.add(br.getTypeName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> seasonAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, baseItems);
        seasonAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSeason.setAdapter(seasonAdapter);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
        }
        return true;
    }
}
