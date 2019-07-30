package com.k1a2.schoolcalculator.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.k1a2.schoolcalculator.R;
import com.k1a2.schoolcalculator.view.GradeRecyclerAdapter;
import com.k1a2.schoolcalculator.view.GradeRecyclerItem;

public class Grade1Fragment extends Fragment {

    private View root = null;
    private RecyclerView recycler_grade = null;
    private Button button_addGrade = null;

    private GradeRecyclerAdapter gradeRecyclerAdapter = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_grade1, container, false);

        gradeRecyclerAdapter = new GradeRecyclerAdapter();

        button_addGrade = root.findViewById(R.id.grade1_button_addgrade);
        recycler_grade = root.findViewById(R.id.grade1_recycler_grade);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler_grade.setLayoutManager(layoutManager);
        recycler_grade.setAdapter(gradeRecyclerAdapter);

        button_addGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final GradeRecyclerItem gradeRecyclerItem = new GradeRecyclerItem();
                gradeRecyclerItem.setPoint("");
                gradeRecyclerItem.setRank("");
                gradeRecyclerItem.setSubjectName("");
                gradeRecyclerAdapter.addItem(gradeRecyclerItem);
            }
        });

        return root;
    }
}
