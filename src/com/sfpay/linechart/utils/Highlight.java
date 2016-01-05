//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.sfpay.linechart.utils;

public class Highlight {
    private int mXIndex;
    private int mDataSetIndex;
    private int mStackIndex;

    public Highlight(int x, int dataSet) {
        this.mStackIndex = -1;
        this.mXIndex = x;
        this.mDataSetIndex = dataSet;
    }

    public Highlight(int x, int dataSet, int stackIndex) {
        this(x, dataSet);
        this.mStackIndex = stackIndex;
    }

    public int getDataSetIndex() {
        return this.mDataSetIndex;
    }

    public int getXIndex() {
        return this.mXIndex;
    }

    public int getStackIndex() {
        return this.mStackIndex;
    }

    public boolean equalTo(Highlight h) {
        return h == null?false:this.mDataSetIndex == h.mDataSetIndex && this.mXIndex == h.mXIndex && this.mStackIndex == h.mStackIndex;
    }
}
