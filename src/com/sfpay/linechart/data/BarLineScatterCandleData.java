package com.sfpay.linechart.data;

import java.util.List;

public abstract class BarLineScatterCandleData<T extends BarLineScatterCandleDataSet<? extends Entry>>
		extends ChartData<T> {
	public BarLineScatterCandleData(List<String> xVals, List<T> sets) {
		super(xVals, sets);
	}
}
