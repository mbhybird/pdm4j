package com.besthings.pdm.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import com.besthings.bean.MaterialPurchaseDetailListRet;
import com.besthings.pdm.R;
import com.besthings.pdm.adapter.MaterialPurchaseDetailListAdapter;

import java.io.IOException;
import java.util.List;

public class MaterialPurchaseDetailActivity extends BaseActivity {

    List<MaterialPurchaseDetailListRet> mDataList;
    RecyclerView mRecyclerView;
    MaterialPurchaseDetailListAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_purchase_detail);

        setCustomActionBar(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.item_material_purchase_detail_rvGrid);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        if (getIntent().hasExtra("MID")) {
            String MID = getIntent().getStringExtra("MID");
            try {
                mDataList = myApp.NetHelperEx.getMaterialPurchaseDetailListBean(MID).getRet();
                mRecyclerView.setLayoutManager(linearLayoutManager);
                mAdapter = new MaterialPurchaseDetailListAdapter(R.layout.item_material_purchase_detail, mDataList);
                mRecyclerView.setAdapter(mAdapter);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
        }
        return true;
    }
}
