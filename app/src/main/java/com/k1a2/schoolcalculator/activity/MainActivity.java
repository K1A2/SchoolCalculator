package com.k1a2.schoolcalculator.activity;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.k1a2.schoolcalculator.R;
import com.k1a2.schoolcalculator.database.DatabaseKey;
import com.k1a2.schoolcalculator.database.ScoreDatabaseHelper;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Button button_editScoreAll = null;
    private Button button_editScore1 = null;
    private Button button_editScore2 = null;
    private Button button_editScore3 = null;
    private DrawerLayout drawer = null;

    private ScoreDatabaseHelper scoreDatabaseHelper = null;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        scoreDatabaseHelper = new ScoreDatabaseHelper(this, DatabaseKey.KEY_DB_NAME, null, 1);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


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



        //리스너 연결
        button_editScoreAll.setOnClickListener(onScoreEditButton);
        button_editScore1.setOnClickListener(onScoreEditButton);
        button_editScore2.setOnClickListener(onScoreEditButton);
        button_editScore3.setOnClickListener(onScoreEditButton);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //평균등급 텍스트들
        TextView textView11 = findViewById(R.id.main_11text);
        TextView textView12 = findViewById(R.id.main_12text);
        TextView textView21 = findViewById(R.id.main_21text);
        TextView textView22 = findViewById(R.id.main_22text);
        TextView textView31 = findViewById(R.id.main_31text);
        TextView textView32 = findViewById(R.id.main_32text);

        //텍스트뷰에 함수 값 연결
        textView11.setText(String.valueOf(getGrade(11)));
        textView12.setText(String.valueOf(getGrade(12)));
        textView21.setText(String.valueOf(getGrade(21)));
        textView22.setText(String.valueOf(getGrade(22)));
        textView31.setText(String.valueOf(getGrade(31)));
        textView32.setText(String.valueOf(getGrade(32)));
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
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //학기별 등급 산출 함수
    private float getGrade(int type) {
        float sumgrade = 0; // 등급, 단위수 곱한 후 합산
        float sumpoint = 0;
        float sumGP11 = 0;
        float sumGP12 = 0;
        float sumGP21 = 0;
        float sumGP22 = 0;
        float sumGP31 = 0;
        float sumGP32 = 0;

        switch (type) {
            case 11: {
                ArrayList<Integer[]> a = scoreDatabaseHelper.getGP(DatabaseKey.KEY_TABLE_11);
                if (a==null) {
                    return 0;
                } else {
                    for (Integer[] r : a) {
                        sumgrade += r[1] * r[0];
                        sumpoint += r[1];
                    }
                    sumGP11 = sumgrade / sumpoint;
                    return sumGP11;
                }
            }
            case 12: {
                ArrayList<Integer[]> a = scoreDatabaseHelper.getGP(DatabaseKey.KEY_TABLE_12);
                if (a==null) {
                    return 0;
                } else {
                    for (Integer[] r : a) {
                        sumgrade += r[1] * r[0];
                        sumpoint += r[1];
                    }
                    sumGP12 = sumgrade / sumpoint;
                    return sumGP12;
                }
            }
            case 21: {
                ArrayList<Integer[]> a = scoreDatabaseHelper.getGP(DatabaseKey.KEY_TABLE_21);
                if (a==null) {
                    return 0;
                } else {
                    for (Integer[] r : a) {
                        sumgrade += r[1] * r[0];
                        sumpoint += r[1];
                    }
                    sumGP21 = sumgrade / sumpoint;
                    return sumGP21;
                }
            }
            case 22: {
                ArrayList<Integer[]> a = scoreDatabaseHelper.getGP(DatabaseKey.KEY_TABLE_22);
                if (a==null) {
                    return 0;
                } else {
                    for (Integer[] r : a) {
                        sumgrade += r[1] * r[0];
                        sumpoint += r[1];
                    }
                    sumGP22 = sumgrade / sumpoint;
                    return sumGP22;
                }
            }
            case 31: {
                ArrayList<Integer[]> a = scoreDatabaseHelper.getGP(DatabaseKey.KEY_TABLE_31);
                if (a==null) {
                    return 0;
                } else {
                    for (Integer[] r : a) {
                        sumgrade += r[1] * r[0];
                        sumpoint += r[1];
                    }
                    sumGP31 = sumgrade / sumpoint;
                    return sumGP31;
                }
            }
            case 32: {
                ArrayList<Integer[]> a = scoreDatabaseHelper.getGP(DatabaseKey.KEY_TABLE_32);
                if (a==null) {
                    return 0;
                } else {
                    for (Integer[] r : a) {
                        sumgrade += r[1] * r[0];
                        sumpoint += r[1];
                    }
                    sumGP32 = sumgrade / sumpoint;
                    return sumGP32;
                }
            }
            default: {
                return 0;
            }
        }
    }
}
