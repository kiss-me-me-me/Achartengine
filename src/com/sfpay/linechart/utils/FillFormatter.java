package com.sfpay.linechart.utils;

import com.sfpay.linechart.data.LineData;
import com.sfpay.linechart.data.LineDataSet;

public abstract interface FillFormatter {
	public abstract float getFillLinePosition(LineDataSet paramLineDataSet,
			LineData paramLineData, float paramFloat1, float paramFloat2);
}
