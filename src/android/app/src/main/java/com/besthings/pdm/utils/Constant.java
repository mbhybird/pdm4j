package com.besthings.pdm.utils;

import android.content.Context;

import java.io.IOException;

/**
 * Created by NickChung on 30/09/2017.
 */

public class Constant {
    public final String PDM_API_GET_URL_TEMPLATE = "http://%s:%s/getData.php";
    public final String PDM_API_GET_PIC_URL_TEMPLATE = "http://%s:%s/getPicture.php";
    public final String PDM_API_UPLOAD_URL_TEMPLATE = "http://%s:%s/uploadBase64.php";
    public final String PDM_API_SET_URL_TEMPLATE = "http://%s:%s/setData.php";
    public final String PDM_APK_URL_TEMPLATE = "http://%s:%s/appfile/pdm_#s.apk";

    public static final boolean DEV_MODE = true;
    public static final int SYSTEM_EXIT_INTERVAL = 3000;
    public static final int SPLASH_DISPLAY_DURATION = 2000;

    public static final int REQUEST_CODE_BRAND = 8000;
    public static final int REQUEST_CODE_YEAR = 8001;
    public static final int REQUEST_CODE_SEASON = 8002;
    public static final int REQUEST_CODE_THEME = 8003;
    public static final int REQUEST_CODE_BAND = 8004;
    public static final int REQUEST_CODE_MAKER = 8005;
    public static final int REQUEST_CODE_STYLE = 8006;
    public static final int REQUEST_CODE_STYLE_A = 8007;
    public static final int REQUEST_CODE_STYLE_B = 8008;
    public static final int REQUEST_CODE_TECHNICIAN = 8009;
    public static final int REQUEST_CODE_TEMPLETEMAN = 8010;
    public static final int REQUEST_CODE_EXPRESS_COMPANY = 8011;

    public Context mContext;
    public Constant(Context context) {
        mContext = context;
    }

    private String getRealURL(String template) {
        String server = "118.31.69.131";
        String port = "55988";
        try {
            if (ACache.get(mContext).get("Server") != null) {
                server = ACache.get(mContext).getAsString("Server");
            }
            if (ACache.get(mContext).get("Port") != null) {
                port = ACache.get(mContext).getAsObject("Port").toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return String.format(template, server, port);
    }

    public String PDM_API_GET_URL() {
        return getRealURL(PDM_API_GET_URL_TEMPLATE);
    }

    public String PDM_API_GET_PIC_URL() {
        return getRealURL(PDM_API_GET_PIC_URL_TEMPLATE);
    }

    public String PDM_API_UPLOAD_URL() {
        return getRealURL(PDM_API_UPLOAD_URL_TEMPLATE);
    }

    public String PDM_API_SET_URL() {
        return getRealURL(PDM_API_SET_URL_TEMPLATE);
    }

    public String PDM_APK_URL() {
        return getRealURL(PDM_APK_URL_TEMPLATE);
    }
}
