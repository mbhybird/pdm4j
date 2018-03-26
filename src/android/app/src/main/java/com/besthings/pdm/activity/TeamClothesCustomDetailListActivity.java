package com.besthings.pdm.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.besthings.bean.TeamClothesCustomOrderDetailListRet;
import com.besthings.pdm.R;
import com.besthings.pdm.adapter.TeamClothesCustomOrderDetailListAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class TeamClothesCustomDetailListActivity extends BaseActivity {

    private static final String TAG = TeamClothesCustomDetailListActivity.class.getSimpleName();
    TextView tvPlateState;
    TextView tvClothState;
    TextView tvStockState;
    TextView tvMatState;
    TextView tvCutState;
    TextView tvAssemblyState;
    TextView tvDeliver;
    TextView tvAll;
    RecyclerView mRecyclerView;

    List<TeamClothesCustomOrderDetailListRet> mDataList;
    TeamClothesCustomOrderDetailListAdapter mAdapter;
    JSONObject jsonObject;
    String mJson;
    String mMID;
    String mPlateState = "";
    String mClothState = "";
    String mStockState = "";
    String mMatState = "";
    String mCutState = "";
    String mAssemblyState = "";
    String mState = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_clothes_custom_detail_list);

        setCustomActionBar(true);
        tvAll = (TextView) findViewById(R.id.team_clothes_custom_detail_list_tvAll);
        tvPlateState = (TextView) findViewById(R.id.team_clothes_custom_detail_list_tvPlateState);
        tvClothState = (TextView) findViewById(R.id.team_clothes_custom_detail_list_tvClothState);
        tvStockState = (TextView) findViewById(R.id.team_clothes_custom_detail_list_tvStockState);
        tvMatState = (TextView) findViewById(R.id.team_clothes_custom_detail_list_tvMatState);
        tvCutState = (TextView) findViewById(R.id.team_clothes_custom_detail_list_tvCutState);
        tvAssemblyState = (TextView) findViewById(R.id.team_clothes_custom_detail_list_tvAssemblyState);
        tvDeliver = (TextView) findViewById(R.id.team_clothes_custom_detail_list_tvDeliver);
        mRecyclerView = (RecyclerView) findViewById(R.id.team_clothes_custom_detail_list_rvRecord);

        tvAll.setSelected(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


        if (getIntent().hasExtra("MID")) {
            mMID = getIntent().getStringExtra("MID");
            showTeamClothesCustomDetailList();
        }
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
                showTeamClothesCustomDetailList();
            }
        });

    }

    private void selectedInit() {
        mPlateState = "";
        mClothState = "";
        mStockState = "";
        mMatState = "";
        mCutState = "";
        mAssemblyState = "";
        mState = "";
        tvAll.setSelected(false);
        tvPlateState.setSelected(false);
        tvClothState.setSelected(false);
        tvStockState.setSelected(false);
        tvMatState.setSelected(false);
        tvCutState.setSelected(false);
        tvAssemblyState.setSelected(false);
        tvDeliver.setSelected(false);

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
            jsonObject.put("ID", 15001);
            jsonObject.put("SeriesID", myApp.mSeriesID);
            jsonObject.put("MID", mMID);
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mJson = jsonObject.toString();
    }

    private void showTeamClothesCustomDetailList(){
        try {
            initJson();
            mDataList = myApp.NetHelperEx.getTeamClothesCustomOrderDetailListBean(mJson).getRet();
            mAdapter = new TeamClothesCustomOrderDetailListAdapter(R.layout.item_team_clothes_custom_detail_list, mDataList);
            mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
            mRecyclerView.setAdapter(mAdapter);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showMatStateAlertDialog() {
        final String[] items = { "无面料","未备料","备料中","已备料"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(TeamClothesCustomDetailListActivity.this);
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
                showTeamClothesCustomDetailList();
                selectedInit();
                tvMatState.setSelected(true);
            }
        });
        listDialog.show();
    }

    private void showPlateStateAlertDialog() {
        final String[] items = { "未开始","进行中","已完成"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(TeamClothesCustomDetailListActivity.this);
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
                showTeamClothesCustomDetailList();
                selectedInit();
                tvPlateState.setSelected(true);
            }
        });
        listDialog.show();
    }

    private void showCutStateAlertDialog() {
        final String[] items = { "未开始","进行中","已完成"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(TeamClothesCustomDetailListActivity.this);
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
                showTeamClothesCustomDetailList();
                selectedInit();
                tvCutState.setSelected(true);
            }
        });
        listDialog.show();
    }

    private void showClothStateAlertDialog() {
        final String[] items = { "未开始","进行中","已完成"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(TeamClothesCustomDetailListActivity.this);
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
                showTeamClothesCustomDetailList();
                selectedInit();
                tvClothState.setSelected(true);
            }
        });
        listDialog.show();
    }

    private void showAssemblyStateAlertDialog() {
        final String[] items = { "未开始","进行中","已完成"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(TeamClothesCustomDetailListActivity.this);
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
                showTeamClothesCustomDetailList();
                selectedInit();
                tvAssemblyState.setSelected(true);
            }
        });
        listDialog.show();
    }

    private void showStockStateAlertDialog() {
        final String[] items = { "未入库","进行中","已入库"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(TeamClothesCustomDetailListActivity.this);
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
                showTeamClothesCustomDetailList();
                selectedInit();
                tvStockState.setSelected(true);
            }
        });
        listDialog.show();
    }

    private void showDeliverStateAlertDialog() {
        final String[] items = {"未发货","已发货"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(TeamClothesCustomDetailListActivity.this);
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
                showTeamClothesCustomDetailList();
                selectedInit();
                tvDeliver.setSelected(true);
            }
        });
        listDialog.show();
    }
}
