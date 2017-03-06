package com.udacity.stockhawk.data;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by toddskinner on 3/6/17.
 */

public class MyValueFormatter implements IAxisValueFormatter {

    private String[] mValues;

    public MyValueFormatter(String[] values) {
        this.mValues = values;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // "value" represents the position of the label on the axis (x or y)
        return mValues[(int) value];
    }
}

