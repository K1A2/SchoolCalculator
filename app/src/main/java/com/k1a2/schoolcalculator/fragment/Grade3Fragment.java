package com.k1a2.schoolcalculator.fragment;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.k1a2.schoolcalculator.R;
import com.k1a2.schoolcalculator.sharedpreference.PreferenceKey;
import com.k1a2.schoolcalculator.view.CardGradeView;

/**3학년 성적 입력 프래그먼트
 * scoreeditactivity에 기생**/

public class Grade3Fragment extends Fragment {

    private View root = null;
    private CheckBox check_include = null;
    private CardGradeView card_32 = null;

    private SharedPreferences preferences = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_grade3, container, false);

        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        check_include = root.findViewById(R.id.grade3_check_isinclude);
        card_32 = root.findViewById(R.id.grade3_card_32);

        final boolean i = preferences.getBoolean(PreferenceKey.KEY_BOOL_ISINCLUDE_GRADE3, false);
        check_include.setChecked(i);
        if (i) {
            card_32.setClickable(true);
            card_32.setVisibility(View.VISIBLE);
            card_32.setEnabled(true);
        } else {
            card_32.setClickable(false);
            card_32.setVisibility(View.GONE);
            card_32.setEnabled(false);
        }

        check_include.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                preferences.edit().putBoolean(PreferenceKey.KEY_BOOL_ISINCLUDE_GRADE3, check_include.isChecked()).commit();
                if (check_include.isChecked()) {
                    card_32.setClickable(true);
                    card_32.setVisibility(View.VISIBLE);
                    card_32.setEnabled(true);
                } else {
                    card_32.setClickable(false);
                    card_32.setVisibility(View.GONE);
                    card_32.setEnabled(false);
                }
            }
        });

        return root;
    }
}
