package com.besthings.pdm.adapter;

import com.besthings.bean.AnalysisOfResearchChartRet;
import com.besthings.pdm.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by chas on 2017/11/3.
 */

public class AnalysisOfResearchChartAdapter extends BaseQuickAdapter<AnalysisOfResearchChartRet, BaseViewHolder> {

    public AnalysisOfResearchChartAdapter(int layoutResId, List<AnalysisOfResearchChartRet> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AnalysisOfResearchChartRet item) {
        helper.setText(R.id.item_analysis_of_research_quantity_analysis_tvNotStarted, item.getS().get(0).toString());
        helper.setText(R.id.item_analysis_of_research_quantity_analysis_tvOnGoing, item.getS().get(1).toString());
        helper.setText(R.id.item_analysis_of_research_quantity_analysis_tvFinished, item.getS().get(2).toString());
        helper.setText(R.id.item_analysis_of_research_quantity_analysis_tvAudited, item.getS().get(3).toString());
        helper.setText(R.id.item_analysis_of_research_quantity_analysis_tvUnusual, item.getS().get(4).toString());
        int sum = 0;
        for (int i = 0 ; i < item.getS().size(); i++) {
            sum += item.getS().get(i);
        }
        helper.setText(R.id.item_analysis_of_research_quantity_analysis_tvSummary, ""+sum);
        switch (item.getSno()) {
            case 0:
                helper.setText(R.id.item_analysis_of_research_quantity_analysis_tvStateName, "设计状态");
                break;
            case 1:
                helper.setText(R.id.item_analysis_of_research_quantity_analysis_tvStateName, "工艺状态");
                break;
            case 2:
                helper.setText(R.id.item_analysis_of_research_quantity_analysis_tvStateName, "订单状态");
                break;
            case 3:
                helper.setText(R.id.item_analysis_of_research_quantity_analysis_tvStateName, "备料状态");
                break;
            case 4:
                helper.setText(R.id.item_analysis_of_research_quantity_analysis_tvStateName, "打版状态");
                break;
            case 5:
                helper.setText(R.id.item_analysis_of_research_quantity_analysis_tvStateName, "车版状态");
                break;
            case 6:
                helper.setText(R.id.item_analysis_of_research_quantity_analysis_tvStateName, "放码状态");
                break;
            case 7:
                helper.setText(R.id.item_analysis_of_research_quantity_analysis_tvStateName, "单裁状态");
                break;
            case 8:
                helper.setText(R.id.item_analysis_of_research_quantity_analysis_tvStateName, "入库状态");
                break;
            case 9:
                helper.setText(R.id.item_analysis_of_research_quantity_analysis_tvStateName, "后道状态");
                break;
            case 10:
                helper.setText(R.id.item_analysis_of_research_quantity_analysis_tvStateName, "排料状态");
                break;
            case 11:
                helper.setText(R.id.item_analysis_of_research_quantity_analysis_tvStateName, "量裁状态");
                break;
            case 12:
                helper.setText(R.id.item_analysis_of_research_quantity_analysis_tvStateName, "缝制状态");
                break;
            case 13:
                helper.setText(R.id.item_analysis_of_research_quantity_analysis_tvStateName, "算料状态");
                break;
            case 14:
                helper.setText(R.id.item_analysis_of_research_quantity_analysis_tvStateName, "领料状态");
                break;
            default:
                helper.setText(R.id.item_analysis_of_research_quantity_analysis_tvStateName, ""+item.getSno());
                break;
        }
    }
}

