package com.k1a2.schoolcalculator.view;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import com.k1a2.schoolcalculator.R;
import com.k1a2.schoolcalculator.activity.TestInputActivity;
import com.k1a2.schoolcalculator.database.DatabaseKey;
import com.k1a2.schoolcalculator.database.TestScoreDatabaseHelper;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class TestEditList extends LinearLayout {

    private int year = 0, month = 0;
    private String name = "";
    private TestScoreDatabaseHelper testScoreDatabaseHelper = null;

    public TestEditList(Context context, int year, int month) {
        super(context);
        this.year = year;
        this.month = month;
        name = String.valueOf(year) + "_" + String.valueOf(month);
        init(context);
    }

    private CheckBox btn_korea_check = null, btn_math_check = null, btn_eng_check = null, btn_history_check = null, btn_tamgu1_check = null, btn_tamgu2_check = null, btn_forgin_check = null;
    private AppCompatSpinner btn_math = null, btn_tamgu1 = null, btn_tamgu2 = null, btn_forgin = null;
    private Button btn_korea_grade = null, btn_math_grade = null, btn_eng_grade = null, btn_history_grade = null, btn_tamgu1_grade = null, btn_tamgu2_grade = null, btn_forgin_grade = null;
    private Button btn_korea_origin = null, btn_math_origin = null, btn_eng_origin = null, btn_history_origin = null, btn_tamgu1_origin = null, btn_tamgu2_origin = null, btn_forgin_origin = null;
    private Button btn_korea_section = null, btn_math_section = null, btn_eng_section = null, btn_history_section = null, btn_tamgu1_section = null, btn_tamgu2_section = null, btn_forgin_section = null;
    private Button btn_korea_percent = null, btn_math_percent = null, btn_eng_percent = null, btn_history_percent = null, btn_tamgu1_percent = null, btn_tamgu2_percent = null, btn_forgin_percent = null;
    private ImageButton btn_delete = null;

    private Object[][] views = new Object[7][6];

    private void init(Context context) {
        final LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = layoutInflater.inflate(R.layout.view_test_input, this, false);

        testScoreDatabaseHelper = new TestScoreDatabaseHelper(getContext(), DatabaseKey.KEY_DB_NAME_TEST, null, 1);

        btn_delete = view.findViewById(R.id.test_data_delete);
        btn_delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TestInputActivity)context).deleteData(name);
            }
        });

        btn_korea_check = view.findViewById(R.id.test_check_korean);
        btn_math_check = view.findViewById(R.id.test_check_math);
        btn_eng_check = view.findViewById(R.id.test_check_english);
        btn_history_check = view.findViewById(R.id.test_check_history);
        btn_tamgu1_check = view.findViewById(R.id.test_check_tamgu);
        btn_tamgu2_check = view.findViewById(R.id.test_check_tamgu2);
        btn_forgin_check = view.findViewById(R.id.test_check_forign);

        btn_korea_check.setOnCheckedChangeListener(checkedChangeListener);
        btn_math_check.setOnCheckedChangeListener(checkedChangeListener);
        btn_eng_check.setOnCheckedChangeListener(checkedChangeListener);
        btn_history_check.setOnCheckedChangeListener(checkedChangeListener);
        btn_tamgu1_check.setOnCheckedChangeListener(checkedChangeListener);
        btn_tamgu2_check.setOnCheckedChangeListener(checkedChangeListener);
        btn_forgin_check.setOnCheckedChangeListener(checkedChangeListener);

        btn_math = view.findViewById(R.id.mathtest_check_subject);
        btn_tamgu1 = view.findViewById(R.id.tamgutest_check_subject);
        btn_tamgu2 = view.findViewById(R.id.tamgu2test_check_subject);
        btn_forgin = view.findViewById(R.id.forigntest_check_subject);

        ArrayAdapter<String> a = new ArrayAdapter<String>(getContext(), R.layout.spinner_background, getContext().getResources().getStringArray(R.array.math));
        btn_math.setAdapter(a);
        btn_math.setOnItemSelectedListener(subjectItmSelectedListenner);

        ArrayAdapter<String> a1 = new ArrayAdapter<String>(getContext(), R.layout.spinner_background, getContext().getResources().getStringArray(R.array.tamgu));
        btn_tamgu1.setAdapter(a1);
        btn_tamgu1.setOnItemSelectedListener(subjectItmSelectedListenner);

        ArrayAdapter<String> a2 = new ArrayAdapter<String>(getContext(), R.layout.spinner_background, getContext().getResources().getStringArray(R.array.tamgu));
        btn_tamgu2.setAdapter(a2);
        btn_tamgu2.setOnItemSelectedListener(subjectItmSelectedListenner);

        ArrayAdapter<String> a3 = new ArrayAdapter<String>(getContext(), R.layout.spinner_background, getContext().getResources().getStringArray(R.array.forgin));
        btn_forgin.setAdapter(a3);
        btn_forgin.setOnItemSelectedListener(subjectItmSelectedListenner);

        btn_korea_grade = view.findViewById(R.id.koreantest_check_grade);
        btn_math_grade = view.findViewById(R.id.mathtest_check_grade);
        btn_eng_grade = view.findViewById(R.id.englishtest_check_grade);
        btn_history_grade = view.findViewById(R.id.historytest_check_grade);
        btn_tamgu1_grade = view.findViewById(R.id.tamgutest_check_grade);
        btn_tamgu2_grade = view.findViewById(R.id.tamgu2test_check_grade);
        btn_forgin_grade = view.findViewById(R.id.forigntest_check_grade);

        btn_korea_grade.setOnClickListener(gradeClickListener);
        btn_math_grade.setOnClickListener(gradeClickListener);
        btn_eng_grade.setOnClickListener(gradeClickListener);
        btn_history_grade.setOnClickListener(gradeClickListener);
        btn_tamgu1_grade.setOnClickListener(gradeClickListener);
        btn_tamgu2_grade.setOnClickListener(gradeClickListener);
        btn_forgin_grade.setOnClickListener(gradeClickListener);

        btn_korea_origin = view.findViewById(R.id.koreantest_check_original_grade);
        btn_math_origin = view.findViewById(R.id.mathtest_check_original_grade);
        btn_eng_origin = view.findViewById(R.id.englishtest_check_original_grade);
        btn_history_origin = view.findViewById(R.id.historytest_check_original_grade);
        btn_tamgu1_origin = view.findViewById(R.id.tamgutest_check_original_grade);
        btn_tamgu2_origin = view.findViewById(R.id.tamgu2test_check_original_grade);
        btn_forgin_origin = view.findViewById(R.id.forigntest_check_original_grade);

        btn_korea_origin.setOnClickListener(originClickListener);
        btn_math_origin.setOnClickListener(originClickListener);
        btn_eng_origin.setOnClickListener(originClickListener);
        btn_history_origin.setOnClickListener(originClickListener);
        btn_tamgu1_origin.setOnClickListener(originClickListener);
        btn_tamgu2_origin.setOnClickListener(originClickListener);
        btn_forgin_origin.setOnClickListener(originClickListener);

        btn_korea_section = view.findViewById(R.id.koreantest_check_section);
        btn_math_section = view.findViewById(R.id.mathtest_check_section);
        btn_eng_section = view.findViewById(R.id.englishtest_check_section);
        btn_history_section = view.findViewById(R.id.historytest_check_section);
        btn_tamgu1_section = view.findViewById(R.id.tamgutest_check_section);
        btn_tamgu2_section = view.findViewById(R.id.tamgu2test_check_section);
        btn_forgin_section = view.findViewById(R.id.forigntest_check_section);

        btn_korea_section.setOnClickListener(positionClickListener);
        btn_math_section.setOnClickListener(positionClickListener);
        btn_eng_section.setOnClickListener(positionClickListener);
        btn_history_section.setOnClickListener(positionClickListener);
        btn_tamgu1_section.setOnClickListener(positionClickListener);
        btn_tamgu2_section.setOnClickListener(positionClickListener);
        btn_forgin_section.setOnClickListener(positionClickListener);

        btn_korea_percent = view.findViewById(R.id.koreantest_check_percent);
        btn_math_percent = view.findViewById(R.id.mathtest_check_percent);
        btn_eng_percent = view.findViewById(R.id.englishtest_check_percent);
        btn_history_percent = view.findViewById(R.id.historytest_check_percent);
        btn_tamgu1_percent = view.findViewById(R.id.tamgutest_check_percent);
        btn_tamgu2_percent = view.findViewById(R.id.tamgu2test_check_percent);
        btn_forgin_percent = view.findViewById(R.id.forigntest_check_percent);

        btn_korea_percent.setOnClickListener(percentClickListener);
        btn_math_percent.setOnClickListener(percentClickListener);
        btn_eng_percent.setOnClickListener(percentClickListener);
        btn_history_percent.setOnClickListener(percentClickListener);
        btn_tamgu1_percent.setOnClickListener(percentClickListener);
        btn_tamgu2_percent.setOnClickListener(percentClickListener);
        btn_forgin_percent.setOnClickListener(percentClickListener);

        views = new Object[][]{{btn_korea_check, null, btn_korea_origin, btn_korea_grade, btn_korea_section, btn_korea_percent},
                {btn_math_check, btn_math, btn_math_origin, btn_math_grade, btn_math_section, btn_math_percent},
                {btn_eng_check, null, btn_eng_origin, btn_eng_grade, btn_eng_section, btn_eng_percent},
                {btn_history_check, null, btn_history_origin, btn_history_grade, btn_history_section, btn_history_percent},
                {btn_tamgu1_check, btn_tamgu1, btn_tamgu1_origin, btn_tamgu1_grade, btn_tamgu1_section, btn_tamgu1_percent},
                {btn_tamgu2_check, btn_tamgu2, btn_tamgu2_origin, btn_tamgu2_grade, btn_tamgu2_section, btn_tamgu2_percent},
                {btn_forgin_check, btn_forgin, btn_forgin_origin, btn_forgin_grade, btn_forgin_section, btn_forgin_percent}};

        addView(view);
    }

    CheckBox.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int id = 0;
            switch (buttonView.getId()) {
                case R.id.test_check_korean:
                    id = 0;
                    break;
                case R.id.test_check_math:
                    id = 1;
                    break;
                case R.id.test_check_english:
                    id = 2;
                    break;
                case R.id.test_check_history:
                    id = 3;
                    break;
                case R.id.test_check_tamgu:
                    id = 4;
                    break;
                case R.id.test_check_tamgu2:
                    id = 5;
                    break;
                case R.id.test_check_forign:
                    id = 6;
                    break;
            }
            testScoreDatabaseHelper.updateIsIn(name, isChecked, id);
            LinearLayout parents = (LinearLayout)buttonView.getParent();
            for (int i = 0;i < parents.getChildCount();i++) {
                if (i == 0) {
                    continue;
                } else if (i == 1&&!(id == 0||id == 2||id == 3)) {
                    AppCompatSpinner child = (AppCompatSpinner) parents.getChildAt(i);
                    if (isChecked) {
                        child.setEnabled(true);
                    } else {
                        child.setEnabled(false);
                    }
                } else if (i == 1&&(id == 0||id == 2||id == 3)) {
                    Button child = (Button) parents.getChildAt(i);
                    if (isChecked) {
                        child.setEnabled(true);
//
                    } else {
                        child.setEnabled(false);
//
                    }
                } else if (i == 4&&(id == 2||id == 3)) {
                    Button child = (Button) parents.getChildAt(i);
                    if (isChecked) {
                        child.setTextColor(Color.parseColor("#FF000000"));
                    } else {
                        child.setTextColor(Color.parseColor("#44000000"));
                    }
                    child.setEnabled(false);
                } else {
                    Button child = (Button)parents.getChildAt(i);
                    if (isChecked) {
                        child.setEnabled(true);
                        child.setTextColor(Color.parseColor("#FF000000"));
                    } else {
                        child.setEnabled(false);
                        child.setTextColor(Color.parseColor("#44000000"));
                    }
                }
            }
        }
    };

    AppCompatSpinner.OnItemSelectedListener subjectItmSelectedListenner = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            int ids = 0;
            switch (parent.getId()) {
                case R.id.mathtest_check_subject:
                    ids = 1;
                    break;
                case R.id.tamgutest_check_subject:
                    ids = 4;
                    break;
                case R.id.tamgu2test_check_subject:
                    ids = 5;
                    break;
                case R.id.forigntest_check_subject:
                    ids = 6;
                    break;
            }
            testScoreDatabaseHelper.updateSubject(name, position, ids);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    Button.OnClickListener positionClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.PickerAlertDialog);
            final View layout = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.dialog_nimberpicker, null, false);
            final NumberPicker numberPicker = layout.findViewById(R.id.dialog_numberpicker);
            numberPicker.setMinValue(1);
            numberPicker.setMaxValue(9);
            int id = 0;
            switch (v.getId()) {
                case R.id.koreantest_check_section:
                    builder.setMessage(Html.fromHtml("<font color='#FFFFFFFF'>국어 등급을 입력하세요.</font>"));
                    builder.setTitle(Html.fromHtml("<font color='#FFFFFFFF'>국어 등급 선택</font>"));
                    id = 0;
                    break;
                case R.id.mathtest_check_section:
                    builder.setMessage(Html.fromHtml("<font color='#FFFFFFFF'>" + btn_math.getSelectedItem().toString() + " 등급을 입력하세요.</font>"));
                    builder.setTitle(Html.fromHtml("<font color='#FFFFFFFF'>" + btn_math.getSelectedItem().toString() + " 등급 선택</font>"));
                    id = 1;
                    break;
                case R.id.englishtest_check_section:
                    builder.setMessage(Html.fromHtml("<font color='#FFFFFFFF'>영어 등급을 입력하세요.</font>"));
                    builder.setTitle(Html.fromHtml("<font color='#FFFFFFFF'>영어 등급 선택</font>"));
                    id = 2;
                    break;
                case R.id.historytest_check_section:
                    builder.setMessage(Html.fromHtml("<font color='#FFFFFFFF'>한국사 등급을 입력하세요.</font>"));
                    builder.setTitle(Html.fromHtml("<font color='#FFFFFFFF'>한국사 등급 선택</font>"));
                    id = 3;
                    break;
                case R.id.tamgutest_check_section:
                    builder.setMessage(Html.fromHtml("<font color='#FFFFFFFF'>" + btn_tamgu1.getSelectedItem().toString() + " 등급을 입력하세요.</font>"));
                    builder.setTitle(Html.fromHtml("<font color='#FFFFFFFF'>" + btn_tamgu1.getSelectedItem().toString() + " 등급 선택</font>"));
                    id = 4;
                    break;
                case R.id.tamgu2test_check_section:
                    builder.setMessage(Html.fromHtml("<font color='#FFFFFFFF'>" + btn_tamgu2.getSelectedItem().toString() + " 등급을 입력하세요.</font>"));
                    builder.setTitle(Html.fromHtml("<font color='#FFFFFFFF'>" + btn_tamgu2.getSelectedItem().toString() + " 등급 선택</font>"));
                    id = 5;
                    break;
                case R.id.forigntest_check_section:
                    builder.setMessage(Html.fromHtml("<font color='#FFFFFFFF'>" + btn_forgin.getSelectedItem().toString() + " 등급을 입력하세요.</font>"));
                    builder.setTitle(Html.fromHtml("<font color='#FFFFFFFF'>" + btn_forgin.getSelectedItem().toString() + " 등급 선택</font>"));
                    id = 6;
                    break;
            }
            final int ids = id;
            numberPicker.setValue(Integer.parseInt(((Button)v).getText().toString()));
            builder.setView(layout);
            builder.setPositiveButton("입력", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    testScoreDatabaseHelper.updatePosition(name, numberPicker.getValue(), ids);
                    ((Button)v).setText(String.valueOf(numberPicker.getValue()));
                }
            });
            builder.setNegativeButton("취소", null);
            final AlertDialog alertDialog = builder.create();
            alertDialog.getWindow().setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.background_dialog_rate));
            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.rgb(46, 144, 242));
                    alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.rgb(255, 110, 158));
                    //l.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(Color.rgb(255, 255, 255));
                }
            });
            alertDialog.show();
        }
    };

    Button.OnClickListener gradeClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.PickerAlertDialog);
            final View layout = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.dialog_nimberpicker, null, false);
            final NumberPicker numberPicker = layout.findViewById(R.id.dialog_numberpicker);
            numberPicker.setMinValue(0);
            numberPicker.setMaxValue(200);
            int id = 0;
            switch (v.getId()) {
                case R.id.koreantest_check_grade:
                    builder.setMessage(Html.fromHtml("<font color='#FFFFFFFF'>국어 표준점수를 입력하세요.</font>"));
                    builder.setTitle(Html.fromHtml("<font color='#FFFFFFFF'>국어 표준점수 선택</font>"));
                    id = 0;
                    break;
                case R.id.mathtest_check_grade:
                    builder.setMessage(Html.fromHtml("<font color='#FFFFFFFF'>" + btn_math.getSelectedItem().toString() + " 표준점수를 입력하세요.</font>"));
                    builder.setTitle(Html.fromHtml("<font color='#FFFFFFFF'>" + btn_math.getSelectedItem().toString() + " 표준점수 선택</font>"));
                    id = 1;
                    break;
                case R.id.englishtest_check_grade:
                    builder.setMessage(Html.fromHtml("<font color='#FFFFFFFF'>영어 표준점수를 입력하세요.</font>"));
                    builder.setTitle(Html.fromHtml("<font color='#FFFFFFFF'>영어 표준점수 선택</font>"));
                    id = 2;
                    break;
                case R.id.historytest_check_grade:
                    builder.setMessage(Html.fromHtml("<font color='#FFFFFFFF'>한국사 표준점수를 입력하세요.</font>"));
                    builder.setTitle(Html.fromHtml("<font color='#FFFFFFFF'>한국사 표준점수 선택</font>"));
                    id = 3;
                    break;
                case R.id.tamgutest_check_grade:
                    builder.setMessage(Html.fromHtml("<font color='#FFFFFFFF'>" + btn_tamgu1.getSelectedItem().toString() + " 표준점수를 입력하세요.</font>"));
                    builder.setTitle(Html.fromHtml("<font color='#FFFFFFFF'>" + btn_tamgu1.getSelectedItem().toString() + " 표준점수 선택</font>"));
                    id = 4;
                    break;
                case R.id.tamgu2test_check_grade:
                    builder.setMessage(Html.fromHtml("<font color='#FFFFFFFF'>" + btn_tamgu2.getSelectedItem().toString() + " 표준점수를 입력하세요.</font>"));
                    builder.setTitle(Html.fromHtml("<font color='#FFFFFFFF'>" + btn_tamgu2.getSelectedItem().toString() + " 표준점수 선택</font>"));
                    id = 5;
                    break;
                case R.id.forigntest_check_grade:
                    builder.setMessage(Html.fromHtml("<font color='#FFFFFFFF'>" + btn_forgin.getSelectedItem().toString() + " 표준점수를 입력하세요.</font>"));
                    builder.setTitle(Html.fromHtml("<font color='#FFFFFFFF'>" + btn_forgin.getSelectedItem().toString() + " 표준점수 선택</font>"));
                    id = 6;
                    break;
            }
            final int ids = id;
            numberPicker.setValue(Integer.parseInt(((Button)v).getText().toString()));
            builder.setView(layout);
            builder.setPositiveButton("입력", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    testScoreDatabaseHelper.updateGrade(name, numberPicker.getValue(), ids);
                    ((Button)v).setText(String.valueOf(numberPicker.getValue()));
                }
            });
            builder.setNegativeButton("취소", null);
            final AlertDialog alertDialog = builder.create();
            alertDialog.getWindow().setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.background_dialog_rate));
            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.rgb(46, 144, 242));
                    alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.rgb(255, 110, 158));
                    //l.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(Color.rgb(255, 255, 255));
                }
            });
            alertDialog.show();
        }
    };

    Button.OnClickListener originClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.PickerAlertDialog);
            final View layout = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.dialog_nimberpicker, null, false);
            final NumberPicker numberPicker = layout.findViewById(R.id.dialog_numberpicker);
            numberPicker.setMinValue(0);
            numberPicker.setMaxValue(100);
            int id = 0;
            switch (v.getId()) {
                case R.id.koreantest_check_original_grade:
                    builder.setMessage(Html.fromHtml("<font color='#FFFFFFFF'>국어 백분위를 입력하세요.</font>"));
                    builder.setTitle(Html.fromHtml("<font color='#FFFFFFFF'>국어 백분위 선택</font>"));
                    numberPicker.setMaxValue(100);
                    id = 0;
                    break;
                case R.id.mathtest_check_original_grade:
                    builder.setMessage(Html.fromHtml("<font color='#FFFFFFFF'>" + btn_math.getSelectedItem().toString() + " 백분위를 입력하세요.</font>"));
                    builder.setTitle(Html.fromHtml("<font color='#FFFFFFFF'>" + btn_math.getSelectedItem().toString() + " 백분위 선택</font>"));
                    numberPicker.setMaxValue(100);
                    id = 1;
                    break;
                case R.id.englishtest_check_original_grade:
                    builder.setMessage(Html.fromHtml("<font color='#FFFFFFFF'>영어 백분위를 입력하세요.</font>"));
                    builder.setTitle(Html.fromHtml("<font color='#FFFFFFFF'>영어 백분위 선택</font>"));
                    numberPicker.setMaxValue(100);
                    id = 2;
                    break;
                case R.id.historytest_check_original_grade:
                    builder.setMessage(Html.fromHtml("<font color='#FFFFFFFF'>한국사 백분위를 입력하세요.</font>"));
                    builder.setTitle(Html.fromHtml("<font color='#FFFFFFFF'>한국사 백분위 선택</font>"));
                    numberPicker.setMaxValue(50);
                    id = 3;
                    break;
                case R.id.tamgutest_check_original_grade:
                    builder.setMessage(Html.fromHtml("<font color='#FFFFFFFF'>" + btn_tamgu1.getSelectedItem().toString() + " 백분위를 입력하세요.</font>"));
                    builder.setTitle(Html.fromHtml("<font color='#FFFFFFFF'>" + btn_tamgu1.getSelectedItem().toString() + " 백분위 선택</font>"));
                    numberPicker.setMaxValue(50);
                    id = 4;
                    break;
                case R.id.tamgu2test_check_original_grade:
                    builder.setMessage(Html.fromHtml("<font color='#FFFFFFFF'>" + btn_tamgu2.getSelectedItem().toString() + " 백분위를 입력하세요.</font>"));
                    builder.setTitle(Html.fromHtml("<font color='#FFFFFFFF'>" + btn_tamgu2.getSelectedItem().toString() + " 백분위 선택</font>"));
                    numberPicker.setMaxValue(50);
                    id = 5;
                    break;
                case R.id.forigntest_check_original_grade:
                    builder.setMessage(Html.fromHtml("<font color='#FFFFFFFF'>" + btn_forgin.getSelectedItem().toString() + " 백분위를 입력하세요.</font>"));
                    builder.setTitle(Html.fromHtml("<font color='#FFFFFFFF'>" + btn_forgin.getSelectedItem().toString() + " 백분위 선택</font>"));
                    numberPicker.setMaxValue(50);
                    id = 6;
                    break;
            }
            final int ids = id;
            numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    switch (ids) {
                        case 2:
                            int sec = 1;
                            if (newVal >= 90) {
                                sec = 1;
                            } else if (newVal >= 80) {
                                sec = 2;
                            } else if (newVal >= 70) {
                                sec = 3;
                            } else if (newVal >= 60) {
                                sec = 4;
                            } else if (newVal >= 50) {
                                sec = 5;
                            } else if (newVal >= 40) {
                                sec = 6;
                            } else if (newVal >= 30) {
                                sec = 7;
                            } else if (newVal >= 20) {
                                sec = 8;
                            } else {
                                sec = 9;
                            }
                            testScoreDatabaseHelper.updatePosition(name, sec, ids);
                            ((Button)views[ids][4]).setText(String.valueOf(sec));
                            break;

                        case 3:
                            int sec1 = 1;
                            if (newVal >= 40) {
                                sec1 = 1;
                            } else if (newVal >= 35) {
                                sec1 = 2;
                            } else if (newVal >= 30) {
                                sec1 = 3;
                            } else if (newVal >= 25) {
                                sec1 = 4;
                            } else if (newVal >= 20) {
                                sec1 = 5;
                            } else if (newVal >= 15) {
                                sec1 = 6;
                            } else if (newVal >= 10) {
                                sec1 = 7;
                            } else if (newVal >= 5) {
                                sec1 = 8;
                            } else {
                                sec1 = 9;
                            }
                            testScoreDatabaseHelper.updatePosition(name, sec1, ids);
                            ((Button)views[ids][4]).setText(String.valueOf(sec1));
                            break;
                    }
                }
            });
            numberPicker.setValue(Integer.parseInt(((Button)v).getText().toString()));
            builder.setView(layout);
            builder.setPositiveButton("입력", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    testScoreDatabaseHelper.updateOrginalGrade(name, numberPicker.getValue(), ids);
                    ((Button)v).setText(String.valueOf(numberPicker.getValue()));
                }
            });
            builder.setNegativeButton("취소", null);
            final AlertDialog alertDialog = builder.create();
            alertDialog.getWindow().setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.background_dialog_rate));
            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.rgb(46, 144, 242));
                    alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.rgb(255, 110, 158));
                    //l.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(Color.rgb(255, 255, 255));
                }
            });
            alertDialog.show();
        }
    };

    Button.OnClickListener percentClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.PickerAlertDialog);
            final View layout = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.dialog_nimberpicker_multi, null, false);
            final NumberPicker numberPicker = layout.findViewById(R.id.dialog_numberpicker_1);
            final NumberPicker numberPicker2 = layout.findViewById(R.id.dialog_numberpicker_2);
            numberPicker.setMinValue(0);
            numberPicker.setMaxValue(100);
            numberPicker2.setMinValue(0);
            numberPicker2.setMaxValue(99);
            numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    if (newVal == 100) {
                        numberPicker2.setMaxValue(0);
                    } else {
                        numberPicker2.setMaxValue(99);
                    }
                }
            });
            int id = 0;
            switch (v.getId()) {
                case R.id.koreantest_check_percent:
                    builder.setMessage(Html.fromHtml("<font color='#FFFFFFFF'>국어 원점수를 입력하세요.</font>"));
                    builder.setTitle(Html.fromHtml("<font color='#FFFFFFFF'>국어 원점수 선택</font>"));
                    id = 0;
                    break;
                case R.id.mathtest_check_percent:
                    builder.setMessage(Html.fromHtml("<font color='#FFFFFFFF'>" + btn_math.getSelectedItem().toString() + " 원점수를 입력하세요.</font>"));
                    builder.setTitle(Html.fromHtml("<font color='#FFFFFFFF'>" + btn_math.getSelectedItem().toString() + " 원점수 선택</font>"));
                    id = 1;
                    break;
                case R.id.englishtest_check_percent:
                    builder.setMessage(Html.fromHtml("<font color='#FFFFFFFF'>영어 원점수를 입력하세요.</font>"));
                    builder.setTitle(Html.fromHtml("<font color='#FFFFFFFF'>영어 원점수 선택</font>"));
                    id = 2;
                    break;
                case R.id.historytest_check_percent:
                    builder.setMessage(Html.fromHtml("<font color='#FFFFFFFF'>한국사 원점수를 입력하세요.</font>"));
                    builder.setTitle(Html.fromHtml("<font color='#FFFFFFFF'>한국사 원점수 선택</font>"));
                    id = 3;
                    break;
                case R.id.tamgutest_check_percent:
                    builder.setMessage(Html.fromHtml("<font color='#FFFFFFFF'>" + btn_tamgu1.getSelectedItem().toString() + " 원점수를 입력하세요.</font>"));
                    builder.setTitle(Html.fromHtml("<font color='#FFFFFFFF'>" + btn_tamgu1.getSelectedItem().toString() + " 원점수 선택</font>"));
                    id = 4;
                    break;
                case R.id.tamgu2test_check_percent:
                    builder.setMessage(Html.fromHtml("<font color='#FFFFFFFF'>" + btn_tamgu2.getSelectedItem().toString() + " 원점수를 입력하세요.</font>"));
                    builder.setTitle(Html.fromHtml("<font color='#FFFFFFFF'>" + btn_tamgu2.getSelectedItem().toString() + " 원점수 선택</font>"));
                    id = 5;
                    break;
                case R.id.forigntest_check_percent:
                    builder.setMessage(Html.fromHtml("<font color='#FFFFFFFF'>" + btn_forgin.getSelectedItem().toString() + " 원점수를 입력하세요.</font>"));
                    builder.setTitle(Html.fromHtml("<font color='#FFFFFFFF'>" + btn_forgin.getSelectedItem().toString() + " 원점수 선택</font>"));
                    id = 6;
                    break;
            }
            final int ids = id;
            String[] split = ((Button)v).getText().toString().split("\\.");
            numberPicker.setValue(Integer.parseInt(split[0]));
            numberPicker2.setValue(Integer.parseInt(split[1]));
            builder.setView(layout);
            builder.setPositiveButton("입력", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d("Number", String.valueOf(numberPicker2.getValue()/100));
                    Log.d("Number All", Float.toString((float)(numberPicker.getValue()) + (float) (((float)numberPicker2.getValue())/100)));
                    float dd  = (float)(numberPicker.getValue()) + (float) (((float)numberPicker2.getValue())/100);
                    testScoreDatabaseHelper.updatePercen(name, dd, ids);
                    ((Button)v).setText(Float.toString(dd));
                }
            });
            builder.setNegativeButton("취소", null);
            final AlertDialog alertDialog = builder.create();
            alertDialog.getWindow().setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.background_dialog_rate));
            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.rgb(46, 144, 242));
                    alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.rgb(255, 110, 158));
                    //l.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(Color.rgb(255, 255, 255));
                }
            });
            alertDialog.show();
        }
    };

    public void setAllDatas(ArrayList<Object[]> datas) {
        for (int i = 0;i < datas.size();i++) {
            Object[] ss = datas.get(i);
            boolean b = false;
            for (int j = 0;j < ss.length;j++) {

                if (j == 0) {
                    if ((int)ss[j] == 0) {
                        b = false;
                    } else {
                        b = true;
                    }
                    CheckBox buttonView = (CheckBox)views[i][j];
                    buttonView.setChecked(b);
                    continue;
                } else if (j == 1){
                    if (!(i == 0||i == 2||i == 3)) {
                        ((Spinner)views[i][j]).setSelection((int)ss[j]);
                    }
                    continue;
                } else if (j == 5) {
                    ((Button)views[i][j]).setText(ss[j].toString());
                    continue;
                } else {
                    ((Button)views[i][j]).setText(String.valueOf((int)ss[j]));
                    continue;
                }
            }

            CheckBox buttonView = (CheckBox)views[i][0];
            LinearLayout parents = (LinearLayout)buttonView.getParent();
            for (int is = 0;is < parents.getChildCount();is++) {
                if (is == 0) {
                    continue;
                } else if (is == 1&&!(i == 0||i == 2||i == 3)) {
                    AppCompatSpinner child = (AppCompatSpinner) parents.getChildAt(is);
                    if (b) {
                        child.setEnabled(true);
                    } else {
                        child.setEnabled(false);
                    }
                } else if (is == 1&&(i == 0||i == 2||i == 3)) {
                    Button child = (Button) parents.getChildAt(is);
                    if (b) {
                        child.setEnabled(true);
                    } else {
                        child.setEnabled(false);
                    }
                } else if (is == 4&&(i == 2||i == 3)) {
                    Button child = (Button) parents.getChildAt(is);
                    if (b) {
                        child.setTextColor(Color.parseColor("#FF000000"));
                    } else {
                        child.setTextColor(Color.parseColor("#44000000"));
                    }
                    child.setEnabled(false);
                } else {
                    Button child = (Button)parents.getChildAt(is);
                    if (b) {
                        child.setEnabled(true);
                        child.setTextColor(Color.parseColor("#FF000000"));
                    } else {
                        child.setEnabled(false);
                        child.setTextColor(Color.parseColor("#44000000"));
                    }
                }
            }
        }
    }
}
