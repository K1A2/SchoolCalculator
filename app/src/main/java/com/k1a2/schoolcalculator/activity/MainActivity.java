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

/**MainActivity 모든 정보를 한번에 보여주는 액티비티**/

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
        //성적 보여주는 텍스트
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
        //성적 차트
        chart_analyze = (LineChart)findViewById(R.id.main_chart_analyze);
        button_goal = (Button)findViewById(R.id.main_button_editGoal);

        //리스너 연결
        button_editScoreAll.setOnClickListener(onScoreEditButton);
        button_editScore1.setOnClickListener(onScoreEditButton);
        button_editScore2.setOnClickListener(onScoreEditButton);
        button_editScore3.setOnClickListener(onScoreEditButton);

        //성적 분석 보기 버튼 클릭 리스너
        ((Button) findViewById(R.id.main_button_showAnalyze)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AnalyzeActivity.class));
            }
        });

        //반영 비율 수정하기 버튼 클릭 리스너
        button_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);//수정 다이얼로그 띄움
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
                            preferences_rate.edit().putInt(PreferenceKey.KEY_INT_RATE_NAME_1, r1).commit();//1학년 반영비율 저장
                            preferences_rate.edit().putInt(PreferenceKey.KEY_INT_RATE_NAME_2, r2).commit();//2학년 반영비율 저장
                            preferences_rate.edit().putInt(PreferenceKey.KEY_INT_RATE_NAME_3, r3).commit();//3학년 반영비율 저장
                            setGradeText();//변경된 비율로 텍스트에 다시 보여주게 설정
                        }
                    }
                });
                dialog.show();
            }
        });

        //목표설정 보기 버튼 클릭 리스너
        button_goal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, GoalActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setGradeText();
    }

    //성적 가져와서 필요한 텍스트/차트에 뿌리는 함수
    private void setGradeText() {
        //성적 계산해서 가져오는 클래스 불러옴
        final CalculateGrade calculateGrade = new CalculateGrade(this);

        //1학년, 2학년, 3학년 반영 비율 sharedpreference에서 가져옴
        int r1 = preferences_rate.getInt(PreferenceKey.KEY_INT_RATE_NAME_1, 0);
        int r2 = preferences_rate.getInt(PreferenceKey.KEY_INT_RATE_NAME_2, 0);
        int r3 = preferences_rate.getInt(PreferenceKey.KEY_INT_RATE_NAME_3, 0);

        //반영비율이 정의가 안되있을때 1로 강제로 가정
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

        chart_analyze.setDragEnabled(true);//차트 드래그 활성화
        chart_analyze.setScaleEnabled(false);//차트 크기조절 비활성화

        YAxis leftAxis = chart_analyze.getAxisLeft();//차트의 왼쪽선 가져옴
        leftAxis.removeAllLimitLines();//제한선 제거
        leftAxis.setAxisMaximum(9.5f);//y의 최고값 정의 (9.5)
        leftAxis.setAxisMinimum(0f);//y의 최솟값 정의 (0)
        leftAxis.enableGridDashedLine(10f, 10f, 0);//이건 나도 뭔지 몰라
        leftAxis.setDrawLimitLinesBehindData(true);//몰라
        leftAxis.setInverted(true);//역순으로 배치 활성화
        leftAxis.setDrawAxisLine(false);//왼쪽선 그리기 비활성화

        chart_analyze.getAxisRight().setEnabled(false);//오른쪽 선은 안보이게 설정

        final ArrayList<Entry> yvalue = new ArrayList<>();//각 학기마다 점수를 담을 arraylist 선언
        //가져온 값이 0이면 특수상수인 NaN값을 넣어줌.
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
        LineDataSet set1 = new LineDataSet(yvalue, "성적");//arraylist를 기준으로 데이터셋 만듬

        set1.setFillAlpha(110);//원 중앙에 빈공간
        set1.setLineWidth(3f);//선 굵기 지정
        set1.setCircleRadius(5f);//원 크기
        set1.setColors(ContextCompat.getColor(this, R.color.colorChartLine));//선 색깔
        set1.setCircleColors(ContextCompat.getColor(this, R.color.colorChartLine));//원 선 색깔
        set1.setValueTextSize(10f);//원 위에 표시되는 텍스트 크기 지정

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();//차트에 표시되는 데이터셋 모임 선언
        dataSets.add(set1);//데잍셋 모임에 set1추가

        LineData data = new LineData(dataSets);//차트에 들어갈 데이터 완성

        chart_analyze.setData(data);//차트에 데이터 연결

        String[] values = new String[] {"1-1", "1-2", "2-1", "2-2", "3-1", "3-2", ""};//x축에 보일 값

        XAxis xAxis = chart_analyze.getXAxis();//x축 가져옴
        xAxis.setValueFormatter(new ValueFormatter() {

            @Override
            public String getAxisLabel(float value, AxisBase axis) {//x축 값 지정
                return values[(int) value];
            }
        });
        xAxis.setGranularity(1f);//몰라
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//x축값 아래에만 표시하게 설정
        xAxis.setDrawGridLinesBehindData(false);//데이터 뒤로 세로선 안보이게 설정
        xAxis.setDrawGridLines(false);//세로선 그리기 비활성화
        xAxis.setAxisMinimum(-0.2f);//x값 최솟값
        xAxis.setAxisMaximum(5.2f);//x값 최댓값
        xAxis.setAxisLineWidth(1f);//x축 굵기

        Description description = new Description();//설명 설정
        description.setText("");

        chart_analyze.setDescription(description);//설명 연결
        chart_analyze.setHighlightPerDragEnabled(false);//몰라
        chart_analyze.setHighlightPerTapEnabled(false);//몰라
        chart_analyze.animateY(1800, Easing.EaseOutSine);//애니메이션 적용
        chart_analyze.invalidate();//차트에 변경사항 있다고 알려줌
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

    //성적 입력하기 액티비티 띄우는 함수
    private void startScoreEditAvtivity(Integer mode) {//mode값에 따라 몇학년 탭을 보여줄지 결정
        Intent intent = new Intent(MainActivity.this, ScoreEditActivity.class);
        switch (mode) {
            case 0: {//all
                intent.putExtra(ActivityKey.KEY_ACTIVITY_MODE, ActivityKey.KEY_ACTIVITY_SCORE_ALL);//1학년
                break;
            }
            case 1: {//1
                intent.putExtra(ActivityKey.KEY_ACTIVITY_MODE, ActivityKey.KEY_ACTIVITY_SCORE_FIRST);//1학년
                break;
            }
            case 2: {//2
                intent.putExtra(ActivityKey.KEY_ACTIVITY_MODE, ActivityKey.KEY_ACTIVITY_SCORE_SECOND);//2학년
                break;
            }
            case 3: {//3
                intent.putExtra(ActivityKey.KEY_ACTIVITY_MODE, ActivityKey.KEY_ACTIVITY_SCORE_THIRD);//3학년
                break;
            }
        }
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {//뒤로가기키 (소프트or물리)가 눌렸을때
        DrawerLayout drawer = findViewById(R.id.drawer_layout);//밀어서 열리는 메뉴
        if (drawer.isDrawerOpen(GravityCompat.START)) {//메뉴가 열려있음 닫음
            drawer.closeDrawer(GravityCompat.START);
        } else {//아님 액티비티 종료
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {//툴바 메뉴 초기화
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {//툴바 메뉴 눌렸을때
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {//눌린 아이템 아이디가 셋팅일경우
            startActivity(new Intent(this, PreferenceActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {//옆으로 밀어서 열리는 메뉴 아이템이 눌렸을때
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_add: {//성적 추가
                startScoreEditAvtivity(0);
                break;
            }
            case R.id.nav_search: {//대학정보
                Intent intentS = new Intent(Intent.ACTION_VIEW, Uri.parse("http://adiga.kr/PageLinkAll.do?link=/kcue/ast/eip/eis/inf/univinf/eipUinfGnrl.do&p_menu_id=PG-EIP-01701"));
                startActivity(intentS);
                break;
            }
            case R.id.nav_homepage: {//홈페이지 연결
                Intent intentH = new Intent(Intent.ACTION_VIEW, Uri.parse("https://ldm0830.wixsite.com/schoolcalculator"));
                startActivity(intentH);
                break;
            }
            case R.id.nav_chart: {//분석
                Intent intentC = new Intent(MainActivity.this, AnalyzeActivity.class);
                intentC.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentC);
                break;
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
