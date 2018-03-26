package com.besthings.pdm.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import com.besthings.bean.AnalysisOfResearchChartRet;
import com.besthings.bean.AnalysisOfResearchStatisticalSummaryRet;
import com.besthings.pdm.R;
import com.besthings.pdm.adapter.AnalysisOfResearchBarChartAdapter;
import com.besthings.pdm.adapter.AnalysisOfResearchChartAdapter;
import com.besthings.pdm.adapter.AnalysisOfResearchSummaryAdapter;
import com.besthings.pdm.utils.Constant;
import com.besthings.pdm.utils.ListUtil;
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
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class AnalysisOfResearchActivity extends BaseActivity {
    private static final String TAG = AnalysisOfResearchActivity.class.getSimpleName();
    RecyclerView mRvSummary;
    Spinner spFilter;
    EditText etSearch;
    Button btnSearch;
    Button btnDayOne;
    Button btnDayThree;
    Button btnDayWeek;
    Button btnCustomize;

    TextView tvAll;
    TextView tvSummary;
    TextView tvChart;
    TextView tvQuantity;
    TextView tvDesignState;
    TextView tvPlateState;
    TextView tvMatState;
    TextView tvFClothState;
    TextView tvSClothState;
    TextView tvTechnicsState;
    TextView tvAllSum;
    TextView tvDesignStateSum;
    TextView tvPlateStateSum;
    TextView tvMatStateSum;
    TextView tvFClothStateSum;
    TextView tvSClothStateSum;
    TextView tvTechnicsStateSum;

    int mAllSum = 0;
    int[] mDesignStateSum;
    int[] mPlateStateSum;
    int[] mMatStateSum;
    int[] mFClothStateSum;
    int[] mSClothStateSum;
    int[] mTechnicsStateSum;

    String mDesignState = "";
    String mPlateState = "";
    String mMatState = "";
    String mFClothState = "";
    String mSClothState = "";
    String mTechnicsState = "";

    RecyclerView mRvChart;
    LinearLayout mLLFilter;
    LinearLayout mLLColorLabel;
    String mStartDate = "";
    ScrollView mQuantity;
    RecyclerView mRvQuantity;
    RefreshLayout mRefreshLayout;
    LinearLayout mLLRefresh;

    DateFormat mSDF = new SimpleDateFormat("yyyy-MM-dd");
    Calendar mCalendar = Calendar.getInstance();
    Calendar mGCalendar = new GregorianCalendar();
    String mEndDate = mSDF.format(new Date());
    List<AnalysisOfResearchStatisticalSummaryRet> mSummaryDataList = new ArrayList<>();
    List<AnalysisOfResearchChartRet> mChartDataList = new ArrayList<>();
    List<AnalysisOfResearchChartRet> mBarChartDataList = new ArrayList<>();
    AnalysisOfResearchSummaryAdapter mSummaryAdapter;
    AnalysisOfResearchChartAdapter mChartAdapter;
    AnalysisOfResearchBarChartAdapter mBarChartAdapter;
    String mYear = "";
    String mSeason = "";
    String mStyle = "";
    String mTheme = "";
    String mBand = "";
    String mMaker = "";
    String mTechnician = "";
    String mTempleteMan = "";
    String mFilterJson;
    JSONObject mFilterJsonObject;
    int mTabTag = 1;
    int mPageStart;
    int mPageEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis_of_research);

        setCustomActionBar(true);

        btnDayOne = (Button) findViewById(R.id.analysis_of_research_btnDayOne);
        btnDayThree = (Button) findViewById(R.id.analysis_of_research_btnDayThr);
        btnDayWeek = (Button) findViewById(R.id.analysis_of_research_btnDaySev);
        btnCustomize = (Button) findViewById(R.id.analysis_of_research_btnDayDef);
        btnSearch = (Button) findViewById(R.id.analysis_of_research_btnSearch);
        etSearch = (EditText) findViewById(R.id.analysis_of_research_etSearch);
        spFilter = (Spinner) findViewById(R.id.analysis_of_research_sFilter);
        tvDesignState = (TextView) findViewById(R.id.analysis_of_research_tvDesignState);
        tvPlateState = (TextView) findViewById(R.id.analysis_of_research_tvPlateState);
        tvMatState = (TextView) findViewById(R.id.analysis_of_research_tvMatState);
        tvFClothState = (TextView) findViewById(R.id.analysis_of_research_tvFClothState);
        tvSClothState = (TextView) findViewById(R.id.analysis_of_research_tvSClothState);
        tvTechnicsState = (TextView) findViewById(R.id.analysis_of_research_tvTechnicsState);
        tvChart = (TextView) findViewById(R.id.analysis_of_research_tvChart);
        tvSummary = (TextView) findViewById(R.id.analysis_of_research_tvSummary);
        tvQuantity = (TextView) findViewById(R.id.analysis_of_research_tvQuantity);
        tvAll = (TextView) findViewById(R.id.analysis_of_research_tvAll);
        mRvSummary = (RecyclerView) findViewById(R.id.analysis_of_research_rvRecord);
        mRvChart = (RecyclerView) findViewById(R.id.analysis_of_research_rvChart);
        mLLFilter = (LinearLayout) findViewById(R.id.analysis_of_research_llFilter);
        mRvQuantity = (RecyclerView) findViewById(R.id.analysis_of_research_rvGrid);
        mQuantity = (ScrollView) findViewById(R.id.analysis_of_research_svQuantity);
        mLLColorLabel = (LinearLayout) findViewById(R.id.analysis_of_research_llBarChart);
        mRefreshLayout = (RefreshLayout) findViewById(R.id.analysis_of_research_refreshLayout);
        mLLRefresh = (LinearLayout) findViewById(R.id.analysis_of_research_llRefresh);
        tvDesignStateSum = (TextView) findViewById(R.id.analysis_of_research_tvDesignStateSum);
        tvPlateStateSum = (TextView) findViewById(R.id.analysis_of_research_tvPlateStateSum);
        tvMatStateSum = (TextView) findViewById(R.id.analysis_of_research_tvMatStateSum);
        tvFClothStateSum = (TextView) findViewById(R.id.analysis_of_research_tvFClothStateSum);
        tvSClothStateSum = (TextView) findViewById(R.id.analysis_of_research_tvSClothStateSum);
        tvTechnicsStateSum = (TextView) findViewById(R.id.analysis_of_research_tvTechnicsStateSum);
        tvAllSum = (TextView) findViewById(R.id.analysis_of_research_tvAllSum);
        tvAll.setSelected(true);

        tvSummary.setSelected(true);
        ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(AnalysisOfResearchActivity.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.statisticalSummaryType));
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFilter.setAdapter(spAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRvSummary.setLayoutManager(linearLayoutManager);
        mRvSummary.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        mRvQuantity.setLayoutManager(linearLayoutManager1);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        mRvChart.setLayoutManager(linearLayoutManager2);
        mRvChart.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));



        mRefreshLayout.setEnableRefresh(true);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                ((View) refreshlayout).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPageStart = 1;
                        mPageEnd = 10;
                        buildSummaryQueryJson();
                        try {
                            mSummaryDataList.clear();
                            Log.i(TAG, mFilterJson);
                            mSummaryDataList = myApp.NetHelperEx.getAnalysisOfResearchStatisticalSummaryBean(mFilterJson).getRet();
                            mSummaryAdapter = new AnalysisOfResearchSummaryAdapter(R.layout.item_analysis_of_research_statistical_summary, mSummaryDataList);
                            mSummaryAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
                            mRvSummary.setAdapter(mSummaryAdapter);
                            if (mSummaryDataList.size() >= 10) {
                                mSummaryAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                                    @Override
                                    public void onLoadMoreRequested() {
                                        mRvSummary.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                mPageStart = mPageEnd + 1;
                                                mPageEnd += 10;
                                                buildSummaryQueryJson();
                                                try {
                                                    mSummaryDataList = myApp.NetHelperEx.getAnalysisOfResearchStatisticalSummaryBean(mFilterJson).getRet();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                                if (mSummaryDataList.size() > 0) {
                                                    mSummaryAdapter.addData(mSummaryDataList);
                                                    mSummaryAdapter.notifyDataSetChanged();
                                                    mSummaryAdapter.loadMoreComplete();

                                                } else {
                                                    mSummaryAdapter.loadMoreEnd();
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

        tvSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initTab();
                mTabTag = 1;
                mLLFilter.setVisibility(View.VISIBLE);
                mLLRefresh.setVisibility(View.VISIBLE);
                mRvChart.setVisibility(View.INVISIBLE);
                mQuantity.setVisibility(View.INVISIBLE);
                mRvQuantity.setVisibility(View.INVISIBLE);
                mLLColorLabel.setVisibility(View.INVISIBLE);
                tvSummary.setSelected(true);
                ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(AnalysisOfResearchActivity.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.statisticalSummaryType));
                spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spFilter.setAdapter(spAdapter);
                spFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        switch (pos) {
                            case 0:
                                mYear = "";
                                mSeason = "";
                                mTheme = "";
                                mBand = "";
                                mMaker = "";
                                mStyle = "";
                                mTechnician = "";
                                mTempleteMan = "";
                                break;
                            case 1:
                                startActivityForFilterResult(Constant.REQUEST_CODE_YEAR);
                                break;
                            case 2:
                                startActivityForFilterResult(Constant.REQUEST_CODE_SEASON);
                                break;
                            case 3:
                                startActivityForFilterResult(Constant.REQUEST_CODE_THEME);
                                break;
                            case 4:
                                startActivityForFilterResult(Constant.REQUEST_CODE_BAND);
                                break;
                            case 5:
                                startActivityForFilterResult(Constant.REQUEST_CODE_MAKER);
                                break;
                            case 6:
                                startActivityForFilterResult(Constant.REQUEST_CODE_TEMPLETEMAN);
                                break;
                            case 7:
                                startActivityForFilterResult(Constant.REQUEST_CODE_TECHNICIAN);
                                break;
                            case 8:
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

            }
        });
        tvChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initTab();
                mTabTag = 2;
                mLLFilter.setVisibility(View.INVISIBLE);
                mLLRefresh.setVisibility(View.INVISIBLE);
                mRvChart.setVisibility(View.VISIBLE);
                mQuantity.setVisibility(View.INVISIBLE);
                mRvQuantity.setVisibility(View.INVISIBLE);
                mLLColorLabel.setVisibility(View.VISIBLE);
                tvChart.setSelected(true);
                Handler handler=new Handler();
                Runnable runnable=new Runnable(){
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        btnSearch.callOnClick();
                    }
                };
                handler.postDelayed(runnable, 200);
                ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(AnalysisOfResearchActivity.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.ChartType));
                spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spFilter.setAdapter(spAdapter);
                spFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        switch (pos) {
                            case 0:
                                mYear = "";
                                mSeason = "";
                                mTheme = "";
                                mBand = "";
                                mStyle = "";
                                break;
                            case 1:
                                startActivityForFilterResult(Constant.REQUEST_CODE_YEAR);
                                break;
                            case 2:
                                startActivityForFilterResult(Constant.REQUEST_CODE_SEASON);
                                break;
                            case 3:
                                startActivityForFilterResult(Constant.REQUEST_CODE_THEME);
                                break;
                            case 4:
                                startActivityForFilterResult(Constant.REQUEST_CODE_BAND);
                                break;
                            case 5:
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

            }
        });
        tvQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initTab();
                mTabTag = 3;
                mLLFilter.setVisibility(View.INVISIBLE);
                mLLRefresh.setVisibility(View.INVISIBLE);
                mRvChart.setVisibility(View.INVISIBLE);
                mQuantity.setVisibility(View.VISIBLE);
                mRvQuantity.setVisibility(View.VISIBLE);
                mLLColorLabel.setVisibility(View.INVISIBLE);
                tvQuantity.setSelected(true);
                Handler handler=new Handler();
                Runnable runnable=new Runnable(){
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        btnSearch.callOnClick();
                    }
                };
                handler.postDelayed(runnable, 200);
                ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(AnalysisOfResearchActivity.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.ChartType));
                spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spFilter.setAdapter(spAdapter);
                spFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        switch (pos) {
                            case 0:
                                mYear = "";
                                mSeason = "";
                                mTheme = "";
                                mBand = "";
                                mStyle = "";
                                break;
                            case 1:
                                startActivityForFilterResult(Constant.REQUEST_CODE_YEAR);
                                break;
                            case 2:
                                startActivityForFilterResult(Constant.REQUEST_CODE_SEASON);
                                break;
                            case 3:
                                startActivityForFilterResult(Constant.REQUEST_CODE_THEME);
                                break;
                            case 4:
                                startActivityForFilterResult(Constant.REQUEST_CODE_BAND);
                                break;
                            case 5:
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
                mRvChart.setVisibility(View.GONE);
            }
        });
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
                switch (mTabTag) {
                    case 1:
                        mRefreshLayout.autoRefresh();
                        countStateSum();
                        break;
                    case 2:
                        buildQuantityQueryJson();
                        try {
                            mChartDataList.clear();
                            Log.i(TAG, myApp.NetHelperEx.getAnalysisOfResearchChartJson(mFilterJson));
                            mChartDataList = myApp.NetHelperEx.getAnalysisOfResearchChartBean(mFilterJson).getRet();
                            mBarChartAdapter = new AnalysisOfResearchBarChartAdapter(R.layout.item_analysis_of_research_chart, mChartDataList);
                            mRvChart.setAdapter(mBarChartAdapter);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 3:
                        buildQuantityQueryJson();
                        try {
                            mBarChartDataList.clear();
                            Log.i(TAG, myApp.NetHelperEx.getAnalysisOfResearchChartJson(mFilterJson));
                            mBarChartDataList = myApp.NetHelperEx.getAnalysisOfResearchChartBean(mFilterJson).getRet();
                            mChartAdapter = new AnalysisOfResearchChartAdapter(R.layout.item_analysis_of_research_quantity_analysis, mBarChartDataList);
                            mRvQuantity.setAdapter(mChartAdapter);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }

                Log.i(TAG, mFilterJson);
            }
        });


        spFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                switch (pos) {
                    case 0:
                        mYear = "";
                        mSeason = "";
                        mTheme = "";
                        mBand = "";
                        mMaker = "";
                        mStyle = "";
                        mTechnician = "";
                        mTempleteMan = "";
                        break;
                    case 1:
                        startActivityForFilterResult(Constant.REQUEST_CODE_YEAR);
                        break;
                    case 2:
                        startActivityForFilterResult(Constant.REQUEST_CODE_SEASON);
                        break;
                    case 3:
                        startActivityForFilterResult(Constant.REQUEST_CODE_THEME);
                        break;
                    case 4:
                        startActivityForFilterResult(Constant.REQUEST_CODE_BAND);
                        break;
                    case 5:
                        startActivityForFilterResult(Constant.REQUEST_CODE_MAKER);
                        break;
                    case 6:
                        startActivityForFilterResult(Constant.REQUEST_CODE_TEMPLETEMAN);
                        break;
                    case 7:
                        startActivityForFilterResult(Constant.REQUEST_CODE_TECHNICIAN);
                        break;
                    case 8:
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
                    field.setInt(spFilter, Integer.MIN_VALUE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

        tvAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initStateSelected();
                initState();
                tvAll.setSelected(true);
                btnSearch.callOnClick();
            }
        });

        tvDesignState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDesignStateAlertDialog();
                initState();
            }
        });

        tvPlateState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPlateStateAlertDialog();
                initState();
            }
        });

        tvMatState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMatStateAlertDialog();
                initState();
            }
        });

        tvFClothState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFClothStateAlertDialog();
                initState();
            }
        });

        tvSClothState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSClothStateAlertDialog();
                initState();
            }
        });

        tvTechnicsState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTechnicsStateAlertDialog();
                initState();
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

    private void initStateSelected() {
        tvAll.setSelected(false);
        tvDesignState.setSelected(false);
        tvPlateState.setSelected(false);
        tvMatState.setSelected(false);
        tvFClothState.setSelected(false);
        tvSClothState.setSelected(false);
        tvTechnicsState.setSelected(false);
    }

    private void initState() {
        mDesignState = "";
        mPlateState = "";
        mMatState = "";
        mFClothState = "";
        mSClothState = "";
        mTechnicsState = "";
    }
    private void initTab() {
        tvChart.setSelected(false);
        tvSummary.setSelected(false);
        tvQuantity.setSelected(false);
    }

    @Override
    protected void doSearchAction(int actionCode, List<String> parameter) {
        String result;
        mYear = "";
        mSeason = "";
        mTheme = "";
        mBand = "";
        mMaker = "";
        mStyle = "";
        mTechnician = "";
        mTempleteMan = "";
        if (parameter != null && parameter.size() > 0) {
            result = ListUtil.listToString(parameter, ';');
            switch (actionCode) {

                case 8001:
                    mYear = result;
                    break;

                case 8002:
                    mSeason = result;
                    break;

                case 8003:
                    mTheme = result;
                    break;

                case 8004:
                    mBand = result;
                    break;

                case 8005:
                    mMaker = result;
                    break;

                case 8006:
                    String style = ListUtil.symbolReplace(result, "&");
                    mStyle = style;
                    break;

                case 8009:
                    mTempleteMan = result;
                    break;

                case 8010:
                    mTechnician = result;

                default:
                    break;
            }
        }
        btnSearch.callOnClick();

    }

    private void buildQuantityQueryJson() {
        mFilterJson = "";
        mFilterJsonObject = new JSONObject();
        try {
            mFilterJsonObject.put("ID", 7001);
            mFilterJsonObject.put("SeriesID", myApp.mSeriesID);
            mFilterJsonObject.put("StartDate", mStartDate);
            mFilterJsonObject.put("EndDate", mEndDate);
            mFilterJsonObject.put("DataType", 2);
            if (!mYear.equals("")) {
                mFilterJsonObject.put("Year", mYear);
            }
            if (!mSeason.equals("")) {
                mFilterJsonObject.put("Season", mSeason);
            }
            if (!mTheme.equals("")) {
                mFilterJsonObject.put("Theme", mTheme);
            }
            if (!mStyle.equals("")) {
                mFilterJsonObject.put("Style", mStyle);
            }
            if (!mBand.equals("")) {
                mFilterJsonObject.put("Band", mBand);
            }
            mFilterJsonObject.put("State", -1);
            mFilterJson = mFilterJsonObject.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void buildQuerySumJson() {
        mFilterJson = "";
        mFilterJsonObject = new JSONObject();
        try {
            mFilterJsonObject.put("ID", 7002);
            mFilterJsonObject.put("SeriesID", myApp.mSeriesID);
            mFilterJsonObject.put("StartDate", mStartDate);
            mFilterJsonObject.put("EndDate", mEndDate);
            mFilterJsonObject.put("DataType", 2);
            if (!mYear.equals("")) {
                mFilterJsonObject.put("Year", mYear);
            }
            if (!mSeason.equals("")) {
                mFilterJsonObject.put("Season", mSeason);
            }
            if (!mTheme.equals("")) {
                mFilterJsonObject.put("Theme", mTheme);
            }
            if (!mStyle.equals("")) {
                mFilterJsonObject.put("Style", mStyle);
            }
            if (!mBand.equals("")) {
                mFilterJsonObject.put("Band", mBand);
            }
            mFilterJsonObject.put("State", -1);
            mFilterJson = mFilterJsonObject.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void buildSummaryQueryJson() {
        mFilterJson = "";
        mFilterJsonObject = new JSONObject();
        try {
            mFilterJsonObject.put("ID", 8001);
            mFilterJsonObject.put("SeriesID", myApp.mSeriesID);
            mFilterJsonObject.put("Type", 1);
            mFilterJsonObject.put("datetype", 4);
            mFilterJsonObject.put("StartDate", mStartDate);
            mFilterJsonObject.put("EndDate", mEndDate);
            if (!mYear.equals("")) {
                mFilterJsonObject.put("Year", mYear);
            }
            if (!mSeason.equals("")) {
                mFilterJsonObject.put("Season", mSeason);
            }
            if (!mTheme.equals("")) {
                mFilterJsonObject.put("Theme", mTheme);
            }
            if (!mStyle.equals("")) {
                mFilterJsonObject.put("Style", mStyle);
            }
            if (!mBand.equals("")) {
                mFilterJsonObject.put("Band", mBand);
            }
            //设计师
            if (!mMaker.equals("")) {
                mFilterJsonObject.put("Designer", mMaker);
            }
            //纸样师
            if (!mTempleteMan.equals("")) {
                mFilterJsonObject.put("TempleteMan", mTempleteMan);
            }
            //车版师
            if (!mTechnician.equals("")) {
                mFilterJsonObject.put("Technician", mTechnician);
            }
            if (!mDesignState.equals("")) {
                mFilterJsonObject.put("DesignState", mDesignState);
            }
            if (!mPlateState.equals("")) {
                mFilterJsonObject.put("PlateState", mPlateState);
            }
            if (!mMatState.equals("")) {
                mFilterJsonObject.put("MatState", mMatState);
            }
            if (!mFClothState.equals("")) {
                mFilterJsonObject.put("ConfirmClothSample_F", mFClothState);
            }
            if (!mSClothState.equals("")) {
                mFilterJsonObject.put("ConfirmClothSample_S", mSClothState);
            }
            if (!mTechnicsState.equals("")) {
                mFilterJsonObject.put("TechnicsState", mTechnicsState);
            }
            mFilterJsonObject.put("fPage", 1);
            mFilterJsonObject.put("PageCnt", "1000");
            mFilterJsonObject.put("PageStart", mPageStart);
            mFilterJsonObject.put("PageEnd", mPageEnd);
            mFilterJsonObject.put("sosearch", etSearch.getText().toString());
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

    private void showDesignStateAlertDialog() {
        final String[] items = { "未开始","进行中","已完成","已审核"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(AnalysisOfResearchActivity.this);
        listDialog.setTitle("请选择设计状态");
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        mDesignState = "-1";
                        break;
                    case 1:
                        mDesignState = "1";
                        break;
                    case 2:
                        mDesignState = "2";
                        break;
                    case 3:
                        mDesignState = "3";
                        break;
                    default:
                        mDesignState = "";
                        break;
                }
                btnSearch.callOnClick();
                initStateSelected();
                tvDesignState.setSelected(true);
            }
        });
        listDialog.show();
    }

    private void showPlateStateAlertDialog() {
        final String[] items = { "未开始","进行中","已完成","异样","已审核"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(AnalysisOfResearchActivity.this);
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
                    case 3:
                        mPlateState = "3";
                        break;
                    case 4:
                        mPlateState = "4";
                        break;
                    default:
                        mPlateState = "";
                        break;
                }
                btnSearch.callOnClick();
                initStateSelected();
                tvPlateState.setSelected(true);
            }
        });
        listDialog.show();
    }

    private void showMatStateAlertDialog() {
        final String[] items = { "无面料","未备料","备料中","已备料"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(AnalysisOfResearchActivity.this);
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
                initStateSelected();
                tvMatState.setSelected(true);
            }
        });
        listDialog.show();
    }

    private void showFClothStateAlertDialog() {
        final String[] items = { "未确认","已确认"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(AnalysisOfResearchActivity.this);
        listDialog.setTitle("请选择头版样衣状态");
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        mFClothState = "0";
                        break;
                    case 1:
                        mFClothState = "1";
                        break;
                    default:
                        mFClothState = "";
                        break;
                }
                btnSearch.callOnClick();
                initStateSelected();
                tvFClothState.setSelected(true);
            }
        });
        listDialog.show();
    }

    private void showSClothStateAlertDialog() {
        final String[] items = { "未确认","已确认"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(AnalysisOfResearchActivity.this);
        listDialog.setTitle("请选择齐色样衣状态");
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        mSClothState = "0";
                        break;
                    case 1:
                        mSClothState = "1";
                        break;
                    default:
                        mSClothState = "";
                        break;
                }
                btnSearch.callOnClick();
                initStateSelected();
                tvSClothState.setSelected(true);
            }
        });
        listDialog.show();
    }

    private void showTechnicsStateAlertDialog() {
        final String[] items = { "进行中","已完成","异样"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(AnalysisOfResearchActivity.this);
        listDialog.setTitle("请选择工艺状态");
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        mTechnicsState = "0";
                        break;
                    case 1:
                        mTechnicsState = "1";
                        break;
                    case 2:
                        mTechnicsState = "2";
                        break;
                    default:
                        mTechnicsState = "";
                        break;
                }
                btnSearch.callOnClick();
                initStateSelected();
                tvTechnicsState.setSelected(true);
            }
        });
        listDialog.show();
    }

    private void countStateSum() {
        buildQuerySumJson();
        try {
            mChartDataList.clear();
            mChartDataList = myApp.NetHelperEx.getAnalysisOfResearchChartBean(mFilterJson).getRet();
            if (mChartDataList.size() > 0) {
                mAllSum =
                        mChartDataList.get(0).getS().get(0) +
                                mChartDataList.get(0).getS().get(1) +
                                mChartDataList.get(0).getS().get(2) +
                                mChartDataList.get(0).getS().get(3) +
                                mChartDataList.get(0).getS().get(4);
                mDesignStateSum = new int[]{
                        mChartDataList.get(0).getS().get(0),
                        mChartDataList.get(0).getS().get(1),
                        mChartDataList.get(0).getS().get(2),
                        mChartDataList.get(0).getS().get(3),
                        mChartDataList.get(0).getS().get(4)};
                mPlateStateSum = new int[]{
                        mChartDataList.get(4).getS().get(0),
                        mChartDataList.get(4).getS().get(1),
                        mChartDataList.get(4).getS().get(2),
                        mChartDataList.get(4).getS().get(3),
                        mChartDataList.get(4).getS().get(4)};
                mMatStateSum = new int[]{
                        mChartDataList.get(3).getS().get(0),
                        mChartDataList.get(3).getS().get(1),
                        mChartDataList.get(3).getS().get(2),
                        mChartDataList.get(3).getS().get(3),
                        mChartDataList.get(3).getS().get(4)};
                mFClothStateSum = new int[]{
                        mChartDataList.get(15).getS().get(0)};
                mSClothStateSum = new int[]{
                        mChartDataList.get(15).getS().get(1)};
                mTechnicsStateSum = new int[]{
                        mChartDataList.get(1).getS().get(0),
                        mChartDataList.get(1).getS().get(1),
                        mChartDataList.get(1).getS().get(2),
                        mChartDataList.get(1).getS().get(3),
                        mChartDataList.get(1).getS().get(4)};
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        tvAllSum.setText(String.valueOf(mAllSum));

        if (mDesignStateSum != null) {
            if (!mDesignState.equals("")) {
                if (mDesignState.equals("-1")) {
                    tvDesignStateSum.setText(String.valueOf(mDesignStateSum[0]));
                } else {
                    tvDesignStateSum.setText(String.valueOf(mDesignStateSum[Integer.valueOf(mDesignState)]));
                }
            } else {
                int designStateSum = 0;
                for (int i = 0; i < 5; i++) {
                    designStateSum += mDesignStateSum[i];
                }
                tvDesignStateSum.setText(String.valueOf(designStateSum));
            }
        }

        if (mPlateStateSum != null) {
            if (!mPlateState.equals("")) {
                if (mPlateState.equals("-1")) {
                    tvPlateStateSum.setText(String.valueOf(mPlateStateSum[0]));
                } else {
                    tvPlateStateSum.setText(String.valueOf(mPlateStateSum[Integer.valueOf(mPlateState)]));
                }
            } else {
                int plateStateSum = 0;
                for (int i = 0; i < 5; i++) {
                    plateStateSum += mPlateStateSum[i];
                }
                tvPlateStateSum.setText(String.valueOf(plateStateSum));
            }
        }

        if (mMatStateSum != null) {
            if (!mMatState.equals("")) {
                if (mMatState.equals("-2")) {
                    tvMatStateSum.setText(String.valueOf(mMatStateSum[0]));
                } else if (mMatState.equals("-1")) {
                    tvMatStateSum.setText(String.valueOf(mMatStateSum[0]));
                } else {
                    tvMatStateSum.setText(String.valueOf(mMatStateSum[Integer.valueOf(mMatState)]));
                }
            } else {
                int matStateSum = 0;
                for (int i = 0; i < 5; i++) {
                    matStateSum += mMatStateSum[i];
                }
                tvMatStateSum.setText(String.valueOf(matStateSum));
            }
        }

        if (mFClothStateSum != null) {
            tvFClothStateSum.setText(String.valueOf(mFClothStateSum[0]));
        }
        if (mSClothStateSum != null) {
            tvSClothStateSum.setText(String.valueOf(mSClothStateSum[0]));
        }

        if (mTechnicsStateSum != null) {
            if (!mTechnicsState.equals("")) {
                tvTechnicsStateSum.setText(String.valueOf(mTechnicsStateSum[Integer.valueOf(mTechnicsState)]));
            } else {
                int technicsStateSum = 0;
                for (int i = 0; i < 5; i++) {
                    technicsStateSum += mTechnicsStateSum[i];
                }
                tvTechnicsStateSum.setText(String.valueOf(technicsStateSum));
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        Handler handler=new Handler();
        Runnable runnable=new Runnable(){
            @Override
            public void run() {
                // TODO Auto-generated method stub
                countStateSum();
            }
        };
        handler.postDelayed(runnable, 200);
    }
}