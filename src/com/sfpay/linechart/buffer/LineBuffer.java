//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.sfpay.linechart.buffer;

import com.sfpay.linechart.buffer.AbstractBuffer;
import com.sfpay.linechart.data.Entry;
import java.util.List;

public class LineBuffer extends AbstractBuffer<Entry> {
    public LineBuffer(int size) {
        super(size < 4?4:size);
    }

    public void moveTo(float x, float y) {
        if(this.index == 0) {
            this.buffer[this.index++] = x;
            this.buffer[this.index++] = y;
            this.buffer[this.index] = x;
            this.buffer[this.index + 1] = y;
        }
    }

    public void lineTo(float x, float y) {
        if(this.index == 2) {
            this.buffer[this.index++] = x;
            this.buffer[this.index++] = y;
        } else {
            float prevX = this.buffer[this.index - 2];
            float prevY = this.buffer[this.index - 1];
            this.buffer[this.index++] = prevX;
            this.buffer[this.index++] = prevY;
            this.buffer[this.index++] = x;
            this.buffer[this.index++] = y;
        }

    }

    public void feed(List<Entry> entries) {
        this.moveTo((float)((Entry)entries.get(this.mFrom)).getXIndex(), ((Entry)entries.get(this.mFrom)).getVal() * this.phaseY);
        int size = (int)Math.ceil((double)((float)(this.mTo - this.mFrom) * this.phaseX + (float)this.mFrom));
        int from = this.mFrom + 1;

        for(int i = from; i < size; ++i) {
            Entry e = (Entry)entries.get(i);
            this.lineTo((float)e.getXIndex(), e.getVal() * this.phaseY);
        }

        this.reset();
    }
}
