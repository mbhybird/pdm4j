package com.besthings.pdm.activity;

import android.app.Application;
import android.os.StrictMode;
import android.os.Handler;

import com.besthings.bean.UserPermissionRet;
import com.besthings.pdm.utils.Constant;
import com.besthings.pdm.utils.NetHelper;
import com.besthings.pdm.utils.NetHelperEx;
import com.blankj.utilcode.util.Utils;

import java.io.IOException;
import java.util.List;

import static com.besthings.pdm.utils.Constant.DEV_MODE;

/**
 * Created by NickChung on 29/08/2017.
 */

public class MyApplication extends Application {
    private static final String TAG = MyApplication.class.getSimpleName();
    public boolean isLogon = false;
    public List<UserPermissionRet> userPermissionRetList = null;
    public Constant Constant;
    public NetHelper NetHelper;
    public NetHelperEx NetHelperEx;
    public String mSeriesID;
    public Handler mHandler;
    public Runnable mRunnable;

    public MyApplication() {
        if (DEV_MODE) {
            StrictMode.setThreadPolicy(
                    new StrictMode
                            .ThreadPolicy
                            .Builder()
                            .detectDiskReads()
                            .detectDiskWrites()
                            .detectNetwork()
                            .penaltyLog()
                            .build());

            StrictMode.setVmPolicy(
                    new StrictMode
                            .VmPolicy
                            .Builder()
                            .detectLeakedSqlLiteObjects()
                            .detectLeakedClosableObjects()
                            .penaltyLog()
                            //.penaltyDeath()
                            .build());
        } else {
            StrictMode.setThreadPolicy(
                    new StrictMode
                            .ThreadPolicy
                            .Builder()
                            .permitAll()
                            .build());

            StrictMode.setVmPolicy(
                    new StrictMode
                            .VmPolicy
                            .Builder()
                            .build());
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);

        Utils.init(this);

        this.Constant = new Constant(this);
        this.NetHelper = new NetHelper(this.Constant);
        this.NetHelperEx = new NetHelperEx(this.Constant);

        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                getSeriesID();
                mHandler.postDelayed(this, 30 * 60 * 1000);
            }
        };
        mHandler.post(mRunnable);
    }

    private void getSeriesID() {
        try {
            mSeriesID = NetHelper.getSeriesIDBean().getRet().get(0).getSeriesID();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
