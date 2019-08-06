package com.k1a2.schoolcalculator.fragment;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.k1a2.schoolcalculator.CalculateGrade;
import com.k1a2.schoolcalculator.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AnalyzeGradeFragment extends Fragment {

    private View root;
    private LineChart chart_all = null;
    private LineChart chart_all2 = null;
    private TextView text_a11 = null;
    private TextView text_a12 = null;
    private TextView text_a21 = null;
    private TextView text_a22 = null;
    private TextView text_a31 = null;
    private TextView text_a32 = null;
    private TextView text_opinion1 = null;
    private TextView text_opinion2 = null;
    private TextView text_opinion3 = null;

    private CalculateGrade calculateGrade = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_analye_grade, container, false);

        calculateGrade = new CalculateGrade(getContext());

        chart_all = root.findViewById(R.id.analye_chart_analyze);
        chart_all2 = root.findViewById(R.id.analye_chart_analyzeA);
        text_a11 = root.findViewById(R.id.fragment_gradeAll_11);
        text_a12 = root.findViewById(R.id.fragment_gradeAll_12);
        text_a21 = root.findViewById(R.id.fragment_gradeAll_21);
        text_a22 = root.findViewById(R.id.fragment_gradeAll_22);
        text_a31 = root.findViewById(R.id.fragment_gradeAll_31);
        text_a32 = root.findViewById(R.id.fragment_gradeAll_32);
        text_opinion1 = root.findViewById(R.id.fragment_opinion_1);
        text_opinion2 = root.findViewById(R.id.fragment_opinion_2);
        text_opinion3 = root.findViewById(R.id.fragment_opinion_3);

        chart_all.setDragEnabled(true);
        chart_all.setScaleEnabled(false);

        setChart_all();
        setChartGrade();

        return root;
    }

    private void setChart_all() {
        chart_all2.setDragEnabled(true);
        chart_all2.setScaleEnabled(false);

        YAxis leftAxis = chart_all2.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.setAxisMaximum(9.5f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.enableGridDashedLine(10f, 10f, 0);
        leftAxis.setDrawLimitLinesBehindData(true);
        leftAxis.setInverted(true);
        leftAxis.setDrawAxisLine(false);

        chart_all2.getAxisRight().setEnabled(false);



        final ArrayList<Entry> yvalue = new ArrayList<>();
        if (calculateGrade.getResult11() == 0) {
            yvalue.add(new Entry(0, Float.NaN));
        } else {
            yvalue.add(new Entry(0, calculateGrade.getResult11()));
        }
        if (calculateGrade.getResult12() == 0) {
            yvalue.add(new Entry(1, Float.NaN));
        } else {
            yvalue.add(new Entry(1, calculateGrade.getResult12()));
        }
        if (calculateGrade.getResult21() == 0) {
            yvalue.add(new Entry(2, Float.NaN));
        } else {
            yvalue.add(new Entry(2, calculateGrade.getResult21()));
        }
        if (calculateGrade.getResult22() == 0) {
            yvalue.add(new Entry(3, Float.NaN));
        } else {
            yvalue.add(new Entry(3, calculateGrade.getResult22()));
        }
        if (calculateGrade.getResult31() == 0) {
            yvalue.add(new Entry(4, Float.NaN));
        } else {
            yvalue.add(new Entry(4, calculateGrade.getResult31()));
        }
        if (calculateGrade.getResult32() == 0) {
            yvalue.add(new Entry(5, Float.NaN));
        } else {
            yvalue.add(new Entry(5, calculateGrade.getResult32()));
        }
        LineDataSet set1 = new LineDataSet(yvalue, "성적");

        set1.setFillAlpha(110);
        set1.setLineWidth(3f);
        //set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set1.setCircleRadius(5f);
        set1.setColors(ContextCompat.getColor(getContext(), R.color.colorChartLine));
        set1.setCircleColors(ContextCompat.getColor(getContext(), R.color.colorChartLine));
        set1.setValueTextSize(10f);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        LineData data = new LineData(dataSets);

        chart_all2.setData(data);

        String[] values = new String[] {"1-1", "1-2", "2-1", "2-2", "3-1", "3-2", ""};

        XAxis xAxis = chart_all2.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {

            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return values[(int) value];
            }
        });
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLinesBehindData(false);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMinimum(-0.2f);
        xAxis.setAxisMaximum(5.2f);
        xAxis.setAxisLineWidth(1f);

        Description description = new Description();
        description.setText("");

        chart_all2.setDescription(description);
        chart_all2.setHighlightPerDragEnabled(false);
        chart_all2.setHighlightPerTapEnabled(false);
        chart_all2.animateY(1800, Easing.EaseOutSine);
        chart_all2.invalidate();
    }

    private void setChartGrade() {
        YAxis leftAxis = chart_all.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.setAxisMaximum(9.5f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.enableGridDashedLine(10f, 10f, 0);
        leftAxis.setDrawLimitLinesBehindData(true);
        leftAxis.setInverted(true);
        leftAxis.setDrawAxisLine(false);

        chart_all.getAxisRight().setEnabled(false);

        final ArrayList<Entry> g1 = new ArrayList<>();
        if (calculateGrade.getResult11() == 0) {
            g1.add(new Entry(0, Float.NaN));
        } else {
            g1.add(new Entry(0, calculateGrade.getResult11()));
        }
        if (calculateGrade.getResult12() == 0) {
            g1.add(new Entry(1, Float.NaN));
        } else {
            g1.add(new Entry(1, calculateGrade.getResult12()));
        }
        text_a11.setText(String.valueOf(calculateGrade.getResult11()));
        text_a12.setText(String.valueOf(calculateGrade.getResult12()));
        LineDataSet set1 = new LineDataSet(g1, "1학년");

        if (calculateGrade.getResult11() != 0&&calculateGrade.getResult12() != 0) {
            float a = calculateGrade.getResult11();
            float b =calculateGrade.getResult12();
            if (a<b) {
                text_opinion1.setText(Html.fromHtml("1학년 성적은 " + "<strong><font color=\'#F23535\'>하락세</font></strong>" + "군요. 좀더 노력해 보세요."));
            } else {
                text_opinion1.setText(Html.fromHtml("1학년 성적은 " + "<strong><font color=\'#0B508C\'>상승세</font></strong>" + "군요. 지금처럼만 공부합시다."));
            }
        } else {
            text_opinion1.setText("1학년은 아직 데이터가 부족합니다.");
        }

        set1.setFillAlpha(110);
        set1.setLineWidth(3f);
        //set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set1.setCircleRadius(5f);
        set1.setColors(ContextCompat.getColor(getContext(), R.color.colorChartLine));
        set1.setCircleColors(ContextCompat.getColor(getContext(), R.color.colorChartLine));
        set1.setValueTextSize(10f);

        final ArrayList<Entry> g2 = new ArrayList<>();
        if (calculateGrade.getResult21() == 0) {
            g2.add(new Entry(0, Float.NaN));
        } else {
            g2.add(new Entry(0, calculateGrade.getResult21()));
        }
        if (calculateGrade.getResult22() == 0) {
            g2.add(new Entry(1, Float.NaN));
        } else {
            g2.add(new Entry(1, calculateGrade.getResult22()));
        }
        text_a21.setText(String.valueOf(calculateGrade.getResult21()));
        text_a22.setText(String.valueOf(calculateGrade.getResult22()));
        LineDataSet set2 = new LineDataSet(g2, "2학년");

        if (calculateGrade.getResult21() != 0&&calculateGrade.getResult22() != 0) {
            float a = calculateGrade.getResult21();
            float b =calculateGrade.getResult22();
            if (a<b) {
                text_opinion2.setText(Html.fromHtml("2학년 성적은 " + "<strong><font color=\'#F23535\'>하락세</font></strong>" + "군요. 좀더 노력해 보세요."));
            } else {
                text_opinion2.setText(Html.fromHtml("2학년 성적은 <strong><font color=\'#0B508C\'>상승세</font></strong>군요. 지금처럼만 공부합시다."));
            }
        } else {
            text_opinion2.setText("2학년은 아직 데이터가 부족합니다.");
        }


        set2.setFillAlpha(110);
        set2.setLineWidth(3f);
        //set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set2.setCircleRadius(5f);
        set2.setColors(ContextCompat.getColor(getContext(), R.color.colorChartLine2));
        set2.setCircleColors(ContextCompat.getColor(getContext(), R.color.colorChartLine2));
        set2.setValueTextSize(10f);

        final ArrayList<Entry> g3 = new ArrayList<>();
        if (calculateGrade.getResult31() == 0) {
            g3.add(new Entry(0, Float.NaN));
        } else {
            g3.add(new Entry(0, calculateGrade.getResult31()));
        }
        if (calculateGrade.getResult32() == 0) {
            g3.add(new Entry(1, Float.NaN));
        } else {
            g3.add(new Entry(1, calculateGrade.getResult32()));
        }
        text_a31.setText(String.valueOf(calculateGrade.getResult31()));
        text_a32.setText(String.valueOf(calculateGrade.getResult32()));
        LineDataSet set3 = new LineDataSet(g3, "3학년");

        if (calculateGrade.getResult31() != 0&&calculateGrade.getResult32() != 0) {
            float a = calculateGrade.getResult31();
            float b =calculateGrade.getResult32();
            if (a<b) {
                text_opinion3.setText(Html.fromHtml("3학년 성적은 " + "<strong><font color=\'#F23535\'>하락세</font></strong>" + "군요. 좀더 노력해 보세요."));
            } else {
                text_opinion3.setText(Html.fromHtml("3학년 성적은 " + "<strong><font color=\'#0B508C\'>상승세</font></strong>" + "군요. 지금처럼만 공부합시다."));
            }
        } else {
            text_opinion3.setText("3학년은 아직 데이터가 부족합니다.");
        }

        set3.setFillAlpha(110);
        set3.setLineWidth(3f);
        //set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set3.setCircleRadius(5f);
        set3.setColors(ContextCompat.getColor(getContext(), R.color.colorChartLine3));
        set3.setCircleColors(ContextCompat.getColor(getContext(), R.color.colorChartLine3));
        set3.setValueTextSize(10f);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        dataSets.add(set2);
        dataSets.add(set3);

        LineData data = new LineData(dataSets);

        chart_all.setData(data);

        String[] values = new String[] {"1학기", "2학기"};

        XAxis xAxis = chart_all.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {

            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return values[(int) value];
            }
        });
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLinesBehindData(false);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMinimum(-0.2f);
        xAxis.setAxisMaximum(1.2f);
        xAxis.setAxisLineWidth(1f);

        Description description = new Description();
        description.setText("");

        chart_all.setDescription(description);
        chart_all.setHighlightPerDragEnabled(false);
        chart_all.setHighlightPerTapEnabled(false);
        chart_all.animateY(1800, Easing.EaseOutSine);
        chart_all.invalidate();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
