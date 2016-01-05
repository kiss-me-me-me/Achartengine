//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.sfpay.linechart.utils;

import android.graphics.Matrix;
import android.graphics.Path;
import com.sfpay.linechart.data.Entry;
import com.sfpay.linechart.utils.PointD;
import com.sfpay.linechart.utils.ViewPortHandler;
import java.util.List;

public class Transformer {
    protected Matrix mMatrixValueToPx = new Matrix();
    protected Matrix mMatrixOffset = new Matrix();
    protected ViewPortHandler mViewPortHandler;

    public Transformer(ViewPortHandler viewPortHandler) {
        this.mViewPortHandler = viewPortHandler;
    }

    public void prepareMatrixValuePx(float xChartMin, float deltaX, float deltaY, float yChartMin) {
        float scaleX = this.mViewPortHandler.contentWidth() / deltaX;
        float scaleY = this.mViewPortHandler.contentHeight() / deltaY;
        this.mMatrixValueToPx.reset();
        this.mMatrixValueToPx.postTranslate(-xChartMin, -yChartMin);
        this.mMatrixValueToPx.postScale(scaleX, -scaleY);
    }

    public void prepareMatrixOffset(boolean inverted) {
        this.mMatrixOffset.reset();
        if(!inverted) {
            this.mMatrixOffset.postTranslate(this.mViewPortHandler.offsetLeft(), this.mViewPortHandler.getChartHeight() - this.mViewPortHandler.offsetBottom());
        } else {
            this.mMatrixOffset.setTranslate(this.mViewPortHandler.offsetLeft(), -this.mViewPortHandler.offsetTop());
            this.mMatrixOffset.postScale(1.0F, -1.0F);
        }

    }

    public float[] generateTransformedValuesLine(List<? extends Entry> entries, float phaseX, float phaseY, int from, int to) {
        int count = (int)Math.ceil((double)((float)(to - from) * phaseX)) * 2;
        float[] valuePoints = new float[count];

        for(int j = 0; j < count; j += 2) {
            Entry e = (Entry)entries.get(j / 2 + from);
            if(e != null) {
                valuePoints[j] = (float)e.getXIndex();
                valuePoints[j + 1] = e.getVal() * phaseY;
            }
        }

        this.pointValuesToPixel(valuePoints);
        return valuePoints;
    }

    public void pathValueToPixel(Path path) {
        path.transform(this.mMatrixValueToPx);
        path.transform(this.mViewPortHandler.getMatrixTouch());
        path.transform(this.mMatrixOffset);
    }

    public void pointValuesToPixel(float[] pts) {
        this.mMatrixValueToPx.mapPoints(pts);
        this.mViewPortHandler.getMatrixTouch().mapPoints(pts);
        this.mMatrixOffset.mapPoints(pts);
    }

    public void pixelsToValue(float[] pixels) {
        Matrix tmp = new Matrix();
        this.mMatrixOffset.invert(tmp);
        tmp.mapPoints(pixels);
        this.mViewPortHandler.getMatrixTouch().invert(tmp);
        tmp.mapPoints(pixels);
        this.mMatrixValueToPx.invert(tmp);
        tmp.mapPoints(pixels);
    }

    public PointD getValuesByTouchPoint(float x, float y) {
        float[] pts = new float[]{x, y};
        this.pixelsToValue(pts);
        double xTouchVal = (double)pts[0];
        double yTouchVal = (double)pts[1];
        return new PointD(xTouchVal, yTouchVal);
    }
}
