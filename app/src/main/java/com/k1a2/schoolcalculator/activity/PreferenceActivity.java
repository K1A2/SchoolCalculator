package com.k1a2.schoolcalculator.activity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.SwitchPreference;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.k1a2.schoolcalculator.BillingKey;
import com.k1a2.schoolcalculator.R;
import com.k1a2.schoolcalculator.sharedpreference.AppStorage;
import com.k1a2.schoolcalculator.sharedpreference.PreferenceKey;

/**설정 액티비티
 * 수정 금지**/

public class PreferenceActivity extends android.preference.PreferenceActivity implements Preference.OnPreferenceClickListener, BillingProcessor.IBillingHandler {

    private SwitchPreference switchPreference = null;
    private SwitchPreference switch_theme = null;
    private Preference pq = null;
    private Preference pref_pw = null;
    private Preference pref_delete = null;
    private PreferenceCategory pref__category = null;

    private BillingProcessor bp = null;
    private AppStorage storage;

    private FirebaseAuth firebaseAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.activity_preference);

//        pref_pw = (Preference)findPreference("pref_pw_change") ;
//        pref_delete = (Preference)findPreference("pref_user_delete");
//        pref__category = (PreferenceCategory)findPreference("pref_category_account");

//        firebaseAuth = FirebaseAuth.getInstance();
//        if (firebaseAuth.getCurrentUser() == null) {
//            pref__category.setEnabled(false);
//        } else {
//            pref__category.setEnabled(true);
//            pref_pw.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//                @Override
//                public boolean onPreferenceClick(Preference preference) {
//                    Intent intent = new Intent(PreferenceActivity.this, AccountActivity.class);
//                    intent.putExtra(ActivityKey.KEY_ACTIVITY_ACCOUNT_MODE, ActivityKey.KEY_ACTIVITY_ACCOUNT_PASSWORD);
//                    startActivity(intent);
//                    return true;
//                }
//            });
//            pref_delete.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//                @Override
//                public boolean onPreferenceClick(Preference preference) {
//                    Intent intent = new Intent(PreferenceActivity.this, AccountActivity.class);
//                    intent.putExtra(ActivityKey.KEY_ACTIVITY_ACCOUNT_MODE, ActivityKey.KEY_ACTIVITY_ACCOUNT_DELETE);
//                    startActivity(intent);
//                    return true;
//                }
//            });
//        }

        switchPreference = (SwitchPreference) findPreference(PreferenceKey.KEY_BOOL_ISINCLUDE_GRADE3);
        switchPreference.setOnPreferenceClickListener(this);

        storage = new AppStorage(this);

        pq = (Preference) findPreference("qq");
        pq.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (storage.purchasedRemoveAds()) {
                    Toast.makeText(PreferenceActivity.this, "이미 구매하셨습니다", Toast.LENGTH_SHORT).show();
                } else {
                    bp.purchase(PreferenceActivity.this, BillingKey.KEY_SKU_NO_ADS);
                }
                return true;
            }
        });

//        switch_theme = (SwitchPreference) findPreference(PreferenceKey.KEY_BOOL_ISDARK_THEME);
//        switch_theme.setOnPreferenceClickListener(this);

        bp = new BillingProcessor(this, BillingKey.LINCENCE_KEY, this);
        bp.initialize();
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference.getKey() == PreferenceKey.KEY_BOOL_ISINCLUDE_GRADE3) {

        }
//        if (preference.getKey() == PreferenceKey.KEY_BOOL_ISDARK_THEME) {
//
//        }
        return false;
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
// 이 메소드는 구매 '성공'시에만 호출된다.
        if (productId.equals(BillingKey.KEY_SKU_NO_ADS)) {
            // TODO: 구매 해 주셔서 감사합니다! 메세지 보내기
            storage.setPurchasedRemoveAds(bp.isPurchased(BillingKey.KEY_SKU_NO_ADS));

            // * 광고 제거는 1번 구매하면 영구적으로 사용하는 것이므로 consume하지 않지만,
            // 만약 게임 아이템 100개를 주는 것이라면 아래 메소드를 실행시켜 다음번에도 구매할 수 있도록 소비처리를 해줘야한다.
            // bp.consumePurchase(Config.Sku);
        }
    }

    @Override
    public void onPurchaseHistoryRestored() {
        storage.setPurchasedRemoveAds(bp.isPurchased(BillingKey.KEY_SKU_NO_ADS));
    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {

    }

    @Override
    public void onBillingInitialized() {
        storage.setPurchasedRemoveAds(bp.isPurchased(BillingKey.KEY_SKU_NO_ADS));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        if (bp != null) {
            bp.release();
        }
        super.onDestroy();
    }
}
