package com.besthings.pdm.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.besthings.bean.WarningTipsPersonCustomRet;
import com.besthings.pdm.R;
import com.besthings.pdm.adapter.WarningTipsAdapter;
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

public class WarningTipsActivity extends BaseActivity {
    private static final String TAG = WarningTipsActivity.class.getSimpleName();
    TextView tvPersonCust;
    TextView tvTeamCust;
    TextView tvBatchCust;
    TextView tvResearch;
    TextView tvMatState;
    TextView tvPlateState;
    TextView tvCutState;
    TextView tvClothState;
    TextView tvAssemblyState;
    TextView tvStockState;
    TextView tvDeliver;
    TextView tvAll;

    Button btnDayOne;
    Button btnDayThr;
    Button btnDaySev;
    Button btnDayDef;
    Button btnSearch;

    EditText etSearch;
    RecyclerView mRecyclerView;
    RefreshLayout mRefreshLayout;
    Calendar mCalendar = Calendar.getInstance();
    Calendar mGCalendar = new GregorianCalendar();
    DateFormat mSDF = new SimpleDateFormat("yyyy-MM-dd");
    JSONObject jsonObject = new JSONObject();
    List<WarningTipsPersonCustomRet> mDataList = new ArrayList<>();
    WarningTipsAdapter mAdapter;
    String mStartDate = "";
    String mEndDate = "";
    String mJson;
    String mMatState = "";
    String mPlateState = "";
    String mCutState = "";
    String mClothState = "";
    String mAssemblyState = "";
    String mStockState = "";
    String mState = "";
    int mPageStart;
    int mPageEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning_tips);

        setCustomActionBar(true);

        tvPersonCust = (TextView) findViewById(R.id.warning_tips_tvPersonCust);
        tvTeamCust = (TextView) findViewById(R.id.warning_tips_tvTeamCust);
        tvBatchCust = (TextView) findViewById(R.id.warning_tips_tvBatchCust);
        tvResearch = (TextView) findViewById(R.id.warning_tips_tvResearch);
        btnDayOne = (Button) findViewById(R.id.warning_tips_btnDayOne);
        btnDayThr = (Button) findViewById(R.id.warning_tips_btnDayThr);
        btnDaySev = (Button) findViewById(R.id.warning_tips_btnDaySev);
        btnDayDef = (Button) findViewById(R.id.warning_tips_btnDayDef);
        etSearch = (EditText) findViewById(R.id.warning_tips_etSearch);
        btnSearch = (Button) findViewById(R.id.warning_tips_btnSearch);

        tvAll = (TextView) findViewById(R.id.warning_tips_tvAll);
        tvMatState = (TextView) findViewById(R.id.warning_tips_tvMatState);
        tvPlateState = (TextView) findViewById(R.id.warning_tips_tvPlateState);
        tvCutState = (TextView) findViewById(R.id.warning_tips_tvCutState);
        tvClothState = (TextView) findViewById(R.id.warning_tips_tvClothState);
        tvAssemblyState = (TextView) findViewById(R.id.warning_tips_tvAssemblyState);
        tvStockState = (TextView) findViewById(R.id.warning_tips_tvStockState);
        tvDeliver = (TextView) findViewById(R.id.warning_tips_tvDeliver);
        mRefreshLayout = (RefreshLayout) findViewById(R.id.warning_tips_refreshLayout);

        tvAll.setSelected(true);

        tvPersonCust.setSelected(true);
        mRecyclerView = (RecyclerView) findViewById(R.id.warning_tips_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        mRefreshLayout.setEnableRefresh(true);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                ((View) refreshlayout).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPageStart = 1;
                        mPageEnd = 10;
                        initJson();
                        try {
                            mDataList.clear();
                            mDataList = myApp.NetHelperEx.getWarningTipsPersonCustomBean(mJson).getRet();
                            mAdapter = new WarningTipsAdapter(R.layout.item_warning_tips, mDataList);
                            mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
                            mRecyclerView.setAdapter(mAdapter);
                            if (mDataList.size() >= 10) {
                                mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                                    @Override
                                    public void onLoadMoreRequested() {
                                        mRecyclerView.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                mPageStart = mPageEnd + 1;
                                                mPageEnd += 10;
                                                initJson();
                                                try {
                                                    mDataList = myApp.NetHelperEx.getWarningTipsPersonCustomBean(mJson).getRet();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                                if (mDataList.size() > 0) {
                                                    mAdapter.addData(mDataList);
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
        mRefreshLayout.setDisableContentWhenRefresh(true);
        mRefreshLayout.autoRefresh();

        btnDayOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnDayClear();
                btnDayOne.setSelected(true);
                mCalendar.setTime(new Date());
                mEndDate = mSDF.format(mCalendar.getTime());
                mCalendar.add(Calendar.DAY_OF_MONTH, -1);
                mStartDate = mSDF.format(mCalendar.getTime());
                btnSearch.callOnClick();
            }
        });

        btnDayThr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnDayClear();
                btnDayThr.setSelected(true);
                mCalendar.setTime(new Date());
                mEndDate = mSDF.format(mCalendar.getTime());
                mCalendar.add(Calendar.DAY_OF_MONTH, -3);
                mStartDate = mSDF.format(mCalendar.getTime());
                btnSearch.callOnClick();
            }
        });

        btnDaySev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnDayClear();
                btnDaySev.setSelected(true);
                mCalendar.setTime(new Date());
                mEndDate = mSDF.format(mCalendar.getTime());
                mCalendar.add(Calendar.DAY_OF_MONTH, -7);
                mStartDate = mSDF.format(mCalendar.getTime());
                btnSearch.callOnClick();
            }
        });


        btnDayDef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();

            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRefreshLayout.autoRefresh();
            }
        });


        tvPlateState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPlateStateAlertDialog();
            }
        });

        tvClothState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showClothStateAlertDialog();
            }
        });

        tvStockState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStockStateAlertDialog();
            }
        });

        tvMatState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMatStateAlertDialog();
            }
        });

        tvCutState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCutStateAlertDialog();
            }
        });

        tvAssemblyState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAssemblyStateAlertDialog();
            }
        });

        tvDeliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeliverStateAlertDialog();
            }
        });

        tvAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedInit();
                tvAll.setSelected(true);
                btnSearch.callOnClick();
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

    public void btnDayClear() {
        btnDayOne.setSelected(false);
        btnDayThr.setSelected(false);
        btnDaySev.setSelected(false);
        btnDayDef.setSelected(false);
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
                        btnDayClear();
                        btnDayDef.setSelected(true);
                        mGCalendar.set(dpStart.getYear(), dpStart.getMonth(), dpStart.getDayOfMonth());
                        mStartDate = mSDF.format(mGCalendar.getTime());
                        mGCalendar.set(dpEnd.getYear(), dpEnd.getMonth(), dpEnd.getDayOfMonth());
                        mEndDate = mSDF.format(mGCalendar.getTime());
                        btnSearch.callOnClick();
                        Log.i(TAG, mStartDate + "," + mEndDate);
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

    public void selectedInit() {
        mState = "";
        mMatState = "";
        mClothState = "";
        mCutState = "";
        mPlateState = "";
        mAssemblyState = "";
        mStockState = "";
        tvAll.setSelected(false);
        tvDeliver.setSelected(false);
        tvMatState.setSelected(false);
        tvClothState.setSelected(false);
        tvCutState.setSelected(false);
        tvStockState.setSelected(false);
        tvPlateState.setSelected(false);
        tvAssemblyState.setSelected(false);
    }


    private void initJson() {
        mJson = "";
        jsonObject = new JSONObject();
        try {
            jsonObject.put("ID", 14015);
            jsonObject.put("SeriesID", myApp.mSeriesID);
            jsonObject.put("Time", "time2000");
            jsonObject.put("StartDate", mStartDate);
            jsonObject.put("EndDate", mEndDate);
            jsonObject.put("SrchFld", "");
            jsonObject.put("NumPerPg", 1000);
            jsonObject.put("State", -1);
            if (!mPlateState.equals("")) {
                jsonObject.put("PlateState", mPlateState);
            }
            if (!mMatState.equals("")) {
                jsonObject.put("MatState", mMatState);
            }
            if (!mCutState.equals("")) {
                jsonObject.put("CutState", mCutState);
            }
            if (!mClothState.equals("")) {
                jsonObject.put("ClothState", mClothState);
            }
            if (!mAssemblyState.equals("")) {
                jsonObject.put("AssemblyState", mAssemblyState);
            }
            if (!mStockState.equals("")) {
                jsonObject.put("StockState", mStockState);
            }
            if (!mState.equals("")) {
                jsonObject.put("State", mState);
            }
            if (!etSearch.getText().toString().equals("")) {
                jsonObject.put("sosearch", etSearch.getText().toString());
            }
            jsonObject.put("fPage", 1);
            jsonObject.put("PageCnt", "1000");
            jsonObject.put("PageStart", mPageStart);
            jsonObject.put("PageEnd", mPageEnd);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mJson = jsonObject.toString();
    }


    private void showMatStateAlertDialog() {
        final String[] items = { "无面料","未备料","备料中","已备料"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(WarningTipsActivity.this);
        listDialog.setTitle("请选择备料状态");
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        mMatState = "-2";
                        break;
                    case 1:
                        mMatState = "-1";
                        break;
                    case 2:
                        mMatState = "1";
                        break;
                    case 3:
                        mMatState = "2";
                        break;
                    default:
                        mMatState = "";
                        break;
                }
                btnSearch.callOnClick();
                selectedInit();
                tvMatState.setSelected(true);
            }
        });
        listDialog.show();
    }

    private void showPlateStateAlertDialog() {
        final String[] items = { "未开始","进行中","已完成"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(WarningTipsActivity.this);
        listDialog.setTitle("请选择打版状态");
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        mPlateState = "-1";
                        break;
                    case 1:
                        mPlateState = "1";
                        break;
                    case 2:
                        mPlateState = "2";
                        break;
                    default:
                        mPlateState = "";
                        break;
                }
                btnSearch.callOnClick();
                selectedInit();
                tvPlateState.setSelected(true);
            }
        });
        listDialog.show();
    }

    private void showCutStateAlertDialog() {
        final String[] items = { "未开始","进行中","已完成"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(WarningTipsActivity.this);
        listDialog.setTitle("请选择车版状态");
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        mCutState = "-1";
                        break;
                    case 1:
                        mCutState = "1";
                        break;
                    case 2:
                        mCutState = "2";
                        break;
                    default:
                        mCutState = "";
                        break;
                }
                btnSearch.callOnClick();
                selectedInit();
                tvCutState.setSelected(true);
            }
        });
        listDialog.show();
    }

    private void showClothStateAlertDialog() {
        final String[] items = { "未开始","进行中","已完成"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(WarningTipsActivity.this);
        listDialog.setTitle("请选择车版状态");
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        mClothState = "-1";
                        break;
                    case 1:
                        mClothState = "1";
                        break;
                    case 2:
                        mClothState = "2";
                        break;
                    default:
                        mClothState = "";
                        break;
                }
                btnSearch.callOnClick();
                selectedInit();
                tvClothState.setSelected(true);
            }
        });
        listDialog.show();
    }

    private void showAssemblyStateAlertDialog() {
        final String[] items = { "未开始","进行中","已完成"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(WarningTipsActivity.this);
        listDialog.setTitle("请选择后道状态");
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        mAssemblyState = "-1";
                        break;
                    case 1:
                        mAssemblyState = "1";
                        break;
                    case 2:
                        mAssemblyState = "2";
                        break;
                    default:
                        mAssemblyState = "";
                        break;
                }
                btnSearch.callOnClick();
                selectedInit();
                tvAssemblyState.setSelected(true);
            }
        });
        listDialog.show();
    }

    private void showStockStateAlertDialog() {
        final String[] items = { "未入库","进行中","已入库"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(WarningTipsActivity.this);
        listDialog.setTitle("请选择入库状态");
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        mStockState = "-1";
                        break;
                    case 1:
                        mStockState = "1";
                        break;
                    case 2:
                        mStockState = "2";
                        break;
                    default:
                        mStockState = "";
                        break;
                }
                btnSearch.callOnClick();
                selectedInit();
                tvStockState.setSelected(true);
            }
        });
        listDialog.show();
    }

    private void showDeliverStateAlertDialog() {
        final String[] items = {"未发货","已发货"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(WarningTipsActivity.this);
        listDialog.setTitle("请选择发货状态");
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        mState = "0";
                        break;
                    case 1:
                        mState = "1";
                        break;
                    default:
                        mState = "";
                        break;
                }
                btnSearch.callOnClick();
                selectedInit();
                tvDeliver.setSelected(true);
            }
        });
        listDialog.show();
    }
}
