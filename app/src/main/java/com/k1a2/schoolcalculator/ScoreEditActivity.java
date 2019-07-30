package com.k1a2.schoolcalculator;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.k1a2.schoolcalculator.fragment.Grade1Fragment;
import com.k1a2.schoolcalculator.fragment.Grade2Fragment;
import com.k1a2.schoolcalculator.fragment.Grade3Fragment;

public class ScoreEditActivity extends AppCompatActivity {

    private Fragment fragment_grade1 = null;
    private Fragment fragment_grade2 = null;
    private Fragment fragment_grade3 = null;
    private TabLayout tabLayout = null;
    private ViewPager viewPager = null;

    private TabPagerAdapter tabPagerAdapter = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_main);

        tabLayout = findViewById(R.id.score_tablayout);
        viewPager = findViewById(R.id.score_viwewpager);

        TabLayout.Tab tab1 = tabLayout.newTab();
        tab1.setText("1학년");
        tabLayout.addTab(tab1);
        TabLayout.Tab tab2 = tabLayout.newTab();
        tab2.setText("2학년");
        tabLayout.addTab(tab2);
        TabLayout.Tab tab3 = tabLayout.newTab();
        tab3.setText("3학년");
        tabLayout.addTab(tab3);

        setPageAdapter();
    }

    private void setPageAdapter() {
        tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabPagerAdapter);
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
//
//        @Override
//        public void getItem(Int): Fragment? {
//                when (position) {
//                0 -> {
//            if (receieveFragment != null) return receieveFragment else {
//                receieveFragment = RecieveFragment()
//                return  receieveFragment
//            }
//        }
//
//        1 -> {
//            if (sendFragment != null) return sendFragment else {
//                sendFragment = SendFragment()
//                return  sendFragment
//            }
//        }
//
//                else -> return null
//            }
//        }
//
//        override fun getCount(): Int {
//            return 2
//        }
    }
}
