package com.besthings.pdm.adapter;

import android.widget.ImageView;

import com.besthings.bean.BuyerSampleNotesRet;
import com.besthings.pdm.R;
import com.besthings.pdm.utils.ImageUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Chas on 2017/10/18 0018.
 */

public class BuyerSampleNotesAdapter extends BaseQuickAdapter<BuyerSampleNotesRet, BaseViewHolder> {
    public BuyerSampleNotesAdapter(int layoutResId, List<BuyerSampleNotesRet> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BuyerSampleNotesRet item) {
        ((ImageView) helper.getView(R.id.item_buyer_sample_note_ivPicture)).setScaleType(ImageView.ScaleType.FIT_CENTER);
        if (item.getPicurl().equals("")) {
            helper.setImageResource(R.id.item_buyer_sample_note_ivPicture, R.drawable.empty);
        } else {
            helper.setImageBitmap(R.id.item_buyer_sample_note_ivPicture, ImageUtil.getURLImage(item.getPicurl()));
        }
        helper.setText(R.id.item_buyer_sample_note_tvNotes, item.getDesc());
        helper.setText(R.id.item_buyer_sample_note_tvRemark, item.getRdesc());
    }
}
