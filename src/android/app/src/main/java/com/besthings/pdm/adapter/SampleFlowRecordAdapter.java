package com.besthings.pdm.adapter;

import com.besthings.bean.SampleFlowRecordRet;
import com.besthings.pdm.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Chas on 2017/10/16 0016.
 */

public class SampleFlowRecordAdapter extends BaseQuickAdapter<SampleFlowRecordRet, BaseViewHolder> {
    public SampleFlowRecordAdapter(int layoutResId, List<SampleFlowRecordRet> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SampleFlowRecordRet item) {
        helper.setText(R.id.item_sample_flow_record_tvNo, ""+item.getSerialNo());
        helper.setText(R.id.item_sample_flow_record_tvBorrowTime, item.getBorrowTime());
        switch (item.getType()) {
            case "Normal":
                helper.setText(R.id.item_sample_flow_record_tvType, "借取");
                break;
            case "Sale":
                helper.setText(R.id.item_sample_flow_record_tvType, "销售");
                break;
            case "Stock":
                helper.setText(R.id.item_sample_flow_record_tvType, "库存");
                break;
            case "Destroy":
                helper.setText(R.id.item_sample_flow_record_tvType, "销毁");
                break;
            case "Appoint":
                helper.setText(R.id.item_sample_flow_record_tvType, "预约");
                break;
            case "":
                helper.setText(R.id.item_sample_flow_record_tvType, "");
                break;
            default:
                helper.setText(R.id.item_sample_flow_record_tvType, "");
                break;

        }
        helper.setText(R.id.item_sample_flow_record_tvBorrower, item.getBorrower());
        helper.setText(R.id.item_sample_flow_record_tvRecord, item.getR1());
        helper.setText(R.id.item_sample_flow_record_tvRemark, item.getR2());

    }

}
