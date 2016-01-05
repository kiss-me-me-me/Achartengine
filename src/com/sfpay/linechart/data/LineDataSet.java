package com.sfpay.linechart.data;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.graphics.DashPathEffect;

import com.sfpay.linechart.utils.Utils;

public class LineDataSet extends LineRadarDataSet<Entry> {
	private List<Integer> mCircleColors = null;
	private int mCircleColorHole = -1;
	private float mCircleSize = 8.0F;
	private float mCubicIntensity = 0.2F;
	private DashPathEffect mDashPathEffect = null;
	private boolean mDrawCircles = true;
	private boolean mDrawCubic = false;
	private boolean mDrawCircleHole = true;

	public LineDataSet(List<Entry> yVals, String label) {
		super(yVals, label);

		this.mCircleColors = new ArrayList();

		this.mCircleColors.add(Integer.valueOf(Color.rgb(140, 234, 255)));
	}

	public DataSet<Entry> copy() {
		return null;
	}

	public float getCubicIntensity() {
		return this.mCubicIntensity;
	}

	public void setCircleSize(float size) {
		this.mCircleSize = Utils.convertDpToPixel(size);
	}

	public float getCircleSize() {
		return this.mCircleSize;
	}

	public void enableDashedLine(float lineLength, float spaceLength,
			float phase) {
		this.mDashPathEffect = new DashPathEffect(new float[] { lineLength,
				spaceLength }, phase);
	}

	public boolean isDashedLineEnabled() {
		return this.mDashPathEffect != null;
	}

	public DashPathEffect getDashPathEffect() {
		return this.mDashPathEffect;
	}

	public void setDrawCircles(boolean enabled) {
		this.mDrawCircles = enabled;
	}

	public boolean isDrawCirclesEnabled() {
		return this.mDrawCircles;
	}

	public void setDrawCubic(boolean enabled) {
		this.mDrawCubic = enabled;
	}

	public boolean isDrawCubicEnabled() {
		return this.mDrawCubic;
	}

	public int getCircleColor(int index) {
		return ((Integer) this.mCircleColors.get(index
				% this.mCircleColors.size())).intValue();
	}

	public void setCircleColor(int color) {
		resetCircleColors();
		this.mCircleColors.add(Integer.valueOf(color));
	}

	public void resetCircleColors() {
		this.mCircleColors = new ArrayList();
	}

	public int getCircleHoleColor() {
		return this.mCircleColorHole;
	}

	public void setDrawCircleHole(boolean enabled) {
		this.mDrawCircleHole = enabled;
	}

	public boolean isDrawCircleHoleEnabled() {
		return this.mDrawCircleHole;
	}
}