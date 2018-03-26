package com.besthings.pdm.utils;

import android.text.TextUtils;

import com.besthings.bean.AnalysisOfResearchChartBean;
import com.besthings.bean.AnalysisOfResearchStatisticalSummaryBean;
import com.besthings.bean.BaseBean;
import com.besthings.bean.BuyerSampleDetailBean;
import com.besthings.bean.BuyerSampleNotesBean;
import com.besthings.bean.CostAccountsBean;
import com.besthings.bean.DesignFileListBean;
import com.besthings.bean.DesignFileListPicBean;
import com.besthings.bean.DesignMaterialBean;
import com.besthings.bean.GraphicNotesBean;
import com.besthings.bean.GraphicNotesPicBean;
import com.besthings.bean.MakerBean;
import com.besthings.bean.MaterialPurchaseDetailListBean;
import com.besthings.bean.MaterialPurchaseListBean;
import com.besthings.bean.OrderNumChartBean;
import com.besthings.bean.PersonCustomBean;
import com.besthings.bean.ResponseBean;
import com.besthings.bean.SampleDetailBean;
import com.besthings.bean.SampleFlowRecordBean;
import com.besthings.bean.SampleManagerBean;
import com.besthings.bean.SampleManagerEditionRecordBean;
import com.besthings.bean.SamplePictureBean;
import com.besthings.bean.SpecialTechnicsBean;
import com.besthings.bean.SpecialTechnicsDetailBean;
import com.besthings.bean.StyleAndCategoryBean;
import com.besthings.bean.TeamClothesCustomOrderDetailListBean;
import com.besthings.bean.TeamClothesCustomOrderListBean;
import com.besthings.bean.UploadBuyerSampleBean;
import com.besthings.bean.UploadDesignMaterialBean;
import com.besthings.bean.UserListBean;
import com.besthings.bean.WarningTipsPersonCustomBean;

import java.io.File;
import java.io.IOException;

/**
 * Created by NickChung on 18/09/2017.
 */

public class NetHelperEx extends NetHelper {
    public NetHelperEx(Constant constant) {
        super(constant);
    }

    public final String getGraphicNotesJson(String mID) throws IOException {
        String seriesID = getSeriesIDBean().getRet().get(0).getSeriesID();
        return post(mConstant.PDM_API_GET_URL(),
                String.format("{\"ID\":4009,\"SeriesID\":\"%s\",\"MID\":\"%s\"}", seriesID, mID));
    }

    public final GraphicNotesBean getGraphicNotesBean(String mID) throws IOException {
        return mapper.readValue(getGraphicNotesJson(mID), GraphicNotesBean.class);
    }

    public final String getGraphicNotesPicJson(String mID) throws IOException {
        String seriesID = getSeriesIDBean().getRet().get(0).getSeriesID();
        return post(mConstant.PDM_API_GET_URL(),
                String.format("{\"ID\":4010,\"SeriesID\":\"%s\",\"MID\":\"%s\",\"PicType\":1}", seriesID, mID));
    }

    public final GraphicNotesPicBean getGraphicNotesPicBean(String mID) throws IOException {
        return mapper.readValue(getGraphicNotesPicJson(mID), GraphicNotesPicBean.class);
    }

    public final String getDesignFileListJson(String json) throws IOException {
        String seriesID = getSeriesIDBean().getRet().get(0).getSeriesID();
        return post(mConstant.PDM_API_GET_URL(),
                String.format("{\"ID\":4001,\"SeriesID\":\"%s\",%s}", seriesID, json));
    }

    public final DesignFileListBean getDesignFileListBean(String json) throws IOException {
        return mapper.readValue(getDesignFileListJson(json), DesignFileListBean.class);
    }

    public final String auditDesignFile(int type, String mID, String Operator) throws IOException {
        String seriesID = getSeriesIDBean().getRet().get(0).getSeriesID();
        String uploadJson = "{\"ID\":4004,\"SeriesID\":\"%s\",\"MID\":\"%s\",\"Type\":%s,\"Operator\":\"%s\"}";
        return post(mConstant.PDM_API_SET_URL(), String.format(uploadJson, seriesID, mID, type, Operator));
    }

    public final String notesUpload(String lID, String type, String maker, String fileExt, String base64) throws IOException {
        String seriesID = getSeriesIDBean().getRet().get(0).getSeriesID();
        String uploadJson = "{\"ID\":1,\"SeriesID\":\"%s\",\"LID\":\"%s\",\"Type\":\"%s\",\"Maker\":\"%s\",\"Remark\":\"款图批注\",\"FileExt\":\"%s\",\"picture\":\"%s\"}";
        return post(mConstant.PDM_API_UPLOAD_URL(), String.format(uploadJson, seriesID, lID, type, maker, fileExt, base64));
    }

    public final String getDesignMaterialJson(String json) throws IOException {
        return post(mConstant.PDM_API_GET_URL(), json);
    }

    public final DesignMaterialBean getDesignMaterialBean(String json) throws IOException {
        return mapper.readValue(getDesignMaterialJson(json), DesignMaterialBean.class);
    }

    public final String uploadDesignMaterial(String json) throws IOException {
        return post(mConstant.PDM_API_SET_URL(), json);
    }

    public final String getCostAccountsJson(String json) throws IOException {
        return post(mConstant.PDM_API_GET_URL(), json);
    }

    public final CostAccountsBean getCostAccountsBean(String json) throws IOException {
        return mapper.readValue(getCostAccountsJson(json), CostAccountsBean.class);
    }

    public final UploadDesignMaterialBean getUploadDesignMaterialBean(String json) throws IOException {
        return mapper.readValue(uploadDesignMaterial(json), UploadDesignMaterialBean.class);
    }

    public final String designMaterialUpload(String mID, String ext, String Maker, String base64) throws IOException {
        String seriesID = getSeriesIDBean().getRet().get(0).getSeriesID();
        String uploadJson = "{\"Type\":6,\"SeriesID\":\"%s\",\"MID\":\"%s\",\"FileExt\":\"%s\",\"Maker\":\"%s\",\"picture\":\"%s\"}";
        return post(mConstant.PDM_API_UPLOAD_URL(), String.format(uploadJson, seriesID, mID, ext, Maker, base64));
    }

    public final String getDesignFilePicJson(String mID) throws IOException {
        String seriesID = getSeriesIDBean().getRet().get(0).getSeriesID();
        String uploadJson = "{\"ID\":5003,\"SeriesID\":\"%s\",\"MID\":\"%s\",\"Type\":0}";
        return post(mConstant.PDM_API_GET_PIC_URL(), String.format(uploadJson, seriesID, mID));
    }

    public final DesignFileListPicBean getDesignFilePicBean(String mID) throws IOException {
        return mapper.readValue(getDesignFilePicJson(mID), DesignFileListPicBean.class);
    }

    public final String getSampleManagerJson(String json) throws IOException {
        return post(mConstant.PDM_API_GET_URL(), json);
    }

    public SampleManagerBean getSampleManagerBean(String json) throws IOException {
        return mapper.readValue(getSampleManagerJson(json), SampleManagerBean.class);
    }

    public final String getUserListJson() throws IOException {
        String seriesID = getSeriesIDBean().getRet().get(0).getSeriesID();
        String uploadJson = "{\"ID\":3101,\"SeriesID\":\"%s\"}";
        return post(mConstant.PDM_API_GET_URL(), String.format(uploadJson, seriesID));
    }

    public UserListBean getUserListBean() throws IOException {
        return mapper.readValue(getUserListJson(), UserListBean.class);
    }

    public final String addBuyerSample(String json, String base64, File imageFile) throws IOException {
        String uploadJson = "{\"ID\":9010, %s";

        if (!TextUtils.isEmpty(base64)) {
            uploadJson += ",\"iPicture\":\"%s\"}";
            return post(mConstant.PDM_API_SET_URL(), String.format(uploadJson, json, base64));
        } else {
            uploadJson += "}";
            return postFormData(mConstant.PDM_API_SET_URL(), String.format(uploadJson, json), imageFile);
        }
    }

    public final UploadBuyerSampleBean addBuyerSampleBean(String json, String base64, File imageFile) throws IOException {
        return mapper.readValue(addBuyerSample(json, base64, imageFile), UploadBuyerSampleBean.class);
    }

    public final String addSampleCourseRecord(String json) throws IOException {
        return post(mConstant.PDM_API_SET_URL(), String.format(
                "{\"ID\":9007,%s}",
                json
        ));
    }

    public final String getSampleFlowRecordJson(String mID) throws IOException {
        String seriesID = getSeriesIDBean().getRet().get(0).getSeriesID();
        String uploadJson = "{\"ID\":9006,\"SeriesID\":\"%s\",\"Time\":\"time2000\",\"MID\":\"%s\",\"NumPerPg\":1000}";
        return post(mConstant.PDM_API_GET_URL(), String.format(uploadJson, seriesID, mID));
    }


    public final SampleFlowRecordBean getSampleFlowRecordBean(String mID) throws IOException {
        return mapper.readValue(getSampleFlowRecordJson(mID), SampleFlowRecordBean.class);
    }

    public final boolean addSampleNotes(String seriesID, String lID, String type, String maker, String notes, String remark, String fileExt, String base64) throws IOException {
        String uploadJson = "{\"ID\":1,\"SeriesID\":\"%s\",\"LID\":\"%s\",\"Type\":\"%s\",\"Maker\":\"%s\",\"Remark\":\"%s\",\"Remark1\":\"%s\",\"FileExt\":\"%s\",\"picture\":\"%s\"}";
        String resultJson = post(mConstant.PDM_API_UPLOAD_URL(), String.format(uploadJson, seriesID, lID, type, maker, notes, remark, fileExt, base64));
        return resultJson.contains("\"Res\":0");
    }

    public final boolean getBuyerSampleDetail(String mID) throws IOException {
        String seriesID = getSeriesIDBean().getRet().get(0).getSeriesID();
        String uploadJson = "{\"ID\":23001,\"SeriesID\":\"%s\",\"MID\":\"%s\",\"State\":1}";
        String resultJson = post(mConstant.PDM_API_GET_URL(), String.format(uploadJson, seriesID, mID));
        return resultJson.contains("\"MID\":");
    }

    public final String getBuyerSampleDetailJson(String mID) throws IOException {
        String seriesID = getSeriesIDBean().getRet().get(0).getSeriesID();
        String uploadJson = "{\"ID\":23001,\"SeriesID\":\"%s\",\"MID\":\"%s\"}";
        return post(mConstant.PDM_API_GET_URL(), String.format(uploadJson, seriesID, mID));
    }

    public final BuyerSampleDetailBean getBuyerSampleDetailBean(String mID) throws IOException {
        return mapper.readValue(getBuyerSampleDetailJson(mID), BuyerSampleDetailBean.class);
    }

    public final boolean arrangeBuyerSampleTask(String Designer, String mID, String Operator) throws IOException {
        String seriesID = getSeriesIDBean().getRet().get(0).getSeriesID();
        String uploadJson = "{\"ID\":9011,\"SeriesID\":\"%s\",\"Designer\":\"%s\",\"MID\":\"%s\",\"Operator\":\"%s\",\"Theme\":\"缤纷世界\",\"BandName\":\"第1波\",\"OrderDate\":\"2017-05-03\",\"BandDateStart\":\"2017-07-01\",\"BandDateEnd\":\"2017-09-23\",\"DesignDateStart\":\"2017-04-01\",\"DesignDateEnd\":\"2017-05-02\",\"DeliveryDateStart\":\"2017-06-28\",\"DeliveryDateEnd\":\"2017-08-30\",\"OnBoardDate\":\"2017-06-30\"}";
        String resultJson = post(mConstant.PDM_API_SET_URL(), String.format(uploadJson, seriesID, Designer, mID, Operator));
        return resultJson.contains("\"Res\":0");
    }

    public final String getBuyerSampleNotesJson(String mID) throws IOException {
        String seriesID = getSeriesIDBean().getRet().get(0).getSeriesID();
        String uploadJson = "{\"ID\":23003,\"SeriesID\":\"%s\",\"SearchType\":1,\"MID\":\"%s\"}";
        return post(mConstant.PDM_API_GET_URL(), String.format(uploadJson, seriesID, mID));
    }

    public final BuyerSampleNotesBean getBuyerSampleNotesBean(String mID) throws IOException {
        return mapper.readValue(getBuyerSampleNotesJson(mID), BuyerSampleNotesBean.class);
    }

    public final String getExplodeSpiltJson(String seriesID) throws IOException {
        String uploadJson = "{\"ID\":7511,\"SeriesID\":\"%s\"}";
        return post(mConstant.PDM_API_GET_URL(), String.format(uploadJson, seriesID));
    }

    public BaseBean getExplodeSpiltBean(String seriesID) throws IOException {
        return mapper.readValue(getExplodeSpiltJson(seriesID), BaseBean.class);
    }
    public final String getSampleReferencePointJson(String seriesID) throws IOException {
        String uploadJson = "{\"ID\":7510,\"SeriesID\":\"%s\"}";
        return post(mConstant.PDM_API_GET_URL(), String.format(uploadJson, seriesID));
    }

    public BaseBean getSampleReferencePointBean(String seriesID) throws IOException {
        return mapper.readValue(getSampleReferencePointJson(seriesID), BaseBean.class);
    }

    public final String getStyleAndCategoryJson(String seriesID) throws IOException {
        return post(mConstant.PDM_API_GET_URL(), String.format("{\"ID\":3301,\"SeriesID\":\"%s\",\"Type\":1}", seriesID));
    }

    public StyleAndCategoryBean getStyleAndCategoryBean(String seriesID) throws IOException {
        return mapper.readValue(getStyleAndCategoryJson(seriesID), StyleAndCategoryBean.class);
    }

    public final String getSampleDetailJson(String mID) throws IOException {
        String seriesID = getSeriesIDBean().getRet().get(0).getSeriesID();
        return post(mConstant.PDM_API_GET_URL(), String.format("{\"ID\":9002,\"SeriesID\":\"%s\",\"MID\":\"%s\"}", seriesID, mID));
    }

    public SampleDetailBean getSampleDetailBean(String mID) throws IOException {
        return mapper.readValue(getSampleDetailJson(mID), SampleDetailBean.class);
    }

    public final String uploadSamplePicture(String mID, String ext, String base64) throws IOException {
        String seriesID = getSeriesIDBean().getRet().get(0).getSeriesID();
        String uploadJson = "{\"Type\":2,\"SeriesID\":\"%s\",\"MID\":\"%s\",\"FileExt\":\"%s\",\"picture\":\"%s\"}";
        return post(mConstant.PDM_API_UPLOAD_URL(), String.format(uploadJson, seriesID, mID, ext, base64));

    }

    public final String getCategoryJson(String seriesID) throws IOException {
        return post(mConstant.PDM_API_GET_URL(),
                String.format("{\"ID\":7512,\"SeriesID\":\"%s\"}", seriesID));
    }

    public BaseBean getCategoryBean(String seriesID) throws IOException {
        return mapper.readValue(getCategoryJson(seriesID), BaseBean.class);
    }

    public final String getSamplePictureJson(String mID) throws IOException {
        String seriesID = getSeriesIDBean().getRet().get(0).getSeriesID();
        return post(mConstant.PDM_API_GET_PIC_URL(), String.format("{\"ID\":9003,\"SeriesID\":\"%s\",\"MID\":\"%s\",\"Type\":0}", seriesID, mID));
    }

    public SamplePictureBean getSamplePictureBean(String mID) throws IOException {
        return mapper.readValue(getSamplePictureJson(mID), SamplePictureBean.class);
    }

    public final String getUnusualRecordListJson(String seriesID) throws IOException {
        return post(mConstant.PDM_API_GET_URL(),
                String.format("{\"ID\":7509,\"SeriesID\":\"%s\"}", seriesID));
    }

    public BaseBean getUnusualRecordListBean(String seriesID) throws IOException {
        return mapper.readValue(getUnusualRecordListJson(seriesID), BaseBean.class);
    }

    public final String postUnusualRecordJson(String seriesID, String maker, String mID, String desc, String difference, String sort, String time) throws IOException {
        return post(mConstant.PDM_API_SET_URL(), String.format(
                "{\"ID\":9009,\"SeriesID\":\"%s\",\"Maker\":\"%s\",\"MID\":\"%s\",\"desc\":\"%s\",\"Difference\":\"%s\",\"cSort\":\"%s\",\"Finish\":\"%s\",\"Type\":0}",
                seriesID, maker, mID, desc, difference, sort, time));
    }

    public ResponseBean postUnusualRecordBean(String seriesID, String maker, String mID, String desc, String difference, String sort, String time) throws IOException {
        return mapper.readValue(postUnusualRecordJson(seriesID, maker, mID, desc, difference, sort, time), ResponseBean.class);
    }

    public final String uploadEditionRecordPic(String seriesID, String mID, String ext, String maker, String base64) throws IOException {
        String uploadJson = "{\"Type\":10,\"SeriesID\":\"%s\",\"MID\":\"%s\",\"FileExt\":\"%s\",\"Maker\":\"%s\",\"Remark\":\"\",\"picture\":\"%s\"}";
        return post(mConstant.PDM_API_UPLOAD_URL(), String.format(uploadJson, seriesID, mID, ext, maker, base64));

    }

    public final String getSampleManagerEditionRecordJson(String mID) throws IOException {
        String seriesID = getSeriesIDBean().getRet().get(0).getSeriesID();
        return post(mConstant.PDM_API_GET_URL(), String.format("{\"ID\":9007,\"SeriesID\":\"%s\",\"MID\":\"%s\"}", seriesID, mID));
    }

    public SampleManagerEditionRecordBean getSampleManagerEditionRecordBean(String mID) throws IOException{
        return mapper.readValue(getSampleManagerEditionRecordJson(mID), SampleManagerEditionRecordBean.class);
    }

    public final String getMaterialPurchaseListJson(String json) throws IOException {
        String seriesID = getSeriesIDBean().getRet().get(0).getSeriesID();
        return post(mConstant.PDM_API_GET_URL(), String.format(
                "{\"ID\":17001,\"SeriesID\":\"%s\",\"Time\":\"time2000\",%s}", seriesID, json
        ));
    }

    public MaterialPurchaseListBean getMaterialPurchaseListBean(String json) throws IOException {
        return mapper.readValue(getMaterialPurchaseListJson(json), MaterialPurchaseListBean.class);
    }

    public final String getMaterialPurchaseDetailListJson(String mID) throws IOException {
        String seriesID = getSeriesIDBean().getRet().get(0).getSeriesID();
        return post(mConstant.PDM_API_GET_URL(), String.format("{\"ID\":17003,\"SeriesID\":\"%s\",\"MID\":\"%s\"}", seriesID, mID));
    }

    public MaterialPurchaseDetailListBean getMaterialPurchaseDetailListBean(String mID) throws IOException {
        return mapper.readValue(getMaterialPurchaseDetailListJson(mID), MaterialPurchaseDetailListBean.class);
    }

    public final String getSpecialTechnicsJson(String json) throws IOException {
        return post(mConstant.PDM_API_GET_URL(), json);
    }

    public SpecialTechnicsBean getSpecialTechnicsBean(String json) throws IOException {
        return mapper.readValue(getSpecialTechnicsJson(json), SpecialTechnicsBean.class);
    }

    public final String getSpecialTechnicsDetailJson(String SeriesID, String mid) throws IOException {
        return post(mConstant.PDM_API_GET_URL(), String.format("{\"ID\":24002,\"SeriesID\":\"%s\",\"MID\":\"%s\"}", SeriesID, mid));
    }

    public SpecialTechnicsDetailBean getSpecialTechnicsDetailBean(String Series, String mid) throws IOException {
        return mapper.readValue(getSpecialTechnicsDetailJson(Series, mid), SpecialTechnicsDetailBean.class);
    }

    public final String getTeamClothesCustomOrderListJson(String json) throws IOException {
        return post(mConstant.PDM_API_GET_URL(), json);
    }

    public TeamClothesCustomOrderListBean getTeamClothesCustomOrderListBean(String json) throws IOException {
        return mapper.readValue(getTeamClothesCustomOrderListJson(json), TeamClothesCustomOrderListBean.class);
    }

    public final String getTeamClothesCustomOrderDetailListJson(String json) throws IOException {
        return post(mConstant.PDM_API_GET_URL(), json);
    }

    public TeamClothesCustomOrderDetailListBean getTeamClothesCustomOrderDetailListBean(String json) throws IOException {
        return mapper.readValue(getTeamClothesCustomOrderDetailListJson(json), TeamClothesCustomOrderDetailListBean.class);
    }
    public final String getUserJson(String seriesID) throws IOException {
        return post(mConstant.PDM_API_GET_URL(),
                String.format("{\"ID\":3101,\"SeriesID\":\"%s\"}", seriesID));
    }

    public MakerBean getUserBean(String seriesID) throws IOException {
        return mapper.readValue(getUserJson(seriesID), MakerBean.class);
    }

    public final String getAnalysisOfResearchStatisticalSummaryJson(String json) throws IOException {
        return post(mConstant.PDM_API_GET_URL(), json);
    }

    public AnalysisOfResearchStatisticalSummaryBean getAnalysisOfResearchStatisticalSummaryBean(String json) throws IOException {
        return mapper.readValue(getAnalysisOfResearchStatisticalSummaryJson(json), AnalysisOfResearchStatisticalSummaryBean.class);
    }

    public final String getAnalysisOfResearchChartJson(String json) throws IOException {
        return post(mConstant.PDM_API_GET_URL(), json);
    }

    public AnalysisOfResearchChartBean getAnalysisOfResearchChartBean(String json) throws IOException {
        return mapper.readValue(getAnalysisOfResearchChartJson(json), AnalysisOfResearchChartBean.class);
    }

    public final String getPersonCustomJson(String json) throws IOException {
        return post(mConstant.PDM_API_GET_URL(), json);
    }

    public PersonCustomBean getPersonCustomBean(String json) throws IOException {
        return mapper.readValue(getPersonCustomJson(json), PersonCustomBean.class);
    }

    public BaseBean getExpressCompanyBean(String seriesID) throws IOException {
        return mapper.readValue(getExpressCompanyJson(seriesID), BaseBean.class);
    }

    public final String getExpressCompanyJson(String seriesID) throws IOException {
        return post(mConstant.PDM_API_GET_URL(),
                String.format("{\"ID\":7514,\"SeriesID\":\"%s\"}", seriesID));
    }

    public final String getOrderNumChartJson(String json) throws IOException {
        return post(mConstant.PDM_API_GET_URL(), json);
    }

    public OrderNumChartBean getOrderNumChartBean(String json) throws IOException {
        return mapper.readValue(getOrderNumChartJson(json), OrderNumChartBean.class);
    }

    public final String getWarningTipsPersonCustomJson(String json) throws IOException {
        return post(mConstant.PDM_API_GET_URL(), json);
    }

    public WarningTipsPersonCustomBean getWarningTipsPersonCustomBean(String json) throws IOException {
        return mapper.readValue(getWarningTipsPersonCustomJson(json), WarningTipsPersonCustomBean.class);
    }
}

