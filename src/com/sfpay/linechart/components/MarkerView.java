//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.sfpay.linechart.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sfpay.linechart.data.Entry;
import com.sfpay.linechart.utils.Utils;

@SuppressLint({ "ViewConstructor" })
public class MarkerView extends RelativeLayout {
	private TextView floatFrame;

	public MarkerView(Context context, int layoutResource, int id) {
		super(context);
		this.setupLayoutResource(layoutResource);
		this.floatFrame = (TextView) this.findViewById(id);
	}

	private void setupLayoutResource(int layoutResource) {
		View inflated = LayoutInflater.from(this.getContext()).inflate(
				layoutResource, this);
		inflated.setLayoutParams(new LayoutParams(-2, -2));
		inflated.measure(MeasureSpec.makeMeasureSpec(0, 0),
				MeasureSpec.makeMeasureSpec(0, 0));
		inflated.layout(0, 0, inflated.getMeasuredWidth(),
				inflated.getMeasuredHeight());
	}

	public void draw(Canvas canvas, float posx, float posy, int position) {
		posx += (float) (position == 0 ? position : this.getXOffset());
		posy += (float) this.getYOffset();
		canvas.translate(posx, posy);
		this.draw(canvas);
		canvas.translate(-posx, -posy);
	}

	public void refreshContent(Entry e, int dataSetIndex) {
		this.floatFrame.setText(Utils.formatNumber(e.getVal(), 3, true));
		Log.d("TAG", "e.getVal() : " + Utils.formatNumber(e.getVal(), 3, true));
	}

	public int getXOffset() {
		return -(this.getWidth() / 2);
	}

	public int getYOffset() {
		return -this.getHeight();
	}
}
