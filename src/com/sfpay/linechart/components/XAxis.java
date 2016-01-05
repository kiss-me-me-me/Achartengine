//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.sfpay.linechart.components;

import java.util.ArrayList;
import java.util.List;

public class XAxis extends AxisBase {
	protected List<String> mValues = new ArrayList();
	public int mLabelWidth = 1;
	public int mLabelHeight = 1;
	private int mSpaceBetweenLabels = 4;
	public int mAxisLabelModulus = 1;
	public int mYAxisLabelModulus = 1;
	private boolean mAvoidFirstLastClipping = false;
	protected boolean mAdjustXAxisLabels = true;
	private XAxis.XAxisPosition mPosition;

	public XAxis() {
		this.mPosition = XAxis.XAxisPosition.BOTTOM;
	}

	public void setAdjustXLabels(boolean enabled) {
		this.mAdjustXAxisLabels = enabled;
	}

	public boolean isAdjustXLabelsEnabled() {
		return this.mAdjustXAxisLabels;
	}

	public XAxis.XAxisPosition getPosition() {
		return this.mPosition;
	}

	public void setPosition(XAxis.XAxisPosition pos) {
		this.mPosition = pos;
	}

	public void setSpaceBetweenLabels(int space) {
		this.mSpaceBetweenLabels = space;
	}

	public int getSpaceBetweenLabels() {
		return this.mSpaceBetweenLabels;
	}

	public void setAvoidFirstLastClipping(boolean enabled) {
		this.mAvoidFirstLastClipping = enabled;
	}

	public boolean isAvoidFirstLastClippingEnabled() {
		return this.mAvoidFirstLastClipping;
	}

	public void setValues(List<String> values) {
		this.mValues = values;
	}

	public List<String> getValues() {
		return this.mValues;
	}

	public String getLongestLabel() {
		String longest = "";

		for (int i = 0; i < this.mValues.size(); ++i) {
			String text = (String) this.mValues.get(i);
			if (longest.length() < text.length()) {
				longest = text;
			}
		}

		return longest;
	}

	public static enum XAxisPosition {
		TOP, BOTTOM, BOTH_SIDED, TOP_INSIDE, BOTTOM_INSIDE;

		private XAxisPosition() {
		}
	}
}
