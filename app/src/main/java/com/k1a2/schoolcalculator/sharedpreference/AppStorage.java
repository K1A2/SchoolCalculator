package com.k1a2.schoolcalculator.sharedpreference;

import android.content.Context;
import android.content.SharedPreferences;

import com.k1a2.schoolcalculator.BillingKey;

public class AppStorage {
    private Context context;
    private SharedPreferences pref;
    private String PURCHASED_REMOVE_ADS = BillingKey.KEY_SKU_NO_ADS;

    public AppStorage(Context context) {
        pref = context.getSharedPreferences("app_storage", Context.MODE_PRIVATE);
        this.context = context;
    }

    public boolean purchasedRemoveAds() {
        return pref.getBoolean(PURCHASED_REMOVE_ADS, false);
    }

    public void setPurchasedRemoveAds(boolean flag) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(PURCHASED_REMOVE_ADS, flag);
        editor.apply();
    }
}
