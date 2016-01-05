package com.sfpay.linechart.interfaces;

import com.sfpay.linechart.data.LineData;
import com.sfpay.linechart.utils.FillFormatter;

public abstract interface LineDataProvider extends
		BarLineScatterCandleDataProvider {
	public abstract LineData getLineData();

	public abstract void setFillFormatter(FillFormatter paramFillFormatter);

	public abstract FillFormatter getFillFormatter();
}