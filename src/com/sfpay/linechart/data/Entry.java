package com.sfpay.linechart.data;

public class Entry {
	private float mVal = 0.0F;
	private int mXIndex = 0;
	private Object mData = null;

	public Entry(float val, int xIndex) {
		this.mVal = val;
		this.mXIndex = xIndex;
	}

	public int getXIndex() {
		return this.mXIndex;
	}

	public float getVal() {
		return this.mVal;
	}

	public boolean equalTo(Entry e) {
		if (e == null) {
			return false;
		}
		if (e.mData != this.mData)
			return false;
		if (e.mXIndex != this.mXIndex) {
			return false;
		}
		return Math.abs(e.mVal - this.mVal) <= 1.0E-005F;
	}
}