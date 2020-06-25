package com.k1a2.schoolcalculator.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

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

import android.preference.PreferenceManager;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.k1a2.schoolcalculator.BillingKey;
import com.k1a2.schoolcalculator.GradeCalculator;
import com.k1a2.schoolcalculator.R;
import com.k1a2.schoolcalculator.database.DatabaseKey;
import com.k1a2.schoolcalculator.database.ScoreDatabaseHelper;
import com.k1a2.schoolcalculator.fragment.SchoolFragment;
import com.k1a2.schoolcalculator.fragment.TestFragment;
import com.k1a2.schoolcalculator.internet.ServerConnecter;
import com.k1a2.schoolcalculator.sharedpreference.AppStorage;
import com.k1a2.schoolcalculator.sharedpreference.PreferenceKey;
import com.k1a2.schoolcalculator.view.SwipeViewPager;
import com.k1a2.schoolcalculator.view.recyclerview.GradeViewItem;
import com.k1a2.schoolcalculator.view.recyclerview.GradeViewRecyclerAdapter;
import com.k1a2.schoolcalculator.view.recyclerview.LinePagerIndicatorDecoration;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**MainActivity 모든 정보를 한번에 보여주는 액티비티**/

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer = null;
    private NavigationView navigationView = null;
    private TextView ti = null;
    private SwipeViewPager swipeViewPager = null;
    private BottomNavigationView bottomNavigationView = null;

    private FirebaseAuth firebaseAuth = null;
    private ServerConnecter serverConnecter = new ServerConnecter();

    private static final int REQUEST_CODE_SIGN_IN_SAVE = 1;
    private static final int REQUEST_CODE_SIGN_IN_LOAD = 3;
    private static final int REQUEST_CODE_SIGN_IN = 5;

    private static final int TASK_DB_SAVE = 99;
    private static final int TASK_DB_SAVE_SCHOOL = 999;
    private static final int TASK_DB_SAVE_TEST = 9910;
    private static final int TASK_DB_LOAD = 100;
    private static final int TASK_DB_LOAD_SCHOOL = 1009;
    private static final int TASK_DB_LOAD_TEST = 1008;

    private SharedPreferences preference_check = null;

    private SchoolFragment schoolFragment= null;
    private TestFragment testFragment = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();

        preference_check = PreferenceManager.getDefaultSharedPreferences(this);
//        if (preferences_rate.getBoolean(PreferenceKey.KEY_BOOL_ISDARK_THEME, false)) {
//            setTheme(R.style.AppTheme_Dark);
//        }

        setContentView(R.layout.activity_mainv2);
        swipeViewPager = findViewById(R.id.frame_layout);
        swipeViewPager.setPagingEnabled(false);
        swipeViewPager.setAdapter(new ContentsPagerAdapter(getSupportFragmentManager(), 2));

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());

//        if (preferences_rate.getBoolean(PreferenceKey.KEY_BOOL_ISDARK_THEME, false)) {
//            ((CardView)findViewById(R.id.main_card_1)).setCardBackgroundColor(getResources().getColor(R.color.colorCardDark));
//            ((CardView)findViewById(R.id.main_card_2)).setCardBackgroundColor(getResources().getColor(R.color.colorCardDark));
//            ((CardView)findViewById(R.id.main_card_3)).setCardBackgroundColor(getResources().getColor(R.color.colorCardDark));
//            ((CardView)findViewById(R.id.main_card_4)).setCardBackgroundColor(getResources().getColor(R.color.colorCardDark));
//            ((CardView)findViewById(R.id.main_card_5)).setCardBackgroundColor(getResources().getColor(R.color.colorCardDark));
//            ((CardView)findViewById(R.id.main_card_6)).setCardBackgroundColor(getResources().getColor(R.color.colorCardDark));
//        }

        if (preference_check.getBoolean(PreferenceKey.KEY_BOOL_IS_LAUNCH_FIRST, true)) {
            preference_check.edit().putBoolean(PreferenceKey.KEY_BOOL_IS_LAUNCH_FIRST, false).commit();
            preference_check.edit().putInt(PreferenceKey.KEY_INT_COUNT_SHOW, 5).commit();
        }

//        if (preference_check.getBoolean("310first", true)) {
//            preference_check.edit().putBoolean("310first", false).commit();
//            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//            builder.setTitle("광고에 관한 공지사항");
//            builder.setMessage("서버비용 문제로 인해 부득이하게 전면광고를 추가하게 되었습니다. 사용자분들에게 양해의 말씀드립니다. 전면광고는 메인 화면에서 분석화면이나 점수 입력 화면으로 넘어가기 직전에 보여지게됩니다. 제발 광고 생겼다고 삭제하지 말아주세요..ㅠ");
//            builder.setPositiveButton("확인", null);
//            builder.show();
//        }

        final FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        firebaseRemoteConfig.fetch().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    firebaseRemoteConfig.activateFetched();

                    String alerts = firebaseRemoteConfig.getString("string_main_alert");
                    if (!alerts.isEmpty()) {
                        try {
                            String alert_b = preference_check.getString(PreferenceKey.KEY_STRING_ALERT, "");
                            boolean alert_Is = preference_check.getBoolean(PreferenceKey.KEY_BOOL_ALERT, false);

                            if (!alerts.equals(alert_b)) {
                                preference_check.edit().putString(PreferenceKey.KEY_STRING_ALERT, alerts).commit();
                                preference_check.edit().putBoolean(PreferenceKey.KEY_BOOL_ALERT, true).commit();
                                final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                                final View a = View.inflate(MainActivity.this, R.layout.dialog_alerts, null);
                                //final String i = firebaseRemoteConfig.getString("string_update_note");
                                alert.setView(a);
                                ((TextView) a.findViewById(R.id.dialog_alerts)).setText(Html.fromHtml(alerts));
                                alert.setPositiveButton("확인", null);
                                alert.setNegativeButton("다시 보지 않기", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        preference_check.edit().putBoolean(PreferenceKey.KEY_BOOL_ALERT, false).commit();
                                    }
                                });
//                        alert.setNeutralButton("다시 보지 않기", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//
//                            }
//                        });
                                final AlertDialog l = alert.create();
                                l.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.background_dialog_rate));
                                l.setOnShowListener(new DialogInterface.OnShowListener() {
                                    @Override
                                    public void onShow(DialogInterface dialogInterface) {
                                        l.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.rgb(46, 144, 242));
                                        l.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.rgb(255, 110, 158));
                                        //l.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(Color.rgb(255, 255, 255));
                                    }
                                });
                                l.show();
                            } else {
                                if (alert_Is) {
                                    final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                                    final View a = View.inflate(MainActivity.this, R.layout.dialog_alerts, null);
                                    //final String i = firebaseRemoteConfig.getString("string_update_note");
                                    alert.setView(a);
                                    ((TextView) a.findViewById(R.id.dialog_alerts)).setText(Html.fromHtml(alerts));
                                    alert.setPositiveButton("확인", null);
                                    alert.setNegativeButton("다시 보지 않기", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            preference_check.edit().putBoolean(PreferenceKey.KEY_BOOL_ALERT, false).commit();
                                        }
                                    });
//                        alert.setNeutralButton("다시 보지 않기", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//
//                            }
//                        });
                                    final AlertDialog l = alert.create();
                                    l.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.background_dialog_rate));
                                    l.setOnShowListener(new DialogInterface.OnShowListener() {
                                        @Override
                                        public void onShow(DialogInterface dialogInterface) {
                                            l.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.rgb(46, 144, 242));
                                            l.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.rgb(255, 110, 158));
                                            //l.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(Color.rgb(255, 255, 255));
                                        }
                                    });
                                    l.show();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("Alert err", e.getMessage());
                        }
                    }

                    final String version = firebaseRemoteConfig.getString("string_app_version");
                    if (!getString(R.string.app_version).equals(version)) {
                        final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                        final View a = View.inflate(MainActivity.this, R.layout.dialog_new_version, null);
                        //final String i = firebaseRemoteConfig.getString("string_update_note");
                        ((TextView) a.findViewById(R.id.dialog_version_new)).setText(String.format("\'%s\' 버전이 플레이 스토어에 출시되었습니다!\n바로 업데이트 해보세요!", version));
                        alert.setView(a);
                        alert.setPositiveButton("업데이트", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {
                                    startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("market://details?id=com.k1a2.schoolcalculator")));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(MainActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        alert.setNegativeButton("다음에 하기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
//                        alert.setNeutralButton("다시 보지 않기", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//
//                            }
//                        });
                        final AlertDialog l = alert.create();
                        l.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.background_dialog_rate));
                        l.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialogInterface) {
                                l.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.rgb(46, 144, 242));
                                l.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.rgb(255, 110, 158));
                                //l.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(Color.rgb(255, 255, 255));
                            }
                        });
                        l.show();
                    }
                }
            }
        });
    }



    class ContentsPagerAdapter extends FragmentStatePagerAdapter {
        private int mPageCount;

        public ContentsPagerAdapter(FragmentManager fm, int pageCount) {
            super(fm);
            this.mPageCount = pageCount;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (schoolFragment == null) {
                        schoolFragment = new SchoolFragment();
                        schoolFragment.setAuth(firebaseAuth);
                    }
                    return schoolFragment;

                case 1:
                    if (testFragment == null) {
                        testFragment = new TestFragment();
                    }
                    return testFragment;

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mPageCount;
        }
    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch (menuItem.getItemId()) {
                case R.id.in:
                    swipeViewPager.setCurrentItem(0);
//                    schoolFragment.loadAd();
                    break;

                case R.id.out:
                    swipeViewPager.setCurrentItem(1);
//                    testFragment.loadAd();
                    break;
            }
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ti = navigationView.getHeaderView(0).findViewById(R.id.login_id);
        switch (requestCode) {
            case REQUEST_CODE_SIGN_IN_SAVE: {
                switch (resultCode) {
                    case ActivityKey.LOGIN_RESULT_SUCCESS: {
                        if (firebaseAuth.getCurrentUser() != null) {
                            if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                                Toast.makeText(MainActivity.this, firebaseAuth.getCurrentUser().getEmail() + "으로 로그인 되었습니다.", Toast.LENGTH_SHORT).show();
                                navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
                                navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
                                ti.setVisibility(View.VISIBLE);
                                ti.setText(firebaseAuth.getCurrentUser().getEmail() + "로그인 ");
                            } else {
                                Toast.makeText(MainActivity.this, "이메일 인증을 완료해주세요.", Toast.LENGTH_SHORT).show();
                                firebaseAuth.signOut();
                                navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
                                navigationView.getMenu().findItem(R.id.nav_login).setVisible(true);
                                ti.setVisibility(View.GONE);
                            }
                        }
                        break;
                    }
                    case ActivityKey.LOGIN_RESULT_FAIL: {
                        if (firebaseAuth.getCurrentUser() != null) {
                            Toast.makeText(MainActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                }
                break;
            }
            case REQUEST_CODE_SIGN_IN_LOAD: {
                switch (resultCode) {
                    case ActivityKey.LOGIN_RESULT_SUCCESS: {
                        if (firebaseAuth.getCurrentUser() != null) {
                            if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                                Toast.makeText(MainActivity.this, firebaseAuth.getCurrentUser().getEmail() + "으로 로그인 되었습니다.", Toast.LENGTH_SHORT).show();
                                navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
                                navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
                                ti.setVisibility(View.VISIBLE);
                                ti.setText(firebaseAuth.getCurrentUser().getEmail() + "로그인 ");
                            } else {
                                Toast.makeText(MainActivity.this, "이메일 인증을 완료해주세요.", Toast.LENGTH_SHORT).show();
                                firebaseAuth.signOut();
                                navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
                                navigationView.getMenu().findItem(R.id.nav_login).setVisible(true);
                                ti.setVisibility(View.GONE);
                            }
                        }
                        break;
                    }
                    case ActivityKey.LOGIN_RESULT_FAIL: {
                        if (firebaseAuth.getCurrentUser() != null) {
                            Toast.makeText(MainActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                }
                break;
            }
            case REQUEST_CODE_SIGN_IN: {
                switch (resultCode) {
                    case ActivityKey.LOGIN_RESULT_SUCCESS: {
                        if (firebaseAuth.getCurrentUser() != null) {
                            if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                                Toast.makeText(MainActivity.this, firebaseAuth.getCurrentUser().getEmail() + "으로 로그인 되었습니다.", Toast.LENGTH_SHORT).show();
                                navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
                                navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
                                ti.setVisibility(View.VISIBLE);
                                ti.setText(firebaseAuth.getCurrentUser().getEmail() + "로그인 ");
                                //Toast.makeText(MainActivity.this, firebaseAuth.getCurrentUser().getProviderData()., Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "이메일 인증을 완료해주세요.", Toast.LENGTH_SHORT).show();
                                firebaseAuth.signOut();
                                navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
                                navigationView.getMenu().findItem(R.id.nav_login).setVisible(true);
                                ti.setVisibility(View.GONE);
                            }
                        }
                        break;
                    }
                    case ActivityKey.LOGIN_RESULT_FAIL: {
                        if (firebaseAuth.getCurrentUser() != null) {
                            Toast.makeText(MainActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                }
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {//뒤로가기키 (소프트or물리)가 눌렸을때
        if (bottomNavigationView.getSelectedItemId() == R.id.in) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {//메뉴가 열려있음 닫음
                drawer.closeDrawer(GravityCompat.START);
            } else {//아님 액티비티 종료
                super.onBackPressed();
            }
        } else {
            bottomNavigationView.setSelectedItemId(R.id.in);
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
                schoolFragment.startScoreEditAvtivity(0);
                break;
            }
            case R.id.nav_search: {//대학정보
                try {
                    Intent intentS = new Intent(Intent.ACTION_VIEW);
                    intentS.setData(Uri.parse("http://adiga.kr/PageLinkAll.do?link=/kcue/ast/eip/eis/inf/univinf/eipUinfGnrl.do&p_menu_id=PG-EIP-01701"));
                    startActivity(intentS);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.nav_homepage: {//홈페이지 연결
                try {
                    Intent intentH = new Intent(Intent.ACTION_VIEW);
                    intentH.setData(Uri.parse("https://ldm0830.wixsite.com/schoolcalculator"));
                    startActivity(intentH);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.nav_chart: {//분석
                Intent intentC = new Intent(MainActivity.this, AnalyzeActivity.class);
                intentC.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentC);
                break;
            }
            case R.id.nav_save: {
                if (firebaseAuth.getCurrentUser() == null) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                    alertDialog.setTitle("로그인 필요");
//                    if (versionInt < Build.VERSION_CODES.M) {
//                        alertDialog = new AlertDialog.Builder(MainActivity.this, R.style.Dialog);
//                    }
                    alertDialog.setMessage("클라우드 저장 기능을 사용하려면 로그인이 필요합니다.");
                    alertDialog.setPositiveButton("로그인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivityForResult(new Intent(MainActivity.this, AccountActivity.class), REQUEST_CODE_SIGN_IN_SAVE);
                        }
                    });
                    alertDialog.setNegativeButton("그만두기", null);
                    alertDialog.show();
                } else {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                    builder.setMessage("어떤 성적을 저장할지 선택해주세요.");
//                    builder.setPositiveButton("수시만", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            serverConnecter.new CheckUser(MainActivity.this, firebaseAuth.getCurrentUser().getUid(), TASK_DB_SAVE_SCHOOL).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                        }
//                    });
//                    builder.setNegativeButton("모의고사만", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            serverConnecter.new CheckUser(MainActivity.this, firebaseAuth.getCurrentUser().getUid(), TASK_DB_SAVE_TEST).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                        }
//                    });
//                    builder.setNeutralButton("모두 저장", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
                            serverConnecter.new CheckUser(MainActivity.this, firebaseAuth.getCurrentUser().getUid(), TASK_DB_SAVE).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                        }
//                    });
//                    builder.show();
                    //FirebaseAuth.getInstance().signOut();
                }
                break;
            }
            case R.id.nav_logout: {
                firebaseAuth.signOut();
                navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
                navigationView.getMenu().findItem(R.id.nav_login).setVisible(true);
                ti.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "로그아웃 됨", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.nav_load: {
                if (firebaseAuth.getCurrentUser() == null) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                    alertDialog.setTitle("로그인 필요");
                    alertDialog.setMessage("클라우드 가져오기 기능을 사용하려면 로그인이 필요합니다.");
                    alertDialog.setPositiveButton("로그인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivityForResult(new Intent(MainActivity.this, AccountActivity.class), REQUEST_CODE_SIGN_IN_LOAD);
                        }
                    });
                    alertDialog.setNegativeButton("그만두기", null);
                    alertDialog.show();
                } else {
                    //FirebaseAuth.getInstance().signOut();
                    serverConnecter.new CheckUser(MainActivity.this, firebaseAuth.getCurrentUser().getUid(), TASK_DB_LOAD).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    //serverConnecter.new Http(MainActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
                break;
            }
            case R.id.nav_login: {
                startActivityForResult(new Intent(MainActivity.this, AccountActivity.class), REQUEST_CODE_SIGN_IN);
            }
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    // 로그아웃
//    public void signOut() {
//        mGoogleApiClient.connect();
//        mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
//            @Override
//            public void onConnected(@Nullable Bundle bundle) {
//                mAuth.signOut();
//                if (mGoogleApiClient.isConnected()) {
//                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
//                        @Override
//                        public void onResult(@NonNull Status status) {
//                            if (status.isSuccess()) {
//                                Log.v("알림", "로그아웃 성공");
//                                setResult(1);
//                            } else {
//                                setResult(0);
//                            }
//                            finish();
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onConnectionSuspended(int i) {
//                Log.v("알림", "Google API Client Connection Suspended");
//                setResult(-1);
//                finish();
//            }
//        });
//    }

    //유저가 저장한 정보가 있는가
    public void isUserExsist(String e, int code) {
        try {
            final JSONObject jsonObject = new JSONObject(e);
            final String res = jsonObject.getString("res");
            switch (code) {
                case TASK_DB_SAVE: {
                    switch(res) {
                        case "1": {//이미 존재
                            final String time = jsonObject.getString("date");
                            AlertDialog.Builder a = new AlertDialog.Builder(MainActivity.this);
                            a.setTitle("이미 저장한 데이터가 있습니다.");
                            a.setMessage(time + "에 저장한 내신성적을 덮어쓰고 저장하시겠습니까?");
                            a.setPositiveButton("덮어쓰기", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    serverConnecter.new SaveDB(MainActivity.this, firebaseAuth.getCurrentUser().getUid(), "1").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                }
                            });
                            a.setNegativeButton("안하기", null);
                            a.show();
                            break;
                        }
                        case "0": {//없음
                            serverConnecter.new SaveDB(MainActivity.this, firebaseAuth.getCurrentUser().getUid(), "0").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            break;
                        }
                        case "sererror": {//서버에러
                            AlertDialog.Builder a = new AlertDialog.Builder(MainActivity.this);
                            a.setTitle("서버 에러");
                            a.setMessage("서버에서 에러가 발생했습니다. 다음에 다시 시도해주세요.");
                            a.setPositiveButton("확인", null);
                            a.show();
                            break;
                        }
                        case "err": {//클라이언트 에러
                            AlertDialog.Builder a = new AlertDialog.Builder(MainActivity.this);
                            a.setTitle("앱 에러");
                            a.setMessage("앱에서 에러가 발생했습니다. 다음에 다시 시도해주세요.");
                            a.setPositiveButton("확인", null);
                            a.show();
                            break;
                        }
                        case "timerr": {//타임아웃 에러
                            AlertDialog.Builder a = new AlertDialog.Builder(MainActivity.this);
                            a.setTitle("타임아웃 에러");
                            a.setMessage("서버가 응답하지 읺습니다. 다음에 다시 시도해주세요.");
                            a.setPositiveButton("확인", null);
                            a.show();
                            break;
                        }
                    }
//                    Toast.makeText(MainActivity.this, e, Toast.LENGTH_LONG).show();
                    break;
                }
                case TASK_DB_LOAD: {
                    switch(res) {
                        case "1": {//이미 존재
                            final String time = jsonObject.getString("date");
                            AlertDialog.Builder a = new AlertDialog.Builder(MainActivity.this);
                            a.setTitle("저장된 데이터가 있습니다.");
                            a.setMessage(time + "에 이 계정으로 저장된 성적이 있습니다. 저장된 데이터를 불러오면 현재 앱에서 입력한 데이터는 사라지고, 서버에 저장된 데이터가 앱에 저장됩니다.");
                            a.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    serverConnecter.new LoadDB(MainActivity.this, firebaseAuth.getCurrentUser().getUid()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                }
                            });
                            a.setNegativeButton("취소", null);
                            a.show();
                            break;
                        }
                        case "0": {//없음final String time = jsonObject.getString("date");
                            AlertDialog.Builder a = new AlertDialog.Builder(MainActivity.this);
                            a.setTitle("저장된 데이터가 없습니다.");
                            a.setMessage("이 계정으로 저장된 성적이 없습니다.");
                            a.setPositiveButton("확인", null);
                            a.show();
                            break;
                        }
                        case "sererror": {//서버에러
                            AlertDialog.Builder a = new AlertDialog.Builder(MainActivity.this);
                            a.setTitle("서버 에러");
                            a.setMessage("서버에서 에러가 발생했습니다. 다음에 다시 시도해주세요.");
                            a.setPositiveButton("확인", null);
                            a.show();
                            break;
                        }
                        case "err": {//클라이언트 에러
                            AlertDialog.Builder a = new AlertDialog.Builder(MainActivity.this);
                            a.setTitle("앱 에러");
                            a.setMessage("앱에서 에러가 발생했습니다. 다음에 다시 시도해주세요.");
                            a.setPositiveButton("확인", null);
                            a.show();
                            break;
                        }
                        case "timerr": {//타임아웃 에러
                            AlertDialog.Builder a = new AlertDialog.Builder(MainActivity.this);
                            a.setTitle("타임아웃 에러");
                            a.setMessage("서버가 응답하지 읺습니다. 다음에 다시 시도해주세요.");
                            a.setPositiveButton("확인", null);
                            a.show();
                            break;
                        }
                    }
                    break;
                }
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
            AlertDialog.Builder a = new AlertDialog.Builder(MainActivity.this);
            a.setTitle("서버 에러");
            a.setMessage("서버에서 잘못된 응답을 전송했습니다. 다음에 다시 시도해주세요.");
            a.setPositiveButton("확인", null);
            a.show();
        }
    }

    //불러오기 성공 여부
    public void isLoadSuccess(String result) {
        switch (result) {
            case "suc": {
                Toast.makeText(MainActivity.this, "불러오기에 성공했습니다.", Toast.LENGTH_SHORT).show();
                schoolFragment.setGradeText();
                break;
            }
            case "sererror": {
                AlertDialog.Builder a = new AlertDialog.Builder(MainActivity.this);
                a.setTitle("서버 에러");
                a.setMessage("서버에서 에러가 발생했습니다. 다음에 다시 시도해주세요.");
                a.setPositiveButton("확인", null);
                a.show();
                break;
            }
            case "err": {
                AlertDialog.Builder a = new AlertDialog.Builder(MainActivity.this);
                a.setTitle("앱 에러");
                a.setMessage("앱에서 에러가 발생했습니다. 다음에 다시 시도해주세요.");
                a.setPositiveButton("확인", null);
                a.show();
                break;
            }
            case "dberror": {
                AlertDialog.Builder a = new AlertDialog.Builder(MainActivity.this);
                a.setTitle("데이터베이스 에러");
                a.setMessage("데이터베이스 조회 과정에서 에러가 발생했습니다. 다음에 다시 시도해주세요.");
                a.setPositiveButton("확인", null);
                a.show();
                break;
            }
            case "resoponerror": {
                AlertDialog.Builder a = new AlertDialog.Builder(MainActivity.this);
                a.setTitle("잘못된 값");
                a.setMessage("서버에서 잘못된 값을 받았습니다. 다음에 다시 시도해주세요.");
                a.setPositiveButton("확인", null);
                a.show();
                break;
            }
            case "timerr": {//타임아웃 에러
                AlertDialog.Builder a = new AlertDialog.Builder(MainActivity.this);
                a.setTitle("타임아웃 에러");
                a.setMessage("서버가 응답하지 읺습니다. 다음에 다시 시도해주세요.");
                a.setPositiveButton("확인", null);
                a.show();
                break;
            }
        }
    }

    //저장 성공 여부
    public void isSaveSuccess(String result) {
        switch (result) {
            case "suc": {
                Toast.makeText(MainActivity.this, "저장에 성공했습니다.", Toast.LENGTH_SHORT).show();
                break;
            }
            case "sererror": {
                AlertDialog.Builder a = new AlertDialog.Builder(MainActivity.this);
                a.setTitle("서버 에러");
                a.setMessage("서버에서 에러가 발생했습니다. 다음에 다시 시도해주세요.");
                a.setPositiveButton("확인", null);
                a.show();
                break;
            }
            case "err": {
                AlertDialog.Builder a = new AlertDialog.Builder(MainActivity.this);
                a.setTitle("앱 에러");
                a.setMessage("앱에서 에러가 발생했습니다. 다음에 다시 시도해주세요.");
                a.setPositiveButton("확인", null);
                a.show();
                break;
            }
            case "dberror": {
                AlertDialog.Builder a = new AlertDialog.Builder(MainActivity.this);
                a.setTitle("데이터베이스 에러");
                a.setMessage("데이터베이스 조회 정에서 에러가 발생했습니다. 다음에 다시 시도해주세요.");
                a.setPositiveButton("확인", null);
                a.show();
                break;
            }
            case "timerr": {//타임아웃 에러
                AlertDialog.Builder a = new AlertDialog.Builder(MainActivity.this);
                a.setTitle("타임아웃 에러");
                a.setMessage("서버가 응답하지 읺습니다. 다음에 다시 시도해주세요.");
                a.setPositiveButton("확인", null);
                a.show();
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("destroy", "destroy");
//        if (firebaseAuth != null) {
//            FirebaseUser user = firebaseAuth.getCurrentUser();
//            if (user != null) {
//
//            }
//        }
    }

    public void setDrawer(DrawerLayout drawer, NavigationView navigationView) {
        this.drawer = drawer;
        this.navigationView = navigationView;
    }
}
