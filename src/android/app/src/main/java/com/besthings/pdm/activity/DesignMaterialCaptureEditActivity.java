package com.besthings.pdm.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.besthings.bean.BaseRet;
import com.besthings.bean.UploadDesignMaterialBean;
import com.besthings.pdm.R;
import com.besthings.pdm.utils.ACache;
import com.besthings.pdm.utils.ImageUtil;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class DesignMaterialCaptureEditActivity extends BaseActivity {
    private static final String TAG = DesignMaterialCaptureEditActivity.class.getSimpleName();
    private final CompositeDisposable disposables = new CompositeDisposable();
    String mJson;
    JSONObject mUploadJsonObject;
    ImageView ibCapture;
    EditText etMaterialName;
    EditText etWidth;
    EditText etSpecs;
    EditText etPrice;
    EditText etColor;
    EditText etUnit;
    EditText etSupplierName;
    EditText etLinkMan;
    EditText etSupplierPhone;
    EditText etSupplierAddr;
    EditText etMoneyUnit;
    Button btnLocal;
    Bitmap mBitmap;
    UploadDesignMaterialBean uploadDesignMaterialBean;
    LocationManager mLocationManager = null;
    String mLocationProvider = null;
    Location mLocation = null;
    List<String> alLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design_material_capture_edit);

        getSupportActionBar().hide();

        ibCapture = (ImageButton) findViewById(R.id.design_material_edit_ibCapture);
        etMaterialName = (EditText) findViewById(R.id.design_material_edit_etMaterialName);
        etWidth = (EditText) findViewById(R.id.design_material_edit_etWidth);
        etSpecs = (EditText) findViewById(R.id.design_material_edit_etSpecs);
        etPrice = (EditText) findViewById(R.id.design_material_edit_etPrice);
        etColor = (EditText) findViewById(R.id.design_material_edit_etColor);
        etUnit = (EditText) findViewById(R.id.design_material_edit_etUnit);
        etSupplierName = (EditText) findViewById(R.id.design_material_edit_etSupplierName);
        etLinkMan = (EditText) findViewById(R.id.design_material_edit_etLinkMan);
        etSupplierPhone = (EditText) findViewById(R.id.design_material_edit_etSupplierPhone);
        etSupplierAddr = (EditText) findViewById(R.id.design_material_edit_etSupplierAddr);
        etMoneyUnit = (EditText) findViewById(R.id.design_material_edit_etMoneyUnit);
        btnLocal = (Button) findViewById(R.id.design_material_edit_btnLocal);
        final Button btnBack = (Button) findViewById(R.id.design_material_edit_btnBack);
        final Button btnFinish = (Button) findViewById(R.id.design_material_edit_btnFinish);
        final ProgressBar pb = (ProgressBar) findViewById(R.id.design_material_edit_pb);
        DesignMaterialCaptureEditActivityPermissionsDispatcher.requestActionWithPermissionCheck(this);
        onRunSchedulerExampleButtonClicked();
        Intent intentGet = getIntent();
        if (intentGet.hasExtra("bitmap")) {
            byte [] bis=intentGet.getByteArrayExtra("bitmap");
            mBitmap= BitmapFactory.decodeByteArray(bis, 0, bis.length);
            ibCapture.setImageBitmap(mBitmap);

        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(DesignMaterialCaptureEditActivity.this);
                    dialog.setMessage("GPS未打开，是否打开?");
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            // 设置完成后返回到原来的界面
                            startActivityForResult(intent, 0);
                        }
                    });
                    dialog.setNegativeButton("返回", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_COARSE);
                //设置不需要获取海拔方向数据
                criteria.setAltitudeRequired(false);
                criteria.setBearingRequired(false);
                //设置允许产生资费
                criteria.setCostAllowed(true);
                //要求低耗电
                criteria.setPowerRequirement(Criteria.POWER_LOW);
                try {
                    mLocationProvider = mLocationManager.getBestProvider(criteria, false);
                    Log.i(TAG, mLocationProvider);
                    LocationListener locationListener = new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            mLocationManager.removeUpdates(this);
                            Log.i(TAG, "Location Changed : (" + location.getLatitude() + "," + location.getLongitude() + ")");
                            //得到纬度
                            double latitude = location.getLatitude();
                            //得到经度
                            double longitude = location.getLongitude();

                            Log.i(TAG, "Location Changed : (" + longitude + "," + latitude + ")");

                            Geocoder gc = new Geocoder(DesignMaterialCaptureEditActivity.this, Locale.getDefault());
                            List<Address> locationList = null;
                            try {
                                locationList = gc.getFromLocation(latitude, longitude, 20);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            alLocation = new ArrayList<String>();
                            for (Address address : locationList) {
                                //Address address = locationList.get(0);//得到Address实例
                                String countryName = address.getCountryName();//得到国家名称，比如：中国
                                String locality = address.getLocality();//得到城市名称，比如：北京市
                                alLocation.add(address.getAddressLine(0) + "\r\n");
                                for (int i = 0; address.getAddressLine(i) != null; i++) {
                                    String addressLine = address.getAddressLine(i);//得到周边信息，包括街道等，i=0，得到街道名称
                                    Log.i(TAG, "addressLine = " + addressLine);
                                }
                            }
                            Collections.sort(alLocation, Collator.getInstance(Locale.CHINA));
                            showSampleReferencePointList();
                        }

                        @Override
                        public void onStatusChanged(String s, int i, Bundle bundle) {
                            Log.i(TAG, "onStatusChanged");
                        }

                        @Override
                        public void onProviderEnabled(String s) {
                            Log.i(TAG, "onProviderEnabled");
                        }

                        @Override
                        public void onProviderDisabled(String s) {
                            Log.i(TAG, "onProviderDisabled");
                        }
                    };

                    mLocationManager.requestLocationUpdates(mLocationProvider, 1000, 100, locationListener);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etMaterialName.getText().toString().equals("")) {
                    showAlert("物料名称必须填写，请检查！");
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btnBack.setEnabled(false);
                                btnFinish.setEnabled(false);
                                pb.setVisibility(View.VISIBLE);
                            }
                        });

                        try {
                            addList();
                            uploadDesignMaterialBean = myApp.NetHelperEx.getUploadDesignMaterialBean(mJson);
                            String mid = uploadDesignMaterialBean.getRet().replace("\'","");
                            String maker = ACache.get(DesignMaterialCaptureEditActivity.this).getAsString("Maker");
                            String base64 = ImageUtil.bitmapToBase64(mBitmap);
                            myApp.NetHelperEx.designMaterialUpload(mid, ".jpg", maker, base64);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (uploadDesignMaterialBean.getRes() == 0) {
                                    btnBack.setEnabled(true);
                                    btnFinish.setEnabled(true);
                                    pb.setVisibility(View.INVISIBLE);
                                    showConfirmDialog();
                                } else {
                                    btnBack.setEnabled(true);
                                    btnFinish.setEnabled(true);
                                    pb.setVisibility(View.INVISIBLE);
                                    showAlert("添加不成功！");
                                }
                            }
                        });
                    }
                }).start();
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

    private void addList() {
        mJson = "";
        mUploadJsonObject = new JSONObject();
        String seriesId = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            seriesId = myApp.NetHelper.getSeriesIDBean().getRet().get(0).getSeriesID();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            mUploadJsonObject.put("ID", 20001);
            mUploadJsonObject.put("SeriesID", seriesId);
            mUploadJsonObject.put("method", 1);
            if (!etMaterialName.getText().toString().equals("")) {

                mUploadJsonObject.put("cMaterialName", etMaterialName.getText());
            }
            if (!etColor.getText().toString().equals("")) {
                mUploadJsonObject.put("cColor", etColor.getText());
            }
            mUploadJsonObject.put("cMatTypeName", "APP物料");
            if (!etSupplierName.getText().toString().equals("")) {
                mUploadJsonObject.put("cSupplierName", etSupplierName.getText());
            }
            if (!etSupplierPhone.getText().toString().equals("")) {
                mUploadJsonObject.put("cSupplierPhone", etSupplierPhone.getText());
            }
            if (!etSupplierAddr.getText().toString().equals("")) {
                mUploadJsonObject.put("cSupplierAddr", etSupplierAddr.getText());
            }
            if (!etMoneyUnit.getText().toString().equals("")) {
                mUploadJsonObject.put("cMoneyUnit", etMoneyUnit.getText());
            }
            if (!etUnit.getText().toString().equals("")) {
                mUploadJsonObject.put("cUnit", etUnit.getText());
            }
            if (!etWidth.getText().toString().equals("")) {
                mUploadJsonObject.put("cWidth", etWidth.getText());
            }
            if (!etSpecs.getText().toString().equals("")) {
                mUploadJsonObject.put("cSpecs", etSpecs.getText());
            }
            if (!etLinkMan.getText().toString().equals("")) {
                mUploadJsonObject.put("cLinkMan", etLinkMan.getText());
            }
            if (!etPrice.getText().toString().equals("")) {
                mUploadJsonObject.put("dPrice", etPrice.getText());
            }
            mUploadJsonObject.put("Operator", ACache.get(DesignMaterialCaptureEditActivity.this).getAsString("Maker"));
            mUploadJsonObject.put("OperatorDate", sdf.format(new Date()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mJson = mUploadJsonObject.toString();

    }

    public void showConfirmDialog() {
        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("系统提示");
        builder.setMessage("上传成功！");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent it = new Intent(DesignMaterialCaptureEditActivity.this, DesignMaterialActivity.class);
                it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                it.putExtra("title", "设计样料");
                startActivity(it);
                DesignMaterialCaptureEditActivity.this.finish();
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        DesignMaterialCaptureEditActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission({
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    })
    protected void requestAction() {
    }


    void onRunSchedulerExampleButtonClicked() {
        disposables.add(sampleObservable()
                // Run on a background thread
                .subscribeOn(Schedulers.io())
                // Be notified on the main thread
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "onError()", e);
                    }

                    @Override
                    public void onNext(String string) {
                        Log.i(TAG, "onNext(" + string + ")");
                    }
                }));
    }

    static Observable<String> sampleObservable() {
        return Observable.defer(new Callable<ObservableSource<? extends String>>() {
            @Override
            public ObservableSource<? extends String> call() throws Exception {
                // Do some long running operation
                SystemClock.sleep(5000);
                return Observable.just("one", "two", "three", "four", "five");
            }
        });
    }

    private void showSampleReferencePointList() {
        if (alLocation == null) {
            return;
        }
        final String[] items = alLocation.toArray(new String[alLocation.size()]) ;
        android.support.v7.app.AlertDialog.Builder listDialog =
                new android.support.v7.app.AlertDialog.Builder(DesignMaterialCaptureEditActivity.this);
        listDialog.setTitle("选择附近地址")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        etSupplierAddr.setText(items[which]);
                    }
                })
                .show();
    }
}
