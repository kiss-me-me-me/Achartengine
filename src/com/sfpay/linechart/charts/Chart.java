package com.sfpay.linechart.charts;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.sfpay.linechart.components.Legend;
import com.sfpay.linechart.components.MarkerView;
import com.sfpay.linechart.data.ChartData;
import com.sfpay.linechart.data.DataSet;
import com.sfpay.linechart.data.Entry;
import com.sfpay.linechart.data.LineDataSet;
import com.sfpay.linechart.interfaces.ChartInterface;
import com.sfpay.linechart.listener.OnChartGestureListener;
import com.sfpay.linechart.listener.OnChartValueSelectedListener;
import com.sfpay.linechart.renderer.DataRenderer;
import com.sfpay.linechart.renderer.LegendRenderer;
import com.sfpay.linechart.utils.DefaultValueFormatter;
import com.sfpay.linechart.utils.Highlight;
import com.sfpay.linechart.utils.Utils;
import com.sfpay.linechart.utils.ValueFormatter;
import com.sfpay.linechart.utils.ViewPortHandler;

@SuppressLint({ "NewApi" })
public abstract class Chart<T extends ChartData<? extends DataSet<? extends Entry>>>
		extends ViewGroup implements ChartInterface {
	protected boolean mLogEnabled = false;

	protected T mData = null;
	protected ValueFormatter mDefaultFormatter;
	protected Paint mDescPaint;
	protected Paint mInfoPaint;
	protected String mDescription = "Description";

	protected boolean mDataNotSet = true;

	protected boolean mDrawUnitInChart = false;

	protected float mDeltaX = 1.0F;

	protected float mXChartMin = 0.0F;
	protected float mXChartMax = 0.0F;

	protected boolean mTouchEnabled = false;

	protected boolean mHighlightEnabled = true;
	protected Legend mLegend;
	protected OnChartValueSelectedListener mSelectionListener;
	private OnChartGestureListener mGestureListener;
	protected LegendRenderer mLegendRenderer;
	protected DataRenderer mRenderer;
	protected ViewPortHandler mViewPortHandler;
	private boolean mOffsetsCalculated = false;
	protected Paint mDrawPaint;
	private PointF mDescriptionPosition;
	protected Highlight[] mIndicesToHightlight = new Highlight[0];

	protected boolean mDrawMarkerViews = true;
	protected MarkerView mMarkerView;
	public static final int PAINT_GRID_BACKGROUND = 4;
	public static final int PAINT_INFO = 7;
	public static final int PAINT_DESCRIPTION = 11;
	public static final int PAINT_HOLE = 13;
	public static final int PAINT_CENTER_TEXT = 14;
	public static final int PAINT_LEGEND_LABEL = 18;
	protected ArrayList<Runnable> mJobs = new ArrayList();

	public Chart(Context context) {
		super(context);
		init();
	}

	public Chart(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public Chart(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	protected void init() {
		setWillNotDraw(false);

		Utils.init(getContext().getResources());

		this.mDefaultFormatter = new DefaultValueFormatter(1);

		this.mViewPortHandler = new ViewPortHandler();
		this.mLegend = new Legend();

		this.mLegendRenderer = new LegendRenderer(this.mViewPortHandler,
				this.mLegend);

		this.mDescPaint = new Paint(1);
		this.mDescPaint.setColor(-16777216);
		this.mDescPaint.setTextAlign(Paint.Align.RIGHT);
		this.mDescPaint.setTextSize(Utils.convertDpToPixel(9.0F));

		this.mInfoPaint = new Paint(1);
		this.mInfoPaint.setColor(Color.rgb(247, 189, 51));
		this.mInfoPaint.setTextAlign(Paint.Align.CENTER);
		this.mInfoPaint.setTextSize(Utils.convertDpToPixel(12.0F));

		this.mDrawPaint = new Paint(4);
	}

	public void setData(T data) {
		if (data == null) {
			return;
		}

		this.mDataNotSet = false;
		this.mOffsetsCalculated = false;
		this.mData = data;

		calculateFormatter(data.getYMin(), data.getYMax());

		for (DataSet set : this.mData.getDataSets()) {
			if (set.needsDefaultFormatter()) {
				set.setValueFormatter(this.mDefaultFormatter);
			}
		}
		notifyDataSetChanged();
	}

	public void clear() {
		this.mData = null;
		this.mDataNotSet = true;
		invalidate();
	}

	public void clearValues() {
		this.mData.clearValues();
		invalidate();
	}

	public abstract void notifyDataSetChanged();

	protected abstract void calculateOffsets();

	protected abstract void calcMinMax();

	protected void calculateFormatter(float min, float max) {
		float reference = 0.0F;

		if ((this.mData == null) || (this.mData.getXValCount() < 2)) {
			reference = Math.max(Math.abs(min), Math.abs(max));
		} else
			reference = Math.abs(max - min);

		int digits = Utils.getDecimals(reference);
		this.mDefaultFormatter = new DefaultValueFormatter(digits);
	}

	protected void onDraw(Canvas canvas) {
		if (!this.mOffsetsCalculated) {
			calculateOffsets();
			this.mOffsetsCalculated = true;
		}
	}

	protected void drawDescription(Canvas c) {
		if (!this.mDescription.equals("")) {
			if (this.mDescriptionPosition == null) {
				c.drawText(this.mDescription, getWidth()
						- this.mViewPortHandler.offsetRight() - 10.0F,
						getHeight() - this.mViewPortHandler.offsetBottom()
								- 10.0F, this.mDescPaint);
			} else
				c.drawText(this.mDescription, this.mDescriptionPosition.x,
						this.mDescriptionPosition.y, this.mDescPaint);
		}
	}

	public boolean valuesToHighlight() {
		return (this.mIndicesToHightlight != null)
				&& (this.mIndicesToHightlight.length > 0)
				&& (this.mIndicesToHightlight[0] != null);
	}

	public void highlightTouch(Highlight high) {
		if (high == null) {
			this.mIndicesToHightlight = null;
		} else {
			this.mIndicesToHightlight = new Highlight[] { high };
		}

		invalidate();

		if (this.mSelectionListener != null) {
			if (!valuesToHighlight()) {
				this.mSelectionListener.onNothingSelected();
			} else {
				Entry e = this.mData.getEntryForHighlight(high);

				this.mSelectionListener.onValueSelected(e,
						high.getDataSetIndex(), high);
			}
		}
	}

	protected void drawMarkers(Canvas canvas) {
		LineDataSet lineDataSet = (LineDataSet) this.mData.getDataSets().get(0);
		float yCircleSize = lineDataSet.getCircleSize();

		Paint renderPaint = this.mRenderer.getPaintRender();
		renderPaint.setStyle(Paint.Style.FILL);

		if ((this.mMarkerView == null) || (!this.mDrawMarkerViews)
				|| (!valuesToHighlight())) {
			List entries = ((DataSet) this.mData.getDataSets().get(0))
					.getYVals();
			Entry e = (Entry) entries.get(entries.size() - 1);

			int dataSetIndex = 0;

			float[] pos = getMarkerPosition(e, dataSetIndex);

			this.mMarkerView.refreshContent(e, dataSetIndex);

			this.mMarkerView.measure(View.MeasureSpec.makeMeasureSpec(0, 0),
					View.MeasureSpec.makeMeasureSpec(0, 0));
			this.mMarkerView.layout(0, 0, this.mMarkerView.getMeasuredWidth(),
					this.mMarkerView.getMeasuredHeight());

			if (pos[1] - this.mMarkerView.getHeight() <= 0.0F) {
				float y = this.mMarkerView.getHeight() - pos[1];
				this.mMarkerView.draw(canvas, pos[0], pos[1] + y,
						entries.size() - 1);
			} else {
				this.mMarkerView.draw(canvas, pos[0], pos[1]
						- (yCircleSize + 1.0F), entries.size() - 1);
			}

			int circleColor = lineDataSet.getCircleColor(0);
			renderPaint.setColor(circleColor);

			canvas.drawCircle(pos[0], pos[1], yCircleSize, renderPaint);

			renderPaint.setColor(lineDataSet.getCircleHoleColor());

			if (lineDataSet.isDrawCircleHoleEnabled())
				canvas.drawCircle(pos[0], pos[1], yCircleSize / 2.0F,
						renderPaint);
			return;
		}
		for (int i = 0; i < this.mIndicesToHightlight.length; i++) {
			int xIndex = this.mIndicesToHightlight[i].getXIndex();
			int dataSetIndex = this.mIndicesToHightlight[i].getDataSetIndex();
			if (xIndex > this.mDeltaX)
				continue;
			Entry e = this.mData
					.getEntryForHighlight(this.mIndicesToHightlight[i]);

			if (e == null) {
				continue;
			}
			float[] pos = getMarkerPosition(e, dataSetIndex);

			if (!this.mViewPortHandler.isInBounds(pos[0], pos[1])) {
				continue;
			}
			this.mMarkerView.refreshContent(e, dataSetIndex);

			this.mMarkerView.measure(View.MeasureSpec.makeMeasureSpec(0, 0),
					View.MeasureSpec.makeMeasureSpec(0, 0));
			this.mMarkerView.layout(0, 0, this.mMarkerView.getMeasuredWidth(),
					this.mMarkerView.getMeasuredHeight());

			if (pos[1] - this.mMarkerView.getHeight() <= 0.0F) {
				float y = this.mMarkerView.getHeight() - pos[1];
				this.mMarkerView.draw(canvas, pos[0], pos[1] + y, xIndex);
			} else {
				this.mMarkerView.draw(canvas, pos[0], pos[1]
						- (yCircleSize + 1.0F), xIndex);
			}

			int circleColor = lineDataSet.getCircleColor(0);
			renderPaint.setColor(circleColor);

			canvas.drawCircle(pos[0], pos[1], yCircleSize, renderPaint);

			renderPaint.setColor(lineDataSet.getCircleHoleColor());

			if (lineDataSet.isDrawCircleHoleEnabled())
				canvas.drawCircle(pos[0], pos[1], yCircleSize / 2.0F,
						renderPaint);
		}
	}

	protected abstract float[] getMarkerPosition(Entry paramEntry, int paramInt);

	public void setOnChartGestureListener(OnChartGestureListener l) {
		this.mGestureListener = l;
	}

	public OnChartGestureListener getOnChartGestureListener() {
		return this.mGestureListener;
	}

	public void setHighlightEnabled(boolean enabled) {
		this.mHighlightEnabled = enabled;
	}

	public boolean isHighlightEnabled() {
		return this.mHighlightEnabled;
	}

	public float getXChartMax() {
		return this.mXChartMax;
	}

	public void setDescription(String desc) {
		if (desc == null)
			desc = "";
		this.mDescription = desc;
	}

	public void setTouchEnabled(boolean enabled) {
		this.mTouchEnabled = enabled;
	}

	public void setMarkerView(MarkerView v) {
		this.mMarkerView = v;
	}

	public MarkerView getMarkerView() {
		return this.mMarkerView;
	}

	public void disableScroll() {
		ViewParent parent = getParent();
		if (parent != null)
			parent.requestDisallowInterceptTouchEvent(true);
	}

	public void enableScroll() {
		ViewParent parent = getParent();
		if (parent != null)
			parent.requestDisallowInterceptTouchEvent(false);
	}

	public void setPaint(Paint p, int which) {
		switch (which) {
		case 7:
			this.mInfoPaint = p;
			break;
		case 11:
			this.mDescPaint = p;
		case 8:
		case 9:
		case 10:
		}
	}

	public Paint getPaint(int which) {
		switch (which) {
		case 7:
			return this.mInfoPaint;
		case 11:
			return this.mDescPaint;
		case 8:
		case 9:
		case 10:
		}
		return null;
	}

	public String getXValue(int index) {
		if ((this.mData == null) || (this.mData.getXValCount() <= index)) {
			return null;
		}
		return (String) this.mData.getXVals().get(index);
	}

	public T getData() {
		return this.mData;
	}

	public ViewPortHandler getViewPortHandler() {
		return this.mViewPortHandler;
	}

	public Bitmap getChartBitmap() {
		Bitmap returnedBitmap = Bitmap.createBitmap(getWidth(), getHeight(),
				Bitmap.Config.RGB_565);

		Canvas canvas = new Canvas(returnedBitmap);

		Drawable bgDrawable = getBackground();
		if (bgDrawable != null) {
			bgDrawable.draw(canvas);
		} else {
			canvas.drawColor(-1);
		}
		draw(canvas);

		return returnedBitmap;
	}

	public boolean saveToPath(String title, String pathOnSD) {
		Bitmap b = getChartBitmap();

		OutputStream stream = null;
		try {
			stream = new FileOutputStream(Environment
					.getExternalStorageDirectory().getPath()
					+ pathOnSD
					+ "/"
					+ title + ".png");

			b.compress(Bitmap.CompressFormat.PNG, 40, stream);

			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		for (int i = 0; i < getChildCount(); i++)
			getChildAt(i).layout(left, top, right, bottom);
	}

	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		if ((w > 0) && (h > 0) && (w < 10000) && (h < 10000)) {
			this.mViewPortHandler.setChartDimens(w, h);

			for (Runnable r : this.mJobs) {
				post(r);
			}

			this.mJobs.clear();
		}
		notifyDataSetChanged();

		super.onSizeChanged(w, h, oldw, oldh);
	}
}

/*
 * Location: C:\Users\xiefeng\Desktop\sfpay-linechart.jar Qualified Name:
 * com.sfpay.linechart.charts.Chart JD-Core Version: 0.6.0
 */