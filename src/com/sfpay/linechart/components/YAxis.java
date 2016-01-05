package com.sfpay.linechart.components;

import android.graphics.Paint;

import com.sfpay.linechart.utils.DefaultValueFormatter;
import com.sfpay.linechart.utils.Utils;
import com.sfpay.linechart.utils.ValueFormatter;

public class YAxis extends AxisBase {
	protected ValueFormatter mValueFormatter;
	public float[] mEntries = new float[0];
	public int mEntryCount;
	public int mDecimals;
	private int mLabelCount = 6;

	private boolean mDrawTopYLabelEntry = true;
	protected boolean mShowOnlyMinMax = false;
	protected boolean mInverted = false;
	protected boolean mStartAtZero = true;
	protected float mCustomAxisMin = (0.0F / 0.0F);
	protected float mCustomAxisMax = (0.0F / 0.0F);
	protected float mSpacePercentTop = 10.0F;
	protected float mSpacePercentBottom = 10.0F;
	public float scale = 0.0F;
	public float mAxisMaximum = 0.0F;
	public float mAxisMinimum = 0.0F;
	public float mAxisRange = 0.0F;
	private YAxisLabelPosition mPosition = YAxisLabelPosition.INSIDE_CHART;
	private AxisDependency mAxisDependency;

	public YAxis(AxisDependency position) {
		this.mAxisDependency = position;
	}

	public AxisDependency getAxisDependency() {
		return this.mAxisDependency;
	}

	public YAxisLabelPosition getLabelPosition() {
		return this.mPosition;
	}

	public void setPosition(YAxisLabelPosition pos) {
		this.mPosition = pos;
	}

	public boolean isDrawTopYLabelEntryEnabled() {
		return this.mDrawTopYLabelEntry;
	}

	public void setDrawTopYLabelEntry(boolean enabled) {
		this.mDrawTopYLabelEntry = enabled;
	}

	public void setLabelCount(int yCount) {
		if (yCount > 15)
			yCount = 15;
		if (yCount < 2) {
			yCount = 2;
		}
		this.mLabelCount = yCount;
	}

	public int getLabelCount() {
		return this.mLabelCount;
	}

	public void setShowOnlyMinMax(boolean enabled) {
		this.mShowOnlyMinMax = enabled;
	}

	public boolean isShowOnlyMinMaxEnabled() {
		return this.mShowOnlyMinMax;
	}

	public void setInverted(boolean enabled) {
		this.mInverted = enabled;
	}

	public boolean isInverted() {
		return this.mInverted;
	}

	public void setStartAtZero(boolean enabled) {
		this.mStartAtZero = enabled;
	}

	public boolean isStartAtZeroEnabled() {
		return this.mStartAtZero;
	}

	public float getAxisMinValue() {
		return this.mCustomAxisMin;
	}

	public void setAxisMinValue(float min) {
		this.mCustomAxisMin = min;
	}

	public void resetAxisMinValue() {
		this.mCustomAxisMin = (0.0F / 0.0F);
	}

	public float getAxisMaxValue() {
		return this.mCustomAxisMax;
	}

	public void setAxisMaxValue(float max) {
		this.mCustomAxisMax = max;
	}

	public void resetAxisMaxValue() {
		this.mCustomAxisMax = (0.0F / 0.0F);
	}

	public void setSpaceTop(float percent) {
		this.mSpacePercentTop = percent;
	}

	public float getSpaceTop() {
		return this.mSpacePercentTop;
	}

	public void setSpaceBottom(float percent) {
		this.mSpacePercentBottom = percent;
	}

	public float getSpaceBottom() {
		return this.mSpacePercentBottom;
	}

	public float getRequiredWidthSpace(Paint p) {
		p.setTextSize(this.mTextSize);

		String label = getLongestLabel();
		return Utils.calcTextWidth(p, label) + getXOffset() * 2.0F;
	}

	public float getRequiredHeightSpace(Paint p) {
		p.setTextSize(this.mTextSize);

		String label = getLongestLabel();
		return Utils.calcTextHeight(p, label) + getYOffset() * 2.0F;
	}

	public String getLongestLabel() {
		String longest = "";

		for (int i = 0; i < this.mEntries.length; i++) {
			String text = getFormattedLabel(i);

			if (longest.length() < text.length())
				longest = text;
		}
		return longest;
	}

	public String getFormattedLabel(int index) {
		if ((index < 0) || (index >= this.mEntries.length)) {
			return "";
		}
		return getValueFormatter().getFormattedValue(this.mEntries[index]);
	}

	public void setValueFormatter(ValueFormatter f) {
		if (f == null) {
			return;
		}
		this.mValueFormatter = f;
	}

	public ValueFormatter getValueFormatter() {
		return this.mValueFormatter;
	}

	public boolean needsDefaultFormatter() {
		if (this.mValueFormatter == null) {
			return true;
		}
		return (this.mValueFormatter instanceof DefaultValueFormatter);
	}

	public boolean needsOffset() {
		return (isEnabled()) && (isDrawLabelsEnabled())
				&& (getLabelPosition() == YAxisLabelPosition.OUTSIDE_CHART);
	}

	public static enum AxisDependency {
		LEFT, RIGHT;
	}

	public static enum YAxisLabelPosition {
		OUTSIDE_CHART, INSIDE_CHART;
	}
}
