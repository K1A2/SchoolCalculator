package com.k1a2.schoolcalculator.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.k1a2.schoolcalculator.R;
import com.k1a2.schoolcalculator.database.DatabaseKey;
import com.k1a2.schoolcalculator.database.TestScoreDatabaseHelper;
import com.k1a2.schoolcalculator.view.TestEditList;

import java.util.Calendar;

public class TestInputActivity extends Activity {

//    private Spinner spinner_year = null;
//    private Spinner spinner_month = null;
    private Button button_add = null;
    private LinearLayout layout_data = null;
    private Button button_date = null;
    private TestScoreDatabaseHelper testScoreDatabaseHelper = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_input);

        testScoreDatabaseHelper = new TestScoreDatabaseHelper(this, DatabaseKey.KEY_DB_NAME_TEST, null, 1);

//        spinner_year = findViewById(R.id.test_spinner_year);
//        spinner_month = findViewById(R.id.test_spinner_month);
        button_add = findViewById(R.id.test_button_input);
        layout_data = findViewById(R.id.layout_test_data);
        button_date = findViewById(R.id.test_button_date);

        button_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] tdt = button_date.getText().toString().split("\\s++");
                int yeari = Integer.parseInt(tdt[0].replaceAll("[^0-9]", ""));
                int monthi = Integer.parseInt(tdt[1].replaceAll("[^0-9]", ""));

                final AlertDialog.Builder builder = new AlertDialog.Builder(TestInputActivity.this, R.style.PickerAlertDialog);
                final View layout = getLayoutInflater().inflate(R.layout.dialog_nimberpicker_yd, null, false);
                final NumberPicker year = layout.findViewById(R.id.dialog_numberpicker_yd1);
                final NumberPicker month = layout.findViewById(R.id.dialog_numberpicker_yd2);
                builder.setMessage(Html.fromHtml("<font color='#FFFFFFFF'>모의고사 날짜 선택</font>"));
                month.setMinValue(1);
                month.setMaxValue(12);
                month.setValue(monthi);
                year.setMinValue(1990);
                year.setMaxValue(2099);
                year.setValue(yeari);

                builder.setView(layout);
                builder.setPositiveButton("입력", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        button_date.setText(year.getValue() + "년 " + month.getValue() + "월");
                        setDatas(year.getValue(), month.getValue());
                    }
                });
                builder.setNegativeButton("취소", null);
                final AlertDialog alertDialog = builder.create();
                alertDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.background_dialog_rate));
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
        });

//        ArrayAdapter typeAdapter1 = ArrayAdapter.createFromResource(this, R.array.testYear, android.R.layout.simple_spinner_item);
//        typeAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner_year.setAdapter(typeAdapter1);
//
//        ArrayAdapter typeAdapter2 = ArrayAdapter.createFromResource(this, R.array.testMonth, android.R.layout.simple_spinner_item);
//        typeAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner_month.setAdapter(typeAdapter2);
//
//        spinner_year.setOnItemSelectedListener(selectedListener);
//        spinner_month.setOnItemSelectedListener(selectedListener);
//
//        spinner_year.setSelection(0);
//        spinner_month.setSelection(0);

        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] tdt = button_date.getText().toString().split("\\s++");
                int year = Integer.parseInt(tdt[0].replaceAll("[^0-9]", ""));
                int monyh = Integer.parseInt(tdt[1].replaceAll("[^0-9]", ""));
                String result = testScoreDatabaseHelper.createTable(year + "_" + monyh);
                switch (result) {
                    case "exist":
                        Toast.makeText(TestInputActivity.this, "이미 존재합니다.", Toast.LENGTH_LONG).show();
                        break;

                    case "succes":
                        TestEditList testEditList = new TestEditList(TestInputActivity.this, year, monyh);
                        layout_data.addView(testEditList);
                        testEditList.setAllDatas(testScoreDatabaseHelper.getDatas(year + "_" + monyh));
                }
            }
        });

        Calendar calendar = Calendar.getInstance();
        int yeari = calendar.get(Calendar.YEAR);
        int monthi = calendar.get(Calendar.MONTH) + 1;

        button_date.setText(yeari + "년 " + monthi + "월");
        setDatas(yeari, monthi);
    }

//    final AdapterView.OnItemSelectedListener selectedListener = new AdapterView.OnItemSelectedListener() {
//        @Override
//        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//            setDatas(spinner_year.getSelectedItemPosition(), spinner_month.getSelectedItemPosition());
//        }
//
//        @Override
//        public void onNothingSelected(AdapterView<?> parent) {
//
//        }
//    };

    private void setDatas(int year, int month) {
        layout_data.removeAllViews();
        int r = testScoreDatabaseHelper.seekTable(year + "_" + month);
        if (r != 0) {
            TestEditList testEditList = new TestEditList(TestInputActivity.this, year, month);
            layout_data.addView(testEditList);
            testEditList.setAllDatas(testScoreDatabaseHelper.getDatas(year + "_" + month));
        }
    }

    public void deleteData(String name) {
        String[] tdt = button_date.getText().toString().split("\\s++");
        int year = Integer.parseInt(tdt[0].replaceAll("[^0-9]", ""));
        int monyh = Integer.parseInt(tdt[1].replaceAll("[^0-9]", ""));

        final AlertDialog.Builder a = new AlertDialog.Builder(TestInputActivity.this);
        a.setTitle("모의고사 삭제");
        a.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                layout_data.removeAllViews();
                testScoreDatabaseHelper.deleteTable(name);
            }
        });
        a.setNegativeButton("취소", null);
        a.setMessage(year + "년 " + monyh + "월 모의고사를 삭제하시겠습니까?");
        final AlertDialog alertDialog = a.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.rgb(235, 64, 52));
            }
        });
        alertDialog.show();
    }
}
