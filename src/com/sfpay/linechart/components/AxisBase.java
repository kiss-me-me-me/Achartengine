//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.sfpay.linechart.components;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import com.sfpay.linechart.components.ComponentBase;
import com.sfpay.linechart.utils.Utils;

public abstract class AxisBase extends ComponentBase {
    private int mGridColor = Color.parseColor("#efefef");
    private float mGridLineWidth = 1.0F;
    protected boolean mDrawGridLines = true;
    protected boolean mDrawAxisLine = true;
    protected boolean mDrawLabels = true;
    private DashPathEffect mGridDashPathEffect = null;
    protected boolean mDrawLimitLineBehindData = false;

    public AxisBase() {
        this.mTextSize = Utils.convertDpToPixel(10.0F);
    }

    public void setDrawGridLines(boolean enabled) {
        this.mDrawGridLines = enabled;
    }

    public boolean isDrawGridLinesEnabled() {
        return this.mDrawGridLines;
    }

    public void setDrawAxisLine(boolean enabled) {
        this.mDrawAxisLine = enabled;
    }

    public boolean isDrawAxisLineEnabled() {
        return this.mDrawAxisLine;
    }

    public void setGridColor(int color) {
        this.mGridColor = color;
    }

    public int getGridColor() {
        return this.mGridColor;
    }

    public void setGridLineWidth(float width) {
        this.mGridLineWidth = Utils.convertDpToPixel(width);
    }

    public float getGridLineWidth() {
        return this.mGridLineWidth;
    }

    public boolean isDrawLabelsEnabled() {
        return this.mDrawLabels;
    }

    public abstract String getLongestLabel();

    public void enableGridDashedLine(float lineLength, float spaceLength, float phase) {
        this.mGridDashPathEffect = new DashPathEffect(new float[]{lineLength, spaceLength}, phase);
    }

    public DashPathEffect getGridDashPathEffect() {
        return this.mGridDashPathEffect;
    }
}
