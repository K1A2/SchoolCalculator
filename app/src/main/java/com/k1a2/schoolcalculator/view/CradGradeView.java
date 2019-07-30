package com.k1a2.schoolcalculator.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.k1a2.schoolcalculator.R;
import com.k1a2.schoolcalculator.view.recyclerview.GradeRecyclerAdapter;
import com.k1a2.schoolcalculator.view.recyclerview.GradeRecyclerItem;

public class CradGradeView extends CardView {

    private TextView text_grade = null;
    private RecyclerView recycler_grade = null;
    private Button button_addGrade = null;

    private GradeRecyclerAdapter gradeRecyclerAdapter = null;

    public CradGradeView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public CradGradeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        getAttrs(attrs);
    }

    public CradGradeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        getAttrs(attrs, defStyleAttr);
    }

    private void initView(Context context) {
        final LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = layoutInflater.inflate(R.layout.view_card_grade, this, false);

        gradeRecyclerAdapter = new GradeRecyclerAdapter();

        text_grade = view.findViewById(R.id.view_card_text_grade);
        button_addGrade = view.findViewById(R.id.grade1_button_addgrade);
        recycler_grade = view.findViewById(R.id.grade1_recycler_grade);

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

        addView(view);
    }

    private void getAttrs(AttributeSet attrs) {
        final TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CradGradeView);
        setTypeArray(typedArray);
    }


    private void getAttrs(AttributeSet attrs, int defStyle) {
        final TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CradGradeView, defStyle, 0);
        setTypeArray(typedArray);
    }

    private void getAttrs(AttributeSet attrs, int defStyle, int defStyleAttr) {
        final TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CradGradeView, defStyle, defStyleAttr);
        setTypeArray(typedArray);
    }

    private void setTypeArray(TypedArray typedArray) {
        final int int_grade = typedArray.getInt(R.styleable.CradGradeView_grade, 1);
        text_grade.setText(int_grade + "학기 성적 입력");
        typedArray.recycle();
    }
}
