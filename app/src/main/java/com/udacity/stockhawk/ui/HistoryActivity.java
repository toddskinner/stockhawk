package com.udacity.stockhawk.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.udacity.stockhawk.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static com.udacity.stockhawk.R.id.chart;

public class HistoryActivity extends AppCompatActivity {

    @BindView(R.id.stock_symbol_tv)
    TextView mStockSymbolView;

    @BindView(chart)
    LineChart mChart;

    Intent historyIntent;
    List<Entry> entries = new ArrayList<>();
    String[] xAxisValues;

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
                int arraySize = historyArray.length;
                xAxisValues = new String[arraySize];

                for (String historyEntryString : historyArray) {
                    float date = Float.parseFloat(historyEntryString.split(",")[0]);
                    float price = Float.parseFloat(historyEntryString.split(",")[1]);
                    entries.add(new Entry(date, price));
                    Timber.d(new Entry(date, price).toString());
                }
            }
        }

        LineDataSet dataSet = new LineDataSet(entries, "Share Price");

        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        LineData lineData = new LineData(dataSet);

        dataSet.setColor(Color.RED);
        dataSet.setDrawCircles(false);

        IAxisValueFormatter xAxisFormatter = new IAxisValueFormatter(){

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                long dateMillis = (long) value;
                Date date = new Date(dateMillis);
                SimpleDateFormat formattedDate = new SimpleDateFormat("MMM-dd-yyyy");
//                return formattedDate.format(date);
                return "1";
            }
        };

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(30f);
        xAxis.setLabelCount(4);
        mChart.setBackgroundColor(Color.LTGRAY);
        mChart.getAxisRight().setEnabled(false);
        mChart.getDescription().setEnabled(false);

        mChart.setLogEnabled(true);

        xAxis.setValueFormatter(xAxisFormatter);

        mChart.setData(lineData);
        mChart.invalidate();
    }
}
