//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.sfpay.linechart.utils;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.View;
import com.sfpay.linechart.utils.Utils;

public class ViewPortHandler {
    protected final Matrix mMatrixTouch = new Matrix();
    protected RectF mContentRect = new RectF();
    protected float mChartWidth = 0.0F;
    protected float mChartHeight = 0.0F;
    private float mMinScaleY = 1.0F;
    private float mMinScaleX = 1.0F;
    private float mScaleX = 1.0F;
    private float mScaleY = 1.0F;
    private float mTransOffsetX = 0.0F;
    private float mTransOffsetY = 0.0F;

    public ViewPortHandler() {
    }

    public void setChartDimens(float width, float height) {
        this.mChartHeight = height;
        this.mChartWidth = width;
        if(this.mContentRect.width() <= 0.0F || this.mContentRect.height() <= 0.0F) {
            this.mContentRect.set(0.0F, 0.0F, width, height);
        }

    }

    public boolean hasChartDimens() {
        return this.mChartHeight > 0.0F && this.mChartWidth > 0.0F;
    }

    public void restrainViewPort(float offsetLeft, float offsetTop, float offsetRight, float offsetBottom) {
        this.mContentRect.set(offsetLeft, offsetTop, this.mChartWidth - offsetRight, this.mChartHeight - offsetBottom);
    }

    public float offsetLeft() {
        return this.mContentRect.left;
    }

    public float offsetRight() {
        return this.mChartWidth - this.mContentRect.right;
    }

    public float offsetTop() {
        return this.mContentRect.top;
    }

    public float offsetBottom() {
        return this.mChartHeight - this.mContentRect.bottom;
    }

    public float contentTop() {
        return this.mContentRect.top;
    }

    public float contentLeft() {
        return this.mContentRect.left;
    }

    public float contentRight() {
        return this.mContentRect.right;
    }

    public float contentBottom() {
        return this.mContentRect.bottom;
    }

    public float contentWidth() {
        return this.mContentRect.width();
    }

    public float contentHeight() {
        return this.mContentRect.height();
    }

    public RectF getContentRect() {
        return this.mContentRect;
    }

    public PointF getContentCenter() {
        return new PointF(this.mContentRect.centerX(), this.mContentRect.centerY());
    }

    public float getChartHeight() {
        return this.mChartHeight;
    }

    public float getChartWidth() {
        return this.mChartWidth;
    }

    public Matrix zoomIn(float x, float y) {
        Matrix save = new Matrix();
        save.set(this.mMatrixTouch);
        save.postScale(1.4F, 1.4F, x, y);
        return save;
    }

    public Matrix zoomOut(float x, float y) {
        Matrix save = new Matrix();
        save.set(this.mMatrixTouch);
        save.postScale(0.7F, 0.7F, x, y);
        return save;
    }

    public Matrix zoom(float scaleX, float scaleY, float x, float y) {
        Matrix save = new Matrix();
        save.set(this.mMatrixTouch);
        save.postScale(scaleX, scaleY, x, y);
        return save;
    }

    public Matrix fitScreen() {
        Matrix save = new Matrix();
        save.set(this.mMatrixTouch);
        float[] vals = new float[9];
        save.getValues(vals);
        vals[2] = 0.0F;
        vals[5] = 0.0F;
        vals[0] = 1.0F;
        vals[4] = 1.0F;
        save.setValues(vals);
        return save;
    }

    public synchronized void centerViewPort(float[] transformedPts, View view) {
        Matrix save = new Matrix();
        save.set(this.mMatrixTouch);
        float x = transformedPts[0] - this.offsetLeft();
        float y = transformedPts[1] - this.offsetTop();
        save.postTranslate(-x, -y);
        this.refresh(save, view, false);
    }

    public Matrix refresh(Matrix newMatrix, View chart, boolean invalidate) {
        this.mMatrixTouch.set(newMatrix);
        this.limitTransAndScale(this.mMatrixTouch, this.mContentRect);
        chart.invalidate();
        newMatrix.set(this.mMatrixTouch);
        return newMatrix;
    }

    public void limitTransAndScale(Matrix matrix, RectF content) {
        float[] vals = new float[9];
        matrix.getValues(vals);
        float curTransX = vals[2];
        float curScaleX = vals[0];
        float curTransY = vals[5];
        float curScaleY = vals[4];
        this.mScaleX = Math.max(this.mMinScaleX, curScaleX);
        this.mScaleY = Math.max(this.mMinScaleY, curScaleY);
        float width = 0.0F;
        float height = 0.0F;
        if(content != null) {
            width = content.width();
            height = content.height();
        }

        float maxTransX = -width * (this.mScaleX - 1.0F);
        float newTransX = Math.min(Math.max(curTransX, maxTransX - this.mTransOffsetX), this.mTransOffsetX);
        float maxTransY = height * (this.mScaleY - 1.0F);
        float newTransY = Math.max(Math.min(curTransY, maxTransY + this.mTransOffsetY), -this.mTransOffsetY);
        vals[2] = newTransX;
        vals[0] = this.mScaleX;
        vals[5] = newTransY;
        vals[4] = this.mScaleY;
        matrix.setValues(vals);
    }

    public void setMinimumScaleX(float xScale) {
        if(xScale < 1.0F) {
            xScale = 1.0F;
        }

        this.mMinScaleX = xScale;
        this.limitTransAndScale(this.mMatrixTouch, this.mContentRect);
    }

    public void setMinimumScaleY(float yScale) {
        if(yScale < 1.0F) {
            yScale = 1.0F;
        }

        this.mMinScaleY = yScale;
        this.limitTransAndScale(this.mMatrixTouch, this.mContentRect);
    }

    public Matrix getMatrixTouch() {
        return this.mMatrixTouch;
    }

    public boolean isInBoundsX(float x) {
        return this.isInBoundsLeft(x) && this.isInBoundsRight(x);
    }

    public boolean isInBoundsY(float y) {
        return this.isInBoundsTop(y) && this.isInBoundsBottom(y);
    }

    public boolean isInBounds(float x, float y) {
        return this.isInBoundsX(x) && this.isInBoundsY(y);
    }

    public boolean isInBoundsLeft(float x) {
        return this.mContentRect.left <= x;
    }

    public boolean isInBoundsRight(float x) {
        x = (float)((int)(x * 100.0F)) / 100.0F;
        return this.mContentRect.right >= x;
    }

    public boolean isInBoundsTop(float y) {
        return this.mContentRect.top <= y;
    }

    public boolean isInBoundsBottom(float y) {
        y = (float)((int)(y * 100.0F)) / 100.0F;
        return this.mContentRect.bottom >= y;
    }

    public float getScaleX() {
        return this.mScaleX;
    }

    public float getScaleY() {
        return this.mScaleY;
    }

    public boolean isFullyZoomedOut() {
        return this.isFullyZoomedOutX() && this.isFullyZoomedOutY();
    }

    public boolean isFullyZoomedOutY() {
        return this.mScaleY <= this.mMinScaleY && this.mMinScaleY <= 1.0F;
    }

    public boolean isFullyZoomedOutX() {
        return this.mScaleX <= this.mMinScaleX && this.mMinScaleX <= 1.0F;
    }

    public void setDragOffsetX(float offset) {
        this.mTransOffsetX = Utils.convertDpToPixel(offset);
    }

    public void setDragOffsetY(float offset) {
        this.mTransOffsetY = Utils.convertDpToPixel(offset);
    }

    public boolean hasNoDragOffset() {
        return this.mTransOffsetX <= 0.0F && this.mTransOffsetY <= 0.0F;
    }
}
