package com.besthings.pdm.adapter;

import android.widget.ImageView;

import com.besthings.bean.SampleManagerRet;
import com.besthings.pdm.R;
import com.besthings.pdm.utils.ImageUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Chas on 2017/10/13 0013.
 */

public class SampleManagerAdapter extends BaseQuickAdapter<SampleManagerRet, BaseViewHolder> {
    public SampleManagerAdapter(int layoutResId, List<SampleManagerRet> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SampleManagerRet item) {
        ((ImageView) helper.getView(R.id.item_sample_manager_ivPic)).setScaleType(ImageView.ScaleType.FIT_CENTER);
        if (item.getPic().equals("")) {
            helper.setImageResource(R.id.item_sample_manager_ivPic, R.drawable.empty);
        } else {
            helper.setImageBitmap(R.id.item_sample_manager_ivPic, ImageUtil.getURLImage(item.getPic()));
        }
        helper.setText(R.id.item_sample_manager_tvStyleNo,item.getStyleNo());
        helper.setText(R.id.item_sample_manager_tvSampleNo,item.getClothSampleNo());
        helper.setText(R.id.item_sample_manager_tvDesigner,item.getDesigner());
        helper.setText(R.id.item_sample_manager_tvTempleteMan,item.getTempleteMan());
        helper.setText(R.id.item_sample_manager_tvTechnician,item.getTechnician());
        helper.setText(R.id.item_sample_manager_tvProgressVersion,item.getSort());
    }
}
