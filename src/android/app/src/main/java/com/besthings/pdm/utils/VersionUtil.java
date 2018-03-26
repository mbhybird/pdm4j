package com.besthings.pdm.utils;

/**
 * Created by NickChung on 13/09/2017.
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;

import com.besthings.pdm.activity.MyApplication;

import java.io.File;
import java.io.IOException;

public class VersionUtil {
    private Context mContext;
    private int mCurrentVersionCode;
    private int mLatestVersionCode;
    MyApplication myApp;

    public String getPathSDCard() {
        return mPathSDCard;
    }

    public String getPathAPK() {
        return mPathAPK;
    }

    private String mPathSDCard = Environment.getExternalStorageDirectory() + "/";
    private String mPathAPK = "com.besthings.pdm/apk/";

    public VersionUtil(Context context) {
        this.mContext = context;
        myApp = (MyApplication) context.getApplicationContext();
    }

    public int getVersionCode() {
        return getPackageInfo().versionCode;
    }

    public String getVersionName() {
        return getPackageInfo().versionName;
    }

    public String getPackageURL() {
        return String.format(myApp.Constant.PDM_APK_URL().replace("#","%"), this.mLatestVersionCode);
    }

    private PackageInfo getPackageInfo() {
        PackageInfo pi = null;
        try {
            PackageManager pm = this.mContext.getPackageManager();
            pi = pm.getPackageInfo(this.mContext.getPackageName(), PackageManager.GET_CONFIGURATIONS);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

    public boolean isNeedUpdate() {
        return this.mCurrentVersionCode < this.mLatestVersionCode;
    }

    public void checkVersionCode() {
        try {
            int apkVersionCode = Integer.valueOf(myApp.NetHelper.base64Decode(myApp.NetHelper.getAppVersionBean().getRet().getData()));
            this.mLatestVersionCode = apkVersionCode;
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.mCurrentVersionCode = this.getVersionCode();
    }

    public String getFileName() {
        return String.format("pdm_%s.apk", this.mLatestVersionCode);
    }

    public void install() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String fileFullPath = this.getPathSDCard().concat(this.getPathAPK()).concat(this.getFileName());
        intent.setDataAndType(Uri.fromFile(new File(fileFullPath)), "application/vnd.android.package-archive");
        this.mContext.startActivity(intent);
        ((Activity) this.mContext).finish();
        System.exit(0);
    }
}

