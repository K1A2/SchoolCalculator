package com.k1a2.schoolcalculator.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.k1a2.schoolcalculator.BillingKey;
import com.k1a2.schoolcalculator.GradeCalculator;
import com.k1a2.schoolcalculator.R;
import com.k1a2.schoolcalculator.activity.ActivityKey;
import com.k1a2.schoolcalculator.activity.AnalyzeActivity;
import com.k1a2.schoolcalculator.activity.MainActivity;
import com.k1a2.schoolcalculator.activity.ScoreEditActivity;
import com.k1a2.schoolcalculator.database.ScoreDatabaseHelper;
import com.k1a2.schoolcalculator.sharedpreference.AppStorage;
import com.k1a2.schoolcalculator.sharedpreference.PreferenceKey;
import com.k1a2.schoolcalculator.view.recyclerview.GradeViewItem;
import com.k1a2.schoolcalculator.view.recyclerview.GradeViewRecyclerAdapter;
import com.k1a2.schoolcalculator.view.recyclerview.LinePagerIndicatorDecoration;

import java.util.ArrayList;

public class SchoolFragment extends Fragment {

    private RecyclerView recycler_grade = null;
    private TextView text_rate = null;
    private ImageButton button_rate = null;
    private SharedPreferences preferences_rate = null;
    private Button button_editScoreAll = null;
    private Button button_editScore1 = null;
    private Button button_editScore2 = null;
    private Button button_editScore3 = null;
    private DrawerLayout drawer = null;
    private View root = null;
    private InterstitialAd mInterstitialAd = null;
    private AdView adView = null;
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
    private NavigationView navigationView = null;
    private TextView ti = null;

    private GradeViewRecyclerAdapter gradeViewRecyclerAdapter = null;

    private FirebaseAuth firebaseAuth = null;
    private ScoreDatabaseHelper scoreDatabaseHelper = null;
    private MainActivity mainActivity = null;
    private BillingProcessor bp = null;
    private AppStorage storage;

    private SharedPreferences preference_check = null;

    private static final int startAnalyzeInt = 8888;
    private static final int startScoreInt = 7777;
    private int now = 0;
    private int scoreIndex = 0;

    public SchoolFragment(){

    }

    public void setAuth(FirebaseAuth auth) {
        this.firebaseAuth = auth;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity)activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.activity_main, container, false);

        preferences_rate = PreferenceManager.getDefaultSharedPreferences(getContext());
        preference_check = PreferenceManager.getDefaultSharedPreferences(getContext());

        //성적 분석 보기 버튼 클릭 리스너
        ((Button) root.findViewById(R.id.main_button_showAnalyze)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAd == null) {
                    startActivity(new Intent(getContext(), AnalyzeActivity.class));
                } else {
                    if (mInterstitialAd.isLoaded()) {
                        now = startAnalyzeInt;
                        mInterstitialAd.show();
                    } else {
                        startActivity(new Intent(getContext(), AnalyzeActivity.class));
                        mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    }
                }
            }
        });

        final int showrate = preference_check.getInt(PreferenceKey.KEY_INT_COUNT_SHOW, 0);
        if (showrate == 0) {
            final AlertDialog.Builder rateDialog = new AlertDialog.Builder(getContext());
            final View rateView = View.inflate(getContext(), R.layout.dialog_rating, null);
            rateDialog.setView(rateView);
            final AlertDialog alertDialog = rateDialog.create();
            alertDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.background_dialog_rate));
            rateView.findViewById(R.id.rate_button_rate).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.k1a2.schoolcalculator")));
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
            preference_check.edit().putInt(PreferenceKey.KEY_INT_COUNT_SHOW, 10).commit();
        } else {
            preference_check.edit().putInt(PreferenceKey.KEY_INT_COUNT_SHOW, showrate -1).commit();
        }

        Toolbar toolbar = root.findViewById(R.id.toolbar);
        toolbar.setTitle("");
        mainActivity.setSupportActionBar(toolbar);
        drawer = root.findViewById(R.id.drawer_layout);
        navigationView = root.findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(mainActivity, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(mainActivity);
        mainActivity.setDrawer(drawer, navigationView);

        //성적 추가하기 버튼들
        button_editScoreAll = (Button)root.findViewById(R.id.main_button_editScoreAll);
        button_editScore1 = (Button)root.findViewById(R.id.main_button_editScore1);
        button_editScore2 = (Button)root.findViewById(R.id.main_button_editScore2);
        button_editScore3 = (Button)root.findViewById(R.id.main_button_editScore3);
        text_rate = (TextView)root.findViewById(R.id.main_text_rate);
        button_rate = (ImageButton)root.findViewById(R.id.main_button_rate);

        //성적 보여주는 텍스트
        if (getContext().getResources().getConfiguration().smallestScreenWidthDp >= 600) {//600dp 이상
            textView11 = (TextView)root.findViewById(R.id.main_11text);
            textView12 = (TextView)root.findViewById(R.id.main_12text);
            textView21 = (TextView)root.findViewById(R.id.main_21text);
            textView22 = (TextView)root.findViewById(R.id.main_22text);
            textView31 = (TextView)root.findViewById(R.id.main_31text);
            textView32 = (TextView)root.findViewById(R.id.main_32text);
            button_editScore1.setOnClickListener(onScoreEditButton);
            button_editScore2.setOnClickListener(onScoreEditButton);
            button_editScore3.setOnClickListener(onScoreEditButton);
        } else {
            //성적 리사이클러뷰
            gradeViewRecyclerAdapter = new GradeViewRecyclerAdapter(this);
            recycler_grade = root.findViewById(R.id.main_recycler_grade);
            recycler_grade.setAdapter(gradeViewRecyclerAdapter);
            recycler_grade.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            final PagerSnapHelper snapHelper = new PagerSnapHelper();
            snapHelper.attachToRecyclerView(recycler_grade);
            recycler_grade.addItemDecoration(new LinePagerIndicatorDecoration());
        }

        textViewAll = (TextView)root.findViewById(R.id.main_text_all);
        textViewAllBar = (TextView)root.findViewById(R.id.main_text_allApp);
        textSum1 = (TextView)root.findViewById(R.id.main_1sumtext);
        textSum2 = (TextView)root.findViewById(R.id.main_2sumtext);
        textSum3 = (TextView)root.findViewById(R.id.main_3sumtext);

        //성적 차트
        chart_analyze = (LineChart)root.findViewById(R.id.main_chart_analyze);
        //button_goal = (Button)findViewById(R.id.main_button_editGoal);

        //리스너 연결
        button_editScoreAll.setOnClickListener(onScoreEditButton);

        //반영 비율 수정하기 버튼 클릭 리스너
        button_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());//수정 다이얼로그 띄움
                final View root = View.inflate(getContext(), R.layout.dialog_rate, null);
                final EditText edit_r1 = (EditText) root.findViewById(R.id.dialog_edit_r1);
                final EditText edit_r2 = (EditText) root.findViewById(R.id.dialog_edit_r2);
                final EditText edit_r3 = (EditText) root.findViewById(R.id.dialog_edit_r3);
                edit_r1.setText(String.valueOf(preferences_rate.getInt(PreferenceKey.KEY_INT_RATE_NAME_1, 1)));
                edit_r2.setText(String.valueOf(preferences_rate.getInt(PreferenceKey.KEY_INT_RATE_NAME_2, 1)));
                edit_r3.setText(String.valueOf(preferences_rate.getInt(PreferenceKey.KEY_INT_RATE_NAME_3, 1)));
                dialog.setView(root);
                dialog.setPositiveButton("설정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final String a1 = edit_r1.getText().toString();
                        final String a2 = edit_r2.getText().toString();
                        final String a3 = edit_r3.getText().toString();
                        if (!a1.isEmpty()&&!a2.isEmpty()&&!a3.isEmpty()) {
                            if (Double.parseDouble(a1) <= Integer.MAX_VALUE&&
                                    Double.parseDouble(a2) <= Integer.MAX_VALUE&&
                                    Double.parseDouble(a3) <= Integer.MAX_VALUE) {
                                int r1 = Integer.parseInt(a1);
                                int r2 = Integer.parseInt(a2);
                                int r3 = Integer.parseInt(a3);

                                if (r1 < 0||r2 < 0||r3 < 0||r1 == 0||r2 == 0||r3 == 0) {
                                    Toast.makeText(getContext(), "음수, 0은 불가능 합니다.", Toast.LENGTH_SHORT).show();
                                } else {
                                    preferences_rate.edit().putInt(PreferenceKey.KEY_INT_RATE_NAME_1, r1).commit();//1학년 반영비율 저장
                                    preferences_rate.edit().putInt(PreferenceKey.KEY_INT_RATE_NAME_2, r2).commit();//2학년 반영비율 저장
                                    preferences_rate.edit().putInt(PreferenceKey.KEY_INT_RATE_NAME_3, r3).commit();//3학년 반영비율 저장
                                    setGradeText();//변경된 비율로 텍스트에 다시 보여주게 설정
                                }
                            } else {
                                Toast.makeText(getContext(), "숫자가 " + String.valueOf(Integer.MAX_VALUE) + "보다 작아야 합니다.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "칸을 모두 입력해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                final ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.WHITE);
                final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("비율 설정");
                spannableStringBuilder.setSpan(foregroundColorSpan, 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                dialog.setTitle(spannableStringBuilder);
                final AlertDialog alertDialog = dialog.create();
                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.WHITE);
                    }
                });
                alertDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.background_dialog_rate));
                alertDialog.show();
            }
        });

        ti = navigationView.getHeaderView(0).findViewById(R.id.login_id);
        //로그인 여부 검사
        if (firebaseAuth == null) firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null) {
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(true);
            ti.setVisibility(View.GONE);
        } else {
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
            ti.setText(firebaseAuth.getCurrentUser().getEmail() + "로그인 됨");
        }
        return root;
    }

    //성적 가져와서 필요한 텍스트/차트에 뿌리는 함수
    public void setGradeText() {
        //성적 계산해서 가져오는 클래스 불러옴
        final GradeCalculator gradeCalculator = new GradeCalculator(getContext());

        //1학년, 2학년, 3학년 반영 비율 sharedpreference에서 가져옴
        int r1 = preferences_rate.getInt(PreferenceKey.KEY_INT_RATE_NAME_1, 0);
        int r2 = preferences_rate.getInt(PreferenceKey.KEY_INT_RATE_NAME_2, 0);
        int r3 = preferences_rate.getInt(PreferenceKey.KEY_INT_RATE_NAME_3, 0);

        //반영비율이 정의가 안되있을때 1로 강제로 가정
        if (r1 == 0||r2 == 0||r3 == 0) {
            r1 = 2;
            r2 = 4;
            r3 = 4;
            preferences_rate.edit().putInt(PreferenceKey.KEY_INT_RATE_NAME_1, 2).commit();
            preferences_rate.edit().putInt(PreferenceKey.KEY_INT_RATE_NAME_2, 4).commit();
            preferences_rate.edit().putInt(PreferenceKey.KEY_INT_RATE_NAME_3, 4).commit();
        }

        //텍스트뷰에 함수 값 연결
        if (getContext().getResources().getConfiguration().smallestScreenWidthDp >= 600) {
            textView11.setText(String.valueOf(gradeCalculator.getResult11()));
            textView12.setText(String.valueOf(gradeCalculator.getResult12()));
            textView21.setText(String.valueOf(gradeCalculator.getResult21()));
            textView22.setText(String.valueOf(gradeCalculator.getResult22()));
            textView31.setText(String.valueOf(gradeCalculator.getResult31()));
            textView32.setText(String.valueOf(gradeCalculator.getResult32()));
        } else {
            final float[][] grades = new float[][] {{gradeCalculator.getResult11(), gradeCalculator.getResult12()}, {gradeCalculator.getResult21()
                    ,gradeCalculator.getResult22()}, {gradeCalculator.getResult31(), gradeCalculator.getResult32()}};

            gradeViewRecyclerAdapter.clearItem();
            for (int i = 0;i < 3;i++) {
                final GradeViewItem gradeViewItem = new GradeViewItem();
                gradeViewItem.setGradeTitle((i + 1) + "학년 평균 등급");
                gradeViewItem.setGrade1(grades[i][0]);
                gradeViewItem.setGrade2(grades[i][1]);
                gradeViewRecyclerAdapter.addItem(gradeViewItem);
            }
        }

        textSum1.setText(String.valueOf(gradeCalculator.getResult1()));
        textSum2.setText(String.valueOf(gradeCalculator.getResult2()));
        textSum3.setText(String.valueOf(gradeCalculator.getResult3()));
        textViewAll.setText(String.valueOf(gradeCalculator.getResultAll()));
        textViewAllBar.setText(String.valueOf(gradeCalculator.getResultAll()));

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
        if (gradeCalculator.getResult11() == 0) {
            yvalue.add(new Entry(0, Float.NaN));
        } else {
            yvalue.add(new Entry(0, gradeCalculator.getResult11()));
        }
        if (gradeCalculator.getResult12() == 0) {
            yvalue.add(new Entry(1, Float.NaN));
        } else {
            yvalue.add(new Entry(1, gradeCalculator.getResult12()));
        }
        if (gradeCalculator.getResult21() == 0) {
            yvalue.add(new Entry(2, Float.NaN));
        } else {
            yvalue.add(new Entry(2, gradeCalculator.getResult21()));
        }
        if (gradeCalculator.getResult22() == 0) {
            yvalue.add(new Entry(3, Float.NaN));
        } else {
            yvalue.add(new Entry(3, gradeCalculator.getResult22()));
        }
        if (gradeCalculator.getResult31() == 0) {
            yvalue.add(new Entry(4, Float.NaN));
        } else {
            yvalue.add(new Entry(4, gradeCalculator.getResult31()));
        }
        if (gradeCalculator.getResult32() == 0) {
            yvalue.add(new Entry(5, Float.NaN));
        } else {
            yvalue.add(new Entry(5, gradeCalculator.getResult32()));
        }
        LineDataSet set1 = new LineDataSet(yvalue, "성적");//arraylist를 기준으로 데이터셋 만듬

        set1.setFillAlpha(110);//원 중앙에 빈공간
        set1.setLineWidth(3f);//선 굵기 지정
        set1.setCircleRadius(5f);//원 크기
        set1.setColors(ContextCompat.getColor(getContext(), R.color.colorChartLine));//선 색깔
        set1.setCircleColors(ContextCompat.getColor(getContext(), R.color.colorChartLine));//원 선 색깔
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
    public void startScoreEditAvtivity(Integer mode) {//mode값에 따라 몇학년 탭을 보여줄지 결정
        startActivityScore(mode);
    }

    public void startActivityScore(int mode) {
        Intent intent = new Intent(getContext(), ScoreEditActivity.class);
        switch (mode) {
            case 0: {//all
                intent.putExtra(ActivityKey.KEY_ACTIVITY_MODE, ActivityKey.KEY_ACTIVITY_SCORE_ALL);//1학년
                scoreIndex = 0;
                break;
            }
            case 1: {//1
                intent.putExtra(ActivityKey.KEY_ACTIVITY_MODE, ActivityKey.KEY_ACTIVITY_SCORE_FIRST);//1학년
                scoreIndex = 1;
                break;
            }
            case 2: {//2
                intent.putExtra(ActivityKey.KEY_ACTIVITY_MODE, ActivityKey.KEY_ACTIVITY_SCORE_SECOND);//2학년
                scoreIndex = 2;
                break;
            }
            case 3: {//3
                intent.putExtra(ActivityKey.KEY_ACTIVITY_MODE, ActivityKey.KEY_ACTIVITY_SCORE_THIRD);//3학년
                scoreIndex = 3;
                break;
            }
        }
        if (mInterstitialAd == null) {
            startActivity(intent);
        } else {
            if (mInterstitialAd.isLoaded()) {
                now = startScoreInt;
                mInterstitialAd.show();
            } else {
                startActivity(intent);
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        }
    }

    private void startActivityAll(int code) {
        switch (code) {
            case startAnalyzeInt: {
                startActivity(new Intent(getContext(), AnalyzeActivity.class));
                break;
            }
            case startScoreInt: {
                Intent intent = new Intent(getContext(), ScoreEditActivity.class);
                switch (scoreIndex) {
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
                break;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setGradeText();
        Log.d("Resume main", "resume");
        loadAd();
    }

    public void loadAd() {
        storage = new AppStorage(getContext());

        bp = new BillingProcessor(getContext(), BillingKey.LINCENCE_KEY, new BillingProcessor.IBillingHandler() {
            @Override
            public void onProductPurchased(String productId, TransactionDetails details) {

            }

            @Override
            public void onPurchaseHistoryRestored() {

            }

            @Override
            public void onBillingError(int errorCode, Throwable error) {

            }

            @Override
            public void onBillingInitialized() {
                storage.setPurchasedRemoveAds(bp.isPurchased(BillingKey.KEY_SKU_NO_ADS));
                if (storage.purchasedRemoveAds()) {
                    root.findViewById(R.id.main_card_6).setVisibility(View.GONE);
                } else {
                    adView = root.findViewById(R.id.ads);
                    adView.setVisibility(View.GONE);
                    AdRequest adRequest = new AdRequest.Builder().build();
                    adView.setAdListener(new AdListener() {

                        @Override
                        public void onAdLoaded() {
                            adView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAdOpened() {
                            adView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAdClosed() {
                            adView.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAdFailedToLoad(int i) {
                            adView.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAdLeftApplication() {

                        }
                    });
                    adView.loadAd(adRequest);

                    try {
                        mInterstitialAd = new InterstitialAd(mainActivity.getApplicationContext());
                        mInterstitialAd.setAdListener(new AdListener() {
                            @Override
                            public void onAdClosed() {
                                startActivityAll(now);
                                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                            }
                        });
                        mInterstitialAd.setAdUnitId("ca-app-pub-1385482690406133/7999547125");
                        mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        bp.initialize();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (bp != null) {
            bp.release();
        }
    }
}
