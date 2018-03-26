package com.besthings.utils;

import com.besthings.common.Constant;
import com.besthings.bean.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.apache.commons.codec.binary.Base64;

/**
 * Created by NickChung on 29/08/2017.
 */

@Deprecated
public class NetHelper {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public static final MediaType JSON_GB
            = MediaType.parse("application/json; charset=gb2312");

    static final OkHttpClient client = new OkHttpClient();
    public static final ObjectMapper mapper = new ObjectMapper();

    public static final String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static final String postGB(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON_GB, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static final String getSeriesIDJson() throws IOException {
        return post(Constant.PDM_API_GET_URL, "{\"ID\":1001,\"appid\":\"1880188\",\"sign\":\"e501c5cdba4c1b55fef39b664044e26d\"}");
    }

    public static final SeriesIDBean getSeriesIDBean() throws IOException {
        return mapper.readValue(getSeriesIDJson(), SeriesIDBean.class);
    }

    public static final String getUserLoginJson(String mID, String phoneNo, String password) throws IOException {
        String seriesID = getSeriesIDBean().getRet().get(0).getSeriesID();
        if (mID != "") {
            return post(Constant.PDM_API_GET_URL,
                    String.format("{\"ID\":1002,\"SeriesID\":\"%s\",\"mid\":\"%s\"}", seriesID, mID));
        } else {
            String hashPassword = "";
            try {
                hashPassword = md5(password);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return post(Constant.PDM_API_GET_URL,
                    String.format("{\"ID\":1002,\"SeriesID\":\"%s\",\"phoneNo\":\"%s\",\"password\":\"%s\"}"
                            , seriesID, phoneNo, hashPassword));
        }
    }

    public static final String md5(String input) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(input.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static final String base64Decode(String str) {
        return new String(new Base64().decode(str.getBytes()));
    }

    public static final UserLoginBean getUserLoginBean(String mID, String phoneNo, String password) throws IOException {
        return mapper.readValue(getUserLoginJson(mID, phoneNo, password), UserLoginBean.class);
    }

    public static final String getUserPermissionJson(String maker) throws IOException {
        String seriesID = getSeriesIDBean().getRet().get(0).getSeriesID();
        return post(Constant.PDM_API_GET_URL,
                String.format("{\"ID\":7503,\"SeriesID\":\"%s\",\"Maker\":\"%s\"}", seriesID, maker));
    }

    public static final UserPermissionBean getUserPermissionBean(String maker) throws IOException {
        return mapper.readValue(getUserPermissionJson(maker), UserPermissionBean.class);
    }

    public static final String getAppVersionJson() throws IOException {
        String seriesID = getSeriesIDBean().getRet().get(0).getSeriesID();
        return post(Constant.PDM_API_GET_URL, String.format("{\"ID\":17735,\"SeriesID\":\"%s\"}", seriesID));
    }

    public static final AppVersionBean getAppVersionBean() throws IOException {
        return mapper.readValue(getAppVersionJson(), AppVersionBean.class);
    }

    public static final String uploadPicture(int type, String mID, String fileExt, String base64) throws IOException {
        String seriesID = getSeriesIDBean().getRet().get(0).getSeriesID();
        String uploadJson = "{\"Type\":%s,\"SeriesID\":\"%s\",\"MID\":\"%s\",\"FileExt\":\"%s\",\"picture\":\"%s\"}";
        return post(Constant.PDM_API_UPLOAD_URL, String.format(uploadJson, type, seriesID, mID, fileExt, base64));
    }

    public static final String uploadPicture(int type, String mID, String fileExt, String maker, String base64) throws IOException {
        String seriesID = getSeriesIDBean().getRet().get(0).getSeriesID();
        String uploadJson = "{\"Type\":%s,\"SeriesID\":\"%s\",\"MID\":\"%s\",\"FileExt\":\"%s\",\"Maker\":\"%s\",\"picture\":\"%s\"}";
        return post(Constant.PDM_API_UPLOAD_URL, String.format(uploadJson, type, seriesID, mID, fileExt, maker, base64));
    }

    public static final String getMyDesignJson(String json) throws IOException {
        return post(Constant.PDM_API_GET_URL, json);
    }

    public static DesignFileListBean getMyDesignBean(String json) throws IOException {
        return mapper.readValue(getMyDesignJson(json), DesignFileListBean.class);
    }

    public static final String getBrandJson(String seriesID) throws IOException {
        return post(Constant.PDM_API_GET_URL,
                String.format("{\"ID\":3201,\"SeriesID\":\"%s\"}", seriesID));
    }

    public static BrandBean getBrandBean(String seriesID) throws IOException {
        return mapper.readValue(getBrandJson(seriesID), BrandBean.class);
    }

    public static final String getStyleJson(String seriesID) throws IOException {
        return post(Constant.PDM_API_GET_URL,
                String.format("{\"ID\":3301,\"SeriesID\":\"%s\"}", seriesID));
    }

    public static StyleBean getStyleBean(String seriesID) throws IOException {
        return mapper.readValue(getStyleJson(seriesID), StyleBean.class);
    }

    public static final String getThemeJson(String seriesID) throws IOException {
        return post(Constant.PDM_API_GET_URL,
                String.format("{\"ID\":7504,\"SeriesID\":\"%s\"}", seriesID));
    }

    public static BaseBean getThemeBean(String seriesID) throws IOException {
        return mapper.readValue(getThemeJson(seriesID), BaseBean.class);
    }

    public static final String getBandJson(String seriesID) throws IOException {
        return post(Constant.PDM_API_GET_URL,
                String.format("{\"ID\":7505,\"SeriesID\":\"%s\"}", seriesID));
    }

    public static BaseBean getBandBean(String seriesID) throws IOException {
        return mapper.readValue(getBandJson(seriesID), BaseBean.class);
    }

    public static final String getYearJson(String seriesID) throws IOException {
        return post(Constant.PDM_API_GET_URL,
                String.format("{\"ID\":7506,\"SeriesID\":\"%s\"}", seriesID));
    }

    public static BaseBean getYeanBean(String seriesID) throws IOException {
        return mapper.readValue(getYearJson(seriesID), BaseBean.class);
    }

    public static final String getSeasonJson(String seriesID) throws IOException {
        return post(Constant.PDM_API_GET_URL,
                String.format("{\"ID\":7507,\"SeriesID\":\"%s\"}", seriesID));
    }

    public static BaseBean getSeasonBean(String seriesID) throws IOException {
        return mapper.readValue(getSeasonJson(seriesID), BaseBean.class);
    }

    public static boolean addNewDesign(String seriesID,String styleNo, String brand, String maker, String band, String style,
                                       String year, String season, String theme, String styleName, String base64) throws IOException {
        String json = "" +
                "{\"ID\":4015" +
                ",\"SeriesID\":\"%s\"" +
                ",\"cFileExt\":\"%s\"" +
                ",\"cStyleNo\":\"%s\"" +
                ",\"cBrand\":\"%s\"" +
                ",\"cMaker\":\"%s\"" +
                ",\"cBandName\":\"%s\"" +
                ",\"cStyleA\":\"%s\"" +
                ",\"cStyleB\":\"%s\"" +
                ",\"iYear\":\"%s\"" +
                ",\"cSeason\":\"%s\"" +
                ",\"cTheme\":\"%s\"" +
                ",\"cStyleCName\":\"%s\"" +
                ",\"cDesigner\":\"%s\"" +
                ",\"iPicture\":\"%s\"}";

        String resultJson = post(Constant.PDM_API_SET_URL, String.format(
                json, seriesID, ".png", styleNo, brand, maker, band, style.split("/")[0], style.split("/")[1],
                year, season, theme, styleName, maker, base64));

        return resultJson.contains("\"Res\":0");

    }

    public static final String getBuyerSampleJson(String json) throws IOException {
        return post(Constant.PDM_API_GET_URL, json);
    }

    public static BuyerSampleBean getBuyerSampleBean(String json) throws IOException {
        return mapper.readValue(getBuyerSampleJson(json), BuyerSampleBean.class);
    }
}


