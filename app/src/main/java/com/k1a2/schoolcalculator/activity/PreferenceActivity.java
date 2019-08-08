package com.k1a2.schoolcalculator.activity;

import android.os.Bundle;

import com.k1a2.schoolcalculator.R;

/**설정 액티비티
 * 수정 금지**/

public class PreferenceActivity extends android.preference.PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.activity_preference);
    }
}
