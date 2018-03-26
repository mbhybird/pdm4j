package com.besthings.pdm.adapter;

import android.widget.ImageView;

import com.besthings.bean.DesignMaterialRet;
import com.besthings.pdm.R;
import com.besthings.pdm.utils.ImageUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;
import java.util.Objects;

/**
 * Created by Chas on 2017/9/28 0028.
 */

public class DesignMaterialAdapter extends BaseQuickAdapter<DesignMaterialRet, BaseViewHolder>{
    public DesignMaterialAdapter(int layoutResId, List<DesignMaterialRet> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, DesignMaterialRet item) {
        ((ImageView) viewHolder.getView(R.id.item_design_material_ivPic)).setScaleType(ImageView.ScaleType.FIT_CENTER);
        if (!"".equals(item.getPic())) {
            viewHolder.setImageBitmap(R.id.item_design_material_ivPic, ImageUtil.getURLImage(item.getPic()));
        } else {
            viewHolder.setImageResource(R.id.item_design_material_ivPic, R.drawable.empty);
        }
        viewHolder.setText(R.id.item_design_material_tvMaterialNo, item.getMaterialno());
        viewHolder.setText(R.id.item_design_material_tvMaterialName, item.getMaterialname());
    }
}
