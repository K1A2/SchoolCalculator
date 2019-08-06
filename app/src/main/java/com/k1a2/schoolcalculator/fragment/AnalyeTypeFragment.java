package com.k1a2.schoolcalculator.fragment;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.k1a2.schoolcalculator.CalculateGrade;
import com.k1a2.schoolcalculator.R;
import com.k1a2.schoolcalculator.database.DatabaseKey;
import com.k1a2.schoolcalculator.database.ScoreDatabaseHelper;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AnalyeTypeFragment extends Fragment {

    private View root = null;
    private LineChart chart_analyeAll = null;
    private CheckBox check_k = null;
    private CheckBox check_m = null;
    private CheckBox check_e = null;
    private CheckBox check_s = null;
    private CheckBox check_sc = null;
    private CheckBox check_r = null;
    private CheckBox check_v = null;
    private TextView text_tk11 = null;
    private TextView text_tk12 = null;
    private TextView text_tk21 = null;
    private TextView text_tk22 = null;
    private TextView text_tk31 = null;
    private TextView text_tk32 = null;
    private TextView text_tm11 = null;
    private TextView text_tm12 = null;
    private TextView text_tm21 = null;
    private TextView text_tm22 = null;
    private TextView text_tm31 = null;
    private TextView text_tm32 = null;
    private TextView text_te11 = null;
    private TextView text_te12 = null;
    private TextView text_te21 = null;
    private TextView text_te22 = null;
    private TextView text_te31 = null;
    private TextView text_te32 = null;
    private TextView text_ts11 = null;
    private TextView text_ts12 = null;
    private TextView text_ts21 = null;
    private TextView text_ts22 = null;
    private TextView text_ts31 = null;
    private TextView text_ts32 = null;
    private TextView text_tsc11 = null;
    private TextView text_tsc12 = null;
    private TextView text_tsc21 = null;
    private TextView text_tsc22 = null;
    private TextView text_tsc31 = null;
    private TextView text_tsc32 = null;
    private TextView text_tr11 = null;
    private TextView text_tr12 = null;
    private TextView text_tr21 = null;
    private TextView text_tr22 = null;
    private TextView text_tr31 = null;
    private TextView text_tr32 = null;
    private RadarChart chart_radar = null;

    private ArrayList<ILineDataSet> dataSets = new ArrayList<>();
    private ScoreDatabaseHelper scoreDatabaseHelper = null;
    private CalculateGrade calculateGrade = null;
    private boolean isFirst = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_analyze_type, container, false);

        calculateGrade = new CalculateGrade(getContext());
        scoreDatabaseHelper = new ScoreDatabaseHelper(getContext(), DatabaseKey.KEY_DB_NAME, null, 1);

        chart_analyeAll = root.findViewById(R.id.analye_chart_analyzeT);
        check_k = root.findViewById(R.id.analye_check_k);
        check_e = root.findViewById(R.id.analye_check_e);
        check_s = root.findViewById(R.id.analye_check_s);
        check_m = root.findViewById(R.id.analye_check_m);
        check_sc = root.findViewById(R.id.analye_check_sc);
        check_r = root.findViewById(R.id.analye_check_r);
        check_v = root.findViewById(R.id.check_void);
        text_tk11 = root.findViewById(R.id.analyze_text_tk11);
        text_tk12 = root.findViewById(R.id.analyze_text_tk12);
        text_tk21 = root.findViewById(R.id.analyze_text_tk21);
        text_tk22 = root.findViewById(R.id.analyze_text_tk22);
        text_tk31 = root.findViewById(R.id.analyze_text_tk31);
        text_tk32 = root.findViewById(R.id.analyze_text_tk32);
        text_tm11 = root.findViewById(R.id.analyze_text_tm11);
        text_tm12 = root.findViewById(R.id.analyze_text_tm12);
        text_tm21 = root.findViewById(R.id.analyze_text_tm21);
        text_tm22 = root.findViewById(R.id.analyze_text_tm22);
        text_tm31 = root.findViewById(R.id.analyze_text_tm31);
        text_tm32 = root.findViewById(R.id.analyze_text_tm32);
        text_te11 = root.findViewById(R.id.analyze_text_te11);
        text_te12 = root.findViewById(R.id.analyze_text_te12);
        text_te21 = root.findViewById(R.id.analyze_text_te21);
        text_te22 = root.findViewById(R.id.analyze_text_te22);
        text_te31 = root.findViewById(R.id.analyze_text_te31);
        text_te32 = root.findViewById(R.id.analyze_text_te32);
        text_ts11 = root.findViewById(R.id.analyze_text_ts11);
        text_ts12 = root.findViewById(R.id.analyze_text_ts12);
        text_ts21 = root.findViewById(R.id.analyze_text_ts21);
        text_ts22 = root.findViewById(R.id.analyze_text_ts22);
        text_ts31 = root.findViewById(R.id.analyze_text_ts31);
        text_ts32 = root.findViewById(R.id.analyze_text_ts32);
        text_tsc11 = root.findViewById(R.id.analyze_text_tsc11);
        text_tsc12 = root.findViewById(R.id.analyze_text_tsc12);
        text_tsc21 = root.findViewById(R.id.analyze_text_tsc21);
        text_tsc22 = root.findViewById(R.id.analyze_text_tsc22);
        text_tsc31 = root.findViewById(R.id.analyze_text_tsc31);
        text_tsc32 = root.findViewById(R.id.analyze_text_tsc32);
        text_tr11 = root.findViewById(R.id.analyze_text_tr11);
        text_tr12 = root.findViewById(R.id.analyze_text_tr12);
        text_tr21 = root.findViewById(R.id.analyze_text_tr21);
        text_tr22 = root.findViewById(R.id.analyze_text_tr22);
        text_tr31 = root.findViewById(R.id.analyze_text_tr31);
        text_tr32 = root.findViewById(R.id.analyze_text_tr32);
        chart_radar = root.findViewById(R.id.analye_chart_analyzeTR);

        isFirst = true;

        setChart();

        check_k.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setKoreaData(check_k.isChecked());
            }
        });

        check_e.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setEnglishData(check_e.isChecked());
            }
        });

        check_m.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setMathData(check_m.isChecked());
            }
        });

        check_sc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setSocialData(check_sc.isChecked());
            }
        });

        check_r.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setRestData(check_r.isChecked());
            }
        });

        check_s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setScienceData(check_s.isChecked());
            }
        });


        ArrayList<Float> v = scoreDatabaseHelper.getTP(DatabaseKey.KEY_DB_TYPE_K);

        text_tk11.setText(String.valueOf(v.get(0)));
        text_tk12.setText(String.valueOf(v.get(1)));
        text_tk21.setText(String.valueOf(v.get(2)));
        text_tk22.setText(String.valueOf(v.get(3)));
        text_tk31.setText(String.valueOf(v.get(4)));
        text_tk32.setText(String.valueOf(v.get(5)));

        ArrayList<Float> v1 = scoreDatabaseHelper.getTP(DatabaseKey.KEY_DB_TYPE_M);

        text_tm11.setText(String.valueOf(v1.get(0)));
        text_tm12.setText(String.valueOf(v1.get(1)));
        text_tm21.setText(String.valueOf(v1.get(2)));
        text_tm22.setText(String.valueOf(v1.get(3)));
        text_tm31.setText(String.valueOf(v1.get(4)));
        text_tm32.setText(String.valueOf(v1.get(5)));

        ArrayList<Float> v2 = scoreDatabaseHelper.getTP(DatabaseKey.KEY_DB_TYPE_E);

        text_te11.setText(String.valueOf(v2.get(0)));
        text_te12.setText(String.valueOf(v2.get(1)));
        text_te21.setText(String.valueOf(v2.get(2)));
        text_te22.setText(String.valueOf(v2.get(3)));
        text_te31.setText(String.valueOf(v2.get(4)));
        text_te32.setText(String.valueOf(v2.get(5)));

        ArrayList<Float> v3 = scoreDatabaseHelper.getTP(DatabaseKey.KEY_DB_TYPE_S);

        text_ts11.setText(String.valueOf(v3.get(0)));
        text_ts12.setText(String.valueOf(v3.get(1)));
        text_ts21.setText(String.valueOf(v3.get(2)));
        text_ts22.setText(String.valueOf(v3.get(3)));
        text_ts31.setText(String.valueOf(v3.get(4)));
        text_ts32.setText(String.valueOf(v3.get(5)));

        ArrayList<Float> v4 = scoreDatabaseHelper.getTP(DatabaseKey.KEY_DB_TYPE_SC);

        text_tsc11.setText(String.valueOf(v4.get(0)));
        text_tsc12.setText(String.valueOf(v4.get(1)));
        text_tsc21.setText(String.valueOf(v4.get(2)));
        text_tsc22.setText(String.valueOf(v4.get(3)));
        text_tsc31.setText(String.valueOf(v4.get(4)));
        text_tsc32.setText(String.valueOf(v4.get(5)));

        ArrayList<Float> v5 = scoreDatabaseHelper.getTP(DatabaseKey.KEY_DB_TYPE_R);

        text_tr11.setText(String.valueOf(v5.get(0)));
        text_tr12.setText(String.valueOf(v5.get(1)));
        text_tr21.setText(String.valueOf(v5.get(2)));
        text_tr22.setText(String.valueOf(v5.get(3)));
        text_tr31.setText(String.valueOf(v5.get(4)));
        text_tr32.setText(String.valueOf(v5.get(5)));

        return root;
    }

    private void setKoreaData(boolean isAdd) {
        if (isAdd) {
            ArrayList<Float> v = scoreDatabaseHelper.getTP(DatabaseKey.KEY_DB_TYPE_K);

            final ArrayList<Entry> yvalue = new ArrayList<>();
            if (v.get(0) == 0) {
                yvalue.add(new Entry(0, Float.NaN));
            } else {
                yvalue.add(new Entry(0, v.get(0)));
            }
            if (v.get(1) == 0) {
                yvalue.add(new Entry(1, Float.NaN));
            } else {
                yvalue.add(new Entry(1, v.get(1)));
            }
            if (v.get(2) == 0) {
                yvalue.add(new Entry(2, Float.NaN));
            } else {
                yvalue.add(new Entry(2, v.get(2)));
            }
            if (v.get(3) == 0) {
                yvalue.add(new Entry(3, Float.NaN));
            } else {
                yvalue.add(new Entry(3, v.get(3)));
            }
            if (v.get(4) == 0) {
                yvalue.add(new Entry(4, Float.NaN));
            } else {
                yvalue.add(new Entry(4, v.get(4)));
            }
            if (v.get(5) == 0) {
                yvalue.add(new Entry(5, Float.NaN));
            } else {
                yvalue.add(new Entry(5, v.get(5)));
            }
            LineDataSet set1 = new LineDataSet(yvalue, "국어 계열");

            set1.setFillAlpha(110);
            set1.setLineWidth(3f);
            //set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setDrawValues(false);
            set1.setCircleRadius(5f);
            set1.setColors(ContextCompat.getColor(getContext(), R.color.colorChartLine));
            set1.setCircleColors(ContextCompat.getColor(getContext(), R.color.colorChartLine));
            set1.setValueTextSize(10f);

            dataSets.add(set1);

            LineData data = new LineData(dataSets);

            chart_analyeAll.setData(data);
            chart_analyeAll.animateY(1800, Easing.EaseOutSine);
            chart_analyeAll.notifyDataSetChanged();
            chart_analyeAll.invalidate();
        } else {
            chart_analyeAll.clearValues();
            chart_analyeAll.invalidate();
            setIsChecked();
        }
    }

    private void setMathData(boolean isAdd) {
        if (isAdd) {
            ArrayList<Float> v = scoreDatabaseHelper.getTP(DatabaseKey.KEY_DB_TYPE_M);

            final ArrayList<Entry> yvalue = new ArrayList<>();
            if (v.get(0) == 0) {
                yvalue.add(new Entry(0, Float.NaN));
            } else {
                yvalue.add(new Entry(0, v.get(0)));
            }
            if (v.get(1) == 0) {
                yvalue.add(new Entry(1, Float.NaN));
            } else {
                yvalue.add(new Entry(1, v.get(1)));
            }
            if (v.get(2) == 0) {
                yvalue.add(new Entry(2, Float.NaN));
            } else {
                yvalue.add(new Entry(2, v.get(2)));
            }
            if (v.get(3) == 0) {
                yvalue.add(new Entry(3, Float.NaN));
            } else {
                yvalue.add(new Entry(3, v.get(3)));
            }
            if (v.get(4) == 0) {
                yvalue.add(new Entry(4, Float.NaN));
            } else {
                yvalue.add(new Entry(4, v.get(4)));
            }
            if (v.get(5) == 0) {
                yvalue.add(new Entry(5, Float.NaN));
            } else {
                yvalue.add(new Entry(5, v.get(5)));
            }
            LineDataSet set1 = new LineDataSet(yvalue, "수학 계열");

            set1.setFillAlpha(110);
            set1.setLineWidth(3f);
            //set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setDrawValues(false);
            set1.setCircleRadius(5f);
            set1.setColors(ContextCompat.getColor(getContext(), R.color.colorChartLine2));
            set1.setCircleColors(ContextCompat.getColor(getContext(), R.color.colorChartLine2));
            set1.setValueTextSize(10f);

            dataSets.add(set1);

            LineData data = new LineData(dataSets);

            chart_analyeAll.setData(data);
            chart_analyeAll.animateY(1800, Easing.EaseOutSine);
            chart_analyeAll.notifyDataSetChanged();
            chart_analyeAll.invalidate();
        } else {
            chart_analyeAll.clearValues();
            chart_analyeAll.invalidate();
            setIsChecked();
        }
    }

    private void setEnglishData(boolean isAdd) {
        if (isAdd) {
            ArrayList<Float> v = scoreDatabaseHelper.getTP(DatabaseKey.KEY_DB_TYPE_E);

            final ArrayList<Entry> yvalue = new ArrayList<>();
            if (v.get(0) == 0) {
                yvalue.add(new Entry(0, Float.NaN));
            } else {
                yvalue.add(new Entry(0, v.get(0)));
            }
            if (v.get(1) == 0) {
                yvalue.add(new Entry(1, Float.NaN));
            } else {
                yvalue.add(new Entry(1, v.get(1)));
            }
            if (v.get(2) == 0) {
                yvalue.add(new Entry(2, Float.NaN));
            } else {
                yvalue.add(new Entry(2, v.get(2)));
            }
            if (v.get(3) == 0) {
                yvalue.add(new Entry(3, Float.NaN));
            } else {
                yvalue.add(new Entry(3, v.get(3)));
            }
            if (v.get(4) == 0) {
                yvalue.add(new Entry(4, Float.NaN));
            } else {
                yvalue.add(new Entry(4, v.get(4)));
            }
            if (v.get(5) == 0) {
                yvalue.add(new Entry(5, Float.NaN));
            } else {
                yvalue.add(new Entry(5, v.get(5)));
            }
            LineDataSet set1 = new LineDataSet(yvalue, "영어 계열");

            set1.setFillAlpha(110);
            set1.setLineWidth(3f);
            //set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setDrawValues(false);
            set1.setCircleRadius(5f);
            set1.setColors(ContextCompat.getColor(getContext(), R.color.colorChartLine3));
            set1.setCircleColors(ContextCompat.getColor(getContext(), R.color.colorChartLine3));
            set1.setValueTextSize(10f);

            dataSets.add(set1);

            LineData data = new LineData(dataSets);

            chart_analyeAll.setData(data);
            chart_analyeAll.animateY(1800, Easing.EaseOutSine);
            chart_analyeAll.notifyDataSetChanged();
            chart_analyeAll.invalidate();
        } else {
            chart_analyeAll.clearValues();
            chart_analyeAll.invalidate();
            setIsChecked();
        }
    }

    private void setScienceData(boolean isAdd) {
        if (isAdd) {
            ArrayList<Float> v = scoreDatabaseHelper.getTP(DatabaseKey.KEY_DB_TYPE_S);

            final ArrayList<Entry> yvalue = new ArrayList<>();
            if (v.get(0) == 0) {
                yvalue.add(new Entry(0, Float.NaN));
            } else {
                yvalue.add(new Entry(0, v.get(0)));
            }
            if (v.get(1) == 0) {
                yvalue.add(new Entry(1, Float.NaN));
            } else {
                yvalue.add(new Entry(1, v.get(1)));
            }
            if (v.get(2) == 0) {
                yvalue.add(new Entry(2, Float.NaN));
            } else {
                yvalue.add(new Entry(2, v.get(2)));
            }
            if (v.get(3) == 0) {
                yvalue.add(new Entry(3, Float.NaN));
            } else {
                yvalue.add(new Entry(3, v.get(3)));
            }
            if (v.get(4) == 0) {
                yvalue.add(new Entry(4, Float.NaN));
            } else {
                yvalue.add(new Entry(4, v.get(4)));
            }
            if (v.get(5) == 0) {
                yvalue.add(new Entry(5, Float.NaN));
            } else {
                yvalue.add(new Entry(5, v.get(5)));
            }
            LineDataSet set1 = new LineDataSet(yvalue, "과학탐구 계열");

            set1.setFillAlpha(110);
            set1.setLineWidth(3f);
            //set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setDrawValues(false);
            set1.setCircleRadius(5f);
            set1.setColors(ContextCompat.getColor(getContext(), R.color.colorChartLine4));
            set1.setCircleColors(ContextCompat.getColor(getContext(), R.color.colorChartLine4));
            set1.setValueTextSize(10f);

            dataSets.add(set1);

            LineData data = new LineData(dataSets);

            chart_analyeAll.setData(data);
            chart_analyeAll.animateY(1800, Easing.EaseOutSine);
            chart_analyeAll.notifyDataSetChanged();
            chart_analyeAll.invalidate();
        } else {
            chart_analyeAll.clearValues();
            chart_analyeAll.invalidate();
            setIsChecked();
        }
    }

    private void setSocialData(boolean isAdd) {
        if (isAdd) {
            ArrayList<Float> v = scoreDatabaseHelper.getTP(DatabaseKey.KEY_DB_TYPE_SC);

            final ArrayList<Entry> yvalue = new ArrayList<>();
            if (v.get(0) == 0) {
                yvalue.add(new Entry(0, Float.NaN));
            } else {
                yvalue.add(new Entry(0, v.get(0)));
            }
            if (v.get(1) == 0) {
                yvalue.add(new Entry(1, Float.NaN));
            } else {
                yvalue.add(new Entry(1, v.get(1)));
            }
            if (v.get(2) == 0) {
                yvalue.add(new Entry(2, Float.NaN));
            } else {
                yvalue.add(new Entry(2, v.get(2)));
            }
            if (v.get(3) == 0) {
                yvalue.add(new Entry(3, Float.NaN));
            } else {
                yvalue.add(new Entry(3, v.get(3)));
            }
            if (v.get(4) == 0) {
                yvalue.add(new Entry(4, Float.NaN));
            } else {
                yvalue.add(new Entry(4, v.get(4)));
            }
            if (v.get(5) == 0) {
                yvalue.add(new Entry(5, Float.NaN));
            } else {
                yvalue.add(new Entry(5, v.get(5)));
            }
            LineDataSet set1 = new LineDataSet(yvalue, "사회탐구 계열");

            set1.setFillAlpha(110);
            set1.setLineWidth(3f);
            //set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setDrawValues(false);
            set1.setCircleRadius(5f);
            set1.setColors(ContextCompat.getColor(getContext(), R.color.colorChartLine5));
            set1.setCircleColors(ContextCompat.getColor(getContext(), R.color.colorChartLine5));
            set1.setValueTextSize(10f);

            dataSets.add(set1);

            LineData data = new LineData(dataSets);

            chart_analyeAll.setData(data);
            chart_analyeAll.animateY(1800, Easing.EaseOutSine);
            chart_analyeAll.notifyDataSetChanged();
            chart_analyeAll.invalidate();
        } else {
            chart_analyeAll.clearValues();
            chart_analyeAll.invalidate();
            setIsChecked();
        }
    }

    private void setRestData(boolean isAdd) {
        if (isAdd) {
            ArrayList<Float> v = scoreDatabaseHelper.getTP(DatabaseKey.KEY_DB_TYPE_R);

            final ArrayList<Entry> yvalue = new ArrayList<>();
            if (v.get(0) == 0) {
                yvalue.add(new Entry(0, Float.NaN));
            } else {
                yvalue.add(new Entry(0, v.get(0)));
            }
            if (v.get(1) == 0) {
                yvalue.add(new Entry(1, Float.NaN));
            } else {
                yvalue.add(new Entry(1, v.get(1)));
            }
            if (v.get(2) == 0) {
                yvalue.add(new Entry(2, Float.NaN));
            } else {
                yvalue.add(new Entry(2, v.get(2)));
            }
            if (v.get(3) == 0) {
                yvalue.add(new Entry(3, Float.NaN));
            } else {
                yvalue.add(new Entry(3, v.get(3)));
            }
            if (v.get(4) == 0) {
                yvalue.add(new Entry(4, Float.NaN));
            } else {
                yvalue.add(new Entry(4, v.get(4)));
            }
            if (v.get(5) == 0) {
                yvalue.add(new Entry(5, Float.NaN));
            } else {
                yvalue.add(new Entry(5, v.get(5)));
            }
            LineDataSet set1 = new LineDataSet(yvalue, "기타 계열");

            set1.setFillAlpha(110);
            set1.setLineWidth(3f);
            //set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setCircleRadius(5f);
            set1.setDrawValues(false);
            set1.setColors(ContextCompat.getColor(getContext(), R.color.colorChartLine6));
            set1.setCircleColors(ContextCompat.getColor(getContext(), R.color.colorChartLine6));
            set1.setValueTextSize(10f);

            dataSets.add(set1);

            LineData data = new LineData(dataSets);

            chart_analyeAll.setData(data);
            chart_analyeAll.animateY(1800, Easing.EaseOutSine);
            chart_analyeAll.notifyDataSetChanged();
            chart_analyeAll.invalidate();
        } else {
            chart_analyeAll.clearValues();
            chart_analyeAll.invalidate();
            setIsChecked();
        }
    }

    private void setIsChecked() {
        if (check_k.isChecked()) {
            setKoreaData(true);
        }
        if (check_m.isChecked()) {
            setMathData(true);
        }
        if (check_e.isChecked()) {
            setEnglishData(true);
        }
        if (check_sc.isChecked()) {
            setSocialData(true);
        }
        if (check_r.isChecked()) {
            setRestData(true);
        }
        if (check_s.isChecked()) {
            setScienceData(true);
        }
    }

    private void setChart() {

        chart_analyeAll.setDragEnabled(true);
        chart_analyeAll.setScaleEnabled(false);

        YAxis leftAxis = chart_analyeAll.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.setAxisMaximum(9.5f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.enableGridDashedLine(10f, 10f, 0);
        leftAxis.setDrawLimitLinesBehindData(true);
        leftAxis.setInverted(true);
        leftAxis.setDrawAxisLine(false);

        chart_analyeAll.getAxisRight().setEnabled(false);

        ////////////////////////////

        String[] values = new String[] {"1-1", "1-2", "2-1", "2-2", "3-1", "3-2", ""};

        XAxis xAxis = chart_analyeAll.getXAxis();
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

        chart_analyeAll.setDescription(description);
        chart_analyeAll.setHighlightPerDragEnabled(false);
        chart_analyeAll.setHighlightPerTapEnabled(false);
        chart_analyeAll.invalidate();








        //chart_radar.setDrag
        //chart_radar.setScaleEnabled(false);

        YAxis yAxis = chart_radar.getYAxis();
        yAxis.removeAllLimitLines();
        yAxis.setAxisMaximum(9f);
        yAxis.setAxisMinimum(0f);
        yAxis.enableGridDashedLine(10f, 10f, 0);
        yAxis.setDrawLimitLinesBehindData(true);
        yAxis.setInverted(true);
        yAxis.setDrawAxisLine(false);

        ArrayList<Float> v = scoreDatabaseHelper.getTP(DatabaseKey.KEY_DB_TYPE_K);

        final ArrayList<RadarEntry> yvalue = new ArrayList<>();
        if (v.get(0) == 0) {
            yvalue.add(new RadarEntry(0, Float.NaN));
        } else {
            yvalue.add(new RadarEntry(0, v.get(0)));
        }
        if (v.get(1) == 0) {
            yvalue.add(new RadarEntry(1, Float.NaN));
        } else {
            yvalue.add(new RadarEntry(1, v.get(1)));
        }
        if (v.get(2) == 0) {
            yvalue.add(new RadarEntry(2, Float.NaN));
        } else {
            yvalue.add(new RadarEntry(2, v.get(2)));
        }
        if (v.get(3) == 0) {
            yvalue.add(new RadarEntry(3, Float.NaN));
        } else {
            yvalue.add(new RadarEntry(3, v.get(3)));
        }
        if (v.get(4) == 0) {
            yvalue.add(new RadarEntry(4, Float.NaN));
        } else {
            yvalue.add(new RadarEntry(4, v.get(4)));
        }
        if (v.get(5) == 0) {
            yvalue.add(new RadarEntry(5, Float.NaN));
        } else {
            yvalue.add(new RadarEntry(5, v.get(5)));
        }
        RadarDataSet set1 = new RadarDataSet(yvalue, "국어 계열");

        set1.setFillAlpha(110);
        set1.setLineWidth(3f);
        //set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set1.setDrawValues(false);
        set1.setColors(ContextCompat.getColor(getContext(), R.color.colorChartLine));
        set1.setValueTextSize(10f);

        ArrayList<IRadarDataSet> dataSets2 = new ArrayList<>();
        dataSets2.add(set1);

        RadarData data = new RadarData(dataSets2);

        chart_radar.setData(data);
        chart_radar.animateY(1800, Easing.EaseOutSine);

        String[] values2 = new String[] {"1-1", "1-2", "2-1", "2-2", "3-1", "3-2", ""};

        XAxis xAxis2 = chart_radar.getXAxis();
        xAxis2.setValueFormatter(new ValueFormatter() {

            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return values2[(int) value];
            }
        });
        xAxis2.setGranularity(1f);
        xAxis2.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis2.setDrawGridLinesBehindData(false);
        xAxis2.setDrawGridLines(false);
        xAxis2.setAxisLineWidth(1f);

        Description description2 = new Description();
        description2.setText("");

        chart_radar.setDescription(description);
        chart_radar.setHighlightPerTapEnabled(false);
        chart_radar.invalidate();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (isFirst) {
            ViewTreeObserver viewTreeObserver = check_sc.getViewTreeObserver();
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    int w = check_sc.getWidth();
                    check_s.setWidth(w);
                    check_m.setWidth(w);
                    check_e.setWidth(w);
                    check_r.setWidth(w);
                    check_k.setWidth(w);
                    check_v.setWidth(w);
                }
            });
            isFirst = false;
        }
    }
}
