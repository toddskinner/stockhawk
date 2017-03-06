package com.udacity.stockhawk.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.MyValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.udacity.stockhawk.R.id.chart;

public class HistoryActivity extends AppCompatActivity {

    @BindView(R.id.stock_symbol_tv)
    TextView mStockSymbolView;

    @BindView(chart)
    LineChart mChart;

    Intent historyIntent;
    List<Entry> entries = new ArrayList<>();
    String[] formattedDateValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);

        historyIntent = getIntent();
        if (historyIntent != null) {

            if (historyIntent.hasExtra("symbol")) {
                String symbol = historyIntent.getStringExtra("symbol");
                mStockSymbolView.setText(symbol);
            }

            if (historyIntent.hasExtra("history")) {
                String history = historyIntent.getStringExtra("history");
                String[] historyArray = history.split("\n");
                formattedDateValues = new String[historyArray.length];

                float[] valuesX = new float[historyArray.length];
                float[] valuesY = new float[historyArray.length];

                for (int i=0 ; i< historyArray.length ; i++) {

                    valuesX[i] = i;
                    valuesY[i] = Float.valueOf(historyArray[historyArray.length - 1 - i].split(",")[1]);

                    float floatDate = Float.valueOf(historyArray[historyArray.length - 1 - i].split(",")[0]);
                    long dateMillis = (long) floatDate;
                    Date date = new Date(dateMillis);
                    SimpleDateFormat formattedDate = new SimpleDateFormat("MMM-yy");
                    formattedDateValues[i] = formattedDate.format(date);
                }
                for (int i=0 ; i< valuesX.length ; i++) {
                    entries.add(new Entry(valuesX[i], valuesY[i]));
                }


            }
        }

        LineDataSet dataSet = new LineDataSet(entries, "Share Price");

        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);


        dataSet.setColor(Color.BLUE);
//        dataSet.setCircleColor(Color.RED);
        dataSet.setDrawCircles(false);

//        IAxisValueFormatter xAxisFormatter = new IAxisValueFormatter(){
//
//            @Override
//            public String getFormattedValue(float value, AxisBase axis) {
//                long dateMillis = (long) value;
//                Date date = new Date(dateMillis);
//                SimpleDateFormat formattedDate = new SimpleDateFormat("MMM-yyyy");
//                return formattedDate.format(date);
//            }
//        };

        LineData lineData = new LineData(dataSet);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setGranularity(30f);
        mChart.setBackgroundColor(Color.LTGRAY);
        mChart.getAxisRight().setEnabled(false);
        mChart.getDescription().setEnabled(false);

        xAxis.setValueFormatter(new MyValueFormatter(formattedDateValues));
        xAxis.setLabelCount(5);

        mChart.setData(lineData);
        mChart.invalidate();
    }
}
