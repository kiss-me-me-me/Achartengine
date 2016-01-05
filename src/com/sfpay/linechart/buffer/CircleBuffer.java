package com.sfpay.linechart.buffer;

import com.sfpay.linechart.data.Entry;
import java.util.List;

public class CircleBuffer extends AbstractBuffer<Entry> {
	public CircleBuffer(int size) {
		super(size);
	}

	protected void addCircle(float x, float y) {
		this.buffer[(this.index++)] = x;
		this.buffer[(this.index++)] = y;
	}

	public void feed(List<Entry> entries) {
		int size = (int) Math.ceil((this.mTo - this.mFrom) * this.phaseX
				+ this.mFrom);

		for (int i = this.mFrom; i < size; i++) {
			Entry e = (Entry) entries.get(i);
			addCircle(e.getXIndex(), e.getVal() * this.phaseY);
		}
		reset();
	}
}

/*
 * Location: C:\Users\xiefeng\Desktop\sfpay-linechart.jar Qualified Name:
 * com.sfpay.linechart.buffer.CircleBuffer JD-Core Version: 0.6.0
 */