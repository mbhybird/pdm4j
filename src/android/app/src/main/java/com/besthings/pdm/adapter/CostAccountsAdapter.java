package com.besthings.pdm.adapter;

import android.widget.ImageView;

import com.besthings.bean.CostAccountsRet;
import com.besthings.pdm.R;
import com.besthings.pdm.utils.ImageUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Chas on 2017/9/30 0030.
 */

public class CostAccountsAdapter extends BaseQuickAdapter<CostAccountsRet, BaseViewHolder> {
    public CostAccountsAdapter(int layoutResId, List<CostAccountsRet> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, CostAccountsRet item) {
        ((ImageView) viewHolder.getView(R.id.item_cost_accounts_ivPic)).setScaleType(ImageView.ScaleType.FIT_CENTER);
        if (!"".equals(item.getPic())) {
            viewHolder.setImageBitmap(R.id.item_cost_accounts_ivPic, ImageUtil.getURLImage(item.getPic()));
        } else {
            viewHolder.setImageResource(R.id.item_cost_accounts_ivPic, R.drawable.empty);
        }
        viewHolder.setText(R.id.item_cost_accounts_tvSNo, item.getSNO());
        viewHolder.setText(R.id.item_cost_accounts_tvMaterialCost, item.getF101());
        viewHolder.setText(R.id.item_cost_accounts_tvAccessory, item.getF102());
        viewHolder.setText(R.id.item_cost_accounts_tvAddCost, item.getF2());
        viewHolder.setText(R.id.item_cost_accounts_tvOutAddCost, item.getF12());
        viewHolder.setText(R.id.item_cost_accounts_tvSpecialCost, item.getF8());
        viewHolder.setText(R.id.item_cost_accounts_tvProductionCost, item.getSum());
        viewHolder.setText(R.id.item_cost_accounts_tvTagCost, item.getTP());
    }

}