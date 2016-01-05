//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.sfpay.linechart.listener;

import android.annotation.SuppressLint;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.sfpay.linechart.charts.BarLineChartBase;
import com.sfpay.linechart.data.BarLineScatterCandleData;
import com.sfpay.linechart.data.BarLineScatterCandleDataSet;
import com.sfpay.linechart.data.DataSet;
import com.sfpay.linechart.data.Entry;
import com.sfpay.linechart.utils.Highlight;
import com.sfpay.linechart.utils.ViewPortHandler;

public class BarLineChartTouchListener<T extends BarLineChartBase<? extends BarLineScatterCandleData<? extends BarLineScatterCandleDataSet<? extends Entry>>>>
		extends SimpleOnGestureListener implements OnTouchListener {
	private Matrix mMatrix = new Matrix();
	private Matrix mSavedMatrix = new Matrix();
	private PointF mTouchStartPoint = new PointF();
	private PointF mTouchPointCenter = new PointF();
	private static final int NONE = 0;
	private static final int DRAG = 1;
	private static final int X_ZOOM = 2;
	private static final int Y_ZOOM = 3;
	private static final int PINCH_ZOOM = 4;
	private static final int POST_ZOOM = 5;
	private int mTouchMode = 0;
	private float mSavedXDist = 1.0F;
	private float mSavedYDist = 1.0F;
	private float mSavedDist = 1.0F;
	private Highlight mLastHighlighted;
	private DataSet<?> mClosestDataSetToTouch;
	private T mChart;
	private GestureDetector mGestureDetector;
	private int lastXIndex = -1;

	public BarLineChartTouchListener(T chart, Matrix touchMatrix) {
		this.mChart = chart;
		this.mMatrix = touchMatrix;
		this.mGestureDetector = new GestureDetector(chart.getContext(), this);
	}

	@SuppressLint({ "ClickableViewAccessibility" })
	public boolean onTouch(View v, MotionEvent event) {
		if (this.mTouchMode == 0) {
			this.mGestureDetector.onTouchEvent(event);
		}

		if (!this.mChart.isDragEnabled() && !this.mChart.isScaleXEnabled()
				&& !this.mChart.isScaleYEnabled()) {
			return true;
		} else {
			switch (event.getAction() & 255) {
			case 0:
				this.saveTouchStart(event);
				break;
			case 1:
				this.mTouchMode = 0;
				this.mChart.enableScroll();
				break;
			case 2:
				if (this.mTouchMode == 1) {
					this.mChart.disableScroll();
					if (this.mChart.isDragEnabled()) {
						this.performDrag(event);
					}
				} else if (this.mTouchMode != 2 && this.mTouchMode != 3
						&& this.mTouchMode != 4) {
					if (this.mTouchMode == 0
							&& Math.abs(distance(event.getX(),
									this.mTouchStartPoint.x, event.getY(),
									this.mTouchStartPoint.y)) > 5.0F) {
						if (this.mChart.hasNoDragOffset()) {
							if (!this.mChart.isFullyZoomedOut()) {
								this.mTouchMode = 1;
							}
						} else {
							this.mTouchMode = 1;
						}
					}
				} else {
					this.mChart.disableScroll();
					if (this.mChart.isScaleXEnabled()
							|| this.mChart.isScaleYEnabled()) {
						this.performZoom(event);
					}
				}
			case 3:
			case 4:
			default:
				break;
			case 5:
				if (event.getPointerCount() >= 2) {
					this.mChart.disableScroll();
					this.saveTouchStart(event);
					this.mSavedXDist = getXDist(event);
					this.mSavedYDist = getYDist(event);
					this.mSavedDist = spacing(event);
					if (this.mSavedDist > 10.0F) {
						if (this.mChart.isPinchZoomEnabled()) {
							this.mTouchMode = 4;
						} else if (this.mSavedXDist > this.mSavedYDist) {
							this.mTouchMode = 2;
						} else {
							this.mTouchMode = 3;
						}
					}

					midPoint(this.mTouchPointCenter, event);
				}
				break;
			case 6:
				this.mTouchMode = 5;
			}

			this.mMatrix = this.mChart.getViewPortHandler().refresh(
					this.mMatrix, this.mChart, true);
			return true;
		}
	}

	private void saveTouchStart(MotionEvent event) {
		this.mSavedMatrix.set(this.mMatrix);
		this.mTouchStartPoint.set(event.getX(), event.getY());
		this.mClosestDataSetToTouch = this.mChart.getDataSetByTouchPoint(
				event.getX(), event.getY());
	}

	private void performDrag(MotionEvent event) {
		this.mMatrix.set(this.mSavedMatrix);
		OnChartGestureListener l = this.mChart.getOnChartGestureListener();
		float dX;
		float dY;
		if (this.mChart.isAnyAxisInverted()
				&& this.mClosestDataSetToTouch != null
				&& this.mChart.getAxis(
						this.mClosestDataSetToTouch.getAxisDependency())
						.isInverted()) {
			dX = event.getX() - this.mTouchStartPoint.x;
			dY = -(event.getY() - this.mTouchStartPoint.y);
		} else {
			dX = event.getX() - this.mTouchStartPoint.x;
			dY = event.getY() - this.mTouchStartPoint.y;
		}

		this.mMatrix.postTranslate(dX, dY);
		if (l != null) {
			l.onChartTranslate(event, dX, dY);
		}

	}

	private void performZoom(MotionEvent event) {
		if (event.getPointerCount() >= 2) {
			OnChartGestureListener l = this.mChart.getOnChartGestureListener();
			float totalDist = spacing(event);
			if (totalDist > 10.0F) {
				PointF t = this.getTrans(this.mTouchPointCenter.x,
						this.mTouchPointCenter.y);
				float yDist;
				float scaleY;
				if (this.mTouchMode == 4) {
					yDist = totalDist / this.mSavedDist;
					scaleY = this.mChart.isScaleXEnabled() ? yDist : 1.0F;
					float scaleY1 = this.mChart.isScaleYEnabled() ? yDist
							: 1.0F;
					this.mMatrix.set(this.mSavedMatrix);
					this.mMatrix.postScale(scaleY, scaleY1, t.x, t.y);
					if (l != null) {
						l.onChartScale(event, scaleY, scaleY1);
					}
				} else if (this.mTouchMode == 2
						&& this.mChart.isScaleXEnabled()) {
					yDist = getXDist(event);
					scaleY = yDist / this.mSavedXDist;
					this.mMatrix.set(this.mSavedMatrix);
					this.mMatrix.postScale(scaleY, 1.0F, t.x, t.y);
					if (l != null) {
						l.onChartScale(event, scaleY, 1.0F);
					}
				} else if (this.mTouchMode == 3
						&& this.mChart.isScaleYEnabled()) {
					yDist = getYDist(event);
					scaleY = yDist / this.mSavedYDist;
					this.mMatrix.set(this.mSavedMatrix);
					this.mMatrix.postScale(1.0F, scaleY, t.x, t.y);
					if (l != null) {
						l.onChartScale(event, 1.0F, scaleY);
					}
				}
			}
		}

	}

	private void performHighlight(MotionEvent e) {
		Highlight h = this.mChart.getHighlightByTouchPoint(e.getX(), e.getY());
		if (h != null) {
			if (this.lastXIndex == h.getXIndex()) {
				return;
			}

			this.lastXIndex = h.getXIndex();
		}

		if (h != null && !h.equalTo(this.mLastHighlighted)) {
			this.mLastHighlighted = h;
			this.mChart.highlightTouch(h);
		} else {
			this.mChart.highlightTouch((Highlight) null);
			this.mLastHighlighted = null;
		}

	}

	private static float distance(float eventX, float startX, float eventY,
			float startY) {
		float dx = eventX - startX;
		float dy = eventY - startY;
		return (float) Math.sqrt((double) (dx * dx + dy * dy));
	}

	private static void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2.0F, y / 2.0F);
	}

	private static float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return (float) Math.sqrt((double) (x * x + y * y));
	}

	private static float getXDist(MotionEvent e) {
		float x = Math.abs(e.getX(0) - e.getX(1));
		return x;
	}

	private static float getYDist(MotionEvent e) {
		float y = Math.abs(e.getY(0) - e.getY(1));
		return y;
	}

	public PointF getTrans(float x, float y) {
		ViewPortHandler vph = this.mChart.getViewPortHandler();
		float xTrans = x - vph.offsetLeft();
		float yTrans = 0.0F;
		if (this.mChart.isAnyAxisInverted()
				&& this.mClosestDataSetToTouch != null
				&& this.mChart.isInverted(this.mClosestDataSetToTouch
						.getAxisDependency())) {
			yTrans = -(y - vph.offsetTop());
		} else {
			yTrans = -((float) this.mChart.getMeasuredHeight() - y - vph
					.offsetBottom());
		}

		return new PointF(xTrans, yTrans);
	}

	public boolean onDoubleTap(MotionEvent e) {
		return super.onDoubleTap(e);
	}

	public void onLongPress(MotionEvent e) {
	}

	public boolean onSingleTapUp(MotionEvent e) {
		this.performHighlight(e);
		return super.onSingleTapUp(e);
	}

	public boolean onSingleTapConfirmed(MotionEvent e) {
		OnChartGestureListener l = this.mChart.getOnChartGestureListener();
		if (l != null) {
			l.onChartSingleTapped(e);
		}

		return super.onSingleTapConfirmed(e);
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return super.onFling(e1, e2, velocityX, velocityY);
	}
}
