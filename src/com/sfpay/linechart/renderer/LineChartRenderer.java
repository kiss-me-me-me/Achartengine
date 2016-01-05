//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.sfpay.linechart.renderer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Paint.Style;
import com.sfpay.linechart.buffer.CircleBuffer;
import com.sfpay.linechart.buffer.LineBuffer;
import com.sfpay.linechart.data.Entry;
import com.sfpay.linechart.data.LineData;
import com.sfpay.linechart.data.LineDataSet;
import com.sfpay.linechart.interfaces.LineDataProvider;
import com.sfpay.linechart.renderer.DataRenderer;
import com.sfpay.linechart.utils.Highlight;
import com.sfpay.linechart.utils.Transformer;
import com.sfpay.linechart.utils.ViewPortHandler;
import java.util.Iterator;
import java.util.List;

public class LineChartRenderer extends DataRenderer {
    protected LineDataProvider mChart;
    protected Paint mCirclePaintInner;
    protected Bitmap mDrawBitmap;
    protected Path cubicPath = new Path();
    protected Path cubicFillPath = new Path();
    protected LineBuffer[] mLineBuffers;
    protected CircleBuffer[] mCircleBuffers;

    public LineChartRenderer(LineDataProvider chart, ViewPortHandler viewPortHandler) {
        super(viewPortHandler);
        this.mChart = chart;
        this.mCirclePaintInner = new Paint(1);
        this.mCirclePaintInner.setStyle(Style.FILL);
        this.mCirclePaintInner.setColor(-1);
    }

    public void initBuffers() {
        LineData lineData = this.mChart.getLineData();
        this.mLineBuffers = new LineBuffer[lineData.getDataSetCount()];
        this.mCircleBuffers = new CircleBuffer[lineData.getDataSetCount()];

        for(int i = 0; i < this.mLineBuffers.length; ++i) {
            LineDataSet set = (LineDataSet)lineData.getDataSetByIndex(i);
            this.mLineBuffers[i] = new LineBuffer(set.getEntryCount() * 4 - 4);
            this.mCircleBuffers[i] = new CircleBuffer(set.getEntryCount() * 2);
        }

    }

    public void drawData(Canvas c) {
        int width = (int)this.mViewPortHandler.getChartWidth();
        int height = (int)this.mViewPortHandler.getChartHeight();
        LineData lineData = this.mChart.getLineData();
        Iterator var6 = lineData.getDataSets().iterator();

        while(var6.hasNext()) {
            LineDataSet set = (LineDataSet)var6.next();
            if(set.isVisible()) {
                this.drawDataSet(c, set);
            }
        }

    }

    protected void drawDataSet(Canvas c, LineDataSet dataSet) {
        List entries = dataSet.getYVals();
        if(entries.size() >= 1) {
            this.calcXBounds(this.mChart.getTransformer(dataSet.getAxisDependency()));
            this.mRenderPaint.setStrokeWidth(dataSet.getLineWidth());
            this.mRenderPaint.setPathEffect(dataSet.getDashPathEffect());
            if(dataSet.isDrawCubicEnabled()) {
                this.drawCubic(c, dataSet, entries);
            } else {
                this.drawLinear(c, dataSet, entries);
            }

            this.mRenderPaint.setPathEffect((PathEffect)null);
        }
    }

    protected void drawCubic(Canvas c, LineDataSet dataSet, List<Entry> entries) {
        Transformer trans = this.mChart.getTransformer(dataSet.getAxisDependency());
        Entry entryFrom = dataSet.getEntryForXIndex(this.mMinX);
        Entry entryTo = dataSet.getEntryForXIndex(this.mMaxX);
        int minx = dataSet.getEntryPosition(entryFrom);
        int maxx = Math.min(dataSet.getEntryPosition(entryTo) + 1, entries.size());
        float phaseX = 1.0F;
        float phaseY = 1.0F;
        float intensity = dataSet.getCubicIntensity();
        this.cubicPath.reset();
        int size = (int)Math.ceil((double)((float)(maxx - minx) * phaseX + (float)minx));
        minx = Math.max(minx - 2, 0);
        size = Math.min(size + 2, entries.size());
        if(size - minx >= 2) {
            float prevDx = 0.0F;
            float prevDy = 0.0F;
            float curDx = 0.0F;
            float curDy = 0.0F;
            Entry cur = (Entry)entries.get(minx);
            Entry next = (Entry)entries.get(minx + 1);
            Entry prev = (Entry)entries.get(minx);
            Entry prevPrev = (Entry)entries.get(minx);
            this.cubicPath.moveTo((float)cur.getXIndex(), cur.getVal() * phaseY);
            prevDx = (float)(next.getXIndex() - cur.getXIndex()) * intensity;
            prevDy = (next.getVal() - cur.getVal()) * intensity;
            cur = (Entry)entries.get(minx + 1);
            next = (Entry)entries.get(minx + (size - minx > 2?2:1));
            curDx = (float)(next.getXIndex() - prev.getXIndex()) * intensity;
            curDy = (next.getVal() - prev.getVal()) * intensity;
            this.cubicPath.cubicTo((float)prev.getXIndex() + prevDx, (prev.getVal() + prevDy) * phaseY, (float)cur.getXIndex() - curDx, (cur.getVal() - curDy) * phaseY, (float)cur.getXIndex(), cur.getVal() * phaseY);

            for(int j = minx + 2; j < size - 1; ++j) {
                prevPrev = (Entry)entries.get(j - 2);
                prev = (Entry)entries.get(j - 1);
                cur = (Entry)entries.get(j);
                next = (Entry)entries.get(j + 1);
                prevDx = (float)(cur.getXIndex() - prevPrev.getXIndex()) * intensity;
                prevDy = (cur.getVal() - prevPrev.getVal()) * intensity;
                curDx = (float)(next.getXIndex() - prev.getXIndex()) * intensity;
                curDy = (next.getVal() - prev.getVal()) * intensity;
                this.cubicPath.cubicTo((float)prev.getXIndex() + prevDx, (prev.getVal() + prevDy) * phaseY, (float)cur.getXIndex() - curDx, (cur.getVal() - curDy) * phaseY, (float)cur.getXIndex(), cur.getVal() * phaseY);
            }

            if(size > entries.size() - 1) {
                cur = (Entry)entries.get(entries.size() - 1);
                prev = (Entry)entries.get(entries.size() - 2);
                prevPrev = (Entry)entries.get(entries.size() >= 3?entries.size() - 3:entries.size() - 2);
                prevDx = (float)(cur.getXIndex() - prevPrev.getXIndex()) * intensity;
                prevDy = (cur.getVal() - prevPrev.getVal()) * intensity;
                curDx = (float)(cur.getXIndex() - prev.getXIndex()) * intensity;
                curDy = (cur.getVal() - prev.getVal()) * intensity;
                this.cubicPath.cubicTo((float)prev.getXIndex() + prevDx, (prev.getVal() + prevDy) * phaseY, (float)cur.getXIndex() - curDx, (cur.getVal() - curDy) * phaseY, (float)cur.getXIndex(), cur.getVal() * phaseY);
            }
        }

        if(dataSet.isDrawFilledEnabled()) {
            this.cubicFillPath.reset();
            this.cubicFillPath.addPath(this.cubicPath);
        }

        this.mRenderPaint.setColor(dataSet.getColor());
        this.mRenderPaint.setStyle(Style.STROKE);
        trans.pathValueToPixel(this.cubicPath);
        this.mRenderPaint.setPathEffect((PathEffect)null);
    }

    protected void drawCubicFill(Canvas c, LineDataSet dataSet, Path spline, Transformer trans, int from, int to) {
        float fillMin = this.mChart.getFillFormatter().getFillLinePosition(dataSet, this.mChart.getLineData(), this.mChart.getYChartMax(), this.mChart.getYChartMin());
        spline.lineTo((float)(to - 1), fillMin);
        spline.lineTo((float)from, fillMin);
        spline.close();
        this.mRenderPaint.setStyle(Style.FILL);
        this.mRenderPaint.setColor(dataSet.getFillColor());
        this.mRenderPaint.setAlpha(dataSet.getFillAlpha());
        trans.pathValueToPixel(spline);
        this.mRenderPaint.setAlpha(255);
    }

    protected void drawLinear(Canvas c, LineDataSet dataSet, List<Entry> entries) {
        int dataSetIndex = this.mChart.getLineData().getIndexOfDataSet(dataSet);
        Transformer trans = this.mChart.getTransformer(dataSet.getAxisDependency());
        float phaseX = 1.0F;
        float phaseY = 1.0F;
        this.mRenderPaint.setStyle(Style.STROKE);
        Canvas canvas = null;
        canvas = c;
        Entry entryFrom = dataSet.getEntryForXIndex(this.mMinX);
        Entry entryTo = dataSet.getEntryForXIndex(this.mMaxX);
        int minx = dataSet.getEntryPosition(entryFrom);
        int maxx = Math.min(dataSet.getEntryPosition(entryTo) + 1, entries.size());
        int range = (maxx - minx) * 4 - 4;
        LineBuffer buffer = this.mLineBuffers[dataSetIndex];
        buffer.setPhases(phaseX, phaseY);
        buffer.limitFrom(minx);
        buffer.limitTo(maxx);
        buffer.feed(entries);
        trans.pointValuesToPixel(buffer.buffer);
        if(dataSet.getColors().size() > 1) {
            for(int j = 0; j < range && this.mViewPortHandler.isInBoundsRight(buffer.buffer[j]); j += 4) {
                if(this.mViewPortHandler.isInBoundsLeft(buffer.buffer[j + 2]) && (this.mViewPortHandler.isInBoundsTop(buffer.buffer[j + 1]) || this.mViewPortHandler.isInBoundsBottom(buffer.buffer[j + 3])) && (this.mViewPortHandler.isInBoundsTop(buffer.buffer[j + 1]) || this.mViewPortHandler.isInBoundsBottom(buffer.buffer[j + 3]))) {
                    this.mRenderPaint.setColor(dataSet.getColor(j / 4 + minx));
                    canvas.drawLine(buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2], buffer.buffer[j + 3], this.mRenderPaint);
                }
            }
        } else {
            this.mRenderPaint.setColor(dataSet.getColor());
            c.drawLines(buffer.buffer, 0, range, this.mRenderPaint);
        }

        this.mRenderPaint.setPathEffect((PathEffect)null);
        if(dataSet.isDrawFilledEnabled() && entries.size() > 0) {
            this.drawLinearFill(c, dataSet, entries, minx, maxx, trans);
        }

    }

    protected void drawLinearFill(Canvas c, LineDataSet dataSet, List<Entry> entries, int minx, int maxx, Transformer trans) {
        this.mRenderPaint.setStyle(Style.FILL);
        this.mRenderPaint.setColor(dataSet.getFillColor());
        this.mRenderPaint.setAlpha(dataSet.getFillAlpha());
        Path filled = this.generateFilledPath(entries, this.mChart.getFillFormatter().getFillLinePosition(dataSet, this.mChart.getLineData(), this.mChart.getYChartMax(), this.mChart.getYChartMin()), minx, maxx);
        trans.pathValueToPixel(filled);
        c.drawPath(filled, this.mRenderPaint);
        this.mRenderPaint.setAlpha(255);
    }

    private Path generateFilledPath(List<Entry> entries, float fillMin, int from, int to) {
        float phaseX = 1.0F;
        float phaseY = 1.0F;
        Path filled = new Path();
        filled.moveTo((float)((Entry)entries.get(from)).getXIndex(), fillMin);
        filled.lineTo((float)((Entry)entries.get(from)).getXIndex(), ((Entry)entries.get(from)).getVal() * phaseY);
        int x = from + 1;

        for(int count = (int)Math.ceil((double)((float)(to - from) * phaseX + (float)from)); x < count; ++x) {
            Entry e = (Entry)entries.get(x);
            filled.lineTo((float)e.getXIndex(), e.getVal() * phaseY);
        }

        filled.lineTo((float)((Entry)entries.get(Math.max(Math.min((int)Math.ceil((double)((float)(to - from) * phaseX + (float)from)) - 1, entries.size() - 1), 0))).getXIndex(), fillMin);
        filled.close();
        return filled;
    }

    public void drawValues(Canvas c) {
    }

    public void drawExtras(Canvas c) {
        this.drawCircles(c);
    }

    protected void drawCircles(Canvas c) {
        this.mRenderPaint.setStyle(Style.FILL);
        float phaseX = 1.0F;
        float phaseY = 1.0F;
        List dataSets = this.mChart.getLineData().getDataSets();

        for(int i = 0; i < dataSets.size(); ++i) {
            LineDataSet dataSet = (LineDataSet)dataSets.get(i);
            if(dataSet.isVisible() && dataSet.isDrawCirclesEnabled()) {
                this.mCirclePaintInner.setColor(dataSet.getCircleHoleColor());
                Transformer trans = this.mChart.getTransformer(dataSet.getAxisDependency());
                List entries = dataSet.getYVals();
                Entry entryFrom = dataSet.getEntryForXIndex(this.mMinX < 0?0:this.mMinX);
                Entry entryTo = dataSet.getEntryForXIndex(this.mMaxX);
                int minx = dataSet.getEntryPosition(entryFrom);
                int maxx = Math.min(dataSet.getEntryPosition(entryTo) + 1, entries.size());
                CircleBuffer buffer = this.mCircleBuffers[i];
                buffer.setPhases(phaseX, phaseY);
                buffer.limitFrom(minx);
                buffer.limitTo(maxx);
                buffer.feed(entries);
                trans.pointValuesToPixel(buffer.buffer);
                float halfsize = dataSet.getCircleSize() / 2.0F;
                int j = (int)Math.ceil((double)((float)(maxx - 1) * phaseX + (float)minx)) * 2;

                for(int count = (int)Math.ceil((double)((float)(maxx - minx) * phaseX + (float)minx)) * 2; j < count; j += 2) {
                    float x = buffer.buffer[j];
                    float y = buffer.buffer[j + 1];
                    if(!this.mViewPortHandler.isInBoundsRight(x)) {
                        break;
                    }

                    if(this.mViewPortHandler.isInBoundsLeft(x) && this.mViewPortHandler.isInBoundsY(y)) {
                        int circleColor = dataSet.getCircleColor(j / 2 + minx);
                        this.mRenderPaint.setColor(circleColor);
                        c.drawCircle(x, y, dataSet.getCircleSize(), this.mRenderPaint);
                        if(dataSet.isDrawCircleHoleEnabled() && circleColor != this.mCirclePaintInner.getColor()) {
                            c.drawCircle(x, y, halfsize, this.mCirclePaintInner);
                        }
                    }
                }
            }
        }

    }

    public void drawHighlighted(Canvas c, Highlight[] indices) {
        for(int i = 0; i < indices.length; ++i) {
            LineDataSet set = (LineDataSet)this.mChart.getLineData().getDataSetByIndex(indices[i].getDataSetIndex());
            if(set != null) {
                this.mHighlightPaint.setColor(set.getHighLightColor());
                int xIndex = indices[i].getXIndex();
                if((float)xIndex <= this.mChart.getXChartMax()) {
                    float y = set.getYValForXIndex(xIndex);
                    float[] pts = new float[]{(float)xIndex, this.mChart.getYChartMax(), (float)xIndex, this.mChart.getYChartMin(), 0.0F, y, this.mChart.getXChartMax(), y};
                    this.mChart.getTransformer(set.getAxisDependency()).pointValuesToPixel(pts);
                    c.drawLines(pts, this.mHighlightPaint);
                }
            }
        }

    }
}
