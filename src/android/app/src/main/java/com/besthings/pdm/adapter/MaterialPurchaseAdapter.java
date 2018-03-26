package com.besthings.pdm.adapter;

import com.besthings.bean.MaterialPurchaseListRet;
import com.besthings.pdm.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Chas on 2017/10/24 0024.
 */

public class MaterialPurchaseAdapter extends BaseQuickAdapter<MaterialPurchaseListRet, BaseViewHolder> {
    public MaterialPurchaseAdapter(int layoutResId, List<MaterialPurchaseListRet> data) {
        super(layoutResId, data);
    }
    
    @Override
    protected void convert(BaseViewHolder helper, MaterialPurchaseListRet item) {
        helper.setText(R.id.item_material_purchase_tvPurchaseNo, item.getNo());
        helper.setText(R.id.item_material_purchase_tvSupport, item.getSup());
        helper.setText(R.id.item_material_purchase_tvPurchaser, item.getGm());
        helper.setText(R.id.item_material_purchase_tvPurchaseDepartment, item.getDept());
        helper.setText(R.id.item_material_purchase_tvRequireTime, item.getNd());
        helper.setText(R.id.item_material_purchase_tvStorageTime, item.getInstockdate());
    }
}
