package com.besthings.pdm.adapter;

import com.besthings.bean.MaterialPurchaseDetailListRet;
import com.besthings.pdm.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Chas on 2017/10/24 0024.
 */

public class MaterialPurchaseDetailListAdapter extends BaseQuickAdapter<MaterialPurchaseDetailListRet, BaseViewHolder> {
    public MaterialPurchaseDetailListAdapter(int layoutResId, List<MaterialPurchaseDetailListRet> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MaterialPurchaseDetailListRet item) {
        helper.setText(R.id.item_material_purchase_detail_tvMaterialNo, item.getSno());
        helper.setText(R.id.item_material_purchase_detail_tvColor, item.getClr());
        if (item.getWidth().equals("") || item.getWidth().equals(null)) {
            helper.setText(R.id.item_material_purchase_detail_tvWidth, item.getWidth());
        } else {
            helper.setText(R.id.item_material_purchase_detail_tvWidth, subZeroAndDot(item.getWidth()));
        }
        if (item.getNum().equals("") || item.getNum().equals(null)) {
            helper.setText(R.id.item_material_purchase_detail_tvPurchaseNum, item.getNum());
        } else {
            helper.setText(R.id.item_material_purchase_detail_tvPurchaseNum, subZeroAndDot(item.getNum()));
        }
        if (item.getInstocknum().equals("") || item.getInstocknum().equals(null)) {
            helper.setText(R.id.item_material_purchase_detail_tvInStockNum, item.getInstocknum());
        } else {
            helper.setText(R.id.item_material_purchase_detail_tvInStockNum, subZeroAndDot(item.getInstocknum()));
        }
    }

    public static String subZeroAndDot(String s){
        if(s.indexOf(".") > 0){
            s = s.replaceAll("0+?$", "");
            s = s.replaceAll("[.]$", "");
        }
        return s;
    }
}

