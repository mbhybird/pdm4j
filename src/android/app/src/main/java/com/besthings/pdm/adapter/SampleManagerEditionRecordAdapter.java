package com.besthings.pdm.adapter;

import android.widget.ImageView;

import com.besthings.bean.SampleManagerEditionRecordBean;
import com.besthings.bean.SampleManagerEditionRecordRet;
import com.besthings.bean.SampleManagerRet;
import com.besthings.pdm.R;
import com.besthings.pdm.utils.ImageUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Chas on 2017/10/23 0023.
 */

public class SampleManagerEditionRecordAdapter extends BaseQuickAdapter<SampleManagerEditionRecordRet, BaseViewHolder> {
    public SampleManagerEditionRecordAdapter(int layoutResId, List<SampleManagerEditionRecordRet> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SampleManagerEditionRecordRet item) {
        ((ImageView) helper.getView(R.id.item_sample_manager_note_ivPicture)).setScaleType(ImageView.ScaleType.FIT_CENTER);
        if (item.getPic().equals("")) {
            helper.setImageResource(R.id.item_sample_manager_note_ivPicture, R.drawable.empty);
        } else {
            helper.setImageBitmap(R.id.item_sample_manager_note_ivPicture, ImageUtil.getURLImage(item.getPic()));
        }
        helper.setText(R.id.item_sample_manager_note_tvNotes, item.getDifference());
    }




}
