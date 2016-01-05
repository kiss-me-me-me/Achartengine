package com.example.chartengine;

import java.util.ArrayList;

import com.sfpay.linechart.charts.LineChart;
import com.sfpay.linechart.components.MarkerView;
import com.sfpay.linechart.components.YAxis;
import com.sfpay.linechart.data.Entry;
import com.sfpay.linechart.data.LineData;
import com.sfpay.linechart.data.LineDataSet;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

public class MainActivity extends Activity {

	private LineChart lineChart;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initLineChart();
        setData();
    }
    
private void initLineChart() {
		
		lineChart = (LineChart) findViewById(R.id.line_chart);

        lineChart.setHighlightEnabled(true);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(false);
        lineChart.setHighlightIndicatorEnabled(false);

        MarkerView mv = new MarkerView(this, R.layout.managemoney_floatframe, R.id.floatframe);
        lineChart.setMarkerView(mv);
        
        lineChart.setVisibleXRange(6.5f);
        lineChart.setBackgroundColor(Color.parseColor("#ffffff"));
        lineChart.setChartMargins(18f);

        YAxis leftAxis = lineChart.getAxisLeft();
        
        leftAxis.setLabelCount(6);
        leftAxis.setStartAtZero(false);
	}
	
	private void setData() {
		String[] dateTime = {"01-01","01-02","01-03","01-04","01-05","01-06","01-07"};//日期
//		float[] seven = {1.00f,2.00f,9.00f,4.00f,1.00f,6.00f,3.00f};//日期
		float[] seven = {4.35f, 4.35f, 4.35f, 4.35f, 4.35f, 8.35f, 4.35f};
		ArrayList<String> xVals = new ArrayList<String>();	//添加x坐标的值
		for (int x = 0;x < dateTime.length;x++) {
			if (dateTime[x].length() != 4) {
				xVals.add(dateTime[x]);
				continue;
			}
			xVals.add(dateTime[x].substring(0, 2) + '-' + dateTime[x].substring(2, 4));
		}
		
		ArrayList<Entry> yVals = new ArrayList<Entry>();	//添加y坐标的值
		for (int y = 0;y < seven.length;y++) {
			yVals.add(new Entry(seven[y], y));
		}
		
		if (xVals.size() > yVals.size()) {
			for (int i = yVals.size();i < xVals.size();i++) {
				xVals.remove(i);
			}
		} else if (yVals.size() > xVals.size()) {
			for (int i = xVals.size();i < yVals.size();i++) {
				yVals.remove(i);
			}
		}
		
		LineDataSet lineDataSet = new LineDataSet(yVals, "");
		lineDataSet.setColor(Color.parseColor("#ea3636"));	//曲线的颜色
		lineDataSet.setCircleColor(Color.parseColor("#ea3636"));	//圆的颜色
		lineDataSet.setLineWidth(2f);	//线宽
		lineDataSet.setCircleSize(6f);	//圆的半径
		lineDataSet.setDrawCircleHole(true);	//true为空心，false为实心
		lineDataSet.setValueTextSize(12f);	//数值的字体大小
		lineDataSet.setFillAlpha(25);	//填充色的透明度，0为完全透明
		lineDataSet.setFillColor(Color.parseColor("#ea3636"));	//填充色
		
		ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
		dataSets.add(lineDataSet);
		
		LineData data = new LineData(xVals, dataSets);
		lineChart.setData(data);
	}
}
