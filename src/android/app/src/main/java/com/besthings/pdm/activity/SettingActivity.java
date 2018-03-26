package com.besthings.pdm.activity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.besthings.pdm.R;
import com.besthings.pdm.utils.ACache;
import com.besthings.pdm.utils.VersionUtil;

import java.io.FileNotFoundException;

public class SettingActivity extends BaseActivity {
    VersionUtil versionUtil;
    DownloadCompleteReceiver completeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        setCustomActionBar(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 0);
            }
        }

        final MyApplication myApp = (MyApplication) getApplication();
        final TextView tvVersion = (TextView) findViewById(R.id.setting_tv_app_version);
        versionUtil = new VersionUtil(this);
        tvVersion.setText("v" + versionUtil.getVersionName());
        completeReceiver = new DownloadCompleteReceiver();
        /** register download success broadcast **/
        registerReceiver(completeReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        RelativeLayout rlAppUpdate = (RelativeLayout) findViewById(R.id.setting_layout_appUpdate);
        rlAppUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                versionUtil.checkVersionCode();
                if (versionUtil.isNeedUpdate()) {
                    Toast.makeText(SettingActivity.this, "检测到新版本，APP下载中...", Toast.LENGTH_LONG).show();
                    downloadNewPackage();
                } else {
                    showAlert("已经是最新版本了");
                }
            }
        });

        Button btnSignOut = (Button) findViewById(R.id.setting_btn_signOut);
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myApp.userPermissionRetList = null;
                ACache.get(SettingActivity.this).remove("UserLogon");
                ACache.get(SettingActivity.this).remove("Maker");
                ACache.get(SettingActivity.this).remove("UserName");
                ACache.get(SettingActivity.this).remove("MobilePhone");

                Intent it = new Intent(SettingActivity.this, LoginActivity.class);
                it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(it);
                SettingActivity.this.finish();
            }
        });

        final CheckBox chkNotification = (CheckBox) findViewById(R.id.setting_chkNotification);
        final CheckBox chkRemember = (CheckBox) findViewById(R.id.setting_chkRemember);
        final CheckBox chkNotifiedBySound = (CheckBox) findViewById(R.id.setting_chkNotifiedBySound);
        final TextView tvServerURL = (TextView) findViewById(R.id.setting_tv_serverURL);

        boolean isNotificationOn = false;
        boolean isRememberOn = false;
        boolean isNotifiedBySoundOn = false;
        try {
            if (ACache.get(SettingActivity.this).get("Notification") != null) {
                isNotificationOn = Boolean.valueOf(ACache.get(SettingActivity.this).getAsString("Notification"));
            }
            if (ACache.get(SettingActivity.this).get("UserLogon") != null) {
                isRememberOn = Boolean.valueOf(ACache.get(SettingActivity.this).getAsString("UserLogon"));
            }
            if (ACache.get(SettingActivity.this).get("NotifiedBySound") != null) {
                isNotifiedBySoundOn = Boolean.valueOf(ACache.get(SettingActivity.this).getAsString("NotifiedBySound"));
            }

            if(ACache.get(SettingActivity.this).get("Server") != null && ACache.get(SettingActivity.this).get("Port") != null) {
                String server = ACache.get(SettingActivity.this).getAsString("Server");
                String port = ACache.get(SettingActivity.this).getAsObject("Port").toString();
                tvServerURL.setText(String.format("当前服务器：%s:%s", server, port));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        chkNotification.setChecked(isNotificationOn);
        chkRemember.setChecked(isRememberOn);
        chkNotifiedBySound.setChecked(isNotifiedBySoundOn);

        chkNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chkNotification.isChecked()) {
                    ACache.get(SettingActivity.this).put("Notification", "true");
                } else {
                    ACache.get(SettingActivity.this).put("Notification", "false");
                }
            }
        });

        chkRemember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chkRemember.isChecked()) {
                    ACache.get(SettingActivity.this).put("UserLogon", "true");
                } else {
                    ACache.get(SettingActivity.this).put("UserLogon", "false");
                }
            }
        });

        chkNotifiedBySound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chkNotifiedBySound.isChecked()) {
                    ACache.get(SettingActivity.this).put("NotifiedBySound", "true");
                } else {
                    ACache.get(SettingActivity.this).put("NotifiedBySound", "false");
                }
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

    private void downloadNewPackage() {
        DownloadManager manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        //创建下载请求
        DownloadManager.Request down = new DownloadManager.Request(Uri.parse(versionUtil.getPackageURL()));
        //设置允许使用的网络类型，这里是移动网络和wifi都可以
        down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        //后台下载
        down.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        //显示下载界面
        down.setVisibleInDownloadsUi(true);
        //设置下载后文件存放的位置
        down.setDestinationInExternalPublicDir(versionUtil.getPathAPK(), versionUtil.getFileName());
        //将下载请求放入队列
        manager.enqueue(down);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (completeReceiver != null) unregisterReceiver(completeReceiver);
    }

    @Override
    protected void onResume() {
        registerReceiver(completeReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        super.onResume();
    }

    //接受下载完成后的intent
    private class DownloadCompleteReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                versionUtil.install();
            }
        }
    }
}
