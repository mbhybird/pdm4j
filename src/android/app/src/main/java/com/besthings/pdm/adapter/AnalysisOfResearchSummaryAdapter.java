package com.besthings.pdm.adapter;

import android.widget.ImageView;

import com.besthings.bean.AnalysisOfResearchStatisticalSummaryRet;
import com.besthings.pdm.R;
import com.besthings.pdm.utils.ImageUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import java.util.List;

/**
 * Created by chas on 2017/11/2.
 */

public class AnalysisOfResearchSummaryAdapter extends BaseQuickAdapter<AnalysisOfResearchStatisticalSummaryRet, BaseViewHolder> {
    public AnalysisOfResearchSummaryAdapter(int layoutResId, List<AnalysisOfResearchStatisticalSummaryRet> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AnalysisOfResearchStatisticalSummaryRet item) {
        ((ImageView) helper.getView(R.id.item_analysis_of_research_summary_ivPic)).setScaleType(ImageView.ScaleType.FIT_CENTER);
        if (item.getPic().equals("")) {
            helper.setImageResource(R.id.item_analysis_of_research_summary_ivPic, R.drawable.empty);
        } else {
            helper.setImageBitmap(R.id.item_analysis_of_research_summary_ivPic, ImageUtil.getURLImage(item.getPic()));
        }
        helper.setText(R.id.item_analysis_of_research_summary_tvStyleNo, item.getStyleno_());
        helper.setText(R.id.item_analysis_of_research_summary_tvStyle, item.getStylea_());
        helper.setText(R.id.item_analysis_of_research_summary_tvCategory, item.getStyleb_());
        helper.setText(R.id.item_analysis_of_research_summary_tvDesigner, item.getDesigner_());
        helper.setText(R.id.item_analysis_of_research_summary_tvTempleteMan, item.getCtempleteman());
        if (item.getIdesignstate() != null) {
            switch (item.getIdesignstate()) {
                case "-1":
                    helper.setText(R.id.item_analysis_of_research_summary_tvDesignState, "未开始");
                    break;
                case "1":
                    helper.setText(R.id.item_analysis_of_research_summary_tvDesignState, "进行中");
                    break;
                case "2":
                    helper.setText(R.id.item_analysis_of_research_summary_tvDesignState, "已完成");
                    break;
                case "3":
                    helper.setText(R.id.item_analysis_of_research_summary_tvDesignState, "已审核");
                    break;
                default:
                    helper.setText(R.id.item_analysis_of_research_summary_tvDesignState, item.getIdesignstate());
                    break;
            }
        }
        if (item.getIplatestate() != null) {
            switch (item.getIplatestate()) {
                case "-1":
                    helper.setText(R.id.item_analysis_of_research_summary_tvPlateState, "未开始");
                    break;
                case "0":
                case "1":
                    helper.setText(R.id.item_analysis_of_research_summary_tvPlateState, "进行中");
                    break;
                case "2":
                    helper.setText(R.id.item_analysis_of_research_summary_tvPlateState, "已完成");
                    break;
                case "3":
                    helper.setText(R.id.item_analysis_of_research_summary_tvPlateState, "异样");
                    break;
                case "4":
                    helper.setText(R.id.item_analysis_of_research_summary_tvPlateState, "已审核");
                    break;
                default:
                    helper.setText(R.id.item_analysis_of_research_summary_tvPlateState, item.getIplatestate());
                    break;
            }
        }
        if (item.getImatstate() != null) {
            switch (item.getImatstate()) {
                case "-1":
                    helper.setText(R.id.item_analysis_of_research_summary_tvMatState, "无面料");
                    break;
                case "0":
                    helper.setText(R.id.item_analysis_of_research_summary_tvMatState, "未备料");
                    break;
                case "1":
                    helper.setText(R.id.item_analysis_of_research_summary_tvMatState, "备料中");
                    break;
                case "2":
                    helper.setText(R.id.item_analysis_of_research_summary_tvMatState, "已备料");
                    break;
                default:
                    helper.setText(R.id.item_analysis_of_research_summary_tvMatState, item.getImatstate());
            }
        }
        if (item.getItechnicsstate() != null) {
            switch (item.getItechnicsstate()) {
                case "0":
                    helper.setText(R.id.item_analysis_of_research_summary_tvSpecialState, "进行中");
                    break;
                case "1":
                    helper.setText(R.id.item_analysis_of_research_summary_tvSpecialState, "已完成");
                    break;
                case "2":
                    helper.setText(R.id.item_analysis_of_research_summary_tvSpecialState, "异样");
                    break;
                default:
                    helper.setText(R.id.item_analysis_of_research_summary_tvFClothState, item.getItechnicsstate());
                    break;
            }
        }
        if (item.getBconfirmclothsampleF() != null) {
            switch (item.getBconfirmclothsampleF()) {
                case "1":
                    helper.setText(R.id.item_analysis_of_research_summary_tvFClothState, "已确认");
                    break;
                case "0":
                    helper.setText(R.id.item_analysis_of_research_summary_tvFClothState, "未确认");
                    break;
                default:
                    helper.setText(R.id.item_analysis_of_research_summary_tvFClothState, item.getBconfirmclothsampleF());
                    break;
            }
        }
        if (item.getBconfirmclothsampleS() != null) {
            switch (item.getBconfirmclothsampleS()) {
                case "1":
                    helper.setText(R.id.item_analysis_of_research_summary_tvSClothState, "已确认");
                    break;
                case "0":
                    helper.setText(R.id.item_analysis_of_research_summary_tvSClothState, "未确认");
                    break;
                default:
                    helper.setText(R.id.item_analysis_of_research_summary_tvSClothState, item.getBconfirmclothsampleF());
                    break;
            }
        }
    }
}
