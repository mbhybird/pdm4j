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
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.besthings.bean.DesignFileListRet;
import com.besthings.pdm.R;
import com.besthings.pdm.adapter.GraphicNotesAdapter;
import com.besthings.pdm.utils.ACache;
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
import java.util.Locale;

public class MyDesignActivity extends BaseActivity {

    private static final String TAG = MyDesignActivity.class.getSimpleName();

    static final String STATE_UNAPPROVE = "1";
    static final String STATE_RELEASE = "2";
    static final String STATE_APPROVAL = "3";
    static final String STATE_PASS = "4";
    static final String STATE_OBSOLETE = "5";

    String mStartDate = "";
    String mEndDate = "";
    DateFormat mSDF = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    Calendar mCalendar = Calendar.getInstance();
    Calendar mGCalendar = new GregorianCalendar();
    String mFilterJson = "";
    JSONObject mFilterJsonObject;
    String mBrand = "";
    String mBand = "";
    String mStyle = "";
    String mMaker = "";
    String mTheme = "";
    String mYear = "";
    String mSeason = "";
    String mState = "";
    Spinner spFilter;
    EditText etSearch;
    Button btnSearch;
    RefreshLayout mRefreshLayout;
    RecyclerView mRecyclerView;
    List<DesignFileListRet> mDataList = new ArrayList<>();
    List<DesignFileListRet> mBaseList = new ArrayList<>();
    GraphicNotesAdapter mAdapter;
    Button btnDayOne;
    Button btnDayThree;
    Button btnDayWeek;
    Button btnCustomize;
    int mPageStart;
    int mPageEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_design);

        setCustomActionBar(true);

        btnDayOne = (Button) findViewById(R.id.my_design_btnDayOne);
        btnDayThree = (Button) findViewById(R.id.my_design_btnDayThree);
        btnDayWeek = (Button) findViewById(R.id.my_design_btnDayWeek);
        btnCustomize = (Button) findViewById(R.id.my_design_btnDayCustomize);
        ImageButton ibCapture = (ImageButton) findViewById(R.id.my_design_ibCapture);
        btnSearch = (Button) findViewById(R.id.my_design_btnSearch);
        final Button btnUnApprove = (Button) findViewById(R.id.my_design_btnUnApprove);
        final Button btnApproval = (Button) findViewById(R.id.my_design_btnApproval);
        final Button btnObsolete = (Button) findViewById(R.id.my_design_btnObsolete);
        final Button btnPass = (Button) findViewById(R.id.my_design_btnPass);
        final Button btnRelease = (Button) findViewById(R.id.my_design_btnReleased);
        final Button btnAll = (Button) findViewById(R.id.my_design_btnAll);
        etSearch = (EditText) findViewById(R.id.my_design_etSearch);
        btnAll.setSelected(true);

        mRefreshLayout = (RefreshLayout) findViewById(R.id.my_design_refreshLayout);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_design_rv);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
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
                            mDataList = myApp.NetHelper.getMyDesignBean(mFilterJson).getRet();
                            mBaseList = mDataList;
                            mAdapter = new GraphicNotesAdapter(R.layout.item_graphic_notes, mDataList);
                            mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                    String mid = mBaseList.get(position).getMID();
                                    if (!TextUtils.isEmpty(mid)) {
                                        Intent it = new Intent(MyDesignActivity.this, MyDesignViewActivity.class);
                                        it.putExtra("mid", mid);
                                        startActivity(it);
                                    }
                                }
                            });
                            mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
                            mRecyclerView.setAdapter(mAdapter);
                            if (mBaseList.size() >= 10) {
                                mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                                    @Override
                                    public void onLoadMoreRequested() {
                                        mRecyclerView.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                mPageStart = mPageEnd + 1;
                                                mPageEnd += 10;
                                                buildQueryJson();
                                                try {
                                                    mDataList = myApp.NetHelper.getMyDesignBean(mFilterJson).getRet();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                                if (mDataList.size() > 0) {
                                                    mAdapter.addData(mDataList);
                                                    mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                                        @Override
                                                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                                            String mid = mBaseList.get(position).getMID();
                                                            if (!TextUtils.isEmpty(mid)) {
                                                                Intent it = new Intent(MyDesignActivity.this, MyDesignViewActivity.class);
                                                                it.putExtra("mid", mid);
                                                                startActivity(it);
                                                            }
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
        mRefreshLayout.setDisableContentWhenRefresh(true);
        mRefreshLayout.autoRefresh();


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRefreshLayout.autoRefresh();
            }
        });

        btnUnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mState = STATE_UNAPPROVE;
                btnUnApprove.setSelected(true);
                btnApproval.setSelected(false);
                btnPass.setSelected(false);
                btnRelease.setSelected(false);
                btnObsolete.setSelected(false);
                btnAll.setSelected(false);
                btnSearch.callOnClick();
            }
        });

        btnApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mState = STATE_APPROVAL;
                btnUnApprove.setSelected(false);
                btnApproval.setSelected(true);
                btnPass.setSelected(false);
                btnRelease.setSelected(false);
                btnObsolete.setSelected(false);
                btnAll.setSelected(false);
                btnSearch.callOnClick();
            }
        });

        btnRelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mState = STATE_RELEASE;
                btnUnApprove.setSelected(false);
                btnApproval.setSelected(false);
                btnPass.setSelected(false);
                btnRelease.setSelected(true);
                btnObsolete.setSelected(false);
                btnAll.setSelected(false);
                btnSearch.callOnClick();
            }
        });

        btnObsolete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mState = STATE_OBSOLETE;
                btnUnApprove.setSelected(false);
                btnApproval.setSelected(false);
                btnPass.setSelected(false);
                btnRelease.setSelected(false);
                btnObsolete.setSelected(true);
                btnAll.setSelected(false);
                btnSearch.callOnClick();
            }
        });

        btnPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mState = STATE_PASS;
                btnUnApprove.setSelected(false);
                btnApproval.setSelected(false);
                btnPass.setSelected(true);
                btnRelease.setSelected(false);
                btnObsolete.setSelected(false);
                btnAll.setSelected(false);
                btnSearch.callOnClick();
            }
        });

        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mState = "";
                btnUnApprove.setSelected(false);
                btnApproval.setSelected(false);
                btnPass.setSelected(false);
                btnRelease.setSelected(false);
                btnObsolete.setSelected(false);
                btnAll.setSelected(true);
                btnSearch.callOnClick();
            }
        });

        ibCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MyDesignActivity.this, MyDesignCaptureActivity.class);
                startActivity(it);
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

        spFilter = (Spinner) findViewById(R.id.my_design_spFilter);
        /*
        String[] mItems = getResources().getStringArray(R.array.filterType);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFilter.setAdapter(adapter);*/
        spFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                String[] filterType = getResources().getStringArray(R.array.filterType);
                Log.i(TAG, filterType[pos]);
                switch (pos) {
                    case 0:
                        mYear = "";
                        mSeason = "";
                        mTheme = "";
                        mBand = "";
                        mMaker = "";
                        mStyle  = "";
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
    }

    @Override
    protected void doSearchAction(int actionCode, List<String> parameter) {
//        super.doSearchAction(actionCode, parameter);
        String result;
        mYear = "";
        mSeason = "";
        mTheme = "";
        mBand = "";
        mMaker = "";
        mStyle  = "";
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
                default:
                    break;
            }
        }
        btnSearch.callOnClick();
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
        }
        return true;
    }

    private void buildQueryJson() {

        mFilterJson = "";
        mFilterJsonObject = new JSONObject();
        try {
            mFilterJsonObject.put("ID", 4001);
            mFilterJsonObject.put("SeriesID", myApp.mSeriesID);
            mFilterJsonObject.put("Operator", ACache.get(this).getAsString("Maker"));
            mFilterJsonObject.put("Time", "time2000");
            mFilterJsonObject.put("Type", "");
            mFilterJsonObject.put("NumPerPg", 1000);
            mFilterJsonObject.put("SrchFld", "");
            //品牌
            mFilterJsonObject.put("Brand", mBrand);
            //开始日期
            mFilterJsonObject.put("StartDate", mStartDate);
            //结束日期
            mFilterJsonObject.put("EndDate", mEndDate);
            //年度
            mFilterJsonObject.put("Year", mYear);
            //季节
            mFilterJsonObject.put("Season", mSeason);
            //主题
            mFilterJsonObject.put("Theme", mTheme);
            //分类/品类
            mFilterJsonObject.put("Style", mStyle);
            //波段
            mFilterJsonObject.put("Band", mBand);
            //设计师
            if (!mMaker.equals("")) {
                mFilterJsonObject.put("Maker", mMaker);
            }
            //状态
            if (!mState.equals("")) {
                mFilterJsonObject.put("State", Integer.valueOf(mState));
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
}
