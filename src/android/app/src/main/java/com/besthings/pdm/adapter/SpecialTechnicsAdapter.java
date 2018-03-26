package com.besthings.pdm.adapter;

import android.widget.ImageView;

import com.besthings.bean.SampleManagerEditionRecordRet;
import com.besthings.bean.SpecialTechnicsRet;
import com.besthings.pdm.R;
import com.besthings.pdm.utils.ImageUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by chas on 2017/10/25.
 */

public class SpecialTechnicsAdapter extends BaseQuickAdapter<SpecialTechnicsRet, BaseViewHolder> {
    public SpecialTechnicsAdapter(int layoutResId, List<SpecialTechnicsRet> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SpecialTechnicsRet item) {
        ((ImageView) helper.getView(R.id.item_special_technics_ivPic)).setScaleType(ImageView.ScaleType.FIT_CENTER);
        if (item.getStylepic().equals("")) {
            helper.setImageResource(R.id.item_special_technics_ivPic, R.drawable.empty);
        } else {
            helper.setImageBitmap(R.id.item_special_technics_ivPic, ImageUtil.getURLImage(item.getStylepic()));
        }
        helper.setText(R.id.item_special_technics_tvNo, item.getCproductcode());
        helper.setText(R.id.item_special_technics_tvMaker, item.getDesigner());
        helper.setText(R.id.item_special_technics_tvStyle, item.getStyle());
    }
}
