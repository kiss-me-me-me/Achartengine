package com.sfpay.linechart.listener;

import android.view.MotionEvent;

public abstract interface OnChartGestureListener {
	public abstract void onChartLongPressed(MotionEvent paramMotionEvent);

	public abstract void onChartDoubleTapped(MotionEvent paramMotionEvent);

	public abstract void onChartSingleTapped(MotionEvent paramMotionEvent);

	public abstract void onChartFling(MotionEvent paramMotionEvent1,
			MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2);

	public abstract void onChartScale(MotionEvent paramMotionEvent,
			float paramFloat1, float paramFloat2);

	public abstract void onChartTranslate(MotionEvent paramMotionEvent,
			float paramFloat1, float paramFloat2);
}