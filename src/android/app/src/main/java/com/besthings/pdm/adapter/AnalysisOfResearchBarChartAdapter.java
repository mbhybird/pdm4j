package com.besthings.pdm.adapter;

import android.graphics.Color;

import com.besthings.bean.AnalysisOfResearchChartRet;
import com.besthings.pdm.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chas on 2017/11/9.
 */

public class AnalysisOfResearchBarChartAdapter extends BaseQuickAdapter<AnalysisOfResearchChartRet, BaseViewHolder> {
    public AnalysisOfResearchBarChartAdapter(int layoutResId, List<AnalysisOfResearchChartRet> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AnalysisOfResearchChartRet item) {
        HorizontalBarChart horizontalBarChart = helper.getView(R.id.item_analysis_of_research_barChart);

        switch (item.getSno()) {
            case 0:
                helper.setText(R.id.item_analysis_of_research_tvStateName, "设计状态");
                break;
            case 1:
                helper.setText(R.id.item_analysis_of_research_tvStateName, "工艺状态");
                break;
            case 2:
                helper.setText(R.id.item_analysis_of_research_tvStateName, "订单状态");
                break;
            case 3:
                helper.setText(R.id.item_analysis_of_research_tvStateName, "备料状态");
                break;
            case 4:
                helper.setText(R.id.item_analysis_of_research_tvStateName, "打版状态");
                break;
            case 5:
                helper.setText(R.id.item_analysis_of_research_tvStateName, "车版状态");
                break;
            case 6:
                helper.setText(R.id.item_analysis_of_research_tvStateName, "放码状态");
                break;
            case 7:
                helper.setText(R.id.item_analysis_of_research_tvStateName, "单裁状态");
                break;
            case 8:
                helper.setText(R.id.item_analysis_of_research_tvStateName, "入库状态");
                break;
            case 9:
                helper.setText(R.id.item_analysis_of_research_tvStateName, "后道状态");
                break;
            case 10:
                helper.setText(R.id.item_analysis_of_research_tvStateName, "排料状态");
                break;
            case 11:
                helper.setText(R.id.item_analysis_of_research_tvStateName, "量裁状态");
                break;
            case 12:
                helper.setText(R.id.item_analysis_of_research_tvStateName, "缝制状态");
                break;
            case 13:
                helper.setText(R.id.item_analysis_of_research_tvStateName, "算料状态");
                break;
            case 14:
                helper.setText(R.id.item_analysis_of_research_tvStateName, "领料状态");
                break;
            default:
                helper.setText(R.id.item_analysis_of_research_tvStateName, ""+item.getSno());
                break;
        }
        horizontalBarChart.setDrawBarShadow(false);
        horizontalBarChart.setDrawValueAboveBar(true);
        horizontalBarChart.getDescription().setEnabled(false);
        horizontalBarChart.setPinchZoom(false);
        horizontalBarChart.setDrawGridBackground(false);
        horizontalBarChart.setDoubleTapToZoomEnabled(false);
        horizontalBarChart.setHighlightFullBarEnabled(false);
        horizontalBarChart.setScaleXEnabled(false);
        horizontalBarChart.setScaleYEnabled(false);

        //x轴
        XAxis xl = horizontalBarChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);
        MyXValueFormatter xValueFormatter = new MyXValueFormatter();
        xl.setValueFormatter(xValueFormatter);


        //y轴
        YAxis yl = horizontalBarChart.getAxisLeft();
        yl.setAxisMinimum(0f);
        float temp = 0f;
        float zero = 0.2f;
        for (int i = 0; i < item.getS().size(); i++) {
            float value = item.getS().get(item.getS().size() - 1 - i);
            if (value > temp) {
                temp = value;
            }
        }
        if (temp < 100f) {
            yl.setAxisMaximum(100f);
        } else
        {
            zero = 0.99f;
        }
        if (temp < 600f) {
            xl.setEnabled(false);
        } else {
            xl.setEnabled(true);
        }
        yl.setEnabled(false);

        //y轴
        YAxis yr = horizontalBarChart.getAxisRight();
        yr.setAxisMinimum(0f);
        yr.setEnabled(false);

        //设置数据
        float barWidth = 9f;
        float spaceForBar = 10f;
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = 0; i < item.getS().size(); i++) {
            float value = item.getS().get(item.getS().size() - 1 - i);
            if (value == 0) {
                yVals1.add(new BarEntry(i * spaceForBar, zero));
            } else {
                yVals1.add(new BarEntry(i * spaceForBar, value));
            }
        }

        BarDataSet set1;
        if (horizontalBarChart.getData() != null &&
                horizontalBarChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) horizontalBarChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            horizontalBarChart.getData().notifyDataChanged();
            horizontalBarChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "DataSet");
            MyIValueFormatter iValueFormatter = new MyIValueFormatter();
            set1.setValueFormatter(iValueFormatter);
            set1.setHighlightEnabled(false);
            set1.setColors(Color.rgb(242, 106, 29),Color.rgb(241, 24, 23),Color.rgb(50, 201, 42),Color.rgb(250, 216, 23),Color.rgb(109, 109, 109));
            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(barWidth);
            horizontalBarChart.setData(data);
        }
        horizontalBarChart.setFitBars(true);

        Legend l = horizontalBarChart.getLegend();
        l.setEnabled(false);

    }

    public class MyIValueFormatter implements IValueFormatter {

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            int i = (int) value;
            if (i != 0) {
                return String.valueOf(i);
            }
            else {
                return "";
            }
        }
    }

    public class MyXValueFormatter implements IAxisValueFormatter {

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return "";
        }
    }
}