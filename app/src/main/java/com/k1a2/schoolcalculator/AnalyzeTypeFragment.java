package com.k1a2.schoolcalculator;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;


/**
 * A simple {@link Fragment} subclass.
 */
public class AnalyzeTypeFragment extends Fragment {

    private View root;
    private LineChart chart_all = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_analyze_type, container, false);

        chart_all = root.findViewById(R.id.main_chart_analyze);

        return root;
    }

}
