//package com.k1a2.schoolcalculator.activity;
//
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.graphics.Color;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Build;
//import android.os.Bundle;
//import android.preference.PreferenceManager;
//import android.text.Html;
//import android.text.SpannableStringBuilder;
//import android.text.Spanned;
//import android.text.style.ForegroundColorSpan;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.ActionBarDrawerToggle;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import androidx.core.content.ContextCompat;
//import androidx.core.view.GravityCompat;
//import androidx.drawerlayout.widget.DrawerLayout;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.PagerSnapHelper;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.anjlab.android.iab.v3.BillingProcessor;
//import com.anjlab.android.iab.v3.TransactionDetails;
//import com.github.mikephil.charting.animation.Easing;
//import com.github.mikephil.charting.charts.LineChart;
//import com.github.mikephil.charting.components.AxisBase;
//import com.github.mikephil.charting.components.Description;
//import com.github.mikephil.charting.components.XAxis;
//import com.github.mikephil.charting.components.YAxis;
//import com.github.mikephil.charting.data.Entry;
//import com.github.mikephil.charting.data.LineData;
//import com.github.mikephil.charting.data.LineDataSet;
//import com.github.mikephil.charting.formatter.ValueFormatter;
//import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
//import com.google.android.gms.ads.AdListener;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
//import com.google.android.gms.ads.InterstitialAd;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.android.material.navigation.NavigationView;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
//import com.k1a2.schoolcalculator.BillingKey;
//import com.k1a2.schoolcalculator.GradeCalculator;
//import com.k1a2.schoolcalculator.R;
//import com.k1a2.schoolcalculator.database.DatabaseKey;
//import com.k1a2.schoolcalculator.database.ScoreDatabaseHelper;
//import com.k1a2.schoolcalculator.internet.ServerConnecter;
//import com.k1a2.schoolcalculator.sharedpreference.AppStorage;
//import com.k1a2.schoolcalculator.sharedpreference.PreferenceKey;
//import com.k1a2.schoolcalculator.view.recyclerview.GradeViewItem;
//import com.k1a2.schoolcalculator.view.recyclerview.GradeViewRecyclerAdapter;
//import com.k1a2.schoolcalculator.view.recyclerview.LinePagerIndicatorDecoration;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//
///**MainActivity 모든 정보를 한번에 보여주는 액티비티**/
//
//public class MainActivityBackup extends AppCompatActivity
//        implements NavigationView.OnNavigationItemSelectedListener {
//
//    private RecyclerView recycler_grade = null;
//    private TextView text_rate = null;
//    private ImageButton button_rate = null;
//    private SharedPreferences preferences_rate = null;
//    private Button button_editScoreAll = null;
//    private Button button_editScore1 = null;
//    private Button button_editScore2 = null;
//    private Button button_editScore3 = null;
//    private DrawerLayout drawer = null;
//    private TextView textView11 = null;
//    private TextView textView12 = null;
//    private TextView textView21 = null;
//    private TextView textView22 = null;
//    private TextView textView31 = null;
//    private TextView textView32 = null;
//    private TextView textViewAll = null;
//    private TextView textViewAllBar = null;
//    private TextView textSum1 = null;
//    private TextView textSum2 = null;
//    private TextView textSum3 = null;
//    private LineChart chart_analyze = null;
//    private Button button_goal = null;
//    private AdView adView = null;
//    private NavigationView navigationView = null;
//    private TextView ti = null;
//
//    private FirebaseAuth firebaseAuth = null;
//    private ScoreDatabaseHelper scoreDatabaseHelper = null;
//    private AppStorage storage;
//    private BillingProcessor bp = null;
//    private GradeViewRecyclerAdapter gradeViewRecyclerAdapter = null;
//    private ServerConnecter serverConnecter = new ServerConnecter();
//
//    private int versionInt = Build.VERSION.SDK_INT;
//
//    private static final int REQUEST_CODE_SIGN_IN_SAVE = 1;
//    private static final int REQUEST_CODE_SIGN_IN_LOAD = 3;
//    private static final int REQUEST_CODE_SIGN_IN = 5;
//
//    private static final int TASK_DB_SAVE = 99;
//    private static final int TASK_DB_LOAD = 100;
//
//    private SharedPreferences preference_check = null;
//
//    private InterstitialAd mInterstitialAd = null;
//    private static final int startAnalyzeInt = 8888;
//    private static final int startScoreInt = 7777;
//    private int now = 0;
//    private int scoreIndex = 0;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        preferences_rate = PreferenceManager.getDefaultSharedPreferences(this);
//        preference_check = PreferenceManager.getDefaultSharedPreferences(this);
////        if (preferences_rate.getBoolean(PreferenceKey.KEY_BOOL_ISDARK_THEME, false)) {
////            setTheme(R.style.AppTheme_Dark);
////        }
//        setContentView(R.layout.activity_main);
////        if (preferences_rate.getBoolean(PreferenceKey.KEY_BOOL_ISDARK_THEME, false)) {
////            ((CardView)findViewById(R.id.main_card_1)).setCardBackgroundColor(getResources().getColor(R.color.colorCardDark));
////            ((CardView)findViewById(R.id.main_card_2)).setCardBackgroundColor(getResources().getColor(R.color.colorCardDark));
////            ((CardView)findViewById(R.id.main_card_3)).setCardBackgroundColor(getResources().getColor(R.color.colorCardDark));
////            ((CardView)findViewById(R.id.main_card_4)).setCardBackgroundColor(getResources().getColor(R.color.colorCardDark));
////            ((CardView)findViewById(R.id.main_card_5)).setCardBackgroundColor(getResources().getColor(R.color.colorCardDark));
////            ((CardView)findViewById(R.id.main_card_6)).setCardBackgroundColor(getResources().getColor(R.color.colorCardDark));
////        }
//
//        scoreDatabaseHelper = new ScoreDatabaseHelper(this, DatabaseKey.KEY_DB_NAME, null, 1);
//
//        storage = new AppStorage(this);
//
//        bp = new BillingProcessor(this, BillingKey.LINCENCE_KEY, new BillingProcessor.IBillingHandler() {
//            @Override
//            public void onProductPurchased(String productId, TransactionDetails details) {
//
//            }
//
//            @Override
//            public void onPurchaseHistoryRestored() {
//
//            }
//
//            @Override
//            public void onBillingError(int errorCode, Throwable error) {
//
//            }
//
//            @Override
//            public void onBillingInitialized() {
//                storage.setPurchasedRemoveAds(bp.isPurchased(BillingKey.KEY_SKU_NO_ADS));
//                if (storage.purchasedRemoveAds()) {
//                    findViewById(R.id.main_card_6).setVisibility(View.GONE);
//                } else {
//                    adView = findViewById(R.id.ads);
//                    adView.setVisibility(View.GONE);
//                    AdRequest adRequest = new AdRequest.Builder().build();
//                    adView.setAdListener(new AdListener() {
//
//                        @Override
//                        public void onAdLoaded() {
//                            adView.setVisibility(View.VISIBLE);
//                        }
//
//                        @Override
//                        public void onAdOpened() {
//                            adView.setVisibility(View.VISIBLE);
//                        }
//
//                        @Override
//                        public void onAdClosed() {
//                            adView.setVisibility(View.GONE);
//                        }
//
//                        @Override
//                        public void onAdFailedToLoad(int i) {
//                            adView.setVisibility(View.GONE);
//                        }
//
//                        @Override
//                        public void onAdLeftApplication() {
//
//                        }
//                    });
//                    adView.loadAd(adRequest);
//
//                    mInterstitialAd = new InterstitialAd(MainActivityBackup.this);
//                    mInterstitialAd.setAdListener(new AdListener() {
//                        @Override
//                        public void onAdClosed() {
//                            startActivityAll(now);
//                            mInterstitialAd.loadAd(new AdRequest.Builder().build());
//                        }
//                    });
//                    mInterstitialAd.setAdUnitId("ca-app-pub-1385482690406133/7999547125");
//                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
//                }
//            }
//        });
//        bp.initialize();
//
//        if (preference_check.getBoolean(PreferenceKey.KEY_BOOL_IS_LAUNCH_FIRST, true)) {
//            preference_check.edit().putBoolean(PreferenceKey.KEY_BOOL_IS_LAUNCH_FIRST, false).commit();
//            preference_check.edit().putInt(PreferenceKey.KEY_INT_COUNT_SHOW, 5).commit();
//        }
//
//        if (preference_check.getBoolean("310first", true)) {
//            preference_check.edit().putBoolean("310first", false).commit();
//            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivityBackup.this);
//            builder.setTitle("광고에 관한 공지사항");
//            builder.setMessage("서버비용 문제로 인해 부득이하게 전면광고를 추가하게 되었습니다. 사용자분들에게 양해의 말씀드립니다. 전면광고는 메인 화면에서 분석화면이나 점수 입력 화면으로 넘어가기 직전에 보여지게됩니다. 제발 광고 생겼다고 삭제하지 말아주세요..ㅠ");
//            builder.setPositiveButton("확인", null);
//            builder.show();
//        }
//
//        final int showrate = preference_check.getInt(PreferenceKey.KEY_INT_COUNT_SHOW, 0);
//        if (showrate == 0) {
//            final AlertDialog.Builder rateDialog = new AlertDialog.Builder(this);
//            final View rateView = View.inflate(this, R.layout.dialog_rating, null);
//            rateDialog.setView(rateView);
//            final AlertDialog alertDialog = rateDialog.create();
//            alertDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.background_dialog_rate));
//            rateView.findViewById(R.id.rate_button_rate).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.k1a2.schoolcalculator")));
//                    alertDialog.dismiss();
//                }
//            });
//            alertDialog.show();
//            preference_check.edit().putInt(PreferenceKey.KEY_INT_COUNT_SHOW, 10).commit();
//        } else {
//            preference_check.edit().putInt(PreferenceKey.KEY_INT_COUNT_SHOW, showrate -1).commit();
//        }
//
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        toolbar.setTitle("");
//        setSupportActionBar(toolbar);
//        drawer = findViewById(R.id.drawer_layout);
//        navigationView = findViewById(R.id.nav_view);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
//        navigationView.setNavigationItemSelectedListener(this);
//
//        //성적 추가하기 버튼들
//        button_editScoreAll = (Button)findViewById(R.id.main_button_editScoreAll);
//        button_editScore1 = (Button)findViewById(R.id.main_button_editScore1);
//        button_editScore2 = (Button)findViewById(R.id.main_button_editScore2);
//        button_editScore3 = (Button)findViewById(R.id.main_button_editScore3);
//        text_rate = (TextView)findViewById(R.id.main_text_rate);
//        button_rate = (ImageButton)findViewById(R.id.main_button_rate);
//
//        //성적 보여주는 텍스트
//        if (MainActivityBackup.this.getResources().getConfiguration().smallestScreenWidthDp >= 600) {//600dp 이상
//            textView11 = (TextView)findViewById(R.id.main_11text);
//            textView12 = (TextView)findViewById(R.id.main_12text);
//            textView21 = (TextView)findViewById(R.id.main_21text);
//            textView22 = (TextView)findViewById(R.id.main_22text);
//            textView31 = (TextView)findViewById(R.id.main_31text);
//            textView32 = (TextView)findViewById(R.id.main_32text);
//            button_editScore1.setOnClickListener(onScoreEditButton);
//            button_editScore2.setOnClickListener(onScoreEditButton);
//            button_editScore3.setOnClickListener(onScoreEditButton);
//        } else {
//            //성적 리사이클러뷰
//            gradeViewRecyclerAdapter = new GradeViewRecyclerAdapter();
//            recycler_grade = findViewById(R.id.main_recycler_grade);
//            recycler_grade.setAdapter(gradeViewRecyclerAdapter);
//            recycler_grade.setLayoutManager(new LinearLayoutManager(MainActivityBackup.this, LinearLayoutManager.HORIZONTAL, false));
//            final PagerSnapHelper snapHelper = new PagerSnapHelper();
//            snapHelper.attachToRecyclerView(recycler_grade);
//            recycler_grade.addItemDecoration(new LinePagerIndicatorDecoration());
//        }
//
//        textViewAll = (TextView)findViewById(R.id.main_text_all);
//        textViewAllBar = (TextView)findViewById(R.id.main_text_allApp);
//        textSum1 = (TextView)findViewById(R.id.main_1sumtext);
//        textSum2 = (TextView)findViewById(R.id.main_2sumtext);
//        textSum3 = (TextView)findViewById(R.id.main_3sumtext);
//
//        //성적 차트
//        chart_analyze = (LineChart)findViewById(R.id.main_chart_analyze);
//        //button_goal = (Button)findViewById(R.id.main_button_editGoal);
//
//        //리스너 연결
//        button_editScoreAll.setOnClickListener(onScoreEditButton);
//
//        //성적 분석 보기 버튼 클릭 리스너
//        ((Button) findViewById(R.id.main_button_showAnalyze)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mInterstitialAd == null) {
//                    startActivity(new Intent(MainActivityBackup.this, AnalyzeActivity.class));
//                } else {
//                    if (mInterstitialAd.isLoaded()) {
//                        now = startAnalyzeInt;
//                        mInterstitialAd.show();
//                    } else {
//                        startActivity(new Intent(MainActivityBackup.this, AnalyzeActivity.class));
//                        mInterstitialAd.loadAd(new AdRequest.Builder().build());
//                    }
//                }
//            }
//        });
//
//        //반영 비율 수정하기 버튼 클릭 리스너
//        button_rate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivityBackup.this);//수정 다이얼로그 띄움
//                final View root = View.inflate(MainActivityBackup.this, R.layout.dialog_rate, null);
//                final EditText edit_r1 = (EditText) root.findViewById(R.id.dialog_edit_r1);
//                final EditText edit_r2 = (EditText) root.findViewById(R.id.dialog_edit_r2);
//                final EditText edit_r3 = (EditText) root.findViewById(R.id.dialog_edit_r3);
//                edit_r1.setText(String.valueOf(preferences_rate.getInt(PreferenceKey.KEY_INT_RATE_NAME_1, 1)));
//                edit_r2.setText(String.valueOf(preferences_rate.getInt(PreferenceKey.KEY_INT_RATE_NAME_2, 1)));
//                edit_r3.setText(String.valueOf(preferences_rate.getInt(PreferenceKey.KEY_INT_RATE_NAME_3, 1)));
//                dialog.setView(root);
//                dialog.setPositiveButton("설정", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        final String a1 = edit_r1.getText().toString();
//                        final String a2 = edit_r2.getText().toString();
//                        final String a3 = edit_r3.getText().toString();
//                        if (!a1.isEmpty()&&!a2.isEmpty()&&!a3.isEmpty()) {
//                            if (Double.parseDouble(a1) <= Integer.MAX_VALUE&&
//                                    Double.parseDouble(a2) <= Integer.MAX_VALUE&&
//                                    Double.parseDouble(a3) <= Integer.MAX_VALUE) {
//                                int r1 = Integer.parseInt(a1);
//                                int r2 = Integer.parseInt(a2);
//                                int r3 = Integer.parseInt(a3);
//
//                                if (r1 < 0||r2 < 0||r3 < 0||r1 == 0||r2 == 0||r3 == 0) {
//                                    Toast.makeText(MainActivityBackup.this, "음수, 0은 불가능 합니다.", Toast.LENGTH_SHORT).show();
//                                } else {
//                                    preferences_rate.edit().putInt(PreferenceKey.KEY_INT_RATE_NAME_1, r1).commit();//1학년 반영비율 저장
//                                    preferences_rate.edit().putInt(PreferenceKey.KEY_INT_RATE_NAME_2, r2).commit();//2학년 반영비율 저장
//                                    preferences_rate.edit().putInt(PreferenceKey.KEY_INT_RATE_NAME_3, r3).commit();//3학년 반영비율 저장
//                                    setGradeText();//변경된 비율로 텍스트에 다시 보여주게 설정
//                                }
//                            } else {
//                                Toast.makeText(MainActivityBackup.this, "숫자가 " + String.valueOf(Integer.MAX_VALUE) + "보다 작아야 합니다.", Toast.LENGTH_SHORT).show();
//                            }
//                        } else {
//                            Toast.makeText(MainActivityBackup.this, "칸을 모두 입력해주세요.", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//                final ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.WHITE);
//                final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("비율 설정");
//                spannableStringBuilder.setSpan(foregroundColorSpan, 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                dialog.setTitle(spannableStringBuilder);
//                final AlertDialog alertDialog = dialog.create();
//                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
//                    @Override
//                    public void onShow(DialogInterface dialogInterface) {
//                        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.WHITE);
//                    }
//                });
//                alertDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.background_dialog_rate));
//                alertDialog.show();
//            }
//        });
//
//        final FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
//        firebaseRemoteConfig.fetch().addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()) {
//                    firebaseRemoteConfig.activateFetched();
//
//                    String alerts = firebaseRemoteConfig.getString("string_main_alert");
//                    if (!alerts.isEmpty()) {
//                        try {
//                            String alert_b = preference_check.getString(PreferenceKey.KEY_STRING_ALERT, "");
//                            boolean alert_Is = preference_check.getBoolean(PreferenceKey.KEY_BOOL_ALERT, false);
//
//                            if (!alerts.equals(alert_b)) {
//                                preference_check.edit().putString(PreferenceKey.KEY_STRING_ALERT, alerts).commit();
//                                preference_check.edit().putBoolean(PreferenceKey.KEY_BOOL_ALERT, true).commit();
//                                final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivityBackup.this);
//                                final View a = View.inflate(MainActivityBackup.this, R.layout.dialog_alerts, null);
//                                //final String i = firebaseRemoteConfig.getString("string_update_note");
//                                alert.setView(a);
//                                ((TextView)a.findViewById(R.id.dialog_alerts)).setText(Html.fromHtml(alerts));
//                                alert.setPositiveButton("확인", null);
//                                alert.setNegativeButton("다시 보지 않기", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        preference_check.edit().putBoolean(PreferenceKey.KEY_BOOL_ALERT, false).commit();
//                                    }
//                                });
////                        alert.setNeutralButton("다시 보지 않기", new DialogInterface.OnClickListener() {
////                            @Override
////                            public void onClick(DialogInterface dialogInterface, int i) {
////
////                            }
////                        });
//                                final AlertDialog l = alert.create();
//                                l.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.background_dialog_rate));
//                                l.setOnShowListener(new DialogInterface.OnShowListener() {
//                                    @Override
//                                    public void onShow(DialogInterface dialogInterface) {
//                                        l.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.rgb(46, 144, 242));
//                                        l.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.rgb(255, 110, 158));
//                                        //l.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(Color.rgb(255, 255, 255));
//                                    }
//                                });
//                                l.show();
//                            } else {
//                                if (alert_Is) {
//                                    final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivityBackup.this);
//                                    final View a = View.inflate(MainActivityBackup.this, R.layout.dialog_alerts, null);
//                                    //final String i = firebaseRemoteConfig.getString("string_update_note");
//                                    alert.setView(a);
//                                    ((TextView)a.findViewById(R.id.dialog_alerts)).setText(Html.fromHtml(alerts));
//                                    alert.setPositiveButton("확인", null);
//                                    alert.setNegativeButton("다시 보지 않기", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialogInterface, int i) {
//                                            preference_check.edit().putBoolean(PreferenceKey.KEY_BOOL_ALERT, false).commit();
//                                        }
//                                    });
////                        alert.setNeutralButton("다시 보지 않기", new DialogInterface.OnClickListener() {
////                            @Override
////                            public void onClick(DialogInterface dialogInterface, int i) {
////
////                            }
////                        });
//                                    final AlertDialog l = alert.create();
//                                    l.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.background_dialog_rate));
//                                    l.setOnShowListener(new DialogInterface.OnShowListener() {
//                                        @Override
//                                        public void onShow(DialogInterface dialogInterface) {
//                                            l.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.rgb(46, 144, 242));
//                                            l.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.rgb(255, 110, 158));
//                                            //l.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(Color.rgb(255, 255, 255));
//                                        }
//                                    });
//                                    l.show();
//                                }
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            Log.e("Alert err", e.getMessage());
//                        }
//                    }
//
//                    final String version = firebaseRemoteConfig.getString("string_app_version");
//                    if (!getString(R.string.app_version).equals(version)) {
//                        final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivityBackup.this);
//                        final View a = View.inflate(MainActivityBackup.this, R.layout.dialog_new_version, null);
//                        //final String i = firebaseRemoteConfig.getString("string_update_note");
//                        ((TextView)a.findViewById(R.id.dialog_version_new)).setText(String.format("\'%s\' 버전이 플레이 스토어에 출시되었습니다!\n바로 업데이트 해보세요!", version));
//                        alert.setView(a);
//                        alert.setPositiveButton("업데이트", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                try {
//                                    startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("market://details?id=com.k1a2.schoolcalculator")));
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                    Toast.makeText(MainActivityBackup.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
//                        alert.setNegativeButton("다음에 하기", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//
//                            }
//                        });
////                        alert.setNeutralButton("다시 보지 않기", new DialogInterface.OnClickListener() {
////                            @Override
////                            public void onClick(DialogInterface dialogInterface, int i) {
////
////                            }
////                        });
//                        final AlertDialog l = alert.create();
//                        l.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.background_dialog_rate));
//                        l.setOnShowListener(new DialogInterface.OnShowListener() {
//                            @Override
//                            public void onShow(DialogInterface dialogInterface) {
//                                l.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.rgb(46, 144, 242));
//                                l.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.rgb(255, 110, 158));
//                                //l.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(Color.rgb(255, 255, 255));
//                            }
//                        });
//                        l.show();
//                    }
//                }
//            }
//        });
//
//        firebaseAuth = FirebaseAuth.getInstance();
//
//        ti = navigationView.getHeaderView(0).findViewById(R.id.login_id);
//        //로그인 여부 검사
//        if (firebaseAuth.getCurrentUser() == null) {
//            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
//            navigationView.getMenu().findItem(R.id.nav_login).setVisible(true);
//            ti.setVisibility(View.GONE);
//        } else {
//            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
//            navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
//            ti.setText(firebaseAuth.getCurrentUser().getEmail() + "로그인 됨");
//        }
//
//        //목표설정 보기 버튼 클릭 리스너
////        button_goal.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                startActivity(new Intent(MainActivity.this, GoalActivity.class));
////            }
////        });
//
////        GoogleSignInOptions gos = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
////                .requestEmail()
////                .build();
////
////        googleSignInClient = GoogleSignIn.getClient(this, gos);
////
////        SignInButton signInButton = findViewById(R.id.sign_in_button);
////        signInButton.setSize(SignInButton.SIZE_STANDARD);
////        signInButton.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
//////                Intent signInIntent = googleSignInClient.getSignInIntent();
//////                startActivityForResult(signInIntent, 0);
////                loginGoogleDriveBtnPressed();
////            }
////        });
////
////        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                //signOut();
////                goFileListActivity();
////            }
////        });
//    }
////
////    @Override
////    protected void onStart() {
////        super.onStart();
////        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
////        updateUI(account);
////    }
////
////    @Override
////    public void onActivityResult(int requestCode, int resultCode, Intent data) {
////        super.onActivityResult(requestCode, resultCode, data);
////
////        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
////        if (requestCode == 0) {
////            // The Task returned from this call is always completed, no need to attach
////            // a listener.
////            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
////            handleSignInResult(task);
////        }
////    }
////
////    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
////        try {
////            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
////
////            // Signed in successfully, show authenticated UI.
////            updateUI(account);
////        } catch (ApiException e) {
////            // The ApiException status code indicates the detailed failure reason.
////            // Please refer to the GoogleSignInStatusCodes class reference for more information.
////            Log.w("err", "signInResult:failed code=" + e.getStatusCode());
////            updateUI(null);
////        }
////    }
////
////    private void updateUI(GoogleSignInAccount account) {
////        if (account != null) {
////            Toast.makeText(this, account.getEmail(), Toast.LENGTH_SHORT).show();
////        }
////    }
////
////    private void signOut() {
////        googleSignInClient.signOut()
////                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
////                    @Override
////                    public void onComplete(@NonNull Task<Void> task) {
////                        if (task.isSuccessful()) {
////                            Toast.makeText(MainActivity.this, "Succes", Toast.LENGTH_SHORT).show();
////                        } else {
////                            Toast.makeText(MainActivity.this, "Fail", Toast.LENGTH_SHORT).show();
////                        }
////                    }
////                });
////    }
//
//    // 파일 보는 화면으로 이동하는 버튼 눌렸을 때.
//    private void goFileListActivity() {
//        Intent intent = new Intent(MainActivityBackup.this, ThirdPartyFileListActivity.class);
//        startActivity(intent);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        switch (requestCode) {
//            case REQUEST_CODE_SIGN_IN_SAVE: {
//                switch (resultCode) {
//                    case ActivityKey.LOGIN_RESULT_SUCCESS: {
//                        if (firebaseAuth.getCurrentUser() != null) {
//                            if (firebaseAuth.getCurrentUser().isEmailVerified()) {
//                                Toast.makeText(MainActivityBackup.this, firebaseAuth.getCurrentUser().getEmail() + "으로 로그인 되었습니다.", Toast.LENGTH_SHORT).show();
//                                navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
//                                navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
//                                ti.setVisibility(View.VISIBLE);
//                                ti.setText(firebaseAuth.getCurrentUser().getEmail() + "로그인 ");
//                            } else {
//                                Toast.makeText(MainActivityBackup.this, "이메일 인증을 완료해주세요.", Toast.LENGTH_SHORT).show();
//                                firebaseAuth.signOut();
//                                navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
//                                navigationView.getMenu().findItem(R.id.nav_login).setVisible(true);
//                                ti.setVisibility(View.GONE);
//                            }
//                        }
//                        break;
//                    }
//                    case ActivityKey.LOGIN_RESULT_FAIL: {
//                        if (firebaseAuth.getCurrentUser() != null) {
//                            Toast.makeText(MainActivityBackup.this, "로그인 실패", Toast.LENGTH_SHORT).show();
//                        }
//                        break;
//                    }
//                }
//                break;
//            }
//            case REQUEST_CODE_SIGN_IN_LOAD: {
//                switch (resultCode) {
//                    case ActivityKey.LOGIN_RESULT_SUCCESS: {
//                        if (firebaseAuth.getCurrentUser() != null) {
//                            if (firebaseAuth.getCurrentUser().isEmailVerified()) {
//                                Toast.makeText(MainActivityBackup.this, firebaseAuth.getCurrentUser().getEmail() + "으로 로그인 되었습니다.", Toast.LENGTH_SHORT).show();
//                                navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
//                                navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
//                                ti.setVisibility(View.VISIBLE);
//                                ti.setText(firebaseAuth.getCurrentUser().getEmail() + "로그인 ");
//                            } else {
//                                Toast.makeText(MainActivityBackup.this, "이메일 인증을 완료해주세요.", Toast.LENGTH_SHORT).show();
//                                firebaseAuth.signOut();
//                                navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
//                                navigationView.getMenu().findItem(R.id.nav_login).setVisible(true);
//                                ti.setVisibility(View.GONE);
//                            }
//                        }
//                        break;
//                    }
//                    case ActivityKey.LOGIN_RESULT_FAIL: {
//                        if (firebaseAuth.getCurrentUser() != null) {
//                            Toast.makeText(MainActivityBackup.this, "로그인 실패", Toast.LENGTH_SHORT).show();
//                        }
//                        break;
//                    }
//                }
//                break;
//            }
//            case REQUEST_CODE_SIGN_IN: {
//                switch (resultCode) {
//                    case ActivityKey.LOGIN_RESULT_SUCCESS: {
//                        if (firebaseAuth.getCurrentUser() != null) {
//                            if (firebaseAuth.getCurrentUser().isEmailVerified()) {
//                                Toast.makeText(MainActivityBackup.this, firebaseAuth.getCurrentUser().getEmail() + "으로 로그인 되었습니다.", Toast.LENGTH_SHORT).show();
//                                navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
//                                navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
//                                ti.setVisibility(View.VISIBLE);
//                                ti.setText(firebaseAuth.getCurrentUser().getEmail() + "로그인 ");
//                                //Toast.makeText(MainActivity.this, firebaseAuth.getCurrentUser().getProviderData()., Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(MainActivityBackup.this, "이메일 인증을 완료해주세요.", Toast.LENGTH_SHORT).show();
//                                firebaseAuth.signOut();
//                                navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
//                                navigationView.getMenu().findItem(R.id.nav_login).setVisible(true);
//                                ti.setVisibility(View.GONE);
//                            }
//                        }
//                        break;
//                    }
//                    case ActivityKey.LOGIN_RESULT_FAIL: {
//                        if (firebaseAuth.getCurrentUser() != null) {
//                            Toast.makeText(MainActivityBackup.this, "로그인 실패", Toast.LENGTH_SHORT).show();
//                        }
//                        break;
//                    }
//                }
//                break;
//            }
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        setGradeText();
//    }
//
//    //성적 가져와서 필요한 텍스트/차트에 뿌리는 함수
//    private void setGradeText() {
//        //성적 계산해서 가져오는 클래스 불러옴
//        final GradeCalculator gradeCalculator = new GradeCalculator(this);
//
//        //1학년, 2학년, 3학년 반영 비율 sharedpreference에서 가져옴
//        int r1 = preferences_rate.getInt(PreferenceKey.KEY_INT_RATE_NAME_1, 0);
//        int r2 = preferences_rate.getInt(PreferenceKey.KEY_INT_RATE_NAME_2, 0);
//        int r3 = preferences_rate.getInt(PreferenceKey.KEY_INT_RATE_NAME_3, 0);
//
//        //반영비율이 정의가 안되있을때 1로 강제로 가정
//        if (r1 == 0||r2 == 0||r3 == 0) {
//            r1 = 2;
//            r2 = 4;
//            r3 = 4;
//            preferences_rate.edit().putInt(PreferenceKey.KEY_INT_RATE_NAME_1, 2).commit();
//            preferences_rate.edit().putInt(PreferenceKey.KEY_INT_RATE_NAME_2, 4).commit();
//            preferences_rate.edit().putInt(PreferenceKey.KEY_INT_RATE_NAME_3, 4).commit();
//        }
//
//        //텍스트뷰에 함수 값 연결
//        if (MainActivityBackup.this.getResources().getConfiguration().smallestScreenWidthDp >= 600) {
//            textView11.setText(String.valueOf(gradeCalculator.getResult11()));
//            textView12.setText(String.valueOf(gradeCalculator.getResult12()));
//            textView21.setText(String.valueOf(gradeCalculator.getResult21()));
//            textView22.setText(String.valueOf(gradeCalculator.getResult22()));
//            textView31.setText(String.valueOf(gradeCalculator.getResult31()));
//            textView32.setText(String.valueOf(gradeCalculator.getResult32()));
//        } else {
//            final float[][] grades = new float[][] {{gradeCalculator.getResult11(), gradeCalculator.getResult12()}, {gradeCalculator.getResult21()
//                    ,gradeCalculator.getResult22()}, {gradeCalculator.getResult31(), gradeCalculator.getResult32()}};
//
//            gradeViewRecyclerAdapter.clearItem();
//            for (int i = 0;i < 3;i++) {
//                final GradeViewItem gradeViewItem = new GradeViewItem();
//                gradeViewItem.setGradeTitle((i + 1) + "학년 평균 등급");
//                gradeViewItem.setGrade1(grades[i][0]);
//                gradeViewItem.setGrade2(grades[i][1]);
//                gradeViewRecyclerAdapter.addItem(gradeViewItem);
//            }
//        }
//
//        textSum1.setText(String.valueOf(gradeCalculator.getResult1()));
//        textSum2.setText(String.valueOf(gradeCalculator.getResult2()));
//        textSum3.setText(String.valueOf(gradeCalculator.getResult3()));
//        textViewAll.setText(String.valueOf(gradeCalculator.getResultAll()));
//        textViewAllBar.setText(String.valueOf(gradeCalculator.getResultAll()));
//
//        if (r1 == 1&&r2 == 1&&r3 == 1) {
//            text_rate.setText(String.format("등급 반영 비율 %d:%d:%d", r1, r2, r3));
//        } else {
//            text_rate.setText(String.format("등급 반영 비율 %d:%d:%d", r1, r2, r3));
//        }
//
//        chart_analyze.setDragEnabled(true);//차트 드래그 활성화
//        chart_analyze.setScaleEnabled(false);//차트 크기조절 비활성화
//
//        YAxis leftAxis = chart_analyze.getAxisLeft();//차트의 왼쪽선 가져옴
//        leftAxis.removeAllLimitLines();//제한선 제거
//        leftAxis.setAxisMaximum(9.5f);//y의 최고값 정의 (9.5)
//        leftAxis.setAxisMinimum(0f);//y의 최솟값 정의 (0)
//        leftAxis.enableGridDashedLine(10f, 10f, 0);//이건 나도 뭔지 몰라
//        leftAxis.setDrawLimitLinesBehindData(true);//몰라
//        leftAxis.setInverted(true);//역순으로 배치 활성화
//        leftAxis.setDrawAxisLine(false);//왼쪽선 그리기 비활성화
//
//        chart_analyze.getAxisRight().setEnabled(false);//오른쪽 선은 안보이게 설정
//
//        final ArrayList<Entry> yvalue = new ArrayList<>();//각 학기마다 점수를 담을 arraylist 선언
//        //가져온 값이 0이면 특수상수인 NaN값을 넣어줌.
//        if (gradeCalculator.getResult11() == 0) {
//            yvalue.add(new Entry(0, Float.NaN));
//        } else {
//            yvalue.add(new Entry(0, gradeCalculator.getResult11()));
//        }
//        if (gradeCalculator.getResult12() == 0) {
//            yvalue.add(new Entry(1, Float.NaN));
//        } else {
//            yvalue.add(new Entry(1, gradeCalculator.getResult12()));
//        }
//        if (gradeCalculator.getResult21() == 0) {
//            yvalue.add(new Entry(2, Float.NaN));
//        } else {
//            yvalue.add(new Entry(2, gradeCalculator.getResult21()));
//        }
//        if (gradeCalculator.getResult22() == 0) {
//            yvalue.add(new Entry(3, Float.NaN));
//        } else {
//            yvalue.add(new Entry(3, gradeCalculator.getResult22()));
//        }
//        if (gradeCalculator.getResult31() == 0) {
//            yvalue.add(new Entry(4, Float.NaN));
//        } else {
//            yvalue.add(new Entry(4, gradeCalculator.getResult31()));
//        }
//        if (gradeCalculator.getResult32() == 0) {
//            yvalue.add(new Entry(5, Float.NaN));
//        } else {
//            yvalue.add(new Entry(5, gradeCalculator.getResult32()));
//        }
//        LineDataSet set1 = new LineDataSet(yvalue, "성적");//arraylist를 기준으로 데이터셋 만듬
//
//        set1.setFillAlpha(110);//원 중앙에 빈공간
//        set1.setLineWidth(3f);//선 굵기 지정
//        set1.setCircleRadius(5f);//원 크기
//        set1.setColors(ContextCompat.getColor(this, R.color.colorChartLine));//선 색깔
//        set1.setCircleColors(ContextCompat.getColor(this, R.color.colorChartLine));//원 선 색깔
//        set1.setValueTextSize(10f);//원 위에 표시되는 텍스트 크기 지정
//
//        ArrayList<ILineDataSet> dataSets = new ArrayList<>();//차트에 표시되는 데이터셋 모임 선언
//        dataSets.add(set1);//데잍셋 모임에 set1추가
//
//        LineData data = new LineData(dataSets);//차트에 들어갈 데이터 완성
//
//        chart_analyze.setData(data);//차트에 데이터 연결
//
//        String[] values = new String[] {"1-1", "1-2", "2-1", "2-2", "3-1", "3-2", ""};//x축에 보일 값
//
//        XAxis xAxis = chart_analyze.getXAxis();//x축 가져옴
//        xAxis.setValueFormatter(new ValueFormatter() {
//
//            @Override
//            public String getAxisLabel(float value, AxisBase axis) {//x축 값 지정
//                return values[(int) value];
//            }
//        });
//        xAxis.setGranularity(1f);//몰라
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//x축값 아래에만 표시하게 설정
//        xAxis.setDrawGridLinesBehindData(false);//데이터 뒤로 세로선 안보이게 설정
//        xAxis.setDrawGridLines(false);//세로선 그리기 비활성화
//        xAxis.setAxisMinimum(-0.2f);//x값 최솟값
//        xAxis.setAxisMaximum(5.2f);//x값 최댓값
//        xAxis.setAxisLineWidth(1f);//x축 굵기
//
//        Description description = new Description();//설명 설정
//        description.setText("");
//
//        chart_analyze.setDescription(description);//설명 연결
//        chart_analyze.setHighlightPerDragEnabled(false);//몰라
//        chart_analyze.setHighlightPerTapEnabled(false);//몰라
//        chart_analyze.animateY(1800, Easing.EaseOutSine);//애니메이션 적용
//        chart_analyze.invalidate();//차트에 변경사항 있다고 알려줌
//    }
//
//    //카드 안 성적 입력하기 버튼 클릭 리스너
//    private View.OnClickListener onScoreEditButton = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            switch (view.getId()) {
//                case R.id.main_button_editScoreAll: {//일반
//                    startScoreEditAvtivity(0);
//                    break;
//                }
//                case R.id.main_button_editScore1: {//1학년
//                    startScoreEditAvtivity(1);
//                    break;
//                }
//                case  R.id.main_button_editScore2: {//2학년
//                    startScoreEditAvtivity(2);
//                    break;
//                }
//                case R.id.main_button_editScore3: {//3학년
//                    startScoreEditAvtivity(3);
//                    break;
//                }
//            }
//        }
//    };
//
//    //성적 입력하기 액티비티 띄우는 함수
//    private void startScoreEditAvtivity(Integer mode) {//mode값에 따라 몇학년 탭을 보여줄지 결정
//        startActivityScore(mode);
//    }
//
//    @Override
//    public void onBackPressed() {//뒤로가기키 (소프트or물리)가 눌렸을때
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);//밀어서 열리는 메뉴
//        if (drawer.isDrawerOpen(GravityCompat.START)) {//메뉴가 열려있음 닫음
//            drawer.closeDrawer(GravityCompat.START);
//        } else {//아님 액티비티 종료
//            super.onBackPressed();
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {//툴바 메뉴 초기화
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {//툴바 메뉴 눌렸을때
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {//눌린 아이템 아이디가 셋팅일경우
//            startActivity(new Intent(this, PreferenceActivity.class));
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {//옆으로 밀어서 열리는 메뉴 아이템이 눌렸을때
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        switch (id) {
//            case R.id.nav_add: {//성적 추가
//                startScoreEditAvtivity(0);
//                break;
//            }
//            case R.id.nav_search: {//대학정보
//                try {
//                    Intent intentS = new Intent(Intent.ACTION_VIEW);
//                    intentS.setData(Uri.parse("http://adiga.kr/PageLinkAll.do?link=/kcue/ast/eip/eis/inf/univinf/eipUinfGnrl.do&p_menu_id=PG-EIP-01701"));
//                    startActivity(intentS);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Toast.makeText(MainActivityBackup.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
//                }
//                break;
//            }
//            case R.id.nav_homepage: {//홈페이지 연결
//                try {
//                    Intent intentH = new Intent(Intent.ACTION_VIEW);
//                    intentH.setData(Uri.parse("https://ldm0830.wixsite.com/schoolcalculator"));
//                    startActivity(intentH);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Toast.makeText(MainActivityBackup.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
//                }
//                break;
//            }
//            case R.id.nav_chart: {//분석
//                Intent intentC = new Intent(MainActivityBackup.this, AnalyzeActivity.class);
//                intentC.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intentC);
//                break;
//            }
//            case R.id.nav_save: {
//                if (firebaseAuth.getCurrentUser() == null) {
//                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivityBackup.this);
//                    alertDialog.setTitle("로그인 필요");
////                    if (versionInt < Build.VERSION_CODES.M) {
////                        alertDialog = new AlertDialog.Builder(MainActivity.this, R.style.Dialog);
////                    }
//                    alertDialog.setMessage("클라우드 저장 기능을 사용하려면 로그인이 필요합니다.");
//                    alertDialog.setPositiveButton("로그인", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            startActivityForResult(new Intent(MainActivityBackup.this, AccountActivity.class), REQUEST_CODE_SIGN_IN_SAVE);
//                        }
//                    });
//                    alertDialog.setNegativeButton("그만두기", null);
//                    alertDialog.show();
//                } else {
//                    serverConnecter.new CheckUser(MainActivityBackup.this, firebaseAuth.getCurrentUser().getUid(), TASK_DB_SAVE).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                    //FirebaseAuth.getInstance().signOut();
//                }
//                break;
//            }
//            case R.id.nav_logout: {
//                firebaseAuth.signOut();
//                navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
//                navigationView.getMenu().findItem(R.id.nav_login).setVisible(true);
//                ti.setVisibility(View.GONE);
//                Toast.makeText(MainActivityBackup.this, "로그아웃 됨", Toast.LENGTH_SHORT).show();
//                break;
//            }
//            case R.id.nav_load: {
//                if (firebaseAuth.getCurrentUser() == null) {
//                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivityBackup.this);
//                    alertDialog.setTitle("로그인 필요");
//                    alertDialog.setMessage("클라우드 가져오기 기능을 사용하려면 로그인이 필요합니다.");
//                    alertDialog.setPositiveButton("로그인", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            startActivityForResult(new Intent(MainActivityBackup.this, AccountActivity.class), REQUEST_CODE_SIGN_IN_LOAD);
//                        }
//                    });
//                    alertDialog.setNegativeButton("그만두기", null);
//                    alertDialog.show();
//                } else {
//                    //FirebaseAuth.getInstance().signOut();
//                    serverConnecter.new CheckUser(MainActivityBackup.this, firebaseAuth.getCurrentUser().getUid(), TASK_DB_LOAD).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                    //serverConnecter.new Http(MainActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                }
//                break;
//            }
//            case R.id.nav_login: {
//                startActivityForResult(new Intent(MainActivityBackup.this, AccountActivity.class), REQUEST_CODE_SIGN_IN);
//            }
//        }
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }
//
////    // 로그아웃
////    public void signOut() {
////        mGoogleApiClient.connect();
////        mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
////            @Override
////            public void onConnected(@Nullable Bundle bundle) {
////                mAuth.signOut();
////                if (mGoogleApiClient.isConnected()) {
////                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
////                        @Override
////                        public void onResult(@NonNull Status status) {
////                            if (status.isSuccess()) {
////                                Log.v("알림", "로그아웃 성공");
////                                setResult(1);
////                            } else {
////                                setResult(0);
////                            }
////                            finish();
////                        }
////                    });
////                }
////            }
////
////            @Override
////            public void onConnectionSuspended(int i) {
////                Log.v("알림", "Google API Client Connection Suspended");
////                setResult(-1);
////                finish();
////            }
////        });
////    }
//
//    //유저가 저장한 정보가 있는가
//    public void isUserExsist(String e, int code) {
//        try {
//            final JSONObject jsonObject = new JSONObject(e);
//            final String res = jsonObject.getString("res");
//            switch (code) {
//                case TASK_DB_SAVE: {
//                    switch(res) {
//                        case "1": {//이미 존재
//                            final String time = jsonObject.getString("date");
//                            AlertDialog.Builder a = new AlertDialog.Builder(MainActivityBackup.this);
//                            a.setTitle("이미 저장한 데이터가 있습니다.");
//                            a.setMessage(time + "에 저장한 내신성적을 덮어쓰고 저장하시겠습니까?");
//                            a.setPositiveButton("덮어쓰기", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    serverConnecter.new SaveDB(MainActivityBackup.this, firebaseAuth.getCurrentUser().getUid(), "1").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                                }
//                            });
//                            a.setNegativeButton("안하기", null);
//                            a.show();
//                            break;
//                        }
//                        case "0": {//없음
//                            serverConnecter.new SaveDB(MainActivityBackup.this, firebaseAuth.getCurrentUser().getUid(), "0").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                            break;
//                        }
//                        case "sererror": {//서버에러
//                            AlertDialog.Builder a = new AlertDialog.Builder(MainActivityBackup.this);
//                            a.setTitle("서버 에러");
//                            a.setMessage("서버에서 에러가 발생했습니다. 다음에 다시 시도해주세요.");
//                            a.setPositiveButton("확인", null);
//                            a.show();
//                            break;
//                        }
//                        case "err": {//클라이언트 에러
//                            AlertDialog.Builder a = new AlertDialog.Builder(MainActivityBackup.this);
//                            a.setTitle("앱 에러");
//                            a.setMessage("앱에서 에러가 발생했습니다. 다음에 다시 시도해주세요.");
//                            a.setPositiveButton("확인", null);
//                            a.show();
//                            break;
//                        }
//                        case "timerr": {//타임아웃 에러
//                            AlertDialog.Builder a = new AlertDialog.Builder(MainActivityBackup.this);
//                            a.setTitle("타임아웃 에러");
//                            a.setMessage("서버가 응답하지 읺습니다. 다음에 다시 시도해주세요.");
//                            a.setPositiveButton("확인", null);
//                            a.show();
//                            break;
//                        }
//                    }
////                    Toast.makeText(MainActivity.this, e, Toast.LENGTH_LONG).show();
//                    break;
//                }
//                case TASK_DB_LOAD: {
//                    switch(res) {
//                        case "1": {//이미 존재
//                            final String time = jsonObject.getString("date");
//                            AlertDialog.Builder a = new AlertDialog.Builder(MainActivityBackup.this);
//                            a.setTitle("저장된 데이터가 있습니다.");
//                            a.setMessage(time + "에 이 계정으로 저장된 성적이 있습니다. 저장된 데이터를 불러오면 현재 앱에서 입력한 데이터는 사라지고, 서버에 저장된 데이터가 앱에 저장됩니다.");
//                            a.setPositiveButton("확인", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    serverConnecter.new LoadDB(MainActivityBackup.this, firebaseAuth.getCurrentUser().getUid()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                                }
//                            });
//                            a.setNegativeButton("취소", null);
//                            a.show();
//                            break;
//                        }
//                        case "0": {//없음final String time = jsonObject.getString("date");
//                            AlertDialog.Builder a = new AlertDialog.Builder(MainActivityBackup.this);
//                            a.setTitle("저장된 데이터가 없습니다.");
//                            a.setMessage("이 계정으로 저장된 성적이 없습니다.");
//                            a.setPositiveButton("확인", null);
//                            a.show();
//                            break;
//                        }
//                        case "sererror": {//서버에러
//                            AlertDialog.Builder a = new AlertDialog.Builder(MainActivityBackup.this);
//                            a.setTitle("서버 에러");
//                            a.setMessage("서버에서 에러가 발생했습니다. 다음에 다시 시도해주세요.");
//                            a.setPositiveButton("확인", null);
//                            a.show();
//                            break;
//                        }
//                        case "err": {//클라이언트 에러
//                            AlertDialog.Builder a = new AlertDialog.Builder(MainActivityBackup.this);
//                            a.setTitle("앱 에러");
//                            a.setMessage("앱에서 에러가 발생했습니다. 다음에 다시 시도해주세요.");
//                            a.setPositiveButton("확인", null);
//                            a.show();
//                            break;
//                        }
//                        case "timerr": {//타임아웃 에러
//                            AlertDialog.Builder a = new AlertDialog.Builder(MainActivityBackup.this);
//                            a.setTitle("타임아웃 에러");
//                            a.setMessage("서버가 응답하지 읺습니다. 다음에 다시 시도해주세요.");
//                            a.setPositiveButton("확인", null);
//                            a.show();
//                            break;
//                        }
//                    }
//                    break;
//                }
//            }
//        } catch (JSONException e1) {
//            e1.printStackTrace();
//            AlertDialog.Builder a = new AlertDialog.Builder(MainActivityBackup.this);
//            a.setTitle("서버 에러");
//            a.setMessage("서버에서 잘못된 응답을 전송했습니다. 다음에 다시 시도해주세요.");
//            a.setPositiveButton("확인", null);
//            a.show();
//        }
//    }
//
//    //불러오기 성공 여부
//    public void isLoadSuccess(String result) {
//        switch (result) {
//            case "suc": {
//                Toast.makeText(MainActivityBackup.this, "불러오기에 성공했습니다.", Toast.LENGTH_SHORT).show();
//                setGradeText();
//                break;
//            }
//            case "sererror": {
//                AlertDialog.Builder a = new AlertDialog.Builder(MainActivityBackup.this);
//                a.setTitle("서버 에러");
//                a.setMessage("서버에서 에러가 발생했습니다. 다음에 다시 시도해주세요.");
//                a.setPositiveButton("확인", null);
//                a.show();
//                break;
//            }
//            case "err": {
//                AlertDialog.Builder a = new AlertDialog.Builder(MainActivityBackup.this);
//                a.setTitle("앱 에러");
//                a.setMessage("앱에서 에러가 발생했습니다. 다음에 다시 시도해주세요.");
//                a.setPositiveButton("확인", null);
//                a.show();
//                break;
//            }
//            case "dberror": {
//                AlertDialog.Builder a = new AlertDialog.Builder(MainActivityBackup.this);
//                a.setTitle("데이터베이스 에러");
//                a.setMessage("데이터베이스 조회 과정에서 에러가 발생했습니다. 다음에 다시 시도해주세요.");
//                a.setPositiveButton("확인", null);
//                a.show();
//                break;
//            }
//            case "resoponerror": {
//                AlertDialog.Builder a = new AlertDialog.Builder(MainActivityBackup.this);
//                a.setTitle("잘못된 값");
//                a.setMessage("서버에서 잘못된 값을 받았습니다. 다음에 다시 시도해주세요.");
//                a.setPositiveButton("확인", null);
//                a.show();
//                break;
//            }
//            case "timerr": {//타임아웃 에러
//                AlertDialog.Builder a = new AlertDialog.Builder(MainActivityBackup.this);
//                a.setTitle("타임아웃 에러");
//                a.setMessage("서버가 응답하지 읺습니다. 다음에 다시 시도해주세요.");
//                a.setPositiveButton("확인", null);
//                a.show();
//                break;
//            }
//        }
//    }
//
//    //저장 성공 여부
//    public void isSaveSuccess(String result) {
//        switch (result) {
//            case "suc": {
//                Toast.makeText(MainActivityBackup.this, "저장에 성공했습니다.", Toast.LENGTH_SHORT).show();
//                break;
//            }
//            case "sererror": {
//                AlertDialog.Builder a = new AlertDialog.Builder(MainActivityBackup.this);
//                a.setTitle("서버 에러");
//                a.setMessage("서버에서 에러가 발생했습니다. 다음에 다시 시도해주세요.");
//                a.setPositiveButton("확인", null);
//                a.show();
//                break;
//            }
//            case "err": {
//                AlertDialog.Builder a = new AlertDialog.Builder(MainActivityBackup.this);
//                a.setTitle("앱 에러");
//                a.setMessage("앱에서 에러가 발생했습니다. 다음에 다시 시도해주세요.");
//                a.setPositiveButton("확인", null);
//                a.show();
//                break;
//            }
//            case "dberror": {
//                AlertDialog.Builder a = new AlertDialog.Builder(MainActivityBackup.this);
//                a.setTitle("데이터베이스 에러");
//                a.setMessage("데이터베이스 조회 정에서 에러가 발생했습니다. 다음에 다시 시도해주세요.");
//                a.setPositiveButton("확인", null);
//                a.show();
//                break;
//            }
//            case "timerr": {//타임아웃 에러
//                AlertDialog.Builder a = new AlertDialog.Builder(MainActivityBackup.this);
//                a.setTitle("타임아웃 에러");
//                a.setMessage("서버가 응답하지 읺습니다. 다음에 다시 시도해주세요.");
//                a.setPositiveButton("확인", null);
//                a.show();
//                break;
//            }
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        Log.e("destroy", "destroy");
//        if (bp != null) {
//            bp.release();
//        }
////        if (firebaseAuth != null) {
////            FirebaseUser user = firebaseAuth.getCurrentUser();
////            if (user != null) {
////
////            }
////        }
//    }
//
//    public void startActivityScore(int mode) {
//        Intent intent = new Intent(MainActivityBackup.this, ScoreEditActivity.class);
//        switch (mode) {
//            case 0: {//all
//                intent.putExtra(ActivityKey.KEY_ACTIVITY_MODE, ActivityKey.KEY_ACTIVITY_SCORE_ALL);//1학년
//                scoreIndex = 0;
//                break;
//            }
//            case 1: {//1
//                intent.putExtra(ActivityKey.KEY_ACTIVITY_MODE, ActivityKey.KEY_ACTIVITY_SCORE_FIRST);//1학년
//                scoreIndex = 1;
//                break;
//            }
//            case 2: {//2
//                intent.putExtra(ActivityKey.KEY_ACTIVITY_MODE, ActivityKey.KEY_ACTIVITY_SCORE_SECOND);//2학년
//                scoreIndex = 2;
//                break;
//            }
//            case 3: {//3
//                intent.putExtra(ActivityKey.KEY_ACTIVITY_MODE, ActivityKey.KEY_ACTIVITY_SCORE_THIRD);//3학년
//                scoreIndex = 3;
//                break;
//            }
//        }
//        if (mInterstitialAd == null) {
//            startActivity(intent);
//        } else {
//            if (mInterstitialAd.isLoaded()) {
//                now = startScoreInt;
//                mInterstitialAd.show();
//            } else {
//                startActivity(intent);
//                mInterstitialAd.loadAd(new AdRequest.Builder().build());
//            }
//        }
//    }
//
//    private void startActivityAll(int code) {
//        switch (code) {
//            case startAnalyzeInt: {
//                startActivity(new Intent(MainActivityBackup.this, AnalyzeActivity.class));
//                break;
//            }
//            case startScoreInt: {
//                Intent intent = new Intent(MainActivityBackup.this, ScoreEditActivity.class);
//                switch (scoreIndex) {
//                    case 0: {//all
//                        intent.putExtra(ActivityKey.KEY_ACTIVITY_MODE, ActivityKey.KEY_ACTIVITY_SCORE_ALL);//1학년
//                        break;
//                    }
//                    case 1: {//1
//                        intent.putExtra(ActivityKey.KEY_ACTIVITY_MODE, ActivityKey.KEY_ACTIVITY_SCORE_FIRST);//1학년
//                        break;
//                    }
//                    case 2: {//2
//                        intent.putExtra(ActivityKey.KEY_ACTIVITY_MODE, ActivityKey.KEY_ACTIVITY_SCORE_SECOND);//2학년
//                        break;
//                    }
//                    case 3: {//3
//                        intent.putExtra(ActivityKey.KEY_ACTIVITY_MODE, ActivityKey.KEY_ACTIVITY_SCORE_THIRD);//3학년
//                        break;
//                    }
//                }
//                startActivity(intent);
//                break;
//            }
//        }
//    }
//}
