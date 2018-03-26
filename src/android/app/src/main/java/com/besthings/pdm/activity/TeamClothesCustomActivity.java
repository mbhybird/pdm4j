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
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import com.besthings.bean.TeamClothesCustomOrderListRet;
import com.besthings.pdm.R;
import com.besthings.pdm.adapter.TeamClothesCustomOrderListAdapter;
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

public class TeamClothesCustomActivity extends BaseActivity {
    private static final String TAG = TeamClothesCustomActivity.class.getSimpleName();
    Button btnDayOne;
    Button btnDayThr;
    Button btnDaySev;
    Button btnMonthOne;
    Button btnDayDef;
    Button btnSearch;
    EditText etSearch;
    RecyclerView mRecyclerView;
    Calendar mCalendar = Calendar.getInstance();
    Calendar mGCalendar = new GregorianCalendar();
    DateFormat mSDF = new SimpleDateFormat("yyyy-MM-dd");
    JSONObject jsonObject = new JSONObject();
    List<TeamClothesCustomOrderListRet> mDataList = new ArrayList<>();
    List<TeamClothesCustomOrderListRet> mBaseList = new ArrayList<>();
    TeamClothesCustomOrderListAdapter mAdapter;
    RefreshLayout mRefreshLayout;

    String mStartDate = "";
    String mEndDate = "";
    String mSoSearch = "";
    String mJson;
    int mPageStart;
    int mPageEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_clothes_custom);

        setCustomActionBar(true);

        btnDayOne = (Button) findViewById(R.id.team_clothes_custom_btnDayOne);
        btnDayThr = (Button) findViewById(R.id.team_clothes_custom_btnDayThr);
        btnDaySev = (Button) findViewById(R.id.team_clothes_custom_btnDaySev);
        btnMonthOne = (Button) findViewById(R.id.team_clothes_custom_btnMonthOne);
        btnDayDef = (Button) findViewById(R.id.team_clothes_custom_btnDayDef);
        etSearch = (EditText) findViewById(R.id.team_clothes_custom_etSearch);
        btnSearch = (Button) findViewById(R.id.team_clothes_custom_btnSearch);
        mRecyclerView = (RecyclerView) findViewById(R.id.team_clothes_custom_rv);
        mRefreshLayout = (RefreshLayout) findViewById(R.id.team_clothes_custom_refreshLayout);

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
                            mDataList = myApp.NetHelperEx.getTeamClothesCustomOrderListBean(mJson).getRet();
                            mBaseList = mDataList;
                            mAdapter = new TeamClothesCustomOrderListAdapter(R.layout.item_team_clothes_custom, mDataList);
                            mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
                            mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                    Intent it = new Intent(TeamClothesCustomActivity.this, TeamClothesCustomDetailListActivity.class);
                                    it.putExtra("MID", mBaseList.get(position).getMid());
                                    it.putExtra("title", "团服定制");
                                    startActivity(it);
                                }

                            });
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
                                                    mDataList = myApp.NetHelperEx.getTeamClothesCustomOrderListBean(mJson).getRet();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                                if (mDataList.size() > 0) {
                                                    mAdapter.addData(mDataList);
                                                    mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                                        @Override
                                                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                                            Intent it = new Intent(TeamClothesCustomActivity.this, TeamClothesCustomDetailListActivity.class);
                                                            it.putExtra("MID", mBaseList.get(position).getMid());
                                                            it.putExtra("title", "团服定制");
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
                mRefreshLayout.autoRefresh();
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

    private void initJson() {
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
            jsonObject.put("iType", 2);
            jsonObject.put("State", -1);
            if (!mSoSearch.equals("")) {
                jsonObject.put("sosearch", mSoSearch);
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

}
