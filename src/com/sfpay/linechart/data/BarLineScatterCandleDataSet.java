package com.sfpay.linechart.data;

import java.util.List;

import android.graphics.Color;

public abstract class BarLineScatterCandleDataSet<T extends Entry> extends
		DataSet<T> {
	protected int mHighLightColor = Color.rgb(255, 187, 115);

	public BarLineScatterCandleDataSet(List<T> yVals, String label) {
		super(yVals, label);
	}

	public int getHighLightColor() {
		return this.mHighLightColor;
	}
}
