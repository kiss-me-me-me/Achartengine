//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.sfpay.linechart.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;

import com.sfpay.linechart.data.DataSet;
import com.sfpay.linechart.utils.Highlight;
import com.sfpay.linechart.utils.Utils;
import com.sfpay.linechart.utils.ViewPortHandler;

public abstract class DataRenderer extends Renderer {
	protected Paint mRenderPaint = new Paint(1);
	protected Paint mHighlightPaint;
	protected Paint mDrawPaint;
	protected Paint mValuePaint;

	public DataRenderer(ViewPortHandler viewPortHandler) {
		super(viewPortHandler);
		this.mRenderPaint.setStyle(Style.FILL);
		this.mDrawPaint = new Paint(4);
		this.mValuePaint = new Paint(1);
		this.mValuePaint.setColor(Color.rgb(63, 63, 63));
		this.mValuePaint.setTextAlign(Align.CENTER);
		this.mValuePaint.setTextSize(Utils.convertDpToPixel(9.0F));
		this.mHighlightPaint = new Paint(1);
		this.mHighlightPaint.setStyle(Style.STROKE);
		this.mHighlightPaint.setStrokeWidth(2.0F);
		this.mHighlightPaint.setColor(Color.rgb(255, 187, 115));
	}

	public Paint getPaintRender() {
		return this.mRenderPaint;
	}

	protected void applyValueTextStyle(DataSet<?> set) {
		this.mValuePaint.setColor(set.getValueTextColor());
		this.mValuePaint.setTypeface(set.getValueTypeface());
		this.mValuePaint.setTextSize(set.getValueTextSize());
	}

	public abstract void initBuffers();

	public abstract void drawData(Canvas var1);

	public abstract void drawValues(Canvas var1);

	public abstract void drawExtras(Canvas var1);

	public abstract void drawHighlighted(Canvas var1, Highlight[] var2);
}
