package com.k1a2.schoolcalculator.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.k1a2.schoolcalculator.R;
import com.k1a2.schoolcalculator.database.DatabaseKey;
import com.k1a2.schoolcalculator.database.ScoreDatabaseHelper;
import com.k1a2.schoolcalculator.view.recyclerview.GradeRecyclerAdapter;
import com.k1a2.schoolcalculator.view.recyclerview.GradeRecyclerItem;

import java.util.ArrayList;

/**성적 입력 커스텀뷰
 * 절대 수정 금지**/

public class CardGradeView extends CardView {

    private TextView text_grade = null;
    private RecyclerView recycler_grade = null;
    private Button button_addGrade = null;

    private int level = 0;
    private GradeRecyclerAdapter gradeRecyclerAdapter = null;

    public CardGradeView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public CardGradeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        getAttrs(attrs);
    }

    public CardGradeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
                gradeRecyclerItem.setPoint("1");
                gradeRecyclerItem.setRank("1");
                gradeRecyclerItem.setSubjectName("국어");
                gradeRecyclerItem.setType(0);
                gradeRecyclerAdapter.addItem(gradeRecyclerItem);
            }
        });

        addView(view);
    }

    private void getAttrs(AttributeSet attrs) {
        final TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CardGradeView);
        setTypeArray(typedArray);
    }


    private void getAttrs(AttributeSet attrs, int defStyle) {
        final TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CardGradeView, defStyle, 0);
        setTypeArray(typedArray);
    }

    private void getAttrs(AttributeSet attrs, int defStyle, int defStyleAttr) {
        final TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CardGradeView, defStyle, defStyleAttr);
        setTypeArray(typedArray);
    }

    private void setTypeArray(TypedArray typedArray) {
        final int int_grade = typedArray.getInt(R.styleable.CardGradeView_grade, 1);
        level = typedArray.getInt(R.styleable.CardGradeView_level, 1);
        gradeRecyclerAdapter.setLevel(level);
        text_grade.setText(int_grade + "학기 성적 입력");
        gradeRecyclerAdapter.setGrade(int_grade);
        typedArray.recycle();

        final ScoreDatabaseHelper databaseHelper = new ScoreDatabaseHelper(getContext(), DatabaseKey.KEY_DB_NAME, null, 1);
        final ArrayList<String[]> values = databaseHelper.getScores(String.valueOf(level)+String.valueOf(int_grade));
        for (String[] s:values) {
            final GradeRecyclerItem gradeRecyclerItem = new GradeRecyclerItem();
            gradeRecyclerItem.setPoint(s[2]);
            gradeRecyclerItem.setRank(s[1]);
            gradeRecyclerItem.setSubjectName(s[0]);
            gradeRecyclerItem.setType(Integer.parseInt(s[3]));
            gradeRecyclerAdapter.addItem(gradeRecyclerItem);
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
//            switch (view.getId()) {
//                case R.id.card_info_s: {
//                    String a = "'과목명'이 뭐죠?";
//                    String b = "'과목명'은 수강한 과목의 이름을 말합니다.";
//                    final ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.WHITE);
//                    final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(a);
//                    spannableStringBuilder.setSpan(foregroundColorSpan, 0, a.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    alert.setTitle(spannableStringBuilder);
//                    final SpannableStringBuilder spannableStringBuilder1 = new SpannableStringBuilder(b);
//                    spannableStringBuilder1.setSpan(foregroundColorSpan, 0, b.trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    alert.setMessage(spannableStringBuilder1);
//                    break;
//                }
//
//                case R.id.card_info_g: {
//                    String a = "'등급'이 뭐죠?";
//                    String b = "'등급'은 각 과목 전체수강생을 성적순서에 따라 4,11,23,40,60,73,89, 96%의 비율기준으로 나누어 1~9등급으로 나누는 등급배분법을 말합니다.";
//                    final ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.WHITE);
//                    final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(a);
//                    spannableStringBuilder.setSpan(foregroundColorSpan, 0, a.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    alert.setTitle(spannableStringBuilder);
//                    final SpannableStringBuilder spannableStringBuilder1 = new SpannableStringBuilder(b);
//                    spannableStringBuilder1.setSpan(foregroundColorSpan, 0, b.trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    alert.setMessage(spannableStringBuilder1);
//                    break;
//                }
//
//                case R.id.card_info_u: {
//                    String a = "'단위'이 뭐죠?";
//                    String b = "'단위'는 각 과목이 일주일에 수업하는 총 횟수 입니다.";
//                    final ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.WHITE);
//                    final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(a);
//                    spannableStringBuilder.setSpan(foregroundColorSpan, 0, a.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    alert.setTitle(spannableStringBuilder);
//                    final SpannableStringBuilder spannableStringBuilder1 = new SpannableStringBuilder(b);
//                    spannableStringBuilder1.setSpan(foregroundColorSpan, 0, b.trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    alert.setMessage(spannableStringBuilder1);
//                    break;
//                }
//
//                case R.id.card_info_q: {
//                    String a = "\'과목 계열\'이 뭐죠?";
//                    String b = "\'과목 계열\'은 각 과목의 성격을 말합니다.";
//                    final ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.WHITE);
//                    final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(a);
//                    spannableStringBuilder.setSpan(foregroundColorSpan, 0, a.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    alert.setTitle(spannableStringBuilder);
//                    final SpannableStringBuilder spannableStringBuilder1 = new SpannableStringBuilder(b);
//                    spannableStringBuilder1.setSpan(foregroundColorSpan, 0, b.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    alert.setMessage(spannableStringBuilder1);
//                    break;
//                }
//            }
//            AlertDialog alertDialog = alert.create();
//            alertDialog.getWindow().setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.background_dialog_rate));
//            alertDialog.show();
        }
    };
}
