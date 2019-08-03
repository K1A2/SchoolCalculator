package com.k1a2.schoolcalculator.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.preference.PreferenceManager;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
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
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

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


        //리스너 연결
        button_editScoreAll.setOnClickListener(onScoreEditButton);
        button_editScore1.setOnClickListener(onScoreEditButton);
        button_editScore2.setOnClickListener(onScoreEditButton);
        button_editScore3.setOnClickListener(onScoreEditButton);

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        setGradeText();
    }

    private void setGradeText() {
        final float[] f11 = getGrade(11);
        final float[] f12 = getGrade(12);
        final float[] f21 = getGrade(21);
        final float[] f22 = getGrade(22);
        final float[] f31 = getGrade(31);
        final float[] f32 = getGrade(32);

        float result11 = 0;
        float result12 = 0;
        float result21 = 0;
        float result22 = 0;
        float result31 = 0;
        float result32 = 0;

        int r1 = preferences_rate.getInt(PreferenceKey.KEY_INT_RATE_NAME_1, 0);
        int r2 = preferences_rate.getInt(PreferenceKey.KEY_INT_RATE_NAME_2, 0);
        int r3 = preferences_rate.getInt(PreferenceKey.KEY_INT_RATE_NAME_3, 0);

        if (r1 == 0||r2 == 0||r3 == 0) {
            r1 = 1;
            r2 = 1;
            r3 = 1;
        }

        //텍스트뷰에 함수 값 연결
        if (f11 == null) {
            result11 = 0;
            textView11.setText("NaN");
        } else {
            result11 = (float) (Math.round(f11[0]/f11[1]*100)/100.0);
            textView11.setText(String.valueOf(result11));
        }
        if (f12 == null) {
            result12 = 0;
            textView12.setText("NaN");
        } else {
            result12 = (float) (Math.round(f12[0]/f12[1]*100)/100.0);
            textView12.setText(String.valueOf(result12));
        }
        if (f21 == null) {
            result21 = 0;
            textView21.setText("NaN");
        } else {
            result21 = (float) (Math.round(f21[0]/f21[1]*100)/100.0);
            textView21.setText(String.valueOf(result21));
        }
        if (f22 == null) {
            result22 = 0;
            textView22.setText("NaN");
        } else {
            result22 = (float) (Math.round(f22[0]/f22[1]*100)/100.0);
            textView22.setText(String.valueOf(result22));
        }
        if (f31 == null) {
            result31 = 0;
            textView31.setText("NaN");
        } else {
            result31 = (float) (Math.round(f31[0]/f31[1]*100)/100.0);
            textView31.setText(String.valueOf(result31));
        }
        if (f32 == null) {
            result32 = 0;
            textView32.setText("NaN");
        } else {
            result32 = (float) (Math.round(f32[0]/f32[1]*100)/100.0);
            textView32.setText(String.valueOf(result32));
        }
        if (f11 !=  null&&f12 != null) {
            textSum1.setText(String.valueOf(Math.round((f11[0] + f12[0])/(f11[1] + f12[1])*100)/100.0));
        } else if (f11 ==  null&&f12 != null) {
            textSum1.setText(String.valueOf(Math.round(f12[0]/f12[1]*100)/100.0));
        } else if (f11 !=  null&&f12 == null) {
            textSum1.setText(String.valueOf(Math.round(f11[0]/f11[1]*100)/100.0));
        } else {
            textSum1.setText("NaN");
        }
        if (f21 !=  null&&f22 != null) {
            textSum2.setText(String.valueOf(Math.round((f21[0] + f22[0])/(f21[1] + f22[1])*100)/100.0));
        } else if (f21 ==  null&&f22 != null) {
            textSum2.setText(String.valueOf(Math.round(f22[0]/f22[1]*100)/100.0));
        } else if (f21 !=  null&&f22 == null) {
            textSum2.setText(String.valueOf(Math.round(f21[0]/f21[1]*100)/100.0));
        } else {
            textSum2.setText("NaN");
        }
        if (f31 !=  null&&f32 != null) {
            textSum3.setText(String.valueOf(Math.round((f31[0] + f32[0])/(f31[1] + f32[1])*100)/100.0));
        } else if (f31 ==  null&&f32 != null) {
            textSum3.setText(String.valueOf(Math.round(f32[0]/f32[1]*100)/100.0));
        } else if (f31 !=  null&&f32 == null) {
            textSum3.setText(String.valueOf(Math.round(f31[0]/f31[1]*100)/100.0));
        } else {
            textSum3.setText("NaN");
        }
        if (f11 == null&&f12 == null&&f21 == null&&f22 == null&&f31 == null&&f32 == null) {
            textViewAll.setText("NaN");
            textViewAllBar.setText("NaN");
        } else {
            float ap1 = 0;
            float ag1 = 0;
            float ap2 = 0;
            float ag2 = 0;
            float ap3 = 0;
            float ag3 = 0;

            if (f11 != null) {
                ag1 += f11[0];
                ap1 += f11[1];
            }
            if (f12 != null) {
                ag1 += f12[0];
                ap1 += f12[1];
            }
            if (f21 != null) {
                ag2 += f21[0];
                ap2 += f21[1];
            }
            if (f22 != null) {
                ag2 += f22[0];
                ap2 += f22[1];
            }
            if (f31 != null) {
                ag3 += f31[0];
                ap3 += f31[1];
            }
            if (f32 != null) {
                ag3 += f32[0];
                ap3 += f32[1];
            }

            if (r1 == 1&&r2 == 1&&r3 == 1) {
                text_rate.setText(String.format("등급 반영 비율 %d:%d:%d", r1, r2, r3));
                final String c = String.valueOf(Math.round((ag1 + ag2 + ag3)/(ap1 + ap2 + ap3)*100)/100.0);
                textViewAll.setText(c);
                textViewAllBar.setText(c);
            } else {
                text_rate.setText(String.format("등급 반영 비율 %d:%d:%d", r1, r2, r3));
                final float rA = r1 + r2 + r3;
                final String c = String.valueOf(Math.round(((ag1/ap1*r1/rA) + (ag2/ap2*r2/rA) + (ag3/ap3*r3/rA))*100)/100.0);
                textViewAll.setText(c);
                textViewAllBar.setText(c);
            }
        }

        final ArrayList<String> xArray = new ArrayList<>();
        xArray.add("1학년 1학기");
        xArray.add("1학년 2학기");
        xArray.add("2학년 1학기");
        xArray.add("2학년 2학기");
        xArray.add("3학년 1학기");
        xArray.add("3학년 2학기");

        final ArrayList<Entry> data = new ArrayList<>();
        data.add(new Entry(0, result11));
        data.add(new Entry(1, result12));
        data.add(new Entry(2, result21));
        data.add(new Entry(3, result22));
        data.add(new Entry(4, result31));
        data.add(new Entry(5, result32));

        LineDataSet lineDataSet = new LineDataSet(data, "성적");
        lineDataSet.setColors(Color.RED);
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setDrawValues(false);

        LineData lineData = new LineData(lineDataSet);
        chart_analyze.setData(lineData);

        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xArray.get((int) value);
            }
        };


        XAxis xAxis = chart_analyze.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setValueFormatter(formatter);

        YAxis yAxis = chart_analyze.getAxisLeft();
        yAxis.setTextColor(Color.BLACK);
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
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //학기별 등급 산출 함수
    private float[] getGrade(int type) {
        float sumgrade = 0; // 등급, 단위수 곱한 후 합산
        float sumpoint = 0;

        switch (type) {
            case 11: {
                ArrayList<Integer[]> a = scoreDatabaseHelper.getGP(DatabaseKey.KEY_TABLE_11);
                if (a==null) {
                    return null;
                } else {
                    for (Integer[] r : a) {
                        sumgrade += r[1] * r[0];
                        sumpoint += r[1];
                    }
                    return new float[] {sumgrade, sumpoint};
                }
            }
            case 12: {
                ArrayList<Integer[]> a = scoreDatabaseHelper.getGP(DatabaseKey.KEY_TABLE_12);
                if (a==null) {
                    return null;
                } else {
                    for (Integer[] r : a) {
                        sumgrade += r[1] * r[0];
                        sumpoint += r[1];
                    }
                    return new float[] {sumgrade, sumpoint};
                }
            }
            case 21: {
                ArrayList<Integer[]> a = scoreDatabaseHelper.getGP(DatabaseKey.KEY_TABLE_21);
                if (a==null) {
                    return null;
                } else {
                    for (Integer[] r : a) {
                        sumgrade += r[1] * r[0];
                        sumpoint += r[1];
                    }
                    return new float[] {sumgrade, sumpoint};
                }
            }
            case 22: {
                ArrayList<Integer[]> a = scoreDatabaseHelper.getGP(DatabaseKey.KEY_TABLE_22);
                if (a==null) {
                    return null;
                } else {
                    for (Integer[] r : a) {
                        sumgrade += r[1] * r[0];
                        sumpoint += r[1];
                    }
                    return new float[] {sumgrade, sumpoint};
                }
            }
            case 31: {
                ArrayList<Integer[]> a = scoreDatabaseHelper.getGP(DatabaseKey.KEY_TABLE_31);
                if (a==null) {
                    return null;
                } else {
                    for (Integer[] r : a) {
                        sumgrade += r[1] * r[0];
                        sumpoint += r[1];
                    }
                    return new float[] {sumgrade, sumpoint};
                }
            }
            case 32: {
                ArrayList<Integer[]> a = scoreDatabaseHelper.getGP(DatabaseKey.KEY_TABLE_32);
                if (a==null) {
                    return null;
                } else {
                    for (Integer[] r : a) {
                        sumgrade += r[1] * r[0];
                        sumpoint += r[1];
                    }
                    return new float[] {sumgrade, sumpoint};
                }
            }
            default: {
                return null;
            }
        }
    }
}
