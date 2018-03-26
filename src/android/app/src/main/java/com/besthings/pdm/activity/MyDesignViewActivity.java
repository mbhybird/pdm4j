package com.besthings.pdm.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.besthings.pdm.R;
import com.besthings.pdm.utils.ImageUtil;

import java.io.IOException;

public class MyDesignViewActivity extends BaseActivity {

    String mPicUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_design_view);

        getSupportActionBar().hide();

        final Button btnCancel = (Button) findViewById(R.id.my_design_view_btnCancel);
        ImageView iv = (ImageView) findViewById(R.id.my_design_view_iv);

        if(getIntent().hasExtra("mid")) {
            iv.setScaleType(ImageView.ScaleType.FIT_CENTER);

            try {
                mPicUrl = myApp.NetHelperEx.getDesignFilePicBean(getIntent().getStringExtra("mid")).getRet();
            } catch (IOException e) {
                e.printStackTrace();
            }
            iv.setImageBitmap(ImageUtil.getURLImage(mPicUrl));
        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDesignViewActivity.this.finish();
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

}
