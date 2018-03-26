package com.besthings.pdm.adapter;

import android.widget.ImageView;

import com.besthings.bean.TeamClothesCustomOrderListRet;
import com.besthings.pdm.R;
import com.besthings.pdm.utils.ImageUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Chas on 2017/10/27 0027.
 */

public class TeamClothesCustomOrderListAdapter extends BaseQuickAdapter<TeamClothesCustomOrderListRet, BaseViewHolder> {
    public TeamClothesCustomOrderListAdapter(int layoutResId, List<TeamClothesCustomOrderListRet> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TeamClothesCustomOrderListRet item) {
        ((ImageView) helper.getView(R.id.item_team_clothes_custom_ivPic)).setScaleType(ImageView.ScaleType.FIT_CENTER);
        if (item.getPic().equals("")) {
            helper.setImageResource(R.id.item_team_clothes_custom_ivPic, R.drawable.empty);
        } else {
            helper.setImageBitmap(R.id.item_team_clothes_custom_ivPic, ImageUtil.getURLImage(item.getPic()));
        }
        helper.setText(R.id.item_team_clothes_custom_tvOrderNo, item.getOrderno());
        helper.setText(R.id.item_team_clothes_custom_tvCustName, item.getCustomer());
        helper.setText(R.id.item_team_clothes_custom_tvAcceptTime, item.getAccept());
        helper.setText(R.id.item_team_clothes_custom_tvAppointTime, item.getDdd());
    }
}
