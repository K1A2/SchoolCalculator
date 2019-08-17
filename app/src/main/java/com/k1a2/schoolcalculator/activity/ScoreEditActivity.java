package com.k1a2.schoolcalculator.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.tabs.TabLayout;
import com.k1a2.schoolcalculator.R;
import com.k1a2.schoolcalculator.fragment.Grade1Fragment;
import com.k1a2.schoolcalculator.fragment.Grade2Fragment;
import com.k1a2.schoolcalculator.fragment.Grade3Fragment;
import com.k1a2.schoolcalculator.sharedpreference.AppStorage;

/**성적 입력 액티비티**/

public class ScoreEditActivity extends AppCompatActivity {

    private Fragment fragment_grade1 = null;//1학년 입력 프래그먼트
    private Fragment fragment_grade2 = null;//2학년 입력 프래그먼트
    private Fragment fragment_grade3 = null;//3학년 입력 프래그먼트
    private View view_indicator = null;//커스터 인디케이터
    private TabLayout tabLayout = null;//화면전환 버튼
    private ViewPager viewPager = null;//화면전환 영역
    private Toolbar toolbar = null;//툴바
    private AdView adView = null;

    private int indicatorWidth;//인디ㅔ이터 너비
    private TabPagerAdapter tabPagerAdapter = null;//탭 전환 어댑터
    private boolean isFirst = false;//처음 실행인지 여부
    private AppStorage storage = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_main);
        isFirst = true;//처음 실행여부 true로 설정

        storage = new AppStorage(this);
        adView = findViewById(R.id.ads_s);

        if (storage.purchasedRemoveAds()) {
            adView.setVisibility(View.GONE);
        } else {
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
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);//??

        tabLayout = findViewById(R.id.score_tablayout);
        viewPager = findViewById(R.id.score_viwewpager);
        view_indicator = findViewById(R.id.indicator);
        toolbar = findViewById(R.id.edit_toolbar);

        toolbar.setTitle("성적 입력하기");
        setSupportActionBar(toolbar);
        //툴바에 뒤로가기 버튼 추가
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_arrow_back_white_24);

        //탭 설정
        TabLayout.Tab tab1 = tabLayout.newTab();
        tab1.setText("1학년");
        tabLayout.addTab(tab1);
        TabLayout.Tab tab2 = tabLayout.newTab();
        tab2.setText("2학년");
        tabLayout.addTab(tab2);
        TabLayout.Tab tab3 = tabLayout.newTab();
        tab3.setText("3학년");
        tabLayout.addTab(tab3);

        setPageAdapter();//화면전환 어댑터 설정

        //동시에 로딩되는 프래그먼트 갯수 설정
        viewPager.setOffscreenPageLimit(3);

        //탭의 움직임에 따라 인디케이터 움직임
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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {//화면의 포커스가 바뀔때 실행됨
        super.onWindowFocusChanged(hasFocus);
        final Intent intent = getIntent();
        if (isFirst) {//처음 한번만 실행되도록 의도
            switch (intent.getStringExtra(ActivityKey.KEY_ACTIVITY_MODE)) {//1,2,3학년중 어느 프래그먼트를 보일지 결정
                case ActivityKey.KEY_ACTIVITY_SCORE_ALL: {
                    tabLayout.getTabAt(0).select();
                    break;
                }
                case ActivityKey.KEY_ACTIVITY_SCORE_FIRST: {
                    tabLayout.getTabAt(0).select();
                    break;
                }
                case ActivityKey.KEY_ACTIVITY_SCORE_SECOND: {
                    tabLayout.getTabAt(1).select();
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)view_indicator.getLayoutParams();
                    int width = tabLayout.getMeasuredWidth();
                    params.leftMargin = (int) width / 2;
                    view_indicator.setLayoutParams(params);
                    break;
                }
                case ActivityKey.KEY_ACTIVITY_SCORE_THIRD: {
                    tabLayout.getTabAt(2).select();
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)view_indicator.getLayoutParams();
                    int width = tabLayout.getMeasuredWidth();
                    params.leftMargin = (int) width - view_indicator.getWidth();
                    view_indicator.setLayoutParams(params);
                    break;
                }
            }
            isFirst = false;//다시는 실행되지 않도록 바꿈
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {//툴바 메뉴 설정
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setPageAdapter() {//화면 전환 어댑터 설정
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
                    if (fragment_grade1 != null) return fragment_grade1; else {
                        fragment_grade1 = new Grade1Fragment();
                        return fragment_grade1;
                    }
                }
                case 1: {
                    if (fragment_grade2 != null) return fragment_grade2; else {
                        fragment_grade2 = new Grade2Fragment();
                        return fragment_grade2;
                    }
                }
                case 2: {
                    if (fragment_grade3 != null) return fragment_grade3; else {
                        fragment_grade3 = new Grade3Fragment();
                        return fragment_grade3;
                    }
                }
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
