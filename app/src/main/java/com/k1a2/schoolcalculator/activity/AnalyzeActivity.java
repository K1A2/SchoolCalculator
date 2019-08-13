package com.k1a2.schoolcalculator.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
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
import com.k1a2.schoolcalculator.fragment.AnalyzeTypeFragment;
import com.k1a2.schoolcalculator.fragment.AnalyzeGradeFragment;
import com.k1a2.schoolcalculator.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**성적 분석 액티비티**/

public class AnalyzeActivity extends AppCompatActivity implements AnalyzeGradeFragment.OnCaptureViewRequestListener, AnalyzeTypeFragment.OnCaptureViewRequestListener {

    private TabLayout tabLayout = null;//페이지 바꾸는 탭
    private ViewPager viewPager = null;//프레그먼트 바뀌는 부분
    private Toolbar toolbar = null;//툴바
    private View view_indicator = null;//커스텀 인디케이터

    private AnalyzeGradeFragment fragment_analyzeG = null;//학기별 분석 프레그먼트
    private AnalyzeTypeFragment fragment_analyzeT = null;//과목별 분석 프레그먼트

    private int indicatorWidth;//인디케이터 너비
    private TabPagerAdapter tabPagerAdapter = null;//탭 페이지 전환 어댑터

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze);

        tabLayout = findViewById(R.id.score_tablayout);
        viewPager = findViewById(R.id.score_viwewpager);
        view_indicator = findViewById(R.id.indicator);
        toolbar = findViewById(R.id.edit_toolbar);

        toolbar.setTitle("성적 분석");//툴바에 보여질 택스트 설정
        setSupportActionBar(toolbar);//이 액티비티의 툴바 지정
        //툴바에 뒤로가기 버튼 지정
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_arrow_back_white_24);

        //탭 추가
        TabLayout.Tab tab1 = tabLayout.newTab();
        tab1.setText("학기별 분석");
        tabLayout.addTab(tab1);
        TabLayout.Tab tab2 = tabLayout.newTab();
        tab2.setText("과목별 분석");
        tabLayout.addTab(tab2);

        setPageAdapter();//어댑터 설정

        //탭의 움직임에 따라 인디케이터 위치 설정
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

    //페이지 전환 어댑터 설정
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
    public void OnViewRequestGrade(View captureView) {
        if (captureView == null) {
            Toast.makeText(this, "캡쳐에 실패했습니다.", Toast.LENGTH_SHORT).show();
            //null이면 오류난거임
        } else {
            LinearLayout container;
            container = (LinearLayout)findViewById(R.id.fragment_analye_grade_layout);
            final getCaptureDrawble getCaptureDrawble = new getCaptureDrawble(this, container);
            getCaptureDrawble.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    //캡쳐할 뷰가 오는곳2
    @Override
    public void OnViewRequestType(View captureView) {
        if (captureView == null) {
            Toast.makeText(this, "캡쳐에 실패했습니다.", Toast.LENGTH_SHORT).show();
            //null이면 오류난거임
        } else {
            LinearLayout container;
            container = (LinearLayout)findViewById(R.id.fragment_analye_type_layout);
            final getCaptureDrawble getCaptureDrawble = new getCaptureDrawble(this, container);
            getCaptureDrawble.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {//툴바 메뉴 설정
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_analyze, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {//툴바 메뉴 클릭시
        switch (item.getItemId()) {
            case R.id.menu_share: {//공유일때
                int p = tabLayout.getSelectedTabPosition();
                switch (p) {
                    case 0: {
                        if (fragment_analyzeG != null) {
                            fragment_analyzeG.requestView();//학기별 분석 프레그먼트에 캡쳐할 뷰 요청
                        }
                        break;
                    }
                    case 1: {
                        if (fragment_analyzeT != null) {
                            fragment_analyzeT.requestView();//학기별 분석 프레그먼트에 캡쳐할 뷰 요청
                        }
                        break;
                    }
                }
                break;
            }
            case android.R.id.home: {//뒤로가기일때
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
                    if (fragment_analyzeG != null) return fragment_analyzeG; else {
                        fragment_analyzeG = new AnalyzeGradeFragment();
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

    //캡쳐클래스
    private class getCaptureDrawble extends AsyncTask<String, String, Boolean> {

        private ProgressDialog progressDialog = null;
        private Context context = null;
        private View container = null;
        private String adress = "";

        public getCaptureDrawble(Context context, View container) {
            this.context = context;
            this.container = container;
            adress = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/capture.jpeg";

            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("이미지 캡쳐중..");
            progressDialog.setMessage("조금만 기다려 주세요..");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
            container.buildDrawingCache();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            Bitmap captureView1 = getBitmapFromView(container);
            FileOutputStream fos;

            try {
                fos = new FileOutputStream(adress);
                captureView1.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            }
        }

        public Bitmap getBitmapFromView(View v) {
            Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight() , Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            v.draw(c);
            return b;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            progressDialog.dismiss();
            if (aBoolean) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                    Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID, new File(adress));
                    Intent shareintent = new Intent(Intent.ACTION_SEND);

                    shareintent.putExtra(Intent.EXTRA_STREAM, uri);
                    shareintent.setType("image/*");

                    Intent chooser = Intent.createChooser(shareintent, "친구에게 공유하기");
                    startActivity(chooser);

                }else{

                    Uri uri = Uri.fromFile(new File(adress));
                    Intent shareintent = new Intent(Intent.ACTION_SEND);

                    shareintent.putExtra(Intent.EXTRA_STREAM, uri);
                    shareintent.setType("image/*");

                    Intent chooser = Intent.createChooser(shareintent, "친구에게 공유하기");
                    startActivity(chooser);
                }
            } else {
                Toast.makeText(context, "캡쳐에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
