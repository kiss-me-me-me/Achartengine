//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.sfpay.linechart.charts;

import android.content.Context;
import android.util.AttributeSet;

import com.sfpay.linechart.data.LineData;
import com.sfpay.linechart.interfaces.LineDataProvider;
import com.sfpay.linechart.renderer.LineChartRenderer;
import com.sfpay.linechart.utils.FillFormatter;

public class LineChart extends BarLineChartBase<LineData> implements
		LineDataProvider {
	protected float mHighlightWidth = 3.0F;
	private FillFormatter mFillFormatter;

	public LineChart(Context context) {
		super(context);
	}

	public LineChart(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LineChart(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	protected void init() {
		super.init();
		this.mRenderer = new LineChartRenderer(this, this.mViewPortHandler);
		this.mFillFormatter = new DefaultFillFormatter();
	}

	protected void calcMinMax() {
		super.calcMinMax();
		if (this.mDeltaX == 0.0F && ((LineData) this.mData).getYValCount() > 0) {
			this.mDeltaX = 1.0F;
		}

	}

	public void setHighlightLineWidth(float width) {
		this.mHighlightWidth = width;
	}

	public float getHighlightLineWidth() {
		return this.mHighlightWidth;
	}

	public void setFillFormatter(FillFormatter formatter) {
		if (formatter == null) {
			new DefaultFillFormatter();
		} else {
			this.mFillFormatter = formatter;
		}

	}

	public FillFormatter getFillFormatter() {
		return this.mFillFormatter;
	}

	public LineData getLineData() {
		return (LineData) this.mData;
	}
}
