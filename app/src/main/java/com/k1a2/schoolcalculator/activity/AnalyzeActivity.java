package com.k1a2.schoolcalculator.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.tabs.TabLayout;
import com.k1a2.schoolcalculator.AnalyeGradeFragment;
import com.k1a2.schoolcalculator.AnalyzeTypeFragment;
import com.k1a2.schoolcalculator.R;
import com.k1a2.schoolcalculator.fragment.Grade1Fragment;
import com.k1a2.schoolcalculator.fragment.Grade2Fragment;
import com.k1a2.schoolcalculator.fragment.Grade3Fragment;

public class AnalyzeActivity extends AppCompatActivity {

    private TabLayout tabLayout = null;
    private ViewPager viewPager = null;
    private Toolbar toolbar = null;
    private View view_indicator = null;

    private AnalyzeTypeFragment fragment_analyzeT = null;
    private AnalyeGradeFragment fragment_analyzeG = null;

    private int indicatorWidth;
    private TabPagerAdapter tabPagerAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze);

        tabLayout = findViewById(R.id.score_tablayout);
        viewPager = findViewById(R.id.score_viwewpager);
        view_indicator = findViewById(R.id.indicator);
        toolbar = findViewById(R.id.edit_toolbar);

        toolbar.setTitle("성적 분석");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_arrow_back_white_24);

        TabLayout.Tab tab1 = tabLayout.newTab();
        tab1.setText("학기별 분석");
        tabLayout.addTab(tab1);
        TabLayout.Tab tab2 = tabLayout.newTab();
        tab2.setText("과목별 분석");
        tabLayout.addTab(tab2);

        setPageAdapter();

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                indicatorWidth = tabLayout.getWidth() / tabLayout.getTabCount();

                FrameLayout.LayoutParams indicatorParams = (FrameLayout.LayoutParams)view_indicator.getLayoutParams();
                indicatorParams.width = indicatorWidth;
                view_indicator.setLayoutParams(indicatorParams);
            }
        });
    }

    private void setPageAdapter() {
        tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.e("scrolled", String.valueOf(position) + " " + String.valueOf(positionOffset) + " " + String.valueOf(positionOffsetPixels));
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)view_indicator.getLayoutParams();

                //Multiply positionOffset with indicatorWidth to get translation
                float translationOffset =  (positionOffset + position) * indicatorWidth ;
                params.leftMargin = (int) translationOffset;
                view_indicator.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    //뷰페이져에 프래그먼트 붙이기
    private class TabPagerAdapter extends FragmentStatePagerAdapter {

        public TabPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: {
                    if (fragment_analyzeG != null) return fragment_analyzeG; else {
                        fragment_analyzeG = new AnalyeGradeFragment();
                        return fragment_analyzeG;
                    }
                }
                case 1: {
                    if (fragment_analyzeT != null) return fragment_analyzeT; else {
                        fragment_analyzeT = new AnalyzeTypeFragment();
                        return fragment_analyzeT;
                    }
                }
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
