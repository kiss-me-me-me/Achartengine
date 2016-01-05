//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.sfpay.linechart.renderer;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Typeface;

import com.sfpay.linechart.components.Legend;
import com.sfpay.linechart.data.ChartData;
import com.sfpay.linechart.data.DataSet;
import com.sfpay.linechart.utils.Utils;
import com.sfpay.linechart.utils.ViewPortHandler;

public class LegendRenderer extends Renderer {
	protected Paint mLegendLabelPaint;
	protected Paint mLegendFormPaint;
	protected Legend mLegend;

	public LegendRenderer(ViewPortHandler viewPortHandler, Legend legend) {
		super(viewPortHandler);
		this.mLegend = legend;
		this.mLegendLabelPaint = new Paint(1);
		this.mLegendLabelPaint.setTextSize(Utils.convertDpToPixel(9.0F));
		this.mLegendLabelPaint.setTextAlign(Align.LEFT);
		this.mLegendFormPaint = new Paint(1);
		this.mLegendFormPaint.setStyle(Style.FILL);
		this.mLegendFormPaint.setStrokeWidth(3.0F);
	}

	public void computeLegend(ChartData<?> data) {
		ArrayList labels = new ArrayList();
		ArrayList colors = new ArrayList();

		for (int tf = 0; tf < data.getDataSetCount(); ++tf) {
			DataSet dataSet = data.getDataSetByIndex(tf);
			List clrs = dataSet.getColors();
			int entryCount = dataSet.getEntryCount();

			for (int j = 0; j < clrs.size() && j < entryCount; ++j) {
				if (j < clrs.size() - 1 && j < entryCount - 1) {
					labels.add((Object) null);
				} else {
					String label = data.getDataSetByIndex(tf).getLabel();
					labels.add(label);
				}

				colors.add((Integer) clrs.get(j));
			}
		}

		this.mLegend.setColors(colors);
		this.mLegend.setLabels(labels);
		Typeface var10 = this.mLegend.getTypeface();
		if (var10 != null) {
			this.mLegendLabelPaint.setTypeface(var10);
		}

		this.mLegendLabelPaint.setTextSize(this.mLegend.getTextSize());
		this.mLegendLabelPaint.setColor(this.mLegend.getTextColor());
		this.mLegend.calculateDimensions(this.mLegendLabelPaint);
	}

	protected void drawForm(Canvas c, float x, float y, int index, Legend legend) {
		if (legend.getColors()[index] != -2) {
			this.mLegendFormPaint.setColor(legend.getColors()[index]);
			float formsize = legend.getFormSize();
			float half = formsize / 2.0F;
			switch (legend.getForm().ordinal()) {
			case 1:
				c.drawRect(x, y - half, x + formsize, y + half,
						this.mLegendFormPaint);
				break;
			case 2:
				c.drawCircle(x + half, y, half, this.mLegendFormPaint);
				break;
			case 3:
				c.drawLine(x, y, x + formsize, y, this.mLegendFormPaint);
			}

		}
	}

	protected void drawLabel(Canvas c, float x, float y, String label) {
		c.drawText(label, x, y, this.mLegendLabelPaint);
	}
}
