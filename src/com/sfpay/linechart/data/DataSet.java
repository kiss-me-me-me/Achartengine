//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.sfpay.linechart.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.graphics.Color;
import android.graphics.Typeface;

import com.sfpay.linechart.components.YAxis.AxisDependency;
import com.sfpay.linechart.utils.DefaultValueFormatter;
import com.sfpay.linechart.utils.Utils;
import com.sfpay.linechart.utils.ValueFormatter;

public abstract class DataSet<T extends Entry> {
	protected List<Integer> mColors = null;
	public List<T> mYVals = null;
	protected float mYMax = 0.0F;
	protected float mYMin = 0.0F;
	private float mYValueSum = 0.0F;
	private String mLabel = "DataSet";
	private boolean mVisible = true;
	protected boolean mDrawValues = true;
	private int mValueColor = -16777216;
	private float mValueTextSize = 17.0F;
	private Typeface mValueTypeface;
	protected ValueFormatter mValueFormatter;
	protected AxisDependency mAxisDependency;

	public DataSet(List<T> yVals, String label) {
		this.mAxisDependency = AxisDependency.LEFT;
		this.mLabel = label;
		this.mYVals = yVals;
		if (this.mYVals == null) {
			this.mYVals = new ArrayList();
		}

		this.mColors = new ArrayList();
		this.mColors.add(Integer.valueOf(Color.rgb(140, 234, 255)));
		this.calcMinMax();
		this.calcYValueSum();
	}

	protected void calcMinMax() {
		if (this.mYVals.size() != 0) {
			this.mYMin = ((Entry) this.mYVals.get(0)).getVal();
			this.mYMax = ((Entry) this.mYVals.get(0)).getVal();

			for (int i = 0; i < this.mYVals.size(); ++i) {
				Entry e = (Entry) this.mYVals.get(i);
				if (e != null) {
					if (e.getVal() < this.mYMin) {
						this.mYMin = e.getVal();
					}

					if (e.getVal() > this.mYMax) {
						this.mYMax = e.getVal();
					}
				}
			}

		}
	}

	private void calcYValueSum() {
		this.mYValueSum = 0.0F;

		for (int i = 0; i < this.mYVals.size(); ++i) {
			Entry e = (Entry) this.mYVals.get(i);
			if (e != null) {
				this.mYValueSum += Math.abs(e.getVal());
			}
		}

	}

	public int getEntryCount() {
		return this.mYVals.size();
	}

	public float getYValForXIndex(int xIndex) {
		Entry e = this.getEntryForXIndex(xIndex);
		return (float) (e != null ? e.getVal() : 0.0F / 0.0);
	}

	public T getEntryForXIndex(int x) {
		int low = 0;
		int high = this.mYVals.size() - 1;

		Entry closest;
		int m;
		for (closest = null; low <= high; closest = (Entry) this.mYVals.get(m)) {
			m = (high + low) / 2;
			if (x == ((Entry) this.mYVals.get(m)).getXIndex()) {
				while (m > 0
						&& ((Entry) this.mYVals.get(m - 1)).getXIndex() == x) {
					--m;
				}

				return (T) this.mYVals.get(m);
			}

			if (x > ((Entry) this.mYVals.get(m)).getXIndex()) {
				low = m + 1;
			} else {
				high = m - 1;
			}
		}

		return (T) closest;
	}

	public List<T> getYVals() {
		return this.mYVals;
	}

	public float getYValueSum() {
		return this.mYValueSum;
	}

	public float getYMin() {
		return this.mYMin;
	}

	public float getYMax() {
		return this.mYMax;
	}

	public abstract DataSet<T> copy();

	public String getLabel() {
		return this.mLabel;
	}

	public boolean isVisible() {
		return this.mVisible;
	}

	public AxisDependency getAxisDependency() {
		return this.mAxisDependency;
	}

	public void setDrawValues(boolean enabled) {
		this.mDrawValues = enabled;
	}

	public boolean isDrawValuesEnabled() {
		return this.mDrawValues;
	}

	public boolean removeEntry(T e) {
		if (e == null) {
			return false;
		} else {
			boolean removed = this.mYVals.remove(e);
			if (removed) {
				float val = e.getVal();
				this.mYValueSum -= val;
				this.calcMinMax();
			}

			return removed;
		}
	}

	public boolean removeEntry(int xIndex) {
		Entry e = this.getEntryForXIndex(xIndex);
		return this.removeEntry((T) e);
	}

	public void setColor(int color) {
		this.resetColors();
		this.mColors.add(Integer.valueOf(color));
	}

	public List<Integer> getColors() {
		return this.mColors;
	}

	public int getColor(int index) {
		return ((Integer) this.mColors.get(index % this.mColors.size()))
				.intValue();
	}

	public int getColor() {
		return ((Integer) this.mColors.get(0)).intValue();
	}

	public void resetColors() {
		this.mColors = new ArrayList();
	}

	public int getEntryPosition(Entry e) {
		for (int i = 0; i < this.mYVals.size(); ++i) {
			if (e.equalTo((Entry) this.mYVals.get(i))) {
				return i;
			}
		}

		return -1;
	}

	public void setValueFormatter(ValueFormatter f) {
		if (f != null) {
			this.mValueFormatter = f;
		}
	}

	public boolean needsDefaultFormatter() {
		return this.mValueFormatter == null ? true
				: this.mValueFormatter instanceof DefaultValueFormatter;
	}

	public void setValueTextColor(int color) {
		this.mValueColor = color;
	}

	public int getValueTextColor() {
		return this.mValueColor;
	}

	public void setValueTypeface(Typeface tf) {
		this.mValueTypeface = tf;
	}

	public Typeface getValueTypeface() {
		return this.mValueTypeface;
	}

	public void setValueTextSize(float size) {
		this.mValueTextSize = Utils.convertDpToPixel(size);
	}

	public float getValueTextSize() {
		return this.mValueTextSize;
	}

	public boolean contains(Entry e) {
		Iterator var3 = this.mYVals.iterator();

		while (var3.hasNext()) {
			Entry entry = (Entry) var3.next();
			if (entry.equals(e)) {
				return true;
			}
		}

		return false;
	}
}
