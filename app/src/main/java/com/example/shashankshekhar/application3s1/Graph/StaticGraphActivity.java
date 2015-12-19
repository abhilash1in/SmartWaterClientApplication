package com.example.shashankshekhar.application3s1.Graph;

import android.graphics.DashPathEffect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.androidplot.util.PixelUtils;
import com.androidplot.xy.CatmullRomInterpolator;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.example.shashankshekhar.application3s1.R;

import java.util.Arrays;

public class StaticGraphActivity extends AppCompatActivity {
    private XYPlot plot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        plot = (XYPlot) findViewById(R.id.plot);
        Number[] series1Num = {1, 4, 2, 8, 4, 16, 8, 32, 16, 64};
        Number[] series2Num = {5, 2, 10, 5, 20, 10, 40, 20, 80, 40};
        XYSeries series1= new SimpleXYSeries(Arrays.asList(series1Num),SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,
                "Series1");
        XYSeries series2= new SimpleXYSeries(Arrays.asList(series2Num),SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,
                "Series2");
        LineAndPointFormatter series1Format = new LineAndPointFormatter();
        series1Format.setPointLabelFormatter(new PointLabelFormatter());
        series1Format.configure(getApplicationContext(),
                R.xml.line_point_formatter_with_labels);

        LineAndPointFormatter series2Format = new LineAndPointFormatter();
        series2Format.setPointLabelFormatter(new PointLabelFormatter());
        series2Format.configure(getApplicationContext(),
                R.xml.line_point_formatter_with_labels_2);

//        series2Format.getLinePaint().setPathEffect(
//                new DashPathEffect(new float[]{
//                        // always use DP when specifying pixel sizes, to keep things consistent across devices:
//                        PixelUtils.dpToPix(20),
//                        PixelUtils.dpToPix(15)}, 0));
        series1Format.setInterpolationParams(
                new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));

        series2Format.setInterpolationParams(
                new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));
        plot.addSeries(series1, series1Format);
        plot.addSeries(series2, series2Format);
        plot.setTicksPerRangeLabel(3);
        plot.getGraphWidget().setDomainLabelOrientation(-45);
    }
}
