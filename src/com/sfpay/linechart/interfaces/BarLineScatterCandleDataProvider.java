//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.sfpay.linechart.interfaces;

import com.sfpay.linechart.components.YAxis.AxisDependency;
import com.sfpay.linechart.interfaces.ChartInterface;
import com.sfpay.linechart.utils.Transformer;

public interface BarLineScatterCandleDataProvider extends ChartInterface {
	Transformer getTransformer(AxisDependency var1);

	boolean isInverted(AxisDependency var1);
}
