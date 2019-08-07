package com.k1a2.schoolcalculator.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.github.mikephil.charting.utils.Utils;
import com.google.android.material.navigation.NavigationView;
import com.k1a2.schoolcalculator.BuildConfig;
import com.k1a2.schoolcalculator.CalculateGrade;
import com.k1a2.schoolcalculator.R;
import com.k1a2.schoolcalculator.database.DatabaseKey;
import com.k1a2.schoolcalculator.database.ScoreDatabaseHelper;
import com.k1a2.schoolcalculator.sharedpreference.PreferenceKey;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView text_rate = null;
    private ImageButton button_rate = null;
    private SharedPreferences preferences_rate = null;
    private Button button_editScoreAll = null;
    private Button button_editScore1 = null;
    private Button button_editScore2 = null;
    private Button button_editScore3 = null;
    private DrawerLayout drawer = null;
    private TextView textView11 = null;
    private TextView textView12 = null;
    private TextView textView21 = null;
    private TextView textView22 = null;
    private TextView textView31 = null;
    private TextView textView32 = null;
    private TextView textViewAll = null;
    private TextView textViewAllBar = null;
    private TextView textSum1 = null;
    private TextView textSum2 = null;
    private TextView textSum3 = null;
    private LineChart chart_analyze = null;
    private Button button_goal = null;

    private ScoreDatabaseHelper scoreDatabaseHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scoreDatabaseHelper = new ScoreDatabaseHelper(this, DatabaseKey.KEY_DB_NAME, null, 1);
        preferences_rate = PreferenceManager.getDefaultSharedPreferences(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //성적 추가하기 버튼들
        button_editScoreAll = (Button)findViewById(R.id.main_button_editScoreAll);
        button_editScore1 = (Button)findViewById(R.id.main_button_editScore1);
        button_editScore2 = (Button)findViewById(R.id.main_button_editScore2);
        button_editScore3 = (Button)findViewById(R.id.main_button_editScore3);
        text_rate = (TextView)findViewById(R.id.main_text_rate);
        button_rate = (ImageButton)findViewById(R.id.main_button_rate);
        textView11 = (TextView)findViewById(R.id.main_11text);
        textView12 = (TextView)findViewById(R.id.main_12text);
        textView21 = (TextView)findViewById(R.id.main_21text);
        textView22 = (TextView)findViewById(R.id.main_22text);
        textView31 = (TextView)findViewById(R.id.main_31text);
        textView32 = (TextView)findViewById(R.id.main_32text);
        textViewAll = (TextView)findViewById(R.id.main_text_all);
        textViewAllBar = (TextView)findViewById(R.id.main_text_allApp);
        textSum1 = (TextView)findViewById(R.id.main_1sumtext);
        textSum2 = (TextView)findViewById(R.id.main_2sumtext);
        textSum3 = (TextView)findViewById(R.id.main_3sumtext);
        chart_analyze = (LineChart)findViewById(R.id.main_chart_analyze);
        //button_goal = (Button)findViewById(R.id.main_button_editGoal);


        //리스너 연결
        button_editScoreAll.setOnClickListener(onScoreEditButton);
        button_editScore1.setOnClickListener(onScoreEditButton);
        button_editScore2.setOnClickListener(onScoreEditButton);
        button_editScore3.setOnClickListener(onScoreEditButton);

        ((Button) findViewById(R.id.main_button_showAnalyze)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AnalyzeActivity.class));
            }
        });

        button_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                final View root = View.inflate(MainActivity.this, R.layout.dialog_rate, null);
                final EditText edit_r1 = (EditText) root.findViewById(R.id.dialog_edit_r1);
                final EditText edit_r2 = (EditText) root.findViewById(R.id.dialog_edit_r2);
                final EditText edit_r3 = (EditText) root.findViewById(R.id.dialog_edit_r3);
                edit_r1.setText(String.valueOf(preferences_rate.getInt(PreferenceKey.KEY_INT_RATE_NAME_1, 1)));
                edit_r2.setText(String.valueOf(preferences_rate.getInt(PreferenceKey.KEY_INT_RATE_NAME_2, 1)));
                edit_r3.setText(String.valueOf(preferences_rate.getInt(PreferenceKey.KEY_INT_RATE_NAME_3, 1)));
                dialog.setView(root);
                dialog.setTitle("비율 설정");
                dialog.setPositiveButton("설정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int r1 = Integer.parseInt(edit_r1.getText().toString());
                        int r2 = Integer.parseInt(edit_r2.getText().toString());
                        int r3 = Integer.parseInt(edit_r3.getText().toString());
                        if (r1 < 0||r2 < 0||r3 < 0||r1 == 0||r2 == 0||r3 == 0) {
                            Toast.makeText(MainActivity.this, "음수, 0은 불가능 합니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            preferences_rate.edit().putInt(PreferenceKey.KEY_INT_RATE_NAME_1, r1).commit();
                            preferences_rate.edit().putInt(PreferenceKey.KEY_INT_RATE_NAME_2, r2).commit();
                            preferences_rate.edit().putInt(PreferenceKey.KEY_INT_RATE_NAME_3, r3).commit();
                            setGradeText();
                        }
                    }
                });
                dialog.show();
            }
        });

//        button_goal.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this, GoalActivity.class));
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setGradeText();
    }

    private void setGradeText() {
        final CalculateGrade calculateGrade = new CalculateGrade(this);

        int r1 = preferences_rate.getInt(PreferenceKey.KEY_INT_RATE_NAME_1, 0);
        int r2 = preferences_rate.getInt(PreferenceKey.KEY_INT_RATE_NAME_2, 0);
        int r3 = preferences_rate.getInt(PreferenceKey.KEY_INT_RATE_NAME_3, 0);

        if (r1 == 0||r2 == 0||r3 == 0) {
            r1 = 1;
            r2 = 1;
            r3 = 1;
        }

        //텍스트뷰에 함수 값 연결
        textView11.setText(String.valueOf(calculateGrade.getResult11()));
        textView12.setText(String.valueOf(calculateGrade.getResult12()));
        textView21.setText(String.valueOf(calculateGrade.getResult21()));
        textView22.setText(String.valueOf(calculateGrade.getResult22()));
        textView31.setText(String.valueOf(calculateGrade.getResult31()));
        textView32.setText(String.valueOf(calculateGrade.getResult32()));
        textSum1.setText(String.valueOf(calculateGrade.getResult1()));
        textSum2.setText(String.valueOf(calculateGrade.getResult2()));
        textSum3.setText(String.valueOf(calculateGrade.getResult3()));
        textViewAll.setText(String.valueOf(calculateGrade.getResultAll()));
        textViewAllBar.setText(String.valueOf(calculateGrade.getResultAll()));
        if (r1 == 1&&r2 == 1&&r3 == 1) {
            text_rate.setText(String.format("등급 반영 비율 %d:%d:%d", r1, r2, r3));
        } else {
            text_rate.setText(String.format("등급 반영 비율 %d:%d:%d", r1, r2, r3));
        }

        chart_analyze.setDragEnabled(true);
        chart_analyze.setScaleEnabled(false);

        YAxis leftAxis = chart_analyze.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.setAxisMaximum(9.5f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.enableGridDashedLine(10f, 10f, 0);
        leftAxis.setDrawLimitLinesBehindData(true);
        leftAxis.setInverted(true);
        leftAxis.setDrawAxisLine(false);

        chart_analyze.getAxisRight().setEnabled(false);

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
        set1.setCircleRadius(5f);
        set1.setColors(ContextCompat.getColor(this, R.color.colorChartLine));
        set1.setCircleColors(ContextCompat.getColor(this, R.color.colorChartLine));
        set1.setValueTextSize(10f);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        LineData data = new LineData(dataSets);

        chart_analyze.setData(data);

        String[] values = new String[] {"1-1", "1-2", "2-1", "2-2", "3-1", "3-2", ""};

        XAxis xAxis = chart_analyze.getXAxis();
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

        chart_analyze.setDescription(description);
        chart_analyze.setHighlightPerDragEnabled(false);
        chart_analyze.setHighlightPerTapEnabled(false);
        chart_analyze.animateY(1800, Easing.EaseOutSine);
        chart_analyze.invalidate();
    }

    //카드 안 성적 입력하기 버튼 클릭 리스너
    private View.OnClickListener onScoreEditButton = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.main_button_editScoreAll: {//일반
                    startScoreEditAvtivity(0);
                    break;
                }
                case R.id.main_button_editScore1: {//1학년
                    startScoreEditAvtivity(1);
                    break;
                }
                case  R.id.main_button_editScore2: {//2학년
                    startScoreEditAvtivity(2);
                    break;
                }
                case R.id.main_button_editScore3: {//3학년
                    startScoreEditAvtivity(3);
                    break;
                }
            }
        }
    };

    private void startScoreEditAvtivity(Integer mode) {
        Intent intent = new Intent(MainActivity.this, ScoreEditActivity.class);
        switch (mode) {
            case 0: {//all
                intent.putExtra(ActivityKey.KEY_ACTIVITY_MODE, ActivityKey.KEY_ACTIVITY_SCORE_ALL);
                break;
            }
            case 1: {//1
                intent.putExtra(ActivityKey.KEY_ACTIVITY_MODE, ActivityKey.KEY_ACTIVITY_SCORE_FIRST);
                break;
            }
            case 2: {//2
                intent.putExtra(ActivityKey.KEY_ACTIVITY_MODE, ActivityKey.KEY_ACTIVITY_SCORE_SECOND);
                break;
            }
            case 3: {//3
                intent.putExtra(ActivityKey.KEY_ACTIVITY_MODE, ActivityKey.KEY_ACTIVITY_SCORE_THIRD);
                break;
            }
        }
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, PreferenceActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_add: {
                startScoreEditAvtivity(0);
                break;
            }
            case R.id.nav_search: {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://adiga.kr/PageLinkAll.do?link=/kcue/ast/eip/eis/inf/univinf/eipUinfGnrl.do&p_menu_id=PG-EIP-01701"));
                startActivity(intent);
            }
            case R.id.nav_chart: {
                startActivity(new Intent(MainActivity.this, AnalyzeActivity.class));
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
