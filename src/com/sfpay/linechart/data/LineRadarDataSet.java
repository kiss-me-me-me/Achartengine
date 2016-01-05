//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.sfpay.linechart.data;

import java.util.List;

import android.graphics.Color;

import com.sfpay.linechart.utils.Utils;

public abstract class LineRadarDataSet<T extends Entry> extends
		BarLineScatterCandleDataSet<T> {
	private int mFillColor = Color.rgb(140, 234, 255);
	private int mFillAlpha = 85;
	private float mLineWidth = 2.5F;
	private boolean mDrawFilled = true;

	public LineRadarDataSet(List<T> yVals, String label) {
		super(yVals, label);
	}

	public int getFillColor() {
		return this.mFillColor;
	}

	public void setFillColor(int color) {
		this.mFillColor = color;
	}

	public int getFillAlpha() {
		return this.mFillAlpha;
	}

	public void setFillAlpha(int alpha) {
		this.mFillAlpha = alpha;
	}

	public void setLineWidth(float width) {
		if (width < 0.2F) {
			width = 0.5F;
		}

		if (width > 10.0F) {
			width = 10.0F;
		}

		this.mLineWidth = Utils.convertDpToPixel(width);
	}

	public float getLineWidth() {
		return this.mLineWidth;
	}

	public void setDrawFilled(boolean filled) {
		this.mDrawFilled = filled;
	}

	public boolean isDrawFilledEnabled() {
		return this.mDrawFilled;
	}
}
