package com.besthings.utils;

import com.besthings.bean.CostAccountsBean;
import com.besthings.bean.DesignFileListBean;
import com.besthings.bean.DesignMaterialBean;
import com.besthings.bean.GraphicNotesBean;
import com.besthings.bean.GraphicNotesPicBean;
import com.besthings.common.Constant;

import java.io.IOException;

/**
 * Created by NickChung on 18/09/2017.
 */

@Deprecated
public class NetHelperEx extends NetHelper {

    public static final String getGraphicNotesJson(String mID) throws IOException {
        String seriesID = getSeriesIDBean().getRet().get(0).getSeriesID();
        return post(Constant.PDM_API_GET_URL,
                String.format("{\"ID\":4009,\"SeriesID\":\"%s\",\"MID\":\"%s\"}", seriesID, mID));
    }

    public static final GraphicNotesBean getGraphicNotesBean(String mID) throws IOException {
        return mapper.readValue(getGraphicNotesJson(mID), GraphicNotesBean.class);
    }

    public static final String getGraphicNotesPicJson(String mID) throws IOException {
        String seriesID = getSeriesIDBean().getRet().get(0).getSeriesID();
        return post(Constant.PDM_API_GET_URL,
                String.format("{\"ID\":4010,\"SeriesID\":\"%s\",\"MID\":\"%s\",\"PicType\":1}", seriesID, mID));
    }

    public static final GraphicNotesPicBean getGraphicNotesPicBean(String mID) throws IOException {
        return mapper.readValue(getGraphicNotesPicJson(mID), GraphicNotesPicBean.class);
    }

    public static final String getDesignFileListJson(String json) throws IOException {
        String seriesID = getSeriesIDBean().getRet().get(0).getSeriesID();
        return post(Constant.PDM_API_GET_URL,
                String.format("{\"ID\":4001,\"SeriesID\":\"%s\",%s,\"NumPerPg\":1000}", seriesID, json));
    }

    public static final DesignFileListBean getDesignFileListBean(String json) throws IOException {
        return mapper.readValue(getDesignFileListJson(json), DesignFileListBean.class);
    }

    public static final String auditDesignFile(int type, String mID, String Operator) throws IOException {
        String seriesID = getSeriesIDBean().getRet().get(0).getSeriesID();
        String uploadJson = "{\"ID\":4004,\"SeriesID\":\"%s\",\"MID\":\"%s\",\"Type\":%s,\"Operator\":\"%s\"}";
        return post(Constant.PDM_API_SET_URL, String.format(uploadJson, seriesID, mID, type, Operator));
    }

    public static final String notesUpload(String lID, String type, String maker, String fileExt, String base64) throws IOException {
        String seriesID = getSeriesIDBean().getRet().get(0).getSeriesID();
        String uploadJson = "{\"ID\":1,\"SeriesID\":\"%s\",\"LID\":\"%s\",\"Type\":\"%s\",\"Maker\":\"%s\",\"Remark\":\"款图批注\",\"FileExt\":\"%s\",\"picture\":\"%s\"}";
        return postGB(Constant.PDM_API_UPLOAD_URL, String.format(uploadJson, seriesID, lID, type, maker, fileExt, base64));
    }

    public static final String getDesignMaterialJson(String json) throws IOException {
        return post(Constant.PDM_API_GET_URL, json);
    }

    public static final DesignMaterialBean getDesignMaterialBean(String json) throws IOException {
        return mapper.readValue(getDesignMaterialJson(json), DesignMaterialBean.class);
    }

    public static final boolean uploadDesignMaterial(String json) throws IOException {
        String resultJson = post(Constant.PDM_API_SET_URL, json);
        return resultJson.contains("\"Res\":0");
    }

    public static final String getCostAccountsJson(String json) throws IOException {
        return post(Constant.PDM_API_GET_URL, json);
    }

    public static final CostAccountsBean getCostAccountsBean(String json) throws IOException {
        return mapper.readValue(getCostAccountsJson(json), CostAccountsBean.class);
    }

}
