package com.besthings.pdm.adapter;

import com.besthings.bean.TeamClothesCustomOrderDetailListRet;
import com.besthings.pdm.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Chas on 2017/10/27 0027.
 */

public class TeamClothesCustomOrderDetailListAdapter extends BaseQuickAdapter<TeamClothesCustomOrderDetailListRet, BaseViewHolder> {
    public TeamClothesCustomOrderDetailListAdapter(int layoutResId, List<TeamClothesCustomOrderDetailListRet> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TeamClothesCustomOrderDetailListRet item) {
        helper.setText(R.id.item_team_clothes_custom_detail_list_tvSerialNo, item.getSerialno());
        helper.setText(R.id.item_team_clothes_custom_detail_list_tvCustomer, item.getCname());
        helper.setText(R.id.item_team_clothes_custom_detail_list_tvStyle, item.getStyle());
        helper.setText(R.id.item_team_clothes_custom_detail_list_tvSize, item.getSize());

        if (item.getOrderstate() != null) {
            switch (item.getOrderstate()) {
                case "0":
                    helper.setText(R.id.item_team_clothes_custom_detail_list_tvOrderState, "未下单");
                    break;
                case "1":
                    helper.setText(R.id.item_team_clothes_custom_detail_list_tvOrderState, "已下单");
                    break;
                case "2":
                    helper.setText(R.id.item_team_clothes_custom_detail_list_tvOrderState, "已回单");
                    break;
                default:
                    helper.setText(R.id.item_team_clothes_custom_detail_list_tvOrderState, item.getOrderstate());
                    break;
            }
        }


        if (item.getPlatestate() != null) {
            switch (item.getPlatestate()) {
                case "-1":
                    helper.setText(R.id.item_team_clothes_custom_detail_list_tvPlateState, "未开始");
                    break;
                case "0":
                case "1":
                    helper.setText(R.id.item_team_clothes_custom_detail_list_tvPlateState, "进行中");
                    break;
                case "2":
                    helper.setText(R.id.item_team_clothes_custom_detail_list_tvPlateState, "已完成");
                    break;
                default:
                    helper.setText(R.id.item_team_clothes_custom_detail_list_tvPlateState, item.getPlatestate());
                    break;
            }
        }

        if (item.getClothstate() != null) {
            switch (item.getClothstate()) {
                case "-1":
                case "0":
                    helper.setText(R.id.item_team_clothes_custom_detail_list_tvClothState, "未开始");
                    break;
                case "1":
                    helper.setText(R.id.item_team_clothes_custom_detail_list_tvClothState, "进行中");
                    break;
                case "2":
                    helper.setText(R.id.item_team_clothes_custom_detail_list_tvClothState, "已完成");
                    break;
                default:
                    helper.setText(R.id.item_team_clothes_custom_detail_list_tvClothState, item.getClothstate());
                    break;
            }
        }

        if(item.getStockstate() != null) {
            switch (item.getStockstate()) {
                case "0":
                    helper.setText(R.id.item_team_clothes_custom_detail_list_tvStockState, "未入库");
                    break;
                case "1":
                    helper.setText(R.id.item_team_clothes_custom_detail_list_tvStockState, "已入库");
                    break;
                case "2":
                    helper.setText(R.id.item_team_clothes_custom_detail_list_tvStockState, "已发货");
                    break;
                default:
                    helper.setText(R.id.item_team_clothes_custom_detail_list_tvStockState, item.getStockstate());
                    break;
            }
        }

        if (item.getMatstate() != null) {
            switch (item.getMatstate()) {
                case "-1":
                    helper.setText(R.id.item_team_clothes_custom_detail_list_tvMatState, "无面料");
                    break;
                case "0":
                    helper.setText(R.id.item_team_clothes_custom_detail_list_tvMatState, "未备料");
                    break;
                case "1":
                    helper.setText(R.id.item_team_clothes_custom_detail_list_tvMatState, "备料中");
                    break;
                case "2":
                    helper.setText(R.id.item_team_clothes_custom_detail_list_tvMatState, "已备料");
                    break;
                default:
                    helper.setText(R.id.item_team_clothes_custom_detail_list_tvMatState, item.getMatstate());
                    break;
            }
        }

        if (item.getCutstate() != null) {
            switch (item.getCutstate()) {
                case "0":
                    helper.setText(R.id.item_team_clothes_custom_detail_list_tvCutState, "未开始");
                    break;
                case "1":
                    helper.setText(R.id.item_team_clothes_custom_detail_list_tvCutState, "进行中");
                    break;
                case "2":
                    helper.setText(R.id.item_team_clothes_custom_detail_list_tvCutState, "已完成");
                    break;
                default:
                    helper.setText(R.id.item_team_clothes_custom_detail_list_tvCutState, item.getCutstate());
                    break;
            }
        }

        if (item.getAssemblystate() != null) {
            switch (item.getAssemblystate()) {
                case "0":
                    helper.setText(R.id.item_team_clothes_custom_detail_list_tvAssemblyState, "未开始");
                    break;
                case "1":
                    helper.setText(R.id.item_team_clothes_custom_detail_list_tvAssemblyState, "进行中");
                    break;
                case "2":
                    helper.setText(R.id.item_team_clothes_custom_detail_list_tvAssemblyState, "已完成");
                    break;
                default:
                    helper.setText(R.id.item_team_clothes_custom_detail_list_tvAssemblyState, item.getAssemblystate());
                    break;
            }
        }

        switch (item.getDeliver()) {
            case "0":
                helper.setText(R.id.item_team_clothes_custom_detail_list_tvDeliverState, "未发货");
                break;
            case "1":
                helper.setText(R.id.item_team_clothes_custom_detail_list_tvDeliverState, "已发货");
                break;
            default:
                helper.setText(R.id.item_team_clothes_custom_detail_list_tvDeliverState, item.getDeliver());
                break;
        }

    }

}