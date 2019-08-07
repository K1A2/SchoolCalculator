package com.k1a2.schoolcalculator.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.k1a2.schoolcalculator.R;

/**1학년 성적 입력 프래그먼트
 * scoreeditactivity에 기생**/

public class Grade1Fragment extends Fragment {

    private View root = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_grade1, container, false);

        return root;
    }
}
