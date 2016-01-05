//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.sfpay.linechart.data.filter;

import java.util.ArrayList;
import java.util.List;

import com.sfpay.linechart.data.Entry;

public class Approximator {
	private Approximator.ApproximatorType mType;
	private double mTolerance;
	private float mScaleRatio;
	private float mDeltaRatio;
	private boolean[] keep;

	public Approximator(Approximator.ApproximatorType type, double tolerance) {
		this.mType = Approximator.ApproximatorType.DOUGLAS_PEUCKER;
		this.mTolerance = 0.0D;
		this.mScaleRatio = 1.0F;
		this.mDeltaRatio = 1.0F;
		this.setup(type, tolerance);
	}

	public void setup(Approximator.ApproximatorType type, double tolerance) {
		this.mType = type;
		this.mTolerance = tolerance;
	}

	public List<Entry> filter(List<Entry> points, double tolerance) {
		if (tolerance <= 0.0D) {
			return points;
		} else {
			this.keep = new boolean[points.size()];
			switch (this.mType.ordinal()) {
			case 1:
				return points;
			case 2:
				return this.reduceWithDouglasPeuker(points, tolerance);
			default:
				return points;
			}
		}
	}

	private List<Entry> reduceWithDouglasPeuker(List<Entry> entries,
			double epsilon) {
		if (epsilon > 0.0D && entries.size() >= 3) {
			this.keep[0] = true;
			this.keep[entries.size() - 1] = true;
			this.algorithmDouglasPeucker(entries, epsilon, 0,
					entries.size() - 1);
			ArrayList reducedEntries = new ArrayList();

			for (int i = 0; i < entries.size(); ++i) {
				if (this.keep[i]) {
					Entry curEntry = (Entry) entries.get(i);
					reducedEntries.add(new Entry(curEntry.getVal(), curEntry
							.getXIndex()));
				}
			}

			return reducedEntries;
		} else {
			return entries;
		}
	}

	private void algorithmDouglasPeucker(List<Entry> entries, double epsilon,
			int start, int end) {
		if (end > start + 1) {
			int maxDistIndex = 0;
			double distMax = 0.0D;
			Entry firstEntry = (Entry) entries.get(start);
			Entry lastEntry = (Entry) entries.get(end);

			for (int i = start + 1; i < end; ++i) {
				double dist = this.calcAngleBetweenLines(firstEntry, lastEntry,
						firstEntry, (Entry) entries.get(i));
				if (dist > distMax) {
					distMax = dist;
					maxDistIndex = i;
				}
			}

			if (distMax > epsilon) {
				this.keep[maxDistIndex] = true;
				this.algorithmDouglasPeucker(entries, epsilon, start,
						maxDistIndex);
				this.algorithmDouglasPeucker(entries, epsilon, maxDistIndex,
						end);
			}

		}
	}

	public double calcAngleBetweenLines(Entry start1, Entry end1, Entry start2,
			Entry end2) {
		double angle1 = this.calcAngleWithRatios(start1, end1);
		double angle2 = this.calcAngleWithRatios(start2, end2);
		return Math.abs(angle1 - angle2);
	}

	public double calcAngleWithRatios(Entry p1, Entry p2) {
		float dx = (float) p2.getXIndex() * this.mDeltaRatio
				- (float) p1.getXIndex() * this.mDeltaRatio;
		float dy = p2.getVal() * this.mScaleRatio - p1.getVal()
				* this.mScaleRatio;
		double angle = Math.atan2((double) dy, (double) dx) * 180.0D / 3.141592653589793D;
		return angle;
	}

	public static enum ApproximatorType {
		NONE, DOUGLAS_PEUCKER;

		private ApproximatorType() {
		}
	}
}
