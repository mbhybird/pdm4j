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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class GraphicNotesActivity extends BaseActivity {
    private static final String TAG = GraphicNotesActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private RefreshLayout mRefreshLayout;
    private GraphicNotesAdapter mAdapter;
    List<DesignFileListRet> mDataList = new ArrayList<>();
    List<DesignFileListRet> mBaseList = new ArrayList<>();
    private JSONObject jsonObject;
    Date mNowTime = new Date();
    //得到日历
    Calendar mCalendar = Calendar.getInstance();
    Calendar mGCalendar = new GregorianCalendar();
    SimpleDateFormat mSDF = new SimpleDateFormat("yyyy-MM-dd");
    String nowDate = mSDF.format(mNowTime);
    String mJson = null;
    String mStartDate = null;
    String mEndDate = nowDate;

    private Button mGraphic_btnDayOne;
    private Button mGraphic_btnDayThr;
    private Button mGraphic_btnDaySev;
    private Button mGraphic_btnDayDef;
    private EditText graphic_etSearch;
    Button btnSearch;
    Spinner spFilter;

    String mYear = "";
    String mSeason = "";
    String mStyle = "";
    String mTheme = "";
    String mBand = "";
    String mMaker = "";
    int mPageStart;
    int mPageEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphic_notes);
        setCustomActionBar(true);
        mRecyclerView = (RecyclerView) findViewById(R.id.graphic_lv);
        graphic_etSearch = (EditText) findViewById(R.id.graphic_etSearch);
        mGraphic_btnDayOne = (Button) findViewById(R.id.graphic_btnDayOne);
        mGraphic_btnDayThr = (Button) findViewById(R.id.graphic_btnDayThr);
        mGraphic_btnDaySev = (Button) findViewById(R.id.graphic_btnDaySev);
        mGraphic_btnDayDef = (Button) findViewById(R.id.graphic_btnDayDef);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


        // SmartRefreshLayout
        mRefreshLayout = (RefreshLayout) findViewById(R.id.graphic_refreshLayout);
        mRefreshLayout.setEnableRefresh(true);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                ((View) refreshlayout).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadData();
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
        // SmartRefreshLayout End


        // 搜索按钮
        btnSearch = (Button) findViewById(R.id.graphic_btnSearch);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRefreshLayout.autoRefresh();
            }
        });

        // Time Filter
        mGraphic_btnDayOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnDayClear();
                view.setSelected(true);
                mCalendar.setTime(mNowTime);
                mCalendar.add(Calendar.DAY_OF_MONTH, 0);
                Date targetDate = mCalendar.getTime();
                mStartDate = mSDF.format(targetDate);
                btnSearch.callOnClick();

            }
        });
        mGraphic_btnDayThr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnDayClear();
                view.setSelected(true);
                mCalendar.setTime(mNowTime);
                mCalendar.add(Calendar.DAY_OF_MONTH, -2);
                Date targetDate = mCalendar.getTime();
                mStartDate = mSDF.format(targetDate);
                btnSearch.callOnClick();

            }
        });
        mGraphic_btnDaySev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnDayClear();
                view.setSelected(true);
                mCalendar.setTime(mNowTime);
                mCalendar.add(Calendar.DAY_OF_MONTH, -6);
                Date targetDate = mCalendar.getTime();
                mStartDate = mSDF.format(targetDate);
                btnSearch.callOnClick();
            }
        });
        mGraphic_btnDayDef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();

            }
        });

        // Spinner OnItemSelectedListener Setup
        String[] filterType = getResources().getStringArray(R.array.filterType);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, filterType);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFilter = (Spinner) findViewById(R.id.graphic_sFilter);
        spFilter.setAdapter(adapter);
        spFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
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
                } catch(Exception e){
                    e.printStackTrace();
                }
                return false;
            }
        });

    }

    @Override
    protected void doSearchAction(int actionCode, List<String> parameter) {
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


    public void btnDayClear() {
        mGraphic_btnDayOne.setSelected(false);
        mGraphic_btnDayThr.setSelected(false);
        mGraphic_btnDaySev.setSelected(false);
        mGraphic_btnDayDef.setSelected(false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
        }
        return true;
    }


    public void loadData() {
        mPageStart = 1;
        mPageEnd = 10;
        initJson();
        if (mJson != null) {
            try {
                mDataList.clear();
                mDataList = myApp.NetHelperEx.getDesignFileListBean(mJson).getRet();
                mBaseList = mDataList;
                mAdapter = new GraphicNotesAdapter(R.layout.item_graphic_notes, mDataList);
                mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        if(!mBaseList.get(position).getIMinPicture().equals("")) {
                            Intent intent = new Intent(GraphicNotesActivity.this, GraphicNotesPicViewerActivity.class);
                            intent.putExtra("imgMID", mBaseList.get(position).getMID());
                            startActivityForResult(intent, 1000);
                        } else {
                            Toast.makeText(GraphicNotesActivity.this, "缺少款式图", Toast.LENGTH_SHORT).show();
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
                                    initJson();
                                    try {
                                        mDataList = myApp.NetHelperEx.getDesignFileListBean(mJson).getRet();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    if (mDataList.size() > 0) {
                                        mAdapter.addData(mDataList);
                                        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                                if (!mBaseList.get(position).getIMinPicture().equals("")) {
                                                    Intent intent = new Intent(GraphicNotesActivity.this, GraphicNotesPicViewerActivity.class);
                                                    intent.putExtra("imgMID", mBaseList.get(position).getMID());
                                                    startActivityForResult(intent, 1000);
                                                } else {
                                                    Toast.makeText(GraphicNotesActivity.this, "缺少款式图", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                        mAdapter.notifyDataSetChanged();
                                        if (mDataList.size() < 10) {
                                            mAdapter.loadMoreEnd();
                                        } else {
                                            mAdapter.loadMoreComplete();
                                        }

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
        }
    }

    private void initJson() {
        mJson = "";
        jsonObject = new JSONObject();
        try {
            jsonObject.put("Operator", ACache.get(GraphicNotesActivity.this).getAsString("Maker"));
            jsonObject.put("Time", "time2000");
            jsonObject.put("Type", "");
            if (!mStyle.equals("")) {
                jsonObject.put("Style", mStyle);
            }
            if (!mBand.equals("")) {
                jsonObject.put("Band", mBand);
            }
            jsonObject.put("StartDate", mStartDate);
            jsonObject.put("EndDate", mEndDate);
            if (!mYear.equals("")) {
                jsonObject.put("Year", mYear);
            }
            if (!mSeason.equals("")) {
                jsonObject.put("Season", mSeason);
            }
            if (!mTheme.equals("")) {
                jsonObject.put("Theme", mTheme);
            }
            if (!mMaker.equals("")) {
                jsonObject.put("Maker", mMaker);
            }
            jsonObject.put("StartDate", mStartDate);
            jsonObject.put("EndDate", mEndDate);
            jsonObject.put("sosearch", graphic_etSearch.getText().toString());
            jsonObject.put("SrchFld", "");
            jsonObject.put("State", 2);
            jsonObject.put("fPage", 1);
            jsonObject.put("PageCnt", 1000);
            jsonObject.put("PageStart", mPageStart);
            jsonObject.put("PageEnd", mPageEnd);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        mJson = jsonObject.toString().replace("{", "").replace("}", "");
    }


    // customize date dialog
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
                        //...To-do
                        mGraphic_btnDayDef.setSelected(true);
                        mGraphic_btnDayOne.setSelected(false);
                        mGraphic_btnDayThr.setSelected(false);
                        mGraphic_btnDaySev.setSelected(false);
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
                                //...To-do
                            }
                        })
                // 显示
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == RESULT_OK) {
            GraphicNotesActivity.this.onResume();
            mRefreshLayout.autoRefresh();
        }
    }

}
