package com.example.stockapp.stockview;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.stockapp.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LineGraphFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LineGraphFragment extends Fragment {
    private LineChart chart;

    public LineGraphFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LineGraphFragment.
     */
    public static LineGraphFragment newInstance(String symbol, Double priceChange) {
        LineGraphFragment fragment = new LineGraphFragment();
        Bundle args = new Bundle();
        args.putString(StockViewActivity.SYMBOL, symbol);
        args.putDouble(StockViewActivity.PRICE_CHANGE, priceChange);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_line_graph, container, false);
        {
            chart = view.findViewById(R.id.chart);
            chart.getDescription().setEnabled(false);
            chart.setDrawGridBackground(false);
            chart.setDragEnabled(true);
            chart.setPinchZoom(true);
        }

        XAxis xAxis;
        {
            xAxis = chart.getXAxis();
        }

        YAxis yAxis;
        {
            yAxis = chart.getAxisLeft();

            chart.getAxisRight().setEnabled(false);

            // axis range
            yAxis.setAxisMaximum(200f);
            yAxis.setAxisMinimum(0f);
        }

        double priceChange = 0;
        boolean isNegative = false;
        if (getArguments() != null) {
            priceChange = getArguments().getDouble(StockViewActivity.PRICE_CHANGE);
            isNegative = priceChange < 0.0;
        }

        setData(20, 180, isNegative);

        chart.animateX(1500);
        chart.getLegend().setEnabled(false);

        return view;
    }

    private void setData(int count, float range, boolean isNegative) {

        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < count; i++) {

            float val = (float) (Math.random() * range);
            values.add(new Entry(i, val, getResources().getDrawable(R.drawable.star)));
        }

        LineDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            set1.notifyDataSetChanged();
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "AAPL");

            set1.setDrawIcons(false);
            if (isNegative) {
                set1.setColor(Color.RED);
            } else {
                set1.setColor(Color.GREEN);
            }

            // line thickness and point size
            set1.setLineWidth(2f);
            set1.setDrawCircles(false);

            // draw points as solid circles
            set1.setDrawCircleHole(false);

            // text size of values
            set1.setValueTextSize(0f);

            // set the filled area
            set1.setDrawFilled(true);
            set1.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return chart.getAxisLeft().getAxisMinimum();
                }
            });

            // set color of filled area
            if (Utils.getSDKInt() >= 18) {
                // drawables only supported on api level 18 and above
                Drawable drawable;
                if (isNegative) {
                    drawable = ContextCompat.getDrawable(this.getContext(), R.drawable.fade_red);
                } else {
                    drawable = ContextCompat.getDrawable(this.getContext(), R.drawable.fade_green);
                }
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.BLACK);
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1); // add the data sets

            // create a data object with the data sets
            LineData data = new LineData(dataSets);

            // set data
            chart.setData(data);
        }
    }
}