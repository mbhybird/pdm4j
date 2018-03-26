package com.besthings.pdm.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;
import com.besthings.bean.PersonCustomRet;
import com.besthings.pdm.R;
import com.besthings.pdm.utils.ImageUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Chas on 2017/11/4 0004.
 */

public class PersonCustomAdapter extends BaseQuickAdapter<PersonCustomRet, BaseViewHolder> {
    public PersonCustomAdapter(int layoutResId, @Nullable List<PersonCustomRet> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PersonCustomRet item) {
        ((ImageView) helper.getView(R.id.item_person_custom_ivPic)).setScaleType(ImageView.ScaleType.FIT_CENTER);
        if (item.getPic().equals("")) {
            helper.setImageResource(R.id.item_person_custom_ivPic, R.drawable.empty);
        } else {
            helper.setImageBitmap(R.id.item_person_custom_ivPic, ImageUtil.getURLImage(item.getPic()));
        }
        helper.setText(R.id.item_person_custom_tvOrderNo, item.getOrderno());
        helper.setText(R.id.item_person_custom_tvBundleNo, item.getBundleno());
        helper.setText(R.id.item_person_custom_tvCustomer, item.getCustomer());
        helper.setText(R.id.item_person_custom_tvExpressNo, item.getExpressno());
        helper.setText(R.id.item_person_custom_tvOrderDate, item.getAccept());
        helper.setText(R.id.item_person_custom_tvDDD, item.getDdd());
        helper.setText(R.id.item_person_custom_tvStore, item.getStore());
        helper.setText(R.id.item_person_custom_tvBrand, item.getBrand());
        if (item.getMatstate() != null) {
            switch (item.getMatstate()) {
                case "-1":
                    helper.setText(R.id.item_person_custom_tvMatState, "无面料");
                    break;
                case "0":
                    helper.setText(R.id.item_person_custom_tvMatState, "未备料");
                    break;
                case "1":
                    helper.setText(R.id.item_person_custom_tvMatState, "备料中");
                    break;
                case "2":
                    helper.setText(R.id.item_person_custom_tvMatState, "已备料");
                    break;
                default:
                    helper.setText(R.id.item_person_custom_tvMatState, item.getMatstate());
                    break;
            }
        }
        if (item.getCutstate() != null) {
            switch (item.getCutstate()) {
                case "0":
                    helper.setText(R.id.item_person_custom_tvCutState, "未开始");
                    break;
                case "1":
                    helper.setText(R.id.item_person_custom_tvCutState, "进行中");
                    break;
                case "2":
                    helper.setText(R.id.item_person_custom_tvCutState, "已完成");
                    break;
                default:
                    helper.setText(R.id.item_person_custom_tvCutState, item.getCutstate());
                    break;
            }
        }
        if (item.getAssemblystate() != null) {
            switch (item.getAssemblystate()) {
                case "0":
                    helper.setText(R.id.item_person_custom_tvAssemblyState, "未开始");
                    break;
                case "1":
                    helper.setText(R.id.item_person_custom_tvAssemblyState, "进行中");
                    break;
                case "2":
                    helper.setText(R.id.item_person_custom_tvAssemblyState, "已完成");
                    break;
                default:
                    helper.setText(R.id.item_person_custom_tvAssemblyState, item.getCutstate());
                    break;
            }
        }
        if (item.getDeliver() != null) {
            switch (item.getDeliver()) {
                case "0":
                    helper.setText(R.id.item_person_custom_tvState, "未发货");
                    break;
                case "1":
                    helper.setText(R.id.item_person_custom_tvState, "已发货");
                    break;
                default:
                    helper.setText(R.id.item_person_custom_tvState, item.getDeliver());
                    break;
            }
        }
        if (item.getOrderstate() != null) {
            switch (item.getOrderstate()) {
                case "0":
                    helper.setText(R.id.item_person_custom_tvOrderState, "未开始");
                    break;
                case "1":
                    helper.setText(R.id.item_person_custom_tvOrderState, "进行中");
                    break;
                case "2":
                    helper.setText(R.id.item_person_custom_tvOrderState, "已完成");
                    break;
                default:
                    helper.setText(R.id.item_person_custom_tvOrderState, item.getOrderstate());
                    break;
            }
        }
        if (item.getPlatestate() != null) {
            switch (item.getPlatestate()) {
                case "-1":
                    helper.setText(R.id.item_person_custom_tvPlateState, "未开始");
                    break;
                case "0":
                case "1":
                    helper.setText(R.id.item_person_custom_tvPlateState, "进行中");
                    break;
                case "2":
                    helper.setText(R.id.item_person_custom_tvPlateState, "已完成");
                    break;
                case "3":
                    helper.setText(R.id.item_person_custom_tvPlateState, "异样");
                    break;
                case "4":
                    helper.setText(R.id.item_person_custom_tvPlateState, "已审核");
                    break;
                default:
                    helper.setText(R.id.item_person_custom_tvPlateState, item.getPlatestate());
                    break;
            }
        }
        if (item.getClothstate() != null) {
            switch (item.getClothstate()) {
                case "-1":
                    helper.setText(R.id.item_person_custom_tvClothState, "未开始");
                    break;
                case "0":
                    helper.setText(R.id.item_person_custom_tvClothState, "未开始");
                    break;
                case "1":
                    helper.setText(R.id.item_person_custom_tvClothState, "进行中");
                    break;
                case "2":
                    helper.setText(R.id.item_person_custom_tvClothState, "已完成");
                    break;
                default:
                    helper.setText(R.id.item_person_custom_tvClothState, item.getClothstate());
                    break;
            }
        }
        if (item.getStockstate() != null) {
            switch (item.getStockstate()) {
                case "0":
                    helper.setText(R.id.item_person_custom_tvClothState, "未入库");
                    break;
                case "1":
                    helper.setText(R.id.item_person_custom_tvClothState, "已入库");
                    break;
                case "2":
                    helper.setText(R.id.item_person_custom_tvClothState, "已发货");
                    break;
                default:
                    helper.setText(R.id.item_person_custom_tvClothState, item.getStockstate());
                    break;
            }
        }
    }
}
