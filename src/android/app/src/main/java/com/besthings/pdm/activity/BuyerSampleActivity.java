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
import android.widget.ImageButton;
import android.widget.Spinner;

import com.besthings.bean.BuyerSampleRet;
import com.besthings.pdm.R;
import com.besthings.pdm.adapter.BuyerSampleAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
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

import static android.nfc.tech.MifareUltralight.PAGE_SIZE;

public class BuyerSampleActivity extends BaseActivity {
    private static final String TAG = BuyerSampleActivity.class.getSimpleName();

    String mStyleNo = "";
    String mSoSearch = "";
    String mFilterJson = "";
    JSONObject mFilterJsonObject;
    DateFormat mSDF = new SimpleDateFormat("yyyy-MM-dd");
    Calendar mCalendar = Calendar.getInstance();
    Calendar mGCalendar = new GregorianCalendar();
    String mStartDate = "";
    String mEndDate = "";
    List<BuyerSampleRet> mDataList = new ArrayList<>();
    List<BuyerSampleRet> mBaseList = new ArrayList<>();
    BuyerSampleAdapter mAdapter;
    RecyclerView mRecyclerView;
    Spinner spFilter;
    EditText etSearch;
    Button btnSearch;
    Button btnDayOne;
    Button btnDayThree;
    Button btnDayWeek;
    Button btnCustomize;
    RefreshLayout refreshLayout;
    String mMID = "";
    int mPageStart;
    int mPageEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_sample);

        setCustomActionBar(true);

        btnDayOne = (Button) findViewById(R.id.buyer_sample_btnDayOne);
        btnDayThree = (Button) findViewById(R.id.buyer_sample_btnDayThree);
        btnDayWeek = (Button) findViewById(R.id.buyer_sample_btnDayWeek);
        btnCustomize = (Button) findViewById(R.id.buyer_sample_btnDayCustomize);
        btnSearch = (Button) findViewById(R.id.buyer_sample_btnSearch);
        etSearch = (EditText) findViewById(R.id.buyer_sample_etSearch);
        refreshLayout = (RefreshLayout) findViewById(R.id.buyer_sample_refreshLayout);
        ImageButton ibCapture = (ImageButton) findViewById(R.id.buyer_sample_ibCapture);
        ibCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(BuyerSampleActivity.this, BuyerSampleEditActivity.class);
                it.putExtra("EditType", 0);
                startActivity(it);
            }
        });

        findViewById(R.id.buyer_sample_btnScanQRCode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                scan qr code...
                new IntentIntegrator(BuyerSampleActivity.this)
                        .setOrientationLocked(false)
                        .initiateScan();
            }
        });

        spFilter = (Spinner) findViewById(R.id.buyer_sample_spFilter);
        ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"筛选","款号"});
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFilter.setAdapter(spAdapter);
        spFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 1) {
//                    startActivityForFilterResult(Constant.REQUEST_CODE_STYLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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

        mRecyclerView = (RecyclerView) findViewById(R.id.buyer_sample_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

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
                            mDataList = myApp.NetHelper.getBuyerSampleBean(mFilterJson).getRet();
                            mBaseList = mDataList;
                            mAdapter = new BuyerSampleAdapter(R.layout.item_buyer_sample, mDataList);
                            mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
                            mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                    Intent it = new Intent(BuyerSampleActivity.this, BuyerSampleEditActivity.class);
                                    it.putExtra("EditType", 1);
                                    it.putExtra("MID", mBaseList.get(position).getMID());
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
                                                buildQueryJson();
                                                try {
                                                    mDataList = myApp.NetHelper.getBuyerSampleBean(mFilterJson).getRet();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                                if (mDataList.size() > 0) {

                                                    mAdapter.addData(mDataList);
                                                    mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                                        @Override
                                                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                                            Intent it = new Intent(BuyerSampleActivity.this, BuyerSampleEditActivity.class);
                                                            it.putExtra("EditType", 1);
                                                            it.putExtra("MID", mBaseList.get(position).getMID());
                                                            startActivity(it);
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
    protected void doSearchAction(int actionCode, List<String> parameter) {
        super.doSearchAction(actionCode, parameter);
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
        mStyleNo = "";
        mFilterJson = "";
        mFilterJsonObject = new JSONObject();
        try {
            mFilterJsonObject.put("ID", 23001);
            mFilterJsonObject.put("SeriesID", myApp.mSeriesID);
            switch (spFilter.getSelectedItemPosition()) {
                case 0:
                    mSoSearch = etSearch.getText().toString();
                    break;
                //款号
                case 1:
                    mStyleNo = etSearch.getText().toString();
                    break;
                //筛选(0)
                default:
                    mSoSearch = "";
                    mStyleNo = "";
                    break;
            }
            //开始日期
            if (!mStartDate.equals("")) {
                mFilterJsonObject.put("StartDate", mStartDate);
            }
            //结束日期
            if (!mEndDate.equals("")) {
                mFilterJsonObject.put("EndDate", mEndDate);
            }
            //款号
            if (!mStyleNo.equals("")) {
                mFilterJsonObject.put("cStyleNo", mStyleNo);
            }
            if (!mSoSearch.equals("")) {
                mFilterJsonObject.put("soserach", mSoSearch);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            mMID = result.getContents();
            Log.i(TAG, mMID.substring(8, mMID.length()-4));
            try {
                boolean qrResult;
                qrResult = myApp.NetHelperEx.getBuyerSampleDetail(mMID.substring(8, mMID.length()-4));
                if(qrResult) {
                    Intent it = new Intent(BuyerSampleActivity.this, BuyerSampleEditActivity.class);
                    it.putExtra("EditType", 1);
                    it.putExtra("MID", mMID.substring(8, mMID.length()-4));
                    startActivity(it);
                } else {
                    showAlert("\"扫码登录失败！\\r\\n1.请检查网络是否正常 \\r\\n2.二维码是否合法\"");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
