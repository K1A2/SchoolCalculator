package com.k1a2.schoolcalculator.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.k1a2.schoolcalculator.BuildConfig;
import com.k1a2.schoolcalculator.fragment.AnalyeTypeFragment;
import com.k1a2.schoolcalculator.fragment.AnalyzeGradeFragment;
import com.k1a2.schoolcalculator.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class AnalyzeActivity extends AppCompatActivity implements AnalyzeGradeFragment.OnCaptureViewRequestListener {

    private TabLayout tabLayout = null;
    private ViewPager viewPager = null;
    private Toolbar toolbar = null;
    private View view_indicator = null;

    private AnalyzeGradeFragment fragment_analyzeT = null;
    private AnalyeTypeFragment fragment_analyzeG = null;

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

    //캡쳐할 뷰가 오는곳
    @Override
    public void OnViewRequest(View captureView) {
        if (captureView == null) {
            Toast.makeText(this, "Error Capture", Toast.LENGTH_SHORT).show();
            //null이면 오류난거임
        } else {
            LinearLayout container;
            container = (LinearLayout)findViewById(R.id.fragment_analye_grade_layout);
            container.buildDrawingCache();
            Bitmap captureView1 = container.getDrawingCache();
            String adress = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/" + "/capture.jpeg";
            FileOutputStream fos;

            try {

                fos = new FileOutputStream(adress);
                captureView1.compress(Bitmap.CompressFormat.JPEG, 100, fos);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Uri uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID, new File(adress));


            Intent shareintent = new Intent(Intent.ACTION_SEND);

            shareintent.putExtra(Intent.EXTRA_STREAM, uri);
            shareintent.setType("image/*");

            Intent chooser = Intent.createChooser(shareintent, "친구에게 공유하기");
            startActivity(chooser);
            //아니면 멀쩡히 잘 온거
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_analyze, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_share: {
                if (fragment_analyzeT != null) {
                    fragment_analyzeT.requestView();
                }
                break;
            }
            case android.R.id.home: {
                finish();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
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
                    if (fragment_analyzeT != null) return fragment_analyzeT; else {
                        fragment_analyzeT = new AnalyzeGradeFragment();
                        return fragment_analyzeT;
                    }
                }
                case 1: {
                    if (fragment_analyzeG != null) return fragment_analyzeG; else {
                        fragment_analyzeG = new AnalyeTypeFragment();
                        return fragment_analyzeG;
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
