package com.besthings.pdm.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;

import java.io.IOException;

import android.widget.Toast;

import com.besthings.bean.UserLoginBean;
import com.besthings.bean.UserPermissionBean;
import com.besthings.bean.UserQRBean;
import com.besthings.pdm.R;
import com.besthings.pdm.fragment.MyGridFragment;
import com.besthings.pdm.fragment.MyListFragment;
import com.besthings.pdm.utils.ACache;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    MyApplication myApp;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content, new MyGridFragment())
                            .addToBackStack(null)
                            .commit();
                    return true;
                case R.id.navigation_scan:
                    //Toast.makeText(MainActivity.this, "Scan QRCode...", Toast.LENGTH_LONG).show();
                    new IntentIntegrator(MainActivity.this)
                            .setOrientationLocked(false)
                            .initiateScan();
                    return true;
                case R.id.navigation_profile:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content, new MyListFragment())
                            .addToBackStack(null)
                            .commit();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                if (result.getContents() == null) {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                } else {
                    UserQRBean userQRBean = myApp.NetHelper.mapper.readValue(result.getContents(), UserQRBean.class);
                    if (userQRBean != null) {
                        ACache.get(MainActivity.this).put("Server", userQRBean.getSvr());
                        ACache.get(MainActivity.this).put("Port", userQRBean.getPort());
                        UserLoginBean userLoginBean = myApp.NetHelper.getUserLoginBean(userQRBean.getMID(), "", "");
                        if (userLoginBean != null && userLoginBean.getRes() == 0) {
                            ACache.get(MainActivity.this).put("Maker", userLoginBean.getRet().getMaker());
                            ACache.get(MainActivity.this).put("UserName", userLoginBean.getRet().getUserName());
                            ACache.get(MainActivity.this).put("MobilePhone", userLoginBean.getRet().getMobilePhone());

                            loadUserPermissionView();
                        } else {
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("系统提示")
                                    .setMessage("扫码登录失败！\r\n1.请检查网络是否正常 \r\n2.二维码是否合法")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                            myApp.userPermissionRetList = null;
                                            ACache.get(MainActivity.this).remove("UserLogon");
                                            ACache.get(MainActivity.this).remove("Maker");
                                            ACache.get(MainActivity.this).remove("UserName");
                                            ACache.get(MainActivity.this).remove("MobilePhone");

                                            Intent it = new Intent(MainActivity.this, LoginActivity.class);
                                            startActivity(it);
                                            MainActivity.this.finish();

                                        }
                                    }).show();
                        }
                    } else {
                        MainActivity.this.showAlert("扫码登录失败！\r\n1.请检查网络是否正常 \r\n2.二维码是否合法");
                    }
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }

        } catch (Exception e) {
            e.printStackTrace();
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("系统提示")
                    .setMessage("扫码登录失败！\r\n1.请检查网络是否正常 \r\n2.二维码是否合法")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            myApp.userPermissionRetList = null;
                            ACache.get(MainActivity.this).remove("UserLogon");
                            ACache.get(MainActivity.this).remove("Maker");
                            ACache.get(MainActivity.this).remove("UserName");
                            ACache.get(MainActivity.this).remove("MobilePhone");

                            Intent it = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(it);
                            MainActivity.this.finish();

                        }
                    }).show();
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myApp = (MyApplication) getApplication();

        if (savedInstanceState == null) {
            if (myApp.userPermissionRetList == null) {
                loadUserPermissionView();
            }
        }

        setCustomActionBar(false);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        /*
        try {
            AssetFileDescriptor afd = getAssets().openFd("alarm.mp3");
            MediaPlayer mp = new MediaPlayer();
            mp.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(), afd.getLength());
            mp.prepare();
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });
        }
        catch(IOException e){
            e.printStackTrace();
        }*/

        /*
        try {
            Toast.makeText(MainActivity.this, NetHelper.getSeriesIDBean().getRet().get(0).getSeriesID(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    private void loadUserPermissionView() {
        try {
            UserPermissionBean userPermissionBean = myApp.NetHelper.getUserPermissionBean(ACache.get(MainActivity.this).getAsString("Maker"));
            if (userPermissionBean != null) {
                myApp.userPermissionRetList = userPermissionBean.getRet();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, new MyGridFragment())
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }
}
