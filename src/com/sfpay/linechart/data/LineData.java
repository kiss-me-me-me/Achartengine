package com.sfpay.linechart.data;

import java.util.List;

public class LineData extends BarLineScatterCandleData<LineDataSet> {
	public LineData(List<String> xVals, List<LineDataSet> dataSets) {
		super(xVals, dataSets);
	}
}
