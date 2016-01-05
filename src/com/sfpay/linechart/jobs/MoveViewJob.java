//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.sfpay.linechart.jobs;

import android.view.View;

import com.sfpay.linechart.utils.Transformer;
import com.sfpay.linechart.utils.ViewPortHandler;

public class MoveViewJob implements Runnable {
	protected ViewPortHandler mViewPortHandler;
	protected float xIndex = 0.0F;
	protected float yValue = 0.0F;
	protected Transformer mTrans;
	protected View view;

	public MoveViewJob(ViewPortHandler viewPortHandler, float xIndex,
			float yValue, Transformer trans, View v) {
		this.mViewPortHandler = viewPortHandler;
		this.xIndex = xIndex;
		this.yValue = yValue;
		this.mTrans = trans;
		this.view = v;
	}

	public void run() {
		float[] pts = new float[] { this.xIndex, this.yValue };
		this.mTrans.pointValuesToPixel(pts);
		this.mViewPortHandler.centerViewPort(pts, this.view);
	}
}
