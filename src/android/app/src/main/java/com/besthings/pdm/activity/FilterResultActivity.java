package com.besthings.pdm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.besthings.bean.BaseRet;
import com.besthings.bean.MakerRet;
import com.besthings.bean.StyleAndCategoryRet;
import com.besthings.pdm.R;
import com.besthings.pdm.adapter.MyListViewAdapter;
import com.besthings.pdm.entity.MyListViewHolder;
import com.besthings.pdm.utils.Constant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class FilterResultActivity extends BaseActivity {
    TextView tv = null;
    ListView lv = null;
    Button btnSelectAll = null;
    Button btnInverseSelect = null;
    Button btnCancel = null;
    String name[] = {"G1", "G2", "G3", "G4", "G5", "G6", "G7", "G8", "G9", "G10", "G11", "G12", "G13", "G14"};

    ArrayList<String> listStr = null;
    private List<HashMap<String, Object>> list = null;
    private MyListViewAdapter adapter;

    Button mBtnOK;
    String mSeriesID;
    List<String> dataSource = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_result);

        getSupportActionBar().hide();

        try {
            mSeriesID = String.valueOf(myApp.NetHelper.getSeriesIDBean().getRet().get(0).getSeriesID());
            switch (getIntent().getIntExtra("RequestCode", -1)) {
                case Constant.REQUEST_CODE_BAND:
                    for (BaseRet br : myApp.NetHelper.getBandBean(mSeriesID).getRet()) {
                        dataSource.add(br.getTypeName());
                    }
                    break;

                case Constant.REQUEST_CODE_BRAND:
                    for (String item : myApp.NetHelper.getBrandBean(mSeriesID).getRet()) {
                        dataSource.add(item);
                    }
                    break;

                case Constant.REQUEST_CODE_MAKER:
                    for (MakerRet mr : myApp.NetHelper.getMakerBean(mSeriesID).getRet()) {
                        if (!mr.getCode().equals("")) {
                            dataSource.add(mr.getCode());
                        }
                    }
                    break;

                case Constant.REQUEST_CODE_SEASON:
                    for (BaseRet br : myApp.NetHelper.getSeasonBean(mSeriesID).getRet()) {
                        dataSource.add(br.getTypeName());
                    }
                    break;

                case Constant.REQUEST_CODE_STYLE:
                    for (String item : myApp.NetHelper.getStyleBean(mSeriesID).getRet()) {
                        dataSource.add(item);
                    }
                    break;

                case Constant.REQUEST_CODE_THEME:
                    for (BaseRet br : myApp.NetHelper.getThemeBean(mSeriesID).getRet()) {
                        dataSource.add(br.getTypeName());
                    }
                    break;

                case Constant.REQUEST_CODE_YEAR:
                    for (BaseRet br : myApp.NetHelper.getYeanBean(mSeriesID).getRet()) {
                        dataSource.add(br.getTypeName());
                    }
                    break;

                case Constant.REQUEST_CODE_STYLE_A:
                    for (StyleAndCategoryRet br : myApp.NetHelperEx.getStyleAndCategoryBean(mSeriesID).getRet()) {
                        dataSource.add(br.getStylea());
                    }
                    break;

                case Constant.REQUEST_CODE_STYLE_B:
                    for (StyleAndCategoryRet br : myApp.NetHelperEx.getStyleAndCategoryBean(mSeriesID).getRet()) {
                        dataSource.add(br.getStyleb());
                    }
                    break;

                case Constant.REQUEST_CODE_TECHNICIAN:
                    for (MakerRet mr : myApp.NetHelperEx.getUserBean(mSeriesID).getRet()) {
                        if (!mr.getCode().equals("")) {
                            dataSource.add(mr.getCode());
                        }
                    }
                    break;

                case Constant.REQUEST_CODE_TEMPLETEMAN:
                    for (MakerRet mr : myApp.NetHelperEx.getUserBean(mSeriesID).getRet()) {
                        if (!mr.getCode().equals("")) {
                            dataSource.add(mr.getCode());
                        }
                    }
                    break;

                case Constant.REQUEST_CODE_EXPRESS_COMPANY:
                    for (BaseRet br : myApp.NetHelperEx.getExpressCompanyBean(mSeriesID).getRet()) {
                        dataSource.add(br.getTypeName());
                    }
                    break;

                default:
                    dataSource = Arrays.asList(name);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        mBtnOK = (Button) findViewById(R.id.filter_result_btnOK);
        mBtnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent filterIntent = new Intent();
                filterIntent.putStringArrayListExtra("Parameter", listStr);
                setResult(RESULT_OK, filterIntent);
                finish();
            }
        });

        tv = (TextView) this.findViewById(R.id.filter_result_tv);
        lv = (ListView) this.findViewById(R.id.filter_result_lv);
        btnSelectAll = (Button) this.findViewById(R.id.filter_result_SelectAll);
        btnInverseSelect = (Button) this.findViewById(R.id.filter_result_btnInverseSelect);
        btnCancel = (Button) this.findViewById(R.id.filter_result_btnCancel);
        showCheckBoxListView();

        //全选
        btnSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                listStr = new ArrayList<String>();
                for (int i = 0; i < list.size(); i++) {
                    MyListViewAdapter.isSelected.put(i, true);
                    listStr.add(dataSource.get(i));
                }
                adapter.notifyDataSetChanged();//注意这一句必须加上，否则checkbox无法正常更新状态
                tv.setText("已选中" + listStr.size() + "项");
            }
        });

        //反选
        btnInverseSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < list.size(); i++) {
                    if (MyListViewAdapter.isSelected.get(i) == false) {
                        MyListViewAdapter.isSelected.put(i, true);
                        listStr.add(dataSource.get(i));
                    } else {
                        MyListViewAdapter.isSelected.put(i, false);
                        listStr.remove(dataSource.get(i));
                    }
                }
                adapter.notifyDataSetChanged();
                tv.setText("已选中" + listStr.size() + "项");
            }

        });

        //取消已选
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < list.size(); i++) {
                    if (MyListViewAdapter.isSelected.get(i) == true) {
                        MyListViewAdapter.isSelected.put(i, false);
                        listStr.remove(dataSource.get(i));
                    }
                }
                adapter.notifyDataSetChanged();
                tv.setText("已选中" + listStr.size() + "项");
            }

        });
    }

    // 显示带有checkbox的ListView
    public void showCheckBoxListView() {
        list = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < dataSource.size(); i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("item_tv", dataSource.get(i));
            map.put("item_cb", false);
            list.add(map);

            adapter = new MyListViewAdapter(this, list, R.layout.item_listview,
                    new String[]{"item_tv", "item_cb"}, new int[]{
                    R.id.item_listview_tv, R.id.item_listview_cb});
            lv.setAdapter(adapter);
            listStr = new ArrayList<String>();
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View view,
                                        int position, long arg3) {
                    MyListViewHolder holder = (MyListViewHolder) view.getTag();
                    holder.cb.toggle();// 在每次获取点击的item时改变checkbox的状态
                    MyListViewAdapter.isSelected.put(position, holder.cb.isChecked()); // 同时修改map的值保存状态
                    if (holder.cb.isChecked() == true) {
                        listStr.add(dataSource.get(position));
                    } else {
                        listStr.remove(dataSource.get(position));
                    }
                    tv.setText("已选中" + listStr.size() + "项");
                }

            });
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        mBtnOK.callOnClick();
        return true;
    }
}