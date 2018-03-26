package com.besthings.pdm.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import com.besthings.bean.MaterialPurchaseListRet;
import com.besthings.pdm.R;
import com.besthings.pdm.adapter.MaterialPurchaseAdapter;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class MaterialPurchaseActivity extends BaseActivity {

    private static final String TAG = MaterialPurchaseActivity.class.getSimpleName();

    String mPurchase = "";
    String mSoSearch = "";
    String mFilterJson = "";
    JSONObject mFilterJsonObject;
    String mStartDate = "";
    String mEndDate = "";
    DateFormat mSDF = new SimpleDateFormat("yyyy-MM-dd");
    Calendar mCalendar = Calendar.getInstance();
    Calendar mGCalendar = new GregorianCalendar();
    List<MaterialPurchaseListRet> mDataList = new ArrayList<>();
    List<MaterialPurchaseListRet> mBaseList = new ArrayList<>();
    MaterialPurchaseAdapter mAdapter;
    RecyclerView mRecycleView;
    RefreshLayout refreshLayout;
    Spinner spFilter;
    EditText etSearch;
    Button btnSearch;
    Button btnDayOne;
    Button btnDayThree;
    Button btnDayWeek;
    Button btnCustomize;
    int mPageStart;
    int mPageEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_purchase);

        setCustomActionBar(true);

        btnDayOne = (Button) findViewById(R.id.material_purchase_btnDayOne);
        btnDayThree = (Button) findViewById(R.id.material_purchase_btnDayThr);
        btnDayWeek = (Button) findViewById(R.id.material_purchase_btnDaySev);
        btnCustomize = (Button) findViewById(R.id.material_purchase_btnDayDef);
        btnSearch = (Button) findViewById(R.id.material_purchase_btnSearch);
        etSearch = (EditText) findViewById(R.id.material_purchase_etSearch);
        refreshLayout = (RefreshLayout) findViewById(R.id.material_purchase_refreshLayout);

        spFilter = (Spinner) findViewById(R.id.material_purchase_sFilter);
        ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"筛选","采购单号"});
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFilter.setAdapter(spAdapter);

        mRecycleView = (RecyclerView) findViewById(R.id.material_purchase_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(linearLayoutManager);
        mRecycleView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        refreshLayout.setEnableRefresh(true);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                ((View) refreshlayout).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPageStart = 1;
                        mPageEnd = 10;
                        buildQueryJson();
                        try {
                            mDataList.clear();
                            mDataList = myApp.NetHelperEx.getMaterialPurchaseListBean(mFilterJson.substring(1, mFilterJson.length() - 1)).getRet();
                            mBaseList = mDataList;
                            mAdapter = new MaterialPurchaseAdapter(R.layout.item_material_purchase, mDataList);
                            mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
                            mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                    Intent it = new Intent(MaterialPurchaseActivity.this, MaterialPurchaseDetailActivity.class);
                                    it.putExtra("MID", mBaseList.get(position).getMid());
                                    it.putExtra("title", "物料采购");
                                    startActivity(it);
                                }
                            });
                            mRecycleView.setAdapter(mAdapter);
                            if (mBaseList.size() >= 10) {
                                mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                                    @Override
                                    public void onLoadMoreRequested() {
                                        mRecycleView.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                mPageStart = mPageEnd + 1;
                                                mPageEnd += 10;
                                                buildQueryJson();
                                                try {
                                                    mDataList = myApp.NetHelperEx.getMaterialPurchaseListBean(mFilterJson.substring(1, mFilterJson.length() - 1)).getRet();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                                if (mDataList.size() > 0) {
                                                    mAdapter.addData(mDataList);
                                                    mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                                        @Override
                                                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                                            Intent it = new Intent(MaterialPurchaseActivity.this, MaterialPurchaseDetailActivity.class);
                                                            it.putExtra("MID", mBaseList.get(position).getMid());
                                                            it.putExtra("title", "物料采购");
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
                        } catch (IOException e) {
                            e.printStackTrace();
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
        refreshLayout.setDisableContentWhenRefresh(true);
        refreshLayout.autoRefresh();

        btnCustomize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        btnDayOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCalendar.setTime(new Date());
                mEndDate = mSDF.format(mCalendar.getTime());
                mCalendar.add(Calendar.DAY_OF_MONTH, 0);
                mStartDate = mSDF.format(mCalendar.getTime());
                btnCustomize.setSelected(false);
                btnDayOne.setSelected(true);
                btnDayThree.setSelected(false);
                btnDayWeek.setSelected(false);
                btnSearch.callOnClick();
            }
        });

        btnDayThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCalendar.setTime(new Date());
                mEndDate = mSDF.format(mCalendar.getTime());
                mCalendar.add(Calendar.DAY_OF_MONTH, -2);
                mStartDate = mSDF.format(mCalendar.getTime());
                btnCustomize.setSelected(false);
                btnDayOne.setSelected(false);
                btnDayThree.setSelected(true);
                btnDayWeek.setSelected(false);
                btnSearch.callOnClick();
            }
        });

        btnDayWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCalendar.setTime(new Date());
                mEndDate = mSDF.format(mCalendar.getTime());
                mCalendar.add(Calendar.DAY_OF_MONTH, -6);
                mStartDate = mSDF.format(mCalendar.getTime());
                btnCustomize.setSelected(false);
                btnDayOne.setSelected(false);
                btnDayThree.setSelected(false);
                btnDayWeek.setSelected(true);
                btnSearch.callOnClick();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshLayout.autoRefresh();
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

    private void buildQueryJson() {
        //clear
        mPurchase = "";
        mFilterJson = "";
        mFilterJsonObject = new JSONObject();
        try {
            switch (spFilter.getSelectedItemPosition()) {
                case 0:
                    mSoSearch = etSearch.getText().toString();
                    break;
                //
                case 1:
                    mPurchase = etSearch.getText().toString();
                    break;
                //筛选(0)
                default:
                    mSoSearch = "";
                    mPurchase = "";
                    break;
            }
            mFilterJsonObject.put("state", "-1");
            mFilterJsonObject.put("NumPerPg", 1000);
            //开始日期
            if (!mStartDate.equals("")) {
                mFilterJsonObject.put("StartDate", mStartDate);
            }
            //结束日期
            if (!mEndDate.equals("")) {
                mFilterJsonObject.put("EndDate", mEndDate);
            }
            if (!mSoSearch.equals("")) {
                mFilterJsonObject.put("sosearch", mSoSearch);
            }
            if (!mPurchase.equals("")) {
                mFilterJsonObject.put("sosearch", mPurchase);
            }
            mFilterJsonObject.put("fPage", 1);
            mFilterJsonObject.put("PageCnt", "1000");
            mFilterJsonObject.put("PageStart", mPageStart);
            mFilterJsonObject.put("PageEnd", mPageEnd);
            mFilterJson = mFilterJsonObject.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showDatePickerDialog() {
        final View dpLayout = getLayoutInflater().inflate(R.layout.item_datepicker, null);
        final DatePicker dpStart = (DatePicker) dpLayout.findViewById(R.id.item_datepicker_dpStartDate);
        final DatePicker dpEnd = (DatePicker) dpLayout.findViewById(R.id.item_datepicker_dpEndDate);

        if (!TextUtils.isEmpty(mStartDate)) {
            String[] dtArray = mStartDate.split("-");
            dpStart.updateDate(Integer.valueOf(dtArray[0]), Integer.valueOf(dtArray[1]) - 1, Integer.valueOf(dtArray[2]));
        }

        if (!TextUtils.isEmpty(mEndDate)) {
            String[] dtArray = mEndDate.split("-");
            dpEnd.updateDate(Integer.valueOf(dtArray[0]), Integer.valueOf(dtArray[1]) - 1, Integer.valueOf(dtArray[2]));
        }

        new AlertDialog.Builder(this)
                .setTitle("自定义日期")
                .setView(dpLayout)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        btnCustomize.setSelected(true);
                        btnDayOne.setSelected(false);
                        btnDayThree.setSelected(false);
                        btnDayWeek.setSelected(false);
                        mGCalendar.set(dpStart.getYear(), dpStart.getMonth(), dpStart.getDayOfMonth());
                        mStartDate = mSDF.format(mGCalendar.getTime());
                        mGCalendar.set(dpEnd.getYear(), dpEnd.getMonth(), dpEnd.getDayOfMonth());
                        mEndDate = mSDF.format(mGCalendar.getTime());
                        btnSearch.callOnClick();
                    }
                })
                .setNegativeButton("返回",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                .show();
    }
}
