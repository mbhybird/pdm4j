package com.besthings.pdm.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
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
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.besthings.pdm.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
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
public class DemoActivity extends BaseActivity {
    private static final String TAG = DemoActivity.class.getSimpleName();
    private final CompositeDisposable disposables = new CompositeDisposable();
    String mFilePath;
    LocationManager mLocationManager = null;
    String mLocationProvider = null;
    Location mLocation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        getSupportActionBar().hide();

        DemoActivityPermissionsDispatcher.requestActionWithPermissionCheck(this);

        onRunSchedulerExampleButtonClicked();

        findViewById(R.id.demo_btnPhotoPicker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoPicker();
            }
        });

        findViewById(R.id.demo_btnGetLocation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(DemoActivity.this);
                    dialog.setMessage("GPS未打开，是否打开?");
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            // 设置完成后返回到原来的界面
                            startActivityForResult(intent, 0);
                        }
                    });
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {

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
//                    if (mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) != null) {
//                        mLocationProvider = LocationManager.NETWORK_PROVIDER;
//                    } else {
//                        mLocationProvider = LocationManager.GPS_PROVIDER;
//                    }
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

                            Geocoder gc = new Geocoder(DemoActivity.this, Locale.getDefault());
                            List<Address> locationList = null;
                            try {
                                locationList = gc.getFromLocation(latitude, longitude, 20);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            EditText etLocationList = (EditText) findViewById(R.id.demo_etLocation);
                            etLocationList.setText("");
                            List<String> alLocation = new ArrayList<String>();
                            for (Address address : locationList) {
                                //Address address = locationList.get(0);//得到Address实例
                                String countryName = address.getCountryName();//得到国家名称，比如：中国
                                Log.i(TAG, "countryName = " + countryName);
                                String locality = address.getLocality();//得到城市名称，比如：北京市
                                Log.i(TAG, "locality = " + locality);
                                alLocation.add(address.getAddressLine(0) + "\r\n");
                                for (int i = 0; address.getAddressLine(i) != null; i++) {
                                    String addressLine = address.getAddressLine(i);//得到周边信息，包括街道等，i=0，得到街道名称
                                    Log.i(TAG, "addressLine = " + addressLine);
                                }
                            }
                            Collections.sort(alLocation, Collator.getInstance(Locale.CHINA));
                            etLocationList.setText(alLocation.toString());
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
    }

    public void photoPicker() {
        CharSequence[] items = {"相册", "相机"};
        new AlertDialog.Builder(this)
                .setTitle("选择图片来源")
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                            intent.setType("image/*");
                            startActivityForResult(Intent.createChooser(intent, "选择图片"), 0);
                        } else {
                            mFilePath = Environment.getExternalStorageDirectory() + "/temp.png";
                            File output = new File(mFilePath);
                            Uri imageUri = Uri.fromFile(output);
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            startActivityForResult(intent, 1);
                        }
                    }
                })
                .create().show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {
                //选择图片
                Uri uri = data.getData();
                ContentResolver cr = this.getContentResolver();
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                    ((ImageView) findViewById(R.id.demo_iv)).setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                FileInputStream fis = null;
                try {
                    //把图片转化为字节流
                    fis = new FileInputStream(mFilePath);
                    //把流转化图片
                    Bitmap bitmap = BitmapFactory.decodeStream(fis);
                    ((ImageView) findViewById(R.id.demo_iv)).setImageBitmap(bitmap);
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
        } else {
            Toast.makeText(DemoActivity.this, "请重新选择图片", Toast.LENGTH_SHORT).show();
        }
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
        DemoActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission({
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
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
}
