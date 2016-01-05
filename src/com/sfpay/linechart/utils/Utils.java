//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.sfpay.linechart.utils;

import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import com.sfpay.linechart.components.YAxis.AxisDependency;
import com.sfpay.linechart.utils.SelInfo;
import java.util.List;

public abstract class Utils {
	private static DisplayMetrics mMetrics;
	private static final int[] POW_10 = new int[] { 1, 10, 100, 1000, 10000,
			100000, 1000000, 10000000, 100000000, 1000000000 };

	public Utils() {
	}

	public static void init(Resources res) {
		mMetrics = res.getDisplayMetrics();
	}

	public static float convertDpToPixel(float dp) {
		if (mMetrics == null) {
			return dp;
		} else {
			DisplayMetrics metrics = mMetrics;
			float px = dp * ((float) metrics.densityDpi / 160.0F);
			return px;
		}
	}

	public static int calcTextWidth(Paint paint, String demoText) {
		return (int) paint.measureText(demoText);
	}

	public static int calcTextHeight(Paint paint, String demoText) {
		Rect r = new Rect();
		paint.getTextBounds(demoText, 0, demoText.length(), r);
		return r.height();
	}

	public static String formatNumber(float number, int digitCount,
			boolean separateThousands) {
		char[] out = new char[35];
		boolean neg = false;
		if (number == 0.0F) {
			return "0";
		} else {
			boolean zero = false;
			if (number < 1.0F && number > -1.0F) {
				zero = true;
			}

			if (number < 0.0F) {
				neg = true;
				number = -number;
			}

			if (digitCount > POW_10.length) {
				digitCount = POW_10.length - 1;
			}

			number *= (float) POW_10[digitCount];
			long lval = (long) Math.round(number);
			int ind = out.length - 1;
			int charCount = 0;
			boolean decimalPointAdded = false;

			int start;
			while (lval != 0L || charCount < digitCount + 1) {
				start = (int) (lval % 10L);
				lval /= 10L;
				out[ind--] = (char) (start + 48);
				++charCount;
				if (charCount == digitCount) {
					out[ind--] = 46;
					++charCount;
					decimalPointAdded = true;
				} else if (separateThousands && lval != 0L
						&& charCount > digitCount) {
					if (decimalPointAdded) {
						if ((charCount - digitCount) % 4 == 0) {
							out[ind--] = 44;
							++charCount;
						}
					} else if ((charCount - digitCount) % 4 == 3) {
						out[ind--] = 44;
						++charCount;
					}
				}
			}

			if (zero) {
				out[ind--] = 48;
				++charCount;
			}

			if (neg) {
				out[ind--] = 45;
				++charCount;
			}

			start = out.length - charCount;
			return String.valueOf(out, start, out.length - start);
		}
	}

	public static float roundToNextSignificant(double number) {
		float d = (float) Math.ceil((double) ((float) Math
				.log10(number < 0.0D ? -number : number)));
		int pw = 1 - (int) d;
		float magnitude = (float) Math.pow(10.0D, (double) pw);
		long shifted = Math.round(number * (double) magnitude);
		return (float) shifted / magnitude;
	}

	public static int getDecimals(float number) {
		float i = roundToNextSignificant((double) number);
		return (int) Math.ceil(-Math.log10((double) i)) + 2;
	}

	public static int[] convertIntegers(List<Integer> integers) {
		int[] ret = new int[integers.size()];

		for (int i = 0; i < ret.length; ++i) {
			ret[i] = ((Integer) integers.get(i)).intValue();
		}

		return ret;
	}

	public static String[] convertStrings(List<String> strings) {
		String[] ret = new String[strings.size()];

		for (int i = 0; i < ret.length; ++i) {
			ret[i] = (String) strings.get(i);
		}

		return ret;
	}

	public static int getClosestDataSetIndex(List<SelInfo> valsAtIndex,
			float val, AxisDependency axis) {
		int index = -1;
		float distance = 3.4028235E38F;

		for (int i = 0; i < valsAtIndex.size(); ++i) {
			SelInfo sel = (SelInfo) valsAtIndex.get(i);
			if (axis == null || sel.dataSet.getAxisDependency() == axis) {
				float cdistance = Math.abs(sel.val - val);
				if (cdistance < distance) {
					index = ((SelInfo) valsAtIndex.get(i)).dataSetIndex;
					distance = cdistance;
				}
			}
		}

		return index;
	}

	public static float getMinimumDistance(List<SelInfo> valsAtIndex,
			float val, AxisDependency axis) {
		float distance = 3.4028235E38F;

		for (int i = 0; i < valsAtIndex.size(); ++i) {
			SelInfo sel = (SelInfo) valsAtIndex.get(i);
			if (sel.dataSet.getAxisDependency() == axis) {
				float cdistance = Math.abs(sel.val - val);
				if (cdistance < distance) {
					distance = cdistance;
				}
			}
		}

		return distance;
	}
}
