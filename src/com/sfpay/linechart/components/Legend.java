//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.sfpay.linechart.components;

import java.util.List;

import android.graphics.Paint;

import com.sfpay.linechart.utils.Utils;

public class Legend extends ComponentBase {
	private int[] mColors;
	private String[] mLabels;
	private Legend.LegendPosition mPosition;
	private Legend.LegendForm mShape;
	private float mFormSize;
	private float mXEntrySpace;
	private float mYEntrySpace;
	private float mFormToTextSpace;
	private float mStackSpace;
	public float mNeededWidth;
	public float mNeededHeight;
	public float mTextHeightMax;
	public float mTextWidthMax;

	public Legend() {
		this.mPosition = Legend.LegendPosition.BELOW_CHART_LEFT;
		this.mShape = Legend.LegendForm.SQUARE;
		this.mFormSize = 8.0F;
		this.mXEntrySpace = 6.0F;
		this.mYEntrySpace = 5.0F;
		this.mFormToTextSpace = 5.0F;
		this.mStackSpace = 3.0F;
		this.mNeededWidth = 0.0F;
		this.mNeededHeight = 0.0F;
		this.mTextHeightMax = 0.0F;
		this.mTextWidthMax = 0.0F;
		this.mFormSize = Utils.convertDpToPixel(8.0F);
		this.mXEntrySpace = Utils.convertDpToPixel(6.0F);
		this.mYEntrySpace = Utils.convertDpToPixel(5.0F);
		this.mFormToTextSpace = Utils.convertDpToPixel(5.0F);
		this.mTextSize = Utils.convertDpToPixel(10.0F);
		this.mStackSpace = Utils.convertDpToPixel(3.0F);
	}

	public void setColors(List<Integer> colors) {
		this.mColors = Utils.convertIntegers(colors);
	}

	public void setLabels(List<String> labels) {
		this.mLabels = Utils.convertStrings(labels);
	}

	public float getMaximumEntryWidth(Paint p) {
		float max = 0.0F;

		for (int i = 0; i < this.mLabels.length; ++i) {
			if (this.mLabels[i] != null) {
				float length = (float) Utils.calcTextWidth(p, this.mLabels[i]);
				if (length > max) {
					max = length;
				}
			}
		}

		return max + this.mFormSize + this.mFormToTextSpace;
	}

	public float getMaximumEntryHeight(Paint p) {
		float max = 0.0F;

		for (int i = 0; i < this.mLabels.length; ++i) {
			if (this.mLabels[i] != null) {
				float length = (float) Utils.calcTextHeight(p, this.mLabels[i]);
				if (length > max) {
					max = length;
				}
			}
		}

		return max;
	}

	public int[] getColors() {
		return this.mColors;
	}

	public Legend.LegendPosition getPosition() {
		return this.mPosition;
	}

	public Legend.LegendForm getForm() {
		return this.mShape;
	}

	public float getFormSize() {
		return this.mFormSize;
	}

	public float getFullWidth(Paint labelpaint) {
		float width = 0.0F;

		for (int i = 0; i < this.mLabels.length; ++i) {
			if (this.mLabels[i] != null) {
				if (this.mColors[i] != -2) {
					width += this.mFormSize + this.mFormToTextSpace;
				}

				width += (float) Utils.calcTextWidth(labelpaint,
						this.mLabels[i]);
				if (i < this.mLabels.length - 1) {
					width += this.mXEntrySpace;
				}
			} else {
				width += this.mFormSize;
				if (i < this.mLabels.length - 1) {
					width += this.mStackSpace;
				}
			}
		}

		return width;
	}

	public float getFullHeight(Paint labelpaint) {
		float height = 0.0F;

		for (int i = 0; i < this.mLabels.length; ++i) {
			if (this.mLabels[i] != null) {
				height += (float) Utils.calcTextHeight(labelpaint,
						this.mLabels[i]);
				if (i < this.mLabels.length - 1) {
					height += this.mYEntrySpace;
				}
			}
		}

		return height;
	}

	public void calculateDimensions(Paint labelpaint) {
		if (this.mPosition != Legend.LegendPosition.RIGHT_OF_CHART
				&& this.mPosition != Legend.LegendPosition.RIGHT_OF_CHART_CENTER
				&& this.mPosition != Legend.LegendPosition.LEFT_OF_CHART
				&& this.mPosition != Legend.LegendPosition.LEFT_OF_CHART_CENTER
				&& this.mPosition != Legend.LegendPosition.PIECHART_CENTER) {
			this.mNeededWidth = this.getFullWidth(labelpaint);
			this.mNeededHeight = this.getMaximumEntryHeight(labelpaint);
			this.mTextWidthMax = this.getMaximumEntryWidth(labelpaint);
			this.mTextHeightMax = this.mNeededHeight;
		} else {
			this.mNeededWidth = this.getMaximumEntryWidth(labelpaint);
			this.mNeededHeight = this.getFullHeight(labelpaint);
			this.mTextWidthMax = this.mNeededWidth;
			this.mTextHeightMax = this.getMaximumEntryHeight(labelpaint);
		}

	}

	public static enum LegendDirection {
		LEFT_TO_RIGHT, RIGHT_TO_LEFT;

		private LegendDirection() {
		}
	}

	public static enum LegendForm {
		SQUARE, CIRCLE, LINE;

		private LegendForm() {
		}
	}

	public static enum LegendPosition {
		RIGHT_OF_CHART, RIGHT_OF_CHART_CENTER, RIGHT_OF_CHART_INSIDE, LEFT_OF_CHART, LEFT_OF_CHART_CENTER, LEFT_OF_CHART_INSIDE, BELOW_CHART_LEFT, BELOW_CHART_RIGHT, BELOW_CHART_CENTER, PIECHART_CENTER;

		private LegendPosition() {
		}
	}
}
