package com.besthings.pdm.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.besthings.bean.BaseRet;
import com.besthings.bean.BuyerSampleDetailRet;
import com.besthings.bean.BuyerSampleNotesRet;
import com.besthings.bean.SampleFlowRecordRet;
import com.besthings.bean.UploadBuyerSampleBean;
import com.besthings.pdm.R;
import com.besthings.pdm.adapter.BuyerSampleNotesAdapter;
import com.besthings.pdm.adapter.SampleFlowRecordAdapter;
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

public class BuyerSampleEditActivity extends BaseActivity {
    private static final String TAG = BuyerSampleEditActivity.class.getSimpleName();
    //请求缩略图信号标识
    private static int REQUEST_THUMBNAIL = 1;
    //请求原图信号标识
    private static int REQUEST_ORIGINAL = 2;
    //请求照相机权限标识
    private static int TAKE_PHOTO_REQUEST_CODE = 0;
    //SD卡的路径
    private String mSDPath;
    //图片存储路径
    private String mPicPath = "";
    private String cPicPath = "";
    Button mBtnAddNotes;
    Button mBtnAssignTask;
    Button mBtnBuyerNotes;
    Button mBtnFlowRecord;
    Button mBtnBorrowSample;
    Button mBtnSave;
    Button mBtnSamplePoint;
    Button mBtnCategory;
    Button mBtnBrand;

    EditText mETSampleNo;
    EditText mETPrice;
    EditText mETBuyer;
    EditText mETStyleNo;
    EditText mETBrand;
    EditText mETCategory;
    EditText mETSamplePoint;

    ImageView mIvCapture;
    AlertDialog mPbDialog;
    RecyclerView mRvRecord;

    int mEditType;
    String mStyleNo = "";
    String mSampleNo = "";
    String mPrice = "";
    String mBuyer = "";
    String mBrand = "";
    String mCategory = "";
    String mSamplePoint = "";
    String mMaker = "";
    String mMID = "";

    JSONObject mJsonObject;
    Bitmap mBitmap;
    UploadBuyerSampleBean mResult;
    String mBorrowResult;
    SampleFlowRecordAdapter mSampleFlowRecordAdapter;
    BuyerSampleNotesAdapter mBuyerSampleNotesAdapter;
    List<String> mSampleReferencePointList;
    List<String> mSampleBrandList;
    List<String> mSampleCategoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_sample_edit);

        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, TAKE_PHOTO_REQUEST_CODE);
            }
        }

        mETStyleNo = (EditText) findViewById(R.id.buyer_sample_edit_etStyleNo);
        mETSampleNo = (EditText) findViewById(R.id.buyer_sample_edit_etSampleNo);
        mETPrice = (EditText) findViewById(R.id.buyer_sample_edit_etPrice);
        mETBuyer = (EditText) findViewById(R.id.buyer_sample_edit_etBuyer);
        mETBrand = (EditText) findViewById(R.id.buyer_sample_edit_etBrand);
        mETCategory = (EditText) findViewById(R.id.buyer_sample_edit_etCategory);
        mETSamplePoint = (EditText) findViewById(R.id.buyer_sample_edit_etSamplePoint);
        Button btnBack = (Button) findViewById(R.id.buyer_sample_edit_btnBack);
        mIvCapture = (ImageView) findViewById(R.id.buyer_sample_edit_iv);

        mBtnSave = (Button) findViewById(R.id.buyer_sample_edit_btnSave);
        mBtnSamplePoint = (Button) findViewById(R.id.buyer_sample_edit_btnSamplePoint);
        mBtnCategory = (Button) findViewById(R.id.buyer_sample_edit_btnCategory);
        mBtnBrand = (Button) findViewById(R.id.buyer_sample_edit_btnBrand);


        mRvRecord = (RecyclerView) findViewById(R.id.buyer_sample_edit_rvRecord);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRvRecord.setLayoutManager(linearLayoutManager);
        mRvRecord.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        mMaker = ACache.get(BuyerSampleEditActivity.this).getAsString("Maker");

        if (getIntent().hasExtra("EditType")) {
            mEditType = getIntent().getIntExtra("EditType", 0);
        }

        if (getIntent().hasExtra("MID")) {
            mMID = getIntent().getStringExtra("MID");
            try {
                List<BuyerSampleDetailRet> buyerSampleDetail;
                buyerSampleDetail = myApp.NetHelperEx.getBuyerSampleDetailBean(mMID).getRet();
                if (buyerSampleDetail.size() > 0) {
                    mETStyleNo.setText(buyerSampleDetail.get(0).getCStyleNo());
                    mETSampleNo.setText(buyerSampleDetail.get(0).getCClothSampleNo());
                    mETPrice.setText(buyerSampleDetail.get(0).getDPrice1());
                    mETBuyer.setText(buyerSampleDetail.get(0).getCBuyer());
                    mETBrand.setText(buyerSampleDetail.get(0).getCBrand());
                    mETCategory.setText(buyerSampleDetail.get(0).getCCategory());
                    mETSamplePoint.setText(buyerSampleDetail.get(0).getCRemark2());
                    mIvCapture.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    mIvCapture.setImageBitmap(ImageUtil.getURLImage(buyerSampleDetail.get(0).getIMinPicture()));
                } else {
                    finish();
                    Toast.makeText(this, "获取详细信息失败", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BuyerSampleEditActivity.this.finish();
            }
        });
        mBtnSamplePoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSampleReferencePointList();
            }
        });
        mBtnAddNotes = (Button) findViewById(R.id.buyer_sample_edit_btnAddNotes);
        mBtnAddNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //添加样衣批注
                Intent it = new Intent(BuyerSampleEditActivity.this, BuyerSampleNotesActivity.class);
                it.putExtra("MID", mMID);
                startActivityForResult(it, 1000);
            }
        });

        mBtnAssignTask = (Button) findViewById(R.id.buyer_sample_edit_btnAssignTask);
        mBtnAssignTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //添加设计任务
                Intent it = new Intent(BuyerSampleEditActivity.this, BuyerSampleAssignTaskActivity.class);
                it.putExtra("MID", mMID);
                startActivity(it);
            }
        });

        mBtnBorrowSample = (Button) findViewById(R.id.buyer_sample_edit_btnBorrowSample);
        mBtnBorrowSample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //添加借取记录
                showBorrowSampleDialog();
            }
        });

        mBtnBuyerNotes = (Button) findViewById(R.id.buyer_sample_edit_btnBuyerNotes);
        mBtnBuyerNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBtnAddNotes.setVisibility(View.VISIBLE);
                mBtnBorrowSample.setVisibility(View.INVISIBLE);
                showBuyerSampleNotes();
            }
        });

        mBtnFlowRecord = (Button) findViewById(R.id.buyer_sample_edit_btnFlowRecord);
        mBtnFlowRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBtnBorrowSample.setVisibility(View.VISIBLE);
                mBtnAddNotes.setVisibility(View.INVISIBLE);
                showSampleFlowRecord();
            }
        });

        mBtnBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBrandList();
            }
        });
        mBtnCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCategoryList();
            }
        });

        //add
        if (mEditType == 0) {
            setControlState(true);
        } else {
            //modify
            setControlState(false);
        }
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSampleNo = mETSampleNo.getText().toString();
                mCategory = mETCategory.getText().toString();
                mSamplePoint = mETSamplePoint.getText().toString();
                mPrice = mETPrice.getText().toString();
                mBuyer = mETBuyer.getText().toString();
                mStyleNo = mETStyleNo.getText().toString();
                mBrand = mETBrand.getText().toString();

                if (null == mBitmap) {
                    showAlert("图片为空，请先进行拍照！");
                    return;
                }

                if (TextUtils.isEmpty(mSampleNo)) {
                    showAlert("样衣编号必须填写，请检查！");
                    return;
                }
                if (TextUtils.isEmpty(mCategory)) {
                    showAlert("类目必须填写，请检查！");
                    return;
                }

                if (TextUtils.isEmpty(mSamplePoint)) {
                    showAlert("样衣参考点必须填写，请检查！");
                    return;
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mBtnSave.setEnabled(false);
                                mPbDialog = showLoadingPB(false);
                            }
                        });

                        String base64 = "";
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                            if (mBitmap != null) {
                                base64 = ImageUtil.bitmapToBase64(mBitmap);
                            }
                        }
                        try {

                            mJsonObject = new JSONObject();
                            mJsonObject.put("SeriesID", myApp.mSeriesID);
                            mJsonObject.put("cClothSampleNo", mSampleNo);
                            mJsonObject.put("cCategory", mCategory);
                            mJsonObject.put("cRemark2", mSamplePoint);
                            if (!mPrice.equals("")) {
                                mJsonObject.put("dPrice", mPrice);
                            }
                            if (!mBuyer.equals("")) {
                                mJsonObject.put("cBuyer", mBuyer);
                            }
                            if (!mStyleNo.equals("")) {
                                mJsonObject.put("cStyleNo", mStyleNo);
                            }
                            if (!mBrand.equals("")) {
                                mJsonObject.put("cBrand", mBrand);
                            }
                            mJsonObject.put("cSource", 3);
                            mJsonObject.put("Type", 1);
                            mJsonObject.put("cFileExt", ".jpg");
                            String json = mJsonObject.toString();
                            Log.i(TAG, json.substring(1, json.length() - 1));
                            mResult = myApp.NetHelperEx.addBuyerSampleBean(json.substring(1, json.length() - 1), base64, new File(mPicPath));
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                boolean state = (boolean) (mResult.getRes() == 0);
                                if (state) {
                                    mMID = mResult.getRet().replace("\'", "");
                                    mBtnSave.setVisibility(View.GONE);
                                    mBtnSave.setEnabled(true);
                                    mPbDialog.dismiss();
                                    setControlState(false);
                                    showAlert("成功添加买手样衣！");
                                } else {
                                    mBtnSave.setEnabled(true);
                                    mPbDialog.dismiss();
                                    showAlert("添加不成功！");
                                }
                            }
                        });
                    }
                }).start();
            }
        });

        mSDPath = Environment.getExternalStorageDirectory().getPath() + "/" + System.currentTimeMillis();
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

    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mPicPath = mSDPath + "temp.jpg";
        cPicPath = mSDPath + "temp_c.jpg";
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mPicPath)));
        startActivityForResult(intent, REQUEST_ORIGINAL);
    }

    private void setControlState(boolean state) {
        mIvCapture.setEnabled(state);
        mETBrand.setEnabled(state);
        mETBuyer.setEnabled(state);
        mETPrice.setEnabled(state);
        mETSampleNo.setEnabled(state);
        mETStyleNo.setEnabled(state);
        mETCategory.setEnabled(state);
        mETSamplePoint.setEnabled(state);

        mBtnSamplePoint.setEnabled(state);
        mBtnCategory.setEnabled(state);
        mBtnBrand.setEnabled(state);
        if (!state) {
            mBtnSave.setVisibility(View.GONE);

        }
        mBtnAssignTask.setEnabled(!state);
        mBtnFlowRecord.setEnabled(!state);
        mBtnBuyerNotes.setEnabled(!state);
        if (state) {
            mETBuyer.setText(mMaker);
        }

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
                // refresh buyer notes
                showBuyerSampleNotes();
            }
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
        AlertDialog pb = normalDialog.show();
        return pb;
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
                                    mJsonObject.put("SeriesID", myApp.mSeriesID);
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
                                        case 4:
                                            type = "Appoint";
                                            break;
                                    }
                                    mJsonObject.put("Type", type);
                                    mJsonObject.put("R1", etRecord.getText().toString());
                                    mJsonObject.put("R2", etRemark.getText().toString());
                                    mJsonObject.put("Maker", mMaker);

                                    String json = mJsonObject.toString();
                                    Log.i(TAG, json.substring(1, json.length() - 1));
                                    mBorrowResult = myApp.NetHelperEx.addSampleCourseRecord(json.substring(1, json.length() - 1));
                                    Log.i(TAG, mBorrowResult);
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
                                            mSampleFlowRecordAdapter.notifyDataSetChanged();
                                            mRvRecord.setAdapter(mSampleFlowRecordAdapter);
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

    private void showBuyerSampleNotes() {
        try {
            List<BuyerSampleNotesRet> list = new ArrayList<>();
            list = myApp.NetHelperEx.getBuyerSampleNotesBean(mMID).getRet();
            mBuyerSampleNotesAdapter = new BuyerSampleNotesAdapter(R.layout.item_buyer_sample_notes, list);
            mBuyerSampleNotesAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
            mRvRecord.setAdapter(mBuyerSampleNotesAdapter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showCategoryList() {
        mSampleCategoryList = new ArrayList<>();
        try {
            for (BaseRet item : myApp.NetHelperEx.getCategoryBean(myApp.mSeriesID).getRet()) {
                mSampleCategoryList.add(item.getTypeName());
            }

            final String[] items = mSampleCategoryList.toArray(new String[mSampleCategoryList.size()]) ;
            AlertDialog.Builder listDialog =
                    new AlertDialog.Builder(BuyerSampleEditActivity.this);
            listDialog.setTitle("请选择类目")
                    .setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mETCategory.setText(items[which]);
                        }
                    })
                    .show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showBrandList() {
        mSampleBrandList = new ArrayList<>();
        try {
            for (String item : myApp.NetHelper.getBrandBean(myApp.mSeriesID).getRet()) {
                mSampleBrandList.add(item);
            }
            final String[] items = mSampleBrandList.toArray(new String[mSampleBrandList.size()]) ;
            AlertDialog.Builder listDialog =
                    new AlertDialog.Builder(BuyerSampleEditActivity.this);
            listDialog.setTitle("请选择品牌")
                    .setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mETBrand.setText(items[which]);
                        }
                    })
                    .show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showSampleReferencePointList() {
        mSampleReferencePointList = new ArrayList<>();
        try {

            for (BaseRet item : myApp.NetHelperEx.getSampleReferencePointBean(myApp.mSeriesID).getRet()) {
                mSampleReferencePointList.add(item.getTypeName());
            }
            final String[] items = mSampleReferencePointList.toArray(new String[mSampleReferencePointList.size()]) ;

            AlertDialog.Builder listDialog =
                    new AlertDialog.Builder(BuyerSampleEditActivity.this);
            listDialog.setTitle("请选择样衣参考点")
                    .setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mETSamplePoint.setText(items[which]);
                        }
                    })
                    .show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

