package com.k1a2.schoolcalculator.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

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
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.k1a2.schoolcalculator.BillingKey;
import com.k1a2.schoolcalculator.R;
import com.k1a2.schoolcalculator.activity.TestInputActivity;
import com.k1a2.schoolcalculator.database.DatabaseKey;
import com.k1a2.schoolcalculator.database.TestScoreDatabaseHelper;
import com.k1a2.schoolcalculator.sharedpreference.AppStorage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TestFragment extends Fragment {

    private View root = null;
    private Spinner spinner_type = null;
    private Button button_input = null;
    private LineChart chart = null;
    private InterstitialAd mInterstitialAd = null;
    private AdView adView = null;
    private CheckBox check_ko = null, check_ma = null, check_en = null, check_hi = null, check_tam1 = null, check_tam2 = null, check_lan = null;

    private TestScoreDatabaseHelper testScoreDatabaseHelper = null;
    private BillingProcessor bp = null;
    private AppStorage storage;

    public TestFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.activity_test, container, false);

        testScoreDatabaseHelper = new TestScoreDatabaseHelper(getContext(), DatabaseKey.KEY_DB_NAME_TEST, null, 1);

        spinner_type = root.findViewById(R.id.test_spinner);
        button_input = root.findViewById(R.id.test_button_add);
        chart = root.findViewById(R.id.test_chart_analyze);

        check_ko = root.findViewById(R.id.test_check_ko);
        check_ma = root.findViewById(R.id.test_check_ma);
        check_en = root.findViewById(R.id.test_check_en);
        check_hi = root.findViewById(R.id.test_check_hi);
        check_tam1 = root.findViewById(R.id.test_check_tam1);
        check_tam2 = root.findViewById(R.id.test_check_tam2);
        check_lan = root.findViewById(R.id.test_check_lan);

        check_ko.setOnCheckedChangeListener(checkedChangeListener);
        check_ma.setOnCheckedChangeListener(checkedChangeListener);
        check_en.setOnCheckedChangeListener(checkedChangeListener);
        check_hi.setOnCheckedChangeListener(checkedChangeListener);
        check_tam1.setOnCheckedChangeListener(checkedChangeListener);
        check_tam2.setOnCheckedChangeListener(checkedChangeListener);
        check_lan.setOnCheckedChangeListener(checkedChangeListener);

        ArrayAdapter typeAdapter = ArrayAdapter.createFromResource(getContext(), R.array.testTypes, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_type.setAdapter(typeAdapter);

        spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_type.setSelection(0);

        button_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd != null) {
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    } else {
                        startActivity(new Intent(getContext(), TestInputActivity.class));
                    }
                } else {
                    startActivity(new Intent(getContext(), TestInputActivity.class));
                }
            }
        });
        loadAd();

        return root;
    }

    CheckBox.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            setList();
        }
    };

    private void setList() {
        int type = spinner_type.getSelectedItemPosition();
        boolean[] k = new boolean[] {check_ko.isChecked(), check_ma.isChecked(), check_en.isChecked(), check_hi.isChecked(),
                check_tam1.isChecked(), check_tam2.isChecked(), check_lan.isChecked()};
        ArrayList<Integer> subjects = new ArrayList<>();
        for (int i = 0;i < k.length;i++) {
            if (k[i]) {
                subjects.add(i + 1);
            }
        }
        String[][] r = testScoreDatabaseHelper.getListDatas(type, subjects);
        Log.d("List result", r[0][0]);

        try {
            JSONObject jsonObject = new JSONObject(r[0][0]);

            chart.setDragEnabled(true);
            chart.setScaleEnabled(false);

            YAxis leftAxis = chart.getAxisLeft();//차트의 왼쪽선 가져옴
            switch (type) {
                case 0:// "origin"; 원점수
                    leftAxis.removeAllLimitLines();//제한선 제거
                    leftAxis.setAxisMaximum(100.05f);//y의 최고값 정의 (9.5)
                    leftAxis.setAxisMinimum(-1f);//y의 최솟값 정의 (0)
                    leftAxis.enableGridDashedLine(10f, 10f, 0);//이건 나도 뭔지 몰라
                    leftAxis.setDrawLimitLinesBehindData(true);//몰라
                    leftAxis.setInverted(false);
                    leftAxis.setDrawAxisLine(false);//왼쪽선 그리기 비활성화
                    break;
                case 1://"grade"; 표준점수
                    leftAxis.removeAllLimitLines();//제한선 제거
                    leftAxis.setAxisMaximum(200f);//y의 최고값 정의 (9.5)
                    leftAxis.setAxisMinimum(-1f);//y의 최솟값 정의 (0)
                    leftAxis.enableGridDashedLine(10f, 10f, 0);//이건 나도 뭔지 몰라
                    leftAxis.setDrawLimitLinesBehindData(true);//몰라
                    leftAxis.setInverted(false);
                    leftAxis.setDrawAxisLine(false);//왼쪽선 그리기 비활성화
                    break;
                case 2://"position"; 등급
                    leftAxis.removeAllLimitLines();//제한선 제거
                    leftAxis.setAxisMaximum(9.5f);//y의 최고값 정의 (9.5)
                    leftAxis.setAxisMinimum(-1f);//y의 최솟값 정의 (0)
                    leftAxis.enableGridDashedLine(10f, 10f, 0);//이건 나도 뭔지 몰라
                    leftAxis.setDrawLimitLinesBehindData(true);//몰라
                    leftAxis.setInverted(true);//역순으로 배치 활성화
                    leftAxis.setDrawAxisLine(false);//왼쪽선 그리기 비활성화
                    break;
                case 3://"percent"; 백분위
                    leftAxis.removeAllLimitLines();//제한선 제거
                    leftAxis.setAxisMaximum(100.05f);//y의 최고값 정의 (9.5)
                    leftAxis.setAxisMinimum(-1f);//y의 최솟값 정의 (0)
                    leftAxis.enableGridDashedLine(10f, 10f, 0);//이건 나도 뭔지 몰라
                    leftAxis.setDrawLimitLinesBehindData(true);//몰라
                    leftAxis.setInverted(false);
                    leftAxis.setDrawAxisLine(false);//왼쪽선 그리기 비활성화
                    break;
            }

            String[] names = r[1];

            int[] colors = new int[] {ContextCompat.getColor(getContext(), R.color.colorChartLine),
                    ContextCompat.getColor(getContext(), R.color.colorChartLine2), ContextCompat.getColor(getContext(), R.color.colorChartLine3),
                    ContextCompat.getColor(getContext(), R.color.colorChartLine4), ContextCompat.getColor(getContext(), R.color.colorChartLine5),
                    ContextCompat.getColor(getContext(), R.color.colorChartLine6), ContextCompat.getColor(getContext(), R.color.colorChartLine7)};

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();//차트에 표시되는 데이터셋 모임 선언
            for (int a = 0;a < subjects.size();a++) {
                final ArrayList<Entry> yvalue = new ArrayList<>();//각 학기마다 점수를 담을 arraylist 선언
                for (int i = 0;i < names.length;i++) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject(names[i]);
                    if (type == 3) {
                        Float value = Float.valueOf(jsonObject1.getString(String.valueOf(subjects.get(a))));
                        System.out.println(value);
                        if (value == -1) {
                            yvalue.add(new Entry(i, 0));
                        } else {
                            yvalue.add(new Entry(i, value));
                        }
                    } else if (type == 2) {
                        int value = (jsonObject1.getInt(String.valueOf(subjects.get(a))));
                        if (value == -1) {
                            yvalue.add(new Entry(i, 9));
                        } else {
                            yvalue.add(new Entry(i, value));
                        }
                    } else {
                        int value = (jsonObject1.getInt(String.valueOf(subjects.get(a))));
                        if (value == -1) {
                            yvalue.add(new Entry(i, 0));
                        } else {
                            yvalue.add(new Entry(i, value));
                        }
                    }
                }

                String tt = "";
                switch (subjects.get(a)) {
                    case 1:
                        tt = "국어";
                        break;
                    case 2:
                        tt = "수학";
                        break;
                    case 3:
                        tt = "영어";
                        break;
                    case 4:
                        tt = "한국사";
                        break;
                    case 5:
                        tt = "탐구1";
                        break;
                    case 6:
                        tt = "탐구2";
                        break;
                    case 7:
                        tt = "제2외국어";
                        break;
                }

                LineDataSet set1 = new LineDataSet(yvalue, tt);//arraylist를 기준으로 데이터셋 만듬

                set1.setFillAlpha(110);//원 중앙에 빈공간
                set1.setLineWidth(3f);//선 굵기 지정
                set1.setCircleRadius(5f);//원 크기
                set1.setColors(colors[subjects.get(a) - 1]);//선 색깔
                set1.setCircleColors(colors[subjects.get(a) - 1]);//원 선 색깔
                set1.setValueTextSize(0);//원 위에 표시되는 텍스트 크기 지정
                dataSets.add(set1);//데잍셋 모임에 set1추가
            }
            LineData data = new LineData(dataSets);//차트에 들어갈 데이터 완성
            chart.setData(data);//차트에 데이터 연결

            chart.getAxisRight().setEnabled(false);//오른쪽 선은 안보이게 설정

            XAxis xAxis = chart.getXAxis();//x축 가져옴
            xAxis.setGranularity(1f);//몰라
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//x축값 아래에만 표시하게 설정
            xAxis.setDrawGridLinesBehindData(false);//데이터 뒤로 세로선 안보이게 설정
            xAxis.setDrawGridLines(false);//세로선 그리기 비활성화
            xAxis.setAxisMinimum(-0.5f);//x값 최솟값
            xAxis.setAxisMaximum(Float.valueOf(String.valueOf(names.length -1)) + 0.5f);//x값 최댓값
            xAxis.setAxisLineWidth(1f);//x축 굵기
            xAxis.setValueFormatter(new ValueFormatter() {
                @Override
                public String getAxisLabel(float value, AxisBase axis) {//x축 값 지정
                    try {
                        String[] n = names[(int) value].split("_");
                        return n[0] + "년 " + n[1] + "월";
                    } catch (ArrayIndexOutOfBoundsException e) {
                        return "";
                    }
                }
            });
            xAxis.setLabelRotationAngle(-30);

            Description description = new Description();//설명 설정
            description.setText("");

            chart.setDescription(description);//설명 연결
            chart.setHighlightPerDragEnabled(false);//몰라
            chart.setHighlightPerTapEnabled(false);//몰라
            chart.animateY(1800, Easing.EaseOutSine);//애니메이션 적용

            chart.setVisibleXRangeMaximum(8f); // allow 20 values to be displayed at once on the x-axis, not more
            chart.moveViewToX(1);
            chart.invalidate();//차트에 변경사항 있다고 알려줌
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setList();
        Log.d("Resume test", "resume");
    }

    public void loadAd() {
        storage = new AppStorage(getContext());

        bp = new BillingProcessor(getContext(), BillingKey.LINCENCE_KEY, new BillingProcessor.IBillingHandler() {
            @Override
            public void onProductPurchased(String productId, TransactionDetails details) {

            }

            @Override
            public void onPurchaseHistoryRestored() {

            }

            @Override
            public void onBillingError(int errorCode, Throwable error) {

            }

            @Override
            public void onBillingInitialized() {
                storage.setPurchasedRemoveAds(bp.isPurchased(BillingKey.KEY_SKU_NO_ADS));
                if (storage.purchasedRemoveAds()) {
                    root.findViewById(R.id.test_card_6).setVisibility(View.GONE);
                } else {
                    adView = root.findViewById(R.id.ads_test);
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

                    mInterstitialAd = new InterstitialAd(getContext());
                    mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            startActivity(new Intent(getContext(), TestInputActivity.class));
                            mInterstitialAd.loadAd(new AdRequest.Builder().build());
                        }
                    });
                    mInterstitialAd.setAdUnitId("ca-app-pub-1385482690406133/9574070368");
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }
            }
        });
        bp.initialize();
    }
}
