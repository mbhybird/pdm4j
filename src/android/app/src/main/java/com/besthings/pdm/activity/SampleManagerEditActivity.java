package com.besthings.pdm.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.besthings.bean.BuyerSampleNotesRet;
import com.besthings.bean.SampleDetailBean;
import com.besthings.bean.SampleDetailRet;
import com.besthings.bean.SampleFlowRecordRet;
import com.besthings.bean.SampleManagerEditionRecordBean;
import com.besthings.bean.SampleManagerEditionRecordRet;
import com.besthings.pdm.R;
import com.besthings.pdm.adapter.BuyerSampleNotesAdapter;
import com.besthings.pdm.adapter.SampleFlowRecordAdapter;
import com.besthings.pdm.adapter.SampleManagerEditionRecordAdapter;
import com.besthings.pdm.utils.ACache;
import com.besthings.pdm.utils.ImageUtil;
import com.besthings.pdm.utils.NativeImageUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SampleManagerEditActivity extends BaseActivity {

    private static final String TAG = SampleManagerEditActivity.class.getSimpleName();
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
    Button mBtnAddNotes;
    Button mBtnBuyerNotes;
    Button mBtnFlowRecord;
    Button mBtnBorrowSample;
    Button mBtnSave;
    Button mBtnBack;

    EditText mETStyleNo;
    EditText mETSampleNo;
    EditText mETDesigner;
    EditText mETTempleteMan;
    EditText mETTechnician;
    EditText mETProgressVersion;

    RecyclerView mRvRecord;

    int mEditType;
    String mMID;
    String mMaker;
    String mSeriesID;
    JSONObject mJsonObject;

    SampleManagerEditionRecordAdapter mSampleManagerEditionRecordAdapter;
    SampleFlowRecordAdapter mSampleFlowRecordAdapter;
    ImageView mIvCapture;
    Bitmap mBitmap;
    AlertDialog mPbDialog;
    String mBorrowResult;
    String mSort;
    boolean mSaveResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_manager_edit);

        getSupportActionBar().hide();

        mETStyleNo = (EditText) findViewById(R.id.sample_manager_edit_etStyleNo);
        mETSampleNo = (EditText) findViewById(R.id.sample_manager_edit_etSampleNo);
        mETDesigner = (EditText) findViewById(R.id.sample_manager_edit_etDesigner);
        mETTempleteMan = (EditText) findViewById(R.id.sample_manager_edit_etTempleteMan);
        mETTechnician = (EditText) findViewById(R.id.sample_manager_edit_etTechnician);
        mETProgressVersion = (EditText) findViewById(R.id.sample_manager_edit_etProgressVersion);
        mIvCapture = (ImageView) findViewById(R.id.sample_manager_edit_ivCapture);
        mBtnSave = (Button) findViewById(R.id.sample_manager_edit_btnSave);
        mBtnBack = (Button) findViewById(R.id.sample_manager_edit_btnBack);
        mRvRecord = (RecyclerView) findViewById(R.id.sample_manager_edit_rvRecord);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRvRecord.setLayoutManager(linearLayoutManager);
        mRvRecord.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        mBtnAddNotes = (Button) findViewById(R.id.sample_manager_edit_btnAddNotes);
        mBtnBorrowSample = (Button) findViewById(R.id.sample_manager_edit_btnBorrowSample);
        mBtnBuyerNotes = (Button) findViewById(R.id.sample_manager_edit_btnBuyerNotes);
        mBtnFlowRecord = (Button) findViewById(R.id.sample_manager_edit_btnFlowRecord);


        try {
            mMaker = ACache.get(SampleManagerEditActivity.this).getAsString("Maker");
            mSeriesID = myApp.NetHelper.getSeriesIDBean().getRet().get(0).getSeriesID();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (getIntent().hasExtra("EditType")) {
            mEditType = getIntent().getIntExtra("EditType", 0);
        }

        if (getIntent().hasExtra("MID")) {
            mMID = getIntent().getStringExtra("MID");
            Log.i(TAG, mMID);
            try {
                SampleDetailRet buyerSampleDetail = myApp.NetHelperEx.getSampleDetailBean(mMID).getRet();
                String url = myApp.NetHelperEx.getSamplePictureBean(mMID).getRet();
                mIvCapture.setScaleType(ImageView.ScaleType.FIT_CENTER);
                mIvCapture.setImageBitmap(ImageUtil.getURLImage(url));
                mETStyleNo.setText(buyerSampleDetail.getStyleno());
                mETSampleNo.setText(buyerSampleDetail.getSampleno());
                mETDesigner.setText(buyerSampleDetail.getDesigner());
                mETTempleteMan.setText(buyerSampleDetail.getTemplateman());
                mETTechnician.setText(buyerSampleDetail.getTechnician());
                mETProgressVersion.setText(buyerSampleDetail.getSort());
                mSort = buyerSampleDetail.getSort();


            } catch (IOException e) {
                e.printStackTrace();
            }

            //add
            if (mEditType == 0) {
                setControlState(true);
            } else {
                //modify
                setControlState(false);
            }

            mBtnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SampleManagerEditActivity.this.finish();
                }
            });

            mBtnAddNotes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //添加样衣批注
                    Intent it = new Intent(SampleManagerEditActivity.this, SampleManagerNotesActivity.class);
                    it.putExtra("MID", mMID);
                    it.putExtra("sort", mSort);
                    startActivityForResult(it, 1000);
                }
            });


            mBtnBorrowSample.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showBorrowSampleDialog();
                }
            });

            mBtnBuyerNotes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mBtnAddNotes.setVisibility(View.VISIBLE);
                    mBtnBorrowSample.setVisibility(View.INVISIBLE);
                    showTrialEditionRecord();
                }
            });

            mBtnFlowRecord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mBtnBorrowSample.setVisibility(View.VISIBLE);
                    mBtnAddNotes.setVisibility(View.INVISIBLE);
                    showSampleFlowRecord();
                }
            });

            mBtnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mPbDialog = showLoadingPB(false);
                                }
                            });

                            String base64 = "";


                            try {
                                if (mBitmap != null) {
                                    base64 = ImageUtil.bitmapToBase64(mBitmap);
                                    mSaveResult = myApp.NetHelperEx.uploadSamplePicture(mMID, ".jpg", base64).contains("\"Res\":0");
                                } else {
                                    mSaveResult = false;
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (mSaveResult) {
                                        mPbDialog.dismiss();
                                        setControlState(false);
                                        showAlert("保存成功！");
                                        mBitmap = null;
                                    } else {
                                        mPbDialog.dismiss();
                                        showAlert("保存失败！请重新拍照");
                                    }
                                }
                            });
                        }
                    }).start();
                }
            });

            mIvCapture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    takePhoto();
                }
            });

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

        }
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

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mPicPath = mSDPath + "temp.jpg";
        cPicPath = mSDPath + "temp_c.jpg";
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mPicPath)));
        startActivityForResult(intent, REQUEST_ORIGINAL);
    }

    private void setControlState(boolean state) {
        mETStyleNo.setEnabled(state);
        mETSampleNo.setEnabled(state);
        mETDesigner.setEnabled(state);
        mETTempleteMan.setEnabled(state);
        mETTechnician.setEnabled(state);
        mETProgressVersion.setEnabled(state);
        mBtnBuyerNotes.setEnabled(!state);
        mBtnFlowRecord.setEnabled(!state);

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
                Bitmap bitmap = (Bitmap) bundle.get("data");
                mIvCapture.setImageBitmap(bitmap);
                mIvCapture.setScaleType(ImageView.ScaleType.FIT_CENTER);
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
                    mIvCapture.setImageBitmap(mBitmap);
                    mIvCapture.setScaleType(ImageView.ScaleType.FIT_CENTER);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fis.close();//关闭流
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (requestCode == 1000) {
                showTrialEditionRecord();
            }
        }
    }

    private void showBorrowSampleDialog() {
        LayoutInflater inflater = getLayoutInflater();
        final View sampleBorrowLayout = inflater.inflate(R.layout.activity_buyer_sample_borrow, null);
        final Spinner spType = (Spinner) sampleBorrowLayout.findViewById(R.id.buyer_sample_borrow_spType);
        final EditText etBorrower = (EditText) sampleBorrowLayout.findViewById(R.id.buyer_sample_borrow_etBorrower);
        final EditText etRecord = (EditText) sampleBorrowLayout.findViewById(R.id.buyer_sample_borrow_etRecord);
        final EditText etRemark = (EditText) sampleBorrowLayout.findViewById(R.id.buyer_sample_borrow_etRemark);
        final ProgressBar pbBorrow = (ProgressBar) sampleBorrowLayout.findViewById(R.id.buyer_sample_borrow_pb);
        final Date mNowTime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss");
        final String nowDate = sdf.format(mNowTime);
        etBorrower.setText(mMaker);

        new android.app.AlertDialog.Builder(this)
                .setTitle("借取样衣")
                .setView(sampleBorrowLayout)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        pbBorrow.setVisibility(View.VISIBLE);
                                    }
                                });

                                try {
                                    mJsonObject = new JSONObject();
                                    mJsonObject.put("SeriesID", mSeriesID);
                                    mJsonObject.put("MID", "");
                                    mJsonObject.put("LID", mMID);
                                    mJsonObject.put("Borrower", etBorrower.getText().toString());
                                    mJsonObject.put("BorrowTime", nowDate);
                                    String type = null;
                                    switch (spType.getSelectedItemPosition()) {
                                        case 0:
                                            type = "Normal";
                                            break;
                                        case 1:
                                            type = "Sale";
                                            break;
                                        case 2:
                                            type = "Stock";
                                            break;
                                        case 3:
                                            type = "Destroy";
                                            break;
                                    }
                                    mJsonObject.put("Type", type);
                                    mJsonObject.put("R1", etRecord.getText().toString());
                                    mJsonObject.put("R2", etRemark.getText().toString());
                                    mJsonObject.put("Maker", mMaker);

                                    String json = mJsonObject.toString();
                                    mBorrowResult = myApp.NetHelperEx.addSampleCourseRecord(json.substring(1, json.length() - 1));
                                } catch (JSONException | IOException e) {
                                    e.printStackTrace();
                                }


                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (mBorrowResult.contains("\"Res\":0")) {
                                            pbBorrow.setVisibility(View.INVISIBLE);
                                            dialog.dismiss();
                                            showSampleFlowRecord();
                                            showAlert("添加记录成功");
                                        } else {
                                            pbBorrow.setVisibility(View.INVISIBLE);
                                            dialog.dismiss();
                                            showAlert("添加不成功！");
                                        }
                                    }
                                });
                            }
                        }).start();

                    }
                })
                .setNegativeButton("返回",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                // 显示
                .show();

    }


    private void showSampleFlowRecord() {
        try {
            List<SampleFlowRecordRet> list = new ArrayList<>();
            list = myApp.NetHelperEx.getSampleFlowRecordBean(mMID).getRet();
            mSampleFlowRecordAdapter = new SampleFlowRecordAdapter(R.layout.item_sample_flow_record, list);
            mSampleFlowRecordAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
            mRvRecord.setAdapter(mSampleFlowRecordAdapter);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showTrialEditionRecord() {
        try {
            List<SampleManagerEditionRecordRet> list;
            list = myApp.NetHelperEx.getSampleManagerEditionRecordBean(mMID).getRet();
            mSampleManagerEditionRecordAdapter = new SampleManagerEditionRecordAdapter(R.layout.item_sample_edition_record, list);
            mSampleManagerEditionRecordAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
            mRvRecord.setAdapter(mSampleManagerEditionRecordAdapter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
