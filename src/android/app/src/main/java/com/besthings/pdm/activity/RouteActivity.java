package com.besthings.pdm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.besthings.pdm.R;
import com.besthings.pdm.utils.ACache;

import java.io.FileNotFoundException;

import static com.besthings.pdm.utils.Constant.SPLASH_DISPLAY_DURATION;

public class RouteActivity extends BaseActivity {

    private static final String TAG = RouteActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        getSupportActionBar().hide();

        /*
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Toast.makeText(RouteActivity.this,String.valueOf(dm.densityDpi),Toast.LENGTH_LONG).show();*/

        final MyApplication myApp = (MyApplication) getApplicationContext();
        try {
            if (ACache.get(RouteActivity.this).get("UserLogon") != null) {
                myApp.isLogon = Boolean.valueOf(ACache.get(RouteActivity.this).getAsString("UserLogon"));
            }
            else{
                ACache.get(RouteActivity.this).put("UserLogon", "false");
                myApp.isLogon = false;
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent it;
                if (myApp.isLogon) {
                    it = new Intent(RouteActivity.this, MainActivity.class);
                } else {
                    it = new Intent(RouteActivity.this, LoginActivity.class);
                }
                startActivity(it);
                RouteActivity.this.finish();
            }
        }, SPLASH_DISPLAY_DURATION);
    }

}
