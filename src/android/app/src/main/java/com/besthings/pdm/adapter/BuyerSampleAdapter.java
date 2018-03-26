package com.besthings.pdm.adapter;

import android.widget.ImageView;

import com.besthings.bean.BuyerSampleRet;
import com.besthings.pdm.utils.ImageUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import com.besthings.pdm.R;

/**
 * Created by NickChung on 27/09/2017.
 */

public class BuyerSampleAdapter extends BaseQuickAdapter<BuyerSampleRet, BaseViewHolder> {
    public BuyerSampleAdapter(int layoutResId, List<BuyerSampleRet> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, BuyerSampleRet item) {
        ((ImageView) viewHolder.getView(R.id.item_buyer_sample_ivPic)).setScaleType(ImageView.ScaleType.FIT_CENTER);
        if (item.getIMinPicture().equals("")) {
            viewHolder.setImageResource(R.id.item_buyer_sample_ivPic, R.drawable.empty);
        } else {
            viewHolder.setImageBitmap(R.id.item_buyer_sample_ivPic, ImageUtil.getURLImage(item.getIMinPicture()));
        }
        viewHolder.setText(R.id.item_buyer_sample_tvSampleNo,item.getCClothSampleNo());
        viewHolder.setText(R.id.item_buyer_sample_tvCategory,item.getCCategory());
        viewHolder.setText(R.id.item_buyer_sample_tvSamplePoint,item.getCRemark2());
        viewHolder.setText(R.id.item_buyer_sample_tvPrice,item.getDPrice1());
        viewHolder.setText(R.id.item_buyer_sample_tvBuyer,item.getCBuyer());
        viewHolder.setText(R.id.item_buyer_sample_tvStyleNo,item.getCStyleNo());
        viewHolder.setText(R.id.item_buyer_sample_tvBrand,item.getCBrand());
    }
}
