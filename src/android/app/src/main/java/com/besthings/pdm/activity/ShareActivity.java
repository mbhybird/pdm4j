package com.besthings.pdm.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.besthings.pdm.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.IOException;

public class ShareActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        setCustomActionBar(true);

        //android
        ImageView androidQrCode = (ImageView) findViewById(R.id.share_iv_android_qrCode);
        String apkVersionCode = null;
        String apkURL = null;
        try {
            apkVersionCode = myApp.NetHelper.base64Decode(myApp.NetHelper.getAppVersionBean().getRet().getData());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (apkVersionCode != null) {
            apkURL = String.format(myApp.Constant.PDM_APK_URL().replace("#","%"), apkVersionCode);
            androidQrCode.setImageBitmap(encodeAsBitmap(apkURL));
        }

        //iOS
        String iOSURL = null;
        iOSURL = apkURL;
        ImageView iOSQrCode = (ImageView) findViewById(R.id.share_iv_ios_qrCode);
        iOSQrCode.setImageBitmap(encodeAsBitmap(iOSURL));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
        }
        return true;
    }

    private Bitmap encodeAsBitmap(String content) {
        Bitmap bitmap = null;
        BitMatrix result = null;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            result = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, 400, 400);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(result);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException iae) {
            return null;
        }

        return bitmap;
    }
}
