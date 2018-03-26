package com.besthings.pdm.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.besthings.bean.WarningTipsPersonCustomRet;
import com.besthings.pdm.R;
import com.besthings.pdm.utils.ImageUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Chas on 2017/11/7 0007.
 */

public class WarningTipsAdapter extends BaseQuickAdapter<WarningTipsPersonCustomRet, BaseViewHolder> {
    public WarningTipsAdapter(int layoutResId, @Nullable List<WarningTipsPersonCustomRet> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WarningTipsPersonCustomRet item) {
        ((ImageView) helper.getView(R.id.item_warning_tips_ivPic)).setScaleType(ImageView.ScaleType.FIT_CENTER);
        if (item.getPic().equals("")) {
            helper.setImageResource(R.id.item_warning_tips_ivPic, R.drawable.empty);
        } else {
            helper.setImageBitmap(R.id.item_warning_tips_ivPic, ImageUtil.getURLImage(item.getPic()));
        }
        helper.setText(R.id.item_warning_tips_tvOrderNo, item.getOrderno());
        helper.setText(R.id.item_warning_tips_tvCustName, item.getCustomer());
        helper.setText(R.id.item_warning_tips_tvAcceptTime, item.getAccept());
        helper.setText(R.id.item_warning_tips_tvAppointTime, item.getDdd());
        if (item.getMatstate() != null) {
            switch (item.getMatstate()) {
                case "-1":
                    helper.setText(R.id.item_warning_tips_tvMatState, "无面料");
                    break;
                case "0":
                    helper.setText(R.id.item_warning_tips_tvMatState, "未备料");
                    break;
                case "1":
                    helper.setText(R.id.item_warning_tips_tvMatState, "备料中");
                    break;
                case "2":
                    helper.setText(R.id.item_warning_tips_tvMatState, "已备料");
                    break;
                default:
                    helper.setText(R.id.item_warning_tips_tvMatState, item.getMatstate());
                    break;
            }
        }
        if (item.getCutstate() != null) {
            switch (item.getCutstate()) {
                case "0":
                    helper.setText(R.id.item_warning_tips_tvCutState, "未开始");
                    break;
                case "1":
                    helper.setText(R.id.item_warning_tips_tvCutState, "进行中");
                    break;
                case "2":
                    helper.setText(R.id.item_warning_tips_tvCutState, "已完成");
                    break;
                default:
                    helper.setText(R.id.item_warning_tips_tvCutState, item.getCutstate());
                    break;
            }
        }
        if (item.getAssemblystate() != null) {
            switch (item.getAssemblystate()) {
                case "0":
                    helper.setText(R.id.item_warning_tips_tvAssemblyState, "未开始");
                    break;
                case "1":
                    helper.setText(R.id.item_warning_tips_tvAssemblyState, "进行中");
                    break;
                case "2":
                    helper.setText(R.id.item_warning_tips_tvAssemblyState, "已完成");
                    break;
                default:
                    helper.setText(R.id.item_warning_tips_tvAssemblyState, item.getCutstate());
                    break;
            }
        }
        if (item.getDeliver() != null) {
            switch (item.getDeliver()) {
                case "0":
                    helper.setText(R.id.item_warning_tips_tvDeliver, "未发货");
                    break;
                case "1":
                    helper.setText(R.id.item_warning_tips_tvDeliver, "已发货");
                    break;
                default:
                    helper.setText(R.id.item_warning_tips_tvDeliver, item.getDeliver());
                    break;
            }
        }
        if (item.getOrderstate() != null) {
            switch (item.getOrderstate()) {
                case "0":
                    helper.setText(R.id.item_warning_tips_tvOrderState, "未开始");
                    break;
                case "1":
                    helper.setText(R.id.item_warning_tips_tvOrderState, "进行中");
                    break;
                case "2":
                    helper.setText(R.id.item_warning_tips_tvOrderState, "已完成");
                    break;
                default:
                    helper.setText(R.id.item_warning_tips_tvOrderState, item.getOrderstate());
                    break;
            }
        }
        if (item.getPlatestate() != null) {
            switch (item.getPlatestate()) {
                case "0":
                    helper.setText(R.id.item_warning_tips_tvPlateState, "未开始");
                    break;
                case "1":
                    helper.setText(R.id.item_warning_tips_tvPlateState, "进行中");
                    break;
                case "2":
                    helper.setText(R.id.item_warning_tips_tvPlateState, "已完成");
                    break;
                case "3":
                    helper.setText(R.id.item_warning_tips_tvPlateState, "异样");
                    break;
                case "4":
                    helper.setText(R.id.item_warning_tips_tvPlateState, "已审核");
                    break;
                default:
                    helper.setText(R.id.item_warning_tips_tvPlateState, item.getPlatestate());
                    break;
            }
        }
        if (item.getClothstate() != null) {
            switch (item.getClothstate()) {
                case "0":
                    helper.setText(R.id.item_warning_tips_tvClothState, "未开始");
                    break;
                case "1":
                    helper.setText(R.id.item_warning_tips_tvClothState, "进行中");
                    break;
                case "2":
                    helper.setText(R.id.item_warning_tips_tvClothState, "已完成");
                    break;
                default:
                    helper.setText(R.id.item_warning_tips_tvClothState, item.getClothstate());
                    break;
            }
        }
        if (item.getStockstate() != null) {
            switch (item.getStockstate()) {
                case "0":
                    helper.setText(R.id.item_warning_tips_tvStockState, "未入库");
                    break;
                case "1":
                    helper.setText(R.id.item_warning_tips_tvStockState, "进行中");
                    break;
                case "2":
                    helper.setText(R.id.item_warning_tips_tvStockState, "已入库");
                    break;
                default:
                    helper.setText(R.id.item_warning_tips_tvStockState, item.getStockstate());
                    break;
            }
        }
    }
}
