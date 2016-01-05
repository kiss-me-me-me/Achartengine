package com.sfpay.linechart.utils;

import java.util.ArrayList;
import java.util.List;

public class ColorTemplate {
	public static List<Integer> createColors(int[] colors) {
		List result = new ArrayList();

		int[] arrayOfInt = colors;
		int j = colors.length;
		for (int i = 0; i < j; i++) {
			i = arrayOfInt[i];
			result.add(Integer.valueOf(i));
		}
		return result;
	}
}
