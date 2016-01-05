//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.sfpay.linechart.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.sfpay.linechart.components.YAxis.AxisDependency;
import com.sfpay.linechart.utils.Highlight;

public abstract class ChartData<T extends DataSet<? extends Entry>> {
	protected float mYMax = 0.0F;
	protected float mYMin = 0.0F;
	protected float mLeftAxisMax = 0.0F;
	protected float mLeftAxisMin = 0.0F;
	protected float mRightAxisMax = 0.0F;
	protected float mRightAxisMin = 0.0F;
	private float mYValueSum = 0.0F;
	private int mYValCount = 0;
	private float mXValAverageLength = 0.0F;
	protected List<String> mXVals;
	protected List<T> mDataSets;

	public ChartData() {
		this.mXVals = new ArrayList();
		this.mDataSets = new ArrayList();
	}

	public ChartData(List<String> xVals) {
		this.mXVals = xVals;
		this.mDataSets = new ArrayList();
		this.init(this.mDataSets);
	}

	public ChartData(String[] xVals) {
		this.mXVals = this.arrayToList(xVals);
		this.mDataSets = new ArrayList();
		this.init(this.mDataSets);
	}

	public ChartData(List<String> xVals, List<T> sets) {
		this.mXVals = xVals;
		this.mDataSets = sets;
		this.init(this.mDataSets);
	}

	public ChartData(String[] xVals, List<T> sets) {
		this.mXVals = this.arrayToList(xVals);
		this.mDataSets = sets;
		this.init(this.mDataSets);
	}

	private List<String> arrayToList(String[] array) {
		return Arrays.asList(array);
	}

	protected void init(List<? extends DataSet<?>> dataSets) {
		this.isLegal(dataSets);
		this.calcMinMax(dataSets);
		this.calcYValueSum(dataSets);
		this.calcYValueCount(dataSets);
		this.calcXValAverageLength();
	}

	private void calcXValAverageLength() {
		if (this.mXVals.size() <= 0) {
			this.mXValAverageLength = 1.0F;
		} else {
			float sum = 1.0F;

			for (int i = 0; i < this.mXVals.size(); ++i) {
				sum += (float) ((String) this.mXVals.get(i)).length();
			}

			this.mXValAverageLength = sum / (float) this.mXVals.size();
		}
	}

	private void isLegal(List<? extends DataSet<?>> dataSets) {
		if (dataSets != null) {
			for (int i = 0; i < dataSets.size(); ++i) {
				if (((DataSet) dataSets.get(i)).getYVals().size() > this.mXVals
						.size()) {
					throw new IllegalArgumentException(
							"One or more of the DataSet Entry arrays are longer than the x-values array of this ChartData object.");
				}
			}

		}
	}

	public void notifyDataChanged() {
		this.init(this.mDataSets);
	}

	protected void calcMinMax(List<? extends DataSet<?>> dataSets) {
		if (dataSets != null && dataSets.size() >= 1) {
			this.mYMin = ((DataSet) dataSets.get(0)).getYMin();
			this.mYMax = ((DataSet) dataSets.get(0)).getYMax();

			for (int firstLeft = 0; firstLeft < dataSets.size(); ++firstLeft) {
				if (((DataSet) dataSets.get(firstLeft)).getYMin() < this.mYMin) {
					this.mYMin = ((DataSet) dataSets.get(firstLeft)).getYMin();
				}

				if (((DataSet) dataSets.get(firstLeft)).getYMax() > this.mYMax) {
					this.mYMax = ((DataSet) dataSets.get(firstLeft)).getYMax();
				}
			}

			DataSet var6 = this.getFirstLeft();
			DataSet firstRight;
			if (var6 != null) {
				this.mLeftAxisMax = var6.getYMax();
				this.mLeftAxisMin = var6.getYMin();
				Iterator dataSet = dataSets.iterator();

				while (dataSet.hasNext()) {
					firstRight = (DataSet) dataSet.next();
					if (firstRight.getAxisDependency() == AxisDependency.LEFT) {
						if (firstRight.getYMin() < this.mLeftAxisMin) {
							this.mLeftAxisMin = firstRight.getYMin();
						}

						if (firstRight.getYMax() > this.mLeftAxisMax) {
							this.mLeftAxisMax = firstRight.getYMax();
						}
					}
				}
			}

			firstRight = this.getFirstRight();
			if (firstRight != null) {
				this.mRightAxisMax = firstRight.getYMax();
				this.mRightAxisMin = firstRight.getYMin();
				Iterator var5 = dataSets.iterator();

				while (var5.hasNext()) {
					DataSet var7 = (DataSet) var5.next();
					if (var7.getAxisDependency() == AxisDependency.RIGHT) {
						if (var7.getYMin() < this.mRightAxisMin) {
							this.mRightAxisMin = var7.getYMin();
						}

						if (var7.getYMax() > this.mRightAxisMax) {
							this.mRightAxisMax = var7.getYMax();
						}
					}
				}
			}

			this.handleEmptyAxis((T) var6, (T) firstRight);
		} else {
			this.mYMax = 0.0F;
			this.mYMin = 0.0F;
		}

	}

	protected void calcYValueSum(List<? extends DataSet<?>> dataSets) {
		this.mYValueSum = 0.0F;
		if (dataSets != null) {
			for (int i = 0; i < dataSets.size(); ++i) {
				this.mYValueSum += Math.abs(((DataSet) dataSets.get(i))
						.getYValueSum());
			}

		}
	}

	protected void calcYValueCount(List<? extends DataSet<?>> dataSets) {
		this.mYValCount = 0;
		if (dataSets != null) {
			int count = 0;

			for (int i = 0; i < dataSets.size(); ++i) {
				count += ((DataSet) dataSets.get(i)).getEntryCount();
			}

			this.mYValCount = count;
		}
	}

	public int getDataSetCount() {
		return this.mDataSets == null ? 0 : this.mDataSets.size();
	}

	public float getYMin() {
		return this.mYMin;
	}

	public float getYMin(AxisDependency axis) {
		return axis == AxisDependency.LEFT ? this.mLeftAxisMin
				: this.mRightAxisMin;
	}

	public float getYMax() {
		return this.mYMax;
	}

	public float getYMax(AxisDependency axis) {
		return axis == AxisDependency.LEFT ? this.mLeftAxisMax
				: this.mRightAxisMax;
	}

	public float getXValAverageLength() {
		return this.mXValAverageLength;
	}

	public float getYValueSum() {
		return this.mYValueSum;
	}

	public int getYValCount() {
		return this.mYValCount;
	}

	public List<String> getXVals() {
		return this.mXVals;
	}

	public List<T> getDataSets() {
		return this.mDataSets;
	}

	protected int getDataSetIndexByLabel(List<T> dataSets, String label,
			boolean ignorecase) {
		int i;
		if (ignorecase) {
			for (i = 0; i < dataSets.size(); ++i) {
				if (label.equalsIgnoreCase(((DataSet) dataSets.get(i))
						.getLabel())) {
					return i;
				}
			}
		} else {
			for (i = 0; i < dataSets.size(); ++i) {
				if (label.equals(((DataSet) dataSets.get(i)).getLabel())) {
					return i;
				}
			}
		}

		return -1;
	}

	public int getXValCount() {
		return this.mXVals.size();
	}

	public Entry getEntryForHighlight(Highlight highlight) {
		return ((DataSet) this.mDataSets.get(highlight.getDataSetIndex()))
				.getEntryForXIndex(highlight.getXIndex());
	}

	public T getDataSetByLabel(String label, boolean ignorecase) {
		int index = this.getDataSetIndexByLabel(this.mDataSets, label,
				ignorecase);
		return (T) (index >= 0 && index < this.mDataSets.size() ? (DataSet) this.mDataSets
				.get(index) : null);
	}

	public T getDataSetByIndex(int index) {
		return (T) (this.mDataSets != null && index >= 0
				&& index < this.mDataSets.size() ? (DataSet) this.mDataSets
				.get(index) : null);
	}

	private void handleEmptyAxis(T firstLeft, T firstRight) {
		if (firstLeft == null) {
			this.mLeftAxisMax = this.mRightAxisMax;
			this.mLeftAxisMin = this.mRightAxisMin;
		} else if (firstRight == null) {
			this.mRightAxisMax = this.mLeftAxisMax;
			this.mRightAxisMin = this.mLeftAxisMin;
		}

	}

	public int getIndexOfDataSet(T dataSet) {
		for (int i = 0; i < this.mDataSets.size(); ++i) {
			if (this.mDataSets.get(i) == dataSet) {
				return i;
			}
		}

		return -1;
	}

	public T getFirstLeft() {
		Iterator var2 = this.mDataSets.iterator();

		while (var2.hasNext()) {
			DataSet dataSet = (DataSet) var2.next();
			if (dataSet.getAxisDependency() == AxisDependency.LEFT) {
				return (T) dataSet;
			}
		}

		return null;
	}

	public T getFirstRight() {
		Iterator var2 = this.mDataSets.iterator();

		while (var2.hasNext()) {
			DataSet dataSet = (DataSet) var2.next();
			if (dataSet.getAxisDependency() == AxisDependency.RIGHT) {
				return (T) dataSet;
			}
		}

		return null;
	}

	public void clearValues() {
		this.mDataSets.clear();
		this.notifyDataChanged();
	}
}
