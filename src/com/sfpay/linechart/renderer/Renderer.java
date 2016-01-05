package com.sfpay.linechart.renderer;

import com.sfpay.linechart.utils.Transformer;
import com.sfpay.linechart.utils.ViewPortHandler;

public abstract class Renderer {
	protected ViewPortHandler mViewPortHandler;
	protected int mMinX = 0;

	protected int mMaxX = 0;

	public Renderer(ViewPortHandler viewPortHandler) {
		this.mViewPortHandler = viewPortHandler;
	}

	protected void calcXBounds(Transformer trans) {
		double minx = trans.getValuesByTouchPoint(
				this.mViewPortHandler.contentLeft(), 0.0F).x;
		double maxx = trans.getValuesByTouchPoint(
				this.mViewPortHandler.contentRight(), 0.0F).x;

		if (!Double.isInfinite(minx))
			this.mMinX = (int) minx;
		if (!Double.isInfinite(maxx))
			this.mMaxX = (int) Math.ceil(maxx);
	}
}
