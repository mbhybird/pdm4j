package com.besthings.pdm.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.besthings.bean.OrderNumChartRet;
import com.besthings.bean.PersonCustomRet;
import com.besthings.pdm.R;
import com.besthings.pdm.adapter.PersonCustomAdapter;
import com.besthings.pdm.utils.Constant;
import com.besthings.pdm.utils.ListUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class PersonCustomActivity extends BaseActivity {
    private static final String TAG = PersonCustomActivity.class.getSimpleName();
    Button btnDayOne;
    Button btnDayThr;
    Button btnDaySev;
    Button btnMonthOne;
    Button btnDayDef;
    Button btnSearch;
    Button btnKLine;
    Button btnList;
    Button btnOrderDay;
    Button btnOrderMonth;
    Button btnOrderYear;
    EditText etSearch;
    Spinner spFilter;

    TextView tvAll;
    TextView tvOrderState;
    TextView tvMatState;
    TextView tvPlateState;
    TextView tvCutState;
    TextView tvClothState;
    TextView tvAssemblyState;
    TextView tvStockState;
    TextView tvDeliver;
    RecyclerView mRecyclerView;
    LineChart mLineChart;
    RelativeLayout mChartLayout;
    RefreshLayout mRefreshLayout;
    RelativeLayout mRlfresh;

    Calendar mCalendar = Calendar.getInstance();
    Calendar mGCalendar = new GregorianCalendar();
    DateFormat mSDF = new SimpleDateFormat("yyyy-MM-dd");
    JSONObject jsonObject = new JSONObject();
    List<PersonCustomRet> mDataList = new ArrayList<>();
    PersonCustomAdapter mAdapter;
    List<OrderNumChartRet> mChartDataList = new ArrayList<>();
    ArrayList<Entry> mChartList = new ArrayList<>();
    List<String> mXValues = new ArrayList<>();

    String mStartDate = "";
    String mEndDate = "";
    String mJson;
    String mOrderState = "";
    String mMatState = "";
    String mPlateState = "";
    String mCutState = "";
    String mClothState = "";
    String mAssemblyState = "";
    String mStockState = "";
    String mState = "";
    String mBrand = "";
    String mExpressCompany = "";
    String mStyle = "";
    String mSType = "1";
    int mShowType = 0;
    int mPageStart;
    int mPageEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_custom);

        setCustomActionBar(true);

        btnDayOne = (Button) findViewById(R.id.person_custom_btnDayOne);
        btnDayThr = (Button) findViewById(R.id.person_custom_btnDayThr);
        btnDaySev = (Button) findViewById(R.id.person_custom_btnDaySev);
        btnMonthOne = (Button) findViewById(R.id.person_custom_btnMonthOne);
        btnDayDef = (Button) findViewById(R.id.person_custom_btnDayDef);
        etSearch = (EditText) findViewById(R.id.person_custom_etSearch);
        btnSearch = (Button) findViewById(R.id.person_custom_btnSearch);
        mRecyclerView = (RecyclerView) findViewById(R.id.person_custom_rv);
        btnKLine = (Button) findViewById(R.id.person_custom_btnKLine);
        spFilter = (Spinner) findViewById(R.id.person_custom_spFilter);
        tvAll = (TextView) findViewById(R.id.person_custom_tvAll);
        tvOrderState = (TextView) findViewById(R.id.person_custom_tvOrderState);
        tvMatState = (TextView) findViewById(R.id.person_custom_tvMatState);
        tvPlateState = (TextView) findViewById(R.id.person_custom_tvPlateState);
        tvCutState = (TextView) findViewById(R.id.person_custom_tvCutState);
        tvClothState = (TextView) findViewById(R.id.person_custom_tvClothState);
        tvAssemblyState = (TextView) findViewById(R.id.person_custom_tvAssemblyState);
        tvStockState = (TextView) findViewById(R.id.person_custom_tvStockState);
        tvDeliver = (TextView) findViewById(R.id.person_custom_tvDeliver);
        btnList = (Button) findViewById(R.id.person_custom_btnList);
        mChartLayout = (RelativeLayout) findViewById(R.id.person_custom_chart_layout);
        btnOrderDay = (Button) findViewById(R.id.person_custom_btnOrderDay);
        btnOrderMonth = (Button) findViewById(R.id.person_custom_btnOrderMonth);
        btnOrderYear = (Button) findViewById(R.id.person_custom_btnOrderYear);
        mLineChart = (LineChart) findViewById(R.id.person_custom_lcChart);
        mRefreshLayout = (RefreshLayout) findViewById(R.id.person_custom_refreshLayout);
        mRlfresh = (RelativeLayout) findViewById(R.id.person_custom_rlRefresh);
        mLineChart.setNoDataText("");
        tvAll.setSelected(true);

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
                        initListJson();
                        try {
                            mDataList.clear();
                            mDataList = myApp.NetHelperEx.getPersonCustomBean(mJson).getRet();
                            mAdapter = new PersonCustomAdapter(R.layout.item_person_custom, mDataList);
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
                                                initListJson();
                                                try {
                                                    mDataList = myApp.NetHelperEx.getPersonCustomBean(mJson).getRet();
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
                mCalendar.add(Calendar.DAY_OF_MONTH, 0);
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
                mCalendar.add(Calendar.DAY_OF_MONTH, -2);
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
                mCalendar.add(Calendar.DAY_OF_MONTH, -6);
                mStartDate = mSDF.format(mCalendar.getTime());
                btnSearch.callOnClick();
            }
        });

        btnMonthOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnDayClear();
                btnMonthOne.setSelected(true);
                mCalendar.setTime(new Date());
                mEndDate = mSDF.format(mCalendar.getTime());
                mCalendar.add(Calendar.MONTH, -1);
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
            switch (mShowType) {
                case 0:
                    mRefreshLayout.autoRefresh();
                    break;
                case 1:
                    initKLineJson();
                    try {
                        mChartDataList.clear();
                        mChartDataList = myApp.NetHelperEx.getOrderNumChartBean(mJson).getRet();
                        mChartList.clear();
                        mXValues.clear();
                        if (mChartDataList.size()>0) {
                            for (int i = 0; i < mChartDataList.size(); i++) {
                                float yValue = mChartDataList.get(i).getOrdernum();
                                mChartList.add(new Entry(i, yValue));
                                mXValues.add(mChartDataList.get(i).getDay());
                            }
                            createLineChart(mLineChart, mChartList, mXValues);
                        } else {
                            mLineChart.clear();
                            mLineChart.setNoDataText("暂无数据");
                            mLineChart.invalidate();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        mLineChart.clear();
                        mLineChart.setNoDataText("暂无数据");
                        mLineChart.invalidate();
                    }

                    break;
            }


            }
        });

        tvAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initState();
                tvAll.setSelected(true);
                btnSearch.callOnClick();
            }
        });

        tvOrderState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOrderStateAlertDialog();
            }
        });

        tvMatState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMatStateAlertDialog();
            }
        });

        tvPlateState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPlateStateAlertDialog();
            }
        });

        tvCutState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCutStateAlertDialog();
            }
        });

        tvClothState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showClothStateAlertDialog();
            }
        });

        tvAssemblyState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAssemblyStateAlertDialog();
            }
        });

        tvStockState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStockStateAlertDialog();
            }
        });

        tvDeliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStateAlertDialog();
            }
        });

        ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(PersonCustomActivity.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.PersonCustomType));
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFilter.setAdapter(spAdapter);
        spFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                switch (pos) {
                    case 0:
                        mBrand = "";
                        mStyle = "";
                        mExpressCompany = "";
                        break;
                    case 1:
                        mBrand = "";
                        mStyle = "";
                        mExpressCompany = "";
                        break;
                    case 2:
                        startActivityForFilterResult(Constant.REQUEST_CODE_BRAND);
                        break;
                    case 3:
                        startActivityForFilterResult(Constant.REQUEST_CODE_EXPRESS_COMPANY);
                        break;
                    case 4:
                        startActivityForFilterResult(Constant.REQUEST_CODE_STYLE);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
        spFilter.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
            try {
                Class<?> clazz = AdapterView.class;
                Field field = clazz.getDeclaredField("mOldSelectedRowId");
                field.setAccessible(true);
                field.setInt(spFilter,Integer.MIN_VALUE);
            } catch(Exception e){
                e.printStackTrace();
            }
            return false;
            }
        });

        btnKLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShowType = 1;
                btnList.setVisibility(View.VISIBLE);
                btnKLine.setVisibility(View.INVISIBLE);
                mRlfresh.setVisibility(View.INVISIBLE);
                mChartLayout.setVisibility(View.VISIBLE);
            }
        });

        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShowType = 0;
                btnKLine.setVisibility(View.VISIBLE);
                btnList.setVisibility(View.INVISIBLE);
                mRlfresh.setVisibility(View.VISIBLE);
                mChartLayout.setVisibility(View.INVISIBLE);
            }
        });

        btnOrderDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSType = "1";
                btnSearch.callOnClick();
            }
        });
        btnOrderMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSType = "2";
                btnSearch.callOnClick();
            }
        });
        btnOrderYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSType = "3";
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
        btnMonthOne.setSelected(false);
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

    private void initState() {
        mOrderState = "";
        mMatState = "";
        mPlateState = "";
        mCutState = "";
        mClothState = "";
        mAssemblyState = "";
        mStockState = "";
        mState = "";
        tvAll.setSelected(false);
        tvOrderState.setSelected(false);
        tvMatState.setSelected(false);
        tvPlateState.setSelected(false);
        tvCutState.setSelected(false);
        tvClothState.setSelected(false);
        tvAssemblyState.setSelected(false);
        tvStockState.setSelected(false);
        tvDeliver.setSelected(false);
    }

    private void initKLineJson() {
        mJson = "";
        jsonObject = new JSONObject();
        try {
            jsonObject.put("ID", 14016);
            jsonObject.put("SeriesID", myApp.mSeriesID);
            jsonObject.put("Time", "time2000");
            jsonObject.put("StartDate", mStartDate);
            jsonObject.put("EndDate", mEndDate);
            jsonObject.put("SrchFld", "");
            jsonObject.put("NumPerPg", 10000);
            jsonObject.put("iType", 1);
            jsonObject.put("sType", mSType);
            if (!mState.equals(""))
            {
                jsonObject.put("State", mState);
            } else {
                jsonObject.put("State", -1);
            }
            if (!mOrderState.equals("")) {
                jsonObject.put("OrderState", mOrderState);
            }
            if (!mMatState.equals("")) {
                jsonObject.put("MatState", mMatState);
            }
            if (!mPlateState.equals("")) {
                jsonObject.put("PlateState", mPlateState);
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
            if (!etSearch.getText().toString().equals("")) {
                jsonObject.put("sosearch", etSearch.getText().toString());
            }
            if (!mBrand.equals("")) {
                jsonObject.put("Brand", mBrand);
            }
            if (!mExpressCompany.equals("")) {
                jsonObject.put("Express", mExpressCompany);
            }
            if (!mStyle.equals("")) {
                jsonObject.put("Style", mStyle);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mJson = jsonObject.toString();
    }

    private void initListJson() {
        mJson = "";
        jsonObject = new JSONObject();
        try {
            jsonObject.put("ID", 14001);
            jsonObject.put("SeriesID", myApp.mSeriesID);
            jsonObject.put("Time", "time2000");
            jsonObject.put("StartDate", mStartDate);
            jsonObject.put("EndDate", mEndDate);
            jsonObject.put("SrchFld", "");
            jsonObject.put("NumPerPg", 1000);
            jsonObject.put("iType", 1);
            if (!mState.equals(""))
            {
                jsonObject.put("State", mState);
            } else {
                jsonObject.put("State", -1);
            }
            if (!mOrderState.equals("")) {
                jsonObject.put("OrderState", mOrderState);
            }
            if (!mMatState.equals("")) {
                jsonObject.put("MatState", mMatState);
            }
            if (!mPlateState.equals("")) {
                jsonObject.put("PlateState", mPlateState);
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
            if (!etSearch.getText().toString().equals("")) {
                jsonObject.put("sosearch", etSearch.getText().toString());
            }
            if (!mBrand.equals("")) {
                jsonObject.put("Brand", mBrand);
            }
            if (!mExpressCompany.equals("")) {
                jsonObject.put("Express", mExpressCompany);
            }
            if (!mStyle.equals("")) {
                jsonObject.put("Style", mStyle);
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

    private void showOrderStateAlertDialog() {
        final String[] items = { "未开始","进行中","已完成"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(PersonCustomActivity.this);
        listDialog.setTitle("请选择订单状态");
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
                initState();
                tvOrderState.setSelected(true);
            }
        });
        listDialog.show();
    }

    private void showMatStateAlertDialog() {
        final String[] items = { "无面料","未备料","备料中","已备料"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(PersonCustomActivity.this);
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
                initState();
                tvMatState.setSelected(true);
            }
        });
        listDialog.show();
    }

    private void showPlateStateAlertDialog() {
        final String[] items = { "未开始","进行中","已完成"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(PersonCustomActivity.this);
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
                initState();
                tvPlateState.setSelected(true);
            }
        });
        listDialog.show();
    }

    private void showClothStateAlertDialog() {
        final String[] items = { "未开始","进行中","已完成"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(PersonCustomActivity.this);
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
                initState();
                tvClothState.setSelected(true);
            }
        });
        listDialog.show();
    }

    private void showCutStateAlertDialog() {
        final String[] items = { "未开始","进行中","已完成"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(PersonCustomActivity.this);
        listDialog.setTitle("请选择单裁状态");
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
                initState();
                tvCutState.setSelected(true);
            }
        });
        listDialog.show();
    }

    private void showAssemblyStateAlertDialog() {
        final String[] items = { "未开始","进行中","已完成"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(PersonCustomActivity.this);
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
                initState();
                tvAssemblyState.setSelected(true);
            }
        });
        listDialog.show();
    }


    private void showStockStateAlertDialog() {
        final String[] items = { "未入库","进行中","已入库"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(PersonCustomActivity.this);
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
                initState();
                tvStockState.setSelected(true);
            }
        });
        listDialog.show();
    }

    private void showStateAlertDialog() {
        final String[] items = {"未发货","已发货"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(PersonCustomActivity.this);
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
                initState();
                tvDeliver.setSelected(true);
            }
        });
        listDialog.show();
    }

    @Override
    protected void doSearchAction(int actionCode, List<String> parameter) {
//        super.doSearchAction(actionCode, parameter);
        String result;
        mBrand = "";
        mExpressCompany = "";
        mStyle  = "";
        if (parameter != null && parameter.size() > 0) {
            result = ListUtil.listToString(parameter, ';');
            switch (actionCode) {
                case 8000:
                    mBrand = result;
                    break;
                case 8011:
                    mExpressCompany = result;
                    Log.i(TAG, mExpressCompany);
                    break;
                case 8006:
                    String style = ListUtil.symbolReplace(result, "&");
                    mStyle = style;
                    break;
                default:
                    break;
            }
        }
        btnSearch.callOnClick();
    }

    public class MyXFormatter implements IAxisValueFormatter {
        List<String> mValues;

        public MyXFormatter(List<String> values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            int i = (int) value % mValues.size();
            if (i < 0 || i == 0) {
                return mValues.get(0);
            } else if(i < mValues.size() && i>0) {
                return mValues.get(i);
            } else {
                return mValues.get(mValues.size() - 1);
            }
        }
    }

    public void createLineChart(LineChart lineChart, ArrayList<Entry> arrayList, List<String> xValue) {
        lineChart.getDescription().setEnabled(false);
        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setNoDataText("");
        lineChart.setScaleXEnabled(true);
        lineChart.setScaleYEnabled(false);
        lineChart.setDoubleTapToZoomEnabled(false);

        MyXFormatter formatter = new MyXFormatter(xValue);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(true);
        xAxis.setAvoidFirstLastClipping(true);
        if (xValue.size() > 3) {
            xAxis.setLabelCount(4);
        } else {
        xAxis.setLabelCount(2);
        }
        xAxis.setValueFormatter(formatter);
        xAxis.enableGridDashedLine(10f,10f,0f);
        xAxis.setSpaceMin(0.5f);
        xAxis.setSpaceMax(0.5f);
        xAxis.setAxisLineWidth(1f);

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisLineWidth(1f);

        LineDataSet setComp1 = new LineDataSet(arrayList, "订单数量");
        setComp1.setColor(Color.DKGRAY);
        setComp1.setDrawCircles(true);
        setComp1.setCircleColor(Color.RED);
        setComp1.setValueFormatter(new DefaultValueFormatter(0));
        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(setComp1);
        LineData data = new LineData(dataSets);
        data.setDrawValues(true);
        lineChart.setData(data);
        lineChart.invalidate();
        lineChart.notifyDataSetChanged();
        lineChart.invalidate();
    }

}
