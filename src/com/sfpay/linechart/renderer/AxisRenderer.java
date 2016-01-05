//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.sfpay.linechart.renderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;

import com.sfpay.linechart.utils.Transformer;
import com.sfpay.linechart.utils.ViewPortHandler;

public abstract class AxisRenderer extends Renderer {
	protected Transformer mTrans;
	protected Paint mGridPaint;
	protected Paint mAxisLabelPaint;
	protected Paint mAxisLinePaint;
	protected Paint mLimitLinePaint;

	public AxisRenderer(ViewPortHandler viewPortHandler, Transformer trans) {
		super(viewPortHandler);
		this.mTrans = trans;
		this.mAxisLabelPaint = new Paint(1);
		this.mGridPaint = new Paint();
		this.mGridPaint.setColor(-7829368);
		this.mGridPaint.setStrokeWidth(1.0F);
		this.mGridPaint.setStyle(Style.STROKE);
		this.mGridPaint.setAlpha(90);
		this.mAxisLinePaint = new Paint();
		this.mAxisLinePaint.setColor(-16777216);
		this.mAxisLinePaint.setStrokeWidth(1.0F);
		this.mAxisLinePaint.setStyle(Style.STROKE);
		this.mLimitLinePaint = new Paint(1);
		this.mLimitLinePaint.setStyle(Style.STROKE);
	}

	public Paint getPaintAxisLabels() {
		return this.mAxisLabelPaint;
	}

	public Paint getPaintGrid() {
		return this.mGridPaint;
	}

	public Paint getPaintAxisLine() {
		return this.mAxisLinePaint;
	}

	public Transformer getTransformer() {
		return this.mTrans;
	}

	public abstract void renderAxisLabels(Canvas var1);

	public abstract void renderGridLines(Canvas var1);

	public abstract void renderAxisLine(Canvas var1);

	public abstract void renderLimitLines(Canvas var1);
}
