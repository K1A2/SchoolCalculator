package com.k1a2.schoolcalculator.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;

import com.k1a2.schoolcalculator.R;

/**목표설정탭 액티비티
 * 개발 중단됨**/

public class GoalActivity extends AppCompatActivity {

    private Toolbar toolbar = null;//툴바

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        toolbar = findViewById(R.id.goal_toolbar);

        toolbar.setTitle("목표 설정&분석");//툴바에 보여질 택스트 설정
        setSupportActionBar(toolbar);//이 액티비티의 툴바 지정
        //툴바에 뒤로가기 버튼 지정
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_arrow_back_white_24);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {//툴바 메뉴 클릭시
        switch (item.getItemId()) {
            case android.R.id.home: {//뒤로가기일때
                finish();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
