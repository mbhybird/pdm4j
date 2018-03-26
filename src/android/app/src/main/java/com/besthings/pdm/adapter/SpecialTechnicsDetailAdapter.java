package com.besthings.pdm.adapter;


import android.widget.ImageView;

import com.besthings.bean.SpecialTechnicsDetailRet;
import com.besthings.pdm.R;
import com.besthings.pdm.utils.ImageUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;


/**
 * Created by chas on 2017/10/26.
 */

public class SpecialTechnicsDetailAdapter extends BaseQuickAdapter<SpecialTechnicsDetailRet, BaseViewHolder> {
    public SpecialTechnicsDetailAdapter(int layoutResId, List<SpecialTechnicsDetailRet> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SpecialTechnicsDetailRet item) {
        ((ImageView) helper.getView(R.id.item_special_technics_detail_ivPic)).setScaleType(ImageView.ScaleType.FIT_CENTER);
        if (item.getSpic().equals("")) {
            helper.setImageResource(R.id.item_special_technics_detail_ivPic, R.drawable.empty);
        } else {
            helper.setImageBitmap(R.id.item_special_technics_detail_ivPic, ImageUtil.getURLImage(item.getSpic()));
        }
        helper.setText(R.id.item_special_technics_tvType, item.getType());
        helper.setText(R.id.item_special_technics_tvPosition, item.getPosition());
        helper.setText(R.id.item_special_technics_tvCust, item.getCustname());
        helper.setText(R.id.item_special_technics_tvSum, item.getSum());
        helper.setText(R.id.item_special_technics_tvArrangeTime, item.getArrangetime());
        helper.setText(R.id.item_special_technics_tvRevertTime, item.getT6());
        helper.setText(R.id.item_special_technics_tvPhone, item.getPhone());
    }

}
