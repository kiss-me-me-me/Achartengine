package com.sfpay.linechart.listener;

import com.sfpay.linechart.data.Entry;
import com.sfpay.linechart.utils.Highlight;

public abstract interface OnChartValueSelectedListener {
	public abstract void onValueSelected(Entry paramEntry, int paramInt,
			Highlight paramHighlight);

	public abstract void onNothingSelected();
}
