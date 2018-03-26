package com.besthings.pdm.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import com.besthings.bean.DesignMaterialRet;
import com.besthings.pdm.R;
import com.besthings.pdm.adapter.DesignMaterialAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DesignMaterialActivity extends BaseActivity {
    private static final String TAG = DesignMaterialActivity.class.getSimpleName();

    DesignMaterialAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private GridLayoutManager mLayoutManager;
    private RefreshLayout mRefreshLayout;
    List<DesignMaterialRet> mDataList = new ArrayList<>();
    List<DesignMaterialRet> mBaseList = new ArrayList<>();
    String mJson = null;
    private JSONObject jsonObject = new JSONObject();
    Button btnSearch;
    int mPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design_material);

        setCustomActionBar(true);

        final ImageButton ibCamera = (ImageButton) findViewById(R.id.design_material_ibCamera);
        final EditText etSearch = (EditText) findViewById(R.id.design_material_etSearch);
        btnSearch = (Button) findViewById(R.id.design_material_btnSearch);
        final Button btnClear = (Button) findViewById(R.id.design_material_btnClear);


        mRecyclerView = (RecyclerView) findViewById(R.id.design_material_recyclerView);
        mLayoutManager = new GridLayoutManager(DesignMaterialActivity.this, 3);
        mRecyclerView.setLayoutManager(mLayoutManager);

        ibCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DesignMaterialActivity.this, DesignMaterialCaptureActivity.class);
                startActivity(intent);
            }
        });

        mRefreshLayout = (RefreshLayout) findViewById(R.id.design_material_refreshLayout);
        mRefreshLayout.setEnableRefresh(true);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                ((View) refreshlayout).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPage = 0;
                        initJson();
                        String matName= etSearch.getText().toString();
                        try {
                            jsonObject.put("MatName", matName);
                            mJson = jsonObject.toString();
                            mDataList = myApp.NetHelperEx.getDesignMaterialBean(mJson).getRet();
                            mBaseList = mDataList;
                            mAdapter = new DesignMaterialAdapter(R.layout.item_design_material, mDataList);
                            mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                    Intent it = new Intent(DesignMaterialActivity.this, DesignMaterialDetailActivity.class);
                                    it.putExtra("pic", mBaseList.get(position).getPic());
                                    it.putExtra("MaterialName", mBaseList.get(position).getMaterialname());
                                    it.putExtra("Width", mBaseList.get(position).getWidth());
                                    it.putExtra("Specs", mBaseList.get(position).getSpecs());
                                    it.putExtra("Price", mBaseList.get(position).getPrice());
                                    it.putExtra("MoneyUnit", mBaseList.get(position).getMoneyunit());
                                    it.putExtra("Color", mBaseList.get(position).getColor());
                                    it.putExtra("Unit", mBaseList.get(position).getUnit());
                                    it.putExtra("SupplierName", mBaseList.get(position).getSuppliername());
                                    it.putExtra("LinkMan", mBaseList.get(position).getLinkman());
                                    it.putExtra("SupplierPhone", mBaseList.get(position).getSupplierphone());
                                    it.putExtra("SupplierAddr", mBaseList.get(position).getSupplieraddr());

                                    startActivity(it);
                                }
                            });
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                        mRecyclerView.setAdapter(mAdapter);
                        if (mBaseList.size() >= 10) {
                            mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                                @Override
                                public void onLoadMoreRequested() {
                                    mRecyclerView.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            mPage += 1;
                                            initJson();
                                            try {
                                                mDataList = myApp.NetHelperEx.getDesignMaterialBean(mJson).getRet();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            if (mDataList.size() > 0) {
                                                mAdapter.addData(mDataList);
                                                mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                                        Intent it = new Intent(DesignMaterialActivity.this, DesignMaterialDetailActivity.class);
                                                        it.putExtra("pic", mBaseList.get(position).getPic());
                                                        it.putExtra("MaterialName", mBaseList.get(position).getMaterialname());
                                                        it.putExtra("Width", mBaseList.get(position).getWidth());
                                                        it.putExtra("Specs", mBaseList.get(position).getSpecs());
                                                        it.putExtra("Price", mBaseList.get(position).getPrice());
                                                        it.putExtra("Color", mBaseList.get(position).getColor());
                                                        it.putExtra("Unit", mBaseList.get(position).getMoneyunit());
                                                        it.putExtra("SupplierName", mBaseList.get(position).getSuppliername());
                                                        it.putExtra("LinkMan", mBaseList.get(position).getLinkman());
                                                        it.putExtra("SupplierPhone", mBaseList.get(position).getSupplierphone());
                                                        it.putExtra("SupplierAddr", mBaseList.get(position).getSupplieraddr());

                                                        startActivity(it);
                                                    }
                                                });
                                                mAdapter.notifyDataSetChanged();
                                                mAdapter.loadMoreComplete();

                                            } else {
                                                mAdapter.loadMoreEnd();
                                            }
                                        }
                                    }, 500);

                                }
                            });
                        }
                        refreshlayout.finishRefresh();
                    }
                }, 2000);
            }
        });

        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                return new ClassicsHeader(context);
            }
        });
        mRefreshLayout.setDisableContentWhenRefresh(true);
        mRefreshLayout.autoRefresh();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRefreshLayout.autoRefresh();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etSearch.setText("");
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
        }
        return true;
    }

    private void initJson() {
        mJson = "";
        jsonObject = new JSONObject();
        try {
            jsonObject.put("ID",3503);
            jsonObject.put("SeriesID", myApp.mSeriesID);
            jsonObject.put("BType", "APP物料");
            jsonObject.put("MType", "");
            jsonObject.put("SType", "");
            jsonObject.put("page", mPage);
            jsonObject.put("NumPerPg", 15);
            jsonObject.put("getpic", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mJson = jsonObject.toString();
    }
}
