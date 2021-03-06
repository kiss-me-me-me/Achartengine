package com.sfpay.linechart.utils;

import java.text.DecimalFormat;

public class DefaultValueFormatter implements ValueFormatter {
	private DecimalFormat mFormat;

	public DefaultValueFormatter(int digits) {
		StringBuffer b = new StringBuffer();
		for (int i = 0; i < digits; i++) {
			if (i == 0)
				b.append(".");
			b.append("0");
		}
		this.mFormat = new DecimalFormat("###,###,###,##0" + b.toString());
	}

	public String getFormattedValue(float value) {
		return this.mFormat.format(value);
	}
}
