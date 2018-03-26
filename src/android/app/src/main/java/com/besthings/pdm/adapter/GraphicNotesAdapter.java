package com.besthings.pdm.adapter;

import com.besthings.bean.DesignFileListRet;
import com.besthings.pdm.R;
import com.besthings.pdm.utils.ImageUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Chas on 2017/9/14 0014.
 */

public class GraphicNotesAdapter extends BaseQuickAdapter<DesignFileListRet, BaseViewHolder> {
    public GraphicNotesAdapter(int layoutResId, List<DesignFileListRet> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, DesignFileListRet item) {
        holder.setText(R.id.graphic_tvStyleNo, item.getStyleNo());
        holder.setText(R.id.graphic_tvMaker, item.getMaker());
        holder.setText(R.id.graphic_tvTheme, item.getTheme());
        holder.setText(R.id.graphic_tvStyle, item.getStyle());
        holder.setText(R.id.graphic_tvYear, String.valueOf(item.getYear()));
        holder.setText(R.id.graphic_tvBand, item.getBand());
        holder.setText(R.id.graphic_tvCategory, item.getCategory());
        holder.setText(R.id.graphic_tvSeason, item.getSeason());
        if (item.getIMinPicture().equals("")) {
            holder.setImageResource(R.id.iv_graphic_notes, R.drawable.empty);
        } else {
            holder.setImageBitmap(R.id.iv_graphic_notes, ImageUtil.getURLImage(item.getIMinPicture()));
        }

    }
}
