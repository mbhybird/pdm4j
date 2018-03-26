package com.besthings.pdm.activity;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;

import com.besthings.bean.SpecialTechnicsDetailRet;
import com.besthings.pdm.R;
import com.besthings.pdm.adapter.SpecialTechnicsDetailAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.io.IOException;
import java.util.List;

public class SpecialTechnicsDetailActivity extends BaseActivity {

    RecyclerView mRecyclerView;
    SpecialTechnicsDetailAdapter mAdapter;
    List<SpecialTechnicsDetailRet> mDataList;
    String mSeriesID;
    String mMID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_technics_detail);

        setCustomActionBar(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.special_technics_detail_recyclerView);

        if (getIntent().hasExtra("MID")) {
            mMID = getIntent().getStringExtra("MID");
        }
        try {
            mSeriesID = myApp.NetHelper.getSeriesIDBean().getRet().get(0).getSeriesID();

        }catch (Exception e) {
            e.printStackTrace();
        }
        LinearLayoutManager LayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(LayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        try {
            mDataList = myApp.NetHelperEx.getSpecialTechnicsDetailBean(mSeriesID, mMID).getRet();
            mAdapter = new SpecialTechnicsDetailAdapter(R.layout.item_special_technics_detail, mDataList);
            mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
        }
        return true;
    }
}
