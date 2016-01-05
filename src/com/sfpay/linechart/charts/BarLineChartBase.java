//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.sfpay.linechart.charts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnDrawListener;
import com.sfpay.linechart.charts.Chart;
import com.sfpay.linechart.components.XAxis;
import com.sfpay.linechart.components.YAxis;
import com.sfpay.linechart.components.Legend.LegendPosition;
import com.sfpay.linechart.components.XAxis.XAxisPosition;
import com.sfpay.linechart.components.YAxis.AxisDependency;
import com.sfpay.linechart.data.BarLineScatterCandleData;
import com.sfpay.linechart.data.BarLineScatterCandleDataSet;
import com.sfpay.linechart.data.DataSet;
import com.sfpay.linechart.data.Entry;
import com.sfpay.linechart.data.LineData;
import com.sfpay.linechart.data.LineDataSet;
import com.sfpay.linechart.data.filter.Approximator;
import com.sfpay.linechart.interfaces.BarLineScatterCandleDataProvider;
import com.sfpay.linechart.listener.BarLineChartTouchListener;
import com.sfpay.linechart.renderer.XAxisRenderer;
import com.sfpay.linechart.renderer.YAxisRenderer;
import com.sfpay.linechart.utils.FillFormatter;
import com.sfpay.linechart.utils.Highlight;
import com.sfpay.linechart.utils.SelInfo;
import com.sfpay.linechart.utils.Transformer;
import com.sfpay.linechart.utils.Utils;
import java.util.ArrayList;
import java.util.List;

@SuppressLint({"RtlHardcoded"})
public abstract class BarLineChartBase<T extends BarLineScatterCandleData<? extends BarLineScatterCandleDataSet<? extends Entry>>> extends Chart<T> implements BarLineScatterCandleDataProvider {
    protected int mMaxVisibleCount = 100;
    protected boolean mPinchZoomEnabled = false;
    protected boolean mDoubleTapToZoomEnabled = true;
    private boolean mDragEnabled = true;
    private boolean mScaleXEnabled = false;
    private boolean mScaleYEnabled = false;
    protected boolean mFilterData = false;
    protected Paint mGridBackgroundPaint;
    protected Paint mBorderPaint;
    protected boolean mHighLightIndicatorEnabled = true;
    protected boolean mDrawGridBackground = true;
    protected boolean mDrawBorders = false;
    protected OnDrawListener mDrawListener;
    protected YAxis mAxisLeft;
    protected XAxis mXAxis;
    protected YAxisRenderer mAxisRendererLeft;
    protected Transformer mLeftAxisTransformer;
    protected XAxisRenderer mXAxisRenderer;
    private float chartMargins = 10.0F;
    private long totalTime = 0L;
    private long drawCycles = 0L;
    protected OnTouchListener mListener;
    private boolean mCustomViewPortEnabled = false;

    public float getChartMargins() {
        return this.chartMargins;
    }

    public void setChartMargins(float chartMargins) {
        this.chartMargins = chartMargins;
    }

    public BarLineChartBase(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public BarLineChartBase(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BarLineChartBase(Context context) {
        super(context);
    }

    protected void init() {
        super.init();
        this.mAxisLeft = new YAxis(AxisDependency.LEFT);
        this.mXAxis = new XAxis();
        this.mLeftAxisTransformer = new Transformer(this.mViewPortHandler);
        this.mAxisRendererLeft = new YAxisRenderer(this.mViewPortHandler, this.mAxisLeft, this.mLeftAxisTransformer);
        this.mXAxisRenderer = new XAxisRenderer(this.mViewPortHandler, this.mXAxis, this.mLeftAxisTransformer);
        this.mListener = new BarLineChartTouchListener(this, this.mViewPortHandler.getMatrixTouch());
        this.mGridBackgroundPaint = new Paint();
        this.mGridBackgroundPaint.setStyle(Style.FILL);
        this.mGridBackgroundPaint.setColor(-1);
        this.mBorderPaint = new Paint();
        this.mBorderPaint.setStyle(Style.STROKE);
        this.mBorderPaint.setColor(-16777216);
        this.mBorderPaint.setStrokeWidth(Utils.convertDpToPixel(1.0F));
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(!this.mDataNotSet) {
            long starttime = System.currentTimeMillis();
            if(this.mXAxis.isAdjustXLabelsEnabled()) {
                this.calcModulus();
            }

            this.drawGridBackground(canvas);
            if(this.mAxisLeft.isEnabled()) {
                this.mAxisRendererLeft.computeAxis(this.mAxisLeft.mAxisMinimum, this.mAxisLeft.mAxisMaximum);
            }

            this.mXAxisRenderer.renderAxisLine(canvas);
            this.mAxisRendererLeft.renderAxisLine(canvas);
            int clipRestoreCount = canvas.save();
            canvas.clipRect(this.mViewPortHandler.getContentRect());
            this.mXAxisRenderer.renderGridLines(canvas);
            this.mAxisRendererLeft.renderGridLines(canvas);
            this.mRenderer.drawData(canvas);
            if(this.mHighlightEnabled && this.mHighLightIndicatorEnabled && this.valuesToHighlight()) {
                this.mRenderer.drawHighlighted(canvas, this.mIndicesToHightlight);
            }

            canvas.restoreToCount(clipRestoreCount);
            this.mXAxisRenderer.renderAxisLabels(canvas);
            this.mAxisRendererLeft.renderAxisLabels(canvas);
            this.drawMarkers(canvas);
            if(this.mLogEnabled) {
                long drawtime = System.currentTimeMillis() - starttime;
                this.totalTime += drawtime;
                ++this.drawCycles;
            }

        }
    }

    public void resetTracking() {
        this.totalTime = 0L;
        this.drawCycles = 0L;
    }

    protected void prepareValuePxMatrix() {
        this.mLeftAxisTransformer.prepareMatrixValuePx(this.mXChartMin, this.mDeltaX, this.mAxisLeft.mAxisRange, this.mAxisLeft.mAxisMinimum);
    }

    protected void prepareOffsetMatrix() {
        this.mLeftAxisTransformer.prepareMatrixOffset(this.mAxisLeft.isInverted());
    }

    public void notifyDataSetChanged() {
        if(!this.mDataNotSet) {
            if(this.mRenderer != null) {
                this.mRenderer.initBuffers();
            }

            this.calcMinMax();
            if(this.mAxisLeft.needsDefaultFormatter()) {
                this.mAxisLeft.setValueFormatter(this.mDefaultFormatter);
            }

            this.mAxisRendererLeft.computeAxis(this.mAxisLeft.mAxisMinimum, this.mAxisLeft.mAxisMaximum);
            this.mXAxisRenderer.computeAxis(((BarLineScatterCandleData)this.mData).getXValAverageLength(), ((BarLineScatterCandleData)this.mData).getXVals());
            this.mLegendRenderer.computeLegend(this.mData);
            this.calculateOffsets();
        }
    }

    protected void calcMinMax() {
        float minLeft = ((BarLineScatterCandleData)this.mData).getYMin(AxisDependency.LEFT);
        float maxLeft = ((BarLineScatterCandleData)this.mData).getYMax(AxisDependency.LEFT);
        this.mXChartMax = (float)((BarLineScatterCandleData)this.mData).getXVals().size() - 0.5F;
        this.mDeltaX = Math.abs(this.mXChartMax - this.mXChartMin);
        float max = !Float.isNaN(this.mAxisLeft.getAxisMaxValue())?this.mAxisLeft.getAxisMaxValue():maxLeft;
        float min = !Float.isNaN(this.mAxisLeft.getAxisMinValue())?this.mAxisLeft.getAxisMinValue():minLeft;
        if(max == min) {
            this.mAxisLeft.mAxisMaximum = max * 1.125F;
            this.mAxisLeft.mAxisMinimum = max * 0.5F;
            this.mAxisLeft.scale = (this.mAxisLeft.mAxisMaximum - this.mAxisLeft.mAxisMinimum) / ((float)this.mAxisLeft.getLabelCount() - 1.0F);
            this.mAxisLeft.mAxisMaximum += this.mAxisLeft.scale * 0.5F;
        } else {
            this.mAxisLeft.scale = (max - min) * 0.75F;
            this.mAxisLeft.mAxisMaximum = max - (max - min) * 0.6F + this.mAxisLeft.scale * 1.5F;
            this.mAxisLeft.mAxisMinimum = max + (max - min) * 0.15F - ((float)this.mAxisLeft.getLabelCount() - 1.0F) * this.mAxisLeft.scale;
        }

        if(this.mAxisLeft.mAxisMinimum < 0.0F) {
            this.mAxisLeft.mAxisMaximum -= this.mAxisLeft.mAxisMinimum;
            this.mAxisLeft.mAxisMinimum = 0.0F;
        }

        if(this.mAxisLeft.isStartAtZeroEnabled()) {
            this.mAxisLeft.mAxisMinimum = 0.0F;
        }

        this.mAxisLeft.mAxisRange = Math.abs(this.mAxisLeft.mAxisMaximum - this.mAxisLeft.mAxisMinimum);
    }

    protected void calculateOffsets() {
        if(!this.mCustomViewPortEnabled) {
            float offsetLeft = 0.0F;
            float offsetRight = 0.0F;
            float offsetTop = 0.0F;
            float offsetBottom = 0.0F;
            if(this.mLegend != null && this.mLegend.isEnabled()) {
                if(this.mLegend.getPosition() != LegendPosition.RIGHT_OF_CHART && this.mLegend.getPosition() != LegendPosition.RIGHT_OF_CHART_CENTER) {
                    if(this.mLegend.getPosition() != LegendPosition.LEFT_OF_CHART && this.mLegend.getPosition() != LegendPosition.LEFT_OF_CHART_CENTER) {
                        if(this.mLegend.getPosition() == LegendPosition.BELOW_CHART_LEFT || this.mLegend.getPosition() == LegendPosition.BELOW_CHART_RIGHT || this.mLegend.getPosition() == LegendPosition.BELOW_CHART_CENTER) {
                            offsetBottom += this.mLegend.mTextHeightMax * 3.0F;
                        }
                    } else {
                        offsetLeft += this.mLegend.mTextWidthMax + this.mLegend.getXOffset() * 2.0F;
                    }
                } else {
                    offsetRight += this.mLegend.mTextWidthMax + this.mLegend.getXOffset() * 2.0F;
                }
            }

            if(this.mAxisLeft.needsOffset()) {
                offsetLeft += this.mAxisLeft.getRequiredWidthSpace(this.mAxisRendererLeft.getPaintAxisLabels());
            }

            float xlabelheight = (float)this.mXAxis.mLabelHeight * 2.0F;
            if(this.mXAxis.isEnabled()) {
                if(this.mXAxis.getPosition() == XAxisPosition.BOTTOM) {
                    offsetBottom += xlabelheight + (float)this.mXAxis.mLabelHeight;
                } else if(this.mXAxis.getPosition() == XAxisPosition.TOP) {
                    offsetTop += xlabelheight / 2.0F;
                } else if(this.mXAxis.getPosition() == XAxisPosition.BOTH_SIDED) {
                    offsetBottom += xlabelheight;
                    offsetTop += xlabelheight / 2.0F;
                }
            }

            float min = Utils.convertDpToPixel(this.getChartMargins());
            this.mViewPortHandler.restrainViewPort(Math.max(min, offsetLeft), Math.max(min / 1.5F, offsetTop), Math.max(min, offsetRight), Math.max(min, offsetBottom));
        }

        this.prepareOffsetMatrix();
        this.prepareValuePxMatrix();
    }

    protected void calcModulus() {
        if(this.mXAxis != null) {
            float[] values = new float[9];
            this.mViewPortHandler.getMatrixTouch().getValues(values);
            if(this.mXAxis.mAxisLabelModulus < 1) {
                this.mXAxis.mAxisLabelModulus = 1;
            }

        }
    }

    protected float[] getMarkerPosition(Entry e, int dataSetIndex) {
        float xPos = (float)e.getXIndex();
        float[] pts = new float[]{xPos, e.getVal()};
        this.getTransformer(((BarLineScatterCandleDataSet)((BarLineScatterCandleData)this.mData).getDataSetByIndex(dataSetIndex)).getAxisDependency()).pointValuesToPixel(pts);
        return pts;
    }

    protected void drawGridBackground(Canvas c) {
        if(this.mDrawGridBackground) {
            c.drawRect(this.mViewPortHandler.getContentRect(), this.mGridBackgroundPaint);
        }

        if(this.mDrawBorders) {
            c.drawRect(this.mViewPortHandler.getContentRect(), this.mBorderPaint);
        }

    }

    public Transformer getTransformer(AxisDependency which) {
        return this.mLeftAxisTransformer;
    }

    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        return this.mListener != null && !this.mDataNotSet?(!this.mTouchEnabled?false:this.mListener.onTouch(this, event)):false;
    }

    public void zoom(float scaleX, float scaleY, float x, float y) {
        Matrix save = this.mViewPortHandler.zoom(scaleX, scaleY, x, -y);
        this.mViewPortHandler.refresh(save, this, true);
    }

    public void setVisibleXRange(float xRange) {
        float xScale = this.mDeltaX / xRange;
        this.mViewPortHandler.setMinimumScaleX(xScale);
    }

    public float getDeltaY(AxisDependency axis) {
        return this.mAxisLeft.mAxisRange;
    }

    public void setHighlightIndicatorEnabled(boolean enabled) {
        this.mHighLightIndicatorEnabled = enabled;
    }

    public void setDragEnabled(boolean enabled) {
        this.mDragEnabled = enabled;
    }

    public boolean isDragEnabled() {
        return this.mDragEnabled;
    }

    public void setScaleEnabled(boolean enabled) {
        this.mScaleXEnabled = enabled;
        this.mScaleYEnabled = enabled;
    }

    public boolean isScaleXEnabled() {
        return this.mScaleXEnabled;
    }

    public boolean isScaleYEnabled() {
        return this.mScaleYEnabled;
    }

    public void setDoubleTapToZoomEnabled(boolean enabled) {
        this.mDoubleTapToZoomEnabled = enabled;
    }

    public boolean isDoubleTapToZoomEnabled() {
        return this.mDoubleTapToZoomEnabled;
    }

    public Highlight getHighlightByTouchPoint(float x, float y) {
        if(!this.mDataNotSet && this.mData != null) {
            float[] pts = new float[]{x, 0.0F};
            this.mLeftAxisTransformer.pixelsToValue(pts);
            double xTouchVal = (double)pts[0];
            double base = Math.floor(xTouchVal);
            double touchOffset = (double)this.mDeltaX * 0.025D;
            if(xTouchVal >= -touchOffset && xTouchVal <= (double)this.mDeltaX + touchOffset) {
                if(base < 0.0D) {
                    base = 0.0D;
                }

                if(base >= (double)this.mDeltaX) {
                    base = (double)(this.mDeltaX - 1.0F);
                }

                int xIndex = (int)base;
                if(xTouchVal - base > 0.5D) {
                    xIndex = (int)base + 1;
                }

                if(xIndex >= this.mXAxis.getValues().size()) {
                    xIndex = this.mXAxis.getValues().size() - 1;
                }

                List valsAtIndex = this.getYValsAtIndex(xIndex);
                float leftdist = Utils.getMinimumDistance(valsAtIndex, y, AxisDependency.LEFT);
                float rightdist = Utils.getMinimumDistance(valsAtIndex, y, AxisDependency.RIGHT);
                if(((BarLineScatterCandleData)this.mData).getFirstRight() == null) {
                    rightdist = 3.4028235E38F;
                }

                if(((BarLineScatterCandleData)this.mData).getFirstLeft() == null) {
                    leftdist = 3.4028235E38F;
                }

                AxisDependency axis = leftdist < rightdist?AxisDependency.LEFT:AxisDependency.RIGHT;
                int dataSetIndex = Utils.getClosestDataSetIndex(valsAtIndex, y, axis);
                return dataSetIndex == -1?null:new Highlight(xIndex, dataSetIndex);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public List<SelInfo> getYValsAtIndex(int xIndex) {
        ArrayList vals = new ArrayList();
        float[] pts = new float[2];

        for(int i = 0; i < ((BarLineScatterCandleData)this.mData).getDataSetCount(); ++i) {
            DataSet dataSet = ((BarLineScatterCandleData)this.mData).getDataSetByIndex(i);
            float yVal = dataSet.getYValForXIndex(xIndex);
            pts[1] = yVal;
            this.getTransformer(dataSet.getAxisDependency()).pointValuesToPixel(pts);
            if(!Float.isNaN(pts[1])) {
                vals.add(new SelInfo(pts[1], i, dataSet));
            }
        }

        return vals;
    }

    public BarLineScatterCandleDataSet<? extends Entry> getDataSetByTouchPoint(float x, float y) {
        Highlight h = this.getHighlightByTouchPoint(x, y);
        return h != null?(BarLineScatterCandleDataSet)((BarLineScatterCandleData)this.mData).getDataSetByIndex(h.getDataSetIndex()):null;
    }

    public int getLowestVisibleXIndex() {
        float[] pts = new float[]{this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentBottom()};
        this.getTransformer(AxisDependency.LEFT).pixelsToValue(pts);
        return pts[0] <= 0.0F?0:(int)(pts[0] + 1.0F);
    }

    public int getHighestVisibleXIndex() {
        float[] pts = new float[]{this.mViewPortHandler.contentRight(), this.mViewPortHandler.contentBottom()};
        this.getTransformer(AxisDependency.LEFT).pixelsToValue(pts);
        return pts[0] >= (float)((BarLineScatterCandleData)this.mData).getXValCount()?((BarLineScatterCandleData)this.mData).getXValCount() - 1:(int)pts[0];
    }

    public boolean isFullyZoomedOut() {
        return this.mViewPortHandler.isFullyZoomedOut();
    }

    public YAxis getAxisLeft() {
        return this.mAxisLeft;
    }

    public YAxis getAxis(AxisDependency axis) {
        return this.mAxisLeft;
    }

    public boolean isInverted(AxisDependency axis) {
        return this.getAxis(axis).isInverted();
    }

    public XAxis getXAxis() {
        return this.mXAxis;
    }

    public void enableFiltering(Approximator a) {
        this.mFilterData = true;
    }

    public void disableFiltering() {
        this.mFilterData = false;
    }

    public boolean isFilteringEnabled() {
        return this.mFilterData;
    }

    public void setPinchZoom(boolean enabled) {
        this.mPinchZoomEnabled = enabled;
    }

    public boolean isPinchZoomEnabled() {
        return this.mPinchZoomEnabled;
    }

    public boolean hasNoDragOffset() {
        return this.mViewPortHandler.hasNoDragOffset();
    }

    public float getYChartMax() {
        return this.mAxisLeft.mAxisMaximum;
    }

    public float getYChartMin() {
        return this.mAxisLeft.mAxisMinimum;
    }

    public boolean isAnyAxisInverted() {
        return this.mAxisLeft.isInverted();
    }

    public void setPaint(Paint p, int which) {
        super.setPaint(p, which);
        switch(which) {
        case 4:
            this.mGridBackgroundPaint = p;
        default:
        }
    }

    public Paint getPaint(int which) {
        Paint p = super.getPaint(which);
        if(p != null) {
            return p;
        } else {
            switch(which) {
            case 4:
                return this.mGridBackgroundPaint;
            default:
                return null;
            }
        }
    }

    protected class DefaultFillFormatter implements FillFormatter {
        protected DefaultFillFormatter() {
        }

        public float getFillLinePosition(LineDataSet dataSet, LineData data, float chartMaxY, float chartMinY) {
            float fillMin = 0.0F;
            if(dataSet.getYMax() > 0.0F && dataSet.getYMin() < 0.0F) {
                fillMin = 0.0F;
            } else if(!BarLineChartBase.this.getAxis(dataSet.getAxisDependency()).isStartAtZeroEnabled()) {
                float max;
                if(data.getYMax() > 0.0F) {
                    max = 0.0F;
                } else {
                    max = chartMaxY;
                }

                float min;
                if(data.getYMin() < 0.0F) {
                    min = 0.0F;
                } else {
                    min = chartMinY;
                }

                fillMin = dataSet.getYMin() >= 0.0F?min:max;
            } else {
                fillMin = 0.0F;
            }

            return fillMin;
        }
    }
}
